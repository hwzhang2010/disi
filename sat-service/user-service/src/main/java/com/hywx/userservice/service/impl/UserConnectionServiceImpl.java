package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.userservice.dao.UserConnection;
import com.hywx.userservice.mapper.UserConnectionMapper;
import com.hywx.userservice.service.UserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author lxy
 * @program: user-serice
 * @description: 第三方用户登录 service 接口实现类
 * @date 2020-03-12
 */
@Service
public class UserConnectionServiceImpl extends ServiceImpl<UserConnectionMapper, UserConnection> implements UserConnectionService {

    @Autowired
    private UserConnectionMapper userConnectionMapper;


    @Override
    public List<UserConnection> list(QueryWrapper<UserConnection> userConnection) {
        return userConnectionMapper.selectList(userConnection);
    }

    @Override
    public IPage<UserConnection> getListByPage(int pageNo, int pageSize, QueryWrapper<UserConnection> userConnection) {
        IPage<UserConnection> list = userConnectionMapper.selectPage(new Page<>(pageNo, pageSize), userConnection);
        return list;
    }


    @Override
    public int saveData(UserConnection userConnection) {
        return userConnectionMapper.insert(userConnection);
    }

    @Override
    public int updateData(UserConnection userConnection) {
        return userConnectionMapper.updateByUserName(userConnection);
    }

    @Override
    public int deleteById(String id) {
        return userConnectionMapper.deleteById(id);
    }

    @Override
    public int batchDelete(String[] ids) {
        List<String> list = Arrays.asList(ids);
        int result = 0;
        for (String username : list) {
            result = userConnectionMapper.deleteByUserName(username);
        }
        return result;
    }

    @Override
    public UserConnection getByUserName(String userName) {
        QueryWrapper<UserConnection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        return userConnectionMapper.selectOne(queryWrapper);
    }
}

