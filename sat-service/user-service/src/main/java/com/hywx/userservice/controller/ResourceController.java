package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.bo.VueRouter;
import com.hywx.userservice.dao.Resource;
import com.hywx.userservice.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

/**
 * @program: user-serice
 * @description:
 * @author tangjing
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/Resource")
public class ResourceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    /**
     * @method  getListByPage
     * @description 列表查询
     * @date: 2020-03-05
     * @author: tangjing
     * @param pageNo
     * @param pageSize
     * @return Resp
     */
    @GetMapping("/getListByPage")
    @ControllerEndpoint(operation = "资源列表查询", exceptionMessage = "资源列表查询失败")
    public Resp getListByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if(pageNo < 1 || pageSize < 1){
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法" ,null, null);
        }
        try {
            QueryWrapper<Resource> resource=new QueryWrapper<Resource>();
            IPage<Resource> resourceList = resourceService.getListByPage(pageNo, pageSize,resource);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看", Resp.LIST, resourceList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("分页列举失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看失败" + e.getMessage(), Resp.LIST, null);
        }
    }

    /**
     * @method  save
     * @description 新增
     * @date: 2020-03-05
     * @author: tangjing
     * @param resource
     * @return Resp
     */
    @PostMapping
    @ControllerEndpoint(operation = "资源新增", exceptionMessage = "资源新增失败")
    public Resp save(@RequestBody Resource resource){
        long startTime = System.currentTimeMillis();
        if (null == resource) {
            return Resp.getInstantiationError("前端错误，参数为空", Resp.SINGLE, null);
        }
        try {
                resourceService.saveData(resource);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("创建成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建异常，原因：" + e.getMessage(), Resp.SINGLE, resource);
        }
    }

    /**
     * @method  update
     * @description 编辑
     * @date: 2020-03-05
     * @author: tangjing
     * @param resource
     * @return Resp
     */
    @PutMapping
    @ControllerEndpoint(operation = "资源编辑", exceptionMessage = "资源编辑失败")
    public Resp update(@RequestBody  Resource resource){
        long startTime =  System.currentTimeMillis();
        try {
            //更新
                if(resourceService.getById(resource.getId()) == null){
                    long endTime = System.currentTimeMillis();
                    LOGGER.error("修改失败，原因：修改不存在，用时" + (endTime - startTime) + "ms");
                    return Resp.getInstantiationError("修改失败，原因：修改的id不存在，", null, null);
                }
                resourceService.updateData(resource);
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
     * @date: 2020-03-05
     * @author: tangjing
     * @param  resourceIds
     * @return Resp
     */
    @DeleteMapping("/{resourceIds}")
    @ControllerEndpoint(operation = "根据id批量删除资源", exceptionMessage = "根据id批量删除资源失败")
    public Resp batchDelete(@PathVariable String resourceIds){
        long startTime = System.currentTimeMillis();
        try {
                String[] ids = resourceIds.split(StringPool.COMMA);
                resourceService.batchDelete(ids);
                long endTime = System.currentTimeMillis();
                LOGGER.info("批量删除成功,用时:" + (endTime-startTime) + "ms");
                return Resp.getInstantiationSuccess("批量删除成功", Resp.LIST, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("批量删除失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("批量删除失败:"+e.getMessage(), Resp.LIST, null);
        }
    }

    @GetMapping
    @ControllerEndpoint(operation = "获取资源信息", exceptionMessage = "获取资源信息失败")
    public Resp resourceList(Resource resource) {
        long startTime = System.currentTimeMillis();
        try {
             Object obj = this.resourceService.getResourceTree(resource);
            long endTime = System.currentTimeMillis();
            LOGGER.info("获取资源信息成功,用时:" + (endTime-startTime) + "ms");
            return Resp.getInstantiationSuccess("获取资源信息成功", Resp.LIST, obj);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("获取资源信息失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("获取资源信息失败:"+e.getMessage(), Resp.LIST, null);
        }
    }
    @GetMapping("/getPermissions")
    @ControllerEndpoint(operation = "根据用户名获取资源信息", exceptionMessage = "根据用户名获取资源信息失败")
    public Resp getUserRouters(@NotBlank(message = "{required}")String username,@NotBlank(message = "{required}")String parentId) {
        long startTime = System.currentTimeMillis();
        try {
            Map<String, Object> result = new HashMap<>();
            List<VueRouter<Resource>> userRouters = resourceService.getUserRouters(username,parentId);
            String userPermissions = resourceService.findUserPermissions(username,parentId);
            String[] permissionArray = new String[0];
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(userPermissions)) {
                permissionArray = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, ",");
            }
            result.put("routes", userRouters);
            result.put("permissions", permissionArray);
            long endTime = System.currentTimeMillis();
            LOGGER.info("获取资源信息成功,用时:" + (endTime-startTime) + "ms");
            return Resp.getInstantiationSuccess("获取资源信息成功", Resp.LIST, result);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("获取资源信息失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("获取资源信息失败:"+e.getMessage(), Resp.LIST, null);
        }
    }

    @GetMapping("/getUserSystemPermission")
    @ControllerEndpoint(operation = "获取用户系统权限信息", exceptionMessage = "获取用户系统权限失败")
    public Resp getUserSystemPermission(@NotBlank(message = "{required}")String username) {
        long startTime = System.currentTimeMillis();
        try {
            Map<String, Object> result = new HashMap<>();
            String userPermissions = resourceService.findUserSystemPermissions(username,"0");
            String[] permissionArray = new String[0];
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(userPermissions)) {
                permissionArray = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, ",");
            }
            result.put("permissions", permissionArray);
            long endTime = System.currentTimeMillis();
            LOGGER.info("获取用户系统权限成功,用时:" + (endTime-startTime) + "ms");
            return Resp.getInstantiationSuccess("获取用户系统权限成功", Resp.LIST, result);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("获取用户系统权限失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("获取用户系统权限失败:"+e.getMessage(), Resp.LIST, null);
        }
    }

}
