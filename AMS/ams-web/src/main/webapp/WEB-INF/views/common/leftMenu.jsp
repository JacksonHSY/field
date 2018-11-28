<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--  left menu -->
<div class="easyui-accordion" data-options="border:0"></div>
<script type="text/javascript">
	//加载菜单
	var html = '';
	$(function() {
		$.ajax({
			type : "POST",
			url : '${ctx}/findMenuTree',
			dataType : 'json',
			async : false, // 设置同步方式
			cache : false,
			success : function(result) {
				//菜单元素建立
				if (result.hasChildren) {
					html = '';
					$("div.easyui-accordion").empty();
					//回调遍历
					writeMenusElement(result.children);
					$("div.easyui-accordion").append(html);
				}
			},
			error : function(data) {
				console.log(JSON.stringify(data));
				$.info("警告", data.responseJSON.repMsg, "warning");
			}
		});
		$(".easyui-accordion li").bind("click", function() {
			$(".selected_menu_li").removeClass("selected_menu_li");
			$(this).addClass("selected_menu_li");
		});
	});
	//回调遍历
	function writeMenusElement(menuTreeList) {
		for (var i = 0; i < menuTreeList.length; i++) {
			var menuNode = menuTreeList[i];
			if (menuNode.hasChildren) {//上级菜单
				html += '<div title="'+menuNode.name+'">';
				var menulist = menuNode.children;
				writeMenusElement(menulist);
				html += '</div>';
			} else {//最低级菜单
				if (i == 0) {
					html += '<ul class="menu_ui">';
				}
				var taskHtml = "";
				if ("初审工作台" == menuNode.name) {
					taskHtml = '<span id="first_taskNumber_span" class="taskNumberTooltip">' + getUserTaskNumber('apply-check') + '</span>'
				} else if ("终审工作台" == menuNode.name) {
					taskHtml = '<span id="final_taskNumber_span" class="taskNumberTooltip">' + getUserTaskNumber('applyinfo-finalaudit') + '</span>'
				}
				html += '<li><a href="javaScript:void(0);" onclick=addTabs("' + menuNode.name + '","' + menuNode.url + '")><i class="fa fa-address-book-o" aria-hidden="true"></i> ' + menuNode.name + taskHtml + '</a></li>';
				//html += '<li onclick=addTabs("'+menuNode.name+'","'+menuNode.url+'")><i class="fa fa-address-book-o" aria-hidden="true"></i> '+menuNode.name+'</li>';
				if (i == menuTreeList.length - 1) {
					html += '</ul>';
				}
			}
		}
	}
	//返回初审工作台或终审工作台待办任务数
	function getUserTaskNumber(taskDefId) {
		var taskNumber = 0;
		$.ajax({
			url : ctx.rootPath() + "/bmsBasiceInfo/getUserTaskNumber/" + taskDefId,
			type : "POST",
			async : false,
			success : function(data) {
				if (data.success) {
					taskNumber = data.data;
				}
			}
		});
		return taskNumber;
	}
	//刷新初审任务数
	function refreshFirstTaskNumber() {
		$("#first_taskNumber_span").html(getUserTaskNumber("apply-check"));
	}
	//刷新终审审任务数
	function refreshFinalTaskNumber() {
		$("#final_taskNumber_span").html(getUserTaskNumber("applyinfo-finalaudit"));
	}
</script>