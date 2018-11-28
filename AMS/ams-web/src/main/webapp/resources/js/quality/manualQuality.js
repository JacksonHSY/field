	//@ sourceURL=manualQuality.js
	$(function() {
		// 延迟加载datagrip
		setTimeout(function () {
			manualDispatchDatagrid("#manualDispatch_datagrid");
		}, 100);
	});
	//手工分派查询
	$("#manualDispatch_Query").click(
	    function manualDispatch_Query(){
	    	if ($("#manualDispatch_Query").parents("form").form("validate")) {
	    		//textbox去空格
		    	$("#manualDispatch_queryForm").find('.easyui-textbox').each(function (index, element ) {
	    			var $self = $(element);
	    			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
	    		});
	    		var queryParams = $("#manualDispatch_queryForm").serializeObject();
	    		var ownid = queryParams.owningBranceId;
	    		if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
	    			queryParams.owningBranceId =ownid;
	    		}else{
	    			var ownids = ownid.join(",");
	    			queryParams.owningBranceId = ownids;
	    		}
	    		$("#manualDispatch_datagrid").datagrid('load',queryParams);
	    	}
	    }
	);
	//手工分派重置
	$("#manualDispatch_Reset").click(
	    function manualDispatch_Reset(){
	    	$("#manualDispatch_queryForm").form('clear');
	    	$(this).parents("form").form('disableValidation');//移除表单里的required 属性 
	    }
	);
	
	//手工导入
	function uploadExcel(){
		//获取导入的excel，对文件格式进行校验
		var fileName= $('#filename').textbox('getValue');
			if(fileName==null || fileName==''){
				$.messager.alert('提示信息',  "请选择文件！", 'info');
			}else{
				var fileType=/\.[^\.]+$/.exec(fileName);
				if(fileType!=".xlsx"){
					$.messager.alert('提示信息',  "请选择excle文件！", 'info');
				}else{
					$("#files").form("submit");
					setTimeout(function(){
						$("#manualQuality_dispatch_import").addClass("display_none").dialog('close');
						$('#manualDispatch_datagrid').datagrid('reload'); 
					}, 1000);
				}
			}
	}
	//手工导入删除 
	function deleteFiles(){
		//获取导入的excel，对文件格式进行校验
		var fileName= $('#delfilename').textbox('getValue');
			if(fileName==null || fileName==''){
				$.messager.alert('提示信息',  "请选择文件！", 'info');
			}else{
				var fileType=/\.[^\.]+$/.exec(fileName);
				if(fileType!=".xlsx"){
					$.messager.alert('提示信息',  "请选择excle文件！", 'info');
				}else{
					$('#delfiles').submit();
					setTimeout(function(){
						$('#manualDispatch_datagrid').datagrid('reload'); 
						$("#manualQuality_dispatch_delete").addClass("display_none").dialog('close');
					}, 1000);
				}
			}
	}
	$("#manualDispatch_delete").click(
			function manualDispatch_import(){
				$("#manualQuality_dispatch_delete").removeClass("display_none").dialog({
					title : "导入删除",
					modal : true,
					width : 400,
					height : 247,
				});
			}	
	);
	$("#manualDispatch_import").click(
		function manualDispatch_imports(){
			$("#manualQuality_dispatch_import").removeClass("display_none").dialog({
				title : "手工分派导入",
				modal : true,
				width : 400,
				height : 247,
			});
		}	
	);
	//手工分派列表导出
	$("#manualDispatch_export").click(
		function manualDispatch_export(){
			var queryParams = $("#manualDispatch_queryForm").serializeObject();
			var ownid = queryParams.owningBranceId;
	    	 if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
	    		 queryParams.owningBranceId =ownid;
	    	 }else{
	    		 var ownids = ownid.join(",");
	    		 queryParams.owningBranceId = ownids;
	    	 }
	    	 window.location.href=ctx.rootPath()+"/manualQuality/exportManualDispatchList?"+$.param(queryParams);
		}
	);
	
	
	//手工分派关闭
	function  dispatchDispatchClose(value,data){
		var rowInfo = $("#manualDispatch_datagrid").datagrid('getSelected');
		if (!data && !rowInfo) {
           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
           	return;
        }
		
		$.messager.confirm("提示","确认关闭吗?关闭后申请件将不再显示",function(isOrNo){
			  if(isOrNo){
				  dispatchClose(value,data);
			  }
		});
		event.stopPropagation();
	}
	
	function  dispatchClose(value,data){
		var rows = $('#manualDispatch_datagrid').datagrid('getSelections');  
        var saveDataArry = [];
		if(!data){
        	 for(var i=0; i<rows.length; i++){ 
        		 saveDataArry.push(rows[i].id);
 	        }  
        }else{
        	saveDataArry.push(data.id);
        }
		
		$.ajax({ 
        	type:"POST", 
        	url:ctx.rootPath()+'/manualQuality/close',
        	dataType:"json",      
        	contentType:"application/json",               
        	data:JSON.stringify(saveDataArry), 
        	success:function(data){ 
        		if(data.type=='SUCCESS'){
        			$.messager.alert("提示", data.messages[0],"info"); 
        		}else{
        			$.messager.alert("提示","关闭失败！！","error");
        		}
        		$('#manualDispatch_datagrid').datagrid('clearChecked');
        		$('#manualDispatch_datagrid').datagrid('reload');  
        	} 
        });
		
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		};
	}
	
	//手工分派
	function dispatch(value,data){
		var rowInfo = $("#manualDispatch_datagrid").datagrid('getSelected');
		
		if (!data && !rowInfo) {
           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
           	return;
        }
		var rows = $('#manualDispatch_datagrid').datagrid('getSelections');  
		
//		if(!data){
//			for(var i=0; i<rows.length; i++){ 
//				if('quality_recheck' == rows[i].status){
//					$.info('提示信息',  "申请复核案件不能分派！！", 'info');
//					return false;
//				}
//			}
//		}
		var saveDataArry = [];
		var message = '';
		if(!data){
        	 for(var i=0; i<rows.length; i++){ 
        		 if('quality_recheck' == rows[i].status || 'quality_recheck_wait' == rows[i].status){
 					$.info('提示信息',  "申请复核案件不能分派！！", 'info');
 					return false;
 				 }
        		 if('quality_temporary_save' == rows[i].status){
        			 message = message+','+rows[i].loanNo;
        		 }
        		 saveDataArry.push(rows[i].id);
 	         }  
        }else{
        	if('quality_temporary_save' == data.status){
        		message = ','+data.loanNo;
        	}
        	saveDataArry.push(data.id);
        }
		
		if(message != ''){
			$.messager.confirm("提示",message.substr(1)+"为暂存件，确定分派吗",function(isOrNo){
				  if(isOrNo){
					  manualDispatch(saveDataArry);
				  }else{
					  return;
				  }
			});
		}else{
			manualDispatch(saveDataArry);
		}
	}
	
	function  manualDispatch(saveDataArry){
		$("#manualDispatch_person_dialog").removeClass("display_none").dialog({
			title : "手工分派",
			modal : true,
			width : 550,
			height : 250,
			buttons : [ {
				text : '确定',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					$.ajax({ 
			        	type:"POST", 
			        	url:ctx.rootPath()+'/manualQuality/updateCheckUser?checkUser='+$('#manualDispatchTo').combobox('getValue'),
			        	dataType:"json",      
			        	contentType:"application/json",               
			        	data:JSON.stringify(saveDataArry), 
			        	success:function(data){ 
			        		if(data.type=='SUCCESS'){
			        			$.messager.alert("提示", data.messages[0],"info"); 
			        		}else{
			        			$.messager.alert("提示",data.messages[0],"error");
			        		}
			        		$("#manualDispatch_person_dialog").dialog("close");
			        		$('#manualDispatch_datagrid').datagrid('clearChecked');
			        		$('#manualDispatch_datagrid').datagrid('reload');  
			        	} 
			        });
				}
			} ]
		});
		
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		};
	}
	
	function manualDispatchDatagrid(id) {
		$(id).datagrid({
			url : ctx.rootPath()+"/manualQuality/manualDispatchList",
			striped : true,
			singleSelect : false,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			remoteSort: false,
			toolbar : '#manualDispatch_toolBarBtn',
			rowStyler: function(index, row){
                if('quality_recheck' == row.status){
                    return 'background-color:#66CC00;';
                }
            },
	/*		onBeforeCheck: function (index,row) {
				if('quality_recheck' == row.status){
	                return false;
	            } else {
	            	return true;
	            }
			},
			onCheckAll: function(rows){
				$.each(rows,function(ind,val){
					if('quality_recheck' == val.status){
		               $(id).datagrid("uncheckRow",ind);
					}
				});
			},*/
			columns : [ [ {
				field : 'ck',
				checkbox : true 
			}, {
				field : 'customerName',
				title : '客户姓名',
				sortable:true,
				width : 40,
			}, {
				field : 'idNo',
				title : '身份证号码',
				sortable:true,
				width : 40,
			}, {
				field : 'customerType',
				title : '客户类型',
				sortable:true,
				width : 40,
			}, {
				field : 'loanNo',
				title : '申请件编号',
				sortable:true,
				width : 80,
			},{
				field : 'owningBrance',
				title : '进件营业部',
				sortable:true,
				width : 80,
			},{
				field : 'applyDate',
				title : '申请日期',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value !=null && value != ''){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'productTypeName',
				title : '贷款类型',
				sortable:true,
				width : 40,
			},{
				field : 'checkPerson',
				title : '初审员',
				sortable:true,
				width : 40,
			}, {
				field : 'finalPerson',
				title : '终审员',
				sortable:true,
				width : 40,
			}, {
				field : 'approveDate',
				title : '审批日期',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value !=null && value != ''){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'approvalStatus',
				title : '审批结果',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value=="0"){
						return "通过";
					}
					if(value=="1"){
						return "拒绝";
					}
					if(value=="2"){
						return "其他";
					}
				}
			},{
				field : 'refuseReasonEmbed',
				title : '拒绝原因',
				sortable:true,
				width : 40,
				formatter: function (value, row, index) {
					if(value!=null){
						return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
					}
                }
			}, {
				field : 'sourceText',
				title : '来源',
				sortable:true,
				width : 40,
			}, {
				field : 'checkUser',
				title : '质检员',
				sortable:true,
				width : 40,
			}, {
				field : 'status',
				title : '质检状态',
				sortable:true,
				width : 40,
				hidden:true,
			}, {
				field : 'id',
				title : '操作',
				width : 80,
				align : 'center',
				formatter : function(value, data, index) {
					var title = data.customerName+'详情';
					var flag = 'done';
					var action;
					var actionManual = "<a href='javaScript:dispatch("+ value +","+JSON.stringify(data)+");'>分派</a>";
					var actionClose = "&nbsp;|&nbsp;<a href='javaScript:dispatchDispatchClose("+ value +","+JSON.stringify(data)+");''>关闭</a>";
					var actionInfo = '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag+'\');">详情</a>';
					if(data.status == 'quality_recheck' || data.status == 'quality_recheck_wait'){
						action = actionInfo+actionClose;
					}else{
						action = actionManual+actionClose;
					}
					return action;
				}
			} ] ]
		});
	}
	
	//查询暂存件
	function getTemporary(value,data){
		$.ajax({ 
        	type:"POST", 
        	url:ctx.rootPath()+'/manualQuality/getTemporary',
        	dataType:"json",      
        	contentType:"application/json",               
        	data:JSON.stringify(saveDataArry), 
        	success:function(data){ 
        		if(data.type=='SUCCESS'){
        			$.messager.alert("提示", data.messages[0],"info"); 
        		}else{
        			$.messager.alert("提示","关闭失败！！","error");
        		}
        		$('#manualDispatch_datagrid').datagrid('clearChecked');
        		$('#manualDispatch_datagrid').datagrid('reload');  
        	} 
        });
	}
	
	/**
	 * 弹出质检作业页面qualityReceive 
	 * @param value
	 */
	var markQualityHandleLoanNo = {};
	function qualityReceiveDialog(title,loanNo,value,flag){
		if(loanNo in markQualityHandleLoanNo){
			markQualityHandleLoanNo[loanNo].focus();
		}else{
            markQualityHandleLoanNo[loanNo] = jDialog.open({url: ctx.rootPath() + "/qualityControlDesk/qualityReceive?flag="+flag+"&loanNo="+loanNo+"&qualityCheckId="+value});
		}
	}
	
	/**
	 * 日期选择事件
	 */
	$('#assignDateStart').datebox({
		onSelect: function(date){
			$('#assignDateEnd').datebox({
				required:true
			});
		}
	});

