<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="first_customerInfo_assetsInfo_div">
    <!-- 保单信息 -->
    <div id="first_customer_POLICY_div" title="保单信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.policyInfoVO, 'POLICY') }" >
        <form id="first_customerPolicy_form" name="POLICY">
            <table class="table_ui W100">
                <tr>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.policyInfoVO.insuranceAmt} requiredFontWeight">
                        保险金额:
                    </th>
                    <td width="20%">
                        <input id="first_customerInfo_assetsInfo_policyInfo_insuranceAmt" name="insuranceAmt" placeholder="保险金额" class="easyui-numberbox input" value="${applyInfo.assetsInfoVO.policyInfoVO.insuranceAmt}">
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.policyInfoVO.insuranceTerm} requiredFontWeight">
                        保险年限:
                    </th>
                    <td width="20%"><input class="easyui-textbox input"
                                           id="first_customerInfo_assetsInfo_policyInfo_insuranceTerm"
                                           value="${applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm == 999? "终身":applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm}"
                                           name="insuranceTerm"></td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.policyInfoVO.paidTerm} requiredFontWeight">
                        已缴年限:
                    </th>
                    <td>
                        <input id="first_customerInfo_assetsInfo_policyInfo_paidTerm" name="paidTerm" class="easyui-numberbox  input" value="${applyInfo.assetsInfoVO.policyInfoVO.paidTerm}" >
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt} requiredFontWeight">年缴金额:</th>
                    <td colspan="5">
                        <input id="first_customerInfo_assetsInfo_policyInfo_yearPaymentAmt" name="yearPaymentAmt" class="easyui-numberbox input" value="${applyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt}">
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 车辆信息 -->
    <div id="first_customer_CAR_div" title="车辆信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.carInfoVO, 'CAR') }" >
        <form id="first_customerCar_form" name="CAR">
            <table class="table_ui W100">
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carBuyPrice} requiredFontWeight">购买价:</th>
                    <td>
                        <input id="first_customerInfo_carInfo_carBuyPrice" class="easyui-numberbox input"
                               value="${applyInfo.assetsInfoVO.carInfoVO.carBuyPrice}" name="carBuyPrice">
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.carInfoVO.carBuyDate}">购买时间:</th>
                    <td><input class="input" id="first_customerInfo_date_CAR"
                               value="${applyInfo.assetsInfoVO.carInfoVO.carBuyDate}" name="carBuyDate"></td>
                    <th class="${oldapplyInfo.assetsInfoVO.carInfoVO.plateNum} requiredFontWeight">车牌号:</th>
                    <td><input class="easyui-textbox input" id="first_customerInfo_car_plateNum"
                               value="${applyInfo.assetsInfoVO.carInfoVO.plateNum}" name="plateNum"/></td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carLoan} requiredFontWeight">
                        是否有车贷:<input type="hidden" value="${applyInfo.assetsInfoVO.carInfoVO.carLoan}" id="hasCarLoan">
                    </th>
                    <td>
                        是<input type="radio" name="carLoan" <c:if test="${'Y' == applyInfo.assetsInfoVO.carInfoVO.carLoan}">checked="checked"</c:if> value="Y"/>
                        否<input type="radio" name="carLoan" <c:if test="${'N' == applyInfo.assetsInfoVO.carInfoVO.carLoan}">checked="checked"</c:if> value="N"/>
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carLoanIssueDate}">车贷发放年月</th>
                    <td><input id="first_customerInfo_carInfo_carLoanGrantDate" class="input" name="carLoanIssueDate">
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}">月供:</th>
                    <td><input id="first_Customer_Info_monthPaymentAmt" class="easyui-numberbox input" value="${applyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}" name="monthPaymentAmt"></td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 公积金信息 -->
    <div id="first_customer_PROVIDENT_div" title="公积金信息" class="easyui-panel" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.providentInfoVO, 'PROVIDENT') }" >
        <form id="first_customerProvident_form" name="PROVIDENT">
            <table class="table_ui W100">
                <tr class="providentLoanRecordTr">
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.providentInfoVO.depositRate} requiredFontWeight">
                        缴存比例:
                    </th>
                    <td width="20%">
                        <input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_providentInfo_depositRate"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.depositRate}" name="depositRate">
                    </td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt} requiredFontWeight">月缴存额:
                    </th>
                    <td>
                        <input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_providentInfo_monthDepositAmt"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt}" name="monthDepositAmt">
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.depositBase} requiredFontWeight">缴存基数:</th>
                    <td>
                        <input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_providentInfo_depositBase"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.depositBase}" name="depositBase">
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.providentInfo} requiredFontWeight">公积金材料:
                    </th>
                    <td>
                        <input class="easyui-combobox input"
                               id="first_customerInfo_assetsInfo_providentInfo_providentInfo"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.providentInfo}" name="providentInfo"/>
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.paymentUnit} requiredFontWeight">缴纳单位同申请单位:
                    </th>
                    <td>
                        <input class="easyui-combobox input"
                               id="first_customerInfo_assetsInfo_providentInfo_paymentUnit"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.paymentUnit}" name="paymentUnit"/>
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum} requiredFontWeight">
                        申请单位已缴月数:
                    </th>
                    <td colspan="5">
                        <input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_providentInfo_paymentMonthNum"
                               value="${applyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum}" name="paymentMonthNum">
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <%--卡友贷信息--%>
    <div class="easyui-panel" title="卡友贷信息" id="first_customer_CARDLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.cardLoanInfoVO, 'CARDLOAN') }" >
        <form id="first_customerCardLoan_form" name="CARDLOAN">
            <table class="table_ui W100" id="first_customerInfo_cardLoan_table">
                <tr>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate} requiredFontWeight">
                        发卡时间:
                    </th>
                    <td width="20%">
                        <input class="input" id="first_customerInfo_date_CARDLOAN" name="issuerDate">
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit} requiredFontWeight">
                        额度:
                    </th>
                    <td>
                        <input class="easyui-numberbox input" id="first_customerInfo_assetsInfo_cardLoanInfo_creditLimit" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit}" name="creditLimit">
                    </td>
                </tr>
                <tr class="cardLoanRecordTr">
                    <th class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1} ${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2} ${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3} ${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4} requiredFontWeight">近4个月账单金额依次为:</th>
                    <td colspan="3">
                        <input class="easyui-numberbox input billAmt" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1}" id="cardLoanBillAmt1" name="billAmt1">
                        <input class="easyui-numberbox input billAmt" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2}" id="cardLoanBillAmt2" name="billAmt2">
                        <input class="easyui-numberbox input billAmt" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3}" id="cardLoanBillAmt3" name="billAmt3">
                        <input class="easyui-numberbox input billAmt" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4}" id="cardLoanBillAmt4" name="billAmt4">
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}">月均:</th>
                    <td colspan="3">
                        <input id="first_customerInfo_cardLoan_payMonthAmt" class=" input" type="hidden" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}" name="payMonthAmt">
                        <span class="numFormat">
                            <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}" pattern="0" maxFractionDigits="0"/>
                        </span>元
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <%--房产信息--%>
    <div class="easyui-panel" title="房产信息" id="first_customer_ESTATE_div"  ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.estateInfoVO, 'ESTATE') }" >
        <form id="first_customerHome_form" name="ESTATE">
            <table class="table_ui W100">
                <tr>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateType} requiredFontWeight">
                        房产类型:
                    </th>
                    <td width="20%"><input class="easyui-combobox  input"
                                           id="first_customerInfo_assetsInfo_estateInfo_estateType"
                                           value="${applyInfo.assetsInfoVO.estateInfoVO.estateType}" name="estateType"/>
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateBuyDate} requiredFontWeight">
                        购买时间:
                    </th>
                    <td width="20%">
                        <input class="input" id="first_customerInfo_date_ESTATE" name="estateBuyDate"></td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.estateInfoVO.referenceAmt} requiredFontWeight">
                        市值参考价:
                    </th>
                    <td><input class="easyui-numberbox input" id="first_customerInfo_assetsInfo_estateInfo_referenceAmt"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.referenceAmt}" name="referenceAmt"></td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered} requiredFontWeight">
                        房产地址同住宅地址:<input type="hidden"
                                         value="${applyInfo.assetsInfoVO.estateInfoVO.estateSameRegistered}"
                                         id="isEqualHomeAddr">
                    </th>
                    <td colspan="5">
                        是<input type="radio" name="estateSameRegistered" value="Y"/>
                        否<input type="radio" name="estateSameRegistered" value="N"/>
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateZoneId} ${oldapplyInfo.assetsInfoVO.estateInfoVO.estateAddress} requiredFontWeight">房产地址:</th>
                    <td>
                        <input style="width: 212px;" id="firstCustomerInfo_house_province_combobox" class="input"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.estateStateId}"
                               name="estateStateId"><input class="region" name="estateState" type="hidden">
                    </td>
                    <td>
                        <input id="firstCustomerInfo_house_city_combobox" class="input"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.estateCityId}" name="estateCityId"
                               style="height: 30px; width: 212px;">
                        <input class="region" name="estateCity" type="hidden">
                    </td>
                    <td>
                        <input id="firstCustomerInfo_house_country_combobox" class="input"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.estateZoneId}" name="estateZoneId"
                               style="height: 30px; width: 212px;">
                        <input class="region" name="estateZone" type="hidden">
                    </td>
                    <td colspan="2">
                        <input id="first_customerInfo_estateAddress" class="easyui-textbox W50" name="estateAddress"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.estateAddress}"
                               style="height: 30px; width: 380px;">
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateLoan} requiredFontWeight">房贷情况:</th>
                    <td>
                        <input class="easyui-combobox input" id="first_customerInfo_estateInfo_estateLoan"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.estateLoan}"
                               name="estateLoan"/>
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateLoanIssueDate}">房贷发放年月</th>
                    <td>
                        <input class="input" id="firstCustomerInfo_house_estateLoanGrantDate" name="estateLoanIssueDate">
                    </td>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt} requiredFontWeight">月供:</th>
                    <td>
                        <input class="easyui-numberbox input" id="firstCustomerInfo_house_monthPaymentAmt"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt}" name="monthPaymentAmt">
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.equityRate} requiredFontWeight">产权比例:</th>
                    <td>
                        <input class="easyui-numberbox input" id="firstCustomerInfo_house_Ownership_numberbox"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.equityRate}" name="equityRate">
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.estateInfoVO.ifMe} requiredFontWeight">单据户名为本人:
                    </th>
                    <td colspan="2" width="20%">
                        <input class="easyui-combobox  input" id="firstCustomerInfo_house_ifMe"
                               value="${applyInfo.assetsInfoVO.estateInfoVO.ifMe}" name="ifMe"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <%--网购达人贷B信息--%>
    <div class="easyui-panel" title="网购达人贷B信息" id="first_customer_MASTERLOAN_B_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.masterLoanInfoBVO, 'MASTERLOAN_B') }" >
        <form id="first_customeMasterLoanB_form" name="MASTERLOAN_B">
            <table class="table_ui W100">
                <tr>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel} requiredFontWeight">
                        京东用户等级:
                    </th>
                    <td width="20%"><input class="easyui-combobox input"
                                           id="first_customerInfo_assetsInfo_masterLoanInfoB_jiDongUserLevel"
                                           value="${applyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel}"
                                           name="jiDongUserLevel"/></td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue} requiredFontWeight">
                        小白信用分:
                    </th>
                    <td width="20%"><input class="easyui-numberbox input"
                                           id="first_customerInfo_assetsInfo_masterLoanInfoB_whiteCreditValue"
                                           value="${applyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue}"
                                           name="whiteCreditValue"></td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount} requiredFontWeight">
                        近一年实际消费金额:
                    </th>
                    <td><input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_masterLoanInfoB_pastYearShoppingAmount"
                               value="${applyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount}"
                               name="pastYearShoppingAmount"></td>
                </tr>
            </table>
        </form>
    </div>

    <%--网购达人贷A信息--%>
    <div class="easyui-panel" title="网购达人贷A信息" id="first_customer_MASTERLOAN_A_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.masterLoanInfoAVO, 'MASTERLOAN_A') }" >
        <form id="first_customeMasterLoanA_form" name="MASTERLOAN_A">
            <table class="table_ui W100" id="first_customeInfo_masterLoanA_table">
                <tr>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel} requiredFontWeight">
                        买家信用等级:
                    </th>
                    <td width="20%"><input class="easyui-combobox input"
                                           id="first_customerInfo_assetsInfo_masterLoanInfoA_buyerCreditLevel"
                                           value="${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel}"
                                           name="buyerCreditLevel"/></td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType} requiredFontWeight">
                        买家信用类型:
                    </th>
                    <td width="20%"><input class="easyui-combobox input"
                                           id="first_customerInfo_assetsInfo_masterLoanInfoA_buyerCreditType"
                                           value="${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType}"
                                           name="buyerCreditType"/></td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue} requiredFontWeight">
                        芝麻信用分:
                    </th>
                    <td><input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_masterLoanInfoA_sesameCreditValue"
                               value="${applyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue}"
                               name="sesameCreditValue"></td>
                </tr>

                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt} requiredFontWeight">
                        近12个月支出额:
                    </th>
                    <td><input class="easyui-numberbox input"
                                           id="first_customerInfo_assetsInfo_masterLoanInfoA_lastYearPayAmt"
                                           value="${applyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt}"
                                           name="lastYearPayAmt"></td>
                    <th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt} requiredFontWeight">
                        淘气值:
                    </th>
                    <td><input class="easyui-numberbox input"
                               id="first_customerInfo_assetsInfo_masterLoanInfoA_naughtyValue"
                               value="${applyInfo.assetsInfoVO.masterLoanInfoVO.naughtyValue}"
                               name="naughtyValue"></td>
                </tr>
            </table>
        </form>
    </div>

    <%--淘宝商户贷--%>
    <div class="easyui-panel" title="淘宝商户贷" id="first_customer_MERCHANTLOAN_div" ms-attr="{'data-options': assetsPanelShow(applyInfo.assetsInfoVO.merchantLoanInfoVO, 'MERCHANTLOAN') }" >
        <form id="first_customerMerchant_form" name="MERCHANTLOAN">
            <table class="table_ui W100" id="first_customerInfo_merchant_table">
                <tr>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate} requiredFontWeight">
                        开店时间:
                    </th>
                    <td width="20%">
                        <input class="input" id="first_customerInfo_date_MERCHANTLOAN" name="setupShopDate">
                    </td>
                    <th width="12%" class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel} requiredFontWeight">卖家信用等级:</th>
                    <td width="20%">
                        <input class="easyui-combobox input" id="first_customerInfo_date_SELLERCREDITLEVEL" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}" name="sellerCreditLevel"/>
                    </td>
                    <th width="12%"
                        class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType} requiredFontWeight">
                        卖家信用类型:
                    </th>
                    <td><input class="easyui-combobox input" id="first_customerInfo_date_SELLERCREDITTYPE"
                               value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType}"
                               name="sellerCreditType"/></td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum} requiredFontWeight">近半年好评数:
                    </th>
                    <td colspan="5"><input class="easyui-numberbox input" id="first_customerInfo_date_REGARDEDNUM"
                                           value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum}"
                                           name="regardedNum"></td>
                </tr>
                <tr class="cardLoanRecordTr">
                    <th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1} ${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2} ${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3} ${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4} ${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5} ${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}requiredFontWeight">近6个月账单金额依次为:</th>
                    <td colspan="5">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1}" id="merchantBillAmt1" name="billAmt1">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2}" id="merchantBillAmt2" name="billAmt2">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3}" id="merchantBillAmt3" name="billAmt3">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4}" id="merchantBillAmt4" name="billAmt4">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5}" id="merchantBillAmt5" name="billAmt5">
                        <input class="easyui-numberbox input" groupSeparator="," value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}" id="merchantBillAmt6" name="billAmt6">
                    </td>
                </tr>
                <tr>
                    <th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}">月均:</th>
                    <td colspan="5" class="numFormat">
                        <fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}" pattern="0" maxFractionDigits="0"/> 元
                    </td>
                </tr>
                <div>
                    <input class=" input" id="first_customerInfo_merchantLoan_payMonthAmt" type="hidden" value="${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}" name="payMonthAmt">
                </div>
            </table>
        </form>
    </div>
</div>