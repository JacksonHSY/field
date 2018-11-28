<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-内部匹配</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
<style>
.datagrid-toolbar .l-btn {
    color: #ffffff;
}
</style>
</head>
<body class="padding_20">

	<input id="firstInsideMatch_loanNo" type="hidden" value="${applyBasiceInfo.loanNo}" placeholder="借款编号">
	<input id="name" type="hidden" value="${applicationInfo.name}" placeholder="申请人姓名">
	<input id="corpName" type="hidden" value="${applicationInfo.corpName}" placeholder="公司名称">
	<input id="licensePlateNum" type="hidden" value="${applicationInfo.licensePlateNum}">
	<input id="estateAddress" type="hidden" value="${applicationInfo.estateAddress}" placeholder="房产地址信息">
	<input id="homeAddress" type="hidden" value="${applicationInfo.homeAddress}" placeholder="家庭地址信息">
	<input id="corpAddress" type="hidden" value="${applicationInfo.corpAddress}" placeholder="公司地址信息">

	<div class="easyui-panel" title="申请信息">
		<table class="table_ui W100">
			<tr>
				<th>借款编号:</th>
				<td>${applyBasiceInfo.loanNo}</td>
				<th>申请人姓名:</th>
				<td>${applyBasiceInfo.name}</td>
				<th>身份证号码:</th>
				<td id="firstInsideMatch_applyInfo_idNo">${applyBasiceInfo.idNo}</td>
			</tr>
			<tr>
				<th>申请产品:</th>
				<td><c:if test="${'证大前前' !=applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
				<th>申请期限:</th>
				<td>${applyBasiceInfo.applyTerm}</td>
			</tr>
		</table>
	</div>
	<hr>

	<div class="easyui-panel" title="反欺诈信息">
		<table id="firstInsideMatch_cheatInfo_table" class="table_list W100">
			<thead>
				<tr>
					<td>来源</td>
					<td>匹配字段</td>
					<td>匹配信息</td>
					<td>欺诈原因</td>
				</tr>
			</thead>
		</table>
	</div>
	<hr>

	<div class="easyui-panel">
		<h3>申请历史信息 <a class="easyui-linkbutton" id="firstInsideMatch_comparison_a" onclick="insideMatchContrastDialog()">比对</a></h3>
		<table id="firstInside_applyHistory_dataGrid" class="table_list W200"></table>
	</div>
	<hr>

	<div class="easyui-tabs" id="firstInsideMatch_phoneMatch_tabs">
		<div title="匹配号码">
			<table id="firstInsideMatch_phoneNum_table"></table>
		</div>
		<div title="其他匹配">

			<table id="firstInsideMatch_nameAndId_table_toolbar" class="W100">
				<tr>
					<td><h3>姓名身份证</h3></td>
				</tr>
			</table>
			<table id="firstInsideMatch_nameAndId_table" data-options="toolbar:'#firstInsideMatch_nameAndId_table_toolbar'"></table>
			<hr>

			<table id="firstInsideMatch_corpName_table_toolbar">
				<tr>
					<td style="width: 7%"><h3>单位名称</h3></td>
					<td style="width: 10%"><input id="unitName" type="text" class="easyui-textbox input" style="height: 24px;" data-options="prompt:'单位名称'" value="${applicationInfo.corpName}"></td>
					<td style="width: 10%"><a onclick="uintNameQuery()" class="easyui-linkbutton_ok06 l-btn l-btn-small">搜&nbsp;索</a></td>
					<td style="width: 73%"></td>
				</tr>
			</table>
			<table id="firstInsideMatch_corpName_table" data-options="toolbar:'#firstInsideMatch_corpName_table_toolbar'"></table>
			<hr>

			<table id="firsthInsideMatch_address_table_toolbar">
				<tr>
					<td style="width: 7%"><h3>地址信息</h3></td>
					<td style="width: 10%"><select id="addressType" class="easyui-combobox select" style="height: 24px;" data-options="onChange:changeAddressInfo,value:'1',multiple:false,editable:false">
							<option value="1">房产地址</option>
							<option value="2">单位地址</option>
							<option value="3">家庭地址</option>
					</select></td>
					<td style="width: 10%">
						<div>
							<input id="addressInfo" type="text" class="easyui-textbox input" style="width: 350px; height: 24px;" data-options="prompt:'地址信息'" value="${applicationInfo.estateAddress}">
						</div>
					</td>
					<td style="width: 10%">
						<div>
							<a onclick="addressQuery()" class="easyui-linkbutton_ok06 l-btn l-btn-small">搜&nbsp;索</a>
						</div>
					</td>
					<td style="width: 63%"></td>
				</tr>
			</table>
			<table id="firsthInsideMatch_address_table" data-options="toolbar:'#firsthInsideMatch_address_table_toolbar'"></table>
			<hr>

			<table id="firstInsideMatch_vehicleNo_table_toolbar" class="W100">
				<tr>
					<td><h3>车牌号</h3></td>
				</tr>
			</table>
			<table id="firstInsideMatch_vehicleNo_table" data-options="toolbar:'#firstInsideMatch_vehicleNo_table_toolbar'"></table>
		</div>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstInsideMatch.js"></script>
</html>
