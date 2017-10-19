package com.aibei.common;

/**
 * 该类封装了，上传文件后的结果信息
 */
public class UploadResult{
	
	private int status;
	private String value;
	
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}