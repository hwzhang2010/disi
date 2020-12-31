package com.hywx.common.core.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: common
 * @description:
 * @author: tangjing
 * @create: 2020-03-16 09:42
 **/
@Data
@ToString
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;
    /**
     * 当前页面数据量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int pageNo = 1;
    /**
     * 排序字段
     */
    private String field;
    /**
     * 排序规则，asc升序，desc降序
     */
    private String order;
    /**
     * 是否分页，true,分页，flase不分页
     */
    private boolean pageFlag=true;

}