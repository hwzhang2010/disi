package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.SortUtil;
import com.hywx.common.core.util.UuidTool;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.dao.Role;
import com.hywx.userservice.dao.RoleResource;
import com.hywx.userservice.mapper.RoleMapper;
import com.hywx.userservice.service.RoleResourceService;
import com.hywx.userservice.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author tangjing
 * @program: user-serice
 * @description: 角色表 service 接口实现类
 * @date 2020-02-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceService roleResourceService;

    @Override
    public List<Role> list() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getId);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Role> getListByPage(QueryRequest request, Role role) {
        Page<Role> page = new Page<>(request.getPageNo(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", CommonConstant.ORDER_ASC, true);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(role.getRoleName()))
            queryWrapper.lambda().like(Role::getRoleName, role.getRoleName());

        return roleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Role> findRolePage(QueryRequest request, Role role) {
        Page<Role> page = new Page<>(request.getPageNo(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", CommonConstant.ORDER_DESC, true);
        return this.baseMapper.findRolePage(page, role);
    }


    @Override
    @Transactional
    public int saveData(Role role) {
        role.setId(UuidTool.getUUID());
        role.setCreateTime(new Date());
        role.setCreateUser(SecurityUtil.getCurrentUser().getId());
        // 插入权限
        if (StringUtils.isNotBlank(role.getResourceIds())) {
            String[] menuIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(role.getResourceIds(), ",");
            setRoleMenus(role, menuIds);
        }
        return roleMapper.insert(role);
    }

    @Override
    @Transactional
    public int updateData(Role role) {
        role.setUpdateTime(new Date());
        role.setUpdateUser(SecurityUtil.getCurrentUser().getId());
        roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRoleId, role.getId()));
        if (StringUtils.isNotBlank(role.getResourceIds())) {
            String[] menuIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(role.getResourceIds(), ",");
            setRoleMenus(role, menuIds);
        }
        return baseMapper.updateById(role);
    }

    @Override
    public int deleteById(String id) {
        return roleMapper.deleteById(id);
    }

    @Override
    public int batchDelete(String[] ids) {
        List<String> list = Arrays.asList(ids);
        // 删除角色权限关联
        this.roleResourceService.deleteResourceByRoleId(ids);
        return baseMapper.deleteBatchIds(list);
    }

    private void setRoleMenus(Role role, String[] resourceIds) {
        List<RoleResource> roleResourceList = new ArrayList<>();
        Arrays.stream(resourceIds).forEach(resourceId -> {
            RoleResource roleResource = new RoleResource();
            if (StringUtils.isNotBlank(resourceId)) {
                roleResource.setResourceId(resourceId);
            }
            roleResource.setRoleId(role.getId());
            roleResourceList.add(roleResource);
        });
        roleResourceService.saveBatch(roleResourceList);
    }


    @Override
    public Role findByName(String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, roleName);
        return roleMapper.selectOne(queryWrapper);
    }
}

