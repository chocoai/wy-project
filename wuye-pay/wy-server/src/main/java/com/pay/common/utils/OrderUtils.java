package com.pay.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单处理
 * 
 * @author FUYUNSHIDAI
 * 
 */
public class OrderUtils {

	/**
	 * 生成订单号
	 * 
	 * @return
	 */
	private static String getOrderNo() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
	}

	/**
	 * 生成订单号
	 * 
	 * @param type
	 *            订单类型
	 * @return
	 */
	public static String getOrderNo(int type) {
		String orderno = getOrderNo();
		if (type == 1) {
			// 正常订单单号
			orderno += "81";
		} else if (type == 2) {
			// 退款订单单号
			orderno += "82";
		}
		return orderno;
	}
	
	/**
	 * 生成订单号
	 * suorce+时间戳+100内随机数
	 * @param args
	 */
	public static String getOrderNo(String source){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		return source + "_" + formatter.format(new Date());
	}
	 

	public static void main(String[] args) {
		System.out.println(getOrderNo());
	}
}
