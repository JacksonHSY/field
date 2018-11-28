<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',split:true" title="菜单" style="width:15%;">
		<div id="dictionary_tree"></div>
	</div>
	<div data-options="region:'center',split:true" class=" W90" style="width:85%;">
		<div class="easyui-panel padding_20 W100" data-options="border:'0'">
			<form id="dictionary_Query_Form">
				<table class="table_ui W60">
					<tr>
						<th>字典编码:</th>
						<td><input type="text" class="easyui-textbox input" data-options="prompt:'字典编码'" /></td>
						<th>字典名称:</th>
						<td><input type="text" class="easyui-textbox input" data-options="prompt:'字典名称'" /></td>
						<td><a class="easyui-linkbutton"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
			<div class="h20"></div>
			<table id="dictionary_datagrid" class="W100"></table>
		</div>
	</div>
</div>
<div id="dictionary_toolBarBtn">
	<a href="javaScript:void(0);" class="easyui-linkbutton" onclick="dictionaryQuery()" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a> 
	<a href="javaScript:void(0);" class="easyui-linkbutton" id="dictionary_Delete" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
</div>
<div id="dictionary_AddOrUpdate_dialog" class="display_none">
	<form id="dictionary_AddOrUpdate_Form" class="margin_20">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>字典编码:</th>
				<td><input type="text" class="easyui-textbox input" id="dictionary_AddOrUpdate_Code" name="Code" data-options="prompt:'字典编码',required:true"></td>
				<th>英文名称:</th>
				<td><input type="text" class="easyui-textbox input" name="Name" data-options="prompt:'英文名称',required:true"></td>
			</tr>
			<tr>
				<th>中文名称:</th>
				<td><input type="text" class="easyui-textbox input" name="cName" data-options="prompt:'中文名称',required:true"></td>
				<th>顺序号:</th>
				<td><input type="text" class="easyui-textbox input" name="Num" data-options="prompt:'顺序号',required:true"></td>
			</tr>
			<tr>
				
				<th>是否启用:</th>
				<td><select class="easyui-combobox select" name="status" data-options="editable:false,panelHeight:'auto'">
						<option></option>
						<option value="0">正常</option>
						<option value="1">禁用</option>
				</select></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/dictionary/dictionary.js"></script>