<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel">
	<form id="loanInfoChangeLog_Query_Form" class="margin_20">
		<table class="table_ui center_m W100">
			<tr>
				<th>借款编号:</th>
				<td>
					<input type="text" class="easyui-textbox input" name="loanCode" data-options="prompt:'借款编号'">
				</td>
				<th>操作环节:</th>
				<td>
					<select class="easyui-combox select">
						<option value="0">A</option>
						<option value="1">B</option>
					</select>
				</td>
				<th>操作时间：</th>
				<td>
					<input class="easyui-datebox input" data-options="prompt:'操作时间',validType:'date'" id="loanInfoChangeLog_Query_Start">
				</td>
				<th>至：</th>
				<td>
					<input type="text" class="easyui-datebox input" data-options="prompt:'操作时间',validType:['date','compareDate[\'#loanInfoChangeLog_Query_Start\']']">
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
	<table id="loanInfoChangeLog_datagrid"></table>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/loanInfoChangeLog/loanInfoChangeLog.js"></script>