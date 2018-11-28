$( function () { 
    // 实例化树形菜单 
	post(ctx.rootPath() + '/qualityPersonnelManagement/getQualityPerson', null, "JSON", function(data) {
		$("#qualityPersonnelManagementWest_tree").tree({
			lines : true,
			cascadeCheck : true,
			checkbox : true,
			data : filterTreeNodeIcon(data),
			onClick : function(node) {
				$("#checkUser").val(node.id);
				$("#checkUserName").val(node.text);
			}
		});
	});
   qualityPersonnelManagementDatagrid();
   window.setTimeout(function () {
   $('.inputmask').find('.textbox-text').inputmask('Regex', {regex:"^[0-9]|[1-9][0-9]|[1-9][0-9][0-9]$"})},1000);
   
});
// 遍历后如果根节点为组织机构，则图标变更
function filterTreeNodeIcon(data) {
	$.each(data, function(index, value) {
		if (isNotNull(value.orgCode) && value.children.length == 0) {
			value.iconCls = 'icon-folder';
		}
		if (value.children.length > 0) {
			filterTreeNodeIcon(value.children);
		}
	});
	return data;
}
//查询
$("#qualityPersonnelManagement_Query").click(
    function qualityPersonnelManagement_Query(){
    	//textbox去空格
    	$("#qualityPersonnelManagement_form").find('.easyui-textbox').each(function (index, element ) {
			var $self = $(element);
			$($self).textbox('setValue', $.trim($self.textbox('getValue')));
		});
		$("#qualityPersonnelManagement_datagrid").datagrid('load',$("#qualityPersonnelManagement_form").serializeObject());
    }
);

//重置
$("#qualityPersonnelManagement_Reset").click(
	function qualityPersonnelManagement_Reset(){
		$('#qualityPersonnelManagement_form').form('clear');
	}
);

function qualityPersonnelManagementDatagrid() {
	$("#qualityPersonnelManagement_datagrid").datagrid({
		url : ctx.rootPath()+"/qualityPersonnelManagement/pageList",
		striped : true,
		singleSelect : false,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar:'#qualityPersonnelManagement_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true,
		}, {
			field : 'checkUser',
			title : '质检员ID',
			width : 5,
		}, {
			field : 'checkUserName',
			title : '质检员姓名',
			width : 5,
		}, {
			field : 'maxTaskNum',
			title : '最大队列值',
			width : 5,
		}, {
			field : 'ifAccept',
			title : '是否接单',
			width : 5,
			formatter : function(value) {
				if (value == '0')
					return '是';
				else
					return '否';
			}
		},{
			field : 'ifReback',
			title : '是否反馈接单',
			formatter : function(value) {
				if (value == '0')
					return '是';
				else
					return '否';
			}
		},{
			field : 'ifApplyCheck',
			title : '是否申请复核接单',
			formatter : function(value) {
				if (value == '0')
					return '是';
				else
					return '否';
			}
		}] ]
	});
}
/**
 * 判断选中的叶子节点是否含有人员
 * */
function checkSelectNodeHaveUser(data) {
	var userLeafNum = 0;// 人员叶子节点数目
	$.each(data, function(index, value) {
		if (value.children.length == 0) {// 叶子节点
			if (!isNotNull(value.orgCode)) {// 人员叶子节点
				userLeafNum += 1;
			}
		}
	});
	return userLeafNum == 0;
}
//设置质检人员
function addPerson(obj){
    var $form = $(obj).parents("form");
	if(!$form.form("validate")){	// 表单验证
		return;
	}

	// 获取机构树被选中的质检人员
	var checkedUsers = $('#qualityPersonnelManagementWest_tree').tree('getChecked');
	//var getJsonArray=JSON.stringify(qualityUsers);
	if (checkedUsers == null || checkSelectNodeHaveUser(checkedUsers)) {
		$.messager.alert('提示信息',  "请选择要添加的被检人员！", 'warning');
		return;
	}

    var params = {
        checkedUsers : JSON.stringify(checkedUsers),
		maxTaskNum: $("#maxTaskNum").val(),
        ifAccept : $("#ifAccept").combobox('getValue'),
        ifReback : $("#ifReback").combobox('getValue'),
        ifApplyCheck : $("#ifApplyCheck").combobox('getValue')
    };
    var tab = $("#layout_container").tabs("getSelected");
    tab.panel('refresh', ctx.rootPath() + '/qualityPersonnelManagement/qualityPersonnelManagement');
    
	post(ctx.rootPath() + '/qualityPersonnelManagement/save', params, 'json', function(data){
        if(data.type == 'SUCCESS'){
            $.info("提示","添加成功", "info", 1000);
        }else{
            $.info("提示","添加失败", "warning", 1000);
        }
        $('#qualityPersonnelManagement_datagrid').datagrid('reload');	// 刷新右侧 data_grid

    }, function (XMLHttpRequest, textStatus, errorThrown) {
        console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');

    });
}

//删除
$("#qualityPersonnelManagement_Delete").click(
	function qualityPersonnelManagement_Delete(){
		var rowInfo = $("#qualityPersonnelManagement_datagrid").datagrid('getSelected');
		if (!rowInfo) {
           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
           	return;
        }
		var rows = $('#qualityPersonnelManagement_datagrid').datagrid('getSelections');  
        var ids = "";
        for(var i=0; i<rows.length; i++){  
            ids+=rows[i].id+",";
        }  
        ids = ids.substr(0, ids.length - 1);
		var api = ctx.rootPath()+'/qualityPersonnelManagement/deleteBatch';
		var params = {"ids" : ids };
		var callback = function(data){
			  if(data.type=='SUCCESS'){
				  $.info("提示","删除成功!","info", 2000);
			  }else{
				  $.info("提示",data.firstMessage,"info", 2000);
			  }
			  $('#qualityPersonnelManagement_datagrid').datagrid('clearChecked');
			  $('#qualityPersonnelManagement_datagrid').datagrid('reload');  
		};
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		};
		$.messager.confirm("提示","确认删除吗？",function(isOrNo){
			  if(isOrNo){
				  post(api, params, 'json',callback, error);
			  }
		});
	}
);