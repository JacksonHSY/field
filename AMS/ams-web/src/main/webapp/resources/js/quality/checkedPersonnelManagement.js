$( function () { 
	// 实例化树形菜单
	post(ctx.rootPath() + '/checkedPersonnelManagement/getPersonTree', null, "JSON", function(data) {
		$("#checkedPersonnelManagementWest_tree").tree({
			   lines :  true,
			   checkbox : true,
			   data : filterTreeNodeIcon(data),
			   onClick :  function (node) {
				   $("#checkedUser").val(node.id);
				   $("#checkedUserName").val(node.text);
			   }
			});
	});

   	// 延迟加载datagrid
	setTimeout(function () {
		checkedPersonnelManagementCenterDatagrid("#checkedPersonnelManagementCenter_datagrid");
    }, 100);

});
//遍历后如果根节点为组织机构，则图标变更
function filterTreeNodeIcon(data) {
	if(isNotNull(data)) {
        $.each(data, function (index, value) {
            if (isNotNull(value.orgCode) && value.children.length == 0) {
                value.iconCls = 'icon-folder';
            }
            if (value.children.length > 0) {
                filterTreeNodeIcon(value.children);
            }
        });
    }
	return data;
}
//查询
$("#checkedPersonnelManagementCenter_Query").click(
	    function checkedPersonnelManagementCenter_Query(){
	    	//textbox去空格
	    	$("#checkedPersonnelManagement_form").find('.easyui-textbox').each(function (index, element ) {
				var $self = $(element);
				$($self).textbox('setValue', $.trim($self.textbox('getValue')));
			});
	    	var queryParams = $("#checkedPersonnelManagement_form").serializeObject();
			$("#checkedPersonnelManagementCenter_datagrid").datagrid('load',queryParams);
	    }
	);

//重置
$("#checkedPersonnelManagement_Reset").click(
	function checkedPersonnelManagement_Reset(){
		$('#checkedPersonnelManagement_form').form('clear');
	}
);

function checkedPersonnelManagementCenterDatagrid(id) {
	$(id).datagrid({
		url : ctx.rootPath()+"/checkedPersonnelManagement/pageList",
		striped : true,
		singleSelect : false,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar:'#checkedPersonnelManagementCenter_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'checkedUser',
			title : '被检人员ID',
			width : 285,
		}, {
			field : 'checkedUserName',
			title : '被检人员姓名',
			width : 285,
		}, {
			field : 'ifRegular',
			title : '是否质检',
			width : 285,
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
function checkSelectNodeHaveUser(data){
	var userLeafNum = 0;//人员叶子节点数目
	$.each(data,function(index,value){
		if(value.children.length == 0){//叶子节点
			if(!isNotNull(value.orgCode)){//人员叶子节点
				userLeafNum += 1;
			}
		}
	});
	return userLeafNum == 0;
}
//添加
$("#checkedPersonnelManagementWest_add").click(
	function checkedPersonnelManagementWest_add(){
		var users = $('#checkedPersonnelManagementWest_tree').tree('getChecked');
		var getJsonArray=JSON.stringify(users);
		if (users==null || users=='' || checkSelectNodeHaveUser(users)) {
           	$.messager.alert('提示信息',  "请选择要添加的被检人员！", 'info');
           	return;
        }
		var api = ctx.rootPath()+'/checkedPersonnelManagement/save';
		var params = {ifRegular:$("#ifRegular").combobox('getValue'),users : getJsonArray};
		var callback = function(data){
			  if(data.type=="SUCCESS"){
				  $.info("提示","新增成功!","info",1000);
			  }else{
				  $.info("提示","新增失败!","info",1000);
			  }
			  $('#checkedPersonnelManagementCenter_datagrid').datagrid('reload');  
		};
		var error = function (XMLHttpRequest, textStatus, errorThrown) {
			console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
		};
		post(api, params, 'json',callback, error);
	}
);

//删除
$("#checkedPersonnelManagement_Delete").click(
	function checkedPersonnelManagement_Delete(){
		var rowInfo = $("#checkedPersonnelManagementCenter_datagrid").datagrid('getSelected');
		if (!rowInfo) {
           	$.messager.alert('提示信息',  "请至少选中一行！", 'info');
           	return;
        }
		var rows = $('#checkedPersonnelManagementCenter_datagrid').datagrid('getSelections');  
        var ids = "";
        for(var i=0; i<rows.length; i++){  
            ids+=rows[i].id+",";
        }  
        ids = ids.substr(0, ids.length - 1);
		var api = ctx.rootPath()+'/checkedPersonnelManagement/deleteBatch';
		var params = {
				"ids" : ids
		};
		var callback = function(data){
			  if(data.type=='SUCCESS'){
				//$.messager.alert('提示信息',  "请选择要添加的被检人员！", 'info');
				  $.info("提示","删除成功!","info",2000);
			  }else{
				  $.info("提示","删除失败!","info",2000);
			  }
			  $('#checkedPersonnelManagementCenter_datagrid').datagrid('clearChecked');
			  $('#checkedPersonnelManagementCenter_datagrid').datagrid('reload');  
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