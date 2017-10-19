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
<script src="${oklongStatic}/js/h5Pay3.js"></script>
<title>好邻邦云社区-物业缴费</title>

<script type="text/javascript">
	var feetype = ${feetype};
	var WYID = ${WYID};
	var HTID = ${HTID};
	var JFYF = ${JFYF};
	var source = "${source}";
	var SIGN = "${SIGN}";

	$(document).ready(function(){		
		
	});
</script>
<style type="text/css">
	.paymentList li a.ab:before{background-image:url(${oklongStatic}/js/ab.png)}
</style>
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
			<dt>商户名称</dt>
			<dd class="toggleItem1">${property}</dd>
		</dl>
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
	<div class="h3" style="margin-bottom: 7px;font-size: 14px;">支付方式</div>
	<c:forEach items="${accountList}" var="account">
	<c:if test="${account.status==1 && account.platsystem==0}">
	<ul class="paymentList">
		<li class="hasDesc">
			<a onclick="abpay('${order.cporderid}', '${account.appid}', '${account.appuserid}', '${account.WYID}')" class="ab topay">
				<p><em> 爱贝支付 </em></p>
				<p class="desc">使用爱贝平台的聚合支付</p> 
			</a>
		</li>
	</ul>
</c:if>	
<c:if test="${account.status==1 && account.platsystem==1}">
	<ul class="paymentList">
		<li class="hasDesc">
			<a onclick="wxpay('${order.cporderid}', '${account.appid}', '${account.appuserid}', '${account.WYID}')" class="weixin topay">
				<p><em> 微信支付 </em></p>
				<p class="desc">推荐安装微信6.0.2及以上版本的用户使用</p> 
			</a>
		</li>
	</ul>
</c:if>	
<c:if test="${account.status==1 && account.platsystem==2}">
	<!-- <div class="h3">请选择支付方式</div> -->
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
			<li style="left:72px;">联系电话：0755-26503033 微信公众号：好邻邦云社区&nbsp;&nbsp;</li>
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
<div class="h3" style="margin-top: 7px;font-size: 14px;">技术支持</div>
<div class="main clearfix size12 marginT10" style="text-align: center;">
	<p class="floatleft gray" style="width: 100%;line-height: 1.3em;">
		深圳市好邻邦科技有限公司
		<!-- <a href="#" class="marginL10 blue">服务中心</a> -->
	</p>
	<p class="floatleft gray" style="line-height: 1.3em;width: 100%;">
		0755-26503033 
	</p>
</div>

</body>
</html>