<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${applyInfo.applicantInfo.personalInfo.name}-前前客户信息</title>
<jsp:include page="../../common/commonJS.jsp"></jsp:include>
<jsp:include page="../../common/theme.jsp"></jsp:include>
<style>#moneyCustomerInfo_tabs .panel-title{font-size:12px;color:#fff;height:26px;line-height:16px;padding-left:30px}a.easyui-linkbutton_ok04:active,a.easyui-linkbutton_ok04:hover{line-height:30px;font-size:14px;padding-left:10px;padding-right:10px}.easyui-linkbutton_ok04,a.easyui-linkbutton_ok04:link,a.easyui-linkbutton_ok04:visited{color:#fff;background:no-repeat #0c99eb;line-height:30px;font-size:14px;padding-left:10px;padding-right:10px}a.easyui-linkbutton_ok04:hover{color:#0c99eb;background:no-repeat #d7ebf9}a.easyui-linkbutton_ok04:active{color:#fff;background:no-repeat #0c99eb}</style>
</head>
<body class="padding_20 ms-controller" ms-controller="moneyCustomerInfo">
<div class="xx_aa" id="ruleEngineHint_parent_div" style="z-index: 9999999; left: 130px; top: 7px;">消息提醒</div>
<div class="xx_dd_yuan" id="ruleEngineHint_number_div" style="left: 113px;">0</div>
<div class="xx_dd" id="ruleEngineHint_div" onMouseOver="ruleEngineHintOver()" onMouseOut="ruleEngineHintOut()" style="left: 110px;">
    <div class="xx_dd_tit">消息提醒</div>
    <ul></ul>
</div>
<div style="position: absolute; top: 38px; left: 280px; width: 85px; text-align: right; z-index: 10000;">
    <a ms-class="['easyui-linkbutton_ok05', 'l-btn', 'l-btn-small']"
       ms-click="save(applyInfo.baseInfo.loanBaseId, applyInfo.baseInfo.loanNo,applyInfo.baseInfo.productCd, applyInfo.baseInfo.version)" >保存
    </a>
</div>
<div class="easyui-panel" title="客户信息">
    <div class="h20"></div>
    <!-- firstCustomerInfo_tabs  用于css-->
    <div id="firstCustomerInfo_tabs">
    <div id="moneyCustomerInfo_tabs" class="easyui-tabs">
        <div title="基本信息" class="padding_20">
            <jsp:include page="moneyCustomerBaseInfo.jsp"></jsp:include>
        </div>
        <div title="联系人信息" class="padding_20">
            <jsp:include page="moneyCustomerContactInfo.jsp"></jsp:include>
        </div>
        <div title="资产信息" class="padding_20">
            <jsp:include page="moneyCustomerAssetsInfo.jsp"></jsp:include>
        </div>
    </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${ctx}/resources/lib/math.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/approve/zdmoney/moneyCustomerInfo.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'moneyCustomerInfo',
        loanNo: '${loanNo}',
        applyInfo: ${applyInfoJson == null ? '{}' : applyInfoJson},
        updateVersion: ${updateVersion == null ? 'false' : 'true'},
        isDirectApp: ${isDirectApp},
    });
</script>
</html>
<%--assetsList: ${assetsListJson == null ? '[]' : assetsListJson},--%>
