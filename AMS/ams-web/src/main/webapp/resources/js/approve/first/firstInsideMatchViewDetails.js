/*
$(function() {
	// 初始化查看详情-查看还款详细信息
	InitFirstInsideMatch_detailsInfo_table();
	// 初始化查看详情-查看账单信息
	InitFirstTelephoneSummary_billInfo_table();
});
/!**
 * 查看详情-查看还款详细信息
 * 
 * @Author LuTing
 * @date 2017年3月2日
 *!/
function InitFirstInsideMatch_detailsInfo_table() {
	$("#firstInsideMatch_detailsInfo_table").datagrid({
		url : ctx.rootPath() + '/firstInsideMatch/firstInsideMatchDetailsInfo',
		striped : true,
		singleSelect : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		columns : [ [ {
			field : 'telDate',
			title : '期数',
			width : 150,
		}, {
			field : 'name',
			title : '应还款日',
			width : 150,
		}, {
			field : 'appleRelationType',
			title : '实际还款日',
			width : 150,
		}, {
			field : 'telPhoneType',
			title : '还款金额',
			width : 150,
		}, {
			field : 'telPhone',
			title : '当期一次性还清金额',
			width : 150,
		}, {
			field : 'telRelationType',
			title : '当期还款状态',
			width : 150,
		}, {
			field : 'askContent',
			title : '当期剩余欠款',
			width : 150,
		}, {
			field : 'createdBy',
			title : '还款方',
			width : 180,
		} ] ]
	});
}
/!**
 * 查看详情-查看账单信息
 * 
 * @Author LuTing
 * @date 2017年3月2日
 *!/
function InitFirstTelephoneSummary_billInfo_table() {
	$("#firstTelephoneSummary_billInfo_table").datagrid({
		url : ctx.rootPath() + '/firstInsideMatch/firstTelephoneSummaryBillInfo',
		striped : true,
		singleSelect : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		columns : [ [ {
			field : 'telDate',
			title : '交易日期',
			width : 110,
		}, {
			field : 'name',
			title : '交易方式',
			width : 110,
		}, {
			field : 'appleRelationType',
			title : '交易类型',
			width : 120,
		}, {
			field : 'telPhoneType',
			title : '期初余额',
			width : 120,
		}, {
			field : 'telPhone',
			title : '收入',
			width : 140,
		}, {
			field : 'telRelationType',
			title : '支出',
			width : 150,
		}, {
			field : 'askContent',
			title : '期末余额',
			width : 150,
		}, {
			field : 'createdBy',
			title : '备注',
			width : 150,
		}, {
			field : 'created',
			title : '流水号',
			width : 150,
		} ] ]
	});
}*/
