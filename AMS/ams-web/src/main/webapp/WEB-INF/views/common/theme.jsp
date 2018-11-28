<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
    var staticPath = "${ctx}/resources";
    var pmsUrl = '${pmsSystemVO.url}';
    var account = '${currentAccount}';
    var attribute = '';

    // 主题自动选择
    $(document).ready(function ($) {
        $.getJSON(pmsUrl + "/api/getUserInfo?callback=?", {
            account : account
        }, function(result) {
            if (result.success && result.data) {
                attribute = result.data;
                if (attribute && attribute.defaultTheme) {
                    var link = $("#easyuiTheme");
                    link.attr("href", staticPath + "/easyui/themes/" + attribute.defaultTheme + "/easyui.css");
                }
            }
        });

       /*
       $.get(pmsUrl + "/getAttribute", {}, function (result) {
            if (result.success && result.data) {
                attribute = result.data;
                if (attribute && attribute.defaultTheme) {
                    var link = $("#easyuiTheme");
                    link.attr("href", staticPath + "/easyui/themes/" + attribute.defaultTheme + "/easyui.css");
                }
            }
        });
        */
    });
</script>