//@ sourceURL = rulConfiguration.js
$(function() {
	$("#save_user_rule_id").bind('click',function(){
		saveUserRule();
	});
	
	$('#user_tree').tree({
		checkbox : true,
		url : ctx.rootPath() + '/pmsApi/userOrgTree',
		onLoadSuccess : function(){
			$('#refuse_tree').tree({
				checkbox : true,
				url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/reject',
				onLoadSuccess : function(){
					$('#return_tree').tree({
						checkbox : true,
						url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/return',
						onLoadSuccess : function(){
							post(ctx.rootPath() + "/ruleconfig/getAllCollectedUserIds", null, "json", function(data){
								if(data.length != 0){
									//去掉已经被选择的初审员
									console.log(data);
									for (var i = 0; i < data.length; i++) {
										var node = $('#user_tree').tree('find', data[i]);
										if(isNotNull(node)){
											$('#user_tree').tree('remove', node.target);
										}
									}
								}
								for (var x = 0; x < 4; x++) {
									var unchecked = $('#user_tree').tree('getChecked', 'unchecked');
									if(unchecked.length != 0){
										for (var m = 0; m < unchecked.length; m++) {
											console.log("1");
											if(isEmpty(unchecked[m].attributes) && unchecked[m].children.length == 0){
												var node = $('#user_tree').tree('find', unchecked[m].id);
												$('#user_tree').tree('remove', node.target);
											}
										}
									}
								}
								$("#return_tree").tree("collapseAll");
								$("#refuse_tree").tree("collapseAll");
								var return_max_node = $('#return_tree').tree('find', 0);
								$("#return_tree").tree("expand", return_max_node.target);
								var refuse_max_node = $('#refuse_tree').tree('find', 0);
								$("#refuse_tree").tree("expand", refuse_max_node.target);
								$('#user_tree').removeClass("display_none");
							});
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
function showRefuseTree(){
	var refuseChecked = document.getElementById("refuse_checkbox").checked;
	if(refuseChecked){
		$('#refuse_tree').removeClass("display_none");
	}else{
		var refuses = $("#refuse_tree").tree('getChecked');
		if(refuses.length != 0){
			for (var i = 0; i < refuses.length; i++) {
				var node = $('#refuse_tree').tree('find', refuses[i].id);
				$('#refuse_tree').tree('uncheck', node.target);
			}
		}
		$('#refuse_tree').addClass("display_none");
	}
}

/**
 * 展示/隐藏 退回原因树
 */
function showReturnTree(){
	var returnChecked = document.getElementById("retrun_checkbox").checked;
	if(returnChecked){
		$('#return_tree').removeClass("display_none");
	}else{
		var returns = $("#return_tree").tree('getChecked');
		if(returns.length != 0){
			for (var i = 0; i < returns.length; i++) {
				var node = $('#return_tree').tree('find', returns[i].id);
				$('#return_tree').tree('uncheck', node.target);
			}
		}
		$('#return_tree').addClass("display_none");
	}
}

/**
 * 保存收回的权限
 */
function saveUserRule(){
	$("#save_user_rule_id").unbind('click');
	var checkedusers = $("#user_tree").tree('getChecked');
	var userIds = new Array();
	var users = new Array();
	if(checkedusers.length != 0){
		for (var t = 0; t < checkedusers.length; t++) {
			if(isNotNull(checkedusers[t].attributes)){
				var a = checkedusers[t].attributes.usercode;
				users.push(checkedusers[t].attributes.usercode);
				userIds.push(checkedusers[t].id);
			}
		}
	}
	var refuses = $("#refuse_tree").tree('getChecked');
	var returns = $("#return_tree").tree('getChecked');
	var passChecked = document.getElementById("pass_checkbox").checked;
	var refuseChecked = document.getElementById("refuse_checkbox").checked;
	var returnChecked = document.getElementById("retrun_checkbox").checked;
	
	if(users.length == 0){
		$.info('提示', '请至少选择一个初审员！', 'info');
		$("#save_user_rule_id").bind('click',function(){
			saveUserRule();
		});
		return;
	}
	
	if(!passChecked && !refuseChecked && !returnChecked){
		$.info('提示', '收回权限请至少选择一项！', 'info');
		$("#save_user_rule_id").bind('click',function(){
			saveUserRule();
		});
		return;
	}
	
	if(refuseChecked && refuses.length == 0){
		$.info('提示', '拒绝原因需至少选择一条记录！', 'info');
		$("#save_user_rule_id").bind('click',function(){
			saveUserRule();
		});
		return;
	}
	
	if(returnChecked && returns.length == 0){
		$.info('提示', '退回原因需至少选择一条记录！', 'info');
		$("#save_user_rule_id").bind('click',function(){
			saveUserRule();
		});
		return;
	}
	
	var userRuleList = new Array();
	for (var i = 0; i < users.length; i++) {
		var userRule = new Object();
		userRule.userCode = users[i];
		userRule.userId = userIds[i];
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
	}
	console.log(userRuleList);
	var params = JSON.stringify(userRuleList);
	var timestamp = Date.parse(new Date());
	api = ctx.rootPath() + '/ruleconfig/addUserRule' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			if (data.success) {
				$.info("提示", "新增成功", "info", 1000, function() {
					closeHTMLWindow();
				});
			} else {
				$.info("提示", data.messages, "error");
				$("#save_user_rule_id").bind('click',function(){
					saveUserRule();
				});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			$.info("提示", "系统繁忙!", "error");
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		}
	});
	
}

