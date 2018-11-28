$(function() {
	timedTask_InitDatagrid();
});
// 初始化定时任务数据表格信息
function timedTask_InitDatagrid() {
	$("#timedTask_datagrid").datagrid({
		url : ctx.rootPath() + "/scheduleJob/pageList",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#timedTask_toolBarBtn',
		onDblClickRow : function(rowIndex, rowData) {
			var api = ctx.rootPath() + '/scheduleJob/findById';
			var params = {
				id : rowData.id
			};
			var callback = function(data) {
				timedTask_Dialog('修改', data);
			};
			post(api, params, 'json', callback);
		},
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'jobName',
			title : '任务名称',
			width : 255,
		}, {
			field : 'jobDesc',
			title : '任务描述',
			width : 255,
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
		}, {
			field : 'cronExpression',
			title : '任务运行时间表达式',
			width : 255,
		}, {
			field : 'jobGroup',
			title : '任务分组',
			width : 255,
		}, {
			field : 'remark',
			title : '备注',
			width : 255,
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
		}, {
			field : 'createdBy',
			title : '创建人',
			width : 255,
		}, {
			field : 'createdDate',
			title : '创建时间',
			width : 255,
			formatter : function(value, data, index) {
				return getLocalTime(value);
			}
		}, {
			field : 'lastModifiedBy',
			title : '修改人',
			width : 255,
		}, {
			field : 'lastModifiedDate',
			title : '修改时间',
			width : 255,
			formatter : function(value, data, index) {
				return getLocalTime(value);
			}
		}, {
			field : 'jobStatus',
			title : '任务状态',
			width : 255,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				// //判断当前是启用还是禁用状态
				// if(isNotNull(value) && (0 == value || 2 == value)){
				// action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick="enabledJob('+data.id+')">启用</a>';
				// }else if(1==value){
				// action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick="disabledJob('+data.id+')">禁用</a>';
				// }
				if (isNotNull(value) && 0 == value) {
					// action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick="enabledJob('+data.id+')">启用</a>';
					action = '&nbsp;<a href="javaScript:void(0);">启用</a>';
				} else if (1 == value) {
					action = '&nbsp;<a href="javaScript:void(0);">禁用</a>';
					// action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick="disabledJob('+data.id+')">禁用</a>';
				} else {
					action = '&nbsp;<a href="javaScript:void(0);">删除</a>';
				}
				return action;
			}
		} ] ]
	});
}
// 开启job
function enabledJob(id) {
	console.info(id);
}
// 禁用job
function disabledJob(id) {
	console.info(id);
}


// 条件查询
function scheduleJobQurey() {
	$("#timedTask_datagrid").datagrid('load',$("#timedTask_Query_Form").serializeObject());
}
/**
 * 定时任务修改弹框
 * 
 * @Author LuTing
 * @date 2017年3月16日
 * @param title
 * @param data
 */
function timedTask_Dialog(title, data) {
	$("#timedTask_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : title,
		modal : true,
		width : 840,
		onBeforeOpen : function() {
			// 清除
			$('#timedTask_AddOrUpdate_Form').form('clear');
			if ("修改" == title) {
				$("#timedTask_AddOrUpdate_Form").form("load", data);
			}
		},
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				timedTask_saveOrUpdate();
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#timedTask_AddOrUpdate_dialog").dialog("close");
			}
		} ]
	});
};
/**
 * 保存或者修改定时任务
 */
function timedTask_saveOrUpdate() {
	var title = $("#timedTask_AddOrUpdate_dialog").dialog('options').title;
	if ("新建" == title) {
		var validate = $("#timedTask_AddOrUpdate_dialog").form('validate');
		if (validate) {
			var api = ctx.rootPath() + '/scheduleJob/save';
			var params = $('#timedTask_AddOrUpdate_Form').serializeObject();
			params.paramType = 'job';
			post(api, params, 'json', function(data) {
				if (data.success) {
					$("#timedTask_AddOrUpdate_dialog").dialog("close");
					$('#timedTask_datagrid').datagrid('reload');
					$.info("提示", data.messages);
				} else {
					$.info("提示", data.message, "error");
				}
			})
		}
	} else if ("修改" == title) {
		var validate = $("#timedTask_AddOrUpdate_dialog").form('validate');
		if (validate) {
			var api = ctx.rootPath() + '/scheduleJob/update';
			var params = $('#timedTask_AddOrUpdate_Form').serializeObject();
			console.info(params);
			post(api, params, 'json', function(data) {
				if (data.type == 'SUCCESS') {
					$("#timedTask_AddOrUpdate_dialog").dialog("close");
					$('#timedTask_datagrid').datagrid('reload');
					$.info("提示", data.messages);
				} else {
					$.info("提示", data.message, "error");
				}
			})
		}

	}
}

/**
 * 删除
 * 
 * @Author LuTing
 * @date 2017年3月16日
 */
function scheduleJobDelete() {
	var rowInfo = $("#timedTask_datagrid").datagrid('getSelected');
	if (!rowInfo) {
		$.messager.alert('提示信息', "请至少选中一行！", 'info');
		return;
	}
	var rows = $('#timedTask_datagrid').datagrid('getSelections');
	var ids = "";
	for (var i = 0; i < rows.length; i++) {
		ids += rows[i].id + ",";
	}
	ids = ids.substr(0, ids.length - 1);
	var api = ctx.rootPath() + "/scheduleJob/delete";
	var params = {
		"ids" : ids
	};
	$.messager.confirm("提示", "确认删除吗？", function(isOrNo) {
		if (isOrNo) {
			post(api, params, 'json', function(data) {
				if (data.type == 'SUCCESS') {
					// 清空选中复选框
					$('#timedTask_datagrid').datagrid('clearChecked');
					// 重新加载数据
					$('#timedTask_datagrid').datagrid('reload');
					$.info("提示", data.messages);
				} else {
					$.info("提示", data.message, "error");
				}
			})
		}
	});
}