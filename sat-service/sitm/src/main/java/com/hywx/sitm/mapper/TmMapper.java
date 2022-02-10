package com.hywx.sitm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.hywx.sitm.po.TmRsltFrame;

@Mapper
@Component
public interface TmMapper {
	// 查询id, name, 表名通过拼接字符串给出
    @Select("SELECT CODENAME as codeName, NAME as name, ID as id, SRCTYPE as srcType, RSLTTYPE as rsltType, BD as bd, BITRANGE as bitRange, BYTEORDER as byteOrder, RANGE as range, VALIDFRAMECNT as validFrameCnt, FRAMEID as frameId, SUBSYSTEMID as subsystemId, OBJID as objId FROM ${tableName} ")
    List<TmRsltFrame> listTmRsltFrames(@Param("tableName") String tableName);
    
    // 判断卫星ID的遥测参数表是否存在
    @Select("SELECT count(*) FROM sqlite_master WHERE type='table' AND TBL_NAME = '${tableName}' ")
    int existTmRsltFrame(@Param("tableName") String tableName);
    
    // 删除表语句
    @Update("DROP TABLE ${tableName}")
    void dropTmRsltFrame(@Param("tableName") String tableName);
    
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
    void createTmRsltFrame(@Param("tableName") String tableName);
    
    
}
