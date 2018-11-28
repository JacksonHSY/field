//@ sourceURL = rulConfiguration.js
$(function() {
	initRuleConfListDatagrid("#rule_conf_list", "规则信息");
});

/*=====================================================================组长复核(收回权限)==========================================================================*/

/**
 * 点击组长复核，弹出组长复核(收回权限)页面
 * 
 * @Author
 * @date 2017年3月6日
 */
function leaderReview() {
	ctx.dialog({
		title : "规则信息",
		modal : true,
		width : 1200,
		height : 800,
		href : ctx.rootPath() + '/ruleconfig/groupLeaderReview',
	});
}

/**
 * 组长复核(收回权限)列表
 * 
 * @param id
 * @param title
 * @param pageLists
 */
function initRuleConfListDatagrid(id, title) {
	$(id).datagrid({
		url : ctx.rootPath() + '/ruleconfig/userRuleList',
		// title : title,
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		toolbar : '#rule_conf_datagrid_tool',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			$(id).datagrid('resize');
			var n = 0;
			var t = 1;
			var m = "";
			var merges = new Array();
			for (var int = 0; int < data.rows.length; int++) {
				if (m == data.rows[int].userName) {
					merges[n].rowspan = merges[n].rowspan + 1;
				} else {
					var s = new Object();
					s.index = int;
					s.rowspan = t;
					merges.push(s);

					if (m != "") {
						n = n + 1;
					}
					m = data.rows[int].userName;
					t = 1;
				}
			}
			for (var i = 0; i < merges.length; i++) {
				$(this).datagrid('mergeCells', {
					index : merges[i].index,
					field : 'userName',
					rowspan : merges[i].rowspan
				});
				$(this).datagrid('mergeCells', {
					index : merges[i].index,
					field : 'groupName',
					rowspan : merges[i].rowspan
				});
				$(this).datagrid('mergeCells', {
					index : merges[i].index,
					field : 'updatePerson',
					rowspan : merges[i].rowspan
				});
				$(this).datagrid('mergeCells', {
					index : merges[i].index,
					field : 'updateTime',
					rowspan : merges[i].rowspan
				});
				$(this).datagrid('mergeCells', {
					index : merges[i].index,
					field : 'action',
					rowspan : merges[i].rowspan
				});
			}
		},
		columns : [ [ {
			field : 'userName',
			title : '姓名',
			width : 150,
		}, {
			field : 'groupName',
			title : '所在组',
			width : 150,
		}, {
			field : 'ruleType',
			title : '收回权限类型',
			width : 150,
			formatter : function(value, data, index) {
				if (value == 'XSCS-REJECT') {
					return "拒绝";
				}
				if (value == 'XSCS-PASS') {
					return "通过";
				}
				if (value == 'XSCS-RETURN') {
					return "退回";
				}
				if (value == "XSCS-ZDQQRETURN"){
					return "退回前前"
				}
			}
		}, {
			field : 'ruleNum',
			title : '原因数量',
			width : 150,
			formatter : function(value, row, index) {
				if(isNotNull(value) && value.indexOf('ALL') < 0){
					return "<a href='javaScript:void(0);' onclick=userRuleDetail('" + row.userCode + "')>"+value+"</a>";
				}else{
					return value;
				}
			}
		}, {
			field : 'updatePerson',
			title : '更新人',
			width : 150,
		}, {
			field : 'updateTime',
			title : '更新时间',
			width : 200,
			formatter : formatDate
		}, {
			field : 'action',
			title : '操作',
			width : 204,
			align : 'center',
			formatter : function(value, data, index) {
				return "<a href='javaScript:void(0);' onclick=editRuleConfig('" + data.userCode + "')>修改</a>&nbsp;|&nbsp;<a href='javaScript:void(0);' onclick=deleteRuleConfg('" + data.id + "')>删除</a>";
			}
		} ] ]
	});
}

/**
 * 点击原因数量查看详细收回权限信息
 * 
 * @param userCode
 */
function userRuleDetail(userCode){
	$("#detail_refuse_tree").addClass("display_none");
	$("#detail_return_tree").addClass("display_none");
	$('#detail_refuse_tree').tree({
		url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/reject',
		onLoadSuccess : function(){
			$('#detail_return_tree').tree({
				url : ctx.rootPath() + '/bmsBasiceInfo/getReasonTree/XSCS/return',
				onLoadSuccess : function(){
					post(ctx.rootPath() + "/ruleconfig/getUserRuleByUserCode/"+userCode, null, "json", function(data) {
						if(data.length > 0){
							for (var i = 0; i < data.length; i++) {
								if("XSCS-REJECT" == data[i].ruleType){
									$("#detail_refuse_tree").removeClass("display_none");
									var nodeIds = new Array();//所有父节点id
									var nodeChildIds = new Array();//所有子节点id
									var refRootData = $('#detail_refuse_tree').tree('getRoots');
									var refData = refRootData[0].children;
									for (var x = 0; x < refData.length; x++) {
										var parentId = refData[x].id;
										nodeIds.push(parentId);
										if(refData[x].children != null && refData[x].children.length != 0){
											for (var y = 0; y < refData[x].children.length; y++) {
												var childrenId = refData[x].children[y].id;
												nodeChildIds.push(childrenId);
											}
										}
									}
									
									//杀儿子
									for (var m = 0; m < nodeChildIds.length; m++) {
										var refReasonIds = data[i].reasonIds.split(",");
										if(!isElementOfArray(nodeChildIds[m], refReasonIds)){
											var node = $('#detail_refuse_tree').tree('find', nodeChildIds[m]);
											$('#detail_refuse_tree').tree('remove', node.target);
										}
									}
									
									//把没儿子，且没有被勾选的爹杀了
									for (var n = 0; n < nodeIds.length; n++) {
										var refReasonIds = data[i].reasonIds.split(",");
										if(!isElementOfArray(nodeIds[n], refReasonIds)){
											var node = $('#detail_refuse_tree').tree('find', nodeIds[n]);
											var children = $('#detail_refuse_tree').tree('getChildren', node.target);
											if(isEmptyObj(children)){
												$('#detail_refuse_tree').tree('remove', node.target);
											}
										}
									}
								}else if("XSCS-RETURN" == data[i].ruleType){
									$("#detail_return_tree").removeClass("display_none");
									var nodeIds = new Array();//所有父节点id
									var nodeChildIds = new Array();//所有子节点id
									var refRootData = $('#detail_return_tree').tree('getRoots');
									var refData = refRootData[0].children;
									for (var x = 0; x < refData.length; x++) {
										var parentId = refData[x].id;
										nodeIds.push(parentId);
										if(refData[x].children != null && refData[x].children.length != 0){
											for (var y = 0; y < refData[x].children.length; y++) {
												var childrenId = refData[x].children[y].id;
												nodeChildIds.push(childrenId);
											}
										}
									}
									
									//杀儿子
									for (var m = 0; m < nodeChildIds.length; m++) {
										var refReasonIds = data[i].reasonIds.split(",");
										if(!isElementOfArray(nodeChildIds[m], refReasonIds)){
											var node = $('#detail_return_tree').tree('find', nodeChildIds[m]);
											$('#detail_return_tree').tree('remove', node.target);
										}
									}
									
									//把没儿子，且没有被勾选的爹杀了
									for (var n = 0; n < nodeIds.length; n++) {
										var refReasonIds = data[i].reasonIds.split(",");
										if(!isElementOfArray(nodeIds[n], refReasonIds)){
											var node = $('#detail_return_tree').tree('find', nodeIds[n]);
											var children = $('#detail_return_tree').tree('getChildren', node.target);
											if(isEmptyObj(children)){
												$('#detail_return_tree').tree('remove', node.target);
											}
										}
									}
								}
								var return_max_node = $('#detail_return_tree').tree('find', 0);
								return_max_node.text = "收回的退回原因";
								var refuse_max_node = $('#detail_refuse_tree').tree('find', 0);
								refuse_max_node.text = "收回的拒绝原因";
							}
							
							$("#user_rule_detail_dialog").removeClass("display_none").dialog({
								title : "查看收回权限",
								modal : true,
								width : 600,
								height : 450,
								buttons : [],
								onClose : function() {
									
								}
							});
						}
					});
				}
			});
		}
	});
}

/**
 * 新增收回权限弹框
 * 
 * @Author
 * @date 2017年3月6日
 */
function addBackDuty() {
	var addUserRuleWindow = null;
	post(ctx.rootPath() + "/ruleconfig/ifAllCollected", null, "json", function(data) {
		if(data == 1){
			$.info("提示", "权限内没有初审员可以操作！");
		}else if(data == 2){
			$.info("提示", "辖下初审员均已存在记录，不可新增！");
		}else{
			addUserRuleWindow = jDialog.open({
				url : ctx.rootPath() + '/ruleconfig/addUserRuleWindow',
				width : 900,
				height : 600,
				onDestroy: function(){
					$("#rule_conf_list", addUserRuleWindow.opener.document).datagrid('reload');	
				}
			});
		}
	});
}

/**
 * 修改收回权限弹框
 */
function editRuleConfig(userCode) {
	var editUserRuleWindow = jDialog.open({
		url : ctx.rootPath() + '/ruleconfig/editUserRuleWindow/' + userCode,
		width : 900,
		height : 600,
		onDestroy: function(){
			$("#rule_conf_list", editUserRuleWindow.opener.document).datagrid('reload');	
		}
	});
}

/**
 * 删除某条规则
 * 
 * @param id
 */
function deleteRuleConfg(id) {
	$.messager.confirm('提示', '确认删除该初审员所有的收回权限？', function(r) {
		if (r) {
			post(ctx.rootPath() + '/ruleconfig/deleteUserRule/' + id, null, "json", function(data) {
				if (data.success) {
					$.info("提示", "删除成功");
					$("#rule_conf_list").datagrid('reload');
				} else {
					$.info("提示", data.messages, "error");
				}
			});
		}
	});
}

/**
 * 判断给定字符是否是数组的一个元素
 * 
 * @param str
 * @param arr
 * @returns {Boolean}
 */
function isElementOfArray(str, arr) {
	for (var i = 0; i < arr.length; i++) {
		if (str == arr[i]) {
			return true;
		}
	}
	return false;
}


/*=====================================================================代理组长==========================================================================*/

/**
 * 代理组长弹框
 * 
 * @Author
 * @date 2017年3月6日
 */
function agentLrader() {
	var agentdialog = ctx.dialog({
		title : "代理组长信息",
		href : ctx.rootPath() + '/ruleconfig/agentLeaderReview',
		modal : true,
		width : 650,
		height : 620,
		buttons : [ {
			text : '确认',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				proxyUserForm();
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				agentdialog.dialog("close");
			}
		} ]
	});
}

/**
 * 代理组长保存
 * 
 * @Author
 * @date 2017年3月6日
 */
function proxyUserForm() {
	var obj = $("#ruleConfiguration_agentLrader_Form").serializeObject();
	var b = $("#ruleConfiguration_agentLrader_Form").form('validate');// 验证
	if (!b) {
		$.info("提示", "必填项请选择！", "error");
		return false;
	}
	var agenLeader = new Array();
	if (obj.userCode != null && obj.userCode instanceof Array && obj.userCode.length > 1) {
		for (var i = 0; i < obj.userCode.length; i++) {
			agenLeader.push({
				userCode : obj.userCode[i],
				proxyUser : obj.proxyUser[i]
			});
		}
	} else {
		if(JSON.stringify(obj) != '{}'){
			agenLeader.push({
				userCode : obj.userCode,
				proxyUser : obj.proxyUser
			});
		} else{
			$.info("提示", "没有可设置的信息！", "error");
			return false;
		}
	}
	var params = JSON.stringify(agenLeader);
	var timestamp = Date.parse(new Date());
	api = ctx.rootPath() + '/ruleconfig/saveProxyUser' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			if (data) {
				$.info("提示", data.messages);
			} else {
				$.info("提示", data.messages, "error");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
            $.info("提示", "系统繁忙!", "error");
		}
	});
}