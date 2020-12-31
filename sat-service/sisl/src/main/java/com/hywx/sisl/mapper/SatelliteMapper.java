package com.hywx.sisl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hywx.sisl.po.SatelliteInfo;
import com.hywx.sisl.po.SatelliteTle;

@Mapper
@Component
public interface SatelliteMapper {
	// 查询所有卫星描述信息
	@Select("SELECT SATELLITEID, SATELLITENAME, SATELLITETEXT, ISUSED FROM T_SATELLITE WHERE ISUSED = 1")
	List<SatelliteInfo> listSatellite();
	
	// 查询所有卫星ID
	@Select("SELECT SATELLITEID FROM T_SATELLITE WHERE ISUSED = 1")
	List<String> listSatelliteId();
	
	// 根据卫星ID查询两行根数
	@Select("SELECT SATELLITEID, TLELINE0 as tleLine0, TLELINE1 as tleLine1, TLELINE2 as tleLine2 FROM T_SATELLITE_TLE WHERE SATELLITEID = #{satelliteId}")
	SatelliteTle getTle(String satelliteId);
	

}
