package com.hywx.siin.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hywx.siin.common.Page;
import com.hywx.siin.po.TmRsltFrame;
import com.hywx.sitm.vo.SitmGroundStationFaultVO;
import com.hywx.sitm.vo.SitmSatelliteFaultVO;
import com.hywx.sitm.vo.SitmSatelliteRunningVO;
import com.hywx.sitm.vo.SitmSatelliteVO;
import com.hywx.sitm.vo.TmRsltFrameVO;

public interface TmService {
	
	// 查询id, name, 表名通过拼接字符串给出
    @Select("SELECT CODENAME as codeName, NAME as name, ID as id, SRCTYPE as srcType, RSLTTYPE as rsltType, BD as bd, BITRANGE as bitRange, BYTEORDER as byteOrder, COEFFICIENT as coefficient, ALGORITHM as algorithm, RANGE as range, PRECONDITION as preCondition, VALIDFRAMECNT as validFrameCnt, FRAMEID as frameId, SUBSYSTEMID as subsystemId, OBJID as objId FROM ${tableName} ")
    List<TmRsltFrame> listTmRsltFrames(@Param("tableName") String tableName);
    
 // 查询表是否存在
    @Select("SELECT count(*) FROM sqlite_master WHERE type='table' AND TBL_NAME = '${tableName}' ")
    int existTmRsltFrame(@Param("tableName") String tableName);
    
    // 删除表语句
    @Update("DROP TABLE ${tableName}")
    int dropTmRsltFrame(@Param("tableName") String tableName);
    
    // 创建表语句
    @Update("create table ${tableName}( CODENAME VARCHAR(100)  NOT NULL, " +
            " NAME VARCHAR(200) NOT NULL, " +
            " ID INTEGER NOT NULL, " +
            " SRCTYPE VARCHAR(10), " + 
            " RSLTTYPE VARCHAR(10), " +
            " BD VARCHAR(100), " +
            " BITRANGE VARCHAR(20), " +
            " BYTEORDER INTEGER, " +
            " COEFFICIENT CLOB, " +
            " ALGORITHM VARCHAR(500), " +
            " RANGE VARCHAR(200), " +
            " PRECONDITION VARCHAR(200), " +
            " VALIDFRAMECNT VARCHAR(100), " +
            " FRAMEID INTEGER NOT NULL, " +
            " SUBSYSTEMID INTEGER NOT NULL, " +
            " OBJID INTEGER NOT NULL, " +
            " PRIMARY KEY ( ID, FRAMEID, OBJID )  )")
    int createTmRsltFrame(@Param("tableName") String tableName);
    
    // 插入1条遥测参数
 	@Insert("INSERT INTO ${tableName}(CODENAME, NAME, ID, SRCTYPE, RSLTTYPE, BD, BITRANGE, BYTEORDER, COEFFICIENT, ALGORITHM, RANGE, PRECONDITION, VALIDFRAMECNT, FRAMEID, SUBSYSTEMID, OBJID) VALUES(#{codeName}, #{name}, #{id}, #{srcType}, #{rsltType}, #{bd}, #{bitRange}, #{byteOrder}, #{coefficient}, #{algorithm}, #{range}, #{preCondition}, #{validFrameCnt}, #{frameId}, #{subsystemId}, #{objId})")
 	int insertTmRslt(@Param("tableName") String tableName, @Param("codeName") String codeName, @Param("name") String name, @Param("id") int id, @Param("srcType") String srcType, @Param("rsltType") String rsltType, @Param("bd") String bd, @Param("bitRange") String bitRange, @Param("byteOrder") int byteOrder , @Param("coefficient") String coefficient, @Param("algorithm") String algorithm, @Param("range") String range, @Param("preCondition") String preCondition, @Param("validFrameCnt") String validFrameCnt, @Param("frameId") int frameId, @Param("subsystemId") int subsystemId, @Param("objId") int objId );
 	
    // 删除1条遥测参数
 	@Delete("DELETE FROM ${tableName} WHERE ID = #{id}")
 	int deleteTmRslt(@Param("tableName") String tableName, @Param("id") int id);
 	
    // 根据卫星ID和参数ID更新遥测参数
 	@Update("UPDATE ${tableName} SET CODENAME=#{codeName}, NAME=#{name}, SRCTYPE=#{srcType}, RSLTTYPE=#{rsltType}, BD=#{bd}, BITRANGE=#{bitRange}, BYTEORDER=#{byteOrder}, COEFFICIENT=#{coefficient}, ALGORITHM=#{algorithm}, RANGE=#{range}, PRECONDITION=#{preCondition}, VALIDFRAMECNT=#{validFrameCnt}, FRAMEID=#{frameId}, SUBSYSTEMID=#{subsystemId}, OBJID=#{objId} WHERE ID = #{id}")  
    int updateTmRslt(@Param("tableName") String tableName, @Param("codeName") String codeName, @Param("name") String name, @Param("srcType") String srcType, @Param("rsltType") String rsltType, @Param("bd") String bd, @Param("bitRange") String bitRange, @Param("byteOrder") int byteOrder , @Param("coefficient") String coefficient, @Param("algorithm") String algorithm, @Param("range") String range, @Param("preCondition") String preCondition, @Param("validFrameCnt") String validFrameCnt, @Param("frameId") int frameId, @Param("subsystemId") int subsystemId, @Param("objId") int objId, @Param("id") int id);
 	
 	int existTmRsltTable2(String satelliteId);
 	
 	int dropTmRsltFrame2(String satelliteId);
 	
 	int createTmRsltFrame2(String satelliteId);
 	
 	int insertTmRslt(String satelliteId, TmRsltFrame rslt);
 	
 	int deleteTmRslt2(String satelliteId, int id);
 	
 	int updateTmRslt(String satelliteId, TmRsltFrame rslt);
 	
	
	// 分页查询遥测参数
	Page listTmRstlFramesByPage(String satelliteId, Integer currentPage, int pageSize);

	
	
	// 从Redis中取出前端显示的遥测仿真数据列表
	List<TmRsltFrameVO> listSitms(String satelliteId);
	
	// 从Redis中取出前端显示的遥测仿真数据列表(分页)
	Page listSitmsByPage(String satelliteId, Integer currentPage, Integer pageSize);
	
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
	
	// 返回遥测仿真发送计数
	int getSatelliteSendCount(String satelliteId);
	
	// 遥测仿真发送计数置0
	void updateSatelliteSendCountZero(String satelliteId);
	
	// 获取遥测仿真自动发送的信关站
	String getGroundStationOfAutoSend(String satelliteId);
	
	// 更新遥测仿真自动发送的信关站
	void updateGroundStationOfAutoSend(String satelliteId, String groundStationId);
	
	// 更新遥测仿真数据故障的卫星
	void updateSatelliteFault(List<String> satelliteIdList);
	
	// 更新故障的信关站
	void updateGroundStationFault(List<String> groundStationIdList);
	
	// 返回已经设置了的故障卫星
	SitmSatelliteFaultVO getSatelliteFaulting();
	
	// 返回已经设置了的故障信关站
	SitmGroundStationFaultVO getGroundStationFaulting();
	
}
