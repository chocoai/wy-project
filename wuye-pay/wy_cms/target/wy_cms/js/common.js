/*//应用上下文路径
var SERVLET_CONTEXT_PATH = "/vv_cms";*/

//图片基本目录路径
var BASE_DIR_PATH = 'upload/';//"home/wwwroot/user_data/image/";

/*//获取rest保存产品路径
var REST_SAVE = SERVLET_CONTEXT_PATH+"/rest/";

//获取rest更新产品路径
var REST_UPDATE = REST_SAVE;
*/


//允许上传的图片格式
var PIC_SUFFIX = new Array("bmp","jpg","png","tiff","gif","pcx","tga","exif","fpx","svg","psd","cdr","pcd","pcd","ufo","eps","ai","raw");

//校验图片格式有效性
function picSuffixVerify(fileName)
{
console.info(fileName);
	if(fileName != "")
	{
        fileName = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length);
		for(var i = 0; i < PIC_SUFFIX.length; i++)
		{

			if(fileName.toLocaleLowerCase() == PIC_SUFFIX[i])
			{
				return true;
			}
		}
	}
	return false;
}


function formatNum(amt,pre) {
	 pre = pre > 0 && pre <= 20 ? pre : 2;
	 
	 amt = parseFloat((amt + "").replace(/[^\d\.-]/g, "")).toFixed(pre) + "";
	 var left = amt.split(".")[0].split("").reverse();
	 
	 var right = amt.split(".")[1];
	 
	 var t = "";
	 for(var i = 0; i < left.length; i ++ ) {
	  t += left[i] + ((i + 1) % 3 == 0 && (i + 1) != left.length ? "" : "");
	 }
	 return t.split("").reverse().join("") + "." + right;
}
/**
 * 效验图片有效性
 * fileName 要校验的文件名
 * AllowImgWidth 指定图片最大宽度px
 * AllowImgHeight 指定图片最大高度px
 * AllowImgFileSize 指定图片最大KB 
 */
function picVerify(fileName,AllowImgWidth,AllowImgHeight,AllowImgFileSize)
{

	if(fileName == "")
	{
		$.messager.alert("提示信息","请选择图片");
		return false;
	}
	if(!picSuffixVerify(fileName))
	{
		$.messager.alert("提示信息","无效的图片");
		return false;
	}
	
	/*
	var ErrMsgErrMsg = "";//错误信息
	var img=new Image();
	img.src=fileName;

	if(AllowImgWidth != "" && img.width > AllowImgWidth){
	ErrMsgErrMsg="\n\n图片宽度超过限制 请上传宽度小于"+AllowImgWidth+"px的文件，当前图片宽度为"+img.width+"px"; 
	jQuery.messager.alert(ErrMsgErrMsg); 
	return false;
	}
	if(AllowImgHeight != "" && img.height > AllowImgHeight){
	ErrMsgErrMsg="\n\n图片高度超过限制 请上传高度小于"+AllowImgHeight+"px的文件，当前图片高度为"+img.height+"px"; 
	jQuery.messager.alert(ErrMsgErrMsg);
	return false;
	}

	var size = formatNum(img.fileSize / 1024,2);
	if(AllowImgFileSize != "" && size > AllowImgFileSize)  {
	ErrMsgErrMsg = "\n\n图片文件大小超过限制 请上传小于"+AllowImgFileSize+"KB的文件，当前文件大小为"+size+"KB"; 
	jQuery.messager.alert(ErrMsgErrMsg);
	return false;
	}*/
	return true;
}

/*
 * 获取全部对象时候 后台返回的json需要处理
 * [list:{{id:0},{id:1}}] 转换成  [{id:0},{id:1}]
 */
function getList(url){
	var list = [];
	$.ajax({
		type:'GET',
		async:false,
		url:url,
		success:function(data){
			list = data.list;
		}
	});
	return list;
}

var type = "^[0-9]*$";
var re = new RegExp(type);
/**
 * 数字验证
 * @param value 需要验证的数字
 * @param passNull	是否允许为空 true 可以为空
 * @returns
 */
function verifyNumber(value,passNull){
	return passNull ? (value == "" || value.match(re)!=null) : (value!= "" && value.match(re)!=null);
}

/**
 * @author linzq
 * 
 * 格式化日期时间
 * 
 * @param format
 * @returns
 */
Date.prototype.format = function(format) {
	if (isNaN(this.getMonth())) {
		return '';
	}
	if (!format) {
		format = "yyyy-MM-dd hh:mm:ss";
	}
	var o = {
		/* month */
		"M+" : this.getMonth() + 1,
		/* day */
		"d+" : this.getDate(),
		/* hour */
		"h+" : this.getHours(),
		/* minute */
		"m+" : this.getMinutes(),
		/* second */
		"s+" : this.getSeconds(),
		/* quarter */
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		/* millisecond */
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

/**
 * 获取时间差
 * 时间格式必须是 yyyy-MM-dd HH:mm:ss
 * @param beginTime 开始时间
 * @param endTime 	结束时间
 * @returns {Number}返回(endTime-beginTime)毫秒数
 */
function comptime(beginTime,endTime) {
    var beginTimes = beginTime.substring(0, 10).split('-');
    var endTimes = endTime.substring(0, 10).split('-');
    beginTime = beginTimes[1] + '/' + beginTimes[2] + '/' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
    endTime = endTimes[1] + '/' + endTimes[2] + '/' + endTimes[0] + ' ' + endTime.substring(10, 19);
    return (Date.parse(endTime) - Date.parse(beginTime));
}

//上传图片
$(function(){
	
	if($(".form4UploadPic").attr('class')){
		//下面两个属性用来获取 显示图片 的元素 id 跟存储 地址的元素 id
		var picKey = null;
		var imgKey = null;
		//上传图片
		$(".form4UploadPic").form({
			//url:'uploadRoomPic.htm',
			onSubmit:function(){
				picKey = $(this).attr('picKey');
				imgKey = $(this).attr('imgKey');
			},
			success:function(data){
		    	var $result = eval("("+data+")");
		    	var status = $result.status;
		    	var value = $result.value;
		    	var title = "上传结果";
		    	var context = "";
		    	if(status == 0)
		    	{
		    		var src = BASE_DIR_PATH+value;
		    		context = "图片上传成功";
		    		//context = "图片路径:"+src;
		    		
		    		$("#"+picKey).val(value);
		    		$("#"+imgKey).attr('src','../'+src);
		    	}else
		    	{
		    		context = "图片上传失败";
		    		//context = "失败原因:"+value;
		    	}
		    	
		    	$.messager.show(
		    	{
		    		title:title,
		    		msg:context
		    	});
		    },
			error:function(){
				jQuery.messager.alert("警告","出现异常，若还有问题请与管理员联系！");
			}  
		});
	}
	
});



//获取专区信息
function getZhuanquInfo(){
	var zhuanquInfos = null ;
	$.ajax({
		url:"../zq/getZhuanquInfo.htm",
		async:false,
		success:function(result){
			if(eval("("+result.data+")").retCode == 1000){
				var data = eval("("+result.data+")");
				zhuanquInfos = data.zhuanquInfo;
			}else{
				var data = eval("("+result.data+")");
				zhuanquInfos = data.retMsg;
			}
		}
	},"json");
	return zhuanquInfos;
}
/*********************************************************************/
/**
 * VV工具包
 */
function VV(){
	
};

/**
 * 封装 异步执行：增删改查，功能
 * @param url		操作地址
 * @param parameter	操作参数
 * @param backFn	回调函数
 */
VV.execute = function execute(url,parameter,backFn){
	$.ajax({
		url:url,
		data:parameter,
		dataType:'json',
		success:function(result){
			backFn(result);
		},
		error:function(result){
			$.messager.alert('系统错误','请联系管理员！'+result.statusText);
			//backFn(result);
		}
	});
};














