package com.pay.thridwy.haolong.bean;

import com.pay.thridwy.haolong.utils.HaolongUtil;

public class GetWeiFee2 {
	
	private String action = "GetWeiFee2";
	
	private String SKID;
	private String urlString;	//http://sdfee.oklong.com/service.asmx
	private String soapActionString;	//http://tempuri.org/GetWeiFee

	private String sercetKey;	
	

	public String getSercetKey() {
		return sercetKey;
	}

	public void setSercetKey(String sercetKey) {
		this.sercetKey = sercetKey;
	}

	public String getSKID() {
		return SKID;
	}

	public void setSKID(String sKID) {
		SKID = sKID;
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

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { SKID };
		String key = HaolongUtil.encrypt(arr_data, timestamp, sercetKey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<GetWeiFee2 xmlns=\"http://tempuri.org/\">" +
			"<SKID>"+SKID+"</SKID>" +
			"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
			"</GetWeiFee2>" +
			"</soap:Body>" +
			"</soap:Envelope>";
		return result;
	}
	
}
