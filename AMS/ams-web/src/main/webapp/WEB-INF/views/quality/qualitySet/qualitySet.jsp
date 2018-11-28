<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-panel W100" data-options="collapsible:true">
	<form id="regular_queryForm" class="margin_20">
		<table class="table_ui W80 left_m">
			<tr>
				<th></th>
				<td>周期起始日期:</td>
				<td>周期结束日期:</td>
				<td>通过件抽检率:</td>
				<td>拒绝件抽检率:</td>
				<td>更新时间:</td>
				<td>更新人:</td>
			</tr>
			<tr>
				<th>本&nbsp;&nbsp;次</th>
				<td><input type="text" id="startdate"name="startDate" value="${now.startDate}" class="easyui-datebox input" data-options="prompt:'周期起始日期',width:120,validType:'date',required:true"></td>
				<td><input type="text" id="enddate" name="endDate" value="${now.endDate}" class="easyui-datebox input" data-options="prompt:'周期结束日期',width:120,validType:'date',required:true"></td>
				<td class="inputmask"><input class="easyui-numberbox input" value="${now.passRate}" id="passRate" name="passRate" data-options="width:120,prompt:'通过件抽检率',buttonText:'%',required:true"></td>
				<td class="inputmask"><input class="easyui-numberbox input" value="${now.rejectRate}" id="rejectRate" name="rejectRate" data-options="width:120,prompt:'拒绝件抽检率',buttonText:'%',required:true"></td>
				<td><input  id="lastModifiedDate" name="lastModifiedDate" value="${now.lastModifiedDate}" class="easyui-datebox input" data-options="readonly:true,width:100"></td>
				<td><input  id="lastModifiedBy" name="lastModifiedBy" value="${now.lastModifiedBy}" class="easyui-textbox input" data-options="readonly:true,width:80"></td>
			</tr>
			<tr>
				<th>下一次</th>
				<td><input type="text" id="nextstartdate" name="nextStartDate" value="${next.startDate}" class="easyui-datebox input" data-options="prompt:'周期起始日期',width:120,validType:'date',required:true,readonly:true"></td>
				<td><input type="text" id="nextenddate" name="nextEndDate" value="${next.endDate}" class="easyui-datebox input" data-options="prompt:'周期结束日期',width:120,validType:'date',required:true"></td>
				<td class="inputmask"><input class="easyui-numberbox input" value="${next.passRate}" id="nextPassRate" name="nextPassRate" data-options="width:120,prompt:'通过件抽检率',buttonText:'%',required:true"></td>
				<td class="inputmask"><input class="easyui-numberbox input" value="${next.rejectRate}" id="nextRejectRate" name="nextRejectRate" data-options="width:120,prompt:'拒绝件抽检率',buttonText:'%',required:true"></td>
		 		<td><input  id="nextlastModifiedDate" name="nextlastModifiedDate" value="${next.lastModifiedDate}" class="easyui-datebox input" data-options="readonly:true,width:100"></td>
				<td><input  id="nextlastModifiedBy" name="nextlastModifiedBy" value="${next.lastModifiedBy}" class="easyui-textbox input" data-options="readonly:true,width:80"></td>
			</tr>
		</table>
			<table class="table_ui W80">
				<tr>
					<td>
						<a class="easyui-linkbutton" onclick="samplingRateSave(this)">设置完成</a>
								&nbsp; &nbsp;
					</td>
				</tr>
			</table>
		</form>
</div>
<script type="text/javascript" src="${ctx}/resources/lib/inputmask/jquery.inputmask.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/lib/inputmask/inputmask.regex.extensions.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualitySet.js"></script>

