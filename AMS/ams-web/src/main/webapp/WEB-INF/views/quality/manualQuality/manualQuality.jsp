<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-tabs">
	<div title="手工分派">
		<div class="easyui-panel W100" data-options="collapsible:true">
			<form id="manualDispatch_queryForm" class="margin_20">
				<table class="table_ui W100 center_m">
					<tr>
						<th>客户姓名:</th>
						<td><input type="text" name="customerName" class="easyui-textbox input" data-options="prompt:'姓名',width:190"></td>
						<th>身份证号码:</th>
						<td><input type="text" name="idNo" class="easyui-textbox input" data-options="prompt:'身份证号码',validType:'IDCard',width:190"></td>
						<th>质检人员:</th>
						<td><input type="text" name="checkUser" class="easyui-combobox input" data-options="textField:'name',valueField:'usercode',prompt:'质检人员',url:'${ctx}/qualityPersonnelManagement/getTwoCyclePerson',width:190,panelHeight:'auto'"></td>
						<th>进件营业部:</th>
						<td><input type="text" name="owningBranceId" class="easyui-combobox input" data-options="textField:'name',valueField:'id',prompt:'进件营业部',url:'${ctx}/pmsApi/findAllDepts',width:190,multiple:true,width:190"></td>

					</tr>
					
					<tr>
						<th>申请件编号:</th>
						<td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'申请件编号',width:190"></td>
						<th>申请状态:</th>
						<td>
							<select class="easyui-combobox select" name="assignType" id="assignType" data-options="width:190,panelHeight:'auto',value:''">
								<option value="1">未分派</option>
								<option value="0">已分派</option>
							</select>
						</td>
						<th>申请来源:</th>
						<td><input type="text" name="source" class="easyui-combobox input" data-options="textField:'qualitySource',valueField:'qualitySource',url:'${ctx}/qualitySource/getAllSource',prompt:'申请来源',width:190,panelHeight:'auto'"></td>
						<th>审批结果:</th>
						<td>
							<select class="easyui-combobox select" name="approvalStatus" id="approvalStatus" data-options="width:190,panelHeight:'auto',value:''">
								<option value="1">拒绝</option>
								<option value="0">通过</option>
								<option value="2">其他</option>
							</select>
						</td>
					</tr>

					<tr>
						<th>分派日期:</th>
						<td><input type="text" name="assignDateStart" class="easyui-datebox input"
							       data-options="editable:false, prompt:'分派开始日期',validType:['date','compareToday'], width:190"
								   id="assignDateStart"></td>
						<th>至:</th>
						<td><input type="text" name="assignDateEnd"class="easyui-datebox input"
								   data-options="prompt:'分派结束日期', editable:false, validType:['date','compareDate[\'#assignDateStart\']','compareToday'],width:190"
					 			   id="assignDateEnd"></td>
						
						<th></th>
						<td></td>
						<th></th>

						<td><a class="easyui-linkbutton" id="manualDispatch_Reset"><i class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
						<a class="easyui-linkbutton" id="manualDispatch_Query"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
					</tr>
				
				</table>
			</form>
		</div>
		<div class="h20"></div>
		<div>
			<div id="manualDispatch_toolBarBtn">
				<a class="easyui-linkbutton" data-options="plain:true" id="manualDispatch_import"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;入</a>&nbsp;
				<a class="easyui-linkbutton" data-options="plain:true" onclick="dispatch();">
				<i class="fa fa-plane" aria-hidden="true" ></i>分&nbsp;派</a>&nbsp; 
				<a class="easyui-linkbutton" data-options="plain:true" onclick="dispatchDispatchClose();">批量关闭</a>&nbsp; 
				<a class="easyui-linkbutton" data-options="plain:true" id="manualDispatch_export"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;出</a>&nbsp;
				<a class="easyui-linkbutton" data-options="plain:true" id="manualDispatch_delete"><i class="fa fa-external-link" aria-hidden="true"></i>导&nbsp;入&nbsp;删&nbsp;除</a>&nbsp;
			</div>
			<table id="manualDispatch_datagrid"></table>
		</div>
		<div id="manualDispatch_person_dialog" class="display_none padding_20">
			<table class="table_ui W100">
				<tr>
					<th>分派至:</th>
					<td>
						<input id="manualDispatchTo" type="text" class="easyui-combobox input" data-options="textField:'checkUserName',valueField:'checkUser',prompt:'质检人员',url:'${ctx}/manualQuality/getAssignPerson'">
					</td>
				</tr>
			</table>
		</div>

<div class="padding_20 display_none" id="manualQuality_dispatch_import" >
		  	<form id="files" method="post" enctype="multipart/form-data" action="${ctx}/manualQuality/uploadExcle">  
  	 				选择文件：　<input id="filename" name="loadExcel" buttonText="选择" class="easyui-filebox" style="width:130px" data-options="prompt:'请选择文件...'">  
  	 				　<a href="#" class="easyui-linkbutton" style="width:80px" onclick="uploadExcel()" >导入</a>
			</form>
</div>
<div class="padding_20 display_none" id="manualQuality_dispatch_delete" >
		  	<form id="delfiles" method="post" enctype="multipart/form-data" action="${ctx}/manualQuality/deleteExcle">  
  	 				选择文件：　<input id="delfilename" name="loadExcel" buttonText="选择" class="easyui-filebox" style="width:130px" data-options="prompt:'请选择文件...'">  
  	 				<td>
  	 				　<a href="#" class="easyui-linkbutton"  style="width:80px" onclick="deleteFiles()" >导入</a>
  	 				</td>
			</form>
</div>
</div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/manualQuality.js"></script>
