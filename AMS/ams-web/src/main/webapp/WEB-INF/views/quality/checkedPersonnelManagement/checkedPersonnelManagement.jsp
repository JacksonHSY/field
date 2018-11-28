<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west'" style="width: 45%;">
		<div class="easyui-panel W100 " data-options="title:'',border:'0',collapsible:true" style="">
			<div id="checkedPersonnelManagementWest_tree"></div>
			<div style="min-height: 375px;"></div>
			<form id="checkedPersonnelManagementWest_queryForm">
				<input type="hidden" id="checkedUser">
				<input type="hidden" id="checkedUserName">
				<table class="W65 center_m">
					<tr>
						<th width="65">是否质检:</th>
						<td>
							<select class="easyui-combobox select" id="ifRegular" data-options="editable:false,required:true,panelHeight:'auto'" style="width:150px">
								<option value="0">是</option>
								<option value="1">否</option>
							</select>
						</td>
						<td width="10"></td>
						<td><a class="easyui-linkbutton" id="checkedPersonnelManagementWest_add">添加</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div data-options="region:'center'" style="width: 55%;">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="checkedPersonnelManagement_form" class="margin_20">
				<table class="table_ui W100 center_m">
					<tr>
						<th>被检人员ID:</th>
						<td><input type="text" name="checkedUser" class="easyui-textbox input" data-options="prompt:'被检人员ID'" style="width:150px"></td>
						<th>被检人员姓名:</th>
						<td><input type="text" name="checkedUserName" class="easyui-textbox input" data-options="prompt:'被检人员姓名'" style="width:150px"></td>
					</tr>
					<tr>
						<th>是否质检:</th>
						<td>
							<select class="easyui-combobox select" name="ifRegular" data-options="editable:false,required:true,panelHeight:'auto'" style="width:150px">
								<option value="0">是</option>
								<option value="1">否</option>
							</select>
						</td>
						<th></th>
						<td colspan="2"><a class="easyui-linkbutton" id="checkedPersonnelManagement_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; 
						<a class="easyui-linkbutton" id="checkedPersonnelManagementCenter_Query"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<div>
			<div id="checkedPersonnelManagementCenter_toolBarBtn">
				<a class="easyui-linkbutton" data-options="plain:true" id="checkedPersonnelManagement_Delete"><i class="fa fa-times" aria-hidden="true"></i>删&nbsp;除</a>&nbsp;
			</div>
			<table  id="checkedPersonnelManagementCenter_datagrid"></table>
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/checkedPersonnelManagement.js"></script>
