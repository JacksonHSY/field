//质检反馈工作台js

/**
 * @Desc: 意见反馈
 * @Author: phb
 * @Date: 2017/5/19 11:18
 */
function oneClickFeedBack() {
    //勾选的判定，如果有终审和初审反馈则需要同时发起到达才能发起意见反馈
    var params = $("#qualityFeedbackM2Form").serializeObject();
    var rows=$("#qualityFeedBack_todo_datagrid").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("提示","请至少选择一条数据","info");
        return;
    }
    var idArray=new Array();
    $(rows).each(function(){
        idArray.push(this.checkResId);
    });
    //数组转化为字符拼接
    var checkResIds=idArray.join(",");
    params.chekResIds=checkResIds;
    var api = ctx.rootPath() + '/qualityFeedBack/finishFeedbackTask';
    var callback = function (data) {
        if (data.success) {
            $.info("提示", data.firstMessage, "info", 2000);
        } else {
            $.info("提示", data.firstMessage, "info", 2000);
        }
        // 关闭当前tab并刷新上一级tab
        closeAndSelectTabs("质检反馈", "质检反馈情况");
    }
    var error = function (XMLHttpRequest, textStatus, errorThrown) {
        console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
    };
    post(api, params, 'json', callback, error);
}
/**
 * @Desc: 查看反馈申请件的详情
 * @Author: phb
 * @Date: 2017/5/5 15:47
 */
function checkDetails(loanNo, qualityCheckId) {
    addTabs('审批办理详情', ctx.rootPath() + '/qualityControlDesk/qualityReceive?flag=done&qualityCheckId=' + qualityCheckId + '&loanNo=' + loanNo);
}
/**
 * @Desc: 发起初审反馈
 * @Author: phb
 * @Date: 2017/5/5 15:48
 */
function firstFeedBack(checkResId, loanNo, qualityCheckId) {
    addTabs('质检反馈', ctx.rootPath() + '/qualityFeedBack/feedBackTableView?checkResId=' + checkResId + '&loanNo=' + loanNo + '&qualityCheckId=' + qualityCheckId);
}
/**
 * @Desc: 发起终审反馈
 * @Author: phb
 * @Date: 2017/5/5 15:48
 */
function finalFeedBack(checkResId, loanNo, qualityCheckId) {
    addTabs('质检反馈', ctx.rootPath() + '/qualityFeedBack/feedBackTableView?checkResId=' + checkResId + '&loanNo=' + loanNo + '&qualityCheckId=' + qualityCheckId);
}
/**
 * @Desc: 查看初审反馈详情
 * @Author: phb
 * @Date: 2017/5/5 15:49
 */
function firstFeedBackDetails(loanNo, qualityCheckId, checkResId) {
    addTabs('初审反馈详情', ctx.rootPath() + '/qualityFeedBack/feedBackDetailsView?&loanNo=' + loanNo + '&qualityCheckId=' + qualityCheckId + '&checkResId=' + checkResId);
}
/**
 * @Desc: 查看终审反馈详情
 * @Author: phb
 * @Date: 2017/5/5 15:49
 */
function finalFeedBackDetails(loanNo, qualityCheckId, checkResId) {
    addTabs('终审反馈详情', ctx.rootPath() + '/qualityFeedBack/feedBackDetailsView?loanNo=' + loanNo + '&qualityCheckId=' + qualityCheckId + '&checkResId=' + checkResId);
}
/**
 * @Desc: 格式化字符串函数
 * @Author: phb
 * @Date: 2017/5/6 13:54
 */
function formatString(str) {
    for (var i = 0; i < arguments.length - 1; i++) {
        str = str.replace("{" + i + "}", arguments[i + 1]);
    }
    return str;
}
/**
 * @Desc: 导出待处理列表
 * @Author: phb
 * @Date: 2017/5/6 13:55
 */
function exportToDoList() {
    var queryParams = $("#qualityFeedBack_todo_queryForm").serializeObject();
    window.open(ctx.rootPath() + "/qualityFeedBack/exportToDoExcel/" + JSON.stringify(queryParams));
}
/**
 * @Desc: 导出已完成列表
 * @Author: phb
 * @Date: 2017/5/6 13:56
 */
function exportDoneList() {
    var queryParams = $("#qualityFeedBack_done_queryForm").serializeObject();
    window.open(ctx.rootPath() + "/qualityFeedBack/exportDoneExcel/" + JSON.stringify(queryParams));
}