package com.hywx.authservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.authservice.dao.UserConnection;
import com.hywx.authservice.mapper.UserConnectionMapper;
import com.hywx.authservice.service.UserConnectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-06 12:09
 **/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserConnectionServiceImpl extends ServiceImpl<UserConnectionMapper, UserConnection> implements UserConnectionService {

    @Override
    public UserConnection selectByCondition(String providerName, String providerUserId) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getProviderName, providerName)
                .eq(UserConnection::getProviderUserId, providerUserId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserConnection> selectByCondition(String username) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getUserName, username);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createUserConnection(UserConnection userConnection) {
        this.baseMapper.insert(userConnection);
    }

    @Override
    @Transactional
    public void deleteByCondition(String username, String providerName) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getUserName, username);
        queryWrapper.eq(UserConnection::getProviderName, providerName);
        this.remove(queryWrapper);
    }

}

