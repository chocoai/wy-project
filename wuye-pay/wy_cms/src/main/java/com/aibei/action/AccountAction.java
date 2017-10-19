package com.aibei.action;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.Account;
import com.aibei.bean.User;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;

@Controller
@RequestMapping("/account")
public class AccountAction {

	
	@Autowired
	private DataBaseService mDataBaseService;
	/**
	 * 查询收费账号信息
	 * @throws Exception 
	 */
	@RequestMapping("getAccount.htm")
	@ResponseBody
    public Map<String, Object> getAccount(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String appid = StringUtils.obj2String(request.getParameter("appid"));
		//String wyid = StringUtils.obj2String(request.getParameter("wyid"));
		String property = StringUtils.obj2String(request.getParameter("property"));
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		Boolean offline = StringUtils.obj2String(request.getParameter("offline")).equals("")?null:StringUtils.obj2String(request.getParameter("offline")).equals("0")?false:true;
		String source = StringUtils.obj2String(request.getParameter("source"));
		String status = StringUtils.obj2String(request.getParameter("status"));
		account.setAppid(appid.equals("")?null:appid);
		//account.setWyid(wyid.equals("")?null:wyid);
		account.setProperty(property.equals("")?null:property);
		account.setSource(source.equals("")?null:source);
		account.setStatus(status.equals("")?null:status);
		account.setPlatsystem(platsystem.equals("")?null:platsystem);
		account.setOffline(offline==null?null:offline);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Account> list = mDataBaseService.getAccount(account);
		Long totalSize = mDataBaseService.getAccountCount(account);
		
		map.put("total", totalSize);
		map.put("rows", list);
		
		return map;
	}
	
	/**
	 * 保存新增收费账号信息
	 * @throws Exception 
	 */
	@RequestMapping("saveAccount.htm")
	@ResponseBody
    public boolean saveAccount(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createtime = sdf.format(new Date());
		String updatetime = createtime;
		
		account.setCreatetime(createtime);
		account.setUpdatetime(updatetime);
		
		User user = (User) request.getSession().getAttribute("user");
		account.setCreater(user.getUsername());
		
		return mDataBaseService.saveAccount(account);
	}
	
//	@RequestMapping("deleteAccount.htm")
//	@ResponseBody
//    public boolean deleteAccount(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception{ 
//		boolean flag = false;
//		
//		account.setStatus("0");
//		
//		String ids = request.getParameter("ids");
//		if (ids.indexOf(",") > 0) {
//			String idArrs[] = ids.split(",");
//			for (int i = 0; i < idArrs.length; i++) {
//				account.setId(idArrs[i]);
//				flag = updateAccount(account, request);
//				if (!flag) {
//					break;
//				}
//			}
//		} else {
//			account.setId(ids);
//			flag = updateAccount(account, request);
//		}
//		return flag;
//	}
	
	@RequestMapping("deleteAccount.htm")
	@ResponseBody
    public boolean deleteAccount(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		boolean flag = false;
		
		
		String ids = request.getParameter("ids");
		if (ids.indexOf(",") > 0) {
			String idArrs[] = ids.split(",");
			for (int i = 0; i < idArrs.length; i++) {
				account.setId(StringUtils.obj2Int(idArrs[i]));
				flag = mDataBaseService.deleteAccount(account);
				if (!flag) {
					break;
				}
			}
		} else {
			account.setId(StringUtils.obj2Int(ids));
			flag = mDataBaseService.deleteAccount(account);
		}
		return flag;
	}
	
	@RequestMapping("updateAccount.htm")
	@ResponseBody
	public boolean updateAccount(Account account, HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		account.setUpdatetime(sdf.format(new Date()));
		
		User user = (User) request.getSession().getAttribute("user");
		account.setEditor(user.getUsername());
		
		return mDataBaseService.updateAccount(account);
	}
	
	/**
	 * 保存新增收费账号信息
	 * @throws Exception 
	 */
	@RequestMapping("copyAccount.htm")
	@ResponseBody
    public boolean copyAccount(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception{	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createtime = sdf.format(new Date());
		String updatetime = createtime;
		
		account.setCreatetime(createtime);
		account.setUpdatetime(updatetime);
		
		User user = (User) request.getSession().getAttribute("user");
		account.setCreater(user.getUsername());
		
		return mDataBaseService.saveAccount(account);
	}
	
	/**
	 * 修改sumflag
	 * @throws Exception 
	 */
	@RequestMapping("changeSumflag.htm")
	@ResponseBody
	public boolean changeSumflag(Account account) throws Exception{
		if(account.getId()!=null && account.getId()!=0){
			account = mDataBaseService.getAccountById(account.getId());
			if(account.getSumflag() == 0){
				account.setSumflag(1);
			}else{
				account.setSumflag(0);
			}
			mDataBaseService.updateAccount(account);
			return true;
		}
		return false;
	}
}
