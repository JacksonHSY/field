<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-复议日志</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
  <div class="easyui-panel" title="复议日志"></div>
	<table class="table_list W100" style="table-layout: fixed;">
		<thead>
			<tr>
				<td style="width: 100px">环节</td>
				<td style="width: 100px">操作</td>
				<td style="width: 100px">操作人</td>
				<td style="width: 150px">操作时间</td>
				<td>操作内容</td>
				<td>备注原因</td>
			</tr>
		</thead>
		<c:if test="${not empty reviewLogList}">
			<c:forEach var="item" items="${reviewLogList}">
				<tr>
					<td>
						<div style="word-wrap: break-word;">${item.reviewStatus}</div>
					</td>
					<td>
						<div style="word-wrap: break-word;">${item.reviewOperation}</div>
					</td>
					<td>
						<div style="word-wrap: break-word;">${item.creatorName}</div>
					</td>
					<td>
						<div style="word-wrap: break-word;">${item.createTime}</div>
					</td>
					<td>
						<div style="word-wrap: break-word;">${item.operationContent}</div>
					</td>
					<td>
						<div style="word-wrap: break-word;">${item.reviewRemark}</div>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty reviewLogList }">
			<tr>
				<td colspan="6">系统错误</td>
			</tr>
		</c:if>
	</table>
</body>
</html>