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

import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.bean.Wares;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;

@Controller
@RequestMapping("/url")
public class UrlAction {

	
	@Autowired
	private DataBaseService mDataBaseService;
	/**
	 * 查询收费账号信息
	 * @throws Exception 
	 */
	@RequestMapping("getUrlMap.htm")
	@ResponseBody
    public Map<String, Object> getUrlMap(UrlMap urlmap, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String urlkey = StringUtils.obj2String(request.getParameter("urlkey"));
		String urlstring = StringUtils.obj2String(request.getParameter("urlstring"));
		String soapactionstring = StringUtils.obj2String(request.getParameter("soapactionstring"));
		String qrcodeurl = StringUtils.obj2String(request.getParameter("qrcodeurl"));
		/*UrlMap urlmap = new UrlMap();*/
		urlmap.setUrlkey(urlkey.equals("")?null:urlkey);
		urlmap.setUrlstring(urlstring.equals("")?null:urlstring);
		urlmap.setSoapactionstring(soapactionstring.equals("")?null:soapactionstring);
		urlmap.setStatus("1");
		Map<String, Object> map = new HashMap<String, Object>();
		List<UrlMap> list = mDataBaseService.getUrlMap(urlmap);
		Long totalSize =mDataBaseService.getUrlMapCount(urlmap);
		map.put("total", totalSize);
		map.put("rows", list);
		return map;
		//return 	mDataBaseService.getUrlMap(urlmap);
		
	}
	
	/**
	 * 保存新增收费账号信息
	 * @throws Exception 
	 */
	@RequestMapping("saveUrlMap.htm")
	@ResponseBody
    public boolean saveUrlMap(UrlMap urlmap, HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createtime = sdf.format(new Date());
		String updatetime = createtime;
		
		urlmap.setCreatetime(createtime);
		urlmap.setUpdatetime(updatetime);
		
		User user = (User) request.getSession().getAttribute("user");
		urlmap.setCreater(user.getUsername());
//		System.out.println(urlmap.toString());
		return mDataBaseService.saveUrlMap(urlmap);
	}
	
//	@RequestMapping("deleteUrlMap.htm")
//	@ResponseBody
//    public boolean deleteUrlMap(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
//		boolean flag = false;
//		
//		UrlMap urlmap = new UrlMap();
//		urlmap.setStatus("0");
//		
//		String ids = request.getParameter("ids");
//		if (ids.indexOf(",") > 0) {
//			String idArrs[] = ids.split(",");
//			for (int i = 0; i < idArrs.length; i++) {
//				urlmap.setId(idArrs[i]);
//				flag = updateUrlMap(urlmap, request);
//				if (!flag) {
//					break;
//				}
//			}
//		} else {
//			urlmap.setId(ids);
//			flag = updateUrlMap(urlmap, request);
//		}
//		return flag;
//	}
	
	@RequestMapping("deleteUrlMap.htm")
	@ResponseBody
    public boolean deleteUrlMap(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		boolean flag = false;
		
		UrlMap urlmap = new UrlMap();
		
		String ids = request.getParameter("ids");
		if (ids.indexOf(",") > 0) {
			String idArrs[] = ids.split(",");
			for (int i = 0; i < idArrs.length; i++) {
				urlmap.setId(StringUtils.obj2Int(idArrs[i]));
				flag = mDataBaseService.deleteUrlMap(urlmap);
				if (!flag) {
					break;
				}
			}
		} else {
			urlmap.setId(StringUtils.obj2Int(ids));
			flag = mDataBaseService.deleteUrlMap(urlmap);
		}
		return flag;
	}
	
	@RequestMapping("updateUrlMap.htm")
	@ResponseBody
	public boolean updateUrlMap(UrlMap urlmap, HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		urlmap.setUpdatetime(sdf.format(new Date()));
		
		User user = (User) request.getSession().getAttribute("user");
		urlmap.setEditor(user.getUsername());
		
		return mDataBaseService.updateUrlMap(urlmap);
	}
	
}
