package com.aibei.action;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.Ad;
import com.aibei.bean.Order;
import com.aibei.bean.UrlMap;
import com.aibei.bean.User;
import com.aibei.common.DateUtils;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;
import com.aibei.thridwy.haolong.bean.GetContract;
import com.aibei.thridwy.haolong.bean.PutWeiFee;
import com.aibei.thridwy.haolong.bean.PutWeiFee1;
import com.aibei.thridwy.haolong.utils.HaolongService;
import com.aibei.common.FileUtils;

@Controller
@RequestMapping("/test")
public class Test {
	private List<Order> list = null;
	private Long totalSize = null;
	@Autowired
    private DataBaseService mDataBaseService;
	
	@Autowired
	private HaolongService haolongService;
	
	@RequestMapping("download.htm")
	@ResponseBody
	public void download(HttpServletRequest request,HttpServletResponse response){
		  	String path = request.getSession().getServletContext().getRealPath("");
		  	//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
	        response.setContentType("multipart/form-data");  
	        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
	        response.setHeader("Content-Disposition", "attachment;fileName="+"123.xls");  
	        ServletOutputStream out;  
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        //System.out.println(path+"/123.txt");
	        
	        File file = new File(path+"/tmp/8b59fd60-038a-4c36-bfee-32a0504cb0bb1484880343470.xls");  
	  
	        try {  
	            FileInputStream inputStream = new FileInputStream(file);  
	  
	            //3.通过response获取ServletOutputStream对象(out)  
	            out = response.getOutputStream();  
	            
	            int b = 0;
	            byte[] buffer = new byte[512];  
	            while (b != -1){  
	                b = inputStream.read();  
	                //4.写到输出流(out)中  
	                out.write(b);  
	            }  
	            inputStream.close();  
	            out.flush();  
	            out.close();  
	  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}
}
