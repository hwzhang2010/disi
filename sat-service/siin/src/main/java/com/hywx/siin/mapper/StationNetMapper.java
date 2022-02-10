package com.hywx.siin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.hywx.siin.po.StationNetReply;
import com.hywx.siin.po.StationNetState;

@Mapper
@Component
public interface StationNetMapper {
	
	// 更新站网计划响应状态
	@Update("UPDATE T_STATIONNET_REPLY SET REPLY=#{reply} WHERE ID = 0")  
	int updateReply(@Param("reply") String reply);
	
	// 根据应答ID查询对应的站网响应
	@Select("SELECT REPLY FROM T_STATIONNET_REPLY WHERE ID = 0")
	String getStationNetReplyById();
	
	// 根据应答查询对应的站网响应信息
	@Select("SELECT ID, REPLY, REASON FROM T_STATIONNET_REPLY WHERE REPLY = #{reply}")
	StationNetReply getStationNetReply(String reply);
	
	// 更新站网状态
	@Update("UPDATE T_STATIONNET_STATE SET GROUNDSTATIONNAME=#{groundStationName}, SUBSYSTEMID=#{subsystemId}, EQUIPMENTID=#{equipmentId}, WARNING=#{warning}, HEALTHLEVEL=#{healthLevel} WHERE ID = 0")  
	int updateState(@Param("groundStationName") String groundStationName, @Param("subsystemId") Integer subsystemId, @Param("equipmentId") String equipmentId, @Param("warning") String warning, @Param("healthLevel") Integer healthLevel);
	
	// 根据ID查询对应的站网状态信息
	@Select("SELECT ID, GROUNDSTATIONNAME, SUBSYSTEMID, EQUIPMENTID, WARNING, HEALTHLEVEL FROM T_STATIONNET_STATE WHERE ID = 0")
	StationNetState getStationNetState();

}
