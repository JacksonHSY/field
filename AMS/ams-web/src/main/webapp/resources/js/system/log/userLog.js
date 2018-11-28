$(function($) {
	// 初始化日志管理
    $("#logManage_datagrid").datagrid({
        url : ctx.rootPath() + '/log/userLogPageList',
        striped : true,
        singleSelect : true,
        rownumbers : true,
        idField : 'id',
        pagination : true,
        fitColumns : true,
        scrollbarSize : 0,
        columns : [ [ {
            field : 'loanNo',
            title : '借款编号',
            width : 100
        }, {
            field : 'ip',
            title : 'ip地址',
            width : 80
        }, {
            field : 'link',
            title : '操作环节',
            width : 80
        }, {
            field : 'operation',
            title : '当前操作',
            width : 100
        }, {
            field : 'method',
            title : '调用方法',
            width : 100
        }, {
            field : 'args',
            title : '参数',
            width : 120,
            formatter : function(value, row, index) {
                return "<a title='" + value + "' href='javaScript:void(0);'>" + value + "</a>";;
            }
        }, {
            field : 'remark',
            title : '备注',
            width : 100,
            formatter : function(value, row, index) {
                return "<a title='" + value + "' href='javaScript:void(0);'>" + value + "</a>";;
            }
        }, {
            field : 'createdBy',
            title : '创建人',
            width : 80
        }, {
            field : 'createdDate',
            title : '创建时间',
            width : 100,
            formatter : function(value, data, index) {
                return getLocalTime(value);
            }
        }, {
            field : 'lastModifiedBy',
            title : '修改人',
            width : 80
        }, {
            field : 'lastModifiedDate',
            title : '修改时间',
            width : 100,
            formatter : function(value, data, index) {
                return getLocalTime(value);
            }
        } ] ]
    });
});

//条件查询
function userLogQuery() {
	$("#logManage_datagrid").datagrid('load', $("#logManage_Query_Form").serializeObject());
}