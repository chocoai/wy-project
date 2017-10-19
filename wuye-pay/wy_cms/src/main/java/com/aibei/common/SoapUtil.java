package com.aibei.common;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.aibei.thridwy.haolong.bean.GetProperty;



public class SoapUtil {

	/**
	 * 解析soapXML
	 * @param soapXML
	 * @return
	 */
	public static WebserviceResultBean parseSoapMessage(String soapXML) {
		WebserviceResultBean resultBean = new WebserviceResultBean();
		try {
			SOAPMessage msg = formatSoapString(soapXML);
			SOAPBody body = msg.getSOAPBody();
			Iterator<SOAPElement> iterator = body.getChildElements();
			parse(iterator, resultBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultBean;
	}

	public static void main(String[] args) {
		System.out.println("开始解析soap...");

		String testXML = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetCustomer xmlns=\"http://tempuri.org/\"><WYID>string</WYID><KHID>string</KHID><Arr_data><string>string</string><string>string</string></Arr_data><Key>string</Key></GetCustomer></soap:Body></soap:Envelope>";
		WebserviceResultBean ret = parseSoapMessage(testXML);

		try {
			SOAPMessage msg = formatSoapString(testXML);
			SOAPBody body = msg.getSOAPBody();
			Iterator<SOAPElement> iterator = body.getChildElements();
			PrintBody(iterator, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("解析soap结束...");
	}

	/**
	 * 把soap字符串格式化为SOAPMessage
	 * 
	 * @param soapString
	 * @return
	 */
	public static SOAPMessage formatSoapString(String soapString) {
		MessageFactory msgFactory;
		try {
			msgFactory = MessageFactory.newInstance();
			SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(),
					new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析soap xml
	 * @param iterator
	 * @param resultBean
	 */
	public static void parse(Iterator<SOAPElement> iterator, WebserviceResultBean resultBean) {
		while (iterator.hasNext()) {
			SOAPElement element = iterator.next();
			if ("ns:BASEINFO".equals(element.getNodeName())) {
				continue;
			} else if ("ns:MESSAGE".equals(element.getNodeName())) {
				Iterator<SOAPElement> it = element.getChildElements();
				SOAPElement el = null;
				while (it.hasNext()) {
					el = it.next();
					if ("RESULT".equals(el.getLocalName())) {
						resultBean.setResult(el.getValue());
						//System.out.println("#### " + el.getLocalName() + "  ====  " + el.getValue());
					} else if ("REMARK".equals(el.getLocalName())) {
						resultBean.setRemark(null != el.getValue() ? el.getValue() : "");
						//System.out.println("#### " + el.getLocalName() + "  ====  " + el.getValue());
					} else if ("XMLDATA".equals(el.getLocalName())) {
						resultBean.setXmlData(el.getValue());
						//System.out.println("#### " + el.getLocalName() + "  ====  " + el.getValue());
					}
				}
			} else if (null == element.getValue()
					&& element.getChildElements().hasNext()) {
				parse(element.getChildElements(), resultBean);
			}
		}
	}


	public static Map<String, String> PrintBody(Iterator<SOAPElement> iterator, String side) {
		Map<String, String> resultMap = new HashMap<String, String>();
		SOAPElement element = null;
		while (iterator.hasNext()) {
			element = (SOAPElement) iterator.next();
			if( null == element.getValue()
					&& element.getChildElements().hasNext()) {
				resultMap = PrintBody(element.getChildElements(), side + "-----");
			}
			if(element.getValue() != null){
				resultMap.put("tag", element.getTagName());
				resultMap.put("value", element.getValue());
			}

		}
		
		return resultMap;
	}

	/**
	 * 直接读取xml格式的字符串，解析成对象
	 * @param xml
	 * @throws DocumentException 
	 * @throws JSONException 
	 */
	public static List<String> parseStrXmlToJsonArry(String xml) throws JSONException, DocumentException{
		List<String>jsonList = new ArrayList<String>();
		Document document = DocumentHelper.parseText(xml);

		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element child : childElements) {
			List<Attribute> attributeList = child.attributes();
			for (Attribute attr : attributeList) {
				//System.out.println(attr.getName() + ": " + attr.getValue());
			}
			List<Element> elementList = child.elements();
			JSONArray jsonMembers = null;
			jsonMembers = new JSONArray();
			for (Element ele : elementList) {
				JSONObject member1 = new JSONObject(); 
				member1.put(ele.getName(), ele.getText());  
				jsonMembers.put(member1);  
			}
			jsonList.add(jsonMembers.toString());
		}
		return jsonList;
	}
	
	public static List<Map<String, String>> parseStrXmlToList(String xml) throws JSONException, DocumentException{
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		Map member1 = null;
		for (Element child : childElements) {
			List<Attribute> attributeList = child.attributes();
			for (Attribute attr : attributeList) {
				//System.out.println(attr.getName() + ": " + attr.getValue());
			}
			List<Element> elementList = child.elements();
			JSONArray jsonMembers = null;
			jsonMembers = new JSONArray();
			member1 = new HashMap<String, String>(); 
			for (Element ele : elementList) {
				
				member1.put(ele.getName(), ele.getText());  
				//jsonMembers.put(member1);  
			}
			mapList.add(member1);
		}
		return mapList;
	}

}