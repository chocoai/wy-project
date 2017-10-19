package com.aibei.bean;

public class PayInfo {
	private String appid;   //平台分配的应用编号
	private Integer waresid;   //应用中的商品编号
	private String waresname;   //商品名称
	private String cporderid;   //商户生成的订单号，需要保证系统唯一
	private float price;   //支付金额
	private String currency;   //货币类型
	private String appuserid;   //用户在商户应用的唯一标识，建议为用户帐号
	private String cpprivateinfo;  //商户私有信息，支付完成后发送支付结果通知时会透传给商户
	private String notifyurl;  //商户服务端接收支付结果通知的地址
	private String Sign;   //对transdata的签名数据
	private String Signtype;   //签名算法类型
	
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public Integer getWaresid() {
		return waresid;
	}
	public void setWaresid(Integer waresid) {
		this.waresid = waresid;
	}
	public String getWaresname() {
		return waresname;
	}
	public void setWaresname(String waresname) {
		this.waresname = waresname;
	}
	public String getCporderid() {
		return cporderid;
	}
	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAppuserid() {
		return appuserid;
	}
	public void setAppuserid(String appuserid) {
		this.appuserid = appuserid;
	}
	public String getCpprivateinfo() {
		return cpprivateinfo;
	}
	public void setCpprivateinfo(String cpprivateinfo) {
		this.cpprivateinfo = cpprivateinfo;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public String getSigntype() {
		return Signtype;
	}
	public void setSigntype(String signtype) {
		Signtype = signtype;
	}
	
	
}
