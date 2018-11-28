<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-审批意见</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
<style type="text/css">
#first_approval_assetsInfo_div .table_list {
	margin: 5px 0px;
}
</style>
</head>
<body class="padding_20 ms-controller" ms-controller="page">
	<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; position: fixed; left: 130px; top: 7px;">消息提醒</div>
	<div class="xx_dd_yuan" style="left: 113px; position: fixed;" id="ruleEngineHint_number_div">0</div>
	<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="left: 110px; position: fixed;">
		<div class="xx_dd_tit">消息提醒</div>
		<ul></ul>
	</div>
	<div style="height: 120px;">
		<div id="firstApprovalOpinion_approveInfo_div" style="margin-top: 0px; width: 100%; border-bottom: 2px solid #95b8e7;">
			<div class="easyui-panel" title="申请信息">
				<table class="table_ui W100">
					<tr>
						<th>申请人姓名:</th>
						<td>{{ applyInfo.applyInfoVO.name }}</td>
						<th>身份证号码:</th>
						<td colspan="3">{{ applyInfo.applyInfoVO.idNo }}</td>
					</tr>
					<tr>
						<th width="12%">申请产品:</th>
						<td width="20%">{{ applyBasicInfo.initProductName }}</td>
						<th width="12%">申请期限:</th>
						<td width="20%">{{ applyInfo.applyInfoVO.applyTerm }}</td>
						<th width="12%">申请额度:</th>
						<td class="numFormat">{{ applyInfo.applyInfoVO.applyLmt }}</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<form id="firstApprovalOpinion_approveCheckData_form">
		<input name="version" type="hidden" ms-duplex="applyInfo.version">
		<input id="loanNo" type="hidden" ms-duplex="loanNo">
		<c:if test="${empty approvalInfo.approvalHistoryList}">
			<!----------------------------------------- Start 审核记录为空--------------------------------------->
			<div class="easyui-panel" title="资料核对">
				<h3>工作信息</h3>
				<table class="table_ui W100">
					<tr>
						<th width="12%">可接受的月最高还款:</th>
						<td width="20%" class="numFormat">{{ applyInfo.basicInfoVO.personInfoVO.monthMaxRepay || '' }}</td>
						<th width="12%">客户工作类型:</th>
						<td width="20%">{{ applyInfoOnlyRead.basicInfoVO.workInfoVO.cusWorkType || ''}}</td>
						<th width="12%">发薪方式:</th>
						<td colspan="2">{{ applyInfoOnlyRead.basicInfoVO.workInfoVO.corpPayWay || ''}}</td>
					</tr>
					<tr>
						<th>单位月收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.monthSalary || ''}}</td>
						<th>其他月收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.otherIncome || ''}}</td>
						<th>月总收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.totalMonthSalary || ''}}</td>
					</tr>
					<tr>
						<th>收入证明金额:</th>
						<td>
							<input type="text" class="easyui-numberbox input" name="incomeCertificate"
								   data-options="groupSeparator:',',precision:0,buttonText:'元',panelHeight:'auto',validType:['compareMaxMin[0,99999999]']">
						</td>
						<th>有无周末发薪:</th>
						<td>
							<input type="radio" id="first_weekendTrue_radioFor"  name="weekendPay" value="0"><label for="first_weekendTrue_radioFor">&nbsp;有</label>&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id="first_weekendFalse_radioFor" name="weekendPay" value="1"><label for="first_weekendFalse_radioFor">&nbsp;无</label>
						</td>
						<th></th>
						<td></td>
					</tr>
				</table>
				<table class="table_ui W100" id="firstApprovalOpinion_record_table">
					<tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
						<th width="12%">个人流水{{ $index != 0 ? $index : '' }}:</th>
						<td width="52%">
							<input type="text" name="personalWater1" ms-attr="{value: checkStatement.water1 || ''}" class="input">&nbsp;
							<input type="text" name="personalWater2" ms-attr="{value: checkStatement.water2 || ''}" class="input">&nbsp;
							<input type="text" name="personalWater3" ms-attr="{value: checkStatement.water3 || ''}" class="input">
						</td>
						<th width="12%">个人流水月均:</th>
						<td class="W10 numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) || ''}}元</td>
						<td>
							<a href="javaScript:void(0);" ms-on-click="addCheckStatement('C')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
							<a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)"  ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
						</td>
					</tr>
					<tr>
						<th>
							个人流水月均合计:
							<input type="hidden" name="personalWaterTotal" ms-attr="{value: personalWaterAverageTotal }">
						</th>
						<td class="personalRecordCount">{{ personalWaterAverageTotal || ''}}元</td>
					</tr>
					<tr class="publicRecordTr" ms-for="($index, checkStatement) in publicCheckStatementArray">
						<th>对公流水{{ $index != 0 ? $index : '' }}:</th>
						<td>
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater1', value: checkStatement.water1 || ''}">&nbsp;
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater2', value: checkStatement.water2 || ''}">&nbsp;
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater3', value: checkStatement.water3 || ''}">
						</td>
						<th>对公流水月均:</th>
						<td class="W10 numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) || ''}}元</td>
						<td>
							<a href="javaScript:void(0);" ms-on-click="addCheckStatement('B')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
							<a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)"  ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
						</td>
					</tr>
					<tr>
						<th>
							对公流水月均合计:
							<input type="hidden" name="publicWaterTotal" ms-attr="{value: publicWaterAverageTotal}">
						</th>
						<td class="publicRecordCount numFormat">{{ publicWaterAverageTotal || ''}}元</td>
					</tr>
					<tr>
						<th>月均流水合计:</th>
						<td class="recordCount numFormat">{{ waterIncomeTotal || ''}}元</td>
					</tr>
				</table>
				<h3>其他核实信息</h3>
				<table class="table_ui W100">
					<tr>
						<th width="12%">法院网核查无异常:</th>
						<td width="20%">
							<input type="checkbox" id="first_courtException_input" />
						</td>
						<th width="12%">内部匹配无异常:</th>
						<td><input type="checkbox" id="first_internalException_input" /></td>
						<th class="requiredFontWeight">征信平台已验证:</th>
						<td><input type="checkbox" id="first_creditCheckException_checkbox" ms-duplex-checked="applyBasicInfo.reportId > 0"></td>
					</tr>
					<tr>
						<th class="requiredFontWeight">有信用记录:</th><!-- 注意:解读出来的报告Type:是否符合无综合信用条件 YES NO 这里NO标示有 YES是无 -->
						<td>
							<input type="checkbox" ms-attr="{id: 'first_creditRecord_checkbox'}" ms-duplex-checked="ifCreditRecord == 1">
						</td>
						<th class="requiredFontWeight">人行近1个月查询:</th>
						<td>
							<input type="text" class="easyui-numberbox input" ms-attr="{id: 'firstApprovalOpinion_oneMonthsCount', name: 'oneMonthsCount'}"
								   ms-duplex="approvalInfo.approveCheckInfo.oneMonthsCount"
								   data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999]']">
						</td>
						<th class="requiredFontWeight">人行近3个月查询:</th>
						<td>
							<input type="text" class="easyui-numberbox input" ms-attr="{id:'firstApprovalOpinion_threeMonthsCount',name: 'threeMonthsCount'}"
								   ms-duplex="approvalInfo.approveCheckInfo.threeMonthsCount"
								   data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999999]','compareNum[\'#firstApprovalOpinion_oneMonthsCount\']']">
						</td>
					</tr>
					<tr>
						<th>备注:</th>
						<td colspan="5">
							<input class="easyui-textbox" name="memo" ms-duplex="approvalInfo.approveCheckInfo.memo || ''"
								   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
						</td>
					</tr>
				</table>
			</div>
			<div class="easyui-panel" title="负债信息">
				<a class="easyui-linkbutton" onclick="firstApprovalOpinionContrastDialog()">征信初判</a>
				<table class="table_list W100" id="firstApprovalOpinion_liabilities_table">
					<tr>
						<td class="specialThCol">信用卡总额度/元:</td>
						<td>
							<input type="text" ms-attr="{name: 'creditTotalLimit', value: reportInfo.creditLimitMoney ? reportInfo.creditLimitMoney : 0}" class="easyui-numberbox input"
								   data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],precision:0,buttonText:'元'">
						</td>
						<th class="specialThCol">信用卡已用额度/元</th>
						<td>
							<input type="text" ms-attr="{name: 'creditUsedLimit', value: reportInfo.alreadyUseMoney || 0}" class="easyui-numberbox input"
								   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
						</td>
						<td class="specialThCol">信用卡负债/元<input name="creditDebt" ms-attr="{value: reportInfo.debt || 0}" type="hidden"></td>
						<td id="first_liabilitiesCreditCard" class="W10 numFormat">{{ reportInfo.debt ? reportInfo.debt : 0 }}</td>
					</tr>
					<tr class="specialTrCol">
						<td class="specialThCol">信用贷款</td>
						<td class="specialThCol">额度/元</td>
						<td class="specialThCol">期限/月</td>
						<td class="specialThCol">负债/元</td>
						<td class="specialThCol">对应关系</td>
						<td class="specialThCol">操作</td>
					</tr>
					<!--  Start 判断是否是追加贷款 -->
					<!-- TODO 暂时取消topup -->
					<c:if test="${'TOPUP' == applyBasiceInfo.applyType }">
						<tr class="topUpTr">
							<td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" value="${topupLoan.money}" name="creditLoanLimit"></td>
							<td><input type="text" class="input" value="${topupLoan.time}" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" value="${topupLoan.returneterm}" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td></td>
						</tr>
					</c:if>
					<!--  End 判断是否是追加贷款 -->
					<!--  Start 判断贷款不为空 -->
					<c:if test="${not empty loanLimitInfo }">
						<c:forEach items="${loanLimitInfo.loan}" var="item" varStatus="itemStatr">
							<tr class="creditCardTr">
								<td>信用贷款<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
								<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
								<td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
								<td></td>
							</tr>
						</c:forEach>
						<tr class="creditCardTr">
							<td>信用贷款<span>${fn:length(loanLimitInfo.loan)+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr>
							<td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
							<td class="creditCountLiabilities" colspan="5">0</td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">房贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${loanLimitInfo.house}" var="item" varStatus="itemStatr">
							<tr class="creditCardTr">
								<td>房贷<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
								<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
								<td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
								<td></td>
							</tr>
						</c:forEach>
						<tr class="creditCardTr">
							<td>房贷<span>${fn:length(loanLimitInfo.house)+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">车贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${loanLimitInfo.car}" var="item" varStatus="itemStatr">
							<tr class="creditCardTr">
								<td>车贷<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
								<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
								<td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
								<td></td>
							</tr>
						</c:forEach>
						<tr class="creditCardTr">
							<td>车贷<span>${fn:length(loanLimitInfo.car)+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">其他贷款</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${loanLimitInfo.other}" var="item" varStatus="itemStatr">
							<tr class="creditCardTr">
								<td>其他贷款<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
								<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
								<td></td>
							</tr>
						</c:forEach>
						<tr class="creditCardTr">
							<td>其他贷款<span>${fn:length(loanLimitInfo.other)+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
					</c:if>
					<!--  End 判断贷款不为空 -->
					<!--  Start 判断贷款为空 -->
					<c:if test="${empty loanLimitInfo }">
						<tr class="creditCardTr">
							<td>信用贷款<span>1</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr>
							<td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
							<td class="creditCountLiabilities" colspan="5">0</td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">房贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<tr class="creditCardTr">
							<td>房贷<span>1</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">车贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<tr class="creditCardTr">
							<td>车贷<span>1</span><input type="hidden" name="debtType" value="CARLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">其他贷款</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<tr class="creditCardTr">
							<td>其他贷款<span>1</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
					</c:if>
					<tr>
						<td class="specialThCol">外部负债总额</td>
						<td>
							<input id="outDebtTotal_id" name="outDebtTotal" value="0" class="easyui-numberbox input"
								   data-options="groupSeparator:',',required:true,min:0,max:99999999,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
						</td>
						<td  class="specialThCol" align="right">速贷还款情况<input id="first_ifBlackRoster" type="hidden" name="ifBlackRoster"></td>
						<td colspan="3">
							<select ms-attr="{id: 'first_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
									data-options="required:true,panelHeight:'auto',editable:false,value:''">
								<option value="NORMAL">正常</option>
								<option value="OVERDUE">逾期</option>
								<option value="SETTLE">结清</option>
								<option value="NOLOAN">无贷款</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="specialThCol">备注</td>
						<td colspan="6">
							<input class="easyui-textbox" name="memo"
								   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'"  >
						</td>
					</tr>
					<!--  End 判断贷款为空 -->
				</table>
			</div>
			<!-- ---------------------------------------End 审核记录为空--------------------------------------->
		</c:if>
		<c:if test="${not empty approvalInfo.approvalHistoryList}">
			<!-- ---------------------------------------Start 审核记录不为空--------------------------------------->
			<div class="easyui-panel" title="资料核对">
				<h3>工作信息:</h3>
				<table class="table_ui W100">
					<tr>
						<th width="12%">可接受的月最高还款:</th>
						<td width="20%" class="numFormat">{{ applyInfo.basicInfoVO.personInfoVO.monthMaxRepay || '' }}</td>
						<th width="12%">客户工作类型:</th>
						<td width="20%">{{ applyInfoOnlyRead.basicInfoVO.workInfoVO.cusWorkType || '' }}</td>
						<th width="12%">发薪方式:</th>
						<td>{{ applyInfoOnlyRead.basicInfoVO.workInfoVO.corpPayWay || ''}}</td>
					</tr>
					<tr>
						<th>单位月收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.monthSalary || ''}}</td>
						<th>其他月收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.otherIncome || ''}}</td>
						<th>月总收入/元:</th>
						<td class="numFormat">{{ applyInfo.basicInfoVO.workInfoVO.totalMonthSalary || ''}}</td>
					</tr>
					<tr>
						<th>收入证明金额:</th>
						<td>
							<input type="text" class="easyui-numberbox input" ms-attr="{name: 'incomeCertificate', value: applyBasicInfo.amoutIncome > 0 ? applyBasicInfo.amoutIncome : ''}"
								   data-options="groupSeparator:',',precision:0,buttonText:'元',panelHeight:'auto',validType:['compareMaxMin[0,99999999]']" />
						</td>
						<th>有无周末发薪:</th>
						<td>
							<input type="radio" ms-attr="{id: 'first_weekendTrue_radioFor',name: 'weekendPay'}" value="0"  ms-duplex-checked="approvalInfo.approveCheckInfo.weekendPay == 0">
								<label for="first_weekendTrue_radioFor">&nbsp;有</label>&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" ms-attr="{id: 'first_weekendFalse_radioFor',name: 'weekendPay'}" value="1" ms-duplex-checked="approvalInfo.approveCheckInfo.weekendPay == 1">
								<label for="first_weekendFalse_radioFor">&nbsp;无</label>
						</td>
						<th></th>
						<td></td>
					</tr>
				</table>
				<table class="table_ui W100" ms-attr="{id: 'firstApprovalOpinion_record_table'}">
					<tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
						<th width="12%">个人流水{{ $index != 0 ? $index : '' }}:</th>
						<td width="52%">
							<input type="text" name="personalWater1" ms-attr="{value: defaultIfNull(checkStatement.water1)}" class="input">&nbsp;
							<input type="text" name="personalWater2" ms-attr="{value: defaultIfNull(checkStatement.water2)}" class="input">&nbsp;
							<input type="text" name="personalWater3" ms-attr="{value: defaultIfNull(checkStatement.water3)}" class="input">
						</td>
						<th width="12%">个人流水月均:</th>
						<td class="W10 numFormat">{{getRecordCount(checkStatement.water1, checkStatement.water2, checkStatement.water3)}}元</td>
						<td>
							<a href="javaScript:void(0);" ms-on-click="addCheckStatement('C')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
							<a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)"  ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
						</td>
					</tr>
					<tr>
						<th>个人流水月均合计:<input type="hidden" name="personalWaterTotal" ms-attr="{value: personalWaterAverageTotal}"></th>
						<td class="personalRecordCount numFormat" ms-html="getWaterTotal('personalWaterAverageTotal') + '元'"></td>
					</tr>
					<tr class="publicRecordTr" ms-for="($index, checkStatement) in publicCheckStatementArray">
						<th>对公流水{{ $index != 0 ? $index : '' }}:</th>
						<td>
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater1', value: defaultIfNull(checkStatement.water1)}">&nbsp;
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater2', value: defaultIfNull(checkStatement.water2)}">&nbsp;
							<input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater3', value: defaultIfNull(checkStatement.water3)}">
						</td>
						<th>对公流水月均:<input name="publicMonthAverage" type="hidden" ms-attr="{value: checkStatement.monthAverage}"></th>
						<td class="W10 numFormat">{{ getRecordCount(checkStatement.water1, checkStatement.water2, checkStatement.water3)}}元</td>
						<td>
							<a href="javaScript:void(0);" ms-on-click="addCheckStatement('B')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
							<a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)" ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
						</td>
					</tr>
					<tr>
						<th>对公流水月均合计:<input type="hidden" name="publicWaterTotal" ms-attr="{value: publicWaterAverageTotal}"></th>
						<td class="publicRecordCount numFormat" ms-html="getWaterTotal('publicWaterAverageTotal') + '元'"></td>
					</tr>
					<tr>
						<th>月均流水合计:<input type="hidden" name="waterIncomeTotal"  ms-attr="{value: waterIncomeTotal}"></th>
						<td class="recordCount numFormat" ms-html="getWaterTotal('waterIncomeTotal') + '元'"></td>
					</tr>
				</table>
				<h3>其他核实信息</h3>
				<table class="table_ui W100">
					<tr>
						<th width="12%">法院网核查无异常:</th>
						<td width="20%"><input type="checkbox" ms-attr="{id:'first_courtException_input'}" ms-duplex-checked="approvalInfo.approveCheckInfo.courtCheckException == 0"/></td>
						<th width="12%">内部匹配无异常:</th>
						<td><input type="checkbox" ms-attr="{id: 'first_internalException_input'}" ms-duplex-checked="approvalInfo.approveCheckInfo.internalCheckException == 0" /></td>
						<th class="requiredFontWeight">征信平台已验证:</th>
						<td><input type="checkbox" ms-attr="{id: 'first_creditCheckException_checkbox'}" ms-duplex-checked="approvalInfo.approveCheckInfo.creditCheckException == 0"></td>
					</tr>
					<tr>
						<th class="requiredFontWeight">有信用记录:</th>
						<td>
							<input type="checkbox" ms-attr="{id: 'first_creditRecord_checkbox'}" ms-duplex-checked="ifCreditRecord == 1">
						</td>
						<th class="requiredFontWeight">人行近1个月查询:</th>
						<td>
							<input type="text" class="easyui-numberbox input" ms-attr="{id: 'firstApprovalOpinion_oneMonthsCount', name: 'oneMonthsCount'}"
								   ms-duplex="approvalInfo.approveCheckInfo.oneMonthsCount"
								   data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999]']">
						</td>
						<th class="requiredFontWeight">人行近3个月查询:</th>
						<td>
							<input type="text" class="easyui-numberbox input" ms-attr="{id:'firstApprovalOpinion_threeMonthsCount',name: 'threeMonthsCount'}"
								   ms-duplex="approvalInfo.approveCheckInfo.threeMonthsCount"
								   data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999999]','compareNum[\'#firstApprovalOpinion_oneMonthsCount\']']">
						</td>
					</tr>
					<tr>
						<th>备注:</th>
						<td colspan="5">
							<input class="easyui-textbox" name="memo" ms-duplex="approvalInfo.approveCheckInfo.memo || ''"
								   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'"  >
						</td>
					</tr>
				</table>
			</div>
			<c:set value="0" var="creditLoanLength" />
			<c:set value="0" var="houseLoanLength" />
			<c:set value="0" var="carLoanLength" />
			<c:set value="0" var="otherLoanLength" />
			<div class="easyui-panel" title="负债信息">
				<a class="easyui-linkbutton" onclick="firstApprovalOpinionContrastDialog()">征信初判</a>
				<!-- 有审批意见时重新解读央行报告 通过标识央行报告是否改变来判断 更为进准 begin-->
				<c:if test="${'Y' == markReportIdChange}">
					<table class="table_list W100" id="firstApprovalOpinion_liabilities_table">
						<tr>
							<td class="specialThCol">信用卡总额度/元:</td>
							<td>
								<input type="text" name="creditTotalLimit" value="${not empty reportInfo.creditLimitMoney?reportInfo.creditLimitMoney:0}"
									   class="easyui-numberbox input"
									   data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],precision:0,buttonText:'元'">
							</td>
							<th class="requiredFontWeight specialThCol">信用卡已用额度/元</th>
							<td>
								<input type="text" name="creditUsedLimit" value="${not empty reportInfo.alreadyUseMoney?reportInfo.alreadyUseMoney:0}"
									   class="easyui-numberbox input"
									   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
							</td>
							<td class="specialThCol">
								信用卡负债/元<input name="creditDebt" value="${not empty reportInfo.debt?reportInfo.debt:0}" type="hidden">
							</td>
							<td ms-attr="{id: 'first_liabilitiesCreditCard'}" class="W10 numFormat">
								<fmt:formatNumber type="number" value="${not empty reportInfo.debt?reportInfo.debt:0}" pattern="0" maxFractionDigits="0" />
							</td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">信用贷款</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<!--  Start 判断是否是追加贷款 -->
						<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
							<c:if test="${not empty item.creditLoanDebt }">
								<c:if test="${item.debtType =='TOPUPLOAN'}">
									<!--  topup显示第一位 -->
									<tr class="topUpTr">
										<td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
										<td><input type="text" class="input" data-options="groupSeparator:','" value="${item.creditLoanLimit}" name="creditLoanLimit"></td>
										<td><input type="text" class="input" value="${item.creditLoanTerm}" name="creditLoanTerm"></td>
										<td><input type="text" class="input" data-options="groupSeparator:','" value="${item.creditLoanDebt}" name="creditLoanDebt"></td>
										<td><input type="hidden" name="creditNo" value="${item.creditNo}" placeholder="对应关系"></td>
										<td></td>
									</tr>
								</c:if>
							</c:if>
						</c:forEach>
						<!--  End 判断是否是追加贷款 -->
						<!--  Start 判断贷款不为空 -->
						<c:if test="${not empty loanLimitInfo }">
							<c:forEach items="${loanLimitInfo.loan}" var="item" varStatus="itemStatr">
								<c:set value="${creditLoanLength+1}" var="creditLoanLength" />
								<tr class="creditCardTr">
									<td>信用贷款<span>${creditLoanLength}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
									<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
									<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
									<td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
									<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
									<td></td>
								</tr>
							</c:forEach>
						</c:if>
							<tr class="creditCardTr">
								<td>信用贷款<span>${creditLoanLength+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr>
								<td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
								<td class="creditCountLiabilities" colspan="5">0</td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">房贷</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
						<c:if test="${not empty loanLimitInfo }">
							<c:forEach items="${loanLimitInfo.house}" var="item" varStatus="itemStatr">
								<c:set value="${houseLoanLength+1}" var="houseLoanLength" />
								<tr class="creditCardTr">
									<td>房贷<span>${houseLoanLength}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
									<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
									<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
									<td><input type="text"value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
									<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
									<td></td>
								</tr>
							</c:forEach>
						</c:if>
							<tr class="creditCardTr">
								<td>房贷<span>${houseLoanLength+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">车贷</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
						<c:if test="${not empty loanLimitInfo }">
							<c:forEach items="${loanLimitInfo.car}" var="item" varStatus="itemStatr">
								<c:set value="${carLoanLength+1}" var="carLoanLength" />
								<tr class="creditCardTr">
									<td>车贷<span>${carLoanLength}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
									<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
									<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
									<td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
									<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
									<td></td>
								</tr>
							</c:forEach>
						</c:if>
							<tr class="creditCardTr">
								<td>车贷<span>${carLoanLength+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">其他贷款</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
						<c:if test="${not empty loanLimitInfo }">
							<c:forEach items="${loanLimitInfo.other}" var="item" varStatus="itemStatr">
								<c:set value="${otherLoanLength +1}" var="otherLoanLength" />
								<tr class="creditCardTr">
									<td>其他贷款<span>${otherLoanLength}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
									<td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
									<td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
									<td><input type="text" value="" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
									<td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
									<td></td>
								</tr>
							</c:forEach>
						</c:if>
							<tr class="creditCardTr">
								<td>其他贷款<span>${otherLoanLength+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
						<!--  End 判断贷款不为空 -->
						<!--  Start 判断贷款为空  贷款为空不需要填充原来的 -->

						<%-- <c:if test="${empty loanLimitInfo }">
							<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
								<c:if test="${not empty item.creditLoanDebt }">
									<c:if test="${item.debtType =='CREDITLOAN'}">
										<c:set value="${creditLoanLength+1}" var="creditLoanLength" />
										<tr class="creditCardTr">
											<td>信用贷款<span>${creditLoanLength}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
											<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
											<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
											<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
											<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
											<td><c:if test="${empty item.creditNo}">
												<a href="javaScript:void(0);" onclick="deleteTr(this,'CREDITLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
											</c:if></td>
										</tr>
									</c:if>
								</c:if>
							</c:forEach>
							<tr class="creditCardTr">
								<td>信用贷款<span>${creditLoanLength+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr>
								<td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}"></td>
								<td class="creditCountLiabilities numFormat" colspan="5"><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}" pattern="0" maxFractionDigits="0" /></td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">房贷</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
							<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
								<c:if test="${item.debtType =='HOUSELOAN'}">
									<c:set value="${houseLoanLength+1}" var="houseLoanLength" />
									<tr class="creditCardTr">
										<td>房贷<span>${houseLoanLength}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
										<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
										<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
										<td><c:if test="${empty item.creditNo}">
											<a href="javaScript:void(0);" onclick="deleteTr(this,'HOUSELOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
										</c:if></td>
									</tr>
								</c:if>
							</c:forEach>
							<tr class="creditCardTr">
								<td>房贷<span>${houseLoanLength+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">车贷</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
							<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
								<c:if test="${item.debtType =='CARLOAN'}">
									<c:set value="${carLoanLength+1}" var="carLoanLength" />
									<tr class="creditCardTr">
										<td>车贷<span>${carLoanLength}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
										<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
										<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
										<td><c:if test="${empty item.creditNo}">
											<a href="javaScript:void(0);" onclick="deleteTr(this,'CARLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
										</c:if></td>
									</tr>
								</c:if>
							</c:forEach>
							<tr class="creditCardTr">
								<td>车贷<span>${carLoanLength+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
							<tr class="specialTrCol">
								<td class="specialThCol">其他贷款</td>
								<td class="specialThCol">额度/元</td>
								<td class="specialThCol">期限/月</td>
								<td class="specialThCol">负债/元</td>
								<td class="specialThCol">对应关系</td>
								<td class="specialThCol">操作</td>
							</tr>
							<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
								<c:if test="${item.debtType =='OTHERLOAN'}">
									<c:set value="${otherLoanLength +1}" var="otherLoanLength" />
									<tr class="creditCardTr">
										<td>其他贷款<span>${otherLoanLength}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
										<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
										<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
										<td><c:if test="${empty item.creditNo}">
											<a href="javaScript:void(0);" onclick="deleteTr(this,'OTHERLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
										</c:if></td>
									</tr>
								</c:if>
							</c:forEach>
							<tr class="creditCardTr">
								<td>其他贷款<span>${otherLoanLength+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
								<td><input type="text" class="input" name="creditLoanTerm"></td>
								<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
								<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
								<td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
							</tr>
						</c:if> --%>
						<tr>
							<td class="specialThCol">外部负债总额</td>
							<td>
								<input id="outDebtTotal_id" value="0" name="outDebtTotal" class="easyui-numberbox input"
									   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
							</td>
							<td class="specialThCol" align="right">
								速贷还款情况
								<input ms-attr="{id: 'first_ifBlackRoster', name: 'ifBlackRoster'}" type="hidden" value="${approvalInfo.debtsInfoList[0].ifBlackRoster}">
							</td>
							<td colspan="3">
								<select ms-attr="{id:'first_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
									   data-options="required:true,panelHeight:'auto',editable:false,value:'${approvalInfo.debtsInfoList[0].fastLoanSituation}'">
									<option value="NORMAL">正常</option>
									<option value="OVERDUE">逾期</option>
									<option value="SETTLE">结清</option>
									<option value="NOLOAN">无贷款</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="specialThCol">备注</td>
							<td colspan="6">
								<input class="easyui-textbox" name="memo" value="${approvalInfo.debtsInfoList[0].memo}"
									   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'"  >
							</td>
						</tr>
						<!--  End 判断贷款为空 -->
					</table>
				</c:if>
				<!-- 有审批意见时重新解读央行报告end -->
				<!-- 有审批意见时不重新解读央行报告begin -->
				<c:if test="${'Y' != markReportIdChange}">
					<table class="table_list W100" id="firstApprovalOpinion_liabilities_table">
						<tr>
							<td class="specialThCol">信用卡总额度/元:</td>
							<td>
								<input type="text" name="creditTotalLimit" value="${approvalInfo.debtsInfoList[0].creditTotalLimit}" class="easyui-numberbox input"
									   data-options="groupSeparator:',',min:0,max:99999999,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
							</td>
							<th class="requiredFontWeight specialThCol">信用卡已用额度</th>
							<td>
								<input type="text" name="creditUsedLimit" value="${approvalInfo.debtsInfoList[0].creditUsedLimit}" class="easyui-numberbox input"
									   data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],required:true,precision:0,buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
							</td>
							<td class="specialThCol">
								信用卡负债<input name="creditDebt" type="hidden" value="${approvalInfo.debtsInfoList[0].creditDebt}">
							</td>
							<td id="first_liabilitiesCreditCard" class="W10 numFormat">
								<fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditDebt}" pattern="0" maxFractionDigits="0" />
							</td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">信用贷款</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
							<c:if test="${not empty item.creditLoanLimit or not empty item.creditLoanTerm or not empty item.creditLoanDebt }">
								<c:if test="${item.debtType =='TOPUPLOAN'}">
									<tr class="topUpTr">
										<td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
										<td><input type="text" name="creditLoanLimit" class="input" value="${item.creditLoanLimit}" data-options="groupSeparator:','" ></td>
										<td><input type="text" name="creditLoanTerm"  class="input" value="${item.creditLoanTerm}" ></td>
										<td><input type="text" name="creditLoanDebt"  class="input" value="${item.creditLoanDebt}" data-options="groupSeparator:','" ></td>
										<td>
											<input type="hidden" name="creditNo" value="${item.creditNo}" placeholder="对应关系">
										</td>
										<td></td>
									</tr>
								</c:if>
								<c:if test="${item.debtType =='CREDITLOAN'}">
									<c:set value="${creditLoanLength+1}" var="creditLoanLength" />
									<tr class="creditCardTr">
										<td>信用贷款<span>${creditLoanLength}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
										<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
										<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
										<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
										<td>
											<input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}
										</td>
										<td>
											<c:if test="${empty item.creditNo}">
											<a href="javaScript:void(0);" onclick="deleteTr(this,'CREDITLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
											</c:if>
										</td>
									</tr>
								</c:if>
							</c:if>
						</c:forEach>
						<tr class="creditCardTr">
							<td>信用贷款<span>${creditLoanLength+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
							<td><input type="text" name="creditLoanLimit" class="input" data-options="groupSeparator:','" ></td>
							<td><input type="text" name="creditLoanTerm" class="input"></td>
							<td><input type="text" name="creditLoanDebt" class="input" data-options="groupSeparator:','"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr>
							<td>外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}"></td>
							<td class="creditCountLiabilities numFormat" colspan="5"><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}" pattern="0" maxFractionDigits="0" /></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">房贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
							<c:if test="${item.debtType =='HOUSELOAN'}">
								<c:set value="${houseLoanLength+1}" var="houseLoanLength" />
								<tr class="creditCardTr">
									<td>房贷<span>${houseLoanLength}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
									<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
									<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
									<td><c:if test="${empty item.creditNo}">
										<a href="javaScript:void(0);" onclick="deleteTr(this,'HOUSELOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
									</c:if></td>
								</tr>
							</c:if>
						</c:forEach>
						<tr class="creditCardTr">
							<td>房贷<span>${houseLoanLength+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">车贷</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
							<c:if test="${item.debtType =='CARLOAN'}">
								<c:set value="${carLoanLength+1}" var="carLoanLength" />
								<tr class="creditCardTr">
									<td>车贷<span>${carLoanLength}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
									<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
									<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
									<td><c:if test="${empty item.creditNo}">
										<a href="javaScript:void(0);" onclick="deleteTr(this,'CARLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
									</c:if></td>
								</tr>
							</c:if>
						</c:forEach>
						<tr class="creditCardTr">
							<td>车贷<span>${carLoanLength+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr class="specialTrCol">
							<td class="specialThCol">其他贷款</td>
							<td class="specialThCol">额度/元</td>
							<td class="specialThCol">期限/月</td>
							<td class="specialThCol">负债/元</td>
							<td class="specialThCol">对应关系</td>
							<td class="specialThCol">操作</td>
						</tr>
						<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
							<c:if test="${item.debtType =='OTHERLOAN'}">
								<c:set value="${otherLoanLength +1}" var="otherLoanLength" />
								<tr class="creditCardTr">
									<td>其他贷款<span>${otherLoanLength}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
									<td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
									<td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
									<td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
									<td><c:if test="${empty item.creditNo}">
										<a href="javaScript:void(0);" onclick="deleteTr(this,'OTHERLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
									</c:if></td>
								</tr>
							</c:if>
						</c:forEach>
						<tr class="creditCardTr">
							<td>其他贷款<span>${otherLoanLength+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
							<td><input type="text" class="input" name="creditLoanTerm"></td>
							<td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
							<td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
							<td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
						</tr>
						<tr>
							<td class="specialThCol">外部负债总额</td>
							<td>
								<input id="outDebtTotal_id" name="outDebtTotal" value="${approvalInfo.debtsInfoList[0].outDebtTotal}" class="easyui-numberbox input"
									   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
							</td>
							<td  class="specialThCol" align="right">速贷还款情况<input id="first_ifBlackRoster" type="hidden" name="ifBlackRoster" value="${approvalInfo.debtsInfoList[0].ifBlackRoster}"></td>
							<td colspan="3">
								<select ms-attr="{id: 'first_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
										data-options="required:true,panelHeight:'auto',editable:false,value:'${approvalInfo.debtsInfoList[0].fastLoanSituation}'">
									<option value="NORMAL">正常</option>
									<option value="OVERDUE">逾期</option>
									<option value="SETTLE">结清</option>
									<option value="NOLOAN">无贷款</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="specialThCol">备注</td>
							<td colspan="6">
								<input class="easyui-textbox" name="memo" value="${approvalInfo.debtsInfoList[0].memo}"
									   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'"  >
							</td>
						</tr>
					</table>
				</c:if>
			</div>
			<!-----------------------------------------End 审核记录不为空--------------------------------------->
		</c:if>
	</form>
	<!-- ---------------------------------------Start 产品信息判断--------------------------------------->
	<div class="easyui-panel" title="产品信息">
		<div id="first_approval_assetsInfo_div" data-name="${applyInfo.applyInfoVO.productCd}">
			<!-- 保单信息 -->
			<table class="table_list W100 display_noH" id="POLICY">
				<tr>
					<td>保险金额:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.policyInfoVO.insuranceAmt}" maxFractionDigits="0" /></td>
					<td>保险年限:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.policyInfoVO.insuranceTerm== 999 ? "终身": applyInfoOnlyRead.assetsInfoVO.policyInfoVO.insuranceTerm.toString().concat(' 年')}</td>
					<td>已缴年限:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.policyInfoVO.paidTerm}</td>
				</tr>
				<tr>
					<td>年缴金额/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.policyInfoVO.yearPaymentAmt}" maxFractionDigits="0" /></td>
					<c:if test="${'Y'== applyInfoOnlyRead.assetsInfoVO.policyInfoVO.policyCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_policyCheckIsVerifyY_radioFor" checked="checked" name="policyCheckIsVerify" value="Y"> <label for="first_policyCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="policyCheckIsVerify" id="first_policyCheckIsVerifyN_radioFor" value="N"> <label for="first_policyCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<c:if test="${ 'N'==applyInfoOnlyRead.assetsInfoVO.policyInfoVO.policyCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_policyCheckIsVerifyY_radioFor" name="policyCheckIsVerify" value="Y"> <label for="first_policyCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" checked="checked" name="policyCheckIsVerify" id="first_policyCheckIsVerifyN_radioFor" value="N"> <label for="first_policyCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<c:if test="${ empty applyInfoOnlyRead.assetsInfoVO.policyInfoVO.policyCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_policyCheckIsVerifyY_radioFor" name="policyCheckIsVerify" value="Y"> <label for="first_policyCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="policyCheckIsVerify" id="first_policyCheckIsVerifyN_radioFor" value="N"> <label for="first_policyCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<td></td>
					<td></td>
				</tr>
			</table>
			<!-- 车辆信息 -->
			<table class="table_list W100 display_noH" id="CAR">
				<tr>
					<td>购买价/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.carInfoVO.carBuyPrice}" maxFractionDigits="0" /></td>
					<td>购买时间:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.carInfoVO.carBuyDate}" pattern="yyyy-MM" type="date" /></td>
					<td>车牌号:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.carInfoVO.plateNum}</td>
				</tr>
				<tr>
					<td>是否有车贷:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.carInfoVO.carLoan}</td>
					<td>贷款发放年月:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.carInfoVO.carLoanIssueDate}" pattern="yyyy-MM" type="date" /></td>
					<td>月供/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.carInfoVO.monthPaymentAmt}" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<c:if test="${'Y'== applyInfoOnlyRead.assetsInfoVO.carInfoVO.carCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_carCheckIsVerifyY_radioFor" checked="checked" name="carCheckIsVerify" value="Y"> <label for="first_carCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="carCheckIsVerify" id="first_carCheckIsVerifyN_radioFor" value="N"> <label for="first_carCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<c:if test="${ 'N'==applyInfoOnlyRead.assetsInfoVO.carInfoVO.carCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_carCheckIsVerifyY_radioFor" name="carCheckIsVerify" value="Y"> <label for="first_carCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="carCheckIsVerify" checked="checked" id="first_carCheckIsVerifyN_radioFor" value="N"> <label for="first_carCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<c:if test="${ empty applyInfoOnlyRead.assetsInfoVO.carInfoVO.carCheckIsVerify}">
						<td>信审保单已核实:</td>
						<td><input type="radio" id="first_carCheckIsVerifyY_radioFor" name="carCheckIsVerify" value="Y"> <label for="first_carCheckIsVerifyY_radioFor">&nbsp;是</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="carCheckIsVerify" id="first_carCheckIsVerifyN_radioFor" value="N"> <label for="first_carCheckIsVerifyN_radioFor">&nbsp;否</label></td>
					</c:if>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</table>
			<!-- 公积金信息 -->
			<table class="table_list W100 display_noH" id="PROVIDENT">
				<tr>
					<td>缴存比例%:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.depositRate}</td>
					<td>月缴存额/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.monthDepositAmt}" maxFractionDigits="0" /></td>
					<td>缴存基数/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.depositBase}" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<td>公积金材料:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.providentInfo}</td>
					<td>缴纳单位同申请单位:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.paymentUnit}</td>
					<td>申请单位已缴月数:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.providentInfoVO.paymentMonthNum}</td>
				</tr>
			</table>
			<!-- 卡友贷信息-->
			<table class="table_list W100 display_noH" id="CARDLOAN">
				<tr>
					<td>发卡时间:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.issuerDate}" pattern="yyyy-MM" type="date" /></td>
					<td>额度/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.creditLimit}" maxFractionDigits="0" /></td>
					<td></td>
				</tr>
				<tr>
					<td>近4个月账单金额依次为:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.billAmt1}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.billAmt2}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.billAmt3}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.billAmt4}" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<td>月均/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.cardLoanInfoVO.payMonthAmt}" maxFractionDigits="0" /></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</table>
			<!-- 房产信息 -->
			<table class="table_list W100 display_noH" id="ESTATE">
				<tr>
					<td>房产类型:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateType}</td>
					<td>购买时间:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateBuyDate}" pattern="yyyy-MM" type="date" /></td>
					<td>市值参考价/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.referenceAmt}" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<td>房产地址同住宅地址:</td>
					<td colspan="5"><c:if test="${'Y'==applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateSameRegistered}">是</c:if> <c:if test="${'Y'!=applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateSameRegistered}">否</c:if></td>
				</tr>
				<tr>
					<td>房产地址:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateState}</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateCity}</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateZone}</td>
					<td colspan="2">${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateAddress}</td>
				</tr>
				<tr>
					<td>房贷情况:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateLoan}</td>
					<td>房贷发放年月:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.estateLoanIssueDate}" pattern="yyyy-MM" type="date" /></td>
					<td>月供/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.monthPaymentAmt}" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<td>产权比例%:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.equityRate}</td>
					<td>单据户名为本人:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.estateInfoVO.ifMe}</td>
					<td></td>
					<td></td>
				</tr>
			</table>
			<!-- 网购达人贷B信息 -->
			<table class="table_list W100 display_noH" id="MASTERLOAN_B">
				<tr>
					<td>京东用户等级:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel}</td>
					<td>小白信用分:</td>
					<td><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.whiteCreditValue}" maxFractionDigits="1" /></td>
					<td>近一年实际消费金额/元:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount}" maxFractionDigits="0" /></td>
				</tr>
			</table>
			<!-- 网购达人贷A信息 -->
			<form id="MASTERLOAN_A_form">
				<table class="table_list W100 display_noH" id="MASTERLOAN_A">
					<tr>
						<td>买家信用等级:</td>
						<td>${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel}</td>
						<td>买家信用类型:</td>
						<td>${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.buyerCreditType}</td>
						<td>芝麻信用分:</td>
						<td>${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.sesameCreditValue}</td>
					</tr>
					<tr>
						<td>近12个月支出额:</td>
						<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt}" maxFractionDigits="0" /></td>
						<td>淘气值:</td>
						<td colspan="4"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoAVO.naughtyValue}" maxFractionDigits="0" /></td>
					</tr>
					<tr>
						<td>3个月内收货地址</td>
						<td><select name="threeMonthsAddress" class="easyui-combobox select" data-options="width:200,panelHeight:'auto',required:true,editable:false,value:'${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.onlineAWithin3MonthsAddress}'">
								<option value="00001">同住址</option>
								<option value="00002">同司址</option>
								<option value="00003">其他</option>
						</select></td>
						<td>6个月外收货地址</td>
						<td><select name="sixMonthsAddress" class="easyui-combobox select" data-options="width:200,panelHeight:'auto',required:true,editable:false,value:'${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.onlineAWithin6MonthsAddress}'">
								<option value="00001">同住址</option>
								<option value="00002">同司址</option>
								<option value="00003">其他</option>
						</select></td>
						<td><c:if test="${isMasterLoanADate}">12个月外收货地址</c:if></td>
						<td><c:if test="${isMasterLoanADate}"><select name="oneYearAddress" class="easyui-combobox select" data-options="width:200,panelHeight:'auto',required:true,editable:false,value:'${applyInfoOnlyRead.assetsInfoVO.masterLoanInfoVO.onlineAWithin12MonthsAddress}'">
								<option value="00001">同住址</option>
								<option value="00002">同司址</option>
								<option value="00003">其他</option>
						</select></c:if></td>
					</tr>
				</table>
			</form>
			<!-- 淘宝商户贷 -->
			<table class="table_list W100 display_noH" id="MERCHANTLOAN">
				<tr>
					<td>开店时间:</td>
					<td><fmt:formatDate value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.setupShopDate}" pattern="yyyy-MM" type="date" /></td>
					<td>卖家信用等级:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}</td>
					<td>卖家信用类型:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.sellerCreditType}</td>
				</tr>
				<tr>
					<td>近半年好评数:</td>
					<td>${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.regardedNum}</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>近6个月账单金额依次为:</td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt1}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt2}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt3}" maxFractionDigits="0" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt4}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt5}" maxFractionDigits="0" /></td>
					<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.billAmt6}" maxFractionDigits="0" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>月均:</td>
					<td colspan="5" class="numFormat"><fmt:formatNumber type="number" value="${applyInfoOnlyRead.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}" maxFractionDigits="0" /></td>
				</tr>
			</table>
		</div>
	</div>
	<!-- ---------------------------------------End 产品信息判断--------------------------------------->
	<!-- ---------------------------------------Start 系统初判--------------------------------------->
	<div class="easyui-panel">
		<h2>
			系统初判 <a class="easyui-linkbutton" onclick="firstSystemDetermine('${applyInfo.applyInfoVO.loanNo}')">系统初判</a>
		</h2>
		<table id="first_systemDetermine_table" class="table_ui W80">
			<tr>
				<th>建议核实收入:</th>
				<td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.adviceVerifyIncome}" maxFractionDigits="0" /></td>
				<th>建议到手金额:</th>
				<td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.adviceAuditLines}" maxFractionDigits="0" /></td>
				<th>预估评级费:</th>
				<td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.estimatedCost}" maxFractionDigits="2"/></td>
			</tr>
		</table>
	</div>
	<!-- ---------------------------------------End 系统初判--------------------------------------->

	<!-- ---------------------------------------Start 审批意见--------------------------------------->
	<div class="easyui-panel" title="审批意见">
		<form id="firstApprovalOpinion_approvalInfo_from">
			<input type="hidden" id="contractBranchId" ms-duplex="applyInfo.applyInfoVO.contractBranchId">
			<c:forEach items="${approvalInfo.approvalHistoryList}" var="item" varStatus="stat">
				<!-- 不是最后一个就直接展示信息 -->
				<c:if test="${!stat.last}">
					<h2>第${stat.index+1}次审批</h2>
					<table class="table_ui W100">
						<tr>
							<c:choose>
								<c:when test="${item.rtfState=='XSZS'}">
									<th>终审人员:</th>
								</c:when>
								<c:otherwise>
									<th>初审人员:</th>
								</c:otherwise>
							</c:choose>
							<td>${item.approvalPersonName}</td>
							<th>审批时间:</th>
							<td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
						</tr>
						<tr>
							<th>申请额度:</th>
							<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
							<th>申请期限:</th>
							<td>${item.approvalApplyTerm}</td>
							<th>审批产品:</th>
							<td>${item.approvalProductName}</td>
						</tr>
						<tr>
							<th>核实收入:</th>
							<td class="numFormat">
								<fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0" />
							</td>
							<c:choose>
								<c:when test="${item.rtfState=='XSZS'}">
									<th>终审审批额度:</th>
								</c:when>
								<c:otherwise>
									<th>初审审批额度:</th>
								</c:otherwise>
							</c:choose>
							<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0" /></td>
							<th>审批期限:</th>
							<td>${item.approvalTerm}</td>
						</tr>
						<tr>
							<th>月还款额:</th>
							<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0" /></td>
							<th>内部负债率:</th>
							<td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1" />%</td>
							<th>总负债率:</th>
							<td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1" />%</td>
						</tr>
						<tr>
							<th>审批意见:</th>
							<td colspan="5" rowspan="2" class="W90">${item.approvalMemo}</td>
						</tr>
					</table>
				</c:if>
				<!-- 是最后一个 -->
				<c:if test="${stat.last}">
					<c:if test="${ not empty item.rtfNodeState}">
						<h2>第${stat.index+1}次审批</h2>
						<table class="table_ui W100">
							<tr>
								<c:choose>
									<c:when test="${item.rtfState=='XSZS'}">
										<th>终审人员:</th>
									</c:when>
									<c:otherwise>
										<th>初审人员:</th>
									</c:otherwise>
								</c:choose>
								<td>${item.approvalPersonName}</td>
								<th>审批时间:</th>
								<td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
							</tr>
							<tr>
								<th>申请额度:</th>
								<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
								<th>申请期限:</th>
								<td>${item.approvalApplyTerm}</td>
								<th>审批产品:</th>
								<td>${item.approvalProductName}</td>
							</tr>
							<tr>
								<th>核实收入:</th>
								<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0" /></td>
								<c:choose>
									<c:when test="${item.rtfState=='XSZS'}">
										<th>终审审批额度:</th>
									</c:when>
									<c:otherwise>
										<th>初审审批额度:</th>
									</c:otherwise>
								</c:choose>
								<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0" /></td>
								<th>审批期限:</th>
								<td>${item.approvalTerm}</td>
							</tr>
							<tr>
								<th>月还款额:</th>
								<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0" /></td>
								<th>内部负债率:</th>
								<td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1" />%</td>
								<th>总负债率:</th>
								<td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1" />%</td>
							</tr>
							<tr>
								<th>审批意见:</th>
								<td colspan="5" rowspan="2" class="W90">${item.approvalMemo}</td>
							</tr>
						</table>
						<h2>信审初审</h2>
						<table class="table_ui W100">
							<tr>
								<th>初审人员:</th>
								<td>${resEmployeeVO.name}</td>
							</tr>
							<tr>
								<th>申请额度:</th>
								<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
								<th>申请期限:</th>
								<td>${applyInfo.applyInfoVO.applyTerm}</td>
								<th>审批产品:</th>
								<td><input type="text" name="approvalProductCd" value="${applyInfo.applyInfoVO.productCd}" class="input"></td>
							</tr>
							<tr>
								<th class="requiredFontWeight">核实收入:</th>
								<td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:','"></td>
								<th class="requiredFontWeight">
									初审审批额度:
									<input id="firstApprovalOpinion_applyMoney_hidden" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.applyInfoVO.applyLmt}">
								</th>
								<td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
								<th class="requiredFontWeight">审批期限:</th>
								<td><input type="text" name="approvalTerm" class="input"></td>
							</tr>
							<tr>
								<th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
								<td class="approvalMonthPay numFormat"></td>
								<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
								<td class="approvalDebtTate"></td>
								<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
								<td class="approvalAllDebtRate"></td>
							</tr>
							<tr>
								<th class="requiredFontWeight">审批意见:</th>
								<td colspan="5" rowspan="2">
									<input ms-attr="{id:'first_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
										   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo" >
									<span class="countSurplusText">可输入200字</span>
								</td>
							</tr>
						</table>
					</c:if>
					<c:if test="${empty item.rtfNodeState}">
						<c:choose>
							<c:when test="${item.approvalPerson==resEmployeeVO.usercode}">
								<h2>信审初审</h2>
								<table class="table_ui W100">
									<tr>
										<th>初审人员:</th>
										<td>${resEmployeeVO.name}</td>
										<th>审批时间:</th>
										<td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
									</tr>
									<tr>
										<th>申请额度:</th>
										<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
										<th>申请期限:</th>
										<td>${applyInfo.applyInfoVO.applyTerm}</td>
										<th>审批产品:</th>
										<td><input type="text" name="approvalProductCd" value="${item.approvalProductCd}" class="input"></td>
									</tr>
									<tr>
										<th class="requiredFontWeight">核实收入:</th>
										<td><input type="text" name="approvalCheckIncome" value="${item.approvalCheckIncome}" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
										<th class="requiredFontWeight">初审审批额度:<input id="firstApprovalOpinion_applyMoney_hidden" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.applyInfoVO.applyLmt}">
										</th>
										<td><input type="text" name="approvalLimit" value="${item.approvalLimit}" class="input" data-options="groupSeparator:','"></td>
										<th class="requiredFontWeight">审批期限:</th>
										<td><input type="text" name="approvalTerm" value="${item.approvalTerm}" class="input"></td>
									</tr>
									<tr>
										<th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
										<td class="approvalMonthPay numFormat"></td>
										<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
										<td class="approvalDebtTate"><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1" />%</td>
										<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
										<td class="approvalAllDebtRate"><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1" />%</td>
									</tr>
									<tr>
										<th class="requiredFontWeight">审批意见:</th>
										<td colspan="5" rowspan="2">
											<input ms-attr="{id:'first_approvalMemo', name: 'approvalMemo'}" value="${item.approvalMemo}" class="easyui-textbox W80"
												   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo">
											<span class="countSurplusText">可输入200字</span>
										</td>
									</tr>
								</table>
							</c:when>
							<c:otherwise>
								<h2>第${stat.index+1}次审批</h2>
								<table class="table_ui W100">
									<tr>
										<c:choose>
											<c:when test="${item.rtfState=='XSZS'}">
												<th>终审人员:</th>
											</c:when>
											<c:otherwise>
												<th>初审人员:</th>
											</c:otherwise>
										</c:choose>
										<td>${item.approvalPersonName}</td>
										<th>审批时间:</th>
										<td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
									</tr>
									<tr>
										<th>申请额度:</th>
										<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
										<th>申请期限:</th>
										<td>${item.approvalApplyTerm}</td>
										<th>审批产品:</th>
										<td>${item.approvalProductName}</td>
									</tr>
									<tr>
										<th>核实收入:</th>
										<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0" /></td>
										<c:choose>
											<c:when test="${item.rtfState=='XSZS'}">
												<th>终审审批额度:</th>
											</c:when>
											<c:otherwise>
												<th>初审审批额度:</th>
											</c:otherwise>
										</c:choose>
										<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0" /></td>
										<th>审批期限:</th>
										<td>${item.approvalTerm}</td>
									</tr>
									<tr>
										<th>月还款额:</th>
										<td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0" /></td>
										<th>内部负债率:</th>
										<td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1" />%</td>
										<th>总负债率:</th>
										<td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1" />%</td>
									</tr>
									<tr>
										<th>审批意见:</th>
										<td colspan="5" rowspan="2" class="W90">${item.approvalMemo}<%-- <input class="easyui-textbox W80" data-options="height:'auto',readonly:true,multiline:true" value="${item.approvalMemo}"> --%></td>
									</tr>
								</table>
								<h2>信审初审</h2>
								<table class="table_ui W100">
									<tr>
										<th>初审人员:</th>
										<td>${resEmployeeVO.name}</td>
									</tr>
									<tr>
										<th>申请额度:</th>
										<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
										<th>申请期限:</th>
										<td>${applyInfo.applyInfoVO.applyTerm}</td>
										<th>审批产品:</th>
										<td><input type="text" name="approvalProductCd" value="${applyInfo.applyInfoVO.productCd}" class="input"></td>
									</tr>
									<tr>
										<th class="requiredFontWeight">核实收入:</th>
										<td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
										<th class="requiredFontWeight">初审审批额度:<input id="firstApprovalOpinion_applyMoney_hidden" class="numFormat" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.applyInfoVO.applyLmt}">
										</th>
										<td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
										<th class="requiredFontWeight">审批期限:</th>
										<td><input type="text" name="approvalTerm" class="input"></td>
									</tr>
									<tr>
										<th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
										<td class="approvalMonthPay"></td>
										<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
										<td class="approvalDebtTate"></td>
										<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
										<td class="approvalAllDebtRate"></td>
									</tr>
									<tr>
										<th class="requiredFontWeight">审批意见:</th>
										<td colspan="5" rowspan="2">
											<input ms-attr="{id:'first_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
												   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo" >
											<span class="countSurplusText">可输入200字</span>
										</td>
									</tr>
								</table>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
			</c:forEach>
			<c:if test="${empty approvalInfo.approvalHistoryList}">
				<h2>信审初次审批</h2>
				<table class="table_ui W100">
					<tr>
						<th>初审人员:</th>
						<td>${resEmployeeVO.name}</td>
					</tr>
					<tr>
						<th>申请额度:</th>
						<td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.applyInfoVO.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
						<th>申请期限:</th>
						<td>${applyInfo.applyInfoVO.applyTerm}</td>
						<th>审批产品:</th>
						<td><input type="text" name="approvalProductCd" value="${applyInfo.applyInfoVO.productCd}" class="input"></td>
					</tr>
					<tr>
						<th class="requiredFontWeight">核实收入:</th>
						<td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
						<th class="requiredFontWeight">
							初审审批额度:
							<input id="firstApprovalOpinion_applyMoney_hidden" type="hidden" class="numFormat" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.applyInfoVO.applyLmt}">
						</th>
						<td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
						<th class="requiredFontWeight">审批期限:</th>
						<td><input type="text" name="approvalTerm" class="input"></td>
					</tr>
					<tr>
						<th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
						<td class="approvalMonthPay numFormat"></td>
						<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
						<td class="approvalDebtTate"></td>
						<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
						<td class="approvalAllDebtRate"></td>
					</tr>
					<tr>
						<th class="requiredFontWeight">审批意见:</th>
						<td colspan="5" rowspan="2">
							<input ms-attr="{id:'first_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
								   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo" >
							<span class="countSurplusText">可输入200字</span>
						</td>
					</tr>
				</table>
			</c:if>
		</form>
	</div>
	<!-- ---------------------------------------End 审批意见--------------------------------------->
	<!-- 征信初判 -->
	<div id="firstApprovalOpinion_contrast_Dialog" class="padding_20 display_none">
		<table class="table_ui W100" id="firstApprovalOpinion_credit_table"></table>
		<table class="table_list W100" id="firstApprovalOpinion_debt_table"></table>
	</div>
	<div class="h20"></div>
	<div class="float_right">
		<a class="easyui-linkbutton_ok05 l-btn l-btn-small" onclick="submit()">确认</a>&nbsp;&nbsp;
		<a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="closeHTMLWindow()">取消</a>&nbsp; &nbsp;
	</div>
</body>
<script src="${ctx}/resources/js/core/coreApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bms/bmsBasicApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstApprovalOpinion.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'page',
        loanNo: '${loanNo}',
        approvalInfo: ${approvalInfoJson == null ? '{}' : approvalInfoJson},
        applyInfoOnlyRead: ${applyInfoOnlyReadJson == null ? '{}' : applyInfoOnlyReadJson},
		applyBasicInfo: ${applyBasicInfoJson == null ? '{}' : applyBasicInfoJson},
        applyInfo: ${applyInfoJson == null ? '{}' : applyInfoJson},
        markReportIdChange: '${markReportIdChange}',
        markIsExecuteEngine: ${markIsExecuteEngine == null ? 'false' : markIsExecuteEngine},
        reportInfo: ${reportInfoJson == null ? '{}' : reportInfoJson},
        loanLimitInfo: ${loanLimitInfoJson == null ? 'null' : loanLimitInfoJson},
        ifCreditRecord: ${ifCreditRecord},
        productList: ${productListJson == null ? '[]' : productListJson},
        topupLoan :${topupLoanJson == null ?'{}':topupLoanJson}
    });
</script>
</html>