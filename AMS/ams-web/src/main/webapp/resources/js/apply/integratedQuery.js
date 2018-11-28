var searchOrNot = false;
$(function() {
	// 动态处理显示搜索table
	var tableObj =$("#integratedQuyer_form").find("table");
	var tableHeight = tableObj.find("tr").length;
	var thList = new Array();
	var tdList = new Array();
	var tdColspan = new Array();
	for (var x = 0; x < tableHeight; x++) {
		var tr = tableObj.find("tr:eq("+x+")");
		var tdLength = tr.find("td").length;
		for (var y = 0; y < tdLength; y++) {
			var th = tr.find("th:eq("+y+")");
			var td = tr.find("td:eq("+y+")");
			thList.push(th);
			tdList.push(td);
			tdColspan.push(td[0].colSpan+th.length);
		}
	}
	var tmp = 0;//行数
	var t = 0;//列数
	for (var int = 0; int < tdList.length; int++) {
		var colspan = tdColspan[int];
		if(t >= 8){
			t = 0;
			tmp = tmp + 1;
		}
		tableObj.find("tr:eq("+tmp+")").append(thList[int]).append(tdList[int]);
		t = t + colspan;
	}
	//去除空行
	for (var m = 0; m < tableHeight; m++) {
		var tr = tableObj.find("tr:eq("+m+")");
		var tdLength = tr.find("td").length;
		if(tdLength == 0){
			tr.remove();
		}
	}
	//重置和搜索按钮放置
	var lastTds = tableObj.find("tr:last").find("td");
	if(lastTds.length == 1){
		for (var i=0;i<4-lastTds.length; i++){
			tableObj.find("tr:last").prepend("<th></th><td></td>");
		}
	}else{
		for (var i=0;i<3-lastTds.length; i++){
			tableObj.find("tr:last").find("td:last").before("<th></th><td></td>");
		}
	}
	
	//初始化地址搜索框
	initAddressCom();
	
	// 初始化综合查询datagrid
//	initIntegratedQueryDatagrid('false');
	
	$("#integratedQuyer_form").mouseover(function(){
		searchOrNot = true;
	});
	$("#integratedQuyer_form").mouseout(function(){
		searchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && searchOrNot) {
			integratedQueryPass();
		}
	});
	
});

/**
 * 初始化地址搜索框
 */
function initAddressCom(newValue, oldValue){
	if(isEmptyObj(newValue)){
		$('#address_detial_div').css('display','none');
		$("#address_detail").textbox({
			value : "",
		});
	}else{
		$('#address_detial_div').css('display','inline-block');
	}
	
}


/**
 * 综合查询datagrid
 * 
 * @Author
 * @date
 */
//function initIntegratedQueryDatagrid(flag) {
//	var queryParams = new Object();
//	queryParams.flag = false;
//	$("#integratedQuery_userList_datagrid").datagrid('load',queryParams);
//	$("#integratedQuery_userList_datagrid").datagrid({
//		url : ctx.rootPath() + '/search/index',
//		queryParams : {
//			flag : flag
//		},
//		striped : true,
//		singleSelect : true,
//		rownumbers : true,
//		idField : 'id',
//		pagination : true,
//		fitColumns : false,
//		scrollbarSize : 0,
//		remoteSort : true,
//		onDblClickRow : function(index, data) {
//			handleInfo(data.loanNo);
//		},
//		onLoadSuccess : function(data){
//			console.log(data);
//			if(data.total == -1){
//				$.info("提示", "查询超时!");
//			}
//		},
//		columns : [ [ {
//			field : 'caseIdentify',
//			title : '案件标识',
//			width : 120,
//			align : 'center',
//			formatter : function(value, data, index) {
//				var iden = [];
//				if(value){
//                    iden = value.split("|");
//				}
//				var html ="";
//                if (iden[2] == "true") {
//                    html = html+ "<img title='触发欺诈规则' src='" + ctx.rootPath() + "/resources/images/loanMark/cheat.png'>&nbsp;";
//                }
//                if (iden[1] == "true") {
//                    html = html+ "<img title='加急件' src='" + ctx.rootPath() + "/resources/images/loanMark/urgent.png'>&nbsp;";
//                }
//				if (iden[0] == "true") {
//                    html = html+  "<img title='APP进件' src='" + ctx.rootPath() + "/resources/images/loanMark/app.png'>&nbsp;";
//				}
//                if (isNotNull(data.ifPreferentialUser) && "Y" == data.ifPreferentialUser) {
//                    html = html +  "<img title='费率优惠客户' src='" + ctx.rootPath() + "/resources/images/loanMark/rate.png'>&nbsp;";
//                }
//                if(isNotNull(data.simplifiedDataUser) && "Y" == data.simplifiedDataUser){
//                	html = html +  "<img title='简化资料客户' src='" + ctx.rootPath() + "/resources/images/loanMark/simple.png'>&nbsp;";
//                }
//				return html;
//			}
//		}, {
//			field : 'loanNo',
//			title : '借款编号',
//			width : 160,
//			sortable : true,
//		}, {
//			field : 'checkPersonCode',
//			title : '初审员',
//			hidden:true,
//			width : 60,
//			sortable : true,
//		}, {
//			field : 'contractNo',
//			title : '合同编号',
//			width : 100,
//			sortable : true,
//			hidden : true,
//		}, {
//			field : 'name',
//			title : '申请人姓名',
//			width : 100,
//			sortable : true,
//		}, {
//			field : 'idNo',
//			title : '身份证号码',
//			width : 100,
//			sortable : true,
//		}, {
//			field : 'applyType',
//			title : '申请类型',
//			width : 100,
//			sortable : true,
//		}, {
//			field : 'initProductName',
//			title : '申请产品',
//			width : 90,
//			sortable : true,
//		},{
//			field : 'productName',
//			title : '审批产品',
//			width : 90,
//			sortable : true,
//			formatter : function(value, data, index){
//				if(data.status.indexOf('申请') >= 0){
//					return '';
//				}else{
//					return value;
//				}
//			}
//		},{
//			field : 'corpName',
//			title : '单位名称',
//			width : 160,
//			sortable : true,
//			formatter : numberFomatter
//		}, {
//			field : 'status',
//			title : '借款状态',
//			width : 100,
//			sortable : true,
//		}, {
//			field : 'conclusion',
//			title : '审批结论',
//			width : 100,
//			formatter : numberFomatter
//		}, {
//			field : 'applyDate',
//			title : '提交时间',
//			width : 130,
//			formatter : formatDateYMD,
//			sortable : true,
//		}, {
//			field : 'branch',
//			title : '营业部',
//			width : 160,
//			sortable : true,
//			order : 'asc',
//			formatter : numberFomatter
//		}, {
//			field : 'branchAttr',
//			title : '营业部属性',
//			width : 100,
//			sortable : false,
//		}, {
//			field : 'action',
//			title : '操作',
//			width : 170,
//			align : 'center',
//			formatter : acctionFomatter
//		} ] ]
//	});
//}

function compare(str) {
	if ('' != str && null != str) {
		return str.indexOf('正常') >= 0 || str.indexOf('结清') >= 0 || str.indexOf('逾期') >= 0;
	}
	return false;
}

// 办理
function handleInfo(loanNo) {
	jDialog.open({url:ctx.rootPath() + "/search/handle/" + loanNo + "/0"});
}

//日志备注对话框
function logInfoDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
		width : 1100,
		height : 800
	});
}

/**
 * 日志
 * 
 * @Author luting
 * @date 2017年3月2日
 */
function integratedQueryLogDialog(loanNo) {
	var integratedQueryLogDialog = ctx.dialog({
		title : '日志',
		modal : true,
		width : 800,
		height : 600,
		href : ctx.rootPath() + '/search/integratedQueryLog/' + loanNo,
		buttons : [ {
			text : '关闭',
			iconCls : 'fa fa-reply',
			handler : function() {
				integratedQueryLogDialog.dialog("close");
			}
		} ]
	});
}
/**
 * 还款明细
 * 
 * @Author LuTing
 * @date 2017年3月2日
 */
function integratedQueryViewDetailsDialog(loanNo) {
	jDialog.open({url: ctx.rootPath() + "/search/integratedQueryViewDetails/" + loanNo});
}

/**
 * 通过查询
 */
function integratedQueryPass() {
	if ($("#integratedQuyer_form").form("validate")) {
		var queryParams = $("#integratedQuyer_form").serializeObject();
		// 转换申请类型
		var at = queryParams.applyType;
		if (at != null && at instanceof Array && at.length > 1) {
			var s = at.join("|");
			queryParams.applyType = s;
		}
		// 转换案件标识
		var ci = queryParams.caseIdentifyList;
		if (ci != null && ci instanceof Array && ci.length > 1) {
			var t = ci.join(",");
			queryParams.caseIdentifyList = t;
		}
		var startDate = queryParams.startTime;
		var endDate = queryParams.endTime;
		if(!((isEmpty(startDate) && isEmpty(endDate)) || (isNotNull(startDate) && isNotNull(endDate)))){
			$.info("提示", "查询时间起和查询时间止必须同时不为空！");
			return;
		}
		//校验地址搜索条件
		var addressList = queryParams.addressList;
		var address = queryParams.address;
		if(isNotNull(addressList) && isEmpty(address)){
			$.info("提示", "请填写地址详细信息！");
			return;
		}
		//转换地址类型
		if (addressList != null && addressList instanceof Array && addressList.length > 1) {
			var s = addressList.join(",");
			queryParams.addressList = s;
		}
		if(isEmpty(queryParams.name) && isEmpty(queryParams.idNo)
				&& isEmpty(queryParams.loanNo) && isEmpty(queryParams.phone) && isEmpty(queryParams.tel) 
				&& isEmpty(queryParams.corpName) && isEmpty(queryParams.caseIdentifyList) && isEmpty(queryParams.startTime)
				&& isEmpty(queryParams.endTime) && isEmpty(queryParams.applyType) && isEmpty(queryParams.addressList)){
			$.info("提示", "至少输入一个查询条件!", "warning");
			return;
		}
		queryParams.flag = true;
		$("#integratedQuery_userList_datagrid").datagrid('load',queryParams);
	}
}
