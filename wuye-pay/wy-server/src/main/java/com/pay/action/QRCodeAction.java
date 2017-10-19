package com.pay.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pay.common.FileUtils;
import com.pay.common.utils.QRCodeUtil;
import com.pay.common.StringUtils;

@Controller
@RequestMapping("/QRCode")
public class QRCodeAction {
	
	private String url="http://www.oklong.com:8080/wy-client/";
//	private String url="http://192.168.1.2/wy-client/";
	
	
	/**
	 * 生成二维码图片
	 * @param 
	 */
	@RequestMapping("/getQRCode.htm")
	@ResponseBody
	public String getQRCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String fileName = "";
		String content = url+request.getParameter("url");
		String destPath = request.getSession().getServletContext().getRealPath("");
		destPath = FileUtils.getParent(destPath)+"/wy-client/images/";
		fileName = QRCodeUtil.encode(content, destPath);
		
		return fileName;
	}
	
	/**
	 * 清理二维码图片
	 */
	@RequestMapping("/clrQRCode.htm")
	@ResponseBody
	public boolean clrQRCode(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		boolean result = false;
		String QRCodeImg = StringUtils.obj2String(request.getParameter("QRCodeImg"));
		if(!"".equals(QRCodeImg)){
			String path =  request.getSession().getServletContext().getRealPath("");
			path = FileUtils.getParent(path)+"/wy-client/images/"+QRCodeImg;
			result = FileUtils.deleteFile(path);
		}
		return result;
	}
	
}