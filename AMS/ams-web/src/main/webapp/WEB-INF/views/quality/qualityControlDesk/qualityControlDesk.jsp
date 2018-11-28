<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-tabs">
	<div title="待处理">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="qualityControlDesk_todo_queryForm" class="margin_20">
				<table class="table_ui W100 center_m">
					<tr>
						<th>客户姓名:</th>
						<td><input type="text" name="customerName" class="easyui-textbox input" data-options="prompt:'客户姓名',width:190"></td>
						<th>身份证号:</th>
						<td><input type="text" name="idNo" class="easyui-textbox input" data-options="prompt:'身份证号',validType:'IDCard',width:190"></td>
						<th>进件营业部:</th>
						<td><input type="text" name="owningBranchId" class="easyui-combobox input" data-options="multiple:true, textField:'name',valueField:'id',prompt:'进件营业部',url:'${ctx}/pmsApi/findAllDepts',width:190"></td>
						<th>申请件编号:</th>
						<td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'申请件编号',width:190"></td>
					</tr>
					<tr>
						<th>审批结果:</th>
						<td><select type="text" class="easyui-combobox input" name="approvalStatus" data-options="value:'',width:190">
								<option value="0">通过</option>
								<option value="1">拒绝</option>
								<option value="2">其他</option>
							</select>
						</td>
						<th>分派日期:</th>
						<td><input type="text" name="assignDateStart" class="easyui-datebox input"
								   data-options="editable:false, prompt:'分派开始日期',validType:['date','compareToday'],width:190"
								   id="queryControlDesk_todo_inStartDate"></td>
						<th>至:</th>
						<td><input type="text" name="assignDateEnd" class="easyui-datebox input"
								   data-options="prompt:'分派结束日期', editable:false, validType:['date','compareDate[\'#queryControlDesk_todo_inStartDate\']','compareToday'],width:190"
								   id="queryControlDesk_todo_inEndDate"></td>
						<th></th>
						<td colspan="2"><a class="easyui-linkbutton" id="qualityControlDesk_todo_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; <a class="easyui-linkbutton" id="qualityControlDesk_todo_Query"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<div>
			<div id="qualityControlDesk_todo_toolBarBtn">
				<a class="easyui-linkbutton" id="qualityControlDesk_exportTodo_btn" data-options="plain:true" onclick="exportQualityList('todo');"><i class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>&nbsp;
			</div>
			<table id="qualityControlDesk_todo_datagrid" class="datagrid-container" ></table>
		</div>
	</div>
	<div title="已完成">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="qualityControlDesk_done_queryForm" class="margin_20">
				<table class="table_ui W100 center_m">
					<tr>
						<th>客户姓名:</th>
						<td><input type="text" name="customerName" class="easyui-textbox input" data-options="prompt:'客户姓名'"></td>
						<th>身份证号:</th>
						<td><input type="text" name="idNo" class="easyui-textbox input" data-options="prompt:'身份证号',validType:'IDCard'"></td>
						<th>进件营业部:</th>
						<td><input type="text" name="owningBranchId" class="easyui-combobox input" data-options="multiple:true, textField:'name',valueField:'id',prompt:'进件营业部',url:'${ctx}/pmsApi/findAllDepts'"></td>
						<th>申请件编号:</th>
						<td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'申请件编号'"></td>
					</tr>
					<tr>
						<th>审批结果:</th>
						<td><select type="text" class="easyui-combobox input" name="approvalStatus" style = "width:210px;">
								<option value=""></option>
								<option value="0">通过</option>
								<option value="1">拒绝</option>
								<option value="2">其他</option>
							</select>
						</td>
						<th>完成日期:</th>
						<td><input type="text" name="endDateStart" class="easyui-datebox input"
						data-options="editable:false, prompt:'完成日期',validType:['date','compareToday']"
						id="queryControlDesk_done_inStartDate"></td>
						<th>至:</th>
						<td><input type="text" name="endDateEnd" class="easyui-datebox input"
						data-options="editable:false, validType:['date','compareDate[\'#queryControlDesk_done_inStartDate\']','compareToday']"
						id="queryControlDesk_done_inEndDate"></td>
						<th></th>
						<td colspan="2"><a class="easyui-linkbutton" id="qualityControlDesk_done_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp; <a class="easyui-linkbutton" id="qualityControlDesk_done_Query"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<table id="qualityControlDesk_done_datagrid" class="datagrid-container"></table>
		<div id ="w"></div>
	<!-- 隐藏的组件 -->
	<div>
		<input type = "hidden" id="qualityControlDesk_quality_roleCodes" value='${roleCodes}'>
		<input type = "hidden" id="qualityControlDesk_quality_qualityUser" value='${qualityUser}'>
	</div>
	</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk.js"></script>
<script>
	var refreshDatagrid = function(){
	    $('.datagrid-container').datagrid('reload');
	}
</script>
