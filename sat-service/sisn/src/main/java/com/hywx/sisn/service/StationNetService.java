package com.hywx.sisn.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hywx.sisn.po.StationNetReply;
import com.hywx.sisn.po.StationNetState;

public interface StationNetService {
	
	// 更新站网计划响应状态
	@Update("UPDATE T_STATIONNET_REPLY SET REPLY=#{reply} WHERE ID = 0")  
	int updateReply(@Param("reply") String reply);
	
	// 根据应答ID查询对应的站网响应
	@Select("SELECT REPLY FROM T_STATIONNET_REPLY WHERE ID = 0")
	String getStationNetReplyById();
	
	// 根据应答查询对应的站网响应信息
	@Select("SELECT ID, REPLY, REASON FROM T_STATIONNET_REPLY WHERE REPLY = #{reply} AND ID > 0")
	StationNetReply getStationNetReply(String reply);
	
	// 更新站网状态
	@Update("UPDATE T_STATIONNET_STATE SET GROUNDSTATIONNAME=#{groundStationName}, SUBSYSTEMID=#{subsystemId}, EQUIPMENTID=#{equipmentId}, WARNING=#{warning}, HEALTHLEVEL=#{healthLevel} WHERE ID = 0")  
	int updateState(@Param("groundStationName") String groundStationName, @Param("subsystemId") Integer subsystemId, @Param("equipmentId") String equipmentId, @Param("warning") String warning, @Param("healthLevel") Integer healthLevel);
	
	// 根据ID查询对应的站网状态信息
	@Select("SELECT ID, GROUNDSTATIONNAME, SUBSYSTEMID, EQUIPMENTID, WARNING, HEALTHLEVEL FROM T_STATIONNET_STATE WHERE ID = 0")
	StationNetState getStationNetState();
		

}
