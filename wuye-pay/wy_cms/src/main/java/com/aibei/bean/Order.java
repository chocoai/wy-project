package com.aibei.bean;

import java.util.Date;

public class Order extends Parameters {
	private Integer id;
	private String appid;   //平台分配的应用编号 
	private String waresid;   //应用中的商品编号
	private Float price;   //支付金额
	private String appuserid;   //用户在商户应用的唯一标识，建议为用户帐号
	private String cpprivate;  //商户信息
	private String HTID;     //合同id
	private String JFYF;      //计费月份
	private String BZIDList;  //费用项目idlist
	private String feiList; //预收款
	private String transid;  //交易流水id
	private String transtime; //交易完成时间
	private String result;  //交易结果：0–交易成功 ,1–交易失败,2-未交易
	private String orderstatus;  //0:支付成功,1:支付失败,2:待支付,3:正在处理,4:系统异常
	private String SKID;  //收款记录
	private String paystatus; //8-微未付，9-微已付
	private String createtime;
	private String updatetime;
	private String feetype;	//计费类型
	private String currency;	//货币类型
	private String cporderid;	//商户订单号（唯一），与transid对应
	private String transtype;	//交易类型:0–支付交易；1–支付冲正（暂未启用）；2–契约退订；3–自动续费
	private String paytype;	
	private String WYID;
	private String property;
	private String building;
	private String room;
	/*
		1 –充值卡
		2 –游戏点卡
		4 –银行卡
		401 –支付宝
		402 –财付通
		501 –支付宝网页
		502 –财付通网页
		403-微信支付
		5 –爱贝币
		6 –爱贝一键支付
		16 - 百度钱包
		30 - 移动话费
		31 - 联通话费
		32 - 电信话费
	*/
	private String platsystem;//新增, 支付平台 0-爱贝  1-好邻邦微信  2-好邻邦支付宝
	private Boolean offline;//新增, false=线上支付   true=线下支付
	private String customername;//新增，用户备注，用于零星费用
	private String from;//0-微信公众号，1-触摸屏，2-通知单，3-短信，4-客显屏 5-扫码铭牌 6-App
	
	private String begintime;
	
	public String getWYID() {
		return WYID;
	}
	public void setWYID(String wYID) {
		WYID = wYID;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	private String endtime;
	private String chargestatus;
	
	public String getChargestatus() {
		return chargestatus;
	}
	public void setChargestatus(String chargestatus) {
		this.chargestatus = chargestatus;
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
	private String source;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getAppuserid() {
		return appuserid;
	}
	public void setAppuserid(String appuserid) {
		this.appuserid = appuserid;
	}
	
	public String getCpprivate() {
		return cpprivate;
	}
	public void setCpprivate(String cpprivate) {
		this.cpprivate = cpprivate;
	}
	public String getHTID() {
		return HTID;
	}
	public void setHTID(String hTID) {
		HTID = hTID;
	}
	public String getJFYF() {
		return JFYF;
	}
	public void setJFYF(String jFYF) {
		JFYF = jFYF;
	}
	public String getBZIDList() {
		return BZIDList;
	}
	public void setBZIDList(String bZIDList) {
		BZIDList = bZIDList;
	}
	public String getTransid() {
		return transid;
	}
	public void setTransid(String transid) {
		this.transid = transid;
	}
	public String getTranstime() {
		return transtime;
	}
	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}
	
	public String getSKID() {
		return SKID;
	}
	public void setSKID(String sKID) {
		SKID = sKID;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getCporderid() {
		return cporderid;
	}
	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}
	public String getWaresid() {
		return waresid;
	}
	public void setWaresid(String waresid) {
		this.waresid = waresid;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
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
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	public String getFeiList() {
		return feiList;
	}
	public void setFeiList(String feiList) {
		this.feiList = feiList;
	}
	public String getPlatsystem() {
		return platsystem;
	}
	public void setPlatsystem(String platsystem) {
		this.platsystem = platsystem;
	}
	public Boolean getOffline() {
		return offline;
	}
	public void setOffline(Boolean offline) {
		this.offline = offline;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", appid=" + appid + ", waresid=" + waresid + ", price=" + price + ", appuserid="
				+ appuserid + ", cpprivate=" + cpprivate + ", HTID=" + HTID + ", JFYF=" + JFYF + ", WYID =" + WYID + ", BZIDList="
				+ BZIDList + ", feiList=" + feiList + ", transid=" + transid + ", transtime=" + transtime + ", result="
				+ result + ", orderstatus=" + orderstatus + ", SKID=" + SKID + ", paystatus=" + paystatus
				+ ", createtime=" + createtime + ", updatetime=" + updatetime + ", feetype=" + feetype + ", currency="
				+ currency + ", cporderid=" + cporderid + ", transtype=" + transtype + ", paytype=" + paytype
				+ ", property=" + property + ", building=" + building + ", room=" + room + ", begintime=" + begintime
				+ ", endtime=" + endtime + ", chargestatus=" + chargestatus + ", source=" + source + ", platsystem="
				+ platsystem + ", offline=" + offline + ", customername=" + customername + ", from=" + from + "]";
	}

	
	
	
}
