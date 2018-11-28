<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--引入标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- 质检员反馈页面 -->
<div id="feedbackProcess">
    <h3>质检历史结论：</h3>
    <div>
        <table id="qualityHistory"></table>
    </div>
    <h3>反馈结果：</h3>
    <div>
        <table id="feedBackRecord"></table>
    </div>
    <form id="attachmentUploadParamForm">
        <%--附件上传参数设置--%>
        <input type="hidden" name="picImgUrl" value="${picImgUrl}">
        <input type="hidden" name="nodeKey" value="${nodeKey}">
        <input type="hidden" name="sysName" value="${sysName}">
        <input type="hidden" name="operator" value="${operator}">
        <input type="hidden" name="jobNumber" value="${jobNumber}">
    </form>
    <%--确认F_000000、争议F_000001、仲裁F_000002、定版F_000003--%>
    <form id="qualityFeedbackM2Form">
        <div>
            <table>
                <tr>
                    <td>
                        <input type="hidden" name="checkResId" value="${checkResId}">
                        <input type="hidden" name="qualityCheckId" value="${qualityCheckId}">
                        <input type="hidden" name="loanNo" value="${loanNo}">
                    </td>
                </tr>
                <tr>
                    <th>
                        <input type="radio" name="type" value="F_000000">确认：
                    </th>
                    <td colspan="5">
                        <input class="easyui-textbox" style="width: 500px; height: 50px;" multiline="true"
                               id="conclusionConfirmInputId"
                               name="opinion">
                    </td>
                </tr>
                <tr>
                    <th>
                        <input type="radio" name="type" value="F_000001">争议：
                    </th>
                    <td colspan="5">
                        <input class="easyui-textbox" style="width: 500px; height: 50px;" multiline="true"
                               id="conclusionConfuseInputId"
                               name="opinion">
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <h3>反馈附件：</h3>
    <div>
        <table id="feedBackAttachmentTable"></table>
    </div>
    <table>
        <div id="feedBackAttachmentBtn" class="padding_20 display_none" onclose="reloadFeedBackAttachment()"></div>
    </table>
    <a class="easyui-linkbutton" onclick="saveFeedBackRecord();">提交</a>
    <div id="attachmentDetailsDialog"></div>
</div>
<div id="uploadToolbar" style="">
    <a class="easyui-linkbutton" onclick="feedBackAttachmentUpload();">附件上传</a>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityFeedBack/qualityFeedBack.js"></script>
