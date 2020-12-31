package com.hywx.sisl.quartz;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.hywx.sisl.bo.PredictFactory;
import com.hywx.sisl.bo.TlePredictionFactory;
import com.hywx.sisl.config.MyConfig;
import com.hywx.sisl.global.GlobalConstant;
import com.hywx.sisl.global.GlobalQueue;
import com.hywx.sisl.orbit.tle.prediction.Tle;
import com.hywx.sisl.redis.RedisFind;
import com.hywx.sisl.util.ByteUtil;
import com.hywx.sisl.util.TimeUtil;
import com.hywx.sisl.vo.SatellitePositionVO;

public class ScheduledJob4Orbit implements Job {
	// 帧头字节长度
	//private final int FRAME_HEADER_LENGTH = 32;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private MyConfig myConfig;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 格式化输出时间
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
        //System.out.println("************************* localdatetime: " + formatter.format(LocalDateTime.now()));

		long timeStamp = 0;
		// 从redis中取出计算的时间，以时间戳的形式
		String timeStampKey = RedisFind.keyBuilder("datetime", "timestamp", "");
        if (redisTemplate.hasKey(timeStampKey)) {
            timeStamp = (long) redisTemplate.opsForValue().get(timeStampKey);
        }
        
        //java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        //nf.setGroupingUsed(false);
        //System.out.println("-----------------" + nf.format(timeStamp / 1000));
		
        // 从redis中取出可用的地面站ID
        String groundStationSignKey = RedisFind.keyBuilder("groundstation", "sign", "");
        List<Object> groundStationIdList = redisTemplate.opsForList().range(groundStationSignKey, 0, -1);
		for (int i = 0, length = groundStationIdList.size(); i < length; i++) {
			String groundStationId = (String) groundStationIdList.get(i);
			// 根据地面站和时间来确定卫星在该时刻的位置
	        GroundStationPosition position = PredictFactory.getGroundStationPosition(groundStationId);
            // 使用SGP4算法计算卫星的位置
	        if (position != null) {
	        	Date current = new Date(timeStamp);
	        	// 把第0次的计算数据发送给遥测，驱动遥测
			    calculateSatellitePosition(timeStamp, groundStationId, position, current, i == 0);
	        }
		}
        
	}


	private void calculateSatellitePosition(long timeStamp, String groundStationId, GroundStationPosition position, Date current, boolean isFirst) {
		String signSatelliteKey = RedisFind.keyBuilder("satellite", "sign", "");
		List<Object> voList = redisTemplate.opsForList().range(signSatelliteKey, 0, -1);
		for (int i = 0, length = voList.size(); i < length; i++) {
			String satelliteId = (String) voList.get(i);
			
			// 包含外测的测距测速和测角
			Satellite satellite = PredictFactory.getSatellite(satelliteId);
			SatPos satPos = satellite.getPosition(position, current);
			//System.out.println("***************************\n" + satelliteId + "\n" + satPos);

			// 可见范围内
			if (satPos.isAboveHorizon()) {
			    // 把位置信息加入队列，提供给发送队列（用于外测测距测速、外测测角）
			    ByteBuffer range = produceFrameRange(timeStamp, groundStationId, satelliteId, satPos.getRange(), satPos.getRangeRate());
			    ByteBuffer angle = produceFrameAngle(timeStamp, groundStationId, satelliteId, satPos.getAzimuth(), satPos.getElevation());
			    GlobalQueue.getSendQueue().offer(range);
			    GlobalQueue.getSendQueue().offer(angle);
			}
			
			
	        //只把第0个地面站计算更新后的值用于驱动遥测，遥测从redis获取地面站
	        if (isFirst) {
//	        	String positionKey = RedisFind.keyBuilder("satellite", "position", satelliteId);
//		        SatellitePositionVO vo = new SatellitePositionVO(satelliteId, satPos);
//		        vo.setId(satelliteId);
//		        redisTemplate.opsForValue().set(positionKey, vo);
	        	
	        	// GPS的卫星位置和速度
				Tle tle = TlePredictionFactory.getTle(satelliteId);
				double[][] rv = tle.getRV(current);
				//System.out.println("*************************r:" + rv[0][0] * 1000 + ", " + rv[0][1] * 1000 + ", " + rv[0][2] * 1000);
				//System.out.println("*************************v:" + rv[1][0] * 1000 + ", " + rv[1][1] * 1000 + ", " + rv[1][2] * 1000);
				
				// 把位置和速度信息加入队列，提供给发送队列(用于遥测GPS数据)
				ByteBuffer gps = produceFrameGps(timeStamp, groundStationId, satelliteId, rv);
				GlobalQueue.getSendQueue().offer(gps);
	        }
	        
		}
		
	}
	

	private ByteBuffer produceFrameRange(long timeStamp, String groundStationId, String satelliteId, double range, double rangeRate) {
		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_RANGE_LENGTH);
		// 填充帧头
		buffer.put(produceFrameHeader(timeStamp, groundStationId, satelliteId, GlobalConstant.BID_RANGE, GlobalConstant.FRAME_RANGE_LENGTH));
		// 设备状态1
		buffer.put(new byte[] {(byte) 0x3E, (byte) 0x3});
		// 时标1：对应于测距对应的时间
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//目标斜距，单位: m
		buffer.put(ByteUtil.fromUInt2Bytes(new Double(range * 1000).longValue(), myConfig.isNet()));
		// 设备状态2
		buffer.put(new byte[] {(byte) 0x3E, (byte) 0x3});
		// 时标2：速度数据对应的时间
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//目标速度，单位：0.002 m/s
		buffer.put(ByteUtil.fromUInt2Bytes(new Double(rangeRate * 1000 / 0.002).longValue()  , myConfig.isNet()));
		
		return buffer;
	}
	
	private ByteBuffer produceFrameAngle(long timeStamp, String groundStationId, String satelliteId, double azimuth, double elevation) {
		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_ANGLE_LENGTH);
		// 填充帧头
		buffer.put(produceFrameHeader(timeStamp, groundStationId, satelliteId, GlobalConstant.BID_ANGLE, GlobalConstant.FRAME_ANGLE_LENGTH));
		// 设备状态
		buffer.put((byte) 0x3);
		//时标：测角信息采样时刻
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		// 目标方位角
	    buffer.put(ByteUtil.fromUInt2Bytes(new Double(azimuth / Math.PI * 180 / 360.0 * Math.pow(2, 32)).longValue(), myConfig.isNet()));
		// 目标俯仰角
	    buffer.put(ByteUtil.fromUInt2Bytes(new Double(elevation / Math.PI * 180 / 360.0 * Math.pow(2, 32)).longValue(), myConfig.isNet()));
		// 方位动态滞后
		buffer.putInt(0);
		// 俯仰动态滞后
		buffer.putInt(0);
		// 方位光脱靶
		buffer.putInt(0);
		// 俯仰光脱靶
		buffer.putInt(0);
		// 接收机自动增益控制
		buffer.put((byte) 0);
		
		return buffer;
	}
	
	private ByteBuffer produceFrameGps(long timeStamp, String groundStationId, String satelliteId, double[][] rv) {
		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_GPS_LENGTH);
		// 填充帧头
		buffer.put(produceFrameHeader(timeStamp, groundStationId, satelliteId, GlobalConstant.BID_GPS, GlobalConstant.FRAME_GPS_LENGTH));
		// 卫星位置：X, Y, Z方向
		buffer.put(ByteUtil.fromDouble2Bytes(rv[0][0] * 1000, myConfig.isNet()));
		buffer.put(ByteUtil.fromDouble2Bytes(rv[0][1] * 1000, myConfig.isNet()));
		buffer.put(ByteUtil.fromDouble2Bytes(rv[0][2] * 1000, myConfig.isNet()));
		// 卫星速度：X, Y, Z方向
		buffer.put(ByteUtil.fromDouble2Bytes(rv[1][0] * 1000, myConfig.isNet()));
		buffer.put(ByteUtil.fromDouble2Bytes(rv[1][1] * 1000, myConfig.isNet()));
		buffer.put(ByteUtil.fromDouble2Bytes(rv[1][2] * 1000, myConfig.isNet()));
		// 时间戳
		buffer.put(ByteUtil.fromLong2Bytes(timeStamp, myConfig.isNet()));
		
		return buffer;
	}
	
	
	
	/**
	 * 根据地面站ID, 卫星ID, BID和字节长度填充外测数据帧头
	 * @param timeStamp
	 * @param groundStationId
	 * @param satelliteId
	 * @param bid
	 * @param len
	 * @return
	 */
	private byte[] produceFrameHeader(long timeStamp, String groundStationId, String satelliteId, long bid, int len) {
		// 将timestamp转为LocalDateTime
		Instant instant = Instant.ofEpochMilli(timeStamp);
		LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		byte[] data = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//ver
		buffer.put((byte)0x0);
		//mid
		int mid = Integer.parseUnsignedInt(satelliteId, 16);
		buffer.put(ByteUtil.fromUShort2Bytes(mid, myConfig.isNet()));
		//sid
		groundStationId = groundStationId.startsWith("0x") ? groundStationId.substring(2) : groundStationId;
		long sid = Long.parseLong(groundStationId, 16);
		buffer.put(ByteUtil.fromUInt2Bytes(sid, myConfig.isNet()));
		//did
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.DID, myConfig.isNet()));
		//bid
		buffer.put(ByteUtil.fromUInt2Bytes(bid, myConfig.isNet()));
		//no
		buffer.putInt(0);
		//flag
		buffer.put((byte)0);
		//reserve
		buffer.putInt(0);
		//jd
		int jd = (int) TimeUtil.calJD(time);
		buffer.put(ByteUtil.fromUShort2Bytes(jd, myConfig.isNet()));
		//js
		long js = TimeUtil.calJS(time);
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//len
		buffer.put(ByteUtil.fromUShort2Bytes(len, myConfig.isNet()));
		
		return data;
	}
	
	
	
	
	
	
	

}
