$(function(){
	getParameter();
	$('#btnSubmit').click(function(){
		var id = $("#id").attr("value");
		var descr = $("#descr").attr("value");
		var imgSrc = $("#imgSrc").attr("src");
		var href = $("#href").attr("value");
		var statue = $("#statue").attr("value");
		var WYID = $("#WYID").attr("value");
		var createtime = $("#createtime").attr("createtime");
		var updatetime = $("#updatetime").attr("updatetime");
		var parameter = {id:id,descr:descr,imgSrc:imgSrc,href:href,statue:statue,WYID:WYID,createtime:createtime,updatetime:updatetime};

		jQuery.ajax({
			url : 'updateAd.htm',
			data : parameter,
			type : 'post',
			success:function(data){
				if(data == true){
					parent.$('#win').window('close');//关闭应用信息填写窗口
			    	parent.$('#list').datagrid("reload");//重新载入表格数据
			    	parent.parent.$.messager.show({
						title:"系统消息",
						msg:"修改成功！"
					});
				}else{
					parent.$('#win').window('close');//关闭应用信息填写窗口
			    	parent.$('#list').datagrid("reload");//重新载入表格数据
			    	parent.parent.$.messager.show({
						title:"系统消息",
						msg:"修改失败！"
					});
				}
			},
			error:function(){
					jQuery.messager.alert("警告","出现异常，若还有问题请与管理员联系！");
			}
		});
    });
});
	
//获取表单数据
function getParameter(){
	var row = parent.currentSelectRow;
	var id = $("#id").val(row.id);
	var descr = $("#descr").val(row.descr);
	var imgSrc = $("#image").attr("src",row.imgSrc);
	var href = $("#href").val(row.href);
	var statue = $("#statue").val(row.statue);
	var WYID = $("#WYID").val(row.wyid);
	var updatetime = $("#updatetime").val(row.updatetime);
	var createtime = $("#createtime").val(row.createtime);

	
	var parameter = {
			id:id,
			descr:descr,
			imgSrc:imgSrc,
			href:href,
			statue:statue,
			WYID:WYID,
			updatetime:updatetime,
			createtime:createtime
	};
	return parameter;

}