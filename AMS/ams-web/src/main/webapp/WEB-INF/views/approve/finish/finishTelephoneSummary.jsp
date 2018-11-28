<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-电核汇总</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20 ms-controller"  ms-controller="page">
	<c:set var="org_province_city" value="${fn:trim(org.province)}${fn:trim(org.city)}"/>
	<div class="easyui-panel" title="电核汇总"></div>
	<div id="finishTelephoneSummartPage">
		<div class="easyui-tabs" style="margin-bottom: 20px;">
			<div title="基本信息">
				<input id="loanNo_hidden" type="hidden" value="${applyBasiceInfo.loanNo}" placeholder="借款编号"> 
				<form id="finishTelephoneSummary_basicInformation_Form">
					<table class="table_list W100" id="finishTelephoneSummary_basicInformation_table">
						<tr>
							<td>申请人姓名:</td>
							<td>${applyBasiceInfo.name}</td>
							<td>单位名称:</td>
							<td>${applyBasiceInfo.corpName}</td>
						</tr>
						<!-- 前前进件 -->
						<c:if test="${1== applyBasiceInfo.zdqqApply}">
							<tr>
								<td>单位地址:</td>
								<td colspan="2">${moneyApplyInfo.applicantInfo.workInfo.corpProvince}&nbsp;${moneyApplyInfo.applicantInfo.workInfo.corpCity}&nbsp;${moneyApplyInfo.applicantInfo.workInfo.corpZone}&nbsp;${applyBasiceInfo.corpAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="baiduMap('${moneyApplyInfo.applicantInfo.workInfo.corpProvince}','${moneyApplyInfo.applicantInfo.workInfo.corpCity}','${moneyApplyInfo.applicantInfo.workInfo.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
									<a class="easyui-linkbutton" style="margin-left: 10px" onclick="showMapFromHomeToCompany('${moneyApplyInfo.applicantInfo.personalInfo.homeState}','${moneyApplyInfo.applicantInfo.personalInfo.homeCity}','${moneyApplyInfo.applicantInfo.personalInfo.homeZone}','${applyBasiceInfo.homeAddress}','${moneyApplyInfo.applicantInfo.workInfo.corpProvince}','${moneyApplyInfo.applicantInfo.workInfo.corpCity}','${moneyApplyInfo.applicantInfo.workInfo.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-car" aria-hidden="true"></i>导航</a>
								</td>
							</tr>
							<tr>
								<td>家庭地址:</td>
								<td colspan="2">${moneyApplyInfo.applicantInfo.personalInfo.homeState}&nbsp;${moneyApplyInfo.applicantInfo.personalInfo.homeCity}&nbsp;${moneyApplyInfo.applicantInfo.personalInfo.homeZone}&nbsp;${applyBasiceInfo.homeAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="baiduMap('${moneyApplyInfo.applicantInfo.personalInfo.homeState}','${moneyApplyInfo.applicantInfo.personalInfo.homeCity}','${moneyApplyInfo.applicantInfo.personalInfo.homeZone}','${applyBasiceInfo.homeAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
								</td>
							</tr>
						</c:if>
						<c:if test="${1!= applyBasiceInfo.zdqqApply}">
							<tr>
								<td width="12%">单位地址:</td>
								<td colspan="2">${applyInfo.basicInfoVO.workInfoVO.corpProvince}&nbsp;${applyInfo.basicInfoVO.workInfoVO.corpCity}&nbsp;${applyInfo.basicInfoVO.workInfoVO.corpZone}&nbsp;${applyBasiceInfo.corpAddress}</td>
								<td>
									<a class="easyui-linkbutton" href="javascript:void(0);" onclick="baiduMap('${applyInfo.basicInfoVO.workInfoVO.corpProvince}','${applyInfo.basicInfoVO.workInfoVO.corpCity}','${applyInfo.basicInfoVO.workInfoVO.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
									<a class="easyui-linkbutton" style="margin-left: 10px" onclick="showMapFromHomeToCompany('${applyInfo.basicInfoVO.personInfoVO.homeState}','${applyInfo.basicInfoVO.personInfoVO.homeCity}','${applyInfo.basicInfoVO.personInfoVO.homeZone}','${applyBasiceInfo.homeAddress}','${applyInfo.basicInfoVO.workInfoVO.corpProvince}','${applyInfo.basicInfoVO.workInfoVO.corpCity}','${applyInfo.basicInfoVO.workInfoVO.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-car" aria-hidden="true"></i>导航</a>
								</td>
							</tr>
							<tr>
								<td>家庭地址</td>
								<td colspan="2">${applyInfo.basicInfoVO.personInfoVO.homeState}&nbsp;${applyInfo.basicInfoVO.personInfoVO.homeCity}&nbsp;${applyInfo.basicInfoVO.personInfoVO.homeZone}&nbsp;${applyBasiceInfo.homeAddress}</td>
								<td>
									<a class="easyui-linkbutton" href="javascript:void(0);" onclick="baiduMap('${applyInfo.basicInfoVO.personInfoVO.homeState}','${applyInfo.basicInfoVO.personInfoVO.homeCity}','${applyInfo.basicInfoVO.personInfoVO.homeZone}','${applyBasiceInfo.homeAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
								</td>
							</tr>
						</c:if>
						<tr>
							<td>第三方查询备注:</td>
							<td colspan="3"><c:if test="${'其他'==applyBasiceInfo.theThirdPartyNote}">
								${applyBasiceInfo.theThirdPartyNote}&nbsp;备注:${applyBasiceInfo.theThirdPartyNoteDetails}
								</c:if> <c:if test="${'其他'!=applyBasiceInfo.theThirdPartyNote}">
								${applyBasiceInfo.theThirdPartyNote}
								</c:if></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div title="联系人信息">
				<div title="联系人信息">
					<form id="finishTelephoneSummary_customInfo_Form">
						<table class="table_list W100" id="finishTelephoneSummary_customInfo_table">
							<thead>
								<tr>
									<td>联系人</td>
									<td>姓名</td>
									<td>与申请人关系</td>
									<td>是否知晓贷款</td>
									<td>单位名称</td>
								</tr>
							</thead>
							<c:forEach items="${1== applyBasiceInfo.zdqqApply ? moneyApplyInfo.contactInfo:applyInfo.contactInfoVOList}" var="item" varStatus="status">
								<tr>
									<td>第${status.index+1}联系人</td>
									<td>${item.contactName}</td>
									<td>${item.contactRelation}</td>
									<td>${item.ifKnowLoan}</td>
									<td>${item.contactEmpName}</td>
								</tr>
							</c:forEach>
						</table>
					</form>
				</div>
			</div>
		</div>
	<c:if test="${resType == 1}">
		<a class="easyui-linkbutton_ok06 l-btn l-btn-small" onclick="seachBaidu()">号码查询</a>
	</c:if>
		<div class="h20" style="width: 100%"></div>
		<div class="easyui-layout"  style="height:605px;width: 100%;" data-options="fit:false" >
			<%-- 联系电话start --%>
			<div data-options="region:'west',split:true" title="联系人信息" style="width: 47%;">
				<table class="table_list W100" id="contact_phone_table">
					<thead>
						<tr>	
							<td width="45px;">关系</td>
							<td width="100px;">姓名</td>
							<td width="65px;">电话类型</td>
							<td width="110px;">电话号码</td>
							<td width="130px;">归属地</td>
						<c:if test="${resType == 1}">
							<td width="100px;">操作</td>
						</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${contactList}" var="each" varStatus="v">
							<c:forEach items="${contactList[v.index]}" var="item" varStatus="status">
								<c:choose>
									<c:when test="${status.first}">
										<tr id="item-${item.sequenceNum}" class="merge">
										<%--动态合并行rowspan="3" colspan --%>
										<td rowspan="${fn:length(contactList[v.index]) }">{{ getcontactRelation('${item.contactRelation}') }}</td>
										<td rowspan="${fn:length(contactList[v.index]) }">${item.contactName}</td>
									</c:when>
									<c:otherwise>
										<tr id="item-${item.sequenceNum}">
									</c:otherwise>
								</c:choose>
								<td>${item.remark}</td>
								<td>${item.phone}</td>
								<%--标红匹配 --%>
								<td>
									<c:choose>
										<c:when test="${empty item.phoneCity}">
											<a href="javaScript:void(0);" title="查无结果">查无结果</a>
										</c:when>
										<c:otherwise>
											<c:if test="${!item.matchBrach}">
												<a href="javaScript:void(0);" title="${item.phoneCity}" style="color: red;">${item.phoneCity}</a>
											</c:if>
											<c:if test="${item.matchBrach}">
												<a href="javaScript:void(0);" title="${item.phoneCity}">${item.phoneCity}</a>
											</c:if>
										</c:otherwise>
									</c:choose>
								</td>
								<c:if test="${resType == 1}">
									<td align="center">
										<a href="javaScript:void(0);" onclick="seachForBaidu('${item.phone}')">号码查询</a>
									</td>
								</c:if>
								</tr>
							</c:forEach>
						</c:forEach>
						
						
						<%-- 第三方新增联系电话信息 start --%>
						<c:forEach items="${mobileHistoryInfoList}" var="item" varStatus="status">
							<tr class="mobileHistoryInfo">
								<td>${item.nameTitle}</td>
								<td>${item.name}</td>
								<td>${item.telPhoneType}</td>
								<td>${item.telPhone}</td>
							<%--标红匹配 --%>
							<td>
								<c:choose>
									<c:when test="${empty item.phoneCity}">
										<a href="javaScript:void(0);" title="查无结果">查无结果</a>
									</c:when>
									<c:otherwise>
										<c:choose>
											<%--如果省市为空，或者号码归属地不包含省市，则标红 --%>
											<c:when test="${empty org_province_city || !fn:contains(item.phoneCity,org_province_city)}">
												<a href="javaScript:void(0);" title="${item.phoneCity}" style="color: red;">${item.phoneCity}</a>
											</c:when>
											<c:otherwise>
												<a href="javaScript:void(0);" title="${item.phoneCity}">${item.phoneCity}</a>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<c:if test="${resType == 1}">
								<td align="center">
									<a href="javaScript:void(0);" onclick="seachForBaidu('${item.telPhone}', this)">号码查询</a>
								</td>
							</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<%-- 联系电话end --%>
		
			<%-- 电核记录信息 start --%>
			<div data-options="region:'center'" title="电核记录" style="padding:1px;width: 53%">
				<!-- <h3>电核记录</h3> -->
				<table class="table_list W100" id="finishTelephoneSummary_phoneRecord_table"></table>
			</div>
			<%-- 电核记录信息 end --%>
		</div>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishTelephoneSummary.js"></script>
<script type="text/javascript">
	pageInit({
		$id: 'page',
        qualityPersonMap: ${qualityPersonMapJson == null ? '{}' : qualityPersonMapJson},
        businessType: '${businessType}'
	});
</script>
</html>