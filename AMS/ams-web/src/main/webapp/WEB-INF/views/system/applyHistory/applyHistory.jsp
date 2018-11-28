<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="firstApprove_tab" class="easyui-tabs">
	<div title="操作日志">
		<div class="easyui-panel">
			<form id="histroty_Query_Form" class="margin_20">
				<table class="table_ui W100">
					<tr>
						<th width="65">借款编号:</th>
						<td width="180">
							<input type="text" class="easyui-textbox input" name="loanNo" data-options="prompt:'借款编号'" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;">
						</td>
						<td>
							<a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
							<a class="easyui-linkbutton " onclick="userLogQuery()"><i class="fa fa-search"></i>搜&nbsp;索</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<table id="applyHistory"></table>
	</div>
	<div title="审批日志">
		<div class="easyui-panel">
			<form id="approveHistroty_Query_Form" class="margin_20">
				<table class="table_ui W100">
					<tr>
						<th width="65">借款编号:</th>
						<td width="180">
							<input type="text" class="easyui-textbox input" name="loanNo" data-options="prompt:'借款编号'" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;">
						</td>
						<td>
							<a class="easyui-linkbutton" onclick="clearForm(this)"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
							<a class="easyui-linkbutton " onclick="approveLogQuery()"><i class="fa fa-search"></i>搜&nbsp;索</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<table id="approveHistory"></table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/applyHistory/applyHistory.js"></script>