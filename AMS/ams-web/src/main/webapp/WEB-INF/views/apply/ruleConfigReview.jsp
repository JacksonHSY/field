<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style>
.datagrid-row-selected {
	background: white;
	color: black;
}
.datagrid-row-over {
	background: white;
	color: black;
}
</style>
<!-- 组长复核弹框 -->
<div id="ruleConfiguration_leaderReview_Dialog" class="padding_20">
	<!-- 新增收回权限 -->
	<div class="h20"></div>
	<div id="rule_conf_datagrid_tool">
		<a onclick="addBackDuty()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-external-link" aria-hidden="true"></i>新&nbsp;增</a>
	</div>
	<table id="rule_conf_list"></table>
</div>

<!-- 点击数量按钮弹框展示拒绝和退回原因 -->
<div id="user_rule_detail_dialog" class="display_none">
	<div class="float_left W50">
		<ul id="detail_refuse_tree" class="easyui-tree display_none"></ul>
	</div>
	<div class="float_left W50">
		<ul id="detail_return_tree" class="easyui-tree display_none"></ul>
	</div>
</div>

<!--新增收回权限弹框 -->
<div id="ruleConfiguration_backDuty_dialog" class="display_none">
	<form id="ruleConfiguration_add_form">
		<input id="rule_config_id" name="id" type="hidden">
		<table class="table_ui W100">
			<tr>
				<th>大组:</th>
				<td><input name="orgPnameId" id="bigGroup_id" class="input"></td>
			</tr>
			<tr>
				<th>小组:</th>
				<td><input name="orgNameId" id="smallGroup_id" class="input"></td>
			</tr>
			<tr>
				<th>姓名:</th>
				<td><input name="userCode" id="user_Id" class="input"></td>
			</tr>
			<tr>
				<th>收回权限类型:</th>
				<td><input name="ruleType" id="ruleType_id" class="input" /></td>
			</tr>
			<tr id="ruleConfigReview_reason_tr" class="display_none">
				<th>拒绝/退回原因一级原因:</th>
				<td><input name="reasonCode" id="reason_id" class="input"></td>
			</tr>
			<tr id="ruleConfigReview_reason_tr_b" class="display_none">
				<th>拒绝/退回原因二级原因:</th>
				<td><input name="reasonCodeB" id="reason_id_b" class="input"></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/apply/ruleConfiguration.js"></script>