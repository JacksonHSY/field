<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="money_customerInfo_assetsInfo_div">
    <!-- 保单信息 -->
    <c:if test="${not empty applyInfo.assetsInfo.policyInfo}">
        <div id="money_customer_POLICY_div" title="保单信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.policyInfoVO, 'POLICY') }">
            <c:forEach var="policyInfo" varStatus="status" items="${ applyInfo.assetsInfo.policyInfo}" >
                    <table class="table_ui W100">
                        <tr>
                            <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].insuranceCompany} requiredFontWeight">投保公司:</th>
                            <td colspan="3">${policyInfo.insuranceCompany}</td>
                            <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].paidTerm} requiredFontWeight">已缴年限:</th>
                            <td>${policyInfo.paidTerm}年</td>
                        </tr>
                        <tr>
                            <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyAccount} requiredFontWeight">查询账号:</th>
                            <td width="18%">${policyInfo.policyAccount}</td>
                            <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyPassword} requiredFontWeight">密码:</th>
                            <td width="18%">${policyInfo.policyPassword}</td>
                            <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].yearPaymentAmt} requiredFontWeight">年缴金额:</th>
                            <td><span class="numFormat">${policyInfo.yearPaymentAmt}</span>元</td>
                        </tr>
                    </table>
                    <hr/>
            </c:forEach>
        </div>
    </c:if>
    <!-- 车辆信息 -->
    <c:if test="${'Y' == applyInfo.assetsInfo.carInfo.unabridged}">
        <div id="money_customer_CAR_div" title="车辆信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.carInfo, 'CAR') }">
            <form id="money_customerCar_form" name="CAR">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyPrice} requiredFontWeight">购车价格:</th>
                        <td width="18%"><span class="numFormat">${applyInfo.assetsInfo.carInfo.carBuyPrice}</span>元</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyDate} requiredFontWeight">购车时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carBuyDate}" pattern="yyyy-MM" type="date"/></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.plateNum} requiredFontWeight">车牌号:</th>
                        <td>${applyInfo.assetsInfo.carInfo.plateNum}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanStatus} requiredFontWeight">车贷情况:</th>
                        <td>${applyInfo.assetsInfo.carInfo.carLoanStatus}</td>
                        <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanIssueDate} requiredFontWeight">车贷发放年月:</th>
                        <td><fmt:formatDate value="${applyInfo.assetsInfo.carInfo.carLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
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
            </form>
        </div>
    </c:if>

    <!-- 公积金信息 -->
    <c:if test="${'Y' == applyInfo.assetsInfo.fundInfo.unabridged}">
        <div id="money_customer_PROVIDENT_div" title="公积金信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.fundInfo, 'PROVIDENT') }">
            <form id="money_customerProvident_form" name="PROVIDENT">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundAccount} requiredFontWeight">公积金账号:</th>
                        <td width="18%">${applyInfo.assetsInfo.fundInfo.accumulationFundAccount}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundPassword} requiredFontWeight">公积金账号密码:</th>
                        <td colspan="3">${applyInfo.assetsInfo.fundInfo.accumulationFundPassword}</td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>

    <!-- 社保信息 -->
    <c:if test="${'Y' == applyInfo.assetsInfo.socialInsuranceInfo.unabridged}">
        <div id="money_customer_SOCIAL_INSURANCE_div" title="社保信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.socialInsuranceInfo, 'SOCIAL_INSURANCE') }">
            <form id="money_customerSOCIAL_INSURANCE_form" name="SOCIAL_INSURANCE">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount} requiredFontWeight">社保账号:</th>
                        <td width="18%">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword} requiredFontWeight">社保账号密码:</th>
                        <td colspan="3">${applyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword}</td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>

    <%--&lt;%&ndash;信用卡信息&ndash;%&gt;
    <c:if test="${'Y' == applyInfo.assetsInfo.cardLoanInfo.unabridged}">
        <div class="easyui-panel" title="信用卡信息" id="money_customer_CARDLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.cardLoanInfo, 'CARDLOAN') }">
            <form id="money_customerCardLoan_form" name="CARDLOAN">
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
            </form>
        </div>
    </c:if>--%>

    <%--房产信息--%>
    <c:if test="${'Y' == applyInfo.assetsInfo.estateInfo.unabridged}">
        <div class="easyui-panel" title="房产信息" id="money_customer_ESTATE_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.estateInfo, 'ESTATE') }">
            <form id="money_customerHome_form" name="ESTATE">
                <table class="table_ui W100">
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateType} requiredFontWeight">房产类型:</th>
                        <td width="18%">${applyInfo.assetsInfo.estateInfo.estateType}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateBuyDate} requiredFontWeight">购房时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateBuyDate}" pattern="yyyy-MM" type="date"/></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.isCommon} requiredFontWeight">与他人共有情况:</th>
                        <td><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==1}">是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==0}">否</c:if></td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateSameRegistered} requiredFontWeight">房产地址同住宅地址:</th>
                        <td colspan="5"><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='Y'}"> 是</c:if><c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='N'}">否</c:if></td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.estateInfo.estateZone} ${oldApplyInfo.assetsInfo.estateInfo.estateAddress} requiredFontWeight">房产地址:</th>
                        <td colspan="5" class="isEqueryFamilyAddress">${applyInfo.assetsInfo.estateInfo.estateState}&nbsp;${applyInfo.assetsInfo.estateInfo.estateCity}&nbsp;${applyInfo.assetsInfo.estateInfo.estateZone}&nbsp;${applyInfo.assetsInfo.estateInfo.estateAddress}</td>
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
                        <td colspan="5"><fmt:formatDate value="${applyInfo.assetsInfo.estateInfo.estateLoanIssueDate}" pattern="yyyy-MM" type="date"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>

    <%--淘宝账号贷--%>
    <c:if test="${'Y' == applyInfo.assetsInfo.masterLoanInfo.unabridged}">
        <div class="easyui-panel" title="淘宝账户信息" id="money_customer_MASTERLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.merchantLoanInfo, 'MASTERLOAN') }">
            <form id="money_customerMerchant_form" name="MASTERLOAN">
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
                        <td><span class="numFormat">${applyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt}</span>元</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.naughtyValue} requiredFontWeight">淘气值:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.naughtyValue}</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.sesameCreditValue} requiredFontWeight">芝麻信用分:</th>
                        <td>${applyInfo.assetsInfo.masterLoanInfo.sesameCreditValue}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.costLimit} requiredFontWeight">花呗额度:</th>
                        <td><span class="numFormat">${applyInfo.assetsInfo.masterLoanInfo.costLimit}</span>元</td>
                        <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.borrowLimit} requiredFontWeight">借呗额度:</th>
                        <td><span class="numFormat">${applyInfo.assetsInfo.masterLoanInfo.borrowLimit}</span>元</td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>

    <%--学历信息--%>
    <c:if test="${'Y' == applyInfo.assetsInfo.educationInfo.unabridged}">
        <div id="money_customer_EDUCATION_div" class="easyui-panel" title="学历信息" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.educationInfo, 'EDUCATION') }">
            <form id="money_customerEDUCATION_form" name="EDUCATION">
                <table class="table_ui W100">
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.educationExperience} requiredFontWeight">教育经历:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.educationExperience}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.schoolName} requiredFontWeight">院校名称:</th>
                        <td colspan="4">${applyInfo.assetsInfo.educationInfo.schoolName}</td>
                    </tr>
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.qualification} requiredFontWeight">最高学历:</th>
                        <td width="18%">${applyInfo.assetsInfo.educationInfo.qualification}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.graduationDate} requiredFontWeight">毕业时间:</th>
                        <td width="18%"><fmt:formatDate value="${applyInfo.assetsInfo.educationInfo.graduationDate}" pattern="yyyy-MM" type="date"/></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.areaProvince} ${oldApplyInfo.assetsInfo.educationInfo.areaCity} requiredFontWeight">地区:</th>
                        <td>
                            <c:if test="${'国内学历' == applyInfo.assetsInfo.educationInfo.educationExperience}">${applyInfo.assetsInfo.educationInfo.areaProvince}&nbsp;${applyInfo.assetsInfo.educationInfo.areaCity}</c:if>
                            <c:if test="${'国内学历' != applyInfo.assetsInfo.educationInfo.educationExperience}">港澳台及海外</c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiAccount} requiredFontWeight">学信网用户名:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.chsiAccount}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiPassword} requiredFontWeight">学信网密码:</th>
                        <td>${applyInfo.assetsInfo.educationInfo.chsiPassword}</td>
                        <th class="${oldApplyInfo.assetsInfo.educationInfo.certificateNumber} requiredFontWeight">认证书编号:</th>
                        <td colspan="2">${applyInfo.assetsInfo.educationInfo.certificateNumber}</td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>
</div>