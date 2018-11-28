<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<META HTTP-EQUIV="expires" CONTENT="0">
<title>${applyBasiceInfo.name}-电核汇总</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
<style type="text/css">
	#contact_phone_table {
		/*为表格设置合并边框模型*/
		 border-collapse: collapse;
		 /*列宽由表格宽度和列宽度设定*/
		 table-layout: fixed;
	}

	#contact_phone_table td{
		white-space: nowrap;
		word-wrap: normal;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.phoneCity{
		color:red;
	}
</style>
</head>

<body class="padding_20 ms-controller" ms-controller="page" style="width: 100%;">
	<c:set var="org_province_city" value="${fn:trim(org.province)}${fn:trim(org.city)}" />
	<div class="easyui-panel" title="电核汇总" style="width: 100%"></div>
	<div id="firstTelephoneSummartPage" style="width: 100%">
		<div class="easyui-tabs" style="margin-bottom: 20px;">
			<div title="基本信息">
				<input id="loanNo_hidden" type="hidden" value="${applyBasiceInfo.loanNo}" placeholder="借款编号"> 
				<input id="branchId" type="hidden" value="${applyBasiceInfo.owningBranchId}" placeholder="营业部id">
				<input id="branchCity" type="hidden" value="${org.cityName}" placeholder="营业部机构市信息">
				<input id="branchProvince" type="hidden" value="${org.province}" placeholder="营业部机构省信息">
				<form id="basicInfo_form">
					<input type="hidden" value="${applyBasiceInfo.version}" name="version" id="telephoneSummarybasicInfo">
					<table class="table_list W100 table_cell" id="basicInfo_table">
						<tr>
							<td width="12%">申请人姓名:</td>
							<td>${applyBasiceInfo.name}</td>
							<td width="12%">单位名称:</td>
							<td width="18%"><input type="hidden" class="input" name="corpName" value="${applyBasiceInfo.corpName}"> ${applyBasiceInfo.corpName}</td>
						</tr>
						<!-- 前前进件 -->
						<c:if test="${1== applyBasiceInfo.zdqqApply}">
							<tr>
								<td>单位地址:</td>
								<td colspan="2">${moneyApplyInfo.applicantInfo.workInfo.corpProvince}&nbsp;${moneyApplyInfo.applicantInfo.workInfo.corpCity}&nbsp;${moneyApplyInfo.applicantInfo.workInfo.corpZone}&nbsp;${applyBasiceInfo.corpAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="firstTelephoneSummaryBaiduMaps('${moneyApplyInfo.applicantInfo.workInfo.corpProvince}','${moneyApplyInfo.applicantInfo.workInfo.corpCity}','${moneyApplyInfo.applicantInfo.workInfo.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
									<a class="easyui-linkbutton" style="margin-left: 10px" onclick="showMapFromHomeToCompany('${moneyApplyInfo.applicantInfo.personalInfo.homeState}','${moneyApplyInfo.applicantInfo.personalInfo.homeCity}','${moneyApplyInfo.applicantInfo.personalInfo.homeZone}','${applyBasiceInfo.homeAddress}','${moneyApplyInfo.applicantInfo.workInfo.corpProvince}','${moneyApplyInfo.applicantInfo.workInfo.corpCity}','${moneyApplyInfo.applicantInfo.workInfo.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-car" aria-hidden="true"></i>导航</a>
								</td>
							</tr>
							<tr>
								<td>家庭地址:</td>
								<td colspan="2">${moneyApplyInfo.applicantInfo.personalInfo.homeState}&nbsp;${moneyApplyInfo.applicantInfo.personalInfo.homeCity}&nbsp;${moneyApplyInfo.applicantInfo.personalInfo.homeZone}&nbsp;${applyBasiceInfo.homeAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="firstTelephoneSummaryBaiduMaps('${moneyApplyInfo.applicantInfo.personalInfo.homeState}','${moneyApplyInfo.applicantInfo.personalInfo.homeCity}','${moneyApplyInfo.applicantInfo.personalInfo.homeZone}','${applyBasiceInfo.homeAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
								</td>
							</tr>
						</c:if>
						<!-- 非前前进件 -->
						<c:if test="${1!= applyBasiceInfo.zdqqApply}">
							<tr>
								<td>单位地址:</td>
								<td colspan="2">${applyInfo.basicInfoVO.workInfoVO.corpProvince}&nbsp;${applyInfo.basicInfoVO.workInfoVO.corpCity}&nbsp;${applyInfo.basicInfoVO.workInfoVO.corpZone}&nbsp;${applyBasiceInfo.corpAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="firstTelephoneSummaryBaiduMaps('${applyInfo.basicInfoVO.workInfoVO.corpProvince}','${applyInfo.basicInfoVO.workInfoVO.corpCity}','${applyInfo.basicInfoVO.workInfoVO.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
									<a class="easyui-linkbutton" style="margin-left: 10px" onclick="showMapFromHomeToCompany('${applyInfo.basicInfoVO.personInfoVO.homeState}','${applyInfo.basicInfoVO.personInfoVO.homeCity}','${applyInfo.basicInfoVO.personInfoVO.homeZone}','${applyBasiceInfo.homeAddress}','${applyInfo.basicInfoVO.workInfoVO.corpProvince}','${applyInfo.basicInfoVO.workInfoVO.corpCity}','${applyInfo.basicInfoVO.workInfoVO.corpZone}','${applyBasiceInfo.corpAddress}')"><i class="fa fa-car" aria-hidden="true"></i>导航</a>
								</td>
							</tr>
							<tr>
								<td>家庭地址:</td>
								<td colspan="2">${applyInfo.basicInfoVO.personInfoVO.homeState}&nbsp;${applyInfo.basicInfoVO.personInfoVO.homeCity}&nbsp;${applyInfo.basicInfoVO.personInfoVO.homeZone}&nbsp;${applyBasiceInfo.homeAddress}</td>
								<td>
									<a class="easyui-linkbutton" onclick="firstTelephoneSummaryBaiduMaps('${applyInfo.basicInfoVO.personInfoVO.homeState}','${applyInfo.basicInfoVO.personInfoVO.homeCity}','${applyInfo.basicInfoVO.personInfoVO.homeZone}','${applyBasiceInfo.homeAddress}')"><i class="fa fa-map-marker" aria-hidden="true"></i>百度地图</a>
								</td>
							</tr>
						</c:if>
						<tr>
							<td>第三方查询备注:</td>
							<td colspan="2"><input class="input" id="ThridRemark" name="theThirdPartyNote" value="${applyBasiceInfo.theThirdPartyNote}"> &nbsp; &nbsp;<span class="otherRemarks">
								<input id="first_otherRemarks_textbox" type="text" class="easyui-textbox" data-options="required:true,height:70,multiline:true,validType:'length[1,200]'" name="theThirdPartyNoteDetails" value="${applyBasiceInfo.theThirdPartyNoteDetails}"></span></td>
							<td><a class="easyui-linkbutton_ok05 l-btn l-btn-small" onclick="updateBasicInfo()">保存</a></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div title="联系人信息">
				<div title="联系人信息">
					<form id="customInfo_form">
						<table class="table_list W100" id="customInfoTable">
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
									<td>第${status.count}联系人</td>
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
		<%--联系电话 start--%>
		<a class="easyui-linkbutton_ok07 l-btn l-btn-small" onclick="addPhoneDialog()">第三方电话增加</a>
		<a class="easyui-linkbutton_ok06 l-btn l-btn-small" onclick="seachBaidu()">号码查询</a>
		<div class="h20" style="width: 100%"></div>
		<div class="easyui-layout"  style="height:610px;width: 100%" data-options="fit:false" >
			<div data-options="region:'west',split:true" title="联系人信息" style="width:47%;">
				<table class="table_list W100" id="contact_phone_table">
					<thead>
						<tr>	
							<td width="45px;">关系</td>
							<td width="100px;">姓名</td>
							<td width="65px;">电话类型</td>
							<td width="140px;">电话号码</td>
							<td width="120px;">归属地</td>
							<td width="180px;">操作</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${contactList}" var="each" varStatus="v">
							<c:forEach items="${contactList[v.index]}" var="item" varStatus="status">
								<c:choose>
									<c:when test="${status.first}">
										<tr id="item-${item.sequenceNum}" class="merge">
										<%--动态合并行rowspan="3" colspan --%>
										<td rowspan="${fn:length(contactList[v.index]) }">{{ getContactRelation('${item.contactRelation}') }}</td>
										<td rowspan="${fn:length(contactList[v.index]) }">${item.contactName}</td>
									</c:when>
									<c:otherwise>
										<tr id="item-${item.sequenceNum}">
									</c:otherwise>
								</c:choose>
								<td>${item.remark}</td>
								<td>${item.phone}</td>
								<%--标红匹配 --%>
								<c:choose>
									<c:when test="${empty item.phoneCity}">
										<td>
											<a href="javaScript:void(0);" title="${item.phoneCity}">查无结果</a>
										</td>
									</c:when>
									<c:when test="${!item.matchBrach}">
										<td>
											<a href="javaScript:void(0);" title="${item.phoneCity}" style="color: red;">${item.phoneCity}</a>
										</td>
									</c:when>
									<c:otherwise>
										<td><a href="javaScript:void(0);" title="${item.phoneCity}">${item.phoneCity}</a></td>
									</c:otherwise>
								</c:choose>
									<td>
										<a href="javaScript:void(0);" onclick="addPhoneRecordDialog(this,'${item.contactName}','${item.contactRelation}')">记录</a>&nbsp;|&nbsp;
										<a href="javaScript:void(0);" onclick="seachForBaidu('${item.phone}')">号码查询</a>
									</td>
								</tr>
								
							</c:forEach>
							
						</c:forEach>
						
						<%-- 第三方新增联系电话信息 start --%>
						<c:forEach items="${mobileHistoryInfoList}" var="item" varStatus="status">
							<tr class="mobileHistoryInfo">
								<td>${item.nameTitle}</td>
								<td>${item.name}</td>
								<td>${item.telPhoneType}</td>
								<c:if test="${item.telPhoneType=='固定电话'}">
									<td><input type="text" class="easyui-textbox input" data-options="required:true,width:140,validType:'telNum'" value="${item.telPhone}"></td>
								</c:if>
								<c:if test="${item.telPhoneType=='移动电话'}">
									<td><input type="text" class="easyui-textbox input" data-options="required:true,width:140,validType:['length[11,11]','mobile']" value="${item.telPhone}"></td>
								</c:if>
								<%--标红匹配 --%>
								<c:choose>
									<c:when test="${empty item.phoneCity}">
										<td>
											<a href="javaScript:void(0);" title="查无结果">查无结果</a>
										</td>
									</c:when>
									<c:otherwise>
										<c:choose>
											<%--如果省市为空，或者号码归属地不包含省市，则标红 --%>
											<c:when test="${empty org_province_city || !fn:contains(item.phoneCity, org_province_city)}">
												<td><a href="javaScript:void(0);" title="${item.phoneCity}" style="color: red;">${item.phoneCity}</a></td>
											</c:when>
											<c:otherwise>
												<td><a href="javaScript:void(0);" title="${item.phoneCity}">${item.phoneCity}</a></td>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
								<td>
									<input type="hidden" value="${item.telPhone}" placeholder="上一次修改记录"/>
									<a href="javaScript:void(0);" onclick="addPhoneRecordDialog(this)">记录</a>&nbsp;|&nbsp;
									<a href="javaScript:void(0);" onclick="modifyThridContact(${item.id},this,'${item.createdDate}')">修改</a>&nbsp;|&nbsp;
									<a href="javaScript:void(0);" onclick="deleteMobile('${item.id}',this)">删除</a>&nbsp;|&nbsp;
									<a href="javaScript:void(0);" onclick="seachForBaidu('${item.telPhone}', this)">号码查询</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<%--联系电话end --%>
			
			<%--电核记录信息 start --%>
			<div data-options="region:'center'" title="电核记录" style="padding:5px;width: 53%">
				<!-- <h3>电核记录</h3> -->
				<table class="table_list" id="phoneRecord_table"></table>
			</div>
			<%--电核记录信息 end --%>
		</div>
		
		<%-- 第三方电话增加面板 start --%>
		<div id="addPhoneDialog" class="padding_20 display_none">
			<form id="addPhoneForm">
				<table class="table_ui W100">
					<tr>
						<th>第三方来源:</th>
						<td>
							<select id="thirdPartSource" class="easyui-combobox select" data-options="required:true,value:'',editable:false">
								<option value="114">114</option>
								<option value="外网">外网</option>
								<option value="信用报告">信用报告</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>电话类型:</th>
						<td>
							<select id="thirdPartphoneType" class="easyui-combobox select" data-options="required:true,value:'',editable:false,onChange:changeTelephoneType">
								<option value="1">固定电话</option>
								<option value="2">移动电话</option>
							</select>
						</td>
					</tr>
					<tr>
						<th class="requiredFontWeight">致电电话:</th>
						<td><input id="phoneNumber" class="easyui-textbox input" data-options="validType:['checkTel[\'#thirdPartSource\',\'.mobileHistoryInfo\']','telNum'],required:true"></td>
					</tr>
				</table>
			</form>
		</div>
		<%-- 第三方电话增加面板 end --%>
	</div>
	
</body>

<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstTelephoneSummary.js"></script>
<script type="text/javascript">
   pageInit({
       $id: 'page'
   });
</script>
</html>