package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.ContextUtils;
import com.hywx.userservice.constant.GwConstant;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.vo.LogSearchVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @program: user-serice
 * @description: 用户操作日志表 service 接口
 * @author tangjing
 * @date 2020-03-06
 */
public interface LogService extends IService<Log> {


    List<Log> list(QueryWrapper<Log> log);

    IPage<Log> getListByPage(QueryRequest request, LogSearchVo searchVo);

    int saveData(Log log);

    int updateData(Log log);

    int deleteById(String id);

    int batchDelete(String[] ids);
    /**
     * 异步保存操作日志 -- 本服务调用方式
     *  @param point     切点
     * @param method    Method
     * @param ip        ip
     * @param operation 操作内容
     * @param username  操作用户
     * @param start     开始时间
     */
    @Async(GwConstant.ASYNC_POOL)
    ContextUtils saveLog(ProceedingJoinPoint point, Method method, String ip, String operation, String username, long start);

    /** @Author: LXYuuuuuu
     * @Description: 异步保存操作日志--微服务调用方式
     * @Date 2020/3/18 14:22
     * @Param log
     * @return boolean
     * @throws:
     **/
    @Async(GwConstant.ASYNC_POOL)
    void saveLog(Log log);
}