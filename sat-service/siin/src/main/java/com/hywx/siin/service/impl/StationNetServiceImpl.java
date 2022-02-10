package com.hywx.siin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hywx.siin.mapper.StationNetMapper;
import com.hywx.siin.po.StationNetReply;
import com.hywx.siin.po.StationNetState;
import com.hywx.siin.redis.RedisFind;
import com.hywx.siin.service.StationNetService;

@Service("stationNetService")
public class StationNetServiceImpl implements StationNetService {
	@Resource
	private StationNetMapper stationNetMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

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

	@Override
	public void sendStationNetState() {
		String stateSignKey = RedisFind.keyBuilder("sisn", "state", "sign", "");
		redisTemplate.opsForValue().set(stateSignKey, 1);
	}

	

}
