<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-质检意见</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<form id="qualityOpinion__query_Form">
	<h2>质检结论 :</h2>
	<input type="checkbox" id="queryQualityOpinoin_checkbox_noError" disabled="disabled" value="1">
	无差错
	<div class="h20"></div>
	<div id="queryQualityOpinoin_quality_opoin" class="easyui-panel">
		<!-- 初审  -->
		<table class="table_ui W100 checkPart">
			<tr>
				<td id = "queryQualityOpion_quality_firstApprove" ></td>
			</tr>
		</table>
		
		<!-- 终审  -->
		<table class="table_ui W100 finalPart">
			<tr>
				<td id = "queryQualityOpion_quality_finishApprove" ></td>
			</tr>
		</table>
		
		<!-- 领导  -->
		<table class="table_ui W100 leaderPart">
			<tr>
				<td>
					<input type="hidden" name="leaderCheckError" value="apply-check"><label class="hand">&nbsp;组长/主管/经理</label>
					<input type="text" id="queryQualityOpi_leader_approvalLeader" class="easyui-combobox input" name = "leader" >
				</td>
			</tr>
			<tr>
				<td>
					<input type="radio" name="leaderCheckResult" value="E_000004">重大差错
					<input type="radio" name="leaderCheckResult" value="E_000003">一般差错
					<input type="radio" name="leaderCheckResult" value="E_000001">预警
					<input type="radio" name="leaderCheckResult" value="E_000002">建议
					<input type="radio" name="leaderCheckResult" value="E_000000">无差错
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" id="queryQualityOpi_leader_errorCode" class="easyui-combobox input errorCode" >
				</td>
			</tr>
			<tr>
				<td>
					<a href="javaScript:void(0);" onclick="diplayNextTr(this)"><span style="font-weight: bold">+&nbsp;</span></a>质检意见
				</td>
			</tr>
			<tr>
				<td id = "queryQualityOpi_leaderQuality_chickView"></td>
			</tr>
		</table>
		<div class="h20"></div>
		
		<!-- 附件上传  -->
		<div>
			<div id="queryQualityOpion_attach_toolBarBtn">
				<table class="table_ui W90">
					<tr>
						<td>附件：</td>
						<td style = "text-align:right;">
							<a class="easyui-linkbutton" id="addQuality_quality_attachmentUpload" data-options="plain:true" onclick="qualityAttachmentUpload()"><i class="fa fa-external-link" aria-hidden="true"></i>附件上传</a>&nbsp;
						</td>
					</tr>
				</table>
			</div>
			<table id = "queryQualityOpion_quality_attach"></table>
			<div id ="attachmentDetailsDialog"  ></div>
			
		</div>
		<div>
			<table>
				<div id="addQuality_quality_attachUpWindow" class="padding_20 display_none" onclose="reloadFeedBackAttachment()"></div>
			</table>
		</div>
		
		<!-- 隐藏组件 -->
		<div>
			<input type = "hidden" id="queryQualityOpion_quality_flag" value='${flag}'>
			<input type = "hidden" id="queryQualityOpion_quality_loanNo" value='${loanNo}'>
			<input type = "hidden" id="queryQualityOpion_quality_picImgUrl" value='${picImgUrl}'>
			<input type = "hidden" id="queryQualityOpion_quality_nodeKey" value='${nodeKey}'>
			<input type = "hidden" id="queryQualityOpion_quality_sysName" value='${sysName}'>
			<input type = "hidden" id="queryQualityOpion_quality_operator" value='${operator}'>
			<input type = "hidden" id="queryQualityOpion_quality_jobNumber" value='${jobNumber}'>
			<input type = "hidden" id="queryQualityOpion_quality_approvalMap"  value='${approvalMapJson}'>
			<input type = "hidden" id="queryQualityOpion_quality_checkResListJson"  value='${checkResListJson}'>
			<input type="hidden" id="addQualityOpinion_quality_checkLeader" value="${checkLeader}" >
			<input type="hidden" id="queryQualityOpion_quality_roleCode" value='${roleCode}'>
		</div>
		
	</div>
	<div class="h20"></div>
		
</form>
</body>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/queryQualityOpoin.js"></script>
</html>