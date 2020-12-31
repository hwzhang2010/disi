package com.hywx.siin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.hywx.siin.po.GroundStationInfo;

@Mapper
@Component
public interface GroundStationMapper {
	// 查询所有地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<GroundStationInfo> listAllGroundStations();
	
	// 查询所有地面站ID
	@Select("SELECT GROUNDSTATIONID FROM T_GROUNDSTATION WHERE ISUSED = 1")
	List<String> listAllGroundStationIds();
	
	// 根据地面站ID查询对应的地面站的描述信息
	@Select("SELECT GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE as groundStationLng, GROUNDSTATIONLATITUDE as groundStationLat, GROUNDSTATIONALTITUDE as groundStationAlt, ISUSED as isUsed FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId}")
	GroundStationInfo getGroundStationById(String groundStationId);

	// 根据信关站ID查询信关站是否存在
	@Select("SELECT 1 FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId} AND ISUSED = 1 LIMIT 1")
	Boolean existGroundStation(String groundStationId);
	
	// 插入信关站描述信息到数据库
	@Insert("INSERT OR IGNORE INTO T_GROUNDSTATION(GROUNDSTATIONID, GROUNDSTATIONNAME, GROUNDSTATIONTEXT, GROUNDSTATIONLONGITUDE, GROUNDSTATIONLATITUDE, GROUNDSTATIONALTITUDE, ISUSED) "
			+ "VALUES(#{groundStationId}, #{groundStationName}, #{groundStationText}, #{groundStationLng}, #{groundStationLat}, #{groundStationAlt}, 1)")
    int insert(@Param("groundStationId") String groundStationId, @Param("groundStationName") String groundStationName, @Param("groundStationText") String groundStatonText, @Param("groundStationLng") double groundStationLng, @Param("groundStationLat") double groundStationLat, @Param("groundStationAlt") double groundStationAlt);
	
	// 根据信关站ID删除信关站描述信息
	@Delete("DELETE FROM T_GROUNDSTATION WHERE GROUNDSTATIONID = #{groundStationId}")  
    int delete(@Param("groundStationId") String groundStationId);
	
	// 根据信关站ID更新信关站描述信息
	@Update("UPDATE T_GROUNDSTATION SET GROUNDSTATIONNAME=#{groundStationName}, GROUNDSTATIONTEXT=#{groundStationText}, GROUNDSTATIONLONGITUDE=#{groundStationLng}, GROUNDSTATIONLATITUDE=#{groundStationLat}, GROUNDSTATIONALTITUDE=#{groundStationAlt} WHERE GROUNDSTATIONID = #{groundStationId}")  
    int update(@Param("groundStationName") String groundStationName, @Param("groundStationText") String groundStationText, @Param("groundStationLng") double groundStationLng, @Param("groundStationLat") double groundStationLat, @Param("groundStationAlt") double groundStationAlt, @Param("groundStationId") String groundStationId);
	
}
