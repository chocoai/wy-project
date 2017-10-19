package com.aibei.thridwy.haolong.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

public interface HaolongService {
	
	public String BEAN_NAME = "HaolongService";

	public List<Map<String, String>> getContract(GetContract bean) throws Exception;

	public List<Map<String, String>> getCustomer(GetCustomer bean) throws Exception;

	public List<Map<String, String>> getFee(Getfee bean) throws Exception;

	public List<Map<String, String>> getLastMonth(GetLastMonth bean) throws Exception;

	public List<Map<String, String>> getWeiFee(GetWeiFee bean) throws IOException;

	public String putWeiFee(PutWeiFee bean) throws Exception;

	public String putWeiFee1(PutWeiFee1 bean) throws Exception;
	
	public List<Map<String, String>> getProperty(GetProperty bean) throws Exception;

	public List<Map<String, String>> getContractA(GetContractA bean) throws Exception;

	public String putWeiFee2(PutWeiFee2 bean) throws Exception;

	public String getSKID(GetSKID bean) throws Exception;
	
	public List<Map<String, String>> getLXXM(GetLXXM bean) throws Exception;

}
