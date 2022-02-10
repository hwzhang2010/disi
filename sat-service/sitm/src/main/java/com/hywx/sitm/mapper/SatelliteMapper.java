package com.hywx.sitm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.po.Satellite;
import com.hywx.sitm.po.SatelliteTle;

@Mapper
@Component
public interface SatelliteMapper {
	// 查询所有卫星描述信息
	@Select("SELECT SATELLITEID, SATELLITENAME, SATELLITETEXT FROM T_SATELLITE WHERE ISUSED = 1")
	List<Satellite> listSatellites();
	
	// 查询所有卫星ID
	@Select("SELECT SATELLITEID FROM T_SATELLITE WHERE ISUSED = 1")
	List<String> listSatelliteIds();
	
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
	
	// 根据卫星ID查询两行根数
	@Select("SELECT SATELLITEID, TLELINE0 as tleLine0, TLELINE1 as tleLine1, TLELINE2 as tleLine2 FROM T_SATELLITE_TLE WHERE SATELLITEID = #{satelliteId}")
	SatelliteTle getTle(String satelliteId);
	
}
