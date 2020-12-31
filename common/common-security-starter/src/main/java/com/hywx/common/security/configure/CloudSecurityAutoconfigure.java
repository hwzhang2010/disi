package com.hywx.common.security.configure;

import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.security.constant.SecurityConstant;
import com.hywx.common.security.handler.ResourceAccessDeniedHandler;
import com.hywx.common.security.handler.ResoureExceptionEntryPoint;
import com.hywx.common.security.properties.CloudSecurityProperties;
import com.hywx.common.security.utils.SecurityUtil;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Base64Utils;

/**
 * feign调用添加token配置
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(CloudSecurityProperties.class)
@ConditionalOnProperty(value = "hywx.cloud.security.enable", havingValue = "true", matchIfMissing = true)
public class CloudSecurityAutoconfigure {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public ResourceAccessDeniedHandler accessDeniedHandler() {
        return new ResourceAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public ResoureExceptionEntryPoint authenticationEntryPoint() {
        return new ResoureExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
       // return new BCryptPasswordEncoder();
        return new Md5PasswordEncoder();
    }

    /**
     * 网关安全配置
     * @return
     */
    @Bean
    public CloudSecurityInteceptorConfigure cloudSecurityInteceptorConfigure() {
        return new CloudSecurityInteceptorConfigure();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String gatewayToken = new String(Base64Utils.encode(CommonConstant.ZUUL_TOKEN_VALUE.getBytes()));
            requestTemplate.header(CommonConstant.ZUUL_TOKEN_HEADER, gatewayToken);
            String authorizationToken = SecurityUtil.getCurrentTokenValue();
            requestTemplate.header(HttpHeaders.AUTHORIZATION, SecurityConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
//            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
//            if (details instanceof OAuth2AuthenticationDetails) {
//                String authorizationToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
//                requestTemplate.header(HttpHeaders.AUTHORIZATION, SecurityConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
//            }
        };
    }
}
