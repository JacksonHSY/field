<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-办理</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; right: 0px; top: 7px;">消息提醒</div>
<div class="xx_dd_yuan" id="ruleEngineHint_number_div">0</div>
<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="right: 40px;">
	<div class="xx_dd_tit">消息提醒</div><ul></ul>
</div>
<div class="float_left W65" style="height: 750px;">
	<iframe  src="${picImageUrl}/api/filedata?nodeKey=${picApproval}&sysName=${sysCode}&appNo=${loanNo}&operator=${operator}&jobNumber=${jobNumber}"  style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>
</div>
<input id="qualityCheckId" name="qualityCheckId" value='${loanNo}' type="hidden">
<input id="flag" name="flag" value="${flag}" type="hidden">
<input id ="applyHistory" value="${applyHistory}" type="hidden">
<div class="float_right W35">
	<div class="easyui-panel padding_20" title="审批办理">
		<table class="table_ui W100  center_m">
			<tr>
				<td><a class="easyui-linkbutton" onclick="finishCustomerInfo('${loanNo}')"><span class="icon_01">
                    </span>客户信息</a>
                </td>
                <td><a class="easyui-linkbutton" onclick="finishInsideMatchDialog('${loanNo}')"><span class="icon_02">
                    </span>内部匹配</a></td>
                <td><a class="easyui-linkbutton" onclick='finishTelephoneSummaryDialog("${loanNo}")'><span class="icon_03">
                    </span>电核汇总</a></td>
                <td><a class="easyui-linkbutton" onclick="logNotesInfoDialog('${loanNo}')"><span class="icon_04">
                    </span>日志备注</a></td>
            </tr>
            <tr>
                <td><a class="easyui-linkbutton" onclick="finishCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')">
                    <i class="fa fa-university" aria-hidden="true"></i>央行征信</a></td>
				<td><a class="easyui-linkbutton" onclick="finishExternalCreditDialog('${loanNo}')">
                    <span class="icon_06"></span>外部征信</a></td>
				<td><a class="easyui-linkbutton" onclick="finishSuanHuaCreditDialog('${loanNo}')"><span class="icon_06"></span>算话征信</a></td>
				<td><a class="easyui-linkbutton" onclick="finishApprovalOpinionDialog('${loanNo}')">
                    <span class="icon_07"></span>审批意见&nbsp;</a></td>
			</tr>
			<tr>
				<td><a class="easyui-linkbutton" id ="qualityOpinion" onclick="qualityOpinionDialog('${loanNo}','${checkUser}')">
					<i class="fa fa-pencil-square-o" aria-hidden="true"></i>质检意见</a></td>
				<td><a class="easyui-linkbutton" onclick="qualityLogDialog('${loanNo}')">
                    <i class="fa fa-pencil-square-o" aria-hidden="true"></i>质检日志</a></td>
			</tr>
		</table>
		<hr>
		<form>
			<table class="table_ui W100 center_m">
				<tr>
					<th>借款编号:</th>
					<td id="quality_approve_loanNo">${loanNo}</td>
					<th>身份证号码:</th>
					<td>${applyBasiceInfo.idNo}</td>
				</tr>
				<tr>
					<th>申请人姓名:</th>
					<td>${applyBasiceInfo.name}</td>
					<th>申请产品:</th>
					<td><c:if test="${'证大前前' != applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
				</tr>

				<tr>
					<th>申请期限:</th>
					<td>${applyBasiceInfo.applyTerm}</td>
					<th>申请额度:</th>
					<td>${applyBasiceInfo.applyLmt}</td>
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
			</table>
		</form>
		<hr>
	</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/qualityReceive.js"></script>
</html>
