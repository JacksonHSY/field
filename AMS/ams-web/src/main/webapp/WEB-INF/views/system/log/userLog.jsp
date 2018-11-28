<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style>
	.log_condition {
		margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;
	}
</style>
<div class="easyui-panel">
	<form id="logManage_Query_Form" class="margin_20">
		<table class="table_ui W100">
			<tr>
				<th width="65">借款编号:</th>
				<td width="180"><input type="text" class="easyui-textbox input log_condition" name="loanNo" data-options="prompt:'借款编号'"></td>
				<th width="65">员工编号:</th>
				<td width="180"><input type="text" class="easyui-textbox input log_condition" name="userCode" data-options="prompt:'员工编号'"></td>
				<th width="65">审批环节:</th>
				<td width="180">
					<select name="link" class="easyui-combobox select log_condition" data-options="editable:false">
						<option value="" selected="selected">请选择</option>
						<option value="信审初审">信审初审</option>
						<option value="信审终审">信审终审</option>
						<option value="复核确认">复核确认</option>
						<option value="是否接单">是否接单</option>
					</select>
				<td>
					<a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
					<a class="easyui-linkbutton" onclick="userLogQuery()"><i class="fa fa-search"></i>搜&nbsp;索</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<table id="logManage_datagrid"></table>
<script type="text/javascript" src="${ctx}/resources/js/system/log/userLog.js"></script>