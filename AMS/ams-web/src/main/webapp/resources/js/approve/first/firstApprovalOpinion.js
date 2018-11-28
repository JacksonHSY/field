var $approvalTerm;              // 加载期限
var $approvalLimit;             // 审批金额
var $approvalCheckIncome;       // 核实收入
var $approvalProductCd;         // 审批产品
var creditCardTrFirstNumberbox = null;              // 标记负债

var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        loanNo: '',
        approvalInfo: {},
        applyInfoOnlyRead: {},
        applyBasicInfo: {},
        applyInfo: {},
        reportInfo: {},
        loanLimitInfo: {},
        ifCreditRecord: '',
        productList: [],
        markReportIdChange: '',
        markIsExecuteEngine: false,
        topupLoan:{},
        calcWaterAverage: function (water1, water2, water3) {	// 计算月均流水
            var waterAverage = 0;
            waterAverage = (( (water1 || 0) + (water2 || 0) + (water3 || 0)) / 3).toFixed(0);
            return parseInt(waterAverage);
        },
        getApproveCheckStatementList: function (type) {			// 根据流水类型，获取流水明细信息
            if(type){
                // 根据类型过滤，返回特定类型的流水信息(比如个人or对公)
                var checkStatementList = _.filter(vm.approvalInfo.approveCheckStatementList, function (checkStatement) {
                    return checkStatement.type == type;
                });

                if(checkStatementList.length == 0){
                    var checkStatement = {
                        infoId: vm.approvalInfo.approveCheckInfo.id,
                            loanNo: vm.loanNo,
                        type: type,
                        water1: null,
                        water2: null,
                        water3: null
                    };
                    vm.approvalInfo.approveCheckStatementList.push(checkStatement);
                    checkStatementList.push(checkStatement);
                }

                return checkStatementList;
            }

            // 如果没有流水类型，则返回所有类型的流水信息(包含个人 + 对公)
            return vm.approvalInfo.approveCheckStatementList;
        },
        // 添加流水记录
        addCheckStatement: function(type) {
            vm.approvalInfo.approveCheckStatementList.push({
                infoId: vm.approvalInfo.approveCheckInfo.id,
                loanNo: vm.loanNo,
                type: type,
                water1: null,
                water2: null,
                water3: null
            });

            if(type == 'C'){    // 如果添加的是个人流水，则初始化个人流水numberbox
                var $newRecord = $('.personalRecordTr:last');
                $("input:lt(3)", $newRecord).numberbox({
                    groupSeparator:',',
                    validType : [ 'compareMaxMin[0,99999999]'],
                    precision : 0,
                    buttonText : '元',
                    onChange : function() {
                        changeRecordCount($newRecord, "personal");
                    }
                });
            }else if(type == 'B'){   // 如果添加的是对公流水，则初始化对公流水numberbox
                var $newRecord = $('.publicRecordTr:last');
                $("input:lt(3)", $newRecord).numberbox({
                    groupSeparator : ',',
                    validType : [ 'compareMaxMin[0,99999999]' ],
                    precision : 0,
                    buttonText : '元',
                    onChange : function() {
                        changeRecordCount($newRecord, "public");
                    }
                });
            }

        },
        // 删除流水记录
        deleteCheckStatement: function(index, element, event){
            var $index = vm.approvalInfo.approveCheckStatementList.indexOf(element);
            vm.approvalInfo.approveCheckStatementList.splice($index, 1);
            if(element.type == 'C'){
                changeRecordCount($("tr.personalRecordTr:first", $("#firstApprovalOpinion_record_table")), "personal");
            }else if(element.type == 'B'){
                changeRecordCount($("tr.publicRecordTr:first", $("#firstApprovalOpinion_record_table")), "public");
            }
        },
		$computed: {
            personalCheckStatementArray: function () {  // 个人流水
                return vm.getApproveCheckStatementList('C');
            },
            publicCheckStatementArray: function () {    // 对公流水
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
		},
        defaultIfNull: function (value) {
            return (isNotNull(value)) ? value : '';
        },
        getRecordCount: function (water1,water2,water3) {
            return (isNotNull(water1) || isNotNull(water2) || isNotNull(water3)) ? vm.calcWaterAverage(water1, water2, water3) : '';
        },
        getWaterTotal: function (type) {
            var waterTotal = 0;
            var ifNull = true;
            if (type == 'personalWaterAverageTotal') {
                if(vm.approvalInfo){
                    avalon.each(vm.personalCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal =  waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            if (type == 'publicWaterAverageTotal') {
                if(vm.approvalInfo){
                    avalon.each(vm.publicCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal =  waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            if (type == 'waterIncomeTotal') {
                if(vm.approvalInfo){
                    avalon.each(vm.allCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal =  waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            return ifNull?'': waterTotal;
        }
    };

    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);

    avalon.config({
        debug: false
    });

    avalon.scan(document.body);
}

avalon.ready(function () {
	setTimeout(function () {
        if ("Y" == vm.markReportIdChange) {  // 提示央行报告是否有改变
            $.info("提示", "客户征信报告已更新，请重新填写负债信息", "warning", 1000);
        }

        // 法院网核查情况 内部匹配情况 初始化
        // otherCheckMesInit();

        // 匹配速贷灰名单
        post(ctx.rootPath() + "/bdsApi/fastLoanBlacklist/" + $('#loanNo').val(), null, "json", function(data) {
            if (data.success) {
                if (data.data) {// 有速贷灰名单
                    var ifBlackRoster = $("#first_ifBlackRoster").val();
                    if ("N" == ifBlackRoster) {
                        $('#first_fastLoanBlacklist').combobox("setValue","");
                    }
                } else {// 无速贷灰名单需要设置默认值并且不能修改
                    $('#first_fastLoanBlacklist').combobox({value:'NOLOAN',readonly:true});
                }
                $("#first_ifBlackRoster").val(data.data ? "Y":"N");
            }
        });

        // 个人流水加载
        $("#firstApprovalOpinion_record_table").find("tr.personalRecordTr").each(function(ind, val) {
            $(val).find("input:lt(3)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]'],
                precision : 0,
                buttonText : '元',
                onChange : function() {
                    changeRecordCount($(val), "personal");
                }
            });
        });

        // 对公流水加载
        $("#firstApprovalOpinion_record_table").find("tr.publicRecordTr").each(function(ind, val) {
            $(val).find("input:lt(3)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]' ],
                precision : 0,
                buttonText : '元',
                onChange : function() {
                    changeRecordCount($(val), "public");
                }
            });
        });

        // topUp贷款
        var topUpHtml = $("#firstApprovalOpinion_liabilities_table").find("tr.topUpTr");
        if (topUpHtml != null && topUpHtml.length > 0) {
            topUpHtml.find("input.input:eq(0)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]' ],
                precision : 0,
                buttonText : '元',
                required : true,
                onChange : function() {
                    changeCalculatedLiabilities(topUpHtml);
                }
            });
            topUpHtml.find("input.input:eq(1)").numberbox({
                validType : [ 'compareMaxMin[0,360]' ],
                precision : 0,
                buttonText : '月',
                required : true,
                onChange : function() {
                    changeCalculatedLiabilities(topUpHtml);
                }
            });
            topUpHtml.find("input.input:eq(2)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]' ],
                precision : 0,
                buttonText : '元',
                required : true,
                onChange : function() {
                    countCalculatedLiabilities();
                }
            });
        }

        // 信用贷款
        $("#firstApprovalOpinion_liabilities_table").find("tr.creditCardTr").each(function(ind, val) {
            $(val).find("input.input:eq(0)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]' ],
                precision : 0,
                buttonText : '元',
                onChange : function() {
                    changeCalculatedLiabilities($(val));
                },
            });
            $(val).find("input.input:eq(1)").numberbox({
                validType : [ 'compareMaxMin[0,360]' ],
                precision : 0,
                buttonText : '月',
                onChange : function() {
                    changeCalculatedLiabilities($(val));
                }
            });
            $(val).find("input.input:eq(2)").numberbox({
                groupSeparator:',',
                validType : [ 'compareMaxMin[0,99999999]' ],
                precision : 0,
                buttonText : '元',
                onChange : function() {
                    countCalculatedLiabilities();
                },
            });
            if (0 == ind) {
                creditCardTrFirstNumberbox = $(val);
            }
        });

		var $approvalInfoContext = $("#firstApprovalOpinion_approvalInfo_from");

		// 加载核实收入
        $approvalCheckIncome = $("input[name='approvalCheckIncome']", $approvalInfoContext).numberbox({
            groupSeparator:',',
            required : true,
            validType : [ 'compareMaxMin[1,99999999]' ],
            precision : 0,
            buttonText : '元',
            onChange : function(newValue, oldValue) {
                calculateMonthPayAndDebtRate(); // 改变核实收入，则重新计算月还款额和负债率
            }
        });

        // 加载初审审批额度
        $approvalLimit = $("input[name='approvalLimit']", $approvalInfoContext).numberbox({
            required : true,
            min : 0,
            buttonText : '元',
            groupSeparator:',',
            validType : [ 'numberMultiple[1000]' ],
            onChange : function(newValue, oldValue) {
                reloadProductLimit();           // 重新加载审批期限下拉框
                calculateMonthPayAndDebtRate(); // 重新计算月还款额和负债率
            }
        });

        // 加载期限
        $approvalTerm = $("input[name='approvalTerm']", $approvalInfoContext).combobox({
            required : true,
            panelHeight : 'auto',
            editable : false,
            valueField : 'auditLimit',
            textField : 'auditLimit',
            missingMessage: '请选择审批期限',
            onChange : function(newValue, oldValue) {
                reloadProductUpperLower();         // 获取审批金额上下限
                calculateMonthPayAndDebtRate(); // 计算负债率
                if (isNotNull(newValue) && newValue != $(this).parents("table").find("tr:eq(1)").find("td:eq(1)").html()) {
                    $.info("提示", "申请期限和审批期限不一致!", "warning")
                }
            },
            onLoadSuccess : function() {
                // 若没有审批期限则提示该审批额度无可审批期限，请修改审批额度
                var approvalTermData = $approvalTerm.combobox("getData");
                if (!isNotNull(approvalTermData)) {
                    $.info('提示', '该审批额度无可审批期限，请修改审批额度', 'warning');
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
            }
        });

        // 加载审批意见中的申请产品
        $("input[name='approvalProductCd']", $approvalInfoContext).combobox({
            required : true,
            editable : false,
            valueField : 'code',
            textField : 'name',
            data : vm.productList,
            onChange : function(newValue, oldValue) {
                if (isNotNull(newValue)) {
                    vm.applyBasicInfo.productCd = newValue;
                    reloadProductUpperLower();          // 获取审批金额上下限
                    reloadProductLimit();               // 获取审批期限
                    calculateMonthPayAndDebtRate();     // 计算负债率
                    showAssetsInfoByProduct(newValue);  // 根据产品获取对应的资产信息显示
                }
            },
            onLoadSuccess : function() {
                $approvalProductCd = $(this);
                // 产品列表下拉框加载成功后，判断已保存的产品CODE是否已过期，已过期则清空产品CODE，让用户重新选择
                var productCode = $approvalProductCd.combobox("getValue");
                var productItem = getProductItemByCode(productCode);
                if(productItem){
                    reloadProductUpperLower();      // 获取审批金额上下限
                    reloadProductLimit();           // 获取审批期限
                    calculateMonthPayAndDebtRate(); // 计算负债率
                }else{
                    $(this).combobox("setValue", "");
                }
            }
        });

        // 根据审核产品code显示资产信息
		showAssetsInfoByProduct($("#first_approval_assetsInfo_div").attr("data-name"));

		// 浮动显示申请人信息
        $("#firstApprovalOpinion_approveInfo_div").addClass("approve_info_fixed");

        // 计算外部总负债
        var countType = 'init';// 标记是否是需要计算(如果央行报告不为空需要重新计算)
        var isLoanLimitInfo=isNotNull(vm.loanLimitInfo) && (vm.loanLimitInfo.loan.length> 0 || vm.loanLimitInfo.car.length> 0 || vm.loanLimitInfo.house.length> 0||vm.loanLimitInfo.other.length> 0);
        var isReportInfo = isNotNull(vm.reportInfo) && vm.reportInfo.debt > 0;
        var isTopup = isNotNull(vm.topupLoan) && vm.topupLoan.returneterm > 0;// TOPUP负债信息
        if (isLoanLimitInfo ||isReportInfo || isTopup) { // 如果央行报告不为空需要重新计算外部负责总额
            countType = null;
        }
        changeCalculatedLiabilities($(creditCardTrFirstNumberbox), countType);
        countCalculatedLiabilities(countType);

        // 计算初审意见备注框剩余可输入字数
        var $approvalMemo = $('#first_approvalMemo');
        var approvalMemo = $approvalMemo.textbox('getValue');
        var $approvalMemoArea = $("#first_approvalMemo").parents("td").find('.countSurplusText');
        $approvalMemoArea.html("剩余可输入" + (approvalMemo.length > 200 ? 0 : 200 - approvalMemo.length) + "字");

        //begin 页面加载重新计算信用卡负债
        var val = $("#firstApprovalOpinion_liabilities_table").find("input[name='creditUsedLimit']").val();
        if (isNotNull(val)) {
            $("#first_liabilitiesCreditCard").html(numberFomatter((val / 10).toFixed(0)));
            $("#firstApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val((val / 10).toFixed(0));
        } else  {
            $("#first_liabilitiesCreditCard").html(0);
            $("#firstApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val(0);
        }
        //end

    },200);
});

/**
 * 根据产品获取对应的资产信息显示
 * 
 * @param product
 */
function showAssetsInfoByProduct(product) {
	if (isNotNull(product)) {
		post(ctx.rootPath() + "/bmsBasiceInfo/getListProductAssetsInfoByCode/" + product, null, "json", function(data) {
			if (data.success) {
				$("#first_approval_assetsInfo_div").find("table").addClass("display_noH");
				$.each(data.dataList, function(ind, val) {
					$("#first_approval_assetsInfo_div").find("table[id='" + val.code + "']").removeClass("display_noH");
				});
			}
		});
	}
}

/**
 * 征信初判对话框
 */
function firstApprovalOpinionContrastDialog() {
	var loanNo = $('#loanNo').val();
	post(ctx.rootPath() + "/creditzx/getCreditJudgment/2/" + loanNo, null, "json", function(data) {
		if (data.success) {
			// 央行征信报告
			var credit = data.data[0];
			if (isNotNull(credit.result)) {
				$("#firstApprovalOpinion_credit_table").append('<tr><td> 征信信息：' + credit.firstMessage + '</td></tr>');
			}
			if (isNotNull(credit.status)) {
				var html = '<tr><th>人行近3个月查询次数:</th><td>' + (isNotNull(credit.threeMonthqueryCount) ? credit.threeMonthqueryCount : "0") + '</td>' + '<th>人行近1个月查询次数:</th><td>' + (isNotNull(credit.oneMonthqueryCount) ? credit.oneMonthqueryCount : "0") + '</td></tr>' + '<tr><th>信用卡总额度:</th><td>' + (isNotNull(credit.creditLimitMoney) ? credit.creditLimitMoney : "0") + '</td>' + '<th>信用卡已用额度:</th><td>' + (isNotNull(credit.alreadyUseMoney) ? credit.alreadyUseMoney : "0") + '</td></tr>' + '<tr><th>信用卡负债:</th><td>' + (isNotNull(credit.debt) ? credit.debt : "0") + '</td></tr>';
				$("#firstApprovalOpinion_credit_table").append(html);
			}
			// 贷款记录
			var loanRecord = data.data[1];
			if (!isNotNull(loanRecord)) {
				$("#firstApprovalOpinion_debt_table").append('<tr><td colspan="5">' + loanRecord.firstMessage + '</td></tr>');
			} else {
                // 信用贷款
                $("#firstApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>信用贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
				if(isNotNull(loanRecord.loan) && loanRecord.loan.length > 0) {
                    $.each(loanRecord.loan, function(key, v) {
                        var trHtml = '<tr><td>信用贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#firstApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#firstApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }

                // 房贷
                $("#firstApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>房贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.house) && loanRecord.house.length>0) {
                    $.each(loanRecord.house, function (key, v) {
                        var trHtml = '<tr><td>房贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#firstApprovalOpinion_debt_table").append(trHtml);
                    })
                }else {
                    $("#firstApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }

                // 车贷
                $("#firstApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>车贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.car) && loanRecord.car.length > 0) {
                    $.each(loanRecord.car, function (key, v) {
                        var trHtml = '<tr><td>车贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#firstApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#firstApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }
                // 其他贷款
                $("#firstApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>其他贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.other) && loanRecord.other.length>0) {
                    $.each(loanRecord.other, function(key, v) {
                        var trHtml = '<tr><td>其他贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#firstApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#firstApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }
			}
		}
	});
	$("#firstApprovalOpinion_contrast_Dialog").removeClass("display_none").dialog({
		title : "征信初判",
		modal : true,
		width : 600,
		height : 400,
		onClose : function() {
			$("#firstApprovalOpinion_credit_table").html('');// 关闭时清空table
			$("#firstApprovalOpinion_debt_table").html('');
		},
		buttons : [ {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {// 关闭时清空table
				$("#firstApprovalOpinion_contrast_Dialog").dialog("close");
				$("#firstApprovalOpinion_credit_table").html('');
				$("#firstApprovalOpinion_debt_table").html('');
			}
		} ]
	})
}

/**
 * 删除tr
 * @param obj
 * @param type
 */
function deleteTr(obj, type) {
    if ("personalRecordTr" == $(obj).parents("tr").attr("class")) {
        $(obj).parents("tr").remove();
        // 重新统计个人流水行数量，重新计算个人流水月均合计
        changeRecordCount($("#firstApprovalOpinion_record_table").find("tr.personalRecordTr:first"), "personal");

    } else if ("publicRecordTr" == $(obj).parents("tr").attr("class")) {
        $(obj).parents("tr").remove();
        // 重新统计对公流水行数量，重新计算对公流水月均合计
        changeRecordCount($("#firstApprovalOpinion_record_table").find("tr.publicRecordTr:first"), "public");

    } else if ("creditCardTr" == $(obj).parents("tr").attr("class")) {
        // 负债率计算
        $(obj).parents("tr").remove();
        countCalculatedLiabilities();
        $('#firstApprovalOpinion_liabilities_table').find("input[value='" + type + "']").each(function(ind, val) {
            $(val).prev().html(ind + 1);
        });

    }
}

/**
 * 流水求和计算
 * @author dmz
 * @date 2017年3月30日
 * @param newValue
 * @param oldValue
 */
function changeRecordCount(html, type) {
    // 更新单个流水月均
	var countTr = 0;
    var isNull = true;//用于判断是否流水输入框全部为空
	html.find("input.textbox-value").each(function(ind, val) {
		var value = $(val).val();
		if (value != "" && !isNaN(value)) {
            isNull = false;
			countTr = countTr + parseInt(value);
		}
	});
    var waterCount ;
	if (isNull) { //如果三个流水输入框全部为空
        waterCount = '';
    } else {
        waterCount = numberFomatter((countTr / 3).toFixed(0));
    }
	html.find("td:eq(1)").html( waterCount+ "元");

    var personalWaterIsNull = true;
    var publicWaterIsNull = true;
	if ("personal" == type) {
        // 计算个人流水月均合计
		var countMonth = 0;
		$("#firstApprovalOpinion_record_table").find("tr.personalRecordTr").each(function(ind, valTr) {
			var countTd = 0;
			$(valTr).find("input.textbox-value").each(function(ind, val) {
				var value = $(val).val();
				if (value != "" && !isNaN(value)) {
                    personalWaterIsNull = false;
					countTd = countTd + parseInt(value);
				}
			});
			countMonth = countMonth + parseInt((countTd / 3).toFixed(0));
		});
        var personalRecordCount ;
        if (personalWaterIsNull) { //如果流水输入框全部为空
            personalRecordCount = '';
        } else {
            personalRecordCount = numberFomatter((countMonth));
        }
		$("#firstApprovalOpinion_record_table").find("td.personalRecordCount").html(personalRecordCount+ "元");
		$("#firstApprovalOpinion_record_table").find("input[name='personalWaterTotal']").val(countMonth);

	} else {
        // 计算对公流水月均合计
		html.find("input[name='publicMonthAverage']").val((countTr / 3).toFixed(0));
		var countMonth = 0;
		$("#firstApprovalOpinion_record_table").find("tr.publicRecordTr").each(function(ind, valTr) {
			var countTd = 0;
			$(valTr).find("input.textbox-value").each(function(ind, val) {
				var value = $(val).val();
				if (value != "" && !isNaN(value)) {
                    publicWaterIsNull = false;
					countTd = countTd + parseInt(value);
				}
			});
			countMonth = countMonth + parseInt((countTd / 3).toFixed(0));
		});

        var publicRecordCount ;
        if (publicWaterIsNull) { //如果流水输入框全部为空
            publicRecordCount = '';
        } else {
            publicRecordCount = numberFomatter(countMonth);
        }

		$("#firstApprovalOpinion_record_table").find("td.publicRecordCount").html(publicRecordCount + "元");
		$("#firstApprovalOpinion_record_table").find("input[name='publicWaterTotal']").val(countMonth);

	}
	// 计算个人+对公流水月均合计
	var total = (parseInt($("#firstApprovalOpinion_record_table").find("input[name='personalWaterTotal']").val()) + parseInt($("#firstApprovalOpinion_record_table").find("input[name='publicWaterTotal']").val())).toFixed(0);
    var publicRecordCountTd = $("#firstApprovalOpinion_record_table").find("td.publicRecordCount").text();
    var personalRecordCountTd = $("#firstApprovalOpinion_record_table").find("td.personalRecordCount").text();


	var totalWater ;
    if (publicRecordCountTd == "元" && personalRecordCountTd == "元") {
        totalWater = '';
    } else {
        totalWater = numberFomatter(total);
    }
	$("#firstApprovalOpinion_record_table").find("td.recordCount").html(totalWater + "元");
	$("#firstApprovalOpinion_record_table").find("input[name='waterIncomeTotal']").val(total);
}

/**
 * TODO 目前无法匹配灰名单--添加速贷
 * @param obj
 */
function addFastLoan(obj) {
	/*
	 * <tr><td>速贷贷款</td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:2,buttonText:'元'"></td> <td><input type="text" class="easyui-textbox input" data-options="min:0,precision:0,buttonText:'月'"></td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:2,buttonText:'元'"></td> <td><input type="text" class="easyui-numberbox input" data-options="min:0,precision:0,buttonText:'月'"></td> <td></td> <td><a href="javaScript:void(0);" onclick="addFastLoan(this)"><i class="fa fa-plus" aria-hidden="true"></i></a>&nbsp; &nbsp; &nbsp;</td> </tr>
	 */
	var html = $('<tr><td>速贷贷款</td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td><input type="text" class="input"></td><td></td><td><a href="javaScript:void(0);" onclick="addFastLoan(this)"><i class="fa fa-plus" aria-hidden="true"></i></a>|&nbsp;<a href="javaScript:void(0);" onclick="deleteTr(this)"><i class="fa fa-minus" aria-hidden="true"></i></a></td></tr>');
	$(obj).parents("tr").after(html);
	html.find("input.input:even").numberbox({
		validType : [ 'compareMaxMin[0,99999999]' ],
		precision : 0,
		buttonText : '元',
		groupSeparator : ',',
	});
	html.find("input.input:odd").numberbox({
		min : 0,
		precision : 0,
		buttonText : '月'
	});
}

/**
 * 添加信贷
 * @param title
 * @param type
 */
function addCredit(title, type) {
	var length = $("#firstApprovalOpinion_liabilities_table").find("input[value='" + type + "']").length + 1;
	var html = $('<tr class="creditCardTr"><td>' + title + '<span>' + length + '</span><input type="hidden" name="debtType" value="' + type + '"></td><td><input type="text" class="input" name="creditLoanLimit"></td><td><input type="text" class="input" name="creditLoanTerm"></td><td><input type="text" class="input" name="creditLoanDebt"></td><td><input type="hidden" name="creditNo" placeholder="对应关系"></td><td><a href="javaScript:void(0);" onclick=deleteTr(this,"' + type + '")><i class="fa fa-minus" aria-hidden="true"></i></a></td></tr>');
	$("#firstApprovalOpinion_liabilities_table").find("input[value='" + type + "']").parents("tr:last").after(html);
	html.find("input.input:eq(0)").numberbox({
		validType : [ 'compareMaxMin[0,99999999]' ],
		precision : 0,
		buttonText : '元',
		groupSeparator : ',',
		onChange : function() {
			changeCalculatedLiabilities(html);
		}
	});
	html.find("input.input:eq(1)").numberbox({
		validType : [ 'compareMaxMin[0,360]' ],
		buttonText : '月',
		onChange : function() {
			changeCalculatedLiabilities(html);
		}
	});
	html.find("input.input:eq(2)").numberbox({
		validType : [ 'compareMaxMin[0,99999999]' ],
		precision : 0,
		buttonText : '元',
		groupSeparator : ',',
		onChange : function() {
			countCalculatedLiabilities();
		}
	});
}

/**
 * 计算信用卡负债率
 * 
 * @author dmz
 * @date 2017年3月31日
 * @param newValue
 * @param oldValue
 */
function changeCreditCard(newValue) {
	var val = newValue || 0;
	$("#first_liabilitiesCreditCard").html(numberFomatter((val / 10).toFixed(0)));
	$("#firstApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val((val / 10).toFixed(0));
	// 计算总负债率
	countCalculatedLiabilities();
}

/**
 * 计算负债
 * 
 * @author dmz
 * @date 2017年3月30日
 */
function changeCalculatedLiabilities(html, type) {
	var quota = html.find("input.textbox-value:eq(0)").val();   // 获取额度
	var term = html.find("input.textbox-value:eq(1)").val();    // 获取期限
    var feeRate = 0;                                            // 费率
    // 如果"额度"和"期限"都不为空的情况下，计算"负债"信息(其他贷款不计算)
    var debtType = $(html).find("td:first").find("input[name='debtType']").val();
	if ("" != quota && !isNaN(quota) && "" != term && !isNaN(term) && "OTHERLOAN" != debtType && term > 0) {
        if (!isNotNull(type)) {
            if ("CREDITLOAN" == debtType){
                feeRate = 0.023;
            }else if("HOUSELOAN" == debtType){
                feeRate = 0.00408;
            }else if("CARLOAN" == debtType){
                feeRate = 0.00625;
            }
            var value = parseInt(quota) / parseInt(term) + parseInt(quota) * feeRate;
            html.find("input[textboxname='creditLoanDebt']").numberbox("setValue", value.toFixed(0));
        }

		countCalculatedLiabilities(type);
        // 额度和期限有一个请求的情况下不清空负责信息
	}/* else {
		if ("CREDITLOAN" == $(html).find("td:first").find("input[name='debtType']").val() && !isNotNull(type)) {
			html.find("input[textboxname='creditLoanDebt']").numberbox("setValue", "");
		}
	}*/

	creditCardTrFirstNumberbox = null;
}

/**
 * 计算总负债率
 * 
 * @author dmz
 * @date 2017年3月31日
 */
function countCalculatedLiabilities(type) {
	var count = 0, outCount = 0;
	var creditDebt = $("#firstApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val()
	if (isNotNull(creditDebt)) {
		count = parseInt(creditDebt);
	}
	outCount = count;
    $("#firstApprovalOpinion_liabilities_table").find("tr.creditCardTr,tr.topUpTr").each(function(ind, val) {
		var test = $(val).find("input[textboxname='creditLoanDebt']").numberbox("getValue");
		if (isNotNull(test) && !isNaN(test)) {
			count = count + parseInt(test);
			if ($(val).find("input[value='CREDITLOAN']").length > 0 || $(val).find("input[value='TOPUPLOAN']").length > 0) {
				outCount = outCount + parseInt(test);
			}
		}
	});
	var outDebtTobalIdOldValue = $("#outDebtTotal_id").numberbox("getValue");
	// 外部信用负债总额
    $("#firstApprovalOpinion_liabilities_table").find("td.creditCountLiabilities").html(numberFomatter(outCount.toFixed(0)));
    $("#outCreditDebtTotal_id").val(outCount.toFixed(0));
	// 外部负债总和
	var min = count.toFixed(0) > 99999999 ? 99999999 : count.toFixed(0);
	if ("init" != type) {
        $("#outDebtTotal_id").numberbox({
            validType : [ 'compareMaxMin[' + min + ',99999999]' ],
            value : count.toFixed(0),
        });
    } else {
        $("#outDebtTotal_id").numberbox({
            validType : [ 'compareMaxMin[' + min + ',99999999]' ],
        });
    }
	if (null == creditCardTrFirstNumberbox && "init" != type) {
		if (count.toFixed(0) != outDebtTobalIdOldValue) {
			$approvalTerm.combobox("setValue", "");
		}
	}

}

/**
 * 重新加载审批金额上下限
 */
function reloadProductUpperLower() {
    var approvalProductCd = $approvalProductCd.combobox('getValue');        // 审批产品
    var owningBranchId = vm.applyBasicInfo.owningBranchId;      // 进件营业部id
    var auditLimit = $approvalTerm.combobox("getValue");
    var productLimit = bmsBasicApi.getProductUpperLower(approvalProductCd, owningBranchId, auditLimit,("Y" == vm.applyBasicInfo.ifPreferentialUser ? 0 : 1));
    var applyAmount = parseInt($("#firstApprovalOpinion_applyMoney_hidden").val());// 申请金额
    if (isNotNull(productLimit.upperLimit)) {
        applyAmount = parseFloat(productLimit.upperLimit) >= parseFloat(applyAmount) ? applyAmount : productLimit.upperLimit;
    }
    $approvalLimit.numberbox({
        validType : [ 'compareMaxMin[' + (productLimit.floorLimit || 1) + ',' + parseInt(applyAmount) + ']', 'numberMultiple[1000]' ]
    });
}

/**
 * 重新加载审批产品期限下拉框
 */
function reloadProductLimit() {
	var owningBranchId = vm.applyBasicInfo.owningBranchId;		// 进件营业部id
	var approvalProductCd = $approvalProductCd.combobox("getValue");// 审批产品
    var approvalLimit = $approvalLimit.numberbox("getValue");// 审批金额
	var params = "productCode=" + approvalProductCd + "&owningBranchId=" + owningBranchId +"&isCanPreferential=" + ("Y" == vm.applyBasicInfo.ifPreferentialUser ? 0 : 1);
	if (isNotNull(approvalLimit)) {
		params = params + "&applyLmt=" + approvalLimit;
	}
	$approvalTerm.combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getProductLimitList?" + params);
}

/**
 * 计算月还款额和负债率
 */
function calculateMonthPayAndDebtRate() {
	var approvalLimit = parseFloat($approvalLimit.numberbox("getValue"));               // 审批金额
	var approvalCheckIncome = parseFloat($approvalCheckIncome.numberbox("getValue"));   // 核实收入
	var approvalTerm = $approvalTerm.combobox("getValue");                              // 审批期限
    var approvalProductCd = $approvalProductCd.combobox('getValue');                    // 产品编号
    var total = parseFloat($("#outCreditDebtTotal_id").val() || 0);                     // 外部信用负债总额
    if (approvalCheckIncome > 0 && approvalLimit > 0 && approvalTerm > 0) {
        var productRate = coreApi.getProductRateByCode(approvalProductCd, approvalTerm);        // 获取产品费率
        var rate = productRate.rate;                                                    // 产品综合费率
        if("Y" == vm.applyBasicInfo.ifPreferentialUser){                                // 判断是否是优惠客户，是则取优惠费率作为计算
            rate = productRate.preferRate;
        }

        if(isEmpty(rate)){  // 判断产品费率是否合法
            $.info('提示', '产品费率问题请联系管理员!', 'warning');
            $approvalTerm.combobox("setValue", '');
            return false;
        }

        var approvalTerm = parseFloat(approvalTerm);   // 审批期限
        // 月还款额 = 审批额度 / 审批期限 + 审批额度 * 产品费率
        var approvalMonthPay = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(2);
        var approvalMonthPayShow = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(0);
        $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val(approvalMonthPayShow);
        $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html(numberFomatter(approvalMonthPayShow));
        if (!vm.markIsExecuteEngine) {// 不调用规则引擎
            // 内部负债率= 本次我司贷款的月还款额 / 月核实收入 * 100%，四舍五入保留一位小数
            approvalDebtTate = (approvalMonthPay / approvalCheckIncome);
            $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(approvalDebtTate.toFixed(3));
            $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(approvalDebtTate) * 100).toFixed(1) + "%");
            // 总负债率 =（本次我司贷款的月还款额 + 外部信用负债总额） / 月核实收入 * 100%，四舍五入保留一位小数
            var approvalAllDebtRate = ((parseFloat(approvalMonthPay) + parseFloat(total)) / approvalCheckIncome);
            $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(approvalAllDebtRate.toFixed(3));
            $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(approvalAllDebtRate) * 100).toFixed(1) + "%");
        }
    }else{
        // 清空月还款额
        $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val('');
        $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html('');
        // 清空内部负债率
        $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val('');
        $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html('');
        // 清空总负债率
        $("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val('');
        $("#firstApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html('');
    }
}

/**
 * 保存审批意见
 * 
 * @author dmz
 * @date 2017年3月31日
 */
var firstMarkSubmitOpinion = false;
function submit() {
	if (!firstMarkSubmitOpinion) {
		// 开始验证表单规则
		if ($("#firstApprovalOpinion_approveCheckData_form").form("validate") && $("#firstApprovalOpinion_approvalInfo_from").form("validate")) {

            // 校验资产信息
			if (!$("#MASTERLOAN_A").hasClass("display_noH")) {
				if (!$("#MASTERLOAN_A_form").form("validate")) {
					return false;
				}
			}

			var productCode = $approvalProductCd.combobox("getValue");
			// 如果产品是[公积金贷款]，则个人流水必须填写一条
			if ("00013" == productCode) {
				var mark = false;
				// 循环个人流水行
				$("#firstApprovalOpinion_record_table").find("tr.personalRecordTr").each(function(index, element) {
					// 循环td
					$(element).find("td:first").find("input.textbox-value").each(function(j, valTd) {
						if (isNotNull($(valTd).val())) {
							mark = true;
							return false;  // 结束循环
						}
					});

					if (mark) {
						return false;// 结束循环
					}
				});
				if (!mark) {
					$.info("提示", "个人流水必须填写一条", "warning", 1000);
					return false;
				}
			}

			// 如果产品是[随薪贷]，且发薪方式为"网银"或"网银+现金"，则个人流水必须填写一条
			if ("00001" == productCode) {
				var mark = false;
				var payWay = vm.applyInfo.basicInfoVO.workInfoVO.corpPayWay;	// 发薪方式为网银或者网银+现金
				if (payWay && ("00001" == payWay || "00003" == payWay)) {
					// 循环个人流水行
					var totalMarkValue = $("#firstApprovalOpinion_record_table").find("tr.personalRecordTr").each(function(i, valTr) {
						// 循环td
						$(valTr).find("td:first").find("input.textbox-value").each(function(j, valTd) {
							if (isNotNull($(valTd).val())) {
								mark = true;
								return false;// 结束循环
							}
						});
						if (mark) {
							return false;// 结束循环
						}
					});

					if (!mark) {
						$.info("提示", "个人流水必须填写一条", "warning", 1000);
						return false;
					}
				}
			}

			// 如果产品是[随薪贷]、[薪生贷]、[公积金贷款]，则"是否周末发薪"必填
			if ("00001" == productCode || "00009" == productCode || "00013" == productCode) {
				if ($("input[name='weekendPay']:checked").length <= 0) {
                    $("input[name='weekendPay']").focus();
					$.info("提示", "是否周末发薪必填", "warning", 1000);
					return false;
				}
			}

			// 审批意见必填
			if (!isNotNull($.trim($("#first_approvalMemo").val()))) {
				$("#first_approvalMemo").focus();
				$.info("提示", "请填写审批意见信息!", "warning", 1000);
				return false;
			}

            //征信平台已验证必填
            if (!$("#first_creditCheckException_checkbox").prop("checked")) {
                $('#first_creditCheckException_checkbox').focus();
                $.info("提示", "征信平台已验证未勾选!", "warning", 1000);
                return false;
            }

			// 资料核对
			var $checkDataForm = $("#firstApprovalOpinion_approveCheckData_form");
			var $approvalForm  = $("#firstApprovalOpinion_approvalInfo_from");
			var $masterLoanForm =  $("#MASTERLOAN_A_form");

            var formData = $.extend({}, $checkDataForm.serializeObject(true), $approvalForm.serializeObject());

			if (!$("#MASTERLOAN_A").hasClass("display_noH")) {
				formData = $.extend({}, formData, $masterLoanForm.serializeObject());

			} else if (!$("#POLICY").hasClass("display_noH")) {
				var isCheckValue = $("#POLICY").find("input[name='policyCheckIsVerify']:checked").val();
				if (isNotNull(isCheckValue)) {
					formData.policyCheckIsVerify = $("#POLICY").find("input[name='policyCheckIsVerify']:checked").val();
				} else {
					$.info("提示", "请选择信审保单是否已核实！", "warning", 1000);
					return false;
				}

			} else if (!$("#CAR").hasClass("display_noH")) {
				var isCheckValue = $("#CAR").find("input[name='carCheckIsVerify']:checked").val();
				if (isNotNull(isCheckValue)) {
					formData.carCheckIsVerify = $("#CAR").find("input[name='carCheckIsVerify']:checked").val();
				} else {
					$.info("提示", "请选择信审保单是否已核实！", "warning", 1000);
					return false;
				}

			}

			formData.creditRecord = $('#first_creditRecord_checkbox').is(':checked') ? 1 : 0;
			firstMarkSubmitOpinion = true;

			// 构造资料核对信息
           	formData = transformToCheckInfoAndStatement(formData);

			$.ajax({
				url : ctx.rootPath() + "/firstApprove/updateApprovalOpinion",
				dataType : "JSON",
				method : 'post',
				data : {
					loanNo: vm.loanNo,
					form : JSON.stringify(formData)
				},
				success : function(data) {
					firstMarkSubmitOpinion = false;
					if (data.success) {
						if (vm.markIsExecuteEngine) {// 调用规则引擎
							executeRuleEngine(vm.loanNo, "XSCS04", null);
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
 * 构造资料核对信息
 * @param formData
 * @author wulj
 */
function transformToCheckInfoAndStatement(formData){
	// 构造资料核对汇总信息
    formData.loanNo = vm.loanNo;
    formData["approveCheckInfo"] = {};
    formData.approveCheckInfo["loanNo"] = vm.loanNo;
    formData.approveCheckInfo["oneMonthsCount"] = formData.oneMonthsCount;
    formData.approveCheckInfo["threeMonthsCount"] = formData.threeMonthsCount;
    formData.approveCheckInfo["weekendPay"] = formData.weekendPay;
    formData.approveCheckInfo["creditCheckException"] = $('#first_creditCheckException_checkbox').is(':checked') ? 0 : 1;	// 征信平台验证无异常
    formData.approveCheckInfo["courtCheckException"] =  $('#first_courtException_input').is(':checked') ? 0 : 1;			// 法院网核查无异常
    formData.approveCheckInfo["internalCheckException"] = $('#first_internalException_input').is(':checked') ? 0 : 1;		// 内部匹配无异常
    formData.approveCheckInfo["memo"] = formData.memo[0];

    formData["approveCheckStatementList"] = [];

    // 构造资料核对个人流水信息
    formData.personalWater1 = convertPropertyToArray(formData.personalWater1);
    formData.personalWater2 = convertPropertyToArray(formData.personalWater2);
    formData.personalWater3 = convertPropertyToArray(formData.personalWater3);
    for(var i=0, len=formData.personalWater1.length; i < len; i++){
        if(formData.personalWater1[i] || formData.personalWater2[i] || formData.personalWater3[i]){
            formData.approveCheckStatementList.push({
                loanNo: vm.loanNo,
                type: 'C',
                water1: formData.personalWater1[i],
                water2: formData.personalWater2[i],
                water3: formData.personalWater3[i]
            });
        }
    }

	// 构造资料核对对公流水信息
    formData.publicWater1 = convertPropertyToArray(formData.publicWater1);
    formData.publicWater2 = convertPropertyToArray(formData.publicWater2);
    formData.publicWater3 = convertPropertyToArray(formData.publicWater3);
    for(var i=0, len=formData.publicWater1.length; i < len; i++){
        if(formData.publicWater1[i] || formData.publicWater2[i] || formData.publicWater3[i]){
            formData.approveCheckStatementList.push({
                loanNo: vm.loanNo,
                type: 'B',
                water1: formData.publicWater1[i],
                water2: formData.publicWater2[i],
                water3: formData.publicWater3[i]
            });
        }
    }


    return formData;
}

/**
 * 将普通属性转换为数组
 * @author wulj
 * @param property 对象属性
 * @returns {Array}
 */
function convertPropertyToArray(property){

    if(!(property instanceof Array)){ // 不是数组，则拼接数组
        var newArray = [property];
        return newArray;
    }

    return property;
}

/**
 * 限制审批意见输入框字数
 */
function limitApprovalMemo(newValue, oldValue) {
    var remainLength = newValue.length > 200 ? 0 : 200 - newValue.length;
    //$("#first_approvalMemo").textbox('setValue', newValue.substring(0, 200));
    $("#first_approvalMemo").parents("td").find(".countSurplusText").html("剩余可输入" + remainLength + "字");
}

/**
 * 系统初判
 * 
 * @author dmz
 * @date 2017年7月6日
 * @param loanNo
 */
function firstSystemDetermine(loanNo) {
	if (vm.markIsExecuteEngine) {// 调用规则引擎
		if ($("#firstApprovalOpinion_approveCheckData_form").form("validate")) {
			var obj = new Object();
			obj.approvalProductCd = $approvalProductCd.combobox("getValue");// 审批产品
			obj.monthAverage = parseInt($("#firstApprovalOpinion_record_table").find("input[name='waterIncomeTotal']").val() || 0); // 月平均
			obj.outDebtTotal = parseInt($("#outDebtTotal_id").numberbox("getValue") || 0);// 外部负责总额
			executeRuleEngine(loanNo, "XSCS03", obj);
		} else {
			$.info("提示", "资料信息或负债信息不正确", "warning", 1000);
		}
	}
}

/**
 * 规则引擎执行
 * 
 * @author dmz
 * @date 2017年7月10日
 * @param loanNo-借款编号
 * @param executeType-执行类型
 */
function executeRuleEngine(loanNo, executeType, obj) {
	post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/" + executeType, obj, "JSON", function(data) {
		if (data.success) {
			var msgList = data.messages;
			if (isNotNull(data.data)) {
			    if (isNotNull(data.data.adviceVerifyIncome)) {// 建议核实收入
					$("#first_systemDetermine_table").find("td:first").html(data.data.adviceVerifyIncome);
				}
				if (isNotNull(data.data.adviceAuditLines)) {// 建议审批额度-建议到手金额
					$("#first_systemDetermine_table").find("td:eq(1)").html(data.data.adviceAuditLines);
				}
                if (isNotNull(data.data.estimatedCost)) {// 预估评级费
                    $("#first_systemDetermine_table").find("td:last").html(data.data.estimatedCost);
                }
				if (isNotNull(data.data.internalDebtRatio)) {// 内部负债率
					$("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(data.data.internalDebtRatio.toFixed(3));
					$("#firstApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(data.data.internalDebtRatio) * 100).toFixed(1) + "%");
				}
				if (isNotNull(data.data.totalDebtRatio)) {// 总负债率
					$("#firstApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(data.data.totalDebtRatio.toFixed(3));
					$("#firstApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(data.data.totalDebtRatio) * 100).toFixed(1) + "%");
				}
                if (isNotNull(data.data.oneMonthsCount)) {// 近一个月查询
                    $("#firstApprovalOpinion_oneMonthsCount").numberbox("setValue",data.data.oneMonthsCount);
                }
                if (isNotNull(data.data.threeMonthsCount)) {// 近三个月查询
                    $("#firstApprovalOpinion_threeMonthsCount").numberbox("setValue",data.data.threeMonthsCount);
                }
                if (isNotNull(data.data.ifCreditRecord)) {// ifCreditRecord
                    $("#first_creditRecord_checkbox").prop("checked", data.data.ifCreditRecord =='Y' ? true:false);
                }

                window.opener.firstSettingRuleEngineData(data.data);// 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
			}
			if ("HINT" == data.firstMessage) {
				window.opener.limitSubmitSetting("true");
				if ("XSCS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
			} else if ("REJECT" == data.firstMessage) {
				$.info("提示", "规则引擎拒绝", "warning", 1000, function() {
					window.opener.firstRuleEngineReject();
				});
			} else if ("LIMIT" == data.firstMessage) {
				if ("XSCS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
				window.opener.limitSubmitSetting("false");
			} else if ("PASS" == data.firstMessage) {
				window.opener.limitSubmitSetting("true");
				if ("XSCS04" == executeType) {
					$.info("提示", "保存成功", "info", 1000);
				}
			}
			$("#ruleEngineHint_div").find("ul").html('');
			for (var i = 2; i < msgList.length; i++) {
				$("#ruleEngineHint_div").find("ul").append("<li>" + msgList[i] + "<br><span>" + msgList[1] + "</span></li>")
			}
			ruleEngineHintShow(msgList.length - 2);// 是否显示提示
			$("#ruleEngineHint_number_div").html(msgList.length - 2);
		} else {
			$.info("警告", data.firstMessage, "warning");
		}
	});
}

/**
 * 获取产品信息
 * @param code 产品code
 */
function getProductItemByCode(code){
    return _.find(vm.productList, function (product) {
        return product.code == code;
    });
}
