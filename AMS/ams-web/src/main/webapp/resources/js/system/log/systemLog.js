$(function(){
	systemLog_InitDatagrid();
});

//数据
function systemLog_InitDatagrid(){
	$("#systemLog_datagrid").datagrid({
		url : ctx.rootPath() + "/log/systemLogPageList",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		columns : [[{
			field : 'ck',
			checkbox : true
		},{
			field : 'operateSystem',
			title : '操作系统',
			width : 255,
		},{
			field : 'work',
			title : '岗位',
			width : 255,
		},{
			field : 'first',
			title : '一级目录',
			width : 255,
		},{
			field : 'second',
			title : '二级目录',
			width : 255,
		},{
			field : 'operateType',
			title : '操作类型',
			width : 255,
		},{
			field : 'operator',
			title : '操作人',
			width : 255,
		},{
			field : 'userCode',
			title : '工号',
			width : 255,
		},{
			field : 'operateDate',
			title : '操作时间',
			width : 255,
			sortable : true,
			order : 'desc',
			formatter : function(value, data, index) {
				return getLocalTime(value);
			}
		},{
			field : 'IP',
			title : 'IP地址',
			width : 255,
		},{
			field : 'desc',
			title : '备注',
			width : 255,
		}]]
	});
}