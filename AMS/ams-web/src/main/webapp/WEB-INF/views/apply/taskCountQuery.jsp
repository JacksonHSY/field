<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel" data-options="title:'任务数查询'">
	<form id="task_form_id" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>审核节点:</th>
				<td>
					<select id="task_count_role_id" name="taskDefId" class="easyui-combobox select" data-options="valueField:'code',textField:'name',prompt:'审核节点',editable:false" >
						<!-- <option value="">请选择</option>
						<option value="check" >初审</option>
						<option value="finalCheck">终审</option> -->
					</select>
				</td>
				<th>工号:</th>
				<td><input name="staffCode" type="text" class="easyui-textbox input" data-options="prompt:'工号'"></td>
				<th>姓名:</th>
				<td><input name="staffName" type="text" class="easyui-textbox input" data-options="prompt:'姓名'"></td>
			</tr>
			<tr>
				<td colspan="5">
					<a class="easyui-linkbutton" onclick="loadTaskCountQuyerDatagrid()"><i class="fa fa-repeat" aria-hidden="true"></i>刷&nbsp;新</a>&nbsp; &nbsp;
					<a class="easyui-linkbutton" onclick="form_Query()"  ><i class="fa fa-search" aria-hidden="true"></i>查&nbsp;询</a>&nbsp; &nbsp;
					<a id="forbid_button" class="easyui-linkbutton" onclick="batchDisabled()">禁止接单</a>&nbsp; &nbsp;<a id="opeon_button" class="easyui-linkbutton" onclick="batchOpen()">开启接单</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<table id="taskCountQuyer_taskList_datagrid"></table>
<script type="text/javascript" src="${ctx}/resources/js/apply/taskCountQuery.js"></script>