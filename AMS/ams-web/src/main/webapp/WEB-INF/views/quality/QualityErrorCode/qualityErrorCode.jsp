<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<div class="easyui-panel W100" data-options="collapsible:true">
	<form id="errorCode_queryForm" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>差错代码:</th>
				<td><input type="text" name="code" class="easyui-textbox input" data-options="prompt:'名称'"></td>
				</tr>
			<tr>
				<td colspan="5"></td>
				<td colspan="2">
					<a class="easyui-linkbutton" id="errorQuery_reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; 
					<a class="easyui-linkbutton" id="errorQuery_Query"><i class="fa fa-search"></i>搜&nbsp;索</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="errorCode_AddOrUpdate_dialog" class="display_none">
	<form id="errorCode_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>差错代码:</th>
				<td><input type="text" class="easyui-textbox input" id="code" name="code" data-options="prompt:'原因编码',required:true"></td>
				<th>是否开启:</th>
				<td><select class="easyui-combobox select" name="status" id="status" data-options="editable:false,required:true,panelHeight:'auto'">
						<option value="1">否</option>
						<option value="0">是</option>
				</select></td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<div id="errorCode_toolBarBtn">
		<a class="easyui-linkbutton" id="errorCode_Dialog_Open" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>&nbsp; <a class="easyui-linkbutton" id="errorCode_Delete" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
	</div>
	<table id="errorCode_datagrid"></table>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityErrorCode.js"></script>
