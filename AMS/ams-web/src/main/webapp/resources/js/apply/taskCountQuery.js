var taskCountSearchOrNot = false;
$(function() {
	api = ctx.rootPath() + '/taskNumber/roleType';
	$.ajax({
		url : api,
		method : 'post',
		success : function(data) {
			var jsonData = new Array();
			jsonData.push({code : "",name : "请选择"}); 
			var arr = data.split(",");
			for (var i = 0; i < arr.length; i++) {
				if ("checkGroupLeader" == arr[i]) {
					jsonData.push({code : "checkGroupLeader",name : "初审组长"});
				} else if("check" == arr[i]){
					jsonData.push({code : "check",name : "初审"});
				} else if("finalCheck" == arr[i]){
					jsonData.push({code : "finalCheck",name : "终审"});
				}
			}
			$("#task_count_role_id").combobox("loadData",jsonData);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!','error');
		}
	});
	// 初始化任务列表datagrid
	initTaskCountQuyerDatagrid();
	
	$("#task_form_id").mouseover(function(){
		taskCountSearchOrNot = true;
	});
	$("#task_form_id").mouseout(function(){
		taskCountSearchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && taskCountSearchOrNot) {
			form_Query();
		}
	});
	
});
/**
 * 任务列表datagrid
 * 
 * @Author LuTing
 * @date 2017年3月7日
 */
function initTaskCountQuyerDatagrid() {
	$("#taskCountQuyer_taskList_datagrid").datagrid({
		url : ctx.rootPath() + '/taskNumber/getTaskList',
		title : '任务列表',
		striped : true,
		singleSelect : false,
		rownumbers : true,
//		idField : 'staffCode',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		remoteSort : true,
		multiSort : true,
		sortName : 'taskDefId,parentOrgName,orgName',
		sortOrder : 'asc,asc,asc',
		onSelect:function(index, row){
			//根据checkGroupLeader让该行不在被选中列表
            if (row.taskDefId == 'checkGroupLeader') {
            	$(this).datagrid("unselectRow",index);
            }
		},
		onSelectAll:function(rows){
			if (isNotNull(rows)) {
				for (var i = 0; i < rows.length; i++) {
					//根据checkGroupLeader让某些行不在被选中列表
	                if (rows[i].taskDefId == 'checkGroupLeader') {
	                	$(this).datagrid("unselectRow",i);
	                }
				}
			}
		},
		onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
	        if (data.rows.length > 0) {
	            //循环判断操作为新增的不能选择
	        	var count = 0;
	            for (var i = 0; i < data.rows.length; i++) {
	                //根据checkGroupLeader让某些行不可选
	                if (data.rows[i].taskDefId == 'checkGroupLeader') {
	                	$("input[type='checkbox']")[i + 1 - count].remove();
	                	count = count + 1;
	                }
	            }
	        }
	    },
		columns : [ [ {
			field : 'idField',
			checkbox : true
		}, {
			field : 'taskDefId',
			title : '审核节点',
			width : 145,
			formatter : function(value, data, index) {
				if (value == 'check') {
					return "初审";
				} else if(value == 'finalCheck'){
					return "终审";
				} else if(value == 'checkGroupLeader'){
					return "初审组长";
				}

			},
			sortable : false,
		}, {
			field : 'staffCode',
			title : '工号',
			width : 170,
			sortable : false,
		}, {
			field : 'staffName',
			title : '姓名',
			width : 170,
			sortable : false,
		}, {
			field : 'parentOrgName',
			title : '大组',
			width : 170,
			sortable : true,
			order : 'ASC'
		}, {
			field : 'orgName',
			title : '小组',
			width : 170,
			sortable : true,
			order : 'ASC'
		}, {
			field : 'currActivieTaskNum',
			title : '正常队列',
			width : 170,
			sortable : false,
		}, {
			field : 'currPriorityNum',
			title : '优先队列',
			width : 170,
			sortable : false,
		}, {
			field : 'currInactiveTaskNum',
			title : '挂起队列',
			width : 170,
			sortable : false,
		}, {
			field : 'ifAccept',
			title : '是否接单',
			width : 100,
			formatter : function(value, data, index) {
				if (value == 'N') {
					return "否";
				} else {
					return "是";
				}
			},
			sortable : false,
		}, {
			field : 'action',
			title : '操作',
			width : 220,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				if(data.taskDefId != 'checkGroupLeader'){
					if('Y' == data.ifAccept){
						action = '&nbsp;<a href="javaScript:void(0);" onclick=disabled("'+data.staffCode+'","'+data.taskDefId+'")><i class="fa fa-pencil" aria-hidden="true"></i>禁止接单</a>';
					}else{
						action = '&nbsp;<a href="javaScript:void(0);" onclick=opentask("'+data.staffCode+'","'+data.taskDefId+'")><i class="fa fa-pencil" aria-hidden="true"></i>开启接单</a>';
					}
				}
				return action;
			}
		} ] ]
	});
}
/**
 * 刷新任务列表datagrid
 * 
 * @Author LuTing
 * @date 2017年3月7日
 */
function loadTaskCountQuyerDatagrid() {
	$('#taskCountQuyer_taskList_datagrid').datagrid('load');
}

//查询
function form_Query(){
	$("#taskCountQuyer_taskList_datagrid").datagrid('load',$("#task_form_id").serializeObject());
}

/**
 *  批量禁止接单提示
 * 
 * @Author 
 * @date 2017年3月6日
 */
function batchDisabled() {
	var rows = $('#taskCountQuyer_taskList_datagrid').datagrid('getSelections');
	if (rows == "") {
		$.messager.alert('提示', "请选择一个人员！", 'info');
		return false;
	}else{
		var a ;
		var b ;
		for (var int = 0; int < rows.length; int++) {
			var s = rows[int].staffCode;
			var m = rows[int].taskDefId;
			 s=s+",";
			 m=m+",";
			 if(a == undefined){
				 a=s;
				 b=m;
			 }else{
				 a=a+s;
				 b=b+m;
			 }
		}
		batchUpdateTask(a,"N",b);
	}
	
}

/**
 * 批量开启接单提示
 * 
 * @Author 
 * @date 2017年3月6日
 */
function batchOpen() {
	var rows = $('#taskCountQuyer_taskList_datagrid').datagrid('getSelections');
	if (rows == "") {
		$.messager.alert('提示', "请选择一个人员！", 'info');
		return false;
	}else{
		var a ;
		var b ;
		for (var int = 0; int < rows.length; int++) {
			var s = rows[int].staffCode;
			var m = rows[int].taskDefId;
			 s=s+",";
			 m=m+",";
			 if(a == undefined){
				 a=s;
				 b=m;
			 }else{
				 a=a+s;
				 b=b+m;
			 }
		}
		batchUpdateTask(a,"Y",b);
	}
}

/**
 * 禁止接单提示
 * 
 * @Author 
 * @date 2017年3月6日
 */
var disFlag = true;
function disabled(personCode,personRole) {
	if(disFlag){
		disFlag = false;
		batchUpdateTask(personCode,"N",personRole);
	}
}

/**
 * 开启接单 提示
 * 
 * @Author LuTing
 * @date 2017年3月6日
 */
var openFlag = true;
function opentask(personCode,personRole) {
	if(openFlag){
		openFlag = false;
		batchUpdateTask(personCode,"Y",personRole);
	}
}


/**
 * 任务数数据更新
 * @Author 
 * @date 
 */
function batchUpdateTask(obj,is,personRole) {
	if("Y" == is){
		$("#opeon_button").linkbutton('disable');
	}else if("N" == is){
		$("#forbid_button").linkbutton('disable');
	}
	var timestamp = Date.parse(new Date());
	var params = {};
	params.userCode = obj;
	params.isaccept = is;
	params.personRole = personRole;
	api = ctx.rootPath() + '/taskNumber/batchUpdateTask' + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : 'json',
		method : 'post',
		data : params,
		success : function(data) {
			console.log(data);
			if (data.success) {
				$.info("提示", data.messages);
				loadTaskCountQuyerDatagrid();
			} else {
				$.info("提示", data.messages, "error");
			}
			openFlag = true;
			disFlag = true;
			if("Y" == is){
				$("#opeon_button").linkbutton('enable');
			}else if("N" == is){
				$("#forbid_button").linkbutton('enable');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!','error');
		}
	});
}
