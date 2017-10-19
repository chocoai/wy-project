package com.pay.dto;

import java.io.Serializable;

/**
 * 外层json
 * 
 * @author FUYUNSHIDAI
 * 
 */
public class WeJson implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int SUCCESS_CODE = 200;// 成功

	public static final int FAILED_CODE = 500;// 失败

	public static final int FAILED_CODE_LOGIN = 401;
	public static final int YOUKU_INVALID_ACCESS_CODE = 1001;

	/**
	 * 返回的状态码
	 */
	private int code;
	/**
	 * 返回信息说明
	 */
	private String msg;

	/**
	 * 具体额返回数据
	 */
	private Object data;

	private WeJson(int code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 成功返回json
	 * 
	 * @param msg
	 * @param obj
	 * @return
	 */
	public static WeJson success(String msg, Object obj) {
		return new WeJson(SUCCESS_CODE, msg, obj);
	}

	public static WeJson success(Object obj) {
		return new WeJson(SUCCESS_CODE, "", obj);
	}

	/**
	 * 失败返回
	 * 
	 * @param msg
	 * @param obj
	 * @return
	 */
	public static WeJson fail(String msg, Object obj) {
		return new WeJson(FAILED_CODE, msg, obj);
	}

	public static WeJson fail(String msg) {
		return new WeJson(FAILED_CODE, msg, null);
	}

	public static WeJson login(String msg) {
		return new WeJson(FAILED_CODE_LOGIN, msg, null);
	}

	public static WeJson fail(int code, String msg) {
		return new WeJson(code, msg, null);
	}
}
