package com.hywx.authservice.provider;

import com.hywx.authservice.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-07-21 16:15
 **/
public class CasAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        CasAuthenticationToken authenticationToken = (CasAuthenticationToken) authentication;
        String userName = (String) authenticationToken.getPrincipal();
        UserDetails user = userDetailsService.loadUserByUsername(userName);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户名不存在");
        }
        CasAuthenticationToken authenticationResult = new CasAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CasAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserDetailsService(UserDetailServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
