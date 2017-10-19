package com.aibei.thridwy.haolong.utils.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aibei.thridwy.haolong.bean.GetContract;
import com.aibei.thridwy.haolong.bean.GetContractA;
import com.aibei.thridwy.haolong.bean.GetCustomer;
import com.aibei.thridwy.haolong.bean.GetLXXM;
import com.aibei.thridwy.haolong.bean.GetLastMonth;
import com.aibei.thridwy.haolong.bean.GetProperty;
import com.aibei.thridwy.haolong.bean.GetSKID;
import com.aibei.thridwy.haolong.bean.GetWeiFee;
import com.aibei.thridwy.haolong.bean.Getfee;
import com.aibei.thridwy.haolong.bean.PutWeiFee;
import com.aibei.thridwy.haolong.bean.PutWeiFee1;
import com.aibei.thridwy.haolong.bean.PutWeiFee2;
import com.aibei.thridwy.haolong.utils.HaolongService;
import com.aibei.thridwy.haolong.utils.HaolongServiceUtil;

@Service(HaolongService.BEAN_NAME)
public class HaolongServiceImpl implements HaolongService {

	@Override
	public List<Map<String, String>> getContract(GetContract bean)
			throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

	@Override
	public List<Map<String, String>> getContractA(GetContractA bean)
			throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}
	
	@Override
	public List<Map<String, String>> getCustomer(GetCustomer bean)
			throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

	@Override
	public List<Map<String, String>> getFee(Getfee bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

	@Override
	public List<Map<String, String>> getLastMonth(GetLastMonth bean)
			throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

	@Override
	public List<Map<String, String>> getWeiFee(GetWeiFee bean)
			throws IOException {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> resultMap = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return resultMap;
	}

	@Override
	public String putWeiFee(PutWeiFee bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		String SKID = HaolongServiceUtil.getPay(urlString, soapActionString,
				bean);
		return SKID;
	}

	@Override
	public List<Map<String, String>> getProperty(GetProperty bean)
			throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

	@Override
	public String putWeiFee1(PutWeiFee1 bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		String SKID = HaolongServiceUtil.getPay(urlString, soapActionString,
				bean);
		return SKID;
	}

	@Override
	public String putWeiFee2(PutWeiFee2 bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		String SKID = HaolongServiceUtil.getPay(urlString, soapActionString,
				bean);
		return SKID;
	}

	@Override
	public String getSKID(GetSKID bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		String SKID = HaolongServiceUtil.getPay(urlString, soapActionString,
				bean);
		return SKID;
	}
	
	@Override
	public List<Map<String, String>> getLXXM(GetLXXM bean) throws Exception {
		String urlString = bean.getUrlString();
		String soapActionString = bean.getSoapActionString();
		List<Map<String, String>> jsonArryList = HaolongServiceUtil
				.toMapListService(urlString, soapActionString, bean);
		return jsonArryList;
	}

}
