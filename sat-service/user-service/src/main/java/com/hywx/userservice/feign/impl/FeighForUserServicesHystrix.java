package com.hywx.userservice.feign.impl;

import com.hywx.common.core.util.results.ContextUtils;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.feign.FeighForUserServices;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: user-service
 * @description: 服务回调
 * @author: tangjing
 * @create: 2020-02-24 10:43
 **/
@Service
public class FeighForUserServicesHystrix implements FeighForUserServices{

    private String SERVICE_INFO = "用户微服务";
    private String ERROR_INFO = "服务异常，请重试";

    @Override
    public ContextUtils getListByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        return ContextUtils.getInstantiationError(SERVICE_INFO + ERROR_INFO, String.class, SERVICE_INFO + ERROR_INFO);
    }

    @Override
    public Resp save(@RequestBody Log log) {
        return Resp.getInstantiationError(SERVICE_INFO + ERROR_INFO, "", SERVICE_INFO + ERROR_INFO);
    }

}
