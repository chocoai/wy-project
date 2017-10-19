package com.pay.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionSoapUtil {
  @SuppressWarnings("resource")
public static HttpURLConnection getSoapConn(String urlString,String soapActionString) throws FileNotFoundException, IOException{
		/*String urlString = "http://sdfee.oklong.com/Service.asmx";
		String soapActionString = "http://tempuri.org/GetMonthFee";//GetCustomer
*/		                         
		URL url = new URL(urlString);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		
		
		//1.xml编入请求信息
		String xmlFilepath = "D:\\xml\\2.xml";
		File fileToSend=new File(xmlFilepath);
		byte[] buf=new byte[(int)fileToSend.length()];
		 
		new FileInputStream(xmlFilepath).read(buf);
		 
		String lengthstr = String.valueOf( buf.length );
//		System.out.println("length : " + lengthstr );
		 
		httpConn.setRequestProperty("Content-Length",lengthstr);
		httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
		httpConn.setRequestProperty("soapActionString",soapActionString);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		//读入buf
		out.write(buf);
		out.close();
		return httpConn;
  }
  
  
//  public static void main(String args[]){
//	    String urlString = "http://sdfee.oklong.com/Service.asmx";
//		String soapActionString = "http://tempuri.org/GetMonthFee";//GetCustomer
//		try {
//			HttpURLConnection httpConn = getSoapConn(urlString, soapActionString);
//			//System.out.println(httpConn.getInputStream());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//  }
}
