<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyInfo.basicInfoVO.personInfoVO.name}-客户信息</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

	<div class="h20"></div>
	<div class="easyui-tabs">
		<div title="基本信息" class="padding_20">
			<div class="easyui-panel" title="个人信息">
				<table class="table_ui W100">
					<tr>
						<th width="13%">申请人姓名:</th>
						<td width="16%">${applyInfo.basicInfoVO.personInfoVO.name}</td>
						<th width="13%">身份证号码:</th>
						<td width="16%">${applyInfo.basicInfoVO.personInfoVO.idNo}</td>
						<th width="13%">性别:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.gender}</td>
					</tr>
					<tr>
						<th>年龄:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.age}</td>
						<th>婚姻状况:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.maritalStatus}</td>
						<th>子女数:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.childrenNum}</td>
					</tr>
					<tr>
						<th>最高学历:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.qualification}</td>
						<th>毕业时间:</th>
						<td><fmt:formatDate value="${applyInfo.basicInfoVO.personInfoVO.graduationDate}" pattern="yyyy-MM" type="date"/></td>
					</tr>
					<tr>
						<th>户籍地址:</th>
						<td colspan="5">${applyInfo.basicInfoVO.personInfoVO.issuerState}${applyInfo.basicInfoVO.personInfoVO.issuerCity}${applyInfo.basicInfoVO.personInfoVO.issuerZone}${applyInfo.basicInfoVO.personInfoVO.idIssuerAddress}</td>
					</tr>
					<tr>
						<th>住宅地址同户籍地址:</th>
						<td colspan="5"><c:if test="${applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == 0}">是</c:if> <c:if test="${applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == 1}">否</c:if></td>
					</tr>
					<tr>
						<th>住宅地址:</th>
						<td colspan="5">${applyInfo.basicInfoVO.personInfoVO.homeState}${applyInfo.basicInfoVO.personInfoVO.homeCity}${applyInfo.basicInfoVO.personInfoVO.homeZone}${applyInfo.basicInfoVO.personInfoVO.homeAddress}</td>
					</tr>
					<tr>
						<th>住宅类型:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.houseType}</td>
						<th>租金:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.personInfoVO.houseRent}" maxFractionDigits="0" />元</td>
						<th>住宅邮编:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerPostcode}</td>
					</tr>
					<tr>
						<th>每月家庭支出:</th>
						<td><span class="numFormat">${applyInfo.basicInfoVO.personInfoVO.familyMonthPay}</span>元</td>
						<th>可接受的月最高还款:</th>
						<td><span class="numFormat">${applyInfo.basicInfoVO.personInfoVO.monthMaxRepay}</span>元</td>
						<th>户籍邮编:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerPostcode}</td>
					</tr>
					<tr>
						<th>常用手机:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.cellphone}</td>
						<th>备用手机:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.cellphoneSec}</td>
						<th>宅电:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.homePhone1}</td>
					</tr>
					<tr>
						<th>QQ号:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.qqNum}</td>
						<th>微信号:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.weChatNum}</td>
						<th>电子邮箱:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.email}</td>
					</tr>
				</table>
			</div>
			<div class="easyui-panel" title="工作信息">
				<table class="table_ui W100">
					<tr>
						<th>单位名称:</th>
						<td colspan="3">${applyInfo.basicInfoVO.workInfoVO.corpName}</td>
						<th>客户工作类型:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.cusWorkType}</td>
					</tr>
					<tr>
						<th>单位地址:</th>
						<td colspan="5">${applyInfo.basicInfoVO.workInfoVO.corpProvince}${applyInfo.basicInfoVO.workInfoVO.corpCity}${applyInfo.basicInfoVO.workInfoVO.corpZone}${applyInfo.basicInfoVO.workInfoVO.corpAddress}</td>
					</tr>
					<tr>
						<th width="13%">工商网信息:</th>
						<td width="16%">${applyInfo.basicInfoVO.workInfoVO.businessNetWork}</td>
						<th width="13%">单位性质:</th>
						<td width="16%">${applyInfo.basicInfoVO.workInfoVO.corpStructure}</td>
						<th width="13%">职业:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.occupation}</td>
					</tr>
					<tr>
						<th>任职部门:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpDepapment}</td>
						<th>职务:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPost}</td>
						<th>单位行业类别:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpType}</td>

					</tr>
					<tr>
						<th>入职时间:</th>
						<td><fmt:formatDate value="${applyInfo.basicInfoVO.workInfoVO.corpStandFrom}" pattern="yyyy-MM" type="date"/></td>
						<th>发薪方式:</th>
						<td colspan="5">${applyInfo.basicInfoVO.workInfoVO.corpPayWay}</td>
					</tr>
					<tr>
						<th>单电:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPhone}</td>
						<th>单电2:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPhoneSec}</td>
						<th>单位邮编:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPostcode}</td>
					</tr>
					<tr>
						<th>单位月收入:</th>
						<td><span class="numFormat">${applyInfo.basicInfoVO.workInfoVO.monthSalary}</span>元</td>
						<th>其他月收入:</th>
						<td><fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.otherIncome}" maxFractionDigits="0" />元</td>
						<th>月总收入:</th>
						<td><span class="numFormat">${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}</span>元</td>
					</tr>
				</table>
			</div>
			<c:if test="${applyInfo.basicInfoVO.workInfoVO.cusWorkType == '私营业主'}">
				<c:if test="${not empty applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}">
					<div class="easyui-panel" title="私营业主信息">
						<table class="table_ui W100">
							<tr>
								<th width="13%">成立时间:</th>
								<td width="16%"><fmt:formatDate value="${applyInfo.basicInfoVO.privateOwnerInfoVO.setupDate}" pattern="yyyy-MM" type="date"/></td>
								<th width="13%">占股比例:</th>
								<td width="16%">
									<fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale}"  pattern="0.00" maxFractionDigits="2" />%
								</td>
								<th width="13%">注册资本:</th>
								<td><span class="numFormat">${applyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds}</span>元</td>
							</tr>
							<tr>
								<th>私营企业类型:</th>
								<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}</td>
								<th>每月净利润额:</th>
								<td colspan="3"><span class="numFormat">${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}</span>元</td>
							</tr>
						</table>
					</div>
				</c:if>
			</c:if>
			<c:if test="${applyInfo.basicInfoVO.workInfoVO.cusWorkType == '自雇人士'}">
				<c:if test="${not empty applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}">
					<div class="easyui-panel" title="私营业主信息">
						<table class="table_ui W100">
							<tr>
								<th width="13%">成立时间:</th>
								<td width="16%"><fmt:formatDate value="${applyInfo.basicInfoVO.privateOwnerInfoVO.setupDate}" pattern="yyyy-MM" type="date"/></td>
								<th width="13%">占股比例:</th>
								<td width="16%">
									<fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale}"  pattern="0.00" maxFractionDigits="2" />%
								</td>
								<th width="13%">注册资本:</th>
								<td><span class="numFormat">${applyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds}</span>元</td>
							</tr>
							<tr>
								<th>私营企业类型:</th>
								<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}</td>
								<th>每月净利润额:</th>
								<td colspan="3"><span class="numFormat">${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}</span>元</td>
							</tr>
						</table>
					</div>
				</c:if>
			</c:if>
		</div>
        <div title="联系人信息" class="padding_20">
            <c:if test="${not empty applyInfo.contactInfoVOList}">
                <c:choose>
                    <c:when test="${applyInfo.contactInfoVOList[0].contactRelation.indexOf('配偶')>=0}">
                        <c:forEach var="item" items="${applyInfo.contactInfoVOList}" varStatus="status">
                            <c:if test="${status.first}">
                                <div class="easyui-panel" title="配偶信息表">
                                    <table class="table_ui W100">
                                        <tr>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
                                            <td width="20%">${item.contactName}</td>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                            <td width="20%">${item.contactRelation}</td>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
                                            <td>${item.ifKnowLoan}</td>
                                        </tr>
                                        <tr>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].ifForeignPenple}">外籍人士:</th>
                                            <td>${item.ifForeignPenple}</td>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactIdNo} requiredFontWeight">身份证号码:</th>
											<td><div style="width:220px;word-wrap:break-word; overflow:hidden">${item.contactIdNo}</div></td>
                                        </tr>
                                        <tr>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                            <td>${item.contactCellPhone}</td>
                                            <c:if test="${not empty item.contactCellPhone_1}">
                                                <td>${item.contactCellPhone_1}</td>
                                            </c:if>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单电:</th>
                                            <td>${item.contactCorpPhone}</td>
                                            <td>${item.contactCorpPhone_1}</td>
                                        </tr>
                                        <tr>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                            <td colspan="5">${item.contactEmpName}</td>
                                        </tr>
                                    </table>
                                </div>
                            </c:if>
                            <c:if test="${!status.first}">
                                <div class="easyui-panel" title="第${status.index}联系人信息">
                                    <table class="table_ui W100">
                                        <tr>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
                                            <td width="20%">${item.contactName}</td>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                            <td width="20%">${item.contactRelation}</td>
                                            <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
                                            <td>${item.ifKnowLoan}</td>
                                        </tr>
                                        <tr>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                            <td>${item.contactCellPhone}</td>
                                            <c:if test="${not empty item.contactCellPhone_1}">
                                                <td>${item.contactCellPhone_1}</td>
                                            </c:if>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单电:</th>
                                            <td>${item.contactCorpPhone}</td>
                                            <td>${item.contactCorpPhone_1}</td>
                                        </tr>
                                        <tr>
                                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                            <td colspan="5">${item.contactEmpName}</td>
                                        </tr>
                                    </table>
                                </div>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${applyInfo.contactInfoVOList}" varStatus="status">
                            <c:choose>
                                <c:when test="${status.last && item.contactRelation.indexOf('配偶') >= 0}">
                                    <div class="easyui-panel" title="配偶信息表">
                                        <table class="table_ui W100">
                                            <tr>
                                                <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
                                                <td width="20%">${item.contactName}</td>
                                                <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                                <td width="20%">${item.contactRelation}</td>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
                                                <td>${item.ifKnowLoan}</td>
                                            </tr>
                                            <tr>
                                                <th>外籍人士:</th>
                                                <td>${item.ifForeignPenple}</td>
                                                <th width="12%">身份证号码:</th>
                                                <td>${item.contactIdNo}</td>
                                            </tr>
                                            <tr>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                                <td>${item.contactCellPhone}</td>
                                                <c:if test="${not empty item.contactCellPhone_1}">
                                                    <td>${item.contactCellPhone_1}</td>
                                                </c:if>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单电:</th>
                                                <td>${item.contactCorpPhone}</td>
                                                <td>${item.contactCorpPhone_1}</td>
                                            </tr>
                                            <tr>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                                <td colspan="5">${item.contactEmpName}</td>
                                            </tr>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="easyui-panel" title="第${status.index+1}联系人信息">
                                        <table class="table_ui W100">
                                            <tr>
                                                <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
                                                <td width="20%">${item.contactName}</td>
                                                <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                                <td width="20%">${item.contactRelation}</td>
                                                <th width="12%" class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
                                                <td>${item.ifKnowLoan}</td>
                                            </tr>
                                            <tr>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                                <td>${item.contactCellPhone}</td>
                                                <c:if test="${not empty item.contactCellPhone_1}">
                                                    <td>${item.contactCellPhone_1}</td>
                                                </c:if>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单电:</th>
                                                <td>${item.contactCorpPhone}</td>
                                                <td>${item.contactCorpPhone_1}</td>
                                            </tr>
                                            <tr>
                                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                                <td colspan="5">${item.contactEmpName}</td>
                                            </tr>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
		<div title="资产信息" class="padding_20">
			<c:if test="${applyInfo.assetsInfoVO.policyInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="保单信息">
					<table class="table_ui W100">
						<tr>
							<th width="16%">保险金额:</th>
							<td width="16%"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.insuranceAmt}" maxFractionDigits="0" />元</td>
							<th width="16%">保险年限: </th>
							<td width="16%">${applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm == 999 ? "终身": applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm.toString().concat(' 年')}</td>
							<th width="16%">已缴年限:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.paidTerm}" pattern="0" maxFractionDigits="0" />年</td>
						</tr>
						<tr>
							<th>年缴金额:</th>
							<td colspan="5"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt}" maxFractionDigits="0" />元</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.carInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="车辆信息">
					<table class="table_ui W100">
						<tr>
							<th width="16%">购买价:</th>
							<td width="16%"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.carBuyPrice}" maxFractionDigits="0" />元</td>
							<th width="16%">购买时间:</th>
							<td width="16%"><fmt:formatDate value="${applyInfo.assetsInfoVO.carInfoVO.carBuyDate}" pattern="yyyy-MM" type="date"/></td>
							<th width="16%">车牌号:</th>
							<td>${applyInfo.assetsInfoVO.carInfoVO.plateNum}</td>
						</tr>
						<tr>
							<th>是否有车贷:</th>
							<td>${applyInfo.assetsInfoVO.carInfoVO.carLoan}</td>
							<th>车贷发放年月:</th>
							<td><fmt:formatDate value="${applyInfo.assetsInfoVO.carInfoVO.carLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
							<th>月供:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}" maxFractionDigits="0" />元</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.providentInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="公积金信息">
					<table class="table_ui W100 ">
						<tr>
							<th width="16%">缴存比例:</th>
							<td width="16%"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.depositRate}" pattern="0.00" maxFractionDigits="2" />%</td>
							<th width="16%">月缴存额:</th>
							<td width="16%"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt}" maxFractionDigits="0" />元</td>
							<th width="16%">缴存基数:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.depositBase}" maxFractionDigits="0" />元</td>
						</tr>
						<tr>
							<th>公积金材料:</th>
							<td>${applyInfo.assetsInfoVO.providentInfoVO.providentInfo}</td>
							<th>缴纳单位同申请单位:</th>
							<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentUnit}</td>
							<th>申请单位已缴月数:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum}" pattern="0" maxFractionDigits="0" />月</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.cardLoanInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="卡友贷信息">
					<table class="table_ui W100 ">
						<tr>
							<th width="16%">发卡时间:</th>
							<td width="16%"><fmt:formatDate value="${applyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate}" pattern="yyyy-MM" type="date"/></td>
							<th width="16%">额度:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit}" maxFractionDigits="0" />元</td>
						</tr>
						<tr>
							<th>近4个月账单金额依次为:</th>
							<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp; <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4}" maxFractionDigits="0" />元
							</td>
						</tr>
						<tr>
							<th>月均:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}" maxFractionDigits="0" />元</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.estateInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="房产信息">
					<table class="table_ui W100">
						<tr>
							<th width="16%">房产类型:</th>
							<td width="16%">${applyInfo.assetsInfoVO.estateInfoVO.estateType}</td>
							<th width="16%">购买时间:</th>
							<td width="16%"><fmt:formatDate value="${applyInfo.assetsInfoVO.estateInfoVO.estateBuyDate}" pattern="yyyy-MM" type="date"/></td>
							<th width="16%">市值参考价:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.referenceAmt}" maxFractionDigits="0" />元</td>
						</tr>
						<tr>
							<th>房产地址同住宅地址:</th>
							<td colspan="5"><c:if test="${applyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered == 'Y'}">是</c:if> <c:if test="${applyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered == 'N'}">否</c:if></td>
						</tr>
						<tr>
							<th>房产地址:</th>
							<td colspan="5">${applyInfo.assetsInfoVO.estateInfoVO.estateState}${applyInfo.assetsInfoVO.estateInfoVO.estateCity}${applyInfo.assetsInfoVO.estateInfoVO.estateZone}${applyInfo.assetsInfoVO.estateInfoVO.estateAddress}</td>
						</tr>
						<tr>
							<th>房贷情况:</th>
							<td>${applyInfo.assetsInfoVO.estateInfoVO.estateLoan}</td>
							<th>房贷发放年月:</th>
							<td><fmt:formatDate value="${applyInfo.assetsInfoVO.estateInfoVO.estateLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
							<th>月供:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt}" maxFractionDigits="0" />元</td>
						</tr>
						<tr>
							<th>产权比例:</th>
							<td>${applyInfo.assetsInfoVO.estateInfoVO.equityRate}</td>
							<th>单据户名为本人:</th>
							<td colspan="3">${applyInfo.assetsInfoVO.estateInfoVO.ifMe}</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.masterLoanInfoBVO.ifEmpty==1}">
				<div class="easyui-panel" title="网购达人贷B信息">
					<table class="table_ui W100">
						<tr>
							<th width="16%">京东用户等级:</th>
							<td width="16%">${applyInfo.assetsInfoVO.masterLoanInfoBVO.jiDongUserLevel}</td>
							<th width="16%">小白信用分:</th>
							<td width="16%"><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoBVO.whiteCreditValue}" maxFractionDigits="1" /></td>
							<th width="16%">近一年实际消费金额:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoBVO.pastYearShoppingAmount}" maxFractionDigits="0" />元</td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.masterLoanInfoAVO.ifEmpty==1}">
				<div class="easyui-panel" title="网购达人贷A信息">
					<table class="table_ui W100">
						<tr>
							<th width="16%">买家信用等级:</th>
							<td width="16%">${applyInfo.assetsInfoVO.masterLoanInfoAVO.buyerCreditLevel}</td>
							<th width="16%">买家信用类型:</th>
							<td width="16%">${applyInfo.assetsInfoVO.masterLoanInfoAVO.buyerCreditType}</td>
							<th width="16%">芝麻信用分:</th>
							<td>${applyInfo.assetsInfoVO.masterLoanInfoAVO.sesameCreditValue}</td>
						</tr>
						<tr>
							<th>近12个月支出额:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoAVO.lastYearPayAmt}" maxFractionDigits="0" />元</td>
							<th>淘气值:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoAVO.naughtyValue}" maxFractionDigits="0" /></td>
						</tr>
					</table>
				</div>
			</c:if>
			<c:if test="${applyInfo.assetsInfoVO.merchantLoanInfoVO.ifEmpty==1}">
				<div class="easyui-panel" title="淘宝商户贷">
					<table class="table_ui W100">
						<tr>
							<th width="16%">开店时间:</th>
							<td width="16%"><fmt:formatDate value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate}" pattern="yyyy-MM" type="date"/></td>
							<th width="16%">卖家信用等级:</th>
							<td width="16%">${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}</td>
							<th width="16%">卖家信用类型:</th>
							<td width="16%">${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType}</td>
						</tr>
						<tr>
							<th>近半年好评数:</th>
							<td colspan="5">${applyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum}</td>
						</tr>
						<tr>
							<th>近6个月账单金额依次为:</th>
							<td colspan="5">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3}" maxFractionDigits="0" />元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4}" maxFractionDigits="0" />元 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5}" maxFractionDigits="0" />元 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber type="number"
									value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}" maxFractionDigits="0" />元
							</td>
						</tr>
						<tr>
							<th>月均:</th>
							<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}" maxFractionDigits="0" />元</td>
						</tr>

					</table>
				</div>
			</c:if>
		</div>
		<c:if test="${showResSupplementalContacts}">
		<div title="补充联系人" class="padding_20">
			<c:if test="${not empty supplementalContactList}">
				<c:forEach var="item" items="${supplementalContactList}">
					<div class="easyui-panel" title="其他联系人信息">
						<table class="table_ui W100">
							<tr>
								<th width="12%" class="requiredFontWeight">姓名:</th>
								<td width="20%">${item.name}</td>
								<th width="12%" class="requiredFontWeight">与申请人关系:</th>
								<td width="20%">${item.relation}</td>
							</tr>
							<tr>
								<th class="<c:if test="${not empty item.mPhone}">requiredFontWeight</c:if>">手机:</th>
								<td>${item.mPhone} &nbsp;&nbsp; ${item.mPhone2}</td>
								<th  class="<c:if test="${not empty item.tel}">requiredFontWeight</c:if>">宅电:</th>
								<td>${item.tel} &nbsp;&nbsp; ${item.tel2}</td>
							</tr>
							<tr>
								<th class="<c:if test="${not empty item.cTel}">requiredFontWeight</c:if>">单电:</th>
								<td>${item.cTel} &nbsp;&nbsp; ${item.cTel2}</td>
							</tr>
							<tr>
								<th>备注:</th>
								<td colspan="3">${item.memo}</td>
							</tr>
						</table>
					</div>
				</c:forEach>
			</c:if>
		</div>
		</c:if>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishCustomerInfo.js"></script>
</html>