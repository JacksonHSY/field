<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>任务数查询</title>
    <jsp:include page="../common/commonJS.jsp"></jsp:include>
</head>
<body class="padding_20">
<div class="easyui-panel" title="日志备注"></div>
<table class="table_list W100">
    <thead>
    <tr>
        <td>id</td>
        <td>工号</td>
        <td>员工姓名</td>
        <td>正常队列</td>
        <td>优先队列</td>
        <td>挂起队列</td>
        <td>等待时间</td>
        <td>是否接单</td>
        <td>初审/终审</td>
        <td>小组名称</td>
        <td>大组名称</td>
        <td>员工状态</td>
        <td>机构编码</td>
        <td>父级机构</td>
        <td>版本控制</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${staffOrderTaskList}">
        <tr>
            <td>${item.id}</td>
            <td>${item.staffCode}</td>
            <td>${item.staffName}</td>
            <td>${item.currActivieTaskNum}</td>
            <td>${item.currPriorityNum}</td>
            <td>${item.currInactiveTaskNum}</td>
            <td><fmt:formatDate value="${item.waitTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:if test="${'Y'==item.ifAccept}">是</c:if><c:if test="${'N'==item.ifAccept}">否</c:if></td>
            <td>
                <c:if test="${'apply-check' == item.taskDefId}">初审</c:if>
                <c:if test="${'applyinfo-finalaudit'==item.taskDefId}">
                 终审&nbsp;&nbsp;${item.finalAuditLevel}
                </c:if>
            </td>
            <td>${item.orgName}</td>
            <td>${item.parentOrgName}</td>
            <td>${item.status}</td>
            <td>${item.orgCode}</td>
            <td>${item.parentOrgCode}</td>
            <td>${item.version}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>