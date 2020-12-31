package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.SortUtil;
import com.hywx.common.core.util.UuidTool;
import com.hywx.common.core.util.results.ContextUtils;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.mapper.LogMapper;
import com.hywx.userservice.service.LogService;
import com.hywx.userservice.util.AddressUtil;
import com.hywx.userservice.vo.LogSearchVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author tangjing
 * @program: user-serice
 * @description: 用户操作日志表 service 接口实现类
 * @date 2020-03-06
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    private final ObjectMapper objectMapper;

    @Override
    public List<Log> list(QueryWrapper<Log> log) {
        return logMapper.selectList(log);
    }

    @Override
    public IPage<Log> getListByPage(QueryRequest request, LogSearchVo searchVo) {

        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(searchVo.getUsername()))
            queryWrapper.lambda().like(Log::getUsername, searchVo.getUsername());
        if (StringUtils.isNotBlank(searchVo.getOperation()))
            queryWrapper.lambda().like(Log::getOperation, searchVo.getOperation());
        if (StringUtils.isNotBlank(searchVo.getCreateTimeFrom()) && StringUtils.isNotBlank(searchVo.getCreateTimeTo()))
            queryWrapper.lambda()
                    .ge(Log::getCreateTime, searchVo.getCreateTimeFrom())
                    .le(Log::getCreateTime, searchVo.getCreateTimeTo());
        Page<Log> page = new Page<>(request.getPageNo(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", CommonConstant.ORDER_DESC, true);

        return this.page(page, queryWrapper);

    }


    @Override
    public int saveData(Log log) {
        return logMapper.insert(log);
    }

    @Override
    public int updateData(Log log) {
        return logMapper.updateById(log);
    }

    @Override
    public int deleteById(String id) {
        return logMapper.deleteById(id);
    }

    @Override
    public int batchDelete(String[] ids) {
        List<String> list = Arrays.asList(ids);
        return logMapper.deleteBatchIds(list);
    }

    @Override
    public ContextUtils saveLog(ProceedingJoinPoint point, Method method, String ip, String operation, String username, long start) {
        Log log = new Log();
        log.setId(UuidTool.getUUID());
        log.setIp(ip);

        log.setUsername(username);
        log.setTime(System.currentTimeMillis() - start);
        log.setOperation(operation);

        String className = point.getTarget().getClass().getName();
        String methodName = method.getName();
        log.setMethod(className + "." + methodName + "()");

        Object[] args = point.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            log.setParams(params.toString());
        }
        log.setCreateTime(new Date());
        //注：如果项目路径存在中文，会出现找不到文件报错的情况
        log.setLocation(AddressUtil.getCityInfo(ip));
//        log.setLocation("内网IP|0|0|内网IP|内网IP");
        // 保存系统日志
        save(log);
        return null;
    }

    @Override
    public void saveLog(Log log) {
        log.setId(UuidTool.getUUID());
        log.setCreateTime(new Date());
        //注：如果项目路径存在中文，会出现找不到文件报错的情况
        log.setLocation(AddressUtil.getCityInfo(log.getIp()));
        logMapper.insert(log);
    }

    @SuppressWarnings("all")
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }
}

