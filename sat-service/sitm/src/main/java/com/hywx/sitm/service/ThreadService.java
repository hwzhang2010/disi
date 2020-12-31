package com.hywx.sitm.service;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sitm.bo.SitmSave;
import com.hywx.sitm.bo.SitmType;
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
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.net.SitmReceiver;
import com.hywx.sitm.redis.RedisFind;
import com.hywx.sitm.util.ByteUtil;
import com.hywx.sitm.util.FileUtil;

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
		    processExternalWithGps(mid, data);
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
		Instant instant = Instant.ofEpochMilli(timeStamp);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
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
		
			//添加到存储队列，提供给数据存储使用
			//GlobalQueue.getSaveQueue().put(new SitmSave(SitmType.CMD, data));
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
		
			//添加到存储队列，提供给数据存储使用
			//GlobalQueue.getSaveQueue().put(new SitmSave(SitmType.INJECTION, data));
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
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
//	/**
//	 * 监测外测驱动
//	 */
//	@Async  //标注为异步任务，在执行时会单独开启线程来执行
//	public void executeDrive() {
//		while (true) {
//			
//			try {
//				if (GlobalQueue.getDriveQueue().isEmpty()) {  //当没有外测数据的时候
//					//当前时刻的时间戳，秒
//					long now = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
//					String driveKey = RedisFind.keyBuilder("tm", "sign", "drive");
//					if (redisTemplate.hasKey(driveKey)) {
//						//得到外测驱动的所有卫星ID
//						Set<Object> satelliteSet = redisTemplate.opsForHash().keys(driveKey);
//						for (Object obj : satelliteSet) {
//							if (obj instanceof String) {
//								String satelliteId = (String) obj;
//								long last = (long) redisTemplate.opsForHash().get(driveKey, satelliteId);
//								//超过过期时间，删除卫星ID
//								if (now - last > EXPIRE_TIME)
//									redisTemplate.opsForHash().delete(driveKey, satelliteId);
//							}
//						}
//					}
//					
//					//挂起1ms，节省CPU
//					Thread.sleep(1);
//				} else {  //接收到了外测数据的时候
//					//提取组播接收队列的数据
//					byte[] data = GlobalQueue.getDriveQueue().take(); 
//		
//					//得到接收数据的卫星ID
//					String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
//					//外测驱动的卫星ID
//					String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
//					if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
//						//得到接收数据的PDXP帧头
//						byte[] header = Arrays.copyOf(data, FRAME_HEADER_LENGTH); 
//						
//						//把外测驱动的数据写入Redis, 并设置过期时间10s
//						String externalKey = RedisFind.keyBuilder("tm", "external", satelliteId);
//						redisTemplate.opsForValue().set(externalKey, header, EXPIRE_TIME, TimeUnit.SECONDS);;
//						
//						//把外测驱动的数据卫星ID写入Redis
//						String driveKey = RedisFind.keyBuilder("tm", "sign", "drive");
//						//当前时刻的时间戳，秒
//						long stamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
//						//把卫星ID和当前时间戳作为HashMap写入Redis
//						redisTemplate.opsForHash().put(driveKey, satelliteId, stamp);
//					}
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
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
	
	
	
}
