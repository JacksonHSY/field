<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel">
	<form id="bankInfo_Query_Form" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>银行编号:</th>
				<td><input type="text" class="easyui-textbox input" name="code" data-options="prompt:'银行编号'"></td>
				<th>银行名称:</th>
				<td><input type="text" class="easyui-textbox input" name="bankName" data-options="prompt:'银行名称'"></td>
				<th>银行类型:</th>
				<td><select class="easyui-combobox select" id="bankInfo_Query_BankType" name="bankType" data-options="editable:false,panelHeight:'auto'">
						<option></option>
						<option value="0">证大投资</option>
						<option value="1">非证大投资</option>
				</select></td>
				<td><a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; <a class="easyui-linkbutton" onclick="bankInfoQuery()"><i class="fa fa-search"></i>查&nbsp;询</a></td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<div id="bankInfo_toolBarBtn">
		<a href="#" class="easyui-linkbutton" onclick="bankInfoOpen()" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>&nbsp; 
		<a href="#" class="easyui-linkbutton" onclick="bankInfoDelete()" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
	</div>
	<table id="bankInfo_datagrid"></table>
</div>
<div id="bankInfo_AddOrUpdate_dialog" class="display_none">
	<form id="bankInfo_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" id="bankInfo_AddOrUpdate_Id" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>银行编号:</th>
				<td><input type="text" class="easyui-textbox input" id="bankInfo_AddOrUpdate_Code" name="code" data-options="prompt:'原因编码',required:true"></td>
				<th>银行名称:</th>
				<td><input type="text" class="easyui-textbox input" id="bankInfo_AddOrUpdate_bankName" name="bankName" data-options="prompt:'原因名称',required:true"></td>
			</tr>
			<tr>
				<th>银行类型:</th>
				<td><select class="easyui-combobox select" id="bankInfo_AddOrUpdate_BankType" name="bankType" data-options="editable:false,panelHeight:'auto',required:true">
						<option></option>
						<option value="0">证大投资</option>
						<option value="1">非证大投资</option>
				</select></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/bankInfo/bankInfo.js"></script>