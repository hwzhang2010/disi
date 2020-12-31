package com.hywx.sitm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.hywx.sitm.po.TmRsltFrame;
import com.hywx.sitm.vo.SitmSatelliteRunningVO;
import com.hywx.sitm.vo.SitmSatelliteVO;
import com.hywx.sitm.vo.TmRsltFrameVO;

public interface TmService {
	// 查询id, name, 表名通过拼接字符串给出
	@Select("SELECT CODENAME as codeName, NAME as name, ID as id, SRCTYPE as srcType, RSLTTYPE as rsltType, BD as bd, BITRANGE as bitRange, BYTEORDER as byteOrder, RANGE as range, VALIDFRAMECNT as validFrameCnt, FRAMEID as frameId, SUBSYSTEMID as subsystemId, OBJID as objId FROM ${tableName} ")
    List<TmRsltFrame> listTmRsltFrames(@Param("tableName") String tableName);
	
	// 把遥测仿真数据表放入Redis
	void redisSitm(List<String> satelliteIdList, String group);
	
	// 从Redis中取出前端显示的遥测仿真数据列表
	List<TmRsltFrameVO> listSitms(String satelliteId);
	
	// 根据卫星ID返回卫星是否在进行遥测仿真
	boolean getSatelliteIsRunning(String satelliteId);
	
	// 返回所有正在进行遥测仿真的卫星列表
	SitmSatelliteRunningVO getSatelliteRunning();
	
	// 根据卫星ID返回卫星是否在进行遥测仿真和遥测仿真发送方式
	SitmSatelliteVO getSatellite(String satelliteId);
	
	// 更新遥测仿真的运行状态
	void updateSatelliteRun(String satelliteId, String sendType, boolean isRun);
	
	// 更新批量遥测仿真的运行状态
	void updateSatelliteRunBatch(List<String> satelliteIdList, String sendType);
	
	// 更新所有遥测仿真的运行状态
	void updateSatelliteRunAll(String sendType, boolean isRun);
	
	// 更新遥测仿真的发送方式
	boolean updateSatelliteSendType(String satelliteId, String sendType);
	
	// 更新遥测仿真的仿真参数类型
	boolean updateSatelliteParamType(String satelliteId, int paramId, String paramType);
	
	// 更新遥测仿真的仿真参数系数
	boolean updateSatelliteCoefficient(String satelliteId, int paramId, double coefficient);
	
}
