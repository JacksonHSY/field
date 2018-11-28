<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html class="WH100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-央行报告</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="WH100">
	<input type="hidden" id="first_centralBankCredit_loanNo" value="${applyBasiceInfo.loanNo}">
	<div class="" style="height: 800px">
		<iframe src="${sysCreditZX}/pbccrc/reportView?customerIdCard=${applyBasiceInfo.idNo}&customerName=${applyBasiceInfo.name}&queryDate=${auditEndTime}&reportId=${applyBasiceInfo.reportId}&sources=${sysCode}" style="width: 100%; height: 99.5%; padding: 0px; margin: 0px; border: 0px"></iframe>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/centralBankCredit.js"></script>
</html>