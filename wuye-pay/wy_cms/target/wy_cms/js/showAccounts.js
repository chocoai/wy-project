
$(function(){
	
	var offsetWidth = document.body.offsetWidth;//网页可见区域宽 (包括边线的宽)
	var offsetHeight =  document.body.offsetHeight;//网页可见区域高(包括边线的高)
	var editRow = undefined; //定义全局变量：当前编辑的行
	var datagrid; //定义全局变量datagrid
	var status = [ 
				      {"statusId":"1","name":"有效"},
				      {"statusId":"0","name":"无效"}
			    ];
	var platsystem = [ 
	              {"platsystemId":"2","name":"好邻邦支付宝"},
			      {"platsystemId":"1","name":"好邻邦微信"},
			      {"platsystemId":"0","name":"爱贝"}
		    ];
	var offline = [ 
			      {"offlineId":"1","name":"是"},
			      {"offlineId":"0","name":"否"}
		    ];
	var sumflag = [ 
			      {"sumflagId":"0","name":"是"},
			      {"sumflagId":"1","name":"否"}
		    ];
	var copydata;//定义全局变量保存复制内容
	
	datagrid = $("#list").datagrid({
		url:'account/getAccount.htm',
		pagination:true,
		height:offsetHeight-150,
		sortName:'',
		sortOrder:'desc',
		columns:[[
		         {field:'ids',title:'编号',width:100,halign:'center',sortable:true,checkbox:true},
		         {field:'id',title:'id',width:40,halign:'center',sortable:false},
		         {field:'status',title:'状态',width:40,halign:'center',align:'center',sortable:true,
	            	   formatter:function(value){  
	                       for(var i=0; i<status.length; i++){  
	                           if (status[i].productid == value) 
	                        	   return status[i].name;  
	                       }  
	                       if (value == "1") {
	                    	   return "有效";
	                       } else {
	                    	   return "无效";
	                       }
	                   },  
	                   editor:{  
	                       type:'combobox',  
	                       options:{  
	                           valueField:'statusId', 
	                           textField:'name',  
	                           data:status,  
	                           required:true  
	                       }  
	                   }},
		         {field:'source',title:'来源',width:80,halign:'center',sortable:false,editor:"text",
                	  formatter:function(value,rowData,rowIndex){
  		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
  		        	 }
	             },
		         {field:'appid',title:'应用编号(服务商商户号)',width:160,halign:'center',sortable:false,editor:"text",
	            	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'appuserid',title:'用户帐号(子商户商户号)',width:160,halign:'center',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'wyid',title:'物业编号',width:55,halign:'center',align:'right',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'property',title:'物业名称',width:105,halign:'center',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'appv_key',title:'应用key(服务商秘钥)',width:160,halign:'center',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'platp_key',title:'platp_key(微信appid)',width:160,halign:'center',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },
		         {field:'appSecret',title:'appSecret(微信公众号appSecret)',width:200,halign:'center',sortable:false,editor:"text",
		        	 formatter:function(value,rowData,rowIndex){
		        		 return "<pre style='margin:0; font-family: inherit;'>"+value+"</pre>";
		        	 }
		         },       
		         {field:'createtime',title:'创建时间',width:130,halign:'center',sortable:false},
		         {field:'creater',title:'创建人',width:60,halign:'center',sortable:false},
		         {field:'updatetime',title:'最后更新时间',width:130,halign:'center',sortable:false},
		         {field:'editor',title:'最后修改人',width:80,halign:'center',sortable:false},
		         {field:'platsystem',title:'平台',width:80,halign:'center',align:'center',sortable:true,
		        	 formatter:function(value,rowData,rowIndex){
		 				switch(value){
	                       case "0":return "爱贝";
	                       case "1":return "好邻邦微信";
	                       case "2":return "好邻邦支付宝";
		 				};
		        	 },
		        	 editor:{  
	                       type:'combobox',  
	                       options:{  
	                           valueField:'platsystemId',  
	                           textField:'name',  
	                           data:platsystem,  
	                           required:true  
	                       }
		        	 }
		         },
		         {field:'offline',title:'线下',width:40,halign:'center',align:'center',sortable:true,
		        	 formatter:function(value,rowData,rowIndex){
		 				if(value==null || value==0){
		 					return "否";
		 				}else{
		 					return "是";
		 				}
		        	 },
			         editor:{  
	                     type:'combobox',  
	                     options:{  
	                         valueField:'offlineId',  
	                         textField:'name',  
	                         data:offline,  
	                         required:true  
	                     }
		        	 }
		         },
		         {field:'sumflag',title:'缴费项目可选',width:80,halign:'center',align:'center',sortable:true,
		        	 formatter:function(value,rowData,rowIndex){
		 				if(value==null || value==0){
		 					return '<a href="#" style="color:red" onclick="changeSumflag('+rowIndex+',this'+')">是</a>';
		 				}else if(value == 1){
		 					return '<a href="#" style="color:green" onclick="changeSumflag('+rowIndex+',this'+')">否</a>';
		 				}
		        	 },
			         editor:{  
	                     type:'combobox',  
	                     options:{  
	                         valueField:'sumflagId',  
	                         textField:'name',  
	                         data:sumflag,  
	                         required:true  
	                     }
		        	 }
		         },
		        /* {field:'wxid',title:'维修ID',width:200,halign:'center',sortable:false,editor:"text"},
		         {field:'ltid',title:'临停ID',width:200,halign:'center',sortable:false,editor:"text"}, */
				 {field:'_operate',title:'操作',width:130,halign:'center',align:'center',sortable:false,formatter:formatOper},
				 ]],
		toolbar : [{
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				//添加列表的操作按钮添加，修改，删除等
                //添加时先判断是否有开启编辑的行，如果有则把开户编辑的那行结束编辑
                if (editRow != undefined) {
                    datagrid.datagrid("endEdit", editRow);
                }
                if($("#list").attr("flag") == "copy" || $("#list").attr("flag") == "tocopy"){
                	$("#list").attr("flag","tocopy");
                	//console.log(copydata);
                	copydata.status = 1;
                	//datagrid.datagrid("beginEdit", editRow);
					//datagrid.datagrid("endEdit", editRow);
                	 datagrid.datagrid("insertRow", {
                         index: 0, // index start with 0
                         row: copydata,
                     });
                     //将新插入的那一行开户编辑状态
                     datagrid.datagrid("beginEdit", 0);
                     //给当前编辑的行赋值
                     editRow = 0;
                }else{
                	//添加时如果没有正在编辑的行，则在datagrid的第一行插入一行
                    $("#list").attr("flag","add");
                    if (editRow == undefined) {
                        datagrid.datagrid("insertRow", {
                            index: 0, // index start with 0
                            row:{
                            	status:1,
                            }
                        });
                        //将新插入的那一行开户编辑状态
                        datagrid.datagrid("beginEdit", 0);
                        //给当前编辑的行赋值
                        editRow = 0;
                    }
                }
                
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var rows = $("#list").datagrid("getSelections");
				// 选择要删除的行
				if (rows.length > 0) {
					$.messager.confirm("Confirm", "确定删除?", function(r) {
						if (r) {
							var ids = new Array();
							for ( var i = 0; i < rows.length; i++) {
								console.info(rows[i].id);
								ids.push(rows[i].id);
							}
							// 将选择到的行存入数组并用,分隔转换成字符串，
							// 本例只是前台操作没有与数据库进行交互所以此处只是弹出要传入后台的id
							// alert(ids.join(','));

							// 执行后台删除操作
							jQuery.ajax({
								url : "account/deleteAccount.htm",
								data : "ids=" + ids,
								type : "post",
								success : function(data) {
									parent.$.messager.show({
										title : "系统消息",
										msg : "删除成功！"
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
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
				// 修改时要获取选择到的行
				var rows = datagrid.datagrid("getSelections");
				// 如果只选择了一行则可以进行修改，否则不操作
				if (rows.length == 1) {
					$("#list").attr("flag","edit");
					// 修改之前先关闭已经开启的编辑行，当调用endEdit该方法时会触发onAfterEdit事件
					datagrid.datagrid("endEdit", editRow);
					// 获取到当前选择行的下标
					var index = datagrid.datagrid("getRowIndex", rows[0]);
					// 开启编辑
					datagrid.datagrid("beginEdit", index);
					// 把当前开启编辑的行赋值给全局变量editRow
					editRow = index;
					// 当开启了当前选择行的编辑状态之后，
					// 应该取消当前列表的所有选择行，要不然双击之后无法再选择其他行进行编辑
					datagrid.datagrid("unselectAll");
				}
			}
		}, '-', {
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				// 保存时结束当前编辑的行，自动触发onAfterEdit事件如果要与后台交互可将数据通过Ajax提交后台
				datagrid.datagrid("endEdit", editRow);
			}
		}, '-', {
			text : '取消编辑',
			iconCls : 'icon-redo',
			handler : function() {
				// 取消当前编辑行把当前编辑行罢undefined回滚改变的数据,取消选择的行
				editRow = undefined;
				datagrid.datagrid("rejectChanges");
				datagrid.datagrid("unselectAll");
			}
		}, '-', {
			text : '复制',
			iconCls : 'icon-cut',
			handler : function() {
				// 修改时要获取选择到的行
				var rows = datagrid.datagrid("getSelections");
				// 如果只选择了一行则可以进行修改，否则不操作
				if (rows.length == 1) {
					$("#list").attr("flag","copy");
					
					// 修改之前先关闭已经开启的编辑行，当调用endEdit该方法时会触发onAfterEdit事件
					datagrid.datagrid("endEdit", editRow);
					// 当无编辑行时
					// 获取到当前选择行的下标
					var index = datagrid.datagrid("getRowIndex", rows[0]);
					// 开启编辑
					datagrid.datagrid("beginEdit", index);
					// 把当前开启编辑的行赋值给全局变量editRow
					editRow = index;
					// 当开启了当前选择行的编辑状态之后，
					// 应该取消当前列表的所有选择行，要不然双击之后无法再选择其他行进行编辑
					datagrid.datagrid("unselectAll");
					//保存时结束当前编辑的行，自动触发onAfterEdit事件如果要与后台交互可将数据通过Ajax提交后台
					datagrid.datagrid("endEdit", editRow);
				}else{
					parent.$.messager.show({
						title : "系统消息",
						msg : "请选择一行进行复制！"
					});
				}
			}
		} ],
		onAfterEdit : function(rowIndex, rowData, changes) {
			// console.info(rowData);
			var flag = $("#list").attr("flag");

			var url;
			if (flag == "add") {
				url = "account/saveAccount.htm";
				editRow = undefined;
			} else if(flag == "copy"){
				copydata = rowData;
				parent.$.messager.show({
					title : "系统消息",
					msg : "复制成功！"
				});
				return;
			} else if (flag == "tocopy"){
				rowData = copydata;
				url = "account/copyAccount.htm";
			} else {
				url = "account/updateAccount.htm";
				editRow = undefined;
			}
			// endEdit该方法触发此事件
			jQuery.ajax({
				url : url,
				data : rowData,
				type : "post",
				success : function(data) {
					// parent.$('#win').window('close');//关闭应用信息填写窗口
					// parent.$('#list').datagrid("reload");//重新载入表格数据
					parent.$.messager.show({
						title : "系统消息",
						msg : "操作成功！"
					});
					if($("#list").attr("flag") != "tocopy"){
						$("#list").attr("flag", "");
					}
					$('#list').datagrid("reload");
				},
				error : function() {
					alert("System error！");
				}
			});
		},
	});
	
	/*
	 * 查询事件
	 */
	$("#btnSearch").click(function(){
		var appid = $("#appid").val();
		//var wyid = $('#wyid').val();
		var property = $('#property').val();
		var source = $('#source').val();
		var platsystem = $('#platsystem').combobox('getValue');
		var offline = $('#offline').combobox('getValue');
		var status = $('#status').combobox('getValue');
		var parameters = {
				appid:appid,
				//wyid:wyid,
				property:property,
				source:source,
				platsystem:platsystem,
				offline:offline,
				status:status
		};
		$("#list").datagrid('load',parameters);
	});
	/*
	 * 重置事件
	 */
	$("#btnReset").click(function(){
		$("#queryForm").form('clear');
	});
	$("#status").combobox({  
		editable:false,
		multiple:false,
		data:[ 
		      {"value":"","name":"全部"},
		      {"value":"0","name":"无效"},
		      {"value":"1","name":"有效"}
	    ],
	    valueField:'value',
	    textField:'name'
	});
	$("#platsystem").combobox({  
		editable:false,
		multiple:false,
		data:[ 
		      {"value":"0","name":"爱贝"},
		      {"value":"1","name":"好邻邦微信"},
		      {"value":"2","name":"好邻邦支付宝"}
	    ],
	    valueField:'value',
	    textField:'name'
	});
	$("#offline").combobox({  
		editable:false,
		multiple:false,
		data:[ 
		      {"value":"0","name":"否"},
		      {"value":"1","name":"是"}
	    ],
	    valueField:'value',
	    textField:'name'
	});
	$("#sumflag").combobox({  
		editable:false,
		multiple:false,
		data:[ 
		      {"value":"0","name":"是"},
		      {"value":"1","name":"否"}
	    ],
	    valueField:'value',
	    textField:'name'
	});
});


function formatOper(val,row,index){  
    return '<a href="#" onclick="exportCode('+index+')">零星二维码</a>';  
} 

function exportCode(index){  
    $('#list').datagrid('selectRow',index);// 关键在这里  
    var row = $('#list').datagrid('getSelected');  
    if (row){  
       	/*jQuery.ajax({
       		url : "sporadic/index.htm",
       		data : {"source":row.source, "wyid":row.wyid},
       		type : "post",
       		success : function(data){
       			window.open('sporadic.html');
       		}
       	});*/
       	window.open('sporadic.html?x1='+row.wyid+'&x4='+row.source);
    }  
} 

function changeSumflag(index, jq){

	$('#list').datagrid('selectRow',index);// 关键在这里  
    var row = $('#list').datagrid('getSelected'); 
    if (row){
    	$.ajax({
			url : "account/changeSumflag.htm",
			type : "post",
			data : "id="+row.id,
			async : false,
			success : function(data){
				if(data){
					/*if(row.sumflag == 1){
						row.sumflag = 0;
						$('#dataGrid').datagrid('updateRow', row);
                        $(jq).attr("color", "red");
					}else if(row.sumflag == 0){
						row.sumflag = 1;
                        $('#dataGrid').datagrid('updateRow', row);
                        $(jq).attr("color", "green");
					}*/
					$('#list').datagrid("reload");
				}
			}
		});
		
    }
}
