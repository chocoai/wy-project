package com.pay.alipay.web;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.pay.alipay.service.AlipayService;
import com.pay.alipay.utils.AlipayConfig;
import com.pay.common.utils.DateUtils;



@Controller
@RequestMapping("/alipay")
public class AlipayController {

	private Logger log = LoggerFactory.getLogger(AlipayController.class);

	@Autowired
	private AlipayService alipayService;

	@ResponseBody
	@RequestMapping("/topay.htm")
	public String topay() {

		// boolean b = alipayService.topay();

		return "success";
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping("/authCallback.htm" ) public String
	 * authCallback(HttpServletRequest request) {
	 * 
	 * // boolean b = alipayService.topay(); String app_auth_code =
	 * request.getParameter("app_auth_code"); String app_id =
	 * request.getParameter("app_id");
	 * 
	 * return "success"; }
	 */

	@ResponseBody
	@RequestMapping("authCallback.htm")
	public String authCallback(HttpServletRequest request1) throws AlipayApiException {

		// boolean b = alipayService.topay();
		String app_auth_code = request1.getParameter("app_auth_code");
		String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+0EH7pbvqasw5A8Mgic6t8/MUvayMfYdOllSZq7/fTO3fwZ0HhIVPdY4ifQNCZ61mE6G3wY79B5I8wpFuRF9LZ8J7C/4XSi/uKUUhQVj1etrBX4FsBQ64Orj9KcaJS2Mn81GQw7I6jdZ2hIT7Kn7aaDNxiC3sonRzMTdkOFr49VZAjmLAQaqHezl8jJIRrVYnBZF/weqNp4/lEyV8o5KBEasrj42Ld044ru2EHd7LRvWrxlAGiCeR7aKSPlgm+lqfDOiKssO24BhpFyORsGY+Mn3AZcWCcnNfzp/L5V/yub/ImkIn5EtqB62mhRu56VWaIcpUSz/mvuXllebOG8OJAgMBAAECggEAOPwQ+IpCWHuUieJpv6noI2MbUTJj+YDzi7Cxi0MThih/UbeModYdyeEdlYcoFgjIbBeo0Cvp+/7q2WJx9DBPFUHjfsPSGjp5nfKNRqUxX/5UnjNbf1rzTmmbHWAAfrgY4LF1xnnrHDmHttVUsX27bJZ55on4zRanGE+2byO1Z2u49wJ/2xkw2uNMy5Ax45xtMasngbeoYpC+jCMQMERee36ABbqV7X4n/0/IEXSumtfWEg1kUb2kKqDyy1gTmMVeWGNmSvhAT/2vI0D9Hym5DnP1H5Iq1LaJig89BOTyjBQmYUEpl5G0Wkj2EBrKbo8ZI6pbl2tKh+WMRXhmRq0vEQKBgQDuYfhtKMe1YAsxzRsWC7gCmwhIUAEa02ftFXEikC22kc2KDcocdIlr1Vw4y+Oz7/S19XPhlJnvOjBuMpaJJXnl5Y9F0WTcBKWghkcX/XIr6Xclr7IxZi0iVKRVC7GgR8JTAkrDew/SkX840OU+MtFTNcr7xR21j2+nWBQ941YNhwKBgQDM6k/4cslhJAjpgwLPNstoZRyBXc4tgfAYd2af1sryfHbH9ewBfaGv/CbRX+bfrfsFJE4O0OrYwE+7uIJPLYrTqWfjTr+NfnVJd1iy//v9ApOjkZLztosiQxWwqu9FBGXW6a9BPgazLq+xCln8RiUlQMSwFgUtK58iXYVF8L5qbwKBgQDn8c19sPs4YH4j89TSC478ggp6CdY5Ws+5IG7XWYARLybVl/s62q1Hje9QmBozX1w0m+In8KYi3fR3lKNiSGOmLEnOfiJr7d2WJnEQR5uKXHyA38Y7SJbL252V3m3zbT8H86f5Mkk/8i/egWs8rMd99k5N0QrKOQlDw3DWkteNCwKBgGQPntFRK+jVccyk/sA0n4rwpMS/C6jJJHauB1zRw/Q4tr/Z5E6uDEAlPsdKrzBWgpb2LW+qsKL69XRTGr6THJFSzbn/gCYVtCwVrShrO59GnNVohnufjb1DSNol/gSTqy8QmVWgdiEVERHAXkSD9CuGvwTFG+38uQ5M4IFQl4rdAoGAYpOPMONelR1m1CZbo+9b48SWAYtWzCSSKJiQhp7TAMBLR9oejRN0CymEuBwtuBz1OU8xI49ns/yxijYqu4tdvLXLFH9yKfYFTyxPNKujhS/4V7fHBV9B7VdV2C8yPtQ/Q/8CfaIiMkYZ0qkgc97+jlaP+aUOJnrXPCc0XKSVQyY=";
		String APPID = "2017042006835717";
		String ALIPAY_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtBB+6W76mrMOQPDIInOrfPzFL2sjH2HTpZUmau/30zt38GdB4SFT3WOIn0DQmetZhOht8GO/QeSPMKRbkRfS2fCewv+F0ov7ilFIUFY9XrawV+BbAUOuDq4/SnGiUtjJ/NRkMOyOo3WdoSE+yp+2mgzcYgt7KJ0czE3ZDha+PVWQI5iwEGqh3s5fIySEa1WJwWRf8HqjaeP5RMlfKOSgRGrK4+Ni3dOOK7thB3ey0b1q8ZQBognke2ikj5YJvpanwzoirLDtuAYaRcjkbBmPjJ9wGXFgnJzX86fy+Vf8rm/yJpCJ+RLagetpoUbuelVmiHKVEs/5r7l5ZXmzhvDiQIDAQAB";

		// String app_id = request1.getParameter("app_id");

		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID, RSA_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizContent("{" + "    \"grant_type\":\"authorization_code\"," + "    \"code\":\"" + app_auth_code + "\"," + "  }");
		
		AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
//		String token = response.getAppAuthToken();
		
		log.info("token={}",response.getAppAuthToken());

		log.info("AlipayOpenAuthTokenAppResponse={}", JSON.toJSONString(response));

		if (response.isSuccess()) {
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
		}

		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/alipay.htm", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET })
	public static String alipay(String body, String subject, String out_trade_no, String total_amount) throws Exception {

		// 公共参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", AlipayConfig.app_id);
		map.put("method", "alipay.trade.app.pay");
		map.put("format", "json");
		map.put("charset", "utf-8");
		map.put("sign_type", "RSA");
		map.put("timestamp", DateUtils.formatDateTime(new Date()));
		map.put("version", "1.0");
		map.put("notify_url", AlipayConfig.service);

		Map<String, String> m = new HashMap<String, String>();

		m.put("body", body);
		m.put("subject", subject);
		m.put("out_trade_no", out_trade_no);
		m.put("timeout_express", "30m");
		m.put("total_amount", total_amount);
		m.put("seller_id", AlipayConfig.partner);
		m.put("product_code", "QUICK_MSECURITY_PAY");

		String bizcontentJson = JSON.toJSONString(m);

		// map.put("biz_content", );
		// 对未签名原始字符串进行签名
		String rsaSign = AlipaySignature.rsaSign(map, AlipayConfig.private_key, "utf-8");

		Map<String, String> map4 = new HashMap<String, String>();

		map4.put("app_id", AlipayConfig.app_id);
		map4.put("method", "alipay.trade.app.pay");
		map4.put("format", "json");
		map4.put("charset", "utf-8");
		map4.put("sign_type", "RSA");
		map4.put("timestamp", URLEncoder.encode(DateUtils.formatDateTime(new Date()), "UTF-8"));
		map4.put("version", "1.0");
		map4.put("notify_url", URLEncoder.encode(AlipayConfig.service, "UTF-8"));
		// 最后对请求字符串的所有一级value（biz_content作为一个value）进行encode，编码格式按请求串中的charset为准，没传charset按UTF-8处理
		map4.put("biz_content", URLEncoder.encode(bizcontentJson.toString(), "UTF-8"));

		return "";
	}

	@ResponseBody
	@RequestMapping("/callback.htm")
	// @RequestMapping(value = "/callback.htm", produces =
	// "text/html;charset=UTF-8", method = { RequestMethod.POST })
	public String callback(HttpServletRequest request) throws Exception {
		// 接收支付宝返回的请求参数
		Map requestParams = request.getParameterMap();

		return "SUCCESS";
	}

	@RequestMapping("/toUrl.htm")
	public String toUrl() {
		String url = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?";
		String app_id = "2017042006835717";
		// String redirect_uri =
		// "http://oklong-pay.tunnel.qydev.com/hlb-shequ-server/wuye/getCode";
		String redirect_uri = "http://oklong-pay.tunnel.qydev.com/wy-server/alipay/authCallback.htm";
		String urls = url + "app_id=" + app_id + "&redirect_uri=" + redirect_uri;

		return "redirect:" + urls;
	}

	public String toAlipay() throws AlipayApiException {
		String app_auth_code = "201704BB211ac13b207c46368719aface43c3X83";
		String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+0EH7pbvqasw5A8Mgic6t8/MUvayMfYdOllSZq7/fTO3fwZ0HhIVPdY4ifQNCZ61mE6G3wY79B5I8wpFuRF9LZ8J7C/4XSi/uKUUhQVj1etrBX4FsBQ64Orj9KcaJS2Mn81GQw7I6jdZ2hIT7Kn7aaDNxiC3sonRzMTdkOFr49VZAjmLAQaqHezl8jJIRrVYnBZF/weqNp4/lEyV8o5KBEasrj42Ld044ru2EHd7LRvWrxlAGiCeR7aKSPlgm+lqfDOiKssO24BhpFyORsGY+Mn3AZcWCcnNfzp/L5V/yub/ImkIn5EtqB62mhRu56VWaIcpUSz/mvuXllebOG8OJAgMBAAECggEAOPwQ+IpCWHuUieJpv6noI2MbUTJj+YDzi7Cxi0MThih/UbeModYdyeEdlYcoFgjIbBeo0Cvp+/7q2WJx9DBPFUHjfsPSGjp5nfKNRqUxX/5UnjNbf1rzTmmbHWAAfrgY4LF1xnnrHDmHttVUsX27bJZ55on4zRanGE+2byO1Z2u49wJ/2xkw2uNMy5Ax45xtMasngbeoYpC+jCMQMERee36ABbqV7X4n/0/IEXSumtfWEg1kUb2kKqDyy1gTmMVeWGNmSvhAT/2vI0D9Hym5DnP1H5Iq1LaJig89BOTyjBQmYUEpl5G0Wkj2EBrKbo8ZI6pbl2tKh+WMRXhmRq0vEQKBgQDuYfhtKMe1YAsxzRsWC7gCmwhIUAEa02ftFXEikC22kc2KDcocdIlr1Vw4y+Oz7/S19XPhlJnvOjBuMpaJJXnl5Y9F0WTcBKWghkcX/XIr6Xclr7IxZi0iVKRVC7GgR8JTAkrDew/SkX840OU+MtFTNcr7xR21j2+nWBQ941YNhwKBgQDM6k/4cslhJAjpgwLPNstoZRyBXc4tgfAYd2af1sryfHbH9ewBfaGv/CbRX+bfrfsFJE4O0OrYwE+7uIJPLYrTqWfjTr+NfnVJd1iy//v9ApOjkZLztosiQxWwqu9FBGXW6a9BPgazLq+xCln8RiUlQMSwFgUtK58iXYVF8L5qbwKBgQDn8c19sPs4YH4j89TSC478ggp6CdY5Ws+5IG7XWYARLybVl/s62q1Hje9QmBozX1w0m+In8KYi3fR3lKNiSGOmLEnOfiJr7d2WJnEQR5uKXHyA38Y7SJbL252V3m3zbT8H86f5Mkk/8i/egWs8rMd99k5N0QrKOQlDw3DWkteNCwKBgGQPntFRK+jVccyk/sA0n4rwpMS/C6jJJHauB1zRw/Q4tr/Z5E6uDEAlPsdKrzBWgpb2LW+qsKL69XRTGr6THJFSzbn/gCYVtCwVrShrO59GnNVohnufjb1DSNol/gSTqy8QmVWgdiEVERHAXkSD9CuGvwTFG+38uQ5M4IFQl4rdAoGAYpOPMONelR1m1CZbo+9b48SWAYtWzCSSKJiQhp7TAMBLR9oejRN0CymEuBwtuBz1OU8xI49ns/yxijYqu4tdvLXLFH9yKfYFTyxPNKujhS/4V7fHBV9B7VdV2C8yPtQ/Q/8CfaIiMkYZ0qkgc97+jlaP+aUOJnrXPCc0XKSVQyY=";
		String APPID = "2017042006835717";
		String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtBB+6W76mrMOQPDIInOrfPzFL2sjH2HTpZUmau/30zt38GdB4SFT3WOIn0DQmetZhOht8GO/QeSPMKRbkRfS2fCewv+F0ov7ilFIUFY9XrawV+BbAUOuDq4/SnGiUtjJ/NRkMOyOo3WdoSE+yp+2mgzcYgt7KJ0czE3ZDha+PVWQI5iwEGqh3s5fIySEa1WJwWRf8HqjaeP5RMlfKOSgRGrK4+Ni3dOOK7thB3ey0b1q8ZQBognke2ikj5YJvpanwzoirLDtuAYaRcjkbBmPjJ9wGXFgnJzX86fy+Vf8rm/yJpCJ+RLagetpoUbuelVmiHKVEs/5r7l5ZXmzhvDiQIDAQAB";
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2017042006835717", RSA_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY,
				"RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数
		// 此次只是参数展示，未进行字符串转义，实际情况下请转义
		request.setBizContent("  { " + "    \"primary_industry_name\":\"IT科技/IT软件与服务\"," + "    \"primary_industry_code\":\"10001/20102\","
				+ "    \"secondary_industry_code\":\"10001/20102\"," + "    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" + "  }");
		// ISV代理商户调用需要传入app_auth_token
		request.putOtherTextParam("app_auth_token", "201704BB211ac13b207c46368719aface43c3X83");
		AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
		// 调用成功，则处理业务逻辑
		if (response.isSuccess()) {
			// .....
		}

		return null;
	}
	
	@RequestMapping("/testPay.htm")
	public String testPay(){
		
		
		return "";
	}
}
