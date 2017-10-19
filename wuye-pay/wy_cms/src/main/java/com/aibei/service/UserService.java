package com.aibei.service;

import java.util.List;

import com.aibei.bean.User;


public interface UserService {
   String BEAN_NAME = "userService";
   public boolean addUser(User user);
   public List<User> getUsers(User user);
   public boolean updateUser(User user);
   public boolean deleteUser(User user);
}
