package com.aibei.bean;

public class Statistics extends Parameters {

	private static final long serialVersionUID = 1L;
	private String date;
	private double count;
	private Integer number;
	
	private String begintime;
	private String endtime;
	private String[] platsystem;//新增, 支付平台 0-爱贝  1-好邻邦微信  2-好邻邦支付宝
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String[] getPlatsystem() {
		return platsystem;
	}
	public void setPlatsystem(String[] platsystem) {
		this.platsystem = platsystem;
	}

	
}
