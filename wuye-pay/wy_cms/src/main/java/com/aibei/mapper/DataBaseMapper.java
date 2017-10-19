package com.aibei.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aibei.bean.Account;
import com.aibei.bean.Ad;
import com.aibei.bean.OperateLog;
import com.aibei.bean.Order;
import com.aibei.bean.Statistics;
import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.bean.Wares;


public interface DataBaseMapper {
	

	public List<Account> getAccount(Account account);

	public int saveAccount(Account account);

	public int updateAccount(Account account);
	
	public int deleteAccount(Account account);
	
	public Long getAccountCount(Account account);

	public List<Ad> getAd(Ad ad);

	public int saveAd(Ad ad);

	public int updateAd(Ad ad);
	
	public int deleteAd(Ad ad);
	
	public Long getAdCount(Ad ad);
	
	public List<Order> getOrder(Order order);
	
	public List<Order> getOrderNoPage(Order order);
	
	public int deleteOrder(Order order);
	
	public Float getOrderCountPrice(Order order);
	
	public int updateOrder(Order order);
	
	public int quicklyRemove(@Param(value="time")String time);
	
	public List<Order> getErrorOrder();
	
	public Long getErrorOrderCount();
	
	public Long getOrderCount(Order order);

	public List<UrlMap> getUrlMap(UrlMap urlMap);

	public int saveUrlMap(UrlMap urlMap);

	public int updateUrlMap(UrlMap urlMap);

	public int deleteUrlMap(UrlMap urlMap);
	
	public int saveUser(User user);

	public List<User> getUser(User user);

	public int updateUser(User user);

	public int deleteUser(User user);
	
	public List<Wares> getWares(Wares wares);
	
	public int saveWares(Wares wares);
	
	public int updateWares(Wares wares);
	
	public int deleteWares(Wares wares);
	
	public Long getWaresCount(Wares wares);
	
	public boolean saveOperateLog(OperateLog operateLog);

	public List<Statistics> getMonthStatistics(Statistics statistics);
	
	public List<Statistics> getDayStatistics(Statistics statistics);

	public Long getUrlMapCount(UrlMap urlmap);

	public Account getAccountById(@Param("id") Integer id);
}
