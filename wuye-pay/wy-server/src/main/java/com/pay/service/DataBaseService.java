package com.pay.service;

import java.io.IOException;
import java.util.List;


import com.pay.bean.Account;
import com.pay.bean.Ad;
import com.pay.bean.Customer;
import com.pay.bean.Order;
import com.pay.bean.Property;
import com.pay.bean.Urlmap;
import com.pay.bean.Wares;

public interface DataBaseService {
	
	public String BEAN_NAME = "DataBaseService";

	// 保存客户信息
	public boolean saveCustomer(Customer customer);
	
	// 获取客户信息
	public List<Customer> getCustomer(Customer customer);

	// 更新客户信息
	public boolean updateCustomer(Customer customer);

//	// 保存物业信息
//	public boolean saveProperty(Property property);
//	
//	// 获取物业信息
//	public List<Property> getProperty(Property property);
//
//	// 更新物业信息
//	public boolean updateProperty(Property property);

	// 获取单个收费账号信息
	public Account getAccount(Account account);

	// 获取广告信息
	public List<Ad> getAd(Ad ad) throws Exception;

	// 保存广告信息
	public boolean saveAd(Ad ad) throws Exception;

	// 更新广告信息
	public boolean updateAd(Ad ad) throws Exception;

	// 获取订单信息
	public List<Order> getOrder(Order order) throws IOException;

	// 保存订单信息
	public boolean saveOrder(Order order) throws IOException;

	// 更新订单信息
	public boolean updateOrder(Order order) throws IOException;
	

	// 通过urlkey值获得urlString和soapActionString
	public Urlmap getUrlmap(Urlmap urlmap) throws Exception;
	
	public Wares getWares(Wares wares) throws Exception;

	/**
	 *	通过source和WYID获取账号list
	 * @param account
	 * @return
	 */
	public List<Account> getAccountList(Account account);
	
}
