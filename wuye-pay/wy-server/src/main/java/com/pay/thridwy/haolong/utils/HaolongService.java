package com.pay.thridwy.haolong.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pay.thridwy.haolong.bean.GetContract;
import com.pay.thridwy.haolong.bean.GetContractA;
import com.pay.thridwy.haolong.bean.GetCustomer;
import com.pay.thridwy.haolong.bean.GetLastMonth;
import com.pay.thridwy.haolong.bean.GetProperty;
import com.pay.thridwy.haolong.bean.GetWeiFee;
import com.pay.thridwy.haolong.bean.GetWeiFee1;
import com.pay.thridwy.haolong.bean.GetWeiFee2;
import com.pay.thridwy.haolong.bean.Getfee;
import com.pay.thridwy.haolong.bean.Getfee1;
import com.pay.thridwy.haolong.bean.Getfee2;
import com.pay.thridwy.haolong.bean.PutWeiFee;
import com.pay.thridwy.haolong.bean.PutWeiFee1;
import com.pay.thridwy.haolong.bean.PutWeiFee2;

public interface HaolongService {
	
	public String BEAN_NAME = "HaolongService";

	public List<Map<String, String>> getContract(GetContract bean) throws Exception;

	public List<Map<String, String>> getCustomer(GetCustomer bean) throws Exception;

	public List<Map<String, String>> getFee(Getfee bean) throws Exception;

	public List<Map<String, String>> getFee1(Getfee1 bean) throws Exception;
	
	public List<Map<String, String>> getFee2(Getfee2 bean) throws Exception;
	
	public List<Map<String, String>> getLastMonth(GetLastMonth bean) throws Exception;

	public List<Map<String, String>> getWeiFee(GetWeiFee bean) throws IOException;
	
	public List<Map<String, String>> getWeiFee1(GetWeiFee1 bean) throws IOException;
	
	public List<Map<String, String>> getWeiFee2(GetWeiFee2 bean) throws IOException;

	public String putWeiFee(PutWeiFee bean) throws Exception;
	
	public String putWeiFee1(PutWeiFee1 bean) throws Exception;
	
	public String putWeiFee2(PutWeiFee2 bean) throws Exception;

	public List<Map<String, String>> getProperty(GetProperty bean) throws Exception;

	public List<Map<String, String>> getContractA(GetContractA bean) throws Exception;
	
	
}
