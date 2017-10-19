/**
 * 123213
 */

package com.pay.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.pay.bean.Urlmap;
import com.pay.common.StringUtils;
import com.pay.service.DataBaseService;
import com.pay.thridwy.haolong.bean.GetContractA;
import com.pay.thridwy.haolong.bean.GetLastMonth;
import com.pay.thridwy.haolong.utils.HaolongService;

@Controller
@RequestMapping("/contract")
public class ContractAction {
	@Autowired
	private HaolongService mHaolongService;

	@Autowired
	private DataBaseService mDataBaseService;

	private static Logger logger = LoggerFactory.getLogger("ContractAction.class");

	private String source = "";
	private String urlString = "";
	private String secretkey = "";
	private String soapActionString;

	/**
	 * 检查地址栏参数是否有效,source和from不能为null,feetype默认为0
	 * source-公司代码,from-支付入口{0:微信公众号移动支付
	 * ,1:显屏扫码支付},feetype-费用类型{0:常规费用,1:临时费用,2:零星费用,3:停车费,4:自定义}
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkParam.htm")
	@ResponseBody
	public String checkParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
		source = request.getParameter("source").equals("") ? null : request.getParameter("source");
		String from = request.getParameter("from").equals("") ? null : request.getParameter("from"); // 0:微信公众号
																										// ；
																										// 1:显屏扫一扫
		String feetype = request.getParameter("feetype"); // 0：常规费用；1：临时费用 ；
															// 2：零星费用

		//logger.info("检查地址栏参数是否有效入参|source={},from={},feetype={}", source, from, feetype);

		JSONObject json = new JSONObject();
		json.put("valid", false);
		json.put("feetype", feetype);
		if (null != source && null != from) {
			json.put("source", source);
			json.put("from", from);
			json.put("valid", true);
		}
		return json.toString();
	}

	/**
	 * 获取合同信息,如果WYID为空字符,则查询该用户名下所有WY合同信息
	 * 
	 * @param cliName
	 *            客户姓名
	 * @param moblie
	 *            联系方式
	 * @param WYID
	 *            物业编号
	 * @return json{cliName,moblie,WYID,urlString,source,contractList，ads-广告}
	 * @throws Exception
	 */
	@RequestMapping("/getContract.htm")
	@ResponseBody
	public String getContract(HttpServletRequest request, HttpServletResponse response) {

		if (request != null)
			try {
				init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String cliName = request.getParameter("cliName");
		String mobile = request.getParameter("mobile");
		String WYID = request.getParameter("WYID");
		String Sumflag = (String) request.getSession().getAttribute("Sumflag");
		if(StringUtils.isEmpty(Sumflag)){
			Sumflag = request.getParameter("Sumflag");
		}
		if(StringUtils.isEmpty(Sumflag)){
			Sumflag = "0";
		}
		request.getSession().setAttribute("Sumflag", Sumflag);

		String logstr = MessageFormat.format("获取合同信息|入参|cliName={0},mobile={1},WYID={2}", cliName, mobile, WYID);
		//logger.info(logstr);

		GetContractA getContractA = new GetContractA();
		getContractA.setCliName(cliName);
		getContractA.setMobile(mobile);
		getContractA.setWYID(WYID);
		getContractA.setUrlString(urlString);
		getContractA.setSoapActionString(soapActionString);
		getContractA.setSecretkey(secretkey);
		// 对调用合同接口进行异常处理
		List<Map<String, String>> ContractInfo = null;
		try {
			ContractInfo = mHaolongService.getContractA(getContractA);
			//logger.info("获取合同信息={}", JSON.toJSONString(ContractInfo) + "|" + logstr);

			if (ContractInfo.toString().contains("error")) {
				return null;
			}
		} catch (Exception e) {
			logger.error("getContractA()出错 : " + e.getMessage().toString());
			logger.error("ContractInfo ，出错返回字符串: " + ContractInfo.toString());
		}

		JSONObject json = new JSONObject();
		json.put("cliName", cliName);
		json.put("mobile", mobile);
		json.put("WYID", WYID);
		json.put("urlString", urlString);
		json.put("source", source);
		String KHID = "";
		String JFYF = "";
		List<Map<String, String>> contractList = new ArrayList<Map<String, String>>();
		for (Map<String, String> contract : ContractInfo) {
			WYID = contract.get("WYID");
			Map<String, String> map = new HashMap<String, String>();
			KHID = contract.get("KHID");
			if (KHID == "")
				return "";
			try {
				JFYF = getLastMonth(WYID);
			} catch (Exception e) {
				logger.error("getLastMonth() : " + e.getMessage().toString());
				logger.error("JFYF : " + JFYF);
				e.printStackTrace();
			}
			map.put("HTID", contract.get("HTID"));
			map.put("WYID", WYID);
			map.put("KHID", KHID);
			map.put("JFYF", JFYF);
			map.put("物业名称", contract.get("物业名称"));
			map.put("大楼名称", contract.get("大楼名称"));
			map.put("房号", contract.get("内部房号"));
			map.put("单元", (contract.get("单元名称") != null && !contract.get("单元名称").equals("")) ? contract.get("单元名称") + "单元" : "");

			contractList.add(map);
		}

		// 广告
		Ad ad = new Ad();
		ad.setWYID(WYID);
		ad.setSource(source);
		ad.setStatus("1"); // 有效状态
		List<Ad> ads = null;
		try {
			ads = mDataBaseService.getAd(ad);
		} catch (Exception e) {
			logger.error("ads : " + ads);
			e.printStackTrace();
		}
		System.out.println(ads.toString());
		json.put("ads", ads);
		json.put("contractList", contractList);

		HttpSession session = request.getSession();
		session.setAttribute("source", "");
		return json.toString();
	}

	private String getLastMonth(String wyid) throws Exception {
		GetLastMonth bean = new GetLastMonth();
		bean.setUrlString(urlString);
		bean.setSoapActionString(soapActionString);
		bean.setWYID(wyid);
		bean.setSecretkey(secretkey);

		List<Map<String, String>> list = mHaolongService.getLastMonth(bean);
		Map<String, String> map = list.get(0);
		String JFYF = map.get("最新月份");

		return JFYF;
	}

	private void init() throws Exception {
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(source);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		urlString = urlmap.getUrlstring();
		soapActionString = urlmap.getSoapactionstring();
		secretkey = urlmap.getSecretkey();
	}
	
	@RequestMapping("/getwyname.htm")
	@ResponseBody
	private String getwyname(HttpServletRequest request, HttpServletResponse response){
		String sources = request.getParameter("source").equals("") ? null : request.getParameter("source");
		String wyids = request.getParameter("WYID").equals("") ? null : request.getParameter("WYID");
		if(sources!=null && wyids!=null){
			Account account = new Account();
			account.setWYID(wyids);
			account.setSource(sources);
			account = mDataBaseService.getAccount(account);
			if(account != null){
				request.getSession().setAttribute("property", account.getProperty());
				return account.getProperty();
			}
		}
		return "false";	
	}
}
