/***************复议办理信息查看按钮**********************/
// 客户信息对话框不标红
function customerInfoWithoutRed(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/finishApprove/customerInfoWithoutRed/' + loanNo,
        height: 750,
        top: 160,
        left: 260,
    });
}

// 内部匹配对话框
function reconsiderationInsideMatchDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/finishApprove/finishInsideMatch/' + loanNo,
        height: 750,
        top: 180,
        left: 280
    });
}

// 电核汇总对话框
function reconsiderationTelephoneSummaryDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/finishApprove/finishTelephoneSummary/' + loanNo + '/2',
        height: 800,
        width: 1400,
        top: 160,
        left: 260
    });
}

// 日志备注对话框
function reconsiderationLogNotesInfoDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
        width: 800,
        height: 750,
        top: 100,
        left: 200
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
 * 央行征信对话框
 *
 * @param reportId
 * @param loanNo
 */
function reconsiderationCentralBankCreditDialog(reportId, loanNo) {
    if (isNotNull(reportId)) {
        centralBankCreditOpenHTMLWindow(loanNo);
    } else {
        $.info("提示", "未绑定央行报告!");
    }
}
// 打开央行报告页面
function centralBankCreditOpenHTMLWindow(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/centralBankCredit/' + loanNo,
        width: 1200,
        top: 240,
        left: 340
    });
}


// 外部征信对话框
function reconsiderationExternalCreditDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/firstApprove/externalCredit/' + loanNo,
        height: 750,
        top: 220,
        left: 320
    });
}
// 算话征信对话框
function reconsiderationSuanHuaCreditDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/creditzx/getSuanHuaCreditWindow/' + loanNo,
        height: 750,
        width: 1200,
        top: 210
    });
}

// 审批意见对话框
function reconsiderationApprovalOpinionDialog(loanNo) {
    jDialog.open({
        url: ctx.rootPath() + '/finishApprove/finishApprovalOpinionWithoutAction/' + loanNo,
        height: 750,
        top: 180,
        left: 260
    });
}

/**********************复议办理**********************************/
// 显示复议退回弹框(F1/F2/F3退回共用)
function reconsiderationApprovalBackDialog(loanNo, personName) {
    $("#reconsideration_return_dialog").removeClass("display_none").dialog({
        title: "复议退回-" + personName,
        modal: true,
        width: 600,
        height: 300,
        onClose: function () {
            $("#reconsideration_return_dialog").find("form").form("reset");
            $("#reconsideration_return_dialog").find(".countSurplusText").html("剩余可输入500字");
        },
        buttons: [{
            text: '提交',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                reconsideration_handle_submit("#reconsideration_return_dialog",loanNo,"RETURN");
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#reconsideration_return_dialog").dialog("close");
            }
        }]
    });
}
/**
 * // F1提交或拒绝
 * @param loanNo
 * @param personName
 * @param operationType
 */
function reconsiderLevelOnePassORReject(loanNo, personName, operationType) {
    if ("SUBMIT" == operationType) {
        reconsiderLevelOnePassORRejectDialog(loanNo, personName, operationType);
    } else {
        $.messager.confirm('提示', '是否确认拒绝此次复议申请？', function (r) {
            if (r) {
                reconsiderLevelOnePassORRejectDialog(loanNo, personName, operationType);
            }
        });
    }
}
/**
 *  F1提交或拒绝弹出dialog
 * @param loanNo
 * @param personName
 * @param operationType
 */
function reconsiderLevelOnePassORRejectDialog(loanNo, personName, operationType) {
    var title = "";
    if ("SUBMIT" == operationType) {
        $("#reconsideration_oneSubmitOrReject_dialog").find("div.reject_text").removeClass("display_none");
        var reconsiderData = getReconsiderInfo(loanNo);
        var rejectValue =reconsiderData.primaryReason + (isNotNull(reconsiderData.secodeReason)? "-"+reconsiderData.secodeReason:"");
        $("#reconsideration_oneSubmitOrReject_dialog").find("div.reject_text").html("拒绝原因 : "+rejectValue);
        title = "复议提交-";
    } else {
        $("#reconsideration_oneSubmitOrReject_dialog").find("div.reject_text").addClass("display_none");
        title = "复议拒绝-"
    }
    $("#reconsideration_oneSubmitOrReject_dialog").removeClass("display_none").dialog({
        title: title + personName,
        modal: true,
        width: 600,
        height: 320,
        onClose: function () {
            $("#reconsideration_oneSubmitOrReject_dialog").find("form").form("reset");
            $("#reconsideration_oneSubmitOrReject_dialog").find(".countSurplusText").html("剩余可输入500字");
        },
        buttons: [{
            text: '提交',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                reconsideration_handle_submit("#reconsideration_oneSubmitOrReject_dialog",loanNo, operationType);
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#reconsideration_oneSubmitOrReject_dialog").dialog("close");
            }
        }]
    });
}

/**
 * F2同意复议、F4同意复议
 * @param loanNo
 */
function reconsiderPass(loanNo, personName) {
    //init
    passDialogInit("#reconsideration_pass_dialog",loanNo);
    $("#reconsideration_pass_dialog").removeClass("display_none").dialog({
        title: "复议通过-" + personName,
        modal: true,
        width: 680,
        height: 430,
        onClose: function () {
            $("#reconsideration_pass_dialog").find("form").form("reset");
            $("#reconsideration_pass_dialog").find(".countSurplusText").html("剩余可输入500字");
        },
        buttons: [{
            text: '提交',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                reconsideration_handle_submit("#reconsideration_pass_dialog",loanNo,"PASS");
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#reconsideration_pass_dialog").dialog("close");
            }
        }]
    });
}
/**
 *F2F4同意拒绝初始化
 * F2发送信审F2复核
 */
function passDialogInit(container,loanNo,operationType) {
    var reconsiderData = getReconsiderInfo(loanNo);
    // 回填差错人
    $(container).find(".errorPerson").html(reconsiderData.rejectPersonName);
    $(container).find("input[name='errorPerson']").val(reconsiderData.rejectPersonCode);
    // 回填原拒绝原因
    var originalReject = isNotNull(reconsiderData.secodeReason)? reconsiderData.primaryReason +"-" +reconsiderData.secodeReason:reconsiderData.primaryReason;
    $(container).find(".originalReject").html(originalReject);
    // 判断是否是系统自动
    var isSystemAuto = "系统自动" == reconsiderData.rejectPersonName;
    // 加载差错代码
    $(container).find('.errorCode').combobox({
        prompt: '差错代码',
        url: ctx.rootPath() + '/qualityControlDesk/quryErrorCode',
        editable: false,
        disabled:true,
        hasDownArrow: true,
        textField: 'code',
        valueField: 'id',
        filter: function (q, row) {
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) == 0;
        }
    });
    // 加载差错等级
    var data = [{"code": "E_000000", "name": "无差错"}, {"code": "E_000003", "name": "一般差错"}, {"code": "E_000004","name": "重大差错"}];
    $(container).find(".errorLevel").combobox({
        data: data,
        prompt: '差错等级',
        required: isSystemAuto,
        panelHeight: 'auto',
        disabled:isSystemAuto,
        editable: false,
        valueField: 'code',
        textField: 'name',
        onChange:function(newValue){
            if ("E_000003" == newValue || "E_000004" == newValue){
                $(container).find('.errorCode').combobox({disabled:false, required: true,})
            } else {
                $(container).find('.errorCode').combobox({disabled:true, required: false,value:''})
            }
        }
    })
    // F2提交信审 F2提交复核初审化
    if ("SENDAPPROVE" == operationType || "SUBMITREVIEW" == operationType) {
        // 加载F3复议员或F4复议员
        var ruleLeve = "F4";
        var applyHistory = null;
        if ("SENDAPPROVE" == operationType) {
            ruleLeve = "F3"
            if ("信审初审拒绝" == reconsiderData.rejectLink) {
                post(ctx.rootPath()+"/applyHistory/getApplyHistoryRejectByLoanNo/"+loanNo,null,"JSON",function(datas){
                   if (datas.success){
                       applyHistory = datas.data;
                   }
                },null,false);
            }
        }
       var $reconsiderOperator = $("#reconsider_reconsiderOperator").combobox({
            url:ctx.rootPath()+"/reconsiderStaff/getReconsiderStaffByRuleLevel/"+ruleLeve,
            required: true,
            panelHeight: 'auto',
            editable: false,
            valueField: 'staffCode',
            textField: 'staffName',
            onChange:function(newValue){
               $("#reconsider_reconsiderOperator").parents("td").find("input[name='reconsiderOperatorName']").val($reconsiderOperator.combobox("getText"));
            },
            onLoadSuccess:function(){
                if (isNotNull(applyHistory)) {
                    var items = $reconsiderOperator.combobox("options").finder.getRow($reconsiderOperator[0],applyHistory.approvalDirector);
                    if (isNotNull(items)){
                        $reconsiderOperator.combobox("select",applyHistory.approvalDirector);
                    }
                }
            }
        });
    }
}

/**
 * F2、F4拒绝复议
 * @param loanNo
 */
function reconsiderReject(loanNo, personName) {
    $.messager.confirm('提示', '是否确认拒绝此次复议申请？', function (r) {
        if (r) {
            //init
            passDialogInit("#reconsideration_reject_dialog",loanNo);
            $("#reconsideration_reject_dialog").removeClass("display_none").dialog({
                title: "复议拒绝-" + personName,
                modal: true,
                width: 680,
                height: 480,
                onClose: function () {
                    $("#reconsider_ifUpdateReject").parents("table").find("tr.updateReject").remove();
                    $("#reconsideration_reject_dialog").find("form").form("reset");
                    $("#reconsideration_reject_dialog").find(".countSurplusText").html("剩余可输入500字");
                },
                buttons: [{
                    text: '提交',
                    iconCls: 'fa fa-arrow-up',
                    handler: function () {
                        reconsideration_handle_submit("#reconsideration_reject_dialog",loanNo,"REJECT");
                    }
                }, {
                    text: '取消',
                    iconCls: 'fa fa-reply',
                    handler: function () {
                        $("#reconsideration_reject_dialog").dialog("close");
                    }
                }]
            });
        }
    });
}
/**
 * F2 发送信审或提交复核
 * @param loanNo
 * @param personName
 * @param operationType
 */
function reconsiderSendOrReviewDialog(loanNo, personName,operationType){
    // 初始化加载
    passDialogInit("#reconsideration_sendOrReview_dialog",loanNo,operationType);
    var title = "SENDAPPROVE"== operationType? "复议发送信审-":"复议提交复核-";
    $("#reconsideration_sendOrReview_dialog").removeClass("display_none").dialog({
        title:title + personName,
        modal: true,
        width: 680,
        height: 480,
        onClose: function () {
            $("#reconsideration_sendOrReview_dialog").find("form").form("reset");
            $("#reconsideration_sendOrReview_dialog").find(".countSurplusText").html("剩余可输入500字");
        },
        buttons: [{
            text: '提交',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                reconsideration_handle_submit("#reconsideration_sendOrReview_dialog",loanNo,operationType);
            }
        }, {
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#reconsideration_sendOrReview_dialog").dialog("close");
            }
        }]
    });
}
/////////////////////////////////////////////////////////////////////////
var reconsiderMarkSubmit = false; // 提交标识
/**
 * F1复议提交或拒绝
 * @param loanNo
 * @param operationType
 */
function reconsideration_handle_submit(container,loanNo, operationType) {
    if (!reconsiderMarkSubmit) {
        reconsiderMarkSubmit = true;
        if ($(container).find("form").form('validate')) {// 验证输入
            var params = $(container).find("form").serializeObject();
            if ("SUBMITREVIEW" == operationType || "SENDAPPROVE" == operationType ||("F3" ==params.reconsiderNode && "SUBMIT" == operationType)) {
                if (!isNotNull(params.reconsiderNodeState)){
                    reconsiderMarkSubmit = false;
                    $.info("提示","请选择同意或拒绝复议","warning",1000);
                    return false;
                }
            }
            params.loanNo = loanNo;
            params.reconsiderState = operationType;
            params.version = $("#reconsider_version").val();
            params.remark = $.trim(params.remark);
            post(ctx.rootPath() + '/reconsideration/reconsiderHandle', params, 'json', function (data) {
                reconsiderMarkSubmit = false;
                if (data.success) {
                    $(container).dialog("close");
                    window.opener.closeAndSelectTabs(null, "复议");
                    $.info("提示", data.firstMessage, "info", 500, function () {
                        closeHTMLWindow();
                    });
                } else {
                    $(container).dialog("close");
                    $.info("警告", data.messages, "warning", 2000);
                }
            });
        } else {
            reconsiderMarkSubmit = false;
        }
    }
}
/**
 * 判断是否需要修改拒绝原因
 */
function ifUpdateReject() {
   if ($("#reconsider_ifUpdateReject").is(':checked')){
       var html = $("<tr class='updateReject'><th>一级原因:</th><td><input class='input' width='198px' id='reconsider_reject_one' name='reconsiderRejectCodeOne'><input type='hidden' name='firstReasonText'></td><th>二级原因:</th><td><input id='reconsider_reject_two' class='input' name='reconsiderRejectCodeTwo'><input type='hidden' name='secondReasonText'></td></tr>");
       $("#reconsider_ifUpdateReject").parents("tr").after(html);
       initClassAReason("#reconsider_reject_one", "SQJXXWH", "reject", "#reconsider_reject_two",210);
       $()
   } else {
       $("#reconsider_ifUpdateReject").parents("table").find("tr.updateReject").remove();
   }
}

/**
 * 限制备注字数显示
 */
function limitReconsiderMemo(id,obj) {
    var value = $(obj).val();
    var remainLength = $.trim(value).length > 500 ? 0 : 500 - $.trim(value).length;
    if ($.trim(value).length> 500) {
        $("#"+id).textbox("setValue",value.substring(0,500))
    }
    $("#" + id).parents("td").find(".countSurplusText").html("剩余可输入" + remainLength + "字");
}

/**
 * 获取复议信息回填
 * @param loanNo
 * @param container
 * @returns {*}
 */
function getReconsiderInfo(loanNo) {
    var reconsiderInfo = window.opener.getReconsiderHandleData(loanNo,$("#reconsider_version").val());
    return reconsiderInfo;
}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------