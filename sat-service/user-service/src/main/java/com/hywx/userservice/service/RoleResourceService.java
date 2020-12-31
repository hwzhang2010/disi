package com.hywx.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.userservice.dao.RoleResource;

/**
 * @program: user-serice
 * @description:  service 接口
 * @author tangjing
 * @date 2020-03-13
 */
public interface RoleResourceService extends IService<RoleResource> {


     void deleteResourceByRoleId(String[] userIds) ;
}