$(function () {
    // 初始化datagrid
    initReconsiderReformDatagrid();
});
/**
 * 初始化datagrid
 */
function initReconsiderReformDatagrid() {
    $("#reconsiderReform_table_datagird").datagrid({
        url: ctx.rootPath() + '/reconsideration/getReconsiderReformList',
        toolbar: '#reconsiderReform_table_datagird_tool',
        striped: true,
        rownumbers: true,
        idField: 'loanNo',
        pagination: true,
        fitColumns: false,
        scrollbarSize: 0,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'loanNo',
            title: '借款编号',
            width: 190,
        }, {
            field: 'name',
            title: '申请人姓名',
            width: 160,
        }, {
            field: 'idNo',
            title: '身份证号码',
            width: 140,
        }, {
            field: 'enterBranch',
            title: '营业部',
            width: 140,
        }, {
            field: 'rejectLink',
            title: '拒绝环节',
            width: 140,
        }, {
            field: 'primaryReason',
            title: '拒绝原因',
            width: 300,
            formatter: function (value, data, index) {
                value = value + (isNotNull(data.secodeReason) ? "-" + data.secodeReason : "");
                return "<label title=\"" + value + "\" class=\"easyui-tooltip\">" + value + "</label>";

            }
        }, {
            field: 'rejectPersonName',
            title: '拒绝人',
            width: 140,
        }, {
            field: 'firstSubmitReviewTime',
            title: '复议日期',
            width: 140,
            formatter:formatDateYMD
        },{
            field: 'handler',
            title: '处理人',
            width: 140,
        }, {
            field: 'reviewStatus',
            title: '复议环节',
            width: 140,
        },  ]]
    });
}
/**
 * 查询
 */
function reconsiderReformQuery() {
    if ($("#reconsiderReform_form").form("validate")) {
        var queryParams = $("#reconsiderReform_form").serializeObject();
        $("#reconsiderReform_table_datagird").datagrid("clearSelections");
        $("#reconsiderReform_table_datagird").datagrid('load', queryParams);
    }
}
/**
 * 复议改派dialog
 */
function reconsiderReformDialog() {
    var rows = $("#reconsiderReform_table_datagird").datagrid("getSelections");
    if (isNotNull(rows) && rows.length > 0) {
        // 初始化处理人
        initOperator();
        if ( isNotNull($("#reconsiderReform_opertor").combobox("getData"))) {
            $("#reconsiderReform_dialog").removeClass("display_none").dialog({
                title: "复议改派",
                modal: true,
                width: 300,
                height: 180,
                buttons: [{
                    text: '提交',
                    iconCls: 'fa fa-arrow-up',
                    handler: function () {
                        reconsiderReformSubmit(rows);
                    }
                }, {
                    text: '取消',
                    iconCls: 'fa fa-reply',
                    handler: function () {
                        $("#reconsiderReform_dialog").dialog("close");
                    }
                }]
            });
        } else {
            $.info("提示", "没有可分配的复议人员");
        }
    }else {
        $.info("提示", "请选择要操作的数据");
    }
}
/**
 * 加载可改派的复议人员
 */
function initOperator() {
    $.ajax({
        url: ctx.rootPath() + "/reconsiderStaff/getReconsiderReformStaff",
        dataType: "json",
        method: 'post',
        contentType: 'application/json',
        data: JSON.stringify(createParams()),
        async: false,
        success: function (data) {
            $("#reconsiderReform_opertor").combobox({
                prompt: '处理人',
                data:data||[],
                editable: false,
                required:true,
                textField: 'staffName',
                valueField: 'staffCode',
            })
        },
        error: function (data) {
            $.info("警告", '系统忙,请稍后', "warning");
        }
    });
}
/**
 * 创建参数
 */
function createParams() {
    var rows = $("#reconsiderReform_table_datagird").datagrid("getSelections");
    var array = [];
    if (isNotNull(rows) && rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            var obj = new Object();
            obj.xsStatus = rows[i].xsStatus;
            obj.handlerCode = rows[i].handlerCode;
            obj.reviewStatus = rows[i].reviewStatus;
            array.push(obj);
        }
    }
    return array;
}

/**
 * 复议改派提交
 */
var reconsiderReformSubmitMark = true;
function reconsiderReformSubmit(rows){
    if (reconsiderReformSubmitMark && $("#reconsiderReform_opertor").combobox("isValid")) {
        reconsiderReformSubmitMark = false;
        $.ajax({
            url: ctx.rootPath() + "/reconsideration/getReconsiderReformSubmit",
            dataType: "json",
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify(submitCreateParams()),
            success: function (data) {
                reconsiderReformSubmitMark =true;
                $.info("提示", data.messages);
                $("#reconsiderReform_dialog").dialog("close");
                if (data.success) {
                    $("#reconsiderReform_table_datagird").datagrid("clearSelections");
                    $("#reconsiderReform_table_datagird").datagrid('reload');
                }
            },
            error: function (data) {
                reconsiderReformSubmitMark =true;
                $.info("警告", '系统忙,请稍后', "warning");
            }
        });
    }
}
/**
 * 提交创建参数
 */
function submitCreateParams() {
    var rows = $("#reconsiderReform_table_datagird").datagrid("getSelections");
    var array = [];
    if (isNotNull(rows) && rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            var obj = new Object();
            obj.xsStatus = rows[i].xsStatus;
            obj.handlerCode = rows[i].handlerCode;
            obj.reviewStatus = rows[i].reviewStatus;
            obj.loanNo=rows[i].loanNo;
            obj.version = rows[i].version;
            obj.xsReviewPersonCode =$("#reconsiderReform_opertor").combobox("getValue");
            obj.handler = $("#reconsiderReform_opertor").combobox("getText");
            array.push(obj);
        }
    }
    return array;
}