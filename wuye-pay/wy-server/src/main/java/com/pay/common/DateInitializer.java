package com.pay.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 注册Date转换器
 * @company 北京优贝在线网络科技有限公司
 * @ClassName DateInitializer
 * @Description TODO
 * @author xiaoQiang web:www.miniing.com email:webmaster@miniing.com
 * @date 2012-10-25
 */
public class DateInitializer implements WebBindingInitializer {

	public void initBinder(WebDataBinder wd, WebRequest wr) {
		// TODO Auto-generated method stub
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        wd.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
