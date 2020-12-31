package com.hywx.authservice.vo;

import lombok.Data;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-04-22 14:19
 **/
@Data
public class UserVo {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码key
     */
    private String key;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密码
     */
    private String clientSecret;


    /**
     * 跳转地址
     */
    private String redirectUri;

    /**
     *  认证code
     */
    private String authorizeCode;

}
