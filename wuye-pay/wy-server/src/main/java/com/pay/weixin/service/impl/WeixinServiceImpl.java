package com.pay.weixin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.pay.bean.Account;
import com.pay.bean.Member;
import com.pay.bean.Order;
import com.pay.bean.Wares;
import com.pay.common.conf.Global;
import com.pay.common.constants.PayTypeConst;
import com.pay.common.utils.CacheUtils;
import com.pay.common.utils.OrderUtils;
import com.pay.common.utils.ServletUtil;
import com.pay.common.utils.StringUtils;
import com.pay.common.utils.WeixinUtil;
import com.pay.common.utils.XmlUtil;
import com.pay.dto.WeJson;
import com.pay.mapper.DataBaseServiceMapper;
import com.pay.weixin.service.WeixinService;

@Service
@Scope("prototype")
public class WeixinServiceImpl implements WeixinService {
	private Logger log = LoggerFactory.getLogger(WeixinServiceImpl.class);

	@Autowired
	private WxMpService wxMpService;
	private WxMpInMemoryConfigStorage config = null;

	@Autowired
	private DataBaseServiceMapper mapper;

	/**
	 * 获取二维码支付链接
	 * 
	 * @param request2
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getQrPayModel2(HttpServletRequest request) throws Exception {
		double money = Double.valueOf(String.valueOf(request.getParameter("price")));
		return toQrpayModel2(money);
	}

	private Map<String, String> toQrpayModel2(double money) throws Exception {
		// 设置配置信息
		setConfig();
		// 将数据保存到数据库中以备对账
		String body = "物业管理费";// 商品名称
		String out_trade_no = OrderUtils.getOrderNo(1);// 生成订单 ；正常订单80结尾
		String total_fee = String.valueOf((int) (money * 100));// 以分为单位
		String spbill_create_ip = StringUtils.getRemoteAddr();// 远程支付地址
		spbill_create_ip = StringUtils.isBlank(spbill_create_ip) ? "120.25.149.6" : spbill_create_ip;
		String notify_url = Global.getConfig("server.project.url") + "/weixin/notify";// 后台接受支付成功通知地址
		String trade_type = "NATIVE";// 调用方式,JSAPI，NATIVE，APP

		// 组装请求参数
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);// 金额要乘以100,发送过去的数据全部是整数,没有小数
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("product_id", "12235413214070356458058");// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。

		log.debug("parameters={}", parameters);
		// 获取扫码支付参数
		Map<String, String> map = wxMpService.getNativePayInfo(parameters);

		log.info("获取jsapi微信支付参数 map={}", map);
		return map;
	}

	private void setConfig() throws RuntimeException {
		setConfig(null);
	}

	/**
	 * 二维码支付配置初始化信息
	 * 
	 * @param state
	 * 
	 * @throws Exception
	 */
	private void setConfig(String state) throws RuntimeException {
		String orderid = null;
		if (StringUtils.isNotBlank(state)) {
			orderid = state;
		} else {
			orderid = (String) ServletUtil.getRequest().getSession().getAttribute("orderid");
			log.info("orderid={}", orderid);
			/*if (StringUtils.isBlank(orderid)) {
				orderid = (String) ServletUtil.getRequest().getSession().getAttribute("orderid");
			}*/
		}

		log.info("state={},orderid={}", state, orderid);

		// 从缓存中获取账号信息
		List<Account> accountList = CacheUtils.get("accountList_" + orderid);

		//log.info("accountList={},size={}", JSON.toJSONString(accountList), accountList.size());

		// 获取微信配置参数
		Account wxAccount =CacheUtils.get("account"+orderid);

		log.info("wxAccount={}", JSON.toJSONString(wxAccount));

		// 将账号信息放入缓存中
		if (CacheUtils.get("account_" + orderid) != null) {
			CacheUtils.remove("account_" + orderid);
		}
		CacheUtils.put("account_" + orderid, wxAccount);

		config = new WxMpInMemoryConfigStorage();
		// 获取微信支付账号
		if (wxAccount != null) {
			if (StringUtils.isNotBlank(wxAccount.getPlatp_key())) {
				config.setAppId(wxAccount.getPlatp_key().trim());// 微信号
			} else {
				throw new RuntimeException("获取微信号出错，数据库没有配置Platp_key字段");
			}

			if (StringUtils.isNotBlank(wxAccount.getAppSecret())) {
				config.setSecret(wxAccount.getAppSecret().trim()); // 设置微信秘钥
			} else {
				throw new RuntimeException("获取微信秘钥出错，数据库没有配置AppSecret字段");
			}

			if (StringUtils.isNotBlank(wxAccount.getAppid())) {
				config.setPartnerId(wxAccount.getAppid().trim());// 服务商商户号
			} else {
				throw new RuntimeException("获取服务商商户号出错，数据库没有配置Appid字段");
			}

			if (StringUtils.isNotBlank(wxAccount.getAppv_key())) {
				config.setPartnerKey(wxAccount.getAppv_key().trim());// 服务商秘钥
			} else {
				throw new RuntimeException("获取服务商秘钥出错，数据库没有配置Appv_key字段");
			}

		} else {
			log.error("微信支付账号没有配置好,请先进行配置，然后在执行");
			throw new RuntimeException("微信支付账号没有配置好,请先进行配置，然后在执行");
		}

		//log.info("config={}", JSON.toJSONString(config));

		config.setOauth2redirectUri(Global.getConfig("server.project.url").trim() + "/wy-server/weixin/authCallback.htm");
		wxMpService.setWxMpConfigStorage(config);
	}

	/**
	 * 生成模式一的微信支付URL地址
	 * 
	 * @param request2
	 * @return
	 */
	public String getNativeMode1Bizpayurl(HttpServletRequest request2) {
		setConfig();
		String productId = request2.getParameter("pid");// 商品id
		// 获取扫描支付模式一的支付参数,获取url地址字符串
		return wxMpService.getNativeMode1Bizpayurl(productId);
	}

	public String dealQrPayMode1(String body) throws Exception {
		setConfig();
		Map<String, String> map = XmlUtil.xml2map(body);

		// 校验是否通过验证
		boolean signFlagb = wxMpService.checkJSSDKCallbackDataSignature(map, map.get("sign"));
		if (!signFlagb) {
			return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名校验失败]]></return_msg></xml>";
		}

		// 产品id
		String product_id = map.get("product_id");
		String openid = map.get("openid");
		Double money = Double.valueOf(map.get("money"));

		Map<String, String> map2 = toQrpay(money, product_id);

		// 获取body信息
		String prepay_id = map2.get("package");
		String prepay_id2[] = prepay_id.split("=");

		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("return_code", "SUCCESS");// SUCCESS/FAIL 返回状态码
		// respMap.put("return_msg", "支付成功");// SUCCESS/FAIL
		respMap.put("appid", config.getAppId());// SUCCESS/FAIL 公众账号ID
		respMap.put("mch_id", config.getPartnerId());// SUCCESS/FAIL 商户号
		respMap.put("nonce_str", map2.get("nonceStr"));// SUCCESS/FAIL 随机字符串
		respMap.put("prepay_id", prepay_id2[1]);// SUCCESS/FAIL 预支付ID
		respMap.put("result_code", "SUCCESS");// SUCCESS/FAIL 业务结果

		// 获取签名
		String finalSign = WxCryptUtil.createSign(respMap, config.getPartnerKey());
		respMap.put("sign", finalSign);

		return XmlUtil.map2xml2(respMap);
	}

	/**
	 * 处理扫码支付模式
	 * 
	 * @param money
	 * @param product_id
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> toQrpay(double money, String product_id) throws Exception {
		// 设置配置信息
		setConfig();
		// 将数据保存到数据库中以备对账
		String body = "物业管理费";// 商品名称
		String out_trade_no = product_id;// 生成订单
		String total_fee = String.valueOf((int) (money * 100));// 以分为单位
		String spbill_create_ip = StringUtils.getRemoteAddr();// 远程支付地址
		String notify_url = Global.getConfig("server.project.url") + "/weixin/notify.htm";// 后台接受支付成功通知地址
		String trade_type = "NATIVE";// 调用方式,JSAPI，NATIVE，APP
		// String sub_mch_id = account.getAppuserid();// 子商户号
		// 组装请求参数
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);// 金额要乘以100,发送过去的数据全部是整数,没有小数
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("product_id", product_id);// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。

		// if (StringUtils.isNotBlank(sub_mch_id)) {
		// parameters.put("sub_mch_id", sub_mch_id);// sub_mch_id
		// }
		// parameters.put("sub_mch_id", sub_mch_id);// 子商户号

		log.debug("parameters={}", parameters);
		// 获取扫码支付参数
		Map<String, String> map = wxMpService.getNativePayInfo(parameters);

		log.info("获取jsapi微信支付参数 map={}", map);
		return map;
	}

	/**
	 * h5支付
	 * 
	 * @param price
	 */
	public WeJson h5Pay(double price) {
		setConfig();
		// 将数据保存到数据库中以备对账
		String body = "物业管理费";// 商品名称
		String out_trade_no = OrderUtils.getOrderNo(1);// 生成订单 ；正常订单80结尾
		String total_fee = String.valueOf((int) (price * 100));// 以分为单位
		String spbill_create_ip = StringUtils.getRemoteAddr();// 远程支付地址
		spbill_create_ip = StringUtils.isBlank(spbill_create_ip) ? "120.25.149.6" : spbill_create_ip;
		String notify_url = Global.getConfig("server.project.url") + "/weixin/notify.htm";// 后台接受支付成功通知地址
		String trade_type = "JSAPI";// 调用方式,JSAPI，NATIVE，APP

		// 组装请求参数
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);// 金额要乘以100,发送过去的数据全部是整数,没有小数
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("openid", "o3v4BuEXevFBJhmpPAXnNsFBWFrw");// openid

		log.debug("parameters={}", parameters);
		// 获取jsapi微信支付参数
		Map<String, String> map = wxMpService.getJSSDKPayInfo(parameters);

		log.info("获取jsapi微信支付参数 map={}", map);

		return WeJson.success(map);

	}

	/**
	 * 获取js支付签名信息
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	public WxJsapiSignature getWxJsapiSignature(String url) throws WxErrorException {
		setConfig();
		String jsapiTicket = wxMpService.getJsapiTicket();

		log.info("jsapiTicket={};当前连接请求地址:url={}", jsapiTicket, url);

		WxJsapiSignature wxJsapiSignature = WeixinUtil.sign(jsapiTicket, url, config.getAppId());
		log.info("获取js验证签名wxJsapiSignature={}", JSON.toJSONString(wxJsapiSignature));

		return wxJsapiSignature;
	}

	/**
	 * 获取js支付签名信息
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	public WxJsapiSignature getWxJsapiSignature() throws WxErrorException {
		setConfig();
		String jsapiTicket = wxMpService.getJsapiTicket();

		// 获取外网地址
		//String url = ServletUtil.getRequestOuterNetUrlAndParas();
		String url = ServletUtil.getRequestUrlAndParas();
		log.info("jsapiTicket={};当前连接请求地址:url={}", jsapiTicket, url);

		WxJsapiSignature wxJsapiSignature = WeixinUtil.sign(jsapiTicket, url, config.getAppId());
		//log.info("获取js验证签名wxJsapiSignature={}", JSON.toJSONString(wxJsapiSignature));

		return wxJsapiSignature;
	}

	/**
	 * 拼接授权连接
	 */
	public String oAuth2oklong() {
		setConfig();
		String orderid = String.valueOf(ServletUtil.getRequest().getSession().getAttribute("orderid"));

		// 测试用返回地址
		if ("true".equals(Global.getConfig("weixin.debug"))) {
			//return "/weixin/choosePayType.htm?isredirect=true&orderid=" + orderid;
			return "/weixin/topay.htm?orderid=" + orderid;
		}

		// 获取进入页面的用户的openid,带有请求sessionId
		return wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE, orderid);
	}

	/**
	 * 回调处理
	 * 
	 * @param request2
	 * @return
	 */
	public String authCallbackOklong(HttpServletRequest request2) {
		try {
			String code = request2.getParameter("code");
			String state = request2.getParameter("state");

			setConfig(state);
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state)) {
				// 获取票据信息
				wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
				log.debug("wxMpOAuth2AccessToken={}", wxMpOAuth2AccessToken);

			}
			return wxMpOAuth2AccessToken.getOpenId();
		} catch (Exception e) {
			log.error("异常信息={}", e);
		}
		return "";
	}

	@Override
	public String weixinPay(int confid, Order order) {

		// 获取配置信息
		Account account = new Account();
		// account.setId(confid);
		account = mapper.getAccount(account);

		// 爱贝支付
		if (PayTypeConst.PAY_TYPE_PLATFORM_AIBEI.equals(order.getPaytype())) {

		} else if (PayTypeConst.PAY_TYPE_PLATFORM_WEIXIN.equals(order.getPaytype())) {
			// 好邻邦微信支付
			// 判断获取的类型,在线支付调用h5支付
			if (account.getOffline()) {
				// h5Pay(account, order);
			} else {
				// 线下扫码支付,如果是打印到支付订单上，就使用第一种支付模式，否则使用第二种支付模式
				// toQrpayModel2("");
			}
		} else if (PayTypeConst.PAY_TYPE_PLATFORM_ALIPAY.equals(order.getPaytype())) {
			// 好邻邦支付宝支付

		} else {
			// 其他默认支付
		}

		return null;
	}

	/**
	 * 处理h5页面调用支付
	 * 
	 * @param account
	 * @param order
	 * @return
	 */
	public Map<String, String> h5Pay(Account account, Order order, Wares wares) {

		// 获取会员信息
		Member member = new Member();
		//member.setOpenid(String.valueOf(ServletUtil.getRequest().getSession().getAttribute("openid")));
		member.setOpenid(String.valueOf(CacheUtils.get("openid"+order.getCporderid())));
		
		// 测试用设置
		if ("true".equals(Global.getConfig("weixin.debug"))) {
			member.setOpenid(Global.getConfig("weixin.openid"));
		}

		setConfig();
		// 将数据保存到数据库中以备对账
		String body = wares.getWaresname();// 商品名称信息
		String out_trade_no = order.getCporderid();// 订单号
		String total_fee = String.valueOf(Math.round(order.getPrice() * 100));// 以分为单位,金额要乘以100,发送过去的数据全部是整数,没有小数
		//String total_fee = String.valueOf((int) (0.01 * 100));
		String spbill_create_ip = StringUtils.getRemoteAddr();// 获取服务器ip
		// 回调地址配置
		String notify_url = wares.getNotifyurl();
		String trade_type = "JSAPI";// 调用方式,JSAPI，NATIVE，APP
		String openid = member.getOpenid();// openid
		String sub_mch_id = account.getAppuserid();// 子商户号

		// 组装请求参数
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);// 以分为单位的整数
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("openid", openid);// openid
		if (StringUtils.isNotBlank(sub_mch_id)) {
			parameters.put("sub_mch_id", sub_mch_id);// sub_mch_id
		}

		log.debug("组装请求参数parameters={}", parameters);

		// 获取jsapi微信支付参数
		Map<String, String> map = wxMpService.getJSSDKPayInfo(parameters);

		log.info("获取jsapi微信支付参数出参map={}", map);

		return map;
	}

	@Override
	public Map<String, String> getQrPay(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String topay(Account account, Order order) {

		return null;
	}

	@Override
	public WxJsapiSignature getWxJsapiSignature(Account account, Order order) throws WxErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getQrPayModel2(Account account, Order order, Wares wares) throws Exception {

		// 设置配置信息
		setConfig();
		// 将数据保存到数据库中以备对账
		String body = wares.getWaresname();// 商品名称
		String out_trade_no = order.getCporderid();// 生成订单 ；正常订单80结尾
		String total_fee = String.valueOf((Math.round(order.getPrice() * 100)));// 以分为单位
		String spbill_create_ip = StringUtils.getRemoteAddr();// ip
		String notify_url = Global.getConfig("server.project.url") + "/weixin/notify.htm";// 后台接受支付成功通知地址
		String trade_type = "NATIVE";// 调用方式,JSAPI，NATIVE，APP

		// 组装请求参数
		Map<String, String> parameters = new HashMap<String, String>();
		String product_id = order.getWaresid();
		String sub_mch_id = account.getAppuserid();// 子商户号

		parameters.put("body", body);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);// 金额要乘以100,发送过去的数据全部是整数,没有小数
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("product_id", product_id);// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
		if (StringUtils.isNotBlank(sub_mch_id)) {
			parameters.put("sub_mch_id", sub_mch_id);// sub_mch_id
		}

		log.debug("parameters={}", parameters);
		// 获取扫码支付参数
		Map<String, String> map = wxMpService.getNativePayInfo(parameters);

		log.info("获取jsapi微信支付参数 map={}", map);
		return map;

	}

	/**
	 * 处理微信支付回调校验
	 */
	@Override
	public Map<String, String> dealNotify(String body) throws DocumentException {
		Map<String, String> map = XmlUtil.xml2map(body);
		ServletUtil.getRequest().getSession().setAttribute("orderid", map.get("out_trade_no"));
		setConfig();

		// 校验是否通过验证
		boolean signFlagResult = wxMpService.checkJSSDKCallbackDataSignature(map, map.get("sign"));
		map.put("signFlagResult", String.valueOf(signFlagResult));

		return map;
	}
}
