<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html class="WH100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-办理</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="WH100">
	<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; right: 0px; top: 7px;">消息提醒</div>
	<div class="xx_dd_yuan" id="ruleEngineHint_number_div">${fn:length(ruleEngineResult)-2}</div>
	<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="right: 40px;">
		<div class="xx_dd_tit">消息提醒</div>
		<ul>
			<c:forEach var="item" items="${ruleEngineResult}" begin="2">
				<li>${item}<br> <span>${ruleEngineResult[1]}</span></li>
			</c:forEach>
		</ul>
	</div>
	<input type="hidden" id="first_flagUpdateVersion_hidden" value="${updateVersion}" placeholder="标识version是否改变true改变">
	<input type="hidden" id="first_limitFirstSubmitDisable" value="${limitFirstSubmitDisable}" placeholder="规则引擎返回类型">
	<input type="hidden" id="fisrt_approve_version_hidden" value="${applyBasiceInfo.version}" placeholder="版本控制">
	<div class="float_left W60" style="height: 750px">
		<iframe src="${picImageUrl}/api/filedata?nodeKey=${picApproval}&sysName=${sysCode}&appNo=${applyBasiceInfo.loanNo}&operator=${operator}&jobNumber=${jobNumber}" style="width: 100%; height: 99.5%; padding: 0px; margin: 0px; border: 0px; display: table"></iframe>
	</div>
	<div class="float_right W40">
		<div class="easyui-panel padding_20" title="审批办理">
			<table class="table_ui W100  center_m">
				<tr>
					<td><a id="first_customerInfo_btn" class="easyui-linkbutton" onclick="customerInfoDialog('${applyBasiceInfo.loanNo}')"><span class="icon_01"></span>客户信息</a></td>
					<td><a id ="first_insideMatch_btn" class="easyui-linkbutton" onclick="insideMatchDialog('${applyBasiceInfo.loanNo}')"><span class="icon_02"></span>内部匹配</a></td>
					<td><a id="first_telephone_btn" class="easyui-linkbutton" onclick="telephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><span class="icon_03"></span>电核汇总</a></td>
					<td><a id="first_logNotes_btn" class="easyui-linkbutton" onclick="logNotesInfoDialog('${applyBasiceInfo.loanNo}')"><span class="icon_04"></span>日志备注</a></td>
				</tr>
				<tr>
					<td><a id="first_centralBankCredit_btn" class="easyui-linkbutton" onclick="centralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')"><span class="icon_05"></span>央行征信</a><input id="first_reportId_hidden" type="hidden" value="${applyBasiceInfo.reportId}"></td>
					<td><a id="first_exteranlCredit_btn" class="easyui-linkbutton" onclick="externalCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>外部征信</a></td>
					<td><a id="first_suanHuaCredit_btn" class="easyui-linkbutton" onclick="suanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
					<td><a id="first_approvalOpinion_btn" class="easyui-linkbutton" onclick="approvalOpinionDialog('${applyBasiceInfo.loanNo}')"><span class="icon_07"></span>审批意见</a></td>
				</tr>
			</table>
			<hr>
			<form>
				<table class="table_ui W100 center_m">
					<tr>
						<th>借款编号:</th>
						<td id="first_approve_loanNo">${applyBasiceInfo.loanNo}</td>
						<th>身份证号码:</th>
						<td>${applyBasiceInfo.idNo}</td>
					</tr>
					<tr>
						<th>申请人姓名:</th>
						<td>${applyBasiceInfo.name}</td>
						<th>申请产品:</th>
						<td><c:if test="${'证大前前'!= applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
					</tr>

					<tr>
						<th>申请期限:</th>
						<td>${applyBasiceInfo.applyTerm}</td>
						<th>申请额度:</th>
						<td class="numFormat"><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
					</tr>
					<tr>
						<th>借款用途:</th>
						<td>${applyBasiceInfo.creditApplication}</td>
						<th>进件营业部:</th>
						<td>${applyBasiceInfo.owningBranch}</td>
					</tr>
					<tr>
						<th>是否加急:</th>
						<td><c:if test="${applyBasiceInfo.ifPri==0}">否</c:if> <c:if test="${applyBasiceInfo.ifPri ==1}">是</c:if></td>
						<th>客户经理:</th>
						<td>${applyBasiceInfo.barnchManagerName}</td>
					</tr>
					<tr>
						<th>备注</th>
						<td colspan="3" class="W80">${applyBasiceInfo.remark}</td>
					</tr>
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
						<td id="first_comCreditRating">${applyBasiceInfo.comCreditRating}</td>
					</tr>
					<tr>
						<th id="firstApprove_isAntifraud" class="markRed"><c:if test="${'Y' == applyBasiceInfo.isAntifraud}">欺诈可疑</c:if></th>
						<td colspan="3"></td>
					</tr>
					<!-- end反欺诈 -->
				</table>
			</form>
			<hr>
			<table class="table_ui W100 center_m">
				<tr>
					<td><a onclick="firstApprovalHungDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.rtfNodeState}')" class="easyui-linkbutton_ok01 l-btn l-btn-small" href="javaScript:void(0);">挂起</a></td>
					<td><a id="firstApproval_submit_btn" class="easyui-linkbutton_ok02 l-btn l-btn-small" href="javaScript:void(0);">提交</a></td>
					<td><a onclick="firstApprovalBackDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.zdqqApply}')" class="easyui-linkbutton_ok03 l-btn l-btn-small" href="javaScript:void(0);">退回</a></td>
					<td><a onclick="firstApprovalRefuseDialog('${applyBasiceInfo.loanNo}')" class="easyui-linkbutton_ok04 l-btn l-btn-small" href="javaScript:void(0);">拒绝</a></td>
				</tr>
			</table>
		</div>
	</div>
	<!-- 拒绝按钮弹框 -->
	<div id="firstApproveReceive_refuse_dialog" class="display_none padding_20">
		<form>
			<input type="hidden" name="remarkBlack" id="first_remarkBlack">
			<table class="table_ui WH100">
				<tr>
					<th>一级原因:</th>
					<td><input id="first_firstRefuse_combobox" name="firstReason" class="input"><input type="hidden" name="conditionType"><input type="hidden" value="none" id="first_ApproveReceive_ConditionType" name="conditionType"><input type="hidden" name="firstReasonText"></td>
					<th>二级原因:</th>
					<td><input id="first_secondRefuse_combobox" name="secondReason" class="input"><input type="hidden" name="secondReasonText"></td>
				</tr>
				<tr>
					<th>备注信息:</th>
					<td colspan="5"><input class="easyui-textbox W30" id="firstApproveReceive_refuse_remark" name="remark" data-options="height:80,width:722,validType:'length[1,200]',multiline:true"></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 退回 -->
	<div id="firstApproveReceive_return_dialog" class="display_none padding_20">
		<form>
			<table class="table_ui WH100 padding20">
				<tr class="markReturnReason">
					<th>一级原因:</th>
					<td><input id="first_firstReturn_combobox" name="firstReason" class="input"><input type="hidden" name="firstReasonText"></td>
					<th>二级原因:</th>
					<td><input id="first_secondReturn_combobox" name="secondReason" class="input" data-options="multiple:${1==applyBasiceInfo.zdqqApply},separator:'|'"><input type="hidden" name="secondReasonText"></td>
					<td><c:if test="${1==applyBasiceInfo.zdqqApply}"><a href="javaScript:void(0);" onclick="addOrDeleteRetureReason('ADD')"><i class="fa fa-plus" aria-hidden="true"></i></a></c:if></td>
				</tr>
				<tr>
					<th>姓名:</th>
					<td colspan="4">${applyBasiceInfo.name}</td>
				</tr>
				<tr>
					<th>备注信息:</th>
					<td colspan="4"><input class="easyui-textbox W30" name="remark" data-options="height:80,width:642,validType:'length[1,200]',multiline:true"></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 挂起 -->
	<div id="firstApproveReceive_hang_dialog" class="display_none padding_20">
		<form>
			<table class="table_ui WH100">
				<tr>
					<th>挂起原因:</th>
					<td><input id="first_hangList_combobox" name="firstReason" class="input"><input type="hidden" name="firstReasonText"></td>
				</tr>
				<tr>
					<th>备注信息:</th>
					<td colspan="2"><input class="easyui-textbox W30" name="remark" data-options="height:80,width:320,validType:'length[1,200]',multiline:true"></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 提交 -->
	<div id="firstApproveReceive_submit_dialog" class="display_none padding_20">
		<form>
			<table class="table_ui WH100">
				<tr>
					<th>审批额度(元):</th>
					<td></td>
					<th>审批期限:</th>
					<td></td>
				</tr>
				<tr>
					<th>内部负债率:</th>
					<td></td>
					<th>总负债率:</th>
					<td></td>
				</tr>
				<tr>
					<th>备注信息:</th>
					<td colspan="3">
						<input class="easyui-textbox W30" name="remark" data-options="height:80,width:420,validType:'length[1,200]',multiline:true">
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstApproveReceive.js" charset="utf-8"></script>
</html>