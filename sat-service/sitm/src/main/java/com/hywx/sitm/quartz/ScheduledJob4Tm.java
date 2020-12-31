package com.hywx.sitm.quartz;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.hywx.sitm.bo.SitmCmd;
import com.hywx.sitm.bo.SitmGpsFromExternal;
import com.hywx.sitm.bo.gps.AbstractSitmGps;
import com.hywx.sitm.bo.gps.SitmGpsFactory;
import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.bo.type.SitmTypeFactory;
import com.hywx.sitm.config.MyConfig;
import com.hywx.sitm.global.GlobalAccess;
import com.hywx.sitm.global.GlobalConstant;
import com.hywx.sitm.global.GlobalQueue;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.redis.RedisFind;
import com.hywx.sitm.util.ByteUtil;
import com.hywx.sitm.util.TimeUtil;
import com.hywx.sitm.vo.TmRsltFrameVO;


public class ScheduledJob4Tm implements Job {
	//private static final Logger log = LoggerFactory.getLogger(ScheduledJob4Tm.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private MyConfig myConfig;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//格式化输出时间
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
        //System.out.println("*************************TM localdatetime: " + formatter.format(LocalDateTime.now()));
        //System.out.println("*************************TM threadid: " + Thread.currentThread().getId());
        
        Vector<String> satelliteIdVector = GlobalAccess.getGroupSatellites();
		if (satelliteIdVector == null) 
            return;
		
		//自动发送的遥测仿真
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		//外测驱动的遥测仿真
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		for (String satelliteId : satelliteIdVector) {
		
			//如果设置了进行自动发送，则生成遥测源码仿真数据
			if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
				produceAutoFrame(satelliteId);
			}
			
			//如果设置了外测驱动，则生成遥测源码仿真数据
			if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
				produceExternalFrame(satelliteId);
			}
			
			// 遥控指令: 1或者数据注入: 2
			String cmdFlagKey = RedisFind.keyBuilder("tm", "cmdflag", satelliteId);
			if (redisTemplate.hasKey(cmdFlagKey)) {
				Integer cmd = (Integer) redisTemplate.opsForValue().get(cmdFlagKey);
				if (cmd == 1) {
					// 遥控指令
					redisTemplate.opsForValue().decrement(cmdFlagKey, 1);
					// 更新遥控关联的遥测参数
					updateFrame4Tc(satelliteId);
				} else if (cmd == 2) {
					// 数据注入
					redisTemplate.opsForValue().decrement(cmdFlagKey, 2);
					// 更新数据注入关联的遥测参数
					updateFrame4Injection(satelliteId);
				} else {
					redisTemplate.opsForValue().set(cmdFlagKey, 0);
				}
			}
			
		}
     
	}
	

	/**
	 * 根据卫星ID生成自动发送的遥测源码
	 * @param satelliteId 卫星ID
	 */
	private void produceAutoFrame(String satelliteId) {
		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_TM_LENGTH);
		
		//填充帧头
        buffer.put(produceAutoFrameHeader(satelliteId));
        //填充遥测源码(自动发送)
        buffer.put(produceAutoFrameData(satelliteId));
		
		try {
	    	//把生成的遥测源码放入UDP组播发送队列
			GlobalQueue.getSendQueue().put(buffer);
			//设置卫星遥测源码帧发送计数+1
			String countKey = RedisFind.keyBuilder("tm", "count", satelliteId);
			redisTemplate.opsForValue().increment(countKey, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 根据卫星ID填充遥测源码帧头
	 * @param satelliteId
	 * @return
	 */
	private byte[] produceAutoFrameHeader(String satelliteId) {
		byte[] data = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//ver
		buffer.put((byte)0x0);
		//mid
		int mid = Integer.parseUnsignedInt(satelliteId, 16);
		buffer.put(ByteUtil.fromUShort2Bytes(mid, myConfig.isNet()));
		//sid
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.SID, myConfig.isNet()));
		//did
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.DID, myConfig.isNet()));
		//bid
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.BID_TM, myConfig.isNet()));
		//no
		buffer.putInt(0);
		//flag
		buffer.put((byte)0x7E);
		//reserve
		buffer.putInt(0);
		//jd
		int jd = (int) TimeUtil.calJD(LocalDateTime.now());
		buffer.put(ByteUtil.fromUShort2Bytes(jd, myConfig.isNet()));
		//js
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//len
		buffer.put(ByteUtil.fromUShort2Bytes(GlobalConstant.FRAME_TM_LENGTH, myConfig.isNet()));
		
		return data;
	}
	
	
	/**
	 * 根据卫星ID填充卫星遥测源码数据
	 * @param satelliteId
	 * @return
	 */
	private byte[] produceAutoFrameData(String satelliteId) {
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
				GpsFrame gpsFrame = GlobalAccess.getGpsFrame(satelliteId);
				
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
	
	
	/**
	 * 根据卫星ID生成外测驱动的遥测源码
	 * @param satelliteId 卫星ID
	 */
	private void produceExternalFrame(String satelliteId) {

		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_TM_LENGTH);
		SitmGpsFromExternal external = GlobalAccess.getSitmGpsFromExternal(satelliteId);
		
		//填充遥测源码(外测GPS驱动)
		GpsFrame gpsFrame = external.getGpsFrame();
		byte[] data = produceExternalFrameData(satelliteId, gpsFrame);
		
		//发送计数
		String countKey = RedisFind.keyBuilder("tm", "count", satelliteId);
		
		// 从redis中取出可用的地面站ID(外测使用的)
        String groundStationSignKey = RedisFind.keyBuilder("sisl", "groundstation", "sign", "");
        List<Object> groundStationIdList = redisTemplate.opsForList().range(groundStationSignKey, 0, -1);
		for (int i = 0, length = groundStationIdList.size(); i < length; i++) {
			String groundStationId = (String) groundStationIdList.get(i);
			// 地面站ID
			long sid = Long.parseLong(groundStationId, 16);
			// 将buffer清空，并把索引复位为0
			buffer.clear();
			byte[] header = external.getHeader();
			header[3] = (byte) (sid & 0xFF);
			header[4] = (byte) ((sid >> 8) & 0xFF);
			header[5] = (byte) ((sid >> 16) & 0xFF);
			header[6] = (byte) ((sid >> 24) & 0xFF);
			
			//填充帧头
			buffer.put(header);
			//填充遥测源码(外测GPS驱动)
			buffer.put(data);
	        
			try {
		    	//把生成的遥测源码放入UDP组播发送队列
				GlobalQueue.getSendQueue().put(buffer);
				//设置卫星遥测源码帧发送计数+1
				redisTemplate.opsForValue().increment(countKey, 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 	
		}
		
	}


	/**
	 * 根据卫星ID填充卫星遥测源码数据(外测驱动)
	 * @param satelliteId
	 * @param gpsFrame
	 * @return
	 */
	private byte[] produceExternalFrameData(String satelliteId, GpsFrame gpsFrame) {
		byte[] data = new byte[GlobalConstant.FRAME_TM_LENGTH];
		
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			//对象深拷贝
			//TmRsltFrameVO copyVO = vo.clone();
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
	
	
	private void updateFrame4Tc(String satelliteId) {
		//如果接收到遥控发令，则对遥控指令关联的遥测参数进行更新，以便下次遥测响应
		String cmdKey = RedisFind.keyBuilder("tm", "cmd", satelliteId);
		SitmCmd cmd = (SitmCmd) redisTemplate.opsForValue().get(cmdKey);
		
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
	
	
	
	

}
