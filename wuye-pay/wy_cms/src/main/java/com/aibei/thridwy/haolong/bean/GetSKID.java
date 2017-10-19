package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class GetSKID {
	
	private String action = "GetSKID";
	private String secretkey;
	private String orderNumber;
	private String feetype;
	private String urlString; // http://sdfee.oklong.com/service.asmx
	private String soapActionString; // http://tempuri.org/GetContract

	

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { orderNumber, feetype };
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	   		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	   		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
	   		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	   		"<soap:Body>" +
	   		"<GetSKID xmlns=\"http://tempuri.org/\">" +
	   		"<OrderNumber>"+orderNumber+"</OrderNumber>" +
	   		"<Feetype>"+feetype+"</Feetype>" +
	   		"<TimeStamp>"+timestamp+"</TimeStamp>" +
	   		"<Key>"+key+"</Key>" +
	   		"</GetSKID>" +
	   		"</soap:Body>" +
	   		"</soap:Envelope>";
		return result;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getFeetype() {
		return feetype;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	
	
}
