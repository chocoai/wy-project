package com.aibei.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.aibei.bean.Statistics;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;



@Controller
@RequestMapping("/statistics")
public class StatisticsAction {

	
	@Autowired
    private DataBaseService mDataBaseService;
	
	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping("index.htm")
	@ResponseBody
	 public Map<String, Object> index(Statistics statistics, HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String begintime = StringUtils.obj2String(request.getParameter("begintime"));
		String endtime = StringUtils.obj2String(request.getParameter("endtime"));
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		String type = StringUtils.obj2String(request.getParameter("type")); 
		
		if(begintime!=null && begintime.equals("")){
			begintime = null;
		}
		if(endtime!=null && endtime.equals("")){
			endtime = null;
		}
		
		statistics.setPlatsystem(platsystem.equals("")?null:platsystem.split(","));
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Statistics> statisticss = null;
		
		
		if(type!=null && type.equals("day")){
			if(begintime!=null){
				//begintime = begintime.substring(0, 7);
				statistics.setBegintime(begintime);
			}else{
				statistics.setBegintime(getCurrentMonthFirstDay());
			}
			if(endtime!=null){
				//endtime = endtime.substring(0, 7);
				statistics.setEndtime(endtime);
			}else{
				statistics.setEndtime(dateFormater.format(new Date()));
			}
			statisticss = mDataBaseService.getDayStatistics(statistics);
		}else{
			if(begintime!=null){
				begintime = begintime.substring(0, 7);
				statistics.setBegintime(begintime);
			}else{
				statistics.setBegintime(getCurrentYearFirstMonth());
			}
			if(endtime!=null){
				endtime = endtime.substring(0, 7);
				statistics.setEndtime(endtime);
			}else{
				statistics.setEndtime(dateFormater.format(new Date()).substring(0, 7));
			}
			
			statisticss = mDataBaseService.getMonthStatistics(statistics);
		}
		List<Statistics> statisticss1 = new ArrayList<Statistics>();
		
		if(begintime!=null){
			if(endtime!=null){
				for(Statistics ss : statisticss){
					/*if(begintime.compareTo(ss.getDate())<=0 && endtime.compareTo(ss.getDate())>=0){
						statisticss1.add(ss);
					}*/
					statisticss1.add(ss);
				}
			}else{
				for(Statistics ss : statisticss){
					/*if(begintime.compareTo(ss.getDate())<=0){
						statisticss1.add(ss);
					}*/
					statisticss1.add(ss);
				}
			}
		}else if(endtime!=null){
			for(Statistics ss : statisticss){
				/*if(endtime.compareTo(ss.getDate())>=0){
					statisticss1.add(ss);
				}*/
				statisticss1.add(ss);
			}
		}else{
			statisticss1 = statisticss;
		}
		
		
		map.put("total", statisticss1.size());
		map.put("rows", statisticss1);
		request.getSession().setAttribute("statistics", statisticss1);
		
		return map;
	}
	
	@RequestMapping("histogram.htm")
	@ResponseBody
	public Map<String, Object> histogram(HttpServletRequest request,HttpServletResponse response){
		List<Statistics> statisticss = (List<Statistics>) request.getSession().getAttribute("statistics");
		Map<String, Object> map = new HashMap<String, Object>();
		String type = request.getParameter("type");
		
		if(statisticss!=null && statisticss.size()>0){
			map.put("result", "success");
			List<String> dates = new ArrayList<String>();
			List<Double> counts = new ArrayList<Double>();
			List<Integer> countss = new ArrayList<Integer>();
			List<Integer> numbers = new ArrayList<Integer>();
			for(Statistics ss : statisticss){
				if(type.equals("") || type.equals("value")){
					dates.add(ss.getDate()+"("+ss.getNumber()+"笔)");
					counts.add(ss.getCount());
				}else if(type.equals("num")){
					dates.add(ss.getDate());
					countss.add(ss.getNumber());
				}
			}
			map.put("dates", dates);
			if(type.equals("") || type.equals("value")){
				map.put("counts", counts);
				map.put("type", "金额(元)");
			}else if(type.equals("num")){
				map.put("counts", countss);
				map.put("type", "笔数");
			}
			/*map.put("numbers", numbers);*/
		}else{
			map.put("result", "fail");
		}
		
		return map;
	}
	
	public String getCurrentMonthFirstDay(){
		String currentDate = dateFormater.format(new Date());
		return currentDate.substring(0, 8)+"01";
	}
	
	public String getCurrentYearFirstMonth(){
		String currentDate = dateFormater.format(new Date());
		return currentDate.substring(0, 5)+"01";
	}
	
	/*public String getNextMonth(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date)
	}*/
}
