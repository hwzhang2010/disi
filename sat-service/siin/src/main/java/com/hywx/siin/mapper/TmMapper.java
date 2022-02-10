package com.hywx.siin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.hywx.siin.po.TmRsltFrame;

@Mapper
@Component
public interface TmMapper {
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
 	
    
}
