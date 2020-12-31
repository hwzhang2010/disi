package com.hywx.authservice.service.impl;


import com.hywx.authservice.bo.AuthServerUser;
import com.hywx.authservice.dao.Resource;
import com.hywx.authservice.dao.User;
import com.hywx.authservice.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-04 11:39
 **/
@Service
public class UserDetailServiceImpl  implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User systemUser = userMapper.findUserByUname(username);
        if (systemUser != null) {
            List<Resource> userPermissions = userMapper.getResourceListByUserId(systemUser.getId());
            String permissions = userPermissions.stream().map(Resource::getCode).collect(Collectors.joining(","));
            boolean notLocked = false;
            if (systemUser.getStatus()==1)
                notLocked = true;
                AuthServerUser authUser = new AuthServerUser(systemUser.getUsername(), systemUser.getPassword(), true, true, true, notLocked,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

            BeanUtils.copyProperties(systemUser,authUser);
            return authUser;
        } else {
            throw new UsernameNotFoundException("");
        }
    }
}
