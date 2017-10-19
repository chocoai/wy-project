/**
 * 
 * 系统异常处理类，专门用于处理control抛出的异常信息
 * 把异常日志写入数据保存
 * 
 * 覆盖SimpleMappingExceptionResolver的getModelAndView方法
 * 
 * 
 */
package com.aibei.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @author 
 *
 */
public class ExceptionHandler extends SimpleMappingExceptionResolver {
	
	/**
	 * 记录错误日志信息
	 */
	private static final Logger _log = Logger.getLogger(ExceptionHandler.class);
	
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex , HttpServletRequest request) {

		String errorMesage = ex.getLocalizedMessage();

		// 把整个系统错误日志都打印出来
		_log.error("系统发生错误，日志如下：",ex);

		request.setAttribute("ex", ex.toString());
		
		ModelAndView mv = new ModelAndView(viewName);// 自定义数据返回
		mv.addObject(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, ex);
		/*
		mv.addObject("errorInfo", info);
		*/
		return mv;
	}
}
