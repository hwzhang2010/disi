package com.hywx.authservice.vo;

import lombok.Data;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-04-24 08:29
 **/
@Data
public class UserTokenVo {

    /**
     * 是否登录
     */
    private  boolean isLogin;

    /**
     *  认证code
     */
    private String authorizeCode;
}
