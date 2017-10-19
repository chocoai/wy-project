package com.aibei.thridwy.haolong.bean;

import com.aibei.thridwy.haolong.utils.HaolongUtil;

public class GetCustomer {
	
	private String action = "GetCustomer";
	private String secretkey;
	
	private String PropID;
	private String CliName;
	private String Mobile;
	private String CertNo;
	private String urlString;	//http://sdfee.oklong.com/service.asmx
	private String soapActionString;	//http://tempuri.org/GetCustomer
	
	public String getPropID() {
		return PropID;
	}

	public void setPropID(String propID) {
		PropID = propID;
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

	public String getCertNo() {
		return CertNo;
	}

	public void setCertNo(String certNo) {
		CertNo = certNo;
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

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String toString(){
		String timestamp = "" + System.currentTimeMillis();
	    String[] arr_data = {PropID, CliName, Mobile, CertNo};
	    String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
	    String result = 
	    	"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	   		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	   		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
	   		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	   		"<soap:Body>" +
	   		"<GetCustomer xmlns=\"http://tempuri.org/\">" +
	   	    "<PropID>"+PropID+"</PropID>" +
	   		"<CliName>"+CliName+"</CliName>" +
	   		"<Mobile>"+Mobile+"</Mobile>" +
	   		"<CertNo>"+CertNo+"</CertNo>" +
	   		"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
	   		"</GetCustomer>" +
	   		"</soap:Body>" +
	   		"</soap:Envelope>";
	    return result;
	 }
	
}
