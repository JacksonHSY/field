<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!-- 质检反馈 -->
<div id="feedbackProcess" >
    <h3>质检历史结论：</h3>
    <div>
        <table id="qualityHistoryDetails"></table>
    </div>
    <h3>反馈结果：</h3>
    <div>
        <table id="feedBackRecordDetails"></table>
    </div>
    <div>
        <input type="hidden" name="qualityCheckId"  value="${qualityCheckId}">
        <input type="hidden" name="checkResId" value="${checkResId}">
        <input type="hidden" name="loanNo" value="${loanNo}">
        <h3>反馈附件：</h3>
        <div>
            <table id="feedBackAttachmentTableDetails"></table>
        </div>
    </div>
    <div id="attachmentDetailsDialogDetails"></div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityFeedBack/feedBackDetails.js"></script>
