$(function() {
	//  申请历史信息
	applyHistoryDataGridInit();
	var loanNo = $("#firstInsideMatch_loanNo").val();
	var idNo = $("#firstInsideMatch_applyInfo_idNo").html();
	var name = document.getElementById("name").value;
	var corpName = document.getElementById("corpName").value;
	var addressType = $('#addressType').combobox("getValue");
	var addressInfo = document.getElementById("estateAddress").value;
	var licensePlateNum = document.getElementById("licensePlateNum").value;
	initMatchPhoneDatagrid(loanNo);
	$('#firstInsideMatch_phoneMatch_tabs').tabs({
		onSelect : function(title, index) {
			if (1 == index) {
				initFirstInsideMatchNameAndIdDatagrid(loanNo, name, idNo);
				initFirstInsideMatchCorpNameDataGrid(loanNo, corpName);
				initFirstInsideMatchAddressDataGrid(loanNo, addressType, addressInfo);
				initFirstInsideMatchVehicleNoDataGrid(loanNo, licensePlateNum);
			}
		}
	});
	// 反欺诈信息匹配
	post(ctx.rootPath() + "/bdsApi/matchAntiFraudInfo/" + $("#firstInsideMatch_loanNo").val(), null, "json", function(data) {
		if (data.success) {
			if (isNotNull(data.dataList)) {
				$.each(data.dataList, function(ind, val) {
					$("#firstInsideMatch_cheatInfo_table").append('<tr><td>' + val.source + '</td><td>' + val.matchField + '</td><td>' + val.matchInfo + '</td><td>' + val.reason + '</td></tr>');
				});
			} else {
				$("#firstInsideMatch_cheatInfo_table").append('<tr><td colspan="4">' + data.firstMessage + '</td></tr>')
			}
		} else {
			$("#firstInsideMatch_cheatInfo_table").append('<tr><td colspan="4">' + data.firstMessage + '</td></tr>');
		}
	});
});

/**
 * 申请历史比对
 * 
 * @author dmz
 * @date 2017年7月5日
 */
function insideMatchContrastDialog() {
	jDialog.open({url : ctx.rootPath() + '/firstInsideMatch/firstContrastHistory/' + $("#firstInsideMatch_loanNo").val(), width : 1300});
}

/**
 * 内部匹配--日志列表
 * 
 * @Author fusj
 * @date 2017年3月2日
 */
function insideMatchViewDialog() {
	var insideMatchViewDialog = ctx.dialog({
		title : '日志',
		modal : true,
		width : 800,
		height : 600,
		href : ctx.rootPath() + '/firstInsideMatch/insideMatchViewInfo',
		buttons : [ {
			text : '关闭',
			iconCls : 'fa fa-reply',
			handler : function() {
				insideMatchViewDialog.dialog("close");
			}
		} ]
	});
}
/**
 * 历史申请信息数据加载
 */
function applyHistoryDataGridInit() {
	$("#firstInside_applyHistory_dataGrid").datagrid({
		url : ctx.rootPath() + '/firstInsideMatch/matchApplicationHistory/' + $("#firstInsideMatch_loanNo").val(),
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'loanNum',
		pagination : false,
		fitColumns : true,
		scrollbarSize : 0,
		remoteSort : false,
		onLoadSuccess : function(data) {
			$("#firstInside_applyHistory_dataGrid").datagrid('resize');
			if (data.total == 1 || data.total == 0) {// 若只有一条数据即只有本次申请就隐藏比对按钮
				$('#firstInsideMatch_comparison_a').hide();
			} else {
				$('#firstInsideMatch_comparison_a').show();
			}
		},
		columns : [ [ {
			field : 'loanNum',
			title : '借款编号',
			align : 'center',
			width : 130,
		}, {
			field : 'applyDate',
			title : '申请/进件时间',
			width : 100,
			sortable : true,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 70,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'corpName',
			title : '单位名称',
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
			width : 70,
			sortable : true,
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 70,
			sortable : true,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 200,
			sortable : true,
			formatter : numberFomatter
		}, {
			field : 'settlementType',
			title : '结清状态',
			width : 100,
			sortable : true,
		}, {
			field : 'applyStore',
			title : '进件营业部',
			width : 160,
			sortable : true,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '&nbsp;<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});
}
/**
 * 打开办理页面(不带操作按钮)
 * 
 * @param loanNo
 */
function firstInsideMatchDetails(loanNo) {
	jDialog.open({url : ctx.rootPath() + "/search/handle/" + loanNo + "/5"});
}

/**
 *  还款明细方法
 * @param loanNo
 */
function firstInsideMatchViewDetails(loanNo) {
	jDialog.open({url : ctx.rootPath() + "/search/integratedQueryViewDetails/" + loanNo});
}
/**
 * 初始化姓名身份证datagrid
 * @param loanNum
 * @param name
 * @param idNo
 */
function initFirstInsideMatchNameAndIdDatagrid(loanNum, name, idNo) {
	$("#firstInsideMatch_nameAndId_table").datagrid({
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
			width : 80,
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
			width : 100,
		}, {
			field : 'branchManager',
			title : '客户经理',
			width : 80,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '&nbsp;<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});
}

/**
 * 初始化单位名称datagrid
 * @param loanNum
 * @param unitName
 */
function initFirstInsideMatchCorpNameDataGrid(loanNum, unitName) {
	$("#firstInsideMatch_corpName_table").datagrid({
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
		}, {
			field : 'loanStatus',
			title : '借款状态',
			width : 80,
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
			width : 80,
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
			width : 190,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '&nbsp;<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
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
	var queryParams = new Object();
	queryParams.unitName = document.getElementById("unitName").value;
	queryParams.loanNum = $("#firstInsideMatch_loanNo").val();
	$("#firstInsideMatch_corpName_table").datagrid('load', queryParams);
}

/**
 * 初始化地址信息datagrid
 */
function initFirstInsideMatchAddressDataGrid(loanNum, addressType, addressInfo) {
	$("#firsthInsideMatch_address_table").datagrid({
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
			width : 70,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '&nbsp;<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
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
	var addressType = $("#addressType").combobox('getValue');
	var addressInfo = document.getElementById("addressInfo").value;
	if (addressType == "" || addressType == null) {
		$.info("提示", "请选择地址类型!", "info", 2000);
		return;
	}
	var queryParams = new Object();
	queryParams.addressType = addressType;
	queryParams.addressInfo = addressInfo;
	queryParams.loanNum = $("#firstInsideMatch_loanNo").val();
	$("#firsthInsideMatch_address_table").datagrid('load', queryParams);
}

/**
 * 初始化车牌号datagrid
 */
function initFirstInsideMatchVehicleNoDataGrid(loanNum, licensePlateNum) {
	$("#firstInsideMatch_vehicleNo_table").datagrid({
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
			width : 80,
		}, {
			field : 'approvalConclusion',
			title : '审批结论',
			width : 80,
			formatter : numberFomatter
		}, {
			field : 'businessLink',
			title : '业务环节',
			width : 80,
		}, {
			field : 'productName',
			title : '借款产品',
			width : 80,
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
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				action = action + '&nbsp;<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
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
var addTelephoneRecordHTMLWindow = null;
function addPhoneRecordDialog(phoneNumber) {
	var loanNo = $('#firstInsideMatch_loanNo').val();
	if (!_.isEmpty(jDialog.openedWindows)) {
		$.messager.alert('提示', '不可同时添加多条电核记录', 'warning', function() {
			addTelephoneRecordHTMLWindow.focus();
		});
	} else {
        addTelephoneRecordHTMLWindow = jDialog.open({
			url : ctx.rootPath() + '/firstTelephoneSummary/addTelephoneRecordWin?telPhone=' + phoneNumber + '&loanNo=' + loanNo,
			width : 900,//宽度不可更改，不然联系人添加电核记录时页面布局会乱
			height : 360
		});
	}
}

/**
 * 判断是否有还款明细
 * 
 * @param str
 * @returns {Boolean}
 */
function compare(str) {
	if ('' != str && null != str) {
		return str.indexOf('正常') >= 0 || str.indexOf('结清') >= 0 || str.indexOf('逾期') >= 0;
	}
	return false;
}
/**
 * 电话号码匹配
 * @param loanNum
 */
function initMatchPhoneDatagrid(loanNum) {
	$("#firstInsideMatch_phoneNum_table").datagrid({
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
			formatter : function(value, data, index) {
				if (value.indexOf('无匹配记录') < 0) {
					return "<a href='javaScript:void(0);' onclick=addPhoneRecordDialog('" + data.phoneNumber + "')>" + data.phoneNumber + "</a>";
				} else {
					return value;
				}
			}
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
			width : 100,
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
			width : 90,
		}, {
			field : 'applyDate',
			title : '被匹配客户申请/进件时间',
			width : 120,
			align : 'center',
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
				action = action + '<a href="javaScript:void(0);" onclick=firstInsideMatchLogInfo("' + data.loanNum + '") >日志</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchApprovalOpinion("' + data.loanNum + '") >审批意见</a>';
				action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchDetails("' + data.loanNum + '") >查看详情</a>';
				if (compare(data.loanStatus)) {
					action = action + '|<a href="javaScript:void(0);" onclick=firstInsideMatchViewDetails("' + data.loanNum + '")>还款明细</a>';
				}
				return action;
			}
		} ] ]
	});
}
// 日志备注对话框
function firstInsideMatchLogInfo(loanNo) {
	jDialog.open({url : ctx.rootPath() + '/firstApprove/logNotesInfo/' + loanNo, width : 1100, height : 800});
}
// 审批意见对话框
function firstInsideMatchApprovalOpinion(loanNo) {
	jDialog.open({url : ctx.rootPath() + '/finishApprove/finishApprovalOpinionWithoutAction/' + loanNo});
}