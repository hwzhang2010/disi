package com.hywx.userservice.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tangjing
 * @program: user-serice
 * @description:
 * @date 2020-03-13
 */
@Data
@TableName("sys_role_resource")
public class RoleResource implements Serializable {

    private static final long serialVersionUID = 1L;


    private String roleId;

    private String resourceId;


}