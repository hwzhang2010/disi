package com.hywx.sisn.quartz;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.hywx.sisn.redis.RedisFind;
import com.hywx.sisn.service.StationNetService;
import com.hywx.sisn.util.ByteUtil;
import com.hywx.sisn.util.StringUtils;
import com.hywx.sisn.util.TimeUtil;
import com.hywx.sisn.vo.FaultVO;
import com.alibaba.fastjson.JSON;
import com.hywx.sisn.bo.state.RunStateInfo;
import com.hywx.sisn.config.MyConfig;
import com.hywx.sisn.global.GlobalConstant;
import com.hywx.sisn.global.GlobalQueue;
import com.hywx.sisn.po.StationNetState;

public class ScheduledJob4Send implements Job {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private MyConfig myConfig;
	@Autowired
	private StationNetService stationNetService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//格式化输出时间
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
        //System.out.println("*************************localdatetime: " + formatter.format(LocalDateTime.now()));
        
        // 要进行故障发送的标志
        String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
        if (redisTemplate.hasKey(faultSignKey)) {
        	Integer faultSign = (Integer) redisTemplate.opsForValue().get(faultSignKey);
            if (faultSign == 1) {
            	redisTemplate.opsForValue().set(faultSignKey, 0);
            	String faultKey = RedisFind.keyBuilder("sisn", "fault", "send", "");
            	FaultVO vo = (FaultVO) redisTemplate.opsForValue().get(faultKey);
            	produceFaultFrame(vo);
            }
        }
        
        // 要进行站网状态发送的标志
        String stateSignKey = RedisFind.keyBuilder("sisn", "state", "sign", "");
        if (redisTemplate.hasKey(stateSignKey)) {
        	Integer stateSign = (Integer) redisTemplate.opsForValue().get(stateSignKey);
            if (stateSign == 1) {
            	redisTemplate.opsForValue().set(stateSignKey, 0);
            	StationNetState state = stationNetService.getStationNetState();
            	
            	System.out.println("************" + state);
            	produceStaionNetStateFrame(state);
            }
        }
        
        
	}



	/**
	 * 根据前端设置内容生成故障数据
	 * @param vo
	 */
	private void produceFaultFrame(FaultVO vo) {
		String satelliteId = vo.getSatelliteId();
		String groundStationIdString = vo.getGroundStationId();
		long groundStationId = Long.parseLong(groundStationIdString, 16);
		Integer id = vo.getDeviceId();
		Integer mainId = vo.getMainId();
		Integer subId = vo.getSubId();
		Integer level = vo.getLevel();
		
        ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_FAULT_LENGTH);
		
		// 填充帧头
        buffer.put(produceFaultFrameHeader(satelliteId, groundStationId));
        // 填充故障数据
        buffer.put(produceFaultFrameData(id, mainId, subId, level));
        try {
	    	//把生成的遥测源码放入UDP组播发送队列
			GlobalQueue.getSendQueue().put(buffer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 根据卫星ID和信关站ID填充故障帧帧头
	 * @param satelliteId
	 * @param groundStationId
	 * @return
	 */
	private byte[] produceFaultFrameHeader(String satelliteId, long groundStationId) {
		byte[] data = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//ver
		buffer.put((byte)0x0);
		//mid
		int mid = Integer.parseUnsignedInt(satelliteId, 16);
		buffer.put(ByteUtil.fromUShort2Bytes(mid, myConfig.isNet()));
		//sid
		buffer.put(ByteUtil.fromUInt2Bytes(groundStationId, myConfig.isNet()));
		//did
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.DID, myConfig.isNet()));
		//bid
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.BID_FAULT, myConfig.isNet()));
		//no
		buffer.putInt(0);
		//flag
		buffer.put((byte)0);
		//reserve
		buffer.putInt(0);
		//jd
		int jd = (int) TimeUtil.calJD(LocalDateTime.now());
		buffer.put(ByteUtil.fromUShort2Bytes(jd, myConfig.isNet()));
		//js
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//len
		buffer.put(ByteUtil.fromUShort2Bytes(GlobalConstant.FRAME_FAULT_LENGTH, myConfig.isNet()));
		
		return data;
	}
	
	/**
	 * 生成故障数据
	 * @param id
	 * @param mainId
	 * @param subId
	 * @param level
	 * @return
	 */
	private byte[] produceFaultFrameData(Integer id, Integer mainId, Integer subId, Integer level) {
		byte[] data = new byte[GlobalConstant.FRAME_FAULT_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		// 故障设备识别码
		buffer.put(ByteUtil.fromUShort2Bytes(id, myConfig.isNet()));
		// 主故障类别
		buffer.put(ByteUtil.fromUShort2Bytes(mainId, myConfig.isNet()));
		// 子故障类别
		buffer.put(ByteUtil.fromUShort2Bytes(subId, myConfig.isNet()));
		// 故障级别
		buffer.put((byte)(level & 0xFF));
		
		return data;
	}
	
	
	/**
	 * 填充站网状态帧帧头
	 * @return
	 */
	private byte[] produceStationNetStateFrameHeader() {
		byte[] data = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//ver
		buffer.put((byte)0x0);
		//mid
		buffer.put(ByteUtil.fromUShort2Bytes(0x0101, myConfig.isNet()));
		//sid
		buffer.put(ByteUtil.fromUInt2Bytes(0x01110000, myConfig.isNet()));
		//did
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.DID, myConfig.isNet()));
		//bid
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.BID_STATIONNET_STATE, myConfig.isNet()));
		//no
		buffer.putInt(0);
		//flag
		buffer.put((byte)0);
		//reserve
		buffer.putInt(0);
		//jd
		int jd = (int) TimeUtil.calJD(LocalDateTime.now());
		buffer.put(ByteUtil.fromUShort2Bytes(jd, myConfig.isNet()));
		//js
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, myConfig.isNet()));
		//len
		buffer.put(ByteUtil.fromUShort2Bytes(0x0, myConfig.isNet()));
		
		return data;
	}
	
	
	/**
	 * 发送站网状态信息
	 * @param state
	 */
	private void produceStaionNetStateFrame(StationNetState state) {
		RunStateInfo info = new RunStateInfo();
		info.setReceiveTime(new Date());
		info.setStationId(state.getGroundStationName());
		info.setSubSystem(state.getSubsystemId());
		info.setEquipId(state.getEquipmentId());
		info.setWarnInfo(state.getWarning());
		info.setHealthLevel(state.getHealthLevel());
		
		// 生成站网状态帧头(有效字节长度为0)
		byte[] header = produceStationNetStateFrameHeader();
		
		String stateJson = JSON.toJSONString(info);
		byte[] data = StringUtils.string2byte(stateJson);
		//有效字节长度
        int length = data.length;
        header[30] = (byte) (length & 0xFF);
        header[31] = (byte) ((length >> 8) & 0xFF);
		
        ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + length);
        // 填充帧头
        buffer.put(header);
        // 填充站网状态数据
        buffer.put(data);
		
        try {
	    	//把生成的遥测源码放入UDP组播发送队列
			GlobalQueue.getSendQueue().put(buffer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
