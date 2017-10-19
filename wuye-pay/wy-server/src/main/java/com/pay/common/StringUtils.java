package com.pay.common;

import java.io.UnsupportedEncodingException;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static String obj2String(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static int obj2Int(Object obj) {
		if (obj != null) {
			return Integer.parseInt(obj.toString());
		} else {
			return 0;
		}
	}

	public static float obj2Float(Object obj) {
		if (obj != null) {
			return Float.parseFloat(obj.toString());
		} else {
			return 0;
		}
	}

	public static double obj2Double(Object obj) {
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		} else {
			return 0;
		}
	}

	public static String getLastStr(String str, int i) {
		if (str != "") {
			return str.substring(str.length() - i, str.length());
		} else {
			return "";
		}
	}

	public static String disposeComma(String str) {
		// str=str.replaceAll("^(,+)", "");
		String[] s = str.split(",");
		for (int i = 0; i < s.length; i++) {
			str = s[i] + ",";
		}
		return str;
	}

	public static String getMD5Str(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			try {
				byte[] b = s.getBytes("GBK");

				if (b[0] > 0) {
					result += s;
				} else if (b[0] < 0) {
					for (int j = 0; j < b.length; j++) {
						s = Integer.toHexString(b[j]);
						s = getLastStr(s, 2).toUpperCase();
						result += s;
					}
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	public static boolean isNotEmpty(String str) {
		if (str == null || "".equals(str))
			return false;
		return true;
	}
}
