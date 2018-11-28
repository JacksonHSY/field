<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-审批意见</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20 ms-controller" ms-controller="page">
	<div style="height: 120px;">
		<div id="finishApprovalOpinion_approveInfo_div" style="margin-top: 0px; width: 100%; border-bottom: 2px solid #95b8e7;">
			<div class="easyui-panel" title="申请信息">
				<table class="table_ui W100">
					<tr>
						<th>姓名:</th>
						<td>{{ applyBasicInfo.name }}</td>
						<th>身份证号码:</th>
						<td>{{ applyBasicInfo.idNo }}</td>
					</tr>
					<tr>
						<th width="16%">申请产品:</th>
						<td width="16%">{{ applyBasicInfo.initProductName }}</td>
						<th width="16%">申请期限:</th>
						<td width="16%">{{ applyBasicInfo.applyTerm }}<input ms-duplex="loanNo" id="loanNo" type="hidden"></td>
						<th width="16%">申请额度:</th>
						<td class="numFormat">{{ numberRound(applyBasicInfo.applyLmt) }}</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div class="h20"></div>
	<form id="finishApprovalOpinion_approveCheckData_form">
		<input name="version" type="hidden" ms-duplex="applyInfo.version">
		<div class="easyui-panel" title="资料核对">
				<h3>工作信息</h3>
				<table class="table_ui W100">
					<tr>
						<th width="16%">可接受的月最高还款:</th>
						<td width="16%" class="numFormat">{{ applyInfo.basicInfoVO.personInfoVO.monthMaxRepay || '' }}元</td>
						<th width="16%">客户工作类型:</th>
						<td width="16%">{{ applyInfo.basicInfoVO.workInfoVO.cusWorkType }}</td>
						<th width="16%">发薪方式:</th>
						<td>{{ applyInfo.basicInfoVO.workInfoVO.corpPayWay }}</td>
					</tr>
					<tr>
						<th>单位月收:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.monthSalary}"  maxFractionDigits="0" />元</td>
						<th>其他月收入:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.otherIncome}"  maxFractionDigits="0" />元</td>
						<th>月总收入:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}"  maxFractionDigits="0" />元</td>
					</tr>
					<tr>
						<th>收入证明金额:</th>
						<td><fmt:formatNumber type="number" value="${applyBasiceInfo.amoutIncome}"  maxFractionDigits="0" />元</td>
						<th>有无周末发薪:</th>
						<td><c:if test="${0 == approvalInfo.approveCheckInfo.weekendPay }">有</c:if>
							<c:if test="${1 == approvalInfo.approveCheckInfo.weekendPay }">无</c:if>
						</td>
					</tr>
				</table>
				<!-- 个人、对公流水 start -->
				<table class="table_ui W100" id="finishApprovalOpinion_record_table">
					<tr class="personalRecordTr" ms-if="personalCheckStatementArray == null || personalCheckStatementArray.length == 0">
						<th width="16%">个人流水:</th>
						<td width="16%"></td>
						<td width="16%"></td>
						<td width="16%"></td>
						<th width="16%">个人流水月均:</th>
						<td></td>
					</tr>
					<tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
						<th width="16%">个人流水{{ $index != 0 ? $index : '' }}:</th>
						<td width="16%" class="numFormat">{{ isNullOrUndefined(checkStatement.water1) ? '' : checkStatement.water1}}元</td>
						<td width="16%" class="numFormat">{{ isNullOrUndefined(checkStatement.water2) ? '' : checkStatement.water2}}元</td>
						<td width="16%" class="numFormat">{{ isNullOrUndefined(checkStatement.water3) ? '' : checkStatement.water3}}元</td>
						<th width="16%">个人流水月均:</th>
						<td class="numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) + '元'}}</td>
					</tr>
					<tr>
						<th>个人流水月均合计:<input type="hidden" name="personalWaterTotal"></th>
						<td class="personalRecordCount numFormat">{{ personalWaterAverageTotal }}元</td>
					</tr>
					<tr class="publicRecordTr" ms-if="publicCheckStatementArray == null || publicCheckStatementArray.length == 0">
						<th>对公流水:</th>
						<td></td>
						<td></td>
						<td></td>
						<th>对公流水月均:</th>
						<td></td>
					</tr>
					<tr class="publicRecordTr" ms-for="($index, checkStatement) in publicCheckStatementArray">
						<th>对公流水{{  $index != 0 ? $index : '' }}:</th>
						<td class="numFormat">{{ isNullOrUndefined(checkStatement.water1) ? '' : checkStatement.water1}}元</td>
						<td class="numFormat">{{ isNullOrUndefined(checkStatement.water2) ? '' : checkStatement.water2}}元</td>
						<td class="numFormat">{{ isNullOrUndefined(checkStatement.water3) ? '' : checkStatement.water3}}元</td>
						<th>对公流水月均:</th>
						<td class="numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) + '元' }}</td>
					</tr>
					<tr>
						<th>对公流水月均合计:<input type="hidden" name="publicWaterTotal"></th>
						<td class="publicRecordCount numFormat">{{ publicWaterAverageTotal }}元</td>
					</tr>
					<tr>
						<th>月均流水合计:<input type="hidden" name="waterIncomeTotal"></th>
						<td class="recordCount numFormat">{{ waterIncomeTotal }}元</td>
					</tr>
				</table>
				<!-- 个人、对公流水 end-->
				<h3>其他核实信息</h3>
				<table class="table_ui W100" ms-if="approvalInfo.approveCheckInfo != null">
					<tr>
						<th width="12%">法院网核查无异常:</th>
						<td width="20%"><input disabled ="true" type="checkbox" ms-duplex-checked="approvalInfo.approveCheckInfo.courtCheckException == 0"/></td>
						<th width="12%">内部匹配无异常:</th>
						<td><input disabled ="true" type="checkbox" ms-duplex-checked="approvalInfo.approveCheckInfo.internalCheckException == 0" /></td>
						<th class="requiredFontWeight">征信平台已验证:</th>
						<td><input disabled ="true" type="checkbox" ms-duplex-checked="approvalInfo.approveCheckInfo.creditCheckException == 0"></td>
					</tr>
					<tr>
						<th>有信用记录:</th>
						<td>{{ applyBasicInfo.ifCreditRecord == '1' ? '有' : '无' }}</td>
						<th>人行近1个月查询:</th>
						<td>{{ approvalInfo.approveCheckInfo.oneMonthsCount }}次</td>
						<th>人行近3个月查询:</th>
						<td>{{ approvalInfo.approveCheckInfo.threeMonthsCount }}次</td>
					</tr>
					<tr>
						<th>备注:</th>
						<td colspan="5" class="W80">{{ approvalInfo.approveCheckInfo.memo }}</td>
					</tr>
				</table>
		</div>
		<hr>
		<div class="easyui-panel" title="负债信息">
		 <a class="easyui-linkbutton" ms-click="finishApprovalOpinionContrastDialog">征信初判</a>
			<table class="table_list W100" id="finishApprovalOpinion_liabilities_table">
				<!-- Start解读央行报告 -->
				<tr>
					<td class="specialThCol">信用卡总额度:</td>
					<c:if test="${not empty approvalInfo}">
						<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditTotalLimit}"  maxFractionDigits="0" />元</td>
					</c:if>
					<c:if test="${empty approvalInfo}">
						<td></td>
					</c:if>
					<td class="specialThCol">信用卡已用额度:</td>
					<c:if test="${not empty approvalInfo}">
						<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditUsedLimit}"  maxFractionDigits="0" />元</td>
					</c:if>
					<c:if test="${empty approvalInfo}">
						<td></td>
					</c:if>
					<td class="specialThCol">信用卡负债:</td>
					<c:if test="${not empty approvalInfo}">
						<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditDebt}"  maxFractionDigits="0" />元</td>
					</c:if>
					<c:if test="${empty approvalInfo}">
						<td></td>
					</c:if>
				</tr>
				<tr class="specialTrCol">
					<td class="specialThCol" width="12%">信用贷款</td>
					<td class="specialThCol" width="20%">额度/元</td>
					<td class="specialThCol" width="12%">期限/月</td>
					<td class="specialThCol" width="20%">负债/元</td>
					<td class="specialThCol" colspan="2">对应关系</td>
				</tr>
				<c:set value="0" var="creditLoanLength" />
				<c:set value="0" var="houseLoanLength" />
				<c:set value="0" var="carLoanLength" />
				<c:set value="0" var="otherLoanLength" />
				<tr ms-for="($index, debtInfo) in getDebtInfoList('TOPUPLOAN')" ms-attr="{class: debtInfo.className}">
					<td>TOPUP贷款</td>
					<td class="numFormat">{{ debtInfo.creditLoanLimit }}</td>
					<td>{{ debtInfo.creditLoanTerm }}</td>
					<td class="numFormat">{{ debtInfo.creditLoanDebt }}</td>
					<td colspan="2">{{ debtInfo.creditNo }}</td>
				</tr>
				<tr ms-for="($index, debtInfo) in getDebtInfoList('CREDITLOAN')" ms-attr="{class: debtInfo.className}">
					<td>信用贷款{{ $index + 1 }}</td>
					<td class="numFormat">{{ debtInfo.creditLoanLimit }}</td>
					<td>{{ debtInfo.creditLoanTerm }}</td>
					<td class="numFormat">{{ debtInfo.creditLoanDebt }}</td>
					<td colspan="2">{{ debtInfo.creditNo }}</td>
				</tr>
				<tr ms-if="getDebtInfoList('TOPUPLOAN').length == 0 && getDebtInfoList('CREDITLOAN').length == 0">
					<td colspan="5">未找到记录</td>
				</tr>
				<tr>
					<td class="specialThCol">外部信用负债总额/元<input type="hidden" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}" name="outCreditDebtTotal"></td>
					<td colspan="5"><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}"  maxFractionDigits="0" /></td>
				</tr>
				<tr class="specialTrCol">
					<td class="specialThCol">房贷</td>
					<td class="specialThCol">额度/元</td>
					<td class="specialThCol">期限/月</td>
					<td class="specialThCol">负债/元</td>
					<td class="specialThCol" colspan="2">对应关系</td>
				</tr>
				<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
					<c:if test="${item.debtType =='HOUSELOAN'}">
						<c:set value="${houseLoanLength+1}" var="houseLoanLength" />
						<tr class="creditCardTr">
							<td>房贷<span>${houseLoanLength}</span></td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
							<td colspan="2">${item.creditNo}</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:if test="${houseLoanLength ==0}">
					<tr>
						<td colspan="5">未找到记录</td>
					</tr>
				</c:if>
				<tr class="specialTrCol">
					<td class="specialThCol">车贷</td>
					<td class="specialThCol">额度/元</td>
					<td class="specialThCol">期限/月</td>
					<td class="specialThCol">负债/元</td>
					<td class="specialThCol" colspan="2">对应关系</td>
				</tr>
				<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
						<c:if test="${item.debtType =='CARLOAN'}">
							<c:set value="${carLoanLength+1}" var="carLoanLength" />
							<tr class="creditCardTr">
								<td>车贷<span>${carLoanLength}</span></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
								<td colspan="2">${item.creditNo}</td>
							</tr>
						</c:if>
				</c:forEach>
				<c:if test="${carLoanLength ==0}">
					<tr>
						<td colspan="5">未找到记录</td>
					</tr>
				</c:if>
				<tr class="specialTrCol">
					<td class="specialThCol">其他贷款</td>
					<td class="specialThCol">额度/元</td>
					<td class="specialThCol">期限/月</td>
					<td class="specialThCol">负债/元</td>
					<td class="specialThCol" colspan="2">对应关系</td>
				</tr>
				<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
						<c:if test="${item.debtType =='OTHERLOAN'}">
							<c:set value="${otherLoanLength +1}" var="otherLoanLength" />
							<tr class="creditCardTr">
								<td>其他贷款<span>${otherLoanLength}</span></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
								<td colspan="2">${item.creditNo}</td>
							</tr>
						</c:if>
				</c:forEach>
				<c:if test="${otherLoanLength ==0}">
					<tr>
						<td colspan="5">未找到记录</td>
					</tr>
				</c:if>
				<tr>
					<td class="specialThCol">外部负债总额/元<input name="outDebtTotal" type="hidden" value="${approvalInfo.debtsInfoList[0].outDebtTotal}"></td>
					<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outDebtTotal}"  maxFractionDigits="0" /></td>
					<td class="specialThCol">速贷还款情况</td>
					<td colspan="2">
						<c:if test="${'NORMAL' == approvalInfo.debtsInfoList[0].fastLoanSituation}">正常</c:if>
						<c:if test="${'OVERDUE' == approvalInfo.debtsInfoList[0].fastLoanSituation}">逾期</c:if>
						<c:if test="${'SETTLE' == approvalInfo.debtsInfoList[0].fastLoanSituation}">结清</c:if>
						<c:if test="${'NOLOAN' == approvalInfo.debtsInfoList[0].fastLoanSituation}">无贷款</c:if>
					</td>
				</tr>
				<tr>
					<td class="specialThCol">备注</td>
					<td colspan="5" class="W80">${approvalInfo.debtsInfoList[0].memo}</td>
				</tr>
			</table>
		</div>
	</form>
	<hr>
	<!-- ---------start-------------- -->
	<div class="easyui-panel" id="finish_approval_assetsInfo_div" title="产品信息" data-name="${applyInfo.applyInfoVO.productCd}">
		<!-- 保单信息 -->
		<table class="table_list W100 display_noH" id="POLICY">
			<tr>
				<td>保险金额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.insuranceAmt}"  maxFractionDigits="0" /></td>
				<td>保险年限:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm== 999 ? "终身": applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm.toString().concat(' 年')}</td>
				<td>已缴年限:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.paidTerm}</td>
			</tr>
			<tr>
				<td>年缴金额/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt}"  maxFractionDigits="0" /></td>
				<td>信审保单已核实:</td>
				<td><c:if test="${'Y'== applyInfo.assetsInfoVO.policyInfoVO.policyCheckIsVerify}">是</c:if> <c:if test="${ 'N'==applyInfo.assetsInfoVO.policyInfoVO.policyCheckIsVerify}">否</c:if></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<!-- 车辆信息 -->
		<table class="table_list W100 display_noH" id="CAR">
			<tr>
				<td>购买价/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.carBuyPrice}"  maxFractionDigits="0" /></td>
				<td>购买时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.carInfoVO.carBuyDate}" pattern="yyyy-MM" type="date" /></td>
				<td>车牌号:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.plateNum}</td>
			</tr>
			<tr>
				<td>是否有车贷:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.carLoan}</td>
				<td>贷款发放年月:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.carInfoVO.carLoanIssueDate}" pattern="yyyy-MM" type="date" /></td>
				<td>月供/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}"  maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>信审保单已核实:</td>
				<td><c:if test="${'Y'== applyInfo.assetsInfoVO.carInfoVO.carCheckIsVerify}">是</c:if> <c:if test="${ 'N'==applyInfo.assetsInfoVO.carInfoVO.carCheckIsVerify}">否</c:if></td>
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
				<td>${applyInfo.assetsInfoVO.providentInfoVO.depositRate}</td>
				<td>月缴存额/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt}"  maxFractionDigits="0" /></td>
				<td>缴存基数/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.depositBase}"  maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>公积金材料:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.providentInfo}</td>
				<td>缴纳单位同申请单位:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentUnit}</td>
				<td>申请单位已缴月数:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum}</td>
			</tr>
		</table>
		<!-- 卡友贷信息 -->
		<table class="table_list W100 display_noH" id="CARDLOAN">
			<tr>
				<td>发卡时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate}" pattern="yyyy-MM" type="date" /></td>
				<td>额度/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit}"  maxFractionDigits="0" /></td>
				<td></td>
			</tr>
			<tr>
				<td>近4个月账单金额依次为:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4}"  maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>月均/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}"  maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<!-- 房产信息 -->
		<table class="table_list W100 display_noH" id="ESTATE">
			<tr>
				<td>房产类型:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateType}</td>
				<td>购买时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.estateInfoVO.estateBuyDate}" pattern="yyyy-MM" type="date" /></td>
				<td>市值参考价/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.referenceAmt}"  maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>房产地址同住宅地址:</td>
				<td colspan="5"><c:if test="${'Y'==applyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered}">是</c:if> <c:if test="${'Y'!=applyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered}">否</c:if></td>
			</tr>
			<tr>
				<td>房产地址:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateState}</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateCity}</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateZone}</td>
				<td colspan="2">${applyInfo.assetsInfoVO.estateInfoVO.estateAddress}</td>
			</tr>
			<tr>
				<td>房贷情况:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateLoan}</td>
				<td>房贷发放年月:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.estateInfoVO.estateLoanIssueDate}" pattern="yyyy-MM" type="date" /></td>
				<td>月供/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt}"  maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>产权比例%:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.equityRate}</td>
				<td>单据户名为本人:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.ifMe}</td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<!-- 网购达人B贷信息 -->
		<table class="table_list W100 display_noH" id="MASTERLOAN_B">
			<tr>
				<td>京东用户等级:</td>
				<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel}</td>
				<td>小白信用分:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue}" maxFractionDigits="1" /></td>
				<td>近一年实际消费金额/元:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount}"  maxFractionDigits="0" /></td>
			</tr>
		</table>
		<!-- 网购达人A贷信息 -->
		<table class="table_list W100 display_noH" id="MASTERLOAN_A">
			<tr>
				<td>买家信用等级:</td>
				<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel}</td>
				<td>买家信用类型:</td>
				<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType}</td>
				<td>芝麻信用分:</td>
				<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue}</td>
			</tr>
			<tr>
				<td>近12个月支出额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt}"  maxFractionDigits="0" /></td>
				<td>淘气值:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoAVO.naughtyValue}" maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>3个月内收货地址</td>
				<td><c:if test="${'00001'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin3MonthsAddress}">同住址</c:if> <c:if test="${'00002'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin3MonthsAddress}">同司址</c:if> <c:if test="${'00003'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin3MonthsAddress}">其他</c:if></td>
				<td>6个月外收货地址</td>
				<td><c:if test="${'00001'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin6MonthsAddress}">同住址</c:if> <c:if test="${'00002'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin6MonthsAddress}">同司址</c:if> <c:if test="${'00003'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin6MonthsAddress}">其他</c:if></td>
				<td><c:if test="${isMasterLoanADate}">12个月外收货地址</c:if></td>
				<td><c:if test="${isMasterLoanADate}"><c:if test="${'00001'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin12MonthsAddress}">同住址</c:if> <c:if test="${'00002'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin12MonthsAddress}">同司址</c:if> <c:if test="${'00003'==applyInfo.assetsInfoVO.masterLoanInfoVO.onlineAWithin12MonthsAddress}">其他</c:if></c:if></td>
			</tr>
		</table>
		<!-- 淘宝商户贷信息 -->
		<table class="table_list W100 display_noH" id="MERCHANTLOAN">
			<tr>
				<td>开店时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate}" pattern="yyyy-MM" type="date" /></td>
				<td>卖家信用等级:</td>
				<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}</td>
				<td>卖家信用类型:</td>
				<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType}</td>
			</tr>
			<tr>
				<td>近半年好评数:</td>
				<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum}</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>近6个月账单金额依次为:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3}"  maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5}"  maxFractionDigits="0" /></td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}"  maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>月均:</td>
				<td colspan="5"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}"  maxFractionDigits="0" /></td>
			</tr>
		</table>
	</div>
	<!-- ----------------------- -->
	<hr>
	<div class="easyui-panel">
		<h2>
			系统初判
			<%-- <a class="easyui-linkbutton" onclick="finalSystemDetermine('${applyBasiceInfo.loanNo}')">系统初判</a> --%>
		</h2>
		<table id="final_systemDetermine_table" class="table_ui W100">
			<tr>
				<th>建议核实收入:</th>
				<td><fmt:formatNumber type="number" value="${auditRulesVO.adviceVerifyIncome}" maxFractionDigits="0"/></td>
				<th>建议到手金额:</th>
				<td><fmt:formatNumber type="number" value="${auditRulesVO.adviceAuditLines}" maxFractionDigits="0"/></td>
				<th>预估评级费:</th>
				<td><fmt:formatNumber type="number" value="${auditRulesVO.estimatedCost}" maxFractionDigits="2"/></td>
			</tr>
		</table>
	</div>
	<hr>
	<c:if test="${not empty approvalInfo.approvalHistoryList}">
		<div class="easyui-panel" title="审批意见">
			<form id="finishApprovalOpinion_approvalInfo_from">
				<c:forEach items="${approvalInfo.approvalHistoryList}" var="item" varStatus="stat">
				<h2>第${stat.index+1}次审批</h2>
				<table class="table_ui W100">
					<tr>
						<c:if test="${item.rtfState=='XSCS'}">
							<th>初审人员:</th>
						</c:if>
						<c:if test="${item.rtfState=='XSZS'}">
							<th>终审人员:</th>
						</c:if>
						<td>${item.approvalPersonName}</td>
						<th>审批时间:</th>
						<td colspan="2"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd" type="date" /></td>
					</tr>
					<tr>
						<th>申请额度:</th>
						<td><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
						<th>申请期限:</th>
						<td>${applyBasiceInfo.applyTerm}</td>
						<th>审批产品:</th>
						<td>${item.approvalProductName}</td>
					</tr>
					<tr>
						<th>核实收入:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalCheckIncome}"  maxFractionDigits="0" /></td>
						<c:if test="${item.rtfState=='XSCS'}">
							<th>初审审批额度:</th>
						</c:if>
						<c:if test="${item.rtfState=='XSZS'}">
							<th>终审审批额度:</th>
						</c:if>
						<td><fmt:formatNumber type="number" value="${item.approvalLimit}"  maxFractionDigits="0" /></td>
						<th>审批期限:</th>
						<td>${item.approvalTerm}</td>
					</tr>
					<tr>
						<th width="16%">月还款额:</th>
						<td width="16%"><fmt:formatNumber type="number" value="${item.approvalMonthPay}"  maxFractionDigits="0" /></td>
						<th width="16%">内部负债率:</th>
						<td width="16%"><fmt:formatNumber type="number" value="${item.approvalDebtTate*100}"  pattern="0.0" maxFractionDigits="1" />%</td>
						<th width="16%">总负债率:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalAllDebtRate*100}"  pattern="0.0" maxFractionDigits="1"/>%</td>
					</tr>
					<tr>
						<th>审批意见:</th>
						<td colspan="4" class="W80">${item.approvalMemo}</td>
					</tr>
				</table>
				<hr>
				</c:forEach>
			</form>
		</div>
	</c:if>
	<!-- 征信初判 -->
	<div id="finishApprovalOpinion_contrast_Dialog" class="padding_20 display_none">
		<table class="table_ui W100" id="finishApprovalOpinion_credit_table"></table>
		<table class="table_list W100" id="finishApprovalOpinion_debt_table"></table>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApprovalOpinion.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'page',
        loanNo: '${loanNo}',
        approvalInfo: ${approvalInfoJson == null ? '{}' : approvalInfoJson},
        applyBasicInfo: ${applyBasicInfoJson == null ? '{}' : applyBasicInfoJson},
        applyInfo: ${applyInfoJson == null ? '{}' : applyInfoJson}
    });
</script>
</html>