$(function() {
	cancelReason_InitData();
	$('#cancelReason_Query_Form').form('clear');
});
 
//初始化下拉框
function cancelReason_InitCombobox(title,data){
	if("新建"==title){
		$('#cancelReason_AddOrUpdate_ParentCode').combobox({
			   url:ctx.rootPath() + "/cancelReason/parentCode?reasonKind=CANCEL",
			   valueField:'reasonCode',
			   textField:'reasonName',
			   width:214
		});
	}else{
		$('#cancelReason_AddOrUpdate_ParentCode').combobox({
			   url:ctx.rootPath() + "/cancelReason/parentCode?reasonKind=CANCEL&reasonCode="+data.reasonCode,
			   valueField:'reasonCode',
			   textField:'reasonName',
			   width:214
		});
	}
	
}
    
//加载
function cancelReason_InitData() {
	$("#cancelReason_datagrid").datagrid({
		url : ctx.rootPath() + "/cancelReason/pageList?reasonKind=CANCEL",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#cancelReason_toolBarBtn',
		onDblClickRow:function(rowIndex, rowData){
	    	var api = ctx.rootPath() + '/cancelReason/findById';
			var params = {
					id : rowData.id
			};
			var callback = function(data){
				cancelReason_ShowDialog("修改", data);
			};
			var error = function (XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		    };
			post(api, params, 'json', callback, error);
	    },
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'reasonCode',
			title : '原因编码',
			width : 155,
		}, {
			field : 'reasonName',
			title : '原因名称',
			width : 155,
		}, {
			field : 'parentCode',
			title : '父级编码',
			width : 155,
		}, {
			field : 'submitDays',
			title : '限制在提交天数',
			width : 155,
		},{
			field : 'reasonType',
			title : '原因类型',
			width : 155,
			formatter:function(value){
		        if(value=='R00')
		            return '回退信审';
		        else if(value=='R01')
		            return '回退门店';
		   	}
		}, {
			field : 'lastModifiedBy',
			title : '最后修改人',
			width : 155,
		}, {
			field : 'lastModifiedDate',
			title : '最后修改时间',
			width : 155,
			sortable : true,
			order : 'desc',
			formatter : function(value, data, index){
				return moment(value).format('YYYY-MM-DD');
			}
		}, {
			field : 'status',
			title : '是否启用',
			width : 243,
			formatter:function(value){
		        if(value==0)
		            return '是';
		        else
		            return '否';
		   	}
		} ] ]
	});
}

//重置
$("#cancelReason_Reset").click(
	function cancelReason_Reset(){
		$('#cancelReason_Query_Form').form('clear');
	}
);

 //条件查询 
$("#cancelReason_Query").click(
    function cancelReason_Query(){
		$("#cancelReason_datagrid").datagrid('load',$("#cancelReason_Query_Form").serializeObject());
    }
);

/*=============新建===========*/
//Open
$("#cancelReason_Dialog_Open").click(
	function cancelReason_Dialog_Open(){
		$("#cancelReason_AddOrUpdate_ReasonCode").textbox({readonly:false});
		cancelReason_ShowDialog("新建");
	}
);

function cancelReason_ShowDialog(title, data){
	$("#cancelReason_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : title,
		width : 800,
		height : 300,
		closed : false,
		modal : true,
		onBeforeOpen : function(){
			$('#cancelReason_AddOrUpdate_Form').form('clear');
			if("新建"==title){
				cancelReason_InitCombobox(title,data);
			}else{
				cancelReason_InitCombobox(title,data);
				$("#cancelReason_AddOrUpdate_Form").form("load", data);
				$("#cancelReason_AddOrUpdate_ReasonCode").textbox({readonly : true}); 
			}
		},
		buttons : [{
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function(){
				cancelReason_Save();
			}
		},{
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function(){
				$("#cancelReason_AddOrUpdate_dialog").dialog("close");
			}
		}]
	});
}

//Save
function cancelReason_Save(){
	var title = $("#cancelReason_AddOrUpdate_dialog").dialog('options').title;
	if(title=='新建'){
		var  validate = $("#returnReason_AddOrUpdate_Form").form('validate');
		if(validate){
			var api = ctx.rootPath() + '/cancelReason/save';
			var params = $('#cancelReason_AddOrUpdate_Form').serializeObject();
			params.reasonKind='CANCEL';
			var callback = function(data){
				if(data.status=="true"){
					$.info("提示","保存成功!","info",2000);
				}else if(data.status=="repeated"){
					$.messager.alert('警告', "原因编码已存在！", 'warning');
				}else{
					$.info("提示","保存失败!","info",2000);
				}
				$("#cancelReason_AddOrUpdate_dialog").dialog("close");
				$('#cancelReason_datagrid').datagrid('reload');
			};
			var error = function (XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		    };
		    post(api, params, 'json', callback, error);
		}
	}else if(title=='修改'){
		var  validate = $("#cancelReason_AddOrUpdate_Form").form('validate');
		if(validate){
			var api = ctx.rootPath() + '/cancelReason/update';
			var params = $('#cancelReason_AddOrUpdate_Form').serializeObject();
			params.reasonKind='CANCEL';
			var callback = function(data){
				if(data.status=="true"){
					$.info("提示","修改成功!","info",2000);
				}else{
					$.info("提示","修改失败!","info",2000);
				}
				$("#cancelReason_AddOrUpdate_dialog").dialog("close");
				$('#cancelReason_datagrid').datagrid('reload');
			};
			var error = function (XMLHttpRequest, textStatus, errorThrown) {
				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		    };
		    if(haveChildren()==true && isNotNull(params.parentCode)){//本身是父节点，想成为子节点，提示是否强制执行
		    	console.info("11111");
				$.messager.confirm("提示","有子节点,确定强制修改吗？",function(isOrNo){
					  if(isOrNo){
						  post(api, params, 'json', callback, error);
					  }
				});
			}else{
				console.info("22222");
				post(api, params, 'json', callback, error);
			}
		}
	}
}

//是否当前记录是否有子节点（true：有   ，false：没有）
function haveChildren(){
	var flag = false;
	var params = $('#cancelReason_AddOrUpdate_Form').serializeObject();
    $.ajax({  
        type : "post",  
        url : ctx.rootPath()+'/cancelReason/haveChlidren',  
        data : params,  
        dataType : 'json',
        async : false,//取消异步  
        success : function(data){
        	if(data.status=="haveChildren"){
    			flag = true;
    		}else{
    			flag = false;
    		}
        }  
    });
    return flag;
}

/*=============新建===========*/

//删除
$("#cancelReason_Delete").click(
	function cancelReason_Delete(){
		var rowInfo = $("#cancelReason_datagrid").datagrid('getSelected');
		if (!rowInfo) {
           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
           	return;
        }
		var rows = $('#cancelReason_datagrid').datagrid('getSelections');  
        var ids = "";
        for(var i=0; i<rows.length; i++){  
            ids+=rows[i].id+",";
        }  
        ids = ids.substr(0, ids.length - 1);
		var api = ctx.rootPath() + '/cancelReason/delete';
		var params = {
				"ids" : ids
		};
		var callback = function(data){
			  if(data.status=='true'){
				  $.info("提示",data.deletedId+"条删除成功!","info",2000);
			  }else if(data.status=='haveChildren'){
				  $.messager.alert('提示', "请先删除子节点！", 'info');
			  }else{
				  $.info("提示","删除失败!","info",2000);
			  }
			  $('#cancelReason_datagrid').datagrid('clearChecked');
			  $('#cancelReason_datagrid').datagrid('reload'); 
		};
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		};
		$.messager.confirm("提示","确认删除吗？",function(isOrNo){
			  if(isOrNo){
				  post(api, params, 'json',callback, error);
			  }
		});
	}
);
