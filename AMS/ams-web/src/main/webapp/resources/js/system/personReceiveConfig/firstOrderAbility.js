//@ sourceURL=firstOrderAbility.js
avalon.config({debug:false});
var selectedOrgId = null;
$(document).ready(function($){
    // 初始化左侧机构树
    $("#personReceiveConfig_tree").tree({
        url : ctx.rootPath() + '/pmsApi/findOrgTreeByOrgCode',
        lines : true,
        onLoadSuccess : function(node, data) {
            if (data.length > 0) {
                selectedOrgId = data[0].id;
                //initOrgDataGrid(data[0].id);		// 初始化机构
                initOrgList(data[0].id);			// 初始化机构
				initEmployeeDataGrid(data[0].id, data[0].orgTypeCode);	// 初始化人员
                var node = $('#personReceiveConfig_tree').tree('find', data[0].id);	// 默认选中第一个
                $('#personReceiveConfig_tree').tree('select', node.target);	// 调用选中事件
            }
        },
        onClick : function(node) {
            selectedOrgId = node.id;
            // 选择终审时隐身接单配置
            var selectNode = $('#personReceiveConfig_tree').tree('getSelected');
            var orgTypeCode = selectNode.orgTypeCode;
            $("#first_order_ability_company_a").show();
            $("#first_order_ability_person_a").show();

            // 获取机构信息
            // 清空机构历史选中数据
            $('#company_datagrid').datagrid('clearChecked');
            //initOrgDataGrid(node.id);
            initOrgList(node.id);
			// 清空人员历史选中数据
            $('#person_datagrid').datagrid('clearChecked');
            // 获取机构人员信息
            initEmployeeDataGrid(node.id, node.orgTypeCode);
        }
    });
});

/**
 * 初始化datagrid(人员) fusj 部门Id
 */
function initEmployeeDataGrid(id, role) {
	var role = role=="finalCheck"?"finalCheck":"check";
	$("#person_datagrid").datagrid({
		url : ctx.rootPath() + "/pmsApi/findByOrgIdAndRoleCode",
		striped : true,
		/* singleSelect : true, */
		idField : 'id',
		pagination : false,
		queryParams : {
			orgId : id,
			roleCode : role
		},
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#personReceiveConfig_Person_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'usercode',
			title : '工号',
			width : 100,
		}, {
			field : 'name',
			title : '姓名',
			width : 100,
		}, {
			field : 'creator',
			title : '上限更新人',
			width : 100,
		}, {
			field : 'createTime',
			title : '上限更新时间',
			width : 120,
			sortable : true,
			order : 'desc',
			formatter : formatDateYMDHS
		},{
            field : 'updator',
            title : '接单能力更新人',
            width : 100,
        }, {
            field : 'updateTime',
            title : '接单能力更新时间',
            width : 120,
            sortable : true,
            order : 'desc',
            formatter : formatDateYMDHS
        },  {
			field : 'status',
			title : '状态',
			width : 80,
			formatter : function(value, data, index) {
				if (value == 0) {
					return "正常";
				} else {
					return "禁用";
				}
			}
		} ] ]
	});
}

/**
 * 上限配置(人员)
 * @returns {boolean}
 */
function personReceiveConfigPersonUp() {
	var rows = $('#person_datagrid').datagrid('getSelections');// 获取选中行
	if (!isNotNull(rows)) {
		$.messager.alert('提示', "请选择一个人员！", 'info');
		return false;
	}
	var taskDefId = setTaskDefIdValue();
	// 单个设置需要回显值
	var userCode = '';
	if (rows.length == 1) {
		userCode = rows[0].usercode;
		post(ctx.rootPath() + "/staffOrderAbility/getStaffOrdreSetByStaffCode", {
			staffCode : userCode,
			taskDefId : taskDefId
		}, "json", function(data) {
			if (data.success) {
				$("#up_config_Form").find("tr:eq(0)").find("input:first").numberbox("setValue", data.data.normalQueueMax);
				$("#up_config_Form").find("tr:eq(1)").find("input:first").numberbox("setValue", data.data.hangQueueMax);
			}
		});
	} else {
		$(rows).each(function(ind, val) {
			userCode = userCode + val.usercode + ",";
		});
	}
	$("#personReceiveConfig_Set_dialog").removeClass("display_none").dialog({
		title : '队列上限配置',
		width : 600,
		height : 250,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				$.messager.alert('提示', '确定要提交!', 'warning', function() {
					saveUserStaffAbiltyUp(userCode);
				});
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#personReceiveConfig_Set_dialog").dialog("close");
			}
		} ],
		onClose : function() {
			$("#up_config_Form").form("reset");
		}
	});
}

/**
 * 机构人员配置
 */
function saveUserStaffAbiltyUp(usercode) {
	if ($("#up_config_Form").form('validate')) {
		var obj = $("#up_config_Form").serializeObject();
		obj.usercode = usercode;
		post(ctx.rootPath() + '/staffOrderAbility/saveUserStaffAbility', obj, "json", function(data) {
			if (data) {
				if (data.success) {
					$.messager.alert('提示', '操作成功!', 'warning', function() {
						$.info("提示", "配置已生效!");
					});
					$("#personReceiveConfig_Set_dialog").dialog("close");
				}
			} else {
				$.info("提示", data.messages, "error");
			}
		});
	}
}

/**
 * 上限配置(机构)
 * @returns {boolean}
 */
function companyUpButton() {
	var row = $('#company_datagrid').datagrid('getSelected');
	if (row == null) {
		$.messager.alert('提示', "请选择一个机构！", 'info');
		return false;
	}
	setTaskDefIdValue();
	$("#personReceiveConfig_Set_dialog").removeClass("display_none").dialog({
		title : '队列上限配置',
		width : 600,
		height : 250,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
				$.messager.alert('提示', '确定要提交!', 'warning', function() {
					saveStaffAbiltyUp(row.id);
				});

				return;
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#personReceiveConfig_Set_dialog").dialog("close");
			}
		} ],
		onClose : function() {
			$("#up_config_Form").form("reset");
		}
	});
}

/**
 * 机构上限配置
 */
function saveStaffAbiltyUp(orgId) {
	if ($("#up_config_Form").form('validate')) {
		var obj = $("#up_config_Form").serializeObject();
		obj.orgId = orgId;
		post(ctx.rootPath() + '/staffOrderAbility/saveStaffAbility', obj, "json", function(data) {
			if (data) {
				$.messager.alert('提示', '操作成功!', 'warning', function() {
					$.info("提示", "配置已生效!");
				});
				$("#personReceiveConfig_Set_dialog").dialog("close");
			} else {
				$.info("提示", data.messages, "error");
			}
		});
	}
}

/**
 * 接单配置(人员)
 * 
 * @author fsj
 * @date
 */
function personReceiveConfigPersonRe() {
	var rows = $('#person_datagrid').datagrid('getSelections');// 获取选中行
	if (!isNotNull(rows)) {
		$.info('提示', "请选择一个人员！");
		return false;
	}
	//判断所选人员是否有初审角色(只要有就行)，如果有初审角色，打开接单能力页面，
	var userCode ="";
    if (rows.length == 1) {
        var roleCodes = rows[0].roleCodes;
        if(roleCodes.contains("check")){
            userCode = rows[0].usercode;
            firstShowStaffAbility("staff", userCode);
        }else {
            $.info("提示","该人员没有初审角色！");
        }

    } else {
        $(rows).each(function(ind, val) {
            var roleCodes = val.roleCodes;
            if(roleCodes.contains("check")) {
                userCode = userCode + val.usercode + ",";
            }
        });
        if(isNotNull(userCode)){
            firstShowStaffAbility("staff", userCode);
        }else{
            $.info("提示","所选人员没有初审角色!");
        }
    }
}

Array.prototype.contains = function ( needle ) {
    for (i in this) {
        if (this[i] == needle) return true;
    }
    return false;
}

/**
 * 接单配置(机构)
 * 
 * @author fsj
 * @date
 */
function companyReButton() {
	var row = $('#company_datagrid').datagrid('getSelections');
	if (!row || row.length==0) {
		$.info('提示', "请选择一个机构！", "warning");
		return false;
	}
	// 判断机构下人员是否有初审角色，有初审角色(不一定所有的人员都要有初审角色)才可以打开接单能力页面
    var orgId = new String();
    $(row).each(function(ind, val) {
    	orgId = orgId +","+val.id;
	})
    post(ctx.rootPath() + "/pmsApi/findEmpByDeptAndRoleCodes", {
    	orgId: orgId,
        roleCodes: 'check'
	}, "json", function(data) {
        if(data.length == 0){
            $.info("提示", "该机构下没有初审员!", "warning");
            return false;
        }else {
            var userCodes="";
            data.forEach(function(res){
				userCodes = userCodes+","+res.usercode
            });
            userCodes = userCodes.substring(1);
            firstShowStaffAbility("staff", userCodes);
        }
    });
}

/**
 * @param type 机构 or 个人
 * @param code 用户Code数组
 */
var firstShowStaffAbilityDialog;
function firstShowStaffAbility(type, code) {
	firstShowStaffAbilityDialog = ctx.dialog({
		title : '接单能力配置',
		width : 1650,
		height : 650,
		href : ctx.rootPath() + '/staffOrderAbility/config',
		queryParams : {type: type, code : code},
		modal : true,
		buttons : [ {
            id: 'saveAbility',
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {
            	// 验证接单能力下拉框是否已选择
                var abilityValidate = $('#abilityConfigForm').valid();
                var flag = validates();
                if(!abilityValidate || flag>0){
                    $.info("警告", '请选择接单能力', "warning");
                	return false;
				}

				var $saveBtn = $('#saveAbility');	// 保存按钮
				$.messager.confirm('提示', '确定要提交?', function(ok) {
					if (ok) {
						$saveBtn.linkbutton('disable');	// 保存按钮禁用
						var staffAbilityList = [];
                        $.each(vm.abilityVoList, function(index, abilityVo){
                            $.each(abilityVo.staffAbilityList, function(idx, abilityItem){
                                staffAbilityList.push(abilityItem);
							});
						});
                        // 序列化

						$.ajax({
							url : ctx.rootPath() + "/staffOrderAbility/saveOrUpdateStaffAbility",
							dataType : "JSON",
							method : 'post',
							data : {
								userCodes: JSON.stringify(vm.userList),
                                staffAbility: JSON.stringify(staffAbilityList)
							},
							success : function(data) {
								$saveBtn.linkbutton('enable');	// 保存按钮启用
								if (data.success) {
									firstShowStaffAbilityDialog.dialog("close");
									$.info("提示", "配置已生效!");
								} else {
									$.info("警告", data.firstMessage, "warning");
								}
							},
							error : function(data) {
								$saveBtn.linkbutton('enable');	// 保存按钮启用
								$.info("警告", data.responseText, "warning");
							}
						});
					}
				});
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				firstShowStaffAbilityDialog.dialog("close");
			}
		} ]
	});
}

/**
 * 点击搜索，根据机构编码,名字查询指定机构id下的子机构
 */
function searchOrgList() {
	var orgCode = $("#org_code").val();// input内填写的组织编码
	var orgName = $("#org_name").val();// input内填写的组织名称
	// 加载机构信息列表
	$('#company_datagrid').datagrid('clearChecked');
	initOrgList(selectedOrgId, orgCode, orgName);
}

/**
 * 初始化机构信息列表
 * 
 * @param orgId
 * @param orgCode
 * @param orgName
 */
function initOrgList(orgId, orgCode, orgName) {
	$("#company_datagrid").datagrid({
		url : ctx.rootPath() + "/pmsApi/findOrgByCodeOrName",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : false,
		queryParams : {
			orgId : orgId,
			orgName : orgName,
			orgCode : orgCode
		},
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#personReceiveConfig_Company_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'orgCode',
			title : '组织编码',
			width : 155
		}, {
			field : 'name',
			title : '组织名称',
			width : 155
		}, {
			field : 'orgType',
			title : '组织类型',
			width : 155
		}, {
			field : 'num',
			title : '顺序号',
			width : 155
		}, {
			field : 'updator',
			title : '最后更新人',
			width : 155
		}, {
			field : 'updateTime',
			title : '最后更新时间',
			width : 155,
			sortable : true,
			order : 'desc',
			formatter : function(value, data, index) {
				if(isNotNull(value)){
                    return moment(value).format('YYYY-MM-DD');
				}
				return '';
			}
		}, {
			field : 'status',
			title : '状态',
			width : 155,
			formatter : function(value, data, index) {
				if (value == 0) {
					return "正常";
				} else {
					return "禁用";
				}
			}
		} ] ]
	});
}

/**
 * 点击搜索，根据工号和名字查询指定机构ID下的人员
 */
function searchUserList() {
	var userCode = $("#user_code").val();// input内填写的工号
	var userName = $("#user_name").val();// input内填写的姓名
	// 获取选中的角色
	var selectNode = $('#personReceiveConfig_tree').tree('getSelected');
	var orgTypeCode = selectNode.orgTypeCode;
	// 加载人员信息列表
	$('#person_datagrid').datagrid('clearChecked');
    $("#person_datagrid").datagrid('load',{orgId:selectedOrgId,roleCode:orgTypeCode,userCode:userCode,userName:userName});
}

/**
 * 判断是初审还是终审
 * 
 * @author dmz
 * @date 2017年8月1日
 */
function setTaskDefIdValue() {
	var selectNode = $('#personReceiveConfig_tree').tree('getSelected');
	var taskDefId = "";
	var orgTypeCode = selectNode.orgTypeCode;
	if (isNotNull(orgTypeCode) && ('check' == orgTypeCode || 'saleCheck' == orgTypeCode)) {
		taskDefId = "apply-check";
	}
	if (isNotNull(orgTypeCode) && 'finalCheck' == orgTypeCode) {
		taskDefId = "applyinfo-finalaudit";
	}
	$("#up_config_Form").find("tr:eq(2)").find("input:first").val(taskDefId);
	return taskDefId;
}