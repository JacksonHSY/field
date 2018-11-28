//@ sourceURL=qualityReceive.js
$(function($) {
    // 添加提示信息
    post(ctx.rootPath() + "/ruleEngineParameter/getRuleEngineParameterLast/" + $("#qualityCheckId").val(), null, "json", function(data) {
        if (isNotNull(data) && isNotNull(data.engineHints)) {
            var messageList = data.engineHints.split("$");
            ruleEngineHintShow(messageList.length);
            $("#ruleEngineHint_number_div").html(messageList.length);
            for (var mes in messageList){
                $("#ruleEngineHint_div").find("ul").append("<li>" + messageList[mes] + "<br><span>" + moment(data.createdDate).format('YYYY-MM-DD HH:mm') + "</span></li>")
            }
        }
    });
	// 如果没有任何审核意见，则禁止点击质检意见
	window.setTimeout(function () {
		var applyHistory = $('#applyHistory').val();
		if(!applyHistory){
			$('#qualityOpinion').linkbutton('disable');
		}
	}, 500);
});

// 审批意见对话框
function finishApprovalOpinionDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/qualityControlDesk/qualityApprovalOpinion/' + loanNo});
}

// 央行征信对话框
function finishCentralBankCreditDialog(reportId, loanNo) {
	if (isNotNull(reportId)) {
		jDialog.open({
			url : ctx.rootPath() + '/firstApprove/centralBankCredit/' + loanNo,
			width : 1200
		});
	} else {
		$.info("提示", "未绑定央行报告!", "warning");
	}
}

// 外部征信对话框
function finishExternalCreditDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/firstApprove/externalCredit/' + loanNo});
}

// 客户信息对话框
function finishCustomerInfo(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/finishApprove/finishCustomerInfo/' + loanNo});
}

// 内部匹配对话框
function finishInsideMatchDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/finishApprove/finishInsideMatch/' + loanNo});
}

// 电核汇总对话框
function finishTelephoneSummaryDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/finishApprove/finishTelephoneSummary/' + loanNo  + '/2' + '?businessType=quality'});
}

// 算话征信对话框
function finishSuanHuaCreditDialog(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/creditzx/getSuanHuaCreditWindow/' + loanNo,
		height:750,
		width:1200,
		top:210
	});
}

//日志备注对话框
function logNotesInfoDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/qualityControlDesk/logNotesInfo/' + loanNo, width:800,height:600});
}

/**
 * 质检意见
 * @param loanNo
 */
function qualityOpinionDialog(loanNo,checkUser) {
	var flag = $("#flag").val();
	if(flag=="done"){
		jDialog.open({url:ctx.rootPath() + '/qualityControlDesk/queryQualityOpoin/' + loanNo, width:1100});
	}else{
		jDialog.open({url:ctx.rootPath() + '/qualityControlDesk/qualityOpinion?flag='+flag+'&loanNo='+loanNo+'&checkUser='+checkUser, width: 1000});
	}
}

/**
 * 质检日志
 * @param loanNo
 * lihuimeng
 */
function qualityLogDialog(loanNo) {
	jDialog.open({url:ctx.rootPath() + '/qualityControlDesk/qualityLog/' + loanNo, width:800, height:600});
}



