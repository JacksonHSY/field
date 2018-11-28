<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyBasiceInfo.name}-办理</title>
<jsp:include page="../common/commonJS.jsp"></jsp:include>
<jsp:include page="../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20">
	<div class="float_left W60" style="height: 750px;">
		<iframe src="${picImageUrl}/api/filedata?nodeKey=${picApproval}&sysName=${sysCode}&appNo=${applyBasiceInfo.loanNo}&operator=${operator}&jobNumber=${jobNumber}&nodeKeyDetail=${reconsiderNode}&closingDate=${nodeCreateDate}" style="width: 100%; height: 99.5%; padding: 0px; margin: 0px; border: 0px; display: table"></iframe>
	</div>
	<div class="float_right W40">
		<div class="easyui-panel padding_20" title="审批办理">
			<input id="version_id" type="hidden" value="${applyBasiceInfo.version}">
			<table class="table_ui W100  center_m">
				<tr>
					<td><a class="easyui-linkbutton" onclick="customerInfoWithoutRed('${applyBasiceInfo.loanNo}')"><span class="icon_01"></span>客户信息</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationInsideMatchDialog('${applyBasiceInfo.loanNo}')"><span class="icon_02"></span>内部匹配</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationTelephoneSummaryDialog('${applyBasiceInfo.loanNo}')"><span class="icon_03"></span>电核汇总</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationLogNotesInfoDialog('${applyBasiceInfo.loanNo}')"><span class="icon_04"></span>日志备注</a></td>
				</tr>
				<tr>
					<td><a class="easyui-linkbutton" onclick="reconsiderationCentralBankCreditDialog('${applyBasiceInfo.reportId}','${applyBasiceInfo.loanNo}')"><span class="icon_05"></span>央行征信</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationExternalCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>外部征信</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationSuanHuaCreditDialog('${applyBasiceInfo.loanNo}')"><span class="icon_06"></span>算话征信</a></td>
					<td><a class="easyui-linkbutton" onclick="reconsiderationApprovalOpinionDialog('${applyBasiceInfo.loanNo}')"><span class="icon_07"></span>审批意见</a></td>
				</tr>
				<tr>
					<td><a class="easyui-linkbutton" onclick="reviewLogDialog('${applyBasiceInfo.loanNo}')"><span class="icon_04"></span>复议日志</a></td>
				</tr>
			</table>
			<hr>
			<form>
				<table class="table_ui W100 center_m">
					<tr>
						<th>借款编号:</th>
						<td id="reconsideration_loanNo">${applyBasiceInfo.loanNo}</td>
						<th>身份证号码:</th>
						<td>${applyBasiceInfo.idNo}<input type="hidden" id="reconsideration_rtfState" value="${applyBasiceInfo.rtfState}"></td>
					</tr>
					<tr>
						<th>申请人姓名:</th>
						<td>${applyBasiceInfo.name}</td>
						<th>申请产品:</th>
						<td><c:if test="${'证大前前' !=applyBasiceInfo.initProductName}">${applyBasiceInfo.initProductName}</c:if></td>
					</tr>

					<tr>
						<th>申请期限:</th>
						<td>${applyBasiceInfo.applyTerm}</td>
						<th>申请额度:</th>
						<td class="numFormat"><fmt:formatNumber type="number" value="${applyBasiceInfo.applyLmt}" pattern="0" maxFractionDigits="0" /></td>
					</tr>
					<tr>
						<th>借款用途:</th>
						<td>${applyBasiceInfo.creditApplication}</td>
						<th>进件营业部:</th>
						<td>${applyBasiceInfo.owningBranch}</td>
					</tr>
					<tr>
						<th>是否加急:</th>
						<td><c:if test="${applyBasiceInfo.ifPri==0}">否</c:if> <c:if test="${applyBasiceInfo.ifPri ==1}">是</c:if></td>
						<th>客户经理:</th>
						<td>${applyBasiceInfo.barnchManagerName}</td>
					</tr>
					<tr>
						<th>备注</th>
						<td colspan="3" class="W80">${applyBasiceInfo.remark}</td>
					</tr>
					<!-- start反欺诈 -->
					<%--<tr>
						<th>反欺诈评分:</th>
						<td>${applyBasiceInfo.antiFraudScore}</td>
						<th>欺诈风险评估:</th>
						<td>${applyBasiceInfo.antiRiskRate}</td>
						<th>反欺诈预警:</th>
						<td>${applyBasiceInfo.antiFraudWarning}</td>
					</tr>--%>
					<tr>
						<th>众安反欺诈等级:</th>
						<td>${applyBasiceInfo.zhongAnRiskGrade}</td>
						<%--<th>众安反欺诈结果:</th>
						<td>${applyBasiceInfo.zhongAnRiskResult}</td>--%>
						<%--<th>芝麻信用分:</th>--%>
						<%--<td>${applyBasiceInfo.sesameCreditValue}</td>--%>
						<th>综合信用评级:</th>
						<td>${applyBasiceInfo.comCreditRating}</td>
					</tr>
					<tr>
						<th class="markRed"><c:if test="${'Y' == applyBasiceInfo.isAntifraud}">欺诈可疑</c:if></th>
						<td colspan="3"></td>
					</tr>
					<!-- end反欺诈 -->
				</table>
			</form>
			<hr>
			<table class="table_ui W100 center_m center_text">
				<tr>
					<c:if test="${ 'F1'==reconsiderNode}">
						<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderLevelOnePassORReject('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','SUBMIT')">提交</a></td>
						<td><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="reconsiderationApprovalBackDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">退回</a></td>
						<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderLevelOnePassORReject('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','REJECT')">拒绝复议</a></td>
					</c:if>
					<c:if test="${'F2' == reconsiderNode}">
						<c:if test="${empty reconsiderNodeStateThree}">
							<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderPass('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">同意复议</a></td>
							<td><a class="easyui-linkbutton_ok03 l-btn l-btn-small" onclick="reconsiderationApprovalBackDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">退回</a></td>
							<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderReject('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">拒绝复议</a></td>
							<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderSendOrReviewDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','SENDAPPROVE')">发送信审</a></td>
						</c:if>
						<c:if test="${'PASS'== reconsiderNodeStateThree}">
							<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderPass('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">同意复议</a></td>
							<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderSendOrReviewDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','SUBMITREVIEW')">提交复核</a></td>
						</c:if>
						<c:if test="${'REJECT' == reconsiderNodeStateThree}">
							<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderReject('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">拒绝复议</a></td>
							<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderSendOrReviewDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','SUBMITREVIEW')">提交复核</a></td>
						</c:if>
					</c:if>
					<c:if test="${'F3' == reconsiderNode}">
						<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderSendOrReviewDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}','SUBMIT')">提交</a></td>
						<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderationApprovalBackDialog('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">退回</a></td>
					</c:if>
					<c:if test="${'F4' == reconsiderNode}">
						<td><a class="easyui-linkbutton_ok02 l-btn l-btn-small" onclick="reconsiderPass('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">同意复议</a></td>
						<td><a class="easyui-linkbutton_ok04 l-btn l-btn-small" onclick="reconsiderReject('${applyBasiceInfo.loanNo}','${applyBasiceInfo.name}')">拒绝复议</a></td>
					</c:if>
				</tr>
			</table>
		</div>
	</div>
	<input id="reconsider_version" type="hidden" value="${version}">
	<!-- 复议退回弹出框 -->
	<div id="reconsideration_return_dialog" class="display_none">
		<form class="padding20">
			<input name="reconsiderNode" value="${reconsiderNode}" type="hidden">
			<table class="table_ui WH100">
				<tr>
					<th>备注:</th>
					<td><input id="reconsiderHandle_return_remark" class="easyui-textbox"  name="remark" data-options="required:true,height:160,width:500,validType: 'max[500]',multiline:true,prompt:'请输入备注信息',events:{keyup:function(){limitReconsiderMemo('reconsiderHandle_return_remark',this)}}">
						<br>
						<div class="countSurplusText float_right">剩余可输入500字</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!--F1提交、F1 拒绝 -->
	<div id="reconsideration_oneSubmitOrReject_dialog" class="display_none">
		<form class="padding20">
			<input name="reconsiderNode" value="${reconsiderNode}" type="hidden">
			<div class="reject_text"></div>
			<table class="table_ui WH100">
				<tr>
					<th>备注:</th>
					<td><input id="reconsiderHandle_oneSubmitOrReject_remark" class="easyui-textbox"  name="remark" data-options="required:true,height:160,width:480,validType: 'max[500]',multiline:true,prompt:'请输入备注信息',events:{keyup:function(){limitReconsiderMemo('reconsiderHandle_oneSubmitOrReject_remark',this)}}">
						<br>
						<div class="countSurplusText float_right">剩余可输入500字</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- F2、F4 同意复议 -->
	<div id="reconsideration_pass_dialog" class="display_none">
		<form class="padding20">
			<input name="reconsiderNode" value="${reconsiderNode}" type="hidden">
			<table class="table_ui WH100">
				<tr><th>差错等级:</th><td><input name="errorLevel" class="input errorLevel"></td><th>差错人:<input type="hidden" name="errorPerson"></th><td class="errorPerson"></td></tr>
				<tr><th>差错代码:</th><td colspan="3"><input name="errorCode" class="input errorCode"></td></tr>
				<tr><th>原拒绝原因:</th><td colspan="3" class="originalReject"></td></tr>
				<tr><th>修改后拒绝原因:<input type="hidden" name="reconsiderRejectCodeOne" value="RJ9999"><input type="hidden" name="reconsiderRejectCodeTwo" value="RJ99990001"></th><td>其他-修改拒绝原因<input type="hidden" name="firstReasonText" value="其他"><input type="hidden" name="secondReasonText" value="修改拒绝原因"></td></tr>
				<tr><th>备注:</th><td colspan="3"><input id="reconsiderHandle_pass_remark" class="easyui-textbox"  name="remark" data-options="required:true,height:160,width:480,validType: 'max[500]',multiline:true,prompt:'请输入备注信息',events:{keyup:function(){limitReconsiderMemo('reconsiderHandle_pass_remark',this)}}">
						<br><div class="countSurplusText float_right">剩余可输入500字</div></td>
				</tr>
			</table>
		</form>
	</div>

	<!-- F2、F4 拒绝复议 -->
	<div id="reconsideration_reject_dialog" class="display_none">
		<form class="padding20">
			<input name="reconsiderNode" value="${reconsiderNode}" type="hidden">
			<table class="table_ui WH100">
				<tr><th>差错等级:</th><td><input name="errorLevel" class="input errorLevel"></td><th>差错人:<input type="hidden" name="errorPerson"></th><td class="errorPerson" width="241px"></td></tr>
				<tr><th>差错代码:</th><td colspan="3"><input name="errorCode" class="input errorCode"></td></tr>
				<tr><th>原拒绝原因:</th><td colspan="3" class="originalReject"></td></tr>
				<tr><td colspan="4"><input id="reconsider_ifUpdateReject" type="checkbox" onclick="ifUpdateReject()"><label for="reconsider_ifUpdateReject">&nbsp;需要修改拒绝原因</label></td></tr>
				<tr><th>备注:</th><td colspan="3"><input id="reconsiderHandle_reject_remark" class="easyui-textbox"  name="remark" data-options="required:true,height:160,width:510,validType: 'max[500]',multiline:true,prompt:'请输入备注信息',events:{keyup:function(){limitReconsiderMemo('reconsiderHandle_reject_remark',this)}}">
					<br><div class="countSurplusText float_right">剩余可输入500字</div></td>
				</tr>
			</table>
		</form>
	</div>
    <!-- F2发送信审或提交复核 F3提交-->
	<div id="reconsideration_sendOrReview_dialog" class="display_none">
		<form class="padding20">
			<input name="reconsiderNode" value="${reconsiderNode}" type="hidden">
			<table class="table_ui WH100">
				<tr><td colspan="2"><input id="reconsider_levelTow_pass" type="radio" name="reconsiderNodeState" value="PASS"><label for="reconsider_levelTow_pass">&nbsp;同意复议</label></td><td colspan="2"><input id="reconsider_levelTow_reject" type="radio" name="reconsiderNodeState" value="REJECT"><label for="reconsider_levelTow_reject">&nbsp;拒绝复议</label></td></tr>
				<tr><th>差错等级:</th><td><input name="errorLevel" class="input errorLevel"></td><th>差错人:<input type="hidden" name="errorPerson"></th><td class="errorPerson" width="241px"></td></tr>
				<c:if test="${'F2' == reconsiderNode}">
				<tr><th>差错代码:</th><td colspan="3"><input name="errorCode" class="input errorCode"></td></tr>
				<tr><th><c:if test="${empty reconsiderNodeStateThree}">反馈人:</c:if><c:if test="${not empty reconsiderNodeStateThree}">F4复议</c:if></th><td><input name="reconsiderOperator" id="reconsider_reconsiderOperator" class="input"><input name="reconsiderOperatorName" type="hidden"></td></tr>
				</c:if>
				<tr><th>备注:</th><td colspan="3"><input id="reconsiderHandle_sendOrReview_remark" class="easyui-textbox"  name="remark" data-options="required:true,height:160,width:510,validType: 'max[500]',multiline:true,prompt:'请输入备注信息',events:{keyup:function(){limitReconsiderMemo('reconsiderHandle_sendOrReview_remark',this)}}">
					<br><div class="countSurplusText float_right">剩余可输入500字</div></td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript" src="${ctx}/resources/js/apply/reconsiderationHandle.js"></script>
</html>