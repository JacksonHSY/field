<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div class="h20"></div>
<div id="div">
	<shiro:hasPermission name="ruleconfig/leaderReview">
		<a class="easyui-linkbutton" onclick="leaderReview()">组长复核</a>
	</shiro:hasPermission>
	&nbsp;&nbsp;
	<shiro:hasPermission name="ruleconfig/proxyleaderReview">
		<a class="easyui-linkbutton" onclick="agentLrader()">代理组长</a>
	</shiro:hasPermission>
</div>
<script type="text/javascript" src="${ctx}/resources/js/apply/ruleConfiguration.js"></script>