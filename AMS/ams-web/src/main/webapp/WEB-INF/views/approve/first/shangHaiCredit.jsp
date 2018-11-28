<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
    td {
        text-align: center;
        color: #666666;
        font-size: 15px;
        padding: 0.2em 1em;
    }

    table {
        border-color: #CCD8EE;
        border-collapse: collapse;
    }

    th {
        font-weight: bold;
        height: 100%;
        font-size: 15px;
        color:  #2779AA;
        padding: 0.2em 1em;
    }
</style>
<div ms-if="undefined!= creditData['个人身份信息']">
<div align="center">
    <div style="border:1px">
        <br>
        <table border="0" cellspacing="0" width="90%">
            <tr style="background-color: lightskyblue">
                <th align="left">查询原因:</th>
                <td>{{creditData["信用报告头"]["查询原因"]}}</td>
                <th>报告编号:</th>
                <td>{{creditData["信用报告头"]["报告编号"]}}</td>
                <th>报告时间:</th>
                <td>{{creditData["信用报告头"]["报告时间"]}}</td>
            </tr>
        </table>
        <br>
    </div>
    <div title="个人信息" style="border:2px;border-top:0px">
        <table border="1"  cellspacing="0" width="90%" style="border-spacing: 0;">
            <th colspan="6" style="font-size: 18px;background-color: lightskyblue">个人基本信息</th>
            <tr>
                <th>姓名</th>
                <th>性别</th>
                <th>出生年月</th>
                <th>证件类型</th>
                <th colspan="2">身份证号码</th>
            </tr>
            <tr>
                <td>{{creditData["个人身份信息"]["姓名"]}}</td>
                <td>{{creditData["个人身份信息"]["性别"]}}</td>
                <td>{{creditData["个人身份信息"]["出生日期"]}}</td>
                <td>{{creditData["个人身份信息"]["证件类型"]}}</td>
                <td colspan="2">{{creditData["个人身份信息"]["证件号码"]}}</td>
            </tr>
            <tr>
                <th>婚姻状况</th>
                <th>手机号码</th>
                <th>住宅电话</th>
                <th>最高学历</th>
                <th>电子邮箱</th>
                <th>职称</th>
            </tr>
            <tr>
                <td>{{creditData["个人身份信息"]["婚姻状况"]["婚姻明细"]}}</td>
                <td>{{creditData["个人身份信息"]["手机号码"]["手机号码明细"]}}</td>
                <td>{{creditData["个人身份信息"]["住宅电话"]["住宅电话明细"]}}</td>
                <td>{{creditData["个人身份信息"]["最高学历"]["学历明细"]}}</td>
                <td>{{creditData["个人身份信息"]["电子邮箱"]["电子邮箱明细"]}}</td>
                <td>{{creditData["个人身份信息"]["职称"]["职称明细"]}}</td>
            </tr>
            <tr>
                <th>信息获取时间</th><th>信息获取时间</th><th>信息获取时间</th><th>信息获取时间</th><th>信息获取时间</th><th>信息获取时间</th>
            </tr>
            <tr>
                <td>{{creditData["个人身份信息"]["婚姻状况"]["信息获取日期"]}}</td>
                <td>{{creditData["个人身份信息"]["手机号码"]["信息获取日期"]}}</td>
                <td>{{creditData["个人身份信息"]["住宅电话"]["信息获取日期"]}}</td>
                <td>{{creditData["个人身份信息"]["最高学历"]["信息获取日期"]}}</td>
                <td>{{creditData["个人身份信息"]["电子邮箱"]["信息获取日期"]}}</td>
                <td>{{creditData["个人身份信息"]["职称"]["信息获取日期"]}}</td>
            </tr>
        </table>
        <br>

        <table border="1" cellpadding="0" width="90%">
            <th colspan="6" style="font-size: 18px;background-color: lightskyblue">配偶信息</th>
            <tr ms-if="checkSpouse(creditData)">
                <th>姓名</th>
                <th>性别</th>
                <th>出生日期</th>
                <th>证件类型</th>
                <th>证件号码</th>
            </tr>
            <tr ms-if="checkSpouse(creditData)">
                <td>{{creditData["个人身份信息"]["配偶姓名"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶性别"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶出生日期"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶证件类型"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶证件号码"]}}</td>
            </tr>
            <tr ms-if="checkSpouse(creditData)">
                <th>联系电话</th>
                <th>信息获取时间</th>
                <th colspan="2">工作单位</th>
                <th>信息获取时间</th>
            </tr>
            <tr ms-if="checkSpouse(creditData)">
                <td>{{creditData["个人身份信息"]["配偶联系电话"]["配偶联系电话明细"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶联系电话"]["信息获取日期"]}}</td>
                <td colspan="2">{{creditData["个人身份信息"]["配偶工作单位"]["配偶工作单位明细"]}}</td>
                <td>{{creditData["个人身份信息"]["配偶工作单位"]["信息获取日期"]}}</td>
            </tr>
            <tr ms-if="!checkSpouse(creditData)">
                <td>无记录</td>
            </tr>
        </table>
        <br>

        <table border="1" cellspacing="0" width="90%">
            <th colspan="6" style="font-size: 18px;background-color: lightskyblue">居住信息</th>
            <tr>
                <th>序号</th>
                <th colspan="3">居住地址</th>
                <th>信息获取时间</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['个人身份信息']['地址'])" ms-if="creditData['个人身份信息'] !=null && getArrayByType(creditData['个人身份信息']['地址']).length>0">
                <td>{{$index + 1}}</td>
                <td colspan="3">{{item['地址明细']}}</td>
                <td>{{item['信息获取日期']}}</td>
            </tr>
            <tr ms-if=" null == creditData['个人身份信息'] || getArrayByType(creditData['个人身份信息']['地址']).length==0">
                <td colspan="6">无记录</td>
            </tr>
        </table>
    </div>

    <div>
        <br>
        <table border="1" cellspacing="0" width="90%">
            <th colspan="8" style="font-size: 18px;background-color: lightskyblue">职业信息</th>
            <tr>
                <th width="8%">编号</th>
                <th colspan="3">工作单位</th>
                <th colspan="2">职业</th>
                <th width="21.5%">信息获取时间</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['个人身份信息']['工作单位'])" ms-if="getArrayByType(creditData['个人身份信息']['工作单位']).length>0">
                <td>{{item['工作序号'].replace(".","")}}</td>
                <td colspan="3">{{item['工作明细']}}</td>
                <td colspan="2">{{item['职业']}}</td>
                <td>{{item['信息获取日期']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['个人身份信息']) || getArrayByType(creditData['个人身份信息']['工作单位']).length==0">
                <td colspan="8">无记录</td>
            </tr>
        </table>
    </div>

    <div style="margin-top: 14px;">
        <table border="1" cellpadding="6" cellspacing="0" width="90%">
            <th colspan="6" style="font-size: 18px;background-color: lightskyblue">联系人信息</th>
            <tr>
                <th width="8%">编号</th>
                <th>类型</th>
                <th>姓名</th>
                <th>关系</th>
                <th>联系电话</th>
                <th width="21.5%">信息获取时间</th>
            </tr>
            <tr ms-for="($index,item) in getContactList(creditData['个人身份信息'])" ms-if="getContactList(creditData['个人身份信息']).length>0">
                <td>{{$index+1}}</td>
                <td>{{ item.type }}</td>
                <td>{{item['联系人姓名']}}</td>
                <td>{{item['联系人关系']}}</td>
                <td>{{item['联系电话']}}</td>
                <td>{{item['信息获取日期']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['个人身份信息']) || getContactList(creditData['个人身份信息']).length==0">
                <td colspan="6">无记录</td>
            </tr>
        </table>
    </div>

    <div>
        <br>
        <table title="贷款申请信息" style="border-top: 0" border="1" cellspacing="0" width="90%">
            <th colspan="10" style="font-size: 18px;background-color: lightskyblue">贷款申请信息</th>
            <tr>
                <th width="8%">编号</th>
                <th>申请机构</th>
                <th>申请时间</th>
                <th>申请额度</th>
                <th>申请月数</th>
                <th>申请类型</th>
                <th>申请状态</th>
                <th>信息获取日期</th>
            </tr>
            <%--ms-if="getArrayByType(creditData['贷款申请信息']['贷款申请信息记录'])"--%>
            <tr ms-for="($index,item) in getArrayByType(creditData['贷款申请信息']['贷款申请信息记录'])" ms-if="getArrayByType(creditData['贷款申请信息']['贷款申请信息记录']).length>0 && 'undefined'!=typeof (creditData['贷款申请信息']['贷款申请信息记录'])">
                <td>{{$index+1}}</td>
                <td>{{item['申请机构']}}</td>
                <td>{{item['申请时间']}}</td>
                <td>{{item['申请金额']}}</td>
                <td>{{item['申请月数']}}</td>
                <td>{{item['申请类型']}}</td>
                <td>{{item['申请状态']}}</td>
                <td>{{item['信息获取日期']}}</td>
            </tr>
            <tr ms-if=" 'undefined'==typeof (creditData['贷款申请信息']['贷款申请信息记录']) || 'undefined'== typeof (creditData['贷款申请信息']) || getArrayByType(creditData['贷款申请信息']['贷款申请信息记录']).length==0">
                <td colspan="10">无记录</td>
            </tr>
    </div>
    <div title="贷款交易信息">
        <br>
        <table style="border-top: 0" border="1" cellspacing="0" width="90%">
            <th colspan="10" style="font-size: 18px;background-color: lightskyblue">贷款交易信息</th>
            <tr>
                <th width="8%">编号</th>
                <th>贷款笔数</th>
                <th>首贷日</th>
                <th>最大授信额度</th>
                <th>贷款总额</th>
                <th>贷款余额</th>
                <th>协定月还款</th>
                <th>当前逾期总额</th>
                <th>最高逾期金额</th>
                <th>最高逾期期数</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['贷款交易信息']['信息概要'])" ms-if="getArrayByType(creditData['贷款交易信息']['信息概要']).length>0">
                <td>{{$index+1}}</td>
                <td>{{item['贷款笔数']}}</td>
                <td>{{item['首贷日']}}</td>
                <td>{{item['最大授信额度']}}</td>
                <td>{{item['贷款总额']}}</td>
                <td>{{item['贷款余额']}}</td>
                <td>{{item['协定月还款']}}</td>
                <td>{{item['当前逾期总额']}}</td>
                <td>{{item['最高逾期金额']}}</td>
                <td>{{item['最高逾期期数']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['贷款交易信息']) ||  getArrayByType(creditData['贷款交易信息']['信息概要']).length==0">
                <td colspan="10">无记录</td>
            </tr>
            </table>
            <table style="border-top: 0" border="1" cellspacing="0" width="90%"  ms-for="($index,item) in getArrayByType(creditData['贷款交易信息']['贷款'])" ms-if="getArrayByType(creditData['贷款交易信息']['贷款']).length>0">
            <th colspan="24" style="font-size: 14px;background-color: lightskyblue">贷款项目{{$index+1}}</th>
            <tr>
                <th colspan="3">贷款项目</th><th colspan="3">机构名称</th><th colspan="3">授信额度</th><th colspan="4">担保方式</th><th colspan="3">开户日期</th><th colspan="3">币种</th><th colspan="5">发生地</th>
            </tr>
            <tr>
                <td colspan="3">{{item['贷款项目']}}</td><td colspan="3">{{item['机构名称']}}</td><td colspan="3">{{item['授信额度']}}</td>
                <td colspan="4">{{item['担保方式']}}</td><td colspan="3">{{item['开户日期']}}</td><td colspan="3">{{item['币种']}}</td><td colspan="5">{{item['发生地']}}</td>
            </tr>
            <tr>
                <th colspan="3">共享授信额度</th><th colspan="3">最大负债额</th><th colspan="3">还款频率</th><th colspan="3">期末贷款余额</th><th colspan="4">剩余还款月数</th><th colspan="4">本月应还款日期</th><th colspan="4">本月应还款金额</th>
            </tr>
                <td colspan="3">{{item['共享授信额度']}}</td><td colspan="3">{{item['最大负债额']}}</td><td colspan="3">{{item['还款频率']}}</td><td colspan="3">{{item['期末贷款余额']}}</td>
                <td colspan="4">{{item['剩余还款月数']}}</td><td colspan="4">{{item['本月应还款日期']}}</td><td colspan="4">{{item['本月应还款金额']}}</td>
            <tr>
            <tr>
                <th colspan="3">帐户状态</th><th colspan="3">实际还款日期</th><th colspan="3">实际还款金额</th><th colspan="3">当前逾期总额</th><th colspan="4">当前逾期期数</th><th colspan="4">累计逾期期数</th>
                <th colspan="4">最高逾期期数</th>
            </tr>
            <tr>
                <td colspan="3">{{item['帐户状态']}}</td><td colspan="3">{{item['实际还款日期']}}</td><td colspan="3">{{item['实际还款金额']}}</td><td colspan="3">{{item['当前逾期总额']}}</td>
                <td colspan="4">{{item['当前逾期期数']}}</td><td colspan="4">{{item['累计逾期期数']}}</td><td colspan="4">{{item['最高逾期期数']}}</td>
            </tr>
                <th colspan="5">逾期31-60天未归还贷款本金</th><th colspan="5">逾期61-90天未归还贷款本金</th><th colspan="5">逾期91-180天未归还贷款本金</th><th colspan="5">逾期180天以上未归还贷款本金</th><th colspan="4">信息获取日期</th>
            </tr>
                <tr>
                    <td colspan="5">{{item['逾期31-60天未归还贷款本金']}}</td><td colspan="5">{{item['逾期61-90天未归还贷款本金']}}</td>
                    <td colspan="5">{{item['逾期91-180天未归还贷款本金']}}</td><td colspan="5">{{item['逾期180天以上未归还贷款本金']}}</td><td colspan="4">{{item['信息获取日期']}}</td>
                </tr>
                <tr>
                    <th colspan="24">24月内各月还款状况</th>
                </tr>
                <tr ms-if="item['二十四月内各月还款状况'].substring(0,1).length>0">
                    <td width="3.125%">{{item['二十四月内各月还款状况'].substring(0,1)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(1,2)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(2,3)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(3,4)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(4,5)}}</td>
                    <td width="3.125%">{{item['二十四月内各月还款状况'].substring(5,6)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(6,7)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(7,8)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(8,9)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(9,10)}}</td>
                    <td width="3.125%">{{item['二十四月内各月还款状况'].substring(10,11)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(11,12)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(12,13)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(13,14)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(14,15)}}</td>
                    <td width="3.125%">{{item['二十四月内各月还款状况'].substring(15,16)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(16,17)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(17,18)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(18,19)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(19,20)}}</td>
                    <td width="3.125%">{{item['二十四月内各月还款状况'].substring(20,21)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(21,22)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(22,23)}}</td><td width="3.125%">{{item['二十四月内各月还款状况'].substring(23,24)}}</td>
                </tr>
        </table>
        <%--<table style="border-top: 0" border="1" cellspacing="0" width="90%" ms-if="'undefined'== typeof (creditData['贷款交易信息']) || getArrayByType(creditData['贷款交易信息']['贷款']).length==0">--%>
            <%--<th width="90%"> 无记录</th>--%>
        <%--</table>--%>
    </div>
    <div>
        <br>
        <table style="border-top: 0" border="1" cellspacing="0" width="90%">
            <th colspan="6" style="font-size: 18px;background-color: lightskyblue">担保信息</th>
            <tr>
                <th width="8%">编号</th>
                <th>担保项目</th>
                <th>担保日期</th>
                <th>担保金额</th>
                <th>担保关系</th>
                <th>信息获取日期</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['为他人担保信息']['担保信息记录'])" ms-if="getArrayByType(creditData['为他人担保信息']['担保信息记录']).length>0 && 'undefined'!= typeof (creditData['为他人担保信息']['担保信息记录'])">
                <td>{{$index+1}}</td>
                <td>{{item['担保项目']}}</td>
                <td>{{item['担保日期']}}</td>
                <td>{{item['担保金额']}}</td>
                <td>{{item['担保关系']}}</td>
                <td>{{item['信息获取日期']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['为他人担保信息']['担保信息记录']) || 'undefined'== typeof (creditData['为他人担保信息']) || getArrayByType(creditData['为他人担保信息']['担保信息记录']).length==0">
                <td colspan="6">无记录</td>
            </tr>
        </table>
    </div>
    <div style="padding: 0px" title="查询记录">
        <br>
        <table style="border-top: 0" border="1" cellspacing="0" width="90%">
            <tr>
                <th colspan="4" style="font-size: 18px;background-color: lightskyblue">查询记录</th>
            </tr>
            <tr>
                <th width="8%">编号</th>
                <th>查询日期</th>
                <th>查询者类型</th>
                <th>查询原因</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['查询信息']['查询记录'])" ms-if="getArrayByType(creditData['查询信息']['查询记录']).length>0 && 'undefined'!=typeof (creditData['查询信息']['查询记录'])">
                <td>{{$index+1}}</td>
                <td>{{item['查询日期']}}</td>
                <td>{{item['查询者类型']}}</td>
                <td>{{item['查询原因']}}</td>
            </tr>
            <tr ms-if="'undefined'==typeof (creditData['查询信息']['查询记录']) || 'undefined'== typeof (creditData['查询信息']) || getArrayByType(creditData['查询信息']['查询记录']).length==0">
                <td colspan="4">无记录</td>
            </tr>
        </table>
    </div>
    <div title="个人声明">
        <br>
        <table style="border-top: 0" border="1" cellspacing="0" width="90%">
            <th colspan="4" style="font-size: 18px;background-color: lightskyblue">个人声明</th>
            <tr>
                <th width="8%">编号</th>
                <th>个人声明内容</th>
                <th>声明日期</th>
            </tr>
            <tr  ms-for="($index,item) in getArrayByType(creditData['个人声明信息']['个人声明'])" ms-if="getArrayByType(creditData['个人声明信息']['个人声明']).length>0 && 'undefined'!=typeof (creditData['个人声明信息']['个人声明'])">
                <td>{{$index+1}}</td>
                <td>{{item['个人声明内容']}}</td>
                <td>{{item['声明日期']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['个人声明信息']) || 'undefined'==typeof (creditData['个人声明信息']['个人声明']) || getArrayByType(creditData['个人声明信息']['个人声明']).length==0">
                <td colspan="3">无记录</td>
            </tr>
        </table>
    </div>
    <div title="资信提示">
        <br>
        <table style="border-top: 0" border="1" cellspacing="0" width="90%">
            <th colspan="4" style="font-size: 18px;background-color: lightskyblue">资信提示</th>
            <tr>
                <th width="8%">编号</th>
                <th>项目</th>
                <th>提示内容</th>
                <th>提示时间</th>
            </tr>
            <tr ms-for="($index,item) in getArrayByType(creditData['资信提示信息']['资信提示'])" ms-if="getArrayByType(creditData['资信提示信息']['资信提示']).length>0">
                <td>{{$index+1}}</td>
                <td>{{item['项目']}}</td>
                <td>{{item['提示内容']}}</td>
                <td>{{item['提示时间']}}</td>
            </tr>
            <tr ms-if="'undefined'== typeof (creditData['资信提示信息']) || 'undefined'== typeof (creditData['资信提示信息']['资信提示']) || getArrayByType(creditData['资信提示信息']['资信提示']).length==0">
                <td colspan="4">无记录</td>
            </tr>
        </table>
    </div>
</div>
<br>
</div>
<div ms-if="undefined == creditData['个人身份信息']">
    <table style="border-top: 0;border-bottom: none" border="1" cellspacing="0" width="100%">
        <th align="center">无记录</th>
    </table>
</div>
