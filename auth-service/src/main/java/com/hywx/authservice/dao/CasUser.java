package com.hywx.authservice.dao;

import lombok.Data;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-07-22 10:53
 **/
@Data
public class CasUser {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    private String birth;
}
