var finishApproveReceive_approvalOpinion_dialog;
var markFinishNameOrIDNoChange = false;// 标记姓名身份证是否有改动

// 提交调用规则引擎
var markFinalSubmitBtn = false;
function finishApprovalSubmitDialog() {
	if (!markFinalSubmitBtn) {
		markFinalSubmitBtn = true;
        // 判断是否提示姓名身份证改变
        if (markFinishNameOrIDNoChange) {
            $.messager.confirm('提示', '姓名或身份证有更新，请关注征信信息、审批意见表、内部匹配相关内容', function(r){
                if (r){
                    showFinishSumitDialog();
                }else {
                    markFinalSubmitBtn = false;
				}
            });
        } else {
            showFinishSumitDialog();
        }
	}
}
/***
 * 提交前调用规则引擎并是否可以提交
 * @param loanNo
 */
function showFinishSumitDialog() {
    var loanNo = $("#finish_approve_loanNo").html();
    post(ctx.rootPath() + "/finishApprove/findCurApprovalOpinion/" + loanNo, null, "json", function(result) {// 查询审批意见是否填写
        if (result.success) {
            post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/XSZS02", null, "JSON", function(data) {// 调用规则引擎
                if (data.success) {
                    var msgList = data.messages;
                    finalSettingRuleEngineData(data.data);// 修改页面值
                    if ("PASS" == data.firstMessage || "HINT" == data.firstMessage) {
                        var taskType = $("#final_taskType").val();
                        $("#finishApproveReceive_submit_dialog").find("tr:first>td:first").html(result.data.approvalLimit);
                        $("#finishApproveReceive_submit_dialog").find("tr:first>td:last").html(result.data.approvalTerm);
                        $("#finishApproveReceive_submit_dialog").find("tr:eq(1)>td:first").html((parseFloat(result.data.approvalDebtTate || 0) * 100).toFixed(1) + "%");
                        $("#finishApproveReceive_submit_dialog").find("tr:eq(1)>td:last").html((parseFloat(result.data.approvalAllDebtRate || 0) * 100).toFixed(1) + "%");
                        // 如果是L4级别的终审员，提交高审按钮不要显示出来
                        if ("L4" == result.data.finalAuditLevel) {
                            $("#finishApproveReceive_submit_dialog").removeClass("display_none").dialog({
                                title : "提交审批信息",
                                modal : true,
                                width : 650,
                                height : 300,
                                buttons : [ {
                                    text : '提交',
                                    iconCls : 'fa fa-arrow-up',
                                    handler : function() {
                                        finishApproval_Submit(loanNo, taskType);
                                    }
                                }, {
                                    text : '取消',
                                    iconCls : 'fa fa-reply',
                                    handler : function() {
                                        $("#finishApproveReceive_submit_dialog").dialog("close");
                                    }
                                } ],
                                onClose : function() {
                                    markFinalSubmitBtn = false;
                                }
                            });
                        } else {
                            $("#finishApproveReceive_submit_dialog").removeClass("display_none").dialog({
                                title : "提交审批信息",
                                modal : true,
                                width : 600,
                                height : 300,
                                buttons : [ {
                                    text : '提交',
                                    iconCls : 'fa fa-arrow-up',
                                    handler : function() {
                                        finishApproval_Submit(loanNo, taskType);
                                    }
                                }, {
                                    text : '提交高审',
                                    iconCls : 'fa fa-arrow-up',
                                    handler : function() {
                                        finishApproval_SubmitHL(loanNo, taskType);
                                    }
                                }, {
                                    text : '取消',
                                    iconCls : 'fa fa-reply',
                                    handler : function() {
                                        $("#finishApproveReceive_submit_dialog").dialog("close");
                                    }
                                } ],
                                onClose : function() {
                                    markFinalSubmitBtn = false;
                                }
                            });
                        }
                    } else if ("REJECT" == data.firstMessage) {
                        $("#finishApproveReceive_submit_dialog").dialog("close");
                        window.opener.closeAndSelectTabs(null, "终审工作台");
                        window.opener.refreshFinalTaskNumber();// 刷新待办任务
                        $.info("提示", msgList[1], "info", 1000, function() {
                            closeHTMLWindow();
                        });
                    } else if ("LIMIT" == data.firstMessage) {
                        finalLimitSubmitSetting("false");
                        markFinalSubmitBtn = false;
                    }
                    $("#ruleEngineHint_div").find("ul").html('');
                    for (var i = 2; i < msgList.length; i++) {
                        $("#ruleEngineHint_div").find("ul").append("<li>" + msgList[i] + "<br><span>" + msgList[1] + "</span></li>")
                    }
                    ruleEngineHintShow(msgList.length - 2);// 是否显示提示
                    $("#ruleEngineHint_number_div").html(msgList.length - 2);
                } else {
                    markFinalSubmitBtn = false;
                    $.info("警告", data.firstMessage, "warning");
                }
            });
        } else {
            markFinalSubmitBtn = false;
            $.info("提示", result.firstMessage, "warning", 2000);
        }
    });
}



// 退回
function finishApprovalBackDialog(loanNo, taskType, personName,zdqqApply) {
	$("#finishApproveReceive_back_dialog").removeClass("display_none").dialog({
		title : "退回审批信息",
		modal : true,
		width : 820,
		height : 320,
		onBeforeOpen : function() {
			var type = $("#finishApproveReceive_back_dialog").find("input:checked").val();
			initClassAReason("#finishApproveReceive_back_ParentCode", type, "return", "#finishApproveReceive_back_ReasonCode",260);
		},
		buttons : [ {
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				finishApproval_Back(loanNo, taskType, personName,zdqqApply);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
                $("#finishApproveReceive_back_dialog").find("tr.markReturnReason:gt(0)").remove();
				$("#finishApproveReceive_back_dialog").dialog("close");
			}
		} ]
	});
}

$(function() {
	$(":radio").linkbutton({
		onClick : function() {
			var back_type = $(this).val();
			if (back_type == "ZSRTNCS") {
				initClassAReason("#finishApproveReceive_back_ParentCode", "ZSRTNCS", "return", "#finishApproveReceive_back_ReasonCode",260);
                $("#finishApproveReceive_back_dialog").find(".markIsAdd").html("&nbsp;&nbsp;");
                $("#finishApproveReceive_back_ReasonCode").combobox({multiple:false});
                $("#finishApproveReceive_back_dialog").find("tr.markReturnReason:gt(0)").remove();
			} else {
                if (1==$("#finish_remak_teturnType").val()) {
                    $("#finishApproveReceive_back_dialog").find(".markIsAdd").html('<a href="javaScript:void(0);" onclick=addOrDeleteRetureReason("ADD")><i class="fa fa-plus" aria-hidden="true"></i></a>');
                    $("#finishApproveReceive_back_ReasonCode").combobox({multiple:true,separator:'|'});
                    initClassAReason("#finishApproveReceive_back_ParentCode", "ZSRTNQQ", "return", "#finishApproveReceive_back_ReasonCode",260);
                } else {
                    initClassAReason("#finishApproveReceive_back_ParentCode", "ZSRTNLR", "return", "#finishApproveReceive_back_ReasonCode",260);
                }
			}
		}
	});
	// 判断规则引擎类型
	var finalRuleEngineType = $("#final_limitFinishSubmitDisable").val();
	if ("LIMIT" == finalRuleEngineType) {// 是否限制提交
		finalLimitSubmitSetting("false");
	} else if ("REJECT" == finalRuleEngineType) {// 是否拒绝
		window.opener.closeAndSelectTabs(null, "终审工作台");
		window.opener.refreshFinalTaskNumber();// 刷新待办任务
		$.info("提示", "规则引擎拒绝", "info", 1000, function() {
			closeHTMLWindow();
		});
	} else {
		finalLimitSubmitSetting("true");
	}
    // 判断姓名身份证是否有改变
	if (isNotNull($("#finish_approve_loanNo").html())) {// 区分终审办理页面和已完成页面
        post(ctx.rootPath()+"/bmsBasiceInfo/judgeCustomerOrIDNOChange",{loanNo:$("#finish_approve_loanNo").html(),taskDefId:'applyinfo-finalaudit'},"json",function (data) {
            if (data.success && data.data) {
                markFinishNameOrIDNoChange = true;
                $.messager.alert('提示','姓名或身份证有更新，请关注征信信息、审批意见表、内部匹配相关内容');
            }
        });
	}
	ruleEngineHintShow($("#ruleEngineHint_number_div").html());// 规则引擎提示是否显示
    /*if ($("#finish_reportId_hidden").length > 0) {// 区分终审办理页面和已完成页面
        if (isNotNull($("#finish_reportId_hidden").val())) {
            finishOppenAllWindows();// 终审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
        } else {
            $.info("提示", "未绑定央行报告!", "warning", 1000, function () {
                finishOppenAllWindows();// 终审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
            });
        }
    }*/
});

// 终审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
function finishOppenAllWindows() {
    if ($("#finish_logNotes_btn").length>0) {	//日志备注
        $('#finish_logNotes_btn').trigger("click");
    }
    if ($("#finish_approvalOpinion_btn").length>0) {// 审批意见表
        $('#finish_approvalOpinion_btn').trigger("click");
    }
    if ($("#finish_telephone_btn").length>0) {// 电核汇总
        $('#finish_telephone_btn').trigger("click");
    }
    if ($("#finish_customerInfo_btn").length>0) {// 客户信息
        $('#finish_customerInfo_btn').trigger("click");
    }
    if ($("#finish_insideMatch_btn").length>0) {// 内部匹配
        $('#finish_insideMatch_btn').trigger("click");
    }
    if ($("#finish_suanHuaCredit_btn").length>0) {// 算话征信
        $('#finish_suanHuaCredit_btn').trigger("click");
    }
    if ($("#finish_exteranlCredit_btn").length>0) {// 外部征信
        $('#finish_exteranlCredit_btn').trigger("click");
    }
    if ($("#finish_centralBankCredit_btn").length>0) {// 央行征信
        if (isNotNull($("#finish_reportId_hidden").val())) {
            $('#finish_centralBankCredit_btn').trigger("click");
        }
    }
}


/**
 * 规则引擎设置是否限制提交
 * 
 * @author dmz
 * @date 2017年7月5日
 * @param flag
 */
function finalLimitSubmitSetting(flag) {
	if ("false" == flag) {
		$("#finalApproval_submit_btn").addClass("submit_disable").unbind('click');
	} else {
		$("#finalApproval_submit_btn").removeClass("submit_disable").bind("click", function() {
			finishApprovalSubmitDialog();
		});

	}
}
/**
 * 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
 * @author
 * @param isAntifraud
 */
function finalSettingRuleEngineData(data) {
    if ("Y" == data.isAntifraud) {
        $("#finishApprove_isAntifraud").html("欺诈可疑");
    } else {
        $("#finishApprove_isAntifraud").html("");
    }
    // 更新综合信用评级
    if (isNotNull(data.comCreditRating)) {
        $("#finish_comCreditRating").html(data.comCreditRating);
    }

}
// 拒绝
function finishApprovalRefuseDialog(loanNo, taskType, personName) {
	$("#finishApproveReceive_refuse_dialog").removeClass("display_none").dialog({
		title : "拒绝审批信息",
		modal : true,
		width : 980,
		height : 280,
		onBeforeOpen : function() {
			initClassAReason("#finishApproveReceive_refuse_ParentCode", "XSZS", "reject", "#finishApproveReceive_refuse_ReasonCode");

			var $refuseReason = $(this).find('#finishApproveReceive_refuse_ParentCode');
			var $remark = $(this).find('#remark');

			var defaultOptions = $remark.data('options');
			$refuseReason.combobox({
				onSelect : function(record) {
					var code = record.code;
					// 如果一级拒绝原因为"其他"，则备注必填
					if (code != null && code === "RJ9999") {
						var newOptions = $.extend({}, $remark.data('options'), {
							required : true
						});
						$remark.textbox(newOptions);
					} else {
						var newOptions = $.extend({}, $remark.data('options'), {
							required : false
						});
						$remark.textbox(newOptions);
					}

					var conType = record.conditionType;// 偿还能力不足标志
					if (isNotNull(conType) && conType.indexOf('debtRatio_Y') >= 0) {
						$('#finish_ApproveReceive_conType').val(conType);
					} else {
						$('#finish_ApproveReceive_conType').val('none');
					}

				}
			});

		},
		onClose : function() {
			// 关闭弹窗时，备注必填属性去掉
			var $remark = $(this).find('#remark');
			var newOptions = $.extend({}, $remark.data('options'), {
				required : false
			});
			$remark.textbox(newOptions);
		},
		buttons : [ {
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				finishApproval_Refuse(loanNo, taskType, personName);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#finishApproveReceive_refuse_dialog").dialog("close");
			}
		} ]

	});
}

// 挂起
function hangUp(loanNo, rtfNodeState, taskType, personName) {
	if ("XSZS-HANGUP" == rtfNodeState) {
		$.info("提示", "挂起件不可操作!");
		return false;
	}
	initClassAReason("#finish_hangList_combobox", "XSZS", "hang", "");
	$("#finishApproveReceive_hang_dialog").removeClass("display_none").dialog({
		title : "挂起原因",
		modal : true,
		width : 500,
		height : 300,
		buttons : [ {
			text : '确定',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				finishApproval_HangUp(loanNo, taskType, personName);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#finishApproveReceive_hang_dialog").dialog("close");
			}
		} ],
		onClose : function() {
			$("#finishApproveReceive_hang_dialog").find("form").form("reset");
		}
	});
}

// 审批意见对话框
function finishApprovalOpinionDialog(loanNo, version) {
	post(ctx.rootPath() + "/finishApprove/getLastVersionOfApplication/" + loanNo, null, "json", function(data) {
		if (version == data) {
			jDialog.open({
				url : ctx.rootPath() + '/finishApprove/finishApprovalOpinion/' + loanNo,
				width : 1400,
				height : 780,
                top:180,
                left:260
			});
		} else {
			$.info("警告", "该笔借款有可能被修改,请重新办理!", "warning");
		}
	}, false);
}

// 审批意见对话框
function finishApprovalOpinionDialog_withoutAction(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/finishApprove/finishApprovalOpinionWithoutAction/' + loanNo,
		width : 1100,
		height : 800,
        top:120,
        left:220
	});
}

// 打开央行报告页面
function centralBankCreditOpenHTMLWindow(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/firstApprove/centralBankCredit/' + loanNo,
		width : 1200,
		height : 800,
		top:180,
        left:340
	});
}

/**
 * 央行征信对话框
 * 
 * @param reportId
 * @param loanNo
 */
function finishCentralBankCreditDialog(reportId, loanNo) {
	if (isNotNull(reportId)) {
		centralBankCreditOpenHTMLWindow(loanNo);
	} else {
		$.info("提示", "未绑定央行报告!");
	}
}

// 外部征信对话框
function finishExternalCreditDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/firstApprove/externalCredit/' + loanNo,
		width : 1400,
		height : 800,
		top:180,
        left:320,
	});
}
// 算话征信对话框
function finishSuanHuaCreditDialog(loanNo) {
    jDialog.open({
        url : ctx.rootPath() + '/creditzx/getSuanHuaCreditWindow/' + loanNo,
        height:750,
        width:1200,
        top:200,
        left:300,
    });
}

// 客户信息对话框不标红
function customerInfoWithoutRed(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/finishApprove/customerInfoWithoutRed/' + loanNo,
		width : 1100,
		height : 800,
        top:160,
        left:260
	});
}

// 客户信息对话框标红
function finishCustomerInfo(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/finishApprove/finishCustomerInfo/' + loanNo,
        height:750,
		top:160,
        left:260
	});
}

// 内部匹配对话框
function finishInsideMatchDialog(loanNo) {
    jDialog.open({
		url : ctx.rootPath() + '/finishApprove/finishInsideMatch/' + loanNo,
        height:750,
		top:180,
        left:280
	});
}

// 电核汇总对话框
function finishTelephoneSummaryDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/finishApprove/finishTelephoneSummary/' + loanNo + '/2',
        height:820,
		top:160,
        left:260
	});
}

//电核汇总对话框--联系人列表带号码查询列
function hasSearch_finishTelephoneSummaryDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/finishApprove/finishTelephoneSummary/' + loanNo + '/1',
        height:820,
        top:160,
        left:260
	});
}

// 日志备注对话框
function finishLogNotesInfoDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
		width : 1100,
		height : 750,
		top:180,
        left:300
	});
}

//复议日志备注对话框
function reviewLogDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/search/reviewLog/' + loanNo,
		width : 1100,
		height : 750,
		top:180,
        left:300
	});
}

/**
 * 返回终审
 */
function finishApprovalBackPage(personName) {
	$("#layout_container").tabs("close", personName + "-终审办理");
	if ($("#layout_container").tabs("exists", "终审工作台")) {
		$("#layout_container").tabs("select", "终审工作台");
	} else {
		addTabs('终审工作台', '/finishApprove/finishApprove');
	}
}

// 拒绝节点流转
function finishApproval_Refuse(loanNo, taskType, personName) {
	var title = $("#finishApproveReceive_refuse_dialog").dialog('options').title;
	if (title == '拒绝审批信息') {
		var validate = $("#finishApproveReceive_refuse_dialog").form('validate');
		if (validate) {
			var api = ctx.rootPath() + '/finishApprove/deny';
			var params = $('#finishApproveReceive_refuse_dialog').find("form").serializeObject();
			var conType = $("#finish_ApproveReceive_conType").val();
			params.loanNo = loanNo;
			params.taskType = taskType;
			params.conType = conType;
			params.version = $("#version_id").val();
			console.log(JSON.stringify(params));
			var callback = function(data) {
				if (data.success) {
					$("#finishApproveReceive_refuse_dialog").dialog("close");
					window.opener.closeAndSelectTabs(null, "终审工作台");
					window.opener.refreshFinalTaskNumber();// 刷新待办任务
					$.info("提示", "拒绝成功!", "info", 1000, function() {
						closeHTMLWindow();
					});
				} else {
					$.info("警告", data.messages, "warning");
				}
			};
			var error = function(XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				$.info("警告", "拒绝失败!", "warning", 2000);
			};
			post(api, params, 'json', callback, error);
		}
	}
}

// 退回节点流转
function finishApproval_Back(loanNo, taskType, personName,zdqqApply) {
	var title = $("#finishApproveReceive_back_dialog").dialog('options').title;
	if (title == '退回审批信息') {
		var validate = $("#finishApproveReceive_back_dialog").form('validate');
		var back_type = $("input[name='backType']:checked").val();
		if (back_type == null || back_type == "") {
			$.info("提示", "请选择退回类型！");
			return false;
		}
		var parentCode = $('#finishApproveReceive_back_ParentCode').combobox("getValue");
		var reasonCode = $('#finishApproveReceive_back_ReasonCode').combobox("getValue");
		if (reasonCode == null || reasonCode == "") {
			reasonCode = 0;
		}
		var remark = $("#back_remark").val();
		if (remark == null || remark == "") {
			remark = 0;
		}
		if (validate) {
			var params = $('#finishApproveReceive_back_dialog').find("form").serializeObject();
			params.loanNo = loanNo;
			params.taskType = taskType;
			params.version = $("#version_id").val();
            if (1 ==zdqqApply && "ZSRTNQQ"==back_type) {// 判断是否是前前进件
                params = createMoneyReturnReason(params);
            }
			post(ctx.rootPath() + '/finishApprove/rollback', {form:JSON.stringify(params)}, 'json', function(data) {
                if (data.success) {
                    $("#finishApproveReceive_back_dialog").dialog("close");
                    window.opener.closeAndSelectTabs(null, "终审工作台");
                    window.opener.refreshFinalTaskNumber();// 刷新待办任务
                    $.info("提示", "退回成功!", "info", 1000, function() {
                        closeHTMLWindow();
                    });
                } else {
                    $.info("警告", data.messages, "warning", 1000);
                }
            });
		}
	}
}

// 挂起节点流转
function finishApproval_HangUp(loanNo, taskType, personName) {
	var title = $("#finishApproveReceive_hang_dialog").dialog('options').title;
	if (title == '挂起原因') {
		var validate = $("#finishApproveReceive_hang_dialog").form('validate');
		if (validate) {
			post(ctx.rootPath() + "/finishApprove/getHangQueenStatus", null, "json", function(data) {
				if ("0" == data) {
					$.info("警告", "当前处理人没有配置挂起队列上限!", "warning");
				} else if ("1" == data) {
					$.info("警告", "已达挂起队列上限！", "warning");
				} else {
					var api = ctx.rootPath() + '/finishApprove/suspend';
					var params = $('#finishApproveReceive_hang_dialog').find("form").serializeObject();
					params.loanNo = loanNo;
					params.taskType = taskType;
					params.version = $("#version_id").val();
					var callback = function(data) {
						if (data.success) {
							$("#finishApproveReceive_hang_dialog").dialog("close");
							window.opener.closeAndSelectTabs(null, "终审工作台");
							window.opener.refreshFinalTaskNumber();// 刷新待办任务
							$.info("提示", "挂起成功!", "info", 1000, function() {
								closeHTMLWindow();
							});
						} else {
							$.info("警告", data.messages, "warning", 2000);
						}
					};
					var error = function(XMLHttpRequest, textStatus, errorThrown) {
						console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
						$.info("警告", "挂起失败!", "warning", 2000);
					};
					post(api, params, 'json', callback, error);
				}
			});
		}
	}
}

// 提交节点流转
var finalMarkSubmit = false;
function finishApproval_Submit(loanNo, taskType) {
	if (!finalMarkSubmit) {
		var title = $("#finishApproveReceive_submit_dialog").dialog('options').title;
		if (title == '提交审批信息') {
			var validate = $("#finishApproveReceive_submit_dialog").form('validate');
			if (validate) {
				finalMarkSubmit = true;
				var params = $('#finishApproveReceive_submit_dialog').find("form").serializeObject();
				params.loanNo = loanNo;
				params.taskType = taskType;
				params.version = $("#version_id").val();
				post(ctx.rootPath() + '/finishApprove/sumbit', params, 'json', function(data) {
					finalMarkSubmit = false;
					if (data.success) {
						$("#finishApproveReceive_submit_dialog").dialog("close");
						window.opener.closeAndSelectTabs(null, "终审工作台");
						window.opener.refreshFinalTaskNumber();// 刷新待办任务
						$.info("提示", "提交成功", "info", 1000, function() {
							closeHTMLWindow();
						});
					} else {
						$.info("警告", data.messages, "warning", 2000);
					}
				});
			}
		}
	}
}

// 提交高审节点流转
var finalMarkSubmitHL = false;
function finishApproval_SubmitHL(loanNo, taskType, personName) {
	if (!finalMarkSubmitHL) {
		var title = $("#finishApproveReceive_submit_dialog").dialog('options').title;
		if (title == '提交审批信息') {
			var validate = $("#finishApproveReceive_submit_dialog").form('validate');
			if (validate) {
				finalMarkSubmitHL = true;
				var params = $('#finishApproveReceive_submit_dialog').find("form").serializeObject();
				params.loanNo = loanNo;
				params.taskType = taskType;
				params.version = $("#version_id").val();
				post(ctx.rootPath() + '/finishApprove/sumbitHL', params, 'json', function(data) {
					finalMarkSubmitHL = false;
					if (data.success) {
						$("#finishApproveReceive_submit_dialog").dialog("close");
						window.opener.closeAndSelectTabs(null, "终审工作台");
						window.opener.refreshFinalTaskNumber();// 刷新待办任务
						$.info("提示", "提交高审成功!", "info", 1000, function() {
							closeHTMLWindow();
						});
					} else {
						$.info("警告", data.messages, "warning", 2000);
					}
				});
			}
		}
	}
}
/**
 * 规则引擎拒绝
 * 
 * @author dmz
 * @date 2017年7月31日
 */
function finalRuleEngineReject() {
	window.opener.closeAndSelectTabs(null, "终审工作台");
	window.opener.refreshFinalTaskNumber();// 刷新待办任务
	closeHTMLWindow();
}

/**
 *
 * 前前添加退回原因或者删除
 *
 * @param operationType-add-delete
 */
function addOrDeleteRetureReason(operationType,obj) {
    if ("ADD" == operationType) {
        var dataSize = $("#finishApproveReceive_back_ParentCode").combobox("getData").length;
        if ($("#finishApproveReceive_back_dialog").find(".markReturnReason").length < dataSize) {
            var html = $("<tr class='markReturnReason'><th>一级原因:</th><td><input name='firstReason' class='input'><input type='hidden' name='firstReasonText'></td><th>二级原因:</th><td><input name='secondReason' class='input' data-options=multiple:true,separator:'|'><input type='hidden' name='secondReasonText'></td><td><a href='javaScript:void(0);' onclick=addOrDeleteRetureReason('DELETE',this)><i class='fa fa-minus' aria-hidden='true'></i></a></td></tr>");
            initClassAReason(html.find("input[name='firstReason']"), "ZSRTNQQ", "return", html.find("input[name='secondReason']"), 260);
            $("#finishApproveReceive_back_dialog").find(".markReturnReason:last").after(html);
        }else {
            $.info("提示","原因不能超过一级原因总数");
        }
    } else {
        $(obj).parents("tr").remove();
    }
}
/**
 * 前前退回参数封装
 * @param obj
 */
function createMoneyReturnReason(obj) {
    var returnReasons = new Array();
    $("#finishApproveReceive_back_dialog").find(".markReturnReason").each(function(ind,trObj){
        var secondCodeArray =  $(trObj).find("input[name='secondReason'][value !='']");
        if (secondCodeArray.length >0) {
            var secondTextArray = ($(trObj).find("input[name='secondReasonText']").val()).substring(1).split("|");
            for(var i =0;i<secondCodeArray.length;i++) {
                var objReasons = new Object();
                objReasons.primaryReasonCode = $(trObj).find("input[name='firstReason']").val();// 一级原因码
                objReasons.primaryReason = $(trObj).find("input[name='firstReasonText']").val();// 一级原因文本
                objReasons.secondReasonCode = $(secondCodeArray[i]).val();// 二级原因码
                objReasons.secondReason = secondTextArray[i];// 二级原因文本

                returnReasons.push(objReasons);
            }
        } else {
            var objReasons = new Object();
            objReasons.primaryReasonCode = $(trObj).find("input[name='firstReason']").val();// 一级原因码
            objReasons.primaryReason = $(trObj).find("input[name='firstReasonText']").val();// 一级原因文本
            returnReasons.push(objReasons);
        }
    });
    /*obj.firstReason=null;
    obj.firstReasonText=null;
    obj.secondReason=null;
    obj.secondReasonText=null;*/
    obj.returnReasons =returnReasons;
    return obj;
}