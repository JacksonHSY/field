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
        markReportIdChange: '',
        markIsExecuteEngine: false,
        topupLoan: {},
        productList:[{ "name":"证大前前","code":"00025"}],
        selectAssetType:'',// 选择的资产类型
        nowYear:'',// 当前年
        calcWaterAverage: function (water1, water2, water3) {	// 计算月均流水
            var waterAverage = 0;
            waterAverage = (( (water1 || 0) + (water2 || 0) + (water3 || 0)) / 3).toFixed(0);
            return parseInt(waterAverage);
        },
        getApproveCheckStatementList: function (type) {			// 根据流水类型，获取流水明细信息
            if (type) {
                // 根据类型过滤，返回特定类型的流水信息(比如个人or对公)
                var checkStatementList = _.filter(vm.approvalInfo.approveCheckStatementList, function (checkStatement) {
                    return checkStatement.type == type;
                });

                if (checkStatementList.length == 0) {
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
        addCheckStatement: function (type) {
            vm.approvalInfo.approveCheckStatementList.push({
                infoId: vm.approvalInfo.approveCheckInfo.id,
                loanNo: vm.loanNo,
                type: type,
                water1: null,
                water2: null,
                water3: null
            });

            if (type == 'C') {    // 如果添加的是个人流水，则初始化个人流水numberbox
                var $newRecord = $('.personalRecordTr:last');
                $("input:lt(3)", $newRecord).numberbox({
                    groupSeparator: ',',
                    validType: ['compareMaxMin[0,99999999]'],
                    precision: 0,
                    buttonText: '元',
                    onChange: function () {
                        changeRecordCount($newRecord, "personal");
                    }
                });
            } else if (type == 'B') {   // 如果添加的是对公流水，则初始化对公流水numberbox
                var $newRecord = $('.publicRecordTr:last');
                $("input:lt(3)", $newRecord).numberbox({
                    groupSeparator: ',',
                    validType: ['compareMaxMin[0,99999999]'],
                    precision: 0,
                    buttonText: '元',
                    onChange: function () {
                        changeRecordCount($newRecord, "public");
                    }
                });
            }

        },
        // 删除流水记录
        deleteCheckStatement: function (index, element, event) {
            var $index = vm.approvalInfo.approveCheckStatementList.indexOf(element);
            vm.approvalInfo.approveCheckStatementList.splice($index, 1);
            if (element.type == 'C') {
                changeRecordCount($("tr.personalRecordTr:first", $("#moneyApprovalOpinion_record_table")), "personal");
            } else if (element.type == 'B') {
                changeRecordCount($("tr.publicRecordTr:first", $("#moneyApprovalOpinion_record_table")), "public");
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
            personalWaterAverageTotal: function () {
                // 获取个人月均流水合计
                var waterAverageTotal = 0;
                if (vm.approvalInfo) {
                    avalon.each(vm.personalCheckStatementArray, function (index, element) {
                        waterAverageTotal = waterAverageTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
                return parseInt(waterAverageTotal.toFixed(0));
            },
            publicWaterAverageTotal: function () {
                // 获取对公月均流水合计
                var waterAverageTotal = 0;
                if (vm.approvalInfo) {
                    avalon.each(vm.publicCheckStatementArray, function (index, element) {
                        waterAverageTotal += vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
                return parseInt(waterAverageTotal.toFixed(0));
            },
            waterIncomeTotal: function () {
                // 获取个人和对公月均流水合计
                var total = 0;
                if (vm.approvalInfo) {
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
        getRecordCount: function (water1, water2, water3) {
            return (isNotNull(water1) || isNotNull(water2) || isNotNull(water3)) ? vm.calcWaterAverage(water1, water2, water3) : '';
        },
        getWaterTotal: function (type) {
            var waterTotal = 0;
            var ifNull = true;
            if (type == 'personalWaterAverageTotal') {
                if (vm.approvalInfo) {
                    avalon.each(vm.personalCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal = waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            if (type == 'publicWaterAverageTotal') {
                if (vm.approvalInfo) {
                    avalon.each(vm.publicCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal = waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            if (type == 'waterIncomeTotal') {
                if (vm.approvalInfo) {
                    avalon.each(vm.allCheckStatementArray, function (index, element) {
                        if (isNotNull(element.water1) || isNotNull(element.water2) || isNotNull(element.water3)) {
                            ifNull = false;
                        }
                        waterTotal = waterTotal + vm.calcWaterAverage(element.water1, element.water2, element.water3);
                    });
                }
            }
            return ifNull ? '' : waterTotal;
        },
        // 勾选资产信息
        getIfSelectAssetType:function(type){
            var assetType =  vm.selectAssetType.split(",");
            return $.inArray(type,assetType) >=0 ;
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
        // 加载审批产品
         getApproveProduct($("#money_selectAssetType_hidden").val(),"INIT");
        // 获取省市级联信息
        post(ctx.rootPath() + "/bmsBasiceInfo/getProvinceCorrespond", null, null, function (data) {
            // 房产信息
            inintArea(data, "#moneyCustomerInfo_house_province_combobox", "#moneyCustomerInfo_house_city_combobox", "#moneyCustomerInfo_house_country_combobox");
            // 学历信息地区
            inintArea(data, "#money_education_province", "#money_education_city", null);
        });
        // 初始化选择资产信息
        $("input[name='selectAssetOption']", "#money_selectAssetOption_table").bind("click", function () {
            changeAssetType($(this).val(),$(this).is(':checked'));
        });
        // 初始化资产信息
        initAssetInfo();
        // 匹配速贷灰名单
        post(ctx.rootPath() + "/bdsApi/fastLoanBlacklist/" + $('#loanNo').val(), null, "json", function (data) {
            if (data.success) {
                if (data.data) {// 有速贷灰名单
                    var ifBlackRoster = $("#money_ifBlackRoster").val();
                    if ("N" == ifBlackRoster) {
                        $('#money_fastLoanBlacklist').combobox("setValue", "");
                    }
                } else {// 无速贷灰名单需要设置默认值并且不能修改
                    $('#money_fastLoanBlacklist').combobox({value: 'NOLOAN', readonly: true});
                }
                $("#money_ifBlackRoster").val(data.data ? "Y" : "N");
            }
        });

        // 个人流水加载
        $("#moneyApprovalOpinion_record_table").find("tr.personalRecordTr").each(function (ind, val) {
            $(val).find("input:lt(3)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                onChange: function () {
                    changeRecordCount($(val), "personal");
                }
            });
        });

        // 对公流水加载
        $("#moneyApprovalOpinion_record_table").find("tr.publicRecordTr").each(function (ind, val) {
            $(val).find("input:lt(3)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                onChange: function () {
                    changeRecordCount($(val), "public");
                }
            });
        });

        // topUp贷款
        var topUpHtml = $("#moneyApprovalOpinion_liabilities_table").find("tr.topUpTr");
        if (topUpHtml != null && topUpHtml.length > 0) {
            topUpHtml.find("input.input:eq(0)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                required: true,
                onChange: function () {
                    changeCalculatedLiabilities(topUpHtml);
                }
            });
            topUpHtml.find("input.input:eq(1)").numberbox({
                validType: ['compareMaxMin[0,360]'],
                precision: 0,
                buttonText: '月',
                required: true,
                onChange: function () {
                    changeCalculatedLiabilities(topUpHtml);
                }
            });
            topUpHtml.find("input.input:eq(2)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                required: true,
                onChange: function () {
                    countCalculatedLiabilities();
                }
            });
        }

        // 信用贷款
        $("#moneyApprovalOpinion_liabilities_table").find("tr.creditCardTr").each(function (ind, val) {
            $(val).find("input.input:eq(0)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                onChange: function () {
                    changeCalculatedLiabilities($(val));
                },
            });
            $(val).find("input.input:eq(1)").numberbox({
                validType: ['compareMaxMin[0,360]'],
                precision: 0,
                buttonText: '月',
                onChange: function () {
                    changeCalculatedLiabilities($(val));
                }
            });
            $(val).find("input.input:eq(2)").numberbox({
                groupSeparator: ',',
                validType: ['compareMaxMin[0,99999999]'],
                precision: 0,
                buttonText: '元',
                onChange: function () {
                    countCalculatedLiabilities();
                },
            });
            if (0 == ind) {
                creditCardTrFirstNumberbox = $(val);
            }
        });

        var $approvalInfoContext = $("#moneyApprovalOpinion_approvalInfo_from");

        // 加载核实收入
        $approvalCheckIncome = $("input[name='approvalCheckIncome']", $approvalInfoContext).numberbox({
            groupSeparator: ',',
            required: true,
            validType: ['compareMaxMin[1,99999999]'],
            precision: 0,
            buttonText: '元',
            onChange: function (newValue, oldValue) {
                calculateMonthPayAndDebtRate(); // 改变核实收入，则重新计算月还款额和负债率
            }
        });

        // 加载初审审批额度
        $approvalLimit = $("input[name='approvalLimit']", $approvalInfoContext).numberbox({
            required: true,
            min: 0,
            buttonText: '元',
            groupSeparator: ',',
            validType: ['numberMultiple[1000]'],
            onChange: function (newValue, oldValue) {
                reloadProductLimit();           // 重新加载审批期限下拉框
                calculateMonthPayAndDebtRate(); // 重新计算月还款额和负债率
            }
        });

        // 加载期限
        $approvalTerm = $("input[name='approvalTerm']", $approvalInfoContext).combobox({
            required: true,
            panelHeight: 'auto',
            editable: false,
            valueField: 'auditLimit',
            textField: 'auditLimit',
            missingMessage: '请选择审批期限',
            onChange: function (newValue, oldValue) {
                reloadProductUpperLower();         // 获取审批金额上下限
                calculateMonthPayAndDebtRate(); // 计算负债率
                if (isNotNull(newValue) && newValue != $(this).parents("table").find("tr:eq(1)").find("td:eq(1)").html()) {
                    $.info("提示", "申请期限和审批期限不一致!", "warning")
                }
            },
            onLoadSuccess: function () {
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
            required: true,
            data:vm.productList,
            editable: false,
            valueField: 'code',
            textField: 'name',
            onChange: function (newValue, oldValue) {
                if (isNotNull(newValue)) {
                    vm.applyBasicInfo.productCd = newValue;
                    reloadProductUpperLower();          // 获取审批金额上下限
                    reloadProductLimit();               // 获取审批期限
                    calculateMonthPayAndDebtRate();     // 计算负债率
                }
            },
            onLoadSuccess: function () {
                $approvalProductCd = $(this);
                // 产品列表下拉框加载成功后，判断已保存的产品CODE是否已过期，已过期则清空产品CODE，让用户重新选择
                var productCode = $approvalProductCd.combobox("getValue");
                if (isNotNull(productCode)) {// 防止产品过期
                    var defVal = $approvalProductCd.combobox("options").finder.getRow($approvalProductCd[0], productCode);
                    if (isNotNull(defVal)) {
                        reloadProductUpperLower();      // 获取审批金额上下限
                        reloadProductLimit();           // 获取审批期限
                        calculateMonthPayAndDebtRate(); // 计算负债率
                    } else {
                        $(this).combobox("setValue", "");
                    }
                }
            }
        });

        // 浮动显示申请人信息
        $("#moneyApprovalOpinion_approveInfo_div").addClass("approve_info_fixed");

        // 计算外部总负债
        var countType = 'init';// 标记是否是需要计算(如果央行报告不为空需要重新计算)
        var isLoanLimitInfo = isNotNull(vm.loanLimitInfo) && (vm.loanLimitInfo.loan.length > 0 || vm.loanLimitInfo.car.length > 0 || vm.loanLimitInfo.house.length > 0 || vm.loanLimitInfo.other.length > 0);
        var isReportInfo = isNotNull(vm.reportInfo) && vm.reportInfo.debt > 0;
        var isTopup = isNotNull(vm.topupLoan) && vm.topupLoan.returneterm > 0;// TOPUP负债信息
        if (isLoanLimitInfo || isReportInfo || isTopup) { // 如果央行报告不为空需要重新计算外部负责总额
            countType = null;
        }
        changeCalculatedLiabilities($(creditCardTrFirstNumberbox), countType);
        countCalculatedLiabilities(countType);

        // 计算初审意见备注框剩余可输入字数
        var $approvalMemo = $('#money_approvalMemo');
        var approvalMemo = $approvalMemo.textbox('getValue');
        var $approvalMemoArea = $("#money_approvalMemo").parents("td").find('.countSurplusText');
        $approvalMemoArea.html("剩余可输入" + (approvalMemo.length > 200 ? 0 : 200 - approvalMemo.length) + "字");

        //begin 页面加载重新计算信用卡负债
        var val = $("#moneyApprovalOpinion_liabilities_table").find("input[name='creditUsedLimit']").val();
        if (isNotNull(val)) {
            $("#money_liabilitiesCreditCard").html(numberFomatter((val / 10).toFixed(0)));
            $("#moneyApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val((val / 10).toFixed(0));
        } else {
            $("#money_liabilitiesCreditCard").html(0);
            $("#moneyApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val(0);
        }
        //end



        // 资产信息房产信息时选择房产信息同住宅信息
        $("[name='estateSameRegistered']","#money_customerHome_div").on("change", function (e) {
            var yesOrNo = $(e.target).val();
            if (yesOrNo == 'Y') {
                // 选择是时，房产地址回填家庭地址内容，显示为文本不可编辑
                var homeProv = vm.applyInfo.applicantInfo.personalInfo.homeStateId;// 住宅省
                var homeCity = vm.applyInfo.applicantInfo.personalInfo.homeCityId;// 住宅市
                var homeCoun = vm.applyInfo.applicantInfo.personalInfo.homeZoneId;// 住宅区
                var homeAddr = vm.applyInfo.applicantInfo.personalInfo.homeAddress;// 家庭地址
                //验证家庭地址是否完整
                if (isEmpty(homeProv) || isEmpty(homeCity) || isEmpty(homeCoun) || isEmpty(homeAddr)) {
                    $.info("提示", "家庭地址不完整，请先填写完整家庭地址!", "warning");
                    $("[name='estateSameRegistered']","#money_customerHome_div").get(0).checked = false;
                    return;
                }
                $("#moneyCustomerInfo_house_province_combobox").combobox('setValue', homeProv);
                $("#moneyCustomerInfo_house_city_combobox").combobox("setValue", homeCity);
                $("#moneyCustomerInfo_house_country_combobox").combobox("setValue", homeCoun);
                $("#money_customerInfo_estateAddress").textbox('setValue', homeAddr);

                $('#moneyCustomerInfo_house_province_combobox').combobox("readonly", true);
                $('#moneyCustomerInfo_house_city_combobox').combobox("readonly", true);
                $('#moneyCustomerInfo_house_country_combobox').combobox("readonly", true);
                $('#money_customerInfo_estateAddress').textbox("readonly", true);
            } else {
                // 选择否时，省、市、区/县为下拉框，详细地址为文本框，可编辑
                $("#moneyCustomerInfo_house_province_combobox").combobox("setValue", "");
                $("#moneyCustomerInfo_house_city_combobox").combobox("setValue", "");
                $("#moneyCustomerInfo_house_country_combobox").combobox("setValue", "");
                $("#money_customerInfo_estateAddress").textbox('setValue', '');

                $('#moneyCustomerInfo_house_province_combobox').combobox("readonly", false);
                $('#moneyCustomerInfo_house_city_combobox').combobox("readonly", false);
                $('#moneyCustomerInfo_house_country_combobox').combobox("readonly", false);
                $('#money_customerInfo_estateAddress').textbox("readonly", false);
            }
        });
        // 初始化页面资产信息样式和验证
        $("input[name='selectAssetOption']", "#money_selectAssetOption_table").each(function(ind,val){
            changeAssetTypeSetting($(val).val(),$(val).is(':checked'));
        });
        // 计算投保总年缴金额
        countPolicyInfoYearPaymentAmt();
    }, 200);
});


/**
 * 征信初判对话框
 */
function moneyApprovalOpinionContrastDialog() {
    var loanNo = $('#loanNo').val();
    post(ctx.rootPath() + "/creditzx/getCreditJudgment/2/" + loanNo, null, "json", function (data) {
        if (data.success) {
            // 央行征信报告
            var credit = data.data[0];
            if (isNotNull(credit.result)) {
                $("#moneyApprovalOpinion_credit_table").append('<tr><td> 征信信息：' + credit.firstMessage + '</td></tr>');
            }
            if (isNotNull(credit.status)) {
                var html = '<tr><th>人行近3个月查询次数:</th><td>' + (isNotNull(credit.threeMonthqueryCount) ? credit.threeMonthqueryCount : "0") + '</td>' + '<th>人行近1个月查询次数:</th><td>' + (isNotNull(credit.oneMonthqueryCount) ? credit.oneMonthqueryCount : "0") + '</td></tr>' + '<tr><th>信用卡总额度:</th><td>' + (isNotNull(credit.creditLimitMoney) ? credit.creditLimitMoney : "0") + '</td>' + '<th>信用卡已用额度:</th><td>' + (isNotNull(credit.alreadyUseMoney) ? credit.alreadyUseMoney : "0") + '</td></tr>' + '<tr><th>信用卡负债:</th><td>' + (isNotNull(credit.debt) ? credit.debt : "0") + '</td></tr>';
                $("#moneyApprovalOpinion_credit_table").append(html);
            }
            // 贷款记录
            var loanRecord = data.data[1];
            if (!isNotNull(loanRecord)) {
                $("#moneyApprovalOpinion_debt_table").append('<tr><td colspan="5">' + loanRecord.firstMessage + '</td></tr>');
            } else {
                // 信用贷款
                $("#moneyApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>信用贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.loan) && loanRecord.loan.length > 0) {
                    $.each(loanRecord.loan, function (key, v) {
                        var trHtml = '<tr><td>信用贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#moneyApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#moneyApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }

                // 房贷
                $("#moneyApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>房贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.house) && loanRecord.house.length > 0) {
                    $.each(loanRecord.house, function (key, v) {
                        var trHtml = '<tr><td>房贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#moneyApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#moneyApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }

                // 车贷
                $("#moneyApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>车贷</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.car) && loanRecord.car.length > 0) {
                    $.each(loanRecord.car, function (key, v) {
                        var trHtml = '<tr><td>车贷' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#moneyApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#moneyApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }
                // 其他贷款
                $("#moneyApprovalOpinion_debt_table").append('<tr class="specialTrCol"><td>其他贷款</td><td>额度/元</td><td>期限/月</td><td>负债/元</td><td>对应关系</td></tr>');
                if (isNotNull(loanRecord.other) && loanRecord.other.length > 0) {
                    $.each(loanRecord.other, function (key, v) {
                        var trHtml = '<tr><td>其他贷款' + (key + 1) + '</td><td>' + (isNotNull(v.grantMoney) ? v.grantMoney : "0") + '</td><td>' + (isNotNull(v.repayPeriods) ? v.repayPeriods : "0") + '</td><td></td><td>' + (isNotNull(v.no) ? v.no : "") + '</td></tr>';
                        $("#moneyApprovalOpinion_debt_table").append(trHtml);
                    })
                } else {
                    $("#moneyApprovalOpinion_debt_table").append("<tr><td colspan='5'>未找到记录</td></tr>");
                }
            }
        }
    });
    $("#moneyApprovalOpinion_contrast_Dialog").removeClass("display_none").dialog({
        title: "征信初判",
        modal: true,
        width: 600,
        height: 400,
        onClose: function () {
            $("#moneyApprovalOpinion_credit_table").html('');// 关闭时清空table
            $("#moneyApprovalOpinion_debt_table").html('');
        },
        buttons: [{
            text: '取消',
            iconCls: 'fa fa-reply',
            handler: function () {// 关闭时清空table
                $("#moneyApprovalOpinion_contrast_Dialog").dialog("close");
                $("#moneyApprovalOpinion_credit_table").html('');
                $("#moneyApprovalOpinion_debt_table").html('');
            }
        }]
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
        changeRecordCount($("#moneyApprovalOpinion_record_table").find("tr.personalRecordTr:first"), "personal");

    } else if ("publicRecordTr" == $(obj).parents("tr").attr("class")) {
        $(obj).parents("tr").remove();
        // 重新统计对公流水行数量，重新计算对公流水月均合计
        changeRecordCount($("#moneyApprovalOpinion_record_table").find("tr.publicRecordTr:first"), "public");

    } else if ("creditCardTr" == $(obj).parents("tr").attr("class")) {
        // 负债率计算
        $(obj).parents("tr").remove();
        countCalculatedLiabilities();
        $('#moneyApprovalOpinion_liabilities_table').find("input[value='" + type + "']").each(function (ind, val) {
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
    html.find("input.textbox-value").each(function (ind, val) {
        var value = $(val).val();
        if (value != "" && !isNaN(value)) {
            isNull = false;
            countTr = countTr + parseInt(value);
        }
    });
    var waterCount;
    if (isNull) { //如果三个流水输入框全部为空
        waterCount = '';
    } else {
        waterCount = numberFomatter((countTr / 3).toFixed(0));
    }
    html.find("td:eq(1)").html(waterCount + "元");

    var personalWaterIsNull = true;
    var publicWaterIsNull = true;
    if ("personal" == type) {
        // 计算个人流水月均合计
        var countMonth = 0;
        $("#moneyApprovalOpinion_record_table").find("tr.personalRecordTr").each(function (ind, valTr) {
            var countTd = 0;
            $(valTr).find("input.textbox-value").each(function (ind, val) {
                var value = $(val).val();
                if (value != "" && !isNaN(value)) {
                    personalWaterIsNull = false;
                    countTd = countTd + parseInt(value);
                }
            });
            countMonth = countMonth + parseInt((countTd / 3).toFixed(0));
        });
        var personalRecordCount;
        if (personalWaterIsNull) { //如果流水输入框全部为空
            personalRecordCount = '';
        } else {
            personalRecordCount = numberFomatter((countMonth));
        }
        $("#moneyApprovalOpinion_record_table").find("td.personalRecordCount").html(personalRecordCount + "元");
        $("#moneyApprovalOpinion_record_table").find("input[name='personalWaterTotal']").val(countMonth);

    } else {
        // 计算对公流水月均合计
        html.find("input[name='publicMonthAverage']").val((countTr / 3).toFixed(0));
        var countMonth = 0;
        $("#moneyApprovalOpinion_record_table").find("tr.publicRecordTr").each(function (ind, valTr) {
            var countTd = 0;
            $(valTr).find("input.textbox-value").each(function (ind, val) {
                var value = $(val).val();
                if (value != "" && !isNaN(value)) {
                    publicWaterIsNull = false;
                    countTd = countTd + parseInt(value);
                }
            });
            countMonth = countMonth + parseInt((countTd / 3).toFixed(0));
        });

        var publicRecordCount;
        if (publicWaterIsNull) { //如果流水输入框全部为空
            publicRecordCount = '';
        } else {
            publicRecordCount = numberFomatter(countMonth);
        }

        $("#moneyApprovalOpinion_record_table").find("td.publicRecordCount").html(publicRecordCount + "元");
        $("#moneyApprovalOpinion_record_table").find("input[name='publicWaterTotal']").val(countMonth);

    }
    // 计算个人+对公流水月均合计
    var total = (parseInt($("#moneyApprovalOpinion_record_table").find("input[name='personalWaterTotal']").val()) + parseInt($("#moneyApprovalOpinion_record_table").find("input[name='publicWaterTotal']").val())).toFixed(0);
    var publicRecordCountTd = $("#moneyApprovalOpinion_record_table").find("td.publicRecordCount").text();
    var personalRecordCountTd = $("#moneyApprovalOpinion_record_table").find("td.personalRecordCount").text();


    var totalWater;
    if (publicRecordCountTd == "元" && personalRecordCountTd == "元") {
        totalWater = '';
    } else {
        totalWater = numberFomatter(total);
    }
    $("#moneyApprovalOpinion_record_table").find("td.recordCount").html(totalWater + "元");
    $("#moneyApprovalOpinion_record_table").find("input[name='waterIncomeTotal']").val(total);
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
        validType: ['compareMaxMin[0,99999999]'],
        precision: 0,
        buttonText: '元',
        groupSeparator: ',',
    });
    html.find("input.input:odd").numberbox({
        min: 0,
        precision: 0,
        buttonText: '月'
    });
}

/**
 * 添加信贷
 * @param title
 * @param type
 */
function addCredit(title, type) {
    var length = $("#moneyApprovalOpinion_liabilities_table").find("input[value='" + type + "']").length + 1;
    var html = $('<tr class="creditCardTr"><td>' + title + '<span>' + length + '</span><input type="hidden" name="debtType" value="' + type + '"></td><td><input type="text" class="input" name="creditLoanLimit"></td><td><input type="text" class="input" name="creditLoanTerm"></td><td><input type="text" class="input" name="creditLoanDebt"></td><td><input type="hidden" name="creditNo" placeholder="对应关系"></td><td><a href="javaScript:void(0);" onclick=deleteTr(this,"' + type + '")><i class="fa fa-minus" aria-hidden="true"></i></a></td></tr>');
    $("#moneyApprovalOpinion_liabilities_table").find("input[value='" + type + "']").parents("tr:last").after(html);
    html.find("input.input:eq(0)").numberbox({
        validType: ['compareMaxMin[0,99999999]'],
        precision: 0,
        buttonText: '元',
        groupSeparator: ',',
        onChange: function () {
            changeCalculatedLiabilities(html);
        }
    });
    html.find("input.input:eq(1)").numberbox({
        validType: ['compareMaxMin[0,360]'],
        buttonText: '月',
        onChange: function () {
            changeCalculatedLiabilities(html);
        }
    });
    html.find("input.input:eq(2)").numberbox({
        validType: ['compareMaxMin[0,99999999]'],
        precision: 0,
        buttonText: '元',
        groupSeparator: ',',
        onChange: function () {
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
    $("#money_liabilitiesCreditCard").html(numberFomatter((val / 10).toFixed(0)));
    $("#moneyApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val((val / 10).toFixed(0));
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
            if ("CREDITLOAN" == debtType) {
                feeRate = 0.023;
            } else if ("HOUSELOAN" == debtType) {
                feeRate = 0.00408;
            } else if ("CARLOAN" == debtType) {
                feeRate = 0.00625;
            }
            var value = parseInt(quota) / parseInt(term) + parseInt(quota) * feeRate;
            html.find("input[textboxname='creditLoanDebt']").numberbox("setValue", value.toFixed(0));
        }

        countCalculatedLiabilities(type);
        // 额度和期限有一个请求的情况下不清空负责信息
    }
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
    var creditDebt = $("#moneyApprovalOpinion_liabilities_table").find("input[name='creditDebt']").val()
    if (isNotNull(creditDebt)) {
        count = parseInt(creditDebt);
    }
    outCount = count;
    $("#moneyApprovalOpinion_liabilities_table").find("tr.creditCardTr,tr.topUpTr").each(function (ind, val) {
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
    $("#moneyApprovalOpinion_liabilities_table").find("td.creditCountLiabilities").html(numberFomatter(outCount.toFixed(0)));
    $("#outCreditDebtTotal_id").val(outCount.toFixed(0));
    // 外部负债总和
    var min = count.toFixed(0) > 99999999 ? 99999999 : count.toFixed(0);
    if ("init" != type) {
        $("#outDebtTotal_id").numberbox({
            validType: ['compareMaxMin[' + min + ',99999999]'],
            value: count.toFixed(0),
        });
    } else {
        $("#outDebtTotal_id").numberbox({
            validType: ['compareMaxMin[' + min + ',99999999]'],
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
    var productLimit = bmsBasicApi.getProductUpperLower(approvalProductCd, owningBranchId, auditLimit, ("Y" == vm.applyBasicInfo.ifPreferentialUser ? 0 : 1));
    var applyAmount = parseInt($("#moneyApprovalOpinion_applyMoney_hidden").val());// 申请金额
    if (isNotNull(productLimit.upperLimit)) {
        applyAmount = parseFloat(productLimit.upperLimit) >= parseFloat(applyAmount) ? applyAmount : productLimit.upperLimit;
    }
    $approvalLimit.numberbox({
        validType: ['compareMaxMin[' + (productLimit.floorLimit || 1) + ',' + parseInt(applyAmount) + ']', 'numberMultiple[1000]']
    });
}

/**
 * 重新加载审批产品期限下拉框
 */
function reloadProductLimit() {
    var owningBranchId = vm.applyBasicInfo.owningBranchId;		// 进件营业部id
    var approvalProductCd = $approvalProductCd.combobox("getValue");// 审批产品
    var approvalLimit = $approvalLimit.numberbox("getValue");// 审批金额
    var params = "productCode=" + approvalProductCd + "&owningBranchId=" + owningBranchId + "&isCanPreferential=" + ("Y" == vm.applyBasicInfo.ifPreferentialUser ? 0 : 1);
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
            var rate =null;
            if ("00025" != approvalProductCd) {
                var productRate = coreApi.getProductRateByCode(approvalProductCd, approvalTerm);        // 获取产品费率
                rate = productRate.rate;                                                    // 产品综合费率
                if ("Y" == vm.applyBasicInfo.ifPreferentialUser) {                                // 判断是否是优惠客户，是则取优惠费率作为计算
                    rate = productRate.preferRate;
                }
            } else {
                rate = 0.018;
            }
            if (isEmpty(rate)) {  // 判断产品费率是否合法
                $.info('提示', '产品费率问题请联系管理员!', 'warning');
                $approvalTerm.combobox("setValue", '');
                return false;
            }

            var approvalTerm = parseFloat(approvalTerm);   // 审批期限
            // 月还款额 = 审批额度 / 审批期限 + 审批额度 * 产品费率
            var approvalMonthPay = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(2);
            var approvalMonthPayShow = (parseFloat(approvalLimit / approvalTerm) + parseFloat(approvalLimit * rate)).toFixed(0);
            $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val(approvalMonthPayShow);
            $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html(numberFomatter(approvalMonthPayShow));
        if (!vm.markIsExecuteEngine) {// 不调用规则引擎
            // 内部负债率= 本次我司贷款的月还款额 / 月核实收入 * 100%，四舍五入保留一位小数
            approvalDebtTate = (approvalMonthPay / approvalCheckIncome);
            $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(approvalDebtTate.toFixed(3));
            $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(approvalDebtTate) * 100).toFixed(1) + "%");
            // 总负债率 =（本次我司贷款的月还款额 + 外部信用负债总额） / 月核实收入 * 100%，四舍五入保留一位小数
            var approvalAllDebtRate = ((parseFloat(approvalMonthPay) + parseFloat(total)) / approvalCheckIncome);
            $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(approvalAllDebtRate.toFixed(3));
            $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(approvalAllDebtRate) * 100).toFixed(1) + "%");
        }
    } else {
        // 清空月还款额
        $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalMonthPay']").val('');
        $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalMonthPay").html('');
        // 清空内部负债率
        $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val('');
        $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html('');
        // 清空总负债率
        $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val('');
        $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html('');
    }
}

/**
 * 保存审批意见
 *
 * @author dmz
 * @date 2017年3月31日
 */
var moneyMarkSubmitOpinion = false;
function submit() {
    if (!moneyMarkSubmitOpinion) {
        // 验证负债信息 和 审批意见是否填写完整
        if ($("#moneyApprovalOpinion_approveCheckData_form").form("validate") && $("#moneyApprovalOpinion_approvalInfo_from").form("validate")) {
            if (!isNotNull(vm.selectAssetType) && "00025" !=vm.applyBasicInfo.productCd) {// 不是证大前前需要勾选产品
                $.info("提示", "客户资产信息未认证，不可提交！", "warning", 1000);
                return false;
            }

            // 定义资产信息对象
            var saveAssetObj = new Object();
            if (!validateAssetsInfo(saveAssetObj)) {
                return false;
            }
            // 审批意见必填
            if (!isNotNull($.trim($("#money_approvalMemo").val()))) {
                $("#money_approvalMemo").focus();
                $.info("提示", "请填写审批意见信息!", "warning", 1000);
                return false;
            }

            //征信平台已验证必填
            if (!$("#money_creditCheckException_checkbox").prop("checked")) {
                $('#money_creditCheckException_checkbox').focus();
                $.info("提示", "征信平台已验证未勾选!", "warning", 1000);
                return false;
            }

            // 资料核对
            var $checkDataForm = $("#moneyApprovalOpinion_approveCheckData_form");
            var $approvalForm = $("#moneyApprovalOpinion_approvalInfo_from");


            var formData = $.extend({}, $checkDataForm.serializeObject(true), $approvalForm.serializeObject());


            formData.creditRecord = $('#money_creditRecord_checkbox').is(':checked') ? 1 : 0;
            moneyMarkSubmitOpinion = true;

            // 构造资料核对信息
            formData = transformToCheckInfoAndStatement(formData);
            var params = new Object();
            params.approveInfo = formData;
            params.assetsInfo = saveAssetObj;
            $.ajax({
                url: ctx.rootPath() + "/firstApprove/updateMoneyApprovalOpinion",
                dataType: "JSON",
                method: 'post',
                data: {
                    loanNo: vm.loanNo,
                    form: JSON.stringify(params)
                },
                success: function (data) {
                    moneyMarkSubmitOpinion = false;
                    if (data.success) {
                        if (vm.markIsExecuteEngine) {// 调用规则引擎
                            executeRuleEngine(vm.loanNo, "XSCS04", null);
                        } else {
                            $.info("提示", "保存成功", "info", 1000);
                        }
                        // 清空车辆信息和房产信息
                        cleanCarInfoEstateInfo(saveAssetObj);
                    } else if ("FAILURE" == data.type) {
                        $.info("警告", data.firstMessage, "warning");
                    }
                },
                error: function (data) {
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
function transformToCheckInfoAndStatement(formData) {
    // 构造资料核对汇总信息
    formData.loanNo = vm.loanNo;
    formData.version =vm.applyInfo.baseInfo.version;
    formData["approveCheckInfo"] = {};
    formData.approveCheckInfo["loanNo"] = vm.loanNo;
    formData.approveCheckInfo["oneMonthsCount"] = formData.oneMonthsCount;
    formData.approveCheckInfo["threeMonthsCount"] = formData.threeMonthsCount;
    formData.approveCheckInfo["weekendPay"] = formData.weekendPay;
    formData.approveCheckInfo["creditCheckException"] = $('#money_creditCheckException_checkbox').is(':checked') ? 0 : 1;	// 征信平台验证无异常
    formData.approveCheckInfo["courtCheckException"] = $('#money_courtException_input').is(':checked') ? 0 : 1;			// 法院网核查无异常
    formData.approveCheckInfo["internalCheckException"] = $('#money_internalException_input').is(':checked') ? 0 : 1;		// 内部匹配无异常
    formData.approveCheckInfo["memo"] = formData.memo[0];

    formData["approveCheckStatementList"] = [];

    // 构造资料核对个人流水信息
    formData.personalWater1 = convertPropertyToArray(formData.personalWater1);
    formData.personalWater2 = convertPropertyToArray(formData.personalWater2);
    formData.personalWater3 = convertPropertyToArray(formData.personalWater3);
    for (var i = 0, len = formData.personalWater1.length; i < len; i++) {
        if (formData.personalWater1[i] || formData.personalWater2[i] || formData.personalWater3[i]) {
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
    for (var i = 0, len = formData.publicWater1.length; i < len; i++) {
        if (formData.publicWater1[i] || formData.publicWater2[i] || formData.publicWater3[i]) {
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
function convertPropertyToArray(property) {

    if (!(property instanceof Array)) { // 不是数组，则拼接数组
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
    $("#money_approvalMemo").parents("td").find(".countSurplusText").html("剩余可输入" + remainLength + "字");
}

/**
 * 系统初判
 *
 * @author dmz
 * @date 2017年7月6日
 * @param loanNo
 */
function moneySystemDetermine(loanNo) {
    if (vm.markIsExecuteEngine) {// 调用规则引擎
        // 定义资产信息对象
        var saveAssetObj = new Object();
        if (!validateAssetsInfo(saveAssetObj)) {
            return false;
        }
        if ($("#moneyApprovalOpinion_approveCheckData_form").form("validate")) {
            var obj = new Object();
            obj.approvalProductCd = $approvalProductCd.combobox("getValue");// 审批产品
            obj.monthAverage = parseInt($("#moneyApprovalOpinion_record_table").find("input[name='waterIncomeTotal']").val() || 0); // 月平均
            obj.outDebtTotal = parseInt($("#outDebtTotal_id").numberbox("getValue") || 0);// 外部负责总额
            obj.assetsInfo =  JSON.stringify(saveAssetObj);//资产信息对象字符串
            obj.selectAssetsType = vm.selectAssetType;// 选择的资产类型
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
    post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/" + executeType, obj, "JSON", function (data) {
        if (data.success) {
            var msgList = data.messages;
            if (isNotNull(data.data)) {
                if (isNotNull(data.data.adviceVerifyIncome)) {// 建议核实收入
                    $("#money_systemDetermine_table").find("td:first").html(data.data.adviceVerifyIncome);
                }
                if (isNotNull(data.data.adviceAuditLines)) {// 建议审批额度-建议到手金额
                    $("#money_systemDetermine_table").find("td:eq(1)").html(data.data.adviceAuditLines);
                }
                if (isNotNull(data.data.estimatedCost)) {// 预估评级费
                    $("#money_systemDetermine_table").find("td:last").html(data.data.estimatedCost);
                }
                if (isNotNull(data.data.internalDebtRatio)) {// 内部负债率
                    $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalDebtTate']").val(data.data.internalDebtRatio.toFixed(3));
                    $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalDebtTate").html((parseFloat(data.data.internalDebtRatio) * 100).toFixed(1) + "%");
                }
                if (isNotNull(data.data.totalDebtRatio)) {// 总负债率
                    $("#moneyApprovalOpinion_approvalInfo_from").find("input[name='approvalAllDebtRate']").val(data.data.totalDebtRatio.toFixed(3));
                    $("#moneyApprovalOpinion_approvalInfo_from").find("td.approvalAllDebtRate").html((parseFloat(data.data.totalDebtRatio) * 100).toFixed(1) + "%");
                }
                if (isNotNull(data.data.oneMonthsCount)) {// 近一个月查询
                    $("#moneyApprovalOpinion_oneMonthsCount").numberbox("setValue", data.data.oneMonthsCount);
                }
                if (isNotNull(data.data.threeMonthsCount)) {// 近三个月查询
                    $("#moneyApprovalOpinion_threeMonthsCount").numberbox("setValue", data.data.threeMonthsCount);
                }
                if (isNotNull(data.data.ifCreditRecord)) {// ifCreditRecord
                    $("#money_creditRecord_checkbox").prop("checked", data.data.ifCreditRecord == 'Y' ? true : false);
                }

                window.opener.firstSettingRuleEngineData(data.data);// 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
            }
            if ("HINT" == data.firstMessage) {
                window.opener.limitSubmitSetting("true");
                if ("XSCS04" == executeType) {
                    $.info("提示", "保存成功", "info", 1000);
                }
            } else if ("REJECT" == data.firstMessage) {
                $.info("提示", "规则引擎拒绝", "warning", 1000, function () {
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
 * 资产信息初始化
 */
function initAssetInfo() {
// ------------------------------------------------ begin 客户保单信息初始化---------------------------------------------------------------------
    if (isNotNull(vm.applyInfo.assetsInfo.policyInfo)) {
        // 加载保单贷信息
        $("#money_customer_POLICY_div").find("form").each(function ($ind, $polity) {
            // 投保公司
            $("#money_insuranceCompany_" + $ind).combobox({
                required: true,
                width: 460,
                editable: false,
                valueField: 'code',
                textField: 'nameCN',
                prompt: '请选择',
                missingMessage: '请选择投保公司',
                data: getEnum('InsuranceCompany')
            });

            //保险年限
            $("#money_customerInfo_assetsInfo_policyInfo_insuranceTerm_" + $ind).textbox({
                required: true,
                buttonText: '年',
                width: 210,
                missingMessage: "请填写保险年限!",
                validType: ["insurancePeriod[1, 99999999, '保险年限']"]
            });

            //保险金额
            $("#money_customerInfo_assetsInfo_policyInfo_insuranceAmt_" + $ind).numberbox({
                required: true,
                groupSeparator: ',',
                precision: 0,
                buttonText: '元',
                missingMessage: "请填写保险金额!",
                validType: ["range[0, 99999999, '保险金额']"]
            });

            //年缴金额
            $("#money_customerInfo_assetsInfo_policyInfo_yearPaymentAmt_" + $ind).numberbox({
                required: true,
                groupSeparator: ',',
                precision: 0,
                buttonText: '元',
                missingMessage: "请填写年缴金额!",
                validType: ["range[1, 99999999, '年缴金额']"],
                onChange:function () {
                    countPolicyInfoYearPaymentAmt();
                }
            });

            //已缴年限
            $("#money_customerInfo_assetsInfo_policyInfo_paidTerm_" + $ind).textbox({
                required: true,
                buttonText: '年',
                width: 210,
                missingMessage: "请填写已缴年限!",
                validType: ["range[0, 99999999, '已缴年限']"]
            });

            // 投保账号
            $("#money_insuranceAccount_" + $ind).textbox({
                required: true,
                missingMessage: "请填写投保账号",
                validType: ["accountAndPwd['投保账号']"]
            })

            // 投保账号密码
            $("#money_insurancePassword_" + $ind).textbox({
                required: true,
                missingMessage: "请填写投保账号密码",
                validType: ["accountAndPwd['投保账号密码']"]
            })
        });
    }
    // ------------------------------------------------ end 客户保单信息初始化---------------------------------------------------------------------

    // ------------------------------------------------ begin 客户车辆产信息初始化------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.carInfo.unabridged) {
        // 购车价格
        $("#money_customerInfo_carInfo_carBuyPrice").numberbox({
            required: true,
            groupSeparator: ',',
            precision: 0,
            buttonText: '元',
            missingMessage: "请填购车价格!",
            validType: ["nonnegativePnum['购车价格']", "range[0, 99999999, '购车价格']"]
        });

        // 购车时间
        $("#money_customerInfo_date_CAR").datebox({
            required: true,
            prompt: '购车时间',
            editable: false,
            missingMessage: "请选择购车时间!",
            validType: ["compareTimeLimitWithParam['1990年1月', '购车时间不可']", "compareNowDate['购车时间']"],
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });

        //车牌号
        $("#money_customerInfo_car_plateNum").textbox({
            required: true,
            missingMessage: "请填写车牌号!",
            validType: ["carPlateNum['车牌号']"]
        });


        // 车贷发放年月
        $("#money_customerInfo_carInfo_carLoanGrantDate").datebox({
            value: isNotNull(vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate) ? moment(vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate).format('YYYY-MM') : '',
            required:  'ING'==vm.applyInfo.assetsInfo.carInfo.carLoanStatus,
            prompt: '车贷发放年月',
            editable: false,
            missingMessage: "请选择车贷发放年月!",
            validType: ["compareTimeLimitWithParam['1998年1月', '车贷发放年月']", "compareNowDate['车贷发放年月']"],
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });

        // 车贷月还款额
        $("#money_Customer_Info_monthPaymentAmt").numberbox({
            groupSeparator: ',',
            required:  'ING'==vm.applyInfo.assetsInfo.carInfo.carLoanStatus,
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写车贷月还款额!",
            validType: ["nonnegativePnum['车贷月还款额']", "range[0, 99999999, '车贷月还款额']"],
        });

        //车贷发放机构
        $("#money_carLoanOrg").textbox({
            required:  'ING'==vm.applyInfo.assetsInfo.carInfo.carLoanStatus,
            width: 460,
            missingMessage: "请填写车贷发放机构!",
            validType: ["chiness","fiterBlank","length[0,50]"]
        });

        // 车贷情况
        $("#money_carLoanSituation").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            prompt: '请选择',
            missingMessage: '请选择车贷情况',
            data: getEnum('EstateType'),
            onChange:function (newValue,OldValue) {
                moneyCarLoanSelect(newValue);
            },
            onLoadSuccess: function () {
                var value = $(this).combobox("getValue");
                if (isNotNull(value)) {
                    moneyCarLoanSelect(value);
                }
            }
        });

        //交强险承保公司
        $("#money_carLoanInsuranceCompany").textbox({
            required: true,
            width: 460,
            missingMessage: "请填写交强险承保公司!",
            validType: ["fiterBlank","length[1,50]"]
        });
    }

    // ------------------------------------------------ end 客户车辆信息初始化----------------------------------------------------------------------

    // ------------------------------------------------ begin 客户公积金信息初始化------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.fundInfo.unabridged) {
        // 公积金账号
        $("#money_accumulationFundAccount").textbox({
            required: true,
            validType: ["accountAndPwd['公积金账号']"]
        });

        // 公积金账号密码
        $("#money_accumulationFundPassword").textbox({
            required: true,
            validType: ["accountAndPwd['公积金账号密码']"]
        })
    }

    // ------------------------------------------------ end 客户公积金信息初始化--------------------------------------------------------------------

    // ------------------------------------------------ begin 客户社保信息初始化--------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.socialInsuranceInfo.unabridged) {
        // 社保账号
        $("#money_socialSecurityAccount").textbox({
            required: true,
            validType: ["accountAndPwd['社保账号']"]
        });

        // 社保账号密码
        $("#money_socialSecurityPassword").textbox({
            required: true,
            validType: ["accountAndPwd['社保账号密码']"]
        })
    }
    // ------------------------------------------------ end 客户社保信息初始化----------------------------------------------------------------------

    // ------------------------------------------------ begin 客户信用卡信息初始化------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.cardLoanInfo.unabridged) {
        // 额度
        $("#money_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox({
            groupSeparator: ',',
            required: false,
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写信用卡额度!",
            validType: ["nonnegativePnum['信用卡额度']", "range[0, 99999999, '额度']"],
        });

        // 发卡银行
        $("#money_cardLoanInfo_issuingBank").textbox({
            required: false,
            validType: ["chineseAndNoSpace['发卡银行']","length[0,20]"],
            missingMessage: "请填写发卡银行!",
        });

        // 发卡时间
        $("#money_cardLoanInfo_issuerDate").datebox({
            required: true,
            prompt: '发卡时间',
            editable: false,
            missingMessage: "请填写发卡时间!",
            validType: ["compareNowDate['发卡时间']"],
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });
    }
    // ------------------------------------------------ end  客户卡友贷信息初始化-------------------------------------------------------------------

    // ------------------------------------------------ begin 客户房产信息初始化-------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.estateInfo.unabridged) {
        //房产类型
        $("#money_customerInfo_assetsInfo_estateInfo_estateType").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('FangType'),
            missingMessage: '请选择房产类型',
            prompt: '请选择',
        });

        //购房时间
        $("#money_customerInfo_date_ESTATE").datebox({
            value: isNotNull(vm.applyInfo.assetsInfo.estateInfo.estateBuyDate) ? moment(vm.applyInfo.assetsInfo.estateInfo.estateBuyDate).format('YYYY-MM') : '',
            required: true,
            editable: false,
            prompt: '购房时间',
            missingMessage: "请选择购房时间!",
            validType: ["compareTimeLimitWithParam['1980年1月', '购房时间不可']", "compareNowDate['购房时间']"],
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });

        //房产地址区信息
        extendOption('moneyCustomerInfo_house_country_combobox', {
            required: true,
            readonly:'Y'==vm.applyInfo.assetsInfo.estateInfo.estateSameRegistered,
            missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
        });

        //房产地址市信息
        extendOption('moneyCustomerInfo_house_city_combobox', {
            required: true,
            readonly:'Y'==vm.applyInfo.assetsInfo.estateInfo.estateSameRegistered,
            missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
        });

        //房产地址省信息
        extendOption('moneyCustomerInfo_house_province_combobox', {
            required: true,
            readonly:'Y'==vm.applyInfo.assetsInfo.estateInfo.estateSameRegistered,
            missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
        });

        //房产地址具体信息
        $("#money_customerInfo_estateAddress").textbox({
            required: true,
            readonly:'Y'==vm.applyInfo.assetsInfo.estateInfo.estateSameRegistered,
            validType: ["chineseAndNoSpace['房产地址输入框中']", "maxLength[200]"],
            missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空！"
        });

        //房贷情况
        $("#money_customerInfo_estateInfo_estateLoan").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('EstateType'),
            prompt: '请选择',
            missingMessage: '请选择房贷情况',
            //validType: ["comboboxUnSelectWithParam['房贷情况']"],
            onChange: function (newValue, oldValue) {
                    moneyHouseLoanSelect(newValue);
            },
            onLoadSuccess: function () {
                var value = $(this).combobox("getValue");
                if (isNotNull(value)) {
                    moneyHouseLoanSelect(value);
                }
            }
        });

        //房贷总额
        $("#money_estateInfo_loanCount").numberbox({
            groupSeparator: ',',
            required: 'ING'==vm.applyInfo.assetsInfo.estateInfo.estateLoan,
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写房贷总额!",
            validType: ["nonnegativePnum['房贷总额']", "range[0, 99999999, '房贷总额']"],
        });

        //月供
        $("#moneyCustomerInfo_house_monthPaymentAmt").numberbox({
            groupSeparator: ',',
            required: 'ING'==vm.applyInfo.assetsInfo.estateInfo.estateLoan,
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写房贷月还款额!",
            validType: ["nonnegativePnum['房贷月还款额']", "range[0, 99999999, '房贷月还款额']"]
        });

        //房贷发放年月
        $("#moneyCustomerInfo_house_estateLoanGrantDate").datebox({
            value: isNotNull(vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate) ? moment(vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate).format('YYYY-MM') : '',
            required: 'ING'==vm.applyInfo.assetsInfo.estateInfo.estateLoan,
            editable: false,
            missingMessage: "请选择房贷发放年月!",
            validType: ["compareTimeLimitWithParam['1980年1月', '房贷发放年月']", "compareNowDate['房贷发放年月']"],
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });
    }
    // ------------------------------------------------ end 客户房产信息初始化---------------------------------------------------------------------
    // ------------------------------------------------ begin 客户淘宝账号信息初始化---------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.masterLoanInfo.unabridged) {
        // 淘宝会员名
        $("#money_taoBaoInfo_account").textbox({
            required: true,
            validType: ["length[1,50]"],
        })
        //淘宝买家信用等级
        $("#money_taoBaoInfo_buyerCreditLevel").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('SellerCreditLevel'),
            prompt: '请选择',
            missingMessage: '请选择淘宝买家信用等级',
        });

        // 淘宝买家信用类型
        $("#money_taoBaoInfo_buyerCreditType").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('SellerCreditType'),
            prompt: '请选择',
            missingMessage: '请选择淘宝买家信用类型',
        });

        // 淘宝年消费额
        $("#money_taoBaoInfo_totalConsumption").numberbox({
            groupSeparator: ',',
            required: true,
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写淘宝年消费额!",
            validType: ["nonnegativePnum['淘宝年消费额']", "range[0, 99999999, '淘宝年消费额']"],
        });

        // 淘气值
        $('#money_taoBaoInfo_naughtyValue').numberbox({
            groupSeparator: ',',
            required: true,
            precision: 0,
            missingMessage: "请填写淘气值!",
            validType: ["range[400, 9999, '淘气值']"]
        });

        // 芝麻信用分
        $("#money_taoBaoInfo_sesameCreditValue").numberbox({
            required: true,
            precision: 0,
            missingMessage: "请填写芝麻信用分!",
            validType: ["range[350,950, '芝麻信用分']"]
        });

        //花呗额度
        $("#money_taoBaoInfo_antSpend").numberbox({
            groupSeparator: ',',
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写花呗额度!",
            validType: ["nonnegativePnum['花呗额度']", "range[0, 99999999, '花呗额度']"],
        });

        //借呗额度
        $("#money_taoBaoInfo_antBorrow").numberbox({
            groupSeparator: ',',
            precision: 0,
            buttonText: '元',
            missingMessage: "请填写借呗额度!",
            validType: ["nonnegativePnum['借呗额度']", "range[0, 99999999, '借呗额度']"],
        });
    }
    // ------------------------------------------------ end 客户淘宝账号信息初始化---------------------------------------------------------------------
    // ------------------------------------------------ begin 资产信息学历信息-------------------------------------------------------------------------
    if ("Y" == vm.applyInfo.assetsInfo.educationInfo.unabridged) {
        // 院校名称
        $("#money_education_schoolName").textbox({
            required: true,
            missingMessage: "请填写院校名称!",
            validType: ["notAllNumberAndLength[50, '院校名称']"]
        });

        // 最高学历
        $("#money_education_qualification").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            prompt: '请选择',
            missingMessage: '请选择最高学历',
            data: getEnum('HighestEducation')
        });

        //毕业时间
        $("#money_education_graduationDate").datebox({
            value: isNotNull(vm.applyInfo.assetsInfo.educationInfo.graduationDate) ? moment(vm.applyInfo.assetsInfo.educationInfo.graduationDate).format('YYYY-MM') : '',
            required: true,
            prompt: '毕业时间',
            editable: false,
            validType: ["compareTimeLimitWithParam['1980年1月', '毕业时间']", "compareNowDate['毕业时间']"],
            missingMessage: "请填写毕业时间！",
            formatter: myformatter,
            parser: myparser
        }).datebox('calendar').calendar({
            validator: function (date) {
                var now = new Date();
                var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return day >= date;
            }
        });

        //学信网用户名
        $("#money_education_chsiAccount").textbox({
            required: '00001' == vm.applyInfo.assetsInfo.educationInfo.educationExperience,
            validType: ["userAndPwd['学信网用户名']"]
        });

        // 学信网密码
        $("#money_education_chsiPassword").textbox({
            required: '00001' == vm.applyInfo.assetsInfo.educationInfo.educationExperience,
            validType: ["userAndPwd['学信网密码']"]
        });

        //认证书编号
        $("#money_education_certificateNumber").textbox({
            required:'00002' == vm.applyInfo.assetsInfo.educationInfo.educationExperience,
            validType:["certificateNumber['"+vm.nowYear+"']"],
        })


        // 教育经历
        $("#money_education_experience").combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('eduExperience'),
            prompt: '请选择教育经历',
            missingMessage: '请选择教育经历',
            onChange:function(newValue, oldValue) {
                if ("00002" == newValue) {
                    $("#money_education_chsiAccount").textbox($("#money_education_chsiAccount").textbox("options").required = false);
                    $("#money_education_chsiAccount").parent("td").prev("th").removeClass("requiredFontWeight");
                    $("#money_education_chsiPassword").textbox( $("#money_education_chsiPassword").textbox("options").required=false);
                    $("#money_education_chsiPassword").parent("td").prev("th").removeClass("requiredFontWeight");
                    $("#money_education_certificateNumber").textbox( $("#money_education_certificateNumber").textbox("options").required=true);
                    $("#money_education_certificateNumber").parent("td").prev("th").addClass("requiredFontWeight");
                    // 隐藏地区
                    $(this).parents("tr").find("td:eq(2)>span:first").addClass("display_noH");
                    $(this).parents("tr").find("td:eq(2)>span:eq(1)").removeClass("display_noH");
                    $(this).parents("tr").find("td:eq(3)").find("span:first").addClass("display_noH");
                    // 去掉地区验证
                    $("#money_education_province").combobox("disableValidation");
                    $("#money_education_city").combobox("disableValidation");
                } else {
                    $("#money_education_chsiAccount").textbox($("#money_education_chsiAccount").textbox("options").required = true);
                    $("#money_education_chsiAccount").parent("td").prev("th").addClass("requiredFontWeight");
                    $("#money_education_chsiPassword").textbox( $("#money_education_chsiPassword").textbox("options").required=true);
                    $("#money_education_chsiPassword").parent("td").prev("th").addClass("requiredFontWeight");
                    $("#money_education_certificateNumber").textbox( $("#money_education_certificateNumber").textbox("options").required=false);
                    $("#money_education_certificateNumber").parent("td").prev("th").removeClass("requiredFontWeight");
                    // 显示地区
                    $(this).parents("tr").find("td:eq(2)>span:first").removeClass("display_noH");
                    $(this).parents("tr").find("td:eq(2)>span:eq(1)").addClass("display_noH");
                    $(this).parents("tr").find("td:eq(3)").find("span:first").removeClass("display_noH");
                    // 添加地区验证
                    $("#money_education_province").combobox("enableValidation");
                    $("#money_education_city").combobox("enableValidation");
                }
            },
            onLoadSuccess: function () {
                var value = $(this).combobox("getValue");
                if ("00002" == value) {
                    $("#money_education_province").combobox("disableValidation");
                    $("#money_education_city").combobox("disableValidation");
                } else {
                    $("#money_education_province").combobox("enableValidation");
                    $("#money_education_city").combobox("enableValidation");
                }
            }
        });
    }
// ------------------------------------------------ end 资产信息学历信息-------------------------------------------------------------------------
}

/**
 * 获取枚举值下拉框
 * @param type
 * @returns {Array}
 */
function getEnum(type) {
    var enumList = [];
    // 获取各种枚举值
    post(ctx.rootPath() + "/bmsBasiceInfo/getEnumCode", {emnuType: type, app: 1}, null, function (result) {
        if (isNotNull(result)) {
            enumList = result;
        }
    });
    return enumList;
}

/**
 * 扩展easyui空间选项
 * @param id
 * @param options
 */
function extendOption(id, options) {
    var defaultOptions = $('#' + id).combobox('options');
    options = $.extend({}, defaultOptions, options);
    $('#' + id).combobox(options);
}

/**
 * 房贷为"还款中"时，月供、房贷发放年月必填，单据户名为本人为非必填
 * @param newValue
 * @param oldValue
 */
function moneyHouseLoanSelect(newValue, oldValue) {
    var loanCount = $("#money_estateInfo_loanCount").numberbox("options");// 房贷总额
    var loanGrantDateOption = $('#moneyCustomerInfo_house_estateLoanGrantDate').datebox('options');//房贷发放年月
    var monthPaymentAmtOption = $('#moneyCustomerInfo_house_monthPaymentAmt').numberbox('options');//房贷月还款额
    if ("ING" == newValue) {    // 还款中
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: true,
            readonly: false,
            value:isNotNull(vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate) ? moment(vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate).format('YYYY-MM') : ''
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: true,
            readonly: false,
            value:vm.applyInfo.assetsInfo.estateInfo.monthPaymentAmt
        });
        loanCount = $.extend({},loanCount,{
            required:true,
            readonly: false,
            value:vm.applyInfo.assetsInfo.estateInfo.estateLoanAmt
        });
        $("#money_estateInfo_loanCount").parent("td").prev("th").addClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_estateLoanGrantDate").parent("td").prev("th").addClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_monthPaymentAmt").parent("td").prev("th").addClass("requiredFontWeight");
    } else if ("ALL" == newValue || "END" == newValue) {    // 全款中、已结清
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: false,
            readonly: true,
            value:'',
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: false,
            readonly: true,
            value:'',
        });
        loanCount = $.extend({},loanCount,{
            required:false,
            readonly: true,
            value:'',
        });
        $("#money_estateInfo_loanCount").parent("td").prev("th").removeClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_estateLoanGrantDate").parent("td").prev("th").removeClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_monthPaymentAmt").parent("td").prev("th").removeClass("requiredFontWeight");
    } else {
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: false,
            readonly: true,
            value: ''
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: false,
            readonly: true,
            value: ''
        });
        loanCount = $.extend({},loanCount,{
            required:false,
            readonly: true,
            value:"",
        });
        $("#money_estateInfo_loanCount").parent("td").prev("th").removeClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_estateLoanGrantDate").parent("td").prev("th").removeClass("requiredFontWeight");
        $("#moneyCustomerInfo_house_monthPaymentAmt").parent("td").prev("th").removeClass("requiredFontWeight");
    }
    $("#money_estateInfo_loanCount").numberbox(loanCount);// 房贷总额
    $('#moneyCustomerInfo_house_estateLoanGrantDate').datebox(loanGrantDateOption);
    $('#moneyCustomerInfo_house_monthPaymentAmt').numberbox(monthPaymentAmtOption);
}

/**
 * 选择资产信息
 * @param assetType
 * @param isCheck
 */
function changeAssetType(assetType,isCheck) {

    changeAssetTypeSetting(assetType,isCheck);
    // 获取勾选资产信息
    var  selectAssetOptionsType = '';
    $("#money_selectAssetOption_table").find("input[type=checkbox]:checked").each(function(ind, obj){
        selectAssetOptionsType = selectAssetOptionsType+ $(obj).val()+",";
    });
    selectAssetOptionsType = selectAssetOptionsType.substring(0,selectAssetOptionsType.length-1);
    $("#money_selectAssetType_hidden").val(selectAssetOptionsType);
    vm.selectAssetType = selectAssetOptionsType;
    getApproveProduct(selectAssetOptionsType);
}
/**
 * 资产信息改变设置页面
 */
function changeAssetTypeSetting(assetType,isCheck) {
    switch (assetType) {
        case "POLICY":// 保单信息
            if (isCheck) {
                $("#money_customer_POLICY_div").find("form").each(function(ind,tab){
                    //保险年限
                    $(tab).find("tr:eq(0)").find("th:eq(1)").addClass("requiredFontWeight");
                    $(tab).find("tr:eq(0)").find("td:eq(1)").find("input:first").textbox($(tab).find("tr:eq(0)").find("td:eq(1)").find("input:first").textbox("options").required=true);
                    // 保险金额
                    $(tab).find("tr:eq(1)").find("th:first").addClass("requiredFontWeight");
                    $(tab).find("tr:eq(1)").find("td:first").find("input:first").numberbox($(tab).find("tr:eq(1)").find("td:first").find("input:first").numberbox("options").required=true);
                    // 验证
                    $(tab).find("th:last").addClass("requiredFontWeight");
                });
            } else {
                $("#money_customer_POLICY_div").find("form").each(function(ind,tab){
                    //保险年限
                    $(tab).find("tr:eq(0)").find("th:eq(1)").removeClass("requiredFontWeight");
                    $(tab).find("tr:eq(0)").find("td:eq(1)").find("input:first").textbox( $(tab).find("tr:eq(0)").find("td:eq(1)").find("input:first").textbox("options").required=false);
                    // 保险金额
                    $(tab).find("tr:eq(1)").find("th:first").removeClass("requiredFontWeight");
                    $(tab).find("tr:eq(1)").find("td:first").find("input:first").numberbox($(tab).find("tr:eq(1)").find("td:first").find("input:first").numberbox("options").required=false);
                    // 验证
                    $(tab).find("th:last").removeClass("requiredFontWeight");
                });
            }
            break;
        case "CAR":// 车辆信息
            if (isCheck) {
                $("#money_customer_CAR_div").find("th:last").addClass("requiredFontWeight");
            } else {
                $("#money_customer_CAR_div").find("th:last").removeClass("requiredFontWeight");
            }
            break;
        case "MASTERLOAN":// 淘宝账户信息
            if (isCheck) {
                $("#money_customerMerchant_div").find("tr:last").find("th").addClass("requiredFontWeight");
                var threeOption = $("#money_customerMerchant_div").find("tr:last").find("td:first").find(".easyui-combobox");
                threeOption.combobox($.extend({}, threeOption.combobox("options"), {
                    required: true,
                }));
                var sixOption = $("#money_customerMerchant_div").find("tr:last").find("td:eq(1)").find(".easyui-combobox");
                sixOption.combobox($.extend({},sixOption.combobox("options"),{
                    required: true,
                }));
            }else {
                $("#money_customerMerchant_div").find("tr:last").find("th").removeClass("requiredFontWeight");
                var threeOption = $("#money_customerMerchant_div").find("tr:last").find("td:first").find(".easyui-combobox");
                threeOption.combobox($.extend({}, threeOption.combobox("options"), {
                    required: false,
                }));
                var sixOption = $("#money_customerMerchant_div").find("tr:last").find("td:eq(1)").find(".easyui-combobox");
                sixOption.combobox($.extend({},sixOption.combobox("options"),{
                    required: false,
                }));
            }
            break;
        case "CARDLOAN":// 信用卡信息
            if (isCheck) {
                 $("#money_customerCardLoan_div").find("th").addClass("requiredFontWeight");
                // 额度
                $("#money_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox($("#money_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox("options").required=true);
                // 发卡银行
                $("#money_cardLoanInfo_issuingBank").textbox($("#money_cardLoanInfo_issuingBank").textbox("options").required=true);
                // 发卡时间
                $("#money_cardLoanInfo_issuerDate").datebox($("#money_cardLoanInfo_issuerDate").datebox("options").required=true);
            } else {
                $("#money_customerCardLoan_div").find("th").removeClass("requiredFontWeight");
                // 额度
                $("#money_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox($("#money_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox("options").required=false);
                // 发卡银行
                $("#money_cardLoanInfo_issuingBank").textbox($("#money_cardLoanInfo_issuingBank").textbox("options").required=false);
                // 发卡时间
                $("#money_cardLoanInfo_issuerDate").datebox($("#money_cardLoanInfo_issuerDate").datebox("options").required=false);
            }
            break;
        case "EDUCATION":// 学历信息
            if (isCheck) {
                $("#money_customer_EDUCATION_div").find("th:last").addClass("requiredFontWeight");
            } else {
                $("#money_customer_EDUCATION_div").find("th:last").removeClass("requiredFontWeight");
            }
            break;
    }
}


/**
 * 根据勾选资产信息获取审批产品
 * @param assetTypeList
 */
function getApproveProduct(assetTypeList,type) {
    if (isNotNull(assetTypeList)) {
        post(ctx.rootPath()+"/bmsBasiceInfo/getProductListByOrgIdAndAssetType",{
            owningBranchId:vm.applyBasicInfo.owningBranchId,
            ifPreferentialUser:vm.applyBasicInfo.ifPreferentialUser,
            assetTypeList:assetTypeList
        },"JSON",function(data) {
            if ('INIT' == type) {
                vm.productList = data;
            } else {
                if (isNotNull(data)) {
                    $approvalProductCd.combobox("loadData", data);
                    var productCode = $approvalProductCd.combobox("getValue");
                    if (isNotNull(productCode)) {// 防止产品过期
                        var defVal = $approvalProductCd.combobox("options").finder.getRow($approvalProductCd[0], productCode);
                        if (!isNotNull(defVal)) {
                            $approvalProductCd.combobox("setValue", "");
                        }
                    }
                } else {
                    $approvalProductCd.combobox("loadData", {});
                    $approvalProductCd.combobox("setValue", "");
                }
            }
        });
    } else {
        if ('INIT' != type) {
            $approvalProductCd.combobox("loadData", [{ "name":"证大前前","code":"00025"}]);
            $approvalProductCd.combobox("setValue", "");
        }
    }
}

/**
 * 添加保单贷信息
 */
function addNewPolicyInfo() {
    if ($("#money_customer_POLICY_div").find("form").length < 3) {
        var isChecked =  $("#money_selectAssetOption_table").find("input[value='POLICY']").is(":checked") ? "requiredFontWeight":"";
        var htmlText = $('<form><input type="hidden" name="unabridged" value="Y"><input type="hidden" name="appAdd" value="N"><table class="table_ui W100"><tr><th class="requiredFontWeight">投保公司:</th><td colspan="3"><input class="input" name="insuranceCompany"></td><th class="'+isChecked+'">保险年限:</th><td><input class="input" name="insuranceTerm"></td></tr><tr><th width="14%" class="'+isChecked+'">保险金额:</th><td width="18%"><input name="insuranceAmt" class="input"></td><th width="14%" class="requiredFontWeight">年缴金额:</th><td width="18%"><input class="input" name="yearPaymentAmt"></td><th width="14%" class="requiredFontWeight">已缴年限:</th><td><input class="input" name="paidTerm"></td></tr><tr><th class="requiredFontWeight">查询账号:</th><td><input class="input" name="policyAccount"></td><th class="requiredFontWeight">密码:</th><td><input class="input" name="policyPassword"></td><th class="'+isChecked+'">寿险保单已验证:</th><td><input type="checkbox" name="policyCheckIsVerify" ><span class="float_right"><a href="javaScript:void(0);" onclick="deletePolicyInfo(this)" title="删除"><i class="fa fa-minus" aria-hidden="true"></i></a></span></td></tr></table><hr/></form>');
        // 初始化数据
        // 投保公司
        htmlText.find("input[name='insuranceCompany']").combobox({
            required: true,
            width: 460,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            prompt: '请选择',
            missingMessage: '请选择投保公司',
            data: getEnum('InsuranceCompany')
        });

        //保险年限
        htmlText.find("input[name='insuranceTerm']").textbox({
            required: $("#money_selectAssetOption_table").find("input[value='POLICY']").is(":checked"),
            buttonText: '年',
            width: 210,
            missingMessage: "请填写保险年限!",
            validType: ["insurancePeriod[1, 99999999, '保险年限']"]
        });

        //保险金额
        htmlText.find("input[name='insuranceAmt']").numberbox({
            required: $("#money_selectAssetOption_table").find("input[value='POLICY']").is(":checked"),
            groupSeparator: ',',
            precision: 0,
            width: 210,
            buttonText: '元',
            missingMessage: "请填写保险金额!",
            validType: ["range[0, 99999999, '保险金额']"]
        });

        //年缴金额
        htmlText.find("input[name='yearPaymentAmt']").numberbox({
            required: true,
            groupSeparator: ',',
            precision: 0,
            width: 210,
            buttonText: '元',
            missingMessage: "请填写年缴金额!",
            validType: ["range[1, 99999999, '年缴金额']"],
            onChange:function () {
                countPolicyInfoYearPaymentAmt();
            }
        });

        //已缴年限
        htmlText.find("input[name='paidTerm']").textbox({
            required: true,
            buttonText: '年',
            width: 210,
            missingMessage: "请填写已缴年限!",
            validType: ["range[0, 99999999, '已缴年限']"]
        });

        // 投保账号
        htmlText.find("input[name='policyAccount']").textbox({
            required: true,
            width: 210,
            missingMessage: "请填写投保账号",
            validType: ["accountAndPwd['投保账号']"]
        });

        // 投保账号密码
        htmlText.find("input[name='policyPassword']").textbox({
            required: true,
            width: 210,
            missingMessage: "请填写投保账号密码",
            validType: ["accountAndPwd['投保账号密码']"]
        });

        $($("#money_customer_POLICY_div").find("form:last").after(htmlText));
    } else {
        $.info("提示","保单信息最多只能有3个");
    }
}

/**
 * 删除保单贷信息
 * @param obj
 */
function deletePolicyInfo(obj){
    $.messager.confirm({
        title: '提示',
        msg: '您确认要删除此条保单信息吗？',
        fn: function(r){
            if (r){
                $(obj).parents("form").remove();
                if ( $("#money_customer_POLICY_div").find("form").length ==0) {
                    $("#money_customer_POLICY_div").remove();// 删除保单信息模块
                    var ifSelect = $("#money_selectAssetOption_table").find("input[name='selectAssetOption']").is(':checked');
                    $("#money_selectAssetOption_table").find("input[value='POLICY']").parents("td").remove();// 删除保单信息勾选
                    if (ifSelect) {
                        changeAssetType("POLICY",false);
                    }
                } else {
                    countPolicyInfoYearPaymentAmt();
                }
            }
        }
    });
}
/**
 * 车贷还款情况
 * @param newValue
 */
function moneyCarLoanSelect(newValue) {
    if ("ING" == newValue) {
        // 车贷发放年月
        var carLoanGrantDateoptions = $("#money_customerInfo_carInfo_carLoanGrantDate").datebox("options");
        carLoanGrantDateoptions.required=true;
        carLoanGrantDateoptions.readonly=false;
        carLoanGrantDateoptions.value=isNotNull(vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate) ? moment(vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate).format('YYYY-MM') : '';
        $("#money_customerInfo_carInfo_carLoanGrantDate").datebox(carLoanGrantDateoptions);
        $("#money_customerInfo_carInfo_carLoanGrantDate").parent("td").prev("th").addClass("requiredFontWeight");
        // 月供
        var monthPaymentAmtOption = $("#money_Customer_Info_monthPaymentAmt").numberbox("options");
        monthPaymentAmtOption.required=true;
        monthPaymentAmtOption.readonly=false;
        monthPaymentAmtOption.value=vm.applyInfo.assetsInfo.carInfo.monthPaymentAmt;
        $("#money_Customer_Info_monthPaymentAmt").numberbox(monthPaymentAmtOption);
        $("#money_Customer_Info_monthPaymentAmt").parent("td").prev("th").addClass("requiredFontWeight");
        //车贷发放机构
        var carLoanOrgOptions = $("#money_carLoanOrg").textbox("options");
        carLoanOrgOptions.required=true;
        carLoanOrgOptions.readonly=false;
        carLoanOrgOptions.value = vm.applyInfo.assetsInfo.carInfo.carLoanOrg;
        $("#money_carLoanOrg").textbox(carLoanOrgOptions);
        $("#money_carLoanOrg").parent("td").prev("th").addClass("requiredFontWeight");
    } else {
        // 车贷发放年月
        var carLoanGrantDateoptions = $("#money_customerInfo_carInfo_carLoanGrantDate").datebox("options");
        carLoanGrantDateoptions.required=false;
        carLoanGrantDateoptions.readonly=true;
        carLoanGrantDateoptions.value= '';
        $("#money_customerInfo_carInfo_carLoanGrantDate").datebox(carLoanGrantDateoptions);
        $("#money_customerInfo_carInfo_carLoanGrantDate").parent("td").prev("th").removeClass("requiredFontWeight");
        // 月供
        var monthPaymentAmtOption = $("#money_Customer_Info_monthPaymentAmt").numberbox("options");
        monthPaymentAmtOption.required=false;
        monthPaymentAmtOption.readonly=true;
        monthPaymentAmtOption.value='';
        $("#money_Customer_Info_monthPaymentAmt").numberbox(monthPaymentAmtOption);
        $("#money_Customer_Info_monthPaymentAmt").parent("td").prev("th").removeClass("requiredFontWeight");
        //车贷发放机构
        var carLoanOrgOptions = $("#money_carLoanOrg").textbox("options");
        carLoanOrgOptions.required=false;
        carLoanOrgOptions.readonly=true;
        carLoanOrgOptions.value = "";
        $("#money_carLoanOrg").textbox(carLoanOrgOptions);
        $("#money_carLoanOrg").parent("td").prev("th").removeClass("requiredFontWeight");
    }
}
/**
 * 计算保险年缴总金额
 */
function countPolicyInfoYearPaymentAmt() {
    var countYearPaymonetAnt = 0;
    $("#money_customer_POLICY_div").find("input[name='yearPaymentAmt']").each(function (ind,obj) {
         if (isNotNull($(obj).val())) {
             countYearPaymonetAnt = countYearPaymonetAnt + parseInt($(obj).val());
         }
    });
    $("#money_policyInfo_countYearPaymentAmt").html(countYearPaymonetAnt);

}

/**
 * 当车辆信息或房产信息还款情况由还款中改为其他保存审批意见表需要清空同理如果不是需要保存值
 * 车辆信息 需要清空或保存 车贷发放年月、月供、发放机构
 * 房产信息 需要清空或保存 房贷总额、房贷发放年月、房贷月供
 */
function cleanCarInfoEstateInfo(obj) {
    // 判断车辆信息是否填写完整
    if ("Y" == vm.applyInfo.assetsInfo.carInfo.unabridged) {
        if ("ING" != obj.carInfo.carLoanStatus) {
            vm.applyInfo.assetsInfo.carInfo.carLoanOrg=null;
            vm.applyInfo.assetsInfo.carInfo.monthPaymentAmt=null;
            vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate=null;
        } else {
            vm.applyInfo.assetsInfo.carInfo.carLoanOrg=obj.carInfo.carLoanOrg;
            vm.applyInfo.assetsInfo.carInfo.monthPaymentAmt=obj.carInfo.monthPaymentAmt;
            vm.applyInfo.assetsInfo.carInfo.carLoanIssueDate=obj.carInfo.carLoanIssueDate;
        }
    }
    // 判断房产信息是否填写完成
    if ("Y" == vm.applyInfo.assetsInfo.estateInfo.unabridged) {
        if ("ING" != obj.estateInfo.estateLoan) {
            vm.applyInfo.assetsInfo.estateInfo.estateLoanAmt=null;
            vm.applyInfo.assetsInfo.estateInfo.monthPaymentAmt=null;
            vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate=null;
        } else {
            vm.applyInfo.assetsInfo.estateInfo.estateLoanAmt=obj.estateInfo.estateLoanAmt;
            vm.applyInfo.assetsInfo.estateInfo.monthPaymentAmt=obj.estateInfo.monthPaymentAmt;
            vm.applyInfo.assetsInfo.estateInfo.estateLoanIssueDate=obj.estateInfo.estateLoanIssueDate;
        }
    }
}

/**
 * 验证资产信息是否合法，并且序列化
 */
function validateAssetsInfo(saveAssetObj) {
    var validatePolicyInfo = true;
    // 验证保单贷信息
    if (isNotNull(vm.applyInfo.assetsInfo.policyInfo)) {
        var policyInfoSaveList = new Array();
        $("#money_customer_POLICY_div").find("form").each(function(ind, obj){
            if (!$(obj).form("validate")) {
                validatePolicyInfo = false;
                return false;
            }
            if (vm.getIfSelectAssetType("POLICY") && !$(obj).find("input[name='policyCheckIsVerify']").is(":checked")){
                validatePolicyInfo = false;
                $(obj).find("input[name='policyCheckIsVerify']").focus();
                $.info("提示", "第"+(ind +1)+"保单信息的寿险保单已验证未填写!", "warning", 1000);
                return false;
            }
            var oneObj = $(obj).serializeObject();
            oneObj.policyCheckIsVerify = $(obj).find("input[name='policyCheckIsVerify']").is(":checked")?"Y":"N";
            if (isNotNull(oneObj.insuranceTerm) && "终身"==oneObj.insuranceTerm) {
                oneObj.insuranceTerm = 999;
            }
            policyInfoSaveList.push(oneObj);// 保存资产信息
        });
        saveAssetObj.policyInfo = policyInfoSaveList;
    }
    if (!validatePolicyInfo) {
        return false;
    }

    // 验证车辆信息
    if ("Y" == vm.applyInfo.assetsInfo.carInfo.unabridged) {
        if (!$("#money_customer_CAR_div").find("form").form("validate")) {
            return false
        }
        if (vm.getIfSelectAssetType("CAR") && !$("#money_customer_CAR_div").find("input[name='carCheckIsVerify']").is(":checked")) {
            $("#money_customer_CAR_div").find("input[name='carCheckIsVerify']").focus();
            $.info("提示", "车辆信息的车险保单已验证未填写!", "warning", 1000);
            return false;
        }
        saveAssetObj.carInfo= $("#money_customer_CAR_div").find("form").serializeObject();//保存车辆信息
        saveAssetObj.carInfo.carCheckIsVerify =$("#money_customer_CAR_div").find("input[name='carCheckIsVerify']").is(":checked")?"Y":"N";
        saveAssetObj.carInfo.carBuyDate = saveAssetObj.carInfo.carBuyDate + "-01";
        if (isNotNull(saveAssetObj.carInfo.carLoanIssueDate)) {
            saveAssetObj.carInfo.carLoanIssueDate = saveAssetObj.carInfo.carLoanIssueDate + "-01";
        }
    }
    // 公积金信息
    if ('Y' ==  vm.applyInfo.assetsInfo.fundInfo.unabridged) {
        if (!$("#money_customer_PROVIDENT_div").find("form").form("validate")) {
            return false;
        }
        saveAssetObj.fundInfo= $("#money_customer_PROVIDENT_div").find("form").serializeObject();//保存公积金信息
        saveAssetObj.fundInfo.accumulationFundAuthenticated = $("#money_fundInfo_checkbox").is(":checked")?'Y':'N';
    } else {
        var obj = new Object();
        obj.accumulationFundAuthenticated = $("#money_fundInfo_checkbox").is(":checked")?'Y':'N';
        saveAssetObj.fundInfo =obj;
    }
    // 社保信息
    if('Y' ==  vm.applyInfo.assetsInfo.socialInsuranceInfo.unabridged) {
        if (!$("#money_customer_SOCIAL_INSURANCE_div").find("form").form("validate")) {
            return false;
        }
        saveAssetObj.socialInsuranceInfo= $("#money_customer_SOCIAL_INSURANCE_div").find("form").serializeObject();//保存社保信息
        saveAssetObj.socialInsuranceInfo.socialInsuranceAuthenticated = $("#money_socialInsuranceInfo_checkbox").is(":checked")?'Y':'N';
    } else {
        var obj = new Object();
        obj.socialInsuranceAuthenticated = $("#money_socialInsuranceInfo_checkbox").is(":checked")?'Y':'N';
        saveAssetObj.socialInsuranceInfo =obj;
    }
    // 验证信用卡信息
    if ('Y' ==  vm.applyInfo.assetsInfo.cardLoanInfo.unabridged) {
        if (!$("#money_customerCardLoan_div").find("form").form("validate")) {
            return false;
        }
        saveAssetObj.cardLoanInfo= $("#money_customerCardLoan_div").find("form").serializeObject();//保存信用卡信息
        if (isNotNull( saveAssetObj.cardLoanInfo.issuerDate)) {
            saveAssetObj.cardLoanInfo.issuerDate =  saveAssetObj.cardLoanInfo.issuerDate + "-01";
        }
    }
    // 验证房产信息
    if ('Y' ==  vm.applyInfo.assetsInfo.estateInfo.unabridged) {
        if (!$("#money_customerHome_div").find("form").form("validate")) {
            return false;
        }
        saveAssetObj.estateInfo= $("#money_customerHome_div").find("form").serializeObject();//保存房产信息
        if (isNotNull(saveAssetObj.estateInfo.estateBuyDate)) {
            saveAssetObj.estateInfo.estateBuyDate = saveAssetObj.estateInfo.estateBuyDate + "-01";
        }
        if (isNotNull(saveAssetObj.estateInfo.estateLoanIssueDate)) {
            saveAssetObj.estateInfo.estateLoanIssueDate = saveAssetObj.estateInfo.estateLoanIssueDate + "-01";
        }
    }
    // 验证淘宝账号信息
    if ('Y' ==  vm.applyInfo.assetsInfo.masterLoanInfo.unabridged) {
        if (!$("#money_customerMerchant_div").find("form").form("validate")) {
            return false;
        }
        saveAssetObj.masterLoanInfo= $("#money_customerMerchant_div").find("form").serializeObject();//保存淘宝账号信息
    }
    // 学历信息
    if ('Y' ==  vm.applyInfo.assetsInfo.educationInfo.unabridged) {
        if (!$("#money_customer_EDUCATION_div").find("form").form("validate")) {
            return false;
        }
        if (vm.getIfSelectAssetType("EDUCATION")&& !$("#money_customer_EDUCATION_div").find("input[name='chsiAuthenticated']").is(":checked")) {
            $("#money_customer_EDUCATION_div").find("input[name='chsiAuthenticated']").focus();
            $.info("提示", "学历信息已验证未填写!", "warning", 1000);
            return false;
        }
        saveAssetObj.educationInfo= $("#money_customer_EDUCATION_div").find("form").serializeObject();//保存学历信息
        saveAssetObj.educationInfo.chsiAuthenticated =$("#money_customer_EDUCATION_div").find("input[name='chsiAuthenticated']").is(":checked")?'Y':'N';
        saveAssetObj.educationInfo.graduationDate = saveAssetObj.educationInfo.graduationDate + "-01";
        if ("00002" == saveAssetObj.educationInfo.educationExperience) {// 如果教育经历是国（境）外学历情况地区信息
            saveAssetObj.educationInfo.areaProvinceId ='';
            saveAssetObj.educationInfo.areaProvince ='';
            saveAssetObj.educationInfo.areaCityId ='';
            saveAssetObj.educationInfo.areaCity ='';
        }
    }
    return true;
}