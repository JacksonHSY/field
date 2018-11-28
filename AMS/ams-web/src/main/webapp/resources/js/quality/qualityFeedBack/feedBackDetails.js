$(function () {
    qualityHistory("#qualityHistoryDetails");
    getFeedBackHistoryByCheckResId("#feedBackRecordDetails");
    feedBackAttachmentTable("#feedBackAttachmentTableDetails");
});
/**
 * @Desc: 质检历史结论查询
 * @Author: phb
 * @Date: 2017/4/26 15:02
 */
function qualityHistory(id) {
    var qualityCheckId = $("input[name='qualityCheckId']").val();
    $(id).datagrid({
        url: ctx.rootPath() + "/qualityFeedBack/qualityHistory?qualityCheckId=" + qualityCheckId,
        method: "POST",
        /* toolbar : id + '_tool', */
        singleSelect: true,
        idField: 'id',
        /* pagination : true, */
        fitColumns: true,
        scrollbarSize: 0,
        columns: [[{
            field: 'checkUser',
            title: '质检人员',
            width: 120,
        }, {
            field: 'checkError',
            title: '类别',
            width: 120,
            formatter: formatterCheckType
        }, {
            field: 'checkResult',
            title: '差错级别',
            width: 120,
            formatter: fomatterCheckError
        }, {
            field: 'errorCode',
            title: '错误代码',
            width: 120,
        }, {
            field: 'checkView',
            title: '质检意见',
            width: 240,
        }, {
            field: 'imgName',
            title: '附件',
            width: 240,
        }]]
    });
}

/**
 * @Desc: 质检反馈结果查询
 * @Author: phb
 * @Date: 2017/4/26 15:02
 */
function getFeedBackHistoryByCheckResId(id) {
    var checkResId = $("input[name='checkResId']").val();
    $(id).datagrid({
        url: ctx.rootPath() + "/qualityFeedBack/getFeedBackHistoryByCheckResId?checkResId=" + checkResId,
        method: "POST",
        idField: 'id',
        fitColumns: true,
        scrollbarSize: 0,
        columns: [[{
            field: 'createdBy',
            title: '反馈人',
            width: 120,
        }, {
            field: 'type',
            title: '反馈结果',
            width: 120,
            formatter:formatterType
        }, {
            field: 'checkType',
            title: '类别',
            width: 120,
            formatter: formatterCheckType
        }, {
            field: 'checkError',
            title: '错误级别',
            width: 120,
            formatter: fomatterCheckError
        }, {
            field: 'errorCode',
            title: '错误代码',
            width: 120,
        }, {
            field: 'opinion',
            title: '描述',
            width: 240,
        }]]
    });
}
/**
 * @Desc: 反馈附件table加载
 * @Author: phb
 * @Date: 2017/4/28 9:16
 */
function feedBackAttachmentTable(id) {
    var loanNo = $("input[name='loanNo']").val();
    $(id).datagrid({
        url: ctx.rootPath() + '/qualityFeedBack/findFeedBackAttachmentList?loanNo=' + loanNo,
        method: "POST",
        idField: 'id',
        fitColumns: true,
        scrollbarSize: 0,
        columns: [[{
            field: 'imgName',
            title: '附件',
            width: 120,
            formatter: function (value, row, index) {
                return formatString('<a href="javascript:void(0)"  onclick="viewAttachment(\'{0}\');" >' + row.imgName + '</a>', row.url);
            }
        }, {
            field: 'createJobnum',
            title: '操作人员',
            width: 120,
        }, {
            field: 'uptime',
            title: '时间',
            width: 120,
        }]]
    });
}
/**
 * @Desc: 单击查看附件
 * @Author: phb
 * @Date: 2017/5/3 15:03
 */
function viewAttachment(url) {
    var ifm = '<iframe width="100%" height="100%" src="' + url + '"><img src="' + url + '"  border="0" /></iframe>';
    $('#attachmentDetailsDialogDetails').dialog({
        title: '反馈附件',
        width:"60%",
        height:"100%",
        closed: false,
        cache: false,
        content: ifm,
    });
}
/**
 * @Desc: 格式化后的字符串
 * @Author: phb
 * @Date: 2017/5/3 15:26
 */
function formatString(str) {
    for (var i = 0; i < arguments.length - 1; i++) {
        str = str.replace("{" + i + "}", arguments[i + 1]);
    }
    return str;
}
/**
 * @Desc: 格式化质检差错
 * @Author: phb
 * @Date: 2017/5/13 11:03
 */
function fomatterCheckError(value, row, index){
    if(value==null){
        return "";
    }else if(value=="E_000004"){
        return "重大差错"
    }else if(value=="E_000003"){
        return "一般差错";
    }else if(value=="E_000002"){
        return "建议";
    }else if(value=="E_000001"){
        return "预警";
    }else if(value=="E_000000"){
        return "无差错";

    }
}
/**
 * @Desc: 格式化反馈类型
 * @Author: phb
 * @Date: 2017/5/14 15:10
 */
function formatterCheckType (value, row, index) {
    if(value==null){
        return "";
    }else if(value=="apply-check"){
        return "初审"
    }else if(value=="applyinfo-finalaudit"){
        return "终审";
    }
}
/**
 * @Desc: 格式化质检反馈结论
 * @Author: phb
 * @Date: 2017/5/14 15:09
 */
function formatterType (value, row, index) {
    if(value==null){
        return "";
    }else if(value=="F_000000"){
        return "确认"
    }else if(value=="F_000001"){
        return "争议";
    }else if(value=="F_000002"){
        return "仲裁";
    }else if(value=="F_000003"){
        return "定版";
    }
}