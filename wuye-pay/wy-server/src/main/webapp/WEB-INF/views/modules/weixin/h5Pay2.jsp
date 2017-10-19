<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/bftags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${oklongStatic}/js/style.css">
<script type="text/javascript" src="${ctxStatic}/js/jquery.min.js"></script>
<script type="text/javascript" src="${oklongStatic}/js/common.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${ctxStatic}/js/layer.js"></script>
<title></title>

<script type="text/javascript">
	$(document).ready(function(){		
		//参数配置
		<c:if test="${isredirect eq 'true'}">
		wxpay();
		</c:if>
	});
	
	//发起微信支付
	function wxpay(){		
		
		//首先预授权获取,再次回调就不需要支付了	
		<c:if test="${isredirect ne 'true'}">		
		window.location.href="/wy-server/weixin/oAuth2.htm";	
		return;	
		</c:if>
		//获取初始化参数
		var currUrl=getRequestUrl();
		var jsapi=null;
		$.ajax({
			url : '/wy-server/weixin/jssdk.htm',
			type : 'POST',
			data : {"url" : currUrl},
			async : false,
			dataType : 'json',
			success : function(data) {				
				jsapi=data.data;				
			},
			error : function(e) {
				
			}
		});
	
		console.info(jsapi);	
	
		if(jsapi!=null){
			//初始化微信接口		
			wx.config({
			    debug: false,
			    appId: jsapi.appid, 
			    timestamp: jsapi.timestamp, 
			    nonceStr: jsapi.noncestr, 
			    signature: jsapi.signature,
			    jsApiList: ["chooseWXPay"]
			});
		}else{
			alert("支付错误");
			return;		
		}		
	
		//更新订单数据
		$.ajax({
			url : '/wy-server/getTransId/updatePlat.htm',
			type : 'POST',
			data : {"platsystem" : "1"},
			async : false,
			dataType : 'json',
			success : function(data) {
				
			},
			error : function(e) {
				
			}
		});
		
		//获取支付订单信息
		var payInfo=null;
		$.ajax({
			url : '/wy-server/weixin/payInfo.htm',
			type : 'POST',
			data : {"url" : currUrl},
			async : false,
			dataType : 'json',
			success : function(data) {
								
				payInfo=data.data;				
			},
			error : function(e) {
				
			}
		});
		
		if(payInfo==null){
			alert("获取支付信息错误");
			return;	
		}
		wx.chooseWXPay({
		    timestamp: payInfo.timeStamp, 
		    nonceStr: payInfo.nonceStr, 
		    package: payInfo.packageInfo, 
		    signType: payInfo.signType,
		    paySign: payInfo.paySign, 
		    success: function (res) {
			    layer.open({
	    		    content: "支付成功"
	    		    ,skin: 'msg'
	    		    ,time: 2 
	    		});
	    		//支付成功跳转地址
			   window.location.href = payInfo.requesturl;			    				        
			 },
			 cancel:function(){
			 	layer.open({
	    		    content: "用户取消支付"
	    		    ,skin: 'msg'
	    		    ,time: 2 
	    		});
			 }			    
		});	
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
</script>
</head>
<body>
	<header class="clearfix">
		<!-- <a class="right" href="#">个人中心</a> -->
		<span class="title">收银台</span>
	</header>
	<div class="errTop" style="display:none;">
		<span class="errBody"></span><a href="javascript:void(0);"
			class="close"></a>
	</div>
	<!-- 普通收银台 -->
	<!--商品信息 -->
	<div class="main bill margin5">
		<dl class="clearfix">
			<dt>商品名称</dt>
			<dd class="toggleItem1">${wares.waresname}</dd>
		</dl>
		<dl>
			<dt>应付金额</dt>
			<dd>
				<span class="red">${order.price}</span>元
			</dd>
		</dl>
	</div>
	<!--选择支付方式-->
	<input type="hidden" id="IgnoreLast">
	<div class="h3">常用支付方式</div>
<c:forEach items="${accountList}" var="account">
<c:if test="${account.status==1 && account.platsystem==1}">
	<ul class="paymentList">
		<li class="hasDesc">
			<a href="javascript:void(0);" onclick="wxpay()" class="weixin topay">
				<p><em> 微信支付 </em></p>
				<p class="desc">推荐安装微信6.0.2及以上版本的用户使用</p> 
			</a>
		</li>
	</ul>
</c:if>	
<c:if test="${account.status==1 && account.platsystem==2}">
	<div class="h3">请选择支付方式</div>
	<!-- 支付方式列表 -->
	<ul class="paymentList">
		<li class="hasDesc">
			<a href="javascript:void(0);" onclick="alipay()" class="zfb topay">
				<p><em> 支付宝 <b>官方推荐</b></em></p>
				<p class="desc">推荐有支付宝账号的用户使用</p> 
			</a>
		</li>
	</ul>
</c:if>	
</c:forEach>	
	<!-- 页尾系统公告 -->
	<div class="acBox">
		<ul>
			<li style="left:72px;">联系电话：0755-26503033 微信公众号：好邻邦社区&nbsp;&nbsp;</li>
		</ul>
		<i class="close"></i>
	</div>
<script type="text/javascript">
$(function(){
	// 系统公告滚动显示
	var $acBox = $('.acBox');
	var $acBox_ul = $acBox.find('ul');
	var acBox_ul_w = $acBox_ul.width();
	var $acBox_li = $acBox.find('li');
	var acBox_li_w = $acBox_li.width();
	var achtml = $acBox_li.html();
	// $acBox_li.html(achtml + achtml);
	var timeAc = null;
	var acBoxLeft = 0;
	function acBoxScroll(){
		if(acBoxLeft < acBox_li_w){
			acBoxLeft += 1;
		}else{
			acBoxLeft = -acBox_ul_w;
		}
		$acBox_li.css('left',-acBoxLeft);
		timeAc = setTimeout(acBoxScroll,20);
	}
//	if(acBox_li_w > acBox_ul_w){
		timeAc = setTimeout(acBoxScroll,1000);
//	}

	// 关闭公告
	$acBox.find('.close').click(function(){
		clearTimeout(timeAc);
		$acBox.remove();
	});
});
</script>
<div class="main clearfix size12 marginT10">
	<p class="floatleft gray">
		联系电话：0755-26503033 
		<a href="#" class="marginL10 blue">服务中心</a>
	</p>
</div>

</body>
</html>