package com.hywx.authservice.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-04 16:43
 **/
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:auth.properties"})
@ConfigurationProperties(prefix = "gw.auth")
public class AuthProperties {

    private ClientsProperties[] clients = {};
    // access_token的有效时间
    private int accessTokenValiditySeconds = 60 * 60 * 24;
    // refresh_token的有效时间
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

    // 免认证路径
    private String anonUrl;

    //验证码配置类
    private ValidateCodeProperties code = new ValidateCodeProperties();

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;
    /**
     * 是否使用 JWT令牌
     */
    private Boolean enableJwt;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;

    /**
     * cas登录地址
     */
    private String casLoginUrl;

    /**
     * casToken
     */
    private String  casTokenUrl;
    /**
     * cas用户信息
     */
    private String  casProfileUrl;

    /**
     * 登录出地址
     */
    private String casLogoutUrl;

}
