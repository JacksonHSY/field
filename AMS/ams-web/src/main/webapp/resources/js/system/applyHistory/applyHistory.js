$(function() {
    applyHistoryDataGrid();
    approveHistoryDataGrid();
});
/**
 * 日志管理
 * 
 * @Author wangzx
 * @date 2017年3月16日
 */
function applyHistoryDataGrid() {
    $("#applyHistory").datagrid({
        url : ctx.rootPath() + '/applyHistory/getApplyHistoryList',
        striped : true,
        singleSelect : true,
        rownumbers : true,
        idField : 'id',
        pagination : true,
        fitColumns : false,
        scrollbarSize : 0,
        onLoadSuccess : function(data) {
            $("#applyHistory").datagrid('resize');
        },
        columns : [ [ {
            field : 'loanNo',
            width : 140,
            title : '借款编号',
        }, {
            field : 'name',
            width : 100,
            title : '客户姓名',
        }, {
            field : 'proName',
            width : 160,
            title : '流程节点名称',
        }, {
            field : 'rtfState',
            width : 100,
            title : '审批状态',
        }, {
            field : 'rtfNodeState',
            width : 120,
            title : '流程节点状态',
        }, {
            field : 'checkNodeState',
            width : 120,
            title : '复核状态',
        },{
            field : 'remark',
            width : 120,
            title : '审批意见表备注',
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
        },{
            field : 'refuseCodeName',
            width : 120,
            title : '拒绝原因',
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
        },{
            field : 'checkPersonName',
            width : 100,
            title : '初审员',
        },{
            field : 'checkComplexName',
            width : 100,
            title : '初审复核人员',
        },{
            field : 'finalPersonName',
            width : 100,
            title : '终审员',
        },{
            field : 'approvalLeaderName',
            width : 120,
            title : '用户组长',
        },{
            field : 'approvalDirectorName',
            width : 120,
            title : '信审主管',
        },{
            field : 'approvalManagerName',
            width : 120,
            title : '信审经理',
        },{
            field : 'approvalPersonName',
            width : 120,
            title : '协审员',
        },{
            field : 'createdByName',
            width : 100,
            title : '创建人',
        }, {
            field : 'lastModifiedByName',
            width : 100,
            title : '修改人',
        }, {
            field : 'createdDate',
            width : 140,
            title : '创建时间',
            formatter : formatDate,
        }] ]
    });
}
//条件查询
function userLogQuery() {
	$("#applyHistory").datagrid('load',$("#histroty_Query_Form").serializeObject());
}

function approveLogQuery(){
    $("#approveHistory").datagrid('load',$("#approveHistroty_Query_Form").serializeObject());

}

/**
 * 审批日志管理
 *
 * @Author wangzx
 * @date 2017年12月5日
 */
function approveHistoryDataGrid() {
    $("#approveHistory").datagrid({
        url : ctx.rootPath() + '/applyHistory/getApproveHistoryList',
        striped : true,
        singleSelect : true,
        rownumbers : true,
        idField : 'id',
        pagination : true,
        fitColumns : false,
        scrollbarSize : 0,
        onLoadSuccess : function(data) {
            $("#approveHistory").datagrid('resize');
        },
        columns : [ [ {
            field : 'loanNo',
            width : 140,
            title : '借款编号',
        }, {
            field : 'rtfState',
            width : 100,
            title : '审批状态',
        }, {
            field : 'rtfNodeState',
            width : 100,
            title : '流程节点状态',
        }, {
            field : 'checkNodeState',
            width : 100,
            title : '复核状态',
        },{
            field : 'approvalProductCdName',
            width : 100,
            title : '申请产品'
        },{
            field : 'approvalMemo',
            width : 180,
            title : '审批意见表备注',
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
        },{
            field : 'approvalPersonName',
            width : 100,
            title : '审批人员',
        },{
            field : 'approvalCheckIncome',
            width : 100,
            title : '核实收入',
        },{
            field : 'approvalApplyLimit',
            width : 100,
            title : '申请金额',
        },{
            field : 'approvalApplyTerm',
            width : 100,
            title : '申请期限',
        },{
            field : 'approvalLimit',
            width : 100,
            title : '审批额度'
        },{
            field : 'approvalMonthPay',
            width : 100,
            title : '月还款额'
        },{
            field : 'approvalDebtTate',
            width : 100,
            title : '内部负债率'
        },{
            field : 'approvalAllDebtRate',
            width : 100,
            title : '总负债率'
        },{
            field : 'largeGroupName',
            width : 100,
            title : '当前大组'
        },{
            field : 'currentGroupName',
            width : 100,
            title : '当前小组'
        },{
            field : 'currentRole',
            width : 100,
            title : '当前用户角色',
            formatter: function (value, row, index) {
                if(value!=null){
                    return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                }
            }
        },{
            field : 'createdByName',
            width : 100,
            title : '创建人',
        }, {
            field : 'createdDate',
            width : 120,
            title : '创建时间',
            formatter : formatDate,
        }] ]
    });
}