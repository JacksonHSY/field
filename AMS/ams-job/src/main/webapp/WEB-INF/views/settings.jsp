<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="session" />
<html>
	<head>
		<title>AMS-系统设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
		<link rel="stylesheet" href="${ctx}/resources/lib/toastr/toastr.css">
	</head>

	<body>
		<div id='content' class='container'>
			<h1 style="color: red;">系统设置</h1>
			<form id="">
				<fieldset>
					<legend>缓存设置</legend>
					<!--
					<label>Label name</label>
					<input type="text" placeholder="Type something…">
					<span class="help-block">Example block-level help text here.</span>
					<label class="checkbox">
						<input type="checkbox"> Check me out
					</label>
					-->
					<button id="cleanCache" type="submit" class="btn" onclick="cleanRedis()">清空redis</button>
				</fieldset>
			</form>
		</div>

	</body>

	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/resources/lib/toastr/toastr.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js" charset="utf-8"></script>
	<script type="text/javascript">
		function cleanRedis(){
		    if(confirm('确定要清空缓存？')){
                $('#cleanCache').attr('disabled','disabled');

                toastr.options = {
                    'closeButton' : false,
                    'newestOnTop' : true,
                    'progressBar' : false,
                    'positionClass' : 'toast-top-center',
                    'preventDuplicates' : true,
                    'onclick' : null,
                    'showDuration' : '200',
                    'hideDuration' : '1000',
                    'timeOut' : '2000',
                    'extendedTimeOut' : '1000',
                    'showEasing' : 'swing',
                    'hideEasing' : 'linear',
                    'showMethod' : 'fadeIn',
                    'hideMethod' : 'fadeOut'
                };

                post('${ctx}/settings/cleanRedis',{},'json',function (result) {

					if(result.type == 'SUCCESS'){
                        toastr.success(result.messages);
                        $('#cleanCache').removeAttr('disabled');
                    }else{
                        toastr.warn(result.messages);
					}
                }, function () {
                    toastr.error('系统异常，请稍后再试');
                });
			}
        }
	</script>
</html>