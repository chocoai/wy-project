package com.pay.common;

import java.io.BufferedReader;
import java.io.IOException;

public class StreamUtil {
	
	public static String getBodyString(BufferedReader br) {
		String str = "";
		try {
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				// String inputLine;
				str = str + inputLine;
			}
			br.close();
		} catch (IOException e) {
			//System.out.println("IOException: " + e);
		}
		return str;
	}

}
