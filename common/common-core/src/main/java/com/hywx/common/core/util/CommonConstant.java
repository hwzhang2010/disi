package com.hywx.common.core.util;

/**
 * @program: common
 * @description:
 * @author: tangjing
 * @create: 2020-03-16 10:15
 **/
public class CommonConstant {

    /*
     *   排序规则：降序
     */
    public static final String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "ascending";


    /**
     * Zuul请求头TOKEN名称（不要有空格）
     */
    public static final String ZUUL_TOKEN_HEADER = "GatewayToken";

    /**
     * Zuul请求头TOKEN值
     */
    public static final String ZUUL_TOKEN_VALUE = "hywx:gateway:123456";


}
