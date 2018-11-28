<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 代理组长弹框 -->
<div id="ruleConfiguration_agentLrader_Dialog" class="padding_20">
	<form id="ruleConfiguration_agentLrader_Form">
		<table class="table_list W100">
			<thead>
				<tr>
					<td>组别</td>
					<td>组长</td>
					<td>组员</td>
					<td>代理组长</td>
				</tr>
			</thead>
			<c:forEach items="${agentlist}" var="item" varStatus="status" >
				<tr>
					<td >${item.groupName}</td>
					<td>${item.groupLeader}</td>
					<td>
						<table class="W100">
							<c:forEach items="${item.employees}" var="employees" varStatus="status1" >
							<tr>
								<td height="30px">
									${employees.name}
									<input type="hidden" name="userCode" value="${employees.usercode}" />
								</td>
							</tr>
							</c:forEach>
						</table>
					</td>
					<td>
						<table >
							<c:forEach items="${item.employees}" var="employees" varStatus="status1" >
							<tr>
								<td>
									<select name="proxyUser"  class="easyui-combobox select" data-options="editable:'false'">
										<option value="-1">请选择</option>
										<c:forEach items="${leaderList}" var="list"  >
											<c:choose>
												<c:when test="${employees.proxyUserCode == list.usercode}">
													<option value="${list.usercode}" selected="selected">${list.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${list.usercode}">${list.name}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
							</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
	<div class="h20"></div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/apply/ruleConfiguration.js"></script>