<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--引入标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--质检处理情况--%>
<div class="easyui-tabs">
    <div title="待处理">
        <div class="easyui-panel W100" data-options="collapsible:true">
            <form id="qualityInspectionProcessToDo_queryForm" class="margin_20">
                <table class="table_ui W100 center_m">
                    <tr>
                        <th>客户姓名:</th>
                        <td><input type="text" name="customerName" style="width:190px;" class="easyui-textbox input"
                                   data-options="prompt:'客户姓名'"></td>
                        <th>质检人员:</th>
                        <td>
                            <select class="easyui-combobox select" name="checkUser" style="width:190px;"
                                    data-options="editable:false, url:'${ctx}/qualityPersonnelManagement/getBranchPerson',
                                    method:'get',
                                    valueField:'usercode',
                                    textField:'name'">
                            </select>
                        </td>
                        <th>身份证号:</th>
                        <td><input type="text" name="idNo" class="easyui-textbox input" style="width:190px;"
                                   data-options="prompt:'身份证号',validType:'IDCard'"></td>
                        <th>进件营业部:</th>
                        <td>
                            <select class="easyui-combobox select" name="owningBranceId" style="width:190px;" id="owningBranceIdTodo"
                                    data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findAllDepts',
                                    method:'get',
                                    multiple:true,
                                    panelHeight:'auto',
                                    editable:true,
                                    panelMaxHeight:'500',
                                    valueField:'id',
                                    textField:'name'">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>申请件编号:</th>
                        <td><input type="text" name="loanNo" class="easyui-textbox input" style="width:190px;" data-options="prompt:'申请件编号'">
                        </td>
                        <th>审批结果:</th>
                        <td>
                            <select name="approvalStatus" class="easyui-combobox select" style="width:190px;"
                                    data-options="editable:false, url:ctx.rootPath()+ '/common/getQualityApprovalStatus',
                                    method:'get',
                                    multiple:false,
                                    panelHeight:'auto',
                                    editable:false,
                                    panelMaxHeight:'500',
                                    valueField:'code',
                                    textField:'value'">
                            </select>
                        </td>
                        <th>分派日期:</th>
                        <td><input type="text" name="assignDateStart" class="easyui-datebox input" style="width:190px;"
                                   data-options="editable:false,prompt:'分派开始时间',validType:['date','compareToday']"
                                   id="qualityInspectionProcessTodo_inStartDate"></td>
                        <th>至:</th>
                        <td><input type="text" name="assignDateEnd" class="easyui-datebox input" style="width:190px;"
                         		   id="qualityInspectionProcessTodo_inEndDate"
                         		   data-options="prompt:'分派结束时间',editable:false,validType:['date','compareDate[\'#qualityInspectionProcessTodo_inStartDate\']','compareToday']">
                        </td>
                    </tr>
                    <tr>
                        <th colspan="7"></th>
                        <td><a class="easyui-linkbutton" id="qualityInspectionProcess_todo_Reset"><i
                                class="fa fa-times"></i>重&nbsp;置</a> <a class="easyui-linkbutton"
                                                                        id="qualityInspectionProcessToDo_Query"><i
                                class="fa fa-search"></i>搜&nbsp;索</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="h20"></div>
        <div>
            <div id="qualityInspectionProcessTodo_toolBarBtn">
                <a class="easyui-linkbutton" data-options="plain:true" onclick="exportToDoExcel();"><i
                        class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>&nbsp;
            </div>
            <table id="qualityInspectionProcessTodo_datagrid" class="datagrid-container"></table>
        </div>
    </div>
    <div title="已完成">
        <div class="easyui-panel W100" data-options="collapsible:true">
            <form id="qualityInspectionProcessDone_queryForm" class="margin_20">
                <table class="table_ui W100 center_m">
                    <tr>
                        <th>客户姓名:</th>
                        <td><input type="text" name="customerName" class="easyui-textbox input"
                                   data-options="prompt:'客户姓名'"></td>
                        <th>质检人员:</th>
                        <td>
                            <select class="easyui-combobox select" name="checkUser"
                                    data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findQualityPerson',
                                    method:'get',
                                    valueField:'usercode',
                                    textField:'name'">
                            </select>
                        </td>
                        <th>身份证号:</th>
                        <td><input type="text" name="idNo" class="easyui-textbox input"
                                   data-options="prompt:'身份证号',validType:'IDCard'"></td>
                        <th>进件营业部:</th>
                        <td>
                            <select class="easyui-combobox select" name="owningBranceId" id="owningBranceIdDone"
                                    data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findAllDepts',
                                    method:'get',
                                    multiple:true,
                                    panelHeight:'auto',
                                    editable:true,
                                    panelMaxHeight:'500',
                                    valueField:'id',
                                    textField:'name'">
                            </select>
                        </td>
                        <th>申请件编号:</th>
                        <td><input type="text" name="loanNo" class="easyui-textbox input" data-options="prompt:'申请件编号'">
                        </td>
                    </tr>
                    <tr>
                        <th>审批结果:</th>
                        <td>
                            <select name="approvalStatus" class="easyui-combobox select"
                                    data-options="editable:false, url:ctx.rootPath()+ '/common/getQualityApprovalStatus',
                                    method:'get',
                                    multiple:false,
                                    panelHeight:'auto',
                                    editable:false,
                                    panelMaxHeight:'500',
                                    valueField:'code',
                                    textField:'value'">
                            </select>
                        <th>完成日期:</th>
                        <td><input name="endDateStart" type="text" class="easyui-datebox input"
                                   data-options="editable:false, prompt:'完成日期',validType:['date','compareToday']"
                                   id="qualityInspectionProcessDone_inStartDate"></td>
                        <th>至:</th>
                        <td><input name="endDateEnd" type="text" class="easyui-datebox input"
                                   data-options="editable:false, validType:['date','compareDate[\'#qualityInspectionProcessDone_inStartDate\']','compareToday']"
                                   id="qualityInspectionProcessDone_inEndDate">
                        </td>
                        <th>差错等级:</th>
                        <td>
                            <select class="easyui-combobox select" name="checkResult" id="checkResultSelect"
                                    data-options="editable:false,
                                    url:ctx.rootPath() + '/common/getQualityCheckResult',
                                    multiple:true,
                                    panelHeight:'auto',
                                    editable:false,
                                    valueField:'code',
                                    textField:'value'">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="10"><a class="easyui-linkbutton" id="qualityInspectionProcess_done_Reset"><i
                                class="fa fa-times"></i>重&nbsp;置</a> <a class="easyui-linkbutton"
                                                                        id="qualityInspectionProcessDone_Query"><i
                                class="fa fa-search"></i>搜&nbsp;索</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="h20"></div>
        <div>
            <div id="qualityInspectionProcessDone_toolBarBtn">
                <a class="easyui-linkbutton" data-options="plain:true" onclick="exportDoneExcel();"><i
                        class="fa fa-external-link" aria-hidden="true"></i>导出列表</a>&nbsp;
                 <a class="easyui-linkbutton" data-options="plain:true" onclick="closeBatch();"><i
                         aria-hidden="true"></i>批量关闭</a>&nbsp;        
                <shiro:hasPermission name="qualityCheckGroupLeader,qualityCheckDirector,qualityCheckManager">
                    <a class="easyui-linkbutton" data-options="plain:true" onclick="qualityFeedbackJobExecute();"><i
                            class="fa fa-commenting" aria-hidden="true"></i>发起反馈</a>&nbsp;
                </shiro:hasPermission>
            </div>
            <table id="qualityInspectionProcessDone_datagrid" class="datagrid-container"></table>
        </div>
    </div>
</div>
<!--信审人员dialog-->
<div id="approvalPerson_dialog" class="display_none">
    <form id="approvalPerson_Form" class="margin_20">
        <input type="hidden" name="id">
        <table class="table_ui W100 center_m">
            <tr>
                <th>初审:</th>
                <td>
                    <select class="easyui-combobox select" name="checkPerson"
                            data-options="editable:false, url:ctx.rootPath()+ '/pmsApi/findCheckPerson',
                            method:'get',
                            valueField:'usercode',
                            textField:'name'">
                    </select>
                </td>
                <th>终审:</th>
                <td>
                    <select class="easyui-combobox select" name="finalPerson"
                            data-options="editable:false, url:ctx.rootPath() + '/pmsApi/findFinalPerson',
                            method:'get',
                            valueField:'usercode',
                            textField:'name'">
                    </select>
                </td>
            </tr>
        </table>
    </form>
    <div>
    	<input type = "hidden" id="qualityInspectionProcess_quality_qualityUser" value='${qualityUser}'>
    </div>
</div>

<!-- 日志详情 -->
<div id="approvalLog_dialog" class="display_none">
    <table class="table_list W100" id="qualityLogShowTable"></table>
</div>

<script type="text/javascript" src="${ctx}/resources/js/quality/qualityInspectionProcess.js"></script>
<script>
    var refreshDatagrid = function(){
        $('.datagrid-container').datagrid('reload');
    }
</script>
