package com.pay.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.bean.Account;
import com.pay.bean.Ad;
import com.pay.bean.Customer;
import com.pay.bean.Order;
import com.pay.bean.Property;
import com.pay.bean.Urlmap;
import com.pay.bean.Wares;
import com.pay.mapper.DataBaseServiceMapper;
import com.pay.service.DataBaseService;

@Service(DataBaseService.BEAN_NAME)
public class DataBaseServiceImpl implements DataBaseService {

	@Autowired
	private DataBaseServiceMapper mapper;

	// 保存客户信息
	@Override
	public boolean saveCustomer(Customer customer) {
		return mapper.saveCustomer(customer) > 0 ? true : false;
	}
	
	// 获取客户信息
	@Override
	public List<Customer> getCustomer(Customer customer) {
		return mapper.getCustomer(customer);
	}

	// 更新客户信息
	@Override
	public boolean updateCustomer(Customer customer) {
		return mapper.updateCustomer(customer) > 0 ? true : false;
	}

	// 保存物业信息
//	@Override
//	public boolean saveProperty(Property property) {
//		return mapper.saveProperty(property) > 0 ? true : false;
//	}
//	
//	// 获取物业信息
//	@Override
//	public List<Property> getProperty(Property property) {
//		return mapper.getProperty(property);
//	}
//
//	// 更新物业信息
//	@Override
//	public boolean updateProperty(Property property) {
//		return mapper.updateProperty(property) > 0 ? true : false;
//	}

	// 获取单个收费账号信息
	@Override
	public Account getAccount(Account account) {
		return mapper.getAccount(account);
	}

	// 获取广告信息
	@Override
	public List<Ad> getAd(Ad ad) throws Exception {
		return mapper.getAd(ad);
	}

	// 保存广告信息
	@Override
	public boolean saveAd(Ad ad) throws Exception {
		return mapper.saveAd(ad) > 0 ? true : false;
	}

	// 更新广告信息
	@Override
	public boolean updateAd(Ad ad) throws Exception {
		return mapper.updateAd(ad) > 0 ? true : false;
	}

	// 获取订单信息
	@Override
	public List<Order> getOrder(Order order) throws IOException {
		return mapper.getOrder(order);
	}

	// 保存订单信息
	@Override
	public boolean saveOrder(Order order) throws IOException {
		if("".equals(order.getTranstime())){
			order.setTranstime(null);
		}
		if("".equals(order.getUpdatetime())){
			order.setUpdatetime(null);
		}
		if("".equals(order.getCreatetime())){
			order.setCreatetime(null);
		}
		return mapper.saveOrder(order) > 0 ? true : false;
	}

	// 更新订单信息
	@Override
	public boolean updateOrder(Order order) throws IOException {
		return mapper.updateOrder(order) > 0 ? true : false;
	}


	// 通过urlkey值获得urlString和soapActionString
	@Override
	public Urlmap getUrlmap(Urlmap urlmap) throws Exception {
		return mapper.getUrlmap(urlmap);
	}

	@Override
	public Wares getWares(Wares wares) throws Exception {
		return mapper.getWares(wares);
	}

	@Override
	public List<Account> getAccountList(Account account) {
		return mapper.getAccountList(account);
	}
}
