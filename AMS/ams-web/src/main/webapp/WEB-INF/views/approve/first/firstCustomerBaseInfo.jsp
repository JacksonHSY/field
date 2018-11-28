<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="easyui-panel" title="个人信息">
    <form id="first_customerInfo_form">
        <table class="table_ui W100">
            <tr>
                <th width="14%" class="${oldapplyInfo.applyInfoVO.name}">申请人姓名:</th>
                <td width="18%" class="customerBaseInfo_name">{{ applyInfo.applyInfoVO.name }}</td>
                <th width="14%" class="${oldapplyInfo.applyInfoVO.idNo}">身份证号码:<input id="applicant_id" type="hidden"
                                                                                      ms-duplex="applyInfo.applyInfoVO.idNo">
                </th>
                <td width="18%">{{ applyInfo.applyInfoVO.idNo }}</td>
                <th width="14%" class="${oldapplyInfo.basicInfoVO.personInfoVO.gender}">性别:</th>
                <td>{{ applyInfo.basicInfoVO.personInfoVO.gender }}<input type="hidden" id="first_customer_gender"
                                                                          value="${applyInfo.basicInfoVO.personInfoVO.gender}">
                </td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.age}">年龄:</th>
                <td>${applyInfo.basicInfoVO.personInfoVO.age}<input type="hidden" id="first_customer_age"
                                                                    value="${applyInfo.basicInfoVO.personInfoVO.age}">
                </td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.maritalStatus} requiredFontWeight">婚姻状况:</th>
                <td><input class="input" id="first_Customer_Info_maritalStatus"
                           value="${applyInfo.basicInfoVO.personInfoVO.maritalStatus}" name="maritalStatus"/></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.childrenNum}">子女数:</th>
                <td><input type="text" id="first_Customer_Info_childrenNum" class="easyui-numberspinner input"
                           value="${applyInfo.basicInfoVO.personInfoVO.childrenNum}" name="childrenNum"></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.qualification} requiredFontWeight">最高学历:</th>
                <td><input class="input" id="first_Customer_Info_qualification" name="qualification"/></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.graduationDate}">毕业时间:</th>
                <td><input class="input" id="first_Customer_Info_graduationDate" name="graduationDate"></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerZoneId} ${oldapplyInfo.basicInfoVO.personInfoVO.idIssuerAddress} requiredFontWeight">户籍地址:</th>
                <td>
                    <input id="firstCustomerInfo_birthplace_province_combobox" class="input idIssuerAddress" value="${applyInfo.basicInfoVO.personInfoVO.issuerStateId}" name="issuerStateId"/>
                    <input class="region" name="issuerState" type="hidden"></td>
                <td>
                    <input id="firstCustomerInfo_birthplace_city_combobox" class="input idIssuerAddress" value="${applyInfo.basicInfoVO.personInfoVO.issuerCityId}" name="issuerCityId"/>
                    <input class="region" name="issuerCity" type="hidden">
                </td>
                <td>
                    <input id="firstCustomerInfo_birthplace_country_combobox" class="input idIssuerAddress" value="${applyInfo.basicInfoVO.personInfoVO.issuerZoneId}" name="issuerZoneId"/>
                    <input class="region" name="issuerZone" type="hidden">
                </td>
                <td colspan="2">
                    <input class="idIssuerAddress" id="firstCustomerInfo_birthplace_idIssuerAddress"
                           name="idIssuerAddress" value="${applyInfo.basicInfoVO.personInfoVO.idIssuerAddress}" style="height: 30px">
                </td>
                <input type="hidden" id="birthplaceAddress" name="birthplaceAddress" ms-duplex="birthAddress">
            </tr>
            <tr>
                <th  class="${oldapplyInfo.basicInfoVO.personInfoVO.homeSameRegistered} requiredFontWeight">家庭地址是否同户籍地址:</th>
                <td>是<input type="radio" name="homeSameRegistered" value="0"
                            <c:if test="${applyInfo.basicInfoVO.personInfoVO.homeSameRegistered==0}">checked="checked"</c:if> />
                    否<input type="radio" name="homeSameRegistered" value="1"
                            <c:if test="${applyInfo.basicInfoVO.personInfoVO.homeSameRegistered==1}">checked="checked"</c:if> />
                </td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.homePostcode}">住宅邮编:</th>
                <td colspan="3"><input class="input "
                                       id="firstCustomerInfo_basicInfo_personInfo_homePostcode"
                                       value="${applyInfo.basicInfoVO.personInfoVO.homePostcode}" name="homePostcode">
                </td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.homeZoneId} ${oldapplyInfo.basicInfoVO.personInfoVO.homeAddress} requiredFontWeight">家庭地址:</th>
                <td><input id="firstCustomerInfo_family_province_combobox" class="input homeAddress"
                           value="${applyInfo.basicInfoVO.personInfoVO.homeStateId}" name="homeStateId"><input
                        class="region" name="homeState" type="hidden"></td>
                <td><input id="firstCustomerInfo_family_city_combobox" class="input homeAddress"
                           value="${applyInfo.basicInfoVO.personInfoVO.homeCityId}" name="homeCityId"><input
                        class="region" name="homeCity" type="hidden"></td>
                <td><input id="firstCustomerInfo_family_country_combobox" class="input homeAddress"
                           value="${applyInfo.basicInfoVO.personInfoVO.homeZoneId}" name="homeZoneId"><input
                        class="region" name="homeZone" type="hidden"></td>
                <td colspan="2"><input id="firstCustomerInfo_family_addr" class="homeAddress"  name="homeAddress"
                                       value="${applyInfo.basicInfoVO.personInfoVO.homeAddress}" style="height: 30px">
                </td>
                <input type="hidden" id="familyAddress" name="familyAddress" ms-duplex="familyAddress">
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.houseType} requiredFontWeight">住宅类型:</th>
                <td><input class="input" id="first_customerInfo_houseType"
                           value="${applyInfo.basicInfoVO.personInfoVO.houseType}" name="houseType"/></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.houseRent}">租金/元:</th>
                <td colspan="2"><input class="input" id="first_customerInfo_houseRent"
                                       value="${applyInfo.basicInfoVO.personInfoVO.houseRent}" name="houseRent"></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.familyMonthPay} requiredFontWeight">每月家庭支出:</th>
                <td><input class="input" id="first_customerInfo_familyMonthPay"
                           value="${applyInfo.basicInfoVO.personInfoVO.familyMonthPay}" name="familyMonthPay"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.monthMaxRepay} requiredFontWeight">可接受的月最高还款:</th>
                <td><input class="input" id="first_customerInfo_monthMaxRepay"
                           value="${applyInfo.basicInfoVO.personInfoVO.monthMaxRepay}" name="monthMaxRepay"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerPostcode}">户籍邮编:</th>
                <td><input class="input" id="first_customerInfo_issuerPostcode"
                           value="${applyInfo.basicInfoVO.personInfoVO.issuerPostcode}" name="issuerPostcode"></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.cellphone} requiredFontWeight">常用手机:</th>
                <td><input class="input " id="first_customerInfo_cellphone"
                           value="${applyInfo.basicInfoVO.personInfoVO.cellphone}" name="cellphone"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.cellphoneSec}">备用手机:</th>
                <td><input class="input" id="first_customerInfo_cellphoneSec"
                           value="${applyInfo.basicInfoVO.personInfoVO.cellphoneSec}" name="cellphoneSec"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.homePhone1}">宅电:</th>
                <td><input class="input" id="first_customerInfo_homePhone1"
                           value="${applyInfo.basicInfoVO.personInfoVO.homePhone1}" name="homePhone1"></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.qqNum}">QQ号:</th>
                <td><input id="firstCustomerInfo_customerInfo_qqNum" class="input"
                           value="${applyInfo.basicInfoVO.personInfoVO.qqNum}" name="qqNum"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.weChatNum}">微信号:</th>
                <td><input id="firstCustomerInfo_customerInfo_weChatNum" class="input"
                           value="${applyInfo.basicInfoVO.personInfoVO.weChatNum}" name="weChatNum"></td>
                <th class="${oldapplyInfo.basicInfoVO.personInfoVO.email}">电子邮箱:</th>
                <td><input id="firstCustomerInfo_customerInfo_email" class="input"
                           value="${applyInfo.basicInfoVO.personInfoVO.email}" name="email"></td>
            </tr>
        </table>
    </form>
</div>
<div class="easyui-panel" title="工作信息">
    <form id="first_customerWork_form">
        <table class="table_ui W100" id="firstCustomerInfo_customerWork_table">
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpName} requiredFontWeight">单位名称:</th>
                <td colspan="3">
                    <input name="corpName" id="firstCustomerInfo_workInfo_corpName_input" value="${applyInfo.basicInfoVO.workInfoVO.corpName}" style="height: 30px">
                </td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.cusWorkType} requiredFontWeight">客户工作类型:</th>
                <td>
                    <input id="firstCustomerInfo_cusWorkType_combobox" class="input" ms-duplex="applyInfo.basicInfoVO.workInfoVO.cusWorkType" name="cusWorkType">
                </td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpZoneId} ${oldapplyInfo.basicInfoVO.workInfoVO.corpAddress} requiredFontWeight">单位地址:</th>
                <td>
                    <input id="firstCustomerInfo_company_province_combobox" class="input" value="${applyInfo.basicInfoVO.workInfoVO.corpProvinceId}" name="corpProvinceId">
                    <input class="region" name="corpProvince" type="hidden">
                </td>
                <td>
                    <input id="firstCustomerInfo_company_city_combobox" class="input" value="${applyInfo.basicInfoVO.workInfoVO.corpCityId}" name="corpCityId">
                    <input class="region" name="corpCity" type="hidden">
                </td>
                <td>
                    <input id="firstCustomerInfo_company_country_combobox" class="input" value="${applyInfo.basicInfoVO.workInfoVO.corpZoneId}" name="corpZoneId">
                    <input class="region" name="corpZone" type="hidden">
                </td>
                <td colspan="2">
                    <input id="corpAddress" value="${applyInfo.basicInfoVO.workInfoVO.corpAddress }" name="corpAddress" style="height: 30px">
                </td>
            </tr>
            <tr>
                <th width="12.3%" class="${oldapplyInfo.basicInfoVO.workInfoVO.businessNetWork} requiredFontWeight">
                    工商网信息:
                </th>
                <td width="18%"><input class="input" id="firstCustomer_businessNetWork_combobox"
                                       value="${applyInfo.basicInfoVO.workInfoVO.businessNetWork}"
                                       name="businessNetWork"/></td>
                <th width="14%" class="${oldapplyInfo.basicInfoVO.workInfoVO.corpStructure} requiredFontWeight">单位性质:
                </th>
                <td width="18%"><input id="firstCustomer_corpStructure_combobox" class="input"
                                       value="${applyInfo.basicInfoVO.workInfoVO.corpStructure}" name="corpStructure"/>
                </td>
                <th width="13%" class="${oldapplyInfo.basicInfoVO.workInfoVO.occupation} requiredFontWeight">职业:</th>
                <td><input class="input" id="firstCustomer_occupation_combobox"
                           value="${applyInfo.basicInfoVO.workInfoVO.occupation}" name="occupation"/></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpDepapment}">任职部门:</th>
                <td><input class="input" id="corpDepapment"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpDepapment}" name="corpDepapment"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPost} requiredFontWeight">职务:</th>
                <td><input id="firstCustomer_corpPost_combobox" class="input"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpPost}" name="corpPost"/></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpType} requiredFontWeight">单位行业类别:</th>
                <td><input class="input" id="corpType"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpType}" name="corpType"/></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpStandFrom} requiredFontWeight">入职时间:</th>
                <td><input class="input" id="corpStandFrom" name="corpStandFrom"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPayWay} requiredFontWeight">发薪方式:</th>
                <td><input class="input" id="corpPayWay"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpPayWay}" name="corpPayWay"/></td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPhone} requiredFontWeight">单电:</th>
                <td><input class="input" id="corpPhone"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpPhone}" name="corpPhone"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPhoneSec}">单电2:</th>
                <td><input class="input" id="corpPhoneSec"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpPhoneSec}" name="corpPhoneSec"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPostcode}">单位邮编:</th>
                <td><input class="input" id="corpPostcode"
                           value="${applyInfo.basicInfoVO.workInfoVO.corpPostcode}" name="corpPostcode"></td>
            </tr>
            <tr class="totalMonthSalaryTr">
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.monthSalary} requiredFontWeight">单位月收入:</th>
                <td><input class="input" id="monthSalary" name="monthSalary" value="${applyInfo.basicInfoVO.workInfoVO.monthSalary}"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.otherIncome}">其他月收入:</th>
                <td><input class="input" id="otherIncome" name="otherIncome"></td>
                <th class="${oldapplyInfo.basicInfoVO.workInfoVO.totalMonthSalary} requiredFontWeight">月总收入:</th>
                <td>
                    <input class="input" type="hidden" id="firstCustomerInfo_customerWork_totalMonthSalary" name="totalMonthSalary" value="${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}">
                    <span id="firstCustomerInfo_customerWork_totalMonthSalary_span" class="numFormat">
                        <fmt:formatNumber type="number" value="${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}" pattern="0" maxFractionDigits="0"/>
                    </span>元
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 客户为自雇人士和私营业主时，显示私营业主信息 -->
<div id="first_customerPrivateOwner_div" class="easyui-panel" ms-visible="showPrivate" ms-attr="{title: showPrivate ? '私营业主信息' : ''}">
    <form id="first_customerPrivateOwner_form">
        <table class="table_ui W100">
            <tr>
                <th width="12%" class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.setupDate} requiredFontWeight">成立时间:</th>
                <td width="18%">
                    <input id="first_customerPrivateOwner_setupDate" class="input" name="setupDate">
                </td>
                <th width="16.5%" class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale} requiredFontWeight">占股比例:</th>
                <td width="18%">
                    <input class="input" id="sharesScale" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale}" name="sharesScale">
                </td>
                <th width="13%" class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds} requiredFontWeight">注册资本:</th>
                <td>
                    <input class="input" id="registerFunds" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds}" name="registerFunds">
                </td>
            </tr>
            <tr>
                <th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType} requiredFontWeight">私营企业类型:</th>
                <td>
                    <input id="firstCustomerInfo_priEnterpriseType_input" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}" type="hidden">
                    <input class="input" id="first_customerInfo_priEnterpriseType" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}" name="priEnterpriseType"/>
                </td>
                <th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt} requiredFontWeight">每月净利润额:</th>
                <td colspan="3">
                    <input class="input" id="monthAmt" value="${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}" name="monthAmt">
                </td>
            </tr>
        </table>
    </form>
</div>