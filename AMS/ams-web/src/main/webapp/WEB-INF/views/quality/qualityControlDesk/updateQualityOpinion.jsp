<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<form id="qualityOpinion_update_Form">
	<h2>质检结论 (修改页面):</h2>
	<input type="radio" name="checkResult" value="A" <c:if test="${checkResponse.checkResult=='A' }">checked="checked"</c:if>>
	无差错
	<div class="h20"></div>
	<div class="easyui-panel">
		<table class="table_ui W100">
			<tr>
				<td>
					<input type="checkbox" name="checkError" value="初审" <c:if test="${checkResponse.checkError=='初审' }">checked="checked"</c:if>><label class="hand">&nbsp;初审</label>
					<input type="radio" name="checkResult" value="B" <c:if test="${checkResponse.checkResult=='B' }">checked="checked"</c:if>>重大差错
					<input type="radio" name="checkResult" value="C" <c:if test="${checkResponse.checkResult=='C' }">checked="checked"</c:if>>一般差错
					<input type="radio" name="checkResult" value="D" <c:if test="${checkResponse.checkResult=='D' }">checked="checked"</c:if>>预警
					<input type="radio" name="checkResult" value="E" <c:if test="${checkResponse.checkResult=='E' }">checked="checked"</c:if>>建议
				</td>
			</tr>
			<tr>
				<td>差错代码: <select class="easyui-combobox select" name="errorCode" data-options="editable:false">
								<option value="c21" <c:if test="${checkResponse.errorCode=='c21' }">selected="selected"</c:if>>c21重大差错</option>
								<option value="c20" <c:if test="${checkResponse.errorCode=='c20' }">selected="selected"</c:if>>c20一般差错</option>
								<option value="345" <c:if test="${checkResponse.errorCode=='345' }">selected="selected"</c:if>>345测试差错</option>
							</select>
				</td>
			</tr>
			<tr>
				<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)"><font style="font-weight: bold">+&nbsp;</font></a>质检意见</td>
			</tr>
			<tr class="display_none">
				<td><textarea class="textarea" name="checkView" maxlength="200">${checkResponse.checkView}</textarea></td>
			</tr>
		</table>
		<table class="table_ui W100">
			<tr>
				<td>
					<input type="checkbox" name="checkError" value="终审" <c:if test="${checkResponse.checkError=='终审' }">checked="checked"</c:if>><label class="hand">&nbsp;终审</label>
					<input type="radio" name="checkResult">重大差错
					<input type="radio" name="checkResult">一般差错
					<input type="radio" name="checkResult">预警
					<input type="radio" name="checkResult">建议
					&nbsp;<a href="javascrpit:void(0);" onclick="addFinalAudit(this)">添加</a>
					&nbsp;<a href="javascrpit:void(0);" onclick="deleteTr(this)">删除</a>
				</td>
			</tr>
			<tr>
				<td>差错代码: <select class="easyui-combobox select" name="errorCode" data-options="editable:false">
								<option>c21重大差错</option>
								<option>c20一般差错</option>
							</select>
				</td>
			</tr>
			<tr>
				<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)"><font style="font-weight: bold">+&nbsp;</font></a>质检意见</td>
			</tr>
			<tr class="display_none">
				<td><textarea class="textarea" name="checkView" maxlength="200"></textarea></td>
			</tr>
		</table>
		<table class="table_ui W100">
			<tr>
				<td>
					<input type="checkbox" name="checkError" value="组长/主管/经理" <c:if test="${checkResponse.checkError=='组长/主管/经理' }">checked="checked"</c:if>><label class="hand">&nbsp;组长/主管/经理</label><input  class="easyui-textbox input">
				</td>
			</tr>
			<tr>
				<td>
					<input type="radio" name="checkResult">重大差错
					<input type="radio" name="checkResult">一般差错
					<input type="radio" name="checkResult">预警
					<input type="radio" name="checkResult">建议
				</td>
			</tr>
			<tr>
				<td>差错代码: <select class="easyui-combobox select" name="errorCode" data-options="editable:false">
								<option>c21重大差错</option>
								<option>c20一般差错</option>
							</select>
				</td>
			</tr>
			<tr>
				<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)"><font style="font-weight: bold">+&nbsp;</font></a>质检意见</td>
			</tr>
			<tr class="display_none">
				<td><textarea class="textarea" name="checkView" maxlength="200"></textarea></td>
			</tr>
		</table>
	</div>
	<div class="h20"></div>
	<table class="table_ui W90  center_m">
		<tr>
			<td><a class="easyui-linkbutton" onclick="completeQuality();">完成质检</a></td>
			<td><a class="easyui-linkbutton" onclick="completeQuality('pause');">暂存</a></td>
			<td><a class="easyui-linkbutton" onclick="reCheck();">申请复核</a></td>
			<%--<td><a class="easyui-linkbutton" onclick="javascript:$('#qualityOpinion_dialog').dialog('close');">返回</a></td>--%>
		</tr>
	</table>	
</form>
<script type="text/javascript">
	$(function() {
		$('input').iCheck({
			checkboxClass : 'icheckbox_square-blue',
			radioClass : 'iradio_square-blue',
			increaseArea : '20%' // optional
		});
	});
	//隐藏显示input输入框
	function diplayNextTr(obj) {
		if($(obj).html()=='<font style="font-weight: bold">+&nbsp;</font>'){
			$(obj).html('<font style="font-weight: bold">-&nbsp;</font>').parents("tr").next("tr").removeClass("display_none");
		}else{
			$(obj).html('<font style="font-weight: bold">+&nbsp;</font>').parents("tr").next("tr").addClass("display_none");
		}
	}
	// 添加终审
	function addFinalAudit(obj) {
		var html = $('<table class="table_ui W100">'
				+'<tr>'
				+'<td>'
				+'<input type="checkbox" name="checkError"><label class="hand">&nbsp;终审</label>'
				+'	<input type="radio" name="checkResult">重大差错'
				+'	<input type="radio" name="checkResult">一般差错'
				+'	<input type="radio" name="checkResult">预警'
				+'	<input type="radio" name="checkResult">建议'
				+'	&nbsp;<a href="javascrpit:void(0);" onclick="addFinalAudit(this)">添加</a>'
				+'	&nbsp;<a href="javascrpit:void(0);" onclick="deleteTr(this)">删除</a>'
				+'</td>'
				+'</tr>'
				+'<tr>'
				+'<td>差错代码: <select class="easyui-combobox select" name="errorCode" data-options="editable:false">'
				+'			<option>c21重大差错</option>'
				+'				<option>c20一般差错</option>'
				+'			</select>'
				+'</td>'
				+'</tr>'
				+'<tr>'
				+'<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)">+&nbsp;</a>质检意见</td>'
				+'</tr>'
				+'<tr class="display_none">'
				+'	<td><textarea class="textarea" name="checkView" maxlength="200"></textarea></td>'
				+'</tr>'
				+'</table>');
		$(obj).parents("table").after(html);
	}
	// 删除tr
	function deleteTr(obj) {
		$(obj).parents("table").remove();
	}
	
	//完成质检
	function completeQuality(temp){
		/*暂定只有一组控件，因为数据库表现在就一个字段,待确认?*/
		var checkResult = $('input[name="checkResult"]').filter(':checked').val();
		var checkError = $('input[name="checkError"]').filter(':checked').val();
		var errorCode = document.getElementsByName('errorCode')[0].value;
		var checkView = document.getElementsByName('checkView')[0].value;
		
		var api = ctx.rootPath()+'/qualityControlDesk/completeQuality';
		var params = {checkResult:checkResult,checkError:checkError,errorCode:errorCode,checkView:checkView};
		var callback = function(data){
			if(data.state=="updateSuccess"){
				$.info("提示",temp=='pause'?"暂存成功":"修改成功!","info",2000);
			}else{
				$.info("提示",temp=='pause'?"暂存失败":"修改失败!","info",2000);
			}
			$("#qualityOpinion_dialog").dialog("close");
		};
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
	    };
	    post(api, params, 'json', callback, error);
	}

    //申请复核
    function reCheck() {
        var api = ctx.rootPath()+'/qualityControlDesk/reCheck';
        $.ajax({
            url : api,
            dataType : 'json',
            method : 'post',
            success : function(data) {
                if (data) {
                    $.info("提示","申请成功");
                } else {
                    $.info("提示", "申请失败", "error");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                console.info('异常信息', textStatus + '  :  ' + errorThrown + '!','error');
            }
        });
    }
	
</script>
