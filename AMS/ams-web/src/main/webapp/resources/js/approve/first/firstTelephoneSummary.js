var vm;
/**
 * 联系人关系(avloan)
 * @param option
 */
function pageInit(option) {
    var defaultOption = {
		$id: 'page',
		contactRelation: {
			'00000':'本人',
    		'00001':'父母',
    		'00002':'子女',
    		'00003':'兄弟',
    		'00004':'姐妹',
    		'00005':'兄妹',
    		'00006':'姐弟',
    		'00007':'朋友',
    		'00008':'同事',
    		'00009':'房东',
    		'00010':'亲属',
    		'00011':'同学',
    		'00012':'其他',
    		'00013':'配偶'
    	},
    	getContactRelation: function(code){
			return vm.contactRelation[code];
		}
    };
    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

$(function() {
	// 第三方备注：其他值添加文本框
	$('#ThridRemark').combobox({
		editable : false,
		valueField : 'value',
		textField : 'label',
		data : [ {
			label : '请选择',
			value : ''
		}, {
			label : '所有电话第三方查询无信息',
			value : '所有电话第三方查询无信息'
		}, {
			label : '无异常',
			value : '无异常'
		}, {
			label : '其他',
			value : '其他'
		} ],
		onChange : function(newValue, oldValue) {
			if ("其他" == newValue) {
				$('#first_otherRemarks_textbox').textbox({
					height : 70,
					width : 700,
					required : true,
				});
				$("#basicInfo_table tr:last").find("span.otherRemarks").removeClass("display_none");
			} else {
				$("#basicInfo_table tr:last").find("span.otherRemarks").addClass("display_none");
				$('#first_otherRemarks_textbox').textbox({
					height : 20,
					required : false
				});
			}
		},
		onLoadSuccess : function() {
			var defaultVal = $(this).combobox("getValue");
			if ("其他" == defaultVal) {
				$('#first_otherRemarks_textbox').textbox({
					height : 70,
					width : 700,
					required : true,
				});
				$("#basicInfo_table tr:last").find("span.otherRemarks").removeClass("display_none");
			} else {
				$("#basicInfo_table tr:last").find("span.otherRemarks").addClass("display_none");
				$('#first_otherRemarks_textbox').textbox({
					height : 20,
					required : false
				});
			}
		}
	});

	// 初始化电核记录datagrid
	initPhoneRecord($("#loanNo_hidden").val());
});

// 窗口关闭时需要关闭添加电核记录
window.onbeforeunload = function() {
	window.opener.telephoneSummaryHTMLWindow = null;
	if (isNotNull(addPhoneRecordWindow)) {
		addPhoneRecordWindow.close();
	}
};

/**
 * 跳转到百度地图
 * 
 * @author dmz
 * @date 2017年7月18日
 * @param province-省
 * @param city-市
 * @param zone-区
 * @param address-街道
 */
function firstTelephoneSummaryBaiduMaps(province, city, zone, address) {
	if (isNotNull(province) && isNotNull(city) && isNotNull(zone) && isNotNull(address)) {
        var location = province + " " + city + " " + zone + " " + address;
        window.open("http://api.map.baidu.com/geocoder?address=" + location + "&output=html&src=yourCompanyName")
	}
}
/**
 * 家庭地址到工作地址路线
 * @param homeProvince-家所在省
 * @param homeCity-家所在市
 * @param homeZone-家所在区
 * @param homeAddress-详细地址
 * @param province-公司所在省
 * @param city-公司所在市
 * @param zone-公司所在区
 * @param address-公司详细地址
 */
function showMapFromHomeToCompany(homeProvince, homeCity, homeZone, homeAddress,province, city, zone, address) {
    var notNull =isNotNull(homeProvince) && isNotNull(homeCity) && isNotNull(homeZone) && isNotNull(homeAddress) &&  isNotNull(province) && isNotNull(city) && isNotNull(zone) && isNotNull(address);
    if (notNull) {
        var home = homeProvince + " " + homeCity + " " + homeZone + " " + homeAddress;
        var name = homeProvince + homeCity + homeZone + homeAddress;
        var company = province + " " + city + " " + zone + " " + address;
        window.open("http://api.map.baidu.com/direction?origin="+home+"|name:" + name + "&destination="+company+"&mode=driving&region="+homeProvince+"&output=html&src=yourCompanyName");
	}
}

/**
 * 修改基本信息
 * 
 * @Author LuTing
 * @date 2017年2月23日
 */
function updateBasicInfo() {
	if ($("#basicInfo_form").form('validate')) {
		var params = $("#basicInfo_form").serializeObject();
		params.loanNo = $("#loanNo_hidden").val();
		params.version = $("#telephoneSummarybasicInfo").val();
		if (params.theThirdPartyNote != "其他") {
			params.theThirdPartyNoteDetails = "0";
		}
		post(ctx.rootPath() + '/firstTelephoneSummary/updateBasicInfo', params, "json", function(data) {
			if (data.success) {
				$.info("提示", data.firstMessage);
			} else if (data.type == "VERSIONERR") {
				$.info('警告', data.firstMessage, 'warning');
			} else {
				$.info("提示", data.firstMessage, "error");
			}
		});
	}
}

/**
 * 弹出第三方电话增加对话框
 * 
 * @Author LuTing
 * @date 2017年2月28日
 */

function addPhoneDialog() {
	$("#addPhoneDialog").removeClass("display_none").dialog({
		title : "第三方电话增加",
		modal : true,
		width : 450,
		height : 250,
		onClose : function() {
			$('#addPhoneForm').form('clear');
		},
		buttons : [ {
			text : '保存',
			iconCls : 'fa fa-check',
			handler : function() {
				addTel();
			}
		}, {
			text : '取消',
			iconCls : 'fa fa-reply',
			handler : function() {
				$('#addPhoneForm').form('clear');
				$("#addPhoneDialog").dialog("close");
			}
		} ]
	})
}



/**
 * 第三方电话增加 修改电话号码文本框 属性
 * 
 * @Author LuTing
 * @date 2017年2月28日
 * @param newValue
 * @param oldValue
 */
function changeTelephoneType(newValue, oldValue) {
	if ("1" == newValue) {
		$("#phoneNumber").textbox({
			validType : ['telNum',"checkTel[\'#thirdPartSource\',\'.mobileHistoryInfo\']"]
		});
	} else {
		$("#phoneNumber").textbox({
			validType : [ 'length[11,11]', 'mobile', "checkTel[\'#thirdPartSource\',\'.mobileHistoryInfo\']" ]
		});
	}
}

/**
 * 初始化修改/新增电核记录Dialog
 * 
 * @Author LuTing
 * @date 2017年11月24日
 * @param obj 当前操作的行对象
 * @param title
 */
var addPhoneRecordWindow = null;
function initPhoneRecordDialog(obj, title, name, rel) {
	var applicantPeople="";
	var phone="";
	var phoneType="";
	var loanNo = $("#loanNo_hidden").val();
	var id="";
	//当前Table所在行对象
	var tr = $(obj).parents("tr");
	if ("新增电核记录" == title) {
//		$("#addTelephoneRecord_Form").form('clear');
		// 与申请人关系
		if(isNotNull(rel)){
			applicantPeople = rel;
		}else{
			applicantPeople = tr.children().eq(0).text();
		}
		// 姓名
//		name = tr.children().eq(1).text();
		if(tr.hasClass("merge")){
			// 电核类型
			phoneType = tr.children().eq(2).text();
			//手机号码
			phone = tr.children().eq(3).text();
		}else if(tr.hasClass("mobileHistoryInfo")){
			name = tr.children().eq(1).text();
			// 电核类型
			phoneType = tr.children().eq(2).text();
			//手机号码
			phone = tr.children().find("input").val();
		}else{
			// 电核类型
			phoneType = tr.children().eq(0).text();
			//手机号码
			phone = tr.children().eq(1).text();
		}
        console.info("mobile:" + phone +",old:"+$(obj).parent().find("input").val());
        //判断是否修改保存

        if(isNotNull($(obj).parent().find("input").val()) && phone != $(obj).parent().find("input").val()){
            $.info("提示", "请先保存修改的信息!", "warning");
            return;
        }
	} else if ("修改电核记录" == title) {
		id = obj;
	}
	
	//判断新增窗口是否已经打开
	if (!_.isEmpty(jDialog.openedWindows)) {
		$.messager.alert('提示', '不可同时添加多条电核记录', 'warning', function() {
            addPhoneRecordWindow.focus();	//当前已打开窗口获取焦点
		});
	} else {
		addTelephoneRecordHTMLWindow = jDialog.open({
			url: ctx.rootPath() + '/firstTelephoneSummary/addTelephoneRecordWin?nameTitle=' + applicantPeople.trim() + '&name=' + name + '&telPhone=' + phone + '&telPhoneType=' + phoneType + '&loanNo=' + loanNo + '&id=' + id,
			width:730,
			height:360,
			onDestroy: function () {
                $("#phoneRecord_table").datagrid('load');
            }
		});
	}
}


/**
 * 第三方电话新增
 */
function addTel() {
	if ($("#addPhoneForm").form('validate')) {
		var contactInfo = new Object();
		contactInfo.loanNo = $("#loanNo_hidden").val();
		contactInfo.nameTitle = "第三方";
		contactInfo.name = $("#thirdPartSource").combobox('getText');// 第三方来源
		contactInfo.telPhone = $("#phoneNumber").val();
		contactInfo.telPhoneType = $("#thirdPartphoneType").combobox('getText');
		contactInfo.version = $("#telephoneSummarybasicInfo").val();
		var typeName = $("#thirdPartphoneType").combobox('getText'); // 电话类型
        var type = $("#thirdPartphoneType").combobox('getValue');
		$.post(ctx.rootPath() + '/firstTelephoneSummary/addThirdPartTel', contactInfo, function(data) {
			if (data.type == "SUCCESS") {
				// 1.关闭当前对话框
				$("#addPhoneDialog").dialog("close");
				var id = data.data.id;
				// 2.刷新联系人信息 
				
				var html = '<tr class="mobileHistoryInfo">'+
				'<td>第三方</td>'+
				'<td>' + contactInfo.name + '</td>'+
				'<td>' + typeName + '</td><td>';
				// 移动电话
				if (type == '2') {
					html+='<input type="text" class="input" name="mobileNumberSelect" value="' + contactInfo.telPhone + '">';
				} else {
					//固定电话
					html+='<input type="text" class="input" name="telephoneNumberSelect" value="' + contactInfo.telPhone + '">';
				}
				
				html+= '</td>';
				
				//新增成功，手机归属地与营业部不在同一个城市，标红
				var branchCity = $('#branchCity').val();
				var branchProvince = $('#branchProvince').val();
				console.info(isNotNull(data.data.phoneCity));
				if(!isNotNull(data.data.phoneCity)){
					html+= '<td><a href="javaScript:void(0);" title="查无结果">查无结果</a>';
				}else if(data.data.phoneCity.indexOf(branchProvince) >= 0 && data.data.phoneCity.indexOf(branchCity) >= 0){
                    html+= '<td><a href="javaScript:void(0);" title="' + data.data.phoneCity + '">' + data.data.phoneCity + '</a>';
				}else{
					html+= '<td><a href="javaScript:void(0);" title="'+ data.data.phoneCity + '" class="phoneCity">' + data.data.phoneCity+ '</a>';
				}
				html+= '</td>'+	
				'<td>'+
				'<input  type="hidden" value="' + contactInfo.telPhone + '">'+
				'<a href="javaScript:void(0);" onclick="addPhoneRecordDialog(this,\''+contactInfo.name+'\')">记录</a>&nbsp;|&nbsp;&nbsp;'+
				'<a href="javaScript:void(0);" onclick="modifyThridContact('+ id + ',this,' + Date.parse(new Date()) + ')">修改</a>&nbsp;|&nbsp;'+
				'<a href="javaScript:void(0);" onclick="deleteMobile('+ id +',this)">删除</a>&nbsp;|&nbsp;'+
				'<a href="javaScript:void(0);" onclick="seachForBaidu(\''+contactInfo.telPhone+'\')">号码查询</a>'+
				'</td>'+
			'</tr>';
				//追加到联系人信息列表中
				html = $(html);
				$("#contact_phone_table").append(html);
				//生成easyUI numberbox控件
				html.find("input[name='mobileNumberSelect']").numberbox({
                    required:true,
					width : 140,
					validType : [ 'length[11,11]', 'mobile' ]
				});
				//固定电话
				html.find("input[name='telephoneNumberSelect']").textbox({
                    required:true,
					width : 140,
					validType : 'telNum'
				});
				$('#addPhoneForm').form('clear');
					
				$.info("提示", "添加成功");
            } else if (data.type == "VERSIONERR") {
				$.info('警告', '信审件可能已经被改派，请关闭当前页面', 'warning');
				closeAndSelectTabs("初审办理", "初审工作台");
			} else {
				$.info("提示", "添加失败", 'warning');
			}
		})
	}
}
var isChange = false;
/**
 * 第三方联系电话修改
 */
function modifyThridContact(id, obj, createdDate) {
    var name = $(obj).parent().parent().children("td").eq(1).text();
	var phone = $(obj).parent().parent().find("input");//获取当前号码(默认获取easyUI的第一个)
    var rowNum = $(obj).parent().parent().index();//获取当前列所在行号
    var flag = true;
    var oldPhone = $(obj).parent().find("input")//本次修改前的电话号码
    //获取第三方联系电话
    $(".mobileHistoryInfo").each(function(idx, val) {
        if (name == $(val).children("td").eq(1).text() && phone.val() == $(val).children("td").eq(3).children("input").val() && rowNum != $(val).index()) {
            $.info('警告', '电话号码重复!','warning');
            flag = false;
            //结束循环
            return flag;
        }
    });
    //验证第三方电话是否合法
    if (!flag || !phone.textbox("isValid")) {
    	return;
	}
	post(ctx.rootPath() + '/firstTelephoneSummary/updateTelephoneRecord', {
		id : id,
		telPhone : phone.val(),
		loanNo : $("#loanNo_hidden").val()
	}, 'json', function(data) {
		if (data.success) {
			//手机归属地与营业部不在同一个城市，标红

			var branchCity = $('#branchCity').val();//营业部所在市
			var branchProvince = $('#branchProvince').val();//营业部所在省
            $(obj).parent().parent().children("td").eq(4).find("a").html(data.data.phoneCity);
            $(obj).parent().parent().children("td").eq(4).find("a").attr("title",data.data.phoneCity);

			if(!isNotNull(data.data.phoneCity)){
                $(obj).parent().parent().children("td").eq(4).find("a").html("查无结果");
                $(obj).parent().parent().children("td").eq(4).find("a").attr("title","查无结果");
                $(obj).parent().parent().children("td").eq(4).find("a").removeClass("phoneCity");
			}else if(data.data.phoneCity.indexOf(branchProvince) >= 0 && data.data.phoneCity.indexOf(branchCity) >= 0){
                /*$(obj).parent().parent().children("td").eq(4).find("a").css({"color": "#2779AA"});*/
                $(obj).parent().parent().children("td").eq(4).find("a").removeClass("phoneCity");
            }else{
				//添加样式标红
				/*$(obj).parent().parent().children("td").eq(4).find("a").css({"color": "red"});*/
                $(obj).parent().parent().children("td").eq(4).find("a").addClass("phoneCity");
			}
            oldPhone.val(phone.val());
			$.info("提示", "修改成功!", "info");
		} else {
			$.info("提示", "修改失败!", "error");
		}
	})
}

/**
 * 新增电核记录
 * 
 * @Author LuTing
 * @date 2017年2月28日
 * @param obj
 */
function addPhoneRecordDialog(obj, name, relation) {
	var rel = null;
	if(isNotNull(relation)){
		rel = vm.getContactRelation(relation);
	}
	initPhoneRecordDialog(obj, "新增电核记录", name, rel);
}
/**
 * 修改电核记录
 * 
 * @Author LuTing
 * @date 2017年2月28日
 * @param obj
 */
function updatePhoneRecordDialog(obj) {
	initPhoneRecordDialog(obj, "修改电核记录");
}


/**
 * 初始化电核记录datagrid
 * 
 * @Author LuTing
 * @date 2017年2月28日
 */
function initPhoneRecord(loanId) {
	$("#phoneRecord_table").datagrid({
		url : ctx.rootPath() + '/firstApprove/telephoneHistory',
		striped : true,
		singleSelect : true,
		idField : 'id',
		pagination : true,
		pageSize : 20,
		fit : true,
		queryParams : {
			loanNo : loanId
		},
		onLoadSuccess : function() {
			$(".easyui-tooltip").tooltip({
				onShow : function() {
					$(this).tooltip('tip').css({
						borderColor : '#000'
					});
				}
			});
		},
		fitColumns : false,
		scrollbarSize : 0,
		columns : [ [ {
			field : 'telDate',
			title : '核查时间',
//			width : 110,
			formatter : formatDateYMDHS,
		}, {
			field : 'name',
			title : '姓名',
//			width : 65,
		}, {
			field : 'nameTitle',
			title : '关系',
//			width : 50,
		}, {
			field : 'telPhoneType',
			title : '电话类型',
			hidden : true,
//			width : 1,
		}, {
			field : 'telPhone',
			title : '电话号码',
//			width : 110,
            formatter : function(value, data, index) {
                if(value!=null){
                    if(value!=null){
                        return "<label title=\""+ value +"\" class=\"easyui-tooltip\">" + value + "</label>";
                    }
                }
            }
		}, {
			field : 'telRelationType',
			title : '与接听人关系',
//			width : 80,
		}, {
			field : 'askContent',
			title : '电核备注',
//            width : 370,
			formatter : function(value, row, index) {
				var showText = "";
				var sourceText = value.split("");
				$.each(sourceText, function(index, val) {
					showText = showText + val;
					if (Number(index + 1) % 30 == 0) {
						showText = showText + "<br>";
					}
				})
				showText = '<span title="' + value + '"  class="easyui-tooltip">' + showText + '</span>'
				return showText;
			}
		}, {
			field : 'createdBy',
			title : '创建人',
//			width : 65,
		}, {
			field : 'action',
			title : '操作',
//			width : 60,
			align : 'center',
			formatter : function(value, data, index) {
				var action = '';
				if (isNotNull(data.flag) && true == data.flag) {
					action = '<a href="javaScript:void(0);" onclick="updatePhoneRecordDialog(' + data.id + ')">修改</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="deleteMobile(' + data.id + ')">删除</a>';
				}
				return action;
			}
		} ] ]
	});
}

/**
 * 删除第三方联系人或电核信息
 * @param id 电核记录主键id
 * @param obj为空时表示电核记录
 */
function deleteMobile(id, obj){
	// 确认删除提示
	$.messager.confirm("提示", "是否确认删除？", function(isOrNo) {
		if (isOrNo) {
			post(ctx.rootPath() + '/firstTelephoneSummary/deleteTelephoneRecord/' + $("#loanNo_hidden").val(), {
				mobileHistryId : id
			}, 'json', function(data) {
				if (data.success) {
					//第三方记录删除
					if(isNotNull(obj)){
						$(obj).parents("tr").remove();// 删除成功后再移除该联系方式
					}else{
						$("#phoneRecord_table").datagrid('load');
					}
					$.info("提示", "删除成功!", "info");
				}else {
					$.info("error", "删除失败!");
				}
			});
		}
	});
}

/**
 * 百度批量搜索电话号码
 * @param mobile
 */
function seachBaidu(){
	var rows = $("#contact_phone_table").find("tr").length; //行数
	var cells = $("#contact_phone_table").find("tr").find("td").length; //列数
	console.info("row="+rows);
	console.info("cell="+cells);
	var phone;
	$("#contact_phone_table tr").each(function(){
		//$(this).children().eq(3).text()!='关系'
		//排除第一行
		if($(this).children().eq(0).text()!='关系'){
			// 判断是否存在合并
			if ($(this).hasClass("merge")) {
				phone = $(this).children().eq(3).text();// 手机号码
			} else if ($(this).hasClass("mobileHistoryInfo")){
				/*phone = $(this).children().eq(3).find("input").val();// 第三方手机号码*/
				phone = $(this).children().find("input:hidden").last().val();
                console.info("phone:" + $(this).children().eq(3).find("input").val()+",old:"+$(this).children().find("input:hidden").last().val());
			}else {
				phone = $(this).children().eq(1).text();// 手机号码
			}

			//固定电话处理
			if(phone.indexOf("-") >= 0){
                var arrays = new Array();
                arrays = phone.split("-");
                window.open('http://www.baidu.com/s?wd='+arrays[0] + "-" +arrays[1]);
				window.open('http://www.baidu.com/s?wd='+arrays[0] + " " + arrays[1]);
				window.open('http://www.baidu.com/s?wd='+arrays[0] + arrays[1]);
			}else{
                window.open('http://www.baidu.com/s?wd='+phone);
			}
		}
	})
}

/**
 * 百度搜索
 * @param mobile
 */
function seachForBaidu(mobile, obj){
	//第三方电话修改后，取值还是获取的默认值
	if(isNotNull(obj) && $(obj).parents("tr").hasClass("mobileHistoryInfo")){
		mobile = $(obj).parents("tr").children().eq(3).find("input").val();// 第三方手机号码
	}
    console.info("mobile:" + mobile+",old:"+$(obj).parent().find("input").val());
	console.info(isNotNull($(obj).parent().find("input").val()));
	//判断是否修改保存
	if(isNotNull($(obj).parent().find("input").val()) && mobile != $(obj).parent().find("input").val()){
        $.info("提示", "请先保存修改的信息!", "warning");
		return;
	}



    //固定电话处理
	if(mobile.indexOf("-") >= 0){
        var arrays = new Array();
        arrays = mobile.split("-");
        window.open('http://www.baidu.com/s?wd=' + arrays[0] + "-" +arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
		window.open('http://www.baidu.com/s?wd=' + arrays[0] + " "+arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
		window.open('http://www.baidu.com/s?wd=' + arrays[0] + arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
	}else {
        window.open('http://www.baidu.com/s?wd=' + mobile, '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
	}
}