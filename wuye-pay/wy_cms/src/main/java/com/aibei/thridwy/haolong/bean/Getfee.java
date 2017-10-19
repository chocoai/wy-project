package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class Getfee {
	
	private String action = "Getfee";
	private String secretkey;
	
	private String HTID;
	private String JFYF;
	private String urlString;// = "http://sdfee.oklong.com/service.asmx";
	private String soapActionString;// = "http://tempuri.org/Getfee";
	
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
		String[] arr_data = { HTID, JFYF };
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<Getfee xmlns=\"http://tempuri.org/\">" +
			"<HTID>"+HTID+"</HTID>" +
			"<JFYF>"+JFYF+"</JFYF>" +
			"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
			"</Getfee>" +
			"</soap:Body>" +
			"</soap:Envelope>";
		return result;
	}
}
