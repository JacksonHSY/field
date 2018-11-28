$(function() {
	// 初始确认复核datagrid
	initReviewConfirmation_datagridDatagrid();
});
/**
 * 确认复核datagrid
 * 
 * @Author LuTing
 * @date 2017年3月9日
 */
function initReviewConfirmation_datagridDatagrid() {
	$("#reviewConfirmation_datagrid").datagrid({
		url : ctx.rootPath() + '/toreviewConfirm/index',
		striped : true,
		singleSelect : true,
		rownumbers : true,
		idField : 'id',
		pagination : true,
		fitColumns : false,
		scrollbarSize : 0,
		remoteSort : true,
		onDblClickRow : function(index, data) {
			handle(data.loanNo,data.version,data.checkPersonCode);
		},
		columns : [ [ {
			field : 'accDate',
			title : '初审提交时间',
			width : 180,
			sortable : true,
			formatter : formatDate
		}, {
			field : 'productName',
			title : '借款产品',
			width : 180,
			sortable : true,
            formatter:formatterProduct,
		}, {
			field : 'applyType',
			title : '申请类型',
			width : 180,
			sortable : true,
		}, {
			field : 'personName',
			title : '申请人姓名',
			width : 180,
			sortable : true,
		}, {
			field : 'checkPersonName',
			title : '初审姓名',
			width : 180,
			sortable : false,
		}, {
			field : 'proxyGroup',
			title : '初审小组',
			width : 180,
			sortable : false,
			order : 'desc'
		}, {
			field : 'rtfNodeStatus',
			title : '初审操作',
			width : 170,
			sortable : true,
			formatter : function(value, data, index) {
				if ('XSCS-PASS' == value || 'HIGH-PASS' == value) {
					return "通过";
				} else if('XSCS-REJECT' == value){
					return "拒绝";
				}else if('XSCS-RETURN' == value){
					return "退回";
				} else if ("XSCS-ZDQQRETURN" == value){
					return "退回前前";
				}
			}
		}, {
			field : 'assistStatus',
			title : '初审结果',
			width : 240,
			sortable : false,
			formatter : function(value, data, index) {
                if(value == 'XSCS-ZDQQRETUR'){
                    return data.qqReturnReason;
                }else{
                    var str = '';
                    var reasonA = '';
                    var reasonB = '';
                    var c = '';
                    if('' != data.primaryReason && null != data.primaryReason){
                        reasonA = data.primaryReason;
                    }
                    if('' != data.secodeReason && null != data.secodeReason){
                        reasonB = data.secodeReason;
                    }
                    if('' != data.primaryReason && null != data.primaryReason && '' != data.secodeReason && null != data.secodeReason){
                        c = '/';
                    }
                    if (value == 'XSCS-PASS' || 'HIGH-PASS' == value) {//通过
                        str = data.accLmt;
                    } else if(value == 'XSCS-REJECT' || value == 'XSCS-RETURN' || value=="XSCS-ZDQQRETURN"){//拒绝或者退回获取退回前前
                        str = reasonA+c+reasonB;
                    }
                    return numberFomatter(str);
                }
			}
		},{
			field : 'version',
			title : '版本',
			width : 120,
			hidden: true,
			sortable : true,
		},{
			field : 'loanNo',
			title : '借款编号',
			width : 170,
			hidden: true,
			sortable : true,
		},{
			field : 'action',
			title : '操作',
			width : 190,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '<a href="javaScript:void(0);" onclick=handle("'+data.loanNo+'","'+data.version+'","'+data.checkPersonCode+'")>办理</a>';
				return action;
			}
		} ] ]
	});
}

//办理
var markReviewHandleLoanNo = {};
function handle(loanNo,version,checkPerson) {
	if(loanNo in markReviewHandleLoanNo){
		markReviewHandleLoanNo[loanNo].focus();
	}else{
		markReviewHandleLoanNo[loanNo] = jDialog.open({url: ctx.rootPath() + "/toreviewConfirm/handle/" + loanNo + "/" + version + "/" +checkPerson});
	}

}
