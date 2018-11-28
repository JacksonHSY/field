<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="auditStatistics_tabs">
	<div title="初审已完成" class="padding_20">
		<table class="table_ui W50">
			<tr>
				<th>通过件合计:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'通过件合计'"></td>
				<th>拒绝件合计:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'拒绝件合计'"></td>
			</tr>
		</table>
		<div class="h20"></div>
		<table id="auditStatistics_first_adopt_datagrid"></table>
		<div class="h20"></div>
		<table id="auditStatistics_first_deny_datagrid"></table>
	</div>
	<div title="终审待处理" class="padding_20">
		<table class="table_ui" >
			<tr>
				<th>终审待处理:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'终审待处理'"></td>
			</tr>
		</table>
		<div class="h20"></div>
		<table id="auditStatistics_handle_adopt_datagrid"></table>
	</div>
	<div title="终审已完成" class="padding_20">
		<table class="table_ui">
			<tr>
				<th>通过件合计:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'通过件合计'"></td>
				<th>拒绝件合计:</th>
				<td><input type="text" class="easyui-textbox input" data-options="prompt:'拒绝件合计'"></td>
			</tr>
		</table>
		<div class="h20"></div>
		<table id="auditStatistics_finish_adopt_datagrid"></table>
		<div class="h20"></div>
		<table id="auditStatistics_finish_deny_datagrid"></table>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$("#auditStatistics_tabs").tabs();
		initAdoptDatagrid("#auditStatistics_first_adopt_datagrid",'通过件合计');
		initAdoptDatagrid("#auditStatistics_handle_adopt_datagrid",'通过件合计');
		initAdoptDatagrid("#auditStatistics_finish_adopt_datagrid",'通过件合计');
		initDenyDatagrid("#auditStatistics_first_deny_datagrid",'拒绝件合计');
		initDenyDatagrid("#auditStatistics_finish_deny_datagrid",'拒绝件合计');
	});
	function initAdoptDatagrid(id,title) {
		$(id).datagrid({
			url : "${ctx}/systemLog/pageList",
			striped : true,
			title:title,
			singleSelect : true,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			columns : [ [ {
				field : 'operation',
				title : '申请件编号',
				width : 160,
			}, {
				field : 'requestContent',
				title : '姓名',
				width : 160,
			}, {
				field : 'responseContent',
				title : '身份证号码',
				width : 160,
			}, {
				field : 'requestDate',
				title : '申请产品',
				width : 160,
			}, {
				field : 'responseDate',
				title : '申请金额',
				width : 160,
			}, {
				field : 'ip',
				title : '申请期限',
				width : 160,
			}, {
				field : 'lastModifiedBy',
				title : '创建人',
				width : 160,
			}, {
				field : 'lastModifiedDate',
				title : '创建时间',
				width : 165,
				sortable : true,
				order : 'asc',
				formatter : function(value, data, index) {
					return moment(value).format('YYYY-MM-DD');
				}
			} ] ]
		});
	}
	function initDenyDatagrid(id,title) {
		$(id).datagrid({
			url : "${ctx}/systemLog/pageList",
			striped : true,
			singleSelect : true,
			title:title,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			columns : [ [ {
				field : 'operation',
				title : '申请件编号',
				width : 180,
			}, {
				field : 'requestContent',
				title : '姓名',
				width : 180,
			}, {
				field : 'responseContent',
				title : '身份证号码',
				width : 180,
			}, {
				field : 'requestDate',
				title : '申请产品',
				width : 180,
			}, {
				field : 'ip',
				title : '拒绝原因',
				width : 180,
			}, {
				field : 'lastModifiedBy',
				title : '创建人',
				width : 200,
			}, {
				field : 'lastModifiedDate',
				title : '创建时间',
				width : 200,
				sortable : true,
				order : 'asc',
				formatter : function(value, data, index) {
					return moment(value).format('YYYY-MM-DD');
				}
			} ] ]
		});
	}
</script>