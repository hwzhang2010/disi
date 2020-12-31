package com.hywx.authservice.config;

import com.hywx.authservice.filter.ValidateCodeFilter;
import com.hywx.authservice.handler.WebLoginFailureHandler;
import com.hywx.authservice.handler.WebLoginSuccessHandler;
import com.hywx.authservice.provider.CasAuthenticationSecurityConfig;
import com.hywx.authservice.service.impl.UserDetailServiceImpl;
import com.hywx.common.security.configure.Md5PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: auth-service
 * @description: WebSecurity类型的安全配置类 用于处理/oauth开头的请求
 * @author: tangjing
 * @create: 2020-03-04 11:07
 **/
@Order(2)
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private  final WebLoginSuccessHandler successHandler;

    private final  WebLoginFailureHandler failureHandler;


    @Autowired
    private CasAuthenticationSecurityConfig casAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;



    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
//         return  new Md5PasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) //添加验证码过滤器
//                .requestMatchers()
//                .antMatchers("/oauth/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and()
//                .csrf().disable();
       // http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//               http.requestMatchers()
//                .antMatchers("/oauth/**", "/login")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and()

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers()
                .antMatchers("/oauth/**", "/login")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
               .successHandler(successHandler)
               .failureHandler(failureHandler)
                .and()
                .csrf().disable();
        http.apply(casAuthenticationSecurityConfig);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);

    }
}
