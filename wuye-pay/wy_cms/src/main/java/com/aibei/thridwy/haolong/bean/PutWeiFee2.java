package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class PutWeiFee2 {
	
	private String action = "PutWeiFee2";
	
	private String WYID;
	private String HTID;
	private String BZID;
	private String fee;
	private String payMode;
	private String orderNum;
	private String customerName;
	private String payDate;
	private String urlString;	//http://sdfee.oklong.com/service.asmx
	private String soapActionString;	//http://tempuri.org/PutWeiFee2
	private String sercetKey;
	
	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getHTID() {
		return HTID;
	}

	public void setHTID(String hTID) {
		HTID = hTID;
	}

	public String getBZID() {
		return BZID;
	}

	public void setBZID(String bZID) {
		BZID = bZID;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getSoapActionString() {
		return soapActionString + action;
	}

	public void setSoapActionString(String soapActionString) {
		this.soapActionString = soapActionString;
	}

	public String getSercetKey() {
		return sercetKey;
	}

	public void setSercetKey(String sercetKey) {
		this.sercetKey = sercetKey;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { BZID,fee,HTID,orderNum,payMode,WYID,customerName, payDate};
		String key = HaolongUtil.encrypt(arr_data, timestamp,  sercetKey);
		String result = 
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<PutWeiFee2 xmlns=\"http://tempuri.org/\">" +
			"<WYID>"+WYID+"</WYID>" +
			"<HTID>"+HTID+"</HTID>" +
			"<BZID>"+BZID+"</BZID>" +
			"<Fee>"+fee+"</Fee>" +
			"<PayMode>"+payMode+"</PayMode>" +
			"<OrderNum>"+orderNum+"</OrderNum>" +
			"<CustomerName>"+customerName+"</CustomerName>" +
			"<PayDate>"+payDate+"</PayDate>" +
			"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
			"</PutWeiFee2>" +
			"</soap:Body>" +
			"</soap:Envelope>";
		return result;
	}
	
}
