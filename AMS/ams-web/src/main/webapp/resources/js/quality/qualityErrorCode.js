	$(function() {
		qualityQueryDatagrid("#errorCode_datagrid");
	});
	//查询
	$("#errorQuery_Query").click(
	    function qualityQuery_Query(){
	    	//textbox去空格
	    	$("#errorCode_queryForm").find('.easyui-textbox').each(function (index, element ) {
				var $self = $(element);
				$($self).textbox('setValue', $.trim($self.textbox('getValue')));
			});
			$("#errorCode_datagrid").datagrid('load',$("#errorCode_queryForm").serializeObject());
	    }
	);
	//重置
	$("#errorQuery_reset").click(
	    function qualityQuery_reset(){
	    	$('#errorCode_queryForm').form('clear');
	    }
	);
	function qualityQueryDatagrid(id) {
		$(id).datagrid({
			url : ctx.rootPath()+"/qualityErrorCode/pageList",
			striped : true,
			singleSelect : false,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			toolbar:'#errorCode_toolBarBtn',
			onDblClickRow : function(rowIndex, rowData) {
				var api = ctx.rootPath() + '/qualityErrorCode/findById';
				var params = {
					id : rowData.id
				};
				var callback = function(data) {
					errorCode_ShowDialog('修改', data);
				};
				var error = function(XMLHttpRequest, textStatus, errorThrown) {
					console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				};
				post(api, params, 'json', callback, error);
			},
			columns : [ [
			             {
			field : 'ck',
			checkbox : true
		},{
				field : 'code',
				title : '差错代码',
				width : 40,
			}, {
				field : 'lastModifiedBy',
				title : '最后修改人',
				width : 40,
			},
			{
				field : 'lastModifiedDate',
				title : '最后修改时间',
				width : 40,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD hh:mm:ss');
					}
				}
			},{
				field : 'status',
				title : '是否开启',
				width : 40,
				formatter : function(value) {
					if (value == '0')
						return '是';
					else
						return '否';
				}
			}
			] ]
		});
	}
	// 新建
	// Open
	$("#errorCode_Dialog_Open").click(function errorCode_Dialog() {
		$("#code").textbox({
			readonly : false
		});
		$("#status").combobox({
			disabled : false
		});
		errorCode_ShowDialog('新建');
	});
	
	$("#errorCode_Delete").click(
			function checkedPersonnelManagement_Delete(){
				var rowInfo = $("#errorCode_datagrid").datagrid('getSelected');
				if (!rowInfo) {
		           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
		           	return;
		        }
				var rows = $('#errorCode_datagrid').datagrid('getSelections');  
		        var ids = "";
		        for(var i=0; i<rows.length; i++){  
		            ids+=rows[i].id+",";
		        }  
		        ids = ids.substr(0, ids.length - 1);
				var api = ctx.rootPath()+'/qualityErrorCode/delete';
				var params = {
						"ids" : ids
				};
				var callback = function(data){
					if(data.type=='true'){
					  }else{
						  $.info("提示","删除成功!","info",2000);
					  }
					  $('#errorCode_datagrid').datagrid('clearChecked');
					  $('#errorCode_datagrid').datagrid('reload');  
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
	
	function errorCode_ShowDialog(title, data) {
		$("#errorCode_AddOrUpdate_dialog").removeClass("display_none").dialog({
			title : title,
			closed : false,
			modal : true,
			onBeforeOpen : function() {
				$('#errorCode_AddOrUpdate_Form').form('clear');
//				if(title=="修改"){
//					$('#code').textbox('textbox').attr('readonly',true); 
//				}
				$("#errorCode_AddOrUpdate_Form").form("load", data);
			},
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					errorCode_Save(data);
				}
			}, {
				text : '返回',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#errorCode_AddOrUpdate_dialog").dialog("close");
				}
			} ]
		});
	}
	
	// SaveOrUpdate
	function errorCode_Save(data) {
			var title = $("#errorCode_AddOrUpdate_dialog").dialog('options').title;
			var validate = $("#errorCode_AddOrUpdate_Form").form('validate');
			if (title == '新建'){
			if (validate) {
				var api = ctx.rootPath() + '/qualityErrorCode/save';
				var params = $('#errorCode_AddOrUpdate_Form').serializeObject();
				var callback = function(data) {
					if (data.status == "true") {
						$.info("提示", "保存成功!", "info", 2000);
					} else if (data.status == "repeated") {
						$.messager.alert('警告', "原因编码已存在！", 'warning');
					} else {
						$.info("提示", "保存失败!", "info", 2000);
					}
					$("#errorCode_AddOrUpdate_dialog").dialog("close");
					$('#errorCode_datagrid').datagrid('reload');
				};
				var error = function(XMLHttpRequest, textStatus, errorThrown) {
					console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				};
				post(api, params, 'json', callback, error);
			}
			
			}else if (title == '修改') {
					var oldCode = data["code"];
					var validate = $("#errorCode_AddOrUpdate_Form").form('validate');
					if (validate) {
						var api = ctx.rootPath() + '/qualityErrorCode/update/'+oldCode;
						var params = $('#errorCode_AddOrUpdate_Form').serializeObject();
						var callback = function(data) {
							if (data.status == "true") {
								$.info("提示", "修改成功!", "info", 2000);
							} else if (data.status == "repeated") {
								$.messager.alert('警告', "原因编码已存在！", 'warning');
							}else{
								$.info("提示", "修改失败!", "info", 2000);
							}
							$("#errorCode_AddOrUpdate_dialog").dialog("close");
							$('#errorCode_datagrid').datagrid('reload');
						};
						var error = function(XMLHttpRequest, textStatus, errorThrown) {
							console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
						};
							post(api, params, 'json', callback, error);
					}
				}
		} 