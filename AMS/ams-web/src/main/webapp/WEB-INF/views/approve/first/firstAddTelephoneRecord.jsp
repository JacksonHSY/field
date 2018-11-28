<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${title}</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20" ms-controller="firstAddTelPage">
	<form id="addPhoneRecordForm">
		<input id="createdDate" type="hidden" value="${mobileHistory.createdDate}"> 
		<input id="loanNo" style="display: none" value="${mobileHistory.loanNo}" name="loanNo"> 
		<input id="telPhone" style="display: none" value="${mobileHistory.telPhone}" name="telPhone"> 
		<input id="telDate" style="display: none" value="${mobileHistory.telDate}" name="telDate"> 
		<input id="telPhoneType" value="${mobileHistory.telPhoneType}" style="display: none" name="telPhoneType">
		
		<c:if test="${empty flag}">
			<input id="nameTitle" value="${mobileHistory.nameTitle}" style="display: none" name="nameTitle">
			<input id="name" style="display: none" value="${mobileHistory.name}" name="name">
		</c:if>
		<input id="mobileHistryId" style="display: none" value="${mobileHistory.id}" name="id"> 
		<input id="addTelephoneRecordFlag" type="hidden" value="${flag}">
		<table class="table_ui W90 center_m" id="addPhoneRecordTable">
			<tr>
				<th>姓名:</th>
				<td>
					<%-- <c:if test="${empty flag}">
						${mobileHistory.name}
					</c:if> 
					<c:if test="${not empty flag}">
						<select class="easyui-combobox select" name="name" data-options="prompt:'选择姓名',required:true,panelHeight:'auto',editable:false,value:''">
							<c:forEach items="${names}" var="item" varStatus="itemStatr">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select>
						<label style="color: #2779AA;">与申请人关系:</label>
						<select class="easyui-combobox select" name="nameTitle" data-options="prompt:'选择关系',required:true,panelHeight:'auto',editable:false,value:''">
							<c:forEach items="${relationResVOList}" var="item" varStatus="itemStatr">
								<option value="${item.contactRatetion}">${item.contactRatetion}</option>
							</c:forEach>
						</select>
					</c:if> --%>
					
					<!-- 如果不是内匹 -->
					<c:if test="${empty flag}">
						${mobileHistory.name}
					</c:if>
					<!-- 如果是内匹 -->
					<c:if test="${not empty flag}">
						<c:choose>
							<c:when test="${fn:length(contactList) > 2}">
								<select class="easyui-combobox select" name="name" data-options="prompt:'选择姓名',required:true,panelHeight:'auto',editable:false,value:''">
									<c:forEach items="${contactList}" var="item" varStatus="v">
										<option value="${item.contactName}">${item.contactName}</option>
									</c:forEach>
								</select>
								<label style="color: #2779AA;">与申请人关系:</label>
								<select class="easyui-combobox select" name="nameTitle" data-options="prompt:'选择关系',required:true,panelHeight:'auto',editable:false,value:''">
									<c:forEach items="${contactList}" var="item" varStatus="v">
										<option ms-attr="{value:{{ getContactRelation('${item.contactRelation}') }} }" >{{ getContactRelation('${item.contactRelation}') }}</option>
									</c:forEach>
								</select>	
							</c:when>
							<c:otherwise>
								${contactList[0].contactName}
								<input ms-attr="{value: getContactRelation('${contactList[0].contactRelation}') }" style="display: none" name="nameTitle" >
								<input style="display: none" value="${contactList[0].contactName}" name="name">
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>
				<th>致电电话:</th>
				<td>${mobileHistory.telPhone}</td>
			</tr>
			<tr>
				<th class="requiredFontWeight">接听人关系:</th>
				<td>
					<select class="easyui-combobox select" id="telRelationType" name="telRelationType" data-options="editable:false,required:true,value:'${mobileHistory.telRelationType}'">
							<option value="本人">本人</option>
							<option value="配偶">配偶</option>
							<option value="亲属">亲属</option>
							<option value="同事">同事</option>
							<option value="其他">其他</option>
							<option value="无人接听">无人接听</option>
					</select>
				</td>
				<th>核查时间:</th>
				<td>${mobileHistory.telDate}</td>
			</tr>
			<tr>
				<th class="requiredFontWeight">电核备注:</th>
				<td colspan="4"><input class="easyui-textbox W70" id="askContent" data-options="required:true,validType:'length[1,2000]',multiline:true,height:150" value="${mobileHistory.askContent}" name="askContent"></td>
			</tr>
		</table>
		<div class="h20"></div>
		<div class="float_right">
			<a id="first_addTelephoneRecord_a" class="easyui-linkbutton_ok05 l-btn l-btn-small" onclick="addTelephoneRecord('${title}')">保存</a>
		</div>
	</form>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstAddTelephoneRecord.js"></script>
<script type="text/javascript">
   pageInit({
       $id: 'firstAddTelPage'
   });
</script>
</html>