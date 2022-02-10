package com.hywx.siin.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hywx.siin.common.Page;
import com.hywx.siin.po.GroundStationBusiness;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.vo.GroundStationBusinessVO;

public interface GroundStationService {
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
	
	// 分页查询信关站描述信息
	Page listGroundStationInfoByPage(Integer currentPage, int pageSize);
	
	
	/*************************************************************************/
	// 查询所有信关站运营信息
	@Select("SELECT GROUNDSTATIONID, USAGE, EQUIPMENT, CARRIER, HEALTH FROM T_GROUNDSTATION_BUSINESS")
	List<GroundStationBusiness> listBusinesses();
	
	// 根据信关站ID查询信关站运营信息是否存在
	@Select("SELECT 1 FROM T_GROUNDSTATION_BUSINESS WHERE GROUNDSTATIONID = #{groundStationId} LIMIT 1")
	Boolean existBusiness(String groundStationId);
			
	// 添加信关站运营信息
	@Insert("INSERT OR IGNORE INTO T_GROUNDSTATION_BUSINESS(GROUNDSTATIONID, USAGE, EQUIPMENT, CARRIER, HEALTH) VALUES(#{groundStationId}, #{usage}, #{equipment}, #{carrier}, #{health})")
	int insertBusiness(@Param("groundStationId") String groundStationId, @Param("usage") double usage, @Param("equipment") String equipment, @Param("carrier") String carrier, @Param("health") String health);
			
	// 根据信关站ID更新信关站运营信息
	@Update("UPDATE T_GROUNDSTATION_BUSINESS SET USAGE=#{usage}, EQUIPMENT=#{equipment}, CARRIER=#{carrier}, HEALTH=#{health}  WHERE GROUNDSTATIONID = #{groundStationId}")  
	int updateBusiness(@Param("usage") double usage, @Param("equipment") String equipment, @Param("carrier") String carrier, @Param("health") String health, @Param("groundStationId") String groundStationId);
	
	List<GroundStationBusinessVO> listGroundStationBusinesses();
	
	int updateGroundStationBusiness(String groundStationId, double usage, String equipment, String carrier, String health);
	
}
