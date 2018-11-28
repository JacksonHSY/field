<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.ymkj.sso.client.ShiroUtils" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主页</title>
	<jsp:include page="common/commonJS.jsp"></jsp:include>
</head>
<body class="default">
	<div id="system_choose_Dialog"  class="padding_20 display_none" >
		<table class="table_ui W100 " >
			<tr>
				<th>可用系统列表:</th>
				<td><input type="text" id="systemList"></td>
			</tr>
		</table>
	</div>
</body>
<script>
	var basePath ="${ctx}";
    var pmsUrl = '${pmsSystemVO.url}';
    var account = '${currentAccount}';
	
    $(function () {
        defaultIndex();
    });

    function defaultIndex() {
        var perms = '${perms}';
        var message = '';
        if (perms == 'none') {
        	message = '当前时间不在信审系统可用时间之内!';
        } else if(perms == 'timeout'){
        	message = '已过今天系统可用时间，系统自动登出!!';
        }
       	$.messager.alert({
            title: '友情提示',
            closable: false,
            msg: message,
            fn: function () {
            	showSystemList();
            }
        });
    }
    
 	// 获取系统列表
    function getSystems() {
        $.getJSON(pmsUrl + "/api/getSystems?callback=?", {account: account}, function (result) {
            if (result.success && result.data) {
            	console.log(JSON.stringify(result.data));
            	$("#systemList").combobox({
            		editable:false,
					valueField : 'url',
					textField : 'name',
					prompt:'选择系统',
					data : result.data,
				});
            }
        });
    }
 
 	var systemChooseDialog;
 	function showSystemList(){
 		getSystems();
 		$("#system_choose_Dialog").removeClass("display_none").dialog({
			title:"选择系统",
			modal:true,
			width:400,
			closable: false,
			buttons : [ {
				text : '确定',
				iconCls : 'fa fa-check',
				handler : function() {
					var url = $("#systemList").combobox("getValue");
					if ($("#systemList").combobox("getText") == '平台系统') {
                        url = url + "/index?command=redirect";
                    }
					window.location.href = url;
				}
			}, {
				text : '退出',
				iconCls : 'fa fa-reply',
				handler : function() {
					$("#system_choose_Dialog").dialog("close");
					location.href = basePath + '/logout';
				}
			} ]
		})
 	}
</script>
</html>