<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-tabs" id="apply_info_tab" data-options='onSelect:tab_change'>
	<div title="通过件">
		<form id="pass_form_Id">
			<div class="easyui-panel padding_20">
				<input type="hidden" name="statu" value="1">
				<table class="table_ui W100">
					<tr>
					    <th>借款编号:</th>
						<td><input type="text" class="easyui-textbox input" name="loanNo" data-options="prompt:'借款编号',validType:'loanNo'"></td>
						<th>申请人姓名:</th>
						<td><input type="text" class="easyui-textbox input" name="customerName" data-options="prompt:'申请人姓名',validType:'customerName'"></td>
						<th>身份证号码:</th>
						<td><input type="text" class="easyui-textbox input" name="cdNo" data-options="prompt:'身份证号码',validType:'cdNo'"></td>
					</tr>
					<tr>
						<th>查询时间起:</th>
						<td><input name="startDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'查询时间起',validType:['date','compareToday']" id="applyDoc_pass_startTime"></td>
						<th>查询时间止:</th>
						<td><input name="endDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'查询时间止',validType:['date','compareToday','compareDate[\'#applyDoc_pass_startTime\']']" id="applyDoc_pass_endTime"></td>
						<th></th>
						<td><a class="easyui-linkbutton" onclick="applyInfoManagePass()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</div>
		</form>
		<div class="h20"></div>
		<table id="approvalStateQuery_pass_datagrid"></table>
	</div>
	<div title="拒绝件">
		<form id="no_pass_form_id">
			<div class="easyui-panel padding_20">
				<input type="hidden" name="statu" value="2">
				<table class="table_ui W100">
					<tr>
					    <th>借款编号:</th>
						<td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'借款编号',validType:'loanNo'"></td>
						<th>申请人姓名:</th>
						<td><input type="text" name="customerName" class="easyui-textbox input" data-options="prompt:'申请人姓名',validType:'customerName'"></td>
						<th>身份证号码:</th>
						<td><input type="text" name="cdNo" class="easyui-textbox input" data-options="prompt:'身份证号码',validType:'cdNo'"></td>
					</tr>
					<tr>
						<th>查询时间起:</th>
						<td><input name="startDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'查询时间起',validType:['date','compareToday']" id="applyDoc_refuse_startTime"></td>
						<th>查询时间止:</th>
						<td><input name="endDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'查询时间止',validType:['date','compareToday','compareDate[\'#applyDoc_refuse_startTime\']']" id="applyDoc_refuse_endTime"></td>
						<th></th>
						<td><a onclick="applyInfoManageNoPass()" class="easyui-linkbutton"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</div>
		</form>
		<div class="h20"></div>
		<table id="approvalStateQuery_deny_datagrid"></table>
	</div>
</div>
<script src="${ctx}/resources/js/bms/bmsBasicApi.js"></script>
<script src="${ctx}/resources/js/core/coreApi.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/apply/applyInfoManage.js"></script>