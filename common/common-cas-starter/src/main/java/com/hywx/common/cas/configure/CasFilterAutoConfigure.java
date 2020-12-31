package com.hywx.common.cas.configure;

import com.hywx.common.cas.filter.AuthenticationFilter;
import com.hywx.common.cas.properties.CasClientProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-23 10:14
 **/
@Configuration
@EnableConfigurationProperties(CasClientProperties.class)
@ConditionalOnProperty(value = "cas.enable", havingValue = "true",matchIfMissing = true)
public class CasFilterAutoConfigure {

    @Autowired
    CasClientProperties casClientProperties;

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean  listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        listenerRegistrationBean.setOrder(1);
        return listenerRegistrationBean;
    }

    /**
     * 单点登录退出
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("casServerUrlPrefix", casClientProperties.getServerUrlPrefix());
        registrationBean.setName("CAS Single Sign Out Filter");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * 单点登录认证
     * @return
     */
    @Bean
    public FilterRegistrationBean AuthenticationFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS Filter");
        registrationBean.addInitParameter("casServerLoginUrl",casClientProperties.getServerUrlPrefix());
        registrationBean.addInitParameter("serverName", casClientProperties.getClientHostUrl());
        registrationBean.addInitParameter("ignorePattern",  casClientProperties.getIgnorePattern());
        //自定义UrlPatternMatcherStrategy 验证规则
        if(casClientProperties.getIgnoreUrlPatternType() != null && !"".equals(casClientProperties.getIgnoreUrlPatternType())){
            registrationBean.addInitParameter("ignoreUrlPatternType", casClientProperties.getIgnoreUrlPatternType());
        }

        registrationBean.setOrder(3);
        return registrationBean;
    }

    /**
     * 单点登录校验
     * @return
     */
    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS Validation Filter");
        registrationBean.addInitParameter("casServerUrlPrefix",casClientProperties.getServerUrlPrefix());
        registrationBean.addInitParameter("serverName", casClientProperties.getClientHostUrl());
        registrationBean.setOrder(4);
        return registrationBean;
    }

    /**
     * 单点登录请求包装
     * @return
     */
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS HttpServletRequest Wrapper Filter");
        registrationBean.setOrder(5);
        return registrationBean;
    }

    /**
     * 单点登录本地用户信息
     * @return
     */
    @Bean
    public FilterRegistrationBean localUserInfoFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new LocalUserInfoFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("localUserInfoFilter");
        registrationBean.setOrder(6);
        return registrationBean;
    }

    /**
     * 网关安全配置
     * @return
     */
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }
}
