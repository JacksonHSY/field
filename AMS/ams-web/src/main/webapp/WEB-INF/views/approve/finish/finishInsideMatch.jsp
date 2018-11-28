<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<div class="easyui-panel" title="申请信息"> 
	<input id="name" type="hidden" value="${applicationInfo.name}"> 
	<input id="idNo" type="hidden" value="${applicationInfo.idNo}"> 
	<input id="corpName" type="hidden" value="${applicationInfo.corpName}"> 
	<input id="licensePlateNum" type="hidden" value="${applicationInfo.licensePlateNum}">

	<%--房产地址信息--%>
	<input id="estateAddress" type="hidden" value="${applicationInfo.estateAddress}">
	<%--家庭地址信息--%>
	<input id="homeAddress" type="hidden" value="${applicationInfo.homeAddress}">
	<%--公司地址信息--%>
	<input id="corpAddress" type="hidden" value="${applicationInfo.corpAddress}">


	<table class="table_ui W100">
		<tr>
			<th>借款编号:</th>
			<td id="finishInsideMatch_loanNo">${applyBasiceInfo.loanNo}</td>
			<th>姓名:</th>
			<td>${applyBasiceInfo.name}</td>
			<th>身份证号码:</th>
			<td>${applyBasiceInfo.idNo}</td>
		</tr>
		<tr>
			<th>申请产品:</th>
			<td><c:if test="${'证大前前'!=applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
			<th>申请期限:</th>
			<td>${applyBasiceInfo.applyTerm}</td>
		</tr>
	</table>
</div>
<div class="h20"></div>
<div class="easyui-panel" title="反欺诈信息">
	<table id="finishInsideMatch_cheatInfo_table" class="table_list W100">
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
<div class="h20"></div>

<div class="easyui-panel">
	<h3>
		申请历史信息 <a class="easyui-linkbutton" id="finishInsideMatch_comparison_a" onclick="finishInsideMatchContrastDialog()">比对</a> 
	</h3>
	<table id="finishInside_applyHistory_dataGrid" class="table_list W200">

	</table>
</div>
<div class="h20"></div>
<div class="easyui-tabs" id="finishInsideMatch_tabs">
	<div title="匹配号码">
		<table id="finishInsideMatch_phoneNum_table"></table>
	</div>
	<div title="其他匹配">
		<table id="finishInsideMatch_nameAndId_table_toolbar" class="W100">
			<tr>
				<td><h3>姓名身份证</h3></td>
			</tr>
		</table>
		<table id="finishInsideMatch_nameAndId_table" data-options="toolbar:'#finishInsideMatch_nameAndId_table_toolbar'"></table>
		<hr>
		<table id="finishInsideMatch_corpName_table_toolbar">
			<tr>
				<td style="width: 7%"><h3>单位名称</h3></td>
				<td style="width: 10%"><input id="unitName" type="text" class="easyui-textbox input" style="height: 24px;" data-options="prompt:'单位名称'" value="${applicationInfo.corpName}"></td>
				<td style="width: 10%"><a onclick="uintNameQuery()" class="easyui-linkbutton_ok06 l-btn l-btn-small">搜&nbsp;索</a></td>
				<td style="width: 73%"></td>
			</tr>
		</table>
		<table id="finishInsideMatch_corpName_table" data-options="toolbar:'#finishInsideMatch_corpName_table_toolbar'"></table>
		<hr>
		<table id="finishInsideMatch_address_table_toolbar">
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
		<table id="finishInsideMatch_address_table" data-options="toolbar:'#finishInsideMatch_address_table_toolbar'"></table>

		<hr>

		<table id="finishInsideMatch_vehicleNo_table_toolbar" class="W100">
			<tr>
				<td><h3>车牌号</h3></td>
			</tr>
		</table>
		<table id="finishInsideMatch_vehicleNo_table" data-options="toolbar:'#finishInsideMatch_vehicleNo_table_toolbar'"></table>
	</div>
</div>
<!-- 新增电核记录 -->
<div id="telephoneSummary_addTelephoneRecord_Dialog" class="padding_20 display_none">
	<form id="telephoneSummary_addTelephoneRecord_Form">
		<table class="table_ui W100" id="telephoneSummary_addTelephoneRecord_table">
			<tr>
				<th>致电号码:</th>
				<td><input id="tel_phone" name="telPhone" style="border: none;" readonly="readonly"></td>
				<th>所有人:</th>
				<td><input id="tel_name" name="name" style="border: none;" readonly="readonly"></td>
			</tr>
			<tr>
				<th>核查时间:</th>
				<td colspan="2"><input id="tel_date" name="telDate" style="border: none;" readonly="readonly"></td>
			</tr>
			<tr>
				<th>电核备注:</th>
				<td colspan="2"><input class="easyui-textbox W90 input" data-options="required:true" name="askContent"></td>
			</tr>
		</table>
	</form>
</div>
<!-- 申请历史对比 -->
<div id="finishInsideMatch_contrast_Dialog" class="padding_20  display_none">
	<div class="easyui-panel">
		<h3>
			客户本人基本信息 <a class="easyui-linkbutton" onclick="finishInsideMatchImageContrastDialog('${picImageUrl}','${picApproval}','${sysCode}','${operator}','${jobNumber}')">影像比对</a>
		</h3>
		<table class="table_list W100" id="finishInsideMatch_customerInfo_table">

		</table>
	</div>
	<hr>
	<div class="easyui-panel">
		<h3>客户直系亲属联系人比对</h3>
		<table class="table_list W100" id="finishInsideMatch_customerRelativesInfos_table">
		</table>
	</div>
	<hr>
	<div class="easyui-panel">
		<h3>重名联系人信息比对</h3>
		<table class="table_list W100" id="finishInsideMatch_duplicateContactsInfos_table">
		</table>
	</div>
</div>
<!-- 影像对比 -->
<div id="finishInsideMatch_imageContrast_Dialog" class="padding_20 display_none"></div>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishInsideMatch.js"></script>