package com.pay.bean;

import java.io.Serializable;

public class Wares implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String waresid;
	private String waresname;
	private String notifyurl;
	private String requesturl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWaresid() {
		return waresid;
	}

	public void setWaresid(String waresid) {
		this.waresid = waresid;
	}

	public String getWaresname() {
		return waresname;
	}

	public void setWaresname(String waresname) {
		this.waresname = waresname;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getRequesturl() {
		return requesturl;
	}

	public void setRequesturl(String requesturl) {
		this.requesturl = requesturl;
	}

	@Override
	public String toString() {
		return "Wares [waresid=" + waresid + ", waresname=" + waresname + ", notifyurl=" + notifyurl + ", requesturl=" + requesturl + "]";
	}

}
