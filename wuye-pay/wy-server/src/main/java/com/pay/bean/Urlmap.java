package com.pay.bean;

import java.io.Serializable;

public class Urlmap implements Serializable {

	private static final long serialVersionUID = 1L;
	private String urlkey;
	private String urlstring;
	private String soapactionstring;
	private String secretkey;
	private String status;

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String getUrlkey() {
		return urlkey;
	}

	public void setUrlkey(String urlkey) {
		this.urlkey = urlkey;
	}

	public String getUrlstring() {
		return urlstring;
	}

	public void setUrlstring(String urlstring) {
		this.urlstring = urlstring;
	}

	public String getSoapactionstring() {
		return soapactionstring;
	}

	public void setSoapactionstring(String soapactionstring) {
		this.soapactionstring = soapactionstring;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Urlmap [urlkey=" + urlkey + ", urlstring=" + urlstring + ", soapactionstring=" + soapactionstring + ", secretkey=" + secretkey + ", status=" + status + "]";
	}

}
