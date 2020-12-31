package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.userservice.dao.LoginLog;

import java.util.List;

/**
 * @program: user-serice
 * @description: 登录日志表 service 接口
 * @author tangjing
 * @date 2020-03-06
 */
public interface LoginLogService extends IService<LoginLog> {


    List<LoginLog> list(QueryWrapper<LoginLog> loginLog);

    IPage<LoginLog> getListByPage(QueryRequest queryRequest, LoginLog loginLog);

    boolean saveData(LoginLog loginLog);

    int updateData(LoginLog loginLog);

    int deleteById(String id);

    int batchDelete(List<String> ids);
}