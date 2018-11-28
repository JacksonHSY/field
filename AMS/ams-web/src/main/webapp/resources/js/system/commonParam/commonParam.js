//初始化
$(function() {
	commonParam_InitDatagrid();
});

// 数据网格
function commonParam_InitDatagrid() {
	$("#commonParam_Datagrid").datagrid({
		url : ctx.rootPath() + "/commonParam/pageList",
		striped : true,
		singleSelect : false,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#commonParam_toolBarBtn',
		onDblClickRow : function(rowIndex, rowData) {
			var api = ctx.rootPath() + '/commonParam/findById';
			var params = {
				id : rowData.id
			};
			var callback = function(data) {
				commonParam_ShowDialog("update", data);
			};
			var error = function(XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			};
			post(api, params, 'json', callback, error);
		},
		columns : [ [ {
			field : 'id',
			checkbox : true
		}, {
			field : 'paramName',
			title : '字段中文名称',
			width : 100,
		}, {
			field : 'paramValue',
			title : '值',
			width : 100,
		}, {
			field : 'paramDesc',
			title : '备注',
			width : 100,
		}, {
			field : 'paramType',
			title : '参数类型',
			width : 100,
		}, {
			field : 'paramKey',
			title : '参数标识',
			width : 80,
		}, {
			field : 'lastModifiedBy',
			title : '最后修改人',
			width : 100,
		}, {
			field : 'lastModifiedDate',
			title : '最后修改时间',
			width : 100,
			sortable : true,
			order : 'desc',
			formatter : function(value, data, index) {
				return moment(value).format('YYYY-MM-DD');
			}
		} ] ]
	});
}

// 条件查询
function commonParamQuery() {
	$("#commonParam_Datagrid").datagrid('load', $("#commonParam_Query_Form").serializeObject());
}

// 保存
function commonParam_ShowDialog(title, data) {
	$("#commonParam_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : title,
		width : 600,
		modal : true,
		onBeforeOpen : function() {
			if ("update" == title) {
				$('#commonParam_AddOrUpdate_dialog').panel({
					title : "修改"
				});
				$("#commonParam_AddOrUpdate_From").form("load", data);
				$("#commonParam_AddOrUpdate_ParamName").textbox({
					disabled : true
				});
				$("#commonParam_AddOrUpdate_ParamType").textbox({
					disabled : true
				});
				$("#commonParam_AddOrUpdate_ParamKey").textbox({
					disabled : true
				});
			} else if ("add" == title) {
				$('#commonParam_AddOrUpdate_From').form('clear');
				$("#commonParam_AddOrUpdate_ParamName").textbox({
					disabled : false
				});
				$("#commonParam_AddOrUpdate_ParamType").textbox({
					disabled : false
				});
				$("#commonParam_AddOrUpdate_ParamKey").textbox({
					disabled : false
				});
				$('#commonParam_AddOrUpdate_dialog').panel({
					title : "新增"
				});
				$("#commonParam_AddOrUpdate_ParamName").textbox({
					required : true
				});
				$("#commonParam_AddOrUpdate_ParamValue").textbox({
					required : true
				});
				$("#commonParam_AddOrUpdate_ParamType").textbox({
					required : true
				});
				$("#commonParam_AddOrUpdate_ParamKey").textbox({
					required : true
				});
				$("#commonParam_AddOrUpdate_From").form("enableValidation");
			}
		},
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				commonParam_Save(title);
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#commonParam_AddOrUpdate_dialog").dialog("close");
			}
		} ]

	});
}

function commonParam_Save(title) {
	// var title = $("#commonParam_AddOrUpdate_dialog").dialog('options').title;
	var params = $('#commonParam_AddOrUpdate_From').serializeObject();
	params.paramStatus = '0';
	var api;
	var callback;
	if (title == 'update') {
		api = ctx.rootPath() + '/commonParam/update';
		callback = function(data) {
			if (data.status == "true") {
				$.info("提示", "修改成功!", "info", 2000);
			} else {
				$.info("提示", "修改失败!", "info", 2000);
			}
			$("#commonParam_AddOrUpdate_dialog").dialog("close");
			$('#commonParam_Datagrid').datagrid('reload');
		};

	} else if (title == 'add') {
		api = ctx.rootPath() + '/commonParam/add';
		callback = function(data) {
			if (data.status == "true") {
				$.info("提示", "新增成功!", "info", 2000);
				$("#commonParam_AddOrUpdate_dialog").dialog("close");
				$('#commonParam_Datagrid').datagrid('reload');
			} else if (data.status == "repeat") {
				$.info("提示", "新增失败,参数标识已经存在!", "info", 2000);
			} else {
				$.info("提示", "新增失败!", "info", 2000);
			}

		};
	}
	var error = function(XMLHttpRequest, textStatus, errorThrown) {
		console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
	};
	if ($("#commonParam_AddOrUpdate_From").form('validate')) {
		post(api, params, 'json', callback, error);
	}
}
function commonParam_delete() {
	var rows = $("#commonParam_Datagrid").datagrid("getSelections");
	if (rows.length > 0) {
		$.messager.confirm("确认", '是否要删除选择的参数', function(r) {
			if (r) {
				var params = [];
				$.each(rows, function(ind, val) {
					params.push(val.id);
				});
				$.ajax({
					url : ctx.rootPath() + '/commonParam/delete',
					dataType : "json",
					method : 'post',
					contentType : 'application/json',
					data : JSON.stringify(params),
					success : function(data) {
						if (data.status == "true") {
							$.info("提示", "删除成功!", "info", 2000);
						} else {
							$.info("提示", "删除失败!", "info", 2000);
						}
						$("#commonParam_Datagrid").datagrid("clearSelections");
						$('#commonParam_Datagrid').datagrid('reload');
					},
					error : function(data) {
						$.info("警告", data.responseText, "warning");
					}
				});
				return true;
			}
		});
	} else {
		$.info("提示", "请选择要删除的数据");
	}
}

// 清除redis缓存
function cleanCache() {
	$.messager.confirm("操作提示", "确定清除所有缓存？", function(data) {
		if (data) {
			var url = ctx.rootPath() + '/commonParam/cleanRedis';

			post(url, {}, 'json', function(result) {
				if (result.type == "SUCCESS")
					$.info("提示", "处理成功!", "info", 1000);
			}, function(data) {
				$.info("警告", data.responseText, "warning");
			});
		}
	});
}