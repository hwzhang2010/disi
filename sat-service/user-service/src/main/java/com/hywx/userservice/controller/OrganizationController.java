package com.hywx.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hywx.common.core.util.results.Resp;
import com.hywx.userservice.aspect.ControllerEndpoint;
import com.hywx.userservice.dao.Organization;
import com.hywx.userservice.service.OrganizationService;
import com.hywx.userservice.vo.OrganizationSearchVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: user-serice
 * @description:
 * @author tangjing
 * @date ${cfg.dateTime}
 */
@RestController
@RequestMapping("/Organization")
public class OrganizationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/getOrganizationTree")
    @ControllerEndpoint(operation = "获取组织树", exceptionMessage = "获取组织树失败")
    public Resp getOrganizationTree(OrganizationSearchVo searchVo) {
        long startTime = System.currentTimeMillis();
        try {
            Object organizationList = organizationService.getOrganizationTree(searchVo);
            long endTime = System.currentTimeMillis();
            LOGGER.info("获取组织成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("获取组织成功", Resp.LIST, organizationList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("获取组织失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("获取组织失败" + e.getMessage(), Resp.LIST, null);
        }
    }

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
    @ControllerEndpoint(operation = "组织树列表查询", exceptionMessage = "组织树列表查询失败")
    public Resp getListByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if(pageNo < 1 || pageSize < 1){
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法" ,null, null);
        }
        try {
            QueryWrapper<Organization> organization=new QueryWrapper<Organization>();
            IPage<Organization> organizationList = organizationService.getListByPage(pageNo, pageSize,organization);
            long endTime = System.currentTimeMillis();
            LOGGER.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看", Resp.LIST, organizationList);
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
     * @param organization
     * @return Resp
     */
    @PostMapping
    @ControllerEndpoint(operation = "新增组织树", exceptionMessage = "新增组织树失败")
    public Resp save(@RequestBody Organization organization){
        long startTime = System.currentTimeMillis();
        if (null == organization) {
            return Resp.getInstantiationError("前端错误，参数为空", Resp.SINGLE, null);
        }
        try {
                organizationService.saveData(organization);
            long endTime = System.currentTimeMillis();
            LOGGER.info("创建成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("创建成功", Resp.SINGLE, null);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            LOGGER.error("创建失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("创建异常，原因：" + e.getMessage(), Resp.SINGLE, organization);
        }
    }

    /**
     * @method  update
     * @description 编辑
     * @date: 2020-03-05
     * @author: tangjing
     * @param organization
     * @return Resp
     */
    @PutMapping
//    @PreAuthorize("hasAuthority('user:update')")
    @ControllerEndpoint(operation = "编辑组织树", exceptionMessage = "编辑组织树失败")
    public Resp update(@RequestBody  Organization organization){
        long startTime =  System.currentTimeMillis();
        try {
            //更新
                if(organizationService.getById(organization.getId()) == null){
                    long endTime = System.currentTimeMillis();
                    LOGGER.error("修改失败，原因：修改不存在，用时" + (endTime - startTime) + "ms");
                    return Resp.getInstantiationError("修改失败，原因：修改的id不存在，", null, null);
                }
                organizationService.updateData(organization);
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
     * @param  ids
     * @return Resp
     */
    @DeleteMapping("/{ids}")
    @ControllerEndpoint(operation = "根据id批量删除组织树", exceptionMessage = "根据id批量删除组织树失败")
    public Resp batchDelete(@PathVariable String ids){
        long startTime = System.currentTimeMillis();
        try {
               String[] idstr = ids.split(StringPool.COMMA);
                organizationService.batchDelete(idstr);
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
