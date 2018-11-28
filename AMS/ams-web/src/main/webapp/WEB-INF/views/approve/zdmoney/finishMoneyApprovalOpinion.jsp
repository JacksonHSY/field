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
<body class="padding_20  ms-controller" ms-controller="page">
	<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; position: fixed; left: 130px; top: 7px;">消息提醒</div>
	<div class="xx_dd_yuan" style="left: 113px; position: fixed;" id="ruleEngineHint_number_div">0</div>
	<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="left: 110px; position: fixed;">
		<div class="xx_dd_tit">消息提醒</div>
		<ul></ul>
	</div>
	<div style="height: 120px;">
		<div id="finishApprovalOpinion_approveInfo_div" style="margin-top: 0px; width: 100%; border-bottom: 2px solid #95b8e7;">
			<input id="finishApprovalOpinion_owningBranchId_hidden" type="hidden" placeholder="进件网点" value="${applyBasiceInfo.owningBranchId}">
			<input id="finishApprovalOpinion_markIsExecuteEngine_hidden" type="hidden" placeholder="标识是否调用规则引擎" value="${markIsExecuteEngine}">
			<input id="finishApprovalOpinion_ifPreferentialUser_hidden" type="hidden" value="${applyBasiceInfo.ifPreferentialUser}" placeholder="标记是否是优惠客户:Y是,N否">
			<div class="easyui-panel" title="申请信息">
				<table class="table_ui W100">
					<tr>
						<th>姓名:</th>
						<td>${applyInfo.baseInfo.name}</td>
						<th>身份证号码:</th>
						<td colspan="3">${applyInfo.baseInfo.idNo}</td>
					</tr>
					<tr>
						<th width="12%">申请产品:</th>
						<td width="20%"><c:if test="${'证大前前'!=applyInfo.baseInfo.initProductName}">${applyInfo.baseInfo.initProductName}</c:if></td>
						<th width="12%">申请期限:</th>
						<td width="20%">${applyInfo.baseInfo.applyTerm }</td>
						<th width="12%">申请额度:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}"  maxFractionDigits="0" /></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div class="h20"></div>
	<form id="finishApprovalOpinion_approveCheckData_form">
		<input name="version" type="hidden" value="${applyInfo.baseInfo.version}">
		<div class="easyui-panel" title="资料核对">
				<h3>工作信息</h3>
				<table class="table_ui W100">
					<tr>
						<th width="12%">可接受的月最高还款:</th>
						<td width="20%" ><fmt:formatNumber type="number" value="${applyInfo.applicantInfo.personalInfo.monthMaxRepay}"  maxFractionDigits="0" />元</td>
						<th width="12%">客户工作类型:</th>
						<td width="20%">${applyInfo.applicantInfo.workInfo.cusWorkType}</td>
						<th width="12%">发薪方式:</th>
						<td>${applyInfo.applicantInfo.workInfo.corpPayWay}</td>
					</tr>
					<tr>
						<th>税前月总收入:</th>
						<td class="numFormat"><fmt:formatNumber type="number" value="${ applyInfo.applicantInfo.workInfo.totalMonthSalary}" maxFractionDigits="0" />元</td>
						<th></th>
						<td></td>
						<th></th>
						<td></td>
					</tr>
					<tr>
						<th>收入证明金额:</th>
						<td><fmt:formatNumber type="number" value="${applyBasiceInfo.amoutIncome}" maxFractionDigits="0" />元</td>
						<th>有无周末发薪:</th>
						<td><c:if test="${0 == approvalInfo.approveCheckInfo.weekendPay }">有</c:if>
							<c:if test="${1 == approvalInfo.approveCheckInfo.weekendPay }">无</c:if>
						</td>
					</tr>
				</table>
				<table class="table_ui W100" id="finishApprovalOpinion_record_table">
					<tr class="personalRecordTr" ms-if="personalCheckStatementArray == null || personalCheckStatementArray.length == 0">
						<th width="12%">个人流水:</th>
						<td width="20%"></td>
						<td width="12%"></td>
						<td width="20%"></td>
						<th width="12%">个人流水月均:</th>
						<td></td>
					</tr>
					<tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
						<th width="12%">个人流水{{ $index != 0 ? $index : '' }}:</th>
						<td width="20%" class="numFormat">{{ checkStatement.water1 !== undefined ? checkStatement.water1 : ''}}元</td>
						<td width="12%" class="numFormat">{{ checkStatement.water2 !== undefined ? checkStatement.water2 : ''}}元</td>
						<td width="20%" class="numFormat">{{ checkStatement.water3 !== undefined ? checkStatement.water3 : ''}}元</td>
						<th width="12%">个人流水月均:</th>
						<td class="numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) + '元' }}</td>
					</tr>
					<tr>
						<th>个人流水月均合计:<input type="hidden" name="personalWaterTotal"></th>
						<td colspan="5" class="personalRecordCount numFormat">{{ personalWaterAverageTotal }}元</td>
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
						<td class="numFormat">{{ checkStatement.water1 !== undefined ? checkStatement.water1 : ''}}元</td>
						<td class="numFormat">{{ checkStatement.water2 !== undefined ? checkStatement.water2 : ''}}元</td>
						<td class="numFormat">{{ checkStatement.water3 !== undefined ? checkStatement.water3 : ''}}元</td>
						<th>对公流水月均:</th>
						<td class="numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) + '元' }}</td>
					</tr>
					<tr>
						<th>对公流水月均合计:<input type="hidden" name="publicWaterTotal"></th>
						<td colspan="5" class="publicRecordCount numFormat">{{ publicWaterAverageTotal }}元</td>
					</tr>
					<tr>
						<th>月均流水收入合计:<input type="hidden" name="waterIncomeTotal"></th>
						<td colspan="5" class="recordCount numFormat">{{ waterIncomeTotal }}元</td>
					</tr>
				</table>
				<h3>其他核实信息</h3>
				<table class="table_ui W100" ms-if="approvalInfo.approveCheckInfo.id != null">
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
						<td id="finish_ifCreditRecord">{{ applyBasicInfo.ifCreditRecord == '1' ? '有' : '无' }}</td>
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
		<div class="easyui-panel" title="负债信息">
			 <a class="easyui-linkbutton" ms-click="finishApprovalOpinionContrastDialog">征信初判</a>
			<table class="table_list W100" id="finishApprovalOpinion_liabilities_table">
				<!-- Start解读央行报告 -->
				<tr>
					<td class="specialThCol">信用卡总额度:</td>
					<c:if test="${not empty approvalInfo}">
						<td ><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditTotalLimit}"  maxFractionDigits="0" />元</td>
					</c:if>
					<c:if test="${empty approvalInfo}">
						<td></td>
					</c:if>
					<td class="specialThCol">信用卡已用额度:</td>
					<c:if test="${not empty approvalInfo}">
						<td ><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditUsedLimit}"  maxFractionDigits="0" />元</td>
					</c:if>
					<c:if test="${empty approvalInfo}">
						<td></td>
					</c:if>
					<td class="specialThCol">信用卡负债:</td>
					<c:if test="${not empty approvalInfo}">
						<td ><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditDebt}"  maxFractionDigits="0" />元</td>
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
					<td colspan="5" ><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}"  maxFractionDigits="0" /></td>
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
								<td ><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
								<td ><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
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
								<td ><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
								<td ><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
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
								<td ><fmt:formatNumber type="number" value="${item.creditLoanLimit}"  maxFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" value="${item.creditLoanTerm}"  maxFractionDigits="0" /></td>
								<td ><fmt:formatNumber type="number" value="${item.creditLoanDebt}"  maxFractionDigits="0" /></td>
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
	<!-- ---------start-------------- -->
	<div class="easyui-panel" id="finish_approval_assetsInfo_div" title="产品信息">
		<table id="money_selectAssetOption_table" class="table_ui W90 center_m">
			<tr>
				<c:if test="${ not empty applyInfo.assetsInfo.policyInfo}">
					<td>保单信息 <input type="checkbox" value="POLICY" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('POLICY')" disabled></td>
				</c:if>
				<c:if test="${'Y' ==  applyInfo.assetsInfo.carInfo.unabridged}">
					<td>车辆信息 <input type="checkbox" value="CAR" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('CAR')" disabled></td>
				</c:if>
				<c:if test="${'Y' ==  applyInfo.assetsInfo.cardLoanInfo.unabridged}">
					<td>信用卡信息 <input type="checkbox" value="CARDLOAN" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('CARDLOAN')" disabled></td>
				</c:if>
				<c:if test="${'Y' ==  applyInfo.assetsInfo.estateInfo.unabridged}">
					<td>房产信息 <input type="checkbox" value="ESTATE" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('ESTATE')" disabled></td>
				</c:if>
				<c:if test="${'Y' ==  applyInfo.assetsInfo.masterLoanInfo.unabridged}">
					<td>淘宝账号信息 <input type="checkbox" value="MASTERLOAN" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('MASTERLOAN')" disabled></td>
				</c:if>
				<c:if test="${'Y' ==  applyInfo.assetsInfo.educationInfo.unabridged}">
					<td>学历信息 <input type="checkbox" value="EDUCATION" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('EDUCATION')" disabled></td>
				</c:if>
			</tr>
		</table>
		<hr/>
		<div id="money_customerInfo_assetsInfo_div">
			<!-- 保单信息 -->
			<c:if test="${ not empty applyInfo.assetsInfo.policyInfo}">
				<div id="money_customer_POLICY_div">
					<h1 class="W90 center_m">保单信息</h1>
					<c:set var="sumpolicyInfoYearPaymentAmt" value="0"></c:set>
					<c:forEach var="policyInfo" varStatus="status" items="${ applyInfo.assetsInfo.policyInfo}">
						    <c:set var="sumpolicyInfoYearPaymentAmt" value="${sumpolicyInfoYearPaymentAmt + policyInfo.yearPaymentAmt}"></c:set>
							<table class="table_list W90 center_m">
								<tr>
									<td>投保公司:</td>
									<td colspan="3">${policyInfo.insuranceCompany}</td>
									<td>保险年限:</td>
									<td>${policyInfo.insuranceTerm == 999 ? "终身": policyInfo.insuranceTerm.toString().concat(' 年')}</td>
								</tr>
								<tr>
									<td width="14%">保险金额:</td>
									<td width="18%"><span class="numFormat"><fmt:formatNumber type="number" value="${policyInfo.insuranceAmt}" maxFractionDigits="0" /></span>元</td>
									<td width="14%">年缴金额:</td>
									<td width="18%"><span class="numFormat"><fmt:formatNumber type="number" value="${policyInfo.yearPaymentAmt}" maxFractionDigits="0" /></span>元</td>
									<td width="14%">已缴年限:</td>
									<td>${policyInfo.paidTerm}</td>
								</tr>
								<tr>
									<td>查询账号:</td>
									<td>${policyInfo.policyAccount}</td>
									<td>密码:</td>
									<td>${policyInfo.policyPassword}</td>
									<td>寿险保单已验证:</td>
									<td>
										<c:if test="${'Y' ==policyInfo.policyCheckIsVerify}">是</c:if>
										<c:if test="${'N' ==policyInfo.policyCheckIsVerify}">否</c:if>
									</td>
								</tr>
							</table>
							<hr/>
					</c:forEach>
					<table class="table_list W90 center_m">
						<tr>
							<td width="14%">总年缴金额:</td>
							<td colspan="5"><span class="numFormat"><fmt:formatNumber type="number" value="${sumpolicyInfoYearPaymentAmt}" maxFractionDigits="0" /></span>元</td>
						</tr>
					</table>
					<hr/>
				</div>
			</c:if>
			<!-- 车辆信息 -->
			<c:if test="${'Y' ==  applyInfo.assetsInfo.carInfo.unabridged}">
				<div id="money_customer_CAR_div">
					<h2 class="W90 center_m">车辆信息</h2>
					<form>
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">购车价格:</td>
								<td width="18%"><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.carInfo.carBuyPrice}" maxFractionDigits="0" /></span>元</td>
								<td width="14%">购车时间:</td>
								<td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carBuyDate}" pattern="yyyy-MM" type="date"/></td>
								<td width="14%">车牌号:</td>
								<td>${applyInfo.assetsInfo.carInfo.plateNum}</td>
							</tr>
							<tr>
								<td>车贷情况:</td>
								<td>${applyInfo.assetsInfo.carInfo.carLoanStatus}</td>
								<td>车贷发放年月:</td>
								<td><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
								<td>车贷月还款额:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.carInfo.monthPaymentAmt}" maxFractionDigits="0" /></span>元</td>
							</tr>
							<tr>
								<td>车贷发放机构:</td>
								<td colspan="2">${applyInfo.assetsInfo.carInfo.carLoanOrg}</td>
								<td>交强险承保公司:</td>
								<td colspan="2">${applyInfo.assetsInfo.carInfo.tciCompany}</td>
							</tr>
							<tr>
								<td>车险保单已验证:</td>
								<td colspan="5">
									<c:if test="${'Y' == applyInfo.assetsInfo.carInfo.carCheckIsVerify}">是</c:if>
									<c:if test="${'N' == applyInfo.assetsInfo.carInfo.carCheckIsVerify}">否</c:if>
								</td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<!-- 公积金信息 -->
			<c:if test="${'Y' ==  applyInfo.assetsInfo.fundInfo.unabridged}">
				<div id="money_customer_PROVIDENT_div">
					<h1 class="W90 center_m">公积金信息</h1>
					<form id="money_customerProvident_form" name="PROVIDENT">
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">公积金账号:</td>
								<td width="18%">${applyInfo.assetsInfo.fundInfo.accumulationFundAccount}</td>
								<td width="14%">公积金账号密码:</td>
								<td colspan="3">${applyInfo.assetsInfo.fundInfo.accumulationFundPassword}</td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<!-- 社保信息 -->
			<c:if test="${'Y' ==  applyInfo.assetsInfo.socialInsuranceInfo.unabridged}">
				<div id="money_customer_SOCIAL_INSURANCE_div">
					<h1 class="W90 center_m">社保信息</h1>
					<form id="money_customerSOCIAL_INSURANCE_form" name="SOCIAL_INSURANCE">
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">社保账号:</td>
								<td width="18%">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount}</td>
								<td width="14%">社保账号密码:</td>
								<td colspan="3">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword}</td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<%--信用卡信息--%>
			<c:if test="${'Y' ==  applyInfo.assetsInfo.cardLoanInfo.unabridged}">
				<div id="money_customerCardLoan_div">
					<h1 class="W90 center_m">信用卡信息</h1>
					<form>
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">信用卡额度:</td>
								<td width="18%"><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.cardLoanInfo.creditLimit}" maxFractionDigits="0" /></span>元</td>
								<td width="14%">发卡银行:</td>
								<td width="18%">${applyInfo.assetsInfo.cardLoanInfo.issusingBank}</td>
								<td width="14%">发卡时间:</td>
								<td><fmt:formatDate value="${applyInfo.assetsInfo.cardLoanInfo.issuerDate}" pattern="yyyy-MM" type="date" /></td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<%--房产信息--%>
			<c:if test="${'Y' ==  applyInfo.assetsInfo.estateInfo.unabridged}">
				<div id="money_customerHome_div">
					<h1 class="W90 center_m">房产信息</h1>
					<form>
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">房产类型:</td>
								<td width="18%">${applyInfo.assetsInfo.estateInfo.estateType}</td>
								<td width="14%">购房时间:</td>
								<td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateBuyDate}" pattern="yyyy-MM" type="date"/></td>
								<td width="14%">与他人共有情况:</td>
								<td><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==1}">是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==0}">否</c:if></td>
							</tr>
							<tr>
								<td>房产地址同住宅地址:</td>
								<td colspan="5"><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='Y'}"> 是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='N'}">否</c:if></td>
							</tr>
							<tr>
								<td>房产地址:</td>
								<td colspan="5">${applyInfo.assetsInfo.estateInfo.estateState}&nbsp;${applyInfo.assetsInfo.estateInfo.estateCity}&nbsp;${applyInfo.assetsInfo.estateInfo.estateZone}&nbsp;${applyInfo.assetsInfo.estateInfo.estateAddress}</td>
							</tr>
							<tr>
								<td>房贷情况:</td>
								<td>${applyInfo.assetsInfo.estateInfo.estateLoan}</td>
								<td>房贷总额:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.estateInfo.estateLoanAmt}" maxFractionDigits="0" /></span>元</td>
								<td>房贷月还款额:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.estateInfo.monthPaymentAmt}" maxFractionDigits="0" /></span>元</td>
							</tr>
							<tr>
								<td>房贷发放年月</td>
								<td colspan="5"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<%--淘宝账号贷--%>
			<c:if test="${'Y' ==  applyInfo.assetsInfo.masterLoanInfo.unabridged}">
				<div id="money_customerMerchant_div">
					<h1 class="W90 center_m">淘宝账户信息</h1>
					<form>
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">淘宝会员名:</td>
								<td width="18%">${applyInfo.assetsInfo.masterLoanInfo.memberName}</td>
								<td width="14%">淘宝买家信用等级:</td>
								<td width="18%">${applyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel}</td>
								<td width="14%">淘宝买家信用类型:</td>
								<td>${applyInfo.assetsInfo.masterLoanInfo.buyerCreditType}</td>
							</tr>
							<tr>
								<td>淘宝年消费额:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt}" maxFractionDigits="0" /></span>元</td>
								<td>淘气值:</td>
								<td>${applyInfo.assetsInfo.masterLoanInfo.naughtyValue}</td>
								<td>芝麻信用分:</td>
								<td>${applyInfo.assetsInfo.masterLoanInfo.sesameCreditValue}</td>
							</tr>
							<tr>
								<td>花呗额度:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.masterLoanInfo.costLimit}" maxFractionDigits="0" /></span>元</td>
								<td>借呗额度:</td>
								<td><span class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.assetsInfo.masterLoanInfo.borrowLimit}" maxFractionDigits="0" /></span>元</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td>3个月内收货地址:</td>
								<td>
									<c:if test="${'00001'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin3MonthsAddress}">同住址</c:if>
									<c:if test="${'00002'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin3MonthsAddress}">同司址</c:if>
									<c:if test="${'00003'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin3MonthsAddress}">其他</c:if></td>
								<td>6个月外收货地址:</td>
								<td>
									<c:if test="${'00001'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin6MonthsAddress}">同住址</c:if>
									<c:if test="${'00002'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin6MonthsAddress}">同司址</c:if>
									<c:if test="${'00003'==applyInfo.assetsInfo.masterLoanInfo.onlineAWithin6MonthsAddress}">其他</c:if></td>
								<td></td>
								<td></td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
			<%--学历信息--%>
			<c:if test="${'Y' ==  applyInfo.assetsInfo.educationInfo.unabridged}">
				<div id="money_customer_EDUCATION_div">
					<h1 class="W90 center_m">学历信息</h1>
					<form>
						<table class="table_list W90 center_m">
							<tr>
								<td width="14%">教育经历:</td>
								<td width="18%">${applyInfo.assetsInfo.educationInfo.educationExperience}</td>
								<td width="14%">院校名称:</td>
								<td width="18%">${applyInfo.assetsInfo.educationInfo.schoolName}</td>
								<td width="14%">地区:</td>
								<td width="18%">
									<c:if test="${'国内学历' == applyInfo.assetsInfo.educationInfo.educationExperience}">${applyInfo.assetsInfo.educationInfo.areaProvince}&nbsp;${applyInfo.assetsInfo.educationInfo.areaCity}</c:if>
									<c:if test="${'国内学历' != applyInfo.assetsInfo.educationInfo.educationExperience}">港澳台及海外</c:if>
								</td>
							</tr>
							<tr>
								<td >最高学历:</td>
								<td>${applyInfo.assetsInfo.educationInfo.qualification}</td>
								<td>毕业时间:</td>
								<td><fmt:formatDate value="${applyInfo.assetsInfo.educationInfo.graduationDate}" pattern="yyyy-MM" type="date"/></td>
								<td>认证书编号:</td>
								<td colspan="2">${applyInfo.assetsInfo.educationInfo.certificateNumber}</td>
							</tr>
							<tr>
								<td>学信网用户名:</td>
								<td>${applyInfo.assetsInfo.educationInfo.chsiAccount}</td>
								<td>学信网密码:</td>
								<td>${applyInfo.assetsInfo.educationInfo.chsiPassword}</td>
								<td>学历信息已验证:</td>
								<td>
									<c:if test="${'Y' ==applyInfo.assetsInfo.educationInfo.chsiAuthenticated}">是</c:if>
									<c:if test="${'N' ==applyInfo.assetsInfo.educationInfo.chsiAuthenticated}">否</c:if>
								</td>
							</tr>
						</table>
						<hr/>
					</form>
				</div>
			</c:if>
		</div>
	</div>
	<!-- ----------------------- -->
	<div class="easyui-panel">
		<h2>
			系统初判 <a class="easyui-linkbutton" onclick="finalSystemDetermine('${applyBasiceInfo.loanNo}')">系统初判</a>
		</h2>
		<table id="final_systemDetermine_table" class="table_ui W100">
			<tr>
				<th>建议核实收入:</th>
				<td ><fmt:formatNumber type="number" value="${auditRulesVO.adviceVerifyIncome}" maxFractionDigits="0"/></td>
				<th>建议到手金额:</th>
				<td ><fmt:formatNumber type="number" value="${auditRulesVO.adviceAuditLines}" maxFractionDigits="0"/></td>
				<th>预估评级费:</th>
				<td><fmt:formatNumber type="number" value="${auditRulesVO.estimatedCost}" maxFractionDigits="2"/></td>
			</tr>
		</table>
	</div>
	<div class="easyui-panel" title="审批意见">
		<form id="finishApprovalOpinion_approvalInfo_from">
			<c:forEach items="${approvalInfo.approvalHistoryList}" var="item" varStatus="stat">
				<!-- 不是最后一个就直接展示信息 -->
				<c:if test="${!stat.last}">
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
							<td colspan="2"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
						</tr>
						<tr>
							<th width="12%">申请额度:</th>
							<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
							<th width="12%">申请期限:</th>
							<td width="20%">${applyBasiceInfo.applyTerm}</td>
							<th width="12%">审批产品:</th>
							<td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
						</tr>
						<tr>
							<th>核实收入:</th>
							<td ><fmt:formatNumber type="number" value="${item.approvalCheckIncome}"  maxFractionDigits="0" /></td>
							<c:if test="${item.rtfState=='XSCS'}">
								<th>初审审批额度:</th>
							</c:if>
							<c:if test="${item.rtfState=='XSZS'}">
								<th>终审审批额度:</th>
							</c:if>
							<td ><fmt:formatNumber type="number" value="${item.approvalLimit}"  maxFractionDigits="0" /></td>
							<th>审批期限:</th>
							<td>${item.approvalTerm}</td>
						</tr>
						<tr>
							<th>月还款额:</th>
							<td ><fmt:formatNumber type="number" value="${item.approvalMonthPay}"  maxFractionDigits="0" /></td>
							<th>内部负债率:</th>
							<td><fmt:formatNumber type="number" value="${item.approvalDebtTate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
							<th>总负债率:</th>
							<td><fmt:formatNumber type="number" value="${item.approvalAllDebtRate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
						</tr>
						<tr>
							<th>审批意见:</th>
							<td colspan="4" rowspan="2" class="W80">${item.approvalMemo}</td>
						</tr>
					</table>
				</c:if>
				<!-- 是最后一个  -->
				<c:if test="${stat.last }">
					<c:if test="${empty currentApprovalHistory}">
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
								<td colspan="2"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
							</tr>
							<tr>
								<th width="12%">申请额度:</th>
								<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
								<th width="12%">申请期限:</th>
								<td width="20%">${applyBasiceInfo.applyTerm}</td>
								<th width="12%">审批产品:</th>
								<td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
							</tr>
							<tr>
								<th>核实收入:</th>
								<td ><fmt:formatNumber type="number" value="${item.approvalCheckIncome}"  maxFractionDigits="0" /></td>
								<c:if test="${item.rtfState=='XSCS'}">
									<th>初审审批额度:</th>
								</c:if>
								<c:if test="${item.rtfState=='XSZS'}">
									<th>终审审批额度:</th>
								</c:if>
								<td ><fmt:formatNumber type="number" value="${item.approvalLimit}"  maxFractionDigits="0" /></td>
								<th>审批期限:</th>
								<td>${item.approvalTerm}</td>
							</tr>
							<tr>
								<th>月还款额:</th>
								<td ><fmt:formatNumber type="number" value="${item.approvalMonthPay}"  maxFractionDigits="0" /></td>
								<th>内部负债率:</th>
								<td><fmt:formatNumber type="number" value="${item.approvalDebtTate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
								<th>总负债率:</th>
								<td><fmt:formatNumber type="number" value="${item.approvalAllDebtRate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
							</tr>
							<tr>
								<th>审批意见:</th>
								<td colspan="4" class="W80">${item.approvalMemo}</td>
							</tr>
						</table>
						<h2>第${stat.index+1+1}次审批</h2>
						<table class="table_ui W100">
							<tr>
								<th>终审人员:</th>
								<td>${resEmployeeVO.name}</td>
								<th>审批时间:</th>
								<td colspan="3"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
							</tr>
							<tr>
								<th width="12%">申请额度:</th>
								<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
								<th width="12%">申请期限:</th>
								<td width="20%">${applyBasiceInfo.applyTerm}</td>
								<th width="12%">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
								<td><input type="text" class="input" name="approvalProductCd" value="${applyInfo.baseInfo.productCd}"></td>
							</tr>
							<tr>
								<th class="requiredFontWeight">核实收入:</th>
								<td><input type="text" ms-attr="{id:'approvalCheckIncome'}" data-options="groupSeparator:','" name="approvalCheckIncome" class="input" value="${item.approvalCheckIncome}"></td>
								<th class="requiredFontWeight">终审审批额度:<input ms-attr="{id:'finishApprovalOpinion_applyMoney_hidden'}" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}"></th>
								<td ><input type="text" ms-attr="{id:'approvalLimit'}" data-options="groupSeparator:','"  name="approvalLimit" class="input"></td>
								<th class="requiredFontWeight">审批期限:</th>
								<td><input type="text" class="input" name="approvalTerm"></td>
							</tr>
							<tr>
								<th>月还款额:<input type="hidden" ms-attr="{id:'approvalMonthPay'}" name="approvalMonthPay"></th>
								<td class="approvalMonthPay "></td>
								<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
								<td class="approvalDebtTate"></td>
								<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
								<td class="approvalAllDebtRate"></td>
							</tr>
							<tr>
								<th>审批意见:</th>
								<td colspan="5" rowspan="2">
									<input class="easyui-textbox W80" ms-attr="{id:'finish_approvalMemo', name:'approvalMemo'}"
										   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo"  >
									<span class="countSurplusText">剩余可输入200字</span></td>
							</tr>
						</table>
					</c:if>
					<c:if test="${not empty currentApprovalHistory}">
						<!-- 如果最后一个是当前人的当前审批意见 -->
						<c:if test="${item.approvalPerson == currentApprovalHistory.approvalPerson}">
							<h2>第${stat.index+1}次审批</h2>
							<table class="table_ui W100">
								<tr>
									<th>终审人员:</th>
									<td>${resEmployeeVO.name}</td>
									<th>审批时间:</th>
									<td colspan="3"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
								</tr>
								<tr>
									<th width="12%">申请额度:</th>
									<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
									<th width="12%">申请期限:</th>
									<td width="20%">${applyBasiceInfo.applyTerm}</td>
									<th width="12%">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
									<td><input type="text" class="input" name="approvalProductCd" value="${item.approvalProductCd}"></td>
								</tr>
								<tr>
									<th class="requiredFontWeight">核实收入:</th>
									<td><input type="text" ms-attr="{id:'approvalCheckIncome'}"  name="approvalCheckIncome" data-options="groupSeparator:','" class="input" value="${item.approvalCheckIncome}"></td>
									<th class="requiredFontWeight">终审审批额度:<input id="finishApprovalOpinion_applyMoney_hidden" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}"></th>
									<td><input type="text" ms-attr="{id:'approvalLimit'}"  name="approvalLimit" data-options="groupSeparator:','"  class="input" value="${item.approvalLimit}"></td>
									<th class="requiredFontWeight">审批期限:</th>
									<td><input type="text" class="input" name="approvalTerm" value="${item.approvalTerm}"></td>
								</tr>
								<tr>
									<th>月还款额:<input type="hidden" ms-attr="{id:'approvalMonthPay'}" name="approvalMonthPay"></th>
									<td class="approvalMonthPay ">${item.approvalMonthPay}</td>
									<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
									<td class="approvalDebtTate"><fmt:formatNumber type="number" value="${item.approvalDebtTate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
									<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
									<td class="approvalAllDebtRate"><fmt:formatNumber type="number" value="${item.approvalAllDebtRate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
								</tr>
								<tr>
									<th>审批意见:</th>
									<td colspan="5" rowspan="2">
										<input class="easyui-textbox W80" ms-attr="{id: 'finish_approvalMemo', name: 'approvalMemo'}" value="${item.approvalMemo}"
											   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo"  >
										<span class="countSurplusText">剩余可输入200字</span>
									</td>
								</tr>
							</table>
						</c:if>
						<!-- 如果最后一个不是当前人的当前审批意见 -->
						<c:if test="${item.approvalPerson != currentApprovalHistory.approvalPerson}">
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
									<td colspan="2"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
								</tr>
								<tr>
									<th width="12%">申请额度:</th>
									<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
									<th width="12%">申请期限:</th>
									<td width="20%">${applyBasiceInfo.applyTerm}</td>
									<th width="12%">审批产品:</th>
									<td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
								</tr>
								<tr>
									<th>核实收入:</th>
									<td ><fmt:formatNumber type="number" value="${item.approvalCheckIncome}"  maxFractionDigits="0" /></td>
									<c:if test="${item.rtfState=='XSCS'}">
										<th>初审审批额度:</th>
									</c:if>
									<c:if test="${item.rtfState=='XSZS'}">
										<th>终审审批额度:</th>
									</c:if>
									<td ><fmt:formatNumber type="number" value="${item.approvalLimit}"  maxFractionDigits="0" /></td>
									<th>审批期限:</th>
									<td>${item.approvalTerm}</td>
								</tr>
								<tr>
									<th>月还款额:</th>
									<td ><fmt:formatNumber type="number" value="${item.approvalMonthPay}"  maxFractionDigits="0" /></td>
									<th>内部负债率:</th>
									<td><fmt:formatNumber type="number" value="${item.approvalDebtTate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
									<th>总负债率:</th>
									<td><fmt:formatNumber type="number" value="${item.approvalAllDebtRate*100}" pattern="0.0" maxFractionDigits="1"/>%</td>
								</tr>
								<tr>
									<th>审批意见:</th>
									<td colspan="4" class="W80">${item.approvalMemo}</td>
								</tr>
							</table>
							<h2>第${stat.index+1+1}次审批</h2>
							<table class="table_ui W100">
								<tr>
									<th>终审人员:</th>
									<td>${resEmployeeVO.name}</td>
									<th>审批时间:</th>
									<td colspan="3"><fmt:formatDate value="${item.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date" /></td>
								</tr>
								<tr>
									<th width="12%">申请额度:</th>
									<td width="20%" ><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"  maxFractionDigits="0" /></td>
									<th width="12%">申请期限:</th>
									<td width="20%">${applyBasiceInfo.applyTerm}</td>
									<th width="12%">审批产品:<input id="money_selectAssetType_hidden"  type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
									<td><input type="text" class="input" name="approvalProductCd" value="${applyInfo.baseInfo.productCd}"></td>
								</tr>
								<tr>
									<th class="requiredFontWeight">核实收入:</th>
									<td><input type="text" ms-attr="{id:'approvalCheckIncome'}"  name="approvalCheckIncome" data-options="groupSeparator:','" class="input" value="${item.approvalCheckIncome}"></td>
									<th class="requiredFontWeight">终审审批额度:<input ms-attr="{id:'finishApprovalOpinion_applyMoney_hidden'}" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}"></th>
									<td><input type="text" ms-attr="{id:'approvalLimit'}" name="approvalLimit" data-options="groupSeparator:','"  class="input" ></td>
									<th class="requiredFontWeight">审批期限:</th>
									<td><input type="text" class="input" name="approvalTerm"></td>
								</tr>
								<tr>
									<th>月还款额:<input type="hidden" ms-attr="{id:'approvalMonthPay'}" name="approvalMonthPay"></th>
									<td class="approvalMonthPay "></td>
									<th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
									<td class="approvalDebtTate"></td>
									<th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
									<td class="approvalAllDebtRate"></td>
								</tr>
								<tr>
									<th>审批意见:</th>
									<td colspan="5" rowspan="2">
										<input class="easyui-textbox W80" ms-attr="{id: 'finish_approvalMemo', name: 'approvalMemo'}"
											   data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo"  >
										<span class="countSurplusText">剩余可输入200字</span>
									</td>
								</tr>
							</table>
						</c:if>
					</c:if>
				</c:if>
			</c:forEach>
		</form>
	</div>
	<!-- 征信初判 -->
	<div id="finishApprovalOpinion_contrast_Dialog" class="padding_20 display_none">
		<table class="table_ui W100" id="finishApprovalOpinion_credit_table"></table>
		<table class="table_list W100" id="finishApprovalOpinion_debt_table"></table>
	</div>
	<div class="h20"></div>
	<div class="float_right">
		<a class="easyui-linkbutton_ok05 l-btn l-btn-small" onclick="finishApprovalOpinionSumbmit('${applyBasiceInfo.loanNo}')">确认</a>&nbsp;&nbsp;
		<a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="closeHTMLWindow()">取消</a>&nbsp; &nbsp;
	</div>
</body>
<script src="${ctx}/resources/js/core/coreApi.js"></script>
<script src="${ctx}/resources/js/bms/bmsBasicApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/zdmoney/finishMoneyApprovalOpinion.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'page',
        loanNo: '${loanNo}',
        approvalInfo: ${approvalInfoJson == null ? '{}' : approvalInfoJson},
        applyBasicInfo: ${applyBasicInfoJson == null ? '{}' : applyBasicInfoJson},
        applyInfo: ${applyInfoJson == null ? '{}' : applyInfoJson},
        markReportIdChange: '${markReportIdChange}',
        selectAssetType:$("#money_selectAssetType_hidden").val(),
    });
</script>
</html>