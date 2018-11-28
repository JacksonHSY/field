<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div class="easyui-panel">
	<form id="integratedQuyer_form" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
			    <th>借款编号:</th>
				<td><input name="loanNo" type="text" class="easyui-textbox input" data-options="prompt:'借款编号'" style="width:190px;"></td>
				<th>申请人姓名:</th>
				<td><input name="name" class="easyui-textbox input" data-options="prompt:'申请人姓名'" style="width:190px;"></td>
				<th>身份证号码:</th>
				<td><input name="idNo" type="text" class="easyui-textbox input" data-options="prompt:'身份证号码',validType:'IDCard'" style="width:190px;"></td>
				<th>手机:</th>
				<td><input name="phone" type="text" class="easyui-textbox input" data-options="prompt:'手机',validType:['length[11,11]','mobile']" style="width:190px;"></td>
			</tr>
			<tr>
				<th>固定电话:</th>
				<td><input name="tel" type="text" class="easyui-textbox input" data-options="prompt:'固定电话',validType:'telNum'" style="width:190px;"></td>
				<th>单位名称:</th>
				<td><input name="corpName" type="text" class="easyui-textbox input" data-options="prompt:'单位名称'" style="width:190px;"></td>
				<shiro:hasPermission name="/applyType">
					<th>申请类型:</th>
					<td><select name="applyType" style="width:190px;" class="easyui-combobox select" data-options="onUnselect:onUnselect,onSelect:onSelect,panelHeight:'auto',value:'',multiple:true,editable:false,prompt:'申请类型'">
							<option value="NEW">NEW</option>
							<option value="TOPUP">TOPUP</option>
							<option value="RELOAN">RELOAN</option>
					</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
				</shiro:hasPermission>
				<shiro:hasPermission name="/caseIdentify">
					<th>案件标识:</th>
					<td><select name="caseIdentifyList" class="easyui-combobox select" data-options="onUnselect:onUnselect,onSelect:onSelect,prompt:'案件标识',panelHeight:'auto',value:'',multiple:true,editable:false" style="width:190px;">
						<option value="1">加急件</option>
						<option value="0">APP进件</option>
						<option value="2">触发欺诈规则</option>
						<option value="3">费率优惠客户</option>
						<option value="4">简化资料客户</option>
						<option value="5">复议再申请客户</option>
						<option value="6">前前进件</option>
					</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<shiro:hasPermission name="/submitTime">
					<th>提交时间起:</th>
					<td><input name="startTime" type="text" style="width:190px;" class="easyui-datebox input" data-options="editable:false,prompt:'提交时间起',validType:['date','compareToday']" id="integratedQuerySubmitDate"></td>
				</shiro:hasPermission>
				<shiro:hasPermission name="/submitTime">
					<th>提交时间止:</th>
					<td><input name="endTime" type="text" style="width:190px;" class="easyui-datebox input" data-options="editable:false,prompt:'提交时间止',validType:['date','compareToday','compareDate[\'#integratedQuerySubmitDate\']']" id="integratedQuerySubmitEndDate"></td>
				</shiro:hasPermission>
				<th>地址类型:</th>
				<td colspan="3"><select id="address_type" name="addressList" class="easyui-combobox select" data-options="onChange:initAddressCom,onUnselect:onUnselect,onSelect:onSelect,prompt:'地址',panelHeight:'auto',value:'',multiple:true,editable:false" style="width:190px;">
						<option value="1">住宅地址</option>
						<option value="2">单位地址</option>
						<option value="3">房产地址</option>
					</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div id="address_detial_div"><input id="address_detail" name="address" type="text" class="easyui-textbox input" data-options="prompt:'地址'" style="width:270px;"></div>
				</td>
			</tr>
			<tr>
				<td colspan="8" style="padding-left: 115px;"><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-undo" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp; <a onclick="integratedQueryPass()" class="easyui-linkbutton"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<!-- <table id="integratedQuery_userList_datagrid"></table> -->
<table class="easyui-datagrid" id="integratedQuery_userList_datagrid" data-options="url:'${ctx}/search/index',fitColumns:true,singleSelect:true,pagination:true,onLoadSuccess:searchLoanSuccess,striped : true,singleSelect : true,rownumbers : true,idField : 'id',fitColumns : false,scrollbarSize : 0,remoteSort : true,onDblClickRow: searchDoubleClick,queryParams:{flag:false}">
	<thead>
		<tr>
			<th data-options="field:'caseIdentify',width:120,align:'center',formatter:identifyFormatter">案件标识</th>
			<th data-options="field:'loanNo',width:160,align:'center',sortable : true">借款编号</th>
			<!-- <th data-options="field:'checkPersonCode',width:60,align:'center',sortable : true">初审员</th>
			<th data-options="field:'contractNo',width:100,align:'center',sortable : true">合同编号</th> -->
			<th data-options="field:'name',width:100,align:'center',sortable : true,formatter:formatterName">申请人姓名</th>
			<th data-options="field:'idNo',width:100,align:'center',sortable : true,formatter:formatterIdNo">身份证号码</th>
			<th data-options="field:'applyType',width:100,align:'center',sortable : true">申请类型</th>
			<th data-options="field:'initProductName',width:90,align:'center',sortable : true,formatter:formatterProduct">申请产品</th>
			<th data-options="field:'productName',width:90,align:'center',sortable : true,formatter:productFormatter">审批产品</th>
			<th data-options="field:'corpName',width:160,align:'center',sortable : true,formatter : numberFomatter">单位名称</th>
			<th data-options="field:'status',width:100,align:'center',sortable : true">借款状态</th>
			<th data-options="field:'conclusion',width:100,align:'center',sortable : true,formatter : numberFomatter">审批结论</th>
			<th data-options="field:'applyDate',width:130,align:'center',sortable : true,formatter : formatDateYMD">提交时间</th>
			<th data-options="field:'branch',width:160,align:'center',sortable : true,formatter : numberFomatter">营业部</th>
			<th data-options="field:'branchAttr',width:100,align:'center',sortable : true">营业部属性</th>
			<th data-options="field:'action',width:170,align:'center',formatter:actionFormatter">操作</th>
		</tr>
    </thead>

</table>
<script type="text/javascript" src="${ctx}/resources/js/apply/integratedQuery.js"></script>
<script type="text/javascript">

/**
 * 双击行
 */
 function searchDoubleClick(index, data){
	 <shiro:hasPermission name="/integratedQuery/detail">
	 	handleInfo(data.loanNo);
	 </shiro:hasPermission>
}

/**
 * 审批产品格式化
 */
 function productFormatter(value, data, index){
    if (isNotNull(data.status) && data.status.indexOf('申请') >= 0) {
        return '';
    } else {
        if ("证大前前" != value) {
            return value;
		} else {
            return "";
		}
    }
}

/**
 * 按键标识列格式化
 */
function identifyFormatter(value, data, index){
	var iden = [];
	if(value){
        iden = value.split("|");
	}
	var html ="";
    if (iden[2] == "true") {
        html = html+ "<img title='触发欺诈规则' src='" + ctx.rootPath() + "/resources/images/loanMark/cheat.png'>&nbsp;";
    }
    if (iden[1] == "true") {
        html = html+ "<img title='加急件' src='" + ctx.rootPath() + "/resources/images/loanMark/urgent.png'>&nbsp;";
    }
	if (iden[0] == "true") {
        html = html+  "<img title='APP进件' src='" + ctx.rootPath() + "/resources/images/loanMark/app.png'>&nbsp;";
	}
    if (isNotNull(data.ifPreferentialUser) && "Y" == data.ifPreferentialUser) {
        html = html +  "<img title='费率优惠客户' src='" + ctx.rootPath() + "/resources/images/loanMark/rate.png'>&nbsp;";
    }
    if(isNotNull(data.simplifiedDataUser) && "Y" == data.simplifiedDataUser){
    	html = html +  "<img title='简化资料客户' src='" + ctx.rootPath() + "/resources/images/loanMark/simple.png'>&nbsp;";
    }
    // 复议在申请
    if (isNotNull(data.ifReconsiderUser) && "Y" == data.ifReconsiderUser) {
        html = html +  "<img title='复议再申请' src='" + ctx.rootPath() + "/resources/images/loanMark/reconsider.png'>&nbsp;";
    }
    //前前进件
    if (isNotNull(data.zdqqApply) && 1 == data.zdqqApply) {
        html = html +  "<img title='证大前前' src='" + ctx.rootPath() + "/resources/images/loanMark/money.png'>&nbsp;";
    }
	return html;
}

/**
 * 列表加载成功
 */
function searchLoanSuccess(data){
	console.log(data);
	if(data.total == -1){
		$.info("提示", "查询超时!");
	}
 	$("#integratedQuery_userList_datagrid").datagrid("fixRownumber");
 	$(".pagination-num").width("5em"); 
}

/**
 * 格式化操作列
 */
function actionFormatter(value, data, index) {
	var action = '';
	<shiro:hasPermission name="/integratedQuery/logOfList">
		action = action + '<a href="javaScript:void(0);" onclick=logInfoDialog("' + data.loanNo + '") >日志</a>';
	</shiro:hasPermission>
	<shiro:hasPermission name="/integratedQuery/detail">
		var s1 = '';
		if(action != ''){
			s1 = '&nbsp|&nbsp';
		}
		action = action + s1 + '<a href="javaScript:void(0);" onclick=handleInfo("' + data.loanNo + '") >查看详情</a>';
	</shiro:hasPermission>
	<shiro:hasPermission name="/integratedQuery/repayment">
		if (compare(data.status)) {
			var s2 = '';
			if(action != ''){
				s2 = '&nbsp|&nbsp';
			}
			action = action + s2 + '<a href="javaScript:void(0);" onclick=integratedQueryViewDetailsDialog("' + data.loanNo + '")>还款明细</a>';
		}
	</shiro:hasPermission>
	return action;
}
/**
 * 格式申请人
 */
function formatterName(value,row,index) {
    if (getOldCardIdExists(row.idNo) && "申请" == row.status) {
        value = "*" + value;
    }
    return value;
}
/**
 * 格式化身份证
 */
function formatterIdNo(value,row,index) {
    return  "*" + value.substring(value.length - 4, value.length);
}

</script>