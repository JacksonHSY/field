<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west'" style="width: 15%;">
		<div id="personReceiveConfig_tree"></div>
	</div>
	<div data-options="region:'center'">
		<div class="easyui-tabs">
			<div title="机构">
				<div class="easyui-panel">
					<div class="margin_20">
						<form id="personReceiveConfig_Company_Query_Form">
							<table class="table_ui W70 ">
								<tr>
									<th width="65">组织编码:</th>
									<td width="180"><input type="text" class="easyui-textbox input" data-options="prompt:'组织编码'" id="org_code" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;" /></td>
									<th width="65">组织名称:</th>
									<td width="180"><input type="text" class="easyui-textbox input" data-options="prompt:'组织名称'" id="org_name" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;" /></td>
									<td><a class="easyui-linkbutton" onclick="searchOrgList()"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<div>
					<!-- 	机构 -->
					<div id="personReceiveConfig_Company_toolBarBtn">
						<a class="easyui-linkbutton" onclick="companyUpButton()" data-options="plain:true">
							<i class="fa fa-plus"></i>上限配置</a>
						<a class="easyui-linkbutton" id="first_order_ability_company_a" onclick="companyReButton()" data-options="plain:true">
							<i class="fa fa-plus"></i>接单配置</a>
					</div>
					<table id="company_datagrid" style="min-height: 500px; max-height: 750px;"></table>
				</div>
			</div>
			<div title="人员">
				<div class="easyui-panel">
					<div class="margin_20">
						<form id="personReceiveConfig_Person_Query_Form">
							<table class="table_ui W70 ">
								<tr>
									<th width="65">工号:</th>
									<td width="180"><input type="text" class="easyui-textbox input" data-options="prompt:'工号'" id="user_code" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;" /></td>
									<th width="65">姓名:</th>
									<td width="180"><input type="text" class="easyui-textbox input" data-options="prompt:'姓名'" id="user_name" style="margin-left: 0px; margin-right: 0px; padding-top: 8px; padding-bottom: 8px; width: 180px;" /></td>
									<td><a class="easyui-linkbutton" onclick="searchUserList()"><i class="fa fa-search"></i>搜&nbsp;索</a></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<div>
					<div id="personReceiveConfig_Person_toolBarBtn">
						<a class="easyui-linkbutton" onclick="personReceiveConfigPersonUp()" data-options="plain:true">
							<i class="fa fa-plus"></i>上限配置</a>
						<a class="easyui-linkbutton" id="first_order_ability_person_a" onclick="personReceiveConfigPersonRe()" data-options="plain:true">
							<i class="fa fa-plus"></i>接单配置</a>
					</div>
					<table id="person_datagrid" style="min-height: 500px; max-height: 750px;"></table>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="personReceiveConfig_Set_dialog" class="display_none">
	<form id="up_config_Form" class="margin_20">
		<table class="table_ui W100 center_m">
			<tr>
				<th>正常队列上限:</th>
				<td><input class="easyui-numberbox input" name="normalQueueMax" data-options="precision:0,validType:['compareMaxMin[0,${MaxNormal}]']"></td>
			</tr>
			<tr>
				<th>挂起队列上限:</th>
				<td><input class="easyui-numberbox input" name="hangQueueMax" data-options="precision:0,validType:['compareMaxMin[0,${MaxHang}]']"></td>
			</tr>
			<tr>
				<td><input type="hidden" name="taskDefId"></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="${ctx}/resources/js/system/personReceiveConfig/firstOrderAbility.js"></script>