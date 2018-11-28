<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table_list W100">
	<thead>
		<tr>
			<td>环节</td>
			<td>操作</td>
			<td>操作人</td>
			<td>操作时间</td>
			<td>备注详情</td>
			<td>备注原因</td>
		</tr>
	</thead>
	<c:forEach items="${LoanLogList}" var="item" varStatus="status" >
	<tr>
		<td>${item.operationType }</td>
		<td>${item.operationModule }</td>
		<td>${item.operator }</td>
		<td>${item.operationTime }</td>
		<td>${item.remark }</td>
		<td>${item.firstLevleReasons }</td>
	</tr>
	</c:forEach>
</table>