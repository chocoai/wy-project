package com.pay.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.pay.bean.Account;
import com.pay.bean.Order;
import com.pay.bean.PayInfo;
import com.pay.bean.Wares;
import com.pay.common.JsonUtil;
import com.pay.common.StringUtils;
import com.pay.common.constants.PayTypeConst;
import com.pay.common.sign.SignHelper;
import com.pay.common.utils.CacheUtils;
import com.pay.common.utils.OrderUtils;
import com.pay.common.utils.ServletUtil;
import com.pay.service.AibeiService;
import com.pay.service.DataBaseService;

@Controller
@RequestMapping("/getTransId")
public class GetTransIdAction {

	@Autowired
	private AibeiService mAibeiService;

	@Autowired
	private DataBaseService mDataBaseService;

	private static Logger logger = Logger.getLogger(GetTransIdAction.class);

	/**
	 * 接口二 通过应用appId调用商户服务端去查找要传入收银台的订单transId 参数 appid 平台分配的应用编号,waresid
	 * 应用中的商品编号, cporderid 商户生成的订单号，price 支付金额(非必填),currency 货币类型, appuserid
	 * 用户id建议为用户帐号,notifyurl 商户服务端接收支付结果通知的地址,waresname 商品名称, sign
	 * 对transdata的签名数据 ,signtype 签名算法类型
	 * 
	 * @return
	 */
	@RequestMapping("getSignStr.htm")
	@ResponseBody
	public String getSignStr(HttpServletRequest request){
		String transid = StringUtils.obj2String(request.getParameter("transId"));
		String redirecturl = StringUtils.obj2String(request.getParameter("redirecturl"));
		String cpurl = StringUtils.obj2String(request.getParameter("cpurl"));

		logger.info("获取签名入参|transid=" + transid + ";redirecturl=" + redirecturl + ";cpurl=" + cpurl);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("transid", transid);
		jsonObject.put("redirecturl", redirecturl);
		jsonObject.put("cpurl", cpurl);
		String content = jsonObject.toString();
		String appv_key = StringUtils.obj2String(request.getSession().getAttribute("appv_key"));
		String sign = "";
		try{
			sign = SignHelper.sign(content, appv_key);
		}catch(Exception ex){
			logger.error(ex.getMessage());
			return "fail";
		}

		logger.info("获取签名出参|sign=" + sign);

		return sign;
	}

	/**
	 * 获取商品号为1的爱贝流水号
	 */
	@RequestMapping("getTransId.htm")
	@ResponseBody
	public String getTransId(HttpServletRequest request) throws Exception {
		logger.info("获取爱贝流水号入参=" + ServletUtil.getParams(request));
		logger.info("getTransId.htm请求的sessionid=" + request.getSession().getId());

		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String waresid = StringUtils.obj2String(request.getParameter("waresid"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		float price = StringUtils.obj2Float(request.getParameter("price"));
		String BZIDlist = StringUtils.obj2String(request.getParameter("BZIDlist"));
		String feiList = StringUtils.obj2String(request.getParameter("feiList"));
		String source = StringUtils.obj2String(request.getParameter("source"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String urlString = StringUtils.obj2String(request.getParameter("urlString"));
		String WYMC = StringUtils.obj2String(request.getParameter("WYMC"));
		String DLMC = StringUtils.obj2String(request.getParameter("DLMC"));
		String FH = StringUtils.obj2String(request.getParameter("FH"));
		String customerName = StringUtils.obj2String(request.getParameter("customerName") == null ? "" : request.getParameter("customerName"));
		String feetype = StringUtils.obj2String(request.getParameter("feetype"));
		String from = StringUtils.obj2String(request.getParameter("from"));
		String transid = "";

		/* 思路一
		 * -------------------------------------
		 * 根据WYID和source确定账户，根据账户的platsystem确定走哪个支付平台
		 * 判断是爱贝平台还是好邻邦平台,好邻邦平台的Account可能不止一个
		 * -----------------------------------------
		 * 思路二
		 * 后台有多少个满足条件的accout，就显示多少个，每种支付方式封装成一个接口
		 */
		// 查询账号列表
		List<Account> accountList = new ArrayList<Account>();
		Account account2 = new Account();
		account2.setWYID(WYID);
		account2.setSource(source);

		logger.info("查询可用的支付方式即账号列表,参数：WYID=" + WYID + ";source=" + source);

		accountList = mDataBaseService.getAccountList(account2);
		logger.info("账号查询条数accountList.size=" + accountList == null ? null : accountList.size());

		// 订单编号
		String cporderid = OrderUtils.getOrderNo(source);
		String respStr = null;
		if (accountList != null) {
			// 商品信息和回调地址
			Wares wares = new Wares();
			wares.setWaresid(waresid);
			wares = mDataBaseService.getWares(wares);

			String appid = null;// 平台分配的应用编号
			String appuserid = null;// 用户在商户应用的唯一标识，建议为用户帐号
			String appv_key = null;// 服务商秘钥

			String notifyurl = null;// 支付成功通知地址
			String requesturl = null;// 前台回调地址
			String waresname = null;// 商品名称

			// cpprivate作为二次验证值使用
			String cpprivate = new Date().getTime() + "__" + urlString;
			String currency = "RMB";
			String platsystem = "";

			// 获取session
			HttpSession session = request.getSession();
			if (accountList.size() == 1) {
				Account account = accountList.get(0);

				appid = account.getAppid();// 平台分配的应用编号
				appuserid = account.getAppuserid();// 用户在商户应用的唯一标识，建议为用户帐号
				appv_key = account.getAppv_key();// 服务商秘钥

				notifyurl = wares.getNotifyurl();
				requesturl = wares.getRequesturl();
				waresname = wares.getWaresname();

				// 爱贝支付
				if (PayTypeConst.PAY_TYPE_PLATFORM_AIBEI.equals(account.getPlatsystem())) {
					platsystem = "0";
					session.setAttribute("appv_key", appv_key);
					/*--------------------------------------------------
					 * 下单前(根据account的数量和状态)区分后续逻辑，如果是爱贝，按原来逻辑走
					 * 如果不是爱贝,先跳出支付平台选择，比如好邻邦微信或好邻邦支付宝，根据选择继续走
					 * -------------------------------------------------
					 */
					// 接入爱贝后台进行下单，获取transID
					PayInfo payInfo = new PayInfo();
					payInfo.setAppid(appid);
					payInfo.setWaresid(Integer.parseInt(waresid));
					payInfo.setCporderid(cporderid);
					payInfo.setPrice(price);
					//payInfo.setPrice(1f);
					payInfo.setCurrency(currency);
					payInfo.setAppuserid(appuserid);// 测试账号
					payInfo.setWaresname(waresname);
					payInfo.setNotifyurl(notifyurl);
					payInfo.setSigntype("RSA");
					payInfo.setAppv_key(appv_key);
					payInfo.setCpprivateinfo(DigestUtils.md5Hex(cpprivate));

					logger.info("接入爱贝后台进行下单,payInfo=" + JSON.toJSONString(payInfo));
					// 成功时:{"transid":"11111"}
					// 失败时:{"code":"1001","errmsg":"签名验证失败"}
					String result = mAibeiService.getTransId(payInfo);
					logger.info("預支付獲取单号,下单后返回的数据:" + result);

					// 下单完成后，把transID更新进订单数据中
					JSONObject json = new JSONObject();
					json.put("requesturl", requesturl);
					if (result.contains("transid")) {
						List<Map<String, Object>> mapList = JsonUtil.getMap4Json(result);
						Map<String, Object> map = mapList.get(0);
						transid = map.get("transid").toString();
						json.put("transid", map.get("transid"));
					}

					respStr = json.toString();
					// 微信支付或者支付宝支付
				} else if (PayTypeConst.PAY_TYPE_PLATFORM_WEIXIN.equals(account.getPlatsystem()) || PayTypeConst.PAY_TYPE_PLATFORM_ALIPAY.equals(account.getPlatsystem())) {
					respStr = "oklongpay";
				}
			} else if (checkOklongPlatform(accountList)) {// 多種选择中，豪龙支付
				respStr = "oklongpay";
			} else {
				// 其他支付方式
				respStr = "otherpay";
			}
			Order order = saveOrder(appid, waresid, price, appuserid, cpprivate, HTID, JFYF, BZIDlist, feiList, cporderid, currency, source, WYID, WYMC, DLMC, FH, customerName,
					transid, feetype, platsystem, from);

			logger.info("产生的订单order=" + JSON.toJSONString(order));

			// 缓存session信息
			session.setAttribute("orderid", order.getCporderid());
			String SIGN = (String) session.getAttribute("SIGN");
			CacheUtils.put("accountList_" + cporderid, accountList);
			CacheUtils.put("order_" + cporderid, order);
			CacheUtils.put("wares_" + cporderid, wares);
			CacheUtils.put("SIGN_" + cporderid, SIGN);

		} else {
			logger.error("数据库支付账号配置错误,查询的数据条数为0");
			throw new Exception("数据库支付账号配置错误,查询的数据条数为0");
		}

		logger.info("支付类型选择respStr=" + respStr);

		return respStr;
	}

	/**
	 * 多条支付方式,校验是否为豪龙支付
	 * 
	 * @param accountList
	 * @return
	 */
	private boolean checkOklongPlatform(List<Account> accountList) {
		boolean b = true;
		for (Account account : accountList) {
			if (!(PayTypeConst.PAY_TYPE_PLATFORM_WEIXIN.equals(account.getPlatsystem()) || PayTypeConst.PAY_TYPE_PLATFORM_ALIPAY.equals(account.getPlatsystem()))) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * 获取商品号为2的爱贝流水号
	 */
	@RequestMapping("getTransId1.htm")
	@ResponseBody
	public String getTransId1(HttpServletRequest request) throws Exception {

		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		float price = StringUtils.obj2Float(request.getParameter("price"));
		String waresid = StringUtils.obj2String(request.getParameter("waresid"));
		String BZIDlist = StringUtils.obj2String(request.getParameter("BZIDlist"));
		String source = StringUtils.obj2String(request.getParameter("source"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String urlString = StringUtils.obj2String(request.getParameter("urlString"));
		String WYMC = StringUtils.obj2String(request.getParameter("WYMC"));
		String DLMC = StringUtils.obj2String(request.getParameter("DLMC"));
		String FH = StringUtils.obj2String(request.getParameter("FH"));
		String customerName = StringUtils.obj2String(request.getParameter("customerName") == null ? "" : request.getParameter("customerName"));

		Account account = new Account();
		account.setWYID(WYID);
		account.setSource(source);
		account = mDataBaseService.getAccount(account);

		String appid = account.getAppid();
		String appuserid = account.getAppuserid();
		String appv_key = account.getAppv_key();
		Wares wares = new Wares();
		wares.setWaresid(waresid);
		// System.out.println(waresid);
		wares = mDataBaseService.getWares(wares);
		// System.out.println(wares.toString());
		String notifyurl = wares.getNotifyurl();
		String requesturl = wares.getRequesturl();
		String waresname = wares.getWaresname();
		JSONObject json = new JSONObject();
		json.put("requesturl", requesturl);
		// cpprivate作为二次验证值使用
		String cpprivate = new Date().getTime() + "__" + urlString;
		String cporderid = source + "_" + new Date().getTime();
		String currency = "RMB";
		// 下单（生成transID）前保存订单信息
		saveOrder1(appid, waresid, price, appuserid, cpprivate, HTID, BZIDlist, cporderid, currency, source, WYID, WYMC, DLMC, FH, customerName);
		HttpSession session = request.getSession();
		session.setAttribute("appv_key", appv_key);
		// 接入爱贝后台进行下单，获取transID
		PayInfo payInfo = new PayInfo();
		payInfo.setAppid(appid);
		payInfo.setWaresid(Integer.parseInt(waresid));
		payInfo.setCporderid(cporderid);
		payInfo.setPrice(price);
		payInfo.setCurrency("RMB");
		payInfo.setAppuserid(appuserid);// 测试账号
		payInfo.setWaresname(waresname);
		payInfo.setNotifyurl(notifyurl);
		payInfo.setSigntype("RSA");
		payInfo.setAppv_key(appv_key);
		payInfo.setCpprivateinfo(DigestUtils.md5Hex(cpprivate));
		// System.out.println(payInfo.toString());
		// 成功时:{"transid":"11111"}
		// 失败时:{"code":"1001","errmsg":"签名验证失败"}
		String result = mAibeiService.getTransId(payInfo);
		logger.info("下单后返回的数据:" + result);
		// 下单完成后，把transID更新进订单数据中
		if (result.contains("transid")) {
			List<Map<String, Object>> mapList = JsonUtil.getMap4Json(result);
			Map<String, Object> map = mapList.get(0);
			updateOrder(map.get("transid").toString(), cporderid);
			json.put("transid", map.get("transid"));
		}
		return json.toString();
	}

	// 下单（生成transID）前保存订单信息
	private Order saveOrder(String appid, String waresid, float price, String appuserid, String cpprivate, String HTID, String JFYF, String BZIDlist, String feiList,
			String cporderid, String currency, String source, String WYID, String property, String building, String room, String customername, String transid, String feetype, String platsystem, String from)
			throws Exception {
		Order order = new Order();
		order.setAppid(appid.equals("")?null:appid);
		order.setWaresid(waresid);
		order.setPrice(price);
		order.setAppuserid(appuserid.equals("")?null:appuserid);
		order.setCpprivate(cpprivate);
		order.setHTID(HTID);
		order.setJFYF(JFYF);
		order.setBZIDList(BZIDlist);
		order.setFeiList(feiList);
		order.setTransid(transid);
		order.setTranstime("");
		order.setResult("2"); // 交易结果：0–交易成功 ,1–交易失败,2-未交易
		order.setOrderstatus("2"); // 0:支付成功,1:支付失败,2:待支付,3:正在处理,4:系统异常
		order.setSKID("");
		order.setPaystatus("8"); // 8-微未付
		order.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		order.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		order.setPaytype("");
		order.setCporderid(cporderid);
		order.setCurrency(currency);
		if(feetype==null || feetype.equals("")){
			feetype = "0";
		}
		order.setFeetype(feetype);
		order.setTranstype("");
		order.setSource(source);
		order.setWYID(WYID);
		order.setProperty(property);
		order.setBuilding(building);
		order.setRoom(room);
		order.setCustomername(customername);
		order.setPlatsystem(platsystem);
		order.setFrom(from);
		// System.out.println(order.toString());
		mDataBaseService.saveOrder(order);

		return order;
	}

	// 下单（生成transID）前保存订单信息
	private void saveOrder1(String appid, String waresid, float price, String appuserid, String cpprivate, String HTID, String BZIDlist, String cporderid, String currency,
			String source, String WYID, String proporty, String building, String room, String customername) throws Exception {
		Order order = new Order();
		order.setAppid(appid);
		order.setWaresid(waresid);
		order.setPrice(price);
		order.setAppuserid(appuserid);
		order.setCpprivate(cpprivate);
		order.setHTID(HTID);
		order.setBZIDList(BZIDlist);
		order.setTransid("");
		order.setTranstime("");
		order.setResult("2"); // 交易结果：0–交易成功 ,1–交易失败,2-未交易
		order.setOrderstatus("2"); // 0:支付成功,1:支付失败,2:待支付,3:正在处理,4:系统异常
		order.setSKID("");
		order.setPaystatus("8"); // 8-微未付
		order.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		order.setUpdatetime("");
		order.setPaytype("");
		order.setCporderid(cporderid);
		order.setCurrency(currency);
		order.setFeetype("");
		order.setTranstype("");
		order.setSource(source);
		order.setWYID(WYID);
		order.setProperty(proporty);
		order.setBuilding(building);
		order.setRoom(room);
		order.setCustomername(customername);
		mDataBaseService.saveOrder(order);
	}

	private void updateOrder(String transid, String cporderid) throws Exception {
		Order order = new Order();
		if(transid!=null && !transid.equals("")){
			order.setTransid(transid);
		}
		if(cporderid!=null && !cporderid.equals("")){
			order.setCporderid(cporderid);
		}
		
		order.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		mDataBaseService.updateOrder(order);
	}
	
	@RequestMapping("updatePlat.htm")
	@ResponseBody
	private void updatePlat(HttpServletRequest request) throws Exception {
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		HttpSession session = request.getSession();
		String cporderid = (String) session.getAttribute("orderid");
		if(cporderid!=null && !cporderid.equals("")){
			Order order = new Order();
			order.setCporderid(cporderid);
			order.setPlatsystem(platsystem);
			mDataBaseService.updateOrder(order);
		}
	}
	
	@RequestMapping("createOrder.htm")
	@ResponseBody
	private Order createOrder(HttpServletRequest request) throws Exception {
		String HTID = StringUtils.obj2String(request.getParameter("HTID"));
		String waresid = StringUtils.obj2String(request.getParameter("waresid"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		float price = StringUtils.obj2Float(request.getParameter("price"));
		String BZIDlist = StringUtils.obj2String(request.getParameter("BZIDlist"));
		String feiList = StringUtils.obj2String(request.getParameter("feiList"));
		String source = StringUtils.obj2String(request.getParameter("source"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String urlString = StringUtils.obj2String(request.getParameter("urlString"));
		String WYMC = StringUtils.obj2String(request.getParameter("WYMC"));
		String DLMC = StringUtils.obj2String(request.getParameter("DLMC"));
		String FH = StringUtils.obj2String(request.getParameter("FH"));
		String customerName = StringUtils.obj2String(request.getParameter("customerName") == null ? "" : request.getParameter("customerName"));
		String feetype = StringUtils.obj2String(request.getParameter("feetype"));
		String SIGN = StringUtils.obj2String(request.getParameter("SIGN"));
		String from = StringUtils.obj2String(request.getParameter("from"));
		String transid = "";
		String platsystem = "";
		String appid = "";
		String appuserid = "";
		String cpprivate = new Date().getTime() + "__" + urlString;
		String cporderid = OrderUtils.getOrderNo(source);
		String currency = "";
		// cpprivate作为二次验证值使用
		request.getSession().setAttribute("from", from);

		Order order = saveOrder(appid, waresid, price, appuserid, cpprivate, HTID, JFYF, BZIDlist, feiList, cporderid, currency, source, WYID, WYMC, DLMC, FH, customerName,
				transid, feetype, platsystem, from);
		
		HttpSession session = request.getSession();
		logger.info("产生的订单order=" + JSON.toJSONString(order));
		
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setWYID(WYID);
		account.setSource(source);

		logger.info("查询可用的支付方式即账号列表,参数：WYID=" + WYID + ";source=" + source);

		accountList = mDataBaseService.getAccountList(account);
		
		Wares wares = new Wares();
		wares.setWaresid(waresid);
		wares = mDataBaseService.getWares(wares);

		// 缓存session信息
		session.setAttribute("orderid", order.getCporderid());
		session.setAttribute("feetype", feetype);
		session.setAttribute("WYID", WYID);
		session.setAttribute("HTID", HTID);
		session.setAttribute("JFYF", JFYF);
		session.setAttribute("source", source);
		session.setAttribute("SIGN", SIGN);
		CacheUtils.put("accountList_" + cporderid, accountList);
		CacheUtils.put("wares_" + cporderid, wares);
		//String SIGN = (String) session.getAttribute("SIGN");
		CacheUtils.put("order_" + cporderid, order);
		
		CacheUtils.put("SIGN_" + cporderid, SIGN);
		
		return order;
	}
}
