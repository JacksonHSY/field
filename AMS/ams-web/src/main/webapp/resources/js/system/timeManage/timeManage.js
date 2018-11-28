$(function() {
	var role_list = new Array();
	post(ctx.rootPath() + "/timeManage/getRole", null, null, function(data) {
		for (var i = 0; i < data.length; i++) {
			var obj = new Object();
			obj.id = data[i].id;
			obj.code = data[i].code;
			obj.text = data[i].name;
			role_list.push(obj);
		}
    });
	
	for (var i = 0; i < role_list.length; i++) {
		if(role_list[i].code == "check"){
			post(ctx.rootPath() + '/pmsApi/userOrgTreeWithoutDataPermission',null,'json',function(data){
				//递归删除没有初审员的节点
				for (var m = 0; m < 5; m++) {
					remove_node(data);
				}
				role_list[i].children = data;
			});
			break;
		}
	}
	
	$('#role_tree').tree({
		data: role_list,
		onClick: function(node){
			$("#check_role_time_manage_form_Id").form('reset');
			$("#other_role_time_manage_form_Id").form('reset');
			$("#checkRole_startTime").datebox({disabled : false,value : "",});
			$("#checkRole_endTime").datebox({disabled : false,value : "",});
			$("#otherRole_startTime").datebox({disabled : false,value : "",});
			$("#otherRole_endTime").datebox({disabled : false,value : "",});
			$("#check_userName").textbox({disabled : false});
			$("#check_userCode").textbox({disabled : false});
			
			//1:其他角色节点，2：初审员节点状态，3：初审员树中的某个机构，4：某个初审员
			var params = new Object();
			if(isEmpty(node.code)){
				$('#check_role').removeClass("display_none");
				$('#other_role').addClass("display_none");
				if(isEmpty(node.orgCode)){
					//点击的是某个初审员
					params.searchType = 4;
					params.checkUserCode = node.attributes.usercode;
					params.checkUserName = node.text;
					$("#check_userName").textbox({disabled : true});
					$("#check_userCode").textbox({disabled : true});
				}else{
					//点击的是初审员树中的某个机构
					params.searchType = 3;
					params.orgId = node.id;
				}
				console.log(node);
				checkRole_timeManageDataGrid(params);
			}else{
				if(node.code == 'check'){
					//点击的是初审员节点
					$('#check_role').removeClass("display_none");
					$('#other_role').addClass("display_none");
					params.searchType = 2;
					checkRole_timeManageDataGrid(params);
				}else{
					//点击的是其他角色节点
					$('#check_role').addClass("display_none");
					$('#other_role').removeClass("display_none");
					params.searchType = 1;
					params.roleCode = node.code;
					otherRole_timeManageDataGrid(params);
				}
			}
			
		}
	});
	
	//新增时禁用当前日期之前的日期
	$('#checkRole_date').datebox().datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day <= date;
        }
    });
	$('#otherRole_date').datebox().datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day <= date;
        }
    });
	
});

function clearForm_all() {
	$("#check_timeType").combobox({value : ""});
	$("#other_timeType").combobox({value : ""});
	var node = $('#role_tree').tree("getSelected");
	if(isEmpty(node.code) && isEmpty(node.orgCode)){
		//点击的是某个初审员
		$("#check_userName").textbox({disabled : true,value : ""});
		$("#check_userCode").textbox({disabled : true,value : ""});
	}else{
		$("#check_userName").textbox({disabled : false,value : ""});
		$("#check_userCode").textbox({disabled : false,value : ""});
	}
	$("#checkRole_startTime").datebox({disabled : false,value : "",});
	$("#checkRole_endTime").datebox({disabled : false,value : "",});
	$("#otherRole_startTime").datebox({disabled : false,value : "",});
	$("#otherRole_endTime").datebox({disabled : false,value : "",});
}


function remove_node(data){
	if(data.length > 0){
		for (var i = 0; i < data.length; i++) {
			if(isEmpty(data[i].attributes) && data[i].children.length == 0){
				data.splice(i,1);
			}else{
				var child = data[i].children;
				remove_node(child);
			}
		}
	}
}

/**
 * 初审员列表
 * 设置为post请求，json格式，后台用@RequsetBody接收（可以接收list或数组）
 * 
 * @returns
 */
function checkRole_timeManageDataGrid(params) {
    $("#checkRole_time_manage_datagrid").datagrid({
        striped : true,
        singleSelect : false,
        rownumbers : true,
        idField : 'id',
        pagination : true,
        pageSize : 20,
        fitColumns : false,
        scrollbarSize : 0,
        onLoadSuccess : function(data) {
            $("#checkRole_time_manage_datagrid").datagrid('resize');
        },
        loader : function(param, success, error) {
        	param.searchType = params.searchType;
        	param.checkUserCode = params.checkUserCode;
        	param.checkUserName = params.checkUserName;
        	param.orgId = params.orgId;
        	param.roleCode = params.roleCode;
        	param.userCode = params.userCode;
        	param.userName = params.userName;
        	param.timeType = params.timeType;
        	param.startDate = params.startDate;
        	param.endDate = params.endDate;
        	console.log(param);
    		$.ajax({
    			type : "POST",
    			url : ctx.rootPath() + '/timeManage/getList',
    			dataType : 'json',
    			contentType : 'application/json;charset=utf-8', // 设置请求头信息
    			data : JSON.stringify(param),
    			success : function(result) {
    				success(result);
    			}
    		});
    	},
    	idField : 'id',
        columns : [ [ {
			field : 'idField',
			checkbox : true
		}, {
            field : 'userCode',
            width : 180,
            title : '工号',
        }, {
            field : 'userName',
            width : 180,
            title : '姓名',
        }, {
            field : 'date',
            width : 180,
            title : '日期',
            formatter: function (value, row, index) {
                if(row.timeType == '2'){
                    return value;
                }else if(row.timeType == '1'){
                	return '节假日';
                }else if(row.timeType == '0'){
                	return '工作日';
                }
            }
        }, {
            field : 'name',
            width : 180,
            title : '可用时间段',
            formatter: function (value, row, index) {
                return row.startTime + " - " + row.endTime;
            }
        }, {
            field : 'modifierName',
            width : 180,
            title : '更新人',
        }, {
            field : 'modifyTime',
            width : 180,
            title : '更新时间',
            formatter : formatDate
        },{
        	field : 'action',
			title : '操作',
			width : 310,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=update("' + index + '") >修改</a>';
				if(data.timeType == '2'){
					action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick=remove("' + data.id + '")>删除</a>';
				}
				return action;
			}
        }] ]
    });
}


/**
 * 其他角色列表
 * 设置为post请求，json格式，后台用@RequsetBody接收（可以接收list或数组）
 * 
 * @returns
 */
function otherRole_timeManageDataGrid(params) {
    $("#otherRole_time_manage_datagrid").datagrid({
        striped : true,
        singleSelect : false,
        rownumbers : true,
        idField : 'id',
        pagination : true,
        pageSize : 20,
        fitColumns : false,
        scrollbarSize : 0,
        onLoadSuccess : function(data) {
        	$("#otherRole_time_manage_datagrid").datagrid('resize');
        },
        loader : function(param, success, error) {
        	param.searchType = params.searchType;
        	param.roleCode = params.roleCode;
        	param.timeType = params.timeType;
        	param.startDate = params.startDate;
        	param.endDate = params.endDate;
        	console.log(param);
    		$.ajax({
    			type : "POST",
    			url : ctx.rootPath() + '/timeManage/getList',
    			dataType : 'json',
    			contentType : 'application/json;charset=utf-8', // 设置请求头信息
    			data : JSON.stringify(param),
    			success : function(result) {
    				success(result);
    			}
    		});
    	},
    	idField : 'id',
        columns : [ [ {
			field : 'idField',
			checkbox : true
		}, {
            field : 'roleName',
            width : 230,
            title : '角色名称',
        }, {
            field : 'date',
            width : 230,
            title : '日期',
            formatter: function (value, row, index) {
                if(row.timeType == '2'){
                    return value;
                }else if(row.timeType == '1'){
                	return '节假日';
                }else if(row.timeType == '0'){
                	return '工作日';
                }
            }
        }, {
            field : 'name',
            width : 230,
            title : '可用时间段',
            formatter: function (value, row, index) {
                return row.startTime + " - " + row.endTime;
            }
        }, {
            field : 'modifierName',
            width : 230,
            title : '更新人',
        }, {
            field : 'modifyTime',
            width : 230,
            title : '更新时间',
            formatter : formatDate
        },{
        	field : 'action',
			title : '操作',
			width : 240,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=update("' + index + '") >修改</a>';
				if(data.timeType == '2'){
					action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);" onclick=remove("' + data.id + '")>删除</a>';
				}

				return action;
			}
        }] ]
    });
}

//判断选中的是那个节点，并组装查询条件
function convert(node){
	
}
	
//条件查询
function query() {
	var node = $('#role_tree').tree("getSelected");
	
	var params = new Object;
	if(!isEmpty(node.code) && node.code != 'check'){
		params = $("#other_role_time_manage_form_Id").serializeObject();
	}else{
		params = $("#check_role_time_manage_form_Id").serializeObject();
	}
	//日期
	var start = params.startDate;
	var end = params.endDate;
	if((isEmpty(start) && !isEmpty(end)) || (!isEmpty(start) && isEmpty(end))){
		$.info("提示", "查询时间范围不符合要求！");
		return;
	}
	if(!isEmpty(start) && !isEmpty(end)){
		if(compareDate(start,end)){
			$.info("提示", "开始时间不可以大于结束时间！");
			return;
		}
	}
	
	//判断选中的是那个节点
	//1:其他角色节点，2：初审员节点状态，3：初审员树中的某个机构，4：某个初审员
	if(isEmpty(node.code)){
		if(isEmpty(node.orgCode)){
			//点击的是某个初审员
			params.searchType = 4;
			params.checkUserCode = node.attributes.usercode;
			params.checkUserName = node.text;
		}else{
			//点击的是初审员树中的某个机构
			params.searchType = 3;
			params.orgId = node.id;
		}
		checkRole_timeManageDataGrid(params)
	}else{
		if(node.code == 'check'){
			//点击的是初审员节点
			params.searchType = 2;
			checkRole_timeManageDataGrid(params)
		}else{
			//点击的是其他角色节点
			params.searchType = 1;
			params.roleCode = node.code;
			otherRole_timeManageDataGrid(params)
		}
	}
	
	
}

/**
 * 查询时时间类型改变
 * @returns
 */
function timeTypeChange_checkRole(newValue,oldValue){
	if(isEmpty(newValue)){
		$("#checkRole_startTime").datebox({
			disabled : false,
			value : "",
		});
		$("#checkRole_endTime").datebox({
			disabled : false,
			value : "",
		});
	}else{
		$("#checkRole_startTime").datebox({
			disabled : true,
			value : "",
		});
		$("#checkRole_endTime").datebox({
			disabled : true,
			value : "",
		});
	}
}

function timeTypeChange_otherRole(newValue,oldValue){
	if(isEmpty(newValue)){
		$("#otherRole_startTime").datebox({
			disabled : false,
			value : "",
		});
		$("#otherRole_endTime").datebox({
			disabled : false,
			value : "",
		});
	}else{
		$("#otherRole_startTime").datebox({
			disabled : true,
			value : "",
		});
		$("#otherRole_endTime").datebox({
			disabled : true,
			value : "",
		});
	}
}


/**
 * 新增记录弹出框
 * 
 * @returns
 */
function addRecord() {
	var params = new Object();
	var node = $('#role_tree').tree("getSelected");
	//1:其他角色节点，2：初审员节点状态，3：初审员树中的某个机构，4：某个初审员
	if(isEmpty(node.code)){
		if(isEmpty(node.orgCode)){
			//点击的是某个初审员
			params.searchType = 4;
			params.checkUserCode = node.attributes.usercode;
			params.checkUserName = node.text;
		}else{
			//点击的是初审员树中的某个机构
			params.searchType = 3;
			params.orgId = node.id;
		}
	}else{
		if(node.code == 'check'){
			//点击的是初审员节点
			params.searchType = 2;
		}else{
			//点击的是其他角色节点
			params.searchType = 1;
		}
	}
	
	if(params.searchType == 1){
		//初始化新增其他角色弹框
		$("#role").html(node.text);
		$("#other_role_add_Dialog").removeClass("display_none").dialog({
			title : "新增",
			modal : true,
			width : 450,
			height : 250,
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					saveRecord(node.code);
				}
			}, {
				text : '关闭',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#other_role_add_Dialog").dialog("close");
				}
			} ],
			onClose : function() {
				$("#other_role_add_Dialog").find("form").form("reset");
				timeTypeChange_add_otherRole('1');
			}
		});
		
	}else{
		//初始化新增初审员弹框
		$.ajax({
			url: ctx.rootPath() + "/timeManage/getCheckUsers",
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			dataType: "JSON",
			data: JSON.stringify(params),
			success: function(response){
				$("#checkRole_userCodes").combobox({
					valueField : 'userCode',
					textField : 'userName',
					prompt:'初审员',
					required : true,
					data : response,
					onChange : onValueChange,
					onUnselect: onUnselect,
					onSelect: onSelect,
					multiple: true
				});
			},
			error : function(data) {
				$.info("警告", '系统繁忙，请稍后再试！', "warning");
			}
		});
		
		$("#check_role_add_Dialog").removeClass("display_none").dialog({
			title : "批量新增",
			modal : true,
			width : 450,
			height : 250,
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					saveRecord("check");
				}
			}, {
				text : '关闭',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#check_role_add_Dialog").dialog("close");
				}
			} ],
			onClose : function() {
				$("#check_role_add_Dialog").find("form").form("reset");
				$("#check_role_add_Form").find(".float_span_class").each(function(){
					this.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
				});
				timeTypeChange_add_checkRole('1');
			}
		});
	}
	
}

/**
 * 新增或者修改时时间类型改变
 * 
 * @returns
 */
function timeTypeChange_add_checkRole(newValue,oldValue){
	if("2" == newValue){
		$("#checkRole_date").datebox({
			required : true,
			disabled : false,
			value : "",
		});
	}else if("1" == newValue || "0" == newValue){
		$("#checkRole_date").datebox({
			required : false,
			disabled : true,
			value : "",
		});
	}
}

function timeTypeChange_add_otherRole(newValue,oldValue){
	if("2" == newValue){
		$("#otherRole_date").datebox({
			required : true,
			disabled : false,
			value : "",
		});
	}else if("1" == newValue || "0" == newValue){
		$("#otherRole_date").datebox({
			required : false,
			disabled : true,
			value : "",
		});
	}
}

/**
 * 新增时间限制记录保存
 * 
 * @returns
 */
function saveRecord(role){
	var validate = false;
	var params = new Object();
	if(role == "check"){
		validate = $("#check_role_add_Form").form('validate');
		params = $("#check_role_add_Form").serializeObject();
		var users = params.userCodes;
		if (Object.prototype.toString.call(users) === "[object String]") {
			var userCodes = new Array();
			userCodes[0] = users;
			params.userCodes = userCodes;
		}
	}else{
		validate = $("#other_role_add_Form").form('validate');
		params = $("#other_role_add_Form").serializeObject();
	}
	if(validate){
		if(params.endTime < params.startTime){
			$.info("提示", "结束时间不能小于开始时间!", "error");
			return;
		}
		params.startTime = params.startTime;
		params.endTime = params.endTime;
		params.role = role;
		console.log(params);
		
		//如果是其他角色，先校验新增的限制记录是否已经存在，如果存在，则给出提示
		if(role != "check"){
			post(ctx.rootPath() + "/timeManage/hasRecord", params, null, function(data) {
				if (data) {
					//如果记录已经存在
					$.messager.confirm('覆盖确认', '该日期/工作日/节假日已设置使用时间段，是否覆盖原有设置？', function(r){
				        if (r){
				        	save(params);
				        }else{
				        	$("#other_role_add_Dialog").dialog("close");
				        	$("#other_role_add_Dialog").find("form").form("reset");
				        	$("#otherRole_time_manage_datagrid").datagrid('reload');
				        }
				    });
				}else{
					save(params);
				}
			});
		}else{
			save(params);
		}
		
		
	}
}

//新增
function save(params){
	$.ajax({
		url : ctx.rootPath() + "/timeManage/add",
		dataType : "json",
		method : 'post',
		contentType : 'application/json',
		data : JSON.stringify(params),
		success : function(data) {
			console.log(data);
			if (data.success) {
				$.info("提示", data.messages);
				if(params.role == "check"){
					$("#check_role_add_Dialog").dialog("close");
					$("#check_role_add_Dialog").find("form").form("reset");
					$("#checkRole_time_manage_datagrid").datagrid('reload');
				}else{
					$("#other_role_add_Dialog").dialog("close");
					$("#other_role_add_Dialog").find("form").form("reset");
					$("#otherRole_time_manage_datagrid").datagrid('reload');
				}
			} else {
				$.info("提示", "新增失败!", "error");
			}
		},
		error : function(data) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			$.info("提示", "新增失败!", "info", 2000);
		}
	});
}

/**
 * 删除某条记录
 * 
 * @param id
 * @returns
 */
function remove(id){
	$.messager.confirm('删除确认', '是否确认删除该角色的限制时间记录？', function(r){
        if (r){
        	post(ctx.rootPath() + "/timeManage/delete", {id: id}, null, function(data) {
        		if (data.success) {
					$.info("提示", "删除成功!");
					
					var node = $('#role_tree').tree("getSelected");
					if(!isEmpty(node.code) && node.code != 'check'){
						$("#otherRole_time_manage_datagrid").datagrid('reload');
					}else{
						$("#checkRole_time_manage_datagrid").datagrid('reload');
					}
				} else {
					$.info("提示", "删除失败!", "error");
				}
    	    });
        }
    });
}

/**
 * 批量删除
 * 
 * @returns
 */
function delRecord_batch(){
	var rows = new Object();
	var node = $('#role_tree').tree("getSelected");
	if(!isEmpty(node.code) && node.code != 'check'){
		rows = $('#otherRole_time_manage_datagrid').datagrid('getSelections');
	}else{
		rows = $('#checkRole_time_manage_datagrid').datagrid('getSelections');
	}
	
	if(rows.length == 0){
		$.info("提示", "请选择要删除的记录!", "warning");
		return;
	}
	
	for (var i = 0; i < rows.length; i++) {
		if(rows[i].timeType != 2){
			$.info("提示", "请选择特殊日期的记录进行删除!", "warning");
			return;
		}
	}
	
	$.messager.confirm('删除确认', '是否确认删除该角色的限制时间记录？', function(r){
        if (r){
        	var ids = new Array(rows.length);
        	for (var i = 0; i < rows.length; i++){
        		ids[i] = rows[i].id;
        	}
        	console.log(ids);
        	
        	$.ajax({
        		url : ctx.rootPath() + "/timeManage/batchDelete",
        		dataType : "json",
        		method : 'post',
        		contentType : 'application/json',
        		data : JSON.stringify(ids),
        		success : function(data) {
        			if (data.success) {
        				$.info("提示", "删除成功!");
        				var node = $('#role_tree').tree("getSelected");
        				if(!isEmpty(node.code) && node.code != 'check'){
        					$("#otherRole_time_manage_datagrid").datagrid('reload');
        					$("#otherRole_time_manage_datagrid").datagrid('clearSelections');
        				}else{
        					$("#checkRole_time_manage_datagrid").datagrid('reload');
        					$("#checkRole_time_manage_datagrid").datagrid('clearSelections');
        				}
        			} else {
        				$.info("提示", "删除失败!", "error");
        			}
        		},
        		error : function(data) {
        			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
        			$.info("提示", "删除失败!", "info", 2000);
        		}
        	});
        }
    });
	
}


function update(index){
	var rows = new Object();//当前页所有行数据
	var row = new Object();//修改的那一行
	var node = $('#role_tree').tree("getSelected");
	var name = "";
	var editType = "";
	var ids = new Array();
	if(!isEmpty(node.code) && node.code != 'check'){
		editType = "other";
		rows = $('#otherRole_time_manage_datagrid').datagrid('getRows'); 
		row = rows[index];
		name = row.roleName;
	}else{
		editType = "check";
		rows = $('#checkRole_time_manage_datagrid').datagrid('getRows');
		row = rows[index];
		name = row.userName + "-" + row.userCode;
	}
	ids[0] = row.id;
	
	showEditDialog(editType, ids, name, row.timeType, row.date, row.startTime, row.endTime);
}

/**
 * 批量修改按钮
 * 只有初审员有批量修改
 * 
 * @returns
 */
function editRecord_batch(){
	var rows = $('#checkRole_time_manage_datagrid').datagrid('getSelections');//选中的行
	if(rows.length == 0){
		$.info("提示", "请选择要修改的记录!", "warning");
		return;
	}
	
	var ids = new Array();
	var editType = "check";
	var dateType = new Array(rows.length);
	var name = "";
	for (var i = 0; i < rows.length; i++) {
		dateType[i] = rows[i].timeType + rows[i].date;
		ids[i] = rows[i].id;
		name = name + rows[i].userName+"-"+rows[i].userCode + "、";
	}
	
	//校验勾选记录中，日期是否为同一值（同为工作日、同为节假日、或同为某一日期）
	for (var i = 0; i < dateType.length; i++) {
		if(dateType[0] != dateType[i]) {
			$.info("提示", "请勾选同一日期类型或同一日期进行修改!", "warning");
		    return;
	    }
	}
	
	showEditDialog(editType, ids, name.substring(0,name.length-1), rows[0].timeType, rows[0].date, "09:00:00", "18:00:00");
}


/**
 * 弹出修改框
 * 
 * @returns
 */
function showEditDialog(editType, ids, name, timeType, date, startTime, endTime){
	if(timeType == '2'){
		timeType = "特殊日期";
    }else if(timeType == '1'){
    	timeType = '节假日';
    	date = "无";
    }else if(timeType == '0'){
    	timeType = '工作日';
    	date = "无";
    }
	
	if(editType == "check"){
		$("#checkRole_users_edit").html(name);
		$("#checkRole_timeType").html(timeType);
		$("#checkRole_date_edit").html(date);
		$('#checkRole_startTime_edit').timespinner('setValue', startTime);
		$('#checkRole_endTime_edit').timespinner('setValue', endTime);
		
		$("#check_role_edit_Dialog").removeClass("display_none").dialog({
			title : "修改",
			modal : true,
			width : 450,
			height : 250,
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					editRecord(editType, ids);
				}
			}, {
				text : '关闭',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#check_role_edit_Dialog").dialog("close");
				}
			} ],
			onClose : function() {
				$("#check_role_edit_Dialog").find("form").form("reset");
			}
		});
	}else{
		$("#otherRole_role_edit").html(name);
		$("#otherRole_timeType").html(timeType);
		$("#otherRole_date_edit").html(date);
		$('#otherRole_startTime_edit').timespinner('setValue', startTime);
		$('#otherRole_endTime_edit').timespinner('setValue', endTime);
		
		$("#other_role_edit_Dialog").removeClass("display_none").dialog({
			title : "修改",
			modal : true,
			width : 450,
			height : 250,
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					editRecord(editType, ids);
				}
			}, {
				text : '关闭',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#other_role_edit_Dialog").dialog("close");
				}
			} ],
			onClose : function() {
				$("#other_role_edit_Dialog").find("form").form("reset");
			}
		});
	}
}

/**
 * 修改提交
 * @param ids
 * @returns
 */
function editRecord(editType, ids){
	var validate = false;
	var params = new Object();
	if(editType == "check"){
		validate = $("#check_role_edit_Form").form('validate');
		params = $("#check_role_edit_Form").serializeObject();
	}else{
		validate = $("#other_role_edit_Form").form('validate');
		params = $("#other_role_edit_Form").serializeObject();
	}
	params.ids = ids.join("-");
	console.log(JSON.stringify(params));
	
	if(validate){
		if(params.endTime < params.startTime){
			$.info("提示", "结束时间不能小于开始时间!", "error");
			return;
		}
		
		post(ctx.rootPath() + "/timeManage/batchUpdate", params, null, function(data) {
			if (data.success) {
				$.info("提示", "修改成功!");
				if(editType == "check"){
					$("#check_role_edit_Dialog").dialog("close");
					$("#check_role_edit_Dialog").find("form").form("reset");
					$("#checkRole_time_manage_datagrid").datagrid('reload');
					$("#checkRole_time_manage_datagrid").datagrid('clearSelections');
				}else{
					$("#other_role_edit_Dialog").dialog("close");
					$("#other_role_edit_Dialog").find("form").form("reset");
					$("#otherRole_time_manage_datagrid").datagrid('reload');
					$("#otherRole_time_manage_datagrid").datagrid('clearSelections');
				}
			} else {
				$.info("提示", "修改失败!", "error");
			}
	    });
	}
}

function disableTime(id1,id2){
	$(id1).timespinner('setValue', "00:00:00");
	$(id2).timespinner('setValue', "00:00:00");
}
    
