package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.userservice.dao.RoleResource;
import com.hywx.userservice.mapper.RoleResourceMapper;
import com.hywx.userservice.service.RoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @program: user-serice
 * @description:  service 接口实现类
 * @author tangjing
 * @date 2020-03-13
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

    @Override
    @Transactional
    public void deleteResourceByRoleId(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        this.baseMapper.delete(new LambdaQueryWrapper<RoleResource>().in(RoleResource::getRoleId, list));
    }
}

