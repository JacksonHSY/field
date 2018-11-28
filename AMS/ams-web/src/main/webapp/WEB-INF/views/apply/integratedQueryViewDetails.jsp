<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-还款明细</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<!-- 还款明细-->
<div id="integratedQuery_viewDetails_Dialog">
	<div class="easyui-tabs">
		<div title="查看借款">
			<input id="intergrated_query_details_loanNo" type="hidden" value="${loanNo}">
			<table class="table_ui W100">
				<tr>
					<th>借款人:</th>
					<td>${loanVo.borrower}</td>
					<th>性别:</th>
					<td>${loanVo.sex}</td>
					<th>身份证号码:</th>
					<td>${loanVo.idnum}</td>
				</tr>
				<tr>
					<th>借款类型:</th>
					<td>${loanVo.loanType}</td>
					<th>所属机构（渠道）：</th>
					<td>${loanVo.organizationName}</td>
					<th>借款方案（渠道）：</th>
					<td>${loanVo.planName}</td>
				</tr>
				<tr>
					<th>申请额度:</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${loanVo.requestMoney}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>申请期限:</th>
					<td>${loanVo.requestTime}</td>
					<th>申请日期:</th>
					<td>${loanVo.requestDate}</td>
				</tr>
				<tr>
					<th>签约额度:</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${loanVo.money}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>签约期限：</th>
					<td>${loanVo.time}</td>
					<th>可接受的最高月还款额：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${loanVo.maxMonthlyPayment}" pattern="0.00" maxFractionDigits="2"/></td>
				</tr>
				<tr>
					<th>借款状态:</th>
					<td>${loanVo.loanFlowState}</td>
					<th>月还款能力：</th>
					<td>${loanVo.restoreEM}</td>
					<th>借款用途：</th>
					<td>${loanVo.purpose}</td>
				</tr>
				<tr>
					<th>管理营业部:</th>
					<td>${loanVo.salesDepartment}</td>
					<th>还款银行:</th>
					<td>${loanVo.giveBackBank}</td>
					<th>放款银行:</th>
					<td>${loanVo.grantBank}</td>
				</tr>
				<tr>
					<th>客户经理:</th>
					<td>${loanVo.salesman}</td>
					<th>客服：</th>
					<td>${loanVo.crm}</td>
					<th>渠道名称：</th>
					<td>${loanVo.fundsSources}</td>
				</tr>
				<tr>
					<th>备注:</th>
					<td>${loanVo.remark}</td>
				</tr>
			</table>
		</div>
		<div title="查看还款汇总信息">
			<table class="table_ui W100">
				<tr>
					<th>姓名:</th>
					<td>${repaySumVo.name}</td>
					<th>证件类型:</th>
					<td>${repaySumVo.idType}</td>
					<th>身份证号码:</th>
					<td>${repaySumVo.idNum}</td>
					<th>手机:</th>
					<td>${repaySumVo.mphone}</td>
				</tr>
				<tr>
					<th>逾期起始日:</th>
					<td><fmt:formatDate value="${repaySumVo.overDueDate}" pattern="yyyy-MM-dd" type="date"/></td>
					<th>逾期总数：</th>
					<td>${repaySumVo.overDueTerm}</td>
					<th>逾期利息：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.overInterest}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>逾期本金：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.overCorpus}" pattern="0.00" maxFractionDigits="2"/></td>
				</tr>
				<tr>
					<th>罚息算起日:</th>
					<td><fmt:formatDate value="${repaySumVo.fineDate}" pattern="yyyy-MM-dd" type="date"/></td>
					<th>罚息天数:</th>
					<td>${repaySumVo.fineDay}</td>
					<th>剩余本息和:</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.remnant}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>罚息金额：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.fine}" pattern="0.00" maxFractionDigits="2"/></td>
				</tr>
				<tr>
					<th>当期还款日:</th>
					<td><fmt:formatDate value="${repaySumVo.currDate}" pattern="yyyy-MM-dd" type="date"/></td>
					<th>当前期数：</th>
					<td>${repaySumVo.currTerm}</td>
					<th>当期利息：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.currInterest}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>当期本金：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.currCorpus}" pattern="0.00" maxFractionDigits="2"/></td>
				</tr>
				<tr>
					<th>挂账金额:</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.accAmount}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>应还总额（不含当期）：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.overdueAmount}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>应还总额（包含当期）：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.currAmount}" pattern="0.00" maxFractionDigits="2"/></td>
					<th>剩余本金：</th>
					<td class="numFormat"><fmt:formatNumber type="number" value="${repaySumVo.residualPactMoney}" pattern="0.00" maxFractionDigits="2"/></td>
				</tr>
			</table>
		</div>
		<div title="查看还款详细信息">
			<table class="table_list W100" id="integratedQuery_detailsInfo_table"></table>
		</div>
		<div title="查看账卡信息">
			<table class="table_list W100" id="integratedQuery_billInfo_table"></table>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/apply/integratedQueryViewDetails.js" charset="utf-8"></script>
</html>