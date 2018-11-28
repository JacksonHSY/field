<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--引入标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- 质检反馈最终定版页面-->
<div id="feedbackProcess">
    <h3>质检历史结论：</h3>
    <div>
        <table id="qualityHistory"></table>
    </div>
    <h3>反馈结果：</h3>
    <div>
        <table id="feedBackRecord"></table>
    </div>
    <h3>反馈附件：</h3>
    <div>
        <table id="feedBackAttachmentTable"></table>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityFeedBack/qualityFeedBack.js"></script>
