<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path ;
%>
<%@ include file="/WEB-INF/views/include/bftags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0" />
<meta name="format-detection" content="telephone=no" />
<base href="<%=basePath%>">

<title>微信支付</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="${ctxStatic}/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${ctxStatic}/js/layer.js"></script>
</head>
<script type="text/javascript">
	//参数配置
	wx.config({
	    debug: false,
	    appId: "${jsapi.appid}", 
	    timestamp: "${jsapi.timestamp}", 
	    nonceStr: "${jsapi.noncestr}", 
	    signature: "${jsapi.signature}",
	    jsApiList: ['requestAttributes.checkJsApi', 'chooseWXPay']
	});	

	wx.ready(function(){
		wx.chooseWXPay({
		    timestamp: "${payInfo.timeStamp}", 
		    nonceStr: "${payInfo.nonceStr}", 
		    package: "${payInfo.packageInfo}", 
		    signType: "${payInfo.signType}",
		    paySign: "${payInfo.paySign}", 
		    success: function (res) {
		    	WeixinJSBridge.log(res.err_msg);
			    layer.open({
	    		    content: "支付成功"
	    		    ,skin: 'msg'
	    		    ,time: 2 
	    		});
	    		if(!res.err_msg){
	    			location.href="${requesturl}";
	    		}

	    		//跳转地址
			   // location.href="<%=basePath%>/weixin/topayShow";			    				        
			 },
			 cancel:function(){
			 	layer.open({
	    		    content: "用户取消支付"
	    		    ,skin: 'msg'
	    		    ,time: 2 
	    		});
	    		window.history.go(-1);
			 }			    
		});	
	});
</script>

<body>
<%-- <c:choose>
<c:when test="${!isfinish}">	
	<span style="font-size:14px;">金额：</span>
	<input id="price" name="price" type="text" value="1"/><br/><br/>
	<button onclick="topay()">发起支付</button>	
</c:when>
<c:otherwise>
	<h3>支付完成，支付金额为：${money}</h3>
</c:otherwise>
</c:choose> --%>


	
</body>
</html>
