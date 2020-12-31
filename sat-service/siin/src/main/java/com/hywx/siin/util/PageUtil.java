package com.hywx.siin.util;

import java.util.List;

/**
 * list分页工具
 * @author zhang.huawei
 *
 */
public class PageUtil {
	/**
     * 开始分页
     * @param list
     * @param currentPage 页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static List<?> startPage(List<?> list, Integer currentPage, Integer pageSize) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (currentPage != pageCount) {
            fromIndex = (currentPage - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (currentPage - 1) * pageSize;
            toIndex = count;
        }

        List<?> pageList = list.subList(fromIndex, toIndex);

        return pageList;
    }
}
