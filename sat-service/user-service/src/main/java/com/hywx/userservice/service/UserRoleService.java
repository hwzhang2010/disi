package com.hywx.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.userservice.dao.UserRole;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-13 09:59
 **/
public interface UserRoleService extends IService<UserRole> {

     void deleteUserRolesByUserId(String[] userIds);
}
