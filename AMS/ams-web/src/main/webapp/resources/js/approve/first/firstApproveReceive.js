var markFirstNameOrIDNoChange = false;// 标记姓名身份证是否有改动
$(function () {
    // 判断当前订单是否被改派
    if ("true" == $("#first_flagUpdateVersion_hidden").val()) {
        $.messager.alert({
            title: '提示',
            msg: '您当前办理的借款单有可能已经被改派,请重新办理!',
            icon: 'info',
            onClose: function () {
                window.opener.closeAndSelectTabs(null, "初审工作台");
                window.opener.refreshFirstTaskNumber();// 刷新待办任务
                closeHTMLWindow();
            }
        });
    }
    // 判断规则引擎类型
    var firstRuleEngineType = $("#first_limitFirstSubmitDisable").val();
    if ("LIMIT" == firstRuleEngineType) {// 是否限制提交
        limitSubmitSetting("false");
    } else if ("REJECT" == firstRuleEngineType) {// 是否拒绝
        window.opener.closeAndSelectTabs(null, "初审工作台");
        window.opener.refreshFirstTaskNumber();// 刷新待办任务
        $.info("提示", "规则引擎拒绝", "info", 1000, function () {
            closeHTMLWindow();
        });
    } else {
        limitSubmitSetting("true");
    }
    ruleEngineHintShow($("#ruleEngineHint_number_div").html());// 规则引擎提示是否显示

    // 判断姓名身份证是否有改变
    post(ctx.rootPath() + "/bmsBasiceInfo/judgeCustomerOrIDNOChange", {
        loanNo: $("#first_approve_loanNo").html(),
        taskDefId: 'apply-check'
    }, "json", function (data) {
        if (data.success && data.data) {
            markFirstNameOrIDNoChange = true;
            $.messager.alert('提示', '姓名或身份证有更新，请关注征信信息、审批意见表、内部匹配相关内容');
        }
    });
    /*if (isNotNull($("#first_reportId_hidden").val())) {
     firstOppenAllWindows();// 初审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
     }else {
     $.info("提示", "未绑定央行报告!","warning",1000,function(){
     firstOppenAllWindows();// 初审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
     });
     }*/
});
// 初审办理依次打开[日志备注][审批意见表][电核汇总][客户信息][内部匹配][算话征信][外部征信][央行征信]。
function firstOppenAllWindows() {
    if ($("#first_logNotes_btn").length > 0) {	//日志备注
        $('#first_logNotes_btn').trigger("click");
    }
    if ($("#first_approvalOpinion_btn").length > 0) {// 审批意见表
        $('#first_approvalOpinion_btn').trigger("click");
    }
    if ($("#first_telephone_btn").length > 0) {// 电核汇总
        $('#first_telephone_btn').trigger("click");
    }
    if ($("#first_customerInfo_btn").length > 0) {// 客户信息
        $('#first_customerInfo_btn').trigger("click");
    }
    if ($("#first_insideMatch_btn").length > 0) {// 内部匹配
        $('#first_insideMatch_btn').trigger("click");
    }
    if ($("#first_suanHuaCredit_btn").length > 0) {// 算话征信
        $('#first_suanHuaCredit_btn').trigger("click");
    }
    if ($("#first_exteranlCredit_btn").length > 0) {// 外部征信
        $('#first_exteranlCredit_btn').trigger("click");
    }
    if ($("#first_centralBankCredit_btn").length > 0) {// 央行征信
        if (isNotNull($("#first_reportId_hidden").val())) {
            $('#first_centralBankCredit_btn').trigger("click");
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
function limitSubmitSetting(flag) {
    if ("false" == flag) {
        $("#firstApproval_submit_btn").addClass("submit_disable").unbind('click');
    } else {
        $("#firstApproval_submit_btn").removeClass("submit_disable").unbind('click').bind("click", function () {
            firstApprovalSubmitDialog();
        });
    }
}
/**
 * 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
 * @author
 * @param isAntifraud
 */
function firstSettingRuleEngineData(data) {
    if (isNotNull(data)) {
        // 更新欺诈可疑
        if ("Y" == data.isAntifraud) {
            $("#firstApprove_isAntifraud").html("欺诈可疑");
        } else {
            $("#firstApprove_isAntifraud").html("");
        }
        // 更新综合信息评级
        if (isNotNull(data.comCreditRating)) {
            $("#first_comCreditRating").html(data.comCreditRating);
        }
    }
}
// 审批意见对话框
function approvalOpinionDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/approvalOpinion/' + loanNo,
        height: 750,
        top: 180,
        left: 260
    });
}

// 央行征信报告信用不良提示
function centralBankCreditDialog(reportId, loanNo) {
    if (isNotNull(reportId)) {
        jDialog.open({
            url: ctx.rootPath() + '/firstApprove/centralBankCredit/' + loanNo,
            width: 1200,
            top: 240,
            left: 340
        });
    } else {
        $.info("提示", "未绑定央行报告!");
    }
}

// 外部征信对话框
function externalCreditDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/externalCredit/' + loanNo,
        height: 750,
        top: 220,
        left: 320
    });
}
// 算话征信
function suanHuaCreditDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/creditzx/getSuanHuaCreditWindow/' + loanNo,
        width: 1200,
        height: 750,
        top: 200,
        left: 300
    });
}
// 客户信息对话框
function customerInfoDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/customerInfo/' + loanNo,
        height: 750,
        top: 160,
        left: 260,
    });
}
// 内部匹配对话框
function insideMatchDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/insideMatch/' + loanNo,
        height: 750,
        top: 180,
        left: 280
    });
}
// 电核汇总对话框
function telephoneSummaryDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/telephoneSummary/' + loanNo,
        height: 800,
        width: 1400,
        top: 160,
        left: 260
    });
}
// 日志备注对话框
function logNotesInfoDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
        width: 800,
        height: 750,
        top: 100,
        left: 200
    });
}
/**
 * 挂起操作
 *
 * @Author LuTing
 * @date 2017年3月15日
 */
function firstApprovalHungDialog(loanNo, rtfNodeState) {
    if ("XSCS-HANGUP" != rtfNodeState) {
        initClassAReason("#first_hangList_combobox", "XSCS", "hang", "");// 初始化挂起原因
        $("#firstApproveReceive_hang_dialog").removeClass("display_none").dialog({
            title: "挂起原因",
            modal: true,
            width: 500,
            height: 300,
            buttons: [{
                text: '确定',
                id: 'firstApproveReceive_hang_btn',
                iconCls: 'fa fa-arrow-up',
                handler: function () {
                    if ($("#firstApproveReceive_hang_dialog").find("form").form("validate")) {
                        $('#firstApproveReceive_hang_btn').linkbutton('disable');
                        post(ctx.rootPath() + "/firstHandle/hang/" + loanNo + "/" + $("#fisrt_approve_version_hidden").val(), $("#firstApproveReceive_hang_dialog").find("form").serializeObject(), "json", function (data) {
                            if (data.success) {
                                $("#firstApproveReceive_hang_dialog").dialog("close");
                                window.opener.closeAndSelectTabs(null, "初审工作台");
                                window.opener.refreshFirstTaskNumber();// 刷新待办任务
                                $.info("提示", data.firstMessage, "info", 1000, function () {
                                    closeHTMLWindow();
                                });
                            } else if ("VERSIONERR" == data.type) {
                                $("#firstApproveReceive_hang_dialog").dialog("close");
                                window.opener.closeAndSelectTabs(null, "初审工作台");
                                window.opener.refreshFirstTaskNumber();// 刷新待办任务
                                $.info("提示", data.firstMessage, "info", 1000, function () {
                                    closeHTMLWindow();
                                });
                            } else {
                                $('#firstApproveReceive_hang_btn').linkbutton('enable');
                                $.info("警告", data.firstMessage, "warning");
                            }
                        });
                    }
                }
            }, {
                text: '取消',
                iconCls: 'fa fa-reply',
                handler: function () {
                    $("#firstApproveReceive_hang_dialog").dialog("close");
                }
            }],
            onClose: function () {
                $("#firstApproveReceive_hang_dialog").find("form").form("reset");
            }
        });
    } else {
        $.info("提示", "挂起件不可操作!");
        return false;
    }
}
/**
 * 提交
 *
 * @Author LuTing
 * @date 2017年3月15日
 */
var markSubmitButton = false;
function firstApprovalSubmitDialog() {
    if (!markSubmitButton) {
        markSubmitButton = true;
        // 判断是否提示姓名身份证改变
        if (markFirstNameOrIDNoChange) {
            $.messager.confirm('提示', '姓名或身份证有更新，请关注征信信息、审批意见表、内部匹配相关内容', function (r) {
                if (r) {
                    showFirstSumitDialog();
                } else {
                    markSubmitButton = false;
                }
            });
        } else {
            showFirstSumitDialog();
        }
    }
}
/***
 * 提交前调用规则引擎并是否可以提交
 * @param loanNo
 */
function showFirstSumitDialog() {
    var loanNo = $("#first_approve_loanNo").html();
    post(ctx.rootPath() + "/firstHandle/approvalHistory/" + loanNo, null, "json", function (result) {// 查询审批意见是否填写
        if (result.success) {
            if (result.data.approvalProductCd !="00025") {
                post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/XSCS02", null, "JSON", function (data) { // 调用规则引擎
                    if (data.success) {
                        firstSettingRuleEngineData(data.data);// 修改值
                        var msgList = data.messages;
                        if ("PASS" == data.firstMessage || "HINT" == data.firstMessage) {
                            $("#firstApproveReceive_submit_dialog").find("tr:first>td:first").html(result.data.approvalLimit);
                            $("#firstApproveReceive_submit_dialog").find("tr:first>td:last").html(result.data.approvalTerm);
                            $("#firstApproveReceive_submit_dialog").find("tr:eq(1)>td:first").html((parseFloat(result.data.approvalDebtTate || 0) * 100).toFixed(1) + "%");
                            $("#firstApproveReceive_submit_dialog").find("tr:eq(1)>td:last").html((parseFloat(result.data.approvalAllDebtRate || 0) * 100).toFixed(1) + "%");
                            $("#firstApproveReceive_submit_dialog").removeClass("display_none").dialog({
                                title: "提交审批信息",
                                modal: true,
                                width: 650,
                                height: 300,
                                buttons: [{
                                    text: '提交高审',
                                    id: 'firstApproveReceive_high_pass_btn',
                                    iconCls: 'fa fa-arrow-up',
                                    handler: function () {
                                        firstApprovalSubmit(loanNo, "HIGH-PASS")
                                    }
                                }, {
                                    text: '提交',
                                    id: 'firstApproveReceive_xscs_pass_btn',
                                    iconCls: 'fa fa-arrow-up',
                                    handler: function () {
                                        firstApprovalSubmit(loanNo, "XSCS-PASS")
                                    }
                                }, {
                                    text: '取消',
                                    iconCls: 'fa fa-reply',
                                    handler: function () {
                                        $("#firstApproveReceive_submit_dialog").dialog("close");
                                    }
                                }],
                                onClose: function () {
                                    markSubmitButton = false;
                                    $("#firstApproveReceive_submit_dialog").find("form").form("reset");
                                }
                            });
                        } else if ("REJECT" == data.firstMessage) {
                            window.opener.closeAndSelectTabs(null, "初审工作台");
                            window.opener.refreshFirstTaskNumber();// 刷新待办任务
                            $.info("提示", msgList[1], "info", 1000, function () {
                                closeHTMLWindow();
                            });
                        } else if ("LIMIT" == data.firstMessage) {
                            limitSubmitSetting("false");
                            markSubmitButton = false;
                        }
                        $("#ruleEngineHint_div").find("ul").html('');
                        for (var i = 2; i < msgList.length; i++) {
                            $("#ruleEngineHint_div").find("ul").append("<li>" + msgList[i] + "<br><span>" + msgList[1] + "</span></li>")
                        }
                        ruleEngineHintShow(msgList.length - 2);// 是否显示提示
                        $("#ruleEngineHint_number_div").html(msgList.length - 2);
                    } else {
                        markSubmitButton = false;
                        $.info("警告", data.firstMessage, "warning");
                    }
                });
            } else  {
                markSubmitButton = false;
                $.info("提示", "客户资产信息未认证，不可提交！", 'warning');
            }
        } else {
            markSubmitButton = false;
            $.info("提示", result.firstMessage, 'warning');
        }
    });
}


/**
 * 信审初审提交
 *
 * @author dmz
 * @date 2017年3月18日
 * @param flag
 */
var markSubmit = false;
function firstApprovalSubmit(loanNo, flag) {
    if (!markSubmit) {
        if ($("#firstApproveReceive_submit_dialog").find("form").form("validate")) {
            markSubmit = true;
            var formData = $("#firstApproveReceive_submit_dialog").find("form").serializeObject();
            $('#firstApproveReceive_xscs_pass_btn').linkbutton('disable');
            $('#firstApproveReceive_high_pass_btn').linkbutton('disable');
            post(ctx.rootPath() + "/firstHandle/submit", {
                loanNo: loanNo,	// 借款编号
                version: $("#fisrt_approve_version_hidden").val(),	// 申请件版本号
                remark: formData.remark,	// 备注信息
                rtfNodeState: flag			// 提交初审 or 提交高审
            }, "json", function (result) {
                markSubmit = false;
                if (result.success) {
                    $("#firstApproveReceive_submit_dialog").dialog("close");
                    window.opener.closeAndSelectTabs(null, "初审工作台");
                    window.opener.refreshFirstTaskNumber();// 刷新待办任务
                    $.info("提示", "提交成功", "info", 1000, function () {
                        closeHTMLWindow();
                    });
                } else if ("VERSIONERR" == result.type) {
                    $("#firstApproveReceive_submit_dialog").dialog("close");
                    window.opener.closeAndSelectTabs(null, "初审工作台");
                    window.opener.refreshFirstTaskNumber();// 刷新待办任务
                    $.info("提示", result.firstMessage, "info", 1000, function () {
                        closeHTMLWindow();
                    });
                } else {
                    $('#firstApproveReceive_xscs_pass_btn').linkbutton('enable');
                    $('#firstApproveReceive_high_pass_btn').linkbutton('enable');
                    $.info("警告", result.firstMessage, "warning");
                }
            });
        }
    }
}
// 退回
function firstApprovalBackDialog(loanNo,zdqqApply) {
    // 初始化退回原因
    initClassAReason("#first_firstReturn_combobox",1==zdqqApply ? 'CSRTNQQ': "XSCS", "return", "#first_secondReturn_combobox",260);
    $("#firstApproveReceive_return_dialog").removeClass("display_none").dialog({
        title: "退回审批信息",
        modal: true,
        width: 820,
        height: 320,
        buttons: [{
            text: '提交',
            id: 'firstApproveReceive_return_btn',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                if ($("#firstApproveReceive_return_dialog").find("form").form("validate")) {
                    $('#firstApproveReceive_return_btn').linkbutton('disable');
                    var params = $("#firstApproveReceive_return_dialog").find("form").serializeObject();
                    if (1 ==zdqqApply) {// 判断是否是前前进件
                        params = createMoneyReturnReason(params);
                    }
                    post(ctx.rootPath() + "/firstHandle/rollback/" + loanNo + "/" + $("#fisrt_approve_version_hidden").val(),{form:JSON.stringify(params) } , "json", function (data) {
                        if (data.success) {
                            $("#firstApproveReceive_return_dialog").dialog("close");
                            window.opener.closeAndSelectTabs(null, "初审工作台");
                            window.opener.refreshFirstTaskNumber();// 刷新待办任务
                            $.info("提示", data.firstMessage, "info", 1000, function () {
                                closeHTMLWindow();
                            });
                        } else if ("VERSIONERR" == data.type) {
                            $("#firstApproveReceive_return_dialog").dialog("close");
                            window.opener.closeAndSelectTabs(null, "初审工作台");
                            window.opener.refreshFirstTaskNumber();// 刷新待办任务
                            $.info("提示", data.firstMessage, "info", 1000, function () {
                                closeHTMLWindow();
                            });
                        } else {
                            $('#firstApproveReceive_return_btn').linkbutton('enable');
                            $.info("警告", data.firstMessage, "warning");
                        }
                    });
                }
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#firstApproveReceive_return_dialog").dialog("close");
            }
        }],
        onClose: function () {
            $("#firstApproveReceive_return_dialog").find("tr.markReturnReason:gt(0)").remove();
            $("#firstApproveReceive_return_dialog").find("form").form("reset");
        }
    });
}
// 拒绝
function firstApprovalRefuseDialog(loanNo) {
    initClassAReason("#first_firstRefuse_combobox", "XSCS", "reject", "#first_secondRefuse_combobox");
    $("#firstApproveReceive_refuse_dialog").removeClass("display_none").dialog({
        title: "拒绝审批信息",
        modal: true,
        width: 820,
        height: 250,
        buttons: [{
            text: '提交',
            id: 'first_firstRefuse_btn',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                if ($("#firstApproveReceive_refuse_dialog").find("form").form("validate")) {
                    $('#first_firstRefuse_btn').linkbutton('disable');
                    post(ctx.rootPath() + "/firstHandle/refuse/" + loanNo + "/" + $("#fisrt_approve_version_hidden").val() + "/" + $("#first_ApproveReceive_ConditionType").val(), $("#firstApproveReceive_refuse_dialog").find("form").serializeObject(), "json", function (data) {
                        if (data.success) {
                            $("#firstApproveReceive_refuse_dialog").dialog("close");
                            window.opener.closeAndSelectTabs(null, "初审工作台");
                            window.opener.refreshFirstTaskNumber();// 刷新待办任务
                            $.info("提示", data.firstMessage, "info", 1000, function () {
                                closeHTMLWindow();
                            });
                        } else if ("VERSIONERR" == data.type) {
                            $("#firstApproveReceive_refuse_dialog").dialog("close");
                            window.opener.closeAndSelectTabs(null, "初审工作台");
                            window.opener.refreshFirstTaskNumber();// 刷新待办任务
                            $.info("提示", data.firstMessage, "info", 1000, function () {
                                closeHTMLWindow();
                            });
                        } else {
                            $('#first_firstRefuse_btn').linkbutton('enable');
                            $.info("警告", data.firstMessage, "warning");
                        }
                    });
                }
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#firstApproveReceive_refuse_dialog").dialog("close");
            }
        }],
        onClose: function () {
            $("#firstApproveReceive_refuse_dialog").find("form").form("reset");
        }
    });
}

$('#first_firstRefuse_combobox').combobox({
    onSelect: function (record) {
        var conditionType = record.conditionType;// 偿还能力不足标志
        if (isNotNull(conditionType) && conditionType.indexOf('debtRatio_Y') >= 0) {
            $('#first_ApproveReceive_ConditionType').val(conditionType);
        } else {
            $('#first_ApproveReceive_ConditionType').val('none');
        }
        if (isNotNull(record.code) && 'RJ9999' == record.code) {//一级原因是其他时备注必填
            $('#firstApproveReceive_refuse_remark').textbox({
                required: true
            });
        } else {
            $('#firstApproveReceive_refuse_remark').textbox({
                required: false
            });
        }

    }
});

/**
 * 规则引擎拒绝
 *
 * @author dmz
 * @date 2017年7月31日
 */
function firstRuleEngineReject() {
    window.opener.closeAndSelectTabs(null, "初审工作台");
    window.opener.refreshFirstTaskNumber();// 刷新待办任务
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
        var dataSize = $("#first_firstReturn_combobox").combobox("getData").length;
        if ($("#firstApproveReceive_return_dialog").find(".markReturnReason").length < dataSize) {
            var html = $("<tr class='markReturnReason'><th>一级原因:</th><td><input name='firstReason' class='input'><input type='hidden' name='firstReasonText'></td><th>二级原因:</th><td><input name='secondReason' class='input' data-options=multiple:true,separator:'|'><input type='hidden' name='secondReasonText'></td><td><a href='javaScript:void(0);' onclick=addOrDeleteRetureReason('DELETE',this)><i class='fa fa-minus' aria-hidden='true'></i></a></td></tr>");
            initClassAReason(html.find("input[name='firstReason']"), "CSRTNQQ", "return", html.find("input[name='secondReason']"),260);
            $("#firstApproveReceive_return_dialog").find(".markReturnReason:last").after(html);
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
    $("#firstApproveReceive_return_dialog").find(".markReturnReason").each(function(ind,trObj){
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
   /* obj.firstReason=null;
    obj.firstReasonText=null;
    obj.secondReason=null;
    obj.secondReasonText=null;*/
    obj.returnReasons =returnReasons;
    return obj;
}