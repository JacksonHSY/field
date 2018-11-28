	$(function(){
		var a = $("#qualityCheckId").val();
		var callback = function(data) {
				var noError =0;
				var first =0;
				var second =0;
				for(var i=0;i<data["length"];i++){
					if(data[i]["checkResult"]=="E_000000"){
						noError++;
					}
					//判断是终审还是初审
					var who = data[i]["checkError"]
					//初审
					if(who=="apply-check"){
						addFirstAudit("#addQualityOpinion_quality_firstApprove",data[i],first)
						b++;
					}
					//终审
					if(who=="applyinfo-finalaudit"){
						addFinalAudit("#addQualityOpinion_quality_finishApprove",data[i],second);
						a++;
					}
					//领导
					if(who=="leader-check"){
						addLeaderAudit("#addQualityOpinion_quality_leaderApprove",data[i]);
					}
				}
				if(noError==data["length"]){
					$("input[name='checkResult']").get(0).checked=true;
				}
			}
	
		post(ctx.rootPath()+'/qualityControlDesk/getHisttoryOpinion/'+a, null, 'json', callback);
	});

	// 添加初审
	function addFirstAudit(obj,data,first) {
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="checkbox" name="firstCheckError'+data["approveHistoryId"]+'" checked = "checked" value="apply-check/"><label class="hand">&nbsp;初审'+Number(first+1)+'</label>'
					+'	<input type="radio" name="firstCheckResult'+data["approveHistoryId"]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="firstCheckResult'+data["approveHistoryId"]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="firstCheckResult'+data["approveHistoryId"]+'" value="E_000001">预警'
					+'	<input type="radio" name="firstCheckResult'+data["approveHistoryId"]+'" value="E_000002">建议'
					+'	<input type="radio" name="firstCheckResult'+data["approveHistoryId"]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="addQualityOpi_firstApp_errorCode" name="firstErrorCode" class="easyui-combobox input errorCode" >'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td>质检意见</td>'
					+'</tr>'
					+'<td><textarea id="addQualityOpi_firstApp_chickView" maxlength="1000" class="textarea" name="	firstCheckView"></textarea></td>'
					+'</table>');
			$(obj).parents("table").after(html);
			if(data["checkResult"]=="E_000000"){
				$("input[name='firstCheckResult"+data["approveHistoryId"]+"']").get(4).checked=true; 
			}
			if(data["checkResult"]=="E_000001"){
				$("input[name='firstCheckResult"+data["approveHistoryId"]+"']").get(2).checked=true; 
			}
			if(data["checkResult"]=="E_000002"){
				$("input[name='firstCheckResult"+data["approveHistoryId"]+"']").get(3).checked=true; 
			}
			if(data["checkResult"]=="E_000003"){
				$("input[name='firstCheckResult"+data["approveHistoryId"]+"']").get(1).checked=true; 
			}
			if(data["checkResult"]=="E_000004"){
				$("input[name='firstCheckResult"+data["approveHistoryId"]+"']").get(0).checked=true; 
			}
			$('#addQualityOpi_firstApp_chickView').html(data["checkView"]);
			$('#addQualityOpi_firstApp_errorCode').val(data["errorCode"]);
			$("input").attr("disabled",true);
			$("textarea").attr("disabled",true);
		
	}
	
	// 添加终审
	function addFinalAudit(obj,data,second) {
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="checkbox" name="finishCheckError'+data["approveHistoryId"]+'"+ checked = "checked" value="applyinfo-finalaudit/"><label class="hand">&nbsp;终审'+Number(second+1)+'</label>'
					+'	<input type="radio" name="finishCheckResult'+data["approveHistoryId"]+'" value="E_000004"/>重大差错'
					+'	<input type="radio" name="finishCheckResult'+data["approveHistoryId"]+'" value="E_000003"/>一般差错'
					+'	<input type="radio" name="finishCheckResult'+data["approveHistoryId"]+'" value="E_000001"/>预警'
					+'	<input type="radio" name="finishCheckResult'+data["approveHistoryId"]+'" value="E_000002"/>建议'
					+'	<input type="radio" name="finishCheckResult'+data["approveHistoryId"]+'" value="E_000000" />无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="addQualityOpi_finishApp_errorCode" name="finishErrorCode" class="easyui-combobox input errorCode" >'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td>质检意见</td>'
					+'</tr>'
					+'<td><textarea id="addQualityOpi_finishApp_chickView" maxlength="1000"class="textarea" name="finishCheckView"></textarea></td>'
					+'</table>');
			$(obj).parents("table").after(html);
			if(data["checkResult"]=="E_000000"){
				$("input[name='finishCheckResult"+data["approveHistoryId"]+"']").get(4).checked=true; 
			}
			if(data["checkResult"]=="E_000001"){
				$("input[name='finishCheckResult"+data["approveHistoryId"]+"']").get(2).checked=true; 
			}
			if(data["checkResult"]=="E_000002"){
				$("input[name='finishCheckResult"+data["approveHistoryId"]+"']").get(3).checked=true; 
			}
			if(data["checkResult"]=="E_000003"){
				$("input[name='finishCheckResult"+data["approveHistoryId"]+"']").get(1).checked=true; 
			}
			if(data["checkResult"]=="E_000004"){
				$("input[name='finishCheckResult"+data["approveHistoryId"]+"']").get(0).checked=true; 
			}
			$('#addQualityOpi_finishApp_chickView').html(data["checkView"]);
			$('#addQualityOpi_finishApp_errorCode').val(data["errorCode"]);
			$("input").attr("disabled",true);
			$("textarea").attr("disabled",true);
	}
	
	// 添加领导
	function addLeaderAudit(obj,data) {
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="checkbox" name="leaderCheckError" checked = "checked" value="leader-check/"><label class="hand">&nbsp;组长/主管/经理</label>'
					+'&nbsp&nbsp<input type="text" id="leadText'+data["approveHistoryId"]+'"/>'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td>'
					+'	<input type="radio" name="leaderCheckResult'+data["approveHistoryId"]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="leaderCheckResult'+data["approveHistoryId"]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="leaderCheckResult'+data["approveHistoryId"]+'" value="E_000001">预警'
					+'	<input type="radio" name="leaderCheckResult'+data["approveHistoryId"]+'" value="E_000002">建议'
					+'	<input type="radio" name="leaderCheckResult'+data["approveHistoryId"]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td>'
					+'<input type="text" id="addQualityOpi_leaderApp_errorCode" name="leaderErrorCode" class="easyui-combobox input errorCode" >'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td>质检意见</td>'
					+'</tr>'
					+'<td><textarea id="addQualityOpi_leaderApp_chickView" maxlength="1000" class="textarea" name="	leaderCheckView"></textarea></td>'
					+'</table>');
			$(obj).parents("table").after(html);
			if(data["checkResult"]=="E_000000"){
				$("input[name='leaderCheckResult"+data["approveHistoryId"]+"']").get(4).checked=true; 
			}
			if(data["checkResult"]=="E_000001"){
				$("input[name='leaderCheckResult"+data["approveHistoryId"]+"']").get(2).checked=true; 
			}
			if(data["checkResult"]=="E_000002"){
				$("input[name='leaderCheckResult"+data["approveHistoryId"]+"']").get(3).checked=true; 
			}
			if(data["checkResult"]=="E_000003"){
				$("input[name='leaderCheckResult"+data["approveHistoryId"]+"']").get(1).checked=true; 
			}
			if(data["checkResult"]=="E_000004"){
				$("input[name='leaderCheckResult"+data["approveHistoryId"]+"']").get(0).checked=true; 
			}
			$('#addQualityOpi_leaderApp_chickView').html(data["checkView"]);
			$('#addQualityOpi_leaderApp_errorCode').val(data["errorCode"]);
			$('#leadText'+data["approveHistoryId"]+'').val(data["qualityLeader"]);
			$("input").attr("disabled",true);
			$("textarea").attr("disabled",true);
	}