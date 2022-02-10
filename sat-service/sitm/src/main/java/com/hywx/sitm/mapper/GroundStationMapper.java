package com.hywx.sitm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hywx.sitm.po.GroundStationInfo;

@Mapper
@Component
public interface GroundStationMapper {
	// 查询所有地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<GroundStationInfo> listGroundStations();
	
	// 查询所有地面站ID
	@Select("SELECT GROUNDSTATIONID FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<String> listGroundStationIds();
	
	// 根据地面站ID查询对应的地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId}")
	GroundStationInfo getGroundStationById(String groundStationId);

	// 根据信关站ID查询信关站是否存在
	@Select("SELECT 1 FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId} AND ISUSED = 1 LIMIT 1")
	Boolean existGroundStation(String groundStationId);
}
