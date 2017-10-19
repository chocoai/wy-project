package com.aibei.bean;

import java.io.Serializable;

/**
 * EasyUI分页等参数
 * @author Administrator
 */
@SuppressWarnings("serial")
public class Parameters implements Serializable {
	
	private Integer beginIndex = 0;
	private int rows = 10;
	private int page = 1;
	private String order = "asc";
	private String sort;
	
	public Integer getBeginIndex() {
		this.beginIndex = (page-1)*rows;
		return beginIndex;
	}
	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
