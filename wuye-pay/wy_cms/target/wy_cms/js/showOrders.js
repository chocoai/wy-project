
$(function() {
	var editRow = undefined;
	var offsetWidth = document.body.offsetWidth;// 网页可见区域宽 (包括边线的宽)
	var offsetHeight = document.body.offsetHeight;// 网页可见区域高(包括边线的高)

	var datagrid = $("#list").datagrid({
		url : 'order/getOrder.htm',
		pagination : true,
		height : offsetHeight - 150,
		sortName : 'download_time',
		sortOrder : 'desc',
		columns : [ [ {
			field : 'id',
			title : 'ID',
			width : 80,
			halign:'center',
			sortable : false,
			checkbox : true
		}, {
			field : 'source',
			title : '来源',
			width : 60,
			halign:'center',
			sortable : false
		}, {
			field : 'appid',
			title : '应用ID',
			width : 80,
			halign:'center',
			algin:'right',
			sortable : false
		}, {
			field : 'waresid',
			title : '商品ID',
			width : 50,
			halign:'center',
			sortable : false
		}, {
			field : 'appuserid',
			title : '用户账号',
			halign:'center',
			width : 130,
			sortable : false
		}, {
			field : 'jfyf',
			title : '计费月份',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false,
			editor:"text"
		},{
			field : 'wyid',
			title : '物业ID',
			halign:'center',
			align:'right',
			width : 50,
			sortable : false
		},{
			field : 'property',
			title : '物业名称',
			halign:'center',
			align:'left',
			width : 100,
			sortable : false
		},{
			field : 'building',
			title : '大楼名称',
			halign:'center',
			align:'left',
			width : 60,
			sortable : false
		},{
			field : 'room',
			title : '房间号',
			halign:'center',
			align:'left',
			width : 50,
			sortable : false
		},{
			field : 'skid',
			title : '销账状态',
			halign:'center',
			align:'left',
			width : 60,
			sortable : false,
			editor:"text"
		},{
			field : 'cporderid',
			title : '订单ID',
			halign:'center',
			width : 150,
			sortable : false
		},{
			field : 'createtime',
			title : '订单创建时间',
			halign:'center',
			align:'center',
			width : 150,
			sortable : false
		}, {
			field : 'price',
			title : '交易额',
			halign:'center',
			align:'right',
			width : 60,
			sortable : false,
			formatter:function(val,rowData,rowIndex){
				if((typeof val)== 'string'){
					val = parseFloat(val);
				}
		        if(val!=null)
		            return val.toFixed(2);
		   },
		   editor:"text"
		}, {
			field : 'result',
			title : '交易结果',
			halign:'center',
			align:'center',
			width : 70,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				if (value == 0)
					return "交易成功";
				if (value == 1)
					return "交易失败";
				if (value == 2)
					return "未交易";
			},
			editor:"text"
		}, {
			field : 'orderstatus',
			title : '订单状态',
			halign:'center',
			align:'center',
			width : 70,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				if (value == 0)
					return "支付成功";
				if (value == 1)
					return "支付失败";
				if (value == 2)
					return "待支付";
				if (value == 3)
					return "正在处理";
				if (value == 4)
					return "系统异常";
			},
			editor:"text"
		}, {
			field : 'transid',
			title : '交易流水ID',
			halign:'center',
			align:'center',
			width : 150,
			sortable : false
		}, {
			field : 'transtime',
			title : '交易时间',
			halign:'center',
			align:'center',
			width : 150,
			sortable : false,
			editor:"text"
		}, {
			field : 'htid',
			title : '合同ID',
			halign:'center',
			align:'center',
			width : 50,
			sortable : false
		}, 
		// {field:'paystatus',title:'好邻邦支付状态',width:100,sortable:false,formatter:function(value,rowData,rowIndex){
		// if(value == 8) return "微未付";
		// if(value == 9) return "微已付";
		// }},

		{
			field : 'updatetime',
			title : '最后更新时间',
			halign:'center',
			align:'center',
			width : 150,
			sortable : false
		}, {
			field : 'feetype',
			title : '计费类型',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false,
			editor:"text"
		}, {
			field : 'currency',
			title : '货币类型',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false
		}, {
			field : 'transtype',
			title : '交易类型',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				if (value == 0)
					return "支付交易";
				if (value == 1)
					return "支付冲正";
				if (value == 2)
					return "契约退订";
				if (value == 3)
					return "自动续费";
			}
		}, {
			field : 'paytype',
			title : '支付类型',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				/*if (value == 1)
					return "充值卡";
				if (value == 2)
					return "游戏点卡";
				if (value == 4)
					return "银行卡";*/
				if (value == 401)
					return "支付宝";
				/*if (value == 402)
					return "财付通";
				if (value == 501)
					return "支付宝网页";
				if (value == 502)
					return "财付通网页";*/
				if (value == 403)
					return "微信支付";
				/*if (value == 5)
					return "爱贝币";
				if (value == 6)
					return "爱贝一键支付";
				if (value == 16)
					return "百度钱包";
				if (value == 30)
					return "移动话费";
				if (value == 31)
					return "联通话费";
				if (value == 32)
					return "电信话费";*/
			},
			editor:"text"
		}, {
			field : 'bzidlist',
			title : '计费项目ID',
			halign:'center',
			width : 150,
			sortable : false
		}, {
			field : 'feiList',
			title : '预收款',
			halign:'center',
			width : 250,
			sortable : false
		}, {
			field : 'platsystem',
			title : '支付平台',
			halign:'center',
			align :'center',
			width : 80,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				switch(value){
	                case "0":return "爱贝";
	                case "1":return "好邻邦微信";
	                case "2":return "好邻邦支付宝";
				}
			}
		}, {
			field : 'offline',
			title : '线下',
			halign:'center',
			align:'center',
			width : 60,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				if(value==null || value==0){
					return "否";
				}else{
					return "是";
				}
			}
		}, {
			field : 'customername',
			title : '用户备注',
			width : 80,
			halign:'center',
			sortable : false
		}, {
			field : 'from',
			title : '来源',
			halign:'center',
			align :'center',
			width : 80,
			sortable : false,
			formatter : function(value, rowData, rowIndex) {
				switch(value){
	                case "0":return "微信公众号";
	                case "1":return "触摸屏";
	                case "2":return "通知单";
	                case "3":return "短信";
	                case "4":return "客显屏";
	                case "5":return "扫码铭牌";
	                case "6":return "App";
				}
			}
		}
		] ],
		toolbar : [ {
			text : '销账',
			iconCls : 'icon-ok',
			handler : function() {

				var rows = $("#list").datagrid("getSelections");
				// 选择要销账的行
				if (rows.length > 0) {
					$.messager.confirm("Confirm", "确定销账?", function(r) {
						if (r) {
							var ids = new Array();
							for (var i = 0; i < rows.length; i++) {
								console.info(rows[i].id);
								ids.push(rows[i].id);
							}
							// 将选择到的行存入数组并用,分隔转换成字符串，
							// 本例只是前台操作没有与数据库进行交互所以此处只是弹出要传入后台的id
							// alert(ids.join(','));

							// 执行后台销账操作
							jQuery.ajax({
								url : "order/writeoff.htm",
								data : "ids=" + ids,
								type : "post",
								success : function(data) {
									parent.$.messager.show({
										title : "系统消息",
										msg : data
									});

									$('#list').datagrid("reload");
								},
								error : function() {
									alert("系统错误！");
								}
							});
						}
					});
				} else {
					$.messager.alert("Confirm", "请至少选择一行!", "error");
				}
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {

				var rows = $("#list").datagrid("getSelections");
				// 选择要销账的行
				if (rows.length > 0) {
					$.messager.confirm("Confirm", "确定删除废单?", function(r) {
						if (r) {
							var ids = new Array();
							for (var i = 0; i < rows.length; i++) {
								console.info(rows[i].id);
								ids.push(rows[i].id);
							}
							// 将选择到的行存入数组并用,分隔转换成字符串，
							// 本例只是前台操作没有与数据库进行交互所以此处只是弹出要传入后台的id
							// alert(ids.join(','));

							// 执行后台销账操作
							jQuery.ajax({
								url : "order/deleteOrder.htm",
								data : "ids=" + ids,
								type : "post",
								success : function(data) {
									parent.$.messager.show({
										title : "系统消息",
										msg : data
									});

									$('#list').datagrid("reload");
								},
								error : function() {
									alert("系统错误！");
								}
							});
						}
					});
				} else {
					$.messager.alert("Confirm", "请至少选择一行!", "error");
				}
			}
		},'-', {
			text : '快捷删除三十天前的废单',
			iconCls : 'icon-remove',
			handler : function() {
				$.messager.confirm("Confirm", "确定删除三十天前废单?", function(r) {
					if (r) {
							jQuery.ajax({
								url : "order/quicklyRemove.htm",
								type : "post",
								success : function(data) {
									parent.$.messager.show({
										title : "系统消息",
										msg : data
									});

									$('#list').datagrid("reload");
								},
								error : function() {
									alert("系统错误！");
								}
							});
					}
			});
			}
		}, '-', { 
			text: '修改', iconCls: 'icon-edit', handler: function(){
				//修改时要获取选择到的行
				var rows = datagrid.datagrid("getSelections");
				//如果只选择了一行则可以进行修改，否则不操作
				if (rows.length == 1) {
					//修改之前先关闭已经开启的编辑行，当调用endEdit该方法时会触发onAfterEdit事件
					$("#list").attr("flag","update");
					if (editRow != undefined) {
						datagrid.datagrid("endEdit", editRow);
					}
					//当无编辑行时
					if (editRow == undefined) {
						//获取到当前选择行的下标
						var index = datagrid.datagrid("getRowIndex", rows[0]);
						//开启编辑
						datagrid.datagrid("beginEdit", index);
						//把当前开启编辑的行赋值给全局变量editRow
						editRow = index;
						//当开启了当前选择行的编辑状态之后，
						//应该取消当前列表的所有选择行，要不然双击之后无法再选择其他行进行编辑
						datagrid.datagrid("unselectAll");
					}
				}
			}
		}, '-', { 
			text: '保存', iconCls: 'icon-save', handler: function(){
				//保存时结束当前编辑的行，自动触发onAfterEdit事件如果要与后台交互可将数据通过Ajax提交后台
				datagrid.datagrid("endEdit", editRow);
			}
		}, '-', { 
			text: '取消编辑', iconCls: 'icon-redo', handler: function(){
				//取消当前编辑行把当前编辑行罢undefined回滚改变的数据,取消选择的行
				editRow = undefined;
				datagrid.datagrid("rejectChanges");
				datagrid.datagrid("unselectAll");
			}
		}, '-', { 
			text: '更新房号', handler: function(){
				var data = $("#list").datagrid("getSelected");
				if (data != null) {
					$.messager.confirm("Confirm", "确定更新房号?", function(r) {
						if (r) {
							$.ajax({
								url:'order/updateRoomInfo.htm',
								data:data,
								success : function(data){
									var msg = "";
									if(data == true){
										msg = "更新成功";
										$('#list').datagrid("reload");
									}else{
										msg = "更新失败"
									}
									parent.$.messager.show({
										title : "系统消息",
										msg : msg
									});
								}
							})
						}
					})
				}
			}
		},'-', { 
			text: '导出到Excel', handler: function(){
					$.messager.confirm("Confirm", "确定导出到Excel?", function(r) {
						if (r) {
							var source = $("#source").val();
							var WYID = $("#WYID").val();
							var JFYF = $("#JFYF").val();
							var platsystem = $("#platsystem").combobox('getValue');
							var offline = $("#offline").combobox('getValue');
							var chargestatus = $("#chargestatus").combobox('getValue');
							var orderstatus = $("#orderstatus").combobox('getValue');
							var begintime = $("#begintime").combobox('getValue');
							var endtime = $("#endtime").combobox('getValue');
							var from = $("#from").combobox('getValue');
							
							var parameters = {
								source : source,
								WYID : WYID,
								JFYF : JFYF,
								platsystem : platsystem,
								offline : offline,
								chargestatus : chargestatus,
								orderstatus : orderstatus,
								begintime : begintime,
								endtime : endtime,
							};
							$.ajax({
								url:'order/getExcelDate.htm',
								data : parameters,
								type : "post",
							})
							window.open('order/exportExcel.htm');
						}
					})
				}
			}
		],
		onLoadSuccess : function() {
			var totalPrice = 0;
			var amount = 0;
			var grid = $(".datagrid-toolbar"); // datagrid
			var extotal = $("#extool");
			grid.append(extotal);
//			jQuery.ajax({
//				url : "order/getCount.htm",
//				// data : "ids=" +ids,
//				type : "post",
//				success : function(data) {
//					var result = eval('(' + data + ')');
//					$("#price").html(result.price);
//					$("#amount").html(result.total);
//				},
//				error : function() {
//					alert("系统错误！");
//				}
//			});

		},
		onAfterEdit: function (rowIndex, rowData, changes) {
			//console.info(rowData);
			var flag = $("#list").attr("flag");
			var url ;
			if(flag == "update"){
				url = "order/updateOrder.htm";
			}
			//endEdit该方法触发此事件
			editRow = undefined;
			jQuery.ajax({
				url : url,
				data : rowData,
				type : "post",
				success : function(data){
					//parent.$('#win').window('close');//关闭应用信息填写窗口
					//parent.$('#list').datagrid("reload");//重新载入表格数据
					parent.$.messager.show({
						title:"系统消息",
						msg:"操作成功！"
					});
					$("#list").attr("flag","");
					$('#list').datagrid("reload");
				},
				error : function(){
					alert("System error！");
				}
			});
		},
	// onCheck:function(index,row){
	// var totalPrice = parseFloat($("#price").html());
	// var amount = parseInt($("#amount").html());
	// if(index>-1){
	// totalPrice += row.price;
	// amount++;
	// }
	// $("#price").html(totalPrice.toFixed(2))
	// $("#amount").html(amount);
	// },
	// onUncheck: function(index,row){
	// var totalPrice = parseFloat($("#price").html());
	// var amount = parseInt($("#amount").html());
	// if(index>-1){
	// totalPrice -= row.price
	// amount--;
	// }
	// $("#price").html(totalPrice.toFixed(2))
	// $("#amount").html(amount);
	// },
	// onCheckAll: function(rows){
	// var totalPrice=0;
	// var amount = rows.length;
	// if(rows.length>0){
	// for(var i=0;i<rows.length;i++){
	// totalPrice+=rows[i].price;
	// }
	// }
	// $("#price").html(totalPrice.toFixed(2));
	// $("#amount").html(amount);
	// },
	// onUncheckAll: function(rows){
	// var totalPrice = 0;
	// if(rows.length>0){
	// $("#price").html(totalPrice.toFixed(2))
	// $("#amount").html(0);
	// }
	// }
	});

	/*
	 * 查询事件
	 */
	$("#btnSearch").click(function() {
		var appid = $("#appid").val();
		var waresid = $("#waresid").val();
		var appuserid = $("#appuserid").val();
		var transid = $("#transid");
		var JFYF = $("#JFYF").val();
		var cporderid = $("#cporderid").val();
		//var result = $("#result").combobox('getValue');
		var orderstatus = $("#orderstatus").combobox('getValue');
		var paytype = $("#paytype").combobox('getValue');
		//var transtype = $("#transtype").combobox('getValue');
		var chargestatus = $("#chargestatus").combobox('getValue');
		var begintime = $("#begintime").combobox('getValue');
		var endtime = $("#endtime").combobox('getValue');
		var source = $("#source").val();
		var platsystem = $("#platsystem").combobox('getValue');
		var offline = $("#offline").combobox('getValue');
		//var WYID = $("#WYID").val();
		var property = $("#property").val();
		var from = $("#from").combobox('getValue');

		var parameters = {
			// appid:appid,
			// waresid:waresid,
			// appuserid:appuserid,
			// transid:transid,
			// HTID:HTID,
			// JFYF:JFYF,
			// SKID:SKID,
			cporderid:cporderid,
			//result : result,
			orderstatus : orderstatus,
			paytype : paytype,
			//transtype : transtype,
			chargestatus : chargestatus,
			begintime : begintime,
			endtime : endtime,
			source : source,
			JFYF : JFYF,
			platsystem : platsystem,
			offline : offline,
			//WYID : WYID
			property : property,
			from : from	
		};

		jQuery.ajax({
			url : "order/getCount.htm",
			data : parameters,
			type : "post",
			success : function(data) {
				var result = eval('(' + data + ')');
				if(result.price == undefined){
					result.price=0;
				}
				$("#price").html(result.price);
				$("#amount").html(result.total);
			},
			error : function() {
				alert("系统错误！");
			}
		});
		
		$("#list").datagrid('load', parameters);
		
	});
	/*
	 * 重置事件
	 */
	$("#btnReset").click(function() {
		var source = $("#source").val();
		var WYID = $("#WYID").val();
		$("#queryForm").form('clear');
		$("#source").val(source);
		$("#WYID").val(WYID);
	});

	$("#result").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "交易成功"
		}, {
			"value" : "1",
			"name" : "交易失败"
		}, {
			"value" : "2",
			"name" : "未交易"
		} ],
		valueField : 'value',
		textField : 'name'
	});

	$("#orderstatus").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "支付成功"
		}, {
			"value" : "2",
			"name" : "待支付"
		}],
		valueField : 'value',
		textField : 'name'
	});

	$("#paytype").combobox({
		editable : false,
		multiple : false,
		data : [/* {
			"value" : "1",
			"name" : "充值卡"
		}, {
			"value" : "2",
			"name" : "游戏点卡"
		}, {
			"value" : "4",
			"name" : "银行卡"
		}, */{
			"value" : "401",
			"name" : "支付宝"
		}, /*{
			"value" : "402",
			"name" : "财付通"
		}, {
			"value" : "501",
			"name" : "支付宝网页"
		}, {
			"value" : "502",
			"name" : "财付通网页"
		}, */{
			"value" : "403",
			"name" : "微信支付"
		}/*, {
			"value" : "5",
			"name" : "爱贝币"
		}, {
			"value" : "6",
			"name" : "爱贝一键支付"
		}, {
			"value" : "16",
			"name" : "百度钱包"
		}, {
			"value" : "30",
			"name" : "移动话费"
		}, {
			"value" : "31",
			"name" : "联通话费"
		}, {
			"value" : "32",
			"name" : "电信话费"
		}*/ ],
		valueField : 'value',
		textField : 'name'
	});

	$("#transtype").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "支付交易"
		}, {
			"value" : "1",
			"name" : "支付冲正"
		}, {
			"value" : "2",
			"name" : "契约退订"
		}, {
			"value" : "3",
			"name" : "自动续费"
		} ],
		valueField : 'value',
		textField : 'name'
	});

	$("#chargestatus").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "销账成功"
		}, {
			"value" : "1",
			"name" : "销账失败"
		}, ],
		valueField : 'value',
		textField : 'name'
	});

	$("#begintime").datetimebox({
		required : false,
		showSeconds : false
	});

	$("#endtime").datetimebox({
		required : false,
		showSeconds : false
	});
	
	$("#platsystem").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "爱贝"
		}, {
			"value" : "1",
			"name" : "好邻邦微信"
		}, {
			"value" : "2",
			"name" : "好邻邦支付宝"
		} ],
		valueField : 'value',
		textField : 'name'
	});
	
	$("#offline").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "否"
		}, {
			"value" : "1",
			"name" : "是"
		} ],
		valueField : 'value',
		textField : 'name'
	});
	
	$("#from").combobox({
		editable : false,
		multiple : false,
		data : [ {
			"value" : "0",
			"name" : "微信公众号"
		}, {
			"value" : "1",
			"name" : "触摸屏"
		}, {
			"value" : "2",
			"name" : "通知单"
		}, {
			"value" : "3",
			"name" : "短信"
		}, {
			"value" : "4",
			"name" : "客显屏"
		}, {
			"value" : "5",
			"name" : "扫码铭牌"
		}, {
			"value" : "6",
			"name" : "App"
		} ],
		valueField : 'value',
		textField : 'name'
	});

});
