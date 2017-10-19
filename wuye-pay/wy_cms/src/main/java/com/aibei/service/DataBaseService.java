package com.aibei.service;

import java.io.IOException;
import java.util.List;

import com.aibei.bean.Account;
import com.aibei.bean.Ad;
import com.aibei.bean.OperateLog;
import com.aibei.bean.Order;
import com.aibei.bean.Statistics;
import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.bean.Wares;

public interface DataBaseService {

	String BEAN_NAME = "DataBaseService";
	

	//收费账号管理操作
	public List<Account> getAccount(Account account) throws Exception;

	public boolean saveAccount(Account account) throws Exception;

	public boolean updateAccount(Account account) throws Exception;
	
	public boolean deleteAccount(Account account) throws Exception;
	
	public Long getAccountCount(Account account) throws Exception;

	//广告管理操作
	public List<Ad> getAd(Ad ad) throws Exception;

	public boolean saveAd(Ad ad) throws Exception;

	public boolean updateAd(Ad ad) throws Exception;
	
	public boolean deleteAd(Ad ad) throws Exception;
	
	public Long getAdCount(Ad ad) throws Exception;

	//订单管理操作
	public boolean deleteOrder(Order order) throws IOException;
	
	public Float getOrderCountPrice(Order order) throws IOException;
	
	public List<Order> getOrder(Order order) throws IOException;
	
	public List<Order> getOrderNoPage(Order order) throws IOException;
	
	
	public boolean updateOrder(Order order) throws IOException;
	
	public Long getOrderCount(Order order) throws IOException;
	
	public List<Order> getErrorOrder() throws IOException;
	
	public boolean quicklyRemove(String time) throws Exception;
	
	public Long getErrorOrderCount() throws IOException;

	//商户域名操作
	public List<UrlMap> getUrlMap(UrlMap urlMap) throws Exception;

	public boolean saveUrlMap(UrlMap urlMap) throws Exception;

	public boolean updateUrlMap(UrlMap urlMap) throws Exception;
	
	public boolean deleteUrlMap(UrlMap urlMap) throws Exception;

	//cms登录账号操作
	public boolean saveUser(User user);

	public List<User> getUsers(User user);

	public boolean updateUser(User user);

	public boolean deleteUser(User user);
	
	//商品编号
	public boolean saveWares(Wares wares);
	
	public Long getWaresCount(Wares wares);
	
	public List<Wares> getWares(Wares wares);
	
	public boolean updateWares(Wares wares);
	
	public boolean deleteWares(Wares wares);
	
	public boolean saveOperateLog(OperateLog operateLog);
	
	//交易统计
	public List<Statistics> getMonthStatistics(Statistics statistics);
	
	public List<Statistics> getDayStatistics(Statistics statistics);

	public Long getUrlMapCount(UrlMap urlmap);

	public Account getAccountById(Integer id);
}
