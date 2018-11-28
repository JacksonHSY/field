<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<input id="qualityCheckId" name="qualityCheckId" value="${qualityCheckId}" type="hidden">
<form id="qualityOpinion__add_Form">
	<h2>质检结论 :</h2>
	<input type="checkbox" id="addQualityOpinoin_checkbox_noError" name="checkResult" value="1">
	无差错
	<div class="h20"></div>
	<div id="addQualityOpinoin_quality_opoin" class="easyui-panel">
		<!-- 初审  -->
		<table class="table_ui W100">
			<tr>
				<td id = "addQualityOpinion_quality_firstApprove" ></td>
				<td>
					<input type = "hidden" id="addQualityOpinion_quality_firstApproveNum" value='${firstIds}'>
				</td>
			</tr>
		</table>
		
		<!-- 终审  -->
		<table class="table_ui W100">
			<tr>
				<td id = "addQualityOpinion_quality_finishApprove" ></td>
				<td>
					<input type = "hidden" id="addQualityOpinion_quality_finishApproveNum" value='${finishIds}'>
				</td>
			</tr>
		</table>
		
		<!-- 领导  -->
		<table class="table_ui W100">
			<tr>
				<td id = "addQualityOpinion_quality_leaderApprove" ></td>
				<td>
					<input type = "hidden" id="addQualityOpinion_quality_leaderApproveNum" value='${leaderIds}'>
				</td>
			</tr>
		</table>
		
	</div>
</form>

<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/QualityHistoryOpinion.js"></script>
