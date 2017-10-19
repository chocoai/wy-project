package com.pay.thridwy.haolong.bean;


import com.pay.thridwy.haolong.utils.HaolongUtil;

public class Getfee1 {
	
	private String action = "Getfee1";
	private String secretkey;
	
	private String HTID;
	private String urlString;// = "http://sdfee.oklong.com/service.asmx";
	private String soapActionString;// = "http://tempuri.org/Getfee";
	
	public String getHTID() {
		return HTID;
	}

	public void setHTID(String hTID) {
		HTID = hTID;
	}

	

	public String getUrlString() {
		return this.urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getSoapActionString() {
		return this.soapActionString;
	}

	public void setSoapActionString(String soapActionString) {
		this.soapActionString = soapActionString + action;
	}
	
	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String toString() {
		String timestamp = "" + System.currentTimeMillis();
		String[] arr_data = { HTID};
		String key = HaolongUtil.encrypt(arr_data, timestamp, secretkey);
		String result =
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<Getfee1 xmlns=\"http://tempuri.org/\">" +
			"<HTID>"+HTID+"</HTID>" +
			"<TimeStamp>"+timestamp+"</TimeStamp>" +
			"<Key>"+key+"</Key>" +
			"</Getfee1>" +
			"</soap:Body>" +
			"</soap:Envelope>";
		return result;
	}
	
//	public static void main(String[] args) {
//		Getfee1 gf=new Getfee1();
//		HaolongServiceImpl s= new HaolongServiceImpl();
//		gf.setHTID("6948");
//		gf.setSecretkey("oklong_iapppay");
//		gf.setSoapActionString("");
//		gf.setUrlString("http://sdfee.oklong.com/service.asmx");
//		try {
//			List<Map<String,String>> list=s.getFee1(gf);
//			for(Map<String,String> map:list){
//				System.out.println("-------------");
//				for(Entry<String, String> entry:map.entrySet()){
//					System.out.println(entry.getKey()+"-"+entry.getValue());
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
