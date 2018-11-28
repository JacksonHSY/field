<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${userInfo.name} - 收回权限</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<input type="hidden" id="edit_userRuleId">
<input type="hidden" id="edit_userCode" value="${userInfo.usercode}">

<div style="height: 36px;">
	<div style="background-color:#D7EBF9; height:35px;width: 100%;padding:0px 0px 2px; position: fixed;">
		<div style="line-height: 35px;font-family:微软雅黑">
			<span style="font-size:14px">通过&nbsp;</span><input id="edit_pass_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-PASS">&nbsp;&nbsp;
			<span style="font-size:14px">拒绝&nbsp;</span><input id="edit_refuse_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-REJECT" onclick="edit_showRefuseTree()">&nbsp;&nbsp;
			<span style="font-size:14px">退回&nbsp;</span><input id="edit_retrun_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-RETURN" onclick="edit_showReturnTree()">
			<div class="float_right">
				<a id="edit_user_rule_id" class="easyui-linkbutton_ok05 l-btn l-btn-small" href="javaScript:void(0);">保&nbsp;&nbsp;存</a>
			</div>
		</div>
	</div>
</div>

<div class="float_left W30">
	<table class="table_ui W100">
		<tr>
			<th>大组:</th>
			<td><span>${group.bigGroupName}</span></td>
		</tr>
		<tr>
			<th>小组:</th>
			<td><span>${group.groupName}</span></td>
		</tr>
		<tr>
			<th>姓名:</th>
			<td><span>${userInfo.name}</span></td>
		</tr>
	</table>
</div>
<div class="float_right W70">
	<div class="float_left W50">
		<ul id="edit_refuse_tree" class="easyui-tree display_none"></ul>
	</div>
	<div class="float_left W50">
		<ul id="edit_return_tree" class="easyui-tree display_none"></ul>
	</div>
</div>

</body>
<script type="text/javascript" src="${ctx}/resources/js/apply/editUserRule.js"></script>
</html>
