<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-质检日志</title>
<jsp:include page="../../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<form id="qualityLogInfo__logGrid_Form">
	<h2>质检日志</h2>
	<div id="qualityLogInfo_logInfo_opoin" class="easyui-panel"></div>
	<div ><input type="hidden" id="qualityLogInfo_hidden_loanNo" value= '${loanNo}' /></div>
</form>
</body>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/qualityReceive/qualityLogInfo.js"></script>
</html>