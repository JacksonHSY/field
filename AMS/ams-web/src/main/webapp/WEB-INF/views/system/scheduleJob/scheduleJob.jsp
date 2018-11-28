<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel">
	<form id="timedTask_Query_Form" class="margin_20">
		<table class="table_ui W100">
			<tr>
				<th width="65">任务名称</th>
				<td width="180"><input type="text" class="easyui-textbox input" data-options="prompt:'任务名称'" name="jobName" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;"></td>
				<td><a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; <a class="easyui-linkbutton " onclick="scheduleJobQurey()"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<div id="timedTask_toolBarBtn">
		<a class="easyui-linkbutton" onclick="timedTask_Dialog('新建')" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>&nbsp; <a class="easyui-linkbutton" onclick="scheduleJobDelete()" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
	</div>
	<table id="timedTask_datagrid"></table>
</div>
<div id="timedTask_AddOrUpdate_dialog" class="display_none">
	<form id="timedTask_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>任务名称:</th>
				<td><input type="text" class="easyui-textbox input" name="jobName" data-options="prompt:'任务名称'"></td>
				<th>任务分组:</th>
				<td><input type="text" class="easyui-textbox input" name="jobGroup" data-options="prompt:'任务分组'"></td>
			</tr>
			<tr>
				<th>是否有效:</th>
				<td>
					<select class="easyui-combobox select" name="jobStatus" data-options="editable:false,panelHeight:'auto'">
							<option value="0">启用</option>
							<option value="1">禁用</option>
<!-- 							<option value="2">删除</option> -->
					</select>
				</td>
				<th>类名:</th>
				<td><input type="text" class="easyui-textbox input" name="remark" data-options="prompt:'类名'"></td>
			</tr>
			<tr>
				<th>执行时间:</th>
				<td><input type="text" class="easyui-textbox input" name="cronExpression" data-options="prompt:'任务执行时间'"></td>
			</tr>
			<tr>
				<th>任务描述:</th>
				<td colspan="3"><textarea class="textarea" name="jobDesc"></textarea></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/scheduleJob/scheduleJob.js"></script>