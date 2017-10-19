package com.pay.bean;

import java.io.Serializable;
import java.util.Date;

public class Ad implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String descr;
	private String imgSrc;
	private String href;
	private Date createtime;
	private Date updatetime;
	private String status;
	private String WYID;
	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getWYID() {
		return WYID;
	}

	public void setWYID(String wYID) {
		WYID = wYID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Ad [id=" + id + ", descr=" + descr + ", imgSrc=" + imgSrc + ", href=" + href + ", createtime=" + createtime + ", updatetime=" + updatetime + ", status=" + status
				+ ", WYID=" + WYID + ", source=" + source + "]";
	}

}
