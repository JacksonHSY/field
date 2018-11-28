var finishReformSearchOrNot = false;
var reformDataList = null;
$(function() {
	// 初始化分派状态
	$("#finishReform_fpStatus").combobox({
        prompt:'分派状态',
		panelHeight:'auto',
        width: 190,
		editable:false,
        onChange:function(newValue,oldValue) {
            changeFpStatus(newValue);
            finishReformQuery();
		},
	});
	
	initFinishReformDatagrid();
	
	$("#finishReform_Query_Form").mouseover(function(){
		finishReformSearchOrNot = true;
	});
	$("#finishReform_Query_Form").mouseout(function(){
		finishReformSearchOrNot = false;
	});
	$(document).keyup(function(evnet) {
		if (evnet.keyCode == '13' && finishReformSearchOrNot) {
			finishReformQuery();
		}
	});
});


function initFinishReformDatagrid() {
	var fpStatus = $("#finishReform_fpStatus").combobox("getValue");
    changeFpStatus(fpStatus);
	$("#finishReform_datagrid").datagrid({
		url : ctx.rootPath() + '/finishApprove/finishReformWorkbench',
		striped : true,
		singleSelect : false,
		checkOnSelect : true,
		toolbar : '#finishReform_datagrid_tool',
		rownumbers : true,
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		idField : 'loanNo',
		remoteSort : true,
		onDblClickRow : function(index, data) {
			handle_finishReform(index);
		},
		onLoadSuccess : function(data) {
			reformDataList = data;
		},
		queryParams:{
            fpStatus:fpStatus
		},
		columns : [ [ {
			field : 'id',
			checkbox : true
		}, {
			field : 'ifPri',
			title : '案件标识',
			width : 130,
			sortable : false,
			formatter : formateCaseIdentification,
		}, {
			field : 'loanNo',
			title : '借款编号',
			width : 140,
			sortable : true,
		}, {
			field : 'xsSubDate',
			title : '提交时间',
			width : 160,
			formatter : ctx.getDate,
			sortable : true,
		}, {
			field : 'customerName',
			title : '申请人姓名',
			width : 100,
			sortable : true,
            formatter : function (value,row,index) {
                if (getOldCardIdExists(row.customerIDNO)) {
                    value = "*" + value;
                }
                return value;
            }
		}, {
			field : 'customerIDNO',
			title : '身份证号码',
			width : 80,
			sortable : true,
            formatter:function (value,row,index){
                return  "*" + value.substring(value.length - 4, value.length);
            }
		}, {
			field : 'productName',
			title : '借款产品',
			width : 100,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'owningBranchName',
			title : '营业部',
			width : 170,
			sortable : true,
		}, {
			field : 'owningBranchAttr',
			title : '营业部属性',
			width : 90,
			sortable : false,
		}, {
			field : 'handleName',
			title : '处理人',
			width : 100,
			sortable : false,
		}, {
			field : 'proxyGroupName',
			title : '管理组',
			width : 60,
			sortable : true,
			hidden : true,
		}, {
			field : 'loanNoTopClass',
			title : '申请件层级',
			width : 80,
			sortable : true,
		}, {
			field : 'checkPersonCode',
			title : '初审姓名',
			width : 100,
			sortable : false,
		}, {
			field : 'cSProxyGroupName',
			title : '初审小组',
			width : 100,
			sortable : false,
		}, {
			field : 'lastCsApprovalAmount',
			title : '提交额度',
			width : 90,
			sortable : false,
			formatter : numberFomatter
		}, {
			field : 'zsIfNewLoanNo',
			title : '所在队列',
			width : 150,
			sortable : false,
			formatter : function(value, data, index) {
				if(null == data.handleCode || "" == data.handleCode){
					return "";
				}else{
					if ('XSZS-HANGUP' == data.rtfNodeStatus) {
						return "挂起队列";
					} else if ('0' == data.zsIfNewLoanNo) {
						return "优先队列";
					} else if ('1' == data.zsIfNewLoanNo) {
						return "正常队列";
					}
				}
			}
		},{
			field : 'action',
			title : '操作',
			width : 60,
			align : 'center',
			formatter : function(value, data, index) {
				return '<a href="javaScript:void(0);" onclick=handle_finishReform("' + index + '")>查看详情</a>';
			}
		} ] ]
	});
}


// 搜索
function finishReformQuery() {
	if ($("#finishReform_Query_Form").form("validate")) {
		var queryParams = $("#finishReform_Query_Form").serializeObject();
		if ("-1" == queryParams.fpStatus) {
			delete queryParams.fpStatus;
			// queryParams.fpStatus = null;
		}
		// 转换案件标识
		var ct = queryParams.caseType;
		if (ct != null && ct instanceof Array && ct.length > 1) {
			var s = ct.join(",");
			queryParams.caseType = s;
		}
		// 转换处理人
		var hc = queryParams.handleCode;
		if (hc != null && hc instanceof Array && hc.length > 1) {
			var s = hc.join(",");
			queryParams.handleCode = s;
		}
		// 转换申请件层级
		var ltc = queryParams.loanNoTopClass;
		if (ltc != null && ltc instanceof Array && ltc.length > 1) {
			var s = ltc.join(",");
			queryParams.loanNoTopClass = s;
		}
		// 转换申请产品
		var pc = queryParams.productCd;
		if (pc != null && pc instanceof Array && pc.length > 1) {
			var s = pc.join(",");
			queryParams.productCd = s;
		}
		// 转换营业部
		var ow = queryParams.owningBranchId;
		if (ow != null && ow instanceof Array && ow.length > 1) {
			var s = ow.join(",");
			queryParams.owningBranchId = s;
		}
		$("#finishReform_datagrid").datagrid("clearSelections");
		$("#finishReform_datagrid").datagrid('load',queryParams);
	}
}

// 改派
function finishReformReassignmentDialog() {
	var rows = $('#finishReform_datagrid').datagrid('getSelections');
	if (rows.length > 0) {
		// 初始化处理人下拉框
		initApprovePerson(rows);

		$("#finishReform_reassignment_dialog").removeClass("display_none").dialog({
			title : "终审改派",
			modal : true,
			width : 400,
			height : 180,
			buttons : [ {
				text : '保存',
				iconCls : 'fa fa-check',
				handler : function() {
					finishReformReassignment(rows);
				}
			}, {
				text : '返回',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#finishReform_reassignment_dialog").dialog("close");
				}
			} ]
		})
	} else {
		$.info("提示", "请选择一条记录!", "info", 2000);
	}
}

// 初始化处理人下拉列表
function initApprovePerson(rows) {
	var approvalAmount = new Array();
	var handler = new Array();
	var loanNo = new Array();
	for (var i = 0; i < rows.length; i++) {
		approvalAmount.push(rows[i].accLmt);
		if(null != rows[i].handleCode){
			handler.push(rows[i].handleCode);
		}
		loanNo.push(rows[i].loanNo);
	}
	var obj = new Object();
	obj.approvalAmountArr = approvalAmount;
	obj.handlerArr = handler;
	obj.loanNoArr = loanNo;
	$.ajax({
			url: ctx.rootPath() + "/finishApprove/approvePersonList",
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			dataType: "JSON",
			data: JSON.stringify(obj),
			success: function(response)
			{
				console.info($("#approvePerson"));
				$("#approvePerson").combobox({
					valueField : 'staffCode',
					textField : 'name',
					prompt:'处理人',
					required : true,
					data : response,
					filter : filterCombo
				});
			},
			error : function(data) {
				$.info("警告", '系统繁忙，请稍后再试！', "warning");
			}
		});
}

// 改派工作流流转
var canReassign = true;
function finishReformReassignment(rows) {
	if(canReassign){
		var title = $("#finishReform_reassignment_dialog").dialog('options').title;
		if (title == '终审改派') {
			if (rows.length > 0) {
				var validate = $("#finishReform_reassignment_dialog").form('validate');
				if (validate) {
					//判断选中的处理人是否在处理人列表中
					var item = $('#approvePerson').combobox("options").finder.getRow($('#approvePerson').get(0), $('#approvePerson').combobox("getValue"));
					if (!isNotNull(item)) {
						$.info("提示", "请选择一个正确的处理人!", "info", 2000);
					} else {
						canReassign = false;
						var api = ctx.rootPath() + '/finishApprove/finishReform/reassignment';
						var approvePerson = $('#approvePerson').combobox('getValue');
						var reformList = new Array();
						for (var i = 0; i < rows.length; i++) {
							var reform = new Object();
							reform.loanNo = rows[i].loanNo;
							reform.zSIfNewLoanNo = rows[i].zSIfNewLoanNo;
							reform.rtfNodeState = rows[i].rtfNodeStatus;
							reform.version = rows[i].version;
							reform.userCode = rows[i].handleCode;
							reform.targetUserCode = approvePerson;
							reform.finalPersonCode = rows[i].finalPersonCode;
							reform.approvalPersonCode = rows[i].approvalPersonCode;
							reformList.push(reform);
						}
						$.ajax({
							url : api,
							dataType : "json",
							method : 'post',
							contentType : 'application/json',
							data : JSON.stringify(reformList),
							success : function(data) {
								if (data.success) {
									$.info("提示", data.messages);
									$("#finishReform_reassignment_dialog").dialog("close");
									$("#finishReform_datagrid").datagrid("clearSelections");
									$("#finishReform_datagrid").datagrid('reload');
								} else {
									$.info("提示", data.messages, "error");
								}
								canReassign = true
							},
							error : function(XMLHttpRequest, textStatus, errorThrown) {
								console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
								$.info("提示", "改派失败!", "info", 2000);
								canReassign = true
							}
						});
					}
				}
			} else {
				$.info("提示", "请选择一条记录!", "info", 2000);
			}
		}
	}
}

/**
 * 根据查询条件导出excel
 */
function exportExcel() {
	var queryParams = $("#finishReform_Query_Form").serializeObject();
	if ("-1" == queryParams.fpStatus) {
		delete queryParams.fpStatus;
		// queryParams.fpStatus = null;
	}
	// 转换案件标识
	var ct = queryParams.caseType;
	if (ct != null && ct instanceof Array && ct.length > 1) {
		var s = ct.join(",");
		queryParams.caseType = s;
	}
	// 转换处理人
	var hc = queryParams.handleCode;
	if (hc != null && hc instanceof Array && hc.length > 1) {
		var s = hc.join(",");
		queryParams.handleCode = s;
	}
	// 转换申请件层级
	var ltc = queryParams.loanNoTopClass;
	if (ltc != null && ltc instanceof Array && ltc.length > 1) {
		var s = ltc.join(",");
		queryParams.loanNoTopClass = s;
	}
	// 转换申请产品
	var pc = queryParams.productCd;
	if (pc != null && pc instanceof Array && pc.length > 1) {
		var s = pc.join(",");
		queryParams.productCd = s;
	}
	// 转换营业部
	var ow = queryParams.owningBranchId;
	if (ow != null && ow instanceof Array && ow.length > 1) {
		var s = ow.join(",");
		queryParams.owningBranchId = s;
	}
	
	window.location.href = ctx.rootPath()+"/finishApprove/exportExcel?" + $.param(queryParams);
}

/**
 * 批量退回(弹出框)
 */
function batchReturn(){
	$("#finishReform_back_dialog").form('clear');
	var rows = $('#finishReform_datagrid').datagrid('getSelections');
	if (rows.length > 0) {
        var moneyOrCommonPiece = 2;// 0:非前前,1:前前;
        var isEqualAll = true;
        $.each(rows, function (ind, val) {
            var obj = new Object();
            if (isNotNull(val.zdqqApply) && 1 == val.zdqqApply) {
                if (0 != moneyOrCommonPiece) {
                    moneyOrCommonPiece = 1;
                } else {
                    isEqualAll = false;
                    return false;
                }
            } else {
                if (1 != moneyOrCommonPiece) {
                    moneyOrCommonPiece = 0;
                } else {
                    isEqualAll = false;
                    return false;
                }
            }
        });
        // 判断是否选择相同的案件(前前或普通件)
        if (!isEqualAll) {
            $.info("提示", "请选择相同的进件!");
            return false;
        }
        $("#finish_remak_teturnType").val(moneyOrCommonPiece);
		//弹出批量退回弹出框
		$("#finishReform_back_dialog").removeClass("display_none").dialog({
			title : "批量退回审批信息",
			modal : true,
			width : 820,
			height : 320,
			onBeforeOpen : function() {
                if (1 == moneyOrCommonPiece) {
                    $("#finishReform_back_dialog").find("label:first").html("退回前前");
                } else {
                    $("#finishReform_back_dialog").find("label:first").html("退回门店");
                }
				initClassAReason("#finishReform_back_ParentCode",null, "return", "#finishReform_back_ReasonCode",260);
			},
			buttons : [ {
				text : '提交',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					finishReformBatchReturn(rows,moneyOrCommonPiece);
				}
			}, {
				text : '取消',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#finishReform_back_dialog").dialog("close");
				}
			} ],
            onClose: function () {
                $("#finishReform_back_dialog").find("tr.markReturnReason:gt(0)").remove();
                $("#finishReform_back_dialog").find("form").form("reset");
            }
		});
	} else {
		$.info("提示", "请选择一条记录!", "info", 2000);
	}
}

$(function() {
	$(":radio").linkbutton({
		onClick : function() {
			var back_type = $(this).val();
			if (back_type == "ZSRTNCS") {
				initClassAReason("#finishReform_back_ParentCode", "ZSRTNCS", "return", "#finishReform_back_ReasonCode",260);
                $("#finishReform_back_dialog").find(".markIsAdd").html("&nbsp;&nbsp;");
                $("#finishReform_back_ReasonCode").combobox({multiple:false});
                $("#finishReform_back_dialog").find("tr.markReturnReason:gt(0)").remove();
			} else {
				var moneyOrCommonPiece =$("#finish_remak_teturnType").val()
				if (1==moneyOrCommonPiece) {
					$("#finishReform_back_dialog").find(".markIsAdd").html('<a href="javaScript:void(0);" onclick=finishAddOrDeleteRetureReason("ADD")><i class="fa fa-plus" aria-hidden="true"></i></a>');
                    $("#finishReform_back_ReasonCode").combobox({multiple:true,separator:'|'});
                    initClassAReason("#finishReform_back_ParentCode", "ZSRTNQQ", "return", "#finishReform_back_ReasonCode",260);
				} else {
                    initClassAReason("#finishReform_back_ParentCode", "ZSRTNLR", "return", "#finishReform_back_ReasonCode",260);
				}

			}
		}
	});
});

/**
 * 判断所属队列
 * 
 * @param data
 * @returns
 */
function getTaskType(data){
	if ('XSZS-HANGUP' == data.rtfNodeStatus) {
		return "3";
	} else if ('0' == data.zSIfNewLoanNo) {
		return "1";
	} else if ('1' == data.zSIfNewLoanNo) {
		return "2";
	}
}

/**
 * 批量退回操作
 * 
 * @param rows
 */
var canBatchReturn = true;
function finishReformBatchReturn(rows,moneyOrCommonPiece){
	if(canBatchReturn){
		var returnList = new Array();//要退回的数据申请件集合
		var params = $('#finishReform_back_dialog').find("form").serializeObject();
		if($("#finishReform_back_dialog").form('validate')){
			canBatchReturn = false;
			var back_type = $("input[name='backType']:checked").val();
			var returnReasons = "";
            if (1 == moneyOrCommonPiece && "ZSRTNLR"==back_type) {
                returnReasons = finishCreateMoneyReturnReason(applyHis)
            }
            for (var i = 0; i < rows.length; i++) {
				var applyHis = new Object();
				applyHis.backType = back_type;
                if (1 == moneyOrCommonPiece && "ZSRTNLR"==back_type) {
                    applyHis.returnReasons =returnReasons;
                } else {
                    applyHis.firstReason = params.firstReason;
                    applyHis.secondReason = params.secondReason;
                    applyHis.firstReasonText = params.firstReasonText;
                    applyHis.secondReasonText = params.secondReasonText;
				}
				applyHis.loanNo = rows[i].loanNo;
				applyHis.remark = params.remark;
				applyHis.version = rows[i].version;
				applyHis.handleCode = rows[i].handleCode;
				applyHis.taskType = getTaskType(rows[i]);
				returnList.push(applyHis);
			}
			$.ajax({
				url : ctx.rootPath() + "/finishApprove/batchReturn",
				dataType : "json",
				method : 'post',
                contentType: 'application/json',
				data : JSON.stringify(returnList),
				success : function(data) {
					if (data.success) {
						$.info("提示", data.messages);
						$("#finishReform_back_dialog").dialog("close");
						$("#finishReform_datagrid").datagrid("clearSelections");
						$("#finishReform_datagrid").datagrid('reload');

					} else {
						$.info("提示", data.messages, "error");
					}
					canBatchReturn = true;
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					$.info("提示", "退回失败!", "info", 2000);
					canBatchReturn = true;
				}
			});
		}
	}
}


/**
 * 批量拒绝（弹出框）
 */
function batchRefuse(){
	$("#finishReform_refuse_dialog").form('clear');
	var rows = $('#finishReform_datagrid').datagrid('getSelections');
	if (rows.length > 0) {
		//判断是否有未分派的申请件,有就提示并不能继续操作
//		for (var int = 0; int < rows.length; int++) {
//			if(isNotNull(rows[int].handleCode)){
//				$.info("提示", "只能拒绝未分派的申请件,请重新选择!", "warning", 2000);
//				return;
//			}
//		}
		//弹出批量退回弹出框
		$("#finishReform_refuse_dialog").removeClass("display_none").dialog({
			title : "批量拒绝审批信息",
			modal : true,
			width : 980,
			height : 320,
			onBeforeOpen : function() {
				initClassAReason("#finishReform_refuse_firstReason", "XSZS", "reject", "#finishReform_refuse_secondReason");
				
				var $refuseReason = $(this).find('#finishReform_refuse_firstReason');
				var $remark = $(this).find('#finishReform_refuse_remark');
				
				var defaultOptions = $remark.data('options');
				$refuseReason.combobox({
					onSelect : function(record) {
						var code = record.code;
						// 如果一级拒绝原因为"其他"，则备注必填
						if (code != null && code === "RJ9999") {
							var newOptions = $.extend({}, $remark.data('options'), {
								required : true
							});
							$remark.textbox(newOptions);
						} else {
							var newOptions = $.extend({}, $remark.data('options'), {
								required : false
							});
							$remark.textbox(newOptions);
						}

						var conType = record.conditionType;// 偿还能力不足标志
						if (isNotNull(conType) && conType.indexOf('debtRatio_Y') >= 0) {
							$('#finishReform_refuse_conType').val(conType);
						} else {
							$('#finishReform_refuse_conType').val('none');
						}
					}
				})
				
			},
			onClose : function() {
				// 关闭弹窗时，备注必填属性去掉
				var $remark = $(this).find('#finishReform_refuse_remark');
				var newOptions = $.extend({}, $remark.data('options'), {
					required : false
				});
				$remark.textbox(newOptions);
			},
			buttons : [ {
				text : '提交',
				iconCls : 'fa fa-arrow-up',
				handler : function() {
					finishReformBatchRefuse(rows);
				}
			}, {
				text : '取消',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#finishReform_refuse_dialog").dialog("close");
				}
			} ]
		});
	} else {
		$.info("提示", "请选择一条记录!", "info", 2000);
	}
}


/**
 * 批量拒绝操作
 * @param rows
 */
var canBatchRefuse = true;
function finishReformBatchRefuse(rows){
	if(canBatchRefuse){
		var refuseList = new Array();//要拒绝的数据申请件集合
		var params = $('#finishReform_refuse_dialog').find("form").serializeObject();
		//console.log(JSON.stringify(rows));
		//console.log(JSON.stringify(params));
		if($("#finishReform_refuse_dialog").form('validate')){
			canBatchRefuse = false;
			for (var i = 0; i < rows.length; i++) {
				var applyHis = new Object();
				applyHis.firstReason = params.firstReason;
				applyHis.secondReason = params.secondReason;
				applyHis.firstReasonText = params.firstReasonText;
				applyHis.secondReasonText = params.secondReasonText;
				applyHis.loanNo = rows[i].loanNo;
				applyHis.remark = params.remark;
				applyHis.version = rows[i].version;
				applyHis.conType = params.conType;
				applyHis.handleCode = rows[i].handleCode;
				applyHis.taskType = getTaskType(rows[i]);
				refuseList.push(applyHis);
			}
			console.log(JSON.stringify(refuseList));
			$.ajax({
				url : ctx.rootPath() + "/finishApprove/batchRefuse",
				dataType : "json",
				method : 'post',
				contentType : 'application/json',
				data : JSON.stringify(refuseList),
				success : function(data) {
					console.log(data);
					if (data.success) {
						$.info("提示", data.messages);
						$("#finishReform_refuse_dialog").dialog("close");
						$("#finishReform_datagrid").datagrid("clearSelections");
						$("#finishReform_datagrid").datagrid('reload');
					} else {
						$.info("提示", data.messages, "error");
					}
					canBatchRefuse = true
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					$.info("提示", "拒绝失败!", "info", 2000);
					canBatchRefuse = true
				}
			});
		}
	}
}

/*=================================================================办理页面改派=========================================================================================*/

/**
 * 打开办理页面(带改派按钮)
 * 
 * @param loanNo
 */
function handle_finishReform(index) {
	var row = reformDataList.rows[index];
	localStorage.setItem(row.loanNo + "-finishReformData", JSON.stringify(row));
	jDialog.open({
		url: ctx.rootPath() + "/search/handle/" + row.loanNo + "/2",
		onDestroy : function(){
			localStorage.removeItem(row.loanNo + "-finishReformData");
			$("#finishReform_datagrid").datagrid('reload');
			$("#finishReform_reassignment_dialog").dialog("close");
		}
	});
}


/**
 * 办理页面改派--弹出处理人dialog
 * 
 * @returns
 */
function handle_finishReformDialog(loanNo) {
	var row = null;
	var cacheData = localStorage.getItem(loanNo + "-finishReformData");
	if(isEmptyObj(cacheData)){
		post(ctx.rootPath() + "/finishApprove/handle/reformInfo/" + loanNo, null, "json", function(data) {
	        if (isNotNull(data)) {
	        	row = data;
	        	localStorage.setItem(row.loanNo + "-finishReformData", JSON.stringify(row));
	        } else {
	            $.info("警告", "没有查到此数据", "warning");
	            return;
	   		 }
	    });
	}else{
		row = JSON.parse(localStorage.getItem(loanNo + "-finishReformData"));
	}
	
	// 初始化处理人下拉框
	handle_initApprovePerson(row);

	$("#handle_finishReform_dialog").removeClass("display_none").dialog({
		title : "终审改派",
		modal : true,
		width : 400,
		height : 180,
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-check',
			handler : function() {
				handle_finishReformReassignment(row);
			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#handle_finishReform_dialog").dialog("close");
			}
		} ]
	})
}


/**
 * 办理页面改派--初始化处理人
 * 
 * @param rows
 * @returns
 */
function handle_initApprovePerson(rows) {
	var approvalAmount = new Array();
	var handler = new Array();
	var loanNo = new Array();
	approvalAmount.push(rows.accLmt);
	if(null != rows.handleCode){
		handler.push(rows.handleCode);
	}
	loanNo.push(rows.loanNo);
	var obj = new Object();
	obj.approvalAmountArr = approvalAmount;
	obj.handlerArr = handler;
	obj.loanNoArr = loanNo;
	$.ajax({
			url: ctx.rootPath() + "/finishApprove/approvePersonList",
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			dataType: "JSON",
			data: JSON.stringify(obj),
			success: function(response)
			{
				console.info($("#handle_finishApprovePerson"));
				$("#handle_finishApprovePerson").combobox({
					valueField : 'staffCode',
					textField : 'name',
					prompt:'处理人',
					required : true,
					data : response,
					filter : filterCombo
				});
			},
			error : function(data){
				$.info("警告", '系统繁忙，请稍后再试！', "warning");
			}
		});
}


/**
 * 办理页面改派--改派
 * 
 * @param rows
 * @returns
 */
var canSubmit = true;// 控制是否可以重复点击提交按钮
function handle_finishReformReassignment(rows) {
	if(canSubmit){
		canSubmit = false;
		var title = $("#handle_finishReform_dialog").dialog('options').title;
		if (title == '终审改派') {
			var validate = $("#handle_finishReform_dialog").form('validate');
			if (validate) {
				//判断选中的处理人是否在处理人列表中
				var item = $('#handle_finishApprovePerson').combobox("options").finder.getRow($('#handle_finishApprovePerson').get(0), $('#handle_finishApprovePerson').combobox("getValue"));
				if (!isNotNull(item)) {
					$.info("提示", "请选择一个正确的处理人!", "info", 2000);
					canSubmit = true;
				} else {
					var api = ctx.rootPath() + '/finishApprove/finishReform/reassignment';
					var approvePerson = $('#handle_finishApprovePerson').combobox('getValue');
					var reformList = new Array();
					var reform = new Object();
					reform.loanNo = rows.loanNo;
					reform.zSIfNewLoanNo = rows.zSIfNewLoanNo;
					reform.rtfNodeState = rows.rtfNodeStatus;
					reform.version = rows.version;
					reform.userCode = rows.handleCode;
					reform.targetUserCode = approvePerson;
					reform.finalPersonCode = rows.finalPersonCode;
					reform.approvalPersonCode = rows.approvalPersonCode;
					reformList.push(reform);
					$.ajax({
						url : api,
						dataType : "json",
						method : 'post',
						contentType : 'application/json',
						data : JSON.stringify(reformList),
						success : function(data) {
							console.log(data);
							if (data.success) {
								$.info("提示", data.messages);
								$("#handle_finishReform_dialog").dialog("close");
								setTimeout(function(){
									canSubmit = true;
									window.close();
								}, 2000);
							} else {
								$.info("提示", data.messages, "error");
								canSubmit = true;
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
							$.info("提示", "改派失败!", "info", 2000);
							canSubmit = true;
						}
					});
				}
			}else{
				canSubmit = true;
			}
		}else{
			canSubmit = true;
		}
	}
}


/**
 * 修改分派状态控制权限显示
 */
function changeFpStatus(value) {
    if ("1" == value) {// 已分派
        $("#finishReform_notAssigned_span").addClass("display_noH");
        $("#finishReform_assigned_span").removeClass("display_noH");
    } else{// 未分派
        $("#finishReform_notAssigned_span").removeClass("display_noH");
        $("#finishReform_assigned_span").addClass("display_noH");
    }
}

/**
 *
 * 前前添加退回原因或者删除
 *
 * @param operationType-add-delete
 */
function finishAddOrDeleteRetureReason(operationType,obj) {
    if ("ADD" == operationType) {
        var dataSize = $("#finishReform_back_ParentCode").combobox("getData").length;
        if ($("#finishReform_back_dialog").find(".markReturnReason").length < dataSize) {
			var html = $("<tr class='markReturnReason'><th>一级原因:</th><td><input name='firstReason' class='input'><input type='hidden' name='firstReasonText'></td><th>二级原因:</th><td><input name='secondReason' class='input' data-options=multiple:true,separator:'|'><input type='hidden' name='secondReasonText'></td><td><a href='javaScript:void(0);' onclick=finishAddOrDeleteRetureReason('DELETE',this)><i class='fa fa-minus' aria-hidden='true'></i></a></td></tr>");
			initClassAReason(html.find("input[name='firstReason']"), "CSRTNQQ", "return", html.find("input[name='secondReason']"),260);
			$("#finishReform_back_dialog").find(".markReturnReason:last").after(html);
        }else {
            $.info("提示","原因不能超过一级原因总数");
        }
    } else {
        $(obj).parents("tr").remove();
    }
}
/**
 * 前前退回参数封装
 * @param obj
 */
function finishCreateMoneyReturnReason() {
    var returnReasons = new Array();
    $("#finishReform_back_dialog").find(".markReturnReason").each(function(ind,trObj){
        var secondCodeArray =  $(trObj).find("input[name='secondReason'][value !='']");
        if (secondCodeArray.length >0) {
            var secondTextArray = ($(trObj).find("input[name='secondReasonText']").val()).substring(1).split("|");
            for(var i =0;i<secondCodeArray.length;i++) {
                var objReasons = new Object();
                objReasons.primaryReasonCode = $(trObj).find("input[name='firstReason']").val();// 一级原因码
                objReasons.primaryReason = $(trObj).find("input[name='firstReasonText']").val();// 一级原因文本
                objReasons.secondReasonCode = $(secondCodeArray[i]).val();// 二级原因码
                objReasons.secondReason = secondTextArray[i];// 二级原因文本
                returnReasons.push(objReasons);
            }
        } else {
            var objReasons = new Object();
            objReasons.primaryReasonCode = $(trObj).find("input[name='firstReason']").val();// 一级原因码
            objReasons.primaryReason = $(trObj).find("input[name='firstReasonText']").val();// 一级原因文本
            returnReasons.push(objReasons);
        }
    });
    return returnReasons;
}






