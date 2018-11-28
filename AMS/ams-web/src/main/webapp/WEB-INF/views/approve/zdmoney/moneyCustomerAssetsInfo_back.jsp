<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="money_customerInfo_assetsInfo_div">
    <!-- 保单信息 -->
    <div id="money_customer_POLICY_div" title="保单信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.policyInfoVO, 'POLICY') }">
        <c:forEach var="policyInfo" varStatus="status" items="${ applyInfo.assetsInfo.policyInfo}" >
            <form>
                <table class="table_ui W100">
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].insuranceCompany} requiredFontWeight">投保公司:</th>
                        <td colspan="3"><input  class="input" name="insuranceCompany" id="money_insuranceCompany_${status.index}" value="${policyInfo.insuranceCompany}"></td>
                        <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].insuranceTerm} requiredFontWeight">保险年限:</th>
                        <td><input class="input" id="money_customerInfo_assetsInfo_policyInfo_insuranceTerm_${status.index}" value="${policyInfo.insuranceTerm}" name="insuranceTerm"></td>
                    </tr>
                    <tr>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].insuranceAmt} requiredFontWeight">保险金额:</th>
                        <td width="18%"><input name="insuranceAmt" id="money_customerInfo_assetsInfo_policyInfo_insuranceAmt_${status.index}" value="${policyInfo.insuranceAmt}" class="easyui-numberbox input" ></td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].yearPaymentAmt} requiredFontWeight">年缴金额:</th>
                        <td width="18%">${policyInfo.yearPaymentAmt}</td>
                        <th width="14%" class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].yearPaymentAmt} requiredFontWeight">已缴年限:</th>
                        <td>${policyInfo.paidTerm}</td>
                    </tr>
                    <tr>
                        <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyAccount} requiredFontWeight">查询账号:</th>
                        <td><input class="easyui-textbox input" name="policyAccount" id="money_insuranceAccount_${status.index}" value="${policyInfo.policyAccount}"></td>
                        <th class="${oldApplyInfo.assetsInfo.policyInfoList[status.index].policyPassword} requiredFontWeight">密码:</th>
                        <td colspan="3"><input class="easyui-textbox input" name="policyPassword" id="money_insurancePassword_${status.index}" value="${policyInfo.policyPassword}"></td>
                    </tr>
                </table>
                <hr/>
            </form>
        </c:forEach>
        <a class="easyui-linkbutton_ok05 l-btn l-btn-small"  onclick="customerAddContacts()">新增保单贷信息</a>&nbsp;&nbsp;
    </div>
    <!-- 车辆信息 -->
    <div id="money_customer_CAR_div" title="车辆信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.carInfo, 'CAR') }">
        <form id="money_customerCar_form" name="CAR">
            <table class="table_ui W100">
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyPrice} requiredFontWeight">购买价格:</th>
                    <td width="18%"><input id="money_customerInfo_carInfo_carBuyPrice" class="easyui-numberbox input" value="${applyInfo.assetsInfo.carInfo.carBuyPrice}" name="carBuyPrice"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.carBuyDate} requiredFontWeight">购买时间:</th>
                    <td width="18%"><input class="easyui-datebox input" id="money_customerInfo_date_CAR" value="${applyInfo.assetsInfo.carInfo.carBuyDate}" name="carBuyDate"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.carInfo.plateNum} requiredFontWeight">车牌号:</th>
                    <td><input class="easyui-textbox input" id="money_customerInfo_car_plateNum" value="${applyInfo.assetsInfo.carInfo.plateNum}" name="plateNum"/></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanStatus} requiredFontWeight">车贷情况:</th>
                    <td><input id="money_carLoanSituation" class="easyui-combobox input" name="carLoan" value="${applyInfo.assetsInfo.carInfo.carLoanStatus}"></td>
                    <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanIssueDate} requiredFontWeight">车贷发放年月:</th>
                    <td><input id="money_customerInfo_carInfo_carLoanGrantDate" class="easyui-datebox input" name="carLoanIssueDate"></td>
                    <th class="${oldApplyInfo.assetsInfo.carInfo.monthPaymentAmt} requiredFontWeight">车贷月还款额:</th>
                    <td><input id="money_Customer_Info_monthPaymentAmt" class="easyui-numberbox input" value="${applyInfo.assetsInfo.carInfo.monthPaymentAmt}" name="monthPaymentAmt"></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.carInfo.carLoanOrg} requiredFontWeight">车贷发放机构:</th>
                    <td colspan="5"><input id="money_carLoanOrg" class="input" value="${applyInfo.assetsInfo.carInfo.carLoanOrg}" name="carLoanOrg"></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.carInfo.tciCompany} requiredFontWeight">交强险承保公司:</th>
                    <td colspan="5"><input id="money_carLoanInsuranceCompany" class="input" value="${applyInfo.assetsInfo.carInfo.tciCompany}" name="tciCompany"></td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 公积金信息 -->
    <div id="money_customer_PROVIDENT_div" title="公积金信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.fundInfo, 'PROVIDENT') }">
        <form id="money_customerProvident_form" name="PROVIDENT">
            <table class="table_ui W100">
                <tr class="providentLoanRecordTr">
                    <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundAccount} requiredFontWeight">公积金账号:</th>
                    <td width="18%"><input class="easyui-textbox input" id="money_accumulationFundAccount" name="accumulationFundAccount" value="${applyInfo.assetsInfo.fundInfo.accumulationFundAccount}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.fundInfo.accumulationFundPassword} requiredFontWeight">公积金账号密码:</th>
                    <td colspan="3"><input id="money_accumulationFundPassword" class="easyui-textbox input" name="accumulationFundPassword" value="${applyInfo.assetsInfo.fundInfo.accumulationFundPassword}"></td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 社保信息 -->
    <div id="money_customer_SOCIAL_INSURANCE_div" title="社保信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.socialInsuranceInfo, 'SOCIAL_INSURANCE') }">
        <form id="money_customerSOCIAL_INSURANCE_form" name="SOCIAL_INSURANCE">
            <table class="table_ui W100">
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount} requiredFontWeight">社保账号:</th>
                    <td width="18%"><input id="money_socialSecurityAccount" class="easyui-textbox input" name="socialInsuranceAccount" value="${applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword} requiredFontWeight">社保账号密码:</th>
                    <td colspan="3"><input id="money_socialSecurityPassword" class="easyui-textbox input" name="socialInsurancePassword" value="${applyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword}"></td>
                </tr>
            </table>
        </form>
    </div>

    <%--卡友贷信息--%>
    <div class="easyui-panel" title="卡友贷信息" id="money_customer_CARDLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.cardLoanInfo, 'CARDLOAN') }">
        <form id="money_customerCardLoan_form" name="CARDLOAN">
            <table class="table_ui W100" id="money_customerInfo_cardLoan_table">
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.creditLimit} requiredFontWeight">信用卡额度:</th>
                    <td width="18%"><input class="easyui-numberbox input" id="money_customerInfo_assetsInfo_cardLoanInfo_creditLimit" value="${applyInfo.assetsInfo.cardLoanInfo.creditLimit}" name="creditLimit"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.issusingBank} requiredFontWeight">发卡银行:</th>
                    <td width="18%"><input id="money_cardLoanInfo_issuingBank" class="easyui-textbox input" name="issusingBank" value="${applyInfo.assetsInfo.cardLoanInfo.issusingBank}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.cardLoanInfo.payMonthAmt} requiredFontWeight">每月还款日:</th>
                    <td><input id="money_cardLoanInfo_dueDate" class="easyui-numberbox input" name="repaymentDay" value="${applyInfo.assetsInfo.cardLoanInfo.repaymentDay}"></td>
                </tr>
            </table>
        </form>
    </div>

    <%--房产信息--%>
    <div class="easyui-panel" title="房产信息" id="money_customer_ESTATE_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.estateInfo, 'ESTATE') }">
        <form id="money_customerHome_form" name="ESTATE">
            <table class="table_ui W100">
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateType} requiredFontWeight">房产类型:</th>
                    <td width="18%"><input class="easyui-combobox  input" id="money_customerInfo_assetsInfo_estateInfo_estateType" value="${applyInfo.assetsInfo.estateInfo.estateType}" name="estateType"/></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.estateBuyDate} requiredFontWeight">购房时间:</th>
                    <td width="18%"><input class="easyui-datebox input" id="money_customerInfo_date_ESTATE" name="estateBuyDate"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.estateInfo.isCommon} requiredFontWeight">与他人共有情况:</th>
                    <td>是<input type="radio" value="1" name="isCommon" <c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==1}">checked="checked"</c:if>/>
                        否<input type="radio" value="0" name="isCommon" <c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==0}">checked="checked"</c:if>/>
                    </td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.estateSameRegistered} requiredFontWeight">房产地址同住宅地址:<input type="hidden" value="${applyInfo.assetsInfo.estateInfo.estateSameRegistered}" id="money_isEqualHomeAddr"></th>
                    <td colspan="5">
                        是<input type="radio" value="Y" name="estateSameRegistered" <c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='Y'}">checked="checked"</c:if>/>
                        否<input type="radio" value="N" name="estateSameRegistered" <c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='N'}">checked="checked"</c:if>/>
                    </td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.estateAddress} requiredFontWeight">房产地址:</th>
                    <td><input style="width: 212px;" id="moneyCustomerInfo_house_province_combobox" class="input" value="${applyInfo.assetsInfo.estateInfo.estateStateId}" name="estateStateId"><input class="region" name="estateState" type="hidden"></td>
                    <td><input id="moneyCustomerInfo_house_city_combobox" class="input" value="${applyInfo.assetsInfo.estateInfo.estateCityId}" name="estateCityId" style="height: 30px; width: 212px;">
                        <input class="region" name="estateCity" type="hidden">
                    </td>
                    <td><input id="moneyCustomerInfo_house_country_combobox" class="input" value="${applyInfo.assetsInfo.estateInfo.estateZoneId}" name="estateZoneId" style="height: 30px; width: 212px;">
                        <input class="region" name="estateZone" type="hidden">
                    </td>
                    <td colspan="2">
                        <input id="money_customerInfo_estateAddress" class="easyui-textbox W50" name="estateAddress" value="${applyInfo.assetsInfo.estateInfo.estateAddress}" style="height: 30px; width: 380px;">
                    </td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoan} requiredFontWeight">房贷情况:</th>
                    <td><input class="easyui-combobox input" id="money_customerInfo_estateInfo_estateLoan" value="${applyInfo.assetsInfo.estateInfo.estateLoan}" name="estateLoan"/></td>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoanAmt} requiredFontWeight">房贷总额:</th>
                    <td><input id="money_estateInfo_loanCount" class="easyui-numberbox input" name="estateLoanAmt" value="${applyInfo.assetsInfo.estateInfo.estateLoanAmt}"></td>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.monthPaymentAmt} requiredFontWeight">房贷月还款额:</th>
                    <td><input class="easyui-numberbox input" id="moneyCustomerInfo_house_monthPaymentAmt" value="${applyInfo.assetsInfo.estateInfo.monthPaymentAmt}" name="monthPaymentAmt"></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.estateInfo.estateLoanIssueDate} requiredFontWeight">房贷发放年月</th>
                    <td colspan="5"><input class="easyui-datebox input" id="moneyCustomerInfo_house_estateLoanGrantDate" name="estateLoanIssueDate"></td>
                </tr>
            </table>
        </form>
    </div>

    <%--淘宝账号贷--%>
    <div class="easyui-panel" title="淘宝账户信息" id="money_customer_MASTERLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.merchantLoanInfo, 'MASTERLOAN') }">
        <form id="money_customerMerchant_form" name="MASTERLOAN">
            <table class="table_ui W100" id="money_customerInfo_merchant_table">
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.memberName} requiredFontWeight">淘宝会员名:</th>
                    <td width="18%"><input id="money_taoBaoInfo_account" class="easyui-textbox input" name="memberName" value="${applyInfo.assetsInfo.masterLoanInfo.memberName}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel} requiredFontWeight">淘宝买家信用等级:</th>
                    <td width="18%"><input id="money_taoBaoInfo_buyerCreditLevel" class="easyui-combobox input" name="buyerCreditLevel" value="${applyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.masterLoanInfo.buyerCreditType} requiredFontWeight">淘宝买家信用类型:</th>
                    <td><input id="money_taoBaoInfo_buyerCreditType" class="easyui-combobox input" name="buyerCreditType" value="${applyInfo.assetsInfo.masterLoanInfo.buyerCreditType}"></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt} requiredFontWeight">淘宝年消费额:</th>
                    <td><input id="money_taoBaoInfo_totalConsumption" class="easyui-numberbox input" name="lastYearPayAmt" value="${applyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt}"></td>
                    <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.naughtyValue} requiredFontWeight">淘气值:</th>
                    <td><input id="money_taoBaoInfo_naughtyValue" class="easyui-numberbox input" name="naughtyValue" value="${applyInfo.assetsInfo.masterLoanInfo.naughtyValue}"></td>
                    <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.sesameCreditValue} requiredFontWeight">芝麻信用分:</th>
                    <td><input id="money_taoBaoInfo_sesameCreditValue" class="easyui-numberbox input" name="sesameCreditValue" value="${applyInfo.assetsInfo.masterLoanInfo.sesameCreditValue}"></td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.costLimit} requiredFontWeight">花呗额度:</th>
                    <td><input id="money_taoBaoInfo_antSpend" class="easyui-numberbox input" name="costLimit" value="${applyInfo.assetsInfo.masterLoanInfo.costLimit}"></td>
                    <th class="${oldApplyInfo.assetsInfo.masterLoanInfo.borrowLimit} requiredFontWeight">借呗额度:</th>
                    <td><input id="money_taoBaoInfo_antBorrow" class="easyui-numberbox input" name="borrowLimit" value="${applyInfo.assetsInfo.masterLoanInfo.borrowLimit}"></td>
                    <th></th>
                    <td></td>
                </tr>
            </table>
        </form>
    </div>

    <%--学历信息--%>
    <div id="money_customer_EDUCATION_div" class="easyui-panel" title="学历信息" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfo.educationInfo, 'EDUCATION') }">
        <form id="money_customerEDUCATION_form" name="EDUCATION">
            <table class="table_ui W100">
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.educationInfo.educationExperience} requiredFontWeight">教育经历:</th>
                    <td><input id="money_education_experience" class="easyui-combobox input" name="educationExperience" value="${applyInfo.assetsInfo.educationInfo.educationExperience}"></td>
                    <th class="${oldApplyInfo.assetsInfo.educationInfo.schoolName} requiredFontWeight">院校名称:</th>
                    <td colspan="4"><input id="money_education_schoolName" class="easyui-textbox input"  name="schoolName" value="${applyInfo.assetsInfo.educationInfo.schoolName}"></td>
                </tr>
                <tr>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.qualification} requiredFontWeight">最高学历:</th>
                    <td width="18%"><input id="money_education_qualification" class="easyui-combobox input"  name="qualification" value="${applyInfo.assetsInfo.educationInfo.qualification}"></td>
                    <th width="14%" class="${oldApplyInfo.assetsInfo.educationInfo.graduationDate} requiredFontWeight">毕业时间:</th>
                    <td width="18%"><input class="easyui-datebox input" id="money_education_graduationDate"  name="graduationDate"></td>
                    <th width="14%" class=" requiredFontWeight">地区:</th>
                    <td>
                        <input id="money_education_province" name="areaProvinceId" data-options="width:145" class="input" value="${applyInfo.assetsInfo.educationInfo.areaProvinceId}">
                        <input class="region" name="areaProvince" type="hidden">
                    </td>
                    <td>
                        <input id="money_education_city" class="input" name="areaCity" data-options="width:145" value="${applyInfo.assetsInfo.educationInfo.areaCityId}">
                        <input class="region" name="areaCity" type="hidden">
                    </td>
                </tr>
                <tr>
                    <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiAccount} requiredFontWeight">学信网用户名:</th>
                    <td><input id="money_education_chsiAccount" class="easyui-textbox input"  name="chsiAccount" value="${applyInfo.assetsInfo.educationInfo.chsiAccount}"></td>
                    <th class="${oldApplyInfo.assetsInfo.educationInfo.chsiPassword} requiredFontWeight">学信网密码:</th>
                    <td><input id="money_education_chsiPassword" class="easyui-textbox input"  name="chsiPassword" value="${applyInfo.assetsInfo.educationInfo.chsiPassword}"></td>
                    <th class="${oldApplyInfo.assetsInfo.educationInfo.certificateNumber} requiredFontWeight">认证书编号:</th>
                    <td colspan="2"><input class="input"  name="certificateNumber" value="${applyInfo.assetsInfo.educationInfo.certificateNumber}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>