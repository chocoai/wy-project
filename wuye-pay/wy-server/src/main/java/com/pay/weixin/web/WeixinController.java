package com.pay.weixin.web;

import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.pay.bean.Account;
import com.pay.bean.Order;
import com.pay.bean.Wares;
import com.pay.common.StreamUtil;
import com.pay.common.conf.Global;
import com.pay.common.constants.PayTypeConst;
import com.pay.common.utils.CacheUtils;
import com.pay.common.utils.DateUtils;
import com.pay.common.utils.QRCodeUtil;
import com.pay.dto.WeJson;
import com.pay.service.DataBaseService;
import com.pay.weixin.service.WeixinService;

/**
 * 微信Controller
 * 
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController {

	private Logger log = LoggerFactory.getLogger(WeixinController.class);

	@Autowired
	private WeixinService weixinService;

	@Autowired
	private DataBaseService mDataBaseService;

	/**
	 * h5发起支付
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	@ResponseBody
	@RequestMapping("/prepay.htm")
	public WeJson prepay(HttpServletRequest request, Model model) {
		try {
			String priceStr = request.getParameter("price");
			priceStr = StringUtils.isBlank(priceStr) ? "1" : priceStr;
			double price = Double.valueOf(priceStr);

			WeJson json = weixinService.h5Pay(price);
			log.info("出参json={}", JSON.toJSON(json));

			return json;
		} catch (Exception e) {
			log.error("预支付获取失败", e);
			return WeJson.fail("预支付获取失败");
		}
	}

	/**
	 * h5发起支付页面
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	@RequestMapping("/topayShow.htm")
	public String show(HttpServletRequest request, Model model) {
		// 获取h5支付js发起参数
		// WxJsapiSignature jsapi;
		model.addAttribute("isfinish", false);
		return "modules/weixin/h5Pay";
	}

	/**
	 * 模式一回调发起支付
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/callback.htm")
	public String callback(HttpServletRequest request) {
		String respstr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>";
		try {
			// 获取body中的值
			String body = StreamUtil.getBodyString(request.getReader());
			log.info("支付成功回调通知；入参body={}", body);

			String str = weixinService.dealQrPayMode1(body);

			log.info("支付回调出参={}", str);

			return str;

		} catch (Exception e) {
			e.printStackTrace();
			String str2 = MessageFormat.format(respstr, e.getMessage());
			return str2;
		}
	}

	/**
	 * 模式一回调发起支付
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/notify.htm")
	public String dealNotify(HttpServletRequest request) {
		String respstr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>";
		try {
			// 获取body中的值
			String body = StreamUtil.getBodyString(request.getReader());
			log.info("支付成功回调通知；入参body={}", body);

			Map<String, String> str = weixinService.dealNotify(body);

			log.info("支付回调出参={}", str);

			return respstr;
		} catch (Exception e) {
			e.printStackTrace();
			String str2 = MessageFormat.format(respstr, e.getMessage());
			return str2;
		}
	}

	/**
	 * 扫码支付,模式二,发起后2小时有效
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getQrPayImg2.htm")
	public String getQrPayImg2(HttpServletRequest request, Model model) throws Exception {
		Map<String, String> map = weixinService.getQrPayModel2(request);
		String codeUrl = map.get("codeUrl");

		String base64Str = QRCodeUtil.encode2ImgBase64(codeUrl);
		model.addAttribute("codeUrl", codeUrl);
		model.addAttribute("base64Str", base64Str);
		model.addAttribute("modetype", "模式二");

		return "modules/weixin/qrPayImg";
	}

	/**
	 * 扫码支付,模式一,直接生成支付二维码,用户扫码后才进行与支付发起，进行后面的支付流程
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getQrPayImg1.htm")
	public String getQrPayImg1(HttpServletRequest request, Model model) throws Exception {
		String codeUrl = weixinService.getNativeMode1Bizpayurl(request);

		String base64Str = QRCodeUtil.encode2ImgBase64(codeUrl);
		model.addAttribute("codeUrl", codeUrl);
		model.addAttribute("base64Str", base64Str);
		model.addAttribute("modetype", "模式一");

		return "modules/weixin/qrPayImg";
	}

	/**
	 * 发起授权url配置
	 * 
	 * @return
	 */
	@RequestMapping("/oAuth2.htm")
	public String oAuth2(HttpServletRequest request) {
		try {
			String orderid = (String) request.getParameter("orderid");
			String appid = (String) request.getParameter("appid");
			String appuserid = (String) request.getParameter("appuserid");
			String wyid = (String) request.getParameter("wyid");

			Order order = new Order();
			order.setCporderid(orderid);
			order = mDataBaseService.getOrder(order).get(0);
			order.setAppid(appid);
			order.setAppuserid(appuserid);
			order.setUpdatetime(DateUtils.formatDateTime(new Date()));
			mDataBaseService.updateOrder(order);
			log.info("mDataBaseService.updateOrder: "+mDataBaseService.getOrder(order).get(0));

			Account account = new Account();
			account.setWYID(wyid);
			account.setSource(order==null?null:order.getSource());
			account.setAppid(appid);
			account.setAppuserid(appuserid);
			account = mDataBaseService.getAccount(account);
			CacheUtils.put("wxappid"+orderid, appid);
			CacheUtils.put("wxappuserid"+orderid, appuserid);
			CacheUtils.put("account"+orderid, account);
			String url = weixinService.oAuth2oklong();
			log.info("oAuth|发起授权url,获取openid;url={}", url);
			return "redirect:" + url;
		} catch (Exception e) {
			log.error("{}", e);
		}
		return null;
	}

	/**
	 * 授权后回调
	 * 
	 * @return
	 */
	@RequestMapping("/authCallback.htm")
	public String authCallback(HttpServletRequest request, HttpServletResponse response) {
		try {
			String code = request.getParameter("code");
			String orderid = request.getParameter("state");
			log.info("authCallbac|授权后回调;入参;code={},state(sessionid)={}", code, orderid);
			log.info("`|授权后回调;入参;code={},orderid(sessionid)={}", code, orderid);

			String openid = weixinService.authCallbackOklong(request);
			request.getSession().setAttribute("openid", openid);
			CacheUtils.put("openid"+orderid, openid);

			log.info("authCallbac|授权后回调;出参;从授权接口中获取的openid={}", openid);

			// 转发到实际支付地址,带有jsessionid,保存原来状态
			// return
			// "redirect:/weixin/choosePayType.htm?isredirect=true&orderid=" +
			// orderid;
			return "redirect:/weixin/topay.htm?orderid=" + orderid;
		} catch (Exception e) {
			log.error("授权后回调,{}", e);
		}
		return "redirect:/weixin/oAuth2.htm";
	}

	/**
	 * 订单提交到支付页面
	 * 
	 * @return
	 */
	@RequestMapping("/topay.htm")
	public String topay(HttpServletRequest request, Model model) {
		try {
			log.info("topay.htm请求的sessionid=" + request.getSession().getId());

			String orderid = request.getParameter("orderid");
			request.getSession().setAttribute("orderid", orderid);
			log.info("topay.htm请求的orderid=" + orderid);

			// List<Account> accountList = CacheUtils.get("accountList_" +
			// orderid);
			Order order = CacheUtils.get("order_" + orderid);
			Wares wares = CacheUtils.get("wares_" + orderid);
			String SIGN = CacheUtils.get("SIGN_" + orderid);
			String appid = CacheUtils.get("wxappid" + orderid);
			String appuserid = CacheUtils.get("wxappuserid" + orderid);
			// model.addAttribute("accountList", accountList);
			model.addAttribute("wares", wares);
			model.addAttribute("order", order);

			Account account = CacheUtils.get("account"+orderid);

			// 测试用设置
			if ("true".equals(Global.getConfig("weixin.debug"))) {
				order.setPrice(0.01f);
			}

			log.info("account=" + JSON.toJSONString(account) + ";order=" + JSON.toJSONString(order) + ";wares=" + JSON.toJSONString(wares) + ";SIGN" + SIGN);
			String source = "";
			// 根据支付的种类，循环怎么支付成功
			// 豪龙微信支付
			// 判断获取的类型,在线支付调用h5支付
			if (account.getOffline()) {
				// 线下模式二
				Map<String, String> map = weixinService.getQrPayModel2(account, order, wares);
				String codeUrl = map.get("codeUrl");

				String base64Str;
				try {
					base64Str = QRCodeUtil.encode2ImgBase64(codeUrl);
					model.addAttribute("codeUrl", codeUrl);
					model.addAttribute("base64Str", base64Str);
					model.addAttribute("modetype", "模式二");
					return "modules/weixin/qrPayImg";
				} catch (Exception e) {
					log.error("线下模式二,获取支付二维码出错={}", e);
				}

			} else {
				// h5支付
				// 获取h5支付js发起参数
				WxJsapiSignature jsapi;

				jsapi = weixinService.getWxJsapiSignature();
				model.addAttribute("jsapi", jsapi);

				// 后去支付参数
				Map<String, String> payInfo = weixinService.h5Pay(account, order, wares);
				payInfo.put("packageInfo", payInfo.get("package"));
				model.addAttribute("payInfo", payInfo);
				
				String url = (String) request.getSession().getAttribute("url");
				// 组装支付成功后前段页面返回参数和地址
				String requesturl = "";
				if(StringUtils.isNotEmpty(url)){
					url = URLDecoder.decode(url, "UTF-8");
					requesturl = url;
				}else{
					requesturl = wares.getRequesturl() + "?x0=" + order.getFeetype() + "&x1=" + order.getWYID() + "&x2=" + order.getHTID() + "&x3=" + order.getJFYF() + "&x4="
							+ order.getSource() + "&SIGN=" + SIGN;
				}
				
				// wares.setRequesturl(requesturl);
				model.addAttribute("requesturl", requesturl);

				return "modules/weixin/h5Pay";
			}
		} catch (Exception e) {
			log.error("程序出现异常={}", e);
		}
		return "modules/weixin/h5Pay";
	}

	public static void main(String[] args) {

	}

	@ResponseBody
	@RequestMapping("/jssdk.htm")
	public WeJson jssdk(Model model, HttpServletRequest request) {
		try {
			String url = request.getParameter("url");
			if (StringUtils.isBlank(url)) {
				return WeJson.fail("url地址不能为空，请添加后重试");
			}
			WxJsapiSignature jsapi = weixinService.getWxJsapiSignature(url);

			return WeJson.success(jsapi);
		} catch (Exception e) {
			log.error("获取微信签名出错:{}", e);
			return WeJson.fail("获取微信签名出错,请稍后重试");
		}
	}

	/**
	 * 微信预支付发起
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/payInfo.htm")
	public WeJson payInfo(Model model, HttpServletRequest request) {
		try {
			String orderid = String.valueOf(request.getSession().getAttribute("orderid"));
			Order order = CacheUtils.get("order_" + orderid);
			Wares wares = CacheUtils.get("wares_" + orderid);
			String SIGN = CacheUtils.get("SIGN_" + orderid);
			Account account = CacheUtils.get("account_" + orderid);
			// 来源
			String source = account.getSource();
			Map<String, String> payInfo = weixinService.h5Pay(account, order, wares);
			String requesturl = wares.getRequesturl() + "?x0=" + order.getFeetype() + "&x1=" + order.getWYID() + "&x2=" + order.getHTID() + "&x3=" + order.getJFYF() + "&x4="
					+ source + "&x5=" + SIGN;
			wares.setRequesturl(requesturl);
			payInfo.put("requesturl", requesturl);

			return WeJson.success(payInfo);
		} catch (Exception e) {
			log.error("获取微信签名出错:{}", e);
			return WeJson.fail("获取微信签名出错,请稍后重试");
		}
	}

	/**
	 * 选择支付方式,支付宝或微信
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/choosePayType.htm")
	public String choosePayType(HttpServletRequest request, Model model) {

		String orderid = String.valueOf(request.getSession().getAttribute("orderid"));
		String isredirect = request.getParameter("isredirect");

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
	}

}
