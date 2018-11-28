<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>新增收回权限</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
<div style="height: 36px;">
	<div style="background-color:#D7EBF9; height:35px;width: 100%;padding:0px 0px 2px; position: fixed;">
		<div style="line-height: 35px;font-family:微软雅黑">
			<span style="font-size:14px">通过&nbsp;</span><input id="pass_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-PASS">&nbsp;&nbsp;
			<span style="font-size:14px">拒绝&nbsp;</span><input id="refuse_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-REJECT" onclick="showRefuseTree()">&nbsp;&nbsp;
			<span style="font-size:14px">退回&nbsp;</span><input id="retrun_checkbox" type="checkbox" style="width: 14px;height: 14px" value="XSCS-RETURN" onclick="showReturnTree()">
			<div class="float_right">
				<a id="save_user_rule_id" class="easyui-linkbutton_ok05 l-btn l-btn-small" href="javaScript:void(0);">保&nbsp;&nbsp;存</a>
			</div>
		</div>
	</div>
</div>
<div class="float_left W30">
	<ul id="user_tree" class="easyui-tree display_none"></ul>
</div>
<div class="float_right W70">
	<div class="float_left W50">
		<ul id="refuse_tree" class="easyui-tree display_none"></ul>
	</div>
	<div class="float_left W50">
		<ul id="return_tree" class="easyui-tree display_none"></ul>
	</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/apply/addUserRule.js"></script>
</html>
