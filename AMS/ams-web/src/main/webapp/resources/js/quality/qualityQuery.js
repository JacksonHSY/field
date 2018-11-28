	$(function() {
		qualityQueryDatagrid("#qualityQuery_datagrid");
	});
	//查询
	$("#qualityQuery_Query").click(
	    function qualityQuery_Query(){
	    	var queryParams = $("#qualityQuery_queryForm").serializeObject();
	    	var ownid = queryParams.owningBranchId;
	    	 if(typeof(ownid)=="string" || typeof(ownid)=="undefined"){
	    		 queryParams.owningBranceId =ownid;
	    	 }else{
	    		 var ownids = ownid.join(",");
	    		 queryParams.owningBranceId = ownids;
	    	 }
			$("#qualityQuery_datagrid").datagrid('load',queryParams);
	    }
	);
	//重置
	$("#qualityQuery_reset").click(
	    function qualityQuery_reset(){
	    	$('#qualityQuery_queryForm').form('clear');
	    }
	);
	function qualityQueryDatagrid(id) {
		$(id).datagrid({
			url : ctx.rootPath()+"/qualityQuery/pageList",
			striped : true,
			singleSelect : false,
			rownumbers : true,
			idField : 'id',
			pagination : true,
			fitColumns : true,
			scrollbarSize : 0,
			toolbar:'#qualityQuery_toolBarBtn',
			columns : [ [{
				field : 'endDate',
				title : '完成日期',
				sortable:true,
				width : 80,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD');
					}
				}
			}, {
				field : 'customerName',
				title : '客户姓名',
				width : 80,
			}, {
				field : 'idNo',
				title : '身份证号',
				width : 80,
				 formatter : function(value, data, index) {
		                if (value != null) {
		                    return '*'+value.slice(-4);
		                }
				 }
			},{
				field : 'customerType',
				title : '客户类型',
				width : 80,
				
			}, {
				field : 'owningBrance',
				title : '进件营业部',
				width : 80,
			}, {
				field : 'source',
				title : '申请来源',
				width : 80,
			}, {
				field : 'loanNo',
				title : '申请件编号',
				width : 80,
			}, {
				field : 'productName',
				title : '贷款类型',
				width : 80,
                formatter:formatterProduct,
			}, {
				field : 'approveState',
				title : '审批结果',
				width : 80,
			}, {
				field : 'checkPerson',
				title : '初审工号',
				width : 80,
			}, {
				field : 'checkPersonName',
				title : '初审姓名',
				width : 80,
			}, {
				field : 'finalPerson',
				title : '终审工号',
				width : 80,
			}, {
				field : 'finalPersonName',
				title : '终审姓名',
				width : 80,
			}, {
				field : 'approveDate',
				title : '审批日期',
				width : 80,
				formatter : function(value, data, index) {
					if(value != null){
						return moment(value).format('YYYY-MM-DD hh:mm:ss');
					}
				}
			},{
				field : 'checkError',
				title : '差错类型',
				width : 80,
				
			}, {
				field : 'errorCode',
				title : '差错代码',
				width : 80,
				
			}, {
				field : 'checkUser',
				title : '质检人员',
				width : 80,
			}, {
				field : 'qualityCheckId',
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, data, index) {
					return '<a href="javascript:void(0);"class="easyui-linkbutton" onclick=approveOption("'+ data.loanNo+'")>审批意见</a> '+
					'   <a href="javascript:addTabs;"class="easyui-linkbutton" onclick=detail("'+data.loanNo+'","'+value+'")>详情</a> '+
					'   <a href="javascript:void(0);"class="easyui-linkbutton" onclick=qualityOpinionDialog("'+ data.qualityCheckId+'")>质检结论</a> '
				}
			} ] ]
		});
	}
	//审批意见
function approveOption(val){
	finishApproveReceive_approvalOpinion_dialog = ctx.dialog({
		title : "审批意见",
		modal : true,
		width : 1400,
		height : 800,
		href : ctx.rootPath() + '/finishApprove/finishApprovalOpinion/' + val,
	});
}
//详情
function detail(val,id) {
		 addTabs('质检意见', ctx.rootPath() + '/qualityControlDesk/qualityReceive?flag=done&qualityCheckId='+id+'&loanNo='+val)
}
//质检结论
function qualityOpinionDialog(val) {
	var qualityReceive_logNotesInfo_dialog = ctx.dialog({
		title : '质检结论',
		modal : true,
		width : 800,
		height : 900,
		href : ctx.rootPath() + '/qualityControlDesk/getQualityCheckOpinion/'+val,
		buttons : [ {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				qualityReceive_logNotesInfo_dialog.dialog("close");
			}
		} ]
	});
}