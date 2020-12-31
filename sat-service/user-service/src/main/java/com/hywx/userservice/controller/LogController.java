package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.Log;
import com.hywx.userservice.feign.FeighForUserServices;
import com.hywx.userservice.service.LogService;
import com.hywx.userservice.vo.LogSearchVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;


/**
 * @program: user-serice
 * @description:
 * @author tangjing
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/Log")
public class LogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;
    @Autowired
    protected FeighForUserServices feighForUserServices;

    /**
     * @method  getListByPage
     * @description 列表查询
     * @date: 2020-03-06
     * @author: tangjing
     * @param queryRequest
     * @param searchVo
     * @return Resp
     */
    @GetMapping
    @ControllerEndpoint(operation = "日志列表查询", exceptionMessage = "日志列表查询失败")
    public Resp getListByPage(QueryRequest queryRequest, LogSearchVo searchVo){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (queryRequest.getPageNo() < 1 || queryRequest.getPageNo() < 1) {
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法", null, null);
        }
        try {

            IPage<Log> logList = logService.getListByPage(queryRequest,searchVo);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看", Resp.LIST, logList);
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
    @DeleteMapping("{ids}")
//    @PreAuthorize("hasAuthority('log:delete')")
    @ControllerEndpoint(operation = "根据id批量删除日志", exceptionMessage = "根据id批量删除日志失败")
    public Resp batchDelete(@NotBlank(message = "{required}") @PathVariable String ids){
        long startTime = System.currentTimeMillis();
        try {
                String[] logIds = ids.split(StringPool.COMMA);
                logService.batchDelete(logIds);
                long endTime = System.currentTimeMillis();
                LOGGER.info("批量删除成功,用时:" + (endTime-startTime) + "ms");
                return Resp.getInstantiationSuccess("批量删除成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除失败:"+e.getMessage(), Resp.LIST, null);
        }
    }

    @PostMapping
    public Resp saveLog(@RequestBody Log log){
        long startTime = System.currentTimeMillis();
        try {
            logService.saveLog(log);
            long endTime = System.currentTimeMillis();
            LOGGER.info("feign方式插入操作日志成功,用时:" + (endTime-startTime) + "ms");
            return Resp.getInstantiationSuccess("feign方式插入操作日志成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("feign方式插入操作日志失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("feign方式插入操作日志失败:"+e.getMessage(), Resp.LIST, null);

        }
    }
}
