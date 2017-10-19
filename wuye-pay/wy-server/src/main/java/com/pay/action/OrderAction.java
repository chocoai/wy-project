package com.pay.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.bean.Account;
import com.pay.bean.Order;
import com.pay.bean.Urlmap;
import com.pay.common.JsonUtil;
import com.pay.common.SignUtils;
import com.pay.common.StreamUtil;
import com.pay.common.StringUtils;
import com.pay.common.sign.SignHelper;
import com.pay.common.utils.CacheUtils;
import com.pay.service.DataBaseService;
import com.pay.service.impl.DataBaseServiceImpl;
import com.pay.thridwy.haolong.bean.GetWeiFee;
import com.pay.thridwy.haolong.bean.GetWeiFee1;
import com.pay.thridwy.haolong.bean.GetWeiFee2;
import com.pay.thridwy.haolong.bean.Getfee1;
import com.pay.thridwy.haolong.bean.Getfee2;
import com.pay.thridwy.haolong.bean.PutWeiFee;
import com.pay.thridwy.haolong.bean.PutWeiFee1;
import com.pay.thridwy.haolong.bean.PutWeiFee2;
import com.pay.thridwy.haolong.utils.HaolongService;
import com.pay.thridwy.haolong.utils.impl.HaolongServiceImpl;
import com.pay.weixin.service.WeixinService;

@Controller
@RequestMapping("/order")
public class OrderAction {

	@Autowired
	private HaolongService mHaolongService;

	@Autowired
	private DataBaseService mDataBaseService;

	private int count = 0;

	@Autowired
	private WeixinService weixinService;

	private static Logger logger = Logger.getLogger(OrderAction.class);

	/*public static void main(String[] args) {
		String respstr = "<xml><return_code><![CDATA[RETURNCODE]]></return_code><return_msg><![CDATA[RETURNMSG]]></return_msg></xml>";

		respstr = respstr.replace("RETURNCODE", "SUCCESS").replace("RETURNMSG", "成功");

		System.out.println(respstr);
	}*/

	/**
	 * 更新订单,并调用支付回调函数
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping("updateOrder.htm")
	@ResponseBody
	public String updateOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("支付结果回调次数:" + ++count + "开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String respData = StreamUtil.getBodyString(request.getReader());
		logger.info("支付结果回调数据入参:" + respData);
		String verifyResult = "";// 原来接收个函数返回的状态值，作为回调的状态值
		String respOrderid = null;
		// 微信支付通知回调参数判断,利用特定格式判断
		if (respData.contains("<return_code>") && respData.contains("<![CDATA[")) {// 微信回调
			String respstr = "<xml><return_code><![CDATA[RETURNCODE]]></return_code><return_msg><![CDATA[RETURNMSG]]></return_msg></xml>";

			String uri = request.getRequestURL().toString();
			logger.info("支付通知成功返回的uri=" + uri);

			// 校验回调返回参数
			Map<String, String> map = weixinService.dealNotify(respData);
			String orderId = map.get("out_trade_no");
			
			Boolean sessionflag = (Boolean) CacheUtils.get("sessionflag"+orderId);
			logger.info("cporderid: "+orderId+" sessionflag: "+sessionflag+" at time: "+ (new Date()));
			if(sessionflag == null){
				CacheUtils.put("sessionflag"+orderId, true);
			}else if(sessionflag){
				return respstr.replace("RETURNCODE", "FAIL").replace("RETURNMSG", "回调中");
			}else{
				CacheUtils.put("sessionflag"+orderId, true);
			}
			
			respOrderid = orderId;
			// 校验是否通过
			boolean signFlagResult = Boolean.valueOf(map.get("signFlagResult"));
			// 签名验证失败
			if (!signFlagResult) {
				update(orderId, "签名验证失败");
				CacheUtils.put("sessionflag"+orderId, false);
				return respstr.replace("RETURNCODE", "FAIL").replace("RETURNMSG", "签名校验失败");
			}

			String total_fee = map.get("total_fee");
			float money = Float.valueOf(total_fee) / 100.0f;// 转成分
			// 金额校验失败
			// if (money != order.getPrice()) {
			// return respstr.replace("RETURNCODE", "FAIL").replace("RETURNMSG",
			// "金额校验失败");
			// }

			// 更改交易结果,0=成功,1=失败
			String result = "SUCCESS".equals(map.get("result_code")) ? "0" : "1";
			// 回调成功更新订单
			updateOrderStatus(orderId, result, map.get("transaction_id"), map.get("time_end"), "403", "0", map.get("fee_type"));

			Order order = new Order();
			order.setCporderid(orderId);
			order = mDataBaseService.getOrder(order).get(0);
			// 去除已缴费项目
			
			String SKID = "";
			if (order.getFeetype().equals("0")) {
				SKID = getSKID(order, money);
			} else if (order.getFeetype().equals("1")) {
				SKID = getSKID1(order, money);
			} else if (order.getFeetype().equals("2")) {
				SKID = getSKID2(order, "" + money);
			}
			
			CacheUtils.put("sessionflag"+orderId, false);
			
			if(SKID.contains("已销账")){
				CacheUtils.put("sessionflag"+orderId, false);
				return "SUCCESS";
			}

			if (SKID.toUpperCase().contains("ERROR")) {
				// 失败
				respstr = respstr.replace("RETURNCODE", "FAIL").replace("RETURNMSG", "业务处理失败");
				if (SKID.contains("[") && SKID.contains("]")) {
					respstr = respstr.replace("FAIL", "SUCCESS").replace("业务处理失败", "交易成功");
				}
			}
			logger.info("调用好邻邦接口，获得SKID:" + SKID);

			// 防止成功后多次回调
			String order_skid1 = order.getSKID();
			if (order_skid1 != null && !order_skid1.equals("") && isNumeric(order_skid1)) {
				CacheUtils.put("sessionflag"+orderId, false);
				return respstr.replace("RETURNCODE", "SUCCESS").replace("RETURNMSG", "交易成功");
			}
			update(orderId, SKID);

			verifyResult = respstr;
			CacheUtils.put("sessionflag"+orderId, false);
		} else if (respData.contains("transdata=")) {// 爱贝回调

			verifyResult = aibeiCallback(respData, request);
		}

		// 后续可考虑将销账接口getSKID做成定时器进行异步处理，而不是通过回调来重新getSKID校验
		logger.info("支付结果回调数据出参(返回给支付平台):" + verifyResult);
		return verifyResult;
	}

	/**
	 * 调用好邻邦接口，获得商品1的SKID,如果SKID不包含ERROR字符串,则进行数据校验,判断缴费项目与已销账项目长度->
	 * 缴费项目是否在已销账项目中 ->缴费项目与已销账项目是否对应->相对应的单项缴费项目与已销账项目是否一致
	 * ->缴费项目、已销账项目、爱贝账单费用是否一致
	 * 
	 * @param feeList
	 * @param source
	 * @param WYID
	 * @param HTID
	 * @param JFYF
	 * @param BZIDList
	 * @param paytype
	 * @param cporderid
	 * @param urlString
	 * @param sercetKey
	 * @param money
	 * @return
	 * @throws Exception
	 */
	private String getSKID(Order order, Float money) throws Exception {
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功
		if (order.getSKID() != null && !order.getSKID().equals("") && isNumeric(order.getSKID())) {
			return order.getSKID();
		}

		String source = order.getSource();
		String sercetKey = null;
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(source);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		if (!source.equals("") && source != null) {
			sercetKey = urlmap.getSecretkey();
		}

		String HTID = order.getHTID();
		String JFYF = order.getJFYF();
		String WYID = order.getWYID();
		String BZIDList = order.getBZIDList();
		String feiList = order.getFeiList() == null ? "" : order.getFeiList();
		// BZIDList不能为空或空字符串，否则调用接口报错
		if (BZIDList == null || "".equals(BZIDList)) {
			logger.error("无效缴费项目");
			return "fail";
		}

		String SKID = "";

		PutWeiFee putWeiFee = new PutWeiFee();
		putWeiFee.setHTID(HTID);
		putWeiFee.setJFYF(JFYF);
		putWeiFee.setBZIDList(BZIDList);
		putWeiFee.setFeiList(feiList);
		putWeiFee.setPayMode(order.getPaytype());
		putWeiFee.setOrderNum(order.getCporderid());
		putWeiFee.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
		putWeiFee.setUrlString(urlmap.getUrlstring());
		putWeiFee.setSoapActionString(null);

		putWeiFee.setSercetKey(sercetKey);
		logger.info("source:" + source + " WYID:" + WYID + " HTID:" + HTID + " JFYF:" + JFYF + " BZIDList:" + BZIDList + " feiList:" + feiList + " paytype:" + order.getPaytype()
				+ " cporderid:" + order.getCporderid() + " urlString:" + urlmap.getUrlstring() + " sercetKey:" + sercetKey + " money:" + money);

		try {
			SKID = mHaolongService.putWeiFee(putWeiFee);
			if(SKID.contains("已销账")){
				return SKID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:" + e.toString());
			logger.error("调用putWeiFee失败:" + SKID);
			return "Error:销账接口调用失败(PutWeiFee)";
		}

		double BZlistPrice = 0.00; // weifeelist 的总金额
		double jxprice = 0.00; // 缴费项目
		boolean invaild = SKID.toUpperCase().contains("ERROR");
		if (invaild) {
			return SKID;
		} else {
			String[] BZIDs = BZIDList.split(",");
			String[] feis = feiList.split(",");

			GetWeiFee getWeiFee = new GetWeiFee();
			getWeiFee.setSKID(SKID);
			getWeiFee.setUrlString(urlmap.getUrlstring());
			getWeiFee.setSoapActionString(null);
			getWeiFee.setSercetKey(sercetKey);
			List<Map<String, String>> getWeiFeeList = null;
			try {
				getWeiFeeList = mHaolongService.getWeiFee(getWeiFee);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("异常:" + e.toString());
				return "Error:[" + SKID + "]销账接口调用失败(GetWeiFee)";
			}

			/**
			 * weifeelist包含一条头信息
			 */
			if (BZIDs.length != getWeiFeeList.size() - 1) {
				// System.out.println("缴费项目与已销账项目长度不一致");
				// System.out.println("BZIDs.length->"+BZIDs.length+",getWeiFeeList.size()-1->"+(getWeiFeeList.size()-1));
				return "Error:[" + SKID + "]缴费项目与已销账项目长度不一致";
			}

			/**
			 * 获取相应的缴费项目
			 */
			boolean flag1 = false;
			String BZID = "";

			for (Map<String, String> map : getWeiFeeList) { // 将getfee的List
															// 改为了getweifee
															// 的list
															// ，直接从订单里的BZIDList与已销账项目
				for (int i = 0; i < BZIDs.length; i++) {
					BZID = BZIDs[i];
					if (BZID.equals(map.get("BZID"))) {
						flag1 = true;
						double shby = StringUtils.obj2Double(map.get("实收本月"));
						double bcznj = StringUtils.obj2Double(map.get("本次滞纳金"));
						double wyznj = StringUtils.obj2Double(map.get("往月滞纳金"));
						double wyss = StringUtils.obj2Double(map.get("实收往月"));
						double b = shby + bcznj + wyznj + wyss; // 合计
						BigDecimal bd = new BigDecimal(b);
						b = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						BZlistPrice = b + BZlistPrice; // 记总额
						bd = new BigDecimal(BZlistPrice);
						BZlistPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						double qshj = StringUtils.obj2Double(feis[i]);
						bd = new BigDecimal(qshj);
						qshj = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						jxprice = qshj + jxprice;
						bd = new BigDecimal(jxprice);
						jxprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						if (qshj != b) {
							// System.out.println("项目"+BZID+"金额不一致");
							// System.out.println("qshj->"+qshj+",b->"+b);
							return "Error:[" + SKID + "]项目" + BZID + "金额不一致";
						}
					}
				}
			}
			if (flag1 == false) {
				// System.out.println("缴费项目不在应收项目中");
				return "Error:[" + SKID + "]缴费项目不在应收项目中";
			}
			/**
			 * 
			 * weiFeeBZID与feeBZID 不相等，说明项目编号不对应
			 */

			// 保留两位小数转成String类型比较
			BigDecimal bDecimal = new BigDecimal(money);
			double bDecimalMoney = bDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (jxprice != bDecimalMoney || BZlistPrice != bDecimalMoney) {
				// System.out.println("总金额不一致");
				// System.out.println("jxprice->"+jxprice+",BZlistPrice->"+BZlistPrice+",money->"+money);
				return "Error:[" + SKID + "]总金额不一致";
			}
		}

		return SKID;
	}

	@RequestMapping("updateOrder1.htm")
	@ResponseBody
	public String updateOrder1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("回调次数:" + ++count + "开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String respData = StreamUtil.getBodyString(request.getReader());
		logger.info("回调数据:" + respData);
		Map<String, String> mapData = SignUtils.getParmters(respData);
		String transdata = URLDecoder.decode(mapData.get("transdata"), "utf-8");
		String sign = URLDecoder.decode(mapData.get("sign"), "utf-8");
		String signtype = URLDecoder.decode(mapData.get("signtype"), "utf-8");
		String verifyResult = "";
		if (signtype == null) {
			// System.out.println(""+respData);
		} else {
			//verifyResult = verify(transdata, sign);
		}

		Map<String, Object> map = JsonUtil.getMap4Json(transdata).get(0);
		String OrderStatus = "0";

		String cporderid = map.get("cporderid").toString();
		String cpprivate = map.get("cpprivate").toString();
		String currency = map.get("currency").toString();
		String feetype = map.get("feetype").toString();
		String price = map.get("money").toString();
		String paytype = map.get("paytype").toString();
		String result = map.get("result").toString();
		String transid = map.get("transid").toString();
		String transtime = map.get("transtime").toString();
		String transtype = map.get("transtype").toString();

		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);
		String order_orderstatus = order.getOrderstatus();

		/*
		 * //如果该订单已经结算完成（重复回调的情况），则直接返回，无需继续操作 if
		 * (order_orderstatus.equals("0")) { return ""; }
		 */
		/*Account account = new Account();
		account.setWYID(order.getWYID());
		account.setSource(order.getSource());
		account = mDataBaseService.getAccount(account);*/
		// 能进到此函数,说明支付成功了，所以订单状态需要改为0,同时
		updateOrderStatus(cporderid, result, transid, transtime, paytype, transtype, currency);

		// 如果skid为大于0的字符串，说明已经获得skid并销账成功
		String order_skid = order.getSKID();
		if (order_skid != null && !order_skid.equals("") && isNumeric(order_skid)) {
			return "success";
		}

		// 通过cpprivate进行回传数据校验
		String order_cpprivate = order.getCpprivate();
		String urlString = order_cpprivate.split("__")[1];
		if (!cpprivate.equals(DigestUtils.md5Hex(order_cpprivate))) {
			logger.error("非法数据");
			return "fail";
		}

		String source = order.getSource();
		String sercetKey = null;
		if (!source.equals("") && source != null) {
			Urlmap urlmap = new Urlmap();
			urlmap.setUrlkey(source);
			urlmap.setStatus("1");
			urlmap = mDataBaseService.getUrlmap(urlmap);
			sercetKey = urlmap.getSecretkey();
		}

		String HTID = order.getHTID();
		String WYID = order.getWYID();
		String BZIDList = order.getBZIDList();
		// BZIDList不能为空或空字符串，否则调用接口报错
		if (BZIDList == null || "".equals(BZIDList)) {
			logger.error("无效缴费项目");
			return "fail";
		}
		Getfee1 fee1 = new Getfee1();
		fee1.setHTID(HTID);
		fee1.setUrlString(urlString);
		fee1.setSoapActionString(null);
		fee1.setSecretkey(sercetKey);
		HaolongService mHaolongService = new HaolongServiceImpl();

		// List<Map<String,String>> feeList = (List<Map<String, String>>)
		// request.getSession().getAttribute("feeList");
		List<Map<String, String>> feeList = mHaolongService.getFee1(fee1);
		/**
		 * 去除已缴费项目
		 */
		List<Map<String, String>> removeList = new ArrayList<Map<String, String>>();
		for (Map<String, String> m : feeList) {
			if ("true".equals(m.get("付清标志"))) {
				removeList.add(m);
			}
		}

		feeList.removeAll(removeList);
		// 调用好邻邦接口，获得SKID

		String SKID = getSKID1(order, Float.valueOf(price));
		if (SKID.toUpperCase().contains("ERROR")) {
			verifyResult = "fail";
			if (SKID.contains("[") && SKID.contains("]")) {
				verifyResult = "success";
			}
		}

		logger.info("调用好邻邦接口，获得SKID:" + SKID);

		// 获取当前最新订单
		Order order1 = new Order();
		order1.setCporderid(cporderid);
		order1 = mDataBaseService.getOrder(order1).get(0);
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功，无需更新订单，直接返回success
		String order_skid1 = order1.getSKID();
		if (order_skid1 != null && !order_skid1.equals("") && isNumeric(order_skid1)) {
			return "success";
		}

		// 更新订单数据
		// 写入指定客户指定计费月份的微支付收款记录，标志为：8-微未付
		update(cporderid, SKID);

		return verifyResult;
	}

	/**
	 * 调用好邻邦接口，获得商品2的SKID,如果SKID不包含ERROR字符串,则进行数据校验,判断缴费项目与已销账项目长度->
	 * 缴费项目是否在已销账项目中 ->缴费项目与已销账项目是否对应->相对应的单项缴费项目与已销账项目是否一致
	 * ->缴费项目、已销账项目、爱贝账单费用是否一致
	 * 
	 * @param feeList
	 * @param source
	 * @param WYID
	 * @param HTID
	 * @param BZIDList
	 * @param paytype
	 * @param cporderid
	 * @param urlString
	 * @param sercetKey
	 * @param money
	 * @return
	 * @throws Exception
	 */
	private String getSKID1(Order order, Float money) throws Exception {
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功
		if (order.getSKID() != null && !order.getSKID().equals("") && isNumeric(order.getSKID())) {
			return order.getSKID();
		}

		String source = order.getSource();
		String sercetKey = null;
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(source);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		if (!source.equals("") && source != null) {
			sercetKey = urlmap.getSecretkey();
		}

		String HTID = order.getHTID();
		String JFYF = order.getJFYF();
		String WYID = order.getWYID();
		String BZIDList = order.getBZIDList();
		String feiList = order.getFeiList() == null ? "" : order.getFeiList();
		// BZIDList不能为空或空字符串，否则调用接口报错
		if (BZIDList == null || "".equals(BZIDList)) {
			logger.error("无效缴费项目");
			return "fail";
		}

		String SKID = "";

		PutWeiFee1 putWeiFee1 = new PutWeiFee1();
		putWeiFee1.setHTID(HTID);
		putWeiFee1.setBZIDList(BZIDList);
		putWeiFee1.setPayMode(order.getPaytype());
		putWeiFee1.setOrderNum(order.getCporderid());
		putWeiFee1.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
		putWeiFee1.setUrlString(urlmap.getUrlstring());
		putWeiFee1.setSoapActionString(null);
		putWeiFee1.setSercetKey(sercetKey);

		try {
			SKID = mHaolongService.putWeiFee1(putWeiFee1);
			if(SKID.contains("已销账")){
				return SKID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:" + e.toString());
			logger.error("调用putWeiFee1失败:" + SKID);
			return "Error:销账接口调用失败(PutWeiFee1)";
		}

		double BZlistPrice = 0.00; // weifeelist 的总金额
		double jxprice = 0.00; // 缴费项目
		boolean invaild = SKID.toUpperCase().contains("ERROR");
		if (invaild) {
			return SKID;
		} else {
			String[] BZIDs = BZIDList.split(",");

			GetWeiFee1 getWeiFee1 = new GetWeiFee1();
			getWeiFee1.setSKID(SKID);
			getWeiFee1.setUrlString(urlmap.getUrlstring());
			getWeiFee1.setSoapActionString(null);
			getWeiFee1.setSercetKey(sercetKey);
			List<Map<String, String>> getWeiFeeList = null;
			try {
				getWeiFeeList = mHaolongService.getWeiFee1(getWeiFee1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("异常:" + e.toString());
				return "Error:[" + SKID + "]销账接口调用失败(GetWeiFee1)";
			}

			/**
			 * weifeelist包含一条头信息
			 */
			if (BZIDs.length != getWeiFeeList.size() - 1) {
				// System.out.println("缴费项目与已销账项目长度不一致");
				// System.out.println("BZIDs.length->"+BZIDs.length+",getWeiFeeList.size()-1->"+(getWeiFeeList.size()-1));
				return "Error:[" + SKID + "]缴费项目与已销账项目长度不一致";
			}

			/**
			 * 获取相应的缴费项目
			 */
			boolean flag1 = false;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : getWeiFeeList) {
				for (String BZID : BZIDs) {
					if (BZID.equals(map.get("BZID"))) {
						flag1 = true;
						list.add(map);
					}
				}
			}
			if (flag1 == false) {
				// System.out.println("缴费项目不在应收项目中");
				return "Error:[" + SKID + "]缴费项目不在应收项目中";
			}
			/**
			 * 
			 * weiFeeBZID与feeBZID 不相等，说明项目编号不对应
			 */
			String weiFeeBZID = "";
			String feeBZID = "";

			for (Map<String, String> fee : list) {
				feeBZID = fee.get("BZID");
				boolean flag = false; // getWeiFeeList 与 list的标记,false说明
										// 缴费项目与已销账项目不对应
				for (Map<String, String> map : getWeiFeeList) {
					if (map.containsKey("BZID")) {
						weiFeeBZID = map.get("BZID");
						if (weiFeeBZID.equals(feeBZID)) {
							flag = true;

							double ssjbf = StringUtils.obj2Double(map.get("实收基本费"));
							BigDecimal bd = new BigDecimal(ssjbf);
							ssjbf = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

							BZlistPrice = ssjbf + BZlistPrice; // 记总额
							bd = new BigDecimal(BZlistPrice);
							BZlistPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

							/*double qsje = StringUtils.obj2Double(fee.get("欠交金额"));
							bd = new BigDecimal(qsje);
							qsje = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();*/

							/*jxprice = qsje + jxprice;
							bd = new BigDecimal(jxprice);
							jxprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

							if (qsje != ssjbf) {
								// System.out.println("项目"+feeBZID+"金额不一致");
								// System.out.println("qsje->"+qsje+",b->"+ssjbf);
								return "Error:[" + SKID + "]项目" + feeBZID + "金额不一致";
							}*/
						}
					}
				}
				if (flag == false) {
					// System.out.println("缴费项目与已销账项目不对应");
					// System.out.println(fee.toString());
					return "Error:[" + SKID + "]缴费项目与已销账项目不对应";
				}
			}

			// 保留两位小数转成String类型比较
			BigDecimal bDecimal = new BigDecimal(money);
			double bDecimalMoney = bDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (BZlistPrice != bDecimalMoney) {
				// System.out.println("总金额不一致");
				// System.out.println("jxprice->"+jxprice+",BZlistPrice->"+BZlistPrice+",money->"+money);
				return "Error:[" + SKID + "]总金额不一致";
			}
		}
		return SKID;
	}

	@RequestMapping("updateOrder2.htm")
	@ResponseBody
	public String updateOrder2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("回调次数:" + ++count + "开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String respData = StreamUtil.getBodyString(request.getReader());
		Map map1 = request.getParameterMap();

		logger.info("回调数据:" + respData);
		Map<String, String> mapData = SignUtils.getParmters(respData);
		String transdata = URLDecoder.decode(mapData.get("transdata"), "utf-8");
		String sign = URLDecoder.decode(mapData.get("sign"), "utf-8");
		String signtype = URLDecoder.decode(mapData.get("signtype"), "utf-8");
		// 测试数据
		/*
		 * String transdata =
		 * "{'transid':'32271703081535321776','redirecturl':'http://localhost/wy-client/index2.html?x0=2&x1=33&x2=7262&x3=200911&x4=haolong&x5=77AEF477292EF844BB47A4C5B1069D8C','cpurl':'http://localhost/wy-client/index2.html?x0=2&x1=33&x2=7262&x3=200911&x4=haolong&x5=77AEF477292EF844BB47A4C5B1069D8C'}"
		 * ; String sign =
		 * "Ml+iIi2+ol/L6fhCj3amfOs628PKn6+nSmUXHl0P35aYpjULd3JOuET+Te0xW7duvZFRH448aHrNXIY3/cVI0IqXlC5xYnT6St3Tsh7Q/beOeAkO29ZSFF/plVm5sI/Mrm5w8praxJvDR0Lc0HCmaWQN943PBNQ2YOGchEKK7kQ"
		 * ; String signtype = "RSA";
		 */
		String verifyResult = "";
		if (signtype == null) {
			// System.out.println(""+respData);
		} else {
			//verifyResult = verify(transdata, sign);
		}

		Map<String, Object> map = JsonUtil.getMap4Json(transdata).get(0);
		String OrderStatus = "0";

		String cporderid = map.get("cporderid").toString();
		String cpprivate = map.get("cpprivate").toString();
		String currency = map.get("currency").toString();
		String feetype = map.get("feetype").toString();
		String price = map.get("money").toString();
		String paytype = map.get("paytype").toString();
		String result = map.get("result").toString();
		String transid = map.get("transid").toString();
		String transtime = map.get("transtime").toString();
		String transtype = map.get("transtype").toString();

		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);
		String order_orderstatus = order.getOrderstatus();
		/*
		 * //如果该订单已经结算完成（重复回调的情况），则直接返回，无需继续操作 if
		 * (order_orderstatus.equals("0")) { return ""; }
		 */
		// 能进到此函数,说明支付成功了，所以订单状态需要改为0
		/*Account account = new Account();
		account.setWYID(order.getWYID());
		account.setSource(order.getSource());
		account = mDataBaseService.getAccount(account);*/
		// 能进到此函数,说明支付成功了，所以订单状态需要改为0,同时
		updateOrderStatus(cporderid, result, transid, transtime, paytype, transtype, currency);
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功
		String order_skid = order.getSKID();
		if (order_skid != null && !order_skid.equals("") && isNumeric(order_skid)) {
			return "success";
		}

		// 通过cpprivate进行回传数据校验
		String order_cpprivate = order.getCpprivate();
		String urlString = order_cpprivate.split("__")[1];
		if (!cpprivate.equals(DigestUtils.md5Hex(order_cpprivate))) {
			logger.error("非法数据");
			return "非法数据";
		}

		String source = order.getSource();
		String sercetKey = null;
		if (!source.equals("") && source != null) {
			Urlmap urlmap = new Urlmap();
			urlmap.setUrlkey(source);
			urlmap.setStatus("1");
			urlmap = mDataBaseService.getUrlmap(urlmap);
			sercetKey = urlmap.getSecretkey();
		}

		String HTID = order.getHTID();
		String WYID = order.getWYID();
		String BZIDList = order.getBZIDList();
		String customername = order.getCustomername();
		// BZIDList不能为空或空字符串，否则调用接口报错
		if (BZIDList == null || "".equals(BZIDList)) {
			logger.error("无效缴费项目");
			return "fail";
		}

		Getfee2 fee2 = new Getfee2();
		fee2.setWYID(WYID);
		fee2.setUrlString(urlString);
		fee2.setSoapActionString(null);
		fee2.setSecretkey(sercetKey);
		HaolongService mHaolongService = new HaolongServiceImpl();

		// List<Map<String,String>> feeList = (List<Map<String, String>>)
		// request.getSession().getAttribute("feeList");
		List<Map<String, String>> feeList = mHaolongService.getFee2(fee2);
		/**
		 * 去除已缴费项目
		 */
		/*
		 * List<Map<String,String>> removeList=new
		 * ArrayList<Map<String,String>>(); for(Map<String,String> m:feeList){
		 * if("true".equals(m.get("付清标志"))){ removeList.add(m); } }
		 * 
		 * feeList.removeAll(removeList);
		 */
		// 调用好邻邦接口，获得SKID

		String SKID = getSKID2(order, price);
		if (SKID.toUpperCase().contains("ERROR")) {
			verifyResult = "fail";
			if (SKID.contains("[") && SKID.contains("]")) {
				verifyResult = "success";
			}
		}

		logger.info("调用好邻邦接口，获得SKID:" + SKID);

		// 获取当前最新订单
		Order order1 = new Order();
		order1.setCporderid(cporderid);
		order1 = mDataBaseService.getOrder(order1).get(0);
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功，无需更新订单，直接返回success
		String order_skid1 = order1.getSKID();
		if (order_skid1 != null && !order_skid1.equals("") && isNumeric(order_skid1)) {
			return "success";
		}

		// 更新订单数据
		// 写入指定客户指定计费月份的微支付收款记录，标志为：8-微未付
		update(cporderid, SKID);
		return verifyResult;
	}

	/**
	 * 调用好邻邦接口，获得商品2的SKID,如果SKID不包含ERROR字符串,则进行数据校验,获取相应的缴费项目->缴费项目、爱贝账单费用是否一致
	 * 
	 * @param feeList
	 * @param source
	 * @param WYID
	 * @param HTID
	 * @param BZIDList
	 * @param paytype
	 * @param cporderid
	 * @param urlString
	 * @param sercetKey
	 * @param money
	 * @return
	 * @throws Exception
	 */
	private String getSKID2(Order order, String money) throws Exception {
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功
		if (order.getSKID() != null && !order.getSKID().equals("") && isNumeric(order.getSKID())) {
			return order.getSKID();
		}

		String source = order.getSource();
		String sercetKey = null;
		Urlmap urlmap = new Urlmap();
		urlmap.setUrlkey(source);
		urlmap.setStatus("1");
		urlmap = mDataBaseService.getUrlmap(urlmap);
		if (!source.equals("") && source != null) {
			sercetKey = urlmap.getSecretkey();
		}

		String HTID = order.getHTID();
		String JFYF = order.getJFYF();
		String WYID = order.getWYID();
		String BZIDList = order.getBZIDList();
		String feiList = order.getFeiList() == null ? "" : order.getFeiList();
		// BZIDList不能为空或空字符串，否则调用接口报错
		if (BZIDList == null || "".equals(BZIDList)) {
			logger.error("无效缴费项目");
			return "fail";
		}

		String SKID = "";

		PutWeiFee2 putWeiFee2 = new PutWeiFee2();
		putWeiFee2.setWYID(WYID);
		putWeiFee2.setHTID(HTID);
		putWeiFee2.setBZID(BZIDList);
		putWeiFee2.setFee(money);
		putWeiFee2.setPayMode(order.getPaytype());
		putWeiFee2.setOrderNum(order.getCporderid());
		putWeiFee2.setCustomerName(order.getCustomername());
		putWeiFee2.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
		putWeiFee2.setUrlString(urlmap.getUrlstring());
		putWeiFee2.setSoapActionString(null);
		putWeiFee2.setSercetKey(sercetKey);

		try {
			SKID = mHaolongService.putWeiFee2(putWeiFee2);
			if(SKID.contains("已销账")){
				return SKID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:" + e.toString());
			logger.error("调用putWeiFee2失败:" + SKID);
			return "Error:销账接口调用失败(PutWeiFee2)";
		}

		double BZlistPrice = 0.00; // weifeelist 的总金额
		double jxprice = 0.00; // 缴费项目
		boolean invaild = SKID.toUpperCase().contains("ERROR");
		if (invaild) {
			return SKID;
		} else {
			String[] BZIDs = BZIDList.split(",");

			GetWeiFee2 getWeiFee2 = new GetWeiFee2();
			getWeiFee2.setSKID(SKID);
			getWeiFee2.setUrlString(urlmap.getUrlstring());
			getWeiFee2.setSoapActionString(null);
			getWeiFee2.setSercetKey(sercetKey);
			List<Map<String, String>> getWeiFeeList = null;
			try {
				getWeiFeeList = mHaolongService.getWeiFee2(getWeiFee2);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("异常:" + e.toString());
				return "Error:[" + SKID + "]销账接口调用失败(GetWeiFee2)";
			}

			/**
			 * 获取相应的缴费项目
			 */
			boolean flag1 = false;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : getWeiFeeList) {
				for (String BZID : BZIDs) {
					if (BZID.equals(map.get("BZID"))) {
						flag1 = true;
						list.add(map);

						double qsje = StringUtils.obj2Double(map.get("实收金额"));
						BigDecimal bd = new BigDecimal(qsje);
						qsje = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						jxprice = qsje + jxprice;
						bd = new BigDecimal(jxprice);
						jxprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
				}
				if (order.getHTID().equals("0")) {
					order.setProperty(map.get("物业名称"));
					order.setBuilding(map.get("大楼名称"));
					mDataBaseService.updateOrder(order);
				}
			}
			if (flag1 == false) {
				// System.out.println("缴费项目不在应收项目中");
				logger.info("失败: 缴费项目不在应收项目中");
				return "Error:[" + SKID + "]缴费项目不在应收项目中";
			}

			// 保留两位小数转成String类型比较
			BigDecimal bDecimal = new BigDecimal(money);
			double bDecimalMoney = bDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (jxprice != bDecimalMoney) {
				// System.out.println("总金额不一致");
				// System.out.println("jxprice->"+jxprice+",BZlistPrice->"+BZlistPrice+",money->"+money);
				logger.info("失败: 总金额不一致");
				return "Error:[" + SKID + "]总金额不一致";
			}
		}
		return SKID;
	}

	/**
	 * 更新订单SKID
	 * 
	 * @param cporderid
	 * @param SKID
	 * @throws Exception
	 */
	private void update(String cporderid, String SKID) throws Exception {
		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);
		order.setSKID(SKID);
		order.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		mDataBaseService.updateOrder(order);

		logger.info("更新订单SKID,cporderid=" + cporderid + ",order=" + JSON.toJSONString(order));
	}

	/**
	 * 回调成功更新订单
	 * 
	 * @param cporderid
	 * @param result
	 * @param transid
	 * @param transtime
	 * @param paytype
	 * @param transtype
	 * @param currency
	 * @throws IOException
	 */
	private void updateOrderStatus(String cporderid, String result, String transid, String transtime, String paytype, String transtype, String currency) throws IOException {
		logger.info("回调成功更新订单,cporderid=" + cporderid);

		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);
		String order_orderstatus = order.getOrderstatus();

		// 能进到此函数,说明支付成功了，所以订单状态需要改为0,同时
		if (!order_orderstatus.equals("0")) {
			Account account = new Account();
			account.setWYID(order.getWYID());
			account.setSource(order.getSource());
			account.setAppid(order.getAppid());
			account.setAppuserid(order.getAppuserid());
			account = mDataBaseService.getAccount(account);

			Order order1 = new Order();
			order1.setCporderid(cporderid);
			order1 = mDataBaseService.getOrder(order1).get(0);
			order1.setOrderstatus("0");
			order1.setPlatsystem(account==null?null:account.getPlatsystem());
			order1.setOffline(account==null?null:account.getOffline());
			order1.setResult(result);
			order1.setTransid(transid);
			order1.setTranstime(transtime);
			order1.setPaytype(paytype);
			order1.setTranstype(transtype);
			order1.setCurrency(currency);
			order1.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mDataBaseService.updateOrder(order1);

			logger.info("回调成功更新订单,order1=" + JSON.toJSONString(order1));
		}
	}

	private String verify(String transdata, String sign, String cporderid) throws IOException {
		String verifyResult = "";
		JSONObject string_to_json = JSONObject.parseObject(transdata);
		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);
		//String appid = string_to_json.getString("appid");
		Account account = new Account();
		account.setWYID(order.getWYID());
		account.setSource(order.getSource());
		account.setAppid(order.getAppid());
		account.setAppuserid(order.getAppuserid());
		account = mDataBaseService.getAccount(account);

		if (SignHelper.verify(transdata, sign, account.getPlatp_key())) {
			verifyResult = "success";
		} else {
			verifyResult = "fail";
		}
		return verifyResult;
	}

	/*
	 * 判断字符串是否为整数
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public String aibeiCallback(String respData, HttpServletRequest request) throws Exception {
		Map<String, String> mapData = SignUtils.getParmters(respData);
		String transdata = URLDecoder.decode(mapData.get("transdata"), "utf-8");
		String sign = URLDecoder.decode(mapData.get("sign"), "utf-8");
		String signtype = URLDecoder.decode(mapData.get("signtype"), "utf-8");
		String verifyResult = "";

		Map<String, Object> map = JsonUtil.getMap4Json(transdata).get(0);
		String cporderid = map.get("cporderid").toString();
		String cpprivate = map.get("cpprivate").toString();
		String currency = map.get("currency").toString();
		String price = map.get("money").toString();
		String paytype = map.get("paytype").toString();
		String result = map.get("result").toString();
		String transid = map.get("transid").toString();
		String transtime = map.get("transtime").toString();
		String transtype = map.get("transtype").toString();
		
		Boolean sessionflag = (Boolean) CacheUtils.get("sessionflag"+cporderid);
		logger.info("cporderid: "+cporderid+" sessionflag: "+sessionflag+" at time: "+ (new Date()));
		if(sessionflag == null){
			CacheUtils.put("sessionflag"+cporderid, true);
		}else if(sessionflag){
			return "fail";
		}else{
			CacheUtils.put("sessionflag"+cporderid, true);
		}
		
		if (StringUtils.isBlank(signtype)) {
			update(cporderid, "signtype为空");
			CacheUtils.put("sessionflag"+cporderid, false);
			return "fail";
		} else {
			verifyResult = verify(transdata, sign, cporderid);
			
			if (verifyResult.equals("fail")) {
				update(cporderid, "签名校验失败");
				CacheUtils.put("sessionflag"+cporderid, false);
				return "fail";
			}
		}

		// 回调成功更新订单
		updateOrderStatus(cporderid, result, transid, transtime, paytype, transtype, currency);

		// 通过cpprivate进行回传数据校验
		Order order = new Order();
		order.setCporderid(cporderid);
		order = mDataBaseService.getOrder(order).get(0);

		// 通过cpprivate进行回传数据校验
		String order_cpprivate = order.getCpprivate();
		String urlString = order_cpprivate.split("__")[1];
		if (!cpprivate.equals(DigestUtils.md5Hex(order_cpprivate))) {
			logger.error("非法数据");
			CacheUtils.put("sessionflag"+cporderid, false);
			return "fail";
		}

		// 调用好邻邦接口，获得SKID
		String SKID = "";
		if (order.getFeetype().equals("0")) {
			SKID = getSKID(order, Float.parseFloat(price));
		} else if (order.getFeetype().equals("1")) {
			SKID = getSKID1(order, Float.parseFloat(price));
		} else if (order.getFeetype().equals("2")) {
			SKID = getSKID2(order, price);
		}
		logger.info("-------------SKID:"+SKID+"-----------------------");
		
		if(SKID.contains("已销账")){
			CacheUtils.put("sessionflag"+cporderid, false);
			return "success";
		}

		logger.info("调用好邻邦接口，获得SKID,orderid=" + order.getCporderid() + ",SKID=" + SKID);

		if (SKID.toUpperCase().contains("ERROR")) {
			verifyResult = "fail";
			if (SKID.contains("[") && SKID.contains("]")) {
				verifyResult = "success";
			}
		}
		logger.info("调用好邻邦接口，获得SKID:" + SKID);

		// 获取当前最新订单
		Order order1 = new Order();
		order1.setCporderid(cporderid);
		order1 = mDataBaseService.getOrder(order1).get(0);
		// 如果skid为大于0的字符串，说明已经获得skid并销账成功，无需更新订单，直接返回success
		String order_skid1 = order1.getSKID();
		if (order_skid1 != null && !order_skid1.equals("") && isNumeric(order_skid1)) {
			CacheUtils.put("sessionflag"+cporderid, false);
			return "success";
		}

		// 更新订单数据
		// 写入指定客户指定计费月份的微支付收款记录，标志为：8-微未付
		update(cporderid, SKID);
		logger.info("回调次数:" + count + "结束时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		CacheUtils.put("sessionflag"+cporderid, false);
		return verifyResult;
	}

	// 测试Getfee2, PutWeiFee1, PutWeiFee2, GetWeiFee2接口
	/*
	 * public static void main(String[] args) throws Exception{ Getfee2 fee2 =
	 * new Getfee2(); fee2.setWYID("33");
	 * fee2.setUrlString("http://sdfee.oklong.com/service.asmx");
	 * fee2.setSoapActionString(null); fee2.setSecretkey("oklong_iapppay");
	 * HaolongService mHaolongService=new HaolongServiceImpl();
	 * List<Map<String,String>> feeList = mHaolongService.getFee2(fee2);
	 * 
	 * PutWeiFee1 putWeiFee1 = new PutWeiFee1(); putWeiFee1.setHTID("6948");
	 * putWeiFee1.setBZIDList("16,17"); putWeiFee1.setPayMode("403");
	 * putWeiFee1.setOrderNum("haolong_1488969999281");
	 * putWeiFee1.setUrlString("http://sdfee.oklong.com/service.asmx");
	 * putWeiFee1.setSoapActionString(null);
	 * putWeiFee1.setSercetKey("oklong_iapppay"); String SKID1 =
	 * mHaolongService.putWeiFee1(putWeiFee1);
	 * 
	 * PutWeiFee2 putWeiFee2 = new PutWeiFee2(); putWeiFee2.setWYID("33");
	 * putWeiFee2.setHTID(""); putWeiFee2.setBZID("2");
	 * putWeiFee2.setFee("12.55"); putWeiFee2.setPayMode("403");
	 * putWeiFee2.setOrderNum("haolong_1489980391499");
	 * putWeiFee2.setCustomerName("粤B:6782");
	 * putWeiFee2.setUrlString("http://sdfee.oklong.com/service.asmx");
	 * putWeiFee2.setSoapActionString(null);
	 * putWeiFee2.setSercetKey("oklong_iapppay"); String SKID =
	 * mHaolongService.putWeiFee2(putWeiFee2);
	 * 
	 * GetWeiFee2 getWeiFee2 = new GetWeiFee2(); getWeiFee2.setSKID(SKID);
	 * getWeiFee2.setUrlString("http://sdfee.oklong.com/service.asmx");
	 * getWeiFee2.setSoapActionString(null);
	 * getWeiFee2.setSercetKey("oklong_iapppay"); List<Map<String, String>>
	 * getWeiFeeList = mHaolongService.getWeiFee2(getWeiFee2);
	 * 
	 * int i = 0; }
	 */
	public static void main(String[] args) throws Exception{
		/*PutWeiFee putWeiFee = new PutWeiFee();
		putWeiFee.setHTID("10795");
		putWeiFee.setJFYF("200912");
		putWeiFee.setBZIDList("156");
		putWeiFee.setFeiList("0.01");
		putWeiFee.setPayMode("403");
		putWeiFee.setOrderNum("haolong_201704111519310794");
		putWeiFee.setPayDate("2017-04-05");
		putWeiFee.setUrlString("http://sdfee.oklong.com/service.asmx");
		putWeiFee.setSoapActionString(null);
		putWeiFee.setSercetKey("oklong_iapppay");
		
		HaolongService mHaolongService=new HaolongServiceImpl();
		String skid = mHaolongService.putWeiFee(putWeiFee);
		int i = 0;*/
		/*String s = "2017-04-05 11:36:27";
		String ss = s.substring(0, 10).replace("-", "/");
		System.out.println(ss);*/
		/*String s = DigestUtils.md5Hex("1493864777093__http://hfwy99.imwork.net:2020/sdservice/service.asmx");
		String s1 = DigestUtils.md5Hex("1493865525109__http://hfwy99.imwork.net:2020/sdservice/service.asmx");
		String s2 = "e3213beccb792463196d5c5d758ba22c";
		String s3 = "eca2ce10a452be4ad1fc501a6ecac4eb";
		int i = 0;*/
		/*Account account = new Account();
		account.setWYID("83");
		account.setAppid("");
		account.setAppuserid("");
		account = mDataBaseService.getAccount(account);*/
	}
}
