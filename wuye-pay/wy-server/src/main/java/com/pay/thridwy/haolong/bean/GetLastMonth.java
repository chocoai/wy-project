package com.pay.thridwy.haolong.bean;

import com.pay.thridwy.haolong.utils.HaolongUtil;

public class GetLastMonth {
	
	private String action = "GetLastMonth";
	private String secretkey;

	private String WYID;
	private String urlString;// = "http://sdfee.oklong.com/service.asmx";
	private String soapActionString;// = "http://tempuri.org/GetLastMonth";

	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getUrlString() {
		return this.urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getSoapActionString() {
		return this.soapActionString;
	}

	public void setSoapActionString(String soapActionString) {
		this.soapActionString = soapActionString + action;
	}

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { WYID };
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result = 
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	   		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	   		"<soap:Body>" +
	   		"<GetLastMonth xmlns=\"http://tempuri.org/\">" +
	   		"<WYID>"+WYID+"</WYID>" +
		    "<TimeStamp>"+timestamp+"</TimeStamp>" +
		   	"<Key>"+key+"</Key>" +
	   		"</GetLastMonth>" +
	   		"</soap:Body>" +
	   		"</soap:Envelope>";
		return result;
	}
}
