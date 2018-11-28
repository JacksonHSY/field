<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form id="reconsiderReform_form">
    <table class="table_ui W100 padding20">
        <tr>
            <th>借款编号:</th>
            <td><input type="text" class="easyui-textbox input" name="loanNo" data-options="prompt:'借款编号',validType:'loanNo'"></td>
            <th>申请人姓名:</th>
            <td><input type="text" class="easyui-textbox input" name="name" data-options="prompt:'申请人姓名',validType:'customerName'"></td>
            <th>身份证号码:</th>
            <td><input type="text" class="easyui-textbox input" name="idNo" data-options="prompt:'身份证号码',validType:'IDCard'"></td>
            <th>营业部:</th>
            <td><input type="text" class="easyui-combobox select" name="owningBranchId" data-options="textField:'name',valueField:'id',prompt:'营业部',url:'${ctx}/pmsApi/findAllDepts'"></td>
            <th>处理人:</th>
            <td><input type="text" class="easyui-combobox select" name="handlerCode" data-options="textField:'staffName',valueField:'staffCode',prompt:'处理人',url:'${ctx}/reconsiderStaff/getAllOrderByName'"></td>
            <td><a onclick="clearForm(this)" class="easyui-linkbutton"><i class="fa fa-times" aria-hidden="true"></i>重&nbsp;置</a>&nbsp;&nbsp;<a class="easyui-linkbutton" onclick="reconsiderReformQuery()"><i class="fa fa-search" aria-hidden="true"></i>搜&nbsp;索</a>
            </td>
        </tr>
    </table>
</form>
<div class="h20"></div>
<div id="reconsiderReform_table_datagird_tool">
   <a class="easyui-linkbutton" data-options="plain:true" onclick="reconsiderReformDialog()"><i class="fa fa-plane" aria-hidden="true"></i>改&nbsp;派</a>&nbsp;&nbsp;
</div>
<table id="reconsiderReform_table_datagird"></table>
<!-- 复议改派弹出框 -->
<div id="reconsiderReform_dialog" class="display_none">
    <table class="table_ui WH100">
        <tr><th>处理人:</th><td><input id="reconsiderReform_opertor" calss="input"></td></tr>
    </table>
</div>
<script type="text/javascript" src="${ctx}/resources/js/apply/reconsiderReform.js"></script>
