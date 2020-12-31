package com.hywx.userservice.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-13 09:44
 **/
@Data
@TableName("sys_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "role_id")
    private String roleId;

}
