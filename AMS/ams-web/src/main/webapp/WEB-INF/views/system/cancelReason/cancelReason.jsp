<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel">
	<form id="cancelReason_Query_Form" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>原因编码:</th>
				<td>
					<input type="text" class="easyui-textbox input" name="reasonCode" data-options="prompt:'原因编码'">
				</td>
				<th>原因名称:</th>
				<td>
					<input type="text" class="easyui-textbox input" name="reasonName" data-options="prompt:'原因名称'">
				</td>
				<th>原因类型:</th>
				<td>
					<select class="easyui-combobox select" id="cancelReason_Query_ReasonType" name="reasonType" data-options="editable:false,panelHeight:'auto'">
						<option value="R00">回退信审</option>
						<option value="R01">回退门店</option>
					</select>
				</td>
				<td>
					<a class="easyui-linkbutton" id="cancelReason_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
					<a class="easyui-linkbutton" id="cancelReason_Query"><i class="fa fa-search"></i>查&nbsp;询</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<div id="cancelReason_toolBarBtn">
		<a href="#" class="easyui-linkbutton" id="cancelReason_Dialog_Open" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>&nbsp;
		<a href="#" class="easyui-linkbutton" id="cancelReason_Delete" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
	</div>
	<table id="cancelReason_datagrid"></table>
</div>
<div id="cancelReason_AddOrUpdate_dialog" class="display_none">
	<form id = "cancelReason_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>原因编码:</th>
				<td><input type="text" class="easyui-textbox input" id="cancelReason_AddOrUpdate_ReasonCode" name="reasonCode" data-options="prompt:'原因编码',required:true"></td>
				<th>原因名称:</th>
				<td><input type="text" class="easyui-textbox input" name="reasonName" data-options="prompt:'原因名称',required:true"></td>
			</tr>
			<tr>
				<th>父级编码:</th>
				<td>
					<select class="select" id="cancelReason_AddOrUpdate_ParentCode" name="parentCode" data-options="editable:false,isClearBtn:true"></select>
				</td>
				<th>限制再提交天数:</th>
				<td><input type="text" class="easyui-textbox input" name="submitDays" data-options="prompt:'限制再提交天数',required:true,panelHeight:'auto'"></td>
			</tr>
			<tr>
				<th>原因类型:</th>
				<td>
					<select class="easyui-combobox select" name="reasonType" data-options="editable:false,required:true,panelHeight:'auto'">
						<option></option>
						<option value="R00">回退信审</option>
						<option value="R01">回退门店</option>
					</select>   
				</td>
				<th>是否启用:</th>
				<td>
					<select class="easyui-combobox select" name="status" data-options="editable:false,required:true,panelHeight:'auto'">
						<option></option>
						<option value="0">是</option>
						<option value="1">否</option>
					</select>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/cancelReason/cancelReason.js"></script>