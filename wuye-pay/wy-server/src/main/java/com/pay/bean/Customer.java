package com.pay.bean;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String KHID; // 客户ID
	private String WYID; // 物业ID
	private String WYMC; // 物业名称
	private String WYGS; // 物业公司
	private String customer;// 客户名称
	private String certNo; // 证件号
	private String phone; // 手机号
	private String createtime;
	private String updatetime;
	private String source;

	public String getKHID() {
		return KHID;
	}

	public void setKHID(String kHID) {
		KHID = kHID;
	}

	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getWYMC() {
		return WYMC;
	}

	public void setWYMC(String wYMC) {
		WYMC = wYMC;
	}

	public String getWYGS() {
		return WYGS;
	}

	public void setWYGS(String wYGS) {
		WYGS = wYGS;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
