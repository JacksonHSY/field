<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript" src="${ctx}/resources/js/system/timeManage/timeManage.js"></script>
<div class="easyui-tabs" >

	<div title="角色时间限制" style="height:800px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west'" style="width: 15%;">
				<div id="role_tree"></div>
			</div>
			
			<div data-options="region:'east'" style="width: 85%;">
				<!-- 由于选择初审员和选择其他角色列表和搜索条件不一样，故分为两个div -->
				<div id="check_role" class="display_none">
					<form id="check_role_time_manage_form_Id">
						<table class="table_ui W100">
							<tr>
							    <th>姓名:</th>
								<td><input id="check_userName" type="text" class="easyui-textbox input" style="width: 150px;" name="userName" ></td>
								<th>工号:</th>
								<td><input id="check_userCode" type="text" class="easyui-textbox input" style="width: 150px;" name="userCode" ></td>
								<th>通用设置:</th>
								<td>
									<select id="check_timeType" class="easyui-combobox select" style="width: 150px;" name="timeType" data-options="onChange:timeTypeChange_checkRole,value:'',prompt:'通用设置',editable:false">
										<option value="">全部</option>
										<option value="0">工作日</option>
										<option value="1">节假日</option>
									</select>
								</td>
								<th>特殊时间起:</th>
								<td><input name="startDate" type="text" class="easyui-datebox input"  style="width: 150px;" data-options="editable:false,prompt:'特殊时间起'" id="checkRole_startTime"></td>
								<th>特殊时间止:</th>
								<td><input name="endDate" type="text" class="easyui-datebox input"  style="width: 150px;" data-options="editable:false,prompt:'特殊时间止'" id="checkRole_endTime"></td>
							</tr>
							<tr>
								<td colspan="10">
									<a class="easyui-linkbutton" onclick="query()"><i class="fa fa-search" aria-hidden="true"></i>查&nbsp;询</a>&nbsp; &nbsp;
									<a class="easyui-linkbutton" onclick="addRecord()"><i class="fa fa-plus" aria-hidden="true"></i>新&nbsp;增</a>&nbsp; &nbsp;
									<a class="easyui-linkbutton" onclick="delRecord_batch()"><i class="fa fa-trash-o fa-lg" aria-hidden="true"></i>删&nbsp;除</a>&nbsp; &nbsp;
									<a class="easyui-linkbutton" onclick="editRecord_batch()"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>修&nbsp;改</a>&nbsp; &nbsp;
									<a onclick="clearForm_all()" class="easyui-linkbutton"><i class="fa fa-undo" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;
								</td>
							</tr>
						</table>
					</form>
					<div class="h20"></div>
					<table id="checkRole_time_manage_datagrid"></table>
				</div>
				
				<div id="other_role" class="display_none">
					<form id="other_role_time_manage_form_Id">
						<table class="table_ui W70">
							<tr>
								<th>通用设置:</th>
								<td>
									<select id="other_timeType" class="easyui-combobox select" style="width: 150px;" name="timeType" data-options="onChange:timeTypeChange_otherRole,value:'',prompt:'通用设置',editable:false">
										<option value="">全部</option>
										<option value="0">工作日</option>
										<option value="1">节假日</option>
									</select>
								</td>
								<th>特殊时间起:</th>
								<td><input name="startDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'特殊时间起'" id="otherRole_startTime"></td>
								<th>特殊时间止:</th>
								<td><input name="endDate" type="text" class="easyui-datebox input" data-options="editable:false,prompt:'特殊时间止'" id="otherRole_endTime"></td>
							</tr>
							<tr>
								<td colspan="6">
									<a class="easyui-linkbutton" onclick="query()"><i class="fa fa-search" aria-hidden="true"></i>查&nbsp;询</a>&nbsp; &nbsp;
									<a class="easyui-linkbutton" onclick="addRecord()"><i class="fa fa-plus" aria-hidden="true"></i>新&nbsp;增</a>&nbsp; &nbsp;
									<a class="easyui-linkbutton" onclick="delRecord_batch()"><i class="fa fa-trash-o fa-lg" aria-hidden="true"></i>删&nbsp;除</a>&nbsp; &nbsp;
									<a onclick="clearForm_all()" class="easyui-linkbutton"><i class="fa fa-undo" aria-hidden="true"></i>重&nbsp;置</a>&nbsp; &nbsp;
								</td>
							</tr>
						</table>
					</form>
					<div class="h20"></div>
					<table id="otherRole_time_manage_datagrid"></table>
				</div>
			</div>
			
		</div>
		
	</div>
	
	<div title="查看日历">
		<jsp:include page="maintenance.jsp"></jsp:include>
	</div>

</div>

<!-- 初审员新增限制记录弹框 -->
<div id="check_role_add_Dialog" class="display_none padding_20">
	<form id="check_role_add_Form">
		<table class="table_ui WH100">
			<tr>
				<th>初审员:</th>
				<td><input id="checkRole_userCodes" name="userCodes" type="text" style="width: 212px;"><span class="float_span_class">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			</tr>
			<tr>
				<th>日期类型:</th>
				<td><select name="timeType" class="easyui-combobox select" style="width: 212px;" data-options="required:true,onChange:timeTypeChange_add_checkRole,value:'',prompt:'日期类型',editable:false">
						<option value="0">工作日</option>
						<option value="1">节假日</option>
						<option value="2">特殊日期</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>选择日期:</th>
				<td><input name="date" id="checkRole_date" type="text" class="easyui-datebox input" data-options="required:false,disabled:true,editable:false,prompt:'选择日期'"></td>
			</tr>
			<tr>
				<th>可用时间段:</th>
				<td><input name="startTime" id="checkRole_startTime_add" value="09:00:00" style="width:100px;" class="easyui-timespinner" data-options="required:true,min:'00:00:00',showSeconds:true">~
					<input name="endTime" id="checkRole_endTime_add" value="18:00:00" style="width:100px;" class="easyui-timespinner" data-options="required:true,max:'23:59:59',showSeconds:true">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="disableTime('#checkRole_startTime_add','#checkRole_endTime_add')" >禁用</a>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 其他角色新增限制记录弹框 -->
<div id="other_role_add_Dialog" class="display_none padding_20">
	<form id="other_role_add_Form">
		<table class="table_ui WH100">
			<tr>
				<th>角色名称:</th>
				<td><span id="role" style="width: 212px;"></span></td>
			</tr>
			<tr>
				<th>日期类型:</th>
				<td><select name="timeType" class="easyui-combobox select" style="width: 212px;" data-options="required:true,onChange:timeTypeChange_add_otherRole,value:'',prompt:'日期类型',editable:false">
						<option value="0">工作日</option>
						<option value="1">节假日</option>
						<option value="2">特殊日期</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>选择日期:</th>
				<td><input name="date" id="otherRole_date" type="text" class="easyui-datebox input" data-options="required:false,disabled:true,editable:false,prompt:'选择日期'"></td>
			</tr>
			<tr>
				<th>可用时间段:</th>
				<td><input name="startTime" id="otherRole_startTime_add" value="09:00:00" style="width:100px;" class="easyui-timespinner" data-options="required:true,min:'00:00:00',showSeconds:true">~
					<input name="endTime" id="otherRole_endTime_add" value="18:00:00" style="width:100px;" class="easyui-timespinner" data-options="required:true,max:'23:59:59',showSeconds:true">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="disableTime('#otherRole_startTime_add','#otherRole_endTime_add')" >禁用</a>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 初审员修改限制记录弹框 -->
<div id="check_role_edit_Dialog" class="display_none padding_20">
	<form id="check_role_edit_Form">
		<table class="table_ui WH100">
			<tr>
				<th style="width: 70px;">姓名:</th>
				<td><span id="checkRole_users_edit" style="width: 212px;"></span></td>
			</tr>
			<tr>
				<th style="width: 70px;">日期类型:</th>
				<td><span id="checkRole_timeType" style="width: 212px;"></span></td>
			</tr>
			<tr>
				<th style="width: 70px;">日期:</th>
				<td><span id="checkRole_date_edit"></span></td>
			</tr>
			<tr>
				<th style="width: 70px;">可用时间段:</th>
				<td><input name="startTime" id="checkRole_startTime_edit" style="width:100px;" class="easyui-timespinner" data-options="required:true,min:'00:00:00',showSeconds:true">~
					<input name="endTime" id="checkRole_endTime_edit" style="width:100px;" class="easyui-timespinner" data-options="required:true,max:'23:59:59',showSeconds:true">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="disableTime('#checkRole_startTime_edit','#checkRole_endTime_edit')" >禁用</a>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 其他角色修改限制记录弹框 -->
<div id="other_role_edit_Dialog" class="display_none padding_20">
	<form id="other_role_edit_Form">
		<table class="table_ui WH100">
			<tr>
				<th>角色名称:</th>
				<td><span id="otherRole_role_edit" style="width: 212px;"></span></td>
			</tr>
			<tr>
				<th>日期类型:</th>
				<td><span id="otherRole_timeType" style="width: 212px;"></span></td>
			</tr>
			<tr>
				<th>日期:</th>
				<td><span id="otherRole_date_edit"></span></td>
			</tr>
			<tr>
				<th>可用时间段:</th>
				<td><input name="startTime" id="otherRole_startTime_edit" style="width:100px;" class="easyui-timespinner" data-options="required:true,min:'00:00:00',showSeconds:true">~
					<input name="endTime" id="otherRole_endTime_edit" style="width:100px;" class="easyui-timespinner" data-options="required:true,max:'23:59:59',showSeconds:true">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="disableTime('#otherRole_startTime_edit','#otherRole_endTime_edit')" >禁用</a>
				</td>
			</tr>
		</table>
	</form>
</div>

