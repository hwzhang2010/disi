package com.hywx.authservice.provider;

import com.hywx.authservice.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-07-21 16:14
 **/
@Component
public class CasAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        //cas provider
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        http.authenticationProvider(provider);
    }
}
