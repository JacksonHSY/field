//@ sourceURL=finalApprovalOpinion.js
var $approvalTerm;// 加载期限
var $approvalLimit;// 审批金额
var $approvalCheckIncome;// 核实收入
var $approvalProductCd;// 审批产品

var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        loanNo: '',
        approvalInfo: {},
        applyBasicInfo: {},
        applyInfo: {},
        markReportIdChange: '',
        calcWaterAverage: function (water1, water2, water3) {	// 计算月均流水
            var waterAverage = 0;
            waterAverage = (( (water1 || 0) + (water2 || 0) + (water3 || 0)) / 3).toFixed(0);

            return parseInt(waterAverage);
        },
        getApproveCheckStatementList: function (type) {			// 根据流水类型，获取流水明细信息
            var checkStatementList = [];
            if(type){
                for(var i= 0, len= vm.approvalInfo.approveCheckStatementList.length; i < len; i++){
                    var checkStatement = vm.approvalInfo.approveCheckStatementList[i];
                    if(checkStatement.type == type){    // 如果有类型，则根据类型过滤返回流水信息
                        checkStatementList.push(checkStatement);
                    }
                }
            }else{
                checkStatementList = vm.approvalInfo.approveCheckStatementList;
            }

            return checkStatementList;
        },
		getDebtInfoList: function (debtType) {
        	var debtInfoList = [];
        	if(vm.approvalInfo.debtsInfoList){
                for(var i =0, len = vm.approvalInfo.debtsInfoList.length; i < len; i++){
                    var _debtInfo = vm.approvalInfo.debtsInfoList[i];
                    var _creditLoanDebt = _debtInfo.creditLoanDebt; // 信用贷款
                    var _creditLoanTerm = _debtInfo.creditLoanTerm; // 期限
					var _creditLoanLimit = _debtInfo.creditLoanLimit;// 额度
                    if(_debtInfo.debtType == debtType  &&( isNotNull(_creditLoanDebt)  ||  isNotNull(_creditLoanTerm) || isNotNull(_creditLoanLimit))){
                        _debtInfo['className'] =  (_debtInfo.debtType == 'TOPUPLOAN' ? 'topUpTr' : 'creditCardTr');
                        debtInfoList.push(_debtInfo);
                    }
                }
			}

			return debtInfoList;
        },
		isNullOrUndefined: function (value) {
        	// 判断value是否为undefined或者null
			return (_.isUndefined(value) || _.isNull(value));
        },
		numberRound: function (value) {
        	// 浮点数取整
			if(vm.isNullOrUndefined(value)){
				return '';
			}
			// 格式化为整数格式
			return numeral(value).format('00');
        },
        finishApprovalOpinionContrastDialog: function () {	// 征信初判对话框
        	post(ctx.rootPath() + "/creditzx/getCreditJudgment/2/" + vm.loanNo, null, "json", function(result) {
				if (result.success) {
					// 央行征信报告
					var credit = result.data[0];
					if (isNotNull(credit.result)) {
						$("#finishApprovalOpinion_credit_table").append('<tr><td> 征信信息：' + credit.firstMessage + '</td></tr>');
					}
					if (isNotNull(credit.status)) {
						var html = '<tr><th>人行近3个月查询次数:</th><td>' + (isNotNull(credit.threeMonthqueryCount) ? credit.threeMonthqueryCount : "0") + '</td>' + '<th>人行近1个月查询次数:</th><td>' + (isNotNull(credit.oneMonthqueryCount) ? credit.oneMonthqueryCount : "0") + '</td></tr>' + '<tr><th>信用卡总额度:</th><td>' + (isNotNull(credit.creditLimitMoney) ? credit.creditLimitMoney : "0") + '</td>' + '<th>信用卡已用额度:</th><td>' + (isNotNull(credit.alreadyUseMoney) ? credit.alreadyUseMoney : "0") + '</td></tr>' + '<tr><th>信用卡负债:</th><td>' + (isNotNull(credit.debt) ? credit.debt : "0") + '</td></tr>';
						$("#finishApprovalOpinion_credit_table").append(html);
					}
					// 贷款记录
					var loanRecord = result.data[1];
					if (!isNotNull(loanRecord)) {
						$("#finishApprovalOpinion_debt_table").append('<tr><td colspan="5">' + loanRecord.firstMessage + '</td></tr>');
					} else {
						// 信用贷款
						$("#finishApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>信用贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
						if (isNotNull(loanRecord.loan) && loanRecord.loan.length>0) {
							$.each(loanRecord.loan, function(key, v) {
								var trHtml = '<tr><td>信用贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
								$("#finishApprovalOpinion_debt_table").append(trHtml);
							})
						} else {
							$("#finishApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
						}
						// 房贷
						$("#finishApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>房贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
						if (isNotNull(loanRecord.house) && loanRecord.house.length>0) {
							$.each(loanRecord.house, function(key, v) {
								var trHtml = '<tr><td>房贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
								$("#finishApprovalOpinion_debt_table").append(trHtml);
							})
						} else {
							$("#finishApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
						}
						// 车贷
						$("#finishApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>车贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
						if (isNotNull(loanRecord.car) && loanRecord.car.length>0) {
							$.each(loanRecord.car, function(key, v) {
								var trHtml = '<tr><td>车贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
								$("#finishApprovalOpinion_debt_table").append(trHtml);
							})
						} else {
							$("#finishApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
						}
						// 其他贷款
						$("#finishApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>其他贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
						if (isNotNull(loanRecord.other) && loanRecord.other.length>0) {
							$.each(loanRecord.other, function(key, v) {
								var trHtml = '<tr><td>其他贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
								$("#finishApprovalOpinion_debt_table").append(trHtml);
							})
						} else {
							$("#finishApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
						}
					}
				}
        	});

			$("#finishApprovalOpinion_contrast_Dialog").removeClass("display_none").dialog({
				title : "征信初判",
				modal : true,
				width : 600,
				height : 400,
				onClose : function() {
					$("#finishApprovalOpinion_credit_table").html('');// 关闭时清空table
					$("#finishApprovalOpinion_debt_table").html('');
				},
				buttons : [ {
					text : '取消',
					iconCls : 'fa fa-reply',
					handler : function() {// 关闭时清空table
						$("#finishApprovalOpinion_contrast_Dialog").dialog("close");
						$("#finishApprovalOpinion_credit_table").html('');
						$("#finishApprovalOpinion_debt_table").html('');
					}
				} ]
			})
    	},
        $computed: {
            personalCheckStatementArray: function () {
                return vm.getApproveCheckStatementList('C');
            },
            publicCheckStatementArray: function () {
                return vm.getApproveCheckStatementList('B');
            },
            allCheckStatementArray: function () {
                return vm.getApproveCheckStatementList();
            },
            personalWaterAverageTotal: function() {
                // 获取个人月均流水合计
                var waterAverageTotal = 0;
                if(vm.approvalInfo){
                    avalon.each(vm.personalCheckStatementArray, function (index, element) {
                        waterAverageTotal =  waterAverageTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });

                }
                return parseInt(waterAverageTotal.toFixed(0));
            },
            publicWaterAverageTotal: function() {
                // 获取对公月均流水合计
                var waterAverageTotal = 0;
                if(vm.approvalInfo){
                    avalon.each(vm.publicCheckStatementArray, function (index, element) {
                        waterAverageTotal += vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }

                return parseInt(waterAverageTotal.toFixed(0));
            },
            waterIncomeTotal: function() {
                // 获取个人和对公月均流水合计
                var total = 0;
                if(vm.approvalInfo){
                    avalon.each(vm.allCheckStatementArray, function (index, element) {
                        total += vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }

                return parseInt(total.toFixed(0));
            }
        }
    };

    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

avalon.ready(function() {
    // 提示央行报告是否有改变
    if ("Y" == vm.markReportIdChange) {
        $.info("提示", "客户征信报告已更新，请重新填写负债信息", "info", 1000);
    }
	// 个人流水加载
	$("#finishApprovalOpinion_record_table").find("tr.personalRecordTr").each(function(ind, val) {
		$(val).find("input:lt(3)").numberbox({
			min : 0,
			precision : 2,
			buttonText : '元',
			onChange : function() {
				changeRecordCount($(val), "personal");
			}
		});
	});
	// 对公流水加载
	$("#finishApprovalOpinion_record_table").find("tr.publicRecordTr").each(function(ind, val) {
		$(val).find("input:lt(3)").numberbox({
			min : 0,
			precision : 2,
			buttonText : '元',
			onChange : function() {
				changeRecordCount($(val), "public");
			}
		});
	});
	// topUp贷款
	var topUpHtml = $("#finishApprovalOpinion_liabilities_table").find("tr.topUpTr");
	if (topUpHtml != null && topUpHtml.length > 0) {
		topUpHtml.find("input.input:eq(0)").numberbox({
			min : 0,
			precision : 2,
			buttonText : '元',
			required : true,
			onChange : function() {
				changeCalculatedLiabilities(topUpHtml);
			}
		});
		topUpHtml.find("input.input:gt(0)").numberbox({
			min : 0,
			precision : 0,
			buttonText : '月',
			required : true,
			onChange : function() {
				changeCalculatedLiabilities(topUpHtml);
			}
		});
	}
	// 信用贷款
	$("#finishApprovalOpinion_liabilities_table").find("tr.creditCardTr").each(function(ind, val) {
		$(val).find("input.input:eq(0)").numberbox({
			min : 0,
			precision : 2,
			buttonText : '元',
			onChange : function() {
				changeCalculatedLiabilities($(val));
			}
		});
		$(val).find("input.input:gt(0)").numberbox({
			min : 0,
			precision : 0,
			buttonText : '月',
			onChange : function() {
				changeCalculatedLiabilities($(val));
			}
		});
	});
	// 审批意见相关

	// 加载核实收入
	$approvalCheckIncome = $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalCheckIncome']").numberbox({
		required : true,
		validType : [ 'compareMaxMin[1,99999999]' ],
		precision : 0,
		buttonText : '元',
		onChange : function(newValue, oldValue) {
			changeApprovalTerm();
		}
	});

	// 加载审批金额
	// firstApprovalOpinion_approvalLimit = $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalLimit']").numberbox({
	// required : true,
	// min : 0,
	// buttonText : '元',
	// validType : [ 'numberMultiple[1000]' ],
	// onChange : function(newValue, oldValue) {
	// getProductLimit();// 获取审批期限
	// }
	// });

	// 加载审批意见中的申请产品
	$approvalProductCd = $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalProductCd']").combobox({
		required : true,
		editable : false,
		valueField : 'code',
		textField : 'name',
		url : ctx.rootPath() + '/bmsBasiceInfo/getProductListByOrgId/' + $("#finishApprovalOpinion_owningBranchId_hidden").val()+"/"+$("#finishApprovalOpinion_ifPreferentialUser_hidden").val(),
		onChange : function(newValue, oldValue) {
			getProductUpperLower();// 获取审批金额上下限
			getProductLimit();// 获取审批期限
			showAssetsInfoByProduct(newValue);
		},
		onLoadSuccess : function() {
			var defValue = $(this).combobox("getValue");
			if (isNotNull(defValue)) {// 防止产品过期
				var defVal = $approvalProductCd.combobox("options").finder.getRow($approvalProductCd[0], defValue);
				if (isNotNull(defVal)) {
					getProductUpperLower();// 获取审批金额上下限
					getProductLimit();// 获取审批期限
				} else {
					$approvalProductCd.combobox("setValue", "");
				}
			}
		},
	});

	// 加载审批金额 后期判断近件营业部上下限--两个异常
	$approvalLimit = $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalLimit']").numberbox({
		required : true,
		min : 0,
		precision : 0,
		buttonText : '元',
		validType : [ 'numberMultiple[1000]' ],
		onChange : function(newValue, oldValue) {
			getProductLimit();// 获取审批期限
			// finishApprovalOpinion_approvalTerm.combobox("setValue", "");
		}
	});
	// 加载期限
	$approvalTerm = $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalTerm']").combobox({
		required : true,
		panelHeight : 'auto',
		editable : false,
		valueField : 'auditLimit',
		textField : 'auditLimit',
		onLoadSuccess : function() {
			// 若没有审批期限则提示该审批额度无可审批期限，请修改审批额度
			var approvalTermData = $approvalTerm.combobox("getData");
			if (!isNotNull(approvalTermData)) {
				$.info('提示', '该审批额度无可审批期限，请修改审批额度');
			}
			// 选中原始值
			var approvalTerm = $approvalTerm.combobox("getValue");// 获取原始值
			if (isNotNull(approvalTerm)) {
				var defVal = $approvalTerm.combobox("options").finder.getRow($approvalTerm[0], approvalTerm);
				if (isNotNull(defVal)) {
					$approvalTerm.combobox("select", approvalTerm);
				} else {
					$approvalTerm.combobox("setValue", "");
				}
			}
		},
		onChange : function(newValue) {
			getProductUpperLower();// 获取审批金额上下限
			if (isNotNull(newValue) && newValue != $(this).parents("table").find("tr:eq(1)").find("td:eq(1)").html()) {
				$.info("提示", "申请期限和审批期限不一致!", "warning")
			}
		}
	});

	// 根据审核产品code显示资产信息
	showAssetsInfoByProduct($("#finish_approval_assetsInfo_div").attr("data-name"));
	// 浮动显示申请人信息
	$("#finishApprovalOpinion_approveInfo_div").addClass("approve_info_fixed");

    // 计算初审意见备注框剩余可输入字数
    var $approvalMemo = $('#finish_approvalMemo');
    if ($approvalMemo.length > 0) {// 区分只读页面和可以填写审批意见
        var approvalMemo = $approvalMemo.textbox('getValue');
        var $approvalMemoArea = $("#finish_approvalMemo").parents("td").find('.countSurplusText');
        $approvalMemoArea.html("剩余可输入" + (approvalMemo.length > 200 ? 0 : 200 - approvalMemo.length) + "字");
    }

	//countSurplusText();
});

/**
 * 流水求和计算
 *
 * @author Shipf
 * @date
 * @param newValue
 * @param oldValue
 */
function changeRecordCount(html, type) {
	var count = 0;
	html.find("input.textbox-value").each(function(ind, val) {
		var value = $(val).val();
		if (value != "" && !isNaN(value)) {
			count = count + parseInt(value);
		}
	});
	html.find("td:eq(1)").html((count / 3).toFixed(2) + "元");
	if ("personal" == type) {// 计算个人
		var count = 0;
		$("#finishApprovalOpinion_record_table").find("tr.personalRecordTr").find("input.textbox-value").each(function(ind, val) {
			var value = $(val).val();
			if (value != "" && !isNaN(value)) {
				count = count + parseInt(value);
			}
		});
		$("#finishApprovalOpinion_record_table").find("td.personalRecordCount").html((count).toFixed(2));
	} else {// 计算对公流水
		var count = 0;
		$("#finishApprovalOpinion_record_table").find("tr.publicRecordTr").find("input.textbox-value").each(function(ind, val) {
			var value = $(val).val();
			if (value != "" && !isNaN(value)) {
				count = count + parseInt(value);
			}
		});
		$("#finishApprovalOpinion_record_table").find("td.publicRecordCount").html((count).toFixed(2));
	}
	// 计算总和
	$("#finishApprovalOpinion_record_table").find("td.recordCount").html((parseInt($("#finishApprovalOpinion_record_table").find("td.personalRecordCount").html()) + parseInt($("#finishApprovalOpinion_record_table").find("td.publicRecordCount").html())).toFixed(2));
	$("#finishApprovalOpinion_record_table").find("td.recordCount").html(total);
	$("#finishApprovalOpinion_record_table").find("input[name='waterIncomeTotal']").val(total);
}
// 目前无法匹配灰名单--添加速贷
function addFastLoan(obj) {
	/*
	 * <tr><td>速贷贷款</td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:2,buttonText:'元'"></td> <td><input type="text" class="easyui-textbox input" data-options="min:0,precision:0,buttonText:'月'"></td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:2,buttonText:'元'"></td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:0,buttonText:'月'"></td> <td></td> <td><a href="javaScript:void(0);" onclick="addFastLoan(this)"><i class="fa fa-plus" aria-hidden="true"></i></a>&nbsp; &nbsp; &nbsp;</td> </tr>
	 */
	var html = $('<tr><td>速贷贷款</td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td></td><td><a href="javaScript:void(0);" onclick="addFastLoan(this)"><i class="fa fa-plus" aria-hidden="true"></i></a>|&nbsp;<a href="javaScript:void(0);" onclick="deleteTr(this)"><i class="fa fa-minus" aria-hidden="true"></i></a></td></tr>');
	$(obj).parents("tr").after(html);
	html.find("input.input:even").numberbox({
		min : 0,
		precision : 2,
		buttonText : '元'
	});
	html.find("input.input:odd").numberbox({
		min : 0,
		precision : 0,
		buttonText : '月'
	});
}
// 添加信贷
function addCredit(obj) {
	var html = $('<tr><td>信用贷款</td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td class="liabilitiesTd"></td><td><input type="text" class="input"></td><td></td><td><a href="javaScript:void(0);" onclick="addCredit(this)"><i class="fa fa-plus" aria-hidden="true"></i></a>|&nbsp;<a href="javaScript:void(0);" onclick="deleteTr(this)"><i class="fa fa-minus" aria-hidden="true"></i></a></td></tr>');
	$(obj).parents("tr").after(html);
	html.find("input.input:eq(0)").numberbox({
		min : 0,
		precision : 2,
		buttonText : '元',
		onChange : function() {
			changeCalculatedLiabilities(html);
		}
	});
	html.find("input.input:gt(0)").numberbox({
		min : 0,
		buttonText : '月',
		onChange : function() {
			changeCalculatedLiabilities(html);
		}
	});
}
/**
 * 计算信用卡负债率
 *
 * @author Shipf
 * @date
 * @param newValue
 * @param oldValue
 */
function chanageCreditCard(newValue) {
	if (newValue != "" && !isNaN(newValue)) {
		$("#finish_liabilitiesCreditCard").html((newValue / 10).toFixed(2));
		// 计算总负债率
		countCalculatedLiabilities();
	}
}

/**
 * 计算负债
 *
 * @author Shipf
 * @date
 */
function changeCalculatedLiabilities(html) {
	var quota = html.find("input.textbox-value:eq(0)").val();
	if ("" != quota && !isNaN(quota)) {
		var term = html.find("input.textbox-value:eq(1)").val();
		if ("" != term && !isNaN(term)) {
			var value = parseInt(quota) / parseInt(term) + parseInt(quota) * 0.023;
			html.find("td.liabilitiesTd").html(value.toFixed(2));
			// 计算总负债率
			countCalculatedLiabilities();
		}
	}
}
/**
 * 计算总负债率
 *
 * @author Shipf
 * @date
 */
function countCalculatedLiabilities() {
	var count = 0;
	$("#finishApprovalOpinion_liabilities_table").find("td.liabilitiesTd").each(function(ind, val) {
		var text = $(val).html();
		if ("" != text) {
			count = count + parseInt(text);
		}
	});
	$("#finishApprovalOpinion_liabilities_table").find("td.countLiabilities").html(count.toFixed(2));
}
// 删除tr
function deleteTr(obj) {
	$(obj).parents("tr").remove();
}
/**
 * 保存审批意见
 *
 * @author Shipf
 * @date
 */
var finalMarkSubmitOpinion = false;
function finishApprovalOpinionSumbmit(loanNo) {
	if (!finalMarkSubmitOpinion) {
		if ($("#finishApprovalOpinion_approveCheckData_form").form("validate") && $("#finishApprovalOpinion_approvalInfo_from").form("validate")) {
			var obj = formatForm($("#finishApprovalOpinion_approveCheckData_form"));
			var obj = $.extend(obj, $("#finishApprovalOpinion_approvalInfo_from").serializeObject());
			if (!isNotNull($.trim($("#finish_approvalMemo").val()))) {
				$.info("提示", "请填写审批意见信息!", "info", 1000);
				return false;
			}
			var approvalCheckIncome = $("#approvalCheckIncome").val();
			if (approvalCheckIncome <= 0) {
				$.info("提示", "核实收入大于0！");
				return false;
			}
			var approvalMonthPay = $("#approvalMonthPay").val();
			if (approvalMonthPay == null || approvalMonthPay == "") {
				$.messager.alert("提示", "请重新选择审批期限！", "info");
				// $.info("提示", "请重新选择审批期限！");
				return false;
			}
			var applyLmt = $("#applyLmt").val();
			var approvalLimit = $("#approvalLimit").val();
			if (parseInt(applyLmt) < parseInt(approvalLimit)) {
				$.messager.alert("提示", "审批额度需要小于等于申请额度！", "info");
				return false;
			}
			// 校验审批金额是否是1000倍数
			if (parseInt(approvalLimit) % parseInt(1000) != 0) {
				$.messager.alert("提示", "审批金额不是1000的倍数！", "info");
				return false;
			}
			finalMarkSubmitOpinion = true;
			$.ajax({
				url : ctx.rootPath() + "/finishApprove/saveApprovalOpinion/" + loanNo,
				dataType : "JSON",
				method : 'post',
				data : {
                    form:JSON.stringify(obj)
				},
				success : function(data) {
					finalMarkSubmitOpinion = false;
					if (data.success) {
						if ("true" == $("#finishApprovalOpinion_markIsExecuteEngine_hidden").val()) {// 调用规则引擎
							finalExecuteRuleEngine(loanNo, "XSZS04", null);
						} else {
							$.info("提示", "保存成功", "info", 1000);
						}
					} else if ("FAILURE" == data.type) {
						$.info("警告", data.firstMessage, "warning");
					}
				},
				error : function(data) {
					$.info("警告", data.responseText, "warning");
				}
			});
		}
	}
}

/**
 * 获取审批金额上下限
 *
 * @author dmz
 * @date 2017年5月19日
 */
function getProductUpperLower() {
	var obj = new Object();
	var approvalTerm = $approvalTerm.combobox("getValue");// 审批期限
	if (isNotNull(approvalTerm)) {
		obj.auditLimit = approvalTerm;
	}
	obj.owningBranchId = $("#finishApprovalOpinion_owningBranchId_hidden").val();// 进件营业部id
	obj.productCode = $approvalProductCd.combobox("getValue");// 审批产品
	obj.isCanPreferential =  "Y" == $("#finishApprovalOpinion_ifPreferentialUser_hidden").val() ? 0 : 1;
	post(ctx.rootPath() + "/bmsBasiceInfo/getProductUpperLower", obj, "json", function(data) {
		if (data.success) {
			var applyMoney = $("#finishApprovalOpinion_applyMoney_hidden").val();// 申请金额
			var maxNumber = parseInt(applyMoney);
			if (isNotNull(data.data.upperLimit)) {
				maxNumber = parseFloat(data.data.upperLimit) >= parseFloat(applyMoney) ? applyMoney : data.data.upperLimit;
			}
			$approvalLimit.numberbox({
				validType : [ 'compareMaxMin[' + (data.data.floorLimit || 0) + ',' + parseInt(maxNumber) + ']', 'numberMultiple[1000]' ]
			});
			var approvalLimit = $approvalLimit.numberbox("getValue");// 审批金额
			if (isNotNull(approvalLimit)) {
				$approvalLimit.numberbox("setValue", approvalLimit);// 审批金额
			}
			changeApprovalTerm();// 计算负债率
		} else {
			$.info("提示", data.firstMessage, "error");
		}
	});
}

/**
 * 获取审批产品期限
 *
 * @author dmz
 * @date 2017年5月19日
 */
function getProductLimit() {
	var owningBranchId = $("#finishApprovalOpinion_owningBranchId_hidden").val();// 进件营业部id
	var productCode = $approvalProductCd.combobox("getValue");// 审批产品
	var paramsStr = "productCode=" + productCode + "&owningBranchId=" + owningBranchId +"&isCanPreferential="+("Y" == $("#finishApprovalOpinion_ifPreferentialUser_hidden").val() ? 0 : 1);
	var approvalLimit = $approvalLimit.numberbox("getValue");// 审批金额
	if (isNotNull(approvalLimit)) {
		paramsStr = paramsStr + "&applyLmt=" + approvalLimit;
	}
	$approvalTerm.combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getProductLimitList?" + paramsStr);
	changeApprovalTerm();// 计算负债率

}

/**
 * 改变期限计算负债率
 *
 * @author Shipf
 * @date
 */
function changeApprovalTerm() {
	var approvalLimit = parseFloat($approvalLimit.numberbox("getValue"));				// 审批金额
	var approvalCheckIncome = parseFloat($approvalCheckIncome.numberbox("getValue"));	// 核实收入
	var approvalTerm = $approvalTerm.combobox("getValue");								// 审批期限
	var approvalProductCd = $approvalProductCd.combo('getValue');						// 审批产品
    var total = parseFloat($("input[name='outCreditDebtTotal']").val() || 0);			// 外部信用负债总额
	if (approvalCheckIncome > 0 && approvalLimit > 0 && approvalTerm > 0) {
        // 获取产品费率
        var productRate = coreApi.getProductRateByCode(approvalProductCd, approvalTerm);
        var rate = productRate.rate;
        if("Y" == vm.applyBasicInfo.ifPreferentialUser){
            rate = productRate.preferRate;
        }

        if(isEmpty(rate)){  // 判断产品费率是否合法
            $.info('提示', '产品费率问题请联系管理员!', 'warning');
            return false;
        }

		// 月还款额 = 审批额度 / 审批期限 + 审批额度 * 产品费率
		var approvalMonthPay = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(2);
		var approvalMonthPayShow = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(0);
		$("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val(approvalMonthPayShow);
		$("#finishApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html($.number(approvalMonthPayShow, 0));
		if ("false" == $("#finishApprovalOpinion_markIsExecuteEngine_hidden").val()) {// 不调用规则引擎
			// 内部负债率= 本次我司贷款的月还款额 / 月核实收入 * 100%，四舍五入保留一位小数
			var approvalDebtTate = (approvalMonthPay / approvalCheckIncome);
			$("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(approvalDebtTate.toFixed(3));
			$("#finishApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(approvalDebtTate) * 100).toFixed(1) + "%");
			// 总负债率 =（本次我司贷款的月还款额 + 外部信用负债总额） / 月核实收入 * 100%，四舍五入保留一位小数
			var approvalAllDebtRate = ((parseFloat(approvalMonthPay) + parseFloat(total)) / approvalCheckIncome);
			$("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(approvalAllDebtRate.toFixed(3));
			$("#finishApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(approvalAllDebtRate) * 100).toFixed(1) + "%");
		}
	}else{
        // 清空月还款额
        $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val('');
        $("#finishApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html('');
		// 清空内部负债率
        $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val('');
        $("#finishApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html('');
        // 清空总负债率
        $("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val('');
        $("#finishApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html('');
	}
}


/**
 * 限制审批意见输入框字数
 * @param newValue
 * @param oldValue
 * @author wulj
 */
function limitApprovalMemo(newValue, oldValue) {
    var remainLength = newValue.length > 200 ? 0 : 200 - newValue.length;
    //$("#finish_approvalMemo").textbox('setValue', newValue.substring(0, 200));
    $("#finish_approvalMemo").parents("td").find(".countSurplusText").html("剩余可输入" + remainLength + "字");
}

/**
 * 序列化form
 *
 * @author Shipf
 * @date
 * @param form
 * @returns
 */
function formatForm(form) {
	var obj = {}, array = form.serializeArray();
	$.each(array, function(arrayIndex, dataObj) {
		var isAdd = false;
		if (dataObj.value != null) {
			isAdd = true;
		}
		if (isAdd) {
			if ("threeMonthsCount,oneMonthsCount,creditTotalLimit,creditUsedLimit,version,personalWaterTotal,publicWaterTotal,waterIncomeTotal,creditDebt,outDebtTotal,outCreditDebtTotal,incomeCertificate,creditRecord".indexOf(dataObj.name) >= 0) {
				obj[dataObj.name] = dataObj.value;
			} else {
				if ($.isArray(obj[dataObj.name]) == true) {
					obj[dataObj.name].push($.trim(dataObj.value));
				} else {
					var newArray = [];
					newArray.push($.trim(dataObj.value));
					obj[dataObj.name] = newArray;
				}
			}
		}
	});
	return obj;
}
/**
 * 系统初判
 *
 * @author dmz
 * @date 2017年7月6日
 * @param loanNo
 */
function finalSystemDetermine(loanNo) {
	if ("true" == $("#finishApprovalOpinion_markIsExecuteEngine_hidden").val()) {// 调用规则引擎
		var obj = new Object();
		obj.approvalProductCd = $approvalProductCd.combobox("getValue");
		finalExecuteRuleEngine(loanNo, "XSZS03",obj);
	}
}
/**
 * 调用规则引擎
 *
 * @author dmz
 * @date 2017年7月6日
 * @param loanNo
 * @param actionCode
 */
function finalExecuteRuleEngine(loanNo, executeType, obj) {
	// 调用规则引擎
	post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/" + executeType, obj, "JSON", function(data) {
		if (data.success) {
			var msgList = data.messages;
			if (isNotNull(data.data)) {
				if (isNotNull(data.data.adviceVerifyIncome)) {// 建议核实收入
					$("#final_systemDetermine_table").find("td:first").html($.number(data.data.adviceVerifyIncome));
				}
				if (isNotNull(data.data.adviceAuditLines)) {// 建议审批额度-建议到手金额
					$("#final_systemDetermine_table").find("td:eq(1)").html($.number(data.data.adviceAuditLines));
				}
                if (isNotNull(data.data.estimatedCost)) {// 预估评级费
                    $("#final_systemDetermine_table").find("td:last").html($.number(data.data.estimatedCost));
                }
				if (isNotNull(data.data.internalDebtRatio)) {// 内部负债率
					$("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(data.data.internalDebtRatio.toFixed(3));
					$("#finishApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(data.data.internalDebtRatio) * 100).toFixed(1) + "%");
				}
				if (isNotNull(data.data.totalDebtRatio)) {// 总负债率
					$("#finishApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(data.data.totalDebtRatio.toFixed(3));
					$("#finishApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(data.data.totalDebtRatio) * 100).toFixed(1) + "%");
				}
                if (isNotNull(data.data.oneMonthsCount)) {// 近一个月查询
                    vm.approvalInfo.approveCheckInfo.oneMonthsCount= data.data.oneMonthsCount;
                }
                if (isNotNull(data.data.threeMonthsCount)) {// 近三个月查询
                    vm.approvalInfo.approveCheckInfo.threeMonthsCount= data.data.threeMonthsCount;
                }
                if (isNotNull(data.data.ifCreditRecord)) {// ifCreditRecord
                    $("#finish_ifCreditRecord").html(data.data.ifCreditRecord =='Y' ? "有":"无");
                }
				window.opener.finalSettingRuleEngineData(data.data);// 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
        }
			if ("HINT" == data.firstMessage) {
				window.opener.finalLimitSubmitSetting("true");
				if ("XSZS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
			} else if ("REJECT" == data.firstMessage) {
				$.info("提示", "规则引擎拒绝", "info", 1000, function() {
					window.opener.finalRuleEngineReject();
				});
			} else if ("LIMIT" == data.firstMessage) {
				window.opener.finalLimitSubmitSetting("false");
				if ("XSZS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
			} else if ("PASS" == data.firstMessage) {
				window.opener.finalLimitSubmitSetting("true");
				if ("XSZS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
			}
			$("#ruleEngineHint_div").find("ul").html('');
			for (var i = 2; i < msgList.length; i++) {
				$("#ruleEngineHint_div").find("ul").append("<li>" + msgList[i] + "<br><span>"+msgList[1]+"</span></li>")
			}
			ruleEngineHintShow(msgList.length - 2);//是否显示提示
			$("#ruleEngineHint_number_div").html(msgList.length - 2);
		} else {
			$.info("警告", data.firstMessage, "warning");
		}
	});
}
function showAssetsInfoByProduct(product) {
	if(isNotNull(product)){
		post(ctx.rootPath() + "/bmsBasiceInfo/getListProductAssetsInfoByCode/" + product, null, "json", function(data) {
			if (data.success) {
				$("#finish_approval_assetsInfo_div").find("table").addClass("display_noH");
				$.each(data.dataList, function(ind, val) {
					$("#finish_approval_assetsInfo_div").find("table[id='" + val.code + "']").removeClass("display_noH");
				});
			}
		});
	}
}