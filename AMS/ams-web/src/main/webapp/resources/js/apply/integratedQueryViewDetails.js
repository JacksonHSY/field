$(function() {
	var loanNo = $("#intergrated_query_details_loanNo").val();
	if(loanNo !=null ){
	// 初始化查看详情-查看还款详细信息
	InitIntegratedQuery_detailsInfo_table(loanNo);
	// 初始化查看详情-查看账单信息
	InitIntegratedQuery_billInfo_table(loanNo);
	}
	
});
/**
 * 查看详情-查看还款详细信息
 * 
 * @Author 
 * @date 2017年3月2日
 */
function InitIntegratedQuery_detailsInfo_table(loanNo) {
	$("#integratedQuery_detailsInfo_table").datagrid({
		url : ctx.rootPath() + '/search/integratedQueryDetailsInfo',
		queryParams : {
			loanNo : loanNo
		},
		striped : true,
		singleSelect : true,
		idField : 'id',
		pagination : false,
		fitColumns : true,
		columns : [ [ {
			field : 'currentTerm',
			title : '期数',
			width : 150,
		}, {
			field : 'returnDate',
			title : '应还款日',
			width : 150,
			formatter : formatDateYMD
		}, {
			field : 'factReturnDate',
			title : '实际还款日',
			width : 150,
			formatter : formatDateYMD
		}, {
			field : 'returneterm',
			title : '还款金额',
			width : 150,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'repaymentAll',
			title : '当期一次性还清金额',
			width : 150,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'repaymentState',
			title : '当期还款状态',
			width : 150,
		}, {
			field : 'deficit',
			title : '当期剩余欠款',
			width : 150,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'borrowerName',
			title : '还款方',
			width : 180,
		} ] ]
	});
}
/**
 * 查看详情-查看账单信息
 * 
 * @Author LuTing
 * @date 2017年3月2日
 */
function InitIntegratedQuery_billInfo_table(loanNo) {
	$("#integratedQuery_billInfo_table").datagrid({
		url : ctx.rootPath() + '/search/integratedQueryBillInfo',
		queryParams : {
			loanNo : loanNo
		},
		striped : true,
		singleSelect : true,
		idField : 'id',
		pagination : false,
		fitColumns : true,
		nowrap : false,
		columns : [ [ {
			field : 'tradeDate',
			title : '交易日期',
			width : 110,
			formatter : formatDateYMD
		}, {
			field : 'tradeType',
			title : '交易方式',
			width : 110,
		}, {
			field : 'tradeName',
			title : '交易类型',
			width : 120,
		}, {
			field : 'beginBalance',
			title : '期初余额',
			width : 120,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'income',
			title : '收入',
			width : 140,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'outlay',
			title : '支出',
			width : 150,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'endBalance',
			title : '期末余额',
			width : 150,
			formatter : numberFomatterTwoDecimal
		}, {
			field : 'memo',
			title : '备注',
			width : 150,
		}, {
			field : 'tradeNo',
			title : '流水号',
			width : 150,
		} ] ]
	});
}