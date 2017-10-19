package com.pay.bean;

import java.io.Serializable;

public class Contract implements Serializable {

	private static final long serialVersionUID = 1L;
	private String WYID; // 物业ID
	private String LYID; // 楼宇ID
	private String FWID;
	private String KHID; // 物业名称
	private String HTID; // 物业名称
	private String WYMC; // 物业名称
	private String DLMC; // 大楼名称
	private String KHMC; // 客户名称
	private String phone;
	private String DLBH; // 大楼编号
	private String DYMC; // 单元名称
	private String DYXH; // 单元序号
	private String LCH; // 楼层号
	private String FXH; // 房序号
	private String FH; // 房号
	private String NBFH; // 内部房号
	private String YZ; // 业主
	private String JZMJ; // 建筑面积
	private String ZYZT; // 止约状态
	private String ZYRQ; // 止约日期
	private String TSDM; // 托收代码
	private String YHZH; // 银行帐号
	private String YHDM; // 银行代码
	private String KHM; // 开户名
	private String createtime;
	private String updatetime;
	private String source;

	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getLYID() {
		return LYID;
	}

	public void setLYID(String lYID) {
		LYID = lYID;
	}

	public String getFWID() {
		return FWID;
	}

	public void setFWID(String fWID) {
		FWID = fWID;
	}

	public String getKHID() {
		return KHID;
	}

	public void setKHID(String kHID) {
		KHID = kHID;
	}

	public String getHTID() {
		return HTID;
	}

	public void setHTID(String hTID) {
		HTID = hTID;
	}

	public String getWYMC() {
		return WYMC;
	}

	public void setWYMC(String wYMC) {
		WYMC = wYMC;
	}

	public String getDLMC() {
		return DLMC;
	}

	public void setDLMC(String dLMC) {
		DLMC = dLMC;
	}

	public String getKHMC() {
		return KHMC;
	}

	public void setKHMC(String kHMC) {
		KHMC = kHMC;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDLBH() {
		return DLBH;
	}

	public void setDLBH(String dLBH) {
		DLBH = dLBH;
	}

	public String getDYMC() {
		return DYMC;
	}

	public void setDYMC(String dYMC) {
		DYMC = dYMC;
	}

	public String getDYXH() {
		return DYXH;
	}

	public void setDYXH(String dYXH) {
		DYXH = dYXH;
	}

	public String getLCH() {
		return LCH;
	}

	public void setLCH(String lCH) {
		LCH = lCH;
	}

	public String getFXH() {
		return FXH;
	}

	public void setFXH(String fXH) {
		FXH = fXH;
	}

	public String getNBFH() {
		return NBFH;
	}

	public void setNBFH(String nBFH) {
		NBFH = nBFH;
	}

	public String getYZ() {
		return YZ;
	}

	public void setYZ(String yZ) {
		YZ = yZ;
	}

	public String getJZMJ() {
		return JZMJ;
	}

	public void setJZMJ(String jZMJ) {
		JZMJ = jZMJ;
	}

	public String getZYZT() {
		return ZYZT;
	}

	public void setZYZT(String zYZT) {
		ZYZT = zYZT;
	}

	public String getZYRQ() {
		return ZYRQ;
	}

	public void setZYRQ(String zYRQ) {
		ZYRQ = zYRQ;
	}

	public String getTSDM() {
		return TSDM;
	}

	public void setTSDM(String tSDM) {
		TSDM = tSDM;
	}

	public String getYHZH() {
		return YHZH;
	}

	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}

	public String getYHDM() {
		return YHDM;
	}

	public void setYHDM(String yHDM) {
		YHDM = yHDM;
	}

	public String getKHM() {
		return KHM;
	}

	public void setKHM(String kHM) {
		KHM = kHM;
	}

	public String getFH() {
		return FH;
	}

	public void setFH(String fH) {
		FH = fH;
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
