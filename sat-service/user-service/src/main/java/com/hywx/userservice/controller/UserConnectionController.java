package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.UserConnection;
import com.hywx.userservice.service.UserConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author lxy
 * @program: user-serice
 * @description:
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/UserConnection")
public class UserConnectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConnectionController.class);

    @Autowired
    private UserConnectionService userConnectionService;

    /**
     * @param pageNo   页码
     * @param pageSize 每页个数
     * @return Resp
     * @method getListByPage
     * @description 列表查询分页
     * @date: 2020-03-12
     * @author: lxy
     */
    @GetMapping("/getListByPage")
    @ControllerEndpoint(operation = "分页获取所有第三方用户登录信息", exceptionMessage = "分页获取所有第三方用户登录信息失败")
    public Resp getListByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (pageNo < 1 || pageSize < 1) {
            return Resp.getInstantiationError("分页查看第三方用户登录失败，分页页数或分页大小不合法", null, null);
        }
        try {
            QueryWrapper<UserConnection> userConnection = new QueryWrapper<UserConnection>();
            IPage<UserConnection> userConnectionList = userConnectionService.getListByPage(pageNo, pageSize, userConnection);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页获取所有第三方用户登录信息成功，用时" + (endTime - startTime) + "ms，" +
                    "参数信息：pageNo=" + pageNo + ",pageSize=" + pageSize +
                    "返回信息：userConnectionList=" + userConnectionList);
            return Resp.getInstantiationSuccess("分页获取所有第三方用户登录信息成功", Resp.LIST, userConnectionList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("分页查看第三方用户登录失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看第三方用户登录失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @param userConnection
     * @return Resp
     * @method save
     * @description 新增第三方用户登录信息
     * @date: 2020-03-12
     * @author: lxy
     */
    @PostMapping
    @ControllerEndpoint(operation = "新增第三方用户登录信息", exceptionMessage = "新增第三方用户登录信息失败")
    public Resp save(@RequestBody UserConnection userConnection) {
        long startTime = System.currentTimeMillis();
        if (null == userConnection) {
            return Resp.getInstantiationError("前端错误，参数为空", Resp.SINGLE, null);
        }
        try {
            userConnectionService.saveData(userConnection);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建第三方用户登录信息成功，用时" + (endTime - startTime) + "ms," +
                    "创建对象为：userConnection=" + userConnection.toString());
            return Resp.getInstantiationSuccess("创建第三方用户登录信息成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建第三方用户登录信息失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建第三方用户登录信息异常，原因：" + e.getMessage(), Resp.SINGLE, userConnection);
        }
    }

    /**
     * @param userConnection
     * @return Resp
     * @method update
     * @description 编辑第三方用户登录信息
     * @date: 2020-03-12
     * @author: lxy
     */
    @PutMapping
    @ControllerEndpoint(operation = "编辑第三方用户登录信息", exceptionMessage = "编辑第三方用户登录信息失败")
    public Resp update(@RequestBody UserConnection userConnection) {
        long startTime = System.currentTimeMillis();
        try {
            //更新
            if (userConnectionService.getByUserName(userConnection.getUserName()) == null) {
                long endTime = System.currentTimeMillis();
                LOGGER.error("修改第三方用户登录信息失败，原因：修改对象不存在，用时" + (endTime - startTime) + "ms，" +
                        "修改对象为：userConnection=" + userConnection.toString());
                return Resp.getInstantiationError("修改失败，原因：修改的第三方用户登录信息id不存在，", null, null);
            }
            userConnectionService.updateData(userConnection);
            long endTime = System.currentTimeMillis();
            LOGGER.info("修改第三方用户登录信息成功，用时" + (endTime - startTime) + "ms，" +
                    "修改第三方用户登录信息为：userConnection=" + userConnection.toString());
            return Resp.getInstantiationSuccess("修改第三方用户登录信息成功", null, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("修改第三方用户登录信息失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("修改第三方用户登录信息失败：" + e.getMessage(), null, null);
        }
    }

    /**
     * @param ids
     * @return Resp
     * @method batchDelete
     * @description 根据id批量删除第三方用户登录信息
     * @date: 2020-03-12
     * @author: lxy
     */
    @DeleteMapping("/{ids}")
    @ControllerEndpoint(operation = "根据id批量删除第三方用户登录信息", exceptionMessage = "根据id批量删除第三方用户登录信息失败")
    public Resp batchDelete(@PathVariable String ids) {
        long startTime = System.currentTimeMillis();
        try {
            String[] idstr = ids.split(StringPool.COMMA);
            userConnectionService.batchDelete(idstr);
            long endTime = System.currentTimeMillis();
            LOGGER.info("批量删除第三方用户登录信息成功,用时:" + (endTime - startTime) + "ms," +
                    "删除的id为=" + ids);
            return Resp.getInstantiationSuccess("批量删除第三方用户登录信息成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除第三方用户登录信息失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除第三方用户登录信息失败:" + e.getMessage(), Resp.LIST, null);
        }
    }

}
