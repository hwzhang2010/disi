package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.userservice.dao.Role;

import java.util.List;

/**
 * @program: user-serice
 * @description: 角色表 service 接口
 * @author tangjing
 * @date 2020-02-24
 */
public interface RoleService extends IService<Role> {


    List<Role> list();

    IPage<Role> getListByPage(QueryRequest request, Role role);

    IPage<Role> findRolePage(QueryRequest request, Role role);

    int saveData(Role role);

    int updateData(Role role);

    int deleteById(String id);

    int batchDelete(String[] ids);

    Role findByName(String roleName);
}