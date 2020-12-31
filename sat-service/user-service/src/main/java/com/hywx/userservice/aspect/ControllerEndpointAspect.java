package com.hywx.userservice.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hywx.common.core.util.CommonUtil;
import com.hywx.common.core.util.results.Resp;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.feign.FeighForUserServices;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: user-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-31 10:27
 **/
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerEndpointAspect extends BaseAspectSupport {

    private final ObjectMapper objectMapper;
    private final String EXPORT_EXCEL_STRING = "导出Excel";
    @Autowired
    private  final FeighForUserServices feighForUserServices;

    @Pointcut("@annotation(com.hywx.userservice.aspect.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Exception {
        Resp result;
        ControllerEndpoint annotation = null;
        try {
            //得到需切入接口操作后的返回信息
            result =(Resp) point.proceed();
            Method targetMethod = resolveMethod(point);
            annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
            String operation;
            //判断接口是否成功 200为成功，取成功日志，其他状态码为不成功，取err日志
            if (result.getCode() == 200) {
                operation = annotation.operation();
            } else {
                operation = annotation.exceptionMessage();
            }
            long start = System.currentTimeMillis();
            if (StringUtils.isNotBlank(operation)) {
                String username = SecurityUtil.getCurrentUsername();
                String ip = CommonUtil.getHttpServletRequestIpAddress();
               // 存入log日志中 -- 本服务调用方式
               // 微服务调用方式
                Log log = creatLog(point, targetMethod, ip, operation, username, start);
                Resp resp = feighForUserServices.save(log);
                if (operation.contains(EXPORT_EXCEL_STRING)) {
                    return null;
                }
            }
            return result;
        } catch (Throwable throwable) {
            String exceptionMessage = annotation.exceptionMessage();
            String message = throwable.getMessage();
            String error = CommonUtil.containChinese(message) ? exceptionMessage + "，" + message : exceptionMessage;
            throw new Exception(error);
        }
    }

    private Log creatLog(ProceedingJoinPoint point, Method method, String ip, String operation, String username, long start) {
        Log log = new Log();
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
//        log.setCreateTime(new Date());
//        //注：如果项目路径存在中文，会出现找不到文件报错的情况
//        log.setLocation(AddressUtil.getCityInfo(ip));
        return log;
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



