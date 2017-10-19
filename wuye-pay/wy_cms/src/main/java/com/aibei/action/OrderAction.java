package com.aibei.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aibei.bean.OperateLog;
import com.aibei.bean.Order;
import com.aibei.bean.UrlMap;
import com.aibei.common.DateUtils;
import com.aibei.common.FileUtils;
import com.aibei.common.StringUtils;
import com.aibei.service.DataBaseService;
import com.aibei.thridwy.haolong.bean.GetContract;
import com.aibei.thridwy.haolong.bean.GetSKID;
import com.aibei.thridwy.haolong.bean.PutWeiFee;
import com.aibei.thridwy.haolong.bean.PutWeiFee1;
import com.aibei.thridwy.haolong.bean.PutWeiFee2;
import com.aibei.thridwy.haolong.utils.HaolongService;

@Controller
@RequestMapping("/order")
public class OrderAction {
	private List<Order> getOrderNoPage = null;
	@Autowired
    private DataBaseService mDataBaseService;
	
	@Autowired
	private HaolongService haolongService;
	/**
	 * 查询订单
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("getOrder.htm")
	@ResponseBody
    public Map<String, Object> getOrder(Order order, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
//		String appid = request.getParameter("appid");
//		String waresid = request.getParameter("waresid");
//		String appuserid = request.getParameter("appuserid");
//		String transid = request.getParameter("transid");
//		String HTID = request.getParameter("HTID");
//		String SKID = request.getParameter("SKID");
		String cporderid = StringUtils.obj2String(request.getParameter("cporderid"));
//		String result = StringUtils.obj2String(request.getParameter("result"));
//		String paytype = StringUtils.obj2String(request.getParameter("paytype"));
//		String transtype = StringUtils.obj2String(request.getParameter("transtype"));
		String chargestatus = StringUtils.obj2String(request.getParameter("chargestatus"));
		String orderstatus = StringUtils.obj2String(request.getParameter("orderstatus"));
		String begintime = StringUtils.obj2String(request.getParameter("begintime"));
		String endtime = StringUtils.obj2String(request.getParameter("endtime"));
		String source = StringUtils.obj2String(request.getParameter("source"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		//String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String property = StringUtils.obj2String(request.getParameter("property"));
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		Boolean offline = StringUtils.obj2String(request.getParameter("offline")).equals("")?null:StringUtils.obj2String(request.getParameter("offline")).equals("0")?false:true;
		String paytype = StringUtils.obj2String(request.getParameter("paytype"));
		String from = StringUtils.obj2String(request.getParameter("from"));
//		order.setResult(result.equals("")?null:result);
//		order.setPaytype(paytype.equals("")?null:paytype);
//		order.setTranstype(transtype.equals("")?null:transtype);
		order.setOrderstatus(orderstatus.equals("")?null:orderstatus);
		order.setChargestatus(chargestatus.equals("")?null:chargestatus);
		order.setSource(source.equals("")?null:source);
		order.setJFYF(JFYF.equals("")?null:JFYF);
		//order.setWYID(WYID.equals("")?null:WYID);
		if(isNumeric(property)){
			order.setWYID(property);
			order.setProperty(null);
		}else{
			order.setProperty(property.equals("")?null:property);
		}		
		order.setPlatsystem(platsystem.equals("")?null:platsystem);
		order.setOffline(offline==null?null:offline);
		order.setPaytype(paytype.equals("")?null:paytype);
		if(begintime == "" && endtime == ""){
			order.setBegintime(DateUtils.StringDateToday());
			order.setEndtime(null);
		}else if(begintime != "" && endtime == ""){
			order.setBegintime(begintime);
			order.setEndtime(null);
		}else if(begintime == "" && endtime != ""){
			order.setBegintime(null);
			order.setEndtime(endtime);
		}else {
			order.setBegintime(begintime);
			order.setEndtime(endtime);
		}
		order.setCporderid(cporderid.equals("")?null:cporderid);
		order.setFrom(from.equals("")?null:from);
		
		Map<String, Object> map = new HashMap<String, Object>();
//		List<Order> list = mDataBaseService.getOrder(order);
//		Long totalSize =mDataBaseService.getOrderCount(order);
		List<Order> list = mDataBaseService.getOrder(order);
		Long totalSize = mDataBaseService.getOrderCount(order);
		
		//System.out.println(list.toString());
		//System.out.println(totalSize);
		map.put("total", totalSize);
		map.put("rows", list);
		
		return map;
	}
	
	@RequestMapping("writeoff.htm")
	@ResponseBody
    public String writeoff(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String flag = "";
		
		String ids = request.getParameter("ids");
		if (ids.indexOf(",") > 0) {
			String idArrs[] = ids.split(",");
			for (int i = 0; i < idArrs.length; i++) {
				Order order = new Order();
				order.setId(StringUtils.obj2Int(idArrs[i]));
				order = mDataBaseService.getOrder(order).get(0);
				if(order.getWaresid().equals("1")){
					flag = toPutWeiFee(order);
				}else if(order.getWaresid().equals("2")){
					flag = toPutWeiFee(order);
				}else if(order.getWaresid().equals("3")){
					flag = toPutWeiFee(order);
				}
				if (!flag.contains("销账成功")) {
					break;
				}
			}
		} else {
			Order order = new Order();
			order.setId(StringUtils.obj2Int(ids));
			order = mDataBaseService.getOrder(order).get(0);
			if(order.getWaresid().equals("1")){
				flag = toPutWeiFee(order);
			}else if(order.getWaresid().equals("2")){
				flag = toPutWeiFee(order);
			}else if(order.getWaresid().equals("3")){
				flag = toPutWeiFee(order);
			}
		}
		
		return flag;
	}
	
	@RequestMapping("deleteOrder.htm")
	@ResponseBody
    public String deleteOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Order order = new Order();
		String result = "";
		String ids = request.getParameter("ids");
		if (ids.indexOf(",") > 0) {
			String idArrs[] = ids.split(",");
			for (int i = 0; i < idArrs.length; i++) {
				order.setId(StringUtils.obj2Int(idArrs[i]));
				result = checkOrder(order);
				
				if(!result.equals("删除成功")){
					return result;
				}
			}
		} else {
			order.setId(StringUtils.obj2Int(ids));
			result = checkOrder(order);
		}
		return result;
	}
	
	
	@RequestMapping("getCount.htm")
	@ResponseBody
	public String getCount(HttpServletRequest request,HttpServletRequest response) throws Exception{
		String source = StringUtils.obj2String(request.getParameter("source"));
		//String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String property = StringUtils.obj2String(request.getParameter("property"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		Boolean offline = StringUtils.obj2String(request.getParameter("offline")).equals("")?null:StringUtils.obj2String(request.getParameter("offline")).equals("0")?false:true;
		String chargestatus = StringUtils.obj2String(request.getParameter("chargestatus"));
		String orderstatus = StringUtils.obj2String(request.getParameter("orderstatus"));
		String paytype = StringUtils.obj2String(request.getParameter("paytype"));
		String begintime = StringUtils.obj2String(request.getParameter("begintime"));
		String endtime = StringUtils.obj2String(request.getParameter("endtime"));
		String from = StringUtils.obj2String(request.getParameter("from"));
		String cporderid = StringUtils.obj2String(request.getParameter("cporderid"));
	
		Order order = new Order();
		Float price = null;
		long total=0;
		order.setSource(source.equals("")?null:source);
		//order.setWYID(WYID.equals("")?null:WYID);
		/*order.setProperty(property.equals("")?null:property);*/
		if(isNumeric(property)){
			order.setWYID(property);
			order.setProperty(null);
		}else{
			order.setProperty(property.equals("")?null:property);
		}	
		order.setJFYF(JFYF.equals("")?null:JFYF);
		order.setPlatsystem(platsystem.equals("")?null:platsystem);
		order.setOffline(offline==null?null:offline);  
		order.setChargestatus(chargestatus.equals("")?null:chargestatus);
		//金额查询满足条件:订单状态为成功和金额大于0
		order.setOrderstatus(orderstatus.equals("")?"0":orderstatus);
		order.setPaytype(paytype.equals("")?null:paytype);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if(begintime.equals("")){
			if(endtime.equals("")){
				order.setBegintime(formatter.format(new Date()));
			}else{
				order.setBegintime(null);
			}
		}else{
			order.setBegintime(begintime);
		}
		//order.setBegintime(begintime.equals("")?formatter.format(new Date()):begintime);
		
		order.setEndtime(endtime.equals("")?null:endtime);
		order.setFrom((from==null||from.equals(""))?null:from);
		order.setCporderid(cporderid.equals("")?null:cporderid);

		price=mDataBaseService.getOrderCountPrice(order);
		total=mDataBaseService.getOrderCount(order);

		JSONObject json = new JSONObject();
		json.put("total", total);
		json.put("price", price);
		return json.toString();
	}
	
	/**
	 * 20161213
	 * 删除：固定条件 not（交易额>0 and 交易结果 已交易）
	 * @param order
	 * @return
	 * @throws Exception
	 */
	private String checkOrder(Order order) throws Exception{
		boolean flag = false;
		order = mDataBaseService.getOrder(order).get(0);
		String content = " 计费月份："+order.getJFYF()+" 物业ID："+order.getWYID()+" 物业名称："+order.getProperty()+
				" 大楼名称："+order.getBuilding()+" 房间号："+order.getRoom()+" 销账状态："+order.getSKID()+" 订单ID:"+
				order.getCporderid();
		String content1 = "删除结果：";
		OperateLog operateLog = new OperateLog();
		operateLog.setAction(0);
		if(order.getResult().equals("0")&&order.getPrice()>0){
			operateLog.setMemo(content1+"删除失败:订单已缴费"+content);
			operateLog.setTime(new Date());
			mDataBaseService.saveOperateLog(operateLog);
			return "删除失败:订单已缴费";
		}
		flag = mDataBaseService.deleteOrder(order);
		if(!flag){
			operateLog.setMemo(content1+"系统错误"+content);
			operateLog.setTime(new Date());
			mDataBaseService.saveOperateLog(operateLog);
			return "系统错误";
		}
		operateLog.setMemo(content1+"删除成功"+content);
		operateLog.setTime(new Date());
		mDataBaseService.saveOperateLog(operateLog);
		return "删除成功";
	}
	
	/**
	 * 20161213
	 * 销账：固定条件 交易额>0,交易结果 已交易，销账状态： Error
	 * @param order
	 * @return
	 * @throws Exception
	 */
	
	private String doPutWeiFee(Order order) throws Exception{
		PutWeiFee putWeiFee = new PutWeiFee();
		UrlMap urlMap = new UrlMap();
		order = mDataBaseService.getOrder(order).get(0);
		
		if(!order.getSKID().equals("")&&!order.getSKID().toUpperCase().contains("ERROR")){
			return "无效操作：重复销账";
		}else if(order.getResult().equals("0")&&order.getPrice()>0){
			urlMap.setUrlkey(order.getSource());
			urlMap = mDataBaseService.getUrlMap(urlMap).get(0);
			
			
			urlMap.setUrlkey(order.getSource());
			urlMap = mDataBaseService.getUrlMap(urlMap).get(0);
			putWeiFee.setHTID(order.getHTID());
			putWeiFee.setJFYF(order.getJFYF());
			putWeiFee.setOrderNum(order.getCporderid());
			putWeiFee.setBZIDList(order.getBZIDList());
			putWeiFee.setFeiList(order.getFeiList());
			putWeiFee.setPayMode("0");
			putWeiFee.setSercetKey(urlMap.getSecretkey());
			putWeiFee.setUrlString(urlMap.getUrlstring());
			putWeiFee.setSoapActionString(urlMap.getSoapactionstring());
			String SKID = haolongService.putWeiFee(putWeiFee);
			if(SKID.contains("已销账")){
				return "已销账";
			}
			order.setSKID(SKID);
			//System.out.println(SKID);
			if("".equals(SKID)||SKID==null||SKID.toUpperCase().contains("ERROR")){
				return "销账失败:订单无法更新";
			}
			
			order.setResult("0"); //交易结果成功
			mDataBaseService.updateOrder(order);
			return "销账成功";
		}else if(order.getPrice()==0){
			return "销账失败:客户未付款";
		}
		
		return "销账失败:不满足销账条件";

	}
	
	
	private String doPutWeiFee1(Order order) throws Exception{
		PutWeiFee1 putWeiFee1 = new PutWeiFee1();
//		SecretKeyMap secretKeyMap = new SecretKeyMap();
		UrlMap urlMap = new UrlMap();
		order = mDataBaseService.getOrder(order).get(0);
		
		if(!order.getSKID().equals("")&&!order.getSKID().toUpperCase().contains("ERROR")){
			return "无效操作：重复销账";
		}else if(order.getResult().equals("0")&&order.getPrice()>0){
			urlMap.setUrlkey(order.getSource());
			urlMap = mDataBaseService.getUrlMap(urlMap).get(0);

			putWeiFee1.setHTID(order.getHTID());
			putWeiFee1.setOrderNum(order.getCporderid());
			putWeiFee1.setBZIDList(order.getBZIDList());
			putWeiFee1.setPayMode("0");
			putWeiFee1.setSercetKey(urlMap.getSecretkey());
			putWeiFee1.setUrlString(urlMap.getUrlstring());
			putWeiFee1.setSoapActionString(null);
			String SKID = haolongService.putWeiFee1(putWeiFee1);
			if(SKID.contains("已销账")){
				return "已销账";
			}
			order.setSKID(SKID);

			if("".equals(SKID)||SKID==null||SKID.toUpperCase().contains("ERROR")){
				return "销账失败:订单无法更新";
			}
			order.setResult("0"); //交易结果成功
			mDataBaseService.updateOrder(order);
			return "销账成功";
		}else if(order.getPrice()==0){
			return "销账失败:客户未付款";
		}
		
		return "销账失败:";

	}
	
	/**
	 * 重构doPutWeiFee,使其共用
	 * @param order
	 * @return
	 * @throws Exception
	 */
	private String toPutWeiFee(Order order) throws Exception{
		UrlMap urlMap = new UrlMap();
		urlMap.setUrlkey(order.getSource());
		urlMap = mDataBaseService.getUrlMap(urlMap).get(0);
		
		order = mDataBaseService.getOrder(order).get(0);
		
		OperateLog operateLog = new OperateLog();
		operateLog.setAction(0);
		String content = " 计费月份："+order.getJFYF()+" 物业ID："+order.getWYID()+" 物业名称："+order.getProperty()+
				" 大楼名称："+order.getBuilding()+" 房间号："+order.getRoom()+" 销账状态："+order.getSKID()+" 订单ID:"+
				order.getCporderid();
		String content1 = "销账结果：";
		if(order.getOrderstatus().equals("0")&&(order.getSKID()==null||order.getSKID().equals(""))){
			//调用GetSKID接口
			String SKID = "";
			GetSKID getSKID = new GetSKID();
			getSKID.setOrderNumber(order.getCporderid());
			getSKID.setFeetype(""+(StringUtils.obj2Int(order.getWaresid())-1));
			getSKID.setSecretkey(urlMap.getSecretkey());
			getSKID.setUrlString(urlMap.getUrlstring());
			getSKID.setSoapActionString(null);
			SKID = haolongService.getSKID(getSKID);
			if(!SKID.equals("")&&!SKID.toUpperCase().contains("ERROR")){
				order.setSKID(SKID);
				mDataBaseService.updateOrder(order);
				operateLog.setMemo(content1+"销账成功"+content);
				operateLog.setTime(new Date());
				mDataBaseService.saveOperateLog(operateLog);
				return "销账成功";
			}
		}else if(!order.getSKID().equals("")&&!order.getSKID().toUpperCase().contains("ERROR")){
			operateLog.setMemo(content1+"无效操作：重复销账"+content);
			operateLog.setTime(new Date());
			mDataBaseService.saveOperateLog(operateLog);
			return "无效操作：重复销账";
		}else if(order.getResult().equals("0")&&order.getPrice()>0){
			String SKID = "";
			if(order.getWaresid().equals("1")){
				PutWeiFee putWeiFee = new PutWeiFee();		
				putWeiFee.setHTID(order.getHTID());
				putWeiFee.setJFYF(order.getJFYF());
				putWeiFee.setOrderNum(order.getCporderid());
				putWeiFee.setBZIDList(order.getBZIDList());
				putWeiFee.setFeiList(order.getFeiList());
				putWeiFee.setPayMode("0");
				putWeiFee.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
				putWeiFee.setSercetKey(urlMap.getSecretkey());
				putWeiFee.setUrlString(urlMap.getUrlstring());
				putWeiFee.setSoapActionString(urlMap.getSoapactionstring());
				SKID = haolongService.putWeiFee(putWeiFee);
			}else if(order.getWaresid().equals("2")){
				PutWeiFee1 putWeiFee1 = new PutWeiFee1();
				putWeiFee1.setHTID(order.getHTID());
				putWeiFee1.setOrderNum(order.getCporderid());
				putWeiFee1.setBZIDList(order.getBZIDList());
				putWeiFee1.setPayMode("0");
				putWeiFee1.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
				putWeiFee1.setSercetKey(urlMap.getSecretkey());
				putWeiFee1.setUrlString(urlMap.getUrlstring());
				putWeiFee1.setSoapActionString(null);
				SKID = haolongService.putWeiFee1(putWeiFee1);
			}else if(order.getWaresid().equals("3")){
				PutWeiFee2 putWeiFee2 = new PutWeiFee2();
				putWeiFee2.setWYID(order.getWYID());
				putWeiFee2.setHTID(order.getHTID());
				putWeiFee2.setBZID(order.getBZIDList());
				putWeiFee2.setFee(order.getPrice().toString());
				putWeiFee2.setPayMode(order.getPaytype());
				putWeiFee2.setOrderNum(order.getCporderid());
				putWeiFee2.setCustomerName(order.getCustomername());
				putWeiFee2.setPayDate(order.getTranstime().substring(0, 10).replace("-", "/"));
				putWeiFee2.setUrlString(urlMap.getUrlstring());
				putWeiFee2.setSoapActionString(null);
				putWeiFee2.setSercetKey(urlMap.getSecretkey());
				SKID = haolongService.putWeiFee2(putWeiFee2);
			}
			
			if(SKID.contains("已销账")){
				if(isNumeric(order.getSKID())){
					operateLog.setMemo(content1+"已销账"+content);
					operateLog.setTime(new Date());
					mDataBaseService.saveOperateLog(operateLog);
					return "已销账";
				}else{
					GetSKID getSKID = new GetSKID();
					getSKID.setOrderNumber(order.getCporderid());
					getSKID.setFeetype(""+(StringUtils.obj2Int(order.getWaresid())-1));
					getSKID.setSecretkey(urlMap.getSecretkey());
					getSKID.setUrlString(urlMap.getUrlstring());
					getSKID.setSoapActionString(null);
					SKID = haolongService.getSKID(getSKID);
					order.setSKID(SKID);
					mDataBaseService.updateOrder(order);
					operateLog.setMemo(content1+"已销账,已更新"+content);
					operateLog.setTime(new Date());
					mDataBaseService.saveOperateLog(operateLog);
					return "已销账,已更新";
				}
			}
			
			order.setSKID(SKID);
			
			if("".equals(SKID)||SKID==null||SKID.toUpperCase().contains("ERROR")){
				operateLog.setMemo(content1+"销账失败:订单无法更新"+content);
				operateLog.setTime(new Date());
				mDataBaseService.saveOperateLog(operateLog);
				return "销账失败:订单无法更新";
			}
			
			order.setResult("0"); //交易结果成功
			mDataBaseService.updateOrder(order);
			operateLog.setMemo(content1+"销账成功"+content);
			operateLog.setTime(new Date());
			mDataBaseService.saveOperateLog(operateLog);
			return "销账成功";
		}else if(order.getPrice()==0){
			operateLog.setMemo(content1+"销账失败:客户未付款"+content);
			operateLog.setTime(new Date());
			mDataBaseService.saveOperateLog(operateLog);
			return "销账失败:客户未付款";
		}
		operateLog.setMemo(content1+"销账失败:不满足销账条件"+content);
		operateLog.setTime(new Date());
		mDataBaseService.saveOperateLog(operateLog);
		return "销账失败:不满足销账条件";
	}
	
	
	@RequestMapping("quicklyRemove.htm")
	@ResponseBody
	public String quicklyRemove(HttpServletRequest request, HttpServletResponse response) throws Exception{
		boolean flag=mDataBaseService.quicklyRemove(DateUtils.BeforeToday(30)); //删除两天前的无效记录
		OperateLog operateLog = new OperateLog();
		operateLog.setAction(1);
		operateLog.setTime(new Date());
		String content1 = "删除30天前废单结果:";
		if(flag){
			operateLog.setMemo(content1+"删除成功");
			mDataBaseService.saveOperateLog(operateLog);
			return "删除成功";
		}
		operateLog.setMemo(content1+"查不到条件");
		mDataBaseService.saveOperateLog(operateLog);
		return "查不到条件";
	}
	
	@RequestMapping("updateOrder.htm")
	@ResponseBody
    public boolean updateAd(String skid,String jfyf,String feetype,String transtype,String paytype,String begintime,String endtime,String chargestatus,Order order, HttpServletRequest request) throws Exception{ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		order.setSKID(skid);
		order.setJFYF(jfyf);
		order.setUpdatetime(sdf.format(new Date().getTime()));
		order.setTranstime(order.getTranstime()==""?null:order.getTranstime());
		order.setFeetype(order.getFeetype()==""?null:order.getFeetype());
		order.setPaytype(order.getPaytype()==""?null:order.getPaytype());
		order.setBegintime(order.getBegintime()==""?null:order.getBegintime());
		order.setEndtime(order.getEndtime()==""?null:order.getEndtime());
		order.setChargestatus(order.getChargestatus()==""?null:order.getChargestatus());
		//System.out.println(order.toString());
		return mDataBaseService.updateOrder(order);
	}
	
	@RequestMapping("updateRoomInfo.htm")
	@ResponseBody
	public boolean updateRoomInfo(String id ,String htid,String cporderid,String source) throws Exception{
		Order order = new Order();
		order.setHTID(htid);
		order.setId(Integer.getInteger(id));
		order.setSource(source);
		order.setCporderid(cporderid);
		UrlMap urlMap = new UrlMap();
		urlMap.setUrlkey(order.getSource());
		urlMap = mDataBaseService.getUrlMap(urlMap).get(0);
		GetContract bean = new GetContract();
		bean.setHTID(htid);
		bean.setKHID("");
		bean.setSecretkey(urlMap.getSecretkey());
		bean.setSoapActionString(urlMap.getSoapactionstring());
		bean.setUrlString(urlMap.getUrlstring());
		
		Map<String,String> map = haolongService.getContract(bean).get(0);
		order.setProperty(map.get("物业名称"));
		order.setBuilding(map.get("大楼名称"));
		order.setRoom(map.get("内部房号"));
		//System.out.println(order.toString());
		boolean result = mDataBaseService.updateOrder(order);
		return result;
	}
	
	@RequestMapping("exportExcel.htm")  
	 public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException{  
		
		
		HSSFWorkbook wb = new HSSFWorkbook();  
		//创建HSSFSheet对象  
		HSSFSheet sheet = wb.createSheet("sheet0");  
		//创建HSSFRow对象  
		HSSFRow row = sheet.createRow(0);  
		//创建HSSFCell对象  
		HSSFCell cell=row.createCell(0);  
		//设置单元格的值  
	
		row.createCell(0).setCellValue("来源");
		row.createCell(1).setCellValue("应用ID");
		row.createCell(2).setCellValue("商品ID");
		row.createCell(3).setCellValue("用户账号");
		row.createCell(4).setCellValue("计费月份");
		row.createCell(5).setCellValue("物业ID");
		row.createCell(6).setCellValue("物业名称");
		row.createCell(7).setCellValue("大楼名称");
		row.createCell(8).setCellValue("房间号");
		row.createCell(9).setCellValue("销账状态");
		row.createCell(10).setCellValue("订单ID");
		row.createCell(11).setCellValue("订单创建时间");
		row.createCell(12).setCellValue("交易额");
		row.createCell(13).setCellValue("交易结果");
		row.createCell(14).setCellValue("订单状态");
		row.createCell(15).setCellValue("交易流水ID");
		row.createCell(16).setCellValue("交易时间");
		row.createCell(17).setCellValue("合同ID");
		row.createCell(18).setCellValue("最后更新时间");
		row.createCell(19).setCellValue("计费类型");
		row.createCell(20).setCellValue("货币类型");
		row.createCell(21).setCellValue("交易类型");
		row.createCell(22).setCellValue("支付类型");
		row.createCell(23).setCellValue("计费项目ID");
		row.createCell(24).setCellValue("预收款");
		row.createCell(25).setCellValue("支付平台 ");
		row.createCell(26).setCellValue("线下");
		row.createCell(27).setCellValue("用户备注");
		
		for(int i = 1; i <= getOrderNoPage.size() ; i++){
			HSSFRow rowi = sheet.createRow(i);
			int index = i - 1 ; 
			rowi.createCell(0).setCellValue(getOrderNoPage.get(index).getSource());
			rowi.createCell(1).setCellValue(getOrderNoPage.get(index).getAppid());
			rowi.createCell(2).setCellValue(getOrderNoPage.get(index).getWaresid());
			rowi.createCell(3).setCellValue(getOrderNoPage.get(index).getAppuserid());
			rowi.createCell(4).setCellValue(getOrderNoPage.get(index).getJFYF());
			rowi.createCell(5).setCellValue(getOrderNoPage.get(index).getWYID());
			rowi.createCell(6).setCellValue(getOrderNoPage.get(index).getProperty());
			rowi.createCell(7).setCellValue(getOrderNoPage.get(index).getBuilding());
			rowi.createCell(8).setCellValue(getOrderNoPage.get(index).getRoom());
			rowi.createCell(9).setCellValue(getOrderNoPage.get(index).getSKID());
			rowi.createCell(10).setCellValue(getOrderNoPage.get(index).getCporderid());
			rowi.createCell(11).setCellValue(getOrderNoPage.get(index).getCreatetime());
			BigDecimal price = new BigDecimal(getOrderNoPage.get(index).getPrice());
			rowi.createCell(12).setCellValue(price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			rowi.createCell(13).setCellValue(getOrderNoPage.get(index).getResult());
			rowi.createCell(14).setCellValue(getOrderNoPage.get(index).getOrderstatus());
			rowi.createCell(15).setCellValue(getOrderNoPage.get(index).getTransid());
			rowi.createCell(16).setCellValue(getOrderNoPage.get(index).getTranstime());
			rowi.createCell(17).setCellValue(getOrderNoPage.get(index).getHTID());
			rowi.createCell(18).setCellValue(getOrderNoPage.get(index).getUpdatetime());
			rowi.createCell(19).setCellValue(getOrderNoPage.get(index).getFeetype());
			rowi.createCell(20).setCellValue(getOrderNoPage.get(index).getCurrency());
			rowi.createCell(21).setCellValue(getOrderNoPage.get(index).getTranstype());
			rowi.createCell(22).setCellValue(getOrderNoPage.get(index).getPaytype());
			rowi.createCell(23).setCellValue(getOrderNoPage.get(index).getBZIDList());
			rowi.createCell(24).setCellValue(getOrderNoPage.get(index).getFeiList());
			String platsystem = getOrderNoPage.get(index).getPlatsystem();
			if(platsystem!=null && !platsystem.equals("")){
				switch(platsystem){
				case "0": platsystem = "爱贝"; break;
				case "1": platsystem = "好邻邦微信"; break;
				case "2": platsystem = "好邻邦支付宝"; break;
			}
			}
			rowi.createCell(25).setCellValue(platsystem);
			Boolean offline = getOrderNoPage.get(index).getOffline()==null?false:true;
			if(offline){
				rowi.createCell(26).setCellValue("是");
			}else{
				rowi.createCell(26).setCellValue("否");
			}		
			rowi.createCell(27).setCellValue(getOrderNoPage.get(index).getCustomername());
		}
		//输出Excel文件  
		String filename = null;
		String destPath = null;
		try {
			UUID uuid = UUID.randomUUID();
			filename = uuid.toString()+System.currentTimeMillis()+".xls";
			destPath = request.getSession().getServletContext().getRealPath("");
			destPath = FileUtils.getParent(destPath)+"/wy_cms/tmp/";
			FileOutputStream output=new FileOutputStream(destPath+filename);
			wb.write(output); 
			output.flush();
			output.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
		 response.setContentType("multipart/msword");
	        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
	        response.setHeader("Content-Disposition", "attachment;fileName="+filename);  
	        ServletOutputStream out;  
	        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        
	        File file = new File(destPath+filename);  
	        try {  
	            FileInputStream inputStream = new FileInputStream(file);  
	  
	            //3.通过response获取ServletOutputStream对象(out)  
	            out = response.getOutputStream();  
	            
	            int b = 0;
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
	
	@RequestMapping("getExcelDate.htm")
	@ResponseBody
	public void getExcelDate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String source = StringUtils.obj2String(request.getParameter("source"));
		String JFYF = StringUtils.obj2String(request.getParameter("JFYF"));
		String WYID = StringUtils.obj2String(request.getParameter("WYID"));
		String platsystem = StringUtils.obj2String(request.getParameter("platsystem"));
		Boolean offline = BooleanUtils.toBooleanObject(request.getParameter("offline"));
		String chargestatus = StringUtils.obj2String(request.getParameter("chargestatus"));
		String orderstatus = StringUtils.obj2String(request.getParameter("orderstatus"));
		String begintime = StringUtils.obj2String(request.getParameter("begintime"));
		String endtime = StringUtils.obj2String(request.getParameter("endtime"));
		
		Order order = new Order();
		order.setSource(source.equals("")?null:source);
		order.setWYID(WYID.equals("")?null:WYID);
		order.setJFYF(JFYF.equals("")?null:JFYF);
		order.setPlatsystem(platsystem.equals("")?null:platsystem);
		order.setOffline(offline==null?null:offline);
		order.setChargestatus(chargestatus.equals("")?null:chargestatus);
		order.setOrderstatus(orderstatus.equals("")?null:orderstatus);
		
		if(begintime == "" && endtime == ""){
			order.setBegintime(DateUtils.StringDateToday());
			order.setEndtime(null);
		}else if(begintime != "" && endtime == ""){
			order.setBegintime(begintime);
			order.setEndtime(null);
		}else if(begintime == "" && endtime != ""){
			order.setBegintime(null);
			order.setEndtime(endtime);
		}else {
			order.setBegintime(begintime);
			order.setEndtime(endtime);
		}
		
		getOrderNoPage = mDataBaseService.getOrderNoPage(order);
	}
	
	public static boolean isNumeric(String str){    
        if(str == null || str.equals("")){    
            return false;    
        }    
        for (int i = str.length();--i>=0;){    
            if (!Character.isDigit(str.charAt(i))){    
                return false;    
            }    
        }    
        return true;    
    } 
	
	/*@RequestMapping("testPutWeiFee.htm")
	public void testPutWeiFee(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PutWeiFee putWeiFee = new PutWeiFee();	
		putWeiFee.setHTID("12404");
		putWeiFee.setJFYF("201707");
		putWeiFee.setOrderNum("xinzirun_201707260954440515");
		putWeiFee.setBZIDList("296,292,295");
		putWeiFee.setFeiList("89.84,191.81,26.64");
		putWeiFee.setPayMode("403");
		putWeiFee.setPayDate("2017/07/26");
		putWeiFee.setSercetKey("xinzirun2017@hlb");
		putWeiFee.setUrlString("http://www.xinzirun.net:88/sdfeeweb/service.asmx");
		putWeiFee.setSoapActionString("http://tempuri.org/");
		String SKID = "";
		try {
			SKID = haolongService.putWeiFee(putWeiFee);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SKID: "+SKID);
	}*/
	
	/*public static void main(String[] args) throws ParseException{
		String s = "2017-03-02 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
		Date date = sdf.parse(s);
		Calendar c = Calendar.getInstance();  
        c.setTime(date);   //设置当前日期  
        c.add(Calendar.DATE, 1); //日期加1天  
        date = c.getTime(); 
        s = sdf.format(date);
        System.out.println(s);
	}*/
}
