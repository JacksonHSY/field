<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${applyInfo.applicantInfo.personalInfo.name}-前前客户信息</title>
    <jsp:include page="../../common/commonJS.jsp"></jsp:include>
    <jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body>
<div class="easyui-tabs">
    <div title="基本信息" class="padding_20">
        <div class="easyui-panel" title="个人信息">
            <table class="table_ui W100">
                <tr>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.name} requiredFontWeight">申请人姓名:</th>
                    <td width="18%">${applyInfo.applicantInfo.personalInfo.name}</td>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.idNo} requiredFontWeight">身份证号码:</th>
                    <td width="18%">${applyInfo.applicantInfo.personalInfo.idNo}</td>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.gender} requiredFontWeight">性别:</th>
                    <td>${ applyInfo.applicantInfo.personalInfo.gender }</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.age} requiredFontWeight">年龄:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.age}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.maritalStatus} requiredFontWeight">婚姻状况:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.maritalStatus}</td>
                    <th>&nbsp;</th>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.qualification} requiredFontWeight">最高学历:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.qualification}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.monthMaxRepay} requiredFontWeight">可接受的月最高还款:</th>
                    <td><span class="numFormat">${applyInfo.applicantInfo.personalInfo.monthMaxRepay}</span>元</td>
                    <th>&nbsp;</th>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.idIssuerAddress} requiredFontWeight">户籍地址:</th>
                    <td colspan="5">${applyInfo.applicantInfo.personalInfo.issuerState}&nbsp;${applyInfo.applicantInfo.personalInfo.issuerCity}&nbsp;${applyInfo.applicantInfo.personalInfo.issuerZone}&nbsp;${applyInfo.applicantInfo.personalInfo.idIssuerAddress}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.homeSameRegistered} requiredFontWeight">住宅地址是否同户籍地址:</th>
                    <td><c:if test="${applyInfo.applicantInfo.personalInfo.homeSameRegistered==0}">是</c:if><c:if test="${applyInfo.applicantInfo.personalInfo.homeSameRegistered==1}">否</c:if></td>
                    <th>&nbsp;</th>
                    <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.homeAddress} requiredFontWeight">住宅地址:</th>
                    <td colspan="5">${applyInfo.applicantInfo.personalInfo.homeState}&nbsp;${applyInfo.applicantInfo.personalInfo.homeCity}&nbsp;${applyInfo.applicantInfo.personalInfo.homeZone}&nbsp;${applyInfo.applicantInfo.personalInfo.homeAddress}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.cellphone} requiredFontWeight">常用手机:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.cellphone}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.cellphoneSec}">备用手机:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.cellphoneSec}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.homePhone1}">家庭电话:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.homePhone1}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.qqNum}">QQ号:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.qqNum}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.weChatNum}">微信号:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.weChatNum}</td>
                    <th class="${oldApplyInfo.applicantInfo.personalInfo.email} requiredFontWeight">电子邮箱:</th>
                    <td>${applyInfo.applicantInfo.personalInfo.email}</td>
                </tr>
            </table>
        </div>
        <div class="easyui-panel" title="工作信息">
            <table class="table_ui W100">
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpName} requiredFontWeight">单位名称:</th>
                    <td colspan="5">${applyInfo.applicantInfo.workInfo.corpName}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpAddress} requiredFontWeight">单位地址:</th>
                    <td colspan="5">${applyInfo.applicantInfo.workInfo.corpProvince}&nbsp;${applyInfo.applicantInfo.workInfo.corpCity}&nbsp;${applyInfo.applicantInfo.workInfo.corpZone}&nbsp;${applyInfo.applicantInfo.workInfo.corpAddress }</td>
                </tr>
                <tr>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.corpStructure} requiredFontWeight">单位性质:</th>
                    <td width="18%">${applyInfo.applicantInfo.workInfo.corpStructure}</td>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.occupation} requiredFontWeight">职业类型:</th>
                    <td width="18%">${applyInfo.applicantInfo.workInfo.occupation}</td>
                    <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.cusWorkType} requiredFontWeight">客户工作类型</th>
                    <td> ${applyInfo.applicantInfo.workInfo.cusWorkType}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpDepapment} requiredFontWeight">任职部门:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpDepapment}</td>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpPost} requiredFontWeight">职务:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpPost}</td>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpType} requiredFontWeight">行业类别:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpType}</td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpStandFrom} requiredFontWeight">入职时间:</th>
                    <td><fmt:formatDate value="${applyInfo.applicantInfo.workInfo.corpStandFrom}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpPayWay} requiredFontWeight">发薪方式:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpPayWay}</td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpPhone} requiredFontWeight">单位电话:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpPhone}</td>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.corpPhoneSec}">单位电话2:</th>
                    <td>${applyInfo.applicantInfo.workInfo.corpPhoneSec}</td>
                    <th class="${oldApplyInfo.applicantInfo.workInfo.totalMonthSalary} requiredFontWeight">税前月总收入:</th>
                    <td><span class="numFormat">${applyInfo.applicantInfo.workInfo.totalMonthSalary}</span>元</td>
                </tr>
            </table>
        </div>
        <c:if test="${applyInfo.applicantInfo.workInfo.cusWorkType == '私营业主' or applyInfo.applicantInfo.workInfo.cusWorkType == '自雇人士'}">
            <c:if test="${not empty applyInfo.applicantInfo.privateOwnerInfo}">
                <div class="easyui-panel" title="私营业主信息">
                    <table class="table_ui W100">
                        <tr>
                            <th width="14%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.setupDate} requiredFontWeight">成立时间:</th>
                            <td width="18%"><fmt:formatDate value="${applyInfo.applicantInfo.privateOwnerInfo.setupDate}" pattern="yyyy-MM" type="date"/></td>
                            <th width="14%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.sharesScale} requiredFontWeight">占股比例:</th>
                            <td width="18%"><fmt:formatNumber type="number" value="${applyInfo.applicantInfo.privateOwnerInfo.sharesScale}" pattern="0.00" maxFractionDigits="2"/>%</td>
                            <th width="14%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.registerFunds} requiredFontWeight">注册资本:</th>
                            <td><span class="numFormat">${applyInfo.applicantInfo.privateOwnerInfo.registerFunds}</span>元</td>
                        </tr>
                        <tr>
                            <th class="${oldApplyInfo.applicantInfo.privateOwnerInfo.priEnterpriseType} requiredFontWeight">私营企业类型:</th>
                            <td>${applyInfo.applicantInfo.privateOwnerInfo.priEnterpriseType}</td>
                            <th class="${oldApplyInfo.applicantInfo.privateOwnerInfo.monthAmt} requiredFontWeight">每月净利润额:</th>
                            <td colspan="3"><span class="numFormat">${applyInfo.applicantInfo.privateOwnerInfo.monthAmt}</span>元</td>
                        </tr>
                    </table>
                </div>
            </c:if>
        </c:if>
    </div>
    <div title="联系人信息" class="padding_20">
        <c:if test="${not empty applyInfo.contactInfo}">
            <c:set value="1" var="ifHasContactRelation"></c:set><!-- 用于标识是否有配偶方便取第几联系人 -->
            <c:forEach var="item" items="${applyInfo.contactInfo}" varStatus="status">
                <c:if test="${status.first and item.contactRelation.indexOf('配偶')>=0}">
                    <c:set value="0" var="ifHasContactRelation"></c:set>
                    <div class="easyui-panel" title="配偶信息表">
                        <table class="table_ui W100">
                            <tr>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].contactName} requiredFontWeight">姓名:</th>
                                <td width="20%">${item.contactName}</td>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                <td width="20%">${item.contactRelation}</td>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].ifKnowLoan} requiredFontWeight">知晓贷款:</th>
                                <td>${item.ifKnowLoan}</td>
                            </tr>
                            <tr>
                                <th class="${oldApplyInfo.contactInfoList[status.index].ifForeignPenple} requiredFontWeight">外籍人士:</th>
                                <td>${item.ifForeignPenple}</td>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactIdNo} requiredFontWeight">身份证号码:</th>
                                <td><div style="width:220px;word-wrap:break-word;overflow:hidden">${item.contactIdNo}</div></td>
                                <th></th>
                                <td></td>
                            </tr>
                            <tr>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                <td>${item.contactCellPhone}</td>
                                <c:if test="${not empty item.contactCellPhone_1}">
                                    <td>${item.contactCellPhone_1}</td>
                                </c:if>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactCorpPhone}">单电:</th>
                                <td>${item.contactCorpPhone}</td>
                                <td>${item.contactCorpPhone_1}</td>
                            </tr>
                            <tr>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                <td colspan="5">${item.contactEmpName}</td>
                            </tr>
                        </table>
                    </div>
                </c:if>
                <c:if test="${ item.contactRelation.indexOf('配偶') < 0}">
                    <div class="easyui-panel" title="第${status.index + ifHasContactRelation}联系人信息">
                        <table class="table_ui W100">
                            <tr>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].contactName} requiredFontWeight">姓名:</th>
                                <td width="20%">${item.contactName}</td>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                                <td width="20%">${item.contactRelation}</td>
                                <th width="12%" class="${oldApplyInfo.contactInfoList[status.index].ifKnowLoan} requiredFontWeight">知晓贷款:</th>
                                <td>${item.ifKnowLoan}</td>
                            </tr>
                            <tr>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                                <td>${item.contactCellPhone}</td>
                                <c:if test="${not empty item.contactCellPhone_1}">
                                    <td>${item.contactCellPhone_1}</td>
                                </c:if>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactCorpPhone}">单电:</th>
                                <td>${item.contactCorpPhone}</td>
                                <td>${item.contactCorpPhone_1}</td>
                            </tr>
                            <tr>
                                <th class="${oldApplyInfo.contactInfoList[status.index].contactEmpName} requiredFontWeight">单位名称:</th>
                                <td colspan="5">${item.contactEmpName}</td>
                            </tr>
                        </table>
                    </div>
                </c:if>
            </c:forEach>
        </c:if>
    </div>
    <div title="资产信息" class="padding_20">
        <c:if test="${not empty applyInfo.assetsInfo.policyInfo}">
            <div class="easyui-panel" title="保单信息">
                <c:forEach var="policyInfo" varStatus="status" items="${ applyInfo.assetsInfo.policyInfo}">
                    <c:if test="${'Y' == policyInfo.unabridged}">
                        <table class="table_ui W100">
                            <tr>
                                <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].insuranceCompany} requiredFontWeight">投保公司:</th>
                                <td colspan="3">${policyInfo.insuranceCompany}</td>
                                <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].yearPaymentAmt} requiredFontWeight">已缴年限:</th>
                                <td>${policyInfo.paidTerm}</td>
                            </tr>
                            <tr>
                                <th  width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyAccount} requiredFontWeight">查询账号:</th>
                                <td width="18%">${policyInfo.policyAccount}</td>
                                <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyPassword} requiredFontWeight">密码:</th>
                                <td width="18%">${policyInfo.policyPassword}</td>
                                <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].yearPaymentAmt} requiredFontWeight">年缴金额:</th>
                                <td><span class="numFormat">${policyInfo.yearPaymentAmt}</span>元</td>
                            </tr>
                        </table>
                        <hr/>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${'Y' == applyInfo.assetsInfo.carInfo.unabridged}">
            <div class="easyui-panel" title="车辆信息">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyPrice} requiredFontWeight">购车价格:</th>
                        <td width="18%"><span class="numFormat">${applyInfo.assetsInfo.carInfo.carBuyPrice}</span>元</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyDate} requiredFontWeight">购车时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carBuyDate}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.plateNum} requiredFontWeight">车牌号:</th>
                        <td>${applyInfo.assetsInfo.carInfo.plateNum}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanStatus} requiredFontWeight">车贷情况:</th>
                        <td>${applyInfo.assetsInfo.carInfo.carLoanStatus}</td>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanIssueDate} requiredFontWeight">车贷发放年月:</th>
                        <td><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carLoanIssueDate}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.monthPaymentAmt} requiredFontWeight">车贷月还款额:</th>
                        <td><span class="numFormat">${applyInfo.assetsInfo.carInfo.monthPaymentAmt}</span>元</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanOrg} requiredFontWeight">车贷发放机构:</th>
                        <td colspan="5">${applyInfo.assetsInfo.carInfo.carLoanOrg}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.tciCompany} requiredFontWeight">交强险承保公司:</th>
                        <td colspan="5">${applyInfo.assetsInfo.carInfo.tciCompany}</td>
                    </tr>
                </table>
            </div>
        </c:if>

        <c:if test="${'Y' == applyInfo.assetsInfo.fundInfo.unabridged}">
            <div class="easyui-panel" title="公积金信息">
                <table class="table_ui W100">
                    <tr class="providentLoanRecordTr">
                        <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundAccount} requiredFontWeight">公积金账号:</th>
                        <td width="18%">${applyInfo.assetsInfo.fundInfo.accumulationFundAccount}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundPassword} requiredFontWeight">公积金账号密码:</th>
                        <td colspan="3">${applyInfo.assetsInfo.fundInfo.accumulationFundPassword}</td>
                    </tr>
                </table>
            </div>
        </c:if>

        <c:if test="${'Y' == applyInfo.assetsInfo.socialInsuranceInfo.unabridged}">
            <div class="easyui-panel" title="社保信息">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount} requiredFontWeight">社保账号:</th>
                        <td width="18%">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword} requiredFontWeight">社保账号密码:</th>
                        <td colspan="3">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword}</td>
                    </tr>
                </table>
            </div>
        </c:if>

        <%--<c:if test="${'Y' == applyInfo.assetsInfo.cardLoanInfo.unabridged}">
            <div class="easyui-panel" title="信用卡信息">
                <table class="table_ui W100" id="money_customerInfo_cardLoan_table">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.creditLimit} requiredFontWeight">信用卡额度:</th>
                        <td width="18%"><span class="numFormat">${applyInfo.assetsInfo.cardLoanInfo.creditLimit}</span>元</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.issusingBank} requiredFontWeight">发卡银行:</th>
                        <td width="18%">${applyInfo.assetsInfo.cardLoanInfo.issusingBank}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.issuerDate} requiredFontWeight">发卡时间:</th>
                        <td><fmt:formatDate value="${applyInfo.assetsInfo.cardLoanInfo.issuerDate}" pattern="yyyy-MM" type="date" /></td>
                    </tr>
                </table>
            </div>
        </c:if>--%>

        <c:if test="${'Y' == applyInfo.assetsInfo.estateInfo.unabridged}">
            <div class="easyui-panel" title="房产信息">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateType} requiredFontWeight">房产类型:</th>
                        <td width="18%">${applyInfo.assetsInfo.estateInfo.estateType}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateBuyDate} requiredFontWeight">购房时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateBuyDate}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.isCommon} requiredFontWeight">与他人共有情况:</th>
                        <td><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==1}">是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==0}">否</c:if></td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateSameRegistered} requiredFontWeight">房产地址同住宅地址:</th>
                        <td colspan="5"><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='Y'}">是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='N'}">否</c:if></td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateAddress} requiredFontWeight">房产地址:</th>
                        <td colspan="5">${applyInfo.assetsInfo.estateInfo.estateState}&nbsp;${applyInfo.assetsInfo.estateInfo.estateCity}&nbsp;${applyInfo.assetsInfo.estateInfo.estateZone}&nbsp;${applyInfo.assetsInfo.estateInfo.estateAddress}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoan} requiredFontWeight">房贷情况:</th>
                        <td>${applyInfo.assetsInfo.estateInfo.estateLoan}</td>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoanAmt} requiredFontWeight">房贷总额:</th>
                        <td><span class="numFormat">${applyInfo.assetsInfo.estateInfo.estateLoanAmt}</span>元</td>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.monthPaymentAmt} requiredFontWeight">房贷月还款额:</th>
                        <td><span class="numFormat">${applyInfo.assetsInfo.estateInfo.monthPaymentAmt}</span>元</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoanIssueDate} requiredFontWeight">房贷发放年月</th>
                        <td colspan="5"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateLoanIssueDate}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                    </tr>
                </table>
            </div>
        </c:if>

        <c:if test="${'Y' == applyInfo.assetsInfo.masterLoanInfo.unabridged}">
            <div class="easyui-panel" title="淘宝账户信息">
                <table class="table_ui W100" id="money_customerInfo_merchant_table">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.memberName} requiredFontWeight">淘宝会员名:</th>
                        <td width="18%">${applyInfo.assetsInfo.masterLoanInfo.memberName}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel} requiredFontWeight">淘宝买家信用等级:</th>
                        <td width="18%">${applyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.buyerCreditType} requiredFontWeight">淘宝买家信用类型:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.buyerCreditType}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt} requiredFontWeight">淘宝年消费额:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt}</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.naughtyValue} requiredFontWeight">淘气值:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.naughtyValue}</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.sesameCreditValue} requiredFontWeight">芝麻信用分:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.sesameCreditValue}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.costLimit} requiredFontWeight">花呗额度:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.costLimit}</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.borrowLimit} requiredFontWeight">借呗额度:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.borrowLimit}</td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
            </div>
        </c:if>

        <c:if test="${'Y' == applyInfo.assetsInfo.educationInfo.unabridged}">
            <div class="easyui-panel" title="学历信息">
                <table class="table_ui W100">
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.educationExperience} requiredFontWeight">教育经历:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.educationExperience}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.schoolName} requiredFontWeight">院校名称:</th>
                        <td colspan="3">${applyInfo.assetsInfo.educationInfo.schoolName}</td>
                    </tr>
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.qualification} requiredFontWeight">最高学历:</th>
                        <td width="18%">${applyInfo.assetsInfo.educationInfo.qualification}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.graduationDate} requiredFontWeight">毕业时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.educationInfo.graduationDate}" pattern="yyyy-MM" type="date"></fmt:formatDate></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.areaProvince} ${oldApplyInfo.assetsInfo.educationInfo.areaCity} requiredFontWeight">地区:</th>
                        <td><c:if test="${'国内学历' == applyInfo.assetsInfo.educationInfo.educationExperience}">${applyInfo.assetsInfo.educationInfo.areaProvince}&nbsp;${applyInfo.assetsInfo.educationInfo.areaCity}</c:if>
                            <c:if test="${'国内学历' != applyInfo.assetsInfo.educationInfo.educationExperience}">港澳台及海外</c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiAccount} requiredFontWeight">学信网用户名:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.chsiAccount}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiPassword} requiredFontWeight">学信网密码:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.chsiPassword}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.certificateNumber} requiredFontWeight">认证书编号:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.certificateNumber}</td>
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
</html>