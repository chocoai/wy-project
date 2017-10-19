package com.pay.thridwy.haolong.utils;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.pay.common.StringUtils;


public class HaolongUtil {
	
	/*
	 * 加密成key返回
	 */	
	
public static String encrypt(String[] arr_data, String timestamp, String secretkey) {
		String result = "";
		if (arr_data != null && secretkey != null) {
			String[] arr = arr_data.clone();
			String str = "";
			Arrays.sort(arr); // 排序数组
			for (int i = 0; i < arr.length; i++) {
				str += arr[i];
			}
			String str2 = timestamp + str + secretkey;
			//System.out.println("加密前:"+str2);
			String str1 = StringUtils.getMD5Str(str2);
			result = DigestUtils.md5Hex(str1).toUpperCase();
			//System.out.println("加密后:"+result);	
		}
		return result ;
	}
	
	/*
	 * 好邻邦排序以及加密算法
	 */
	public static String encrypt2(String[] arr_data, String key1) { //arr-date={WYID,HTID,JFYF},key1=cpkey
		String result = "";
		if (arr_data != null && key1 != null) {
			String[] arr = arr_data.clone();
			StringBuilder sb = new StringBuilder();
			Arrays.sort(arr);
			for (int i = 0; i < arr.length; i++) {
				sb.append(arr[i]);
			}
			sb.append(key1);
			result = sb.toString();
			//System.out.println("加密前:"+result);
			result = DigestUtils.md5Hex(result).toUpperCase();
			//System.out.println("加密后:"+result);
		}
		return result;
	}
	
	/*
	 * 解密后字符串：
	 * 四个参数值：88,12356,201511,123.234.18.89
	 * 对应：WYID,HTID,JFYF,FromIP
	 */
	public static String decode(String code) {
		String decode = "";
		
		String move = code.substring(code.length() - 2);
		int mov = Integer.parseInt(move, 16);
		
		String sstr = code.substring(0, code.length()-2);
		char[] chstr = sstr.toCharArray();
		for(int i=0;i<chstr.length;i++) {
			chstr[i] = (char) (chstr[i] - mov);
		}
		decode = new String(chstr);
		return decode;
	}
	

	
}
