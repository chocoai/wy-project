
$(function(){
	
	var offsetWidth = document.body.offsetWidth;//网页可见区域宽 (包括边线的宽)
	var offsetHeight =  document.body.offsetHeight;//网页可见区域高(包括边线的高)
	var editRow = undefined; //定义全局变量：当前编辑的行
	var datagrid; //定义全局变量datagrid
//	var status = [ 
//				      {"statusId":"1","name":"有效"},
//				      {"statusId":"0","name":"无效"}
//			    ];
	
	datagrid = $("#list").datagrid({
		url:'wares/getWares.htm',
		pagination:true,
		height:offsetHeight,
		sortName:'',
		sortOrder:'desc',
		columns:[[
		         {field:'ids',title:'编号',width:100,halign:'center',sortable:true,checkbox:true},
		         {field:'id',title:'ID',width:40,halign:'center',sortable:false},
		         {field:'waresid',title:'商品编号',width:60,halign:'center',sortable:false,editor:"text"},
		         {field:'waresname',title:'商户名称',width:80,halign:'center',align:'center',sortable:false,editor:"text"},
		         {field:'notifyurl',title:'通知地址',width:300,halign:'center',sortable:false,editor:"text"},
		         {field:'requesturl',title:'回调地址',width:280,halign:'center',sortable:false,editor:"text"},
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
                //添加时如果没有正在编辑的行，则在datagrid的第一行插入一行
                $("#list").attr("flag","add");
                if (editRow == undefined) {
                    datagrid.datagrid("insertRow", {
                        index: 0, // index start with 0
                        row: {

                        }
                    });
                    //将新插入的那一行开户编辑状态
                    datagrid.datagrid("beginEdit", 0);
                    //给当前编辑的行赋值
                    editRow = 0;
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
								url : "wares/deleteWares.htm",
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
					// 修改之前先关闭已经开启的编辑行，当调用endEdit该方法时会触发onAfterEdit事件
					if (editRow != undefined) {
						datagrid.datagrid("endEdit", editRow);
					}
					// 当无编辑行时
					if (editRow == undefined) {
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
		} ],
		onAfterEdit : function(rowIndex, rowData, changes) {
			// console.info(rowData);
			var flag = $("#list").attr("flag");
			var url;
			if (flag == "add") {
				url = "wares/saveWares.htm";
			} else {
				url = "wares/updateWares.htm";
			}
			// endEdit该方法触发此事件
			editRow = undefined;
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
					$("#list").attr("flag", "");
					$('#list').datagrid("reload");
				},
				error : function() {
					alert("System error！");
				}
			});
		},
	});
	
});
