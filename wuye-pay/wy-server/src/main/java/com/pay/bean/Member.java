package com.pay.bean;

import java.io.Serializable;

/**
 * 会员
 * 
 * @author Administrator
 * 
 */
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;// 会员id
	private Integer memberType;// 会员类型
	private String openid;// 对应微信号的openid
	private Integer roomId;// 房号

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

}
