<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${applyBasiceInfo.name}-办理</title>
    <jsp:include page="../../common/commonJS.jsp"></jsp:include>
    <jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; right: 0px; top: 7px;">消息提醒</div>
<div class="xx_dd_yuan" id="ruleEngineHint_number_div">${fn:length(ruleEngineResult)-2}</div>
<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()"
     style="right: 40px;">
    <div class="xx_dd_tit">消息提醒</div>
    <ul>
        <c:forEach var="item" items="${ruleEngineResult}" begin="2">
            <li>${item}<br><span>${ruleEngineResult[1]}</span></li>
        </c:forEach>
    </ul>
</div>
<input type="hidden" id="final_limitFinishSubmitDisable" value="${limitFinishSubmitDisable}" placeholder="规则引擎返回类型">
<div class="float_left W60" style="height: 750px;">
    <iframe src="${picImageUrl}/api/filedata?nodeKey=${picApproval}&sysName=${sysCode}&appNo=${applyBasiceInfo.loanNo}&operator=${operator}&jobNumber=${jobNumber}"
            style="width: 100%; height: 99.5%; padding: 0px; margin: 0px; border: 0px; display: table"></iframe>
</div>
<div class="float_right W40">
    <div class="easyui-panel padding_20" title="审批办理">
        <input id="version_id" type="hidden" value="${applyBasiceInfo.version}"><input id="final_taskType" type="hidden"
                                                                                       value="${taskType}">
        <table class="table_ui W100  center_m">
            <tr>
                <td><a id="finish_customerInfo_btn" class="easyui-linkbutton" onclick="finishCustomerInfo('${applyBasiceInfo.loanNo}')"><span
                        class="icon_01"></span>客户信息</a></td>
                <td><a id="finish_insideMatch_btn" class="easyui-linkbutton" onclick="finishInsideMatchDialog('${applyBasiceInfo.loanNo}')"><span
                        class="icon_02"></span>内部匹配</a></td>
                <td><a id="finish_telephone_btn" class="easyui-linkbutton"
                       onclick="hasSearch_finishTelephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><span class="icon_03"></span>电核汇总</a>
                </td>
                <td><a id="finish_logNotes_btn" class="easyui-linkbutton" onclick="finishLogNotesInfoDialog('${applyBasiceInfo.loanNo}')"><span
                        class="icon_04"></span>日志备注</a></td>
            </tr>
            <tr>
                <td><a  id="finish_centralBankCredit_btn" class="easyui-linkbutton"
                       onclick="finishCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')"><span
                        class="icon_05"></span>央行征信</a><input id="finish_reportId_hidden" type="hidden" value="${applyBasiceInfo.reportId}"></td>
                <td><a id="finish_exteranlCredit_btn" class="easyui-linkbutton" onclick="finishExternalCreditDialog('${applyBasiceInfo.loanNo}')"><span
                        class="icon_06"></span>外部征信</a></td>
                <td><a id="finish_suanHuaCredit_btn" class="easyui-linkbutton" onclick="finishSuanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
                <td><a id="finish_approvalOpinion_btn" class="easyui-linkbutton"
                       onclick="finishApprovalOpinionDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.version}')"><span
                        class="icon_07"></span>审批意见</a></td>
            </tr>
        </table>
        <hr>
        <form>
            <table class="table_ui W100 center_m">
                <tr>
                    <th>借款编号:</th>
                    <td id="finish_approve_loanNo">${applyBasiceInfo.loanNo}</td>
                    <th>身份证号码:</th>
                    <td>${applyBasiceInfo.idNo}</td>
                </tr>
                <tr>
                    <th>申请人姓名:</th>
                    <td>${applyBasiceInfo.name}</td>
                    <th>申请产品:</th>
                    <td><c:if test="${'证大前前' != applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
                </tr>

                <tr>
                    <th>申请期限:</th>
                    <td>${applyBasiceInfo.applyTerm}</td>
                    <th>申请额度:</th>
                    <td class="numFormat"><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}"
                                                            pattern="0" maxFractionDigits="0"/></td>
                </tr>
                <tr>
                    <th>借款用途:</th>
                    <td>${applyBasiceInfo.creditApplication}</td>
                    <th>进件营业部:</th>
                    <td>${applyBasiceInfo.owningBranch}</td>
                </tr>
                <tr>
                    <th>是否加急:</th>
                    <td><c:if test="${applyBasiceInfo.ifPri==0}">否</c:if> <c:if
                            test="${applyBasiceInfo.ifPri ==1}">是</c:if></td>
                    <th>客户经理:</th>
                    <td>${applyBasiceInfo.barnchManagerName}</td>
                </tr>
                <tr>
                    <th>备注</th>
                    <td colspan="3" class="W80">${applyBasiceInfo.remark}</td>
                </tr>
                <!-- start反欺诈 -->
                <%-- <tr>
                    <th>反欺诈评分:</th>
                    <td>${applyBasiceInfo.antiFraudScore}</td>
                    <th>欺诈风险评估:</th>
                    <td>${applyBasiceInfo.antiRiskRate}</td>
                    <th>反欺诈预警:</th>
                    <td>${applyBasiceInfo.antiFraudWarning}</td>
                </tr>--%>
                <tr>
                    <th>众安反欺诈等级:</th>
                    <td>${applyBasiceInfo.zhongAnRiskGrade}</td>
                    <%--<th>众安反欺诈结果:</th>
					<td>${applyBasiceInfo.zhongAnRiskResult}</td>--%>
                    <%--<th>芝麻信用分:</th>--%>
                    <%--<td>${applyBasiceInfo.sesameCreditValue}</td>--%>
                    <th>综合信用评级:</th>
                    <td id="finish_comCreditRating">${applyBasiceInfo.comCreditRating}</td>
                </tr>
                <tr>
                    <th id="finishApprove_isAntifraud" class="markRed"><c:if
                            test="${'Y' == applyBasiceInfo.isAntifraud}">欺诈可疑</c:if></th>
                    <td colspan="3"></td>
                </tr>
                <!-- end反欺诈 -->
            </table>
        </form>
        <hr>
        <table class="table_ui W100 center_m">
            <tr>
                <td><a class="easyui-linkbutton_ok01 l-btn l-btn-small"
                       onclick="hangUp('${applyBasiceInfo.loanNo}','${applyBasiceInfo.rtfNodeState}','${taskType}','${applyBasiceInfo.name}')">挂起</a>
                </td>
                <td><a id="finalApproval_submit_btn" class="easyui-linkbutton_ok02 l-btn l-btn-small">提交</a></td>
                <td><a class="easyui-linkbutton_ok03 l-btn l-btn-small"
                       onclick="finishApprovalBackDialog('${applyBasiceInfo.loanNo}','${taskType}','${applyBasiceInfo.name}','${applyBasiceInfo.zdqqApply}')">退回</a>
                </td>
                <td><a class="easyui-linkbutton_ok04 l-btn l-btn-small"
                       onclick="finishApprovalRefuseDialog('${applyBasiceInfo.loanNo}','${taskType}','${applyBasiceInfo.name}')">拒绝</a>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- 拒绝弹出框 -->
<div id="finishApproveReceive_refuse_dialog" class="padding_20 display_none">
    <form id="finishApproveReceive_refuse_Form" class="margin_20">
        <table class="table_ui WH100">
            <tr>
                <th>一级原因:</th>
                <td><select class="easyui-combobox select" id="finishApproveReceive_refuse_ParentCode"
                            name="firstReason"
                            data-options="editable:false,isClearBtn:true,required:true"></select><input type="hidden"
                                                                                                        name="firstReasonText"><input
                        type="hidden" value="none" id="finish_ApproveReceive_conType" name="conditionType"></td>
                <th>二级原因:</th>
                <td><select class="easyui-combobox select" id="finishApproveReceive_refuse_ReasonCode"
                            name="secondReason" data-options="editable:false,isClearBtn:true"></select><input
                        type="hidden" name="secondReasonText"></td>
            </tr>
            <tr>
                <th>备注信息:</th>
                <td colspan="5"><input id="remark" class="easyui-textbox W30" name="remark"
                                       data-options="height:80,width:804,validType:'length[1,200]',multiline:true"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 退回弹出框 -->
<div id="finishApproveReceive_back_dialog" class="padding_20 display_none">
    <input id="finish_remak_teturnType" type="hidden" value="${applyBasiceInfo.zdqqApply}" placeholder="标记是否是前前进件">
    <form>
        <table class="table_ui WH100">
            <tr>
                <th>退回类型:</th>
                <td colspan="3">
                    <label><input name="backType" type="radio" checked="checked"class="easyui-validatebox"
                        <c:if test="${1 ==applyBasiceInfo.zdqqApply}"> value="ZSRTNQQ" />退回前前</c:if>
                        <c:if test="${1 !=applyBasiceInfo.zdqqApply}"> value="ZSRTNLR" />退回门店</c:if></label>
                    <label><input name="backType" type="radio" class="easyui-validatebox" value="ZSRTNCS"/>退回初审</label>
                </td>
            </tr>
            <tr class="markReturnReason">
                <th>一级原因:</th>
                <td><input class="select" id="finishApproveReceive_back_ParentCode" name="firstReason"></input><input type="hidden" name="firstReasonText"></td>
                <th>二级原因:</th>
                <td><input class="input" id="finishApproveReceive_back_ReasonCode" name="secondReason" data-options="multiple:${1==applyBasiceInfo.zdqqApply},separator:'|'"></input><input type="hidden" name="secondReasonText"></td>
                <td class="markIsAdd"><c:if test="${1==applyBasiceInfo.zdqqApply}"><a href="javaScript:void(0);" onclick="addOrDeleteRetureReason('ADD')"><i class="fa fa-plus" aria-hidden="true"></i></a></c:if></td>
            </tr>
            <tr>
                <th>姓名:</th>
                <td colspan="4">${applyBasiceInfo.name}</td>
            </tr>
            <tr>
                <th>备注信息:</th>
                <td colspan="4"><input class="easyui-textbox W30" id="remark" name="remark" data-options="height:80,width:642,validType:'length[1,200]',multiline:true"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 挂起弹出框 -->
<div id="finishApproveReceive_hang_dialog" class="display_none padding_20">
    <form>
        <table class="table_ui WH100">
            <tr>
                <th>挂起原因:</th>
                <td><input id="finish_hangList_combobox" name="firstReason" class="input"><input type="hidden"
                                                                                                 name="firstReasonText">
                </td>
            </tr>
            <tr>
                <th>备注信息:</th>
                <td colspan="2"><input id="remark" class="easyui-textbox W30" name="remark"
                                       data-options="height:80,width:320,validType:'length[1,200]',multiline:true"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 提交弹出框 -->
<div id="finishApproveReceive_submit_dialog" class="padding_20 display_none">
    <form>
        <table class="table_ui WH100">
            <tr>
                <th>审批额度(元):</th>
                <td></td>
                <th>审批期限:</th>
                <td></td>
            </tr>
            <tr>
                <th>内部负债率:</th>
                <td></td>
                <th>总负债率:</th>
                <td></td>
            </tr>
            <tr>
                <th>备注信息:</th>
                <td colspan="3"><input id="remark" class="easyui-textbox W30" name="remark"
                                       data-options="height:80,width:400,validType:'length[1,200]',multiline:true"></td>
            </tr>
        </table>
    </form>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApproveReceive.js"></script>
</html>
