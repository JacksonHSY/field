<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="easyui-panel" title="个人信息">
    <form id="money_customerInfo_form">
        <table class="table_ui W100">
            <tr>
                <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.name} requiredFontWeight">申请人姓名:</th>
                <td width="18%" class="customerBaseInfo_name">{{applyInfo.applicantInfo.personalInfo.name}}</td>
                <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.idNo} requiredFontWeight">身份证号码:</th>
                <td width="18%">{{applyInfo.applicantInfo.personalInfo.idNo}}</td>
                <th width="14%" class="${oldApplyInfo.applicantInfo.personalInfo.gender} requiredFontWeight">性别:</th>
                <td>{{ applyInfo.applicantInfo.personalInfo.gender }}</td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.age} requiredFontWeight">年龄:</th>
                <td>${applyInfo.applicantInfo.personalInfo.age}</td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.maritalStatus} requiredFontWeight">婚姻状况:</th>
                <td><input class="input" id="money_Customer_Info_maritalStatus" value="${applyInfo.applicantInfo.personalInfo.maritalStatus}" name="maritalStatus"/></td>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.qualification} requiredFontWeight">最高学历:</th>
                <td><input class="input" id="money_Customer_Info_qualification" name="qualification"/></td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.monthMaxRepay} requiredFontWeight">可接受的月最高还款:</th>
                <td><input class="input" id="money_customerInfo_monthMaxRepay" value="${applyInfo.applicantInfo.personalInfo.monthMaxRepay}" name="monthMaxRepay"></td>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.issuerZoneId} ${oldApplyInfo.applicantInfo.personalInfo.idIssuerAddress} requiredFontWeight">户籍地址:</th>
                <td>
                    <input id="moneyCustomerInfo_birthplace_province_combobox" class="input idIssuerAddress" value="${applyInfo.applicantInfo.personalInfo.issuerStateId}" name="issuerStateId"/>
                    <input class="region" name="issuerState" type="hidden"></td>
                <td>
                    <input id="moneyCustomerInfo_birthplace_city_combobox" class="input idIssuerAddress" value="${applyInfo.applicantInfo.personalInfo.issuerCityId}" name="issuerCityId"/>
                    <input class="region" name="issuerCity" type="hidden">
                </td>
                <td>
                    <input id="moneyCustomerInfo_birthplace_country_combobox" class="input idIssuerAddress" value="${applyInfo.applicantInfo.personalInfo.issuerZoneId}" name="issuerZoneId"/>
                    <input class="region" name="issuerZone" type="hidden">
                </td>
                <td colspan="2">
                    <input class="idIssuerAddress" id="moneyCustomerInfo_birthplace_idIssuerAddress" name="idIssuerAddress" value="${applyInfo.applicantInfo.personalInfo.idIssuerAddress}" class="input">
                </td>
                <input type="hidden" id="birthplaceAddress" name="birthplaceAddress" ms-duplex="birthAddress">
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.homeSameRegistered} requiredFontWeight">住宅地址是否同户籍地址:</th>
                <td>是<input type="radio" name="homeSameRegistered" value="0"
                            <c:if test="${applyInfo.applicantInfo.personalInfo.homeSameRegistered==0}">checked="checked"</c:if> />
                    否<input type="radio" name="homeSameRegistered" value="1"
                            <c:if test="${applyInfo.applicantInfo.personalInfo.homeSameRegistered==1}">checked="checked"</c:if> />
                </td>
                <th>&nbsp;</th>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.homeZoneId} ${oldApplyInfo.applicantInfo.personalInfo.homeAddress} requiredFontWeight">住宅地址:</th>
                <td><input id="moneyCustomerInfo_family_province_combobox" class="input homeAddress" value="${applyInfo.applicantInfo.personalInfo.homeStateId}" name="homeStateId"><input class="region" name="homeState" type="hidden"></td>
                <td><input id="moneyCustomerInfo_family_city_combobox" class="input homeAddress" value="${applyInfo.applicantInfo.personalInfo.homeCityId}" name="homeCityId"><input class="region" name="homeCity" type="hidden"></td>
                <td><input id="moneyCustomerInfo_family_country_combobox" class="input homeAddress" value="${applyInfo.applicantInfo.personalInfo.homeZoneId}" name="homeZoneId"><input class="region" name="homeZone" type="hidden"></td>
                <td colspan="2"><input id="moneyCustomerInfo_family_addr" class="homeAddress" name="homeAddress" value="${applyInfo.applicantInfo.personalInfo.homeAddress}"class="input"></td>
                <input type="hidden" id="familyAddress" name="familyAddress" ms-duplex="familyAddress">
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.cellphone} requiredFontWeight">常用手机:</th>
                <td><input class="input " id="money_customerInfo_cellphone" value="${applyInfo.applicantInfo.personalInfo.cellphone}" name="cellphone"></td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.cellphoneSec}">备用手机:</th>
                <td><input class="input" id="money_customerInfo_cellphoneSec" value="${applyInfo.applicantInfo.personalInfo.cellphoneSec}" name="cellphoneSec"></td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.homePhone1}">家庭电话:</th>
                <td><input class="input" id="money_customerInfo_homePhone1" value="${applyInfo.applicantInfo.personalInfo.homePhone1}" name="homePhone1"></td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.qqNum}">QQ号:</th>
                <td><input id="moneyCustomerInfo_customerInfo_qqNum" class="input" value="${applyInfo.applicantInfo.personalInfo.qqNum}" name="qqNum"></td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.weChatNum}">微信号:</th>
                <td><input id="moneyCustomerInfo_customerInfo_weChatNum" class="input" value="${applyInfo.applicantInfo.personalInfo.weChatNum}" name="weChatNum"></td>
                <th class="${oldApplyInfo.applicantInfo.personalInfo.email} requiredFontWeight">电子邮箱:</th>
                <td><input id="moneyCustomerInfo_customerInfo_email" class="input" value="${applyInfo.applicantInfo.personalInfo.email}" name="email"></td>
            </tr>
        </table>
    </form>
</div>
<div class="easyui-panel" title="工作信息">
    <form id="money_customerWork_form">
        <table class="table_ui W100" id="moneyCustomerInfo_customerWork_table">
            <tr>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpName} requiredFontWeight">单位名称:</th>
                <td colspan="5">
                    <input name="corpName" id="moneyCustomerInfo_workInfo_corpName_input" value="${applyInfo.applicantInfo.workInfo.corpName}"  class="input">
                </td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpZoneId} ${oldApplyInfo.applicantInfo.workInfo.corpAddress} requiredFontWeight">单位地址:</th>
                <td>
                    <input id="moneyCustomerInfo_company_province_combobox" class="input" value="${applyInfo.applicantInfo.workInfo.corpProvinceId}" name="corpProvinceId">
                    <input class="region" name="corpProvince" type="hidden">
                </td>
                <td>
                    <input id="moneyCustomerInfo_company_city_combobox" class="input" value="${applyInfo.applicantInfo.workInfo.corpCityId}" name="corpCityId">
                    <input class="region" name="corpCity" type="hidden">
                </td>
                <td>
                    <input id="moneyCustomerInfo_company_country_combobox" class="input" value="${applyInfo.applicantInfo.workInfo.corpZoneId}" name="corpZoneId">
                    <input class="region" name="corpZone" type="hidden">
                </td>
                <td colspan="2">
                    <input id="corpAddress" value="${applyInfo.applicantInfo.workInfo.corpAddress }" name="corpAddress" class="input">
                </td>
            </tr>
            <tr>
                <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.corpStructure} requiredFontWeight">单位性质:</th>
                <td width="18%"><input id="moneyCustomer_corpStructure_combobox" class="input" value="${applyInfo.applicantInfo.workInfo.corpStructure}" name="corpStructure"/></td>
                <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.occupation} requiredFontWeight">职业类型:</th>
                <td  width="18%"><input class="input" id="moneyCustomer_occupation_combobox" value="${applyInfo.applicantInfo.workInfo.occupation}" name="occupation"/></td>
                <th width="14%" class="${oldApplyInfo.applicantInfo.workInfo.cusWorkType} requiredFontWeight">客户工作类型</th>
                <td> <input id="moneyCustomerInfo_cusWorkType_combobox" class="input" ms-duplex="applyInfo.applicantInfo.workInfo.cusWorkType" name="cusWorkType"></td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpDepapment} requiredFontWeight">任职部门:</th>
                <td><input class="input" id="money_corpDepapment" value="${applyInfo.applicantInfo.workInfo.corpDepapment}" name="corpDepapment"></td>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpPost} requiredFontWeight">职务:</th>
                <td><input id="moneyCustomer_corpPost_combobox" class="input" value="${applyInfo.applicantInfo.workInfo.corpPost}" name="corpPost"/></td>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpType} requiredFontWeight">行业类别:</th>
                <td><input class="input" id="money_corpType" value="${applyInfo.applicantInfo.workInfo.corpType}" name="corpType"/></td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpStandFrom} requiredFontWeight">入职时间:</th>
                <td><input class="input" id="money_corpStandFrom" name="corpStandFrom"></td>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpPayWay} requiredFontWeight">发薪方式:</th>
                <td><input class="input" id="money_corpPayWay" value="${applyInfo.applicantInfo.workInfo.corpPayWay}" name="corpPayWay"/></td>
                <th></th>
                <td></td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpPhone} requiredFontWeight">单位电话:</th>
                <td><input class="input" id="corpPhone" value="${applyInfo.applicantInfo.workInfo.corpPhone}" name="corpPhone"></td>
                <th class="${oldApplyInfo.applicantInfo.workInfo.corpPhoneSec}">单位电话2:</th>
                <td><input class="input" id="corpPhoneSec" value="${applyInfo.applicantInfo.workInfo.corpPhoneSec}" name="corpPhoneSec"></td>
                <th class="${oldApplyInfo.applicantInfo.workInfo.totalMonthSalary} requiredFontWeight">税前月总收入:</th>
                <td><input id="money_monthCountPay" class="input" name="totalMonthSalary" value="${applyInfo.applicantInfo.workInfo.totalMonthSalary}"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 客户为自雇人士和私营业主时，显示私营业主信息 -->
<div id="money_customerPrivateOwner_div" class="easyui-panel" ms-visible="showPrivate" ms-attr="{title: showPrivate ? '私营业主信息' : ''}">
    <form id="money_customerPrivateOwner_form">
        <table class="table_ui W100">
            <tr>
                <th width="12%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.setupDate} requiredFontWeight">成立时间:</th>
                <td width="18%"><input id="money_customerPrivateOwner_setupDate" class="input" name="setupDate"></td>
                <th width="16.5%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.sharesScale} requiredFontWeight">占股比例:</th>
                <td width="18%"><input class="input" id="money_sharesScale" value="${applyInfo.applicantInfo.privateOwnerInfo.sharesScale}" name="sharesScale"></td>
                <th width="13%" class="${oldApplyInfo.applicantInfo.privateOwnerInfo.registerFunds} requiredFontWeight">注册资本:</th>
                <td><input class="input" id="money_registerFunds" value="${applyInfo.applicantInfo.privateOwnerInfo.registerFunds}" name="registerFunds"></td>
            </tr>
            <tr>
                <th class="${oldApplyInfo.applicantInfo.privateOwnerInfo.priEnterpriseType} requiredFontWeight">私营企业类型:</th>
                <td><input class="input" id="money_customerInfo_priEnterpriseType" value="${applyInfo.applicantInfo.privateOwnerInfo.priEnterpriseType}" name="priEnterpriseType"/></td>
                <th class="${oldApplyInfo.applicantInfo.privateOwnerInfo.monthAmt} requiredFontWeight">每月净利润额:</th>
                <td colspan="3">
                    <input class="input" id="money_monthAmt" value="${applyInfo.applicantInfo.privateOwnerInfo.monthAmt}" name="monthAmt">
                </td>
            </tr>
        </table>
    </form>
</div>