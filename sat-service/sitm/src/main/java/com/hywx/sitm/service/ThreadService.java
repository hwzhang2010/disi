package com.hywx.sitm.service;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sitm.bo.SitmSave;
import com.hywx.sitm.bo.SitmType;
import com.hywx.sitm.bo.TlePredictionFactory;
import com.hywx.sitm.bo.SitmCmd;
import com.hywx.sitm.bo.SitmGpsFromExternal;
import com.hywx.sitm.bo.gps.AbstractSitmGps;
import com.hywx.sitm.bo.gps.SitmGpsFactory;
import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.bo.type.SitmTypeFactory;
import com.hywx.sitm.config.FileConfig;
import com.hywx.sitm.config.MyConfig;
import com.hywx.sitm.global.GlobalAccess;
import com.hywx.sitm.global.GlobalConstant;
import com.hywx.sitm.global.GlobalQueue;
import com.hywx.sitm.net.SitmSender;
import com.hywx.sitm.orbit.tle.prediction.Sgp4;
import com.hywx.sitm.orbit.tle.prediction.Tle;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.net.SitmReceiver;
import com.hywx.sitm.redis.RedisFind;
import com.hywx.sitm.util.ByteUtil;
import com.hywx.sitm.util.FileUtil;
import com.hywx.sitm.vo.TmRsltFrameVO;

/**
 * 创建多线程任务
 * @author zhang.huawei
 *
 */
@Service
@EnableAsync
//@DependsOn("sitmPreloadService")
public class ThreadService {
   
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private FileConfig fileConfig;
	@Autowired
	private MyConfig myConfig;
    
	/**
	 * UDP组播接收
	 */
	@Async  //标注为异步任务，在执行时会单独开启线程来执行
	public void executeRecv() {
		String multicastFileName = fileConfig.getLocalpathMulticast().concat(File.separator).concat(fileConfig.getFilenameMulticast());
		InetSocketAddress address = readMulticast(multicastFileName);
		if (address == null) {
		    address = new InetSocketAddress("224.1.2.3", 30001);
		}
		String local = readLocal(multicastFileName);
		if (local == null) {
			local = "127.0.0.1";
		}
		System.out.println("******************UDP multicast address(recv): " + address + ", " + local);
		SitmReceiver sitmRecv = new SitmReceiver(address);
		//SitmReceiver sitmRecv = new SitmReceiver(address, local);
		
		while (true) {
			try {
				//UDP组播接收
				sitmRecv.recv();
				
				//挂起1ms, 避免CPU长时间被占用
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 判断提取UDP组播接收的数据
	 */
	@Async
	public void executeJudge() {
		
		
		while (true) {
			try {
	        	if (GlobalQueue.getRecvQueue().isEmpty()) {  //当没有数据的时候
	        		//挂起1ms, 避免CPU长时间占用
					Thread.sleep(1); 
					continue;
	        	}
	        	
	        	byte[] data = GlobalQueue.getRecvQueue().take();
	        	
	        	//得到MID
	        	int mid = ByteUtil.toUShort(data, 1, myConfig.isNet());
	        	//得到接收数据的卫星ID
	    		String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
	    		//卫星ID不在当前分组内
	    		if (!GlobalAccess.isInGroup(satelliteId)) 
	    			continue;
	    		
	        	//得到BID
	        	long bid = ByteUtil.toUInt(data, 11, myConfig.isNet());
	        	//根据BID判断数据类别，并进行处理
	        	judgeAndProcess(mid, bid, data);
	        	
	        	//System.out.println("----------mid, bid: " + String.format("%04X, %08X", mid, bid));
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}

    /**
     * 根据数据来源进行处理
     * @param mid
     * @param bid
     * @param data
     */
	private void judgeAndProcess(int mid, long bid, byte[] data) {
		//排除遥测参数源码
		if (bid == GlobalConstant.BID_TM) 
		    return;
		
		//判断BID
		if (bid == GlobalConstant.BID_GPS) {  //外测提供的GPS数据，以此作为遥测源码发送的驱动。	
		    //processExternalWithGps(mid, data);
		} else if (bid == GlobalConstant.BID_TC) {  //遥控
			processTc(data);
		} else if (bid == GlobalConstant.BID_INJECTION) {  //注入
			processInjection(data);
		} 
		else {
		}
		
	}

	/**
     * 外测驱动(外测提供的GPS)
     * @param mid
     * @param data
     */
	private void processExternalWithGps(int mid, byte[] data) {
		//信息处理标识, 自己发送的固定填写0x7E, 避免UDP组播收到自己发送的后进行处理.
		byte flag = data[19];
		if (flag == 0x7E)
			return;
		if (data.length < GlobalConstant.FRAME_GPS_MIN_LENGTH)
			return;
		
		//得到卫星ID
		String satelliteId = String.format("%04X", mid);
		
		//外测驱动的遥测源码仿真
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {	
			//填充帧头
			byte[] header = Arrays.copyOfRange(data, 0, GlobalConstant.FRAME_HEADER_LENGTH);
			// bid 变更为遥测源码的bid = 0x00009000;
			//header[11] = 0x0;
			//header[12] = (byte) 0x90;
			//header[13] = 0x0;
			//header[14] = 0x0;
			// bid 变更为遥测源码的bid = 0x00009000;
			long bid = GlobalConstant.BID_TM;
			header[11] = (byte) (bid & 0xFF);
			header[12] = (byte) ((bid >> 8) & 0xFF);
			header[13] = (byte) ((bid >> 16) & 0xFF);
			header[14] = (byte) ((bid >> 24) & 0xFF);
			//信息处理标识，固定填写0x7E
			header[19] = 0x7E;
			//有效字节长度，变更为遥测源码的长度：1024
			int length = GlobalConstant.FRAME_TM_LENGTH;
			header[30] = (byte) (length & 0xFF);
			header[31] = (byte) ((length >> 8) & 0xFF);
			
			//GPS数据
			GpsFrame gpsFrame = getGpsFromExternal(data);
			SitmGpsFromExternal sitmGpsFromExternal = new SitmGpsFromExternal(header, gpsFrame);
			
			GlobalAccess.putSitmGpsFromExternal(satelliteId, sitmGpsFromExternal);
			//GlobalAccess.putSitmGpsArrived(satelliteId, true);
		}
		
		
	}

	
	/**
	 * 外测提供的GPS数据
	 * @param data
	 * @return
	 */
	private GpsFrame getGpsFromExternal(byte[] data) {
		//接收的GPS数据
		double sx = ByteUtil.toDouble(data, 32, myConfig.isNet());
		double sy = ByteUtil.toDouble(data, 40, myConfig.isNet());
		double sz = ByteUtil.toDouble(data, 48, myConfig.isNet());
		double vx = ByteUtil.toDouble(data, 56, myConfig.isNet());
		double vy = ByteUtil.toDouble(data, 64, myConfig.isNet());
		double vz = ByteUtil.toDouble(data, 72, myConfig.isNet());
		long timeStamp = ByteUtil.toLong(data, 80, myConfig.isNet());
		
		// 将timestamp转为LocalDateTime
		//Instant instant = Instant.ofEpochMilli(timeStamp);
		//LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timeStamp / 1000, 0, ZoneOffset.ofHours(8));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
		String time = formatter.format(localDateTime);
		GpsFrame frame = new GpsFrame(time, sx, sy, sz, vx, vy, vz);
		
		return frame;
	}
	

	
	/**
	 * 遥控处理
	 * @param data
	 */
	private void processTc(byte[] data) {
		byte flag = data[19];
		if (flag == 0x7E)
			return;
		
		if (data.length < GlobalConstant.FRAME_TC_MIN_LENGTH)
			return;
		
		int id = data[GlobalConstant.FRAME_TC_ID_INDEX] & 0xF;
		String evenValue = String.valueOf(data[GlobalConstant.FRAME_TC_ID_INDEX + 1] << 8 | data[GlobalConstant.FRAME_TC_ID_INDEX]);
		String oddValue = String.format("%s.%d", evenValue, id);
		SitmCmd sitmCmd = new SitmCmd();
		sitmCmd.setCmd(id, evenValue, oddValue);
		
		try {
			//挂起100ms
			Thread.sleep(100);
			
			//得到卫星ID
			String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
			
			//redis中的遥控指令标识
		    String cmdFlagKey = RedisFind.keyBuilder("tm", "cmdflag", satelliteId);
		    redisTemplate.opsForValue().increment(cmdFlagKey, 1);
		    
		    //添加到对应卫星ID的redis中
			String cmdKey = RedisFind.keyBuilder("tm", "cmd", satelliteId);
			redisTemplate.opsForValue().set(cmdKey, sitmCmd);
			
			//updateFrame4Tc(satelliteId, sitmCmd);
			System.out.println("****satelliteId:" + satelliteId);
			
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * 数据注入处理
	 * @param data
	 */
	private void processInjection(byte[] data) {
		byte flag = data[19];
		if (flag == 0x7E)
			return;
		
		try {
			//挂起200ms
			Thread.sleep(200);
			
			//得到卫星ID
			String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
			
			//redis中的遥控指令标识
		    String cmdFlagKey = RedisFind.keyBuilder("tm", "cmdflag", satelliteId);
		    redisTemplate.opsForValue().increment(cmdFlagKey, 2);
		
			//updateFrame4Injection(satelliteId);
			
			
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
	}
	
	
	

	/**
	 * UDP组播发送
	 * 当前做法：把所有的卫星遥测源码仿真加入到一个队列中，只要队列中有数据就进行发送
	 * 后续修改：可以考虑每个卫星一个队列，每隔1秒遍历发送队列，依次进行发送，以此保证时间间隔的精确性
	 * @throws IOException 
	 */
	@Async  //标注为异步任务，在执行时会单独开启线程来执行
	public void executeSend() throws IOException {
		String multicastFileName = fileConfig.getLocalpathMulticast().concat(File.separator).concat(fileConfig.getFilenameMulticast());
		InetSocketAddress address = readMulticast(multicastFileName);
		if (address == null) {
		    address = new InetSocketAddress("224.1.2.3", 30001);
		}
		String local = readLocal(multicastFileName);
		if (local == null) {
			local = "127.0.0.1";
		}
		System.out.println("******************UDP multicast address(send): " + address + ", " + local);
		SitmSender sitmSend = new SitmSender(address.getHostString(), address.getPort());
		//SitmSender sitmSend = new SitmSender(address.getHostString(), address.getPort(), local);
		
		while (true) {
			try {
				//sitmSend.send(new byte[] { (byte)0xEB, (byte)0x90 });
				ByteBuffer buffer = GlobalQueue.getSendQueue().take();
				
				sitmSend.send(buffer.array());
				//挂起1ms, 节省CPU
				//Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * UDP组播配置文件: 组播地址和端口
	 * @param fileName
	 * @return
	 */
	private InetSocketAddress readMulticast(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return null;
		
		JSONObject multicast = JSONObject.parseObject(jsonString);
		String address = multicast.getString("address");
		int port = multicast.getInteger("port");
		
		return new InetSocketAddress(address, port);
	}
	
	/**
	 * UDP组播配置文件: 本地地址(网卡)
	 * @param fileName
	 * @return
	 */
	private String readLocal(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return null;
		
		JSONObject multicast = JSONObject.parseObject(jsonString);
		String local = multicast.getString("local");
		
		return local;
	}
	
	
	
	private void updateFrame4Tc(String satelliteId, SitmCmd cmd) {
		//如果接收到遥控发令，则对遥控指令关联的遥测参数进行更新，以便下次遥测响应
		//String cmdKey = RedisFind.keyBuilder("tm", "cmd", satelliteId);
		//SitmCmd cmd = (SitmCmd) redisTemplate.opsForValue().get(cmdKey);
		
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			//对象深拷贝
			//TmRsltFrameVO copyVO = vo.clone();
			//源码参数ID
			int id = vo.getId();
			//源码参数代号
			//String codeName = vo.getCodeName();
			//源码参数起始波道
			//int bd = vo.getBd();
			//源码参数值
			String value = vo.getValue();
			//源码参数数据类型
			String srcType = vo.getSrcType();
			//源码参数仿真类型
			//String paramType = vo.getParamType();
			//源码参数系数
			//double coefficient = vo.getCoefficient();
			
			//仿真参数
			SitmParam param = null;
			//根据参数ID进行遥控发令关联的处理
			if (id == 1) {  //第0个遥测参数: 遥控指令计数 + 1
				param = SitmTypeFactory.getInstance(srcType)
                                       .getSitmParam(value, "increment", 1);
				vo.setParamType("cmd");
				vo.setCoefficient(1);
				vo.setValue(param.getValue());
				
				redisTemplate.opsForList().set(rawKey, index, vo);
			} else if (id == cmd.getId()) {  //第1 - 14个遥测参数，分 奇数，偶数与遥控指令对应
				param = SitmTypeFactory.getInstance(srcType)
                                       .getSitmParam(cmd.getValue(), "cmd", 1);
				//设置为遥控指令参数
		        vo.setParamType("cmd");
		        vo.setCoefficient(1);
		        vo.setValue(param.getValue());
		        
		        redisTemplate.opsForList().set(rawKey, index, vo);
			} else {
			}
		}
		
	}
	
	
	private void updateFrame4Injection(String satelliteId) {
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			//对象深拷贝
			//TmRsltFrameVO copyVO = vo.clone();
			//源码参数ID
			int id = vo.getId();
			//源码参数代号
			//String codeName = vo.getCodeName();
			//源码参数起始波道
			//int bd = vo.getBd();
			//源码参数值
			String value = vo.getValue();
			//源码参数数据类型
			String srcType = vo.getSrcType();
			//源码参数仿真类型
			//String paramType = vo.getParamType();
			//源码参数系数
			//double coefficient = vo.getCoefficient();
			
			//仿真参数
			SitmParam param = null;
			//根据参数ID进行遥控发令关联的处理
			if (id == 1) {  //第0个遥测参数: 遥控指令计数 + 1
				param = SitmTypeFactory.getInstance(srcType)
                                       .getSitmParam(value, "increment", 1);
				vo.setParamType("cmd");
				vo.setCoefficient(1);
				//更新源码参数的值，并写入Redis
			    vo.setValue(param.getValue());
			    
			    redisTemplate.opsForList().set(rawKey, index, vo);
			}
		}
	}
	
	/**
	 * 根据卫星ID填充卫星遥测源码数据
	 * @param satelliteId
	 * @return
	 */
	private byte[] produceFrameData(String satelliteId) {
//		long timeStamp = 0;
//		// 从redis中取出计算的时间，以时间戳的形式
//		String timeStampKey = RedisFind.keyBuilder("sisl", "datetime", "timestamp", "");
//        if (redisTemplate.hasKey(timeStampKey)) {
//            timeStamp = (long) redisTemplate.opsForValue().get(timeStampKey);
//        }
		
		// GPS的卫星位置和速度
		Date current = new Date();
		long timeStamp = current.getTime();
		Tle tle = TlePredictionFactory.getTle(satelliteId);
		double[][] rv = tle.getRV(current);
		double[][] rvECEF = fromECItoECEF(timeStamp, rv);
		
		GpsFrame gpsFrame = new GpsFrame(LocalDateTime.ofEpochSecond(timeStamp / 1000, 0, ZoneOffset.ofHours(8)));
		gpsFrame.setSx(rvECEF[0][0] * 1000);
		gpsFrame.setSy(rvECEF[0][1] * 1000);
		gpsFrame.setSz(rvECEF[0][2] * 1000);
		gpsFrame.setVx(rvECEF[1][0] * 1000);
		gpsFrame.setVy(rvECEF[1][1] * 1000);
		gpsFrame.setVz(rvECEF[1][2] * 1000);
		
		byte[] data = new byte[GlobalConstant.FRAME_TM_LENGTH];
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			//对象深拷贝
			//TmRsltFrameVO copyVO = vo.clone();
			//源码参数ID
			//int id = vo.getId();
			//源码参数代号
			String codeName = vo.getCodeName();
			//源码参数起始波道
			//int bd = vo.getBd();
			//源码参数值
			String value = vo.getValue();
			//源码参数数据类型
			String srcType = vo.getSrcType();
			//源码参数仿真类型
			String paramType = vo.getParamType();
			//源码参数系数
			double coefficient = vo.getCoefficient();
			
			//仿真参数
			SitmParam param = null;
			if (Arrays.binarySearch(GlobalConstant.GPS_PARAMS, codeName) > -1) {  //GPS数据
				//GPS数据帧
				//GpsFrame gpsFrame = GlobalAccess.getGpsFrame(satelliteId);
				// 控制秒间隔为1
				//if ("GPS_SpeedZ".equals(codeName)) {
				//	// 计数+1
				//	GlobalAccess.incrementGpsFrameCountMap(satelliteId);
				//}			
				
				//得到GPS遥测参数的字节，并写入ByteBuffer
				AbstractSitmGps sitmGps = SitmGpsFactory.getInstance(codeName);
				if (sitmGps != null && gpsFrame != null) {
					param = sitmGps.getGpsParam(satelliteId, value, gpsFrame);
					vo.setParamType("increment");
					vo.setCoefficient(1);
					
				}
			} else {  //非GPS数据
				//得到源码参数的字节，并写入ByteBuffer
				param = SitmTypeFactory.getInstance(srcType)
				                       .getSitmParam(value, paramType, coefficient);
				
			}
			
			if (param != null) {
				//根据波道起始位置和参数字节长度写入源码仿真字节数组
				int start = vo.getBd() - 1;
			    int length = param.getBuffer().length;
			    System.arraycopy(param.getBuffer(), 0, data, start, length);
			    
			    //更新源码参数的值，并写入Redis
			    vo.setValue(param.getValue());
			    
			    redisTemplate.opsForList().set(rawKey, index, vo);
			}
		}
		
		return data;
	}
	
	
	private double[][] fromECItoECEF(long timeStamp, double[][] rv) {
		// 本地时间转UTC
		//long localTimeInMillis = current.getTime();
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        //Date utcDate=new Date(calendar.getTimeInMillis());
		
        // 得到儒略日, 月份从0开始, 24小时制
		double[] jdut1 = Sgp4.jday(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		// 得到恒星时
		double gstime = Sgp4.gstime(jdut1[0] + jdut1[1]);
		
		// ECEF的坐标初值
		double r[] = new double[3];
        double v[] = new double[3];
		
		// 坐标系转换(位置)
		r[0] =  Math.cos(gstime) * rv[0][0] + Math.sin(gstime) * rv[0][1];
		r[1] = -Math.sin(gstime) * rv[0][0] + Math.cos(gstime) * rv[0][1];
		r[2] =  rv[0][2];
	    
	    // 坐标系转换(速度)
		v[0] =  Math.cos(gstime) * rv[1][0] + Math.sin(gstime) * rv[1][1] + GlobalConstant.WZ * r[1];
		v[1] = -Math.sin(gstime) * rv[1][0] + Math.cos(gstime) * rv[1][1] - GlobalConstant.WZ * r[0];
		v[2] =  rv[1][2];
		
		double[][] rvECEF = { r, v };

		return rvECEF;
	}
	
	
	
	
	
	
	
}
