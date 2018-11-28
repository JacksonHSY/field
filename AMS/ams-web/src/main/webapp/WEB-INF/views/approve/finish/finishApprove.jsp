<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="finishApprove_task_tabs" class="easyui-tabs">
	<div title="待办任务">
		<h3>
			<label style="cursor: pointer;">&nbsp;是否接单</label>&nbsp;&nbsp;<input id="finishApprove_orders_radio" type="radio">
		</h3>
		<table id="finishApprove_priorityTask_datagrid"></table>
		<table id="finishApprove_normalTask_datagrid"></table>
		<table id="finishApprove_hangUpTask_datagrid"></table>
	</div>
	<div title="已完成任务" class="W100">
		<div class="easyui-panel W100" data-options="collapsible:true" >
			<form class="margin_20" id="finish_approval_Query_Form">
				<table class="table_ui W60">
					<tr>
						<th>完成时间起:</th>
						<td><input name="offStartDate" type="text" class="easyui-datebox input" data-options="editable:false,required:true,prompt:'完成开始时间',validType:['date','compareToday'],required:true" id="finishApproveCompleteDate"></td>
						<th>至:</th>
						<td><input name="offEndDate" type="text" class="easyui-datebox input" data-options="editable:false,required:true,prompt:'完成结束时间',validType:['date','compareToday','compareDate[\'#finishApproveCompleteDate\']']" id="finishApproveCompleteEndDate"></td>
						<td colspan="2"><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a onclick="finishApproveQuery()" class="easyui-linkbutton"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<table id="finishApprove_finishTask_datagrid"></table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishApprove.js"></script>
