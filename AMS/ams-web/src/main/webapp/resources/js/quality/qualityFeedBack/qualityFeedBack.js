$(function () {
    qualityHistory("#qualityHistory");
    getFeedBackHistoryByCheckResId("#feedBackRecord");
    feedBackAttachmentTable("#feedBackAttachmentTable");
    $(":radio").click(function(){
        if($(this).val()=='F_000001' || $(this).val()=='F_000002'){
            $("#checkType").combobox({
                readonly: false
            });
            $("#checkError").combobox({
                readonly: false
            });
            $("#errorCode").combobox({
                readonly: false
            });
            $("#conclusionConfuseInputId").textbox({
                readonly: false
            });
            $("#conclusionConfirmInputId").textbox({
                readonly: true,
                style:"background:#CCCCCC"
            });
        }else{
            $("#checkType").combobox({
                readonly: true
            });
            $("#checkError").combobox({
                readonly: true
            });
            $("#errorCode").combobox({
                readonly: true
            });
            $("#conclusionConfirmInputId").textbox({
                readonly: false
            });
            $("#conclusionConfuseInputId").textbox({
                readonly: true,
                style:"background:#CCCCCC"
            });
        }
        $(".easyui-textbox").textbox("reset");
    });
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
            title: '差错代码',
            width: 120,
        }, {
            field: 'checkView',
            title: '质检意见',
            width: 120,
        }, {
            field: 'imgName',
            title: '附件',
            width: 300,
        }]]
    });
}

/**
 * @Desc: 质检反馈历史记录查询
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
            field: 'lastModifiedBy',
            title: '反馈人',
            width: 80,
        }, {
            field: 'type',
            title: '反馈结果',
            width: 120,
            formatter:formatterType
        }, {
            field: 'checkType',
            title: '类别',
            width: 80,
            formatter: formatterCheckType
        }, {
            field: 'checkError',
            title: '差错级别',
            width: 120,
            formatter: fomatterCheckError
        }, {
            field: 'errorCode',
            title: '差错代码',
            width: 120,
        }, {
            field: 'opinion',
            title: '描述',
            width: 300,
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
        toolbar:'#uploadToolbar',
        columns: [[{
            field: 'imgName',
            title: '附件',
            width: 240,
            formatter: function (value, row, index) {
                return formatString('<a href="javascript:void(0)"  onclick="viewAttachment(\'{0}\');" >' + row.imgName + '</a>', row.url);
                // return '<a href="javascript:void(0)" target="_parent"><img src="'+row.url+'" width="200" height="80" border="0" /></a>';
            }
        }, {
            field: 'createJobnum',
            title: '操作人员',
            width: 120,
        }, {
            field: 'uptime',
            title: '时间',
            width: 150,
        }, {
            field: 'option',
            title: '操作',
            width: 150,
            formatter: function (value, row, index) {
                return formatString('<a href="javascript:void(0)" onclick="deleteAttachmentById(\'{0}\',\'{1}\',\'{2}\');" >删除</a>',row.id,row.creator,row.createJobnum);
            }
        }]]
    });
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
/**
 * @Desc: 保存质检反馈记录
 * @Author: phb
 * @Date: 2017/5/18 17:21
 */
function saveFeedBackRecord(){
    var params = $("#qualityFeedbackM2Form").serializeObject();
    var api = ctx.rootPath() + '/qualityFeedBack/saveFeedBackRecord';
    var callback = function (data) {
        if (data.success) {
            $.info("提示",data.firstMessage,"info", 2000);
        } else {
            $.info("提示",data.firstMessage,"info", 2000);
        }
        // 关闭当前tab并刷新上一级tab
        closeAndSelectTabs("质检反馈","质检反馈情况");
    }
    var error = function (XMLHttpRequest, textStatus, errorThrown) {
        console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
    };
    post(api, params, 'json', callback, error);
}

/**
 * @Desc: 质检反馈附件上传
 * @Author: phb
 * @Date: 2017/4/26 15:07
 */
function feedBackAttachmentUpload() {
    var loanNo=$("input[name='loanNo']").val();
    var picImgUrl=$("input[name='picImgUrl']").val();
    var sysName=$("input[name='sysName']").val();
    var nodeKey=$("input[name='nodeKey']").val();
    var operator=$("input[name='operator']").val();
    var jobNumber=$("input[name='jobNumber']").val();

    var ifm = '<iframe src="'+picImgUrl+'?nodeKey='+nodeKey+'&sysName='+sysName+'&operator=' + operator + '&jobNumber=' + jobNumber + '&appNo=' + loanNo +
        '" style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>';
    $("#feedBackAttachmentBtn").removeClass("display_none").dialog({
        title: "附件上传",
        modal: true,
        width: 800,
        height: 680,
        content: ifm
    })
    $("#feedBackAttachmentTable").datagrid("reload");
    // 关闭当前tab并刷新上一级tab
    closeAndSelectTabs("质检反馈","质检反馈情况");
}
/**
 * @Desc: 上传完成后重新加载反馈附件table
 * @Author: phb
 * @Date: 2017/5/4 15:15
 */
function reloadFeedBackAttachment(){
    $("#feedBackAttachmentTable").datagrid("reload");
}

/**
 * @Desc: 单击查看附件
 * @Author: phb
 * @Date: 2017/5/3 15:03
 */
function viewAttachment(url) {
    var ifm = '<iframe width="100%" height="100%" src="' + url + '"><img src="' + url + '"  border="0" /></iframe>';
    $('#attachmentDetailsDialog').dialog({
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
 * @Desc: 根据id删除反馈附件
 * @Author: phb
 * @Date: 2017/5/6 9:16
 */
function deleteAttachmentById(id,creator,createJobnum){
    $.messager.confirm('确认', '确认删除该附件吗?', function(r){
        if (r){
            var api = ctx.rootPath() + '/qualityFeedBack/deleteAttachmentById';
            var params={id:id,operator:creator,jobNumber:createJobnum};
            var callback = function (data) {
                if (data.type=='SUCCESS') {
                    $.info("提示",data.firstMessage,"info", 2000);
                }else{
                    $.info("提示",data.firstMessage,"info", 2000);
                }
                $("#feedBackAttachmentTable").datagrid("reload");
            }
            var error = function (XMLHttpRequest, textStatus, errorThrown) {
                console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
            };
            post(api, params, 'json', callback, error);
        }
    });
}
