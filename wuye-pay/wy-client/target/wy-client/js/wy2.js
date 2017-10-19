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
	var requestUrl;
	var from;
	var url;
	var price;
	var bzid;
	
	function initFormData(){
		feetype = getQueryString("x0");
		WYID = getQueryString("x1");
		HTID = getQueryString("x2");
		JFYF = getQueryString("x3");
		source = getQueryString("x4");
		url = getQueryString("x5");//回调地址
		SIGN = getQueryString("SIGN");
		fromtype = getQueryString("fromtype");
		from = getQueryString("from");//来源
		price = getQueryString("price");//价格
		bzid = getQueryString("bzid");
		/*SIGN = getQueryString("x5");
		from = getQueryString("x6");//来源
		url = getQueryString("x7");//回调地址
		price = getQueryString("x8");//价格
		bzid = getQueryString("x9");*/
		if(fromtype!=null && fromtype!=''){
			from = fromtype;
		}else if(from == "singlemessage"){
			from = 2;
		}
		
		if(JFYF === null){
			JFYE = "";
		}

		//alert("0:"+feetype+" 1:"+WYID+" 2:"+HTID+" 3:"+JFYF+" 4:"+source+" 5:"+SIGN);
		if(url==null || url==""){
			requestUrl = "/wy-client/index2.html?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&SIGN="+SIGN+"&fromtype="+from;
		}else{
			requestUrl = url;
		}
		
		
		doAction(WYID, HTID, JFYF, source, SIGN, from, url, price, bzid);
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
	
	function doAction(WYID, HTID, JFYF, source, SIGN, from, url, price, bzid) {
		var param = {
				"feetype":feetype,
				"WYID":WYID,
				"HTID":HTID,
				"JFYF":JFYF,
				"SOURCE":source,
				"SIGN":SIGN,
				"from":from,
				"url":url,
				"price":price,
				"bzid":bzid
		};
		jQuery.ajax({
			url : "/wy-server/htmlAction/doAction2.htm",
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
					if((cliName=="") && (moblie=="")){
						$("#info1").css("display", "none");
						$("#common").css("display", "block");
					}
					$("#username").html(result.username);
					$("#phone").html(result.phone);
					$("#addr").html(result.addr);
					WYMC = result.WYMC;
					DLMC = result.DLMC;
					FH = result.FH;
					showAds(result.ads);
					urlString = result.urlString;
					showFee(result.feelist, result.price);
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
	
	//显示费用,data是零星费用的数据，json格式
	function showFee1(data, price) {
		//测试数据
		//data = "[{'BZID':'1', '项目名称':'停车费'},{'BZID':'2', '项目名称':'维修费'},{'BZID':'3', '项目名称':'搬运费'}]";
		//data = eval('(' + data + ')');
		var length = data.length;
		
		var txt = "";
		var txt00 = "";
		var sel = "<span style='display: inline-block; width:30%; font-size: 14px; height: 24px; margin: 6px 3% -3px 0%' >缴费项目</span><select id='sel' style='position:absolute;height: 27px; display: inline-block; width: 60%; border: 2px solid #ccc; font-size:14px; margin: 0px 0px 0px 0;'>";
		for(var i=0; i<length; i++){
			var bzid = data[i].BZID;
			var feename = data[i].项目名称;
			var option = "<option value='"+bzid+"'>"+feename+"</option>";
			sel += option;
		}
		sel += "</select>";
		 
		var detailList = $("#detailList");
		var finishedList = $("#finishedList");
		var memo = $("#memo");
		var jf = $("#jf");
		
		//detailList.html("<input placeholder='输入金额' onkeyup='clearNoNum(this)' type='number' id='moneyNum' style='width:60%; border: 2px solid #ccc; height: 30px; font-size:14px; margin: 3px 0px 10px 0;'/>");
	//	detailList.html("<input placeholder='输入金额' onkeyup='clearNoNum(this)' type='number' id='moneyNum' style='width:100%; line-height:100px; border: 2px solid #ccc; font-size:34px; margin: 3px 0px 10px 0;'/>");
		finishedList.html(sel);
		memo.html("<span style='display: inline-block; width:30%; font-size: 14px; height: 24px; margin: 5% 3% 0px 0%;padding-left: 7px;'>用户备注</span> <input id='customerName' type='text' style='font-size: 14px; height: 27px;display: inline-block; width: 65%;position: absolute;right: 1.5%;top:26%; border: 2px solid #ccc;' />");
		if(price!=null && price!=""){
			detailList.html("<div style='background-color: #fff;width: 100%;height: 50px;color: #c1c1c1;font-size: 14px;padding-top: 15px;margin-top: 8%;padding-left: 5%;'>金额(元)</div><input onkeyup='clearNoNum(this)' type='number' step='0.01' id='moneyNum' placeholder='输入金额' value='"+price+"' disabled='true' style='background-color: #fff;font-size: 50px;width: 100%;border: none;height: 70px;padding-bottom: 10px;padding-left: 5%;'>");
		}else{
			detailList.html("<div style='background-color: #fff;width: 100%;height: 50px;color: #c1c1c1;font-size: 14px;padding-top: 15px;margin-top: 8%;padding-left: 5%;'>金额(元)</div><input onkeyup='clearNoNum(this)' type='number' step='0.01' id='moneyNum' placeholder='输入金额' style='background-color: #fff;font-size: 50px;width: 100%;border: none;height: 70px;padding-bottom: 10px;padding-left: 5%;'>");
		}
		jf.html("<button style='width: 96%;height: 40px; border-radius: 5px;border: none;background-color: #59f;font-size: 18px;text-align: center;line-height: 30px;color: #fff; margin:10%  0 0 3%;' type='button' >缴费</button>  ");
	}

	function showFee(data, price) {
		//测试数据
		//data = "[{'BZID':'1', '项目名称':'停车费'},{'BZID':'2', '项目名称':'维修费'},{'BZID':'3', '项目名称':'搬运费'}]";
		//data = eval('(' + data + ')');
		var length = data.length;
		
		var txt = "";
		var txt00 = "";
		var co1 = "<div class='weui-cells'>";
		var co2 = "<div class='weui-cell weui-cell_select weui-cell_select-after'>";
		var co3 = "<div class='weui-cell__hd'>";
		var co4 = "<label class='weui-label'>缴费项目</label>";
		var co5 = "</div>";
		var co6 = "<div class='weui-cell__bd'>";
		var co7 = "<select class='weui-select' name='select2' id='sel'>";
		var co8 = "";
		for(var i=0; i<length; i++){
			var bzid = data[i].BZID;
			var feename = data[i].项目名称;
			var option = "<option value='"+bzid+"'>"+feename+"</option>";
			co8 += option;
		}
		co8 += "</select>";
		var sel = co1+co2+co3+co4+co5+co6+co7+co8+co5+co5+co5;
		 
		var detailList = $("#detailList");
		var finishedList = $("#finishedList");
		var memo1 = $("#memo1");
		var jf = $("#jf");
		
		//detailList.html("<input placeholder='输入金额' onkeyup='clearNoNum(this)' type='number' id='moneyNum' style='width:60%; border: 2px solid #ccc; height: 30px; font-size:14px; margin: 3px 0px 10px 0;'/>");
	//	detailList.html("<input placeholder='输入金额' onkeyup='clearNoNum(this)' type='number' id='moneyNum' style='width:100%; line-height:100px; border: 2px solid #ccc; font-size:34px; margin: 3px 0px 10px 0;'/>");
		finishedList.html(sel);
		if(price!=null && price!=""){
			detailList.html("<div style='background-color: #fff;width: 100%;height: 50px;color: #c1c1c1;font-size: 14px;padding-top: 15px;margin-top: 8%;padding-left: 5%;'>金额(元)</div><input onkeyup='clearNoNum(this)' type='number' step='0.01' id='moneyNum' placeholder='输入金额' value='"+price+"' disabled='true' style='background-color: #fff;font-size: 50px;width: 100%;border: none;height: 70px;padding-bottom: 10px;padding-left: 5%';>");
		}else{
			detailList.html("<div style='background-color: #fff;width: 100%;height: 50px;color: #c1c1c1;font-size: 14px;padding-top: 15px;margin-top: 8%;padding-left: 5%;'>金额(元)</div><input onkeyup='clearNoNum(this)' type='number' step='0.01' id='moneyNum' placeholder='输入金额' style='background-color: #fff;font-size: 50px;width: 100%;border: none;height: 70px;padding-bottom: 10px;padding-left: 5%';>");
		}

		var cp1 = "<div class='weui-cells' style='margin-top: 0px;font-size: 13px;'>";
		var cp2 = "<div class='weui-cell'>";
		var cp3 = "<div class='weui-cell__hd' id='usermemo'>";
		var cp4 = "<label class='weui-label' style='color: #1e60de;'>用户备注</label>";
		var cp5 = "</div>";
		var cp6 = "<div class='weui-cell__bd'>";
		var cp7 = "<input class='weui-input' placeholder='请输入备注'' id='customerName'>";
		memo1.html(cp1+cp2+cp3+cp4+cp5+cp6+cp7+cp5+cp5+cp5);


		jf.html("<button style='width: 96%;height: 40px; border-radius: 5px;border: none;background-color: #59f;font-size: 18px;text-align: center;line-height: 30px;color: #fff; margin:10%  0 0 3%;' type='button' >缴费</button>  ");
	
		$("#usermemo").click(function(){
			$("#customerName").focus();
		});
	}
	
	/*function clearNoNum(obj) {
		alert(obj.value);
		//先把非数字的都替换掉，除了数字和.
		obj.value = obj.value.replace(/[^\d.]/g,"");
		//必须保证第一个为数字而不是.
		obj.value = obj.value.replace(/^\./g,"");
		//保证只有出现一个.而没有多个.
		obj.value = obj.value.replace(/\.{2,}/g,".");
		//保证.只出现一次，而不能出现两次以上
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		//小数点后保留两位
		var pos = obj.value.indexOf(".");
		if(pos>-1){
			var pos1 = obj.value.substring(pos);
			if(pos1.length > 3){
				obj.value = obj.value.substring(0, pos+3);
			}
		}
	}*/
	function clearNoNum(obj) {
	    //先把非数字的都替换掉，除了数字和.
	    var vv = $("#moneyNum").val();
	    if((vv.indexOf(".")==-1) && vv.length>0){
	    	return;
	    }
	    
	    vv = vv.replace(/[^\d.]/g,"");
	    //必须保证第一个为数字而不是.
	    vv = vv.replace(/^\./g,"");
	    //保证只有出现一个.而没有多个.
	    vv = vv.replace(/\.{2,}/g,".");
	    //保证.只出现一次，而不能出现两次以上
	    vv = vv.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	    //小数点后保留两位
	    var pos = vv.indexOf(".");
	    //alert(pos);
	    if(pos>-1){
	        var pos1 = vv.substring(pos);
	       // alert(pos1);
	        if(pos1.length > 3){
	            vv = vv.substring(0, pos+3);
	        }
	    }
	   // alert(vv);
	    $("#moneyNum").val(vv);
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
		var jmglBZID = $("#sel").find("option:selected").val();
		return jmglBZID;
	}
	
	/*//计算复选框的金额
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

	}*/
   
	//调用爱贝计费
	var aibeiPay = new AiBeiPay(); //初始化爱贝支付JS(此类有且只能创建一次)
	
	
	
	function aiPay1() {
		var BZIDlist = getBZID();
		var waresid = parseInt(feetype)+1;
		var totalPrice = $.trim($("#moneyNum").val());
		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
			alert("缴费金额不能为空！");
			return;
		}
		var price = $("#moneyNum").val();
		var customerName = $("#customerName").val();
		var redata =  serverOrder(price, HTID,waresid, BZIDlist, customerName);
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
		//alert("http://localhost/wy-server/order/updateOrder2.htm?transdata="+encodeURIComponent(jsda)+"&sign="+encodeURIComponent(sign)+"&signtype=RSA");
		//window.location.href="http://localhost/wy-server/order/updateOrder2.htm?transdata="+jsda+"&sign="+sign+"&signtype=RSA";
	}
	
	
	
	function serverOrder(price, HTID,waresid, BZIDlist, customerName) {
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
				"customerName" : customerName,
				"feetype" : feetype
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
					return null;
				}
				//微信支付
				if(data=="oklongpay"){
					window.location.href = "/wy-server/pay/choosePayType.htm";
//					window.location.href = "/wy-server/weixin/topay.htm";
					return null;
				}else if(data=="otherpay"){
					//支付宝
					window.location.href = "/wy-server/pay/otherpay.htm";
					return null;
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
		var pri=value;
		if(pri==0.00 || yearValue != JFYF2year || monthValue != JFYF2month){
			$("#jf").attr("disabled",true);
		}else if(pri>0){
			$("#jf").attr("disabled",false);
		}
	}
	
	function getFeeList(){

		var Syear=$("#year").val()
		var Smonth=$("#month").val();
		var SJFYF=Syear+Smonth;
		
	
		doAction(WYID, HTID, SJFYF, source, SIGN);
		checkAll();
		adjust()
	}

	function aiPay2() {
		var BZIDlist = getBZID();
		var waresid = parseInt(feetype)+1;
		var totalPrice = $.trim($("#moneyNum").val());
		if(totalPrice == "0.00" || totalPrice == "" || totalPrice == "0" ){
			alert("缴费金额不能为空！");
			return;
		}
		var price = $("#moneyNum").val();
		var customerName = $("#customerName").val();
		serverOrder2(price, HTID,waresid, BZIDlist, customerName);
	}
	
	
	
	function serverOrder2(price, HTID,waresid, BZIDlist, customerName) {
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
				"customerName" : customerName,
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
		return redata;
	}

	