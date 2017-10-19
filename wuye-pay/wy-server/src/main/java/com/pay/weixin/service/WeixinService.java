package com.pay.weixin.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;

import com.pay.bean.Account;
import com.pay.bean.Order;
import com.pay.bean.Wares;
import com.pay.dto.WeJson;

public interface WeixinService {

	WeJson h5Pay(double price);

	WxJsapiSignature getWxJsapiSignature(Account account, Order order) throws WxErrorException;
	
	WxJsapiSignature getWxJsapiSignature() throws WxErrorException;

	String dealQrPayMode1(String body) throws DocumentException, IOException, Exception;

	Map<String, String> getQrPay(HttpServletRequest request);

	String getNativeMode1Bizpayurl(HttpServletRequest request);

	String oAuth2oklong();

	String authCallbackOklong(HttpServletRequest request);

	/**
	 * 调用微信支付,调用
	 * 
	 * @return
	 */
	String weixinPay(int confid, Order order);

	/**
	 * 程序发起调用
	 * 
	 * @param account
	 * @param order
	 * @return
	 */
	String topay(Account account, Order order);

	/**
	 * 微信H5发起
	 * 
	 * @param account
	 * @param order
	 * @param wares 
	 */
	Map<String, String> h5Pay(Account account, Order order, Wares wares);

	/**
	 * 线下模式二支付方式
	 * 
	 * @param account
	 * @param order
	 * @param wares 
	 * @return
	 * @throws Exception 
	 */
	Map<String, String> getQrPayModel2(Account account, Order order, Wares wares) throws Exception;

	WxJsapiSignature getWxJsapiSignature(String url) throws WxErrorException;

	Map<String, String> getQrPayModel2(HttpServletRequest request) throws Exception;

	Map<String, String> dealNotify(String body) throws DocumentException;

}
