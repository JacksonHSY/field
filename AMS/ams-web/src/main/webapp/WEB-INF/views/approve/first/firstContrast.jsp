<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-申请历史比对</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body>
	<input type="hidden" id="firstContrast_loanNo" value="${applyBasiceInfo.loanNo}">
	<div class="easyui-panel">
		<h3>
			客户本人基本信息 <a class="easyui-linkbutton" onclick="insideMatchImageContrastDialog('${picImageUrl}','${picApproval}','${sysCode}','${operator}','${jobNumber}')">影像比对</a>
		</h3>
		<table class="table_list W100" id="firstInsideMatch_customerInfo_table">

		</table>
	</div>
	<hr>
	<div class="easyui-panel">
		<h3>客户直系亲属联系人比对</h3>
		<table class="table_list W100" id="firstInsideMatch_customerRelativesInfos_table">
		</table>
	</div>
	<hr>
	<div class="easyui-panel">
		<h3>重名联系人信息比对</h3>
		<table class="table_list W100" id="firstInsideMatch_duplicateContactsInfos_table">
		</table>
	</div>
	<!-- 影像对比 -->
	<div id="firstInsideMatch_imageContrast_Dialog" class="padding_20 display_none"></div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstContrast.js"></script>
</html>