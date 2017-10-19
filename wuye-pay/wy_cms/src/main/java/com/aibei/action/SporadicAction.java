package com.aibei.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.Statistics;
import com.aibei.bean.UrlMap;
import com.aibei.service.DataBaseService;
import com.aibei.thridwy.haolong.bean.GetLXXM;
import com.aibei.thridwy.haolong.utils.HaolongService;



@Controller
@RequestMapping("/sporadic")
public class SporadicAction {

	
	@Autowired
    private DataBaseService mDataBaseService;
	
	@Autowired
	private HaolongService haolongService;
	
	/*@RequestMapping("index.htm")
	@ResponseBody
	public String index(HttpServletRequest request,HttpServletResponse response){
		String source = request.getParameter("source");
		String wyid = request.getParameter("wyid");
		
		request.getSession().setAttribute("source", source);
		request.getSession().setAttribute("wyid", wyid);
		
		return "success";
	}*/
	
	@RequestMapping("getLXXM.htm")
	@ResponseBody
	public  List<Map<String, String>> getLXXM(String source, HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<Map<String, String>> lxxmList = new ArrayList<Map<String, String>>();
		if(source!=null && !source.equals("")){
			UrlMap urlMap = new UrlMap();
			urlMap.setUrlkey(source);
			urlMap = mDataBaseService.getUrlMap(urlMap).size()>0?mDataBaseService.getUrlMap(urlMap).get(0):null;
			if(urlMap != null){
				GetLXXM getLXXM = new GetLXXM();
				getLXXM.setSource(source);
				getLXXM.setSecretkey(urlMap.getSecretkey());
				getLXXM.setSoapActionString(urlMap.getSoapactionstring());
				getLXXM.setUrlString(urlMap.getUrlstring());
				lxxmList = haolongService.getLXXM(getLXXM);
			}
		}
	
		return lxxmList;
	}

	
}
