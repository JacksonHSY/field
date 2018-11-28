<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- 拒绝件修改拒绝原因弹框 -->
<script type="text/javascript">
/**
 * 初始化一级原因二级原因
 */
 $(function() {
	 var rtfStatus = $("#deny_update_rtf_status_id").val();
	 initClassAReason("#reject_apply_first_deny_Id", "SQJXXWH", "reject", "#reject_apply_end_deny_Id");
 });
 
</script>

<div id="deny_update_Dialog" class="padding_20 ">
	<input type="hidden" value="${rtfStatus}" id = "deny_update_rtf_status_id">
	<form id="deny_update_Form">
		<table class="table_ui WH100" id="applyInfoManage_deny_update_table">
			<tr>
				<th>原一级拒绝原因:</th>
				<td>${applyDoc.primaryReasonText}</td>
				<th>原二级拒绝原因:</th>
				<td>${applyDoc.secodeReasonText}</td>
			</tr>
			<tr>
				<th>一级拒绝原因:</th>
				<td><input id="reject_apply_first_deny_Id" name="primaryReason" class="input"></td>
				<th>二级拒绝原因:</th>
				<td><input id="reject_apply_end_deny_Id"  name="secodeReason" class="input"></td>
			</tr>
			<tr>
				<th>备注:</th>
				<td colspan="5"><input class="easyui-textbox W30" name="remark" data-options="height:80,width:788,validType:'length[1,200]',multiline:true"></td>
			</tr>
		</table>
	</form>
</div>
