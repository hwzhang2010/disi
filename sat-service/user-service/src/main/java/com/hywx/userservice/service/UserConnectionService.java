package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.userservice.dao.UserConnection;

import java.util.List;

/**
 * @program: user-serice
 * @description: 第三方用户登录 service 接口
 * @author lxy
 * @date 2020-03-12
 */
public interface UserConnectionService extends IService<UserConnection> {


    List<UserConnection> list(QueryWrapper<UserConnection> userConnection);

    IPage<UserConnection> getListByPage(int pageNo, int pageSize, QueryWrapper<UserConnection> userConnection);

    int saveData(UserConnection userConnection);

    int updateData(UserConnection userConnection);

    int deleteById(String id);

    int batchDelete(String[] ids);

    /** @Author: LXYuuuuuu
     * @Description: 根据系统用户名查询用户
     * @Date 2020/3/14 14:43
     * @Param userName
     * @return com.hywx.userservice.dao.UserConnection
     * @throws:
     **/
    UserConnection getByUserName(String userName);
}