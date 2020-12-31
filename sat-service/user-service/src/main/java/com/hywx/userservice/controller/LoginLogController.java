package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.LoginLog;
import com.hywx.userservice.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: user-serice
 * @description:
 * @author tangjing
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/LoginLog")
public class LoginLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogController.class);

    @Autowired
    private LoginLogService loginLogService;

    /**
     * @method  getListByPage
     * @description 列表查询
     * @date: 2020-03-06
     * @author: tangjing
     * @param queryRequest
     * @param loginLog
     * @return Resp
     */
    @GetMapping
    @ControllerEndpoint(operation = "登录日志列表查询", exceptionMessage = "登录日志列表查询失败")
    public Resp getListByPage(QueryRequest queryRequest, LoginLog loginLog){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (queryRequest.getPageNo() < 1 || queryRequest.getPageNo() < 1) {
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法", null, null);
        }
        try {
            IPage<LoginLog> loginLogList = loginLogService.getListByPage(queryRequest,loginLog);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看", Resp.LIST, loginLogList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("分页列举失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @method  batchDelete
     * @description 根据id批量删除
     * @date: 2020-03-06
     * @author: tangjing
     * @param  ids
     * @return Resp
     */
    @DeleteMapping
    @ControllerEndpoint(operation = "根据id批量删除登录日志", exceptionMessage = "根据id批量删除登录日志失败")
    public Resp batchDelete(@RequestParam("ids") List<String> ids){
        long startTime = System.currentTimeMillis();
        try {
                loginLogService.batchDelete(ids);
                long endTime = System.currentTimeMillis();
                LOGGER.info("批量删除成功,用时:" + (endTime-startTime) + "ms");
                return Resp.getInstantiationSuccess("批量删除成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除失败:"+e.getMessage(), Resp.LIST, null);
        }
    }

}
