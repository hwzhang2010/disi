package com.hywx.zuulservice.dao;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: user-serice
 * @description: 用户信息实体类
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String id;

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
     * 单位id
     */
    private String orgId;

    /**
     * 状态 0:禁用，1:正常，2：删除
     */
    private int status;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**角色id**/
    @TableField(exist = false)
    private String roleId;

    /**角色名称**/
    @TableField(exist = false)
    private String roleName;


    @TableField(exist = false)
    private String organizationName;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", password=" + password +
                ", mobile=" + mobile +
                ", orgId=" + orgId +
                ", status=" + status +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", remark=" + remark +
                "}";
    }

}