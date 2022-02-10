package com.hywx.siin.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hywx.siin.common.Page;
import com.hywx.siin.po.Device;
import com.hywx.siin.po.Fault;
import com.hywx.siin.po.FaultSend;
import com.hywx.sisn.vo.FaultGroundStationVO;
import com.hywx.sisn.vo.FaultLevelVO;
import com.hywx.sisn.vo.FaultSatelliteVO;

public interface FaultService {
	// 查询所有设备信息
	@Select("SELECT ID, NAME, FAULTMAIN FROM T_DEVICE")
	List<Device> listDevices();
	
	// 查询所有设备信息(卫星)
	@Select("SELECT ID, NAME, FAULTMAIN FROM T_DEVICE WHERE ID <= 22")
	List<Device> listSatelliteDevices();
	
	// 查询所有设备信息(信关站)
	@Select("SELECT ID, NAME, FAULTMAIN FROM T_DEVICE WHERE ID > 22")
	List<Device> listGroundStationDevices();
	
	// 根据ID查询设备名称
	@Select("SELECT NAME FROM T_DEVICE WHERE ID = #{id}")
	String getDeviceNameById(Integer id);
	
	// 根据ID查询主类别
	@Select("SELECT FAULTMAIN FROM T_DEVICE WHERE ID = #{id}")
	Integer getMainById(Integer id);
	
	// 分页查询设备信息
	Page listDevicesByPage(Integer currentPage, int pageSize);

	
	// 根据主ID和子ID查询故障信息
	@Select("SELECT MAINID, MAINNAME, SUBID, SUBNAME FROM T_FAULT WHERE MAINID = #{mainId} AND SUBID = #{subId}")
	Fault getFaultById(Integer mainId, Integer subId);
	
	// 根据主ID查询所有故障信息
	@Select("SELECT MAINID, MAINNAME, SUBID, SUBNAME FROM T_FAULT WHERE MAINID = #{mainId} ORDER BY SUBID")
	List<Fault> listFaultByMainId(Integer mainId);
	
	
	// 插入等待发送的故障信息
	@Insert("INSERT OR IGNORE INTO T_FAULT_SEND(TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING) "
			+ "VALUES(#{type}, #{satelliteId}, #{groundStationId}, #{deviceId}, #{mainId}, #{mainName}, #{subId}, #{subName}, #{level}, #{waiting})")
    int insertFault(@Param("type") Integer type, @Param("satelliteId") String satelliteId, @Param("groundStationId") String groundStationId, @Param("deviceId") Integer deviceId, 
    		@Param("mainId") Integer mainId, @Param("mainName") String mainName, @Param("subId") Integer subId, @Param("subName") String subName, @Param("level") Integer level,
    		@Param("waiting") Boolean waiting);
	
	// 批量插入故障信息
	@Insert({
		"<script>",
		 "insert or ignore into T_FAULT_SEND(TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING) values ",
		 "<foreach collection='list' item='item' index='index' separator=','>",
		 "(#{item.type}, #{item.satelliteId}, #{item.groundStationId}, #{item.deviceId}, #{item.mainId}, #{item.mainName}, #{item.subId}, #{item.subName}, #{item.level}, #{item.waiting})",
		 "</foreach>",
		 "</script>"
	})
	int insertBatchFault(@Param(value = "list") List<FaultSend> list);
	
	// 更新等待状态
	@Update("UPDATE T_FAULT_SEND SET WAITING=#{waiting}")  
	int updateFault(@Param("waiting") Boolean waiting);
	
	// 根据等待状态删除故障卫星
	@Delete("DELETE FROM T_FAULT_SEND WHERE SATELLITEID = #{satelliteId} AND TYPE = 0")  
    int deleteFaultSatellite(@Param("satelliteId") String satelliteId);
	
	
	// 根据等待状态删除故障卫星
	@Delete("DELETE FROM T_FAULT_SEND WHERE GROUNDSTATIONID = #{groundStationId} AND TYPE = 1")  
	int deleteFaultGroundStation(@Param("groundStationId") String groundStationId);
	
	// 删除故障恢复
	@Delete("DELETE FROM T_FAULT_SEND WHERE MAINID = 0 AND SUBID = 0")  
	int deleteFault();
	
	// 根据卫星ID查询卫星故障是否存在
	@Select("SELECT 1 FROM T_FAULT_SEND WHERE TYPE = 0 AND SATELLITEID = #{satelliteId} LIMIT 1")
	Boolean existFaultSatellite(String satelliteId);
		
	// 根据卫星ID查询故障卫星信息
	@Select("SELECT TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING FROM T_FAULT_SEND WHERE TYPE = 0 AND SATELLITEID = #{satelliteId} LIMIT 1")
	FaultSend getFaultSatelliteById(String satelliteId);
		
	// 根据信关站ID查询信关站故障是否存在
	@Select("SELECT 1 FROM T_FAULT_SEND WHERE TYPE = 1 AND GROUNDSTATIONID = #{groundStationId} LIMIT 1")
	Boolean existFaultGroundStation(String groundStationId);
				
	// 根据信关站ID查询故障信关站信息
	@Select("SELECT TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING FROM T_FAULT_SEND WHERE TYPE = 1 AND GROUNDSTATIONID = #{groundStationId} LIMIT 1")
	FaultSend getFaultGroundStationById(String groundStationId);
	
	// 查询故障卫星ID
	@Select("SELECT DISTINCT SATELLITEID FROM T_FAULT_SEND WHERE TYPE = 0 ORDER BY SATELLITEID")
	List<String> listFaultSatelliteIds();
	
	// 查询故障信关站ID
	@Select("SELECT DISTINCT GROUNDSTATIONID FROM T_FAULT_SEND WHERE TYPE = 1 ORDER BY GROUNDSTATIONID")
	List<String> listFaultGroundStationIds();
	
	// 查询故障信息
	@Select("SELECT TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING FROM T_FAULT_SEND")
	List<FaultSend> listFaults();
	
	// 查询故障信息(等待发送)
	@Select("SELECT TYPE, SATELLITEID, GROUNDSTATIONID, DEVICEID, MAINID, MAINNAME, SUBID, SUBNAME, LEVEL, WAITING FROM T_FAULT_SEND WHERE WAITING = #{waiting}")
	List<FaultSend> listFaultWaiting(@Param("waiting") Boolean waiting);
	
	List<FaultLevelVO> listFaultLevels();
	
    List<FaultSatelliteVO> listFaultSatellites();
	
	List<FaultSatelliteVO> listFaultSatellitesById(String satelliteId);
	
	List<FaultSatelliteVO> listFaultSatellitesByLevel(Integer level);
	
	List<FaultSatelliteVO> listFaultSatellitesByIdLevel(String satelliteId, Integer level);
		
	List<FaultGroundStationVO> listFaultGroundStations();
	
	List<FaultGroundStationVO> listFaultGroundStationsById(String groundStationId);
	
	List<FaultGroundStationVO> listFaultGroundStationsByLevel(Integer level);
	
	List<FaultGroundStationVO> listFaultGroundStationsByIdLevel(String groundStationId, Integer level);
	
	// 故障标识放入redis等待发送
	void sendFault(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level);
	
	// 卫星故障放入sqlite等待发送
	void sendFaultSatellite(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level);
	
	// 卫星故障放入sqlite等待发送
	void sendFaultSatellites(List<String> satelliteIdList, String groundStationId, Integer id, Fault fault, Integer level);
	
	// 信关站故障放入sqlite等待发送
	void sendFaultGroundStations(String satelliteId, List<String> groundStationIdList, Integer id, Fault fault, Integer level);
	
	// 信关站故障放入sqlite等待发送
	void sendFaultGroundStation(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level);
		
	
}
