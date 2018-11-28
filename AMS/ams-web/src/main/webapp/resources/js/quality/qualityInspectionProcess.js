    $(function () {

        // 延迟加载datagrid
        setTimeout(function () {
            qualityInspectionProcessTodoDatagrid("#qualityInspectionProcessTodo_datagrid");
            qualityInspectionProcessDoneDatagrid("#qualityInspectionProcessDone_datagrid");
        }, 50);

    });

    // 待处理查询
    $("#qualityInspectionProcessToDo_Query").click(function qualityInspectionProcessToDo_Query() {
    	if ($("#qualityInspectionProcessToDo_Query").parents("form").form("validate")) {//表单验证
    		// 所有textbox去前后空格
    		$("#qualityInspectionProcessToDo_queryForm").find('.easyui-textbox').each(function (index, element ) {
    			var $self = $(element);
    			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
    		});
    		var queryParams = $("#qualityInspectionProcessToDo_queryForm").serializeObject();
    		var ownId = $("#owningBranceIdTodo").combobox('getValues');
    		if(ownId["length"]!=0){
    			queryParams.owningBranceId = ownId.toString();
    		}
    		$('#qualityInspectionProcessTodo_datagrid').datagrid('options').queryParams = queryParams;
    		$("#qualityInspectionProcessTodo_datagrid").datagrid('load');
    	}
    });

    // 待处理重置
    $("#qualityInspectionProcess_todo_Reset").click(function qualityInspectionProcess_todo_Reset() {
        $('#qualityInspectionProcessToDo_queryForm').form('clear');
        $(this).parents("form").form('disableValidation');//移除表单里的required 属性 
    });

    function qualityInspectionProcessTodoDatagrid(id) {
        $(id).datagrid({
            url: ctx.rootPath() + "/qualityInspectionProcess/toDoPageList",
            striped: true,
            singleSelect: false,
            rownumbers: true,
            idField: 'id',
            pagination: true,
            fitColumns: true,
            nowrap:true,
            scrollbarSize: 0,
            remoteSort:false,
            rowStyler: function(index, row){
                if (row.ifTemporal){
                    return 'background-color:#97FFFF;';
                }

                if(row.ifRecheck){
                    return 'background-color:#FFA500;';
                }
            },
            toolbar: '#qualityInspectionProcessTodo_toolBarBtn',
            columns: [[{
                field: 'ck',
                checkbox: true
            }, {
                field: 'assignDate',
                title: '分派日期',
                width : 40,
                sortable: true,
                order: 'desc',
                formatter: function (value, data, index) {
                    if (value) {
                        return moment(value).format('YYYY-MM-DD');
                    } else {
                        return "";
                    }
                }
            }, {
                field: 'customerName',
                title: '客户姓名',
                sortable: true,
                width : 40
            }, {
                field: 'idNo',
                title: '身份证号',
                sortable: true,
                width : 40,
                formatter: function (value, data, index) {
                    if (value != null) {
                        return '*' + value.slice(-4);
                    }
                }
            }, {
                field: 'customerType',
                title: '客户类型',
                sortable: true,
                width : 40
            }, {
                field: 'owningBrance',
                title: '进件营业部',
                sortable: true,
                width : 40
            }, {
                field: 'sourceText',
                title: '申请来源',
                sortable: true,
                width : 40
            }, {
                field: 'loanNo',
                title: '申请件编号',
                sortable: true,
                width : 60
            }, {
                field: 'applyDate',
                title: '申请日期',
                width : 40,
                sortable: true,
                order: 'desc',
                formatter: function (value, data, index) {
                    if (value) {
                        return moment(value).format('YYYY-MM-DD');
                    } else {
                        return "";
                    }
                }
            }, {
                field: 'productTypeName',
                title: '贷款类型',
                sortable: true,
                width : 40
            }, {
                field: 'checkPersonName',
                title: '初审姓名',
                sortable: true,
                width : 40
            }, {
                field: 'finalPersonName',
                title: '终审姓名',
                sortable: true,
                width : 40
            }, {
                field: 'approveDate',
                title: '审批日期',
                sortable: true,
                width : 40,
                formatter: function (value, data, index) {
                    if (value) {
                        return moment(value).format('YYYY-MM-DD');
                    } else {
                        return "";
                    }
                }
            }, {
                field: 'approvalStatusText',
                title: '审批结果',
                sortable: true,
                width : 40
            }, {
                field: 'refuseReasonEmbed',
                title: '拒绝原因',
                sortable: true,
                width : 40,
                formatter: function (value, row, index) {
                    return "<label title=\""+ row.refuseReasonEmbed +"\" class=\"easyui-tooltip\">" + row.refuseReasonEmbed + "</label>";
                }
            }, {
                field: 'checkUserName',
                title: '质检人员',
                sortable: true,
                width : 40
            }, {
                field: 'checkUser',
                title: '质检人员',
                hidden: true
            }, {
                field: 'qualityCheckId',
                title: '操作',
                align: 'center',
                formatter: function (value, data, index) {
					var title = data.customerName+"质检";
					var flag = 'todo';
					var qualityUser = $('#qualityInspectionProcess_quality_qualityUser').val();
					if(data.checkUser != qualityUser){
						flag = 'todoOthers'					
					}
					return '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag+'\','+'\''+data.checkUser+'\');">质检</a>';
                
                }
            }]]
        });
    }
    // 已完成查询
    $("#qualityInspectionProcessDone_Query").click(function qualityInspectionProcessDone_Query() {
    	if ($("#qualityInspectionProcessDone_Query").parents("form").form("validate")) {
    		//文本框去空格
    		$("#qualityInspectionProcessDone_queryForm").find('.easyui-textbox').each(function (index, element ) {
    			var $self = $(element);
    			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
    		});
    		var queryParams = $("#qualityInspectionProcessDone_queryForm").serializeObject();
    		var ownId = $("#owningBranceIdDone").combobox('getValues');
    		if(ownId["length"]!=0){
    			queryParams.owningBranceId = ownId.toString();
    		}
    		var checkResults = $("#checkResultSelect").combobox('getValues');
    		if(checkResults["length"]!=0){
    			queryParams.checkResult = checkResults.toString();
    		}
    		
    		$('#qualityInspectionProcessDone_datagrid').datagrid('options').queryParams = queryParams;
    		$("#qualityInspectionProcessDone_datagrid").datagrid('reload');
    	}
    });

    // 已完成重置
    $("#qualityInspectionProcess_done_Reset").click(function qualityInspectionProcess_done_Reset() {
        $('#qualityInspectionProcessDone_queryForm').form('clear');
        $(this).parents("form").form('disableValidation');//移除表单里的required 属性 
    });

    function qualityInspectionProcessDoneDatagrid(id) {
        $(id).datagrid({
            url: ctx.rootPath() + "/qualityInspectionProcess/donePageList",
            striped: true,
            singleSelect: false,
            rownumbers: true,
            idField: 'id',
            pagination: true,
            fitColumns: true,
            scrollbarSize: 0,
            remoteSort: false,
            toolbar: '#qualityInspectionProcessDone_toolBarBtn',
            rowStyler: function(index, row){
                if(row.checkStatus == '1'){
                    return 'background-color:#FFA500;';
                }
            },
            columns: [[{
                field: 'ck',
                checkbox: true
            }, {
                field: 'endDate',
                title: '完成日期',
                width : 30,
                sortable: true,
                order: 'desc',
                formatter: function (value, data, index) {
                    if (value) {
                        return moment(value).format('YYYY-MM-DD');
                    } else {
                        return "";
                    }
                }
            }, {
                field: 'customerName',
                title: '客户姓名',
                sortable: true,
                width : 40
            }, {
                field: 'idNo',
                title: '身份证号',
                sortable: true,
                formatter: function (value, data, index) {
                    if (value != null) {
                        return '*' + value.slice(-4);
                    }
                }
            }, {
                field: 'customerType',
                title: '客户类型',
                sortable: true,
                width : 30
            }, {
                field: 'owningBrance',
                title: '进件营业部',
                sortable: true,
                width : 40
            }, {
                field: 'sourceText',
                title: '申请来源',
                sortable: true,
                width : 40
            }, {
                field: 'loanNo',
                title: '申请件编号',
                sortable: true,
                width : 60
            }, {
                field: 'productTypeName',
                title: '贷款类型',
                sortable: true,
                width : 40
            },{
                field: 'checkPersonName',
                title: '初审姓名',
                sortable: true,
                width : 40
            }, {
                field: 'finalPersonName',
                title: '终审姓名',
                sortable: true,
                width : 40
            }, {
                field: 'approveDate',
                title: '审批日期',
                sortable: true,
                width : 40,
                formatter: function (value, data, index) {
                    if (value) {
                        return moment(value).format('YYYY-MM-DD');
                    } else {
                        return "";
                    }
                }
            }, {
                field: 'approvalStatusText',
                title: '审批结果',
                sortable: true,
                width : 40
            }, {
                field: 'refuseReasonEmbed',
                title: '拒绝原因',
                sortable: true,
                width : 40,
                formatter: function (value, row, index) {
                    return "<label title=\""+ row.refuseReasonEmbed +"\" class=\"easyui-tooltip\">" + row.refuseReasonEmbed + "</label>";
                }
            },{
                field: 'checkUserName',
                title: '质检人员',
                sortable: true,
                width : 40
            }, {
                field: 'checkResultText',
                sortable: true,
                title: '差错类型'
            }, {
                field: 'qualityCheckId',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var action = '';
                    action += '<a href="javaScript:qualityReceiveDialog(\'' + row.customerName + '质检详情\',' + '\'' + row.loanNo+'\',' + '\'' + value + '\',' + '\'done\');">详情</a>';
                    if(row.ifEditor == true){
                    	action += ' | ';
	                    action += '<a href="javaScript:qualityReceiveDialog(\'' + row.customerName + '质检详情\',' + '\'' + row.loanNo+'\',' + '\'' + value + '\',' + '\'updateDone\');">编辑</a>';
                    }
                    return action;
                }
            }]]
        });
    }

    // 弹出质检详情页面
     var markQualityHandleSituationLoanNo = {};
    function qualityReceiveDialog(title,loanNo,value,flag,checkUser){
    	if(loanNo in markQualityHandleSituationLoanNo){
    		markQualityHandleSituationLoanNo[loanNo].focus();
    	}else{
    		markQualityHandleSituationLoanNo[loanNo] = jDialog.open({
                url:ctx.rootPath() + "/qualityControlDesk/qualityReceive?flag="+flag+"&loanNo="+loanNo+"&qualityCheckId="+value+"&checkUser="+checkUser,
                width: 1400,
                height: 800
            });
    	}
    }

//质检处理情况
//质检日志
function qualityLogShow(id,loanNo) {
    $(id).datagrid({
        url: ctx.rootPath() + "/qualityInspectionProcess/findQualityLogList?loanNo="+loanNo,
        rownumbers: true,
        pagination: true,
        scrollbarSize: 0,
        columns: [[ {
            field: 'link',
            title: '环节',
            width:120
        }, {
            field: 'operation',
            title: '操作',
            width:120
        }, {
            field: 'createdBy',
            title: '操作人',
            width:120
        }, {
            field: 'createdDate',
            title: '操作时间',
            width:150,
            formatter: function (value, data, index) {
                if (value) {
                    return moment(value).format('YYYY-MM-DD hh:mm:ss');
                } else {
                    return "";
                }
            }
        }, {
            field: 'remark',
            title: '备注',
            width:240
        }]]
    });
}
//修改信审员
function showApprovalPerson(qualityCheckId) {
    $("#approvalPerson_dialog").removeClass("display_none").dialog({
        title: '修改信审人员',
        closed: false,
        modal: true,
        buttons: [{
            text: '确定',
            iconCls: 'fa fa-arrow-up',
            handler: function () {
                var api = ctx.rootPath() + '/qualityInspectionProcess/modifyApprovePerson';
                var params = $('#approvalPerson_Form').serializeObject();
                params.id = qualityCheckId;
                var callback = function (data) {
                    if (data.type == 'SUCCESS') {
                        $.info("提示", "修改成功!", "info", 2000);
                    } else {
                        $.info("提示", "修改失败!", "info", 2000);
                    }
                    $("#approvalPerson_dialog").dialog("close");
                    $("#qualityInspectionProcessDone_datagrid").datagrid("reload");
                };
                var error = function (XMLHttpRequest, textStatus, errorThrown) {
                    console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
                };
                post(api, params, 'json', callback, error);
            }
        }, {
            text: '返回',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#approvalPerson_dialog").dialog("close");
            }
        }]
    });
}
/**
 * 日志详情
 *
 * @Author LuTing
 * @date 2017年3月10日
 */
function showLog(loanNo) {
    qualityLogShow("#qualityLogShowTable",loanNo);
    $("#approvalLog_dialog").removeClass("display_none").dialog({
        title: '日志详情',
        modal: true,
        height: 600,
        width: 800,
        buttons: [{
            text: '返回',
            iconCls: 'fa fa-reply',
            handler: function () {
                $("#approvalLog_dialog").dialog("close");
            }
        }]
    });
}
/**
 * @Desc: 待处理列表导出
 * @Author: phb
 * @Date: 2017/4/25 16:05
 */
function exportToDoExcel() {
    var queryParams = $("#qualityInspectionProcessToDo_queryForm").serializeObject();
    var url = encodeURI(ctx.rootPath() + "/qualityInspectionProcess/exportToDoExcel?queryParams=" + JSON.stringify(queryParams));
    window.open(url);
}
/**
 * @Desc: 已完成列表导出
 * @Author: phb
 * @Date: 2017/4/25 16:05
 */
function exportDoneExcel() {
    var queryParams = $("#qualityInspectionProcessDone_queryForm").serializeObject();
    var url = encodeURI(ctx.rootPath() + "/qualityInspectionProcess/exportDoneExcel?queryParams=" + JSON.stringify(queryParams));
    window.open(url);
}
/**
 * @Desc: 自动发起符合条件的申请件反馈
 * @Author: phb
 * @Date: 2017/5/8 17:21
 */
function qualityFeedbackJobExecute(){
    var api = ctx.rootPath() + '/qualityInspectionProcess/qualityFeedbackJobExecute';
    var callback = function (data) {
        if (data.type=='SUCCESS') {
            $.info("提示",data.firstMessage,"info", 2000);
        }else{
            $.info("提示",data.firstMessage,"info", 2000);
        }
        //重新加载已完成列表
        $("#qualityInspectionProcessDone_datagrid").datagrid("reload");
    }
    var error = function (XMLHttpRequest, textStatus, errorThrown) {
        console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
    };
    post(api, null, 'json', callback, error);
}

/**
 * 弹出质检作业页面qualityReceive 
 * @param value
 */
//function qualityReceiveDialog(title,loanNo,value,flag){
//	qualityControlDesk_qualityReceive_dialog = ctx.dialog({
//		title : title,
//		modal : true,
//		width : 1200,
//		height : 900,
//		resizable: true,
//		collapsible:true,
//		modal:false,
//		href : ctx.rootPath()+"/qualityControlDesk/qualityReceive?flag="+flag+"&loanNo="+loanNo+"&qualityCheckId="+value
//	});
//}

//关闭
function  closeBatch(){
	var rowInfo = $("#qualityInspectionProcessDone_datagrid").datagrid('getSelected');
	if (!rowInfo) {
       	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
       	return;
    }
	
	$.messager.confirm("提示","确认关闭吗?关闭后申请件将不再显示",function(isOrNo){
		if(isOrNo){
			var rows = $('#qualityInspectionProcessDone_datagrid').datagrid('getSelections');  
	        var saveDataArry = [];
        	for(var i=0; i<rows.length; i++){
//        		if('quality_recheck' == rows[i].status || 'quality_recheck_wait' == rows[i].status){
//        			$.messager.alert('提示信息',  "申请复核案件无法关闭！！", 'info');
//        			return false;
//        		} 	
        		saveDataArry.push(rows[i].id);
 	        }  
			$.ajax({ 
	        	type:"POST", 
	        	url:ctx.rootPath()+'/qualityInspectionProcess/close',
	        	dataType:"json",      
	        	contentType:"application/json",               
	        	data:JSON.stringify(saveDataArry), 
	        	success:function(data){ 
	        		if(data.type=='SUCCESS'){
	        			$.messager.alert("提示", data.messages[0],"info"); 
	        		}else{
	        			$.messager.alert("提示","关闭失败！！","error");
	        		}
	        		$('#qualityInspectionProcessDone_datagrid').datagrid('clearChecked');
	        		$('#qualityInspectionProcessDone_datagrid').datagrid('reload');  
	        	} 
	        });
		 }
	});
	event.stopPropagation();
}

/**
 * 日期选择事件
 */
$('#qualityInspectionProcessTodo_inStartDate').datebox({
	onSelect: function(date){
		$('#qualityInspectionProcessTodo_inEndDate').datebox({
			required:true
		});
	}
});
$('#qualityInspectionProcessDone_inStartDate').datebox({
	onSelect: function(date){
		$('#qualityInspectionProcessDone_inEndDate').datebox({
			required:true
		});
	}
});
