$(function(){
	loanInfoChangeLog_InitDatagrid();
});

//数据初始化
function loanInfoChangeLog_InitDatagrid(){
	$("#loanInfoChangeLog_datagrid").datagrid({
		url : ctx.rootPath() + "/loanInfoChangeLog/pageList",
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
			field : 'loanCode',
			title : '借款编号',
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
			field : 'operateNode',
			title : '操作环节',
			width : 255,
		},{
			field : 'operateDate',
			title : '操作时间',
			width : 255,
			sortable : true,
			order : 'desc',
			formatter : function(value, data, index) {
				return moment(value).format('YYYY-MM-DD');
			}
		},{
			field : 'IP',
			title : 'IP地址',
			width : 255,
		},{
			field : 'changeContents',
			title : '变更内容',
			width : 255,
		}]]
	});
}
