package com.hywx.authservice.util;

/**
 * @program: auth-service
 * @description: 常量类
 * @author: tangjing
 * @create: 2020-03-06 08:54
 **/
public class CommonConstant {
    /**
     * gif类型
     */
    public static final String GIF = "gif";
    /**
     * png类型
     */
    public static final String PNG = "png";

    /**
     * 验证码 key前缀
     */
    public static final String CODE_PREFIX = "gw.captcha.";

    /**
     * 验证码 key
     */
    public static final String VALIDATE_CODE_KEY = "key";
    /**
     * 验证码 code
     */
    public static final String VALIDATE_CODE_CODE = "code";
    /**
     * 认证类型参数 key
     */
    public static final String GRANT_TYPE = "grant_type";
    /**
     * 登录类型
     */
    public static final String LOGIN_TYPE = "login_type";

    public static final String SOCIAL_LOGIN = "social_login";

    public static final String SOCIAL_LOGIN_PASSWORD = "febs_social_login_password";

    /**
     * 刷新模式
     */
    public static final String REFRESH_TOKEN = "refresh_token";
    /**
     * 授权码模式
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";
    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    /**
     * 密码模式
     */
    public static final String PASSWORD = "password";
    /**
     * 简化模式
     */
    public static final String IMPLICIT = "implicit";

}
