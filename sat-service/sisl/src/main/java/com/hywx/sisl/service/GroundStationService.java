package com.hywx.sisl.service;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.alibaba.fastjson.JSONArray;
import com.github.amsacode.predict4java.SatPassTime;
import com.hywx.sisl.po.GroundStationInfo;
import com.hywx.sisl.vo.GroundStationFollowVO;
import com.hywx.sisl.vo.GroundStationPassVO;

public interface GroundStationService {
	// 查询所有地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<GroundStationInfo> listGroundStation();
	
	// 查询所有地面站ID
	@Select("SELECT GROUNDSTATIONID FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<String> listGroundStationId();
	
	// 根据地面站ID查询对应的地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId}")
	GroundStationInfo getGroundStationById(String groundStationId);
	
	// 把用于SGP4计算的数据放入redis
	void redisSgp4();
	
	// 从redis中取出使用的地面站ID
	List<String> listGroundStationIdInRedis();
	
	// 把前端使用的地面站ID存入redis
	void insertGroundStationIdToRedis(JSONArray groundStationIdArray);
	
	// 从redis中根据地面站ID取出对应的地面站
	GroundStationInfo getGroundStation(String groundStationId);
	
	// 根据信关站ID和卫星ID预测过境时间
	GroundStationPassVO getNextPass(String groundStationId, String satelliteId, Long timeStamp);
	
	// 根据信关站ID和卫星ID预测一定时间范围内的过境时间
	List<GroundStationPassVO> listNextPasses(String groundStationId, String satelliteId, Long timeStamp, Integer hours);

	// 根据信关站ID和卫星ID预测过境时间内的角度
	List<GroundStationFollowVO> listNextFollows(String groundStationId, String satelliteId, Long timeStamp);
}
