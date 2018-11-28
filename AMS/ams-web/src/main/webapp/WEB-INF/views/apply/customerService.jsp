<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel" data-options="title:'客服接单管理'">
	<form class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>管理营业部:</th>
				<td><select class="easyui-combobox select" data-options="prompt:'管理营业部'"></select></td>
				<th>员工编号:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'员工编号'"></td>
				<th>人员姓名:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'人员姓名'"></td>
				<td><a class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a class="easyui-linkbutton"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<table id="customerService_userList_datagrid"></table>
<script type="text/javascript">
	$(function() {
		initCustomerServiceDatagrid();
	});

 	function initCustomerServiceDatagrid() {
		$("#customerService_userList_datagrid").datagrid({
			url : "${ctx}/systemLog/pageList",
			title : '客服接单管理',
			striped : true,
			//toolbar : '#new_task_datagrid_tool',
			singleSelect : true,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			columns : [ [ {
				field : 'operation',
				title : '人员ID',
				width : 80,
			}, {
				field : 'requestContent',
				title : '人员姓名',
				width : 80,
			}, {
				field : 'responseContent',
				title : '组织名称',
				width : 80,
			}, {
				field : 'requestDate',
				title : '接单状态',
				width : 80,
			}, {
				field : 'action',
				title : '操作',
				width : 80,
				align : 'center',
				formatter : function(value, data, index) {
					var action = '<a href="javaScript:void(0);"><i class="fa fa-pencil" aria-hidden="true"></i>禁止接单</a>';
					action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);"><i class="fa fa-pencil" aria-hidden="true"></i>其他操作</a>';
					return action;
				}
			} ] ]
		});
	}
</script>