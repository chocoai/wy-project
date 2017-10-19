package com.pay.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.bean.Account;
import com.pay.bean.Ad;
import com.pay.bean.Contract;
import com.pay.bean.Urlmap;
import com.pay.common.StringUtils;
import com.pay.service.DataBaseService;
import com.pay.thridwy.haolong.bean.GetContract;
import com.pay.thridwy.haolong.bean.GetLastMonth;
import com.pay.thridwy.haolong.bean.Getfee;
import com.pay.thridwy.haolong.bean.Getfee1;
import com.pay.thridwy.haolong.bean.Getfee2;
import com.pay.thridwy.haolong.utils.HaolongService;
import com.pay.thridwy.haolong.utils.HaolongUtil;

/**
 * @author Administrator
 * @category
 
   
 */
@Controller
@RequestMapping("/htmlAction")
public class HtmlAction {

	@Autowired
	private HaolongService mHaolongService;

	@Autowired
	private DataBaseService mDataBaseService;

	/*
	 * private String source = ""; private String secretkey = ""; private String
	 * urlString; private String soapActionString;
	 */

	private Logger log = LoggerFactory.getLogger(HtmlAction.class);

	/**
	 * 对缴费页面参数加密，采用MD5,返回密钥
	 * 
	 * @param feetype
	 *            x0
	 * @param WYID
	 *            x1
	 * @param HTID
	 *            x2
	 * @param JFYF
	 *            x3
	 * @param SOURCE
	 *            x4
	 * @return SIGN(x5)
	 * @throws Exception
	 */
	@RequestMapping("getSIGN.htm")
	@ResponseBody
	public String getSIGN(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String feetype = StringUtils.obj2String(request.getParameter("feetype"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String SOURCE = StringUtils.obj2String(request.getParameter("SOURCE"));

		String[] arr_data = { feetype, WYID, HTID, JFYF };
		String sign = HaolongUtil.encrypt2(arr_data, SOURCE);
		HttpSession session = request.getSession();
		session.setAttribute("SIGN", sign);

		//log.info("对缴费页面参数加密，采用MD5,返回密钥,入参:feetype={},WYID={},HTID={},JFYF={},SOURCE={};加密后SIGN={}", feetype, WYID, HTID, JFYF, SOURCE, sign);

		return sign;
	}

	/**
	 * 获取欠费信息,最新的缴费月份必须大于JFYF,getLastMonth获取最新缴费月份
	 * 
	 * @param feetype
	 *            x0
	 * @param WYID
	 *            x1
	 * @param HTID
	 *            x2
	 * @param JFYF
	 *            x3
	 * @param SOURCE
	 *            x4
	 * @param SING
	 *            x5
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doAction.htm")
	@ResponseBody
	public String doAction(HttpServletRequest request, HttpServletResponse response) {
		String feetype = StringUtils.obj2String(request.getParameter("feetype"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String SOURCE = StringUtils.obj2String(request.getParameter("SOURCE"));
		String SIGN = StringUtils.obj2String(request.getParameter("SIGN"));
		String url = StringUtils.obj2String(request.getParameter("url"));
		String secretkey = "";
		String urlString = "";
		String soapActionString = "";
		
		if(SOURCE!=null && WYID!=null){
			Account account = new Account();
			account.setWYID(WYID);
			account.setSource(SOURCE);
			account = mDataBaseService.getAccount(account);
			if(account != null){
				request.getSession().setAttribute("property", account.getProperty());
			}
		}

		// --添加日志20170508--by nlp
		String logstr = MessageFormat.format(":feetype={0},WYID={1},HTID={2},JFYF={3},SOURCE={4},SIGN={5}", feetype, WYID, HTID, JFYF, SOURCE, SIGN);
		//log.info(logstr);

		// source = SOURCE;
		boolean isValidData = false;
		String[] arr_data = { feetype, WYID, HTID, JFYF, url};
		String key1 = SOURCE;

		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(SOURCE);
		urlmap.setStatus("1");
		try {
			urlmap = mDataBaseService.getUrlmap(urlmap);

			// urlmap日志输出
			//log.info(logstr + ";urlmap={}", JSONObject.toJSONString(urlmap));

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		secretkey = urlmap.getSecretkey();

		if (SIGN != null && SIGN.equals(HaolongUtil.encrypt2(arr_data, key1))) {
			isValidData = true;
		}
		JSONObject json = new JSONObject();

		if (isValidData) {

			// 获取urlString和soapActionString

			urlString = urlmap.getUrlstring();
			soapActionString = "http://tempuri.org/";

			// 获取合同信息
			Contract contract = null;
			try {
				contract = getContract(HTID, urlString, soapActionString, secretkey, SOURCE);

				// 新增日志信息
				//log.info(logstr + ";拿到合同信息={}" + JSONObject.toJSONString(contract));

			} catch (Exception e) {
				log.error(logstr + ";getContract出错 : " + e.getMessage().toString());
				e.printStackTrace();
				return "error";
			}
			String KHMC = contract.getKHMC();
			String phone = contract.getPhone();
			String WYMC = contract.getWYMC().isEmpty() ? "" : contract.getWYMC();
			String DLMC = contract.getDLMC().isEmpty() ? "" : contract.getDLMC();
			String DYMC = contract.getDYMC().isEmpty() ? "" : contract.getDYMC();
			if (com.pay.common.utils.StringUtils.isNotBlank(DYMC)) {
				DYMC = DYMC + "单元";
			}
			String FH = contract.getNBFH().isEmpty() ? "" : contract.getNBFH();
			String addr = WYMC + DLMC + DYMC + FH;
			json.put("WYMC", WYMC);
			json.put("DLMC", DLMC);
			json.put("FH", FH);
			// 获取客户信息

			// 获取计费月份
			String JFYF2 = null;
			try {
				JFYF2 = getLastMonth(contract.getWYID(), urlString, soapActionString, secretkey);
				//JFYF = JFYF2;
				//log.info(logstr + ";获取计费月份:JFYF2={}", JFYF2);

			} catch (Exception e) {
				log.error("getContract : " + e.getMessage().toString());
				log.error("JFYF2 : " + JFYF2);
				e.printStackTrace();
				return "error";
			}
			// String JFYF = "";
			String JFYFstr = JFYF.substring(0, 4) + "年" + JFYF.substring(4) + "月";

			// 获取广告信息
			Ad ad = new Ad();
			ad.setSource(SOURCE);
			ad.setWYID(contract.getWYID());
			ad.setStatus("1"); // 有效状态
			List<Ad> ads = null;
			try {
				ads = mDataBaseService.getAd(ad);
			} catch (Exception e) {
				log.error("getAd : " + e.getMessage().toString());
				log.error("ads : " + ads);
				e.printStackTrace();
				return "error";
			}

			// 获取欠费信息
			List<Map<String, String>> feeList = null;
			try {
				feeList = getFee(HTID, JFYF, urlString, soapActionString, secretkey);
			} catch (Exception e) {
				log.error("getFee : " + e.getMessage().toString());
				log.error("feeList : " + feeList);
				e.printStackTrace();
				return "error";
			}
			int JFYFb = StringUtils.obj2Int(JFYF);
			int JFYFe = StringUtils.obj2Int(JFYF2);
			if (JFYFb <= JFYFe) {
				// 组合返回值
				// json.put("JFYF", JFYF);
				json.put("JFYFstr", JFYFstr);
				json.put("JFYF2", JFYF2);
				json.put("ads", ads);
				json.put("username", KHMC);
				json.put("phone", phone);
				json.put("addr", addr);
				json.put("feelist", feeList);
				json.put("HTID", HTID);
				// json.put("source", source);
				// json.put("WYID", contract.getWYID());
				json.put("urlString", urlmap.getUrlstring());
			} else {
				return null;
			}

		}

		json.put("valid", isValidData);
		return json.toString();
	}

	private List<Map<String, String>> getFee(String HTID, String JFYF, String urlString, String soapActionString, String secretkey) throws Exception {
		List<Map<String, String>> resultMaps = new ArrayList<Map<String, String>>();
		Getfee fee = new Getfee();
		fee.setHTID(HTID);
		fee.setJFYF(JFYF);
		fee.setUrlString(urlString);
		fee.setSoapActionString(soapActionString);
		fee.setSecretkey(secretkey);

		resultMaps = mHaolongService.getFee(fee);
		return resultMaps;
	}

	@RequestMapping("doAction1.htm")
	@ResponseBody
	public String doAction1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String feetype = StringUtils.obj2String(request.getParameter("feetype")); //
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String SOURCE = StringUtils.obj2String(request.getParameter("SOURCE"));
		String SIGN = StringUtils.obj2String(request.getParameter("SIGN"));
		String url = StringUtils.obj2String(request.getParameter("url"));
		String secretkey = "";
		String urlString = "";
		String soapActionString = "";

		boolean isValidData = false;
		String[] arr_data = { feetype, WYID, HTID, JFYF, url }; //
		String key1 = SOURCE;
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(SOURCE);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		secretkey = urlmap.getSecretkey();

		if (SIGN != null && SIGN.equals(HaolongUtil.encrypt2(arr_data, key1))) {
			isValidData = true;
		}
		JSONObject json = new JSONObject();
		if (isValidData) {

			urlString = urlmap.getUrlstring();
			soapActionString = "http://tempuri.org/";

			// 获取合同信息
			Contract contract = getContract(HTID, urlString, soapActionString, secretkey, SOURCE);
			String KHMC = contract.getKHMC();
			String phone = contract.getPhone();
			String WYMC = contract.getWYMC().isEmpty() ? "" : contract.getWYMC();
			String DLMC = contract.getDLMC().isEmpty() ? "" : contract.getDLMC();
			String DYMC = contract.getDYMC().isEmpty() ? "" : contract.getDYMC();
			if (com.pay.common.utils.StringUtils.isNotBlank(DYMC)) {
				DYMC = DYMC + "单元";
			}

			String FH = contract.getNBFH().isEmpty() ? "" : contract.getNBFH();
			String addr = WYMC + DLMC + DYMC + FH;
			json.put("WYMC", WYMC);
			json.put("DLMC", DLMC);
			json.put("FH", FH);
			// 获取广告信息
			Ad ad = new Ad();
			ad.setSource(SOURCE);
			ad.setWYID(contract.getWYID());
			ad.setStatus("1"); // 有效状态
			List<Ad> ads = mDataBaseService.getAd(ad);

			// 获取欠费信息
			// List<Map<String, String>> feeList = getFee(HTID, JFYF);
			List<Map<String, String>> feeList = getFee1(HTID, urlString, soapActionString, secretkey);
			// for(Map<String,String> map:feeList){
			// System.out.println("-------");
			// for(Entry<String, String> entry:map.entrySet()){
			// System.out.println(entry.getKey()+"-"+entry.getValue());
			// }
			// }
			// 组合返回值
			json.put("ads", ads);
			json.put("username", KHMC);
			json.put("phone", phone);
			json.put("addr", addr);
			json.put("feelist", feeList);
			json.put("HTID", HTID);
			json.put("urlString", urlString);

		}
		json.put("valid", isValidData);
		// System.out.println(json.get("valid"));
		// System.out.println("json: " + json.toString());
		return json.toString();
	}

	private List<Map<String, String>> getFee1(String HTID, String urlString, String soapActionString, String secretkey) throws Exception {
		List<Map<String, String>> resultMaps = new ArrayList<Map<String, String>>();
		Getfee1 fee1 = new Getfee1();
		fee1.setHTID(HTID);
		fee1.setUrlString(urlString);
		fee1.setSoapActionString(soapActionString);
		fee1.setSecretkey(secretkey);
		resultMaps = mHaolongService.getFee1(fee1);
		return resultMaps;
	}

	@RequestMapping("doAction2.htm")
	@ResponseBody
	public String doAction2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String feetype = StringUtils.obj2String(request.getParameter("feetype")); //
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String SOURCE = StringUtils.obj2String(request.getParameter("SOURCE"));
		String SIGN = StringUtils.obj2String(request.getParameter("SIGN"));
		String from = StringUtils.obj2String(request.getParameter("from"));
		String url = StringUtils.obj2String(request.getParameter("url"));
		String price = StringUtils.obj2String(request.getParameter("price"));
		String bzid = StringUtils.obj2String(request.getParameter("bzid"));
		String secretkey = "";
		String urlString = "";
		String soapActionString = "";
		request.getSession().setAttribute("url", url);

		boolean isValidData = true;
		String[] arr_data = { feetype, WYID, HTID, JFYF, url}; //
		String key1 = SOURCE;
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(SOURCE);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		secretkey = urlmap.getSecretkey();

		if (SIGN != null && SIGN.equals(HaolongUtil.encrypt2(arr_data, key1))) {
			isValidData = true;
		}
		JSONObject json = new JSONObject();
		if (isValidData) {

			urlString = urlmap.getUrlstring();
			soapActionString = "http://tempuri.org/";

			String KHMC = "";
			String phone = "";
			String WYMC = "";
			String DLMC = "";
			String DYMC = "";
			String FH = "";

			if (HTID.equals("0")) {

			} else {
				// 获取合同信息
				Contract contract = getContract(HTID, urlString, soapActionString, secretkey, SOURCE);
				KHMC = contract.getKHMC();
				phone = contract.getPhone();
				WYMC = contract.getWYMC().isEmpty() ? "" : contract.getWYMC();
				DLMC = contract.getDLMC().isEmpty() ? "" : contract.getDLMC();
				DYMC = contract.getDYMC().isEmpty() ? "" : contract.getDYMC();
				FH = contract.getNBFH().isEmpty() ? "" : contract.getNBFH();
			}
			if (com.pay.common.utils.StringUtils.isNotBlank(DYMC)) {
				DYMC = DYMC + "单元";
			}

			String addr = WYMC + DLMC + DYMC + FH;

			json.put("WYMC", WYMC);
			json.put("DLMC", DLMC);
			json.put("FH", FH);
			// 获取广告信息
			Ad ad = new Ad();
			ad.setSource(SOURCE);
			ad.setWYID(WYID);
			ad.setStatus("1"); // 有效状态
			List<Ad> ads = mDataBaseService.getAd(ad);

			// 获取零星费用信息
			// List<Map<String, String>> feeList = getFee(HTID, JFYF);
			List<Map<String, String>> feeList = getFee2(WYID, urlString, soapActionString, secretkey);
			if(StringUtils.isNotBlank(bzid) && feeList!=null){
				List<Map<String, String>> tempfeeList = new ArrayList<Map<String, String>>();
				for(Map<String, String> fee : feeList){
					if(bzid.equals(fee.get("BZID"))){
						tempfeeList.add(fee);
					}
				}
				feeList = tempfeeList;
			}
			// for(Map<String,String> map:feeList){
			// System.out.println("-------");
			// for(Entry<String, String> entry:map.entrySet()){
			// System.out.println(entry.getKey()+"-"+entry.getValue());
			// }
			// }
			// 组合返回值
			json.put("ads", ads);
			json.put("username", KHMC);
			json.put("phone", phone);
			json.put("addr", addr);
			json.put("feelist", feeList);
			json.put("HTID", HTID);
			json.put("urlString", urlString);
			json.put("price", price);

		}
		json.put("valid", isValidData);
		// System.out.println(json.get("valid"));
		return json.toString();
	}
	
	private List<Map<String, String>> getFee2(String WYID, String urlString, String soapActionString, String secretkey) throws Exception {
		List<Map<String, String>> resultMaps = new ArrayList<Map<String, String>>();
		Getfee2 fee2 = new Getfee2();
		fee2.setWYID(WYID);
		fee2.setUrlString(urlString);
		fee2.setSoapActionString(soapActionString);
		fee2.setSecretkey(secretkey);
		resultMaps = mHaolongService.getFee2(fee2);
		return resultMaps;
	}

	private Contract getContract(String HTID, String urlString, String soapActionString, String secretkey, String source) throws Exception {
		GetContract bean = new GetContract();
		bean.setHTID(HTID);
		bean.setKHID("");
		bean.setUrlString(urlString);
		bean.setSoapActionString(soapActionString);
		bean.setSecretkey(secretkey);

		List<Map<String, String>> contractList = mHaolongService.getContract(bean);
		Contract contract = new Contract();
		if (contractList != null && contractList.size() > 0) {
			Map<String, String> map = contractList.get(0);
			contract.setHTID(map.get("HTID"));
			contract.setSource(source);
			// 查询本地数据库
			// List<Contract> db_contract =
			// mDataBaseService.getContract(contract);
			contract.setWYID(map.get("WYID"));
			contract.setLYID(map.get("LYID"));
			contract.setFWID(map.get("FWID"));
			contract.setKHID(map.get("KHID"));
			contract.setWYMC(map.get("物业名称"));
			contract.setDLMC(map.get("大楼名称"));
			contract.setKHMC(map.get("客户名称"));
			contract.setPhone(map.get("手机"));
			contract.setDLBH(map.get("大楼编号"));
			contract.setDYMC(map.get("单元名称"));
			contract.setDYXH(map.get("单元序号"));
			contract.setLCH(map.get("楼层号"));
			contract.setFXH(map.get("房序号"));
			contract.setFH(map.get("房号"));
			contract.setNBFH(map.get("内部房号"));
			contract.setYZ(map.get("业主"));
			contract.setJZMJ(map.get("建筑面积"));
			contract.setZYZT("false".equals(map.get("止约状态")) ? "0" : "1");
			contract.setZYRQ(map.get("止约日期"));
			contract.setTSDM(map.get("托收代码"));
			contract.setYHZH(map.get("银行帐号"));
			contract.setYHDM(map.get("银行代码"));
			contract.setKHM(map.get("开户名"));
			// 验证数据是否已经存在，不存在则添加，存在则修改
			// String time = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			// if (db_contract != null && db_contract.size() > 0) {
			// contract.setUpdatetime(time);
			// mDataBaseService.updateContract(contract);
			// } else {
			// contract.setCreatetime(time);
			// mDataBaseService.saveContract(contract);
			// }
		}
		return contract;
	}

	/**
	 * 获取计费月份
	 * 
	 * @param wyid
	 * @return
	 * @throws Exception
	 */
	private String getLastMonth(String wyid, String urlString, String soapActionString, String secretkey) throws Exception {
		GetLastMonth bean = new GetLastMonth();
		bean.setUrlString(urlString);
		bean.setSoapActionString(soapActionString);
		bean.setWYID(wyid);
		bean.setSecretkey(secretkey);

		log.info("获取计费月份;wyid={},GetLastMonth={}", wyid, JSON.toJSONString(bean));

		List<Map<String, String>> list = mHaolongService.getLastMonth(bean);
		Map<String, String> map = list.get(0);
		String JFYF = map.get("最新月份");

		return JFYF;
	}

	@RequestMapping("getLM.htm")
	@ResponseBody
	public JSONObject getLM(String WYID, String source) throws Exception {
		// System.out.println(WYID);
		JSONObject json = new JSONObject();
		Urlmap urlMap = new Urlmap();
		urlMap.setUrlkey(source);
		urlMap = mDataBaseService.getUrlmap(urlMap);
		GetLastMonth bean = new GetLastMonth();
		bean.setUrlString(urlMap.getUrlstring());
		bean.setSoapActionString(urlMap.getSoapactionstring());
		bean.setWYID(WYID);
		bean.setSecretkey(urlMap.getSecretkey());
		Map<String, String> map = mHaolongService.getLastMonth(bean).get(0);
		json.put("lastMonth", map.get("最新月份"));
		return json;
	}

	/*
	 * 根据固定物业和公司获取零星收费固定签名 应用场景，通过二维码扫码进行零星缴费
	 */
	/*
	 * public static void main(String[] args) throws Exception{ String feetype =
	 * "2"; String WYID = "33"; String HTID = "0"; String JFYF = ""; String
	 * SOURCE = "haolong"; String [] arr_data={feetype,WYID,HTID,JFYF}; String
	 * sign=HaolongUtil.encrypt2(arr_data, SOURCE); System.out.println(sign);
	 * 
	 * GetContract bean = new GetContract(); bean.setHTID("173418");
	 * bean.setKHID("");
	 * bean.setUrlString("http://hfwy99.imwork.net:2020/sdservice/service.asmx"
	 * ); bean.setSoapActionString("http://tempuri.org/");
	 * bean.setSecretkey("oklong_iapppay1");
	 * 
	 * HaolongService mHaolongService1 = new HaolongServiceImpl();
	 * List<Map<String, String>> contractList =
	 * mHaolongService1.getContract(bean);
	 * 
	 * int i = 0; }
	 */
	
	@RequestMapping("getJFYF.htm")
	@ResponseBody
	public JSONObject getJFYF(String wyid, String source) throws Exception {
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(source);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		
		GetLastMonth bean = new GetLastMonth();
		bean.setUrlString(urlmap.getUrlstring());
		bean.setSoapActionString(urlmap.getSoapactionstring());
		bean.setWYID(wyid);
		bean.setSecretkey(urlmap.getSecretkey());

		log.info("获取计费月份;wyid={},GetLastMonth={}", wyid, JSON.toJSONString(bean));

		List<Map<String, String>> list = mHaolongService.getLastMonth(bean);
		Map<String, String> map = list.get(0);
		String JFYF = map.get("最新月份");
		
		JSONObject json = new JSONObject();
		json.put("jfyf", JFYF);
		return json;
	}
	
	/*
	 * 根据source和wyid获取sumflag
	 */
	@RequestMapping("getSumflag.htm")
	@ResponseBody
	public Integer getSumflag(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String source = request.getParameter("source");
		String wyid = request.getParameter("wyid");

		Account account = new Account();
		account.setSource(source);
		account.setWYID(wyid);
		account.setStatus("1");
		try{
			account = mDataBaseService.getAccountList(account).get(0);
			return account.getSumflag();
		}catch(Exception e){
			return 0;
		}
	}
}
