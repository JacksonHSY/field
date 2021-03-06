<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<div class="easyui-panel W100" data-options="collapsible:true">
	<form id="qualitySource_queryForm" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>申请来源:</th>
				<td><input type="text" name="qualitySource" class="easyui-textbox input" data-options="prompt:'名称'"></td>
				</tr>
			<tr>
				<td colspan="5"></td>
				<td colspan="2">
					<a class="easyui-linkbutton" id="qualityQuery_reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; 
					<a class="easyui-linkbutton" id="qualityQuery_Query"><i class="fa fa-search"></i>搜&nbsp;索</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="qualitySource_AddOrUpdate_dialog" class="display_none">
	<form id="qualitySource_AddOrUpdate_Form" class="margin_20">
	<input type="hidden" name="id">
		<table class="table_ui W100 center_m">
			<tr>
				<th>申请来源:</th>
				<td><input type="text" class="easyui-textbox input" id="qualitySource" name="qualitySource" data-options="prompt:'原因编码',required:true"></td>
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
	<div id="qualitySource_toolBarBtn">
		<a class="easyui-linkbutton" id="qualitySource_Dialog_Open" data-options="plain:true"><i class="fa fa-plus"></i>新&nbsp;建</a>&nbsp; <a class="easyui-linkbutton" id="qualitySource_Delete" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>
	</div>
	<table id="qualitySource_datagrid"></table>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualitySource.js"></script>
