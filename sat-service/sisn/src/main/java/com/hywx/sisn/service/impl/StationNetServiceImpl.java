package com.hywx.sisn.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hywx.sisn.mapper.StationNetMapper;
import com.hywx.sisn.po.StationNetReply;
import com.hywx.sisn.po.StationNetState;
import com.hywx.sisn.service.StationNetService;

@Service("stationNetService")
public class StationNetServiceImpl implements StationNetService {
	@Resource
	private StationNetMapper stationNetMapper;

	@Override
	public int updateReply(String reply) {
		
		return stationNetMapper.updateReply(reply);
	}
	
	@Override
	public String getStationNetReplyById() {
		
		return stationNetMapper.getStationNetReplyById();
	}

	@Override
	public StationNetReply getStationNetReply(String reply) {
		
		return stationNetMapper.getStationNetReply(reply);
	}
	

	@Override
	public int updateState(String groundStationName, Integer subsystemId, String equipmentId, String warning, Integer healthLevel) {
		
		return stationNetMapper.updateState(groundStationName, subsystemId, equipmentId, warning, healthLevel);
	}

	@Override
	public StationNetState getStationNetState() {
		
		return stationNetMapper.getStationNetState();
	}

	

	


}
