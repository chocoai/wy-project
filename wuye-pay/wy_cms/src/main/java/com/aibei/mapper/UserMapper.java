package com.aibei.mapper;

import java.util.List;

import com.aibei.bean.User;


public interface UserMapper {
  public boolean addUser(User user);
  public List<User> getUserById(User user);
  public boolean updateUser(User user);
  public boolean deleteUser(User user);
}
