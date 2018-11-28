<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-质检意见</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">

<form id="qualityOpinion__add_Form">
	<h2>质检结论 :</h2>
	<input type="checkbox" id="addQualityOpinoin_checkbox_noError" name="checkResult" value="1">
	无差错
	<div class="h20"></div>
	<div id="addQualityOpinoin_quality_opoin" class="easyui-panel">
		<!-- 初审  -->
		<table class="table_ui W100">
			<tr>
				<td id = "addQualityOpinion_quality_firstApprove${loanNo}" ></td>
			</tr>
		</table>

		<!-- 终审  -->
		<table class="table_ui W100">
			<tr>
				<td id = "addQualityOpinion_quality_finishApprove" ></td>
			</tr>
		</table>

		<!-- 领导  -->
		<table class="table_ui W100">
			<tr>
				<td>
					<input type="hidden" name="leaderCheckError" value="apply-check">
					<label id="addQualityOpi_leaderCheckError_label" class="hand">&nbsp;组长/主管/经理</label>
					<input type="text" id="addQualityOpi_leader_approvePerson" class="easyui-combobox input" name="leader">
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
					<input type="text" id="addQualityOpi_leader_errorCode" class="easyui-combobox input errorCode" >
				</td>
			</tr>
			<tr>
				<td>
					<a href="javaScript:void(0);" onclick="diplayNextTr(this)">
						<span style="font-weight: bold">+&nbsp;</span>
					</a>
					质检意见
				</td>
			</tr>
			<tr>
				<td id = "addQualityOpinion_leaderAppHistory_checkView"></td>
			</tr>
			<tr>
				<td>
					<textarea id="addQualityOpi_leaderApp_checkView" maxlength="1000" class="textarea checkView" name="checkView"></textarea>
				</td>
			</tr>
		</table>
		<div class="h20"></div>

		<!-- 附件上传  -->
		<div>
			<div id="addQualityOpinion_attach_toolBarBtn">
				<table class="table_ui W90">
					<tr>
						<td>附件：</td>
						<td style = "text-align:right;">
							<a class="easyui-linkbutton" id="addQuality_quality_attachmentUpload" data-options="plain:true" onclick="qualityAttachmentUpload();">
								<i class="fa fa-external-link" aria-hidden="true"></i>附件上传
							</a>&nbsp;
						</td>
					</tr>
				</table>
			</div>
			<!-- 附件列表 -->
			<div id ="addQualityOpinion_quality_attach" style="width: 100%">

			</div>
		</div>

		<!-- PIC上传附件窗口 -->
		<div id="addQuality_quality_attachUpWindow" class="padding_20" style="display: none"></div>

		<!-- 隐藏组件 -->
		<div>
			<input type = "hidden" id="addQualityOpinion_quality_flag" value='${flag}'>
			<input type = "hidden" id="addQualityOpinion_quality_loanNo" value='${loanNo}'>
			<input type = "hidden" id="addQualityOpinion_quality_picImgUrl" value='${picImgUrl}'>
			<input type = "hidden" id="addQualityOpinion_quality_nodeKey" value='${nodeKey}'>
			<input type = "hidden" id="addQualityOpinion_quality_sysName" value='${sysName}'>
			<input type = "hidden" id="addQualityOpinion_quality_operator" value='${operator}'>
			<input type = "hidden" id="addQualityOpinion_quality_jobNumber" value='${jobNumber}'>
			<input type = "hidden" id="addQualityOpinion_quality_approvalMap"  value='${approvalMapJson}'>
			<input type = "hidden" id="addQualityOpinion_quality_checkResListJson"  value='${checkResListJson}'>
			<input type="hidden" id="addQualityOpinion_quality_checkLeader" value="${checkLeader}" >
			<input type="hidden" id="addQualityOpinion_quality_roleCode" value='${roleCode}'>
			<input type="hidden" id="addQualityOpinion_quality_checkUser" value='${checkUser}'>
		</div>

	</div>
	<div class="h20"></div>
	<table class="table_ui W90  center_m">
		<tr>
			<td id ="addQualityOpinion_button_completeQuality"><a class="easyui-linkbutton" onclick="completeQuality('complete');">完成质检</a></td>
			<td id ="addQualityOpinion_button_holdQuality"><a class="easyui-linkbutton" onclick="completeQuality('savedone');">保存</a></td>
			<td id ="addQualityOpinion_button_pauseQuality"><a class="easyui-linkbutton" onclick="completeQuality('pause');">暂存</a></td>
			<td id ="addQualityOpinion_button_reCheckQuality"><a class="easyui-linkbutton" onclick="completeQuality('reCheck');">申请复核</a></td>
		</tr>
	</table>
</form>
</body>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/addQualityOpinion.js"></script>
</html>