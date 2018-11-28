	var roleCodes = $('#qualityControlDesk_quality_roleCodes').val();
	var isLeader = false;
	$(function() {
		var roleArry = roleCodes.split(",");
		for(var i=0; i<roleArry.length; i++){
			var v = roleArry[i];
			if(roleArry[i]=='qualityCheckGroupLeader' || roleArry[i]=='qualityCheckDirector' || roleArry[i]=='qualityCheckManager'){
				isLeader = true;
			}
		}
		if(isLeader == false){
			$('#qualityControlDesk_exportTodo_btn').hide();
			$('#qualityControlDesk_exportDone_btn').hide();
		}

		// 延迟加载datagrid
        setTimeout(function () {
            qualityControlDeskToDoDatagrid("#qualityControlDesk_todo_datagrid");//待处理
            qualityControlDeskDoneDatagrid("#qualityControlDesk_done_datagrid");//已完成
        }, 100);


	});
	
	//待处理查询
	$("#qualityControlDesk_todo_Query").click(
	    function qualityControlDesk_todo_Query(){
	    	//textbox去空格
	    	$("#qualityControlDesk_todo_queryForm").find('.easyui-textbox').each(function (index, element ) {
    			var $self = $(element);
    			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
    		});
	    	if ($("#qualityControlDesk_todo_Query").parents("form").form("validate")) {
	    		var queryParams = $("#qualityControlDesk_todo_queryForm").serializeObject();
	    		var ownid = queryParams.owningBranchId;
	    		if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
	    			queryParams.owningBranceId =ownid;
	    		}else{
	    			var ownids = ownid.join(",");
	    			queryParams.owningBranceId = ownids;
	    		}
	    		$("#qualityControlDesk_todo_datagrid").datagrid('load',queryParams);
	    	}
	    }
	);

	//待处理重置
	$("#qualityControlDesk_todo_Reset").click(
		function qualityControlDesk_todo_Reset(){
			$('#qualityControlDesk_todo_queryForm').form('clear');
			$(this).parents("form").form('disableValidation');//移除表单里的required 属性 
		}
	);

	//待处理任务列表
	function qualityControlDeskToDoDatagrid(id) {
		$(id).datagrid({
			url : ctx.rootPath() + "/qualityControlDesk/toDoPageList",
			striped : true,
			singleSelect : true,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			remoteSort : false,
			toolbar : '#qualityControlDesk_todo_toolBarBtn',
			onSortColumn:function (sort,order){
				
			},
			rowStyler: function(index,row){
				if('quality_temporary_save' == row.status){
					return 'background-color:#97FFFF;';
				}
			},
			columns : [ [{
				field : 'assignDate',
				title : '分派日期',
				width : 40,
				sortable:true,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'customerName',
				title : '客户姓名',
				sortable:true,
				width : 40
			},{
				field : 'idNo',
				title : '身份证号',
				sortable:true,
				width : 40
			}, {
				field : 'customerType',
				title : '客户类型',
				sortable:true,
				width : 40
			},{
				field : 'owningBrance',
				title : '进件营业部',
				sortable:true,
				width : 40
			}, {
				field : 'qualitySource',
				title : '申请来源',
				sortable:true,
				width : 40
			}, {
				field : 'loanNo',
				title : '申请件编号',
				sortable:true,
				width : 80
			}, {
				field : 'applyDate',
				title : '申请日期',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'productTypeName',
				title : '贷款类型',
				sortable:true,
				width : 40
			}, {
				field : 'approvalStatusText',
				title : '审批结果',
				sortable:true,
				width : 40
			}, {
				field : 'refuseReasonEmbed',
				title : '拒绝原因',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value!=null){
						if(value!=null){
							return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
						}
					}
				}
			}, {
				field : 'approveDate',
				title : '审批日期',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'qualityCheckId',
				title : '操作',
				width : 40,
				align : 'center',
				formatter : function(value, data, index) {
					var title = data.customerName + "质检";
					var flag = 'todo';
					return '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag+'\','+'\''+data.checkUser+'\');">质检</a>';
				}
			} ] ]
		});
	}
	//已完成查询
	$("#qualityControlDesk_done_Query").click(
	    function qualityControlDesk_done_Query(){
	    	if ($("#qualityControlDesk_done_Query").parents("form").form("validate")) {
	    		//textbox去空格
		    	$("#qualityControlDesk_done_queryForm").find('.easyui-textbox').each(function (index, element ) {
	    			var $self = $(element);
	    			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
	    		});
		    	var queryParams = $("#qualityControlDesk_done_queryForm").serializeObject();
		    	var ownid = queryParams.owningBranchId;
	    		if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
	    			queryParams.owningBranceId =ownid;
	    		}else{
	    			var ownids = ownid.join(",");
	    			queryParams.owningBranceId = ownids;
	    		}
	    		$("#qualityControlDesk_done_datagrid").datagrid('load',queryParams);
		    	
	    	}
	    }
	);
	
	//已完成重置
	$("#qualityControlDesk_done_Reset").click(
		function qualityControlDesk_done_Reset(){
			$('#qualityControlDesk_done_queryForm').form('clear');
			$(this).parents("form").form('disableValidation');//移除表单里的required 属性 
		}
	);
	
	function qualityControlDeskDoneDatagrid(id) {
		$(id).datagrid({
			url : ctx.rootPath() + "/qualityControlDesk/donePageList",
			striped : true,
			toolbar : id + '_tool',
			singleSelect : true,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			remoteSort : false,
			scrollbarSize : 0,
			rowStyler: function(index, row){
                if('1' == row.checkStatus){
                	return 'background-color:#FFA500;';
                }
            },
			columns : [ [{
				field : 'endDate',
				title : '完成日期',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'customerName',
				title : '客户姓名',
				sortable:true,
				width : 40
			}, {
				field : 'idNo',
				title : '身份证号',
				sortable:true,
				width : 40
			}, {
				field : 'customerType',
				title : '客户类型',
				sortable:true,
				width : 40
			}, {
				field : 'owningBrance',
				title : '进件营业部',
				sortable:true,
				width : 40
			}, {
				field : 'qualitySource',
				title : '申请来源',
				sortable:true,
				width : 40
			}, {
				field : 'loanNo',
				title : '申请件编号',
				sortable:true,
				width : 80
			}, {
				field : 'productTypeName',
				title : '贷款类型',
				sortable:true,
				width : 40
			}, {
				field : 'approvalStatus',
				title : '审批结果',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value == "0"){
						return "通过";
					}if(value == "1"){
						return "拒绝";
					}if(value == "2"){
						return "其他";
					}
				}
			}, {
				field : 'refuseReasonEmbed',
				title : '拒绝原因',
				sortable:true,
				width : 40,
				formatter : function(value, data, index) {
					if(value!=null){
						if(value!=null){
							return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
						}
					}
				}
			}, {
				field : 'approveDate',
				title : '审批日期',
				sortable:true,
				width : 80,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'checkResult',
				title : '差错类型',
				sortable:true,
				width : 80,
				formatter : function(value) {
					if(value=="E_000000"){
						return "无差错";
					}
					if(value=="E_000001"){
						return "预警";
					}
					if(value=="E_000002"){
						return "建议";
					}
					if(value=="E_000003"){
						return "一般差错"
					}
					if(value=="E_000004"){
						return "重大差错";
					}
				}
			}, {
				field : 'qualityCheckId',
				title : '操作',
				width : 80,
				align : 'center',
				formatter : function(value, data, index) {
					var title = data.customerName+"质检详情";
					var flag = 'done';
					var action = '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag+'\');">详情</a>';
					if(data.ifEditor == true && data.checkStatus == '0'){//判断是否能修改
						var flag1 = 'updateDone';
						action = '<a href="javaScript:qualityReceiveDialog(\''+title+'\','+'\''+data.loanNo+'\','+'\''+value+'\','+'\''+flag1+'\');">编辑</a>';
					}
					return action;
				}
			} ] ]
		});
	}


	//待处理列表导出
	function exportQualityList(type)  {
		var queryParams;
		if(type == 'todo'){
			queryParams = $("#qualityControlDesk_todo_queryForm").serialize();
		}else{
			queryParams = $("#qualityControlDesk_done_queryForm").serialize();
		}
		
		$.ajax({
		    type: 'POST',
		    url: ctx.rootPath()+"/qualityControlDesk/exportQualityList/"+type,
		    data: queryParams,
		    success: function(data) {
		    	$.messager.alert("提示", data,"info"); 
		    }
		});
	}
	
/*	//查询处理意见
	function quryQualityOpion(title,loanNo,value){
		qualityControlDesk_qualityReceive_dialog = ctx.dialog({
			title : title,
			modal : true,
			width : 1200,
			height : 900,
			resizable: true,
			collapsible:true,
			modal:false,
			href : ctx.rootPath()+"/qualityControlDesk/qualityReceive?flag=done&loanNo="+loanNo+"&qualityCheckId="+value
		});
	}*/
	
	/**
	 * 弹出质检作业页面qualityReceive 
	 * @param value
	 */
	function qualityReceiveDialog(title,loanNo,value,flag,checkUser){
		jDialog.open({url: ctx.rootPath() + "/qualityControlDesk/qualityReceive?flag="+flag+"&loanNo="+loanNo+"&qualityCheckId="+value+"&checkUser="+checkUser});
	}
	
	/**
	 * 日期选择事件
	 */
	$('#queryControlDesk_todo_inStartDate').datebox({
		onSelect: function(date){
			$('#queryControlDesk_todo_inEndDate').datebox({
				required:true
			});
		}
	});
	
	$('#queryControlDesk_done_inStartDate').datebox({
		onSelect: function(date){
			$('#queryControlDesk_done_inEndDate').datebox({
				required:true
			});
		}
	});
