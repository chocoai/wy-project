package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class GetContract {
	
	private String action = "GetContract";
	private String secretkey;

	private String KHID;
	private String HTID;
	private String urlString; // http://sdfee.oklong.com/service.asmx
	private String soapActionString; // http://tempuri.org/GetContract

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

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { KHID, HTID };
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	   		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	   		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
	   		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	   		"<soap:Body>" +
	   		"<GetContract xmlns=\"http://tempuri.org/\">" +
	   		"<KHID>"+KHID+"</KHID>" +
	   		"<HTID>"+HTID+"</HTID>" +
	   		"<TimeStamp>"+timestamp+"</TimeStamp>" +
	   		"<Key>"+key+"</Key>" +
	   		"</GetContract>" +
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
}
