$(function(){
	var offsetWidth = document.body.offsetWidth;//网页可见区域宽 (包括边线的宽)
	var offsetHeight =  document.body.offsetHeight;//网页可见区域高(包括边线的高)
	var editRow = undefined; //定义全局变量：当前编辑的行
	var datagrid; //定义全局变量datagrid

	datagrid = $("#list").datagrid({
		url:'statistics/index.htm',
		pagination:true,
		height:offsetHeight-150,
		sortName:'',
		sortOrder:'desc',
		columns:[[
			{field:'date',title:'日期',width:100,halign:'center',algin:'center',sortable:false,editor:"text"},
			{field:'count',title:'总金额',width:100,halign:'center',algin:'center',sortable:false,editor:"text"},
			{field:'number',title:'交易笔数',width:100,halign:'center',algin:'center',sortable:false,editor:"text"},
			]],
		toolbar : [
			{ 
				text: '金额直方图', handler: function(){
					window.open('histogram.html?type=value');
				}
			},
			'-',
			{
				text: '笔数直方图', handler: function(){
					window.open('histogram.html?type=num');
				}
			}
		],
	});
	
	/*
	 * 查询事件
	 */
	$("#daybtnSearch").click(function(){
		var begintime = $("#begintime").combobox('getValue');
		var endtime = $("#endtime").combobox('getValue');
		/*var platsystem = $("#platsystem").combobox('getValue');*/
		var platsystem = "";
		$("input:checkbox[name='platsystem']:checked").each(function(){ 
			platsystem+=$(this).val()+","; 
		});
		var parameters = {
			begintime : begintime,
			endtime : endtime,
			platsystem : platsystem,
			type : "day"
		};
		$("#list").datagrid('load',parameters);
	});

	$("#monthbtnSearch").click(function(){
		var begintime = $("#begintime").combobox('getValue');
		var endtime = $("#endtime").combobox('getValue');
		/*var platsystem = $("#platsystem").combobox('getValue');*/
		var platsystem = "";
		$("input:checkbox[name='platsystem']:checked").each(function(){ 
			platsystem+=$(this).val()+","; 
		});
		var parameters = {
			begintime : begintime,
			endtime : endtime,
			platsystem : platsystem,
			type : "month"
		};
		$("#list").datagrid('load',parameters);
	});
	/*
	 * 重置事件
	 */
	$("#btnReset").click(function(){
		$("#queryForm").form('clear');
	});
	
	/*$("#begintime").datetimebox({
		required : false,
		showSeconds : false
	});

	$("#endtime").datetimebox({
		required : false,
		showSeconds : false
	});*/
	
	/*$("#platsystem").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "爱贝"
		}, {
			"value" : "1",
			"name" : "好邻邦微信"
		}],
		valueField : 'value',
		textField : 'name'
	});*/
	$("#begintime").datebox({
		required : false
	});

	$("#endtime").datebox({
		required : false
	});
	
});
