$(function () {
    // 设置开始日期不可编辑，抽检率只能输入100以内整数
    window.setTimeout(function () {
        //本次开始时间有值，设置为只读
        if(isNotNull($('#startdate').datebox('getValue'))){
            $('#startdate').datebox('readonly','readonly');
        }
        //如果下次的结束日期为空，则把更新人和更新时间置空
        if(!isNotNull($('#nextenddate').datebox('getValue'))){
            $('#nextlastModifiedDate').datebox('setValue','');
            $('#nextlastModifiedBy').textbox('setValue','');
        }
        // 如果本次结束日期为空，更新人和更新时间置空
        if(!isNotNull($('#enddate').datebox('getValue'))){
            $('#lastModifiedDate').datebox('setValue','');
            $('#lastModifiedBy').textbox('setValue','');
        }
        // 限制input标签只能输入0-100的整数
        $('.inputmask').find('.validatebox-text').inputmask('Regex', {regex:"^[0-9]|[1-9][0-9]$|^100$"}),100});
});

// 下一次结束抽检率的开始日期是本次抽检结束日期的下一天
$('#enddate').datebox({
    onSelect: function (date) {
        var nextdate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + (date.getDate() + 1);
        $('#nextstartdate').datebox('setValue', nextdate);
    }
});

// 抽检率设置
function samplingRateSave(obj) {
    if ($(obj).parents("form").form("validate")) {
        var api = ctx.rootPath() + '/QualitySet/samplingRateSave';
        var params = $('#regular_queryForm').serializeObject();
        delete params.lastModifiedBy;
        delete params.lastModifiedDate;
        delete params.nextlastModifiedBy;
        delete params.nextlastModifiedDate;
        // 对输入日期做校验
        var start = new Date($('#startdate').datebox('getValue').replace(/\-/g,
            "\/"));
        var enddate = new Date($('#enddate').datebox('getValue').replace(/\-/g,
            "\/"));
        var nextstartdate = new Date($('#nextstartdate').datebox('getValue')
            .replace(/\-/g, "\/"));
        var nextenddate = new Date($('#nextenddate').datebox('getValue').replace(
            /\-/g, "\/"));
        if ($('#startdate').datebox('getValue') == ''
            || $('#enddate').datebox('getValue') == ''
            || $('#nextstartdate').datebox('getValue') == ''
            || $('#nextenddate').datebox('getValue') == '') {
            $.info("提示", "日期不能为空", "info", 2000);
        } else {
            if (enddate - start<0 || nextenddate -nextstartdate<0) {
                $.info("提示", "结束日期必须大于开始日期", "info", 2000);
            } else if ((nextstartdate - enddate) / 86400000 != 1) {
                $.info("提示", "下次周期开始日必须是本次周期结束日的下一天", "info", 2000);
            } else {
                params.isRegular = "0";
                var callback = function (data) {
                    if (data.type == 'SUCCESS') {
                        $.info("提示", data.firstMessage, "info", 2000);
                        window.setTimeout(function () {
                            var tab = $("#layout_container").tabs("getSelected");
                            tab.panel('refresh', ctx.rootPath() + '/QualitySet/qualitySet');
                        }, 1000);
                    } else {
                        $.info("提示", data.firstMessage, "info", 2000);
                    }
                };
                var error = function (XMLHttpRequest, textStatus, errorThrown) {
                    console.info('异常信息', textStatus + '  :  ' + errorThrown + '!',
                        'error');
                };
                post(api, params, 'json', callback, error);
            }
        }
    }
}