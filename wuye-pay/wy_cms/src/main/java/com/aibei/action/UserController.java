package com.aibei.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.User;
import com.aibei.service.impl.UserServiceImpl;


@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
    private UserServiceImpl userService;
	
	
	@RequestMapping("addUser.htm")
	@ResponseBody
	public boolean addUser(User user,ModelMap modelMap, HttpServletRequest request,HttpServletResponse response){
		return userService.addUser(user);
	}
	
	@RequestMapping("toGetUsers.htm")
	public String toGetUsers(){
		return "showUsers.html";
	}
	
	@RequestMapping("getUsers.htm")
	@ResponseBody
	public List<User> getUsers(User user){
		List<User> users = userService.getUsers(user);
		/*for(Iterator<User> i = users.iterator();i.hasNext();){
			User user2 = i.next();
			System.out.println("id:"+user2.getId()+",name:"+user2.getName()+",password:"+user2.getPassword());
		}*/
		return users;
	}
	
	/**
	 * 跳转到用户信息编辑页
	* @Description TODO
	 */
	@RequestMapping("toEditUser.htm")
	public String toEditUser(){
		return "editUser.html";
	}
	
	
	@RequestMapping("updateUser.htm")
	@ResponseBody
	public String updateUser(User user){
		return userService.updateUser(user)==true ? "更新成功":"更新失败";
	}
	
	@RequestMapping("deleteUser.htm")
	@ResponseBody
	public boolean deleteUser(User user,HttpServletRequest request) throws Exception{
		boolean flag = false;
		String ids = request.getParameter("ids");
		if(ids.indexOf(",") > 0){
			String idArrs[] = ids.split(",");
			for(int i = 0;i < idArrs.length; i++){
//				System.out.println(idArrs[i]);
				user.setId(Integer.parseInt(idArrs[i]));
				flag = userService.deleteUser(user);
				if(!flag) break;
			}
		}else{
//			System.out.println(ids);
			user.setId(Integer.parseInt(ids));
			flag = userService.deleteUser(user);
		}
//		return userService.deleteUser(user);
		return flag;
	}
	
	//获取角色
	@RequestMapping("getUserRole.htm")
	@ResponseBody
	public String getUserRole(User user,HttpServletRequest request) throws Exception{
		String userID = (String) request.getSession().getAttribute("userID");
		userID = "1";//假数据
		String roleId = "1000";//userService.getRoleByUid(Integer.parseInt(userID)).get("roleId").toString();
		return roleId;
	}
	
	//登录验证
	@RequestMapping("checkLogin.htm")
	@ResponseBody
	public String checkLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user.setUsername(username);
		
		User u = new User();
		String flag = "error";
		if(userService.getUsers(user).size() < 1){//无用户
			flag = "noUser";
		}else{
			u = userService.getUsers(user).get(0);
			if(password != null){
				if(!password.equals(u.getPassword())){//密码错误
					flag = "pwdError";
				}else{
					flag = "success";
					request.getSession().setAttribute("user", u);
				}
			}
		}
		
		return flag;
	}
	
	//是否登录
	@RequestMapping("ifLogin.htm")
	@ResponseBody
	public String ifLogin(User user,HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(request.getSession().getAttribute("loginEmail") != null){
			return "logined";
		}else{
			return "failed";
		}
	}
	
	
	@RequestMapping("getUserSession.htm")
	@ResponseBody
	public Object getUserSession(HttpServletRequest request) throws Exception{
		if(request.getSession().getAttribute("user") != null){
			return request.getSession().getAttribute("user");
		}else{
			return "";
		}
	}
	
	@RequestMapping("sessionOut.htm")
	@ResponseBody
	public String sessionOut(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		String user = session.getAttribute("user").toString();
		session.removeAttribute("user");
		return user;
	}
}
