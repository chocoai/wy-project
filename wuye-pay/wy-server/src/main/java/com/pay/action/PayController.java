package com.pay.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.pay.bean.Account;
import com.pay.bean.Order;
import com.pay.bean.PayInfo;
import com.pay.bean.Urlmap;
import com.pay.bean.Wares;
import com.pay.common.JsonUtil;
import com.pay.common.StringUtils;
import com.pay.common.utils.CacheUtils;
import com.pay.common.utils.DateUtils;
import com.pay.service.AibeiService;
import com.pay.service.DataBaseService;

@Controller
@RequestMapping("/pay")
public class PayController {
	
	@Autowired
	private DataBaseService mDataBaseService;
	
	@Autowired
	private AibeiService mAibeiService;

	private static Logger logger = Logger.getLogger(PayController.class);
	/**
	 * 选择支付方式,支付宝或微信
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	/*@RequestMapping("/choosePayType.htm")
	public String topay(HttpServletRequest request, Model model) {

		String orderid = String.valueOf(request.getSession().getAttribute("orderid"));
		String isredirect=request.getParameter("isredirect");

		if (StringUtils.isBlank(orderid)) {
			// /如果orderid为空，则认为是重新请求过来的，从request请求
			orderid = request.getParameter("orderid");
			request.getSession().setAttribute("orderid", orderid);
		}
		List<Account> accountList = CacheUtils.get("accountList_" + orderid);
		Order order = CacheUtils.get("order_" + orderid);
		Wares wares = CacheUtils.get("wares_" + orderid);
		
		model.addAttribute("accountList", accountList);
		model.addAttribute("wares", wares);
		model.addAttribute("order", order);
		model.addAttribute("isredirect", isredirect);

		return "modules/weixin/h5Pay2";
	}*/
	
	@RequestMapping("/choosePayType.htm")
	public String choosePayType(HttpServletRequest request, Model model) throws IOException {

		String orderid = request.getParameter("orderid");
		//String orderid = (String) request.getSession().getAttribute("orderid");
		//request.getSession().setAttribute("orderid", orderid);
		List<Account> accountList = CacheUtils.get("accountList_" + orderid);
		Order order = new Order();
		order.setCporderid(orderid);
		order = mDataBaseService.getOrder(order).get(0);
		logger.info("choosePayType:" +  orderid);
		
		Wares wares = CacheUtils.get("wares_" + orderid);
		String property = (String) request.getSession().getAttribute("property");
		if(StringUtils.isEmpty(property)){
			Account account = new Account();
			account.setWYID(order.getWYID());
			account.setSource(order.getSource());
			account = mDataBaseService.getAccount(account);
			if(account != null){
				property = account.getProperty();
				request.getSession().setAttribute("property", account.getProperty());
			}
		}

		model.addAttribute("accountList", accountList);
		model.addAttribute("wares", wares);
		model.addAttribute("order", order);
		model.addAttribute("feetype", request.getSession().getAttribute("feetype"));
		model.addAttribute("WYID", request.getSession().getAttribute("WYID"));
		model.addAttribute("HTID", request.getSession().getAttribute("HTID"));
		model.addAttribute("JFYF", request.getSession().getAttribute("JFYF").equals("")?0:request.getSession().getAttribute("JFYF"));
		model.addAttribute("source", request.getSession().getAttribute("source"));
		model.addAttribute("SIGN", request.getSession().getAttribute("SIGN"));
		model.addAttribute("property", property);

		return "modules/weixin/h5Pay3";
	}
	
	@ResponseBody
	@RequestMapping("/abpay.htm")
	public String abpay(HttpServletRequest request, Model model) throws Exception{
		Order order = new Order();
		order.setCporderid(String.valueOf(request.getParameter("orderid")));
		order = mDataBaseService.getOrder(order).get(0);
		
		order.setAppid(String.valueOf(request.getParameter("appid")));
		order.setAppuserid(String.valueOf(request.getParameter("appuserid")));
		order.setUpdatetime(DateUtils.formatDateTime(new Date()));
		mDataBaseService.updateOrder(order);
		logger.info("mDataBaseService.updateOrder: " + mDataBaseService.getOrder(order).get(0));

		Account account = new Account();
		account.setWYID(String.valueOf(request.getParameter("wyid")));
		account.setSource(order==null?null:order.getSource());
		account.setAppid(String.valueOf(request.getParameter("appid")));
		account.setAppuserid(String.valueOf(request.getParameter("appuserid")));
		account = mDataBaseService.getAccount(account);
		
		Wares wares = new Wares();
		wares.setWaresid(order.getWaresid());
		wares = mDataBaseService.getWares(wares);
		
		Urlmap urlMap = new Urlmap();
		urlMap.setUrlkey(order.getSource());
		urlMap = mDataBaseService.getUrlmap(urlMap);
		
		// 接入爱贝后台进行下单，获取transID
		PayInfo payInfo = new PayInfo();
		payInfo.setAppid(account.getAppid());
		payInfo.setWaresid(Integer.valueOf(order.getWaresid()));
		payInfo.setCporderid(order.getCporderid());
		payInfo.setPrice(order.getPrice());
		//payInfo.setPrice(1f);
		payInfo.setCurrency("RMB");
		payInfo.setAppuserid(account.getAppuserid());
		payInfo.setWaresname(wares.getWaresname());
		payInfo.setNotifyurl(wares.getNotifyurl());
		payInfo.setSigntype("RSA");
		payInfo.setAppv_key(account.getAppv_key());
		payInfo.setCpprivateinfo(DigestUtils.md5Hex(order.getCpprivate()));
		
		request.getSession().setAttribute("appv_key", account.getAppv_key());

		logger.info("接入爱贝后台进行下单,payInfo=" + JSON.toJSONString(payInfo));
		// 成功时:{"transid":"11111"}
		// 失败时:{"code":"1001","errmsg":"签名验证失败"}
		String result = mAibeiService.getTransId(payInfo);
		logger.info("預支付獲取单号,下单后返回的数据:" + result);

		// 下单完成后，把transID更新进订单数据中
		String transid = "";
		String respStr = null;
		JSONObject json = new JSONObject();
		json.put("requesturl", wares.getRequesturl());
		String url = (String) request.getSession().getAttribute("url");
		String from = (String) request.getSession().getAttribute("from");
		json.put("url", url);
		json.put("from", from);
		if (result.contains("transid")) {
			List<Map<String, Object>> mapList = JsonUtil.getMap4Json(result);
			Map<String, Object> map = mapList.get(0);
			transid = map.get("transid").toString();
			json.put("transid", map.get("transid"));
		}

		respStr = json.toString();
		
		order.setAppid(account.getAppid());
		order.setAppuserid(account.getAppuserid());
		order.setTransid(transid);
		order.setTranstime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		order.setTranstype("0");
		order.setPlatsystem(account.getPlatsystem());
		order.setOffline(account.getOffline());
		order.setCurrency("RMB");
		mDataBaseService.updateOrder(order);
		
		return respStr;
	}
	

}
