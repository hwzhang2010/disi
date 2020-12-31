package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.userservice.dao.LoginLog;
import org.springframework.stereotype.Repository;

/**
 * @program: user-serice
 * @description: 登录日志表 Mapper 接口
 * @author tangjing
 * @date 2020-03-06
 */
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}

