package com.hywx.sisl.service.impl;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;

import com.hywx.sisl.bo.PredictFactory;
import com.hywx.sisl.config.MyConfig;
import com.hywx.sisl.global.GlobalQueue;
import com.hywx.sisl.mapper.GroundStationMapper;
import com.hywx.sisl.mapper.SatelliteMapper;
import com.hywx.sisl.po.GroundStationInfo;
import com.hywx.sisl.po.SatelliteInfo;
import com.hywx.sisl.po.SatelliteTle;
import com.hywx.sisl.redis.RedisFind;
import com.hywx.sisl.service.SatelliteService;
import com.hywx.sisl.util.FrameUtil;
import com.hywx.sisl.vo.OrbitElemVO;
import com.hywx.sisl.vo.SatelliteCircleVO;
import com.hywx.sisl.vo.SatelliteCoverVO;
import com.hywx.sisl.vo.SatellitePositionVO;

@Service("satelliteService")
public class SatelliteServiceImpl implements SatelliteService {
	@Resource
    private SatelliteMapper satelliteMapper;
	@Resource
	private GroundStationMapper groundStationMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private MyConfig myConfig;
	
	@Override
	public List<SatelliteInfo> listSatellite() {
		
		return satelliteMapper.listSatellite();
	}

	@Override
	public List<String> listSatelliteId() {
		
		return satelliteMapper.listSatelliteId();
	}

	@Override
	public SatelliteTle getSatelliteTle(String satelliteId) {
		
		return satelliteMapper.getTle(satelliteId);
	}
	
	@Override
	public void redisSgp4() {
		List<String> satelliteIdList = listSatelliteId();
		//暂时把所有使用的卫星都放入redis缓存，提供给SGP4计算
		String signKey = RedisFind.keyBuilder("satellite", "sign", "");
		// 清除之前的缓存
		long size = redisTemplate.opsForList().size(signKey);
		if (size > 0) {
			for (int i = 0; i < size; i++) 
		        redisTemplate.opsForList().leftPop(signKey);
		}
		// 把使用的卫星放入缓存
		redisTemplate.opsForList().rightPushAll(signKey, satelliteIdList.subList(0, 1).toArray());
		
		// 卫星计算时间
		String timeStampKey = RedisFind.keyBuilder("datetime", "timestamp", "");
		redisTemplate.opsForValue().set(timeStampKey, new Date().getTime());
	}
	
	@Override
	public List<String> listSatelliteIdInRedis() {
		List<String> list = new ArrayList<>();
		
		String signKey = RedisFind.keyBuilder("satellite", "sign", "");
		List<Object> voList = redisTemplate.opsForList().range(signKey, 0, -1);
		for (int i = 0, length = voList.size(); i < length; i++) {
			String satelliteId = (String) voList.get(i);
			list.add(satelliteId);
		}
		
		return list;
	}
	
	@Override
	public void insertSatelliteIdToRedis(JSONArray satelliteIdArray) {
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(satelliteIdArray, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  satelliteIdList = JSONObject.parseArray(jsonString, String.class);
		
		String signKey = RedisFind.keyBuilder("satellite", "sign", "");
		// 清空之前存入的卫星缓存
		long size = redisTemplate.opsForList().size(signKey);
		if (size > 0) {
			for (int i = 0; i < size; i++) 
		        redisTemplate.opsForList().leftPop(signKey);
		}
		// 清空并重新注册
		PredictFactory.getSatelliteMap().clear();
		
		if (satelliteIdList.size() > 0) {
			// 把使用的卫星放入缓存
			redisTemplate.opsForList().rightPushAll(signKey, satelliteIdList.toArray());
			// 重新注册
			for (String satelliteId : satelliteIdList) {
				SatelliteTle tle = getSatelliteTle(satelliteId);
			    PredictFactory.registerSatellite(satelliteId, new TLE(tle.getTle()));
			}
		}
		
	}
	
//	@Override
//	public String updateTimeStamp(Long timeStamp, Double multiplier) {
//		String timeStampKey = RedisFind.keyBuilder("satellite", "timestamp", "");
//		Long lastTimeStamp = (Long) redisTemplate.opsForValue().get(timeStampKey);
//		if (timeStamp <= lastTimeStamp) {
//        	timeStamp = lastTimeStamp + Math.round(multiplier * 1000);
//		}
//		redisTemplate.opsForValue().set(timeStampKey, timeStamp);
//		
//		String multiplierKey = RedisFind.keyBuilder("satellite", "multiplier", "");
//		redisTemplate.opsForValue().set(multiplierKey, multiplier);
//		
//		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
//		
//		return format.format(new Date(timeStamp));
//	}

	@Override
	public SatellitePositionVO getSatellitePosition(String satelliteId) {
		String positionKey = RedisFind.keyBuilder("satellite", "position", satelliteId);
		if (redisTemplate.hasKey(positionKey)) 
			return (SatellitePositionVO) redisTemplate.opsForValue().get(positionKey);
		
		return null;
	}

	@Override
	public List<SatellitePositionVO> listSatellitePosition() {
		List<SatellitePositionVO> list = new ArrayList<>();
		
		String signKey = RedisFind.keyBuilder("satellite", "sign", "");
		List<Object> voList = redisTemplate.opsForList().range(signKey, 0, -1);
		for (int i = 0, length = voList.size(); i < length; i++) {
			String satelliteId = (String) voList.get(i);
			SatellitePositionVO vo = getSatellitePosition(satelliteId);
			list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<SatellitePositionVO> listSatellitePositions(String satelliteId, Long timeStamp) {
		// 计算时间区间长度为24小时，时间间隔为1分钟
		int count = 24 * 60;
		return listSatellitePositionsByTime(satelliteId, timeStamp, count);
	}
	
	@Override
	public List<SatellitePositionVO> listSatelliteTlePositions(String satelliteId, Long timeStamp) {
		// 计算时间区间长度为一个周期，时间间隔为1分钟
		double period = getSatelliteTlePeriod(satelliteId);
		int count = (int) (period / 60.0);
		return listSatellitePositionsByTime(satelliteId, timeStamp, count);
	}
	
	private List<SatellitePositionVO> listSatellitePositionsByTime(String satelliteId, Long timeStamp, int count) {
        List<SatellitePositionVO> list = new ArrayList<>();
		
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 实例化SGP4计算的卫星
		Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		// 计算时间区间的星下点位置
		for (int i = 0; i < count; i++) {
			SatPos satPos = satellite.getPosition(groundStationPosition, new Date(timeStamp + i * 60 * 1000L));
		    SatellitePositionVO vo = new SatellitePositionVO(satelliteId, satPos);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<SatelliteCircleVO> listSatelliteCircles(String satelliteId, Long timeStamp) {
        List<SatelliteCircleVO> list = new ArrayList<>();
		
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 实例化SGP4计算的卫星
		Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		// 计算时间区间长度为24小时，时间间隔为1分钟
		int count = 24 * 60;
		for (int i = 0; i < count; i++) {
			SatPos satPos = satellite.getPosition(groundStationPosition, new Date(timeStamp + i * 60 * 1000L));
		    SatelliteCircleVO vo = new SatelliteCircleVO(satelliteId, satPos);
		    list.add(vo);
		}
		
		return list;
	}

	@Override
	public OrbitElemVO getOrbitElem(String satelliteId) {
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 得到两行根数
		TLE tle = new TLE(satelliteTle.getTle());
		// 每天环绕地球的圈数。这个的倒数就是周期。
		double n = tle.getMeanmo();
		// 轨道半长轴
		// 计算公式: R=（GM·T^2／4π^2）^（1／3）
		// 其中，GM=398.60047×10^12，
		// 代入各常数后计算得知，R=21613.546×T^2/3, T由每天的圈数求得。
		double a = 21613.546 * Math.pow(86400 / n, 2 / 3.0);
		// 偏心率
		double e = tle.getEccn();
		// 轨道倾角
		double i = tle.getIncl();
		// 升交点赤经
		double o = tle.getRaan();
		// 近地点幅角
		double w = tle.getArgper();
		// 平近点角
		double m = tle.getMeanan();
		
		OrbitElemVO vo = new OrbitElemVO(a, e, i, o, w, m);
		
		return vo;
	}

	@Override
	public OrbitElemVO getOrbitElemSend(String satelliteId) {
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 得到两行根数
		TLE tle = new TLE(satelliteTle.getTle());
		// 每天环绕地球的圈数。这个的倒数就是周期。
		double n = tle.getMeanmo();
		// 轨道半长轴
		// 计算公式: R=（GM·T^2／4π^2）^（1／3）
		// 其中，GM=398.60047×10^12，
		// 代入各常数后计算得知，R=21613.546×T^2/3, T由每天的圈数求得。
		double a = 21613.546 * Math.pow(86400 / n, 2 / 3.0);
		// 偏心率
		double e = tle.getEccn();
		// 轨道倾角
		double i = tle.getIncl();
		// 升交点赤经
		double o = tle.getRaan();
		// 近地点幅角
		double w = tle.getArgper();
		// 平近点角
		double m = tle.getMeanan();
		
		
		
		double[] orbitElem = new double[] { a, e, i, o, w, m };
		
		// 从redis中取出可用的地面站ID
        String groundStationSignKey = RedisFind.keyBuilder("groundstation", "sign", "");
        List<Object> groundStationIdList = redisTemplate.opsForList().range(groundStationSignKey, 0, -1);
		for (int j = 0, length = groundStationIdList.size(); j < length; j++) {
			// 地面站ID
			String groundStationId = (String) groundStationIdList.get(j);
			// 生成轨道根数帧
			ByteBuffer buffer = FrameUtil.produceFrameOrbitElem(groundStationId, satelliteId, orbitElem, myConfig.isNet());
			// 加入UDP组播发送队列
			GlobalQueue.getSendQueue().offer(buffer);
		}
		
		OrbitElemVO vo = new OrbitElemVO(a, e, i, o, w, m);
		
		return vo;
	}

	@Override
	public Double getSatelliteTlePeriod(String satelliteId) {
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 得到两行根数
		TLE tle = new TLE(satelliteTle.getTle());
		// 每天环绕地球的圈数。这个的倒数就是周期。
		double n = tle.getMeanmo();
		double t = 86400 / n;
		
		return t;
	}

	@Override
	public Double getSatelliteTleMinPeriod(JSONArray satelliteIdArray) {
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(satelliteIdArray, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  satelliteIdList = JSONObject.parseArray(jsonString, String.class);
		
		List<Double> periodList= new ArrayList<>();
		for (int i = 0; i < satelliteIdList.size(); i++) {
			Double period = getSatelliteTlePeriod(satelliteIdList.get(i));
			periodList.add(period);
		}
		Double minPeriod = Collections.min(periodList);
		
		return minPeriod;
	}

	@Override
	public List<SatelliteCoverVO> listSatelliteCovers(String satelliteId, Long timeStamp, JSONArray groundStationIdArray) {
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(groundStationIdArray, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  groundStationIdList = JSONObject.parseArray(jsonString, String.class);
		
		List<SatelliteCoverVO> list = new ArrayList<>();
		
		// 实例化TLE
		SatelliteTle satelliteTle = getSatelliteTle(satelliteId);
		// 实例化SGP4计算的卫星
		Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
		
		// 计算时间区间长度为24小时，时间间隔为1分钟
		int count = 24 * 60;
		for (int i = 0; i < count; i++) {
			Date date = new Date(timeStamp + i * 60 * 1000L);
			
			StringBuilder groundStations = new StringBuilder();
			if (groundStationIdList != null && !groundStationIdList.isEmpty()) {
				for (int j = 0; j < groundStationIdList.size(); j++) {
					GroundStationInfo info = groundStationMapper.getGroundStationById(groundStationIdList.get(j));
					GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLng(), info.getGroundStationLat(), info.getGroundStationAlt());
					SatPos satPos = satellite.getPosition(groundStationPosition, date);
					if (satPos.isAboveHorizon()) {
						groundStations.append(info.getGroundStationText()).append(",");
					}
				}
			}
			// 使用北京的地理坐标作为地面站进行计算卫星的位置
			GroundStationPosition groundStationPosition4Radius = new GroundStationPosition(116, 39, 0);
			SatPos satPos = satellite.getPosition(groundStationPosition4Radius, date);
			// 卫星的覆盖半径
			double radius = satPos.getRangeCircleRadiusKm();
			
			SatelliteCoverVO vo = new SatelliteCoverVO(date, groundStations.toString(), radius);
			list.add(vo);
		}
		
		return list;
	}

	

	

	

	


	

}
