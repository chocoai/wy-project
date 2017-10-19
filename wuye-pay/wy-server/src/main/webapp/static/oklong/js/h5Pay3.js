function abpay(orderid, appid, appuserid, wyid){
	var redata;
	jQuery.ajax({
		url : "/wy-server/pay/abpay.htm",
		type : "post",
		//data : {"order":order, "account":account},
		data : {"orderid":orderid, "appid": appid, "appuserid": appuserid, "wyid": wyid},
		async : false,
		dataType : "json",
		success : function(data){
			redata = data;		
		}
	});
 
	var object = JSON.parse(redata);
	var transId = object.transid;
	var url = object.url;
	var requestUrl;
	if(url==null || url==""){
		requestUrl = object.requesturl+"?x0="+feetype+"&x1="+WYID+"&x2="+HTID+"&x3="+JFYF+"&x4="+source+"&SIGN="+SIGN+"&from="+object.from;
	}else{
		requestUrl = url;
	}

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

//微信支付
function wxpay(orderid, appid, appuserid, wyid){
	window.location.href="/wy-server/weixin/oAuth2.htm?orderid="+orderid+"&appid="+appid+"&appuserid="+appuserid+"&wyid="+wyid;	
}


//支付宝支付
function alipay(){
	$.ajax({
		url : '/wy-server/getTransId/updatePlat.htm',
		type : 'POST',
		data : {"platsystem" : "2"},
		async : false,
		dataType : 'json',
		success : function(data) {
			
		},
		error : function(e) {
			
		}
	});
	layer.open({
	    content: "正在开发中，请选择其他支付方式"
	    ,skin: 'msg'
	    ,time: 2 
	});		
}