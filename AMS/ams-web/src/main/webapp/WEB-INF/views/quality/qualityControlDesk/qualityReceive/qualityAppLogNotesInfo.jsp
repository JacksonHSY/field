<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-日志备注</title>
<jsp:include page="../../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<table class="table_list W100" style="table-layout:fixed;">
	<thead>
		<tr>
			<td>环节</td>
			<td>操作</td>
			<td>操作人</td>
			<td  style="width:180px">操作时间</td>
			<td>备注详情</td>
			<td>备注原因</td>
		</tr>
	</thead>
	<c:if test="${not empty loanLogList}">
		<c:forEach var="item" items="${loanLogList}">
			<tr>
				<td><div style="width:100px;word-wrap:break-word;">${item.operationModule}</div></td>
				<td><div style="width:100px;word-wrap:break-word;">${item.operationType}</div></td>
				<td><div style="width:100px;word-wrap:break-word;">${item.operator}</div></td>
				<td><div style="width:180px;word-wrap:break-word;"><fmt:formatDate value="${item.operationTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/></div></td>
				<td><div style="width:100px;word-wrap:break-word;">${item.remark}</div></td>
				<td><div style="width:100px;word-wrap:break-word;">${item.firstLevleReasons} ${item.twoLevleReasons}</div></td>
			</tr>
		</c:forEach>
	</c:if>
	<c:if test="${empty loanLogList }">
		<tr>
			<td colspan="6">系统错误</td>
		</tr>
	</c:if>
</table>
</body>
</html>