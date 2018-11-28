<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 通过件拒绝审批信息弹框 -->
<div id="pass_deny_Dialog" class="padding_20">
	<input type="hidden" value="${rtfStatus}" id = "info_deny_rtf_status_id">
	<form id="pass_deny_Form">
		<table class="table_ui WH100">
			<tr>
				<th>一级原因:</th>
				<td><input id="first_deny_Id" name="primaryReason" class="input"></td>
				<th>二级原因:</th>
				<td><input id="end_deny_Id"  name="secodeReason" class="input"></td>
			</tr>
			<tr>
				<th>备注信息:</th>
				<td colspan="5"><input class="easyui-textbox W30" name="remark" data-options="height:80,width:790,validType:'length[1,200]',multiline:true"></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
/**
 * 初始化一级原因二级原因
 */
 $(function() {
	 var rtfStatus = $("#info_deny_rtf_status_id").val();
	 initClassAReason("#first_deny_Id", "SQJXXWH", "reject", "#end_deny_Id");
 });
</script>