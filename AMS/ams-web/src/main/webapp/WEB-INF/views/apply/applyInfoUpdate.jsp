<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 通过件修改审批信息弹框 -->
<div id="applyInfo_pass_update_Dialog" class="padding_20" ms-controller="page">
	<form id="applyInfoManage_pass_update_Form">
		<input type="hidden" name="productCode" value="${applyDoc.productCode}" id="applyDoc_productCode">
		<input type="hidden" name="loanNo" value="${applyDoc.loanNo}" id="loan_no">
		<input type="hidden" name="owningBranchId" value="${applyDoc.owningBranchId}" id="applyDoc_owningBranchId">
		<input type="hidden" name="applyLmt" value="${applyDoc.applyLmt}" id="applyDoc_applyLmt">
		<table class="table_ui WH100" id="applyInfoManage_pass_update_table">
			<tr>
				<th>审批额度:</th>
				<td id="old_acc_lmt" class="numFormat">${applyDoc.accLmt}</td>
				<th>审批期限:</th>
				<td id="old_acc_term">${applyDoc.accTerm}</td>
			</tr>
			<tr>
				<th>审批额度:</th>
				<td>
					<input name="accLmt" id="acc_lmt"  class="input" value="${applyDoc.accLmt}">
				</td>
				<th>审批期限:</th>
				<td>
					<input name="accTerm" id="acc_term" class="select" value="${applyDoc.accTerm}">
				</td>
			</tr>
			<tr>
				<th>备注:</th>
				<td colspan="5">
					<input class="W30" name="remark">
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script>
var vm = avalon.define({
	$id: 'page',
	applyDoc: ${applyDocJson},
    basicInfoJson: ${basicInfoJson},
    contractAmtLimit: ${contractAmtLimit}
});

$(function ($) {
    $('#acc_lmt').numberbox({
        required:true,
        missingMessage: '请填写审批额度',
        tipPosition: 'top',
        groupSeparator:',',
        onChange: function(newValue, oldValue) {
            $('#acc_term').combobox('reload', ctx.rootPath() + '/applyDoc/getApprovalPeriodList/' + vm.applyDoc.loanNo + "/" + newValue)
        }
	});

	$('#acc_term').combobox({
        required:true,
        missingMessage: '请选择审批期限',
        tipPosition: 'top',
        editable:false,
        valueField:'auditLimit',
        textField:'auditLimit',
        url:'${ctx}/applyDoc/getApprovalPeriodList/${applyDoc.loanNo}/${applyDoc.accLmt}',
        onLoadSuccess: function(){
            // 若没有审批期限则提示该审批额度无可审批期限，请修改审批额度
            var approvalTermData = $("#acc_term").combobox("getData");
            if (isEmpty(approvalTermData)) {
                $.info('提示', '该产品当前审批额度无可审批期限，请修改审批额度');
            }
            // 选中原始值
            var approvalTerm = $('#acc_term').combobox("getValue");// 获取原始值
            if (isNotNull(approvalTerm)) {
                var defVal = $('#acc_term').combobox("options").finder.getRow($('#acc_term')[0], approvalTerm);
                if (isNotNull(defVal)) {
                    $('#acc_term').combobox("select", approvalTerm);
                } else {
                    $('#acc_term').combobox("setValue", "");
                }
            }
            flag = true;
            reloadApprovalLimit();
        },
        onChange: reloadApprovalLimit
	});

	$('input[name="remark"]').textbox({
        height:80,
		width:586,
		validType:'length[1,200]',
		multiline:true
	});
});
// $("#applyInfo_pass_update_Dialog").mouseleave(function(){
//     alert("out");
//
// });

/**
 * 重新加载产品对应的审批额度上下限
 * @param newValue
 * @param oldValue
 * @author wulj
 */
function reloadApprovalLimit(newValue, oldValue) {
    var auditLimit = $("#acc_term").combobox("getValue");		// 审批期限
    var productLimit = bmsBasicApi.getProductUpperLower(vm.applyDoc.productCode, vm.applyDoc.owningBranchId, auditLimit);
    var maxNumber = parseInt(vm.applyDoc.applyLmt);
    if (isNotNull(productLimit.upperLimit)) {
        // 如果"产品审批上限" > "客户申请额度"，则"客户申请额度"作为"审批额度"的上限值
        maxNumber = parseFloat(productLimit.upperLimit) >= parseFloat(vm.applyDoc.applyLmt) ? vm.applyDoc.applyLmt : productLimit.upperLimit;
    }
    $("#acc_lmt").numberbox({
        validType : [ 'compareMaxMin[' + (productLimit.floorLimit || 0) + ',' + parseInt(maxNumber) + ']', 'numberMultiple[1000]' ]
    });
}
</script>