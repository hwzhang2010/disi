package com.hywx.userservice.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangjing
 * @program: user-serice
 * @description:
 * @date 2020-03-05
 */
@Data
@TableName("sys_organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String id;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 上级单位
     */
    private String parentId;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态 1:正常，0：删除
     */
    private Boolean status;

    /**
     * 创建人
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
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


    private Integer orderNum;


}