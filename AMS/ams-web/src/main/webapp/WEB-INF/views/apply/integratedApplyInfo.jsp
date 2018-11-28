<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-查看详情</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; right: 0px; top: 7px;">消息提醒</div>
<div class="xx_dd_yuan" id="ruleEngineHint_number_div">0</div>
<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="right: 40px;">
	<div class="xx_dd_tit">消息提醒</div><ul></ul>
</div>
<div class="float_left W60" style="height: 750px;">
	<iframe  src="${picImageUrl}/api/filedata?nodeKey=${picApproval}&sysName=${sysCode}&appNo=${applyBasiceInfo.loanNo}&operator=${operator}&jobNumber=${jobNumber}"  style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>
</div>
<div class="float_right W40">
	<div class="easyui-panel padding_20" title="查看详情">
		<table class="table_ui W100  center_m" id="integrated_table">
			<!-- 如果是综合查询页面 -->
			<c:if test="${approveType == 0}">
				<tr>
					<shiro:hasPermission name="/integratedQuery/customerInfo">
						<td><a class="easyui-linkbutton" onclick="customerInfoWithoutRed('${applyBasiceInfo.loanNo}')"><i class="fa fa-user-o" aria-hidden="true"></i>客户信息</a></td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/insideMatch">
						<td><a class="easyui-linkbutton" onclick="finishInsideMatchDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-search-plus" aria-hidden="true"></i>内部匹配</a></td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/telSummarizing">
						<td><a class="easyui-linkbutton" onclick="finishTelephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-phone-square" aria-hidden="true"></i>电核汇总</a></td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/log">
						<td><a class="easyui-linkbutton" onclick="finishLogNotesInfoDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-tags" aria-hidden="true"></i>日志备注</a></td>
					</shiro:hasPermission>
				</tr>
				<tr>
					<shiro:hasPermission name="/integratedQuery/centralBankCredit">
						<td>
							<a class="easyui-linkbutton" onclick="finishCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')">
							<i class="fa fa-university" aria-hidden="true"></i>央行征信</a>
						</td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/outerCredit">
						<td><a class="easyui-linkbutton" onclick="finishExternalCreditDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-binoculars" aria-hidden="true"></i>外部征信</a></td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/suanhuaCredit">
						<td><a class="easyui-linkbutton" onclick="finishSuanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
					</shiro:hasPermission>
					<shiro:hasPermission name="/integratedQuery/approvalOpinion">
						<td><a class="easyui-linkbutton" onclick="finishApprovalOpinionDialog_withoutAction('${applyBasiceInfo.loanNo}')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>审批意见</a></td>
					</shiro:hasPermission>
				</tr>
				<tr>
					<c:if test="${applyBasiceInfo.reconsidered == true}">
						<shiro:hasPermission name="/integratedQuery/reviewLog">
							<td><a class="easyui-linkbutton" onclick="reviewLogDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-tags" aria-hidden="true"></i>复议日志</a></td>
						</shiro:hasPermission>
					</c:if>
				</tr>
			</c:if>
			<!-- 如果是初审改派，初审已完成，或者终审改派，终审已完成页面 -->
			<c:if test="${approveType == 1 || approveType == 2 || approveType == 3 || approveType == 4}">
				<tr>
					<td><a class="easyui-linkbutton" onclick="customerInfoWithoutRed('${applyBasiceInfo.loanNo}')"><i class="fa fa-user-o" aria-hidden="true"></i>客户信息</a></td>
					<td><a class="easyui-linkbutton" onclick="finishInsideMatchDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-search-plus" aria-hidden="true"></i>内部匹配</a></td>
					<td><a class="easyui-linkbutton" onclick="finishTelephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-phone-square" aria-hidden="true"></i>电核汇总</a></td>
					<td><a class="easyui-linkbutton" onclick="finishLogNotesInfoDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-tags" aria-hidden="true"></i>日志备注</a></td>
				</tr>
				<tr>
					<td><a class="easyui-linkbutton" onclick="finishCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')"><i class="fa fa-university" aria-hidden="true"></i>央行征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishExternalCreditDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-binoculars" aria-hidden="true"></i>外部征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishSuanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishApprovalOpinionDialog_withoutAction('${applyBasiceInfo.loanNo}')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>审批意见</a></td>
				</tr>
			</c:if>
			<!-- 如果是内部匹配，或者复议已完成查看详情页面 -->
			<c:if test="${approveType == 5 || approveType == 6}">
				<tr>
					<td><a class="easyui-linkbutton" onclick="customerInfoWithoutRed('${applyBasiceInfo.loanNo}')"><i class="fa fa-user-o" aria-hidden="true"></i>客户信息</a></td>
					<td><a class="easyui-linkbutton" onclick="finishInsideMatchDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-search-plus" aria-hidden="true"></i>内部匹配</a></td>
					<td><a class="easyui-linkbutton" onclick="finishTelephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-phone-square" aria-hidden="true"></i>电核汇总</a></td>
					<td><a class="easyui-linkbutton" onclick="finishLogNotesInfoDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-tags" aria-hidden="true"></i>日志备注</a></td>
				</tr>
				<tr>
					<td><a class="easyui-linkbutton" onclick="finishCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')"><i class="fa fa-university" aria-hidden="true"></i>央行征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishExternalCreditDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-binoculars" aria-hidden="true"></i>外部征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishSuanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
					<td><a class="easyui-linkbutton" onclick="finishApprovalOpinionDialog_withoutAction('${applyBasiceInfo.loanNo}')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>审批意见</a></td>
				</tr>
				<tr>
					<c:if test="${applyBasiceInfo.reconsidered == true}">
						<td><a class="easyui-linkbutton" onclick="reviewLogDialog('${applyBasiceInfo.loanNo}')"><i class="fa fa-tags" aria-hidden="true"></i>复议日志</a></td>
					</c:if>
				</tr>
			</c:if>
		</table>
		<hr>
		<form>
			<table class="table_ui W100 center_m">
				<tr>
					<th>借款编号:</th>
					<td id="search_loanNo">${applyBasiceInfo.loanNo}</td>
					<th>身份证号码:</th>
					<td>${applyBasiceInfo.idNo}</td>
				</tr>
				<tr>
					<th>申请人姓名:</th>
					<td>${applyBasiceInfo.name}</td>
					<th>申请产品:</th>
					<td><c:if test="${'证大前前' !=applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
				</tr>

				<tr>
					<th>申请期限:</th>
					<td>${applyBasiceInfo.applyTerm}</td>
					<th>申请额度:</th>
					<td class = "numFormat"><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<th>借款用途:</th>
					<td>${applyBasiceInfo.creditApplication}</td>
					<th>进件营业部:</th>
					<td>${applyBasiceInfo.owningBranch}</td>
				</tr>
				<tr>
					<th>是否加急:</th>
					<td><c:if test="${applyBasiceInfo.ifPri==0}">否</c:if><c:if test="${applyBasiceInfo.ifPri ==1}">是</c:if></td>
					<th>客户经理:</th>
					<td>${applyBasiceInfo.barnchManagerName}</td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3" class="W80">${applyBasiceInfo.remark}</td>
				</tr>
				
				<%-- <shiro:hasPermission name="/integratedQuery/anti-fraud"> --%>
				<!-- start反欺诈 -->
				<%--<tr>
					<th>反欺诈评分:</th>
					<td>${applyBasiceInfo.antiFraudScore}</td>
					<th>欺诈风险评估:</th>
					<td>${applyBasiceInfo.antiRiskRate}</td>
					<th>反欺诈预警:</th>
					<td>${applyBasiceInfo.antiFraudWarning}</td>
				</tr>--%>
				<tr>
					<th>众安反欺诈等级:</th>
					<td>${applyBasiceInfo.zhongAnRiskGrade}</td>
					<%--<th>众安反欺诈结果:</th>
					<td>${applyBasiceInfo.zhongAnRiskResult}</td>--%>
					<%--<th>芝麻信用分:</th>--%>
					<%--<td>${applyBasiceInfo.sesameCreditValue}</td>--%>
					<th>综合信用评级:</th>
					<td>${applyBasiceInfo.comCreditRating}</td>
				</tr>
				<tr>
					<th class="markRed"><c:if test="${'Y' == applyBasiceInfo.isAntifraud}">欺诈可疑</c:if></th>
					<td colspan="3"></td>
				</tr>
				<!-- end反欺诈 -->
				<%-- </shiro:hasPermission> --%>
			</table>
		</form>
		<hr>
		<!-- 如果是终审改派 -->
		<c:if test="${approveType == 2}">
			<!-- 终审已分派 -->
				<c:if test="${applyBasiceInfo.rtfNodeState=='XSZS-ASSIGN' or applyBasiceInfo.rtfNodeState=='XSZS-HANGUP'}">
					<shiro:hasPermission name="/finishApprove/finishReform/assigned/reform"><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="handle_finishReformDialog('${applyBasiceInfo.loanNo}')">改&nbsp;派</a></shiro:hasPermission>
				</c:if>
			<!-- 终审未分配 -->
				<c:if test="${applyBasiceInfo.rtfNodeState!='XSZS-ASSIGN' and applyBasiceInfo.rtfNodeState!='XSZS-HANGUP'}">
					<shiro:hasPermission name="/finishApprove/finishReform/notAssigned/reform"><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="handle_finishReformDialog('${applyBasiceInfo.loanNo}')">改&nbsp;派</a></shiro:hasPermission>
				</c:if>
		</c:if>
		<!-- 如果是初审改派 -->
		<c:if test="${approveType == 1}">
			<!-- 初审已分派 -->
			<c:if test="${applyBasiceInfo.rtfNodeState=='XSCS-ASSIGN' or applyBasiceInfo.rtfNodeState=='XSCS-HANGUP'}">
				<shiro:hasPermission name="/firstApprove/firstReform/assigned/reform"><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="handle_firstReformDialog('${applyBasiceInfo.loanNo}')">改&nbsp;派</a></shiro:hasPermission>
			</c:if>
			<!-- 初审未分派 -->
			<c:if test="${applyBasiceInfo.rtfNodeState!='XSCS-ASSIGN' and applyBasiceInfo.rtfNodeState!='XSCS-HANGUP'}">
				<shiro:hasPermission name="/firstApprove/firstReform/notAssigned/reform"><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="handle_firstReformDialog('${applyBasiceInfo.loanNo}')">改&nbsp;派</a></shiro:hasPermission>
			</c:if>
		</c:if>
	</div>
</div>

<!-- 终审改派dialog -->
<div id="handle_finishReform_dialog" class="padding_20 display_none">
	<table class="table_ui W100">
		<tr>
			<th>处理人:</th>
			<td><input type="text" class="input" id="handle_finishApprovePerson"></td>
		</tr>
	</table>
</div>

<!-- 初审改派dialog -->
<div id="handle_firstReform_dialog" class="padding_20 display_none">
	<form>
		<table class="table_ui W100">
			<tr>
				<th>大组:</th>
				<td><input id="handle_firstReform_bigGroup" class="input"></td>
			</tr>
			<tr>
				<th>小组:</th>
				<td><input id="handle_firstReform_smallGroup" class="input"></td>
			</tr>
			<tr>
				<th>处理人:</th>
				<td><input id="handle_firstReform_operator" class="input"></td>
			</tr>
		</table>
	</form>
</div>

</body>
<script type="text/javascript">
	$(function(){
		//动态处理展示页面上的八个按钮
		var tableObj = $("#integrated_table");
		var tableHeight = tableObj.find("tr").length;
		var tdList = new Array();
		for (var x = 0; x < tableHeight; x++) {
			var tr = tableObj.find("tr:eq("+x+")");
			var tdLength = tr.find("td").length;
			for (var y = 0; y < tdLength; y++) {
				var td = tr.find("td:eq("+y+")");
				tdList.push(td);
			}
		}
		//重新放置按钮
		var row = 0;
		var col = 0;
		for (var i = 0; i < tdList.length; i++) {
			if(col >= 4){
				col = 0;
				row = row + 1;
			}
			tableObj.find("tr:eq("+row+")").append(tdList[i]);
			col = col + 1;
		}
		//去除空行
		for (var m = 0; m < tableObj.find("tr").length; m++) {
			var tr = tableObj.find("tr:eq("+m+")");
			var tdLength = tr.find("td").length;
			if(tdLength == 0){
			    m--;
				tr.remove();
			}
		}
		// 添加提示信息
		var applyBasiceInfo = ${applyBasiceInfoJSON};
		// 调用判断是否显示规则引擎提示信息
        showRuleEngineHint(applyBasiceInfo.rtfState,applyBasiceInfo.rtfNodeState,applyBasiceInfo.checkNodeState);
	});

    /**
	 * 判断是否显示规则引擎提示信息
     * @param rtfState
     * @param rtfNodeState
     * @param checkNodeState
     */
	function showRuleEngineHint(rtfState,rtfNodeState,checkNodeState) {
        // 录入环节不显示,信审环节部分显示,信审通过后环节显示
		/* 初审分派： 分派退回(改派批量退回)---不显示
		 *             待分派-------------------不显示
		 *  初审办理： 已分派未办理过的---------不显示
		 *  		   初审退回门店-------------不显示
		 *       初审复合确认：
		 *             拒绝-不同意--------------不显示
		 *             退回-同意----------------不显示
		 *             通过-不同意--------------不显示
		 *  终审分派： 分派退回门店-------------不显示
		 *             分派退回初审-------------不显示
		 *  终审办理： 终审退回门店-------------不显示
		 *             终审退回初审-------------不显示
		 * */
        // 判断流程环节---APP申请录入、门店申请录入、前前录入环节---这几个环节不显示,其他环节需要显示(多加逗号是因为前前录入包括门店录入包括)
        var ifRtfState = ",APPSQLR,ZDQQAPPSQLR,SQLR".indexOf(","+rtfState) < 0;
        // 判断子节点---初审分派退回、待分派(包括终审退回初审)、初审退回门店、终审分派退回门店、终审分派退回初审、终审退回门店、终审退回初审
        //                   初审分派退回门店 初审分派退回前前 终审退回初审   终审分派退回前前  终审退回前前  终审分派退回和终审退回门店是同一个
        var ifRtfNodeState ="CSFP-RETURN,CSFP-ZDQQRETURN,XSZS-RTNCS,ZSFP-ZDQQRETURN,XSZS-ZDQQRETURN,XSZS-RETURN".indexOf(rtfNodeState) < 0;
		if (ifRtfState && ifRtfNodeState) {
           // 初审复合确认：
			// 拒绝-不同意--------------不显示
			// 退回-同意----------------不显示
			//      不同意--------------不显示
			// 通过-不同意--------------不显示
            if (!(("XSCS-REJECT" ==rtfNodeState && "CHECK_NO_PASS"==checkNodeState) || ("XSCS-RETURN" ==rtfNodeState && "CHECK_PASS"!=checkNodeState) || ("XSCS-ZDQQRETURN" ==rtfNodeState && "CHECK_PASS"!=checkNodeState) ||("XSZS-PASS" ==rtfNodeState && "CHECK_NO_PASS"==checkNodeState))) {
                //已分派(特殊需要判断)
				var remark = false;// 用于标识是否是初审已分派如果是且为点击过办理需要显示,否则不显示
				if ("XSCS" == rtfState && "XSCS-ASSIGN" == rtfNodeState) {
					remark = true;
				}
                post(ctx.rootPath() + "/ruleEngineParameter/getRuleEngineParameterLastHand/" + $("#search_loanNo").html()+"/"+remark, null, "json", function (data) {
                    if (isNotNull(data) && isNotNull(data.engineHints)) {
                        var messageList = data.engineHints.split("$");
                        $("#ruleEngineHint_number_div").html(messageList.length);
                        for (var mes in messageList) {
                            $("#ruleEngineHint_div").find("ul").append("<li>" + messageList[mes] + "<br><span>" + moment(data.createdDate).format('YYYY-MM-DD HH:mm') + "</span></li>")
                        }
                    }
                });
			}
		}
	}
</script>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApproveReceive.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishReform.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstReform.js"></script>
</html>
