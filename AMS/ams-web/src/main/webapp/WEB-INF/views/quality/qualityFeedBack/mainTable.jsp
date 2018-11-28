<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--引入标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
    $(function () {
        qualityFeedBackToDoDatagrid("#qualityFeedBack_todo_datagrid");
        qualityFeedBackDoneDatagrid("#qualityFeedBack_done_datagrid");
    });

    // 待处理查询
    $("#qualityFeedBack_todo_Query").click(function qualityFeedBack_todo_Query() {
        var queryParams = $("#qualityFeedBack_todo_queryForm").serializeObject();
        $('#qualityFeedBack_todo_datagrid').datagrid('options').queryParams = queryParams;
        $("#qualityFeedBack_todo_datagrid").datagrid('reload');
    });

    // 待处理重置
    $("#qualityFeedBack_todo_Reset").click(function qualityFeedBack_todo_Reset() {
        $('#qualityFeedBack_todo_queryForm').form('clear');
    });
    /**
     * @Desc: 待处理反馈table
     * @Author: phb
     * @Date: 2017/4/28 9:17
     */
    function qualityFeedBackToDoDatagrid(id) {
        $(id).datagrid({
            url: ctx.rootPath() + "/qualityFeedBack/toDoPageList",
            striped: true,
            toolbar: id + '_tool',
            rownumbers: true,
            idField: 'id',
            pagination: true,
            fitColumns: true,
            scrollbarSize: 0,
            rowStyler:function(index,row){
                if (row.type=='F_000001' ||row.type=='F_000002'){
                    return 'background-color:red;';
                }
            },
            toolbar: '#qualityFeedBack_todo_toolBarBtn',
            columns: [[{
                field: 'ck',
                checkbox: true
            },{
                field: 'endDate',
                title: '完成日期',
                formatter: function (value, data, index) {
                    if (value != null) {
                        return moment(value).format('YYYY-MM-DD hh:mm:ss');
                    }
                }
            }, {
                field: 'customerName',
                title: '客户姓名',
            }, {
                field: 'idNo',
                title: '身份证号',
                formatter: function (value, data, index) {
                    if (value != null) {
                        return '*' + value.slice(-4);
                    }
                }
            }, {
                field: 'customerType',
                title: '客户类型',
            }, {
                field: 'owningBrance',
                title: '进件营业部',
            }, {
                field: 'source',
                title: '申请来源',
            }, {
                field: 'loanNo',
                title: '申请件编号',
            }, {
                field: 'applyDate',
                title: '申请日期',
                formatter: function (value, data, index) {
                    if (value != null) {
                        return moment(value).format('YYYY-MM-DD hh:mm:ss');
                    }
                }
            }, {
                field: 'productName',
                title: '贷款类型',
                formatter:formatterProduct,
            },
                /*信审人员页面没有这字段*/
                <shiro:hasPermission name="qualityFeedBack/mainTable/rtfState">
                {
                    field: 'rtfState',
                    title: '审批结果',
                },
                </shiro:hasPermission>
                /*终审人员和质检员不显示，质检管理可看*/
                <shiro:hasPermission  name="qualityFeedBack/mainTable/checkPerson">
                {
                    field: 'checkPersonName',
                    title: '初审人员',
                },
                </shiro:hasPermission>
                /*初审人员和质检员不显示，质检管理可看*/
                <shiro:hasPermission  name="qualityFeedBack/mainTable/finalPerson">
                {
                    field: 'finalPersonName',
                    title: '终审人员',
                },
                </shiro:hasPermission>
                /*1质检员不看具体姓名，质检管理人员看具体姓名（组长名字）
                 2初审人员都可看初审组长名字
                 3终审人员不显示
                 */
                <shiro:hasPermission  name="qualityFeedBack/mainTable/checkLeaderName">
                {
                    field: 'checkLeaderName',
                    title: '初审小组',
                },
                </shiro:hasPermission>
                {
                    field: 'approveDate',
                    title: '审批日期',
                    formatter: function (value, data, index) {
                        if (value != null) {
                            return moment(value).format('YYYY-MM-DD hh:mm:ss');
                        }
                    }
                },
                /*信审人员页面没有这字段*/
                <shiro:hasPermission name="qualityFeedBack/mainTable/checkUser">
                {
                    field: 'checkUser',
                    title: '质检人员',
                },
                </shiro:hasPermission>
                {
                    field: 'qualityCheckId',
                    title: '操作',
                    align: 'center',
                    formatter: function (value, row, index) {
                        var btn1 = '';
                        var btn2 = '';
                        var btn3 = '';
                        btn1 = formatString('<a href="javascript:void(0)" onclick="checkDetails(\'{0}\',\'{1}\');" >详情</a>', row.loanNo, value);
                        btn2 = formatString('<a href="javascript:void(0)" onclick="firstFeedBack(\'{0}\',\'{1}\',\'{2}\');" >初审反馈</a>', row.checkResId, row.loanNo, value);
                        btn3 = formatString('<a href="javascript:void(0)" onclick="finalFeedBack(\'{0}\',\'{1}\',\'{2}\');" >终审反馈</a>', row.checkResId, row.loanNo, value);
                        if (row.checkError == "apply-check") {
                            return btn1 + ' | ' + btn2;
                        } else if (row.checkError == "applyinfo-finalaudit") {
                            return btn1 + ' | ' + btn3;
                        }
                    }
                }]]
        });
    }

    // 已完成查询
    $("#qualityFeedBack_done_Query").click(function qualityFeedBack_done_Query() {
        var queryParams = $("#qualityFeedBack_done_queryForm").serializeObject();
        $('#qualityFeedBack_done_datagrid').datagrid('options').queryParams = queryParams;
        $("#qualityFeedBack_done_datagrid").datagrid('reload');
    });

    // 已完成重置
    $("#qualityFeedBack_done_Reset").click(function qualityFeedBack_done_Reset() {
        $('#qualityFeedBack_done_queryForm').form('clear');
    });
    /**
     * @Desc: 已完成反馈table
     * @Author: phb
     * @Date: 2017/4/28 9:17
     */
    function qualityFeedBackDoneDatagrid(id) {
        $(id).datagrid({
            url: ctx.rootPath() + "/qualityFeedBack/donePageList",
            striped: true,
            toolbar: id + '_tool',
            singleSelect: true,
            rownumbers: true,
            idField: 'id',
            pagination: true,
            fitColumns: true,
            scrollbarSize: 0,
            toolbar: "#qualityFeedBack_done_toolBarBtn",
            columns: [[{
                field: 'ck',
                checkbox: true
            },{
                field: 'endDate',
                title: '完成日期',
                formatter: function (value, data, index) {
                    if (value != null) {
                        return moment(value).format('YYYY-MM-DD hh:mm:ss');
                    }
                }
            }, {
                field: 'customerName',
                title: '客户姓名',
            }, {
                field: 'idNo',
                title: '身份证号',
                formatter: function (value, data, index) {
                    if (value != null) {
                        return '*' + value.slice(-4);
                    }
                }
            }, {
                field: 'customerType',
                title: '客户类型',
            }, {
                field: 'owningBrance',
                title: '进件营业部',
            }, {
                field: 'source',
                title: '申请来源',
            }, {
                field: 'loanNo',
                title: '申请件编号',
            }, {
                field: 'productName',
                title: '贷款类型',
                formatter:formatterProduct,
            },
                /*信审人员页面没有这字段*/
                <shiro:hasPermission name="qualityFeedBack/mainTable/rtfState">
                {
                    field: 'rtfState',
                    title: '审批结果',
                },
                </shiro:hasPermission>
                /*终审人员和质检员不显示，质检管理可看*/
                <shiro:hasPermission  name="qualityFeedBack/mainTable/checkPerson">
                {
                    field: 'checkPersonName',
                    title: '初审人员',
                },
                </shiro:hasPermission>
                /*初审人员和质检员不显示，质检管理可看*/
                <shiro:hasPermission  name="qualityFeedBack/mainTable/finalPerson">
                {
                    field: 'finalPersonName',
                    title: '终审人员',
                },
                </shiro:hasPermission>
                /*1质检员不看具体姓名，质检管理人员看具体姓名（组长名字）
                 2初审人员都可看初审组长名字
                 3终审人员不显示
                 */
                <shiro:hasPermission  name="qualityFeedBack/mainTable/checkLeaderName">
                {
                    field: 'checkLeaderName',
                    title: '初审小组',
                },
                </shiro:hasPermission>
                {
                    field: 'approveDate',
                    title: '审批日期',
                    formatter: function (value, data, index) {
                        if (value != null) {
                            return moment(value).format('YYYY-MM-DD hh:mm:ss');
                        }
                    }
                },
                /*信审人员页面没有这字段*/
                <shiro:hasPermission name="qualityFeedBack/mainTable/checkUser">
                {
                    field: 'checkUser',
                    title: '质检人员',
                },
                </shiro:hasPermission>
                {
                    field: 'qualityCheckId',
                    title: '操作',
                    align: 'center',
                    formatter: function (value, row, index) {
                        // return '<a href="javaScript:addTabs(\'审批办理\', \'/qualityFeedBack/firstApproveReceive?flag=done&qualityCheckId=' + value + '\');"><i class="fa fa-pencil" aria-hidden="true"></i>编辑</a>';
                        var btn1 = '';
                        var btn2 = '';
                        var btn3 = '';
                        btn1 = formatString('<a href="javascript:void(0)" onclick="checkDetails(\'{0}\',\'{1}\');" >详情</a>', row.loanNo, value);
                        btn2 = formatString('<a href="javascript:void(0)" onclick="firstFeedBackDetails(\'{0}\',\'{1}\',\'{2}\');" >初审反馈详情</a>', row.loanNo, value, row.checkResId);
                        btn3 = formatString('<a href="javascript:void(0)" onclick="finalFeedBackDetails(\'{0}\',\'{1}\',\'{2}\');" >终审反馈详情</a>', row.loanNo, value, row.checkResId);
                        if (row.checkError == "apply-check") {
                            return btn1 + ' | ' + btn2;
                        } else if (row.checkError == "applyinfo-finalaudit") {
                            return btn1 + ' | ' + btn3;
                        }
                    }
                }]]
        });
    }
</script>
<div class="easyui-tabs">
    <div title="待处理">
        <div class="easyui-panel W100" data-options="collapsible:true">
            <form id="qualityFeedBack_todo_queryForm" class="margin_20">
                <table class="table_ui W100 center_m">
                    <tr>
                        <th>客户姓名:</th>
                        <td><input type="text" name="customerName" class="easyui-textbox input"
                                   data-options="prompt:'客户姓名'"></td>
                        <th>身份证号:</th>
                        <td><input type="text" name="idNo" class="easyui-textbox input"
                                   data-options="prompt:'身份证号',validType:'IDCard'"></td>

                        <shiro:hasPermission name="qualityCheck,qualityCheckDirector,qualityCheckManager">
                            <th>质检人员:</th>
                            <td>
                                <select class="easyui-combobox select" name="checkUser"
                                        data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findQualityPerson',
                                    method:'get',
                                    valueField:'usercode',
                                    textField:'name'">
                                </select>
                            </td>
                        </shiro:hasPermission>

                        <th>进件营业部:</th>
                        <td>
                            <select class="easyui-combobox select" name="owningBranceId"
                                    data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findAllDepts',
                                    method:'get',
                                    valueField:'orgCode',
                                    textField:'name'">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>差错代码:</th>
                        <td><input type="text" name="errorCode" class="easyui-textbox input"
                                   data-options="prompt:'差错代码'">
                        </td>
                        <shiro:hasPermission name="qualityFeedBack/mainTable/assignDateStart">
                            <th>完成日期:</th>
                            <td><input type="text" name="assignDateStart" class="easyui-datebox input"
                                       data-options="prompt:'完成开始时间',validType:'date'"
                                       id="queryControlDesk_todo_inStartDate"></td>
                            <th>至:</th>
                            <td><input type="text" name="assignDateEnd" class="easyui-datebox input"
                                       data-options="prompt:'完成结束时间',validType:['date','compareDate[\'#queryControlDesk_todo_inStartDate\']']">
                            </td>
                        </shiro:hasPermission>
                        <th>
                        </th>
                        <td>
                            <a class="easyui-linkbutton" data-options="plain:true" id="qualityFeedBack_todo_Reset"><i
                                    class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
                            <a class="easyui-linkbutton" data-options="plain:true" id="qualityFeedBack_todo_Query"><i
                                    class="fa fa-search"></i>搜&nbsp;索</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="h20"></div>
        <div>
            <div id="qualityFeedBack_todo_toolBarBtn">
                <shiro:hasPermission name="qualityCheck,qualityCheckDirector,qualityCheckManager">
                    <a class="easyui-linkbutton" data-options="plain:true" onclick="exportToDoList();"><i
                            class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>&nbsp;
                </shiro:hasPermission>
                <a class="easyui-linkbutton" data-options="plain:true" onclick="oneClickFeedBack();"><i
                        class="fa fa-commenting" aria-hidden="true"></i>一键反馈</a>&nbsp;
            </div>
            <table id="qualityFeedBack_todo_datagrid"></table>
        </div>
    </div>
    <div title="已完成">
        <div class="easyui-panel W100" data-options="collapsible:true">
            <form id="qualityFeedBack_done_queryForm" class="margin_20">
                <table class="table_ui W100 center_m">
                    <tr>
                        <th>客户姓名:</th>
                        <td><input type="text" name="customerName" class="easyui-textbox input"
                                   data-options="prompt:'客户姓名'"></td>
                        <th>身份证号:</th>
                        <td><input type="text" name="idNo" class="easyui-textbox input"
                                   data-options="prompt:'身份证号',validType:'IDCard'"></td>
                        <shiro:hasPermission name="qualityFeedBack/mainTable/checkUser">
                            <th>质检人员:</th>
                            <td>
                                <select class="easyui-combobox select" name="checkUser"
                                        data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findQualityPerson',
                                    method:'get',
                                    valueField:'usercode',
                                    textField:'name'">
                                </select>
                            </td>
                        </shiro:hasPermission>
                        <th>进件营业部:</th>
                        <td>
                            <select class="easyui-combobox select" name="owningBranceId"
                                    data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findAllDepts',
                                    method:'get',
                                    valueField:'orgCode',
                                    textField:'name'">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>差错代码:</th>
                        <td>
                            <input type="text" name="errorCode" class="easyui-textbox input"
                                   data-options="prompt:'差错代码'">
                        </td>
                        <shiro:hasPermission name="qualityCheck,qualityCheckDirector,qualityCheckManager">
                            <th>完成日期:</th>
                            <td><input type="text" name="endDateStart" class="easyui-datebox input"
                                       data-options="prompt:'完成开始时间',validType:'date'"
                                       id="queryControlDesk_done_inStartDate"></td>
                            <th>至:</th>
                            <td><input type="text" name="endDateEnd" class="easyui-datebox input"
                                       data-options="prompt:'完成结束时间',validType:['date','compareDate[\'#queryControlDesk_done_inStartDate\']']">
                            </td>
                        </shiro:hasPermission>
                        <th>
                        </th>
                        <td>
                            <a class="easyui-linkbutton" data-options="plain:true" id="qualityFeedBack_done_Reset"><i
                                    class="fa fa-times"></i>重&nbsp;置</a>&nbsp;
                            <a class="easyui-linkbutton" data-options="plain:true" id="qualityFeedBack_done_Query"><i
                                    class="fa fa-search"></i>搜&nbsp;索</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="h20"></div>
        <div>
            <div id="qualityFeedBack_done_toolBarBtn">
                <shiro:hasPermission name="qualityCheck,qualityCheckDirector,qualityCheckManager">
                    <a class="easyui-linkbutton" data-options="plain:true" onclick="exportDoneList();"><i
                            class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>&nbsp;
                </shiro:hasPermission>
            </div>
        </div>
        <table id="qualityFeedBack_done_datagrid"></table>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityFeedBack/mainTable.js"></script>

