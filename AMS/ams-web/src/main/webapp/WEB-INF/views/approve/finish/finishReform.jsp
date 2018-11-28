<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<form class="margin_20" id="finishReform_Query_Form">
	<table class="table_ui W100 center_m">
		<tr>
			<th>借款编号:</th>
			<td><input type="text" class="easyui-textbox input" style="width: 190px;" name="loanNo" data-options="prompt:'借款编号'"></td>
			<th>申请人姓名:</th>
			<td><input type="text" class="easyui-textbox input" style="width: 190px;" name="customerName" data-options="prompt:'申请人姓名'"></td>
			<th>身份证号码:</th>
			<td><input type="text" class="easyui-textbox input" style="width: 190px;" name="customerIDNO" data-options="prompt:'身份证号码',validType:'IDCard'"></td>
			<th>申请件层级:</th>
			<td><select class="easyui-combobox select" style="width: 190px;" name="loanNoTopClass" data-options="onUnselect:onUnselect,onSelect:onSelect,value:'',prompt:'申请件层级',editable:false,multiple:true">
					<option value="L1">L1</option>
					<option value="L2">L2</option>
					<option value="L3">L3</option>
					<option value="L4">L4</option>
			</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
		</tr>
		<tr>
			<th>借款产品:</th>
			<td><input type="text" class="easyui-combobox input" style="width: 190px;" name="productCd" data-options="onUnselect:onUnselect,onSelect:onSelect,prompt:'借款产品',editable:false,valueField:'code',textField:'name',url:'${ctx}/bmsBasiceInfo/getProductList',multiple:true"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>营业部:</th>
			<td><input type="text" class="easyui-combobox input" style="width: 190px;" name="owningBranchId" data-options="onChange:onValueChange,onUnselect:onUnselect,onSelect:onSelect,prompt:'营业部',valueField:'id',textField:'name',url:'${ctx}/pmsApi/findAllDepts',multiple:true"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>处理人:</th>
			<td><input type="text" class="easyui-combobox input" style="width: 190px;" name="handleCode" data-options="onChange:onValueChange,onUnselect:onUnselect,onSelect:onSelect,prompt:'处理人',valueField:'staffCode',textField:'name',url:'${ctx}/staffOrderTask/getFinalAcceptOrder',multiple:true"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			<th>案件标识:</th>
			<td><select name="caseType" class="easyui-combobox select" style="width: 190px;" name="caseType" data-options="onUnselect:onUnselect,onSelect:onSelect,value:'',prompt:'案件标识',panelHeight:'auto',editable:false,multiple:true">
					<option value="1">加急件</option>
					<option value="2">APP进件</option>
					<option value="3">触发欺诈规则</option>
				    <option value="5">费率优惠客户</option>
				    <option value="6">简化资料客户</option>
					<option value="7">复议再申请客户</option>
				    <option value="8">前前进件</option>
			</select><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
		</tr>
		<tr>
			<th>提交时间:</th>
			<td><input type="text" class="easyui-datebox input" style="width: 190px;" name="xsStartDate" data-options="editable:false,prompt:'提交时间',validType:['date','compareToday']" id="finishReformSubmitDate"></td>
			<th>至:</th>
			<td><input type="text" class="easyui-datebox input" style="width: 190px;" name="xsEndDate" data-options="editable:false,prompt:'结束时间',validType:['date','compareToday','bothEmpty[\'#finishReformSubmitDate\']']"></td>
			<th>分派状态:</th>
			<td><select id="finishReform_fpStatus" name="fpStatus" class="select" >
					<shiro:hasPermission name="/finishApprove/finishReform/notAssigned"><option value="0" selected>未分派</option></shiro:hasPermission>
					<shiro:hasPermission name="/finishApprove/finishReform/assigned"><option value="1" selected>已分派</option></shiro:hasPermission>
			</select></td>
			<th></th>
			<td><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;<a class="easyui-linkbutton" onclick="finishReformQuery()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a></td>
		</tr>
	</table>
</form>
<div class="h20"></div>
<div id="finishReform_datagrid_tool">
	<!-- 未分派 -->
	<span id="finishReform_notAssigned_span" class="display_noH">
		<shiro:hasPermission name="/finishApprove/finishReform/notAssigned/reform"><a class="easyui-linkbutton" data-options="plain:true" onclick="finishReformReassignmentDialog()"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/finishApprove/finishReform/notAssigned/batchReturn"><a class="easyui-linkbutton" onclick="batchReturn()" data-options="plain:true"><i class="fa fa-backward" aria-hidden="true"></i>批量回退</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/finishApprove/finishReform/notAssigned/batchReject"><a class="easyui-linkbutton" onclick="batchRefuse()" data-options="plain:true"><i class="fa fa-times" aria-hidden="true"></i>批量拒绝</a>&nbsp;&nbsp;</shiro:hasPermission>
	</span>
	<!-- 已分派 -->
	<span id="finishReform_assigned_span" class="display_noH">
		<shiro:hasPermission name="/finishApprove/finishReform/assigned/reform"><a class="easyui-linkbutton" data-options="plain:true" onclick="finishReformReassignmentDialog()"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/finishApprove/finishReform/assigned/batchReturn"><a class="easyui-linkbutton" onclick="batchReturn()" data-options="plain:true"><i class="fa fa-backward" aria-hidden="true"></i>批量回退</a>&nbsp;&nbsp;</shiro:hasPermission>
		<shiro:hasPermission name="/finishApprove/finishReform/assigned/batchReject"><a class="easyui-linkbutton" onclick="batchRefuse()" data-options="plain:true"><i class="fa fa-times" aria-hidden="true"></i>批量拒绝</a>&nbsp;&nbsp;</shiro:hasPermission>
	</span>
	<a onclick="exportExcel()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;出</a>
	


	<%-- <a onclick="finishReformReassignmentDialog()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;
	<a onclick="exportExcel()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;出</a>&nbsp;&nbsp;
	<shiro:hasPermission name="/batchReturn">
		<a onclick="batchReturn()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-backward" aria-hidden="true"></i>批量回退</a>&nbsp;&nbsp;
	</shiro:hasPermission>
	<shiro:hasPermission name="/batchRefuse">
		<a onclick="batchRefuse()" class="easyui-linkbutton" data-options="plain:true"><i class="fa fa-times" aria-hidden="true"></i>批量拒绝</a>
	</shiro:hasPermission> --%>
</div>
<table id="finishReform_datagrid"></table>
<div id="finishReform_reassignment_dialog" class="padding_20 display_none">
	<table class="table_ui W100">
		<tr>
			<th>处理人:</th>
			<td><input type="text" class="input" id="approvePerson"></td>
		</tr>
	</table>
</div>

<!-- 退回弹出框 -->
<div id="finishReform_back_dialog" class="padding_20 display_none">
	<input id="finish_remak_teturnType" type="hidden"  placeholder="标记是否是前前进件">
	<form>
		<table class="table_ui WH100">
			<tr>
				<th>退回类型:</th>
				<td colspan="4">
					<input id="finishReform_md_back_type" name="backType" type="radio" checked="checked" class="easyui-validatebox" value="ZSRTNLR"/><label for="finishReform_md_back_type">退回门店</label>
					<input id="finishReform_cs_back_type" name="backType" type="radio" class="easyui-validatebox" value="ZSRTNCS" /><label for="finishReform_cs_back_type">退回初审</label>
				</td>
			</tr>
			<tr class="markReturnReason">
				<th>一级原因:</th>
				<td><input class="input" id="finishReform_back_ParentCode" name="firstReason"/><input type="hidden" name="firstReasonText"></td>
				<th>二级原因:</th>
				<td><input class="input" id="finishReform_back_ReasonCode" name="secondReason"/><input type="hidden" name="secondReasonText"></td>
				<td class="markIsAdd">&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<th>备注信息:</th>
				<td colspan="4"><input class="easyui-textbox W30" id="finishReform_back_remark" name="remark" data-options="height:80,width:642,validType:'length[1,200]',multiline:true"></td>
			</tr>
		</table>
	</form>
</div>

<!-- 拒绝弹出框 -->
<div id="finishReform_refuse_dialog" class="padding_20 display_none">
	<form id="finishReform_refuse_form" class="margin_20">
	<table class="table_ui WH100">
		<tr>
			<th>一级原因:</th>
			<td><select class="easyui-combobox select" id="finishReform_refuse_firstReason" name="firstReason" data-options="editable:false,isClearBtn:true,required:true"></select><input type="hidden" name="firstReasonText"><input type="hidden" value="none"  id="finishReform_refuse_conType" name="conType"></td>
			<th>二级原因:</th>
			<td><select class="easyui-combobox select" id="finishReform_refuse_secondReason" name="secondReason" data-options="editable:false,isClearBtn:true"></select><input type="hidden" name="secondReasonText"></td>
		</tr>
		<tr>
			<th>备注信息:</th>
			<td colspan="5"><input class="easyui-textbox W30" id="finishReform_refuse_remark" name="remark" data-options="height:80,width:804,validType:'length[1,200]',multiline:true"></td>
		</tr>	
	</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/approve/finish/finishReform.js"></script>
