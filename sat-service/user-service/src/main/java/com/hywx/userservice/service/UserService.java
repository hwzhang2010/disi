package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.userservice.dao.User;
import com.hywx.userservice.vo.UserSearchVo;

import java.util.List;

/**
 * @program: user-serice
 * @description: UserService
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
public interface UserService extends IService<User> {


    List<User> list(UserSearchVo searchVo);

    IPage<User> getListByPage(QueryRequest request, UserSearchVo searchVo);

    int saveData(User user);

    int updateData(User user);

    int deleteById(String id);

    int batchDelete(String[] ids);

    void updateLoginTime(String username);

    User findByName(String username);

    void resetPassword(String[] usernames);

    int register(User user);
}