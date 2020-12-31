package com.hywx.userservice.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lxy
 * @program: user-serice
 * @description: 第三方用户登录
 * @date 2020-03-12
 */
@Data
@TableName("t_user_connection")
public class UserConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统用户名
     */
    private String userName;

    /**
     * 第三方平台名称
     */
    private String providerName;

    /**
     * 第三方平台账户ID
     */
    private String providerUserId;

    /**
     * 第三方平台用户名
     */
    private String providerUserName;

    /**
     * 第三方平台昵称
     */
    private String nickName;

    /**
     * 第三方平台头像
     */
    private String imageUrl;

    /**
     * 地址
     */
    private String location;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "UserConnection{" +
                ", userName='" + userName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                ", providerUserName='" + providerUserName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}