// 是否禁止接单提示标识
var markSwith = true;
$(function() {
	// 加载优先队列
	initFirstApproveDatagrid("#firstApprove_priorityTask_datagrid", "优先队列", 1);
	// 加载正常队列
	initFirstApproveDatagrid("#firstApprove_normalTask_datagrid", "正常队列", 2);
	// 加载挂起队列
	initFirstApproveDatagrid("#firstApprove_hangUpTask_datagrid", "挂起队列", 3, [ 3, 5, 7, 9 ]);
	// 加载当前用户是否接单
	post(ctx.rootPath() + "/staffOrderTask/getIsAcceptOrder", null, "json", function(data) {
		if (data.success) {
			if ("Y" == data.data) {
				$('#firstApprove_orders_radio').attr("checked", "true");
			}
		}
		$('#firstApprove_orders_radio').switchbutton({
			onText : '是',
			offText : '否',
			onChange : function(checked) {
				var info = checked == true ? '开启' : '禁止';
				if (markSwith) {
					markSwith = false;
					$("<div id='firstApprove_orders_radio_confirm'></div>").dialog({
						title:'提示',
                        width: 302,
                        height: 160,
						modal: true,
						onClose:function(){
                            $(this).dialog("destroy");
						},
                        onDestroy: function () {
							if (!markSwith) {
                                $('#firstApprove_orders_radio').switchbutton("resize", {
                                    checked : checked == true ? false : true
                                });
                                markSwith = true;
							}
						},
                        content:'<div class="messager-body panel-body panel-body-noborder window-body" style="width: 266px;">' +
						'<div class="messager-icon messager-question"></div><div>确认'+info+'接单？</div><div style="clear:both;"></div>' +
						'<div class="messager-button">' +
						'<a href="javascript:void(0)" onclick="firstOkOrCancelButton(true,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">确定</span></span></a>' +
						'<a href="javascript:void(0)" onclick="firstOkOrCancelButton(false,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></div></div>'
					})
				}
			}
		});
	});

    // datebox设置成今天-已完成页面查询
    $("#firstApprove_dateStart_datebox").val(ctx.getDate());
    $("#firstApprove_dateEnd_datebox").val(ctx.getDate());
    // 完成任务不要和优先队列一起查询
	initFirstApproveFinishDatagrid("#firstApprove_finishTask_datagrid");
});

/**
 * 初审关闭开启提示确认或取消
 * @param flag
 */
function firstOkOrCancelButton(flag,checked) {
    markSwith = flag;
	$("#firstApprove_orders_radio_confirm").dialog("destroy");
	if (flag) { // 确认是否开放接单
        post(ctx.rootPath() + "/staffOrderTask/isAcceptOrder", {
            ifAccept : checked == true ? 'Y' : 'N'
        }, "json", function(data) {
            if (data.success) {
                $.info("提示", "操作成功!");
            } else {
                markSwith = false;
                $('#firstApprove_orders_radio').switchbutton("resize", {
                    checked : checked == true ? false : true
                });
                $.info("警告", data.firstMessage, "warning");
       		 }
        });
	} else {
        $('#firstApprove_orders_radio').switchbutton("resize", {
            checked : checked == true ? false : true
        });
	}
    markSwith = true;
}

/**
 * 待办任务列表
 * @param id-控件id
 * @param title-datagird表头
 * @param taskType-待办任务类型-1:优先;2:正常;3:挂起
 * @param pageLists-分页每页显示条数list
 */
function initFirstApproveDatagrid(id, title, taskType, pageLists) {
	var pageSize = pageLists ? 3 : 10;
	var pageList = pageLists || [ 10, 20, 30, 40, 50 ];
	$(id).datagrid({
		url : ctx.rootPath() + '/firstApprove/firstWorkbench',
		title : title,
		striped : true,
		singleSelect : true,
		rownumbers : true,
		queryParams : {
			taskType : taskType
		},
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		pageSize : pageSize,
		pageList : pageList,
		remoteSort : true,
		onLoadSuccess : function(data) {
			$(id).datagrid('resize');
		},
		onDblClickRow : function(index, data) {
			handleFirstApprove(data.loanNo, data.version, data.personName, data.checkPersonCode);
		},
		rowStyler : function(index, row) {
			var a = moment();
			var b = moment(row.auditEndTime).add(3, "days");
			if (a.diff(b) >= 0) {
				return 'background-color:#ffffcc;color:#000;';
			}
		},
		columns : [ [ {
			field : 'ifPri',
			title : '案件标识',
			width : 200,
			align : 'center',
			formatter : formatLoanMark
		}, {
			field : 'personName',
			title : '申请人姓名',
			sortable : true,
			width : 120,
			formatter: function (value, row, index) {
				if (getOldCardIdExists(row.idNo)) {
					value = "*" + value;
				}
				return value;
			}
		}, {
			field : 'productName',
			title : '借款产品',
			sortable : true,
			width : 200,
            formatter:formatterProduct,
		}, {
			field : 'applyLmt',
			title : '申请额度',
			width : 200,
			formatter : numberFomatter,
			sortable : true,
		}, {
			field : 'owningBrance',
			title : '营业部',
			width : taskType == 2 ? 400 : 200,
			sortable : true,
		}, {
			field : 'auditEndTime',
			title : '提交时间',
			width : 200,
			sortable : true,
			formatter : ctx.getDate,
		}, {
			field : 'applyType',
			title : '申请类型',
			width : 150,
			sortable : true,
		}, {
			field : 'refNodeStatus',
			title : '状态',
			hidden : taskType == 2 ? true : false,
			width : 200,
			sortable : true,
			formatter : formatWorkbenchRtfNodeState
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				return '<a href="javaScript:void(0);" onclick=handleFirstApprove("' + data.loanNo + '","' + data.version + '","' + data.personName + '","' + data.checkPersonCode + '")><div class="icon_14_div"><img class="icon_14"></div>办理</a>';
			}
		} ] ]
	});
}
/**
 * 已完成任务列表
 * @param id-控件id
 */
function initFirstApproveFinishDatagrid(id) {
	$(id).datagrid({
		url : ctx.rootPath() + '/firstApprove/firstFinishWorkbench',
		title : '已完成',
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : false,
		queryParams : $("#firstApprove_finish_form").serializeObject(),
		scrollbarSize : 0,
		remoteSort : true,
		onDblClickRow : function(index, data) {
			firstApproveLook(data.loanNo);
		},
		onLoadSuccess : function(data) {
			$(id).datagrid('resize');
		},
		columns : [ [ {
			field : 'ifPri',
			title : '案件标识',
			width : 80,
			align : 'center',
			formatter : formatLoanMark
		}, {
			field : 'personName',
			title : '申请人姓名',
			width : 120,
			sortable : true,
		}, {
			field : 'loanNo',
			title : '借款编号',
			width : 200,
            sortable : true,
		}, {
			field : 'owningBrance',
			title : '营业部',
			width : 200,
			sortable : true,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 120,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'applyType',
			title : '申请类型',
			width : 120,
			sortable : true,
		}, {
			field : 'historNodeStatus',
			title : '初审操作',
			width : 120,
			formatter : function(value, data, index) {
				if ("XSCS-PASS" == value) {// 通过
					return "初审通过";
				} else if ("HIGH-PASS" == value) {// 提交高审
					return "初审提交高审";
				} else if ("XSCS-REJECT" == value) {// 拒绝
					return "初审拒绝";
				} else if ("XSCS-RETURN" == value) {// 退回
					return "初审退回";
				} else if ("XSCS-ZDQQRETURN" == value){
					return "初审退回前前";
				} else {
					return "";
				}
			},
		}, {
			field : 'refNodeStatus',
			title : '当前状态',
			width : 120,
			formatter : function(value, data, index) {
				return formatRtfNodeState(value, data, index);
			},
		}, {
			field : 'csStartDate',
			title : '开始时间',
			width : 200,
			sortable : true,
			formatter : formatDate,
		}, {
			field : 'operationTime',
			title : '完成时间',
			width : 200,
			sortable : true,
			align : 'center',
			formatter : formatDate,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				return '<a href="javaScript:void(0);" onclick=firstApproveLook("' + data.loanNo + '")><i class="fa fa-pencil" aria-hidden="true"></i>查看</a>';
			}
		} ] ]
	});
}
/**
 * 已完成页面搜索
 * 
 * @author dmz
 * @date 2017年4月13日
 */
function firstApproveSearch() {
	if ($("#firstApprove_finish_form").form("validate")) {
		$("#firstApprove_finishTask_datagrid").datagrid("load", $("#firstApprove_finish_form").serializeObject());
	}
}

// 借款单办理 edit by zw at 2017-04-28 初审工作台高权限人员不可以操作非自己的订单 修改
function handleFirstApprove(id, version, name, checkPersonCode) {
	var staffCode = $.trim($("#staffCode").val());
	if (staffCode == checkPersonCode) {
		jDialog.open({
			url: ctx.rootPath() + "/firstApprove/firstApproveReceive/" + id + "/" + version,
			width: 1400,
			height: 800,
			top:-1,
			data: jDialog.openedWindows
		});
	} else {
		$.info("提示", "只能办理自己审核的申请单!");
	}
}

/**
 * 打开办理页面(不带操作按钮)
 * 
 * @param id
 */
function firstApproveLook(loanNo) {
	jDialog.open({url: ctx.rootPath() + "/search/handle/" + loanNo + "/3"});
}