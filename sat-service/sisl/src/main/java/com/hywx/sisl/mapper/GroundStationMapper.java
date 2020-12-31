package com.hywx.sisl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hywx.sisl.po.GroundStationInfo;

@Mapper
@Component
public interface GroundStationMapper {
	// 查询所有地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<GroundStationInfo> listGoundStation();
	
	// 查询所有地面站ID
	@Select("SELECT GROUNDSTATIONID FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<String> listGroundStationId();
	
	// 根据地面站ID查询对应的地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId}")
	GroundStationInfo getGroundStationById(String groundStationId);
	

}
