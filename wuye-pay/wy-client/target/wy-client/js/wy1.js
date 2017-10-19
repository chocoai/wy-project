	//初始化表单数据
	var jmglfeehj = 0;
	var jmshuifeehj = 0;
	var clickCount = 0;
	 
	function setTotal(){
	    jmglfeehj = parseFloat($("#byqs").html()) + parseFloat($("#qswy").html()) + parseFloat($("#bcznj").html()) + parseFloat($("#wyznj").html());   
		$("#jmglfeehj").html(jmglfeehj);
		       
		jmshuifeehj = parseFloat($("#byqs2").html()) + parseFloat($("#qswy2").html()) + parseFloat($("#bcznj2").html()) + parseFloat($("#wyznj2").html());   
		$("#jmshuifeehj").html(jmshuifeehj);
               
		var total = jmglfeehj + jmshuifeehj;
        total = total.toFixed(2);  
        return total;
    }
	
	var cliName;
	var moblie
	var feetype;
	var HTID;
	var JFYF;
	var source;//CorpCode
	var WYID;
	var urlString;
	var SIGN;
	var WYMC;
	var DLMC;
	var FH;
	var from;
	var url;
	var requestUrl;
	var Sumflag;
	
	function initFormData(){
		//获取物业费用客户信息
		
		/*var pri=parseFloat($("#totalPrice").val());
		if(pri==0){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}*/
		var adminShow = getQueryString("adminShow");
		if(adminShow == "true"){
			$("#adminShow").attr("style","display:block");
		}
		feetype = getQueryString("x0");
		WYID = getQueryString("x1");
		HTID = getQueryString("x2");
		JFYF = getQueryString("x3");
		source = getQueryString("x4");
		url = getQueryString("x5");
		SIGN = getQueryString("SIGN");
		fromtype = getQueryString("fromtype");
		from = getQueryString("from");//来源

		jQuery.ajax({
			url : "/wy-server/htmlAction/getSumflag.htm",
			type : "post",
			data : {"source":source, "wyid":WYID},
			async : false,
			dataType : "json",
			success : function(data){
				Sumflag = data;
			}
		});

		if(fromtype!=null && fromtype!=''){
			from = fromtype;
		}else if(from == "singlemessage"){
			from = 2;
		}
		
		if(url==null || url==""){
			requestUrl = "/wy-client/index1.html?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&SIGN="+SIGN+"&fromtype="+from;
		}else{
			requestUrl = url;
		}
		

		doAction(WYID, HTID, JFYF, source, SIGN, url);
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
				"SIGN":SIGN,
				"url":url
		};
		jQuery.ajax({
			url : "/wy-server/htmlAction/doAction1.htm",
			type : "post",
			data : param,
			async : false,
			dataType : "json",
			success : function(data){
				$("#INFO").html("");
				var result = eval('('+data+')');
				var valid = result.valid;
				if (valid) {
					
					cliName = result.username;
					moblie = result.phone;
					$("#username").html(result.username);
					$("#phone").html(result.phone);
					$("#addr").html(result.addr);
					WYMC = result.WYMC;
					DLMC = result.DLMC;
					FH = result.FH;
					showAds(result.ads);
					urlString = result.urlString;
					showFee(result.feelist);
				} else {
					alert("未查到记录！");
				}
				
			},
			error : function(jqXHR, textStatus, errorThrown){
				$("#feeDetail").attr("style","display:none");
				if(jqXHR.status==200){
					$("#feeDetail3").attr("style","display:none");
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
			var finished = data[i].付清标志;
			var display = "none";
			var disableFlag = "";
			if(finished == "true"){
				$("#finishedTitle").attr("style","display:block;height:30px;");
				finished = "（已付清）";
				$("#detail"+i).attr("style","display:none;");
				disableFlag = "disabled";
				var txt2 = "<input type='checkbox' name='mov' id='mov"+i+"' checked=true onclick='changeState("+i+")' onchange='showDetail("+i+");' "+disableFlag+" value='"+data[i].欠交金额+"'/>"
				   +"<a href='#' style='text-decoration:none;cursor:pointer;'  name='showf' id='showf"+j+"'><span id='feeName"+i+"'>"+data[i].项目名称+"</span>：<span id='jmglfeehj"+i+"'>"+data[i].欠交金额+"</span>&nbsp;&nbsp;"
				   +"<span id='jmglBZID"+i+"' value='"+data[i].BZID+"'></span></a>"
				   +"<div id='detail"+i+"' style='margin-left: 20px;height: 50px;display:"+display+"'>"+"</div><br></br>";
				txt = txt + txt2;
			}
		}
		txt00="<div style = 'padding-bottom:4px;padding-top:4px;'><input type='checkbox' checked=true id='checka'  onclick='checkAll()'  />"+"<span>全选</span></div><br/>"
		for (var j=0;j<length;j++) {
			var finished = data[j].付清标志;
			var display = "none";
			var disableFlag = "";
			if(finished == "false"){
				finished = "（未付清）";
				//display = "block";
				var txt22 = "<input type='checkbox' name='mov' id='mov"+j+"' checked=true onclick='changeState("+j+")' onchange='showDetail("+j+");' "+disableFlag+" value='"+data[j].欠交金额+"'/>"
					//+"<label for='mov"+j+"'>" +
					+"<a href='#' style='text-decoration:none;cursor:pointer;color:#000000' name='showf' id='showf"+j+"'><span id='feeName"+j+"'>"+data[j].项目名称+"</span>：<span id='jmglfeehj"+j+"'>"+data[j].欠交金额+"</span>&nbsp;&nbsp;"
					+"<span id='jmglBZID"+j+"' value='"+data[j].BZID+"'></span></a>"
					+"<div name='detail' id='detail"+j+"' style='margin-left: 20px;height: 50px;display:"+display+"'>"+"</div><br></br>";
				txt00 = txt00 + txt22;
			}
		}
		detailList.html(txt);
		finishedList.html(txt00);
		checkSumflag(Sumflag);
	}
	
	function checkSumflag(Sumflag){
		if(Sumflag == "0"){
			/*$("input[type='checkbox']").prop("checked", false);
			var value = 0.00;
			$("#totalPrice").val(value.toFixed(2));
			$("#price2").html(value.toFixed(2));*/
		}else if(Sumflag == "1"){
			$("input[type='checkbox']").attr("disabled","disabled");
		}
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
		/*var pri=parseFloat($("#totalPrice").val());
		if(pri==0){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}*/

	}
   
	//调用爱贝计费
	var aibeiPay = new AiBeiPay(); //初始化爱贝支付JS(此类有且只能创建一次)
	
	function aiPay1() {
		var BZIDlist = getBZID();
		var waresid = parseInt(feetype)+1;
		var totalPrice = $.trim($("#totalPrice").val());
		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
			alert("合计金额不能为空，请选中缴费项目！");
			return;
		}
		var price = $("#totalPrice").val();
		var redata =  serverOrder(price, HTID,waresid, BZIDlist);
		var object = JSON.parse(redata);
		var transId = object.transid;
		var requestUrl = object.requesturl+"?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&x5="+SIGN;
		if (transId == undefined){
			return false;
		}
		var data = {};
		
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
	
	
	
	function serverOrder(price, HTID,waresid, BZIDlist) {
		var transid;
		var redata;
		var order = {
				"cliName":cliName,
				"moblie":moblie,
				"price":price,
				"HTID":HTID,
				"waresid":waresid,
				"JFYF":JFYF,
				"BZIDlist":BZIDlist,
				"source":source,
				"WYID":WYID,
				"urlString":urlString,
				"WYMC" : WYMC,
				"DLMC" : DLMC,
				"FH" : FH,
				"feetype" : feetype,
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
				} else {
					alert("没有对应支付方式");
				}
				//微信支付
				if(data=="oklongpay"){
					window.location.href="/wy-server/pay/choosePayType.htm";
					return null;
				}else if(data=="otherpay"){
					//支付宝
					window.location.href="/wy-server/pay/otherpay.htm";
					return null;
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
	

	
	function putPaymode(){
		alert("划款成功！");
	}
	
	function checkAll(){
		
		
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
		/*var pri=value;
		if(pri==0.00 || yearValue != JFYF2year || monthValue != JFYF2month){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}*/
	}
	
	function getFeeList(){

		var Syear=$("#year").val()
		var Smonth=$("#month").val();
		var SJFYF=Syear+Smonth;
		
	
		doAction(WYID, HTID, SJFYF, source, SIGN);
		checkAll();
		adjust()
	}

	function serverOrder2(price, HTID,waresid, BZIDlist) {
		clickCount = clickCount + 1;
		if(clickCount > 1){
			return;
		}
		var transid;
		var redata;
		var order = {
				"cliName":cliName,
				"moblie":moblie,
				"price":price,
				"HTID":HTID,
				"waresid":waresid,
				"JFYF":JFYF,
				"BZIDlist":BZIDlist,
				"source":source,
				"WYID":WYID,
				"urlString":urlString,
				"WYMC" : WYMC,
				"DLMC" : DLMC,
				"FH" : FH,
				"feetype" : feetype,
				"SIGN" : SIGN,
				"from" : from
		};
		$.ajax({
			url : '/wy-server/getTransId/createOrder.htm',
			type : 'POST',
			data : order,
			async : false,
			dataType : 'json',
			success : function(data) {
				window.location.href = "/wy-server/pay/choosePayType.htm?orderid="+data.cporderid;
			},
			error : function(e) {
				alert("serverOrder error:"+e);
			}
		});
	}

	function aiPay2() {
		var BZIDlist = getBZID();
		var waresid = parseInt(feetype)+1;
		var totalPrice = $.trim($("#totalPrice").val());
		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
			alert("合计金额不能为空，请选中缴费项目！");
			return;
		}
		var price = $("#totalPrice").val();
		serverOrder2(price, HTID,waresid, BZIDlist);
	}
	

	