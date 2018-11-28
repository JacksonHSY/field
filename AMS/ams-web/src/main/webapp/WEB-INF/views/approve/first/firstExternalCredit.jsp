<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>${applyBasiceInfo.name}-外部征信</title>
	<jsp:include page="../../common/commonJS.jsp"></jsp:include>
	<jsp:include page="../../common/theme.jsp"></jsp:include>
</head>
<body class="padding_20 ms-controller" ms-controller="page">
<div class="easyui-panel" title="外部征信" style="width: 100%"></div>
<input id="firstExternalCredit_loanNo_hidden" type="hidden" value="${applyBasiceInfo.loanNo}">
<input type="hidden" id="education" value="${applyBasiceInfo.productCd}" />
<input type="hidden" id="firstExternalCredit_name" value="${applyBasiceInfo.name}">
<input type="hidden" id="firstExternalCredit_idCard" value="${applyBasiceInfo.idNo}">
<div class="h20"></div>
<div class="easyui-tabs" style="width: 100%">
	<div title="上海资信" class="padding_20" >
		<jsp:include page="shangHaiCredit.jsp"></jsp:include>
	</div>
	<div title="第三方征信" class="padding_20">
		<div class="easyui-panel" title="手机实名认证及入网时长" style="width: 100%">
			<table id="first_mobileOnlineInfo_table" class="table_list W100">
				<thead ms-if="mobileOnlineList.length > 0">
				<tr>
					<td>姓名</td>
					<td>证件号码</td>
					<td>手机号</td>
					<td>手机在网时长</td>
					<td>认证结果</td>
				</tr>
				</thead>
				<tbody>
				<tr ms-for="($index, item) in mobileOnlineList">
					<td ms-if="$index == 0" ms-attr="{rowspan: mobileOnlineList.length}">{{ item.name }}</td>
					<td ms-if="$index == 0" ms-attr="{rowspan: mobileOnlineList.length}">{{ item.idNo }}</td>
					<td>{{ item.mobile }}</td>
					<td>{{ item.mobileOnline || '查无结果' }}</td>
					<td>{{ item.realCerti || '查无结果'}}</td>
				</tr>
				</tbody>
				<tr ms-if="mobileOnlineList.length == 0">
					<td colspan="5">
						查无结果
					</td>
				</tr>
			</table>
		</div>
		<!--
        <div class="easyui-panel" title="个人学历">
            <table id="first_educationInfo_table" class="table_ui W100"></table>
        </div>
        -->
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstExternalCredit.js"></script>
<script type="text/javascript">
    // 定义页面VM
    pageInit({
        $id: 'page',
        loanNo: '${applyBasiceInfo.loanNo != null ? applyBasiceInfo.loanNo : ''}',
        education: '${applyBasiceInfo.productCd != null ? applyBasiceInfo.productCd : ''}',
        customerName: '${applyBasiceInfo.name != null ? applyBasiceInfo.name : ''}',
        customerIdCard: '${applyBasiceInfo.idNo != null ? applyBasiceInfo.idNo : ''}',
        mobileOnlineList: ${mobileOnlineList != null ? mobileOnlineList : '[]'}
    });
</script>
</body>
</html>