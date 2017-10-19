package com.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.pay.bean.PayInfo;
import com.pay.common.Constant;
import com.pay.common.HttpUtils;
import com.pay.common.sign.SignHelper;
import com.pay.service.AibeiService;

@Service(AibeiService.BEAN_NAME)
public class AibeiServiceImpl implements AibeiService {

	private static Logger logger = LoggerFactory.getLogger(AibeiServiceImpl.class);

	/*
	 * 通过应用appId调用商户服务端去查找要传入收银台的订单transId 参数 appid 平台分配的应用编号,waresid 应用中的商品编号,
	 * cporderid 商户生成的订单号，price 支付金额(非必填),currency 货币类型, appuserid
	 * 用户id建议为用户帐号,notifyurl 商户服务端接收支付结果通知的地址,waresname 商品名称, sign
	 * 对transdata的签名数据(transId+redirecturl+cpurl) ,signtype 签名算法类型
	 */
	@Override
	public String getTransId(PayInfo payInfo) {
		String appid = payInfo.getAppid();
		int waresid = payInfo.getWaresid();
		String cporderid = payInfo.getCporderid();
		String cpprivateinfo = payInfo.getCpprivateinfo();// 放HTID,JFYF,BZID的list
		String currency = payInfo.getCurrency();
		String appuserid = payInfo.getAppuserid();
		Float price = payInfo.getPrice();
		String notifyurl = payInfo.getNotifyurl();
		String waresname = payInfo.getWaresname();
		String signtype = payInfo.getSigntype();
		String appv_key = payInfo.getAppv_key();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", appid);
		jsonObject.put("waresid", waresid);
		jsonObject.put("cporderid", cporderid);
		jsonObject.put("currency", currency);
		jsonObject.put("appuserid", appuserid);
		// 以下是参数列表中的可选参数
		if (!waresname.isEmpty()) {
			jsonObject.put("waresname", waresname);
		}
		if (price != 0) {
			/*
			 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候 price 不用传值
			 */
			jsonObject.put("price", price);
		}
		if (!cpprivateinfo.isEmpty()) {
			jsonObject.put("cpprivateinfo", cpprivateinfo);
		}
		if (!notifyurl.isEmpty()) {

			/*
			 * * 如果此处不传同步地址，则是以后台传的为准。
			 */
			jsonObject.put("notifyurl", notifyurl);
		}
		String content = jsonObject.toString();// 组装成 json格式数据
		String sign = SignHelper.sign(content, appv_key);
		String data = "transdata=" + content + "&sign=" + sign + "&signtype=" + signtype;

		logger.info("调用爱贝预支付接口，传入参数,data={}",data);
		
		String resultData = HttpUtils.sentPost(Constant.IAPPPAY_ORDER_HTTP_URL, data, "UTF-8");
		
		logger.info("调用爱贝预支付接口，传出参数,resultData={}",resultData);
		
		return resultData.split("&")[0].split("=")[1];

	}

}
