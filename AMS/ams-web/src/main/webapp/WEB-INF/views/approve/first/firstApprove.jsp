<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="firstApprove_task_tabs" class="easyui-tabs">
	<div title="待办任务">
		<h3>
			<label style="cursor: pointer;">&nbsp;是否接单</label>&nbsp;&nbsp;<input id="firstApprove_orders_radio" type="radio"> <input type="hidden" id="staffCode" value="${staffCode}">
		</h3>
		<table id="firstApprove_priorityTask_datagrid"></table>
		<table id="firstApprove_normalTask_datagrid"></table>
		<table id="firstApprove_hangUpTask_datagrid"></table>
	</div>
	<div title="已完成任务" class="W100">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="firstApprove_finish_form" class="margin_20">
				<table class="table_ui W60">
					<tr>
						<th>完成时间起:</th>
						<td><input type="text" id="firstApprove_dateStart_datebox" name="startDate" class="easyui-datebox input" data-options="required:true,editable:false,prompt:'完成开始时间',validType:['date','compareToday','compareOffset[\'start\',\'#firstApprove_dateEnd_datebox\',30]']"></td>
						<th>至</th>
						<td><input id="firstApprove_dateEnd_datebox" name="endDate" type="text" class="easyui-datebox input" data-options="required:true,prompt:'完成结束时间',editable:false,validType:['date','compareToday','compareDate[\'#firstApprove_dateStart_datebox\']','compareOffset[\'end\',\'#firstApprove_dateStart_datebox\',30]']"></td>
						<td><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a class="easyui-linkbutton" onclick="firstApproveSearch()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<table id="firstApprove_finishTask_datagrid"></table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstApprove.js"></script>