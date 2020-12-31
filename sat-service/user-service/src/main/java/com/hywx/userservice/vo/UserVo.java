package com.hywx.userservice.vo;

import lombok.Data;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-04-13 13:42
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
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0=男,1=女,2=保密
     */
    private Integer sex;
}
