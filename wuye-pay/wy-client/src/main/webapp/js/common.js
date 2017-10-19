function getSIGN(param){
	var sign ;
	
	$.ajax({
		url : "/wy-server/htmlAction/getSIGN.htm",
		type : "post",
		data : param,
		async : false,
		dataType : "json",
		success:function(data){
			sign = data;
		},
		error:function(jqXHR, textStatus, errorThrown){
			alert("加密失败");
		}
	});
	return sign;
}

//from代表   0:微信公众号，1：二维码
function selectMode(from,param,feetype){
	var url;
	//alert("from:"+from+" param:"+param+" feetype:"+feetype);
	if(feetype==0){
		url = "index.html?"+ 
		"x0="+feetype+"&x1="+param.WYID+"&x2="+param.HTID+"&x3="+param.JFYF+"&x4="+param.SOURCE+"&SIGN="+param.SIGN+"&fromtype="+from;
	}else if(feetype==1){
		url = "index1.html?"+ 
		"x0="+feetype+"&x1="+param.WYID+"&x2="+param.HTID+"&x3="+param.JFYF+"&x4="+param.SOURCE+"&SIGN="+param.SIGN+"&fromtype="+from;
	}else if(feetype==2){
		url = "index2.html?"+ 
		"x0="+feetype+"&x1="+param.WYID+"&x2="+param.HTID+"&x3="+param.JFYF+"&x4="+param.SOURCE+"&SIGN="+param.SIGN+"&fromtype="+from;
	}
	
	//salert(url);
	
	if(from=="0"){
		document.location.href = url;
	}else if(from=="1"){
		var QRCode = {};
		QRCode.url = url;
		$.ajax({
			url : "/wy-server/QRCode/getQRCode.htm",
			type : "post",
			data : QRCode,
			async : false,
			success : function (data){
				sessionStorage.setItem("QRCodeImg",data);
				window.location.href="/wy-client/QRCode.html"
			},
			error : function (){
				alert("无法生成二维码");
			}
		});
	}else{
		alert("fromtype is error");
	}
}

function adjust() {
	var width = document.body.offsetWidth;
	$("#adsmain").attr("style", "width:"+width+";height:"+width/2);
	$("#adsmyslide").attr("style", "float: left;height: "+width/2+";width: "+width+";position: absolute;overflow: hidden;");
	$("#ads").attr("style", "position: absolute;width:"+width*100);
	$("#ads a").attr("style", "float: left;");
	$("#ads a img").attr("style", "float: left;width:"+width+";height:"+width/2);
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

function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) {
		return unescape(r[2]);
	}
	return null; 
}