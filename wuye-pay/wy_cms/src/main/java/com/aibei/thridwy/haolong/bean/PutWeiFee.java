package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class PutWeiFee {
	
	private String action = "PutWeiFee";
	
	private String HTID;
	private String JFYF;
	private String BZIDList;
	private String FeiList;
	private String payMode;
	private String orderNum;
	private String payDate;
	private String urlString;	//http://sdfee.oklong.com/service.asmx
	private String soapActionString;	//http://tempuri.org/PutWeiFee
	private String sercetKey;
	
	

	public String getFeiList() {
		return FeiList;
	}

	public void setFeiList(String feiList) {
		FeiList = feiList;
	}

	public String getSercetKey() {
		return sercetKey;
	}

	public void setSercetKey(String sercetKey) {
		this.sercetKey = sercetKey;
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
	
	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getSoapActionString() {
		return soapActionString;
	}

	public void setSoapActionString(String soapActionString) {
		this.soapActionString = soapActionString + action;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		BZIDList = BZIDList.replaceAll("^(,+)", "");
		String[] arr_data = { HTID, JFYF, BZIDList,FeiList, payMode, orderNum, payDate};
		String key = HaolongUtil.encrypt(arr_data, timestamp, sercetKey);
		String result = 
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<PutWeiFee xmlns=\"http://tempuri.org/\">" +
			"<HTID>"+HTID+"</HTID>" +
			"<JFYF>"+JFYF+"</JFYF>" +
			"<BZIDList>"+BZIDList+"</BZIDList>" +
			"<FeiList>"+FeiList+"</FeiList>" +
			"<PayMode>"+payMode+"</PayMode>" +
			"<OrderNum>"+orderNum+"</OrderNum>" +
			"<PayDate>"+payDate+"</PayDate>" +
			"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
			"</PutWeiFee>" +
			"</soap:Body>" +
			"</soap:Envelope>";
		return result;
	}
	
}
