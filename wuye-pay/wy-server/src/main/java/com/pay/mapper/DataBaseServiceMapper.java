package com.pay.mapper;

import java.util.List;

import com.pay.bean.Account;
import com.pay.bean.Ad;
import com.pay.bean.Customer;
import com.pay.bean.Order;
import com.pay.bean.Urlmap;
import com.pay.bean.Wares;

public interface DataBaseServiceMapper {

	public int saveCustomer(Customer customer);
	public List<Customer> getCustomer(Customer customer);
	public int updateCustomer(Customer customer);

//	public int saveProperty(Property property);
//	public List<Property> getProperty(Property property);
//	public int updateProperty(Property property);

	public Account getAccount(Account account);

	public List<Ad> getAd(Ad ad);
	public int saveAd(Ad ad);
	public int updateAd(Ad ad);

	public List<Order> getOrder(Order order);
	public int saveOrder(Order order);
	public int updateOrder(Order order);


	public Urlmap getUrlmap(Urlmap urlmap);
	
	public Wares getWares(Wares wares);
	public List<Account> getAccountList(Account account);
	
	public Account getHLBPayAccount(Account account);
}
