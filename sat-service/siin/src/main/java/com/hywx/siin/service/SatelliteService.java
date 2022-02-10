package com.hywx.siin.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hywx.siin.common.Page;
import com.hywx.siin.po.GpsFrame;
import com.hywx.siin.po.SatelliteBusiness;
import com.hywx.siin.po.SatelliteInfo;
import com.hywx.siin.vo.SatelliteBusinessVO;

public interface SatelliteService {
	// 查询所有卫星描述信息
	@Select("SELECT SATELLITEID, SATELLITENAME, SATELLITETEXT, ISUSED FROM T_SATELLITE WHERE ISUSED = 1")
	List<SatelliteInfo> listAllSatelliteInfos();
	
	// 查询所有卫星ID
	@Select("SELECT SATELLITEID FROM T_SATELLITE WHERE ISUSED = 1")
	List<String> listAllSatelliteIds();
	
	// 批量插入GPS数据
	@Insert({
		"<script>",
		 "insert into T_SATELLITE_GPS(TIME, SX, SY, SZ, VX, VY, VZ) values ",
		 "<foreach collection='list' item='item' index='index' separator=','>",
		 "(#{item.time}, #{item.sx}, #{item.sy}, #{item.sz}, #{item.vx}, #{item.vy}, #{item.vz})",
		 "</foreach>",
		 "</script>"
	})
	int insertBatchGpsFrame(@Param(value = "list") List<GpsFrame> list);
	
	// 查询GPS数据
	@Select("SELECT TIME, SX, SY, SZ, VX, VY, VZ FROM T_SATELLITE_GPS")
	List<GpsFrame> listGpsFrames();

	// 从txt文件中读取GPS数据
	List<GpsFrame> listGpsFrameFromDataSource();


	/***********************************************************************/
	
	// 分页查询卫星描述信息
	Page listSatelliteInfoByPage(Integer currentPage, int pageSize);
	
	// 根据卫星ID查询卫星是否存在
	@Select("SELECT 1 FROM T_SATELLITE WHERE SATELLITEID = #{satelliteId} AND ISUSED = 1 LIMIT 1")
	Boolean existSatellite(String satelliteId);
	
	// 根据卫星ID查询卫星信息
	@Select("SELECT SATELLITEID, SATELLITENAME, SATELLITETEXT, ISUSED FROM T_SATELLITE WHERE SATELLITEID = #{satelliteId} AND ISUSED = 1")
	SatelliteInfo getSatelliteById(String satelliteId);
	
	// 插入卫星描述信息到数据库
	@Insert("INSERT OR IGNORE INTO T_SATELLITE(SATELLITEID, SATELLITENAME, SATELLITETEXT, ISUSED) "
			+ "VALUES(#{satelliteId}, #{satelliteName}, #{satelliteText}, 1)")
    int insert(@Param("satelliteId") String satelliteId, @Param("satelliteName") String satelliteName, @Param("satelliteText") String satelliteText);
	
	// 根据卫星ID删除卫星描述信息
	@Delete("DELETE FROM T_SATELLITE WHERE SATELLITEID = #{satelliteId}")  
    int delete(@Param("satelliteId") String satelliteId);
	
	// 根据卫星ID更新卫星描述信息
	@Update("UPDATE T_SATELLITE SET SATELLITENAME=#{satelliteName}, SATELLITETEXT=#{satelliteText} WHERE SATELLITEID = #{satelliteId}")  
    int update(@Param("satelliteName") String satelliteName, @Param("satelliteText") String satelliteText, @Param("satelliteId") String satelliteId);
	
	
	/*******************************************************************/
	// 查询所有卫星运营信息
	@Select("SELECT SATELLITEID, USAGE, CONDITION, HEALTH FROM T_SATELLITE_BUSINESS")
	List<SatelliteBusiness> listBusinesses();
	
	// 根据卫星ID查询卫星运营信息是否存在
	@Select("SELECT 1 FROM T_SATELLITE_BUSINESS WHERE SATELLITEID = #{satelliteId} LIMIT 1")
	Boolean existBusiness(String satelliteId);
		
	// 添加卫星运营信息
	@Insert("INSERT OR IGNORE INTO T_SATELLITE_BUSINESS(SATELLITEID, USAGE, CONDITION, HEALTH) VALUES(#{satelliteId}, #{usage}, #{condition}, #{health})")
	int insertBusiness(@Param("satelliteId") String satelliteId, @Param("usage") double usage, @Param("condition") String condition, @Param("health") String health);
		
	
	// 根据卫星ID更新卫星描述信息
	@Update("UPDATE T_SATELLITE_BUSINESS SET USAGE=#{usage}, CONDITION=#{condition}, HEALTH=#{health}  WHERE SATELLITEID = #{satelliteId}")  
	int updateBusiness(@Param("usage") double usage, @Param("condition") String condition, @Param("health") String health, @Param("satelliteId") String satelliteId);
	
	List<SatelliteBusinessVO> listSatelliteBusinesses(Long timeStamp);
	
	int updateSatelliteBusiness(String satelliteId, double usage, String condition, String health);
	
}
