
$(function() {
	var loanNo = $('#qualityLogInfo_hidden_loanNo').val();
	qualityLogGrid('#qualityLogInfo_logInfo_opoin',loanNo);
});
function qualityLogGrid(id,loanNo) {
	
	$(id).datagrid({
		url : ctx.rootPath()+'/qualityControlDesk/queryQualityLog/'+loanNo,
		striped : true,
		toolbar : id + '_tool',
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		columns : [[{
			field : 'linkText',
			title : '环节',
			width : 80,
		},{
			field : 'operationText',
			title : '操作',
			width : 80,
		},{
			field : 'creatorName',
			title : '操作人',
			width : 80,
		},{
			field : 'createdDate',
			title : '操作时间',
			width : 80,
			formatter : function(value, data, index) {
				if(value != null){
					return moment(value).format('YYYY-MM-DD hh:mm:ss');
				}
			}
		},{
			field : 'remark',
			title : '备注',
			width : 80,
		}]]
	})
}