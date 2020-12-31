package com.hywx.sisl.common;


import java.io.Serializable;

/**
 * @program: common
 * @description:
 * @author: tangjing
 * @create: 2020-03-16 09:42
 **/

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
    
    public QueryRequest() {
    }

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(boolean pageFlag) {
		this.pageFlag = pageFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "QueryRequest: {pageSize=" + pageSize + ", pageNo=" + pageNo + ", field=" + field + ", order=" + order
				+ ", pageFlag=" + pageFlag + "}";
	}
    
    

}