<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-tabs">
	<div title="待办任务">
		<div class="easyui-panel padding_20">
			<h3>
				<label style="cursor: pointer;">&nbsp;是否接单</label>&nbsp;&nbsp;<input id="reconsideraction_orders_radio" type="radio" data-options="checked:${'Y' == ifAccetp}">
			</h3>
			<form id="reconsideration_task_form">
				<table class="table_ui W100">
					<tr>
						<th>借款编号:</th>
						<td><input type="text" class="easyui-textbox input" name="loanNo" data-options="prompt:'借款编号',validType:'loanNo'"></td>
						<th>申请人姓名:</th>
						<td><input type="text" class="easyui-textbox input" name="name" data-options="prompt:'申请人姓名',validType:'customerName'"></td>
						<th>身份证号码:</th>
						<td><input type="text" class="easyui-textbox input" name="idNo" data-options="prompt:'身份证号码',validType:'IDCard'"></td>
						<td>&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;</td>
						<td><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a class="easyui-linkbutton" onclick="reconsiderationTask()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<table id="reconsideration_query_datagrid"></table>
	</div>
	<div title="已完成任务">
		<table id="reconsideration_finish_query_datagrid"></table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/apply/reconsideration.js"></script>