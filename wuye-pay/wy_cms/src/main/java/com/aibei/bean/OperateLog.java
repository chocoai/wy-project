package com.aibei.bean;

import java.io.Serializable;
import java.util.Date;

public class OperateLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer action;//0.销账 1.删除废单
	private Date time;
	private String memo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
