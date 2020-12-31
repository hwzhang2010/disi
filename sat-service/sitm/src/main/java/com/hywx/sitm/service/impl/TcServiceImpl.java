package com.hywx.sitm.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hywx.sitm.config.MyConfig;
import com.hywx.sitm.global.GlobalConstant;
import com.hywx.sitm.mapper.TcMapper;
import com.hywx.sitm.po.InjectionData;
import com.hywx.sitm.po.TcData;
import com.hywx.sitm.service.TcService;
import com.hywx.sitm.util.ByteUtil;

@Service("tcService")
public class TcServiceImpl implements TcService {
	@Resource
	private TcMapper tcMapper;
	
	@Autowired
	private MyConfig myConfig;

	@Override
	public int insertTc(long sendTime, String satelliteId, String stationId, String data) {
		
		return tcMapper.insertTc(sendTime, satelliteId, stationId, data);
	}
	
	@Override
	public int deleteTc(long sendTime) {
		
		return tcMapper.deleteTc(sendTime);
	}

	@Override
	public List<TcData> listTcBySatelliteId(String satelliteId, long startTime, long endTime) {
		
		return tcMapper.listTcBySatelliteId(satelliteId, startTime, endTime);
	}

	@Override
	public List<TcData> listTcBySatelliteIdAndStationId(String satelliteId, String stationId, long startTime, long endTime) {
		
		return tcMapper.listTcBySatelliteIdAndStationId(satelliteId, stationId, startTime, endTime);
	}

	@Override
	public int saveTc(byte[] data) {
		//得到接收数据的卫星ID
		String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
		//得到接收数据的信关站ID
		String stationId = String.format("%08X", ByteUtil.toUInt(data, 7, myConfig.isNet()));
		
		//时间戳
		long sendTime = System.currentTimeMillis();
		//指令数据
		int length = data.length >= 256 ? 256 : data.length;
		byte[] cmdData = Arrays.copyOfRange(data, GlobalConstant.FRAME_HEADER_LENGTH, length);
		
		return tcMapper.insertTc(sendTime, satelliteId, stationId, ByteUtil.toHex(cmdData));
	}

	@Override
	public int deleteTc() {
		//获取当前日期的前一个月
		Calendar calendar = Calendar.getInstance();     
		calendar.add(Calendar.MONTH, -1);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 6 || hour > 22) {
			long timeStamp = calendar.getTime().getTime();
		    return tcMapper.deleteTc(timeStamp);
		}
		
		return 0;
	}

	@Override
	public int insertInjection(long sendTime, String satelliteId, String stationId, String data) {
		
		return tcMapper.insertInjection(sendTime, satelliteId, stationId, data);
	}

	@Override
	public int deleteInjection(long sendTime) {
		
		return tcMapper.deleteInjection(sendTime);
	}

	@Override
	public List<InjectionData> listInjectionBySatelliteId(String satelliteId, long startTime, long endTime) {
		
		return tcMapper.listInjectionBySatelliteId(satelliteId, startTime, endTime);
	}

	@Override
	public List<InjectionData> listInjectionBySatelliteIdAndStationId(String satelliteId, String stationId, long startTime, long endTime) {
		
		return tcMapper.listInjectionBySatelliteIdAndStationId(satelliteId, stationId, startTime, endTime);
	}

	@Override
	public int saveInjection(byte[] data) {
		//得到接收数据的卫星ID
		String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
		//得到接收数据的信关站ID
		String stationId = String.format("%08X", ByteUtil.toUInt(data, 7, myConfig.isNet()));
		
		//时间戳
		long sendTime = System.currentTimeMillis();
		//指令数据
		int length = data.length;
		
		return tcMapper.insertInjection(sendTime, satelliteId, stationId, String.format("%d", length));
	}

	@Override
	public int deleteInjection() {
		//获取当前日期的前一个月
		Calendar calendar = Calendar.getInstance();     
		calendar.add(Calendar.MONTH, -1);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 6 || hour > 22) {
			long timeStamp = calendar.getTime().getTime();
		    return tcMapper.deleteInjection(timeStamp);
		}
		
		return 0;
	}
	
	
	

}
