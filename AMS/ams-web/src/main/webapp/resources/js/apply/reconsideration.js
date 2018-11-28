// 是否禁止接单提示标识
var markSwith = true;
$(function () {
    // 初始化待办任务datagrid
    initReconsiderationDatagrid();
    // 初始化已完成datagrid
    initReconsiderationFinishDatagrid();

    $('#reconsideraction_orders_radio').switchbutton({
        onText: '是',
        offText: '否',
        onChange: function (checked) {
            var info = checked == true ? '开启' : '禁止';
            if (markSwith) {
                markSwith = false;
                $("<div id='reconsideraction_orders_radio_confirm'></div>").dialog({
                    title: '提示',
                    width: 302,
                    height: 160,
                    modal: true,
                    onClose: function () {
                        $(this).dialog("destroy");
                    },
                    onDestroy: function () {
                        if (!markSwith) {
                            $('#reconsideraction_orders_radio').switchbutton("resize", {
                                checked: checked == true ? false : true
                            });
                            markSwith = true;
                        }
                    },
                    content: '<div class="messager-body panel-body panel-body-noborder window-body" style="width: 266px;">' +
                    '<div class="messager-icon messager-question"></div><div>确认' + info + '接单？</div><div style="clear:both;"></div>' +
                    '<div class="messager-button">' +
                    '<a href="javascript:void(0)" onclick="reconsiderOkOrCancelButton(true,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">确定</span></span></a>' +
                    '<a href="javascript:void(0)" onclick="reconsiderOkOrCancelButton(false,' + checked + ')" style="margin-left:10px" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></div></div>'
                })
            }
        }
    });
});


/**
 * 复议待办任务datagrid
 *
 * @Author zhouwen
 * @date 2017年07月18日
 */
function initReconsiderationDatagrid() {
    $("#reconsideration_query_datagrid").datagrid({
        url: ctx.rootPath() + '/reconsideration/getReconsiderationUnfinishedList',
        title: "待办任务",
        fitColumns: false,
        remoteSort: false,
        striped: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        pagination: true,
        scrollbarSize: 0,
        onDblClickRow: function (index, data) {
            reconsiderHandleInfo(data);
        },
        columns: [[{
            field: 'loanNo',
            title: '借款编号',
            width: 140,
            sortable: true,
        }, {
            field: 'name',
            title: '申请人姓名',
            width: 120,
            sortable: true,
        }, {
            field: 'idNo',
            title: '身份证号码',
            width: 120,
            sortable: true,
        }, {
            field: 'applyTerm',
            title: '申请期限',
            width: 100,
            sortable: true,
        }, {
            field: 'productName',
            title: '申请产品',
            width: 140,
            sortable: true,
            formatter:formatterProduct,
        }, {
            field: 'enterBranch',
            title: '营业部',
            width: 140,
            sortable: true,
        }, {
            field: 'firstSubmitReviewTime',
            title: '复议日期',
            width: 140,
            sortable: true,
            formatter: formatDateYMD,
        },{
            field: 'rejectLink',
            title: '拒绝环节',
            width: 140,
            sortable: true,
        }, {
            field: 'refuseDate',
            title: '拒绝时间',
            width: 140,
            sortable: true,
        }, {
            field: 'primaryReason',
            title: '拒绝原因',
            width: 260,
            sortable: true,
            formatter: function (value, data, index) {
                value = value + (isNotNull(data.secodeReason) ? "-" + data.secodeReason : "");
                return "<label title=\"" + value + "\" class=\"easyui-tooltip\">" + value + "</label>";

            }
        }, {
            field: 'rejectPersonName',
            title: '拒绝人',
            width: 120,
            sortable: true,
        }, {
            field: 'action',
            title: '操作',
            width: 120,
            align: 'center',
            formatter: function (value, data, index) {
                var action = "<a href='javaScript:reconsiderHandleInfo(" + JSON.stringify(data) + ")'>办理</a>";
                return action;
            }
        }]]
    });
}
/**
 * 已完成复议datagrid
 *
 * @Author zhouwen
 * @date 2017年07月18日
 */
function initReconsiderationFinishDatagrid() {
    $("#reconsideration_finish_query_datagrid").datagrid({
        url: ctx.rootPath() + '/reconsideration/getReconsiderationFinishedList',
        title: "已完成任务",
        fitColumns: false,
        remoteSort: false,
        striped: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        pagination: true,
        scrollbarSize: 0,
        columns: [[{
            field: 'loanNo',
            title: '借款编号',
            width: 160,
            sortable: true,
        }, {
            field: 'name',
            title: '申请人姓名',
            width: 140,
            sortable: true,
        }, {
            field: 'idNo',
            title: '身份证号码',
            width: 140,
            sortable: true,
        }, {
            field: 'enterBranch',
            title: '营业部',
            width: 140,
            sortable: true,
        }, {
            field: 'rejectLink',
            title: '拒绝环节',
            width: 140,
            sortable: true,
        }, {
            field: 'primaryReason',
            title: '拒绝原因',
            sortable: true,
            width: 260,
            formatter: function (value, data, index) {
                value = value + (isNotNull(data.secodeReason) ? "-" + data.secodeReason : "");
                return "<label title=\"" + value + "\" class=\"easyui-tooltip\">" + value + "</label>";
            }
        }, {
            field: 'rejectPersonName',
            title: '拒绝人',
            width: 140,
            sortable: true,
        }, {
            field: 'firstSubmitReviewTime',
            title: '复议日期',
            width: 140,
            formatter: formatDateYMD,
            sortable: true,
        }, {
            field: 'reviewResult',
            title: '复议结果',
            width: 140,
            formatter: function (value, data, index) {
                var text = "";
                if (1 == value) {
                    text = "拒绝"
                } else if (0 == value) {
                    text = "通过"
                }
                return text;
            },
            sortable: true
        },{
            field: 'reviewResultDate',
            title: '复议结果日期',
            width: 140,
            formatter:formatDateYMD,
            sortable: true
        }, {
            field: 'action',
            title: '操作',
            width: 140,
            align: 'center',
            formatter: function (value, data, index) {
                return '<a href="javaScript:void(0);" onclick=handleInfoFinish("' + data.loanNo + '") >查看详情</a>';
            }
        }]]
    });
}

/**
 * 复议关闭开启提示确认或取消
 * @param flag
 */
function reconsiderOkOrCancelButton(flag, checked) {
    markSwith = flag;
    $("#reconsideraction_orders_radio_confirm").dialog("destroy");
    if (flag) { // 确认是否开放接单
        post(ctx.rootPath() + "/reconsiderStaff/updateReconsiderStaffAccept", {
            ifAccept: checked == true ? 'Y' : 'N'
        }, "json", function (data) {
            if (data.success) {
                $.info("提示", "操作成功!");
            } else {
                markSwith = false;
                $('#reconsideraction_orders_radio').switchbutton("resize", {
                    checked: checked == true ? false : true
                });
                $.info("警告", data.firstMessage, "warning");
            }
        });
    } else {
        $('#reconsideraction_orders_radio').switchbutton("resize", {
            checked: checked == true ? false : true
        });
    }
    markSwith = true;
}

/**
 * 待办任务查询
 */
function reconsiderationTask() {
    if ($("#reconsideration_task_form").form("validate")) {
        var params = $("#reconsideration_task_form").serializeObject();
        $("#reconsideration_query_datagrid").datagrid('load', params);
    }
}

// 未完成办理
var reconsiderHandleDataList = [];
function reconsiderHandleInfo(data) {
    reconsiderHandleDataList[data.loanNo + "-" + data.version] = data;
    jDialog.open({
        url: ctx.rootPath() + "/reconsideration/handle/" + data.loanNo + "/" + data.xsReviewPersonLevel + "/" + data.version,
        width: 1400,
        height: 800,
        top: -1,
    });
}
/**
 * 获取代办任务数据
 * @param loanNo
 * @param reconsiderNode
 * @returns {*}
 */
function getReconsiderHandleData(loanNo, version) {
    return reconsiderHandleDataList[loanNo + "-" + version];
}
// 已完成办理
function handleInfoFinish(loanNo) {
    jDialog.open({url: ctx.rootPath() + "/search/handle/" + loanNo + "/6"});
}
// //////////////////////////////////////////////////////////////
