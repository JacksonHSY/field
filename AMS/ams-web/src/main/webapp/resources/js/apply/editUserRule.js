//@ sourceURL = rulConfiguration.js
$(function() {
	$("#edit_user_rule_id").bind('click',function(){
		editUserRule();
	});
	
	$('#edit_refuse_tree').tree({
		checkbox : true,
		url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/reject',
		onLoadSuccess : function(){
			$('#edit_return_tree').tree({
				checkbox : true,
				url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/return',
				onLoadSuccess : function(){
					var userCode = $("#edit_userCode").val();
					post(ctx.rootPath() + "/ruleconfig/getUserRuleByUserCode/"+userCode, null, "json", function(data) {
						if(data.length > 0){
							$("#edit_userRuleId").val(data[0].id);
							for (var i = 0; i < data.length; i++) {
								if("XSCS-PASS" == data[i].ruleType){
									$("#edit_pass_checkbox").attr("checked","true");
								}else if("XSCS-REJECT" == data[i].ruleType){
									$("#edit_refuse_checkbox").attr("checked","true");
									edit_showRefuseTree();
									var refReasonIds = data[i].reasonIds.split(",");
									for (var m = 0; m < refReasonIds.length; m++) {
										var node = $('#edit_refuse_tree').tree('find', refReasonIds[m]);
										$('#edit_refuse_tree').tree('check', node.target);
									}
								}else if("XSCS-RETURN" == data[i].ruleType){
									$("#edit_retrun_checkbox").attr("checked","true");
									edit_showReturnTree();
									console.log(new Date());
									var retReasonIds = data[i].reasonIds.split(",");
									for (var n = 0; n < retReasonIds.length; n++) {
										var node = $('#edit_return_tree').tree('find', retReasonIds[n]);
										$('#edit_return_tree').tree('check', node.target);
									}
								}
							}
							$("#edit_return_tree").tree("collapseAll");
							$("#edit_refuse_tree").tree("collapseAll");
							var return_max_node = $('#edit_return_tree').tree('find', 0);
							$("#edit_return_tree").tree("expand", return_max_node.target);
							var refuse_max_node = $('#edit_refuse_tree').tree('find', 0);
							$("#edit_refuse_tree").tree("expand", refuse_max_node.target);
						}
					});
				}
			});
		}
	});
});


/**
 * 展示/隐藏 拒绝原因树
 */
function edit_showRefuseTree(){
	var refuseChecked = document.getElementById("edit_refuse_checkbox").checked;
	if(refuseChecked){
		$('#edit_refuse_tree').removeClass("display_none");
	}else{
		var refuses = $("#edit_refuse_tree").tree('getChecked');
		if(refuses.length != 0){
			for (var i = 0; i < refuses.length; i++) {
				var node = $('#edit_refuse_tree').tree('find', refuses[i].id);
				$('#edit_refuse_tree').tree('uncheck', node.target);
			}
		}
		$('#edit_refuse_tree').addClass("display_none");
	}
}

/**
 * 展示/隐藏 退回原因树
 */
function edit_showReturnTree(){
	var returnChecked = document.getElementById("edit_retrun_checkbox").checked;
	if(returnChecked){
		$('#edit_return_tree').removeClass("display_none");
	}else{
		var returns = $("#edit_return_tree").tree('getChecked');
		if(returns.length != 0){
			for (var i = 0; i < returns.length; i++) {
				var node = $('#edit_return_tree').tree('find', returns[i].id);
				$('#edit_return_tree').tree('uncheck', node.target);
			}
		}
		$('#edit_return_tree').addClass("display_none");
	}
}

/**
 * 保存收回的权限
 */
function editUserRule(){
	$("#edit_user_rule_id").unbind('click');
	var id = $("#edit_userRuleId").val();
	var userCode = $("#edit_userCode").val();
	var refuses = $("#edit_refuse_tree").tree('getChecked');
	var returns = $("#edit_return_tree").tree('getChecked');
	var passChecked = document.getElementById("edit_pass_checkbox").checked;
	var refuseChecked = document.getElementById("edit_refuse_checkbox").checked;
	var returnChecked = document.getElementById("edit_retrun_checkbox").checked;
	
	if(!passChecked && !refuseChecked && !returnChecked){
		$.info('提示', '收回权限请至少选择一项！', 'info');
		$("#edit_user_rule_id").bind('click',function(){
			editUserRule();
		});
		return;
	}
	
	if(refuseChecked && refuses.length == 0){
		$.info('提示', '拒绝原因需至少选择一条记录！', 'info');
		$("#edit_user_rule_id").bind('click',function(){
			editUserRule();
		});
		return;
	}
	
	if(returnChecked && returns.length == 0){
		$.info('提示', '退回原因需至少选择一条记录！', 'info');
		$("#edit_user_rule_id").bind('click',function(){
			editUserRule();
		});
		return;
	}
	
	var userRuleList = new Array();
	var userRule = new Object();
	userRule.id = id;
	userRule.userCode = userCode;
	var subList = new Array();
	if(passChecked){
		var userRuleSub = new Object();
		userRuleSub.ruleType = 'XSCS-PASS';
		subList.push(userRuleSub);
	}
	if(refuseChecked && refuses.length != 0){
		var userRuleSub = new Object();
		userRuleSub.ruleType = 'XSCS-REJECT';
		var reasonIds = "";
		var reasonCodes = "";
		for (var m = 0; m < refuses.length; m++) {
			reasonIds = reasonIds + "," + refuses[m].id;
			reasonCodes = reasonCodes + "," + refuses[m].code;
		}
		userRuleSub.reasonIds = reasonIds.substring(1);
		userRuleSub.reasonCodes = reasonCodes.substring(1);
		subList.push(userRuleSub);
	}
	if(returnChecked && returns.length != 0){
		var userRuleSub = new Object();
		userRuleSub.ruleType = 'XSCS-RETURN';
		var reasonIds = "";
		var reasonCodes = "";
		for (var m = 0; m < returns.length; m++) {
			reasonIds = reasonIds + "," + returns[m].id;
			reasonCodes = reasonCodes + "," + returns[m].code;
		}
		userRuleSub.reasonIds = reasonIds.substring(1);
		userRuleSub.reasonCodes = reasonCodes.substring(1);
		subList.push(userRuleSub);
	}
	userRule.subList = subList;
	userRuleList.push(userRule);
	console.log(userRuleList);
	
	var params = JSON.stringify(userRuleList);
	var timestamp = Date.parse(new Date());
	api = ctx.rootPath() + '/ruleconfig/editUserRule' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			if (data.success) {
				$.info("提示", "修改成功", "info", 1000, function() {
					closeHTMLWindow();
				});
			} else {
				$.info("提示", data.messages, "error");
				$("#edit_user_rule_id").bind('click',function(){
					editUserRule();
				});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			$.info("提示", "系统繁忙!", "error");
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		}
	});
	
}

