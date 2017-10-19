package com.pay.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

import me.chanjar.weixin.common.bean.WxJsapiSignature;

import com.pay.common.conf.Global;

public class WeixinUtil {

	public static WxJsapiSignature sign(String jsapi_ticket, String url,String appid) {
		String nonce_str = create_nonce_str();
		long timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		WxJsapiSignature jsapiSignature = new WxJsapiSignature();
		jsapiSignature.setAppid(appid);
		jsapiSignature.setTimestamp(timestamp);
		jsapiSignature.setNoncestr(nonce_str);
		jsapiSignature.setUrl(url);
		jsapiSignature.setSignature(signature);

		return jsapiSignature;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static long create_timestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 微信付款给会员
	 * 
	 * @return
	 */
	public String payToMember() {
		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		String mch_appid = Global.getConfig("weixin.appid");// 商户appid
		String mchid = Global.getConfig("weixin.pay.partnerid");// 微信支付分配的商户号
		String sub_mch_id = "";// 微信支付分配的子商户号，受理模式下必填
		String device_info = "";// 微信支付分配的终端设备号
		String nonce_str = create_nonce_str();// 随机字符串，不长于32位
		String sign = "";// partner_trade_no
		String partner_trade_no = OrderUtils.getOrderNo(1);// 商户订单号，需保持唯一性
		String openid = "";// 商户appid下，某用户的openid
		String check_name = "NO_CHECK";// NO_CHECK：不校验真实姓名//FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）//OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
		String re_user_name = "";// 收款用户真实姓名。
									// 如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
		String amount = "40";// 企业付款金额，单位为分
		String desc = "用户收益付款";// 企业付款操作说明信息。必填。
		// String spbill_create_ip = StringUtils.getRemoteAddr(request2);//
		// 调用接口的机器Ip地址

		return "";
	}
}
