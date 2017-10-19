package com.aibei.thridwy.haolong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.aibei.common.SoapUtil;
import com.aibei.thridwy.haolong.bean.GetCustomer;


public class HaolongServiceUtil {

	String xml = "";
	
	public static List<String> getHaoLongService(String urlString,String soapActionString,Object obj) throws IOException{

		URL url = new URL(urlString);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		
		
//		httpConn.setRequestProperty("Content-Length",lengthstr);
		httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
		httpConn.setRequestProperty("soapActionString",soapActionString);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		 
		OutputStream out = httpConn.getOutputStream();
		
		//通过bean的toString()方法读取xml字符串
		out.write(obj.toString().getBytes());
		out.close();

		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),"utf-8");
		BufferedReader in = new BufferedReader(isr);

		String inputLine;
		//BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.xml")));
		List<String> jsonArray = null;
		while ((inputLine = in.readLine()) != null)
		{
//			 WebserviceResultBean ret = SoapUtil.parseSoapMessage(inputLine);
		        try {
		            SOAPMessage msg = SoapUtil.formatSoapString(inputLine);
		            SOAPBody body = msg.getSOAPBody();
		            Iterator<SOAPElement> iterator = body.getChildElements();
		            Map<String, String> map = SoapUtil.PrintBody(iterator, null);
//		            jsonArray = SoapUtil.parseStrXmlToJsonArry("<物业项目查询><物业><WYID>33</WYID><物业编号>BHLDS</物业编号><物业名称>好邻邦大厦</物业名称><详细地址></详细地址><负责人></负责人><联系电话></联系电话><备注>关于本物业项目的说明</备注></物业></物业项目查询>");
		            try {
		            	String valueString = map.get("value");
		            	if(valueString.indexOf("There is no record") >= 0){
		            		jsonArray = new ArrayList<String>();
		            		jsonArray.add(valueString);
		            		return jsonArray;//"[{\"tag\":\"GetPropertyResult\"},{\"value\":\"error:07-There is no record(没有记录)\"}]";
		            	    //假数据测试
		            		/*String testXML = "<欠费查询><欠费><HTID>1358</HTID><计费月份>201409</计费月份><BZID>55</BZID><计费编号>01</计费编号><项目编号>0101</项目编号><项目名称>居民水费</项目名称><本月应收款>123.56</本月应收款><本月欠收款>123.56</本月欠收款><欠收往月>327.83</欠收往月><往月滞纳金>18.00</往月滞纳金><本次滞纳金>1.23</本次滞纳金><YSID>123</YSID></欠费><欠费><HTID>1358</HTID><计费月份>201409</计费月份><BZID>56</BZID><计费编号>10</计费编号><项目编号>1001</项目编号><项目名称>居民管理费</项目名称><本月应收款>88.00</本月应收款><本月欠收款>88.00</本月欠收款><欠收往月>700</欠收往月><往月滞纳金>27</往月滞纳金><本次滞纳金>3.56</本次滞纳金><YSID>124</YSID></欠费></欠费查询>";
		            		return SoapUtil.parseStrXmlToJsonArry(testXML);*/
		            	}
						jsonArray = SoapUtil.parseStrXmlToJsonArry(map.get("value"));
					} catch (Exception e) {
						e.printStackTrace();
					}
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			
			/*bw.write(inputLine);
			bw.newLine();*/
		}
		
	//	bw.close();
		in.close();
		
		return jsonArray;
	}
	
	public static List<Map<String, String>> toMapListService(String urlString,String soapActionString,Object obj) throws IOException{

		URL url = new URL(urlString);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		
		
//		httpConn.setRequestProperty("Content-Length",lengthstr);
		httpConn.setRequestProperty("Content-Type","text/xml; charset=gbk");
		httpConn.setRequestProperty("soapActionString",soapActionString);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		
		OutputStream out = httpConn.getOutputStream();
		//通过bean的toString()方法读取xml字符串
		out.write(obj.toString().getBytes());
		out.close();
		
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),"utf-8");
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		List<Map<String, String>> mapList = null;
		while ((inputLine = in.readLine()) != null)
		{
//			 WebserviceResultBean ret = SoapUtil.parseSoapMessage(inputLine);
		        try {
		            SOAPMessage msg = SoapUtil.formatSoapString(inputLine);
		            SOAPBody body = msg.getSOAPBody();
		            Iterator<SOAPElement> iterator = body.getChildElements();
		            Map<String, String> map = SoapUtil.PrintBody(iterator, null);
		            try {
		            	String valueString = map.get("value");
		            	if(valueString.indexOf("There is no record") >= 0){
		            		List<Map<String, String>> nullList = new ArrayList<Map<String,String>>();
		            		Map<String, String> nullMap = new HashMap<String, String>();
		            		nullMap.put("value", "error:07-There is no record(没有记录)");
		            		nullList.add(nullMap);
		            		return nullList;
		            		//"[{YSID=123, 本月欠收款=123.56, 本月应收款=123.56, 计费编号=01, HTID=1358, 本次滞纳金=1.23, 欠收往月=327.83, 项目编号=0101, 计费月份=201409, BZID=55, 往月滞纳金=18.00, 项目名称=居民水费}]";
		            	}
		            	mapList = SoapUtil.parseStrXmlToList(map.get("value"));
					} catch (Exception e) {
						e.printStackTrace();
					}
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			
		}
		in.close();
		return mapList;
	}
	
	public static void main(String[] args) throws Exception {
		String urlString = "http://sdfee.oklong.com/service.asmx";
		String soapActionString = "http://tempuri.org/sdfsf";
		/*GetProperty obj = new GetProperty();
		obj.setWYID("33");*/
		GetCustomer obj = new GetCustomer();
        obj.setCliName("黄雁萍");
        obj.setMobile("15999659364");
        obj.setPropID("33");
		getHaoLongService(urlString,soapActionString,obj);
	}

	public static String getPay(String urlString,String soapActionString,Object obj) throws IOException, SOAPException{

		URL url = new URL(urlString);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		
		
//		httpConn.setRequestProperty("Content-Length",lengthstr);
		httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
		httpConn.setRequestProperty("soapActionString",soapActionString);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		 
		OutputStream out = httpConn.getOutputStream();
		
		//通过bean的toString()方法读取xml字符串
		out.write(obj.toString().getBytes());
		out.close();

		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),"utf-8");
		BufferedReader in = new BufferedReader(isr);

		String inputLine;
		String valueString = "";
		while ((inputLine = in.readLine()) != null)
		{
//			WebserviceResultBean ret = SoapUtil.parseSoapMessage(inputLine);
            SOAPMessage msg = SoapUtil.formatSoapString(inputLine);
            SOAPBody body = msg.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            Map<String, String> map = SoapUtil.PrintBody(iterator, null);
            valueString = map.get("value");
		}
		
		in.close();
		
		return valueString;
	}
	
}

