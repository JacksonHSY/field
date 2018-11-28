<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-审批意见</title>
<jsp:include page="../../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<div style="height: 116px;">
	<div id="finishApprovalOpinion_approveInfo_div">
		<div class="easyui-panel" title="申请信息">
			<table class="table_ui W100">
				<tr>
					<th>姓名:</th>
					<td>${applyInfo.applyInfoVO.name}</td>
					<th>身份证号码:</th>
					<td>${applyInfo.applyInfoVO.idNo}</td>
				</tr>
				<tr>
					<th>申请产品:</th>
					<td><c:if test="${'证大前前'!=applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
					<th>申请期限:</th>
					<td>${applyBasiceInfo.applyTerm}<input type="hidden" value="${applyInfo.applyInfoVO.loanNo}" id="loanNo"></td>
					<th>申请额度:</th>
					<td><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
				</tr>
			</table>
		</div>
	</div>
</div>
<div class="h20"></div>
<form id="finishApprovalOpinion_approveCheckData_form">
	<input name="version" type="hidden" value="${applyInfo.version}">
	<div class="easyui-panel" title="资料核对">
		<div title="工作信息">
			<h2>工作信息:</h2>
			<table class="table_ui W100">
				<tr>
					<th>可接受的月最高还款:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.personInfoVO.monthMaxRepay}" pattern="0" maxFractionDigits="0" /></td>
					<th>客户工作类型:</th>
					<td>${applyInfo.basicInfoVO.workInfoVO.cusWorkType}</td>
					<th>发薪方式:</th>
					<td>${applyInfo.basicInfoVO.workInfoVO.corpPayWay}</td>
				</tr>
				<tr>
					<th>单位月收入/元:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.monthSalary}" pattern="0" maxFractionDigits="0" /></td>
					<th>其他月收入/元:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.otherIncome}" pattern="0" maxFractionDigits="0" /></td>
					<th>月总收入/元:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}" pattern="0" maxFractionDigits="0" /></td>
				</tr>
				<tr>
					<th>收入证明金额:</th>
					<td><fmt:formatNumber type="number" value="${applyBasiceInfo.amoutIncome}" pattern="0" maxFractionDigits="0" />元</td>
					<th>有信用记录:</th>
					<c:if test="${reportInfo.type=='YES'?1:0=='1'}">
						<td>是</td>
					</c:if>
					<c:if test="${reportInfo.type=='YES'?1:0=='0'}">
						<td>否</td>
					</c:if>
				</tr>
			</table>
			<c:if test="${not empty approvalInfo.approveCheckDataList}">
				<table class="table_ui W100" id="finishApprovalOpinion_record_table">
					<c:forEach items="${approvalInfo.approveCheckDataList}" var="item" varStatus="status">
						<c:if test="${not empty item.personMonthAverage}">
							<tr class="personalRecordTr">
								<th>个人流水<c:if test="${!status.first}">${status.index}</c:if>:</th>
								<td><fmt:formatNumber type="number" value="${item.personalWater1}" pattern="0" maxFractionDigits="0" />元</td>
								<td><fmt:formatNumber type="number" value="${item.personalWater2}" pattern="0" maxFractionDigits="0" />元</td>
								<td><fmt:formatNumber type="number" value="${item.personalWater3}" pattern="0" maxFractionDigits="0" />元</td>
								<th>个人流水月均:</th>
								<td><fmt:formatNumber type="number" value="${item.personMonthAverage}" pattern="0" maxFractionDigits="0" />元</td>
							</tr>
						</c:if>
					</c:forEach>
					<c:if test="${'0.00' != approvalInfo.approveCheckDataList.get(0).personalWaterTotal}">
						<tr>
							<th>个人流水合计:<input type="hidden" name="personalWaterTotal"></th>
							<td class="personalRecordCount"><fmt:formatNumber type="number" value="${approvalInfo.approveCheckDataList.get(0).personalWaterTotal}" pattern="0" maxFractionDigits="0" />元</td>
						</tr>
					</c:if>
					<c:forEach items="${approvalInfo.approveCheckDataList}" var="item" varStatus="status">
						<c:if test="${not empty item.publicMonthAverage}">
							<tr class="publicRecordTr">
								<th>对公流水<c:if test="${!status.first}">${status.index}</c:if>:</th>
								<td><fmt:formatNumber type="number" value="${item.publicWater1}" pattern="0" maxFractionDigits="0" />元</td>
								<td><fmt:formatNumber type="number" value="${item.publicWater2}" pattern="0" maxFractionDigits="0" />元</td>
								<td><fmt:formatNumber type="number" value="${item.publicWater3}" pattern="0" maxFractionDigits="0" />元</td>
								<th>对公流水月均:</th>
								<td><fmt:formatNumber type="number" value="${item.publicMonthAverage}" pattern="0" maxFractionDigits="0" />元</td>
							</tr>
						</c:if>
					</c:forEach>
					<c:if test="${'0.00' != approvalInfo.approveCheckDataList.get(0).publicWaterTotal}">
						<tr>
							<th>对公流水合计:<input type="hidden" name="publicWaterTotal"></th>
							<td class="publicRecordCount"><fmt:formatNumber type="number" value="${approvalInfo.approveCheckDataList.get(0).publicWaterTotal}" pattern="0" maxFractionDigits="0" />元</td>
						</tr>
					</c:if>
					<c:if test="${not empty approvalInfo.approveCheckDataList.get(0).waterIncomeTotal}">
						<tr>
							<th>流水收入合计:<input type="hidden" name="waterIncomeTotal"></th>
							<td class="recordCount"><fmt:formatNumber type="number" value="${approvalInfo.approveCheckDataList.get(0).waterIncomeTotal}" pattern="0" maxFractionDigits="0" />元</td>
						</tr>
					</c:if>
				</table>
			</c:if>
		</div>
		<div class="easyui-panel" title="其他核实信息">
			<c:if test="${not empty approvalInfo.approveCheckDataList.get(0)}">
				<table class="table_ui W100">
					<tr>
						<th>法院网核查情况：</th>
						<td>${courtCheck}</td>
						<th>内部匹配情况：</th>
						<td>${insideMatch}</td>
					</tr>
					<tr>
						<th>近三个月查询:</th>
						<td>${approvalInfo.approveCheckDataList.get(0).threeMonthsCount}次</td>
						<th>近一个月查询:</th>
						<td>${approvalInfo.approveCheckDataList.get(0).oneMonthsCount}次</td>
					</tr>
					<tr>
						<th>备注:</th>
						<td class="W80">${approvalInfo.approveCheckDataList.get(0).memo}</td>
					</tr>
				</table>
			</c:if>
		</div>
	</div>
	<hr>
	<div class="easyui-panel">
		<h3>
			负债信息 <a class="easyui-linkbutton c6" onclick="finishApprovalOpinionContrastDialog()">征信初判</a>
		</h3>
		<table class="table_list W100" id="finishApprovalOpinion_liabilities_table">
			<!-- Start解读央行报告 -->
			<tr>
				<td>信用卡总额度:</td>
				<c:if test="${not empty approvalInfo}">
					<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditTotalLimit}" pattern="0" maxFractionDigits="0" /></td>
				</c:if>
				<c:if test="${empty approvalInfo}">
					<td></td>
				</c:if>
				<td>信用卡已用额度:</td>
				<c:if test="${not empty approvalInfo}">
					<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditUsedLimit}" pattern="0" maxFractionDigits="0" /></td>
				</c:if>
				<c:if test="${empty approvalInfo}">
					<td></td>
				</c:if>
				<td>信用卡负债:</td>
				<c:if test="${not empty approvalInfo}">
					<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditDebt}" pattern="0" maxFractionDigits="0" /></td>
				</c:if>
				<c:if test="${empty approvalInfo}">
					<td></td>
				</c:if>
			</tr>
			<tr>
				<td>信用贷款</td>
				<td>额度/元</td>
				<td>期限/月</td>
				<td>负债/元</td>
				<td>已还款期数</td>
				<td>对应关系</td>
			</tr>
			<c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="status">
				<c:if test="${not empty item.creditLoanDebt }">
					<c:if test="${item.debtType =='CREDITLOAN'}">
						<tr class="creditCardTr">
							<td>信用贷款<c:if test="${!status.first}">${status.index}</c:if><input type="hidden" name="debtType" value="CREDITLOAN"></td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanLimit}" pattern="0" maxFractionDigits="0" /></td>
							<td>${item.creditLoanTerm}</td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanDebt}" pattern="0" maxFractionDigits="0" /></td>
							<td>${item.creditLoanAlsoPay}</td>
							<td>${item.creditNo}</td>
						</tr>
					</c:if>
					<c:if test="${item.debtType =='TOPUPLOAN'}">
						<tr class="topUpTr">
							<td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanLimit}" pattern="0" maxFractionDigits="0" /></td>
							<td>${item.creditLoanTerm}</td>
							<td><fmt:formatNumber type="number" value="${item.creditLoanDebt}" pattern="0" maxFractionDigits="0" /></td>
							<td><input type="hidden" class="input" name="creditLoanAlsoPay" value="${item.creditLoanAlsoPay}">${item.creditLoanAlsoPay}</td>
							<td>${item.creditNo}</td>
						</tr>
					</c:if>
				</c:if>
			</c:forEach>
			<tr>
				<td>外部负债总额/元<input name="outDebtTotal" type="hidden" value="${approvalInfo.debtsInfoList[0].outDebtTotal}"></td>
				<td><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outDebtTotal}" pattern="0" maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="6" class="W80">${approvalInfo.debtsInfoList[0].memo}</td>
			</tr>
		</table>
	</div>
</form>
<hr>
<!-- ---------start-------------- -->
<div class="easyui-panel" title="产品信息">
	<!-- 保单信息 -->
	<c:if test="${applyInfo.assetsInfoVO.policyInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>保险金额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.insuranceAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>保险年限:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm== 999 ? "终身": applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm.toString().concat(' 年')}</td>
				<td>已缴年限:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.paidTerm}</td>
			</tr>
			<tr>
				<td>最近一次缴纳时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.policyInfoVO.lastPaymentDate}" pattern="yyyy-MM-dd" type="date" /></td>
				<td>缴费方式:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.paymentMethod}</td>
				<td>与被保险人关系:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.policyRelation}</td>
			</tr>
			<tr>
				<td>年缴金额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>保单真伪核实方式:</td>
				<td>${applyInfo.assetsInfoVO.policyInfoVO.policyCheck}</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</c:if>
	<!-- 车辆信息 -->
	<c:if test="${applyInfo.assetsInfoVO.carInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>车辆类型:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.carType}</td>
				<td>本地车牌:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.localPlate}</td>
				<td>购买时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.carInfoVO.carBuyDate}" pattern="yyyy-MM" type="date" /></td>
			</tr>
			<tr>
				<td>是否有车贷:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.carLoan}</td>
				<td>月供:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>裸车价:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.nakedCarPrice}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>购买价:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.carBuyPrice}" pattern="0" maxFractionDigits="0" /></td>
				<td>贷款剩余期数:</td>
				<td>${applyInfo.assetsInfoVO.carInfoVO.carLoanTerm}</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</c:if>
	<!-- 公积金信息 -->
	<c:if test="${applyInfo.assetsInfoVO.providentInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>开户时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.providentInfoVO.openAccountDate}" pattern="yyyy-MM" type="date" /></td>
				<td>续存比例:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.depositRate}</td>
				<td>月缴存额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>缴存基数:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.depositBase}" pattern="0" maxFractionDigits="0" /></td>
				<td>公积金材料:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.providentInfo}</td>
				<td>缴纳单位同申请单位:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentUnit}</td>
			</tr>
			<tr>
				<td>申请单位已缴月数:</td>
				<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum}</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</c:if>
	<!-- 卡友贷信息 -->
	<c:if test="${applyInfo.assetsInfoVO.cardLoanInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>发卡时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate}" pattern="yyyy-MM" type="date" /></td>
				<td>额度:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>近4个月账单金额依次为:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4}" pattern="0" maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>月均:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</c:if>
	<!-- 房产信息 -->
	<c:if test="${applyInfo.assetsInfoVO.estateInfoVO.ifEmpty == '1'}">		
		<table class="table_list W100">
			<tr>
				<td>房产类型:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateType}</td>
				<td>单据户名为本人:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.ifMe}</td>
				<td>建筑面积:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.builtupArea}</td>
			</tr>
			<tr>
				<td>房产所在省:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateStateId}</td>
				<td>房产所在市:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateCityId}</td>
				<td>房产所在区:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateZoneId}</td>
			</tr>
			<tr>
				<td>房产地址:</td>
				<td colspan="3">${applyInfo.assetsInfoVO.estateInfoVO.estateAddress}</td>
				<td>房贷情况:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.estateLoan}</td>
			</tr>
			<tr>
				<td>购买时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.estateInfoVO.estateBuyDate}" pattern="yyyy-MM" type="date" /></td>
				<td>购买总价值:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.estateAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>市值参考价:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.referenceAmt}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>房贷金额:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.estateLoanAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>月供:</td>
				<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt}" pattern="0" maxFractionDigits="0" /></td>
				<td>已还期数:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.hasRepaymentNum}</td>
			</tr>
			<tr>
				<td>房产所有权:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.houseOwnership}</td>
				<td>产权比例:</td>
				<td>${applyInfo.assetsInfoVO.estateInfoVO.equityRate}</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</c:if>
	<!-- 随薪贷信息	 -->
	<c:if test="${applyInfo.assetsInfoVO.salaryLoanInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>条件类型:</td>
				<td>${applyInfo.assetsInfoVO.salaryLoanInfoVO.conditionType}</td>
				<td class="W50">&nbsp;</td>
				<td>&nbsp; &nbsp;</td>
			</tr>
		</table>
	</c:if>
	<!-- 网购达人贷信息 -->
	<c:if test="${applyInfo.assetsInfoVO.masterLoanInfoVO.ifEmpty == '1'}">
			<table class="table_list W100">
				<tr>
					<td>京东用户等级:</td>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel}</td>
					<td>小白信用分:</td>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue}" maxFractionDigits="1" /></td>
					<td>近一年实际消费金额:</td>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount}" pattern="0" maxFractionDigits="0" /></td>
				</tr>
			</table>
			<table class="table_list W100">
				<tr>
					<td>账户注册时间:</td>
					<td><fmt:formatDate value="${applyInfo.assetsInfoVO.masterLoanInfoVO.acctRegisterDate}" pattern="yyyy-MM-dd" type="date" /></td>
					<td>买家信用等级:</td>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel}</td>
					<td>买家信用类型:</td>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType}</td>
				</tr>
				<tr>
					<td>好评率:</td>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.goodRate}</td>
					<td>上一年度支出额:</td>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt}" pattern="0" maxFractionDigits="0" /></td>
					<td>芝麻信用分:</td>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue}</td>
				</tr>
				<tr>
					<td>近三个月支出总额:</td>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt1}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt2}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt3}" pattern="0" maxFractionDigits="0" /></td>
					<td>月均:</td>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.payMonthAmt}" pattern="0" maxFractionDigits="0" /></td>
					<td></td>
					<td></td>
				</tr>
			</table>
	</c:if>
	<!-- 淘宝商户贷信息 -->
	<c:if test="${applyInfo.assetsInfoVO.merchantLoanInfoVO.ifEmpty == '1'}">
		<table class="table_list W100">
			<tr>
				<td>开店时间:</td>
				<td><fmt:formatDate value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate}" pattern="yyyy-MM" type="date" /></td>
				<td>卖家信息等级:</td>
				<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}</td>
				<td>卖家信息类型:</td>
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
				<td colspan="5"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5}" pattern="0" maxFractionDigits="0" />&nbsp;|&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
			<tr>
				<td>月均:</td>
				<td colspan="5"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}" pattern="0" maxFractionDigits="0" /></td>
			</tr>
		</table>
	</c:if>
</div>
<!-- ----------------------- -->
<hr>
<div class="easyui-panel">
	<h2>
		系统初判 <a class="easyui-linkbutton c6">系统初判</a>
	</h2>
	<table class="table_ui W100">
		<tr>
			<th>系统评分:</th>
			<td>200</td>
			<th>系统判定核实收入:</th>
			<td>1000</td>
			<th>topup客户建议审批额度:</th>
			<td></td>
		</tr>
		<tr>
			<th>最小可审批额度:</th>
			<td></td>
			<th>最大可审批额度:</th>
			<td></td>
			<th>系统建议审批期限:</th>
			<td></td>
		</tr>
	</table>
</div>
<hr>
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
						<c:set var="nameKey" value ='${item.rtfState}${item.approvalPerson}' scope="page"></c:set>
						<td>${approvalMapJson[nameKey]}</td>
<%-- 						<td>${item.approvalPerson}</td> --%>
						<th>审批时间:</th>
						<td colspan="2"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd" type="date" /></td>
					</tr>
					<tr>
						<th>申请额度:</th>
						<td><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
			            <th>申请期限:</th>
			            <td>${applyBasiceInfo.applyTerm}</td>
			            <th>审批产品:</th>
						<td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
					</tr>
					<tr>
						<th>核实收入:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0" /></td>
						<th>审批额度:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0" /></td>
						<th>审批期限:</th>
						<td>${item.approvalTerm}</td>
					</tr>
					<tr>
						<th>月还款额:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0" /></td>
						<th>内部负债率:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalDebtTate * 100} " maxFractionDigits="3"/>%</td>
						<th>总负债率:</th>
						<td><fmt:formatNumber type="number" value="${item.approvalAllDebtRate * 100} " maxFractionDigits="3"/>%</td>
					</tr>
					<tr>
						<th>备注:</th>
						<td colspan="4" class="W80">${item.approvalMemo}</td>
					</tr>
				</table>
				<hr>
		</c:forEach>
	</form>
</div>
<!-- 征信初判 -->
<div id="finishApprovalOpinion_contrast_Dialog" class="padding_20 display_none">
	<table class="table_ui W100" id="finishApprovalOpinion_credit_table"></table>
	<table class="table_list W100" id="finishApprovalOpinion_debt_table"></table>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApprovalOpinion.js"></script>
</html>