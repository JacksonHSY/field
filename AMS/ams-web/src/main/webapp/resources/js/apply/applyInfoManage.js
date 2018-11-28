//@ sourceURL=applyInfoManage.js
var passSearchOrNot = false;
var denySearchOrNot = false;
var flag = false; // 用于标记接口是否已经返回
$(function() {
	
	// 初始化通过件datagrid
	initApprovalStatePassDatagrid();
	// 初始化拒绝件datagrid
	initApprovalStateDenyDatagrid();
	
	
	$("#pass_form_Id").mouseover(function(){
		passSearchOrNot = true;
	});
	$("#pass_form_Id").mouseout(function(){
		passSearchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && passSearchOrNot) {
			applyInfoManagePass();
		}
	});
	
	
	$("#no_pass_form_id").mouseover(function(){
		denySearchOrNot = true;
	});
	$("#no_pass_form_id").mouseout(function(){
		denySearchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && denySearchOrNot) {
			applyInfoManageNoPass();
		}
	});

});

function tab_change(title, index) {
	if (1 == index) {
		deny_Query();
	}else if(0 == index){
		pass_Query();
	}
}
/**
 * 通过件datagrid
 * 
 * @Author
 * @date 2017年3月7日
 */
var passDatagridData;
function initApprovalStatePassDatagrid() {
	$("#approvalStateQuery_pass_datagrid").datagrid({
		url : ctx.rootPath() + '/applyDoc/index',
		queryParams : {
			statu : 1
		},
		onLoadSuccess : function(data) {
			passDatagridData = data;
			$("#approvalStateQuery_pass_datagrid").datagrid('resize');
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		remoteSort : true,
		columns : [ [ {
			field : 'loanNo',
			title : '借款编号',
			width : 170,
			sortable : true,
		}, {
			field : 'owningBanchName',
			title : '进件营业部',
			width : 170,
			sortable : true,
		}, {
			field : 'auditEndTime',
			title : '提交时间',
			width : 140,
			formatter : ctx.getDate,
			sortable : true,
		}, {
			field : 'customerName',
			title : '申请人姓名',
			width : 140,
			sortable : true,
		}, {
			field : 'cdNo',
			title : '身份证号码',
			width : 80,
			sortable : true,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 140,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'accLmt',
			title : '审批额度',
			width : 140,
			sortable : true,
			formatter : numberFomatter
		}, {
			field : 'accTerm',
			title : '审批期限',
			width : 90,
			sortable : true,
			order : 'desc'
		}, {
			field : 'checkPersonName',
			title : '初审员',
			width : 140,
			sortable : false,
		}, {
			field : 'finalPersonName',
			title : '终审员',
			width : 140,
			sortable : false,
			order : 'asc',
			formatter : function(value, data, index){
				var approvalPersonName = data.approvalPersonName;
				if(null != approvalPersonName && '' != approvalPersonName){
					return approvalPersonName;
				}
				return data.finalPersonName;
			}
		}, {
			field : 'accDate',
			title : '最后终审提交时间',
			width : 150,
			formatter : formatDate,
			sortable : true,
		}, {
			field : 'action',
			title : '操作',
			width : 180,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=InitApplyInfoManagePassDenyDialog("' + index + '") >拒绝</a>';
				action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick=InitApplyInfoManagePassUpdateDialog("' + index + '")>修改</a>';

				return action;
			}
		} ] ]
	});
}

/**
 * 拒绝件datagrid
 * 
 * @Author LuTing
 * @date 2017年3月7日
 */
var rejectDatagridData;
function initApprovalStateDenyDatagrid() {
	$("#approvalStateQuery_deny_datagrid").datagrid({
		url : ctx.rootPath() + '/applyDoc/index',
		queryParams : {
			statu : 2
		},
		onLoadSuccess : function(data) {
			rejectDatagridData = data;
			$("#approvalStateQuery_deny_datagrid").datagrid('resize');
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		remoteSort : true,
		columns : [ [ {
			field : 'loanNo',
			title : '借款编号',
			width : 140,
			sortable : true,
		}, {
			field : 'owningBanchName',
			title : '进件营业部',
			width : 170,
			sortable : true,
		}, {
			field : 'auditEndTime',
			title : '提交时间',
			width : 140,
			formatter : ctx.getDate,
			sortable : true,
		}, {
			field : 'customerName',
			title : '申请人姓名',
			width : 140,
			sortable : true,
		}, {
			field : 'cdNo',
			title : '身份证号码',
			width : 140,
			sortable : true,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 140,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'primaryReason',
			title : '一级原因',
			width : 180,
			sortable : true,
		}, {
			field : 'secodeReason',
			title : '二级原因',
			width : 180,
			sortable : true,
		}, {
			field : 'rejectPersonName',
			title : '最后拒绝人',
			width : 140,
			sortable : false,
		}, {
			field : 'rejectPersonDate',
			title : '最后拒绝时间',
			width : 150,
			formatter : formatDate,
			sortable : true,
		}, {
			field : 'action',
			title : '操作',
			width : 150,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=InitApplyInfoManageDenyUpdateDialog("' + index + '")>修改</a>';
				return action;
			}
		} ] ]
	});
}

/**
 * 通过件--拒绝--弹出框
 * 
 */
var refusePassedDialog;
function InitApplyInfoManagePassDenyDialog(index) {
	var needRefuseData = passDatagridData.rows[index];
	refusePassedDialog = ctx.dialog({
		title : "通过件拒绝",
		href : ctx.rootPath() + '/applyDoc/applyInfoDeny/'+needRefuseData.rtfStatus,
		modal : true,
		width : 950,
		height : 250,
		buttons : [ {
			id : 'refusePassedButton1',
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
                    refuseSaveApplyDoc(needRefuseData);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				refusePassedDialog.dialog("close");
				// 刷新
				pass_Query();
			}
		} ]
	});
}

/**
 * 通过件拒绝
 */
function refuseSaveApplyDoc(needRefuseData) {
	var obj = $("#pass_deny_Form").serializeObject();
	var b = $("#pass_deny_Form").form('validate');// 验证
	if (!b) {
		$.info("提示", "请选择必填项！", "warning");
		return false;
	}
	$('#refusePassedButton1').linkbutton('disable');
	
	var timestamp = Date.parse(new Date());
	needRefuseData.primaryReason = obj.primaryReason;
	needRefuseData.secodeReason = obj.secodeReason;
	needRefuseData.primaryReasonText = $("#first_deny_Id").combobox("getText");
	needRefuseData.secodeReasonText = $("#end_deny_Id").combobox("getText");
	needRefuseData.remark = obj.remark;
	var params = needRefuseData;
	api = ctx.rootPath() + '/applyDoc/applyInfoDenySubmit' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		success : function(data) {
			if (data) {
				if (data.success) {
					refusePassedDialog.dialog("close");
					pass_Query();
				}
				$.info("提示", data.messages);
			} else {
				$.info("提示", data.messages, "error");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			$.info("警告", '系统繁忙，请稍后再试！', "warning");
		}
	});
}

/**
 * 通过件--修改--弹出框
 */
var updatePassedDialog;
function InitApplyInfoManagePassUpdateDialog(index) {
	var needEditData = passDatagridData.rows[index];
	updatePassedDialog = ctx.dialog({
		title : "修改审批信息",
		href : ctx.rootPath() + '/applyDoc/applyInfoUpdate',
		queryParams : {
			loanNo : needEditData.loanNo,
			productCode : needEditData.productCode,
			accLmt : needEditData.accLmt,
			accTerm : needEditData.accTerm
		},
		modal : true,
		width : 750,
		height : 300,
		buttons : [ {
			id : 'updatePassedButton1',
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
                if(flag){ // 接口是否已经返回
                    saveApplyDoc(needEditData);
                }
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				updatePassedDialog.dialog("close");
				// 刷新
				pass_Query();

			}
		} ]
	});
}

/**
 * 通过件修改
 * @param needEditData
 */
function saveApplyDoc(needEditData) {
	var oldAccLmt = Math.round($("#old_acc_lmt").html().replace(',',''));
	var oldAccTerm = $("#old_acc_term").html();
	var formData = $("#applyInfoManage_pass_update_Form").serializeObject();
	if(oldAccLmt == formData.accLmt && oldAccTerm == formData.accTerm ){
		$.info("提示", "请修改内容后提交！", "warning");
		return false;
	}

	var result = $("#applyInfoManage_pass_update_Form").form('validate');// 验证
	if (result) {
		// 根据产品名、审批金额、审批期限、渠道编号、是否费率优惠客户标识，试算该笔借款的合同金额
		var loanContractTrail = coreApi.getLoanContractTrail(vm.applyDoc.productCode, formData.accLmt, formData.accTerm , '00001', vm.basicInfoJson.ifPreferentialUser);
        if(isEmpty(loanContractTrail)){
        	console.log('调用核心合同试算接口失败，无法提交修改');
        	return false;
		}

		var residualPackMoney = 0;
		if (vm.basicInfoJson.applyType == 'TOPUP') {	// 如果是TOPUP客户，则根据身份证和姓名，查询上笔借款未结清金额
            residualPackMoney  = coreApi.getResidualPactMoney(vm.basicInfoJson.name, vm.basicInfoJson.idNo);
        }
		var contractAmtLimit = vm.contractAmtLimit - residualPackMoney;	// 合同金额上限 - 未结清的金额
		if(isNotNull(loanContractTrail) && loanContractTrail.pactMoney > contractAmtLimit){
            $.info("提示", "客户合同金额总和超过"+ vm.contractAmtLimit + "，请修改审批额度或期限！", "warning");
            return false;
		}
		$('#updatePassedButton1').linkbutton('disable');
		
        var timestamp = Date.parse(new Date());
        needEditData.accLmt = formData.accLmt;
        needEditData.accTerm = formData.accTerm;
        needEditData.remark = formData.remark;
        var params = needEditData;
        $.ajax({
            url : ctx.rootPath() + '/applyDoc/applyInfoUpdateSubmit' + "?timestamp=" + timestamp,
            dataType : 'json',
            method : 'POST',
            data : params,
            success : function(data) {
                if (data.success) {
                    $.info('提示', data.messages, 'info');
                    updatePassedDialog.dialog("close");
                    pass_Query();
                }else{
                    $.info("提示", data.messages, 'warning');
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
                $.info("警告", '系统繁忙，请稍后再试！', "warning");
            }
        });
	}
}

/**
 * 拒绝件--修改--弹出框
 */
var updateRefusedDialog;
function InitApplyInfoManageDenyUpdateDialog(index) {
	var needEditData = rejectDatagridData.rows[index];
	updateRefusedDialog = ctx.dialog({
		title : "拒绝件修改",
		href : ctx.rootPath() + '/applyDoc/applyInfoDenyReasonUpdate/'+needEditData.rtfStatus,
		queryParams : {
			loanNo : needEditData.loanNo,
			primaryReason : needEditData.primaryReasonCode,
			primaryReasonText : needEditData.primaryReason,
			secodeReason : needEditData.secodeReasonCode,
			secodeReasonText : needEditData.secodeReason,
			blackList : needEditData.blackList
		},
		modal : true,
		width : 950,
		height : 300,
		buttons : [ {
			id:'updateRefusedButton1',
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				denyRefuseUpdate(needEditData);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				updateRefusedDialog.dialog("close");
				// 刷新
				deny_Query();
			}
		} ]
	});
}

/**
 * 拒绝件修改
 */
function denyRefuseUpdate(needEditData) {
	var oldPrimaryReason = needEditData.primaryReasonCode;
	var oldSecodeReason = needEditData.secodeReasonCode;
	console.info("原一级原因，二级原因为："+oldPrimaryReason+","+oldSecodeReason);
	var obj = $("#deny_update_Form").serializeObject();
	obj.secodeReason = obj.secodeReason || '';
	console.info("修改后的一级原因，二级原因为："+obj.primaryReason+","+obj.secodeReason);
	if(oldPrimaryReason == obj.primaryReason && oldSecodeReason == obj.secodeReason ){
		$.info("提示", "请修改内容后提交！", "warning");
		return;
	}
	var b = $("#deny_update_Form").form('validate');// 验证
	if (!b) {
		$.info("提示", "请选择必填项！", "error");
		return false;
	}
	// validate done -> button disable -> ajax
	$('#updateRefusedButton1').linkbutton('disable');
	
	var timestamp = Date.parse(new Date());
	var oldFirstReasonDendy = needEditData.primaryReasonCode;// 原一级拒绝原因code
	needEditData.primaryReason = obj.primaryReason;
	needEditData.secodeReason = obj.secodeReason;
	needEditData.primaryReasonText = $("#reject_apply_first_deny_Id").combobox("getText");
	needEditData.secodeReasonText = $("#reject_apply_end_deny_Id").combobox("getText");
	needEditData.remark = obj.remark;
	var params = needEditData;
	params.firstReasonDendy = oldFirstReasonDendy;
	api = ctx.rootPath() + '/applyDoc/applyInfoDenyReasonUpdateSubmit' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		success : function(data) {
			if (data) {
				if (data.success) {
					updateRefusedDialog.dialog("close");
					deny_Query();
				}
				$.info("提示", data.messages);
			} else {
				$.info("提示", data.messages, "error");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			$.info("警告", '系统繁忙，请稍后再试！', "warning");
		}
	});

}

/**
 * 通过查询
 */
function applyInfoManagePass() {
	if($("#pass_form_Id").form("validate")){
		var param = $("#pass_form_Id").serializeObject();
		var startDate = param.startDate;
		var endDate = param.endDate;
		if(!((isEmpty(startDate) && isEmpty(endDate)) || (isNotNull(startDate) && isNotNull(endDate)))){
			$.info("提示", "查询时间起和查询时间止必须同时不为空！");
			return;
		}
		var differDays = getDifferDays(startDate, endDate);
		if (differDays > 30) {
			$.info("提示", "查询时间起和查询时间止不能相差超过30天！");
			return;
		}
		$("#approvalStateQuery_pass_datagrid").datagrid('load', param);
	}
}

// 通过件刷新
function pass_Query() {
	applyInfoManagePass();
}

/**
 * 不通过查询
 */
function applyInfoManageNoPass() {
	if($("#no_pass_form_id").form("validate")){
		var param = $("#no_pass_form_id").serializeObject();
		var startDate = param.startDate;
		var endDate = param.endDate;
		if(!((isEmpty(startDate) && isEmpty(endDate)) || (isNotNull(startDate) && isNotNull(endDate)))){
			$.info("提示", "查询时间起和查询时间止必须同时不为空！");
			return;
		}
		var differDays = getDifferDays(startDate, endDate);
		if (differDays > 30) {
			$.info("提示", "查询时间起和查询时间止不能相差超过30天！");
			return;
		}
		$("#approvalStateQuery_deny_datagrid").datagrid('load', param);
	}
}

//不通过件刷新
function deny_Query() {
	applyInfoManageNoPass();
}