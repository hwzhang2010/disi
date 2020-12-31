package com.hywx.sisl.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.PassPredictor;
import com.github.amsacode.predict4java.SatNotFoundException;
import com.github.amsacode.predict4java.SatPassTime;
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.TLE;
import com.hywx.sisl.bo.PredictFactory;
import com.hywx.sisl.mapper.GroundStationMapper;
import com.hywx.sisl.mapper.SatelliteMapper;
import com.hywx.sisl.po.GroundStationInfo;
import com.hywx.sisl.po.SatelliteTle;
import com.hywx.sisl.redis.RedisFind;
import com.hywx.sisl.service.GroundStationService;
import com.hywx.sisl.vo.GroundStationFollowVO;
import com.hywx.sisl.vo.GroundStationPassVO;

@Service("groundStationService")
public class GroundStationServiceImpl implements GroundStationService {
	@Resource
    private SatelliteMapper satelliteMapper;
	@Resource
	private GroundStationMapper groundStationMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<GroundStationInfo> listGroundStation() {
		
		return groundStationMapper.listGoundStation();
	}
	
	@Override
	public List<String> listGroundStationId() {
		
		return groundStationMapper.listGroundStationId();
	}
	
	@Override
	public GroundStationInfo getGroundStationById(String groundStationId) {
		
		return groundStationMapper.getGroundStationById(groundStationId);
	}

	@Override
	public void redisSgp4() {
		List<GroundStationInfo> groundStationList = listGroundStation();
		List<String> groundStationIdList = listGroundStationId();
		
		//把所有地面站都放入redis缓存，提供给SGP4计算
		String positionKey = RedisFind.keyBuilder("groundstation", "position", "");
		// 清除之前的地面站缓存
		long sizeOfPositionKey = redisTemplate.opsForList().size(positionKey);
		if (sizeOfPositionKey > 0) {
			for (int i = 0; i < sizeOfPositionKey; i++) 
		        redisTemplate.opsForList().leftPop(positionKey);
		}
		// 把所有地面站放入缓存
		redisTemplate.opsForList().rightPushAll(positionKey, groundStationList.toArray());
		
		
		//暂时把所有使用的地面站都放入redis缓存，提供给SGP4计算
		String signKey = RedisFind.keyBuilder("groundstation", "sign", "");
		// 清除之前的缓存
		long sizeOfSignKey = redisTemplate.opsForList().size(signKey);
		if (sizeOfSignKey > 0) {
			for (int i = 0; i < sizeOfSignKey; i++) 
		        redisTemplate.opsForList().leftPop(signKey);
		}
		// 把使用的地面站放入缓存
		//redisTemplate.opsForList().rightPushAll(signKey, groundStationIdList.subList(0, 1).toArray());
		//redisTemplate.opsForList().rightPushAll(signKey, groundStationIdList.toArray());
		// 北京站和基律纳站
		redisTemplate.opsForList().rightPush(signKey, "01110000");
		redisTemplate.opsForList().rightPush(signKey, "001E0000");
	}

	@Override
	public List<String> listGroundStationIdInRedis() {
		List<String> list = new ArrayList<>();
		
		String signKey = RedisFind.keyBuilder("groundstation", "sign", "");
		List<Object> voList = redisTemplate.opsForList().range(signKey, 0, -1);
		for (int i = 0, length = voList.size(); i < length; i++) {
			String groundStationId = (String) voList.get(i);
			list.add(groundStationId);
		}
		
		return list;
	}
	
	@Override
	public void insertGroundStationIdToRedis(JSONArray groundStationIdArray) {
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(groundStationIdArray, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  groundStationIdList = JSONObject.parseArray(jsonString, String.class);
		
		String signKey = RedisFind.keyBuilder("groundstation", "sign", "");
		// 清空之前存入的地面站缓存
		long size = redisTemplate.opsForList().size(signKey);
		if (size > 0) {
			for (int i = 0; i < size; i++) 
		        redisTemplate.opsForList().leftPop(signKey);
		}
		// 清空并重新注册
		PredictFactory.getGroundStationMap().clear();
		if (groundStationIdList.size() > 0) {
			// 把使用的地面站放入缓存
			redisTemplate.opsForList().rightPushAll(signKey, groundStationIdList.toArray());
			// 重新注册
			for (String groundStationId : groundStationIdList) {
				GroundStationInfo info = getGroundStation(groundStationId);
				GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLat(), info.getGroundStationLng(), info.getGroundStationAlt());
			    PredictFactory.registerGroundStation(groundStationId, groundStationPosition);
			}
		}
		
	}

	@Override
	public GroundStationInfo getGroundStation(String groundStationId) {
		String positionKey = RedisFind.keyBuilder("groundstation", "position", "");
		List<Object> voList = redisTemplate.opsForList().range(positionKey, 0, -1);
		for (int i = 0, length = voList.size(); i < length; i++) {
			GroundStationInfo groundStation = (GroundStationInfo) voList.get(i);
			if (groundStationId.equals(groundStation.getGroundStationId()))
				return groundStation;
		}
		
		return null;
	}

	@Override
	public GroundStationPassVO getNextPass(String groundStationId, String satelliteId, Long timeStamp) {
		// 得到信关站信息
		GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
		// 实例化GroundStationPosition
		GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLat(), groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationAlt());
		
		// 实例化TLE
		SatelliteTle satelliteTle = satelliteMapper.getTle(satelliteId);
		TLE tle = new TLE(satelliteTle.getTle());
		
		// 时间
		Date date = new Date(timeStamp);
		
		try {
			// 实例化PassPredictor
			PassPredictor predictor = new PassPredictor(tle, groundStationPosition);
			// 计算过境时间
			SatPassTime satPassTime = predictor.nextSatPass(date);
			
			return new GroundStationPassVO(satPassTime);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SatNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public List<GroundStationPassVO> listNextPasses(String groundStationId, String satelliteId, Long timeStamp, Integer hours) {
		List<GroundStationPassVO> list = new ArrayList<>();
		
		// 得到信关站信息
		GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
		// 实例化GroundStationPosition
		GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLat(), groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationAlt());
		
		// 实例化TLE
		SatelliteTle satelliteTle = satelliteMapper.getTle(satelliteId);
		TLE tle = new TLE(satelliteTle.getTle());
		
		// 时间
		Date date = new Date(timeStamp);
		
		try {
			// 实例化PassPredictor
			PassPredictor predictor = new PassPredictor(tle, groundStationPosition);
			// 计算过境时间
			List<SatPassTime> satPassTimeList = predictor.getPasses(date, hours, false);
			for (int i = 0; i < satPassTimeList.size(); i++) {
				list.add(new GroundStationPassVO(satPassTimeList.get(i)));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SatNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return list;
	}

	@Override
	public List<GroundStationFollowVO> listNextFollows(String groundStationId, String satelliteId, Long timeStamp) {
		List<GroundStationFollowVO> list = new ArrayList<>();
		
		// 得到信关站信息
		GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
		// 实例化GroundStationPosition
		GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLat(), groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationAlt());
		
		// 实例化TLE
		SatelliteTle satelliteTle = satelliteMapper.getTle(satelliteId);
		TLE tle = new TLE(satelliteTle.getTle());
		
		// 时间
		Date date = new Date(timeStamp);
		
		try {
			// 实例化PassPredictor
			PassPredictor predictor = new PassPredictor(tle, groundStationPosition);
			// 计算过境时间
			SatPassTime satPassTime = predictor.nextSatPass(date);
			// 过境开始时间和结束时间
			long start = satPassTime.getStartTime().getTime();
			long end = satPassTime.getEndTime().getTime();
			// 过境持续时间, 单位:ms
			long duration = end - start;
			for (int i = 0; i < duration; i += 1000) {
				Date passTime = new Date(start + i);
				SatPos satPos = predictor.getSatPos(passTime);
				
				GroundStationFollowVO vo = new GroundStationFollowVO(passTime, satPos.getAzimuth(), satPos.getElevation()); 
				list.add(vo);
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SatNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	

	

	

}
