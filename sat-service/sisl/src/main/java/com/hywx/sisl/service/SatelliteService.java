package com.hywx.sisl.service;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.hywx.sisl.vo.OrbitElemVO;
import com.hywx.sisl.vo.SatelliteCircleVO;
import com.hywx.sisl.vo.SatelliteCoverVO;
import com.hywx.sisl.vo.SatellitePositionVO;
import com.alibaba.fastjson.JSONArray;
import com.hywx.sisl.po.SatelliteInfo;
import com.hywx.sisl.po.SatelliteTle;

public interface SatelliteService {
	// 查询所有卫星描述信息
	@Select("SELECT SATELLITEID, SATELLITENAME, SATELLITETEXT, ISUSED FROM T_SATELLITE WHERE ISUSED = 1")
	List<SatelliteInfo> listSatellite();
	
	// 查询所有卫星ID
	@Select("SELECT SATELLITEID FROM T_SATELLITE WHERE ISUSED = 1")
	List<String> listSatelliteId();
	
	// 根据卫星ID查询两行根数
	@Select("SELECT SATELLITEID, TLELINE0 as tleLine0, TLELINE1 as tleLine1, TLELINE2 as tleLine2 FROM T_SATELLITE_TLE WHERE SATELLITEID = #{satelliteId}")
	SatelliteTle getSatelliteTle(String satelliteId);
	
	// 把用于SGP4计算的数据放入redis
	void redisSgp4();
	
	// 从redis中取出所有卫星ID
	List<String> listSatelliteIdInRedis();
	
	// 把前端使用的卫星ID存入redis
	void insertSatelliteIdToRedis(JSONArray satelliteIdArray);
	
	// 设置SGP4的计算时间
	//String updateTimeStamp(Long timeStamp, Double multiplier);
	
	// 根据卫星ID获取卫星(星下点)位置
	SatellitePositionVO getSatellitePosition(String satelliteId);
	
	// 获取卫星(星下点)位置
	List<SatellitePositionVO> listSatellitePosition();
	
	// 根据卫星ID获取卫星一段时间内的(星下点)位置
	List<SatellitePositionVO> listSatellitePositions(String satelliteId, Long timeStamp);
	
	// 根据卫星ID获取卫星一个周期内的(星下点)位置
	List<SatellitePositionVO> listSatelliteTlePositions(String satelliteId, Long timeStamp);
	
	// 根据卫星ID获取卫星一段时间内的覆盖范围
	List<SatelliteCircleVO> listSatelliteCircles(String satelliteId, Long timeStamp);
	
	// 根据卫星ID获取卫星轨道根数
	OrbitElemVO getOrbitElem(String satelliteId);
	
	// 根据卫星ID获取对外发送卫星轨道根数
	OrbitElemVO getOrbitElemSend(String satelliteId);
	
	// 根据卫星ID获取卫星运行的周期
	Double getSatelliteTlePeriod(String satelliteId);
	
	// 查找给定卫星的最小运行周期
	Double getSatelliteTleMinPeriod(JSONArray satelliteIdArray);
	
	// 根据卫星ID获取卫星一段时间内的覆盖范围
	List<SatelliteCoverVO> listSatelliteCovers(String satelliteId, Long timeStamp, JSONArray groundStationIdArray);

}
