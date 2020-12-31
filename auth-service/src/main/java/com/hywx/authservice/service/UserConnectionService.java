package com.hywx.authservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.authservice.dao.UserConnection;

import java.util.List;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-06 12:10
 **/
public interface UserConnectionService extends IService<UserConnection> {

    /**
     * 根据条件查询关联关系
     *
     * @param providerName   平台名称
     * @param providerUserId 平台用户ID
     * @return 关联关系
     */
    UserConnection selectByCondition(String providerName, String providerUserId);

    /**
     * 根据条件查询关联关系
     *
     * @param username 用户名
     * @return 关联关系
     */
    List<UserConnection> selectByCondition(String username);

    /**
     * 新增
     *
     * @param userConnection userConnection
     */
    void createUserConnection(UserConnection userConnection);

    /**
     * 删除
     *
     * @param username     username 用户名
     * @param providerName providerName 平台名称
     */
    void deleteByCondition(String username, String providerName);
}
