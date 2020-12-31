package com.hywx.sitm.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.hywx.sitm.po.InjectionData;
import com.hywx.sitm.po.TcData;

public interface TcService {
	
	// 插入1条遥控指令
	@Insert("INSERT INTO T_SATELLITE_CMD(SENDTIME, SATELLITEID, STATIONID, DATA) VALUES(#{sendTime}, #{satelliteId}, #{stationId}, #{data})")
	int insertTc(@Param("sendTime") long sendTime, @Param("satelliteId") String satelliteId, @Param("stationId") String stationId, @Param("data") String data);
	
	// 删除1月之前的遥控指令
	@Delete("DELETE FROM T_SATELLITE_CMD WHERE SENDTIME > #{sendTime}")
	int deleteTc(@Param("sendTime") long sendTime);
		
	// 根据卫星ID和时间范围查询遥控指令
	@Select("SELECT SENDTIME, SATELLITEID, STATIONID, DATA FROM T_SATELLITE_CMD WHERE SATELLITEID = #{satelliteId} AND SENDTIME BETWEEN #{startTime} AND #{endTime} ORDER BY SENDTIME DESC LIMIT 1024")
	List<TcData> listTcBySatelliteId(@Param("satelliteId") String satelliteId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	// 根据卫星ID, 信关站ID和时间范围查询遥控指令
	@Select("SELECT SENDTIME, SATELLITEID, STATIONID, DATA FROM T_SATELLITE_CMD WHERE SATELLITEID = #{satelliteId} AND STATIONID = #{stationId} AND SENDTIME BETWEEN #{startTime} AND #{endTime} ORDER BY SENDTIME DESC LIMIT 1024")
	List<TcData> listTcBySatelliteIdAndStationId(@Param("satelliteId") String satelliteId, @Param("stationId") String stationId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	// 存储遥控指令
	int saveTc(byte[] data);
	
	// 删除1月之前的遥控指令
	int deleteTc();
	
	
	// 插入1条数据注入
	@Insert("INSERT INTO T_SATELLITE_INJECTION(SENDTIME, SATELLITEID, STATIONID, DATA) VALUES(#{sendTime}, #{satelliteId}, #{stationId}, #{data})")
	int insertInjection(@Param("sendTime") long sendTime, @Param("satelliteId") String satelliteId, @Param("stationId") String stationId, @Param("data") String data);
	
	// 删除1月之前的数据注入
	@Delete("DELETE FROM T_SATELLITE_INJECTION WHERE SENDTIME > #{sendTime}")
	int deleteInjection(@Param("sendTime") long sendTime);
	
	// 根据卫星ID和时间范围查询数据注入
	@Select("SELECT SENDTIME, SATELLITEID, STATIONID, DATA FROM T_SATELLITE_INJECTION WHERE SATELLITEID = #{satelliteId} AND SENDTIME BETWEEN #{startTime} AND #{endTime} ORDER BY SENDTIME DESC LIMIT 1024")
	List<InjectionData> listInjectionBySatelliteId(@Param("satelliteId") String satelliteId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	// 根据卫星ID, 信关站ID和时间范围查询数据注入
	@Select("SELECT SENDTIME, SATELLITEID, STATIONID, DATA FROM T_SATELLITE_INJECTION WHERE SATELLITEID = #{satelliteId} AND STATIONID = #{stationId} AND SENDTIME BETWEEN #{startTime} AND #{endTime} ORDER BY SENDTIME DESC LIMIT 1024")
	List<InjectionData> listInjectionBySatelliteIdAndStationId(@Param("satelliteId") String satelliteId, @Param("stationId") String stationId, @Param("startTime") long startTime, @Param("endTime") long endTime);
	
	// 存储数据注入
	int saveInjection(byte[] data);
	
	// 删除1月之前的数据注入
	int deleteInjection();
	
	

}
