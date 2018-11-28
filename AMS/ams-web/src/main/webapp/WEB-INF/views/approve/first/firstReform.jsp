<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<form class="margin_20" id="firstReform_query_form">
	<table class="table_ui W100 center_m">
		<tr>
			<th>借款编号:</th>
			<td><input type="text" name="loanNo" style="width: 190px;" class="easyui-textbox input" data-options="prompt:'借款编号'"></td>
			<th>申请人姓名:</th>
			<td><input type="text" name="customerName" style="width: 190px;" class="easyui-textbox input" data-options="prompt:'申请人姓名'"></td>
			<th>身份证号码:</th>
			<td><input type="text" name="customerIDNO" style="width: 190px;" class="easyui-textbox input" data-options="prompt:'身份证号码',validType:'IDCard'"></td>
			<th>客户经理:</th>
			<td><input type="text" name="branchManagerName" style="width: 190px;" class="easyui-textbox input" data-options="prompt:'客户经理'"></td>
		</tr>
		<tr>
			<th>申请产品:</th>
			<td><input class="easyui-combobox input" style="width: 190px;" name="productCd" data-options="onUnselect:onUnselect,onSelect:onSelect,multiple:true,prompt:'申请产品',valueField:'code',textField:'name',editable:false,value:'',url:'${ctx}/bmsBasiceInfo/getProductList'"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>营业部:</th>
			<td><input class="easyui-combobox input" style="width: 190px;" name="owningBranchId" data-options="onChange:onValueChange,onUnselect:onUnselect,onSelect:onSelect,multiple:true,textField:'name',valueField:'id',prompt:'营业部',url:'${ctx}/pmsApi/findAllDepts'"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>处理人:</th>
			<td><input class="easyui-combobox input" name="handleCode" style="width: 190px;" data-options="onChange:onValueChange,onUnselect:onUnselect,onSelect:onSelect,multiple:true,prompt:'处理人',textField:'name',valueField:'usercode',editable:true,url:'${ctx}/pmsApi/findEmpByUsercode'"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>案件标识:</th>
			<td><select name="caseType" style="width: 190px;" class="easyui-combobox select" data-options="onUnselect:onUnselect,onSelect:onSelect,multiple:true,prompt:'案件标识',panelHeight:'auto',editable:false,value:''">
					<option value="1">加急件</option>
					<option value="2">APP进件</option>
					<option value="3">触发欺诈规则</option>
					<option value="4">退回件</option>
					<option value="5">费率优惠客户</option>
				    <option value="6">简化资料客户</option>
					<option value="7">复议再申请客户</option>
					<option value="8">前前进件</option>
			</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
		</tr>
		<tr>
			<th>提交时间:</th>
			<td><input type="text" name="xsStartDate" style="width: 190px;" class="easyui-datebox input" data-options="prompt:'提交时间',validType:['date','compareToday']" id="firstReformSubmitDate"></td>
			<th>至:</th>
			<td><input type="text" name="xsEndDate" style="width: 190px;" class="easyui-datebox input" data-options="prompt:'结束时间',validType:['date','compareToday','bothEmpty[\'#firstReformSubmitDate\']']"></td>
			<th>分派状态:</th>
			<td><select id="firstReform_fpStatus" class="select" name="fpStatus">
				<shiro:hasPermission name="/firstApprove/firstReform/notAssigned"><option value="2" selected>未分派</option></shiro:hasPermission>
				<shiro:hasPermission name="/firstApprove/firstReform/assigned"><option value="1" selected>已分派</option></shiro:hasPermission>
			</select></td>
			<td colspan="2"><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a class="easyui-linkbutton" onclick="firstReformQuery()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
		</tr>
	</table>
</form>
<div class="h20">
</div>
<div id="firstReform_task_datagrid_tool">
	<!-- 未分派 -->
	<span id="firstReform_notAssigned_span" class="display_noH">
		<shiro:hasPermission name="/firstApprove/firstReform/notAssigned/reform"><a class="easyui-linkbutton" data-options="plain:true" onclick="firstReformReassignmentDialog()"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/firstApprove/firstReform/notAssigned/batchReturn"><a class="easyui-linkbutton" onclick="firstReformBackDialog()" data-options="plain:true"><i class="fa fa-backward" aria-hidden="true"></i>批量回退</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/firstApprove/firstReform/notAssigned/batchReject"><a class="easyui-linkbutton" onclick="firstReformRefuseDialog()" data-options="plain:true"><i class="fa fa-times" aria-hidden="true"></i>批量拒绝</a>&nbsp;&nbsp;</shiro:hasPermission>
	</span>
	<!-- 已分派 -->
	<span id="firstReform_assigned_span" class="display_noH">
		<shiro:hasPermission name="/firstApprove/firstReform/assigned/reform"><a class="easyui-linkbutton" data-options="plain:true" onclick="firstReformReassignmentDialog()"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/firstApprove/firstReform/assigned/batchReturn"><a class="easyui-linkbutton" onclick="firstReformBackDialog()" data-options="plain:true"><i class="fa fa-backward" aria-hidden="true"></i>批量回退</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/firstApprove/firstReform/assigned/batchReject"><a class="easyui-linkbutton" onclick="firstReformRefuseDialog()" data-options="plain:true"><i class="fa fa-times" aria-hidden="true"></i>批量拒绝</a>&nbsp;&nbsp;</shiro:hasPermission>
	</span>
	<a onclick="exportExcel()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;出</a>
</div>
<table id="firstReform_task_datagrid"></table>
<!-- 改派dialog -->
<div id="firstReform_reassignment_dialog" class="padding_20 display_none">
	<form>
		<table class="table_ui W100">
			<tr>
				<th>大组:</th>
				<td><input id="firstReform_bigGroup" class="input"></td>
			</tr>
			<tr>
				<th>小组:</th>
				<td><input id="firstReform_smallGroup" class="input"></td>
			</tr>
			<tr>
				<th>处理人:</th>
				<td><input id="firstReform_operator" class="input"></td>
			</tr>
		</table>
	</form>
</div>
<!-- 批量拒绝弹框 -->
<div id="firstReform_refuse_dialog" class="display_none padding_20">
	<form>
		<input type="hidden" name="remarkBlack" id="firstReform_remarkBlack">
		<table class="table_ui WH100">
			<tr>
				<th>一级原因:</th>
				<td><input id="firstReform_firstRefuse_combobox" name="firstReason" class="input"><input type="hidden" name="firstReasonText"></td>
				<th>二级原因:</th>
				<td><input id="firstReform_secondRefuse_combobox" name="secondReason" class="input"><input type="hidden" name="secondReasonText"></td>
			</tr>
			<tr>
				<th>备注信息:</th>
				<td colspan="3"><input class="easyui-textbox W30" id="firstReform_refuse_remark" name="remark" data-options="height:80,width:790,validType:'length[1,200]',multiline:true"></td>
			</tr>
		</table>
	</form>
</div>
<!-- 批量退回退回弹框 -->
<div id="firstReform_return_dialog" class="display_none padding_20">
	<form>
		<table class="table_ui WH100">
			<tr class="markReturnReason">
				<th>一级原因:</th>
				<td><input id="firstReform_firstReturn_combobox" name="firstReason" class="input"><input type="hidden" name="firstReasonText"></td>
				<th>二级原因:</th>
				<td><input id="firstReform_secondReturn_combobox" name="secondReason" class="input"><input type="hidden" name="secondReasonText"></td>
				<td class="markIsAdd">&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<th>备注信息:</th>
				<td colspan="4"><input class="easyui-textbox W30"  name="remark" data-options="height:80,width:642,validType:'length[1,200]',multiline:true"></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/approve/first/firstReform.js"></script>
