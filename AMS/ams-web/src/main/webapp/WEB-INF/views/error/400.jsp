<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=0">
    <title>400异常-信审管理系统</title>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" href="${ctx}/resources/css/error.css">
</head>


<body class="error">
	<img src="${ctx}/resources/images/error/bg.jpg" width="100%" class="error_pic">
    <div class="error_k">
    	<img src="${ctx}/resources/images/error/404.png" width="100%">
    </div>
    <div class="error_text">
    	<div class="bold">sorry 服务器无法理解你的请求</div><br>
		建议检查您的网址拼写是否正确，检查您是否使用了错误的网址链接
    </div>
    <div class="error_btn" onClick="javascript:location.href='${ctx}/'">
    	<img src="${ctx}/resources/images/error/btn.png" width="100%">
    </div>
</body>
<script type="text/javascript" src="${ctx}/resources/easyui/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/resources/lib/hotkeys/hotkeys.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/keyMap.js"></script>
</html>