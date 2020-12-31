package com.hywx.userservice.feign;

import com.hywx.common.core.util.results.ContextUtils;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.feign.impl.FeighForUserServicesHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: user-service
 * @description: feign调用
 * @author: tangjing
 * @create: 2020-02-24 10:33
 **/
//@FeignClient(name= "user-test",path= "/user-service/v1" ,fallback= FeighForUserServicesHystrix.class)
@FeignClient(name= "user-service",fallback= FeighForUserServicesHystrix.class)
public interface FeighForUserServices {
    /**
     * @param :@param      pageNo
     * @param :@param      pageSize
     * @param :@return(形参)
     * @return :ContextUtils(返回类型)
     * @throws :(异常类型)
     * @method :getListByPage(方法名)
     * @author :cz(作者)
     * @description :添加页面
     * @time :2019年04月13日(最后修改时间)
     */
    @GetMapping(value ="/user")
    public ContextUtils getListByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    @PostMapping(value = "/Log")
    Resp save(@RequestBody Log log);
}
