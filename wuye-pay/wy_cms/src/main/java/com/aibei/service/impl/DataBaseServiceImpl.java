package com.aibei.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibei.bean.Account;
import com.aibei.bean.Ad;
import com.aibei.bean.OperateLog;
import com.aibei.bean.Order;
import com.aibei.bean.Statistics;
import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.bean.Wares;
import com.aibei.mapper.DataBaseMapper;
import com.aibei.service.DataBaseService;

@Service(DataBaseService.BEAN_NAME)
public class DataBaseServiceImpl implements DataBaseService {

	@Autowired
	private DataBaseMapper mapper;

	@Override
	public List<Account> getAccount(Account account) throws Exception {
		return mapper.getAccount(account);
	}

	@Override
	public boolean saveAccount(Account account) throws Exception {
		return mapper.saveAccount(account) > 0 ? true : false;
	}

	@Override
	public boolean updateAccount(Account account) throws Exception {
		return mapper.updateAccount(account) > 0 ? true : false;
	}

	@Override
	public List<Ad> getAd(Ad ad) throws Exception {
		return mapper.getAd(ad);
	}

	@Override
	public boolean saveAd(Ad ad) throws Exception {
		return mapper.saveAd(ad) > 0 ? true : false;
	}

	@Override
	public boolean updateAd(Ad ad) throws Exception {
		return mapper.updateAd(ad) > 0 ? true : false;
	}

	@Override
	public List<Order> getOrder(Order order) throws IOException {
		return mapper.getOrder(order);
	}

	@Override
	public List<UrlMap> getUrlMap(UrlMap urlMap) throws Exception {
		return mapper.getUrlMap(urlMap);
	}

	@Override
	public boolean saveUrlMap(UrlMap urlMap) throws Exception {
		return mapper.saveUrlMap(urlMap) > 0 ? true : false;
	}

	@Override
	public boolean updateUrlMap(UrlMap urlMap) throws Exception {
		return mapper.updateUrlMap(urlMap) > 0 ? true : false;
	}

	@Override
	public boolean saveUser(User user) {
		return mapper.saveUser(user) > 0 ? true : false;
	}

	@Override
	public List<User> getUsers(User user) {
		return mapper.getUser(user);
	}

	@Override
	public boolean updateUser(User user) {
		return mapper.updateUser(user) > 0 ? true : false;
	}

	@Override
	public boolean deleteUser(User user) {
		return mapper.deleteUser(user) > 0 ? true : false;
	}

	@Override
	public Long getOrderCount(Order order) throws IOException {
		return mapper.getOrderCount(order);
	}

	@Override
	public Long getAdCount(Ad ad) throws Exception {
		return mapper.getAdCount(ad);
	}

	@Override
	public Long getAccountCount(Account account) throws Exception {
		return mapper.getAccountCount(account);
	}

	@Override
	public boolean deleteUrlMap(UrlMap urlMap) throws Exception {
		return mapper.deleteUrlMap(urlMap) > 0 ? true : false;
	}

	@Override
	public boolean deleteAccount(Account account) throws Exception {
		return mapper.deleteAccount(account) > 0 ? true : false;
	}

	@Override
	public boolean deleteAd(Ad ad) throws Exception {
		return mapper.deleteAd(ad) > 0 ? true : false;
	}

	@Override
	public List<Order> getErrorOrder() throws IOException {
		return mapper.getErrorOrder();
	}

	@Override
	public Long getErrorOrderCount() throws IOException {
		return mapper.getErrorOrderCount();
	}

	@Override
	public boolean updateOrder(Order order) throws IOException {
		return mapper.updateOrder(order) > 0 ? true:false;
	}

	@Override
	public boolean deleteOrder(Order order) throws IOException {
		return mapper.deleteOrder(order) > 0 ? true : false;
	}

	@Override
	public Float getOrderCountPrice(Order order) throws IOException {
		return mapper.getOrderCountPrice(order);
	}

	@Override
	public boolean quicklyRemove(String time) throws Exception {
		return mapper.quicklyRemove(time)>0?true:false;
	}

	@Override
	public boolean saveWares(Wares wares) {
		return mapper.saveWares(wares)>0?true:false;
	}

	@Override
	public List<Wares> getWares(Wares wares) {
		return mapper.getWares(wares);
	}

	@Override
	public boolean updateWares(Wares wares) {
		return mapper.updateWares(wares)>0?true:false;
	}

	@Override
	public boolean deleteWares(Wares wares) {
		return mapper.deleteWares(wares)>0?true:false;
	}

	@Override
	public Long getWaresCount(Wares wares) {
		return mapper.getWaresCount(wares);
	}

	@Override
	public List<Order> getOrderNoPage(Order order) throws IOException {
		return mapper.getOrderNoPage(order);
	}

	@Override
	public boolean saveOperateLog(OperateLog operateLog) {
		return mapper.saveOperateLog(operateLog);
	}
	
	@Override
	public List<Statistics> getMonthStatistics(Statistics statistics){
		return mapper.getMonthStatistics(statistics);
	}
	
	@Override
	public List<Statistics> getDayStatistics(Statistics statistics){
		return mapper.getDayStatistics(statistics);
	}

	@Override
	public Long getUrlMapCount(UrlMap urlmap) {
		return mapper.getUrlMapCount(urlmap);
	}

	@Override
	public Account getAccountById(Integer id) {
		return mapper.getAccountById(id);
	}
	
}
