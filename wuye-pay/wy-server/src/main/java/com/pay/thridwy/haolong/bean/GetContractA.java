package com.pay.thridwy.haolong.bean;

import com.pay.thridwy.haolong.utils.HaolongUtil;

public class GetContractA {
	private String action = "GetContractA";
	private String secretkey;
	
	private String CliName;
	private String Mobile;
	private String WYID;
	private String urlString; // http://sdfee.oklong.com/service.asmx
	private String soapActionString; // http://tempuri.org/GetContractA
	
	public String getWYID() {
		return WYID;
	}
	public void setWYID(String wYID) {
		WYID = wYID;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public String getCliName() {
		return CliName;
	}
	public void setCliName(String cliName) {
		CliName = cliName;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
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
		String[] arr_data = { CliName, Mobile, WYID};
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	   		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	   		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
	   		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	   		"<soap:Body>" +
	   		"<GetContractA xmlns=\"http://tempuri.org/\">" +
	   		"<CliName>"+CliName+"</CliName>" +
	   		"<WYID>"+WYID+"</WYID>" +
	   		"<Mobile>"+Mobile+"</Mobile>" +
	   		"<TimeStamp>"+timestamp+"</TimeStamp>" +
	   		"<Key>"+key+"</Key>" +
	   		"</GetContractA>" +
	   		"</soap:Body>" +
	   		"</soap:Envelope>";

		return result;
	}

}
