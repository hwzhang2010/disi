package com.hywx.userservice.dao;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hywx.common.core.util.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
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
@Excel("用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    // 默认密码
    public static final String DEFAULT_PASSWORD = "12345678";

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @ExcelField(value = "用户名")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    @ExcelField(value = "手机号")
    private String mobile;

    /**
     * 单位名称
     */
    private String orgId;

    /**
     * 状态 0:禁用，1:正常，2：删除
     */
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    private Integer status;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(value = "创建时间")
    private Date createTime;

    /**
     * 修改者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(value = "更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 最后登录时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(value = "最近访问时间",writeConverter = TimeConverter.class)
    private Date lastLoginTime;

    /**
     * 性别
     */
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    private Integer sex;

    /**
     * 邮箱
     */
    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 组织名称
     */
    @TableField(exist = false)
    @ExcelField(value = "组织名称")
    private  String orgName;

    /**
     * 角色id
     */
    @TableField(exist = false)
    private String roleId;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;

}