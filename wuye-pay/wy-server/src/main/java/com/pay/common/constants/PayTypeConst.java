package com.pay.common.constants;

/**
 * 支付常用常量
 * 
 * @author Administrator
 * 
 */
public interface PayTypeConst {
	/**
	 * 支付平台，爱贝支付
	 */
	static String PAY_TYPE_PLATFORM_AIBEI = "0";

	/**
	 * 支付平台，好邻邦微信支付
	 */
	static String PAY_TYPE_PLATFORM_WEIXIN = "1";

	/**
	 * 支付平台，好邻邦支付宝支付
	 */
	static String PAY_TYPE_PLATFORM_ALIPAY = "2";

	/**
	 * 支付平台，其他支付
	 */
	static String PAY_TYPE_PLATFORM_OTHER = "30";

}
