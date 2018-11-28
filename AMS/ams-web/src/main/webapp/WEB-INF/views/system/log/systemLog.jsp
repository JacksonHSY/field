<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel">
	<form id="systemLog_Query_Form" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>操作人:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">A</option>
						<option value="1">B</option>
					</select>
				</td>
				<th>操作系统:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">信审平台系统</option>
						<option value="1">信贷录单系统</option>
						<option value="2">审批管理系统</option>
						<option value="3">借款管理系统</option>
						<option value="4">报表管理系统</option>
					</select>
				</td>
				<th>一级目录:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">A</option>
						<option value="1">B</option>
					</select>
				</td>
				<th>二级目录:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">A</option>
						<option value="1">B</option>
					</select>
				</td>
				<th>操作类型:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">A</option>
						<option value="1">B</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>操作时间:</th>
				<td>
					<input class="easyui-datebox input" data-options="prompt:'操作时间',validType:'date'" id="systemLog_Query_Start">
				</td>
				<th>至:</th>
				<td>
					<input type="text" class="easyui-datebox input" data-options="prompt:'操作时间',validType:['date','compareDate[\'#systemLog_Query_Start\']']">
				</td>
				<td colspan="2">
					<a class="easyui-linkbutton"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
					<a class="easyui-linkbutton "><i class="fa fa-search"></i>搜&nbsp;索</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<table id="systemLog_datagrid"></table>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/log/systemLog.js"></script>