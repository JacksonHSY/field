<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel">
	<div class="margin_20">
		<form id="dictionaryType_Query_Form">
			<table class="table_ui W60 ">
				<tr>
					<th>字典类型编码:</th>
					<td><input type="text" class="easyui-textbox input" data-options="prompt:'字典类型编码'"/></td>
					<th>字典类型名称:</th>
					<td><input type="text" class="easyui-textbox input" data-options="prompt:'字典类型名称'"/></td>
					<td><a class="easyui-linkbutton"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<div class="h20"></div>
<div id="dictionaryType_toolBarBtn">
	<a class="easyui-linkbutton" onclick="dictionaryType_ShowDialog('新建')" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>
	<a class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
</div>
<div>
	<table id="dictionaryType_datagrid"></table>
</div>
<div id="dictionaryType_AddOrUpdate_dialog" class="display_none">
	<form id="dictionaryType_AddOrUpdate_From">
		<input type="hidden" id="dictionaryType_AddOrUpdate_Id" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>字典类型编码:</th>
				<td><input type="text" class="easyui-textbox input" id="dictionaryType_AddOrUpdate_DictionaryTypeCode" name="dictionaryTypeCode" data-options="prompt:'字典类型编码'"></td>
				<th>英文名称:</th>
				<td><input type="text" class="easyui-textbox input" id="dictionaryType_AddOrUpdate_DictionaryTypeEnglishName" name="dictionaryTypeName" data-options="prompt:'英文名称'"></td>
			</tr>
			<tr>
				<th>中文名称:</th>
				<td><input type="text" class="easyui-textbox input" id="dictionaryType_AddOrUpdate_DictionaryTypeChineseName" name="dictionaryTypeName" data-options="prompt:'英文名称'"></td>
				<th>顺序号:</th>
				<td><input type="text" class="easyui-textbox input" id="dictionaryType_AddOrUpdate_Num" name="Num" data-options="prompt:'顺序号'"></td>
			</tr>
			<tr>
				<th>父字典类型:</th>
				<td>
					<select class="easyui-combobox select" id="dictionaryType_AddOrUpdate_parentDictionaryType" name="parentDictionaryType" data-options="editable:false,panelHeight:'auto'">
						<option value="1">1</option>
						<option value="0">0</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>描述:</th>
				<td colspan="3"><textarea class="textarea" maxlength="200"></textarea></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/dictionaryType/dictionaryType.js"></script>