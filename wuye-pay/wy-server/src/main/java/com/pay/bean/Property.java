package com.pay.bean;

import java.io.Serializable;

/**
 * 物业信息
 */
public class Property implements Serializable {

	private static final long serialVersionUID = 1L;
	private String WYID; // 物业ID
	private String WYBH; // 物业编号
	private String WYMC; // 物业名称
	private String address; // 详细地址
	private String admin; // 负责人
	private String phone; // 联系电话
	private String description; // 备注
	private String updatetime;
	private String createtime;
	private String source; // 物业源

	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getWYBH() {
		return WYBH;
	}

	public void setWYBH(String wYBH) {
		WYBH = wYBH;
	}

	public String getWYMC() {
		return WYMC;
	}

	public void setWYMC(String wYMC) {
		WYMC = wYMC;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
