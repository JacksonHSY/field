$(function() {
	bankInfo_InitData();
});

// 初始化
function bankInfo_InitData() {
	$("#bankInfo_datagrid").datagrid({
		url : ctx.rootPath() + "/bankInfo/pageList",
		striped : true,
		singleSelect : false,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#bankInfo_toolBarBtn',
		onDblClickRow : function(rowIndex, rowData) {
			var api = ctx.rootPath() + '/bankInfo/findById';
			var params = {
				id : rowData.id
			};
			var callback = function(data) {
				bankInfo_ShowDialog("修改", data);
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
			field : 'code',
			title : '银行编号',
			width : 100,
		}, {
			field : 'bankName',
			title : '银行名称',
			width : 100,
		}, {
			field : 'bankType',
			title : '银行类型',
			width : 100,
			formatter : function(value) {
				if (value == '0')
					return '证大投资';
				else if (value == '1')
					return '非证大投资';
			}
		} ] ]
	});
}
//查询
function bankInfoQuery() {
	$("#bankInfo_datagrid").datagrid('load',$("#bankInfo_Query_Form").serializeObject());
}


// -------------新建--------------start
// Open_dialog
function bankInfoOpen() {
	$("#bankInfo_AddOrUpdate_Code").textbox({
		disabled : false
	});
	bankInfo_ShowDialog("新建");
}
function bankInfo_ShowDialog(title, data) {
	$("#bankInfo_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : title,
		width : 800,
		modal : true,
		onBeforeOpen : function() {
			$('#bankInfo_AddOrUpdate_Form').form('clear');
			if ("修改" == title) {
				$("#bankInfo_AddOrUpdate_Form").form("load", data);
				$("#bankInfo_AddOrUpdate_Code").textbox({
					disabled : true
				});
			}
		},
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				bankInfo_Save();
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#bankInfo_AddOrUpdate_dialog").dialog("close");
			}
		} ]
	});
}

// Save
function bankInfo_Save() {
	var title = $("#bankInfo_AddOrUpdate_dialog").dialog('options').title;
	if (title == '新建') {
		var validate = $("#bankInfo_AddOrUpdate_Form").form('validate');
		if (validate) {
			var api = ctx.rootPath() + '/bankInfo/save';
			var params = $('#bankInfo_AddOrUpdate_Form').serializeObject();
			var callback = function(data) {
				if (data.status == "true") {
					$.info("提示", "保存成功!", "info", 2000);
				} else {
					$.info("提示", "保存失败!", "info", 2000);
				}
				$("#bankInfo_AddOrUpdate_dialog").dialog("close");
				$('#bankInfo_datagrid').datagrid('reload');
			};
			var error = function(XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			};
			post(api, params, 'json', callback, error);
		}
	} else if (title == '修改') {
		var validate = $("#bankInfo_AddOrUpdate_Form").form('validate');
		if (validate) {
			var api = ctx.rootPath() + '/bankInfo/update';
			var params = $('#bankInfo_AddOrUpdate_Form').serializeObject();
			var callback = function(data) {
				if (data.status == "true") {
					$.info("提示", "修改成功!", "info", 2000);
				} else {
					$.info("提示", "修改失败!", "info", 2000);
				}
				$("#bankInfo_AddOrUpdate_dialog").dialog("close");
				$('#bankInfo_datagrid').datagrid('reload');
			};
			var error = function(XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			};
			post(api, params, 'json', callback, error);
		}
	}
}
// -------------新建--------------end

// 删除
function bankInfoDelete() {
	var rowInfo = $("#bankInfo_datagrid").datagrid('getSelected');
	if (!rowInfo) {
		$.messager.alert('提示信息', "请至少选中一行！", 'info');
		return;
	}
	var rows = $('#bankInfo_datagrid').datagrid('getSelections');
	var ids = "";
	for (var i = 0; i < rows.length; i++) {
		ids += rows[i].id + ",";
	}
	ids = ids.substr(0, ids.length - 1);
	var api = ctx.rootPath() + '/bankInfo/deletes';
	var params = {
		"ids" : ids
	};
	var callback = function(data) {
		if (data.status == 'true') {
			$.info("提示", data.deletedId + "条删除成功!", "info", 2000);
		} else {
			$.info("提示", "删除失败!", "info", 2000);
		}
		$('#bankInfo_datagrid').datagrid('clearChecked');
		$('#bankInfo_datagrid').datagrid('reload');
	};
	var error = function(XMLHttpRequest, textStatus, errorThrown) {
		console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
	};
	$.messager.confirm("提示", "确认删除吗？", function(isOrNo) {
		if (isOrNo) {
			post(api, params, 'json', callback, error);
		}
	});
}
