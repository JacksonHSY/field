<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="calendarMaintain_Calendar" class="margin_20" style="width:600px;height:500px;">
</div>
<div id="calendarMaintain_AddOrUpdate_dialog" class="display_none">
	<form id="calendarMaintain_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>日期:</th>
				<td><input type="text" class="easyui-textbox input" id="calendarMaintain_date"></td>
			</tr>
			<tr>
				<th>类型:</th>
				<td>
					<select class="easyui-combobox select" data-options="editable:false">
						<option></option>
						<option value="0">节假日</option>
						<option value="1">工作日</option>
					</select>
				</td>
				<th>有效范围:</th>
				<td><select class="easyui-combobox select" data-options="editable:false,panelHeight:'auto',required:true">
						<option value="0">同用</option>
						<option value="1">借款管理系统</option>
						<option value="2">录单管理系统</option>
						<option value="3">征审管理系统</option>
				</select></td>
			</tr>
			<tr>
				<th>有效时间：</th>
				<td>
					<input class="easyui-datebox input" data-options="prompt:'有效时间',validType:'date'" id="calendarMaintain_Query_Start">
				</td>
				<th>至：</th>
				<td>
					<input type="text" class="easyui-datebox input" data-options="prompt:'有效时间'">
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/calendarMaintain/calendarMaintain.js"></script>