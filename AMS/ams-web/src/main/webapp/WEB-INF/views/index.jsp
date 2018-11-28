<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="session" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信审管理系统</title>
<jsp:include page="common/commonJS.jsp"></jsp:include>
</head>
<body class="easyui-layout">
    <div data-options="region:'north',split:false,collapsible:false,border:0">
        <jsp:include page="common/header.jsp"></jsp:include>
    </div>
    <div data-options="region:'west',split:false,collapsible:false,border:0,href:'${ctx}/leftMenu'" style="width: 200px; background-color: #e9f6fe;"></div>
    <div data-options="region:'center'">
        <div id="layout_container">
            <div title="首页"></div>
        </div>
    </div>
</body>
<script type="text/javascript">
    function topCenter(info){
        $.messager.show({
            title:'警告',
            msg:info,
            showType:'show',
            timeout: 0,
            height: 'auto',
            style:{
                right:'',
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
    }
    
    var remainingTimeWebSocket;

    // web socket连接
    function remainSocket(){
        var wsUrl = ctx.rootPath();
        wsUrl = wsUrl.replace("http", "ws");
        remainingTimeWebSocket = new WebSocket(wsUrl + "/socket/remainingTime/" + account);
        remainingTimeWebSocket.onmessage = function (e) {
            var data = JSON.parse(e.data);
            console.log(data);
            var currentLoginRole = data.currentLoginRole;
            var remainLoginTime = data.remainLoginTime;
            var remainRoleTime = data.remainRoleTime;
            var needRefresh = data.needRefresh;
            console.log(account + '登陆角色为:'+ currentLoginRole);
            console.log(account + '登陆剩余秒数为:'+ remainLoginTime);
            console.log(account + '刷新剩余秒数≈'+ remainRoleTime);
            
            //1.判断剩余登陆时间，不足三分钟提示(只提示一次)。没有时间就退出
            if(0 < remainLoginTime && remainLoginTime <= 180){
                var tip = localStorage.getItem(account + "-3minutesInfo");
                if(isEmpty(tip) || !isEmpty(tip) && ( (new Date().getTime() - tip) >= 0 ) ){
                    //针对登陆人----没有提示过，或者提示信息已经超时
                    topCenter("今天系统可用时间不足3分钟，请及时保存当前工作，届时系统将自动登出！");
                    localStorage.setItem(account + "-3minutesInfo", new Date().getTime() + remainLoginTime*1000);
                }
            } else if(remainLoginTime == 0){
                localStorage.removeItem(account + "-3minutesInfo");
                localStorage.removeItem(account + "-3minutesInfo-role");
                jDialog.closeAll();
                location.href = basePath + '/logout';
            }
            
            //2.判断当前登陆角色剩余时间，不足三分钟提示(只提示一次)。没有时间就刷新页面
            if(0 < remainRoleTime && remainRoleTime <= 180){
            	var tip = localStorage.getItem(account + "-3minutesInfo-role");
            	if(isEmpty(tip) || !isEmpty(tip) && ( (new Date().getTime() - tip) >= 0 ) ){
            		topCenter("当前角色可用时间不足3分钟，请及时保存当前工作，届时系统将自动刷新！");
                    localStorage.setItem(account + "-3minutesInfo-role", new Date().getTime() + remainRoleTime*1000);
            	}
            }
            setTimeout(function(){
            	if(needRefresh == 1){
                	localStorage.removeItem(account + "-3minutesInfo-role");
                    jDialog.closeAll();
                    window.location.reload();
                }
            },1000);
            
        };
        
        remainingTimeWebSocket.onclose = function (e) {
            remainingTimeWebSocket = -1;
        };
        remainingTimeWebSocket.onerror = function (e) {
            remainingTimeWebSocket = -1;
        };
        remainingTimeWebSocket.onopen = function (e) {
            console.log("当前登陆用户:" + account);
        }
    }

    setInterval(function(){
        getRemainingTime()
    },10000);

    function getRemainingTime() {
        remainingTimeWebSocket.send(account);
    }

    var $layoutContainer = $("#layout_container");
    $(function() {
        remainSocket();

        $layoutContainer.tabs({
            border : 0,
            fit : true,
            onLoad : function(panel) {
                var idStr = "";
                $(panel).find("div[id$='_dialog'],div[id$='_Dialog']").each(function() {
                    if ("" == idStr) {
                        idStr = $(this).attr("id");
                    } else {
                        idStr = $(this).attr("id") + "," + idStr;
                    }
                });
                $(panel).data("dialog_id", idStr);
            },
            onBeforeClose : function(title, index) {
                var idStr = $layoutContainer.tabs("getTab", index).data("dialog_id");
                if (idStr) {
                    $.each(idStr.split(","), function(ind, val) {
                        $("#" + val + ".panel-body").dialog("destroy");
                    });
                }
            }
        });

        /**质检抽检率设置提醒**/
        post(ctx.rootPath() + '/QualitySet/ifLeader',null,'json', function(data) {
            if(data["type"]=="SUCCESS" && data["firstMessage"]=="true" && data["messages"][1]==0){
                $.info("提示", "下周期的结束时间为空，请及时设置", "info", 2000);
            }
        });


        // 刷新或关闭主页面，同时关闭所有子弹窗
        window.addEventListener("beforeunload",cleanOpenedDialog);
    });

    /**
     * 刷新或关闭主页面，同时关闭所有子弹窗
     */
    function cleanOpenedDialog(e){
        jDialog.closeAll();
        var confirmationMessage = null;
        //(e || window.event).returnValue = confirmationMessage; //Gecko + IE
        return confirmationMessage;                            //Webkit, Safari, Chrome
    }

    /**添加tab**/
    var workflowHTMLWindow = null;
    function addTabs(title, url, queryParams) {
        if ("初审工作台" == title) {//刷新初审队列数
            refreshFirstTaskNumber();
        } else if ("终审工作" == title) {//刷新终审队列数
            refreshFinalTaskNumber();
        }
        if ($layoutContainer.tabs("exists", title)) {//获取当前tab
            $layoutContainer.tabs("select", title);
            var tab = $layoutContainer.tabs("getSelected");
            var idStr = tab.data("dialog_id");
            if (idStr) {
                $.each(idStr.split(","), function(ind, val) {
                    $("#" + val + ".panel-body").dialog("destroy");
                });
            }
            tab.panel('open').panel('refresh', '${ctx}' + url);
        } else {// 新建
            if ("工作流管理" == title) {
                workflowHTMLWindow = jDialog.open({url: ctx.rootPath() + url});
            } else {
                if (title.indexOf("-初审办理") > 0 || title.indexOf("-终审办理") > 0) {
                    var tabAll = $layoutContainer.tabs("tabs");
                    for (var i = 0; i < tabAll.length; i++) {
                        var panelTitel = tabAll[i].panel("options").title;
                        if (panelTitel.indexOf("-初审办理") > 0 || panelTitel.indexOf("-终审办理") > 0) {
                            $layoutContainer.tabs("close", panelTitel);
                        }
                    }
                }
                $layoutContainer.tabs("add", {
                    title : title,
                    closable : true,
                    queryParams : queryParams || {},
                    href : '${ctx}' + url,
                });
            }
        }
    }
    /**关闭tab并且选择刷新目标tab**/
    function closeAndSelectTabs(closeTitle, selectTitle) {
        $layoutContainer.tabs("close", closeTitle);
        if (selectTitle) {
            $layoutContainer.tabs("select", selectTitle);
            var tab = $layoutContainer.tabs("getSelected");
            tab.panel('open').panel('refresh');
        }
    }

</script>
</html>