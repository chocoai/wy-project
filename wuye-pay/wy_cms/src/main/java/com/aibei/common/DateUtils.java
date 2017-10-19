package com.aibei.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date String2Date(String strdate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		if(strdate!=null && !strdate.equals("")){
			try {
				date = sdf.parse(strdate);
				return date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String StringDateToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = sdf.format(new Date());
		return result ;
	}
	
	/**
	 * 
	 * @param i 几天前
	 * @return i天前的时间
	 */
	public static String BeforeToday(Integer i){
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, -i);
		Date date =cal.getTime();
		String result = sdf.format(date);
		return result;
	}
}
