$(function() {
	dictionary_InitDatagrid();
});
function dictionary_InitDatagrid() {
	$("#dictionary_datagrid").datagrid({
		url : ctx.rootPath() + "/systemLog/pageList",
		striped : true,
		singleSelect : false,
		idField : 'id',
		pagination : true,
		fitColumns : true,
		scrollbarSize : 0,
		toolbar : '#dictionary_toolBarBtn',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'operation',
			title : '字典编码',
			width : 200,
		}, {
			field : 'requestContent',
			title : '字典名称',
			width : 155,
		}, {
			field : 'responseContent',
			title : '顺序号',
			width : 155,
		}, {
			field : 'ip',
			title : '限制在提交天数',
			width : 155,
		}, {
			field : 'ip',
			title : '最后更新人',
			width : 155,
		}, {
			field : 'remark',
			title : '最后更新时间',
			width : 155,
		}, {
			field : 'createdDate',
			title : '状态',
			width : 169,
		}, {
			field : 'action',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick="Test()"><i class="fa fa-pencil" aria-hidden="true"></i>修改</a>';
				action = action + '&nbsp;|&nbsp;<a href="javaScript:void(0);"><i class="fa fa-pencil" aria-hidden="true"></i>删除</a>';
				return action;
			}
		} ] ]
	});
}

function Test() {
	console.info("1111");
}

var treeData = [ {
	id : 1,
	text : "业务字典",
	children : [ {
		id : 12,
		text : "行动类型",
		attributes : {
			url : ""
		}
	}, {
		id : 13,
		text : "资产模块列表顺序",
		attributes : {
			url : ""
		}
	} ]
}, {
	id : 2,
	text : "系统字典",
	state : "closed",
	children : [ {
		id : 21,
		text : "审计日志字典",
		children : [ {
			id : 211,
			text : "操作类型",
			attributes : {
				url : ""
			}
		} ]
	}, {
		id : 22,
		text : "使用状态",
		attributes : {
			url : ""
		}
	} ]
}, {
	id : 3,
	text : "当前状态",
	attributes : {
		url : ""
	}
} ];

$("#dictionary_tree").tree({
	data : treeData,
	line : true,
	onClick : function(node) {
		console.info(node);
		dictionary_treeLoad(node.id);
	}

});
function dictionary_treeLoad(id) {
	var params = {
		id : id
	};
	$("#dictionary_datagrid").datagrid({
		url : ctx.rootPath() + "/systemLog/pageListById",
		queryParams : params
	});
	// $("#dictionary_datagrid").datagrid('reload');
}
//新建
function dictionaryQuery(){
	$("#dictionary_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : '添加字典',
		width : 800,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-arrow-up',
			handler : function() {

			}
		}, {
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function() {
				$("#dictionary_AddOrUpdate_dialog").dialog("close");
			}
		} ]
	});
}
