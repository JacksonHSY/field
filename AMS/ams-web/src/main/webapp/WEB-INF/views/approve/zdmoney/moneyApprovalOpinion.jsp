<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${applyBasiceInfo.name}-审批意见</title>
    <jsp:include page="../../common/commonJS.jsp"></jsp:include>
    <jsp:include page="../../common/theme.jsp"></jsp:include>
    <style type="text/css">
        #money_approval_assetsInfo_div .table_list {
            margin: 5px 0px;
        }
    </style>
</head>
<body class="padding_20 ms-controller" ms-controller="page">
<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; position: fixed; left: 130px; top: 7px;">消息提醒</div>
<div class="xx_dd_yuan" style="left: 113px; position: fixed;" id="ruleEngineHint_number_div">0</div>
<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="left: 110px; position: fixed;">
    <div class="xx_dd_tit">消息提醒</div>
    <ul></ul>
</div>
<div style="height: 120px;">
    <div id="moneyApprovalOpinion_approveInfo_div" style="margin-top: 0px; width: 100%; border-bottom: 2px solid #95b8e7;">
        <div class="easyui-panel" title="申请信息">
            <table class="table_ui W100">
                <tr>
                    <th>申请人姓名:</th>
                    <td>${applyInfo.baseInfo.name}</td>
                    <th>身份证号码:</th>
                    <td colspan="3">${applyInfo.baseInfo.idNo }</td>
                </tr>
                <tr>
                    <th width="12%">申请产品:</th>
                    <td width="20%"><c:if test="${'证大前前'!=applyInfo.baseInfo.initProductName}">${ applyInfo.baseInfo.initProductName }</c:if></td>
                    <th width="12%">申请期限:</th>
                    <td width="20%">{{ applyInfo.baseInfo.applyTerm }}</td>
                    <th width="12%">申请额度:</th>
                    <td class="numFormat">{{ applyInfo.baseInfo.applyLmt }}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
<form id="moneyApprovalOpinion_approveCheckData_form">
    <input name="version" type="hidden" ms-duplex="applyInfo.version">
    <input id="loanNo" type="hidden" ms-duplex="loanNo">
    <c:if test="${empty approvalInfo.approvalHistoryList}">
        <!----------------------------------------- Start 审核记录为空--------------------------------------->
        <div class="easyui-panel" title="资料核对">
            <h3>工作信息</h3>
            <table class="table_ui W100">
                <tr>
                    <th width="12%">可接受的月最高还款/元:</th>
                    <td width="20%" class="numFormat">${applyInfo.applicantInfo.personalInfo.monthMaxRepay}</td>
                    <th width="12%">客户工作类型:</th>
                    <td width="20%">${ applyInfo.applicantInfo.workInfo.cusWorkType}</td>
                    <th width="12%">发薪方式:</th>
                    <td colspan="2">${applyInfo.applicantInfo.workInfo.corpPayWay}</td>
                </tr>
                <tr>
                    <th>税前月总收入:</th>
                    <td class="numFormat">${ applyInfo.applicantInfo.workInfo.totalMonthSalary}</td>
                    <th></th>
                    <td></td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th>收入证明金额:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" name="incomeCertificate"
                               data-options="groupSeparator:',',precision:0,buttonText:'元',panelHeight:'auto',validType:['compareMaxMin[0,99999999]']">
                    </td>
                    <th>有无周末发薪:</th>
                    <td>
                        <input type="radio" id="money_weekendTrue_radioFor" name="weekendPay" value="0"><label for="money_weekendTrue_radioFor">&nbsp;有</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="money_weekendFalse_radioFor" name="weekendPay" value="1"><label for="money_weekendFalse_radioFor">&nbsp;无</label>
                    </td>
                    <th></th>
                    <td></td>
                </tr>
            </table>
            <table class="table_ui W100" id="moneyApprovalOpinion_record_table">
                <tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
                    <th width="12%">个人流水{{ $index != 0 ? $index : '' }}:</th>
                    <td width="52%">
                        <input type="text" name="personalWater1" ms-attr="{value: checkStatement.water1 || ''}" class="input">&nbsp;
                        <input type="text" name="personalWater2" ms-attr="{value: checkStatement.water2 || ''}" class="input">&nbsp;
                        <input type="text" name="personalWater3" ms-attr="{value: checkStatement.water3 || ''}" class="input">
                    </td>
                    <th width="12%">个人流水月均:</th>
                    <td class="W10 numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) || ''}}元</td>
                    <td>
                        <a href="javaScript:void(0);" ms-on-click="addCheckStatement('C')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)" ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>
                        个人流水月均合计:
                        <input type="hidden" name="personalWaterTotal" ms-attr="{value: personalWaterAverageTotal }">
                    </th>
                    <td class="personalRecordCount">{{ personalWaterAverageTotal || ''}}元</td>
                </tr>
                <tr class="publicRecordTr" ms-for="($index, checkStatement) in publicCheckStatementArray">
                    <th>对公流水{{ $index != 0 ? $index : '' }}:</th>
                    <td>
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater1', value: checkStatement.water1 || ''}">&nbsp;
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater2', value: checkStatement.water2 || ''}">&nbsp;
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater3', value: checkStatement.water3 || ''}">
                    </td>
                    <th>对公流水月均:</th>
                    <td class="W10 numFormat">{{ calcWaterAverage(checkStatement.water1, checkStatement.water2, checkStatement.water3) || ''}}元</td>
                    <td>
                        <a href="javaScript:void(0);" ms-on-click="addCheckStatement('B')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)" ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>
                        对公流水月均合计:
                        <input type="hidden" name="publicWaterTotal" ms-attr="{value: publicWaterAverageTotal}">
                    </th>
                    <td class="publicRecordCount numFormat">{{ publicWaterAverageTotal || ''}}元</td>
                </tr>
                <tr>
                    <th>月均流水合计:</th>
                    <td class="recordCount numFormat">{{ waterIncomeTotal || ''}}元</td>
                </tr>
            </table>
            <h3>其他核实信息</h3>
            <table class="table_ui W100">
                <tr>
                    <th width="12%">法院网核查无异常:</th>
                    <td width="20%">
                        <input type="checkbox" id="money_courtException_input"/>
                    </td>
                    <th width="12%">内部匹配无异常:</th>
                    <td><input type="checkbox" id="money_internalException_input"/></td>
                    <th class="requiredFontWeight">征信平台已验证:</th>
                    <td><input type="checkbox" id="money_creditCheckException_checkbox" ms-duplex-checked="applyBasicInfo.reportId > 0"></td>
                </tr>
                <tr>
                    <th class="requiredFontWeight">有信用记录:</th><!-- 注意:解读出来的报告Type:是否符合无综合信用条件 YES NO 这里NO标示有 YES是无 -->
                    <td>
                        <input type="checkbox" ms-attr="{id: 'money_creditRecord_checkbox'}" ms-duplex-checked="ifCreditRecord == 1">
                    </td>
                    <th class="requiredFontWeight">人行近1个月查询:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" ms-attr="{id: 'moneyApprovalOpinion_oneMonthsCount', name: 'oneMonthsCount'}"
                               ms-duplex="approvalInfo.approveCheckInfo.oneMonthsCount"
                               data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999]']">
                    </td>
                    <th class="requiredFontWeight">人行近3个月查询:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" ms-attr="{id:'moneyApprovalOpinion_threeMonthsCount',name: 'threeMonthsCount'}"
                               ms-duplex="approvalInfo.approveCheckInfo.threeMonthsCount"
                               data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999999]','compareNum[\'#moneyApprovalOpinion_oneMonthsCount\']']">
                    </td>
                </tr>
                <tr>
                    <th>社保信息已验证:</th>
                    <td><input id="money_socialInsuranceInfo_checkbox" type="checkbox"></td>
                    <th>公积金信息已验证:</th>
                    <td><input id="money_fundInfo_checkbox" type="checkbox"></td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th>备注:</th>
                    <td colspan="5">
                        <input class="easyui-textbox" name="memo" ms-duplex="approvalInfo.approveCheckInfo.memo || ''"
                               data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
                    </td>
                </tr>
            </table>
        </div>
        <div class="easyui-panel" title="负债信息">
            <a class="easyui-linkbutton" onclick="moneyApprovalOpinionContrastDialog()">征信初判</a>
            <table class="table_list W100" id="moneyApprovalOpinion_liabilities_table">
                <tr>
                    <td class="specialThCol">信用卡总额度/元:</td>
                    <td>
                        <input type="text" ms-attr="{name: 'creditTotalLimit', value: reportInfo.creditLimitMoney ? reportInfo.creditLimitMoney : 0}" class="easyui-numberbox input"
                               data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],precision:0,buttonText:'元'">
                    </td>
                    <th class="specialThCol">信用卡已用额度/元</th>
                    <td>
                        <input type="text" ms-attr="{name: 'creditUsedLimit', value: reportInfo.alreadyUseMoney || 0}" class="easyui-numberbox input"
                               data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
                    </td>
                    <td class="specialThCol">信用卡负债/元<input name="creditDebt" ms-attr="{value: reportInfo.debt || 0}" type="hidden"></td>
                    <td id="money_liabilitiesCreditCard" class="W10 numFormat">{{ reportInfo.debt ? reportInfo.debt : 0 }}</td>
                </tr>
                <tr class="specialTrCol">
                    <td class="specialThCol">信用贷款</td>
                    <td class="specialThCol">额度/元</td>
                    <td class="specialThCol">期限/月</td>
                    <td class="specialThCol">负债/元</td>
                    <td class="specialThCol">对应关系</td>
                    <td class="specialThCol">操作</td>
                </tr>
                <!--  Start 判断是否是追加贷款 -->
                <!-- TODO 暂时取消topup -->
                <c:if test="${'TOPUP' == applyBasiceInfo.applyType }">
                    <tr class="topUpTr">
                        <td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" value="${topupLoan.money}" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" value="${topupLoan.time}" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" value="${topupLoan.returneterm}" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td></td>
                    </tr>
                </c:if>
                <!--  End 判断是否是追加贷款 -->
                <!--  Start 判断贷款不为空 -->
                <c:if test="${not empty loanLimitInfo }">
                    <c:forEach items="${loanLimitInfo.loan}" var="item" varStatus="itemStatr">
                        <tr class="creditCardTr">
                            <td>信用贷款<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                            <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                            <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                            <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                            <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>信用贷款<span>${fn:length(loanLimitInfo.loan)+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr>
                        <td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
                        <td class="creditCountLiabilities" colspan="5">0</td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">房贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${loanLimitInfo.house}" var="item" varStatus="itemStatr">
                        <tr class="creditCardTr">
                            <td>房贷<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                            <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                            <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                            <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                            <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>房贷<span>${fn:length(loanLimitInfo.house)+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">车贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${loanLimitInfo.car}" var="item" varStatus="itemStatr">
                        <tr class="creditCardTr">
                            <td>车贷<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                            <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                            <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                            <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                            <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>车贷<span>${fn:length(loanLimitInfo.car)+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">其他贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${loanLimitInfo.other}" var="item" varStatus="itemStatr">
                        <tr class="creditCardTr">
                            <td>其他贷款<span>${itemStatr.index+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                            <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                            <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                            <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                            <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>其他贷款<span>${fn:length(loanLimitInfo.other)+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                </c:if>
                <!--  End 判断贷款不为空 -->
                <!--  Start 判断贷款为空 -->
                <c:if test="${empty loanLimitInfo }">
                    <tr class="creditCardTr">
                        <td>信用贷款<span>1</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr>
                        <td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
                        <td class="creditCountLiabilities" colspan="5">0</td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">房贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <tr class="creditCardTr">
                        <td>房贷<span>1</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">车贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <tr class="creditCardTr">
                        <td>车贷<span>1</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">其他贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <tr class="creditCardTr">
                        <td>其他贷款<span>1</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                </c:if>
                <tr>
                    <td class="specialThCol">外部负债总额</td>
                    <td>
                        <input id="outDebtTotal_id" name="outDebtTotal" value="0" class="easyui-numberbox input"
                               data-options="groupSeparator:',',required:true,min:0,max:99999999,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
                    </td>
                    <td class="specialThCol" align="right">速贷还款情况<input id="money_ifBlackRoster" type="hidden" name="ifBlackRoster"></td>
                    <td colspan="3">
                        <select ms-attr="{id: 'money_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
                                data-options="required:true,panelHeight:'auto',editable:false,value:''">
                            <option value="NORMAL">正常</option>
                            <option value="OVERDUE">逾期</option>
                            <option value="SETTLE">结清</option>
                            <option value="NOLOAN">无贷款</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="specialThCol">备注</td>
                    <td colspan="6">
                        <input class="easyui-textbox" name="memo"
                               data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
                    </td>
                </tr>
                <!--  End 判断贷款为空 -->
            </table>
        </div>
        <!-- ---------------------------------------End 审核记录为空--------------------------------------->
    </c:if>
    <c:if test="${not empty approvalInfo.approvalHistoryList}">
        <!-- ---------------------------------------Start 审核记录不为空--------------------------------------->
        <div class="easyui-panel" title="资料核对">
            <h3>工作信息:</h3>
            <table class="table_ui W100">
                <tr>
                    <th width="12%">可接受的月最高还款/元:</th>
                    <td width="20%" class="numFormat">${applyInfo.applicantInfo.personalInfo.monthMaxRepay}</td>
                    <th width="12%">客户工作类型:</th>
                    <td width="20%">${ applyInfo.applicantInfo.workInfo.cusWorkType}</td>
                    <th width="12%">发薪方式:</th>
                    <td colspan="2">${applyInfo.applicantInfo.workInfo.corpPayWay}</td>
                </tr>
                <tr>
                    <th>税前月总收入:</th>
                    <td class="numFormat">${ applyInfo.applicantInfo.workInfo.totalMonthSalary}</td>
                    <th></th>
                    <td></td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th>收入证明金额:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" ms-attr="{name: 'incomeCertificate', value: applyBasicInfo.amoutIncome > 0 ? applyBasicInfo.amoutIncome : ''}"
                               data-options="groupSeparator:',',precision:0,buttonText:'元',panelHeight:'auto',validType:['compareMaxMin[0,99999999]']"/>
                    </td>
                    <th>有无周末发薪:</th>
                    <td>
                        <input type="radio" ms-attr="{id: 'money_weekendTrue_radioFor',name: 'weekendPay'}" value="0" ms-duplex-checked="approvalInfo.approveCheckInfo.weekendPay == 0">
                        <label for="money_weekendTrue_radioFor">&nbsp;有</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" ms-attr="{id: 'money_weekendFalse_radioFor',name: 'weekendPay'}" value="1" ms-duplex-checked="approvalInfo.approveCheckInfo.weekendPay == 1">
                        <label for="money_weekendFalse_radioFor">&nbsp;无</label>
                    </td>
                    <th></th>
                    <td></td>
                </tr>
            </table>
            <table class="table_ui W100" ms-attr="{id: 'moneyApprovalOpinion_record_table'}">
                <tr class="personalRecordTr" ms-for="($index, checkStatement) in personalCheckStatementArray">
                    <th width="12%">个人流水{{ $index != 0 ? $index : '' }}:</th>
                    <td width="52%">
                        <input type="text" name="personalWater1" ms-attr="{value: defaultIfNull(checkStatement.water1)}" class="input">&nbsp;
                        <input type="text" name="personalWater2" ms-attr="{value: defaultIfNull(checkStatement.water2)}" class="input">&nbsp;
                        <input type="text" name="personalWater3" ms-attr="{value: defaultIfNull(checkStatement.water3)}" class="input">
                    </td>
                    <th width="12%">个人流水月均:</th>
                    <td class="W10 numFormat">{{getRecordCount(checkStatement.water1, checkStatement.water2, checkStatement.water3)}}元</td>
                    <td>
                        <a href="javaScript:void(0);" ms-on-click="addCheckStatement('C')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)" ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>个人流水月均合计:<input type="hidden" name="personalWaterTotal" ms-attr="{value: personalWaterAverageTotal}"></th>
                    <td class="personalRecordCount numFormat" ms-html="getWaterTotal('personalWaterAverageTotal') + '元'"></td>
                </tr>
                <tr class="publicRecordTr" ms-for="($index, checkStatement) in publicCheckStatementArray">
                    <th>对公流水{{ $index != 0 ? $index : '' }}:</th>
                    <td>
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater1', value: defaultIfNull(checkStatement.water1)}">&nbsp;
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater2', value: defaultIfNull(checkStatement.water2)}">&nbsp;
                        <input type="text" class="input" data-options="groupSeparator:','" ms-attr="{name:'publicWater3', value: defaultIfNull(checkStatement.water3)}">
                    </td>
                    <th>对公流水月均:<input name="publicMonthAverage" type="hidden" ms-attr="{value: checkStatement.monthAverage}"></th>
                    <td class="W10 numFormat">{{ getRecordCount(checkStatement.water1, checkStatement.water2, checkStatement.water3)}}元</td>
                    <td>
                        <a href="javaScript:void(0);" ms-on-click="addCheckStatement('B')" ms-if="$index == 0"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <a href="javaScript:void(0);" ms-on-click="deleteCheckStatement($index, checkStatement, $event)" ms-if="$index != 0"><i class="fa fa-minus" aria-hidden="true"></i></a>
                    </td>
                </tr>
                <tr>
                    <th>对公流水月均合计:<input type="hidden" name="publicWaterTotal" ms-attr="{value: publicWaterAverageTotal}"></th>
                    <td class="publicRecordCount numFormat" ms-html="getWaterTotal('publicWaterAverageTotal') + '元'"></td>
                </tr>
                <tr>
                    <th>月均流水合计:<input type="hidden" name="waterIncomeTotal" ms-attr="{value: waterIncomeTotal}"></th>
                    <td class="recordCount numFormat" ms-html="getWaterTotal('waterIncomeTotal') + '元'"></td>
                </tr>
            </table>
            <h3>其他核实信息</h3>
            <table class="table_ui W100">
                <tr>
                    <th width="12%">法院网核查无异常:</th>
                    <td width="20%"><input type="checkbox" ms-attr="{id:'money_courtException_input'}" ms-duplex-checked="approvalInfo.approveCheckInfo.courtCheckException == 0"/></td>
                    <th width="12%">内部匹配无异常:</th>
                    <td><input type="checkbox" ms-attr="{id: 'money_internalException_input'}" ms-duplex-checked="approvalInfo.approveCheckInfo.internalCheckException == 0"/></td>
                    <th class="requiredFontWeight">征信平台已验证:</th>
                    <td><input type="checkbox" ms-attr="{id: 'money_creditCheckException_checkbox'}" ms-duplex-checked="approvalInfo.approveCheckInfo.creditCheckException == 0"></td>
                </tr>
                <tr>
                    <th class="requiredFontWeight">有信用记录:</th>
                    <td>
                        <input type="checkbox" ms-attr="{id: 'money_creditRecord_checkbox'}" ms-duplex-checked="ifCreditRecord == 1">
                    </td>
                    <th class="requiredFontWeight">人行近1个月查询:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" ms-attr="{id: 'moneyApprovalOpinion_oneMonthsCount', name: 'oneMonthsCount'}"
                               ms-duplex="approvalInfo.approveCheckInfo.oneMonthsCount"
                               data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999]']">
                    </td>
                    <th class="requiredFontWeight">人行近3个月查询:</th>
                    <td>
                        <input type="text" class="easyui-numberbox input" ms-attr="{id:'moneyApprovalOpinion_threeMonthsCount',name: 'threeMonthsCount'}"
                               ms-duplex="approvalInfo.approveCheckInfo.threeMonthsCount"
                               data-options="required:true,precision:0,buttonText:'次',validType:['compareMaxMin[0,99999999]','compareNum[\'#moneyApprovalOpinion_oneMonthsCount\']']">
                    </td>
                </tr>
                <tr>
                    <th>社保信息已验证:</th>
                    <td><input ms-attr="{id:'money_socialInsuranceInfo_checkbox'}" ms-duplex-checked="applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAuthenticated == 'Y'" type="checkbox"></td>
                    <th>公积金信息已验证:</th>
                    <td><input ms-attr="{id:'money_fundInfo_checkbox'}"  ms-duplex-checked="applyInfo.assetsInfo.fundInfo.accumulationFundAuthenticated == 'Y'" type="checkbox"></td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th>备注:</th>
                    <td colspan="5">
                        <input class="easyui-textbox" name="memo" ms-duplex="approvalInfo.approveCheckInfo.memo || ''"
                               data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
                    </td>
                </tr>
            </table>
        </div>
        <c:set value="0" var="creditLoanLength"/>
        <c:set value="0" var="houseLoanLength"/>
        <c:set value="0" var="carLoanLength"/>
        <c:set value="0" var="otherLoanLength"/>
        <div class="easyui-panel" title="负债信息">
            <a class="easyui-linkbutton" onclick="moneyApprovalOpinionContrastDialog()">征信初判</a>
            <!-- 有审批意见时重新解读央行报告 通过标识央行报告是否改变来判断 更为进准 begin-->
            <c:if test="${'Y' == markReportIdChange}">
                <table class="table_list W100" id="moneyApprovalOpinion_liabilities_table">
                    <tr>
                        <td class="specialThCol">信用卡总额度/元:</td>
                        <td>
                            <input type="text" name="creditTotalLimit" value="${not empty reportInfo.creditLimitMoney?reportInfo.creditLimitMoney:0}"
                                   class="easyui-numberbox input"
                                   data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],precision:0,buttonText:'元'">
                        </td>
                        <th class="requiredFontWeight specialThCol">信用卡已用额度/元</th>
                        <td>
                            <input type="text" name="creditUsedLimit" value="${not empty reportInfo.alreadyUseMoney?reportInfo.alreadyUseMoney:0}"
                                   class="easyui-numberbox input"
                                   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
                        </td>
                        <td class="specialThCol">
                            信用卡负债/元<input name="creditDebt" value="${not empty reportInfo.debt?reportInfo.debt:0}" type="hidden">
                        </td>
                        <td ms-attr="{id: 'money_liabilitiesCreditCard'}" class="W10 numFormat">
                            <fmt:formatNumber type="number" value="${not empty reportInfo.debt?reportInfo.debt:0}" pattern="0" maxFractionDigits="0"/>
                        </td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">信用贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <!--  Start 判断是否是追加贷款 -->
                    <c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
                        <c:if test="${not empty item.creditLoanDebt }">
                            <c:if test="${item.debtType =='TOPUPLOAN'}">
                                <!-- topup显示第一位 -->
                                <tr class="topUpTr">
                                    <td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
                                    <td><input type="text" class="input" data-options="groupSeparator:','" value="${item.creditLoanLimit}" name="creditLoanLimit"></td>
                                    <td><input type="text" class="input" value="${item.creditLoanTerm}" name="creditLoanTerm"></td>
                                    <td><input type="text" class="input" data-options="groupSeparator:','" value="${item.creditLoanDebt}" name="creditLoanDebt"></td>
                                    <td><input type="hidden" name="creditNo" value="${item.creditNo}" placeholder="对应关系"></td>
                                    <td></td>
                                </tr>
                            </c:if>
                        </c:if>
                    </c:forEach>
                    <!--  End 判断是否是追加贷款 -->
                    <!--  Start 判断贷款不为空 -->
                    <c:if test="${not empty loanLimitInfo }">
                        <c:forEach items="${loanLimitInfo.loan}" var="item" varStatus="itemStatr">
                            <c:set value="${creditLoanLength+1}" var="creditLoanLength"/>
                            <tr class="creditCardTr">
                                <td>信用贷款<span>${creditLoanLength}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                                <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                                <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                                <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                                <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                                <td></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr class="creditCardTr">
                        <td>信用贷款<span>${creditLoanLength+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr>
                        <td class="specialThCol">外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="0"></td>
                        <td class="creditCountLiabilities" colspan="5">0</td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">房贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:if test="${not empty loanLimitInfo }">
                        <c:forEach items="${loanLimitInfo.house}" var="item" varStatus="itemStatr">
                            <c:set value="${houseLoanLength+1}" var="houseLoanLength"/>
                            <tr class="creditCardTr">
                                <td>房贷<span>${houseLoanLength}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                                <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                                <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                                <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                                <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                                <td></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr class="creditCardTr">
                        <td>房贷<span>${houseLoanLength+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">车贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:if test="${not empty loanLimitInfo }">
                        <c:forEach items="${loanLimitInfo.car}" var="item" varStatus="itemStatr">
                            <c:set value="${carLoanLength+1}" var="carLoanLength"/>
                            <tr class="creditCardTr">
                                <td>车贷<span>${carLoanLength}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                                <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                                <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                                <td><input type="text" value="${item.debtMoney}" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                                <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                                <td></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr class="creditCardTr">
                        <td>车贷<span>${carLoanLength+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">其他贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:if test="${not empty loanLimitInfo }">
                        <c:forEach items="${loanLimitInfo.other}" var="item" varStatus="itemStatr">
                            <c:set value="${otherLoanLength +1}" var="otherLoanLength"/>
                            <tr class="creditCardTr">
                                <td>其他贷款<span>${otherLoanLength}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                                <td><input type="text" value="${item.grantMoney}" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                                <td><input type="text" value="${item.repayPeriods}" class="input" name="creditLoanTerm"></td>
                                <td><input type="text" value="" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                                <td><input type="hidden" value="${item.no}" name="creditNo" placeholder="对应关系">${item.no}</td>
                                <td></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr class="creditCardTr">
                        <td>其他贷款<span>${otherLoanLength+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <!--  End 判断贷款不为空 -->
                    <tr>
                        <td class="specialThCol">外部负债总额</td>
                        <td>
                            <input id="outDebtTotal_id" value="0" name="outDebtTotal" class="easyui-numberbox input"
                                   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
                        </td>
                        <td class="specialThCol" align="right">
                            速贷还款情况
                            <input ms-attr="{id: 'money_ifBlackRoster', name: 'ifBlackRoster'}" type="hidden" value="${approvalInfo.debtsInfoList[0].ifBlackRoster}">
                        </td>
                        <td colspan="3">
                            <select ms-attr="{id:'money_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
                                    data-options="required:true,panelHeight:'auto',editable:false,value:'${approvalInfo.debtsInfoList[0].fastLoanSituation}'">
                                <option value="NORMAL">正常</option>
                                <option value="OVERDUE">逾期</option>
                                <option value="SETTLE">结清</option>
                                <option value="NOLOAN">无贷款</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="specialThCol">备注</td>
                        <td colspan="6">
                            <input class="easyui-textbox" name="memo" value="${approvalInfo.debtsInfoList[0].memo}"
                                   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
                        </td>
                    </tr>
                    <!--  End 判断贷款为空 -->
                </table>
            </c:if>
            <!-- 有审批意见时重新解读央行报告end -->
            <!-- 有审批意见时不重新解读央行报告begin -->
            <c:if test="${'Y' != markReportIdChange}">
                <table class="table_list W100" id="moneyApprovalOpinion_liabilities_table">
                    <tr>
                        <td class="specialThCol">信用卡总额度/元:</td>
                        <td>
                            <input type="text" name="creditTotalLimit" value="${approvalInfo.debtsInfoList[0].creditTotalLimit}" class="easyui-numberbox input"
                                   data-options="groupSeparator:',',min:0,max:99999999,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
                        </td>
                        <th class="requiredFontWeight specialThCol">信用卡已用额度</th>
                        <td>
                            <input type="text" name="creditUsedLimit" value="${approvalInfo.debtsInfoList[0].creditUsedLimit}" class="easyui-numberbox input"
                                   data-options="groupSeparator:',',validType:['compareMaxMin[0,99999999]'],required:true,precision:0,buttonText:'元',onChange:function(newValue){changeCreditCard(newValue);}">
                        </td>
                        <td class="specialThCol">
                            信用卡负债<input name="creditDebt" type="hidden" value="${approvalInfo.debtsInfoList[0].creditDebt}">
                        </td>
                        <td id="money_liabilitiesCreditCard" class="W10 numFormat">
                            <fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].creditDebt}" pattern="0" maxFractionDigits="0"/>
                        </td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">信用贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
                        <c:if test="${not empty item.creditLoanLimit or not empty item.creditLoanTerm or not empty item.creditLoanDebt }">
                            <c:if test="${item.debtType =='TOPUPLOAN'}">
                                <tr class="topUpTr">
                                    <td>TOPUP贷款<input type="hidden" name="debtType" value="TOPUPLOAN"></td>
                                    <td><input type="text" name="creditLoanLimit" class="input" value="${item.creditLoanLimit}" data-options="groupSeparator:','"></td>
                                    <td><input type="text" name="creditLoanTerm" class="input" value="${item.creditLoanTerm}"></td>
                                    <td><input type="text" name="creditLoanDebt" class="input" value="${item.creditLoanDebt}" data-options="groupSeparator:','"></td>
                                    <td>
                                        <input type="hidden" name="creditNo" value="${item.creditNo}" placeholder="对应关系">
                                    </td>
                                    <td></td>
                                </tr>
                            </c:if>
                            <c:if test="${item.debtType =='CREDITLOAN'}">
                                <c:set value="${creditLoanLength+1}" var="creditLoanLength"/>
                                <tr class="creditCardTr">
                                    <td>信用贷款<span>${creditLoanLength}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                                    <td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
                                    <td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
                                    <td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
                                    <td>
                                        <input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}
                                    </td>
                                    <td>
                                        <c:if test="${empty item.creditNo}">
                                            <a href="javaScript:void(0);" onclick="deleteTr(this,'CREDITLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                        </c:if>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>信用贷款<span>${creditLoanLength+1}</span><input type="hidden" name="debtType" value="CREDITLOAN"></td>
                        <td><input type="text" name="creditLoanLimit" class="input" data-options="groupSeparator:','"></td>
                        <td><input type="text" name="creditLoanTerm" class="input"></td>
                        <td><input type="text" name="creditLoanDebt" class="input" data-options="groupSeparator:','"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('信用贷款','CREDITLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr>
                        <td>外部信用负债总额<input id="outCreditDebtTotal_id" name="outCreditDebtTotal" type="hidden" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}"></td>
                        <td class="creditCountLiabilities numFormat" colspan="5"><fmt:formatNumber type="number" value="${approvalInfo.debtsInfoList[0].outCreditDebtTotal}" pattern="0" maxFractionDigits="0"/></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">房贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
                        <c:if test="${item.debtType =='HOUSELOAN'}">
                            <c:set value="${houseLoanLength+1}" var="houseLoanLength"/>
                            <tr class="creditCardTr">
                                <td>房贷<span>${houseLoanLength}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                                <td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
                                <td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
                                <td><c:if test="${empty item.creditNo}">
                                    <a href="javaScript:void(0);" onclick="deleteTr(this,'HOUSELOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
                                </c:if></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>房贷<span>${houseLoanLength+1}</span><input type="hidden" name="debtType" value="HOUSELOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('房贷','HOUSELOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">车贷</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
                        <c:if test="${item.debtType =='CARLOAN'}">
                            <c:set value="${carLoanLength+1}" var="carLoanLength"/>
                            <tr class="creditCardTr">
                                <td>车贷<span>${carLoanLength}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                                <td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
                                <td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
                                <td><c:if test="${empty item.creditNo}">
                                    <a href="javaScript:void(0);" onclick="deleteTr(this,'CARLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
                                </c:if></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>车贷<span>${carLoanLength+1}</span><input type="hidden" name="debtType" value="CARLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('车贷','CARLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr class="specialTrCol">
                        <td class="specialThCol">其他贷款</td>
                        <td class="specialThCol">额度/元</td>
                        <td class="specialThCol">期限/月</td>
                        <td class="specialThCol">负债/元</td>
                        <td class="specialThCol">对应关系</td>
                        <td class="specialThCol">操作</td>
                    </tr>
                    <c:forEach items="${approvalInfo.debtsInfoList}" var="item" varStatus="itemStatr">
                        <c:if test="${item.debtType =='OTHERLOAN'}">
                            <c:set value="${otherLoanLength +1}" var="otherLoanLength"/>
                            <tr class="creditCardTr">
                                <td>其他贷款<span>${otherLoanLength}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                                <td><input type="text" name="creditLoanLimit" value="${item.creditLoanLimit}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="text" name="creditLoanTerm" value="${item.creditLoanTerm}" class="input"></td>
                                <td><input type="text" name="creditLoanDebt" value="${item.creditLoanDebt}" class="input" data-options="groupSeparator:','"></td>
                                <td><input type="hidden" value="${item.creditNo}" name="creditNo" placeholder="对应关系">${item.creditNo}</td>
                                <td><c:if test="${empty item.creditNo}">
                                    <a href="javaScript:void(0);" onclick="deleteTr(this,'OTHERLOAN')"><i class="fa fa-minus" aria-hidden="true"></i></a>
                                </c:if></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <tr class="creditCardTr">
                        <td>其他贷款<span>${otherLoanLength+1}</span><input type="hidden" name="debtType" value="OTHERLOAN"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanLimit"></td>
                        <td><input type="text" class="input" name="creditLoanTerm"></td>
                        <td><input type="text" class="input" data-options="groupSeparator:','" name="creditLoanDebt"></td>
                        <td><input type="hidden" name="creditNo" placeholder="对应关系"></td>
                        <td><a href="javaScript:void(0);" onclick="addCredit('其他贷款','OTHERLOAN')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                    </tr>
                    <tr>
                        <td class="specialThCol">外部负债总额</td>
                        <td>
                            <input id="outDebtTotal_id" name="outDebtTotal" value="${approvalInfo.debtsInfoList[0].outDebtTotal}" class="easyui-numberbox input"
                                   data-options="groupSeparator:',',required:true,precision:0,validType:['compareMaxMin[0,99999999]'],buttonText:'元'">
                        </td>
                        <td class="specialThCol" align="right">速贷还款情况<input id="money_ifBlackRoster" type="hidden" name="ifBlackRoster" value="${approvalInfo.debtsInfoList[0].ifBlackRoster}"></td>
                        <td colspan="3">
                            <select ms-attr="{id: 'money_fastLoanBlacklist', name: 'fastLoanSituation'}" class="easyui-combobox select"
                                    data-options="required:true,panelHeight:'auto',editable:false,value:'${approvalInfo.debtsInfoList[0].fastLoanSituation}'">
                                <option value="NORMAL">正常</option>
                                <option value="OVERDUE">逾期</option>
                                <option value="SETTLE">结清</option>
                                <option value="NOLOAN">无贷款</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="specialThCol">备注</td>
                        <td colspan="6">
                            <input class="easyui-textbox" name="memo" value="${approvalInfo.debtsInfoList[0].memo}"
                                   data-options="multiline:true,width:1000,height:70,prompt:'请输入备注信息',validType: 'max[200]'">
                        </td>
                    </tr>
                </table>
            </c:if>
        </div>
        <!-----------------------------------------End 审核记录不为空--------------------------------------->
    </c:if>
</form>
<!-- ---------------------------------------Start 产品信息判断--------------------------------------->
<div class="easyui-panel" title="产品信息">
    <table id="money_selectAssetOption_table" class="table_ui W90 center_m">
        <tr>
            <c:if test="${ not empty applyInfo.assetsInfo.policyInfo}">
                <td>保单信息 <input type="checkbox" value="POLICY" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('POLICY')"></td>
            </c:if>
            <c:if test="${'Y' ==  applyInfo.assetsInfo.carInfo.unabridged}">
                <td>车辆信息 <input type="checkbox" value="CAR" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('CAR')"></td>
            </c:if>
            <c:if test="${'Y' ==  applyInfo.assetsInfo.cardLoanInfo.unabridged}">
                <td>信用卡信息 <input type="checkbox" value="CARDLOAN" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('CARDLOAN')"></td>
            </c:if>
            <c:if test="${'Y' ==  applyInfo.assetsInfo.estateInfo.unabridged}">
                <td>房产信息 <input type="checkbox" value="ESTATE" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('ESTATE')"></td>
            </c:if>
            <c:if test="${'Y' ==  applyInfo.assetsInfo.masterLoanInfo.unabridged}">
                <td>淘宝账号信息 <input type="checkbox" value="MASTERLOAN" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('MASTERLOAN')"></td>
            </c:if>
            <c:if test="${'Y' ==  applyInfo.assetsInfo.educationInfo.unabridged}">
                <td>学历信息 <input type="checkbox" value="EDUCATION" name="selectAssetOption" ms-duplex-checked="getIfSelectAssetType('EDUCATION')"></td>
            </c:if>
        </tr>
    </table>
    <hr/>
    <div id="money_customerInfo_assetsInfo_div">
        <!-- 保单信息 -->
        <c:if test="${ not empty applyInfo.assetsInfo.policyInfo}">
            <div id="money_customer_POLICY_div">
                <h1 class="W90 center_m">保单信息</h1>
                <c:forEach var="policyInfo" varStatus="status" items="${ applyInfo.assetsInfo.policyInfo}">
                    <form>
                        <input type="hidden" name="id" value="${policyInfo.id}">
                        <input type="hidden" name="unabridged" value="${policyInfo.unabridged}">
                        <input type="hidden" name="appAdd" value="${policyInfo.appAdd}">
                        <table class="table_ui W100">
                            <tr>
                                <th class="requiredFontWeight">投保公司:</th>
                                <td colspan="3"><input class="input" name="insuranceCompany" id="money_insuranceCompany_${status.index}" value="${policyInfo.insuranceCompany}"></td>
                                <th class="requiredFontWeight">保险年限:</th>
                                <td><input class="input" id="money_customerInfo_assetsInfo_policyInfo_insuranceTerm_${status.index}" value="${policyInfo.insuranceTerm == 999? "终身":policyInfo.insuranceTerm}" name="insuranceTerm"></td>
                            </tr>
                            <tr>
                                <th width="14%" class="requiredFontWeight">保险金额:</th>
                                <td width="18%"><input name="insuranceAmt" id="money_customerInfo_assetsInfo_policyInfo_insuranceAmt_${status.index}" value="${policyInfo.insuranceAmt}" class="easyui-numberbox input"></td>
                                <th width="14%" class="requiredFontWeight">年缴金额:</th>
                                <td width="18%"><input class="input" id="money_customerInfo_assetsInfo_policyInfo_yearPaymentAmt_${status.index}" value="${policyInfo.yearPaymentAmt}" name="yearPaymentAmt"></td>
                                <th width="14%" class="requiredFontWeight">已缴年限:</th>
                                <td><input class="input" id="money_customerInfo_assetsInfo_policyInfo_paidTerm_${status.index}" value="${policyInfo.paidTerm}" name="paidTerm"></td>
                            </tr>
                            <tr>
                                <th class="requiredFontWeight">查询账号:</th>
                                <td><input class="easyui-textbox input" name="policyAccount" id="money_insuranceAccount_${status.index}" value="${policyInfo.policyAccount}"></td>
                                <th class="requiredFontWeight">密码:</th>
                                <td><input class="easyui-textbox input" name="policyPassword" id="money_insurancePassword_${status.index}" value="${policyInfo.policyPassword}"></td>
                                <th>寿险保单已验证:</th>
                                <td>
                                    <input type="checkbox" name="policyCheckIsVerify"  <c:if test="${ 'Y'==policyInfo.policyCheckIsVerify}">checked</c:if>>
                                    <c:if test="${'N' == policyInfo.appAdd}">
                                        <span class="float_right"><a href="javaScript:void(0);" onclick="deletePolicyInfo(this)" title="删除"><i class='fa fa-minus' aria-hidden='true'></i></a></span>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                        <hr/>
                    </form>
                </c:forEach>
                <table class="table_ui W100">
                    <tr>
                        <th width="14%">总年缴金额/元:</th>
                        <td width="18%" id="money_policyInfo_countYearPaymentAmt"></td>
                        <td colspan="3"><a class="easyui-linkbutton" onclick="addNewPolicyInfo()">新增保单贷信息</a></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.carInfo.unabridged}">
                    <table class="table_ui W100">
                        <tr>
                            <th width="14%" class="requiredFontWeight">购车价格:</th>
                            <td width="18%"><input id="money_customerInfo_carInfo_carBuyPrice" class="easyui-numberbox input" value="${applyInfo.assetsInfo.carInfo.carBuyPrice}" name="carBuyPrice"></td>
                            <th width="14%" class="requiredFontWeight">购车时间:</th>
                            <td width="18%"><input class="easyui-datebox input" id="money_customerInfo_date_CAR" value="${applyInfo.assetsInfo.carInfo.carBuyDate}" name="carBuyDate"></td>
                            <th width="14%" class="requiredFontWeight">车牌号:</th>
                            <td><input class="easyui-textbox input" id="money_customerInfo_car_plateNum" value="${applyInfo.assetsInfo.carInfo.plateNum}" name="plateNum"/></td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">车贷情况:</th>
                            <td><input id="money_carLoanSituation" class="easyui-combobox input" name="carLoanStatus" value="${applyInfo.assetsInfo.carInfo.carLoanStatus}"></td>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.carInfo.carLoanStatus}">requiredFontWeight</c:if>">车贷发放年月:</th>
                            <td><input id="money_customerInfo_carInfo_carLoanGrantDate" class="easyui-datebox input" name="carLoanIssueDate"></td>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.carInfo.carLoanStatus}">requiredFontWeight</c:if>">车贷月还款额:</th>
                            <td><input id="money_Customer_Info_monthPaymentAmt" class="easyui-numberbox input" value="${applyInfo.assetsInfo.carInfo.monthPaymentAmt}" name="monthPaymentAmt"></td>
                        </tr>
                        <tr>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.carInfo.carLoanStatus}">requiredFontWeight</c:if>">车贷发放机构:</th>
                            <td colspan="2"><input id="money_carLoanOrg" class="input" value="${applyInfo.assetsInfo.carInfo.carLoanOrg}" name="carLoanOrg"></td>
                            <th class="requiredFontWeight">交强险承保公司:</th>
                            <td colspan="2"><input id="money_carLoanInsuranceCompany" class="input" value="${applyInfo.assetsInfo.carInfo.tciCompany}" name="tciCompany"></td>
                        </tr>
                        <tr>
                            <th>车险保单已验证</th>
                            <td colspan="5"><input type="checkbox" name="carCheckIsVerify" <c:if test="${'Y' == applyInfo.assetsInfo.carInfo.carCheckIsVerify}">checked</c:if>></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.fundInfo.unabridged}">
                    <table class="table_ui W100">
                        <tr class="providentLoanRecordTr">
                            <th width="14%" class="requiredFontWeight">公积金账号:</th>
                            <td width="18%"><input class="easyui-textbox input" id="money_accumulationFundAccount" name="accumulationFundAccount" value="${applyInfo.assetsInfo.fundInfo.accumulationFundAccount}"></td>
                            <th width="14%" class="requiredFontWeight">公积金账号密码:</th>
                            <td colspan="3"><input id="money_accumulationFundPassword" class="easyui-textbox input" name="accumulationFundPassword" value="${applyInfo.assetsInfo.fundInfo.accumulationFundPassword}"></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.socialInsuranceInfo.unabridged}">
                    <table class="table_ui W100">
                        <tr>
                            <th width="14%" class="requiredFontWeight">社保账号:</th>
                            <td width="18%"><input id="money_socialSecurityAccount" class="easyui-textbox input" name="socialInsuranceAccount" value="${applyInfo.assetsInfo.socialInsuranceInfo.socialInsuranceAccount}"></td>
                            <th width="14%" class="requiredFontWeight">社保账号密码:</th>
                            <td colspan="3"><input id="money_socialSecurityPassword" class="easyui-textbox input" name="socialInsurancePassword" value="${applyInfo.assetsInfo.socialInsuranceInfo.socialInsurancePassword}"></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.cardLoanInfo.unabridged}">
                    <table class="table_ui W100" id="money_customerInfo_cardLoan_table">
                        <tr>
                            <th width="14%" class="requiredFontWeight">信用卡额度:</th>
                            <td width="18%"><input class="easyui-numberbox input" id="money_customerInfo_assetsInfo_cardLoanInfo_creditLimit" value="${applyInfo.assetsInfo.cardLoanInfo.creditLimit}" name="creditLimit"></td>
                            <th width="14%" class="requiredFontWeight">发卡银行:</th>
                            <td width="18%"><input id="money_cardLoanInfo_issuingBank" class="easyui-textbox input" name="issusingBank" value="${applyInfo.assetsInfo.cardLoanInfo.issusingBank}"></td>
                            <th width="14%" class="requiredFontWeight">发卡时间:</th>
                            <td><input id="money_cardLoanInfo_issuerDate" class="easyui-datebox input" name="issuerDate" value="${applyInfo.assetsInfo.cardLoanInfo.issuerDate}"></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.estateInfo.unabridged}">
                    <table class="table_ui W100">
                        <tr>
                            <th width="14%" class="requiredFontWeight">房产类型:</th>
                            <td width="18%"><input class="easyui-combobox  input" id="money_customerInfo_assetsInfo_estateInfo_estateType" value="${applyInfo.assetsInfo.estateInfo.estateType}" name="estateType"/></td>
                            <th width="14%" class="requiredFontWeight">购房时间:</th>
                            <td width="18%"><input class="easyui-datebox input" id="money_customerInfo_date_ESTATE" name="estateBuyDate"></td>
                            <th width="14%" class="requiredFontWeight">与他人共有情况:</th>
                            <td>是<input type="radio" value="1" name="isCommon" <c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==1}">checked="checked"</c:if>/>
                                否<input type="radio" value="0" name="isCommon" <c:if test="${applyInfo.assetsInfo.estateInfo.isCommon==0}">checked="checked"</c:if>/>
                            </td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">房产地址同住宅地址:<input type="hidden" value="${applyInfo.assetsInfo.estateInfo.estateSameRegistered}" id="money_isEqualHomeAddr"></th>
                            <td colspan="5">
                                是<input type="radio" value="Y" name="estateSameRegistered" <c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='Y'}">checked="checked"</c:if>/>
                                否<input type="radio" value="N" name="estateSameRegistered" <c:if test="${applyInfo.assetsInfo.estateInfo.estateSameRegistered=='N'}">checked="checked"</c:if>/>
                            </td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">房产地址:</th>
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
                            <th class="requiredFontWeight">房贷情况:</th>
                            <td><input class="easyui-combobox input" id="money_customerInfo_estateInfo_estateLoan" value="${applyInfo.assetsInfo.estateInfo.estateLoan}" name="estateLoan"/></td>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.estateInfo.estateLoan}">requiredFontWeight</c:if>">房贷总额:</th>
                            <td><input id="money_estateInfo_loanCount" class="easyui-numberbox input" name="estateLoanAmt" value="${applyInfo.assetsInfo.estateInfo.estateLoanAmt}"></td>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.estateInfo.estateLoan}">requiredFontWeight</c:if>">房贷月还款额:</th>
                            <td><input class="easyui-numberbox input" id="moneyCustomerInfo_house_monthPaymentAmt" value="${applyInfo.assetsInfo.estateInfo.monthPaymentAmt}" name="monthPaymentAmt"></td>
                        </tr>
                        <tr>
                            <th class="<c:if test="${'ING'==applyInfo.assetsInfo.estateInfo.estateLoan}">requiredFontWeight</c:if>">房贷发放年月:</th>
                            <td colspan="5"><input class="easyui-datebox input" id="moneyCustomerInfo_house_estateLoanGrantDate" name="estateLoanIssueDate"></td>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.masterLoanInfo.unabridged}">
                    <table class="table_ui W100" id="money_customerInfo_merchant_table">
                        <tr>
                            <th width="14%" class="requiredFontWeight">淘宝会员名:</th>
                            <td width="18%"><input id="money_taoBaoInfo_account" class="easyui-textbox input" name="memberName" value="${applyInfo.assetsInfo.masterLoanInfo.memberName}"></td>
                            <th width="14%" class="requiredFontWeight">淘宝买家信用等级:</th>
                            <td width="18%"><input id="money_taoBaoInfo_buyerCreditLevel" class="easyui-combobox input" name="buyerCreditLevel" value="${applyInfo.assetsInfo.masterLoanInfo.buyerCreditLevel}"></td>
                            <th width="14%" class="requiredFontWeight">淘宝买家信用类型:</th>
                            <td><input id="money_taoBaoInfo_buyerCreditType" class="easyui-combobox input" name="buyerCreditType" value="${applyInfo.assetsInfo.masterLoanInfo.buyerCreditType}"></td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">淘宝年消费额:</th>
                            <td><input id="money_taoBaoInfo_totalConsumption" class="easyui-numberbox input" name="lastYearPayAmt" value="${applyInfo.assetsInfo.masterLoanInfo.lastYearPayAmt}"></td>
                            <th class="requiredFontWeight">淘气值:</th>
                            <td><input id="money_taoBaoInfo_naughtyValue" class="easyui-numberbox input" name="naughtyValue" value="${applyInfo.assetsInfo.masterLoanInfo.naughtyValue}"></td>
                            <th class="requiredFontWeight">芝麻信用分:</th>
                            <td><input id="money_taoBaoInfo_sesameCreditValue" class="easyui-numberbox input" name="sesameCreditValue" value="${applyInfo.assetsInfo.masterLoanInfo.sesameCreditValue}"></td>
                        </tr>
                        <tr>
                            <th>花呗额度:</th>
                            <td><input id="money_taoBaoInfo_antSpend" class="easyui-numberbox input" name="costLimit" value="${applyInfo.assetsInfo.masterLoanInfo.costLimit}"></td>
                            <th>借呗额度:</th>
                            <td><input id="money_taoBaoInfo_antBorrow" class="easyui-numberbox input" name="borrowLimit" value="${applyInfo.assetsInfo.masterLoanInfo.borrowLimit}"></td>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <th>3个月内收货地址:</th>
                            <td><select name="onlineAWithin3MonthsAddress" class="easyui-combobox select" data-options="panelHeight:'auto',editable:false,value:'${applyInfo.assetsInfo.masterLoanInfo.onlineAWithin3MonthsAddress}'">
                                <option value="00001">同住址</option>
                                <option value="00002">同司址</option>
                                <option value="00003">其他</option>
                            </select></td>
                            <th>6个月外收货地址:</th>
                            <td><select name="onlineAWithin6MonthsAddress" class="easyui-combobox select" data-options="panelHeight:'auto',editable:false,value:'${applyInfo.assetsInfo.masterLoanInfo.onlineAWithin6MonthsAddress}'">
                                <option value="00001">同住址</option>
                                <option value="00002">同司址</option>
                                <option value="00003">其他</option>
                            </select></td>
                            <th></th>
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
                    <input type="hidden" name="unabridged" value="${applyInfo.assetsInfo.educationInfo.unabridged}">
                    <table class="table_ui W100">
                        <tr>
                            <th width="14%" class="requiredFontWeight">教育经历:</th>
                            <td width="18%"><input id="money_education_experience" class="easyui-combobox input" name="educationExperience" value="${applyInfo.assetsInfo.educationInfo.educationExperience}"></td>
                            <th width="14%" class="requiredFontWeight">院校名称:</th>
                            <td width="18%"><input id="money_education_schoolName" class="easyui-textbox input" name="schoolName" value="${applyInfo.assetsInfo.educationInfo.schoolName}"></td>
                            <th width="14%" class=" requiredFontWeight">地区:</th>
                            <td>
                                <span <c:if test="${'00002' == applyInfo.assetsInfo.educationInfo.educationExperience}">class="display_noH"</c:if>>
                                    <input id="money_education_province" name="areaProvinceId" data-options="width:145" class="input" value="${applyInfo.assetsInfo.educationInfo.areaProvinceId}">
                                    <input class="region" name="areaProvince" type="hidden">
                                </span>
                                <span <c:if test="${'00001' == applyInfo.assetsInfo.educationInfo.educationExperience}">class="display_noH"</c:if>>港澳台及海外</span>
                            </td>
                            <td>
                                <span <c:if test="${'00002' == applyInfo.assetsInfo.educationInfo.educationExperience}">class="display_noH"</c:if>>
                                    <input id="money_education_city" class="input" name="areaCityId" data-options="width:145" value="${applyInfo.assetsInfo.educationInfo.areaCityId}">
                                    <input class="region" name="areaCity" type="hidden">
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">最高学历:</th>
                            <td><input id="money_education_qualification" class="easyui-combobox input" name="qualification" value="${applyInfo.assetsInfo.educationInfo.qualification}"></td>
                            <th class="requiredFontWeight">毕业时间:</th>
                            <td><input class="easyui-datebox input" id="money_education_graduationDate" name="graduationDate"></td>
                            <th class="<c:if test="${ '00002' == applyInfo.assetsInfo.educationInfo.educationExperience}">requiredFontWeight</c:if>">认证书编号:</th>
                            <td colspan="2"><input class="easyui-textbox input" id="money_education_certificateNumber" name="certificateNumber" value="${applyInfo.assetsInfo.educationInfo.certificateNumber}"></td>
                        </tr>
                        <tr>
                            <th class="<c:if test="${ '00001' == applyInfo.assetsInfo.educationInfo.educationExperience}">requiredFontWeight</c:if>">学信网用户名:</th>
                            <td><input id="money_education_chsiAccount" class="easyui-textbox input" name="chsiAccount" value="${applyInfo.assetsInfo.educationInfo.chsiAccount}"></td>
                            <th class="<c:if test="${ '00001' == applyInfo.assetsInfo.educationInfo.educationExperience}">requiredFontWeight</c:if>">学信网密码:</th>
                            <td><input id="money_education_chsiPassword" class="easyui-textbox input" name="chsiPassword" value="${applyInfo.assetsInfo.educationInfo.chsiPassword}"></td>
                            <th>学历信息已验证:</th>
                            <td colspan="2"><input type="checkbox" name="chsiAuthenticated" <c:if test="${'Y' ==applyInfo.assetsInfo.educationInfo.chsiAuthenticated}">checked</c:if>></td>
                        </tr>
                    </table>
                    <hr/>
                </form>
            </div>
        </c:if>
    </div>
</div>
<!-- ---------------------------------------End 产品信息判断--------------------------------------->
<!-- ---------------------------------------Start 系统初判--------------------------------------->
<div class="easyui-panel" title="系统初判">
    <h2>
        系统初判 <a class="easyui-linkbutton" onclick="moneySystemDetermine('${applyInfo.baseInfo.loanNo}')">系统初判</a>
    </h2>
    <table id="money_systemDetermine_table" class="table_ui W80">
        <tr>
            <th>建议核实收入:</th>
            <td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.adviceVerifyIncome}" maxFractionDigits="0"/></td>
            <th>建议到手金额:</th>
            <td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.adviceAuditLines}" maxFractionDigits="0"/></td>
            <th>预估评级费:</th>
            <td class="numFormat"><fmt:formatNumber type="number" value="${auditRulesVO.estimatedCost}" maxFractionDigits="2"/></td>
        </tr>
    </table>
</div>
<!-- ---------------------------------------End 系统初判--------------------------------------->

<!-- ---------------------------------------Start 审批意见--------------------------------------->
<div class="easyui-panel" title="审批意见">
    <form id="moneyApprovalOpinion_approvalInfo_from">
        <input type="hidden" id="contractBranchId" ms-duplex="applyInfo.baseInfo.contractBranchId">
        <c:forEach items="${approvalInfo.approvalHistoryList}" var="item" varStatus="stat">
            <!-- 不是最后一个就直接展示信息 -->
            <c:if test="${!stat.last}">
                <h2>第${stat.index+1}次审批</h2>
                <table class="table_ui W100">
                    <tr>
                        <c:choose>
                            <c:when test="${item.rtfState=='XSZS'}">
                                <th>终审人员:</th>
                            </c:when>
                            <c:otherwise>
                                <th>初审人员:</th>
                            </c:otherwise>
                        </c:choose>
                        <td>${item.approvalPersonName}</td>
                        <th>审批时间:</th>
                        <td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/></td>
                    </tr>
                    <tr>
                        <th>申请额度:</th>
                        <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                        <th>申请期限:</th>
                        <td>${item.approvalApplyTerm}</td>
                        <th>审批产品:</th>
                        <td><c:if test="${'证大前前'!=item.approvalProductName}">${item.approvalProductName}</c:if></td>
                    </tr>
                    <tr>
                        <th>核实收入:</th>
                        <td class="numFormat">
                            <fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0"/>
                        </td>
                        <c:choose>
                            <c:when test="${item.rtfState=='XSZS'}">
                                <th>终审审批额度:</th>
                            </c:when>
                            <c:otherwise>
                                <th>初审审批额度:</th>
                            </c:otherwise>
                        </c:choose>
                        <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0"/></td>
                        <th>审批期限:</th>
                        <td>${item.approvalTerm}</td>
                    </tr>
                    <tr>
                        <th>月还款额:</th>
                        <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0"/></td>
                        <th>内部负债率:</th>
                        <td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                        <th>总负债率:</th>
                        <td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                    </tr>
                    <tr>
                        <th>审批意见:</th>
                        <td colspan="5" rowspan="2" class="W90">${item.approvalMemo}</td>
                    </tr>
                </table>
            </c:if>
            <!-- 是最后一个 -->
            <c:if test="${stat.last}">
                <c:if test="${ not empty item.rtfNodeState}">
                    <h2>第${stat.index+1}次审批</h2>
                    <table class="table_ui W100">
                        <tr>
                            <c:choose>
                                <c:when test="${item.rtfState=='XSZS'}">
                                    <th>终审人员:</th>
                                </c:when>
                                <c:otherwise>
                                    <th>初审人员:</th>
                                </c:otherwise>
                            </c:choose>
                            <td>${item.approvalPersonName}</td>
                            <th>审批时间:</th>
                            <td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/></td>
                        </tr>
                        <tr>
                            <th>申请额度:</th>
                            <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                            <th>申请期限:</th>
                            <td>${item.approvalApplyTerm}</td>
                            <th>审批产品:</th>
                            <td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
                        </tr>
                        <tr>
                            <th>核实收入:</th>
                            <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0"/></td>
                            <c:choose>
                                <c:when test="${item.rtfState=='XSZS'}">
                                    <th>终审审批额度:</th>
                                </c:when>
                                <c:otherwise>
                                    <th>初审审批额度:</th>
                                </c:otherwise>
                            </c:choose>
                            <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0"/></td>
                            <th>审批期限:</th>
                            <td>${item.approvalTerm}</td>
                        </tr>
                        <tr>
                            <th>月还款额:</th>
                            <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0"/></td>
                            <th>内部负债率:</th>
                            <td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                            <th>总负债率:</th>
                            <td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                        </tr>
                        <tr>
                            <th>审批意见:</th>
                            <td colspan="5" rowspan="2" class="W90">${item.approvalMemo}</td>
                        </tr>
                    </table>
                    <h2>信审初审</h2>
                    <table class="table_ui W100">
                        <tr>
                            <th>初审人员:</th>
                            <td>${resEmployeeVO.name}</td>
                        </tr>
                        <tr>
                            <th>申请额度:</th>
                            <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                            <th>申请期限:</th>
                            <td>${applyInfo.baseInfo.applyTerm}</td>
                            <th class="requiredFontWeight">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
                            <td><input type="text" name="approvalProductCd" value="${applyInfo.baseInfo.productCd}" class="input"></td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">核实收入:</th>
                            <td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:','"></td>
                            <th class="requiredFontWeight">
                                初审审批额度:
                                <input id="moneyApprovalOpinion_applyMoney_hidden" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}">
                            </th>
                            <td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
                            <th class="requiredFontWeight">审批期限:</th>
                            <td><input type="text" name="approvalTerm" class="input"></td>
                        </tr>
                        <tr>
                            <th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
                            <td class="approvalMonthPay numFormat"></td>
                            <th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
                            <td class="approvalDebtTate"></td>
                            <th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
                            <td class="approvalAllDebtRate"></td>
                        </tr>
                        <tr>
                            <th class="requiredFontWeight">审批意见:</th>
                            <td colspan="5" rowspan="2">
                                <input ms-attr="{id:'money_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
                                       data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo">
                                <span class="countSurplusText">可输入200字</span>
                            </td>
                        </tr>
                    </table>
                </c:if>
                <c:if test="${empty item.rtfNodeState}">
                    <c:choose>
                        <c:when test="${item.approvalPerson==resEmployeeVO.usercode}">
                            <h2>信审初审</h2>
                            <table class="table_ui W100">
                                <tr>
                                    <th>初审人员:</th>
                                    <td>${resEmployeeVO.name}</td>
                                    <th>审批时间:</th>
                                    <td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/></td>
                                </tr>
                                <tr>
                                    <th>申请额度:</th>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                                    <th>申请期限:</th>
                                    <td>${applyInfo.baseInfo.applyTerm}</td>
                                    <th class="requiredFontWeight">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
                                    <td><input type="text" name="approvalProductCd" value="${item.approvalProductCd}" class="input"></td>
                                </tr>
                                <tr>
                                    <th class="requiredFontWeight">核实收入:</th>
                                    <td><input type="text" name="approvalCheckIncome" value="${item.approvalCheckIncome}" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
                                    <th class="requiredFontWeight">初审审批额度:<input id="moneyApprovalOpinion_applyMoney_hidden" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}">
                                    </th>
                                    <td><input type="text" name="approvalLimit" value="${item.approvalLimit}" class="input" data-options="groupSeparator:','"></td>
                                    <th class="requiredFontWeight">审批期限:</th>
                                    <td><input type="text" name="approvalTerm" value="${item.approvalTerm}" class="input"></td>
                                </tr>
                                <tr>
                                    <th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
                                    <td class="approvalMonthPay numFormat"></td>
                                    <th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
                                    <td class="approvalDebtTate"><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                                    <th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
                                    <td class="approvalAllDebtRate"><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                                </tr>
                                <tr>
                                    <th class="requiredFontWeight">审批意见:</th>
                                    <td colspan="5" rowspan="2">
                                        <input ms-attr="{id:'money_approvalMemo', name: 'approvalMemo'}" value="${item.approvalMemo}" class="easyui-textbox W80"
                                               data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo">
                                        <span class="countSurplusText">可输入200字</span>
                                    </td>
                                </tr>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <h2>第${stat.index+1}次审批</h2>
                            <table class="table_ui W100">
                                <tr>
                                    <c:choose>
                                        <c:when test="${item.rtfState=='XSZS'}">
                                            <th>终审人员:</th>
                                        </c:when>
                                        <c:otherwise>
                                            <th>初审人员:</th>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${item.approvalPersonName}</td>
                                    <th>审批时间:</th>
                                    <td colspan="2"><fmt:formatDate value="${item.lastModifiedDate}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/></td>
                                </tr>
                                <tr>
                                    <th>申请额度:</th>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                                    <th>申请期限:</th>
                                    <td>${item.approvalApplyTerm}</td>
                                    <th>审批产品:</th>
                                    <td><c:if test="${'证大前前'!= item.approvalProductName}">${item.approvalProductName}</c:if></td>
                                </tr>
                                <tr>
                                    <th>核实收入:</th>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalCheckIncome}" pattern="0" maxFractionDigits="0"/></td>
                                    <c:choose>
                                        <c:when test="${item.rtfState=='XSZS'}">
                                            <th>终审审批额度:</th>
                                        </c:when>
                                        <c:otherwise>
                                            <th>初审审批额度:</th>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalLimit}" pattern="0" maxFractionDigits="0"/></td>
                                    <th>审批期限:</th>
                                    <td>${item.approvalTerm}</td>
                                </tr>
                                <tr>
                                    <th>月还款额:</th>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${item.approvalMonthPay}" pattern="0" maxFractionDigits="0"/></td>
                                    <th>内部负债率:</th>
                                    <td><fmt:formatNumber type="number" value="${100*item.approvalDebtTate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                                    <th>总负债率:</th>
                                    <td><fmt:formatNumber type="number" value="${100*item.approvalAllDebtRate}" pattern="0.0" maxFractionDigits="1"/>%</td>
                                </tr>
                                <tr>
                                    <th>审批意见:</th>
                                    <td colspan="5" rowspan="2" class="W90">${item.approvalMemo}<%-- <input class="easyui-textbox W80" data-options="height:'auto',readonly:true,multiline:true" value="${item.approvalMemo}"> --%></td>
                                </tr>
                            </table>
                            <h2>信审初审</h2>
                            <table class="table_ui W100">
                                <tr>
                                    <th>初审人员:</th>
                                    <td>${resEmployeeVO.name}</td>
                                </tr>
                                <tr>
                                    <th>申请额度:</th>
                                    <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                                    <th>申请期限:</th>
                                    <td>${applyInfo.baseInfo.applyTerm}</td>
                                    <th class="requiredFontWeight">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型" value="${item.assetType}"></th>
                                    <td><input type="text" name="approvalProductCd" value="${applyInfo.baseInfo.productCd}" class="input"></td>
                                </tr>
                                <tr>
                                    <th class="requiredFontWeight">核实收入:</th>
                                    <td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
                                    <th class="requiredFontWeight">初审审批额度:<input id="moneyApprovalOpinion_applyMoney_hidden" class="numFormat" type="hidden" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}">
                                    </th>
                                    <td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
                                    <th class="requiredFontWeight">审批期限:</th>
                                    <td><input type="text" name="approvalTerm" class="input"></td>
                                </tr>
                                <tr>
                                    <th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
                                    <td class="approvalMonthPay"></td>
                                    <th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
                                    <td class="approvalDebtTate"></td>
                                    <th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
                                    <td class="approvalAllDebtRate"></td>
                                </tr>
                                <tr>
                                    <th class="requiredFontWeight">审批意见:</th>
                                    <td colspan="5" rowspan="2">
                                        <input ms-attr="{id:'money_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
                                               data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo">
                                        <span class="countSurplusText">可输入200字</span>
                                    </td>
                                </tr>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:if>
        </c:forEach>
        <c:if test="${empty approvalInfo.approvalHistoryList}">
            <h2>信审初次审批</h2>
            <table class="table_ui W100">
                <tr>
                    <th>初审人员:</th>
                    <td>${resEmployeeVO.name}</td>
                </tr>
                <tr>
                    <th>申请额度:</th>
                    <td class="numFormat"><fmt:formatNumber type="number" value="${applyInfo.baseInfo.applyLmt}" pattern="0" maxFractionDigits="0"/></td>
                    <th>申请期限:</th>
                    <td>${applyInfo.baseInfo.applyTerm}</td>
                    <th class="requiredFontWeight">审批产品:<input id="money_selectAssetType_hidden" type="hidden" name="assetType" placeholder="用于记录审批勾选资产类型"></th>
                    <td><input type="text" name="approvalProductCd" value="${applyInfo.baseInfo.productCd}" class="input"></td>
                </tr>
                <tr>
                    <th class="requiredFontWeight">核实收入:</th>
                    <td><input type="text" name="approvalCheckIncome" class="input" data-options="groupSeparator:',',validType:[ 'compareMaxMin[0,99999999]']"></td>
                    <th class="requiredFontWeight">初审审批额度:<input id="moneyApprovalOpinion_applyMoney_hidden" type="hidden" class="numFormat" placeholder="申请额度用于控制审批额度上下限" value="${applyInfo.baseInfo.applyLmt}"></th>
                    <td><input type="text" name="approvalLimit" class="input" data-options="groupSeparator:','"></td>
                    <th class="requiredFontWeight">审批期限:</th>
                    <td><input type="text" name="approvalTerm" class="input"></td>
                </tr>
                <tr>
                    <th>月还款额:<input type="hidden" name="approvalMonthPay"></th>
                    <td class="approvalMonthPay numFormat"></td>
                    <th>内部负债率:<input type="hidden" name="approvalDebtTate"></th>
                    <td class="approvalDebtTate"></td>
                    <th>总负债率:<input type="hidden" name="approvalAllDebtRate"></th>
                    <td class="approvalAllDebtRate"></td>
                </tr>
                <tr>
                    <th class="requiredFontWeight">审批意见:</th>
                    <td colspan="5" rowspan="2">
                        <input ms-attr="{id:'money_approvalMemo', name: 'approvalMemo'}" class="easyui-textbox W80"
                               data-options="multiline:true,width:1000,height:70,prompt:'请输入审批意见',validType: 'max[200]', onChange: limitApprovalMemo">
                        <span class="countSurplusText">可输入200字</span>
                    </td>
                </tr>
            </table>
        </c:if>
    </form>
</div>
<!-- ---------------------------------------End 审批意见--------------------------------------->
<!-- 征信初判 -->
<div id="moneyApprovalOpinion_contrast_Dialog" class="padding_20 display_none">
    <table class="table_ui W100" id="moneyApprovalOpinion_credit_table"></table>
    <table class="table_list W100" id="moneyApprovalOpinion_debt_table"></table>
</div>
<div class="h20"></div>
<div class="float_right">
    <a class="easyui-linkbutton_ok05 l-btn l-btn-small" onclick="submit()">确认</a>&nbsp;&nbsp;
    <a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="closeHTMLWindow()">取消</a>&nbsp; &nbsp;
</div>
</body>
<script src="${ctx}/resources/js/core/coreApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bms/bmsBasicApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/zdmoney/moneyApprovalOpinion.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'page',
        loanNo: '${loanNo}',
        approvalInfo: ${approvalInfoJson == null ? '{}' : approvalInfoJson},
        applyBasicInfo: ${applyBasicInfoJson == null ? '{}' : applyBasicInfoJson},
        applyInfo: ${applyInfoJson == null ? '{}' : applyInfoJson},
        markReportIdChange: '${markReportIdChange}',
        markIsExecuteEngine: ${markIsExecuteEngine == null ? 'false' : markIsExecuteEngine},
        reportInfo: ${reportInfoJson == null ? '{}' : reportInfoJson},
        loanLimitInfo: ${loanLimitInfoJson == null ? 'null' : loanLimitInfoJson},
        ifCreditRecord: ${ifCreditRecord},
        topupLoan:${topupLoanJson == null ?'{}':topupLoanJson},
        selectAssetType:$("#money_selectAssetType_hidden").val(),
        nowYear:${nowYear},// 当前年
    });
</script>
</html>