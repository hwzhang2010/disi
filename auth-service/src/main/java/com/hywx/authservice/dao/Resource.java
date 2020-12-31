package com.hywx.authservice.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author tangjing
 * @program: user-serice
 * @description:
 * @date 2020-03-05
 */
@Data
@TableName("sys_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String id;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * 对应路由path
     */
    private String url;

    /**
     * 类型   0, 系统 1：目录   2：菜单   3：按钮
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", parentId=" + parentId +
                ", url=" + url +
                ", type=" + type +
                ", orderNum=" + orderNum +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                "}";
    }

}