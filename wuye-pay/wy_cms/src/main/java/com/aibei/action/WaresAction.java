package com.aibei.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.Order;
import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.bean.Wares;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;

@Controller
@RequestMapping("/wares")
public class WaresAction {
	@Autowired
	private DataBaseService mDataBaseService;
	
	
	@RequestMapping("getWares.htm")
	@ResponseBody
    public List<Wares> getWares(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String waresid = StringUtils.obj2String(request.getParameter("waresid"));
		String waresname = StringUtils.obj2String(request.getParameter("waresname"));
		String requesturl = StringUtils.obj2String(request.getParameter("requesturl"));
		String notifyurl = StringUtils.obj2String(request.getParameter("notifyurl"));
		Wares wares = new Wares();
		wares.setWaresid(waresid);
		wares.setWaresname(waresname.equals("")?null:waresname);
		wares.setRequesturl(requesturl.equals("")?null:requesturl);
		wares.setNotifyurl(notifyurl.equals("")?null:notifyurl);
//		System.out.println(wares.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wares> list = mDataBaseService.getWares(wares);
		Long totalSize =mDataBaseService.getWaresCount(wares);
		map.put("total", totalSize);
		map.put("rows", list);
		return list;
	}
	
	@RequestMapping("saveWares.htm")
	@ResponseBody
	public boolean saveWares(Wares wares,HttpServletRequest request, HttpServletResponse response) throws Exception{
//		System.out.println("save------------>"+wares.toString());
		return mDataBaseService.saveWares(wares);
	}
	
	@RequestMapping("deleteWares.htm")
	@ResponseBody
    public boolean deleteWares(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		boolean flag = false;
		
		Wares wares = new Wares();
		
		String ids = request.getParameter("ids");
//		System.out.println(ids);
		if (ids.indexOf(",") > 0) {
			String idArrs[] = ids.split(",");
			for (int i = 0; i < idArrs.length; i++) {
				wares.setId(StringUtils.obj2Int(idArrs[i]));
//				System.out.println(wares.toString());
				flag = mDataBaseService.deleteWares(wares);
				if (!flag) {
					break;
				}
			}
		} else {
			wares.setId(StringUtils.obj2Int(ids));
			flag = mDataBaseService.deleteWares(wares);
		}
		return flag;
	}
	
	@RequestMapping("updateWares.htm")
	@ResponseBody
	public boolean updateWares(Wares wares, HttpServletRequest request) throws Exception {
//		System.out.println("update-------->"+wares.toString());
		return mDataBaseService.updateWares(wares);
	}
}
