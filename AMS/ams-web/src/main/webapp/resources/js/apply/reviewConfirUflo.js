$(function () {
    // 添加提示信息
    post(ctx.rootPath() + "/ruleEngineParameter/getRuleEngineParameterLast/" + $("#review_loanNo").html(), null, "json", function(data) {
        if (isNotNull(data) && isNotNull(data.engineHints)) {
            var messageList = data.engineHints.split("$");
            ruleEngineHintShow(messageList.length);
            $("#ruleEngineHint_number_div").html(messageList.length);
            for (var mes in messageList){
                $("#ruleEngineHint_div").find("ul").append("<li>" + messageList[mes] + "<br><span>" + moment(data.createdDate).format('YYYY-MM-DD HH:mm') + "</span></li>")
            }
        }
    });
});
/**
 * 返回
 * 按钮
 */
function toReturn(){
	closeHTMLWindow();
//	$("#layout_container").tabs("close", "复核办理");
//	if ($("#layout_container").tabs("exists", "复核办理")) {
//		$("#layout_container").tabs("select", "复核确认");
//	} else {
//		addTabs("复核确认","/applayManagement/reviewConfirmation");
//	}
}

/**
 * 确认
 * 按钮
 */
function toSubmitDialog(loanNo,checkPerson){
	 $.messager.confirm('复核确认', '是否确认通过？', function(r){
         if (r){
        	 ToSubmit(loanNo, checkPerson);
         }
     });
}

/**
 * 提交
 * @author 
 * @date 
 * @param 
 */
function ToSubmit(loanNo,checkPerson) {
	var obj = new Object();
	obj.loanNo = loanNo;
	obj.checkPerson = checkPerson;
	post(ctx.rootPath() + "/toreviewConfirm/submit/1", obj, "json", function(data) {
		if (data.success) {
			window.opener.closeAndSelectTabs(null,"复核确认");
			$.info("提示", "操作成功!", "info", 1000, function(){
				closeHTMLWindow();
			});
		} else {
			$.info("警告", data.messages, "warning");
		}
	});
}




/**
 * 退回
 * 按钮
 */
function toBackDialog(loanNo,checkPerson){
	// 初始化退回原因
//	initReason("#first_firstReturn_combobox", "#first_secondReturn_combobox", "RETURN");
//	initClassAReason("#first_firstReturn_combobox", "XSCS", "return","#first_secondReturn_combobox");
	$("#toReview_return_dialog").removeClass("display_none").dialog({
		title : "退回备注信息",
		modal : true,
		width : 450,
		height : 230,
		buttons : [{
			text : '提交',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				ToReturn(loanNo,checkPerson);
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#toReview_return_dialog").dialog("close");
			}
		} ],
		onClose : function() {
			$("#toReview_return_dialog").find("form").form("reset");
		}
	});
}

function ToReturn(loanNo, checkPerson) {
	if ($("#toReview_return_dialog").find("form").form("validate")) {
		var obj = $("#toReview_return_form").serializeObject();
		obj.loanNo = loanNo;
		obj.checkPerson = checkPerson;
		//obj.firstReasonText = $("#first_firstReturn_combobox").combobox("getText");
		//obj.secondReasonText = $("#first_secondReturn_combobox").combobox("getText");
		post(ctx.rootPath() + "/toreviewConfirm/submit/0", obj, "json", function(data) {
			if (data.success) {
				window.opener.closeAndSelectTabs(null,"复核确认");
				$("#toReview_return_dialog").dialog("close");
				$.info("提示", "退回成功!", "info", 1000, function(){
					closeHTMLWindow();
				});
			} else {
				$.info("警告", data.messages, "warning");
			}
		});
	}else{
		//$.info("警告", "请填写必填项!", "warning");
	}
}
