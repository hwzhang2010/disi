package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.userservice.dao.Log;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: user-serice
 * @description: 用户操作日志表 Mapper 接口
 * @author tangjing
 * @date 2020-03-06
 */
@Repository
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}

