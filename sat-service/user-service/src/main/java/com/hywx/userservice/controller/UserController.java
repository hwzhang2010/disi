package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.Resp;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.LoginLog;
import com.hywx.userservice.dao.User;
import com.hywx.userservice.feign.FeighForUserServices;
import com.hywx.userservice.service.LoginLogService;
import com.hywx.userservice.service.UserService;
import com.hywx.userservice.vo.UserSearchVo;
import com.hywx.userservice.vo.UserVo;
import com.wuwenze.poi.ExcelKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @program: user-serice
 * @description: 用户信息接口类
 * @author: tangjing
 * @create: 2020-02-24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    protected FeighForUserServices feighForUserServices;

    @Autowired
    protected LoginLogService loginLogService;


    @GetMapping("getCuuser")
    public Principal currentUser(Principal principal) {
        // CommonUtil.getCurrentUser();
        return principal;
    }

    /**
     * @param request
     * @method loginSuccess
     * @description 登录成功后回调函数
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @GetMapping("/success")
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        // update last login time
        this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveData(loginLog);
    }


    @GetMapping("index")
    @ControllerEndpoint(operation = "获取访问统计查询", exceptionMessage = "获取访问统计失败")
    public Resp index() {
        long startTime = System.currentTimeMillis();
        try {
            Map<String, Object> data = new HashMap<>();
            // 获取系统访问记录
            data.put("totalVisitCount", 1);
            data.put("todayVisitCount", 2);
            data.put("todayIp", 2);
            data.put("lastTenVisitCount", 2);
            User param = new User();
            param.setUsername(SecurityUtil.getCurrentUsername());
            data.put("lastTenUserVisitCount", 3);
            long endTime = System.currentTimeMillis();
            LOGGER.info("获取访问统计成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("获取访问统计成功", Resp.LIST, data);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("获取访问统计失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("获取访问统计失败" + e.getMessage(), Resp.LIST, null);
        }
    }


    /**
     * @param searchVo
     * @return Resp
     * @method getAllUser
     * @description 用户列表查询
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @PostMapping("/getAllUser")
//    @ControllerEndpoint(operation = "用户列表查询", exceptionMessage = "用户列表查询失败")
    public Resp getAllUser(@RequestBody UserSearchVo searchVo) {
        //查询列表数据
        long startTime = System.currentTimeMillis();
        try {
            List<User> userList = userService.list(searchVo);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看成功", Resp.LIST, userList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("分页列举失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看失败" + e.getMessage(), Resp.LIST, null);
        }
    }


    /**
     * @param queryRequest
     * @param searchVo
     * @return Resp
     * @method getListByPage
     * @description 用户列表查询
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @GetMapping
    //@ControllerEndpoint(operation = "用户列表查询", exceptionMessage = "用户列表查询失败")
    public Resp getListByPage(QueryRequest queryRequest, UserSearchVo searchVo) {
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (queryRequest.getPageNo() < 1 || queryRequest.getPageNo() < 1) {
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法", null, null);
        }
        try {
            IPage<User> userList = userService.getListByPage(queryRequest, searchVo);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看成功", Resp.LIST, userList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("分页列举失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @param user
     * @return Resp
     * @method save
     * @description 用户新增
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @PostMapping
    @ControllerEndpoint(operation = "用户新增", exceptionMessage = "用户新增失败")
    public Resp save(@RequestBody User user) {
        long startTime = System.currentTimeMillis();
        if (null == user) {
            return Resp.getInstantiationError("前端错误，角色为空", Resp.SINGLE, null);
        }
        try {
            userService.saveData(user);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("创建成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建异常，原因：" + e.getMessage(), Resp.SINGLE, user);
        }
    }

    /**
     * @param user
     * @return Resp
     * @method update
     * @description 用户编辑
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @PutMapping
//    @PreAuthorize("hasAuthority('user:update')")
    @ControllerEndpoint(operation = "用户编辑", exceptionMessage = "用户编辑失败")
    public Resp update(@RequestBody User user) {
        long startTime = System.currentTimeMillis();
        try {
            //更新
            if (userService.getById(user.getId()) == null) {
                long endTime = System.currentTimeMillis();
                LOGGER.error("修改失败，原因：修改不存在，用时" + (endTime - startTime) + "ms");
                return Resp.getInstantiationError("修改失败，原因：修改的id不存在，", null, null);
            }
            userService.updateData(user);
            long endTime = System.currentTimeMillis();
            LOGGER.info("修改成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("修改成功", null, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("修改失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("修改失败：" + e.getMessage(), null, null);
        }
    }

    /**
     * @param userIds
     * @return Resp
     * @method batchDelete
     * @description 用户根据id批量删除
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @DeleteMapping("/{userIds}")
//    @PreAuthorize("hasAuthority('user:delete')")
    @ControllerEndpoint(operation = "用户根据id批量删除", exceptionMessage = "用户根据id批量删除失败")
    public Resp batchDelete(@PathVariable String userIds) {
        long startTime = System.currentTimeMillis();
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            userService.batchDelete(ids);
            long endTime = System.currentTimeMillis();
            LOGGER.info("批量删除成功,用时:" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("批量删除成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除失败:" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * 检查用户用户名是否存在
     *
     * @param username
     * @return
     */
    @GetMapping("check/{username}")
    @ControllerEndpoint(operation = "查询用户名是否存在", exceptionMessage = "查询用户名是否存在失败")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    /**
     * 用户导出
     * 注意：ControllerEndpoint中信息一定要带有“导出Excel”，依照字段串比较进行返回值判断
     * @param searchVo
     * @param response
     */
    @PostMapping("/excel")
//    @PreAuthorize("hasAuthority('user:export')")
    @ControllerEndpoint(operation = "导出Excel用户数据", exceptionMessage = "导出Excel用户数据失败")
    public void export(@RequestBody UserSearchVo searchVo, HttpServletResponse response) {
        List<User> users = userService.list(searchVo);
        ExcelKit.$Export(User.class, response).downXlsx(users, false);
    }

    @PutMapping("password/reset")
//    @PreAuthorize("hasAuthority('user:reset')")
    @ControllerEndpoint(operation = "重置用户密码",exceptionMessage = "重置用户密码失败")
    public Resp resetPassword(@NotBlank(message = "{required}") String usernames) {
        long startTime = System.currentTimeMillis();
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            userService.resetPassword(usernameArr);
            long endTime = System.currentTimeMillis();
            LOGGER.info("重置用户密码成功,用时:" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("重置用户密码成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("重置用户密码失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("重置用户密码失败:" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @param uservo
     * @return Resp
     * @method save
     * @description 用户注册
     * @date: 2020-02-24 09:49
     * @author: tangjing
     */
    @PostMapping("register")
    @ControllerEndpoint(operation = "用户注册", exceptionMessage = "用户注册失败")
    public Resp register(@RequestBody UserVo uservo) {
        long startTime = System.currentTimeMillis();
        if (null == uservo) {
            return Resp.getInstantiationError("前端错误，信息为空", Resp.SINGLE, null);
        }
        try {
            User user=new User();
            BeanUtils.copyProperties(user,uservo);
            userService.register(user);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("创建成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建异常，原因：" + e.getMessage(), Resp.SINGLE, null);
        }
    }

    /** @Author: LXYuuuuu
     * @Description: 根据id查询用户信息
     * @Date 2020/5/13 14:00
     * @Param userId
     * @return com.hywx.common.core.util.results.Resp
     * @throws:
     **/
    @GetMapping("/getUserByUserId")
    public Resp getUserByUserId(String userId) {
        //查询列表数据
        long startTime = System.currentTimeMillis();
        try {
            User user = userService.getById(userId);
            long endTime = System.currentTimeMillis();
            LOGGER.info("根据id查询用户信息成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("根据id查询用户信息成功", Resp.LIST, user);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("根据id查询用户信息失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("根据id查询用户信息失败" + e.getMessage(), Resp.LIST, null);
        }
    }
}
