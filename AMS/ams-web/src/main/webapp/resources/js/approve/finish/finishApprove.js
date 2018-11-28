var finishCompleteSearchOrNot = false;
//是否禁止接单提示标识
var markSwith = true;
$(function() {
	$("#finishApproveCompleteDate").val(ctx.getDate());
	$("#finishApproveCompleteEndDate").val(ctx.getDate());

	initFinishApproveDatagrid("#finishApprove_priorityTask_datagrid", "优先队列", 1);
	initFinishApproveDatagrid("#finishApprove_normalTask_datagrid", "正常队列", 2);
	initFinishApproveDatagrid("#finishApprove_hangUpTask_datagrid", "挂起队列", 3, [ 3, 5, 7, 9 ]);

	// 加载当前用户是否接单
	post(ctx.rootPath() + "/staffOrderTask/getFinalIsAcceptOrder", null, "json", function(data) {
		if (data.success) {
			if ("Y" == data.data) {
				$('#finishApprove_orders_radio').attr("checked", "true");
			}
			$('#finishApprove_orders_radio').switchbutton({
				onText : '是',
				offText : '否',
				onChange : function(checked) {
					var info = checked == true ? '开启' : '禁止';
					if (markSwith) {
						markSwith = false;
						$("<div id='finishApprove_orders_radio_confirm'></div>").dialog({
							title:'提示',
	                        width: 302,
	                        height: 160,
							modal: true,
							onClose:function(){
	                            $(this).dialog("destroy");
							},
	                        onDestroy: function () {
								if (!markSwith) {
	                                $('#finishApprove_orders_radio').switchbutton("resize", {
	                                    checked : checked == true ? false : true
	                                });
	                                markSwith = true;
								}
							},
	                        content:'<div class="messager-body panel-body panel-body-noborder window-body" style="width: 266px;">' +
							'<div class="messager-icon messager-question"></div><div>确认'+info+'接单？</div><div style="clear:both;"></div>' +
							'<div class="messager-button">' +
							'<a href="javascript:void(0)" onclick="finishOkOrCancelButton(true,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">确定</span></span></a>' +
							'<a href="javascript:void(0)" onclick="finishOkOrCancelButton(false,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></div></div>'
						})
					}
				}
			});
		} 
	})
	initFinishApproveFinishDatagrid("#finishApprove_finishTask_datagrid", "已完成");
	
	$("#finish_approval_Query_Form").mouseover(function(){
		finishCompleteSearchOrNot = true;
	});
	$("#finish_approval_Query_Form").mouseout(function(){
		finishCompleteSearchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && finishCompleteSearchOrNot) {
			finishApproveQuery();
		}
	});
});


/**
 * 关闭开启提示确认或取消
 * @param flag
 */
function finishOkOrCancelButton(flag,checked) {
    markSwith = flag;
	$("#finishApprove_orders_radio_confirm").dialog("destroy");
	if (flag) { // 确认是否开放接单
        post(ctx.rootPath() + "/staffOrderTask/isFinalAcceptOrder", {
            ifAccept : checked == true ? 'Y' : 'N'
        }, "json", function(data) {
            if (data.success) {
                $.info("提示", "操作成功!");
            } else {
                markSwith = false;
                $('#finishApprove_orders_radio').switchbutton("resize", {
                    checked : checked == true ? false : true
                });
                $.info("警告", data.firstMessage, "warning");
       		 }
        });
	} else {
        $('#finishApprove_orders_radio').switchbutton("resize", {
            checked : checked == true ? false : true
        });
	}
    markSwith = true;
}


function fixWidth(percent)
{
    return (document.body.clientWidth - 5) * percent ;
}
// 待办任务列表
function initFinishApproveDatagrid(id, title, taskType, pageLists) {
	var pageSize = pageLists ? 3 : 10;
	var pageList = pageLists || [ 10, 20, 30, 40, 50 ];
	$(id).datagrid({
		url : ctx.rootPath() + '/finishApprove/finalWorkbench',
		title : title,
		striped : true,
		singleSelect : true,
		checkOnSelect : true,
		rownumbers : true,
		remoteSort : true,
		queryParams : {
			taskType : taskType
		},
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		pageSize : pageSize,
		pageList : pageList,
		onLoadSuccess : function(data) {
			$(id).datagrid('resize');
		},
		onDblClickRow : function(index, data) {
			handleFinishApprove(data.loanNo, data.version, data.personName, taskType);
		},
		rowStyler : function(index, row) {
			var a = moment();
			var b = moment(row.auditEndTime).add(3, "days");
			if (a.diff(b) >= 0) {
				return 'background-color:#ffffcc;color:#000;';
			}
		},
		columns : [ [ {
			field : 'Identification',
			title : '案件标识',
			width : fixWidth(0.09),
			align : 'center',
			formatter : formateCaseIdentification
		}, {
			field : 'personName',
			title : '申请人姓名',
			width : fixWidth(0.09),
			sortable : true,
			formatter: function(value,row,index) {
                if (getOldCardIdExists(row.idNo)) {
                    value = "*" + value;
                }
                return value;
			}
		}, {
			field : 'productName',
			title : '借款产品',
			width : fixWidth(0.09),
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'applyLmt',
			title : '申请额度',
			width : fixWidth(0.09),
			formatter : numberFomatter,
			sortable : true,
		}, {
			field : 'owningBrance',
			title : '营业部',
			width : fixWidth(0.09),
			sortable : true,
		}, {
			field : 'applyType',
			title : '申请类型',
			width : fixWidth(0.06),
			sortable : true,
		}, {
			field : 'refNodeStatus',
			title : '状态',
			hidden : taskType == 2 ? true : false,
			width : fixWidth(0.05),
			sortable : true,
			formatter : formatRtfNodeState,
			align : 'center'
		}, {
			field : 'checkPersonCode',
			title : '提交初审',
			width : taskType == 2 ? fixWidth(0.1) : fixWidth(0.05),
			sortable : false,
		}, {
			field : 'submitAuditDate',
			title : '提交时间',
			width : fixWidth(0.09),
			sortable : true,
			formatter : ctx.getDate
		}, {
			field : 'proxyGroupName',
			title : '初审小组',
			width : fixWidth(0.09),
			sortable : false,
			align : 'center'

		},
		/*
		 * { field : 'remark', title : '提交时间', width : 170, },
		 */
		{
			field : 'action',
			title : '操作',
			width : fixWidth(0.09),
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=handleFinishApprove("' + data.loanNo + '","' + data.version + '","' + data.personName + '","' + taskType + '")><div class="icon_14_div"><img class="icon_14"></div>办理</a>';
				return action;
			}
		} ] ]
	});
}

// 已完成列表
function initFinishApproveFinishDatagrid(id, title, pageLists) {
	var pageSize = pageLists ? 3 : 10;
	var pageList = pageLists || [ 10, 20, 30, 40, 50 ];
	$(id).datagrid({
		url : ctx.rootPath() + '/finishApprove/getCompletedTask',
		title : title,
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		pageSize : pageSize,
		pageList : pageList,
		remoteSort : true,
		onLoadSuccess : function(data) {
			// $(id).datagrid('resize');
		},
		onDblClickRow : function(index, data) {
			handle(data.loanNo);
		},
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'ifPri',
			title : '案件标识',
			width : 170,
			align : 'center',
			formatter : formateCaseIdentification,
			sortable : false,
		}, {
			field : 'personName',
			title : '申请人姓名',
			width : 120,
			sortable : true,
		}, {
			field : 'loanNo',
			title : '借款编号',
			width : 170,
			sortable : true,
		}, {
			field : 'owningBrance',
			title : '营业部',
			width : 170,
			sortable : true,
		}, {
			field : 'applyType',
			title : '申请类型',
			width : 100,
			sortable : true,
		}, {
			field : 'productName',
			title : '审批产品',
			width : 170,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'accLmt',
			title : '审批额度',
			formatter : numberFomatter,
			width : 100,
			sortable : true,
		}, {
			field : 'historNodeStatus',
			title : '终审操作',
			width : 100,
			formatter : function(value, data, index) {
				if (value == "XSZS-PASS" || value == "XSZS-SUBMIT-HIGH" || value == "XSZS-SUBMIT-BACK" || value == "XSZS-SUBMIT-APPROVAL") {
					return "通过";
				} else if (value == "XSZS-REJECT") {
					return "拒绝";
				} else if (value == "XSZS-RTNCS") {
					return "退回初审";
				} else if (value == "XSZS-RETURN") {
					return "退回门店";
				} else if (value == "XSZS-ZDQQRETURN"){
					return "退回前前";
				}
			},
			sortable : false,
		}, {
			field : 'refNodeStatus',
			title : '当前状态',
			width : 140,
			formatter : function(value, data, index) {
				return formatRtfNodeState(value, data, index);
			},
			sortable : false,
		}, {
			field : 'zsStartDate',
			title : '开始时间',
			width : 170,
			formatter : formatDate,
			sortable : false,
		}, {
			field : 'operationTime',
			title : '完成时间',
			width : 170,
			formatter : formatDate,
			sortable : true,
		}, {
			field : 'action',
			title : '操作',
			width : 60,
			align : 'center',
			formatter : function(value, data, index) {
				return '<a href="javaScript:void(0);" onclick=handle("' + data.loanNo + '")>查看详情</a>';
			},
		} ] ]
	});
}
/**
 * 查询
 * 
 * @author dmz
 * @date 2017年6月3日
 */
function finishApproveQuery() {
	if ($("#finish_approval_Query_Form").form("validate")) {
		var queryParams = $("#finish_approval_Query_Form").serializeObject();
		console.info(queryParams);
		var offStartDate = queryParams.offStartDate;
		var offEndDate = queryParams.offEndDate;
		var differDays = getDifferDays(offStartDate, offEndDate);
		if (differDays > 30) {
			$.info("提示", "开始日期和结束日期不能相差超过30天！");
			return;
		}
		$("#finishApprove_finishTask_datagrid").datagrid('load', queryParams);
	}
}

// 终审办理(带操作按钮)
function handleFinishApprove(loanNo, version, personName, taskType) {
	post(ctx.rootPath() + "/finishApprove/getLastVersionOfApplication/" + loanNo, null, "json", function(data) {
		if (version == data) {
			jDialog.open({url: ctx.rootPath() + "/finishApprove/finishApproveReceive/" + loanNo + "/" + version + "/" + taskType,top:-1});
		} else {
			$.info("警告", "该笔借款有可能被修改,请刷新工作台后重新办理!", "warning");
			window.closeAndSelectTabs(null, "终审工作台");
			window.refreshFinalTaskNumber();// 刷新待办任务
		}
	});
}

/**
 * 打开办理页面(不带操作按钮)
 * 
 * @param loanNo
 */
function handle(loanNo) {
	jDialog.open({url: ctx.rootPath() + "/search/handle/" + loanNo + "/4"});
}
// 调用规则引擎
/*
 * post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/XSZS01", null, "JSON", function(data) { if (data.success) { var msgList = data.messages; if ("PASS" == data.firstMessage) { var handleFinishApproveHTMLWindow = null; handleFinishApproveHTMLWindow = openHTMLWindow(handleFinishApproveHTMLWindow, ctx.rootPath() + "/finishApprove/finishApproveReceive/" + loanNo + "/" + version + "/" + taskType, 1400, 800); markFinishHandleLoanNo[loanNo] = handleFinishApproveHTMLWindow; } else if ("HINT" == data.firstMessage) { $.info("提示", msgList[1], "info", 1000, function() { var handleFinishApproveHTMLWindow = null; handleFinishApproveHTMLWindow = openHTMLWindow(handleFinishApproveHTMLWindow, ctx.rootPath() + "/finishApprove/finishApproveReceive/" + loanNo + "/" + version + "/" + taskType, 1400, 800); markFinishHandleLoanNo[loanNo] = handleFinishApproveHTMLWindow; }); } else if ("REJECT" == data.firstMessage) { $.info("提示", msgList[1]); closeAndSelectTabs(null, "终审工作台");
 * refreshFirstTaskNumber();// 刷新待办任务 } else if ("LIMIT" == data.firstMessage) { $.info("提示", msgList[1], "info", 1000, function() { var handleFinishApproveHTMLWindow = null; handleFinishApproveHTMLWindow = openHTMLWindow(handleFinishApproveHTMLWindow, ctx.rootPath() + "/finishApprove/finishApproveReceive/" + loanNo + "/" + version + "/" + taskType, 1400, 800); markFinishHandleLoanNo[loanNo] = handleFinishApproveHTMLWindow; }); } } else { $.info("警告", data.firstMessage, "warning"); } });
 */