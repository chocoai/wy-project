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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.aibei.bean.Ad;
import com.aibei.bean.User;
import com.aibei.common.StringUtils;
import com.aibei.common.UploadUtils;
import com.aibei.service.DataBaseService;



@Controller
@RequestMapping("/ad")
public class AdAction {

	
	@Autowired
    private DataBaseService mDataBaseService;
	
	@RequestMapping("getAd.htm")
	@ResponseBody
	 public Map<String, Object> getAd(Ad ad,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String WYID = StringUtils.obj2String(request.getParameter("wyid"));
		String status = StringUtils.obj2String(request.getParameter("status"));
		String source = StringUtils.obj2String(request.getParameter("source"));
		ad.setWyid(WYID.equals("")?null:WYID);
		ad.setStatus(status.equals("")?null:status);
		ad.setSource(source.equals("")?null:source);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Ad> ads = mDataBaseService.getAd(ad);
		Long totalSize = mDataBaseService.getAdCount(ad);
		
		map.put("total", totalSize);
		map.put("rows", ads);
		
		return map;
	}
	
	@RequestMapping("saveAd.htm")
	@ResponseBody
    public boolean saveAd(Ad ad, HttpServletRequest request, HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createtime = sdf.format(new Date());
		ad.setCreatetime(createtime);
		User user = (User) request.getSession().getAttribute("user");
		ad.setCreater(user.getUsername());
		return mDataBaseService.saveAd(ad);
	}
	
	@RequestMapping("updateAd.htm")
	@ResponseBody
    public boolean updateAd(Ad ad, HttpServletRequest request) throws Exception{ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ad.setUpdatetime(sdf.format(new Date().getTime()));
		User user = (User) request.getSession().getAttribute("user");
		ad.setEditor(user.getUsername());
		return mDataBaseService.updateAd(ad);
	}
	
//	@RequestMapping("deleteAd.htm")
//	@ResponseBody
//    public boolean deleteAd(Ad ad, HttpServletRequest request, HttpServletResponse response) throws Exception{ 
//		System.out.println("delete...................");
//		boolean flag = false;
//		ad.setStatus("0");
//		String ids = request.getParameter("ids");
//		if(ids.indexOf(",") > 0){
//			String idArrs[] = ids.split(",");
//			for(int i = 0;i < idArrs.length; i++){
//				ad.setId(idArrs[i]);
//				flag = updateAd(ad, request);
//				if(!flag) break;
//			}
//		}else{
//			ad.setId(ids);
//			flag = updateAd(ad, request);
//		}
//		
//		return flag;
//	}
	
	@RequestMapping("deleteAd.htm")
	@ResponseBody
    public boolean deleteAd(Ad ad, HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		boolean flag = false;
		String ids = request.getParameter("ids");
		if(ids.indexOf(",") > 0){
			String idArrs[] = ids.split(",");
			for(int i = 0;i < idArrs.length; i++){
				ad.setId(StringUtils.obj2Int(idArrs[i]));
				flag = mDataBaseService.deleteAd(ad);
				if(!flag) break;
			}
		}else{
			ad.setId(StringUtils.obj2Int(ids));
			flag = mDataBaseService.deleteAd(ad);
		}
		
		return flag;
	}
	
	@RequestMapping("toEditAd.htm")
	public String toEditAd(){
		return "editAd.html";
	}
	
	@RequestMapping("uploadPic.htm")
	@ResponseBody
	public String uploadGiftLargePic(@RequestParam("imgSrc") CommonsMultipartFile mFile,HttpServletRequest request)throws Exception { 
		return UploadUtils.upload(request, mFile, "/ads/images/");
	}
	
}
