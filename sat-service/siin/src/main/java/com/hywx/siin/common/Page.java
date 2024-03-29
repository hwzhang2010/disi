package com.hywx.siin.common;

import java.util.List;

/**
 * @ClassName: 对查询的List进行分页处理
 * @Description:
 * @Author LXYuuuuu
 * @Date 2020/3/28 19:42
 */
public class Page {
    private Integer currentPage;//当前页
    private int pageSize;//每页显示记录条数
    private int totalPage;//总页数
    private List<?> dataList;//每页显示的数据
    private long total;//总记录条数

    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public List<?> getDataList() {
        return dataList;
    }
    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
    
    
    
}
