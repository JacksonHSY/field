$(function() {
	// 查询央行报告提示
	post(ctx.rootPath() + "/ruleEngineParameter/getRuleEngineParameterLast/" + $('#first_centralBankCredit_loanNo').val(), null, "json", function(data) {
		if (isNotNull(data) && isNotNull(data.creditReportHints)) {
			var message = "";
			var messageList = data.creditReportHints.split("$");
			if (messageList.length > 0) {
				for (var mes in messageList) {
					message += messageList[mes] +"<br>";
				}
			} else  {
				message = data.creditReportHints;
			}
            $.messager.alert('央行报告提示', message , 'warning');
		}
	});
})