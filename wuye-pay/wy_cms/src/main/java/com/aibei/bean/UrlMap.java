package com.aibei.bean;

public class UrlMap extends Parameters {

	private Integer id;	
	private String urlkey;
	private String urlname;
	private String urlstring;	
	private String soapactionstring;	
	private String secretkey;
	private String createtime;	//创建时间
	private String updatetime;	//最后更新时间
	private String creater;	//创建人
	private String editor;	//最后修改人
	private String status;	//0-无效，1-有效
	
	public UrlMap() {
		
	}
	
	
	public String getUrlname() {
		return urlname;
	}


	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}


	public String getSecretkey() {
		return secretkey;
	}


	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}




	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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

	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "UrlMap [id=" + id + ", urlkey=" + urlkey + ", urlname=" + urlname + ", urlstring=" + urlstring
				+ ", soapactionstring=" + soapactionstring + ", secretkey=" + secretkey + ", createtime=" + createtime
				+ ", updatetime=" + updatetime + ", creater=" + creater + ", editor=" + editor + ", status=" + status
				+ "]";
	}

	
	
}
