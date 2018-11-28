<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%--引入标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="easyui-panel W100" data-options="collapsible:true">
	<form id="qualityQuery_queryForm" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>客户姓名:</th>
				<td><input type="text" name="customerName" class="easyui-textbox input" data-options="prompt:'客户姓名',width:180"></td>
				<th>身份证号:</th>
				<td><input type="text" name="idNo" class="easyui-textbox input" data-options="prompt:'身份证号',validType:'IDCard',width:180"></td>
				<th>进件营业部:</th>
				<td><input type="text" name="owningBranchId" class="easyui-combobox input" data-options="textField:'name',valueField:'id',prompt:'进件营业部',url:'${ctx}/pmsApi/findAllDepts',multiple:true,width:180"></td>
				<th>申请件编号:</th>
				<td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'申请件编号',width:180"></td>
			</tr>
			<tr>
				<th>审批结果:</th>
				<td><select type="text" name="approvalStatus" class="easyui-combobox input" data-options="value:'',width:180" >
							<option value="2">其他</option>
							<option value="0">通过</option>
							<option value="1">拒绝</option>
					</select>
				</td>
				<th>完成起始日期:</th>
				<td>
                    <input type="text" id="endDateStart" name="endDateStart" class="easyui-datebox input"
				           data-options="width:160, editable:false, prompt:'完成日期日期', validType:['date','compareToday'], width:180">
                </td>
				<th>完成截止日期:</th>
				<td>
                    <input type="text" id="endDateEnd" name="endDateEnd" class="easyui-datebox input"
						   data-options="prompt:'完成截止日期', editable:false, validType:['date','compareDate[\'#endDateStart\']','compareToday'], width:180">
                </td>
					<th>申请来源:</th>
				<td><input type="text" name="qualitySource" class="easyui-combobox input" data-options="panelHeight:'auto',textField:'qualitySource',valueField:'qualitySource',prompt:'申请来源',url:'${ctx}/qualitySource/getAllSource',width:180"></td>
			</tr>
			<tr>
			<th>差错类型:</th>
				<td><select type="text" name="checkResult" class="easyui-combobox input" data-options="panelHeight:'auto',multiple:true,value:'',width:180">
							<option value="E_000000">无差错</option>
							<option value="E_000001">预警</option>
							<option value="E_000002">建议</option>
							<option value="E_000003">一般差错</option>
							<option value="E_000004">重大差错</option>
					</select>
				</td>
			<th>客户类型:</th>
				<td><select type="text" name="customerType" class="easyui-combobox input" data-options="panelHeight:'auto',value:'',width:180">
							<option value="NEW">NEW</option>
							<option value="RELOAN">RELOAN</option>
							<option value="TOPUP">TOPUP</option>
				</select>
				</td>
			<th>质检人员:</th>
				<td><input type="text" name="checkUser" class="easyui-combobox input" data-options="textField:'name',valueField:'usercode',prompt:'质检人员',url:'${ctx}/qualityPersonnelManagement/getBranchPerson',width:180"></td>

			<th></th>
			<td>
					<a class="easyui-linkbutton" id="qualityQuery_reset"><i class="fa fa-times"></i>重&nbsp;置</a>
					<a class="easyui-linkbutton" id="qualityQuery_Query"><i class="fa fa-search"></i>搜&nbsp;索</a>
			</td>
			</tr>
		</table>
	</form>
</div>
<div class="h20"></div>
<div>
	<div id="qualityQuery_toolBarBtn">
		<a class="easyui-linkbutton" data-options="plain:true" onclick="exportToDoList();"><i class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>
	</div>
	<table id="qualityQuery_datagrid"></table>
</div>
<script type="text/javascript">
$(function($) {
	qualityQueryDatagrid("#qualityQuery_datagrid");
});

//综合查询数据导出
function exportToDoList() {
	var queryParams = $("#qualityQuery_queryForm").serializeObject();
 	var ownid = queryParams.owningBranchId;
	 if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
		 queryParams.owningBranceId =ownid;
	 }else{
		 var ownids = ownid.join(",");
		 queryParams.owningBranceId = ownids;
	 }
	 //多个差错类型
	 var result = queryParams.checkResult;
	 if(typeof(result)=="string" || typeof(result)=="undefined"){
		 queryParams.checkResult =result;
	 }else{
		 var result = result.join(",");
		 queryParams.checkResult = result;
	 }
	 var source = queryParams.qualitySource;
	 queryParams.source=source; 
	 window.location.href = ctx.rootPath()+"/qualityControlDesk/exportQueryList?" + $.param(queryParams);
}

//查询
$("#qualityQuery_Query").click(function qualityQuery_Query(){
	if ($(this).parents("form").form("validate")) {
		//textbox去空格
		$("#qualityQuery_queryForm").find('.easyui-textbox').each(function (index, element ) {
			var $self = $(element);
			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
		});
		var queryParams = $("#qualityQuery_queryForm").serializeObject();
		var ownid = queryParams.owningBranchId;
		if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
			 queryParams.owningBranceId =ownid;
		}else{
			 var ownids = ownid.join(",");
			 queryParams.owningBranceId = ownids;
		}
		 //多个差错类型
		var result = queryParams.checkResult;
		if(typeof(result)=="string" || typeof(result)=="undefined"){
			 queryParams.checkResult =result;
		}else{
			 var result = result.join(",");
			 queryParams.checkResult = result;
		}
		var source = queryParams.qualitySource;
		queryParams.source=source;
		$('#qualityQuery_datagrid').datagrid('options').queryParams=queryParams;
		$("#qualityQuery_datagrid").datagrid('reload');
	}
});

//重置
$("#qualityQuery_reset").click(function qualityQuery_reset(){
	$('#qualityQuery_queryForm').form('clear');
	$(this).parents("form").form('disableValidation');//移除表单里的required 属性
});

function qualityQueryDatagrid(id) {
	$(id).datagrid({
		url : ctx.rootPath()+"/qualityQuery/pageList",
		striped : true,
		singleSelect : false,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		remoteSort:false,
		toolbar:'#qualityQuery_toolBarBtn',
		columns : [ [{
			field : 'endDate',
			title : '完成日期',
			sortable:true,
			width : 80,
			formatter : function(value, data, index) {
				if(value != null){
					return moment(value).format('YYYY-MM-DD');
				}
			}
		}, {
			field : 'customerName',
			title : '客户姓名',
			sortable:true,
			width : 80,
		}, {
			field : 'idNo',
			title : '身份证号',
			sortable:true,
			width : 80,
			 formatter : function(value, data, index) {
	                if (value != null) {
	                    return '*'+value.slice(-4);
	                }
			 }
		},{
			field : 'customerType',
			title : '客户类型',
			sortable:true,
			width : 80,
			
		}, {
			field : 'owningBrance',
			title : '进件营业部',
			sortable:true,
			width : 80,
		}, {
			field : 'source',
			title : '申请来源',
			sortable:true,
			width : 80,
		}, {
			field : 'loanNo',
			title : '申请件编号',
			sortable:true,
			width : 80,
		}, {
			field : 'productTypeName',
			title : '贷款类型',
			sortable:true,
			width : 80,
		}, {
			field : 'approvalStatus',
			title : '审批结果',
			sortable:true,
			width : 60,
			formatter : function(value, data, index) {
				if(value == "1"){
					return "拒绝";
				}else if(value=="0"){
					return "通过";
				}else{
					return "其他";
				}
			}
		},{
			field : 'refuseReasonEmbed',
			title : '拒绝原因',
			sortable:true,
			width : 80,
			formatter : function(value, data, index) {
				if(value!=null){
					if(value!=null){
						return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
					}
				}
			}
		}, 
		<shiro:hasAnyRoles name="qualityCheckGroupLeader,qualityCheckDirector,qualityCheckManager">
		{
			field : 'checkPerson',
			title : '初审姓名',
			sortable:true,
			width : 80,
		}, 
		{
			field : 'finalPerson',
			title : '终审姓名',
			sortable:true,
			width : 80,
		},
		</shiro:hasAnyRoles>
		{
			field : 'approveDate',
			title : '审批日期',
			sortable:true,
			width : 80,
			formatter : function(value, data, index) {
				if(value != null){
					return moment(value).format('YYYY-MM-DD');
				}
			}
		},{
			field : 'checkResult',
			title : '差错类型',
			sortable:true,
			width : 80,
			formatter : function(value) {
				if(value=="E_000000"){
					return "无差错";
				}
				if(value=="E_000001"){
					return "预警";
				}
				if(value=="E_000002"){
					return "建议";
				}
				if(value=="E_000003"){
					return "一般差错"
				}
				if(value=="E_000004"){
					return "重大差错";
				}
			}
			
		},{
			field : 'checkUser',
			title : '质检人员',
			sortable:true,
			width : 80,
		}, {
			field : 'qualityCheckId',
			title : '操作',
			width : 80,
			align : 'center',
			formatter : function(value, data, index) {
				var title = data.customerName+"详情";
				var flag = 'done';
				var action = '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag+'\');">详情</a>';
				return action;
			}
		} ] ]
	});
}

/**
 * 弹出质检工作台页面
 * */
function qualityReceiveDialog(title,loanNo,value,flag){
    jDialog.open({url: ctx.rootPath() + "/qualityControlDesk/qualityReceive?flag="+flag+"&loanNo="+loanNo+"&qualityCheckId="+value});
}

/**
 * 日期选择事件
 */
$('#endDateStart').datebox({
	onSelect: function(date){
		$('#endDateEnd').datebox({
			required:true
		});
	}
});
</script>
