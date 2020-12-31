package com.hywx.zuulservice.config;

/**
 * @program: auth-server
 * @description:
 * @author: tangjing
 * @create: 2020-03-05 14:28
 **/
public class ZuulConfigure {

    /**
     * Zuul请求头TOKEN名称（不要有空格）
     */
    public static final String ZUUL_TOKEN_HEADER = "GatewayToken";

    /**
     * Zuul请求头TOKEN值
     */
    public static final String ZUUL_TOKEN_VALUE = "hywx:gateway:123456";
}
