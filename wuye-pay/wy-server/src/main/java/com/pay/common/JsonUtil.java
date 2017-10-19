package com.pay.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;





/***
 * json数据处理
 */
public class JsonUtil {
	/**
	 * 将json字符串转换成list对象
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> getMap4Json(String jsonString){
		if(jsonString == null || jsonString.equals("")){
			jsonString = "{}";
		}
		String json[] = jsonString.replace("},{", "}---{").split("---");
		String key;
		Object value;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>() ;
		net.sf.json.JSONObject jsonObject = null;
		for (int i = 0; i < json.length; i++) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			jsonObject = net.sf.json.JSONObject.fromObject(json[i]);
			Iterator<?>  keyIter = jsonObject.keys();
			while( keyIter.hasNext()){
				key = (String)keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key,value);
			}
			list.add(valueMap);
		}
		return list;
	}
	
	public static String getJsonValue(String rescontent, String key) {
		JSONObject jsonObject;
		String v = null;
		try {
			jsonObject = new JSONObject(rescontent);
			v = jsonObject.getString(key);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return v;
	}
	
//	public static void main(String args[]) throws JSONException{
//    	String jsonString = "{\"RetCode\":0,\"OrderStatus\":0,\"SignData\":\"transdata={\"appid\":\"3002811959\",\"appuserid\":\"weitou@iapppay.com\",\"cporderid\":\"1442483370230\",\"cpprivate\":\"7262/201509/,55,56\",\"currency\":\"RMB\",\"feetype\":0,\"money\":0.01,\"paytype\":501,\"result\":0,\"transid\":\"32021509171748598332\",\"transtime\":\"2015-09-17+17:49:41\",\"transtype\":0,\"waresid\":1}&sign=SNUR2VUymGyoSgdIcWD4TC2NJFEKhwL+VMfRCmKJYuREYF1MUPlQB62Gh2uyh+IN9NhTtdL7ozAv7/JjqdoLtwKYYpsmY+sOdHjs62jgpmBBxO2WxHUvQO+9LnHST7kmeMU5B7H48oOELF3f+3YFV3jiI8cYYmhHqd3Ig4vAaiE=&signtype=RSA\",\"Type\":0}";
//        String []str = jsonString.split("transdata=");
//        System.out.println(str[1]);
//        List<Map<String, Object>> mapList = getMap4Json(str[1]);
//        System.out.println(mapList.get(0).get("cpprivate"));
//    	
//    }
}
