$(function() {
	dictionaryType_InitDatagrid();
});

// 初始化datagrid
function dictionaryType_InitDatagrid() {
	$("#dictionaryType_datagrid").datagrid({
		url : ctx.rootPath() + "/dictionaryType/pageList",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#dictionaryType_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'dictionaryTypeCode',
			title : '字典类型编码',
			width : 155,
		}, {
			field : 'dictionaryTypeName',
			title : '字典类型名称',
			width : 155,
		}, {
			field : 'num',
			title : '顺序号',
			width : 155,
		}, {
			field : 'parentDictionaryName',
			title : '父字典名称',
			width : 155,
		}, {
			field : 'desc',
			title : '描述',
			width : 155,
		} ] ]
	});
}
//新建
function dictionaryType_ShowDialog(title, data) {
	$("#dictionaryType_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : title,
		width : 800,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {

			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#dictionaryType_AddOrUpdate_dialog").dialog("close");
			}
		} ]
	});
}