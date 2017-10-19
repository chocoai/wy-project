package com.aibei.bean;

public class Account extends Parameters {

	private Integer id;
	private String appid; // 平台分配的应用编号 (服务商商户号)
	private String appuserid; // 用户在商户应用的唯一标识，建议为用户帐号(子商户商户号)
	private String wyid; // 物业编号（小区编号）
	private String property;//物业名称
	private String createtime; // 创建时间
	private String updatetime; // 最后更新时间
	private String creater; // 创建人
	private String editor; // 最后修改人
	private String status; // 0-无效，1-有效
	private String appv_key;//(服务商秘钥)
	private String appSecret;//公众号秘钥
	private String source;
	private String platp_key; //platsystem,offline (微信appid)
	private String platsystem;//新增, 支付平台 0-爱贝  1-好邻邦微信  2-好邻邦支付宝
	private Boolean offline;//新增, false=线上支付   true=线下支付
	private String wxid;//维修id
	private String ltid;//临停id
	private Integer sumflag;

	public Account() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppuserid() {
		return appuserid;
	}

	public void setAppuserid(String appuserid) {
		this.appuserid = appuserid;
	}

	public String getWyid() {
		return wyid;
	}

	public void setWyid(String wyid) {
		this.wyid = wyid;
	}
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
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

	public String getAppv_key() {
		return appv_key;
	}

	public void setAppv_key(String appv_key) {
		this.appv_key = appv_key;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getPlatp_key() {
		return platp_key;
	}

	public void setPlatp_key(String platp_key) {
		this.platp_key = platp_key;
	}

	public String getPlatsystem() {
		return platsystem;
	}

	public void setPlatsystem(String platsystem) {
		this.platsystem = platsystem;
	}

	public Boolean getOffline() {
		return offline;
	}

	public void setOffline(Boolean offline) {
		this.offline = offline;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getLtid() {
		return ltid;
	}

	public void setLtid(String ltid) {
		this.ltid = ltid;
	}

	public Integer getSumflag() {
		return sumflag;
	}

	public void setSumflag(Integer sumflag) {
		this.sumflag = sumflag;
	}

	
}
