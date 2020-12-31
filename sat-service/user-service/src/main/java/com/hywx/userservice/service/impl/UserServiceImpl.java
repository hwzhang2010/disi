package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.SortUtil;
import com.hywx.common.core.util.UuidTool;
import com.hywx.common.security.configure.Md5PasswordEncoder;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.dao.User;
import com.hywx.userservice.dao.UserRole;
import com.hywx.userservice.mapper.UserMapper;
import com.hywx.userservice.service.UserRoleService;
import com.hywx.userservice.service.UserService;
import com.hywx.userservice.vo.UserSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: user-serice
 * @description: 服务实现类
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleService userRoleService;
//        @Autowired
//        private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //PasswordEncoder 对于一个相同的密码，每次加密出来的加密串都不同：
        // return new BCryptPasswordEncoder();
        return new Md5PasswordEncoder();
    }

    @Override
    public List<User> list(UserSearchVo searchVo) {
        return userMapper.getUserListPage(searchVo);
    }

    @Override
    public IPage<User> getListByPage(QueryRequest request, UserSearchVo searchVo) {
        Page<User> page = new Page<>(request.getPageNo(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", CommonConstant.ORDER_ASC, true);
        IPage<User> list = userMapper.getUserListPage(page, searchVo);
        return list;
    }

    @Override
    @Transactional
    public int saveData(User user) {
        user.setId(UuidTool.getUUID());
        // 创建默认密码
        user.setPassword(passwordEncoder().encode(StringUtils.isEmpty(user.getPassword())? User.DEFAULT_PASSWORD:user.getPassword()));
        user.setCreateTime(new Date());
        user.setCreateUser(SecurityUtil.getCurrentUser().getId());
        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);
        return userMapper.insert(user);
    }

    @Override
    public int register(User user) {
        user.setId(UuidTool.getUUID());
        user.setCreateTime(new Date());
        user.setCreateUser(user.getId());
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public int updateData(User user) {
        user.setUpdateTime(new Date());
        user.setUpdateUser(SecurityUtil.getCurrentUser().getId());
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);
        return  userMapper.updateById(user);
    }

    @Override
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int batchDelete(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.userRoleService.deleteUserRolesByUserId(ids);
        return userMapper.deleteBatchIds(list);
    }

    @Override
    public void updateLoginTime(String username) {
        User user = new User();
        user.setLastLoginTime(new Date());
        userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public User findByName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    private void setUserRoles(User user, String[] roles) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roles).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        userRoleService.saveBatch(userRoles);
    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) {
        User params = new User();
        params.setPassword(passwordEncoder().encode(User.DEFAULT_PASSWORD));

        List<String> list = Arrays.asList(usernames);
        userMapper.update(params, new LambdaQueryWrapper<User>().in(User::getUsername, list));

    }
}

