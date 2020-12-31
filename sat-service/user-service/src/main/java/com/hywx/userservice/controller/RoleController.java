package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.Role;
import com.hywx.userservice.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @program: user-serice
 * @description:
 * @author tangjing
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/Role")
public class RoleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * @method  getRoleList
     * @description 列表查询
     * @date: 2020-02-24
     * @author: tangjing
     * @return Resp
     */
    @GetMapping("/getRoleList")
    @ControllerEndpoint(operation = "获取所有角色", exceptionMessage = "获取所有角色失败")
    public Resp getRoleList(){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        try {
            List<Role> roleList = roleService.list();
            long endTime = System.currentTimeMillis();
            LOGGER.info("列表查询成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("列表查询成功", Resp.LIST, roleList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("列表查询失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("列表查询失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    @GetMapping
    @ControllerEndpoint(operation = "获取角色列表分页", exceptionMessage = "获取角色列表分页失败")
    public Resp getListByPage(QueryRequest queryRequest, Role role){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (queryRequest.getPageNo() < 1 || queryRequest.getPageNo() < 1) {
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法", null, null);
        }
        try {
            IPage<Role> roleList = roleService.findRolePage(queryRequest, role);
            long endTime = System.currentTimeMillis();
            LOGGER.info("列表查询成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("列表查询成功", Resp.LIST, roleList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("列表查询失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("列表查询失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @method  save
     * @description 新增
     * @date: 2020-02-24
     * @author: tangjing
     * @param role
     * @return Resp
     */
    @PostMapping
    @ControllerEndpoint(operation = "新增角色", exceptionMessage = "新增角色失败")
    public Resp save(@RequestBody Role role){
        long startTime = System.currentTimeMillis();
        if (null == role) {
            return Resp.getInstantiationError("前端错误，参数为空", Resp.SINGLE, null);
        }
        try {
            roleService.saveData(role);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("创建成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建异常，原因：" + e.getMessage(), Resp.SINGLE, role);
        }
    }

    /**
     * @method  update
     * @description 编辑
     * @date: 2020-02-24
     * @author: tangjing
     * @param role
     * @return Resp
     */
    @PutMapping
//    @PreAuthorize("hasAuthority('role:update')")
    @ControllerEndpoint(operation = "编辑角色", exceptionMessage = "编辑角色失败")
    public Resp update(@RequestBody  Role role){
        long startTime =  System.currentTimeMillis();
        try {
            //更新
                if(roleService.getById(role.getId()) == null){
                    long endTime = System.currentTimeMillis();
                    LOGGER.error("修改失败，原因：修改不存在，用时" + (endTime - startTime) + "ms");
                    return Resp.getInstantiationError("修改失败，原因：修改的id不存在，", null, null);
                }
                roleService.updateData(role);
                long endTime =  System.currentTimeMillis();
                LOGGER.info("修改成功，用时" + (endTime - startTime) + "ms");
                return Resp.getInstantiationSuccess("修改成功", null, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("修改失败，原因："+ e.getMessage() +"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("修改失败：" + e.getMessage(), null, null);
        }
    }


    /**
     * @method  batchDelete
     * @description 根据id批量删除
     * @date: 2020-02-24
     * @author: tangjing
     * @param  roleIds
     * @return Resp
     */
    @DeleteMapping("/{roleIds}")
//    @PreAuthorize("hasAuthority('role:delete')")
    @ControllerEndpoint(operation = "删除角色", exceptionMessage = "删除角色失败")
    public Resp batchDelete(@PathVariable String roleIds){
        long startTime = System.currentTimeMillis();
        try {
                String[] ids = roleIds.split(StringPool.COMMA);
                // 检查角色下是否有用户

                roleService.batchDelete(ids);
                long endTime = System.currentTimeMillis();
                LOGGER.info("批量删除成功,用时:" + (endTime-startTime) + "ms");
                return Resp.getInstantiationSuccess("批量删除成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除失败:"+e.getMessage(), Resp.LIST, null);
        }
    }


    /**
     * 检查用户用户名是否存在
     *
     * @param username
     * @return
     */
    @GetMapping("/check/{username}")
    @ControllerEndpoint(operation = "查询用户名是否存在", exceptionMessage = "查询用户名是否存在失败")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.roleService.findByName(username) == null;
    }
}
