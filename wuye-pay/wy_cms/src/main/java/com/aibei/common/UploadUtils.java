package com.aibei.common;

import java.io.File;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class UploadUtils{
	//
//	private final static String BASE_DIR_PATH = PropertieFactory.getProperty("uploadPath");//"home/wwwroot/user_data/image/";
	private final static String BASE_DIR_PATH = "upload";
	/**
	 * 上传文件工具类
	* @Title: upload 
	* @param  request	用来获取绝对路径
	* @param  mFile	上传控件的属性name的值
	* @param  dirs	上传目录
	* @param 		相对路径
	 */
	public static String upload(HttpServletRequest request,CommonsMultipartFile mFile,String dirs){ // 请求参数一定要与form中的参数名对应
		return UploadUtils.upload(request, mFile, dirs, null);
	}
	/**
	 * 上传文件工具类
	* @Title: upload 
	* @param  request	用来获取绝对路径
	* @param  mFile	上传控件的属性name的值
	* @param  dirs	上传目录
	* @param maxSize 最大限制  单位字节
	* @param @return		相对路径
	 */
	public static String upload(HttpServletRequest request,CommonsMultipartFile mFile,String dirs,Long maxSize){ // 请求参数一定要与form中的参数名对应
		
		UploadResult result = new UploadResult();
		
		if(maxSize != null){
			if(mFile.getSize() > maxSize){
				result.setStatus(1);
				result.setValue("文件超出大小");
				return JSONObject.fromObject(result).toString();
			}
		}
		
		if(dirs != null && dirs.trim() != "")
		{
			int beginIndex = 0;
			int endIndex = dirs.length();
			if(dirs.charAt(0) == '/')
			{
				beginIndex = 1;
			}
			
			if(dirs.charAt(dirs.length()-1) == '/')
			{
				endIndex = dirs.length()-1;
			}
			
			dirs = dirs.substring(beginIndex, endIndex);
		}
		//String result = "";
		if (!mFile.isEmpty()) {
			String realPath = request.getSession().getServletContext().getRealPath("/"); // 获取本地存储路径
		    //1.获取文件的完整路径
			//String path = mFile.getOriginalFilename();
			//2.获取文件的总大小
			//Long fileSize = mFile.getSize();
			//3.获取文件名
			String fileName = mFile.getOriginalFilename();//path.substring(path.lastIndexOf("\\")+1);
			//4.获取文件扩展名 ps：没有扩展名 将得到全名
			String extName = fileName.substring(fileName.lastIndexOf('.')+1);
			//5.根据系统时间 生成保存到服务器的文件名
			String prefix = String.valueOf(Calendar.getInstance().getTimeInMillis());
			//6.新文件名称
			String newFileName = prefix +'.'+ extName;
			//7.拼接目录路径
			String dirPath = BASE_DIR_PATH +'/'+ dirs;
			//7.拼接最终目录结构
			String finalRealPath = realPath + "../"+ dirPath;
			File dir = new File(finalRealPath);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			//8.保存文件
			File file = new File(dir,newFileName); // 新建一个文件
			
			try {
				mFile.getFileItem().write(file); // 将上传的文件写入新建的文件中
				result.setStatus(0);//result = "{\"status\":0,\"value\":"+newFileName+"}";
				String filePath = dirs + "/" + newFileName; 
				result.setValue(filePath);
			} catch (Exception e) {
				//result = "{\"status\":1,\"value\":"+e.getMessage()+"}";
				result.setStatus(1);
				result.setValue(e.toString());
				e.printStackTrace();
			}
		}
		return JSONObject.fromObject(result).toString();
	}

}

