$(function() {
	// 初始化申请历史信息
	applyHistoryDataGridInit();

	// 反欺诈信息匹配
	post(ctx.rootPath() + "/bdsApi/matchAntiFraudInfo/" + $("#finishInsideMatch_loanNo").html(), null, "json", function(data) {
		if (data.success) {
			if (isNotNull(data.dataList)) {
				$.each(data.dataList, function(ind, val) {
					$("#finishInsideMatch_cheatInfo_table").append('<tr><td>' + val.source + '</td><td>' + val.matchField + '</td><td>' + val.matchInfo + '</td><td>' + val.reason + '</td></tr>');
				});
			} else {
				$("#finishInsideMatch_cheatInfo_table").append('<tr><td colspan="4">' + data.firstMessage + '</td></tr>')
			}
		} else {
			$("#finishInsideMatch_cheatInfo_table").append('<tr><td colspan="4">' + data.firstMessage + '</td></tr>');
		}
	});

	var loanNo = $("#finishInsideMatch_loanNo").html();
	var name = document.getElementById("name").value;
	var idNo = document.getElementById("idNo").value;
	var corpName = document.getElementById("corpName").value;
	var addressType = $('#addressType').combobox("getValue");
	var addressInfo = document.getElementById("estateAddress").value;
	var licensePlateNum = document.getElementById("licensePlateNum").value;
	initFinishInsideMatchPhoneNumDatagrid(loanNo);
	$('#finishInsideMatch_tabs').tabs({
		onSelect : function(title, index) {
			if (1 == index) {
				initFinishInsideMatchNameAndIdDatagrid(loanNo, name, idNo);
				initFinishInsideMatchCorpNameDataGrid(loanNo, corpName);
				initFinishInsideMatchAddressDataGrid(loanNo, addressType, addressInfo);
				initFinishInsideMatchVehicleNoDataGrid(loanNo, licensePlateNum);
			}
		}
	});
});
// 窗口关闭时需要关闭其他办理窗口
window.onbeforeunload = function() {
	if (isNotNull(insideMatchContrastHTMLWindow)) {// 关闭历史匹配
		insideMatchContrastHTMLWindow.close();
	}
	closeHTMLWindowList(markFinishInsideMatchLoanNo);// 关闭查看详情
	closeHTMLWindowList(finishInsideMatchQueryViewDetailsLoanNo);// 还款明细
	window.opener.insideMatchHTMLWindow = null;
};
/**
 * 初始化姓名身份证datagrid
 */
function initFinishInsideMatchNameAndIdDatagrid(loanNum, name, idNo) {
	$("#finishInsideMatch_nameAndId_table").datagrid({
		url : ctx.rootPath() + '/finishInsideMatch/matchByNameAndIdCard',
		queryParams : {
			loanNum : loanNum,
			name : name,
			idNo : idNo
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			if (data.total <= 0) {
				$(this).datagrid('appendRow', {
					'name' : '无匹配记录'
				});
				$(this).datagrid('mergeCells', {
					index : 0,
					field : 'name',
					colspan : 14
				});
			}
		},
		columns : [ [ {
			field : 'name',
			title : '姓名',
			width : 80,
		}, {
			field : 'idNo',
			title : '身份证号码',
			width : 80,
		}, {
			field : 'relation',
			title : '关系',
			width : 60,
		}, {
			field : 'matchFieldName',
			title : '被匹配对应信息',
			width : 120,
		}, {
			field : 'unitName',
			title : '单位名称',
			width : 80,
		}, {
			field : 'matchName',
			title : '被匹配客户',
			width : 80,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 130,
			align : 'center',
		// formatter : formatDate
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 70,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 70,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
            formatter:formatterProduct,
		}, {
			field : 'owningBranch',
			title : '进件营业部',
			width : 100,
		}, {
			field : 'branchManager',
			title : '客户经理',
			width : 80,
		}, {
			field : 'action',
			title : '操作',
			width : 220,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});

}

function compare(str) {
	if ('' != str && null != str) {
		return str.indexOf('正常') >= 0 || str.indexOf('结清') >= 0 || str.indexOf('逾期') >= 0;
	}
	return false;
}

/**
 * 初始化单位名称datagrid
 */
function initFinishInsideMatchCorpNameDataGrid(loanNum, unitName) {
	$("#finishInsideMatch_corpName_table").datagrid({
		url : ctx.rootPath() + '/finishInsideMatch/matchByUnitName',
		queryParams : {
			loanNum : loanNum,
			unitName : unitName
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			if (data.total <= 0) {
				$(this).datagrid('appendRow', {
					'matchUnitName' : '无匹配记录'
				});
				$(this).datagrid('mergeCells', {
					index : 0,
					field : 'matchUnitName',
					colspan : 11
				});
			}
		},
		columns : [ [ {
			field : 'unitName',
			title : '单位名称',
			width : 80,
			hidden : true,
		}, {
			field : 'matchUnitName',
			title : '被匹配单位名称',
			width : 100,
		}, {
			field : 'matchFieldName',
			title : '被匹配对应字段',
			width : 120,
		}, {
			field : 'matchName',
			title : '被匹配客户',
			width : 80,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 140,
		// formatter : formatDate
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 60,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 60,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
            formatter:formatterProduct,
		}, {
			field : 'owningBranch',
			title : '进件营业部',
			width : 80,
		}, {
			field : 'branchManager',
			title : '客户经理',
			width : 80,
		}, {
			field : 'action',
			title : '操作',
			width : 220,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});

}

/**
 * 单位名称匹配搜索
 */
function uintNameQuery() {
	var loanNum = $("#finishInsideMatch_loanNo").html();
	var unitName = document.getElementById("unitName").value;
	var queryParams = new Object();
	queryParams.unitName = unitName;
	queryParams.loanNum = loanNum;
	$("#finishInsideMatch_corpName_table").datagrid('load', queryParams);
}

/**
 * 初始化地址信息datagrid
 */
function initFinishInsideMatchAddressDataGrid(loanNum, addressType, addressInfo) {
	$("#finishInsideMatch_address_table").datagrid({
		url : ctx.rootPath() + '/finishInsideMatch/matchByAddressInfo',
		queryParams : {
			loanNum : loanNum,
			addressType : addressType,
			addressInfo : addressInfo
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			if (data.total <= 0) {
				$(this).datagrid('appendRow', {
					'fieldName' : '无匹配记录'
				});
				$(this).datagrid('mergeCells', {
					index : 0,
					field : 'fieldName',
					colspan : 12
				});
			}
		},
		columns : [ [ {
			field : 'addressInfo',
			title : '地址信息',
			width : 80,
			hidden : true,
		}, {
			field : 'fieldName',
			title : '字段名称',
			width : 80,
		}, {
			field : 'matchAddressInfo',
			title : '被匹配地址信息',
			width : 100,
		}, {
			field : 'matchFieldName',
			title : '被匹配对应字段',
			width : 120,
		}, {
			field : 'matchName',
			title : '被匹配客户',
			width : 80,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 140,
		// formatter : formatDate
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 70,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 60,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
            formatter:formatterProduct,
		}, {
			field : 'owningBranch',
			title : '进件营业部',
			width : 80,
		}, {
			field : 'branchManager',
			title : '客户经理',
			width : 80,
		}, {
			field : 'action',
			title : '操作',
			width : 220,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});

}

function changeAddressInfo(newValue) {
	if ('1' == newValue) {
		$("#addressInfo").textbox('setValue', $("#estateAddress").val());
	} else if ('2' == newValue) {
		$("#addressInfo").textbox('setValue', $("#corpAddress").val());

	} else if ('3' == newValue) {
		$("#addressInfo").textbox('setValue', $("#homeAddress").val());
	}
}

/**
 * 地址匹配搜索
 */
function addressQuery() {
	var loanNum = $("#finishInsideMatch_loanNo").html();
	var addressType = $("#addressType").combobox('getValue');
	var addressInfo = document.getElementById("addressInfo").value;
	if (addressType == "" || addressType == null) {
		$.info("提示", "请选择地址类型!", "info", 2000);
		return;
	}
	var queryParams = new Object();
	queryParams.addressType = addressType;
	queryParams.addressInfo = addressInfo;
	queryParams.loanNum = loanNum;
	$("#finishInsideMatch_address_table").datagrid('load', queryParams);
}

/**
 * 初始化车牌号datagrid
 */
function initFinishInsideMatchVehicleNoDataGrid(loanNum, licensePlateNum) {
	$("#finishInsideMatch_vehicleNo_table").datagrid({
		url : ctx.rootPath() + '/finishInsideMatch/matchByLicensePlate',
		queryParams : {
			loanNum : loanNum,
			licensePlateNum : licensePlateNum
		},
		striped : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			if (data.total <= 0) {
				$(this).datagrid('appendRow', {
					'licensePlateNum' : '无匹配记录'
				});
				$(this).datagrid('mergeCells', {
					index : 0,
					field : 'licensePlateNum',
					colspan : 11
				});
			}
		},
		columns : [ [ {
			field : 'licensePlateNum',
			title : '车牌号',
			width : 80,
		}, {
			field : 'matchLicensePlateNum',
			title : '被匹配车牌号',
			width : 80,
		}, {
			field : 'name',
			title : '被匹配客户',
			width : 80,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 140,
			align : 'center',
		// formatter : formatDate
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 70,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 70,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
            formatter:formatterProduct,
		}, {
			field : 'owningBranch',
			title : '进件营业部',
			width : 80,
		}, {
			field : 'branchManager',
			title : '客户经理',
			width : 80,
		}, {
			field : 'action',
			title : '操作',
			width : 210,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});

}

/**
 * 新增电核记录
 * 
 * @param obj
 */
function telephoneSummaryAddTelephoneRecordDialog(phoneNumber, correspondingInfo) {
	$("#telephoneSummary_addTelephoneRecord_Dialog").removeClass("display_none").dialog({
		title : "电核记录",
		modal : true,
		width : 600,
		height : 270,
		onBeforeOpen : function() {
			$("#telephoneSummary_addTelephoneRecord_Form").form('clear');
			$("#tel_phone").val(phoneNumber);
			$("#tel_name").val(correspondingInfo);
			$("#tel_date").val(getLocalTime(Date.parse(new Date())));
		},
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-check',
			handler : function() {
				var validata = $("#telephoneSummary_addTelephoneRecord_Form").form('validate');
				if (validata) {
					var params = $("#telephoneSummary_addTelephoneRecord_Form").serializeObject();
					params.loanNo = document.getElementById("loanNum").value;
					post(ctx.rootPath() + '/firstTelephoneSummary/addTelephoneRecord', params, 'json', function(data) {
						if (data.success) {
							// 关闭当前对话框
							$("#telephoneSummary_addTelephoneRecord_Dialog").dialog("close");
						} else {
							$.info("error", data.firstMessage);
						}
					});
				}

			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#telephoneSummary_addTelephoneRecord_Dialog").dialog("close");
			}
		} ]
	});
}

/**
 * 申请历史对比
 * 
 * @Author LuTing
 * @date 2017年3月2日
 */
function finishInsideMatchContrastDialog() {
	var loanNo = $("#finishInsideMatch_loanNo").html();
	jDialog.open({url: ctx.rootPath() + '/firstInsideMatch/firstContrastHistory/' + loanNo, width : 1300, height : 600});
	// 用初始内批后续
	/*
	 * $("#finishInsideMatch_contrast_Dialog").removeClass("display_none").dialog({
	 * title : "申请历史比对", modal : true, width : 1300, height : 600, onClose :
	 * function() {
	 * $("#finishInsideMatch_duplicateContactsInfos_table").html('');
	 * $("#finishInsideMatch_customerRelativesInfos_table").html('');
	 * $("#finishInsideMatch_customerInfo_table").html(''); } });
	 * applyHistoryDataComparison();
	 */
}

/**
 * 日志
 * 
 * @Author shipf
 * @date
 */
function finishInsideMatchLogDialog(loanNo) {
	$("#finishInsideMatch_log_Dialog").removeClass("display_none").dialog({
		title : "日志",
		modal : true,
		width : 800,
		height : 600,
		href : ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
		buttons : [ {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#finishInsideMatch_log_Dialog").dialog("close");
			}
		} ]
	})
}

/**
 * 还款明细
 * 
 * @Author shipf
 * @date
 */
function finishInsideMatchViewDetailsDialog(loanNo) {
	jDialog.open({url : ctx.rootPath() + "/search/integratedQueryViewDetails/" + loanNo});
}

/**
 * 影像对比
 * 
 * @Author LuTing
 * @date 2017年3月2日
 */
function finishInsideMatchImageContrastDialog(picImageComUrl, finalApprovalCompare, sysCode, operator, jobNumber) {
	if ($("input[name='Fruit_finish']:checked").length != 2) {
		insideMatchImage();
		return false;
	}
	// add by zw at 2017-05-06 获取选中的两个比对申请单编号
	var comparisons = $("input[name='Fruit_finish']:checked");
	var appNo1 = comparisons[0].value;
	var appNo2 = comparisons[1].value;
	// end at 2017-05-06
	var ifm = '<iframe src="' + picImageComUrl + '/api/filedata/compareTo?nodeKey=' + finalApprovalCompare + '&sysName=' + sysCode + '&appNo=' + appNo1 + '&appNo2=' + appNo2 + '&title=本次申请&title2=历史申请&operator=' + operator + '&jobNumber=' + jobNumber + '" style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>';
	$("#finishInsideMatch_imageContrast_Dialog").removeClass("display_none").dialog({
		title : "影像资料对比",
		modal : true,
		width : 1200,
		height : 680,
		content : ifm,
	})
}

function insideMatchImage() {
	$.messager.confirm('提示', '请勾选两条需比对的申请记录！?', function(r) {
		if (r) {
			return false;
		}
	});
}

/**
 * 打开办理页面（不带操作按钮）
 * 
 * @param loanNo
 */
function finishInsideMatchDetails(loanNo) {
	jDialog.open({url : ctx.rootPath() + "/search/handle/" + loanNo + "/5"});
}

// add by zw at 2017-05-03 历史申请信息数据加载
function applyHistoryDataGridInit(type) {
	// idNo = $("#idNo").html();
	$("#finishInside_applyHistory_dataGrid").datagrid({
		url : ctx.rootPath() + '/firstInsideMatch/matchApplicationHistory/' + $("#finishInsideMatch_loanNo").html(),
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'loanNum',
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		remoteSort : false,
		onLoadSuccess : function(data) {
			$("#finishInside_applyHistory_dataGrid").datagrid('resize');
			if (data.total == 1 || data.total == 0) {// 若只有一条数据即只有本次申请就隐藏比对按钮
				$('#finishInsideMatch_comparison_a').hide();
			} else {
				$('#finishInsideMatch_comparison_a').show();
			}

		},
		columns : [ [ {
			field : 'loanNum',
			title : '借款编号',
			align : 'center',
			width : 130,
		// formatter : formatLoanMark
		}, {
			field : 'applyDate',
			title : '申请/进件时间',
			width : 100,
			sortable : true,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 90,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'corpName',
			title : '单位名称',
			// width : 170,
			sortable : true,
			formatter : function(value, data, index) {
				if (isEmpty(data.corpName)) {
					return "";
				} else {
					return '<span title="' + data.corpName + '" class="easyui-tooltip">' + data.corpName + '</span>';
				}
			}
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 100,
			sortable : true,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 220,
			sortable : true,
			formatter : numberFomatter
		}, {
			field : 'settlementType',
			title : '结清状态',
			width : 90,
			sortable : true,
		}, {
			field : 'applyStore',
			title : '申请门店',
			width : 120,
			sortable : true,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});
}
// end at 2017-05-03
// add by zw at 2017-05-05 申请历史信息比对
function applyHistoryDataComparison() {
	var loanNo = $("#finishInsideMatch_loanNo").html();
	post(ctx.rootPath() + '/firstInsideMatch/getCustomerKeyInformation/' + loanNo, null, "json", function(data) {
		if (data.type == "SUCCESS") {

			// 客户本人基本信息table
			var customerBasicInfosList = data.data.customerBasicInfos;
			//
			$('#finishInsideMatch_customerInfo_table').append('<thead><tr><td></td><td>本次申请&nbsp;<input name="Fruit_finish" value="' + loanNo + '" type="checkbox" /></td></tr></thead><tr><td>申请时间</td></tr><tr><td>借款状态</td></tr><tr><td>常用手机</td></tr><tr><td>单位名称</td></tr><tr><td>单位地址</td></tr><tr><td>单位电话</td></tr><tr><td>家庭地址</td></tr>');
			var index = 0;
			$.each(customerBasicInfosList, function(key, v) {
				if (loanNo != v.loanNum) {
					$('#finishInsideMatch_customerInfo_table tr:eq(0)').append('<td>历史申请&nbsp;' + index + '<input name="Fruit_finish" value="' + v.loanNum + '" type="checkbox" /></td>');
				}
				$('#finishInsideMatch_customerInfo_table tr:eq(1)').append('<td>' + v.applyDate + '</td>');
				$('#finishInsideMatch_customerInfo_table tr:eq(2)').append('<td>' + v.applyStatus + '</td>');
				if (!isNotNull(v.phoneNumber2)) {
					$('#finishInsideMatch_customerInfo_table tr:eq(3)').append('<td>' + v.phoneNumber1 + '</td>');
				} else {
					$('#finishInsideMatch_customerInfo_table tr:eq(3)').append('<td>' + v.phoneNumber1 + '<br>' + v.phoneNumber2 + '</td>');
				}
				$('#finishInsideMatch_customerInfo_table tr:eq(4)').append('<td>' + v.corpName + '</td>');
				$('#finishInsideMatch_customerInfo_table tr:eq(5)').append('<td>' + v.corpAddress + '</td>');
				var corpPhone = v.corpPhone1;
				if (!isNotNull(v.corpPhone2)) {
					$('#finishInsideMatch_customerInfo_table tr:eq(6)').append('<td>' + v.corpPhone1 + '</td>');
				} else {
					$('#finishInsideMatch_customerInfo_table tr:eq(6)').append('<td>' + v.corpPhone1 + '<br>' + v.corpPhone2 + '</td>');
				}
				$('#finishInsideMatch_customerInfo_table tr:eq(7)').append('<td>' + v.homeAddress + '</td>');
				// if (!isNotNull(v.estateAddress)) {
				// $('#finishInsideMatch_customerInfo_table
				// tr:eq(8)').append('<td></td>');
				// }else{
				// $('#finishInsideMatch_customerInfo_table
				// tr:eq(8)').append('<td>' + v.estateAddress + '</td>');
				// }
				index++;
			});
			// 客户信息对比标红
			$('#finishInsideMatch_customerInfo_table tr').each(function(indexTr, v) {
				var flag = false;
				if (indexTr > 1) {
					var phone = '';
					var arrPhone = new Array();
					var other = new Array();
					var thisValue;
					$(this).find('td').each(function(num) {
						if (num == 0) {
							thisValue = this;
						}
						if (indexTr == 3 || indexTr == 6) {// 手机号码标红
							if (num == 1) {
								phone = $(this).html();
							} else if (num > 1) {
								// console.info(num+'---------:'+$(this).html())
								arrPhone.push($(this).html());
							}
						} else {// 其他标红取值
							if (num > 0) {
								other.push($(this).html());
							}
						}
					});
					if (isNotNull(phone)) {
						var flag = 0;
						var number = 0;
						$.each(phone.split('<br>'), function(key, v) {
							number++;
							$.each(arrPhone, function(key, vOther) {
								if (vOther.indexOf(v) < 0 || vOther.length != v.length) {
									flag++;
									return false;
								}
							});
						});
						if (number == 2) {
							if (flag == 2) {
								$(thisValue).addClass('markRed');
							}
						} else {
							if (flag == 1) {
								$(thisValue).addClass('markRed');
							}
						}
					}
					if (isNotNull(other)) {
						var firstValue = '';
						var otherIndex = 0;
						for (var i = 0; i < other.length; i++) {
							if (i == 0) {
								firstValue = other[i];
							} else {
								if (other[i] != firstValue) {
									$(thisValue).addClass('markRed');
									break;
								}
							}
						}
					}
				}
			});
			// 客户直系亲属联系人对比finishInsideMatch_customerRelativesInfos_table
			var customerRelativesInfosList = data.data.customerRelativesInfos;

			$("#finishInsideMatch_customerRelativesInfos_table").append('<thead><tr><td>联系人</td><td>相应字段</td></tr></thead>');

			$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>配偶</td><td>姓名</td></tr>');
			$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>配偶</td><td>手机</td></tr>');
			$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>配偶</td><td>工作单位</td></tr>');

			for (var i = 0; i < 2; i++) {
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>父母</td><td>姓名</td></tr>');
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>父母</td><td>手机</td></tr>');
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>父母</td><td>工作单位</td></tr>');
			}
			for (var i = 0; i < 3; i++) {
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>子女</td><td>姓名</td></tr>');
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>子女</td><td>手机</td></tr>');
				$("#finishInsideMatch_customerRelativesInfos_table").append('<tr><td>子女</td><td>工作单位</td></tr>');
			}
			index = 0;
			$.each(customerRelativesInfosList, function(key, v) {
				var indexP = 1;
				var indexC = 2;
				for (var i = 1; i < 25; i++) {
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + i + ')').append('<td></td>');
				}

				if (index == 0) {
					$("#finishInsideMatch_customerRelativesInfos_table tr:eq(0)").append('<td>本次申请&nbsp;</td>');
				} else {
					$("#finishInsideMatch_customerRelativesInfos_table tr:eq(0)").append('<td>历史申请&nbsp;' + index + '</td>');
				}
				var spouse = v.spouse[0];
				if (isNotNull(spouse)) {
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(1)').children("td").eq(Number(index) + 2).text(spouse.name);
					var spousePhone = spouse.phoneNumber1;
					if (spouse.phoneNumber2 != null) {
						spousePhone = spousePhone + '<br>' + spouse.phoneNumber2;
					}
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(2)').children("td").eq(Number(index) + 2).html(spousePhone);
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(3)').children("td").eq(Number(index) + 2).text(spouse.corpName);
				}
				$.each(v.parent, function(key, p) {

					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexP) + 1) + ')').children("td").eq(Number(index) + 2).text(p.name);
					var parentPhone = p.phoneNumber1;
					if (p.phoneNumber2 != null) {
						parentPhone = parentPhone + '<br>' + p.phoneNumber2;
					}
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexP) + 2) + ')').children("td").eq(Number(index) + 2).html(parentPhone);
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexP) + 3) + ')').children("td").eq(Number(index) + 2).text(p.corpName);
					indexP++;
				});
				$.each(v.children, function(key, c) {
					indexC++;
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexC) + 1) + ')').children("td").eq(Number(index) + 2).text(c.name);
					var childrenPhone = c.phoneNumber1;
					if (c.phoneNumber2 != null) {
						childrenPhone = childrenPhone + '<br>' + c.phoneNumber2;
					}
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexC) + 2) + ')').children("td").eq(Number(index) + 2).html(childrenPhone);
					$('#finishInsideMatch_customerRelativesInfos_table tr:eq(' + (3 * Number(indexC) + 3) + ')').children("td").eq(Number(index) + 2).text(c.corpName);

				});

				index++;
			});
			// 去空格

			$("#finishInsideMatch_customerRelativesInfos_table tr").each(function() {
				var tableValue = "";
				$(this).find('td').each(function(num) {
					if (num > 1) {
						tableValue += $(this).text() + "";
					}
				})

				if (!isNotNull(tableValue)) {
					$(this).remove();
				}
			});
			// 客户直系亲属联系人对比标红
			$('#finishInsideMatch_customerRelativesInfos_table tr').each(function(indexTr, v) {
				var flag = false;
				if (indexTr > 0) {
					var phone = '';
					var arrPhone = new Array();
					var other = new Array();
					var contactsValue;
					var fieldValue;
					$(this).find('td').each(function(num) {
						if (num == 0) {
							contactsValue = this;
						}
						if (num == 1) {
							fieldValue = this;
						}
						if (indexTr % 3 == 2) {// 手机号码标红
							if (num == 2) {
								phone = $(this).html();
							} else if (num > 2) {
								arrPhone.push($(this).html());
							}
						} else {// 其他标红取值
							if (num > 1) {
								other.push($(this).html());
							}
						}
					});
					if (isNotNull(phone)) {
						var flag = 0;
						var number = 0;
						$.each(phone.split('<br>'), function(key, v) {
							number++;
							$.each(arrPhone, function(key, vOther) {
								if (vOther.indexOf(v) < 0 || vOther.length != v.length) {
									flag++;
									return false;
								}
							});
						});
						if (number == 2) {
							if (flag == 2) {
								$(contactsValue).addClass('markRed');
								$(fieldValue).addClass('markRed');
							}
						} else {
							if (flag == 1) {
								$(contactsValue).addClass('markRed');
								$(fieldValue).addClass('markRed');
							}
						}
					}
					if (isNotNull(other)) {
						var firstValue = '';
						var otherIndex = 0;
						for (var i = 0; i < other.length; i++) {
							// alert(indexTr+":"+other[i]);
							if (i == 0) {
								firstValue = other[i];
							} else {
								if (other[i] != firstValue) {
									$(contactsValue).addClass('markRed');
									$(fieldValue).addClass('markRed');
									break;
								}
							}
						}
					}
				}
			});
			// end
			var duplicateContactsInfosList = data.data.duplicateContactsInfos;
			index = 1;
			var finish = 0;

			$('#finishInsideMatch_duplicateContactsInfos_table').append('<thead><tr><td>姓名</td></tr></thead>');
			$.each(duplicateContactsInfosList, function(key, d) {
				$('#finishInsideMatch_duplicateContactsInfos_table').append('<tr><td>' + d.name + '</td></tr>');
				var duplicateContactsInfos = d.contacts.split('|');
				$.each(duplicateContactsInfos, function(key, v) {
					if (index == 1) {
						if (finish == 0) {
							$("#finishInsideMatch_duplicateContactsInfos_table tr:eq(0)").append('<td>本次申请&nbsp;</td>');
						} else {
							$("#finishInsideMatch_duplicateContactsInfos_table tr:eq(0)").append('<td>历史申请&nbsp;' + finish + '</td>');
						}
						finish++;
					}
					$("#finishInsideMatch_duplicateContactsInfos_table tr:eq(" + index + ")").append('<td>' + v + '</td>');
				});
				index++;
			})

		} else {
			if (isNotNull(data.login_status) && data.login_status == 300) {
				$.info("提示", data.message, "error");
			} else {
				$.info("提示", data.firstMessage, "error");
			}
		}
	});
}
// end at 2017-05-05

function initFinishInsideMatchPhoneNumDatagrid(loanNum) {
	$("#finishInsideMatch_phoneNum_table").datagrid({
		url : ctx.rootPath() + '/finishInsideMatch/matchByPhoneNumber/' + loanNum,
		striped : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		fitColumns : false,
		scrollbarSize : 0,
		onLoadSuccess : function(data) {
			if (data.total <= 0) {
				$(this).datagrid('appendRow', {
					'phoneNumber' : '无匹配记录'
				});
				$(this).datagrid('mergeCells', {
					index : 0,
					field : 'phoneNumber',
					colspan : 14
				});
			} else {
				// $(id).datagrid('resize');
				var n = 0;
				var t = 1;
				var m = "";
				var merges = new Array();
				for (var int = 0; int < data.rows.length; int++) {
					if (m == data.rows[int].phoneNumber) {
						merges[n].rowspan = merges[n].rowspan + 1;
					} else {
						var s = new Object();
						s.index = int;
						s.rowspan = t;
						merges.push(s);

						if (m != "") {
							n = n + 1;
						}
						m = data.rows[int].phoneNumber;
						t = 1;
					}
				}
				console.log(JSON.stringify(merges));
				for (var i = 0; i < merges.length; i++) {
					$(this).datagrid('mergeCells', {
						index : merges[i].index,
						field : 'phoneNumber',
						rowspan : merges[i].rowspan
					});
					$(this).datagrid('mergeCells', {
						index : merges[i].index,
						field : 'fieldName',
						rowspan : merges[i].rowspan
					});
					$(this).datagrid('mergeCells', {
						index : merges[i].index,
						field : 'correspondingInfo',
						rowspan : merges[i].rowspan
					});
				}
			}
		},
		columns : [ [ {
			field : 'phoneNumber',
			title : '匹配号码',
			width : 90,
		}, {
			field : 'fieldName',
			title : '字段名称',
			width : 90,
		}, {
			field : 'correspondingInfo',
			title : '对应信息',
			width : 80,
		}, {
			field : 'matchFieldName',
			title : '被匹配字段名称',
			width : 90,
		}, {
			field : 'matchCorrespondingInfo',
			title : '被匹配对应信息',
			width : 100,
			formatter : function(value, data, index) {
				if (isEmpty(data.matchCorrespondingInfo)) {
					return "";
				} else {
					if ('1' == data.matchCorrespondingInfoSetRed) {
						return '<span style="color:red;font-weight:bold;" title="' + data.matchCorrespondingInfo + '" class="easyui-tooltip">' + data.matchCorrespondingInfo + '</span>';
					} else {
						return '<span title="' + data.matchCorrespondingInfo + '" class="easyui-tooltip">' + data.matchCorrespondingInfo + '</span>';
					}
				}
			}
		}, {
			field : 'matchName',
			title : '被匹配客户',
			width : 80,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 120,
			align : 'center',
		// formatter : formatDate
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
            formatter:formatterProduct,
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 70,
			formatter : function(value, data, index) {
				if ('1' == data.loanStatusSetRed) {
					return '<span style="color:red;font-weight:bold;">' + data.loanStatus + '</span>';
				} else {
					return data.loanStatus;
				}
			}
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 70,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'owningBranch',
			title : '进件营业部',
			width : 100,
			formatter : function(value, data, index) {
				if (isEmpty(data.owningBranch)) {
					return "";
				} else {
					return '<span title="' + data.owningBranch + '" class="easyui-tooltip">' + data.owningBranch + '</span>';
				}
			}
		}, {
			field : 'source',
			title : '反欺诈匹配',
			width : 80,
			formatter : function(value, data, index) {
				if (isEmpty(data.source)) {
					return "";
				} else {
					return '<span style="color:red;">' + data.source + '</span>';
				}
			}
		}, {
			field : 'action',
			title : '操作',
			width : 220,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '<a href="javaScript:void(0);" onclick=finishInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=finishInsideMatchViewDetailsDialog("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});

}
// 日志备注对话框
function finishInsideMatchLogInfo(loanNo) {
	jDialog.open({
		url : ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo,
		width : 1100,
		height : 800
	});
}
// 审批意见对话框
function finishInsideMatchApprovalOpinion(loanNo) {
	jDialog.open({url : ctx.rootPath() + '/finishApprove/finishApprovalOpinionWithoutAction/' + loanNo});
}