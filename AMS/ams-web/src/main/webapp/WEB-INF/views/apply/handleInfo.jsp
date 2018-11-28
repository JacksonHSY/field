<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-办理</title>
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
	<div class="easyui-panel padding_20" title="审批办理">
		<table class="table_ui W100  center_m">
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
		</table>
		<hr>
		<form>
			<table class="table_ui W100 center_m">
				<tr>
					<th>借款编号:</th>
					<td id="review_loanNo">${applyBasiceInfo.loanNo}</td>
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
					<td>${applyBasiceInfo.contractBranch}</td>
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
				<%--
				<tr>
					<th>反欺诈评分:</th>
					<td>${applyBasiceInfo.antiFraudScore}</td>
					<th>欺诈风险评估:</th>
					<td >${applyBasiceInfo.antiRiskRate}</td>
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
			</table>
		</form>
		<hr>
	<!-- 复核确认  角色 按钮权限控制 start -->
		<table class="table_ui W100 center_m">
					<tr>
						<td><a class="easyui-linkbutton" href="javaScript:void(0);" onclick="toSubmitDialog('${applyBasiceInfo.loanNo}','${checkPerson}')"><i class="fa fa-anchor" aria-hidden="true"></i>确认</a></td>
						<td><a class="easyui-linkbutton" href="javaScript:void(0);" onclick="toBackDialog('${applyBasiceInfo.loanNo}','${checkPerson}')"><i class="fa fa-arrow-left" aria-hidden="true"></i>退回</a></td>
						<!-- <td><a class="easyui-linkbutton" href="javaScript:void(0);" onclick="toReturn()"><i class="fa fa-reply" aria-hidden="true"></i>返回</a></td> -->
					</tr>
		</table> 
			
	</div>
</div>
<!-- 提交 -->
<%-- <div id="toReview_submit_dialog" class="display_none padding_20">
	<form id="toReview_submit_form">
		<table class="table_ui W100">
			<tr>
				<th>审批额度:</th>
				<td>${applyBasiceInfo.applyLmt }</td>
				<th>审批期限:</th>
				<td>${applyBasiceInfo.applyTerm }</td>
			</tr>
			<tr>
				<th>内部负债率:</th>
				<td>${applyBasiceInfo.applyTerm }</td>
				<th>总负债率:</th>
				<td>${applyBasiceInfo.applyTerm }</td>
			</tr>
			<tr>
				<th>备注信息:</th>
				<td colspan="2"><textarea class="textarea" name="remark"></textarea></td>
			</tr>
		</table>
	</form>
</div> --%>
<!-- 退回 -->
<div id="toReview_return_dialog" class="display_none padding_20">
	<form id="toReview_return_form">
		<table class="table_ui W100">
			<%-- <tr>
				<th>一级原因:</th>
				<td><input id="first_firstReturn_combobox" name="firstReason" class="input"></td>
				<th>二级原因:</th>
				<td><input id="first_secondReturn_combobox" name="secondReason" class="input"></td>
			</tr>
			<tr>
				<th>姓名:</th>
				<td>${applyBasiceInfo.name}</td>
			</tr> --%>
			<tr>
				<th>备注信息:</th>
				<td colspan="2"><textarea name="remark" class="easyui-textbox" style="width: 300px;height: 90px;" data-options="validType:'length[1,200]',required: true,multiline:true"></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApproveReceive.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/apply/reviewConfirUflo.js"></script>
</html>