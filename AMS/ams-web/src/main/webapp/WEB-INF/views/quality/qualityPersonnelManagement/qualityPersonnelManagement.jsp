<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west'" style="width: 45%;">
		<div class="easyui-panel W100 " data-options="title:'',border:'0',collapsible:true">
			<div id="qualityPersonnelManagementWest_tree"></div>
			<div style="min-height: 430px;"></div>
			<form id="qualityPersonnelManagementWest_queryForm">
				<input type="hidden" id="checkUser">
				<input type="hidden" id="checkUserName">
				<table class="W100 center_m">
					<tr>
						<td align="right">最大队列值:</td>
						<td class="inputmask"><input class="easyui-numberbox input" id="maxTaskNum"  data-options="width:90,prompt:'0-999整数',required:true"></td>
						<td align="right">是否接单:</td>
						<td>
							<select class="easyui-combobox select" id="ifAccept" data-options="width:90,editable:false,required:true,panelHeight:'auto'">
								<option value="0">是</option>
								<option value="1">否</option>
							</select>
						</td>
						<td></td>
					</tr>
					<tr>
						<td align="right">是否反馈接单:</td>
						<td>
							<select class="easyui-combobox select" id="ifReback" data-options="width:90,editable:false,required:true,panelHeight:'auto'">
								<option value="0">是</option>
								<option value="1">否</option>
							</select>
						</td>
						<td align="right">是否申请复核接单:</td>
						<td>
						<select class="easyui-combobox select" id="ifApplyCheck" data-options="width:90,editable:false,required:true,panelHeight:'auto'">
								<option value="0">是</option>
								<option value="1">否</option>
							</select>
						</td>
						<td><a class="easyui-linkbutton" id="qualityPersonnelManagementWest_add" onclick="addPerson(this)">添加</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div data-options="region:'center'" style="width: 55%;">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="qualityPersonnelManagement_form" class="margin_20">
				<table class="table_ui W100 center_m">
					<tr>
						<th width="75">质检员ID:</th>
						<td width="140"><input type="text" name="checkUser" class="easyui-textbox input" data-options="prompt:'质检员ID'" style="width:140px;"></td>
						<th width="75">质检员姓名:</th>
						<td width="140"><input type="text" name="checkUserName" class="easyui-textbox input" data-options="prompt:'质检员姓名'" style="width:140px;"></td>
						<td><a class="easyui-linkbutton" id="qualityPersonnelManagement_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;<a class="easyui-linkbutton" id="qualityPersonnelManagement_Query"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<div>
			<div id="qualityPersonnelManagement_toolBarBtn">
				<a class="easyui-linkbutton" id="qualityPersonnelManagement_Delete" data-options="plain:true"><i class="fa fa-trash-o"></i>删&nbsp;除</a>&nbsp;
			</div>
			<table id="qualityPersonnelManagement_datagrid"></table>
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/lib/inputmask/jquery.inputmask.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/lib/inputmask/inputmask.regex.extensions.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityPersonnelManagement.js"></script>
