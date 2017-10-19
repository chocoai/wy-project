package com.aibei.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibei.bean.User;
import com.aibei.mapper.UserMapper;
import com.aibei.service.UserService;

@Service(UserService.BEAN_NAME)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	public boolean addUser(User user) {
		return userMapper.addUser(user);
	}

	public List<User> getUsers(User user) {
		return userMapper.getUserById(user);
	}
	
	public boolean updateUser(User user) {
		return userMapper.updateUser(user);
	}
	
	public boolean deleteUser(User user) {
		return userMapper.deleteUser(user);
	}
}
