<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div class="easyui-panel">
	<form id = "commonParam_Query_Form" class="margin_20">
		<table class="table_ui W100">
			<tr>
				<th width="90">字段中文名称:</th>
				<td width="180"><input type="text" class="easyui-textbox input" name="paramName" data-options="prompt:'字段中文名称'" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;"></td>
				<td>
					<a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
					<a class="easyui-linkbutton" onclick="commonParamQuery()"><i class="fa fa-search"></i>查&nbsp;询</a>
					<shiro:hasPermission name="/commonParam/cleanCache">
						<a class="easyui-linkbutton" onclick="cleanCache()"><i class="fa fa-times"></i>清除缓存</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div id="commonParam_toolBarBtn">
	<a class="easyui-linkbutton"  onclick="commonParam_ShowDialog('add', '')" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>
	<a class="easyui-linkbutton" onclick="commonParam_delete()" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
</div>
<div>
	<table id="commonParam_Datagrid" class="easyui-datagrid table_list W100"></table>
</div>
<div id="commonParam_AddOrUpdate_dialog" class="display_none">
	<form id="commonParam_AddOrUpdate_From">
		<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>字段中文名称:</th>
				<td><input type="text" class="easyui-textbox input" id="commonParam_AddOrUpdate_ParamName" name="paramName" data-options="prompt:'字段中文名称'"></td>	
			</tr>
			<tr>
				<th>值:</th>
				<td><input type="text" class="easyui-textbox input" name="paramValue" id="commonParam_AddOrUpdate_ParamValue" data-options="prompt:'值'"></td>
			</tr>
			<tr>
				<th>参数类型:</th>
				<td><input type="text" class="easyui-textbox input" name="paramType" id="commonParam_AddOrUpdate_ParamType" data-options="prompt:'参数类型'"></td>
			</tr>
			<tr>
				<th>参数标识:</th>
				<td><input type="text" class="easyui-textbox input" name="paramKey" id="commonParam_AddOrUpdate_ParamKey" data-options="prompt:'参数标识'"></td>
			</tr>
			<tr>
				<th>备注:</th>
				<td colspan="3"><textarea class="textarea" name="paramDesc" maxlength="200"></textarea></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/commonParam/commonParam.js"></script>