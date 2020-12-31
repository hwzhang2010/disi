package com.hywx.zuulservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.zuulservice.dao.User;

import java.util.List;

/**
 * @program: user-serice
 * @description: UserService
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
public interface UserService extends IService<User> {


    User findByName(String username);


}