var firstReformSearchOrNot = false;
var firstReformDataList = null;
$(function () {
    // 初始化分派状态
    $("#firstReform_fpStatus").combobox({
        prompt: '分派状态',
        panelHeight: 'auto',
        width: 190,
        editable: false,
        onChange: function (newValue, oldValue) {
            changeFpStatus(newValue);
            firstReformQuery();
        },
    });

    // 初始化datagrid
    initFirstReformDatagrid();

    $("#firstReform_query_form").mouseover(function () {
        firstReformSearchOrNot = true;
    });
    $("#firstReform_query_form").mouseout(function () {
        firstReformSearchOrNot = false;
    });
    $(document).keyup(function (evnet) {
        if (evnet.keyCode == '13' && firstReformSearchOrNot) {
            firstReformQuery();
        }
    });
});
/**
 * 初始化datagrid
 */
function initFirstReformDatagrid() {
    var fpStatus = $("#firstReform_fpStatus").combobox("getValue");
    changeFpStatus(fpStatus);
    $("#firstReform_task_datagrid").datagrid({
        url: ctx.rootPath() + '/firstReform/loanNoListPage',
        toolbar: '#firstReform_task_datagrid_tool',
        striped: true,
        rownumbers: true,
        idField: 'loanNo',
        pagination: true,
        fitColumns: false,
        scrollbarSize: 0,
        remoteSort: true,
        queryParams: {
            fpStatus: fpStatus
        },
        onDblClickRow: function (index, data) {
            handle_firstApproveLook(index);
        },
        onLoadSuccess: function (data) {
            firstReformDataList = data;
        },
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'ifPri',
            title: '案件标识',
            width: 140,
            formatter: formatLoanMark,
        }, {
            field: 'loanNo',
            title: '借款编号',
            width: 190,
            sortable: true,
        }, {
            field: 'xsSubDate',
            title: '提交时间',
            width: 120,
            sortable: true,
            formatter: ctx.getDate,
        }, {
            field: 'customerName',
            title: '申请人姓名',
            width: 140,
            sortable: true,
            formatter: function (value, row, index) {
                if (getOldCardIdExists(row.customerIDNO)) {
                    value = "*" + value;
                }
                return value;
            }
        }, {
            field: 'customerIDNO',
            title: '身份证号码',
            width: 140,
            sortable: true,
            formatter: function (value, row, index) {
                return "*" + value.substring(value.length - 4, value.length);
            }
        }, {
            field: 'initProductName',
            title: '申请产品',
            width: 120,
            sortable: true,
            formatter:formatterProduct,
        }, {
            field: 'owningBranchName',
            title: '营业部',
            width: 120,
            sortable: true,
        }, {
            field: 'owningBranchAttr',
            title: '营业部属性',
            width: 190,
            sortable: true,
        }, {
            field: 'checkPerson',
            title: '处理人',
            width: 130,
            sortable: true,
            formatter: function (value, row, index) {
                if (isNotNull(row.rtfNodeStatus) && ('XSCS-ASSIGN' != row.rtfNodeStatus && 'XSCS-HANGUP' != row.rtfNodeStatus)) {
                    value = '';
                }
                return value;
            }
        }, {
            field: 'cSProxyGroupName',
            title: '管理组',
            width: 190,
            sortable: true,
            formatter: function (value, row, index) {
                if (isNotNull(row.rtfNodeStatus) && ('XSCS-ASSIGN' != row.rtfNodeStatus && 'XSCS-HANGUP' != row.rtfNodeStatus)) {
                    value = '';
                }
                return value;
            }
        }, {
            field: 'lastModifiedBy',
            title: '所在队列',
            width: 110,
            formatter: formatInTask
        }, {
            field: 'action',
            title: '操作',
            width: 60,
            align: 'center',
            formatter: function (value, data, index) {
                return '<a href="javaScript:void(0);" onclick=handle_firstApproveLook("' + index + '")>查看详情</a>';
            }
        }]]
    });
}
/**
 * 修改分派状态控制权限显示
 */
function changeFpStatus(value) {
    if ("1" == value) {// 已分派
        $("#firstReform_notAssigned_span").addClass("display_noH");
        $("#firstReform_assigned_span").removeClass("display_noH");
    } else {// 未分派
        $("#firstReform_notAssigned_span").removeClass("display_noH");
        $("#firstReform_assigned_span").addClass("display_noH");
    }
}

/**
 * 批量改派dialog
 *
 * @author dmz
 * @date 2017年4月12日
 */
function firstReformReassignmentDialog() {
    var rows = $("#firstReform_task_datagrid").datagrid("getSelections");	// 获取需要改派的的申请件
    if (isNotNull(rows) && rows.length > 0) {
        // 验证被改派的借款单是否有综合信用评级
        var ifComCreditRating = null;
        $.each(rows, function (ind, row) {
            if (!isNotNull(row.comCreditRating)) {
                ifComCreditRating = row.loanNo;
                return false;
            }
        });
        if (!isNotNull(ifComCreditRating)) {
            post(ctx.rootPath() + "/firstReform/findGroupInfoByAccount", null, "json", function (result) {
                if (result.success) {
                    var $assignDialog = $("#firstReform_reassignment_dialog");	// 获取初审改派对话框
                    initOperator();							// 初始化处理人信息
                    initSmallGroup(null);	// 初始化小组信息
                    initBigGroup(null);	// 初始化大组信息

                    $assignDialog.removeClass("display_none").dialog({
                        title: "初审改派",
                        modal: true,
                        width: 400,
                        height: 300,
                        buttons: [{
                            text: '保存',
                            iconCls: 'fa fa-check',
                            handler: function () {
                                firstReformUpdate();
                            }
                        }, {
                            text: '返回',
                            iconCls: 'fa fa-reply',
                            handler: function () {
                                $assignDialog.dialog("close");
                            }
                        }],
                    });

                } else {
                    $.info("提示", result.firstMessage);
                }
            });
        } else {
            $.info("提示", "借款[" + ifComCreditRating + "]没有综合信用评级");
        }
    } else {
        $.info("提示", "请选择要操作的数据");
    }
}

/**
 * 批量改派提交
 *
 * @author dmz
 * @date 2017年4月8日
 */
var firstReformMarkSub = true; // 标记是否可以提交防止重复提交
function firstReformUpdate() {
    if (firstReformMarkSub) {
        firstReformMarkSub = false;
        var $operator = $("#firstReform_operator");
        var targetUserCode = $operator.combobox("getValue");
        var selectUser = $operator.combobox("options").finder.getRow($operator[0], targetUserCode);
        if (isNotNull(selectUser)) {
            var targetUserName = $operator.combobox("getText");
            if ($("#firstReform_reassignment_dialog").find("form").form("validate")) {
                var rows = $("#firstReform_task_datagrid").datagrid("getSelections");
                if (isNotNull(rows) && rows.length > 0) {
                    var list = [];
                    var markUser = true;
                    $.each(rows, function (ind, val) {
                        if (val.checkPersonCode == targetUserCode && isNotNull(val.rtfNodeStatus) && (val.rtfNodeStatus == 'XSCS-ASSIGN' || val.rtfNodeStatus == 'XSCS-HANGUP')) {
                            markUser = false;
                            return markUser;
                        }
                        var obj = new Object();
                        obj.loanNo = val.loanNo;
                        obj.version = val.version;
                        obj.rtfNodeState = val.rtfNodeStatus;
                        obj.ifNewLoanNo = val.ifNewLoanNo;
                        // 已分配或者挂起需要获取原初审员
                        if (val.rtfNodeStatus == 'XSCS-ASSIGN' || val.rtfNodeStatus == "XSCS-HANGUP") {
                            obj.userCode = val.checkPersonCode;
                        }
                        obj.targetUserCode = targetUserCode;
                        obj.targetuserName = targetUserName;
                        obj.personId = val.personId;
                        list.push(obj);
                    });
                    if (markUser) {
                        $.ajax({
                            url: ctx.rootPath() + "/firstReform/updateReform",
                            dataType: "json",
                            method: 'post',
                            contentType: 'application/json',
                            data: JSON.stringify(list),
                            success: function (data) {
                                firstReformMarkSub = true;
                                if (data.success) {
                                    $.info("提示", data.firstMessage, "info", 3000)
                                    $("#firstReform_reassignment_dialog").dialog("close");
                                    $("#firstReform_task_datagrid").datagrid("clearSelections");
                                    $("#firstReform_task_datagrid").datagrid("reload");
                                } else {
                                    $.info("提示", data.firstMessage, "error");
                                }
                            },
                            error: function (data) {
                                firstReformMarkSub = true;
                                $.info("警告", data.responseText, "warning");
                            }
                        });
                    } else {
                        firstReformMarkSub = true;
                        $.info("提示", "自己不能改派给自己");
                    }
                } else {
                    firstReformMarkSub = true;
                    $.info("提示", "请选择要改派的订单!");
                }
            }
        } else {
            firstReformMarkSub = true;
            $operator.combobox("setValue", "");
        }
    }
}

/**
 * 批量退回dialog
 *
 * @author dmz
 * @date 2017年4月12日
 * @param loanNo
 */
function firstReformBackDialog() {
    var rows = $("#firstReform_task_datagrid").datagrid("getSelections");
    if (isNotNull(rows) && rows.length > 0) {
        var list = [];
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
            obj.loanNo = val.loanNo;
            obj.version = val.version;
            obj.rtfNodeState = val.rtfNodeStatus;
            list.push(obj);
        });
        // 判断是否选择相同的案件(前前或普通件)
        if (!isEqualAll) {
            $.info("提示", "请选择相同的进件!");
            return false;
        }
        if (1 == moneyOrCommonPiece) {
          $("#firstReform_return_dialog").find(".markIsAdd").html('<a href="javaScript:void(0);" onclick=firstAddOrDeleteRetureReason("ADD")><i class="fa fa-plus" aria-hidden="true"></i></a>');
          $("#firstReform_secondReturn_combobox").combobox({multiple:true,separator:'|'});
        } else {
            $("#firstReform_return_dialog").find(".markIsAdd").html("&nbsp;&nbsp;");
            $("#firstReform_secondReturn_combobox").combobox({multiple:false});
        }
        // 初始化退回原因
        initClassAReason("#firstReform_firstReturn_combobox",1== moneyOrCommonPiece?"CSRTNQQ" :"XSCS", "return", "#firstReform_secondReturn_combobox",260);
        // end at 2017-05-05
        $("#firstReform_return_dialog").removeClass("display_none").dialog({
            title: "退回审批信息",
            modal: true,
            width: 820,
            height: 320,
            buttons: [{
                text: '提交',
                iconCls: 'fa fa-arrow-up',
                handler: function () {
                    if ($("#firstReform_return_dialog").find("form").form("validate")) {
                        var objForm = $("#firstReform_return_dialog").find("form").serializeObject();
                        objForm.reformVOList = list;
                        if (1 == moneyOrCommonPiece) {
                            objForm = firstCreateMoneyReturnReason(objForm);
                        }
                        post(ctx.rootPath() + "/firstReform/updateReformReturn/",{form:JSON.stringify(objForm)},"json",function (data) {
                            if (data.success) {
                                $.info("提示", data.firstMessage);
                                $("#firstReform_return_dialog").dialog("close");
                                $("#firstReform_task_datagrid").datagrid("clearSelections");
                                $("#firstReform_task_datagrid").datagrid("reload");
                            } else {
                                $.info("警告", data.firstMessage, "warning");
                            }
                        });
                    }
                }
            }, {
                text: '取消',
                iconCls: 'fa fa-reply',
                handler: function () {
                    $("#firstReform_return_dialog").dialog("close");
                }
            }],
            onClose: function () {
                $("#firstReform_return_dialog").find("tr.markReturnReason:gt(0)").remove();
                $("#firstReform_return_dialog").find("form").form("reset");
            }
        });
    } else {
        $.info("提示", "请选择要退回的订单!");
    }
}

/**
 * 批量拒绝dialog
 *
 * @author dmz
 * @date 2017年4月12日
 * @param loanNo
 */
function firstReformRefuseDialog() {
    var rows = $("#firstReform_task_datagrid").datagrid("getSelections");
    if (isNotNull(rows) && rows.length > 0) {
        var list = [];
        $.each(rows, function (ind, val) {
            var obj = new Object();
            obj.loanNo = val.loanNo;
            obj.version = val.version;
            obj.rtfNodeState = val.rtfNodeStatus;
            list.push(obj);
        });
        // 初始化拒绝原因
        initClassAReason("#firstReform_firstRefuse_combobox", "XSCS", "reject", "#firstReform_secondRefuse_combobox");
        // end at 2017-05-05
        $("#firstReform_refuse_dialog").removeClass("display_none").dialog({
            title: "拒绝审批信息",
            modal: true,
            width: 950,
            height: 220,
            buttons: [{
                text: '提交',
                iconCls: 'fa fa-arrow-up',
                handler: function () {
                    if ($("#firstReform_refuse_dialog").find("form").form("validate")) {
                        var objForm = $("#firstReform_refuse_dialog").find("form").serializeObject();
                        objForm.reformVOList = list;
                        $.ajax({
                            url: ctx.rootPath() + "/firstReform/updateReformReject",
                            dataType: "json",
                            method: 'post',
                            contentType: 'application/json',
                            data: JSON.stringify(objForm),
                            success: function (data) {
                                if (data.success) {
                                    $.info("提示", data.firstMessage);
                                    $("#firstReform_refuse_dialog").dialog("close");
                                    $("#firstReform_task_datagrid").datagrid("clearSelections");
                                    $("#firstReform_task_datagrid").datagrid("reload");
                                } else {
                                    $.info("警告", data.firstMessage, "warning");
                                }
                            },
                            error: function (data) {
                                $.info("警告", data.responseText, "warning");
                            }
                        });
                    }
                }
            }, {
                text: '取消',
                iconCls: 'fa fa-reply',
                handler: function () {
                    $("#firstReform_refuse_dialog").dialog("close");
                }
            }],
            onClose: function () {
                $("#firstReform_refuse_dialog").find("form").form("reset");
            }
        });
    } else {
        $.info("提示", "请选择要拒绝的订单!");
    }
}
/**
 * 根据查询条件导出excel
 */
function exportExcel() {
    var queryParams = $("#firstReform_query_form").serializeObject();
    queryParams = getQueryParams(queryParams);
    window.open(ctx.rootPath() + "/firstApprove/exportExcel/" + JSON.stringify(queryParams));
}

/**
 * 查询
 */
function firstReformQuery() {
    if ($("#firstReform_query_form").form("validate")) {
        var queryParams = $("#firstReform_query_form").serializeObject();
        queryParams = getQueryParams(queryParams);
        $("#firstReform_task_datagrid").datagrid("clearSelections");
        $("#firstReform_task_datagrid").datagrid('load', queryParams);
    }
}

function getQueryParams(queryParams) {
    // 转换案件标识
    var ct = queryParams.caseType;
    if (ct != null && ct instanceof Array && ct.length > 1) {
        var s = ct.join(",");
        queryParams.caseType = s;
    }
    // 转换处理人
    var hc = queryParams.handleCode;
    if (hc != null && hc instanceof Array && hc.length > 1) {
        var hangdels = hc.join(",");
        queryParams.handleCode = hangdels;
    }
    // 转换申请产品
    var pc = queryParams.productCd;
    if (pc != null && pc instanceof Array && pc.length > 1) {
        var s = pc.join(",");
        queryParams.productCd = s;
    }
    // 营业部转换
    var owningBranch = queryParams.owningBranchId;
    if (owningBranch != null && owningBranch instanceof Array && owningBranch.length > 1) {
        var s = owningBranch.join(",");
        queryParams.owningBranchId = s;
    }
    return queryParams;
}

/**
 * 初始化大组下拉框
 */
function initBigGroup(value) {
    var $bigGroup = $("#firstReform_bigGroup");
    $.ajax({
        url: ctx.rootPath() + "/firstReform/findBigGroupByAccountAndAbility",
        data: {
            taskDef: 'apply-check',
            reformVoStr: JSON.stringify(getReformList())
        },
        dataType: 'json',
        method: 'post',
        async: false,
        success: function (result) {
            $bigGroup.combobox({
                valueField: 'id',
                textField: 'name',
                editable: false,
                readonly: isNotNull(value),
                value: value,
                data: result.dataList,
                onChange: function (newValue, oldValue) {
                    $("#firstReform_smallGroup").combobox("setValue", "");	// 清空小组下拉框选项
                    $("#firstReform_operator").combobox("setValue", "");	// 清空处理人下拉框选项
                    reloadSmallGroup(newValue);// 重新加载小组下拉框选项
                },
                onLoadSuccess: function () {
                    var defaultVal = $bigGroup.combobox("getValue");
                    if (isNotNull(defaultVal)) {//回填的时候如果没有可选择的组时需要清空
                        var findValue = $bigGroup.combobox("options").finder.getRow($bigGroup[0], defaultVal);
                        if (!isNotNull(findValue)) {
                            defaultVal = "";
                            $bigGroup.combobox("setValue", "");
                        }
                    }
                    reloadSmallGroup(defaultVal);//(大组有可能为空) 重新加载小组下拉框选项
                }
            });
        }
    });
}

/**
 * 根据大组ID，初始化小组下拉框
 */
function initSmallGroup(defaultValue) {
    var $bigGroup = $("#firstReform_bigGroup");
    var $smallGroup = $("#firstReform_smallGroup");
    var $operator = $("#firstReform_operator");
    $smallGroup.combobox({
        valueField: 'id',
        textField: 'name',
        editable: false,
        readonly: isNotNull(defaultValue),
        value: defaultValue || '',
        onChange: function (newValue, oldValue) {
            if (isNotNull(newValue)) {
                //var checkPersonCodes = getCheckPersonCodes();	// 获取选中要改派的申请件的处理人
                $operator.combobox("setValue", "");				// 清空处理人下拉框
                // 重新加载处理人下拉框
                $.ajax({
                    url: ctx.rootPath() + "/pmsApi/getUserInfoByLikeUserName",
                    data: {
                        orgId: newValue,
                        //checkPersonCodes: checkPersonCodes,
                        fpStatus:$("#firstReform_fpStatus").combobox("getValue"),
                        roleCode: 'check',
                        reformVoStr: JSON.stringify(getReformList())
                    },
                    dataType: 'json',
                    method: 'post',
                    async: false,
                    success: function (result) {
                        $operator.combobox("loadData", result);
                    }
                });
            }
        },
        onLoadSuccess: function () {
            var smallGroupId = $smallGroup.combobox("getValue");
            if (isNotNull(smallGroupId)) {	// 回填的时候，如果没有可选择的组时，需要清空
                var findValue = $smallGroup.combobox("options").finder.getRow($smallGroup[0], smallGroupId);
                if (!isNotNull(findValue)) {
                    smallGroupId = "";
                    $smallGroup.combobox("setValue", "");
                }
            }

            if (!isNotNull(smallGroupId)) {
                smallGroupId = $bigGroup.combobox("getValue");
            }

            //var checkPersonCodes = getCheckPersonCodes();// 获取选中要改派的申请件的处理人
            $operator.combobox("setValue", "");				// 清空处理人下拉框
            // 重新加载处理人下拉框
            $.ajax({
                url: ctx.rootPath() + "/pmsApi/getUserInfoByLikeUserName",
                data: {
                    orgId: smallGroupId,
                    //checkPersonCodes: checkPersonCodes,
                    fpStatus:$("#firstReform_fpStatus").combobox("getValue"),
                    roleCode: 'check',
                    reformVoStr: JSON.stringify(getReformList())
                },
                dataType: 'json',
                method: 'post',
                async: false,
                success: function (result) {
                    $operator.combobox("loadData", result);
                }
            });
        }
    });
}
/**
 * 过滤小组(orgId有可能为空)
 * @param orgId
 */
function reloadSmallGroup(orgId) {
    $.ajax({
        url: ctx.rootPath() + "/firstReform/findTeamByAccountAndOrgIdAndAbility",
        data: {
            orgId: orgId,
            taskDef: 'apply-check',
            reformVoStr: JSON.stringify(getReformList())
        },
        dataType: 'json',
        method: 'post',
        async: false,
        success: function (result) {
            if (result.type == 'SUCCESS') {
                $("#firstReform_smallGroup").combobox("loadData", result.dataList);
            }
        }
    });
}

/**
 * 初始化处理人下拉框(根据小组id获取员工)
 */
function initOperator() {
    var $operator = $("#firstReform_operator");
    $operator.combobox({
        valueField: 'usercode',
        textField: 'name',
        /*  editable : false,*/
        required: true,
        filter: filterCombo,
        onLoadSuccess: function () {
            var date = $operator.combobox("getData");
            if (!isNotNull(date) || date.length < 1) {
                $.info("提示", "没有可分配的员工", "info", 1000);
            }
        }
    });
}

/**
 * 获取选中的申请件
 * @author wulj
 */
function getReformList(userCode) {
    var selectedRows = $("#firstReform_task_datagrid").datagrid("getSelections");
    var reformList = [];
    $.each(selectedRows, function (index, element) {
        // 如果当前申请件未分派，则清空申请件初审员工号
        var rtfNodeStatus = element.rtfNodeStatus;
        var checkPersonCode = element.checkPersonCode;
        if (isNotNull(checkPersonCode) && isNotNull(rtfNodeStatus) && ('XSCS-ASSIGN' != rtfNodeStatus && 'XSCS-HANGUP' != rtfNodeStatus)) {
            checkPersonCode = '';
        }

        reformList.push({
            loanNo: element.loanNo,
            specialOrg: element.specialOrg,
            applyType: element.applyType,
            productCd: element.initProductCd,
            comCreditRating: element.comCreditRating,
            userCode: checkPersonCode,
            targetUserCode: userCode || null
        });
    });

    return reformList;
}

//批量拒绝时一级原因选择其他备注为必填
$('#firstReform_firstRefuse_combobox').combobox({
    onSelect: function (record) {
        if (isNotNull(record.code) && 'RJ9999' == record.code) {//一级原因是其他时备注必填
            $('#firstReform_refuse_remark').textbox({
                required: true
            });
        } else {
            $('#firstReform_refuse_remark').textbox({
                required: false
            });
        }
    }
});

/*=================================================================办理页面改派=========================================================================================*/

/**
 * 打开办理页面(带改派按钮)
 *
 * @param loanNo
 */
function handle_firstApproveLook(index) {
    var row = firstReformDataList.rows[index];
    localStorage.setItem(row.loanNo + "-firstReformData", JSON.stringify(row));
    var handleFirstApproveHTMLWindowWithoutJurisdiction = jDialog.open({
        url: ctx.rootPath() + "/search/handle/" + row.loanNo + "/1",
        onDestroy: function () {
            localStorage.removeItem(row.loanNo + "-firstReformData");
            $("#firstReform_task_datagrid").datagrid('reload');
            $("#firstReform_reassignment_dialog").dialog("close");
        }
    });
}

/**
 * 办理页面改派--弹出处理人dialog
 *
 * @returns
 */
function handle_firstReformDialog(loanNo) {
    var row = null;
    var cacheData = localStorage.getItem(loanNo + "-firstReformData");
    if (isEmptyObj(cacheData)) {
        post(ctx.rootPath() + "/firstReform/handle/reformInfo/" + loanNo, null, "json", function (data) {
            if (isNotNull(data)) {
                row = data;
                localStorage.setItem(row.loanNo + "-firstReformData", JSON.stringify(row));
            } else {
                $.info("警告", "没有查到此数据", "warning");
                return;
            }
        });
    } else {
        row = JSON.parse(localStorage.getItem(loanNo + "-firstReformData"))
    }
    if (!isNotNull(row.comCreditRating)) {
        $.info("提示", "没有综合信用评级");
        return false;
    }
    post(ctx.rootPath() + "/firstReform/findGroupInfoByAccount", null, "json", function (result) {
        if (result.success) {
            var $assignDialog = $("#handle_firstReform_dialog");	// 获取初审改派对话框
            handle_initOperator(row);							// 初始化处理人信息
            handle_initSmallGroup(row, null);	// 初始化小组信息
            handle_initBigGroup(row, null);	// 初始化大组信息

            $assignDialog.removeClass("display_none").dialog({
                title: "初审改派",
                modal: true,
                width: 400,
                height: 300,
                buttons: [{
                    text: '保存',
                    iconCls: 'fa fa-check',
                    handler: function () {
                        handle_firstReformUpdate(row);
                    }
                }, {
                    text: '返回',
                    iconCls: 'fa fa-reply',
                    handler: function () {
                        $assignDialog.dialog("close");
                    }
                }],
            });

        } else {
            $.info("提示", result.firstMessage);
        }
    });
}

/**
 * 初始化处理人信息
 *
 * @returns
 */
function handle_initOperator(row) {
    var $operator = $("#handle_firstReform_operator");
    $operator.combobox({
        valueField: 'usercode',
        textField: 'name',
        /*  editable : false,*/
        required: true,
        filter: filterCombo,
        onLoadSuccess: function () {
            var date = $operator.combobox("getData");
            if (!isNotNull(date) || date.length < 1) {
                $.info("提示", "没有可分配的员工", "info", 1000);
            }
        }
    });
}
/**
 * 获取选中的申请件
 * @author wulj
 */
function handle_getReformList(row, userCode) {
    var reformList = [];
    // 如果当前申请件未分派，则清空申请件初审员工号
    var rtfNodeStatus = row.rtfNodeStatus;
    var checkPersonCode = row.checkPersonCode;
    if (isNotNull(checkPersonCode) && isNotNull(rtfNodeStatus) && ('XSCS-ASSIGN' != rtfNodeStatus && 'XSCS-HANGUP' != rtfNodeStatus)) {
        checkPersonCode = '';
    }

    reformList.push({
        loanNo: row.loanNo,
        specialOrg: row.specialOrg,
        applyType: row.applyType,
        productCd: row.initProductCd,
        comCreditRating: row.comCreditRating,
        userCode: checkPersonCode,
        targetUserCode: userCode || null
    });

    return reformList;
}

/**
 * 根据大组ID，初始化小组下拉框
 */
function handle_initSmallGroup(row, defaultValue) {
    var $bigGroup = $("#handle_firstReform_bigGroup");
    var $smallGroup = $("#handle_firstReform_smallGroup");
    var $operator = $("#handle_firstReform_operator");
    $smallGroup.combobox({
        valueField: 'id',
        textField: 'name',
        editable: false,
        readonly: isNotNull(defaultValue),
        value: defaultValue || '',
        onChange: function (newValue, oldValue) {
            if (isNotNull(newValue)) {
                //var checkPersonCodes = getCheckPersonCodes();	// 获取选中要改派的申请件的处理人
                $operator.combobox("setValue", "");   // 清空处理人下拉框
                var fpStatus = 2;// 未分派
                if ('XSCS-ASSIGN'==row.rtfNodeStatus || 'XSCS-HANGUP'== row.rtfNodeStatus) {
                    fpStatus = 1;// 已分派
                }
                // 重新加载处理人下拉框
                $.ajax({
                    url: ctx.rootPath() + "/pmsApi/getUserInfoByLikeUserName",
                    data: {
                        orgId: newValue,
                        //checkPersonCodes: checkPersonCodes,
                        fpStatus:fpStatus,
                        roleCode: 'check',
                        reformVoStr: JSON.stringify(handle_getReformList(row))
                    },
                    dataType: 'json',
                    method: 'post',
                    async: false,
                    success: function (result) {
                        $operator.combobox("loadData", result);
                    }
                });
            }
        },
        onLoadSuccess: function () {
            var smallGroupId = $smallGroup.combobox("getValue");
            if (isNotNull(smallGroupId)) {	// 回填的时候，如果没有可选择的组时，需要清空
                var findValue = $smallGroup.combobox("options").finder.getRow($smallGroup[0], smallGroupId);
                if (!isNotNull(findValue)) {
                    smallGroupId = "";
                    $smallGroup.combobox("setValue", "");
                }
            }

            if (!isNotNull(smallGroupId)) {
                smallGroupId = $bigGroup.combobox("getValue");
            }

            //var checkPersonCodes = getCheckPersonCodes();// 获取选中要改派的申请件的处理人
            $operator.combobox("setValue", "");				// 清空处理人下拉框
            var fpStatus = 2;// 未分派
            if ('XSCS-ASSIGN'==row.rtfNodeStatus || 'XSCS-HANGUP'== row.rtfNodeStatus) {
                fpStatus = 1;// 已分派
            }
            // 重新加载处理人下拉框
            $.ajax({
                url: ctx.rootPath() + "/pmsApi/getUserInfoByLikeUserName",
                data: {
                    orgId: smallGroupId,
                    //checkPersonCodes: checkPersonCodes,
                    fpStatus:fpStatus,
                    roleCode: 'check',
                    reformVoStr: JSON.stringify(handle_getReformList(row))
                },
                dataType: 'json',
                method: 'post',
                async: false,
                success: function (result) {
                    $operator.combobox("loadData", result);
                }
            });
        }
    });
}

/**
 * 初始化大组下拉框
 */
function handle_initBigGroup(row, value) {
    var $bigGroup = $("#handle_firstReform_bigGroup");
    $.ajax({
        url: ctx.rootPath() + "/firstReform/findBigGroupByAccountAndAbility",
        data: {
            taskDef: 'apply-check',
            reformVoStr: JSON.stringify(handle_getReformList(row))
        },
        dataType: 'json',
        method: 'post',
        async: false,
        success: function (result) {
            $bigGroup.combobox({
                valueField: 'id',
                textField: 'name',
                editable: false,
                readonly: isNotNull(value),
                value: value,
                data: result.dataList,
                onChange: function (newValue, oldValue) {
                    $("#firstReform_smallGroup").combobox("setValue", "");	// 清空小组下拉框选项
                    $("#firstReform_operator").combobox("setValue", "");	// 清空处理人下拉框选项
                    handle_reloadSmallGroup(row, newValue);// 重新加载小组下拉框选项
                },
                onLoadSuccess: function () {
                    var defaultVal = $bigGroup.combobox("getValue");
                    if (isNotNull(defaultVal)) {//回填的时候如果没有可选择的组时需要清空
                        var findValue = $bigGroup.combobox("options").finder.getRow($bigGroup[0], defaultVal);
                        if (!isNotNull(findValue)) {
                            defaultVal = "";
                            $bigGroup.combobox("setValue", "");
                        }
                    }
                    handle_reloadSmallGroup(row, defaultVal);//(大组有可能为空) 重新加载小组下拉框选项
                }
            });
        }
    });
}

/**
 * 过滤小组(orgId有可能为空)
 * @param orgId
 */
function handle_reloadSmallGroup(row, orgId) {
    $.ajax({
        url: ctx.rootPath() + "/firstReform/findTeamByAccountAndOrgIdAndAbility",
        data: {
            orgId: orgId,
            taskDef: 'apply-check',
            reformVoStr: JSON.stringify(handle_getReformList(row))
        },
        dataType: 'json',
        method: 'post',
        async: false,
        success: function (result) {
            if (result.type == 'SUCCESS') {
                $("#handle_firstReform_smallGroup").combobox("loadData", result.dataList);
            }
        }
    });
}


/**
 * 办理页面改派--改派提交
 */
var canSubmit = true;// 控制是否可以重复点击提交按钮
function handle_firstReformUpdate(row) {
    if (canSubmit) {
        canSubmit = false;
        var $operator = $("#handle_firstReform_operator");
        var targetUserCode = $operator.combobox("getValue");
        var selectUser = $operator.combobox("options").finder.getRow($operator[0], targetUserCode);
        if (isNotNull(selectUser)) {
            var targetUserName = $operator.combobox("getText");
            if ($("#handle_firstReform_dialog").find("form").form("validate")) {
                if (isNotNull(row)) {
                    var list = [];
                    var markUser = true;
                    if (row.checkPersonCode == targetUserCode && isNotNull(row.rtfNodeStatus) && (row.rtfNodeStatus == 'XSCS-ASSIGN' || row.rtfNodeStatus == 'XSCS-HANGUP')) {
                        markUser = false;
                    }
                    var obj = new Object();
                    obj.loanNo = row.loanNo;
                    obj.version = row.version;
                    obj.rtfNodeState = row.rtfNodeStatus;
                    obj.ifNewLoanNo = row.ifNewLoanNo;
                    // 已分配或者挂起需要获取原初审员
                    if (row.rtfNodeStatus == 'XSCS-ASSIGN' || row.rtfNodeStatus == "XSCS-HANGUP") {
                        obj.userCode = row.checkPersonCode;
                    }
                    obj.targetUserCode = targetUserCode;
                    obj.targetuserName = targetUserName;
                    obj.personId = row.personId;
                    list.push(obj);
                    if (markUser) {
                        $.ajax({
                            url: ctx.rootPath() + "/firstReform/updateReform",
                            dataType: "json",
                            method: 'post',
                            contentType: 'application/json',
                            data: JSON.stringify(list),
                            success: function (data) {
                                if (data.success) {
                                    $.info("提示", data.messages, "info", 3000)
                                    $("#firstReform_reassignment_dialog").dialog("close");
                                    setTimeout(function () {
                                        canSubmit = true;
                                        window.close();
                                    }, 2000);
                                } else {
                                    $.info("提示", data.messages, "error");
                                    canSubmit = true;
                                }
                            },
                            error: function (data) {
                                $.info("警告", data.responseText, "warning");
                                canSubmit = true;
                            }
                        });
                    } else {
                        $.info("提示", "自己不能改派给自己");
                        canSubmit = true;
                    }
                } else {
                    $.info("提示", "请选择要改派的订单!");
                    canSubmit = true;
                }
            }
        } else {
            $operator.combobox("setValue", "");
            canSubmit = true;
        }
    }
}

/**
 *
 * 前前添加退回原因或者删除
 *
 * @param operationType-add-delete
 */
function firstAddOrDeleteRetureReason(operationType,obj) {
    if ("ADD" == operationType) {
        var dataSize = $("#firstReform_firstReturn_combobox").combobox("getData").length;
        if ($("#firstReform_return_dialog").find(".markReturnReason").length < dataSize) {
            var html = $("<tr class='markReturnReason'><th>一级原因:</th><td><input name='firstReason' class='input' data-options='onChange:function(newValue,oldValue){validateRepetitionRason(newValue,oldValue)}'><input type='hidden' name='firstReasonText'></td><th>二级原因:</th><td><input name='secondReason' class='input' data-options=multiple:true,separator:'|'><input type='hidden' name='secondReasonText'></td><td><a href='javaScript:void(0);' onclick=firstAddOrDeleteRetureReason('DELETE',this)><i class='fa fa-minus' aria-hidden='true'></i></a></td></tr>");
            initClassAReason(html.find("input[name='firstReason']"), "CSRTNQQ", "return", html.find("input[name='secondReason']"), 260);
            $("#firstReform_return_dialog").find(".markReturnReason:last").after(html);
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
function firstCreateMoneyReturnReason(obj) {
    var returnReasons = new Array();
    $("#firstReform_return_dialog").find(".markReturnReason").each(function(ind,trObj){
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
   /* obj.firstReason=null;
    obj.firstReasonText=null;
    obj.secondReason=null;
    obj.secondReasonText=null;*/
    obj.returnReasons =returnReasons;
    return obj;
}