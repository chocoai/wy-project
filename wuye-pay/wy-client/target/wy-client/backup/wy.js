	//初始化表单数据
	var jmglfeehj = 0;
	var jmshuifeehj = 0;
	 
	function setTotal(){
	    jmglfeehj = parseFloat($("#byqs").html()) + parseFloat($("#qswy").html()) + parseFloat($("#bcznj").html()) + parseFloat($("#wyznj").html());   
		$("#jmglfeehj").html(jmglfeehj);
		       
		jmshuifeehj = parseFloat($("#byqs2").html()) + parseFloat($("#qswy2").html()) + parseFloat($("#bcznj2").html()) + parseFloat($("#wyznj2").html());   
		$("#jmshuifeehj").html(jmshuifeehj);
               
		var total = jmglfeehj + jmshuifeehj;
        total = total.toFixed(2);  
        return total;
    }
	var feetype;
	var HTID;
	var JFYF;
	var source;//CorpCode
	var WYID;
	var urlString;
	var SIGN;
	
	var requestUrl;
	
	function initFormData(){
		//获取物业费用客户信息
	
	
		
		var pri=parseFloat($("#totalPrice").val());
		if(pri==0){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}
		var adminShow = getQueryString("adminShow");
		if(adminShow == "true"){
			$("#adminShow").attr("style","display:block");
		}
		feetype=getQueryString("x0");
		WYID = getQueryString("x1");
		HTID = getQueryString("x2");
		JFYF = getQueryString("x3");
		source = getQueryString("x4");
		SIGN = getQueryString("x5");
		
		requestUrl = "/wy-client/index.html?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&x5="+SIGN;
		
		doAction(WYID, HTID, JFYF, source, SIGN);
		adjust();
		doSlide();
	}
	
	function adjust() {
		var width = document.body.offsetWidth;
		$("#adsmain").attr("style", "width:"+width+";height:"+width/2);
		$("#adsmyslide").attr("style", "float: left;height: "+width/2+";width: "+width+";position: absolute;overflow: hidden;");
		$("#ads").attr("style", "position: absolute;width:"+width*100);
		$("#ads a").attr("style", "float: left;");
		$("#ads a img").attr("style", "float: left;width:"+width+";height:"+width/2);
	}
	var yearValue;
	var monthValue;
	var JFYF2year;
	var JFYF2month;
	function doAction(WYID, HTID, JFYF, source, SIGN) {
		var param = {
				"feetype":feetype,
				"WYID":WYID,
				"HTID":HTID,
				"JFYF":JFYF,
				"SOURCE":source,
				"SIGN":SIGN
		};
		jQuery.ajax({
			url : "/wy-server/htmlAction/doAction.htm",
			type : "post",
			data : param,
			async : false,
			dataType : "json",
			success : function(data){
				if(data==null){
					$("#feeDetail").attr("style","display:none");
					$("#feeDetail3").attr("style","display:none");
					$("#INFO").html("非法操作(超过当前缴费月份)!").css("color","red");	
				}else{
				$("#INFO").html("");
				var result = eval('('+data+')');
				var valid = result.valid;
				if (valid) {
					yearValue=result.JFYFstr.substr(0,4);
					monthValue = result.JFYFstr.substr(5,2);
					JFYF2year = result.JFYF2.substr(0,4);
					JFYF2month = result.JFYF2.substr(4,2);
					showDate();
					
					$("#username").html(result.username);
					$("#phone").html(result.phone);
					$("#addr").html(result.addr);
					showAds(result.ads);
					urlString = result.urlString;
					
					showFee(result.feelist);
				} else {
					alert("未查到记录！");
				}}
				
			},
			error : function(jqXHR, textStatus, errorThrown){
				$("#feeDetail").attr("style","display:none");
				if(jqXHR.status==200){
					$("#feeDetail3").attr("style","display:none");
					$("#INFO").html("非法操作(超过当前缴费月份)!").css("color","red");				
				}else if(jqXHR.status==500){
					alert("接口服务器异常!");
				}
				
			}
		});
	}
	
	function showAds(ads) {
		var txtimg = "";
		var txtol = "";
		for (var i=0;i<ads.length;i++) {
			var txt2 = "<a href=\""+ads[i].href + "\"><img src=\"" + ads[i].imgSrc + "\"></a>";
			txtimg = txtimg + txt2;
			
			var txt3 = "<li>" + i + "</li>";
			txtol = txtol + txt3;
		}
		$("#ads").html(txtimg);
		$("#slideol").html(txtol);
	}
	
	function showFee(data) {
		var length = data.length;
    	if(data[0].value == "error:07-There is no record(没有记录)"){
			$("#feeDetail").attr("style","display:none");
			$("#feeDetail3").attr("style","display:block");			
			return;
		}else{
			$("#feeDetail").attr("style","display:block");
			$("#feeDetail3").attr("style","display:none");
		}
		var txt = "";
		var txt00 = "";
		 
		var detailList = $("#detailList");
		var finishedList = $("#finishedList");
		txt00="<div style = 'padding-bottom:4px;padding-top:4px;'><input type='checkbox' checked=true id='checka' onclick='checkAll()'  />"+"<span>全选</span></div><br/>"
		for (var i=0;i<length;i++) {
			var finished = data[i].是否付清;
			var display = "none";
			var disableFlag = "";
			if(finished == "true"){
				$("#finishedTitle").attr("style","display:block;height:30px;");
				finished = "（已付清）";
				$("#detail"+i).attr("style","display:none;");
				disableFlag = "disabled";
				var txt2 = "<input type='checkbox' name='mov' id='mov"+i+"' checked=true onclick='changeState("+i+")' onchange='showDetail("+i+");'  value='"+data[i].欠收合计+"'/>"
				   //+"<label for='mov"+i+"'>" +
				   +"<a href='#' style='text-decoration:none;cursor:pointer;' onclick='showD("+j+")'  name='showf' id='showf"+j+"'><span id='feeName"+i+"'>"+data[i].项目名称+"</span>：<span id='jmglfeehj"+i+"'>"+data[i].欠收合计+"</span>&nbsp;&nbsp;"
				   +"<span id='finished"+i+"'>"+"</span><span id='jmglBZID"+i+"' value='"+data[i].BZID+"'></span></a>预收<select id='s"+i+"'  onchange='calculation(this.id,"+data[i].月费用+","+data[i].欠收合计+")'></select>个月"
				   +"<div id='detail"+i+"' style='margin-left: 20px;height: 50px;display:"+display+"'>"
				   +"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>本月欠费用：<span id='byqs"+i+"'>"+data[i].欠收应收款+"</span></span>"
				   +"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>计费月份：<span id='qswy"+i+"'>"+data[i].欠收往月+"</span></span>"
				   +"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>违约金:<span id='wyznj"+i+"'>"+data[i].欠收滞纳金+"</span></span></div><br></br>";
				txt = txt + txt2;
			}
		}
		txt00="<div style = 'padding-bottom:4px;padding-top:4px;'><input type='checkbox' checked=true id='checka'  onclick='checkAll()'  />"+"<span>全选</span></div><br/>"
		for (var j=0;j<length;j++) {
			var finished = data[j].是否付清;
			var display = "none";
			var disableFlag = "";
			if(finished == "false"){
				finished = "（未付清）";
				//display = "block";
				var txt22 = "<input type='checkbox' name='mov' id='mov"+j+"' checked=true onclick='changeState("+j+")' onchange='showDetail("+j+");' "+disableFlag+" value='"+data[j].欠收合计+"'/>"
					//+"<label for='mov"+j+"'>" +
					+"<a href='#' style='text-decoration:none;cursor:pointer;color:#000000' onclick='showD("+j+")' name='showf' id='showf"+j+"'><span id='feeName"+j+"'>"+data[j].项目名称+"</span>：<span id='jmglfeehj"+j+"'>"+data[j].欠收合计+"</span>&nbsp;&nbsp;"
					+"<span id='finished"+j+"'>"+"</span><span id='jmglBZID"+j+"' value='"+data[j].BZID+"'></span></a>预收<select id='s"+j+"'  onchange='calculation(this.id,"+data[j].月费用+","+data[j].欠收合计+")'></select>个月"
					+"<div name='detail' id='detail"+j+"' style='margin-left: 20px;height: 50px;display:"+display+"'>"
					+"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>本月费用：<span id='byqs"+j+"'>"+data[j].欠收应收款+"</span></span>"
					+"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>计费月份：<span id='qswy"+j+"'>"+data[j].欠收往月+"</span></span>"
					+"<span class='ui-btn-inner' style='padding-top:4px;padding-bottom: 4px;'>违约金:<span id='wyznj"+j+"'>"+data[j].欠收滞纳金+"</span></span></div><br></br>";
				txt00 = txt00 + txt22;
			}
		}
		
		detailList.html(txt);
		finishedList.html(txt00);
		initPrePay(length);
	}
	
	function changeState(j){
		if($("#mov"+j).is(":checked")){
			$("#mov"+j).attr("checked",true)
		}else{
			$("#mov"+j).attr("checked",false)
		}
	}
	
	function getQueryString(name) { 
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
		var r = window.location.search.substr(1).match(reg); 
		if (r != null) {
			return unescape(r[2]);
		}
		return null; 
	}
	  
	

	function getBZID(){
		var jmglBZID = "";
		var mov = document.getElementsByName("mov");
		var value = 0;
		
		var len=$("#finishedList").children("input").length;
		
		for(var j = 0; j < mov.length; j++){
			for(var i=0;i<len;i++){
			
			var movid=$("#finishedList").children("input").eq(i).attr("id")
			
			if($("#mov"+j).is(":checked")&&("mov"+j)==movid){
				if (j > 0) {
					jmglBZID += ",";
				}
				jmglBZID += $("#jmglBZID"+j).attr("value");
			} else {
			
			}
			}
		}  
		if (jmglBZID.substr(0,1)==','){
			jmglBZID=jmglBZID.substr(1);
		}
		return jmglBZID;
	}
	
	
	function showDetail(i){
		
		var mov = document.getElementsByName("mov");
		var value = 0;
		if($("[name=mov]:checkbox").prop("checked")==true){
			$("#checka").prop("checked",true);
		}
		for(var j = 0; j < mov.length; j++){
			if(mov[j].checked){
				value += parseFloat(mov[j].value);
			} else {
				if($("#checka").prop("checked")==true){
					$("#checka").prop("checked",false);
				}
			}
		}
		$("#totalPrice").val(value.toFixed(2));
		$("#price2").html(value.toFixed(2));
		var pri=parseFloat($("#totalPrice").val());
		if(pri==0){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}

	}
   
	//调用爱贝计费
	var aibeiPay = new AiBeiPay(); //初始化爱贝支付JS(此类有且只能创建一次)
	
//	function aiPay1() {
//		var totalPrice = $.trim($("#totalPrice").val());
//		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
//			alert("合计金额不能为空，请选中缴费项目！");
//			return;
//		}
//		var price = $("#totalPrice").val();
//		var transId = serverOrder(price, HTID, JFYF, BZIDLIST);
//
//		if (transId == undefined){
//			return false;
//		}
//		var data = {};
//		data.transId = transId;
//		data.retFunc = "callbackData";
//		data.baseZIndex = 88;
//		data.closeTxt="返回首页"; //自定义返回游戏按钮
//		data.redirecturl = requestUrl; //当支付页面跳出收银台后，查询结果页回跳地址（必填）
//		data.cpurl = requestUrl; //返回商户地址，可以不填写
//		aibeiPay.clickAibei(data);
//		
//	}
	
	
	
	
	function aiPay1() {
		var BZIDlist = getBZID();
		var waresid = parseInt(feetype)+1;
		var totalPrice = $.trim($("#totalPrice").val());
		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
			alert("合计金额不能为空，请选中缴费项目！");
			return;
		}
		var price = $("#totalPrice").val();
		var redata =  serverOrder(price, HTID, waresid,JFYF, BZIDlist);
		var object = JSON.parse(redata);
		var transId = object.transid;
		var requestUrl = object.requesturl+"?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&x5="+SIGN;
		if (transId == undefined){
			return false;
		}
		var data = {};
		
//		requestUrl = "http://sdpms.vicp.net:2020"+requestUrl;
//		requestUrl = "http://www.oklong.com:8080"+requestUrl;
		data.transId = transId;
		data.retFunc = "callbackData";
		data.baseZIndex = 88;
		data.closeTxt="返回首页"; //自定义返回游戏按钮
		data.redirecturl = requestUrl; //当支付页面跳出收银台后，查询结果页回跳地址（必填）
		data.cpurl = requestUrl; //返回商户地址，可以不填写

		var sign ;
		jQuery.ajax({
			url : "/wy-server/getTransId/getSignStr.htm",
			type : "post",
			data : data,
			async : false,
			dataType : "json",
			success : function(d){
				if(d!=null){
					sign = d;
				}else{
					alert("未查到记录")
				}
					
			}
		});
		
		var transdata={
				"transid": transId,
				"redirecturl" : requestUrl,
				"cpurl":requestUrl
		}
		var jsda=JSON.stringify(transdata);
		
		window.location.href="https://web.iapppay.com/h5/exbegpay?transdata="+encodeURIComponent(jsda)+
		"&sign="+encodeURIComponent(sign)+"&signtype=RSA" 
			
	}
	
	
	
	function serverOrder(price, HTID,waresid,JFYF, BZIDlist) {
		var transid;
		var redata;
		var order = {
				"price":price,
				"HTID":HTID,
				"waresid":waresid,
				"JFYF":JFYF,
				"BZIDlist":BZIDlist,
				"source":source,
				"WYID":WYID,
				"urlString":urlString
		};
		$.ajax({
			url : '/wy-server/getTransId/getTransId.htm',
			type : 'POST',
			data : order,
			async : false,
			dataType : 'json',
			success : function(data) {
				if(data!=""){
					redata = data;
				}
				var result = eval('('+data+')');
				if (result.transid != undefined) {
					transid = result.transid;
				} else {
					alert(result.errmsg);
				}
			},
			error : function(e) {
				alert("serverOrder error:"+e);
			}
		});
		return redata;
	}
	
	function saveUserId(appuserid) {
		try {
			sessionStorage.appuserid = appuserid;
		} catch (e) {
			alert('浏览器不支持本地缓存！');
		}
	}

	
	function callbackData(datas) {
	
		var status = datas.OrderStatus;
		if(status == 1){
		   status = "支付失败";
		}else if(status == 2){
		   status = "待支付";
		}else if(status == 3){
		   status = "正在处理";
		}else if(status == 4){
		   status = "系统异常";
		}else if(status == 0){
		   status = "支付成功";
		}
		console.info(datas);
//		alert(status+" ,流水号:"+datas.TransId);//TransId: "32071509081614477958", RetCode: 0, OrderStatus: 2
		window.location.href = requestUrl; 
	}
	


	//支付成功后的监听事件,保存订单信息
//	window.addEventListener('message', function(e) {
//		//var data = decodeURIComponent("{\"RetCode\":0,\"OrderStatus\":0,\"SignData\":\"transdata=%7B%22appid%22%3A%223002811959%22%2C%22appuserid%22%3A%22weitou%40iapppay.com%22%2C%22cporderid%22%3A%221442483370230%22%2C%22cpprivate%22%3A%227262%2F201509%2F%2C55%2C56%22%2C%22currency%22%3A%22RMB%22%2C%22feetype%22%3A0%2C%22money%22%3A0.01%2C%22paytype%22%3A501%2C%22result%22%3A0%2C%22transid%22%3A%2232021509171748598332%22%2C%22transtime%22%3A%222015-09-17+17%3A49%3A41%22%2C%22transtype%22%3A0%2C%22waresid%22%3A1%7D&sign=SNUR2VUymGyoSgdIcWD4TC2NJFEKhwL%2BVMfRCmKJYuREYF1MUPlQB62Gh2uyh%2BIN9NhTtdL7ozAv7%2FJjqdoLtwKYYpsmY%2BsOdHjs62jgpmBBxO2WxHUvQO%2B9LnHST7kmeMU5B7H48oOELF3f%2B3YFV3jiI8cYYmhHqd3Ig4vAaiE%3D&signtype=RSA\",\"Type\":0}");
//		$.ajax({
//			url : "/wy-server/order/updateOrder.htm",
//			type : "post",
//			data : urlString+"&&"+e.data,
//			success : function(data){
//				var result = eval('('+data+')');
//				if (result.SKID != undefined) {
//					$("#SKIDtxt").val(data);
//				} else {
//					alert(data);
//				}
//			},
//			error : function(data){
//				alert("更新订单信息错误！");
//			}
//		});
//	}, false);
	
	function putPaymode(){
		alert("划款成功！");
	}
	
	function checkAll(){
		
//		var len=$("#detailList").children("input").length;
//		
//		for(var i=0;i<len;i++){
//			alert($("#detailList").children("input").eq(i).attr("id"));
//			var movid=$("#detailList").children("input").eq(i).attr("id")
//		}
		
		
		var mov= document.getElementsByName('mov');
		var value=0.00;
		
	    if ($("#checka").is(":checked")) {
	    
	        $("[name=mov]:checkbox").prop("checked", true);
	        for(var j =0;j<mov.length;j++){
	       		 value+=parseFloat(mov[j].value);
	        }
	    } else {
	        $("#finishedList>[name=mov]:checkbox").prop("checked", false);
	        
	        value=0.00;
	    }
	
	    $("#totalPrice").val(value.toFixed(2));
		$("#price2").html(value.toFixed(2));
		var pri=value;
		if(pri==0.00 || yearValue != JFYF2year || monthValue != JFYF2month){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}
	}
	
	function showD(showfId){
		var nextElement=$("#detail"+showfId);

		if(nextElement.css('display')=='none'){
			nextElement.attr("style","display:block;margin-left: 20px;height: 50px;");
		}else{
			nextElement.attr("style","display:none");
		}
	}
	
	function showDate(){

		var currentDate  = new Date();
		var year = currentDate.getFullYear();
		var month = currentDate.getMonth()+1;
		var yearSelect =  "<select id='year' onchange='javascript:getFeeList()'></select>"+"年";
		var monthSelect = "<select id='month' onchange='javascript:getFeeList()'></select>"+"月";;
		var content;
		var text = yearSelect + monthSelect;
		$("#date").html(text);
		for(var i = year - 10 ; i<year+10 ; i++){
			content += "<option value='"+i+"'>"+i+"</option>";		
		}
		$("#year").html(content);
		$("#year option[value="+yearValue+"]").attr("selected","selected");

		var content1;
		for(var i=1; i <= 12 ; i++){
			if(i <= 9){
				content1 += "<option value='0"+i+"'>0"+i+"</option>";
			}else {
				content1 += "<option value='"+i+"'>"+i+"</option>";
			} 			
		}
		
		
		$("#month").html(content1);
		$("#month option[value="+monthValue+"]").attr("selected","selected");
		
	}
	
	function getFeeList(){

		var Syear=$("#year").val()
		var Smonth=$("#month").val();
		var SJFYF=Syear+Smonth;
		
		var param={
				"feetype":feetype,
				"WYID":WYID,
				"HTID":HTID,
				"JFYF":SJFYF,
				"SOURCE":source,
			}
		jQuery.ajax({
			url : "/wy-server/htmlAction/getSIGN.htm",
			type : "post",
			data : param,
			async : false,
			dataType : "json",
			success : function(data){
				if(data!=null){
					SIGN=data;
				}else{
					alert("未查到记录")
				}
					
			}
		});
	
		doAction(WYID, HTID, SJFYF, source, SIGN);
		checkAll();
		adjust()
	}
	
	function initPrePay(length){
		for(var i = 0 ; i<=12 ;i++){
			for(var j=0;j<length;j++){
				$("#s"+j+"").append("<option value="+i+">"+i+"</option>");
			}
		}
	}
	
	function calculation(o,prefee,fee){
		var s = "#"+o;
		var jmglfeehjid = $(s).prev().children().next().attr("id");
		var movid = $(s).prev().prev().attr("id");
		var jmglfeehjn =  $("#"+jmglfeehjid);
		var movn = $("#"+movid);
		var count = $(s).find("option:selected").val()
		var feeList={};
		var total = prefee * count; 
		total=total.toFixed(2);
		total = Number(total)+fee;
		jmglfeehjn.html(total.toFixed(2));
		document.getElementById(movid).value=total.toFixed(2);
		showDetail(1)
	}
	