//@ sourceURL=extendsEasyui.js
/** 定义全局变量* */
var ctx = $.extend({}, ctx);

/**
 * 
 * 
 * 获取项目路径
 * 
 */
ctx.rootPath = function() {
	var path = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = path.indexOf(pathName, 9);
	var localhostPath = path.substring(0, pos);
	// var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') +
	// 1);
	var projectName = $("#SYS_PROJECT_NAME").attr("name");
	return (localhostPath + projectName);
};
ctx.getDate = function(date) {
	var dateNew = date || new Date();
	return moment(dateNew).format('YYYY-MM-DD');
}
/**
 * @author dmz
 * 
 * @requires jQuery,EasyUI
 * 
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	if ((top == 1 && left > 0) || (left == 1 && top > 0)){
		return;
	}
	var parentObj = $(this).panel("panel").parent();
	var obj = $(this).panel("options");
	var left1,top1,l = parentObj.width() - obj.width, t = parentObj.height() - obj.height;
	var overflow = parentObj.css("overflow") == "hidden" ? true : false;
	if ((left < 0) || (overflow && l < 0)) {
		left1 = 1;
	}
	if ((top < 0) || (overflow && t < 0)) {
		top1 = 1 ;
	}
	if (overflow && (left > l && l > 0 )) {
		left1 = l;
	}
	if (overflow && (top > t && t > 0)) {
		top1 = t;
	}
	if (left1 || top1) {
		$(this).window("move",{
			left : left1,
			top : top1,
		})
	}
};

$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;

/**
 * Extends Easyui textBox 为textBox 添加清空按钮,并且设置默认搞定为30px
 */
$.extend($.fn.textbox.defaults, {
	height : 27,
	isClearBtn : true,// 默认添加
// icons : null,
});
$.extend($.fn.textbox.methods, {
	/*
	 * initValue : function(jq, value) { return jq.each(function() { var target = $.data(this, "textbox"); target.options.value = ""; $(this).textbox("setText", value); target.textbox.find(".textbox-value").val(value); $(this).val(value); if (target.options.isClearBtn) { $(this).textbox("addClearBtn", 'icon-clear'); } }); },
	 */
	/*
	 * setText : function(jq, newVal) { return jq.each(function() { var opts = $(this).textbox("options"); var textBox = $(this).textbox("textbox"); newVal = newVal == undefined ? "" : String(newVal); if ($(this).textbox("getText") != newVal) { textBox.val(newVal); } opts.value = newVal; if (!textBox.is(":focus")) { if (newVal) { textBox.removeClass("textbox-prompt"); } else { textBox.val(opts.prompt).addClass("textbox-prompt"); } } if ("" != newVal) { $(this).textbox('getIcon', 0).css('visibility', 'visible'); } else { $(this).textbox('getIcon', 0).css('visibility', 'hidden'); } $(this).textbox("validate"); }); },
	 */
	addClearBtn : function(jq, iconCls) {
		return jq.each(function() {
			var target = $(this);
			var opts = $.data(this, "textbox").options;
			opts.isClearBtn = false;// 避免重复添加
			opts.icons = opts.icons || [];
			opts.icons.unshift({
				iconCls : iconCls,
				handler : function(e) {
					$(e.data.target).textbox("clear").textbox("textbox").focus();
					$(this).css('visibility', 'hidden');
					if (opts.addClearHandler) {
						opts.addClearHandler.call();
					}
				}
			});
			target.textbox();
			if (!target.textbox("getText")) {
				target.textbox("getIcon", 0).css('visibility', 'hidden');
			}
			target.textbox("textbox").bind("keyup", function() {
				var icon = target.textbox('getIcon', 0);
				if ($(this).val()) {
					icon.css('visibility', 'visible');
				} else {
					icon.css('visibility', 'hidden');
				}
			});
		});
	},
	addClearHandler : function() {

	}
});

/**
 * Extends Easyui datebox 为datebox 设置默认高为30px
 */
$.extend($.fn.datebox.defaults, {
	height : 27
});

/** * Extends Easyui combo 为combo 设置默认高为30px*** */
$.extend($.fn.combo.defaults, {
	height : 27
});
/** * Extends Easyui combobox 为combobox 设置默认高为30px*** */
$.extend($.fn.combobox.defaults, {
	height : 27,
});

/** * Extends Easyui numberbox 为numberbox 设置默认高为30px*** */
$.extend($.fn.numberbox.defaults, {
	height : 27,
// isClearBtn : true,// 默认添加
// icons : null,
});
/** * Extends Easyui numberspinner 为numberspinner 设置默认高为30px*** */
$.extend($.fn.numberspinner.defaults, {
	height : 27,
// isClearBtn : true,// 默认添加
// icons : null,
});
/** * Extends Easyui datebox 为datebox设置清空按钮*** */
$.extend($.fn.datebox.defaults, {
// isClearBtn : true,// 默认添加
// icons : null,
});
/** * Extends Easyui message show *** */
(function($) {
	jQuery.extend({// info,error,warning
		info : function(title, info, icon, timeout, backFun) {
			var iCl = icon || "info";
			var html = '';
			if ("error" == iCl) {
				html = '<i class="fa fa-times-circle error" aria-hidden="true"></i>' + info;
			} else if ("warning" == iCl) {
				html = '<i class="fa fa-exclamation-triangle warning" aria-hidden="true"></i>' + info;
			} else {
				html = '<i class="fa fa-info-circle info" aria-hidden="true"></i>' + info;
			}
			$.messager.show({
				title : title,
				height : 'auto',
				msg : "<div class='alert_info'>" + html + "</div>",
				timeout : timeout || 2000,
				showType : 'fade',
                zIndex: 10000,
				style : {
					right : '',
					bottom : ''
				},
				onClose : backFun || function() {
				}
			});
		}
	});
})(jQuery);

/** ***************清空form******************* */
/**
 * obj可以是form，也可以是form地下任意一个控件id
 */
function clearForm(obj) {
	if ($(obj).is("form")) {
		$(obj).form('reset');
		$(obj).find(".float_span_class").each(function(){
			this.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
		});
	} else {
		$(obj).parents("form").form('reset');
		$(obj).parents("form").find(".float_span_class").each(function(){
			this.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
		});
	}
}


// 拓展easyui-textbox插件的验证规则
$.extend($.fn.textbox.defaults.rules,{
	max: {
    validator : function(value, param) {
        return value.length <= param;
    },
    message : "字数不能超过{0}个"
}
});

/**
 * 扩展dialog
 * @param options
 * @returns {jQuery}
 */
ctx.dialog = function(options) {

	var defaultOptions = $.extend({},{
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);

	return $('<div class="padding_20"/>').dialog(defaultOptions);
};

/** ************************Start 省市级联******************************************* */
/**
 * 初始化省-市-级-如果不需要区请传null
 * @param data
 * @param provinceId	省
 * @param cityId		市
 * @param areaId		区域
 * @param backFun
 */
function inintArea(data,provinceId, cityId, areaId, backFun) {
	initArea(areaId, backFun);
	initCity(cityId, areaId, backFun);
	initProvince(data,provinceId, cityId, areaId, backFun);
}

/**
 * 初始化省份
 * @param datajson
 * @param provinceId
 * @param cityId
 * @param countryId
 * @param backFun
 */
function initProvince(datajson, provinceId, cityId, countryId, backFun) {
	$(provinceId).combobox({
		valueField : 'code',
		textField : 'name',
        prompt: '-----省-----',
		required : true,
		editable : false,
		data : datajson,
		onLoadSuccess : function() {
			var defaultVal = $(this).combobox("getValue");
			if ("" != defaultVal) {
				// 初始化省有值就加载对应的市列表
				var data = $(this).combobox("options").finder.getRow(this, defaultVal);
				if (data) {
					$(cityId).combobox("loadData", data.children);
				}
				$(provinceId).parents("td").find("input.region").val($(provinceId).combobox("getText"));
			}
		},
		/*
		 * onSelect : function(data) { // 动态为市添加数据 $(cityId).combobox("loadData", data.children); },
		 */
		onChange : function(newValue, oldValue) {
			var data = $(this).combobox("options").finder.getRow(this, newValue);
			if (data) {
				$(cityId).combobox("loadData", data.children);
			}
			$(cityId).combobox("setValue", "");
			if (isNotNull(countryId)) {
                $(countryId).combobox("setValue", "");
			}
            $(provinceId).parents("td").find("input.region").val($(provinceId).combobox("getText"));
			if (isNotNull(backFun)) {
				backFun();
			}
		},
		addClearHandler : function() {
			$(cityId).combobox("setValue", "");
			$(cityId).combobox("loadData", {});
			if (isNotNull(countryId)) {
                $(countryId).combobox("setValue", "");
                $(countryId).combobox("loadData", {});
			}
		}
	});
}

/**
 * 初始化市
 * @param cityId
 * @param countryId
 * @param backFun
 */
function initCity(cityId, countryId, backFun) {
	$(cityId).combobox({
		valueField : 'code',
		textField : 'name',
        prompt: '-----市-----',
		required : true,
		editable : false,
		onLoadSuccess : function() {
			var defaultVal = $(this).combobox("getValue");
			if ("" != defaultVal) {
				// 初始化市有值加载区列表
				var data = $(this).combobox("options").finder.getRow(this, defaultVal);
				if (data && isNotNull(countryId)) {
					$(countryId).combobox("loadData", data.children);
				}
				$(cityId).parents("td").find("input.region").val($(cityId).combobox("getText"));
			}
		},
		/*
		 * onSelect : function(data) { // 动态给地区添数据 $(countryId).combobox("loadData", data.children); },
		 */
		onChange : function(newValue, oldValue) {
			var data = $(this).combobox("options").finder.getRow(this, newValue);
			if (isNotNull(countryId)) {
                if (data) {
                    $(countryId).combobox("loadData", data.children);
                }
				$(countryId).combobox("setValue", "");
            }
			$(cityId).parents("td").find("input.region").val($(cityId).combobox("getText"));
			if (isNotNull(backFun)) {
				backFun();
			}
		},
		addClearHandler : function() {
			if (isNotNull(countryId)) {
                $(countryId).combobox("setValue", "");
                $(countryId).combobox("loadData", {});
			}
		}
	});
}

/**
 * 初始化县/地区
 * @param countryId
 * @param backFun
 */
function initArea(countryId, backFun) {
	if (isNotNull(countryId)){
        $(countryId).combobox({
            valueField : 'code',
            textField : 'name',
            prompt: '-----区-----',
            required : true,
            editable : false,
            value : $(countryId).val(),
            onLoadSuccess : function() {
                var defaultVal = $(this).combobox("getValue");
                if ("" != defaultVal) {
                    $(countryId).parents("td").find("input.region").val($(countryId).combobox("getText"));
                }
            },
            onChange : function(newValue, oldValue) {
                $(countryId).parents("td").find("input.region").val($(countryId).combobox("getText"));
                if (isNotNull(backFun)) {
                    backFun();
                }
            },
        });
	}
}
/** ************************End 省市级联******************************************* */

/**
 * 校验身份证合法性
 * @param idcard
 * @returns {*}
 */
function checkIdcard(idcard) {
	var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!");
	var area = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	};
	var Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	// 地区检验
	if (area[parseInt(idcard.substr(0, 2))] == null) {
		return Errors[4];
	}
	// 身份号码位数及格式检验
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
		}
		if (ereg.test(idcard)) {
			return true;
		} else {
			return Errors[2];
		}
		break;
	case 18:
		// 18位身份号码检测
		// 出生日期的合法性检查
		// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
		// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
		if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
		} else {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
		}
		if (ereg.test(idcard)) {// 测试出生日期的合法性
			// 计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 + parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1);// 判断校验位
			if (M == idcard_array[17]) {
				return true;
			} else {
				return Errors[3];
			}
		} else {
			return Errors[2];
		}
		break;
	default:
		return Errors[1];
		break;
	}
}
/*
 * 比较两个日期的大小 传入的参数推荐是"yyyy-mm-dd"的格式，其他的日期格式也可以，但要保证一致
 */
var dateCompare = function(date1, date2) {
	if (date1 && date2) {
		var a = new Date(date1);
		var b = new Date(date2);
		return a <= b;
	}
};
/*
 * 比较两个时间的大小（传入的参数是"HH:mm"的格式，） @param: time1:目标时间;time2:被比较时间
 */
var timeCompare = function(time1, time2) {
	try {
		if (time1 && time2) {
			var t1 = parseInt(time1.split(":")[0] * 60) + parseInt(time1.split(":")[1]);
			var t2 = parseInt(time2.split(":")[0] * 60) + parseInt(time2.split(":")[1]);
			return t1 < t2;
		}
		return false;
	} catch (e) {
		return false;
	}
};
/*
 * 比较两个时间的大小，支持的格式可在formatArr扩展 @param: datetime1:目标时间;datetime2:被比较时间
 */
var dateTimeCompare = function(datetime1, datetime2) {

	var formatArr = new Array('YYYY-MM-DD', 'YYYY-MM-DD HH:mm', 'YYYY-MM-DD HH:mm:ss');// 支持的格式
	try {
		if (datetime1 && datetime2) {
			var dt1 = moment(datetime1, formatArr);
			var dt2 = moment(datetime2, formatArr);
			return dt1 < dt2;
		}
		return false;
	} catch (e) {
		return false;
	}
};
/**
 * 格式化流程节点状态
 * 
 * @author dmz
 * @date 2017年4月11日
 * @param value
 * @param row
 * @param index
 */
function formatRtfNodeState(value, row, index) {
	var html = "无状态";
	if (isNotNull(value)) {
		if ("SQLR-SUBMIT" == value) {// 录入复核办理
			html = "录入复核办理";
		} else if ("CSFP-SUBMIT" == value) {// 初审分派办理
			html = "初审分派办理";
		} else if ("CSFP-RETURN" == value) {// 初审分派办理退回
			html = "初审分派办理退回";
		} else if ("CSFP-ZDQQRETURN" == value) {// 初审分派办理退回前前
            html = "初审分派办理退回前前";
        } else if ("CSFP-REJECT" == value) {// 初审分派办理拒绝
			html = "初审分派办理拒绝";
		} else if ("XSCS-ASSIGN" == value) {// 信审初审办理
			html = " 信审初审办理";
		} else if ("XSCS-HANGUP" == value) { // 信审初审挂起
			html = "信审初审挂起";
		} else if ("XSCS-PASS" == value) {// 信审初审通过
			if (isNotNull(row.checkNodeState) && "NO_CHECK" != row.checkNodeState) {
				if ("CHECK" == row.checkNodeState) {
					html = "信审初审通过待复核"
				} else if ("CHECK_PASS" == row.checkNodeState) {
					html = "信审初审通过复核通过"
				} else if ("CHECK_NO_PASS" == row.checkNodeState) {
					html = "信审初审通过复核拒绝"
				}
			} else {
				html = "信审初审通过";
			}
		} else if ("XSCS-REJECT" == value) { // 信审初审拒绝
			if (isNotNull(row.checkNodeState) && "NO_CHECK" != row.checkNodeState) {
				if ("CHECK" == row.checkNodeState) {
					html = "信审初审拒绝待复核";
				} else if ("CHECK_PASS" == row.checkNodeState) {
					html = "信审初审拒绝复核通过";
				} else if ("CHECK_NO_PASS" == row.checkNodeState) {
					html = "信审初审拒绝复核拒绝";
				}
			} else {
				html = "信审初审拒绝";
			}
		} else if ("XSCS-RETURN" == value) { // 初审退回录入
			if (isNotNull(row.checkNodeState) && "NO_CHECK" != row.checkNodeState) {
				if ("CHECK" == row.checkNodeState) {
					html = "初审退回录入待复核";
				} else if ("CHECK_PASS" == row.checkNodeState) {
					html = "初审退回录入复核通过";
				} else if ("CHECK_NO_PASS" == row.checkNodeState) {
					html = "初审退回录入复核拒绝";
				}
			} else {
				html = "初审退回录入";
			}
		}else if ("XSCS-ZDQQRETURN" == value) { // 初审退回前前
            if (isNotNull(row.checkNodeState) && "NO_CHECK" != row.checkNodeState) {
                if ("CHECK" == row.checkNodeState) {
                    html = "初审退回前前待复核";
                } else if ("CHECK_PASS" == row.checkNodeState) {
                    html = "初审退回前前复核通过";
                } else if ("CHECK_NO_PASS" == row.checkNodeState) {
                    html = "初审退回前前复核拒绝";
                }
            } else {
                html = "初审退回前前";
            }
        } else if ("HIGH-PASS" == value) { // 初审提交高审
			if (isNotNull(row.checkNodeState) && "NO_CHECK" != row.checkNodeState) {
				if ("CHECK" == row.checkNodeState) {
					html = "初审提交高审待复核";
				} else if ("CHECK_PASS" == row.checkNodeState) {
					html = "初审提交高审复核通过";
				} else if ("CHECK_NO_PASS" == row.checkNodeState) {
					html = "初审提交高审复核拒绝";
				}
			} else {
				html = "初审提交高审";
			}
		} else if ("XSCS-SUBMIT" == value) { // 终审分派办理
			html = "终审分派办理";
		} else if ("XSZS-ASSIGN" == value) { // 信审终审办理
			html = "信审终审办理";
		} else if ("XSZS-HANGUP" == value) { // 信审终审挂起
			html = "信审终审挂起";
		} else if ("XSZS-PASS" == value) { // 信审终审通过
			html = "信审终审通过";
		} else if ("XSZS-REJECT" == value) { // 信审终审拒绝
			html = "信审终审拒绝";
		} else if ("XSZS-RETURN" == value) { // 终审退回录入
			html = "终审退回录入";
		} else if ("XSZS-ZDQQRETURN" == value) { // 终审退回前前
			html = "终审退回前前";
		} else if ("XSZS-RTNCS" == value) { // 终审分配退回初审
			html = "终审分配退回初审";
		} else if ("XSZS-SUBMIT-HIGH" == value) { // 终审提交高审
			html = "终审提交高审";
		} else if ("XSZS-SUBMIT-BACK" == value) { // 终审回到终审
			html = "终审回到终审";
		} else if ("XSZS-SUBMIT-APPROVAL" == value) { // 终审提交协审
			html = "终审提交协审";
		} else if ("SQJWH-REJECT" == value) {// 申请件维护拒绝
			html = "申请件维护拒绝"
		}
		if (isNotNull(row.loanNo) && "无状态" == html && isNotNull(index)) {
			$.ajax({
				type : "POST",
				url : ctx.rootPath() + "/applyHistory/getLastOpertionState/" + row.loanNo,
				async : false,
				success : function(data) {
					if (data.success) {
						html = formatRtfNodeState(data.data.rtfNodeState, data.data, null);
					}
				}
			});
		}
	}
	return html;
}
/**
 * 格式化案件标识
 * 
 * @author dmz
 * @date 2017年4月12日
 * @param value
 * @param row
 * @param index
 */
function formatLoanMark(value, row, index) {
	var html = "";
	// 添加疑似欺诈
	if (isNotNull(row.ifSuspectCheat) && '1' == row.ifSuspectCheat) {
		html = "<img title='触发欺诈规则' src='" + ctx.rootPath() + "/resources/images/loanMark/cheat.png'>&nbsp;";
	}
	// 添加是否加急
	if (isNotNull(row.ifPri) && '1' == row.ifPri) {
		html = html + "<img title='加急件' src='" + ctx.rootPath() + "/resources/images/loanMark/urgent.png'>&nbsp;";
	}
	// 添加是否是app进件
	if (isNotNull(row.appInputFlag) && '1' == row.appInputFlag) {
		html = html + "<img title='APP进件' src='" + ctx.rootPath() + "/resources/images/loanMark/app.png'>&nbsp;";
	}
	// 添加费率优惠客户
	if (isNotNull(row.ifPreferentialUser) && "Y" == row.ifPreferentialUser) {
		html = html +  "<img title='费率优惠客户' src='" + ctx.rootPath() + "/resources/images/loanMark/rate.png'>&nbsp;";
	}
	// 添加是否是退回件
	if (isNotNull(row.ifNewLoanNo) && 0 == row.ifNewLoanNo) {
		var rtfNodeState = getRtfNodeState(row);
		if (isNotNull(rtfNodeState)) {
			if ('RTNCS' == rtfNodeState || 'CHECK_NO_PASS' == rtfNodeState) {
				html = html + "<img title='退回件' src='" + ctx.rootPath() + "/resources/images/loanMark/return.png'>&nbsp;";
			}
		}
	}
	//添加简化资料客户
	if(isNotNull(row.simplifiedDataUser) && "Y" == row.simplifiedDataUser){
    	html = html +  "<img title='简化资料客户' src='" + ctx.rootPath() + "/resources/images/loanMark/simple.png'>&nbsp;";
    }
    // 复议在申请
	if (isNotNull(row.ifReconsiderUser) && "Y" == row.ifReconsiderUser) {
        html = html +  "<img title='复议再申请' src='" + ctx.rootPath() + "/resources/images/loanMark/reconsider.png'>&nbsp;";
	}
	//前前进件
	if (isNotNull(row.zdqqApply) && 1 == row.zdqqApply) {
        html = html +  "<img title='证大前前' src='" + ctx.rootPath() + "/resources/images/loanMark/money.png'>&nbsp;";
	}
	return html;
}
/**
 * @Desc: 将金额格式化为千分位形式，如20000显示为20,000
 * 
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numberFomatter(value, row, index) {
	if (isNotNull(value)) {
		var reg = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
		if(reg.test(value)){
			return $.number(Number(value).toFixed(0), 0);
		}else{
			return '<span title="' + value + '" class="easyui-tooltip">' + value + '</span>';
		}
	}
}

/**
 * @Desc: 将金额格式化为千分位形式，如20000显示为20,000,并保留两位小数20,000.00
 * 
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numberFomatterTwoDecimal(value, row, index) {
	if (isNotNull(value)) {
		return $.number(Number(value).toFixed(2), 2);
	}
}


/**
 * 格式化案件标识（贾长星用）
 * 
 * @param value
 * @param row
 * @param index
 */
function formateCaseIdentification(value, row, index) {
	var html = "";
    // 添加疑似欺诈
    if (isNotNull(row.ifSuspectCheat) && '1' == row.ifSuspectCheat) {
        html = html + "<img title='触发欺诈规则' src='" + ctx.rootPath() + "/resources/images/loanMark/cheat.png'>&nbsp;";
    }
	// 添加是否加急
	if (isNotNull(row.ifPri) && '1' == row.ifPri) {
		html = "<img title='加急件' src='" + ctx.rootPath() + "/resources/images/loanMark/urgent.png'>&nbsp;";
	}
	// 添加是否是app进件
	if (isNotNull(row.appInputFlag) && '1' == row.appInputFlag) {
		html = html + "<img title='APP进件' src='" + ctx.rootPath() + "/resources/images/loanMark/app.png'>&nbsp;";
	}
	// 添加费率优惠客户
    if (isNotNull(row.ifPreferentialUser) && "Y" == row.ifPreferentialUser) {
        html = html +  "<img title='费率优惠客户' src='" + ctx.rootPath() + "/resources/images/loanMark/rate.png'>&nbsp;";
    }
    //添加简化资料客户
    if(isNotNull(row.simplifiedDataUser) && "Y" == row.simplifiedDataUser){
    	html = html +  "<img title='简化资料客户' src='" + ctx.rootPath() + "/resources/images/loanMark/simple.png'>&nbsp;";
    }
    // 复议在申请
    if (isNotNull(row.ifReconsiderUser) && "Y" == row.ifReconsiderUser) {
        html = html +  "<img title='复议再申请' src='" + ctx.rootPath() + "/resources/images/loanMark/reconsider.png'>&nbsp;";
    }
    // 前前进件
    if (isNotNull(row.zdqqApply) && 1 == row.zdqqApply) {
        html = html +  "<img title='证大前前' src='" + ctx.rootPath() + "/resources/images/loanMark/money.png'>&nbsp;";
    }
	return html;
}

/**
 * 格式化所在的队列
 * 
 * @author dmz
 * @date 2017年4月13日
 * @param value
 * @param row
 * @param index
 */
function formatInTask(value, row, index) {
	var html = "";
	// 信审初审
	if (isNotNull(row.rtfStatus) && "XSCS" == row.rtfStatus) {
		if (isNotNull(row.rtfNodeStatus) && "XSCS-HANGUP" == row.rtfNodeStatus) {
			html = "挂起队列";
			return html;
		}
		// 过滤复核确认退回的情况
		if (isNotNull(row.ifNewLoanNo) && isNotNull(row.rtfNodeStatus) && (row.rtfNodeStatus != 'XSCS-PASS' && row.rtfNodeStatus != 'XSCS-REJECT' && row.rtfNodeStatus != 'XSCS-RETURN' && row.rtfNodeStatus !="XSCS-ZDQQRETURN" && row.rtfNodeStatus != 'HIGH-PASS')) {
			if ("0" == row.ifNewLoanNo) {
				html = "优先队列";
			} else {
				html = "正常队列";
			}
		}
		return html;
	} else if (isNotNull(row.rtfStatus) && "XSZS" == row.rtfStatus) {// 信审终审
		if (isNotNull(row.rtfNodeStatus) && "XSZS-HANGUP" == row.rtfNodeStatus) {
			html = "挂起队列";
			return html;
		}

		if (isNotNull(row.ifNewLoanNo)) {
			if ("0" == row.ifNewLoanNo && (isNotNull(row.rtfNodeStatus) && "XSZS-RTNCS" != row.rtfNodeStatus)) {
				html = "优先队列";
			}
			if ("1" == row.ifNewLoanNo) {
				html = "正常队列";
			}
		}
	}
	return html;
}

/**
 * 格式化时间
 * 
 * @author dmz
 * @date 2017年5月9日
 * @param value
 * @param row
 * @param index
 */
function formatDate(value, row, index) {
	if (value != null) {
		return moment(value).format('YYYY-MM-DD HH:mm:ss');
	} else {
		return "";
	}
}
function formatDateYMDHS(value, row, index) {
	if (isNotNull(value)) {
		return moment(value).format('YYYY-MM-DD HH:mm');
	} else {
		return "";
	}
}
function formatDateYMD(value, row, index) {
	if (value != null) {
		return moment(value).format('YYYY-MM-DD');
	} else {
		return "";
	}
}
/** *************************************************************************************************************************************** */
/** *************************************************************************************************************************************** */
/**
 * 初始化一级二级原因
 * 
 * @Param elementAId
 *            需要被赋值的一级原因的元素id
 * 
 * @Param operationModule
 *            操作模块: SQLR 申请录入 LRFH 录入复核 XSCS 信审初审 XSZS 信审终审 HTQY 合同签约 HTQR 合同确认 FKSH 放款审核 FKQR 放款确认 ZSRTNLR终审退回门店 ZSRTNCS 终审退回初审 SQJXXWH 申请件信息维护
 * 
 * @Param operationType
 *            类型: return 返回 reject 拒绝 cancel 取消 hang挂起
 * 
 * @Param elementBId
 *            需要被赋值的二级原因的元素id
 * @param width 宽度默认320
 */

function initClassAReason(elementAId, operationModule, operationType, elementBId,width) {
	width = width || 320,
	initClassAReasonFirst(elementAId, operationModule, operationType, elementBId,width);
	if (isNotNull(elementBId)) {
		initClassAReasonSecond(elementBId,width)
	}
}
// 加载一级原因
function initClassAReasonFirst(elementAId, operationModule, operationType, elementBId,width) {
	$(elementAId).combobox({
		valueField : 'code',
		textField : 'reason',
		required : true,
		editable : false,
		width : width,
		value : $(elementAId).val(),
		queryParams : {
			operationModule : operationModule,
			operationType : operationType,
			type : "1"
		},
		url : ctx.rootPath() + "/bmsBasiceInfo/getReasonList",
		onChange : function(newValue, oldValue) {
			$(elementAId).parents("td").find("input[name='firstReasonText']").val($(elementAId).combobox("getText"));
			if (isNotNull(elementBId)) {
				$(elementBId).combobox("setValue", "");
				$(elementBId).parents("td").find("input[name='secondReasonText']").val("");
             	// 如果有多个一级原因需要判断一级原因不能重复
                var parentsTd = $(this).parents("td");
                if ($(elementAId).parents("form").find("input[name='firstReason']").length > 1) {
                    $(elementAId).parents("form").find("input[name='firstReason']").each(function (ind, obj) {
                        if (isNotNull($(obj).val()) && !$(obj).parents("td").is(parentsTd) && newValue == $(obj).val()) {
                            $.info("提示", "原因重复请重新选择");
                            $(parentsTd).find("input:first").combobox("setValue","");
                            $(elementBId).combobox("loadData", {});
                            return false;
                        }
                    });
                }
                var defaultVal = $(this).combobox("getValue");
                if ("" != defaultVal) {
                    var id = $(this).combobox("options").finder.getRow(this, defaultVal).id;
                    $(elementBId).combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getReasonList?type=2&id=" + id);
                }
			}
		},
	});
}
// 加载二级原因
function initClassAReasonSecond(elementBId,width) {
	$(elementBId).combobox({
		valueField : 'code',
		textField : 'reason',
		required : true,
		width : width,
		editable : false,
		value : $(elementBId).val(),
		onLoadSuccess : function(data) {
			if (data == null || data.length == 0) {
				$(elementBId).combobox("disableValidation");
			} else {
				$(elementBId).combobox("enableValidation");
			}
		},
		onChange : function() {
			$(elementBId).parents("td").find("input[name='secondReasonText']").val($(elementBId).combobox("getText"));
		},
	});
}

/**
 * 关闭htmlWindow窗口
 */
function closeHTMLWindow() {
	window.close();
}
/**
 * 关闭集合类型htmlWindow窗口
 * 
 * @author dmz
 * @date 2017年7月5日
 * @param list
 */
function closeHTMLWindowList(list) {
	if (isNotNull(list)) {
		for ( var i in list) {
			list[i].close();
		}
	}
}
/**
 * 初审工作台状态转换
 * 
 * @param value
 * @param row
 * @param index
 * @returns {String}
 */
function formatWorkbenchRtfNodeState(value, row, index) {
	var html = "";
	var rtfNodeState = getRtfNodeState(row);
	if (isNotNull(rtfNodeState)) {
		if ('RTNCS' == rtfNodeState) {
			html = '终审退回';
		} else if ('RETURN' == rtfNodeState) {
			html = '门店重提';
		} else if ('CHECK_NO_PASS' == rtfNodeState) {
			html = '复核退回';
		}
	}
	return html;
}

/**
 * 通过审批历史记录来查询审核节点状态
 * 
 * @param row
 * @returns {String}
 */
function getRtfNodeState(row) {
	var rtfNodeStateResult = '';
	if (isNotNull(row.ifNewLoanNo) && 0 == row.ifNewLoanNo) {
		$.ajax({
			type : "POST",
			url : ctx.rootPath() + "/applyHistory/getWorkbenchStateByLoanNo/" + row.loanNo,
			async : false,
			success : function(data) {
				if (data.success) {
					var result = data.data;
					if (isNotNull(result)) {
						var rtfState = result.rtfState;
						var rtfNodeState = result.rtfNodeState;
						var checkNodeState = result.checkNodeState;
						if ('XSZS' == rtfState && 'XSZS-RTNCS' == rtfNodeState) {
							rtfNodeStateResult = 'RTNCS';
						} else if (('XSZS' == rtfState && 'XSZS-ZDQQRETURN' == rtfNodeState) || ('XSZS' == rtfState && 'XSZS-RETURN' == rtfNodeState) || ('XSCS' == rtfState && ('XSCS-RETURN' == rtfNodeState || "XSCS-ZDQQRETURN" == rtfNodeState) && 'NO_CHECK' == checkNodeState)||('XSCS' == rtfState && ('XSCS-RETURN' == rtfNodeState || "XSCS-ZDQQRETURN" == rtfNodeState)&& 'CHECK_PASS' == checkNodeState)||('CSFP' == rtfState && ('CSFP-RETURN' == rtfNodeState || "CSFP-ZDQQRETURN" == rtfNodeState))) {
							rtfNodeStateResult = 'RETURN';
						} else if (isNotNull(checkNodeState) && 'CHECK_NO_PASS' == checkNodeState) {
							rtfNodeStateResult = 'CHECK_NO_PASS';
						}
					}
				}
			}
		});
	}
	return rtfNodeStateResult;
}

/**
 * combobox复选框，当选择的某一项时，去掉手动输入的内容，只留下满足条件的数据
 */
function onSelect() {
	var currentElementName = this.getAttribute("comboname");
	var valueField = $(this).combobox("options").valueField;
	var textField = $(this).combobox("options").textField;
	var currentVals = $(this).combobox("getValues"); // 当前combobox的所有值
	var allData = $(this).combobox("getData"); // 获取combobox所有原始数据
	var newVals = new Array();
	var newText = "";
	for (var m = 0; m < currentVals.length; m++) {
		OK: for (var i = 0; i < allData.length; i++) {
			if (currentVals[m] == allData[i][valueField]) {
				newVals.push(currentVals[m]);
				newText = newText + allData[i][textField] + "&#10;";
				break OK;
			}
		}
	}
	$(this).combobox("setValues", newVals);

	var calenderObj = $(this).next().next()[0];
	if(newVals.length > 0){
		var firstValue = newVals[0];
		var firstText = "";
		for(var j = 0; j < allData.length; j++){
			if(firstValue == allData[j][valueField]){
				firstText = allData[j][textField];
				break;
			}
		}
		$(this).combobox("setText", firstText + "(" + newVals.length + ")");
		
	    calenderObj.innerHTML = '<a class="redPointTooltip" title='+newText+'>'+newVals.length+'</a>';
	}else{
		calenderObj.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
	}
}

/**
 * combobox复选框，当去掉选择的某一项时，去掉手动输入的内容，只留下满足条件的数据
 */
function onUnselect() {
	var currentElementName = this.getAttribute("comboname");
	var valueField = $(this).combobox("options").valueField;
	var textField = $(this).combobox("options").textField;
	var currentVals = $(this).combobox("getValues"); // 当前combobox的所有值
	var allData = $(this).combobox("getData"); // 获取combobox所有原始数据
	var newVals = new Array();
	var newText = "";
	for (var m = 0; m < currentVals.length; m++) {
		OK: for (var i = 0; i < allData.length; i++) {
			if (currentVals[m] == allData[i][valueField]) {
				newVals.push(currentVals[m]);
				newText = newText + allData[i][textField] + "&#10;";
				break OK;
			}
		}
	}
	$(this).combobox("setValues", newVals);
	var calenderObj = $(this).next().next()[0];
	if(newVals.length > 0){
		var firstValue = newVals[0];
		var firstText = "";
		for(var j = 0; j < allData.length; j++){
			if(firstValue == allData[j][valueField]){
				firstText = allData[j][textField];
				break;
			}
		}
		$(this).combobox("setText", firstText + "(" + newVals.length + ")");
		
	    calenderObj.innerHTML = '<a class="redPointTooltip" title='+newText+'>'+newVals.length+'</a>';
	}else{
		calenderObj.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
	}
}

/*combox复选框，可手动输入搜索，值改变的时候，如果为空，去掉后面的提示图标*/
function onValueChange(){
	var currentText = $(this).combobox("getText"); // 当前combobox的所有值
	if(isEmpty(currentText)){
		var calenderObj = $(this).next().next()[0];
		calenderObj.innerHTML = '<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
	}
}

// 自定义函数向后插入
function insertAfter(newElement, targetElement) {
	var parent = targetElement.parentNode;
	if (parent.lastChild == targetElement) {
		// 如果最后的节点是目标元素，则直接添加。因为默认是最后
		parent.append(newElement);
	} else {
		// 如果不是，则插入在目标元素的下一个兄弟节点的前面。也就是目标元素的后面。
		parent.insertBefore(newElement, targetElement.nextSibling);
	}
}

/**
 * 根据id初始化日期框为年-月
 * 
 * @param id
 */
function initYMDateBox(id){
	$(id).datebox({
		width : 210,
		formatter : myformatter,
		parser : myparser
	});
}

function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m);
}
function myparser(s) {
	s = formatDateYMD(s);
	if (!s)
		return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m)) {
		return new Date(y, m - 1);
	} else {
		return new Date();
	}
}


/**
 * 数组去重
 * 
 * @param a
 * @returns {Array}
 */
function arrayDuplicateRemoval(a) {
	var hash = {}, len = a.length, result = [];

	for (var i = 0; i < len; i++) {
		if (!hash[a[i]]) {
			hash[a[i]] = true;
			result.push(a[i]);
		}
	}
	return result;
}


/**
 * combobox检索扩展--包含拼音（全拼，简拼等），汉字
 * 
 * @param q
 * @param row
 * @returns {Boolean}
 */
function filterCombo(q, row) {
	var opts = $(this).combobox('options');
	var keys = new Array();
	keys[keys.length] = opts.textField;
	keys[keys.length] = opts.valueField;
	return filterComboboxData(q, row, keys);
}

function filterComboboxData(q, row, keys) {
	var result;
	if (keys && keys.length > 0) {
		for (var i = 0; i < keys.length; i++) {
			if (escape(q).indexOf("%u") != -1) {// 汉字
				result = row[keys[i]].indexOf(q) > -1;
			} else {
				result = ConvertPinyin(row[keys[i]]).indexOf(q) > -1;
			}
			if (result == true) {
				return true;
			}
		}
	} else {
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q) > -1;
	}
}


/**
 * 获取两个日期相差多少天
 * 
 * @param strDateStart
 *            yy-mm-dd
 * @param strDateEnd
 *            yy-mm-dd
 * @returns
 */
function getDifferDays(strDateStart, strDateEnd) {
	if(isEmpty(strDateStart) || isEmpty(strDateEnd)){
		return 0;
	}
	var strSeparator = "-"; // 日期分隔符
	var oDate1, oDate2, iDays;
	oDate1 = strDateStart.split(strSeparator);
	oDate2 = strDateEnd.split(strSeparator);
	var strDateS = new Date(oDate1[0], oDate1[1] - 1, oDate1[2]);
	var strDateE = new Date(oDate2[0], oDate2[1] - 1, oDate2[2]);
	iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24)// 把相差的毫秒数转换为天数
	return iDays;
}

/**
 * 比较两个日期大小
 * 
 * @returns
 * t1 > t2，返回true
 */
function compareDate(t1,t2) {
	// 2把字符串格式转换为日期类
	var startTime = new Date(Date.parse(t1));
	var endTime = new Date(Date.parse(t2));

	// 3进行比较
	return startTime > endTime;
}

//扩展方法，修改rownumber宽度
$.extend($.fn.datagrid.methods, {  
      fixRownumber :function(jq) {  
            return jq.each(function() {  
                  var panel = $(this).datagrid("getPanel");  
                  //获取最后一行的number容器,并拷贝一份  
                  var clone = $(".datagrid-cell-rownumber", panel).last().clone();  
                  //由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下  
                  clone.css({  
                        "position":"absolute",  
                        left : -1000  
                  }).appendTo("body");  
                  var width = clone.width("auto").width();  
                  //默认宽度是25,所以只有大于25的时候才进行fix  
                  if(width > 25) {  
                        //多加5个像素,保持一点边距  
                        $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);  
                        //修改了宽度之后,需要对容器进行重新计算,所以调用resize  
                        $(this).datagrid("resize");  
                        //一些清理工作  
                        clone.remove();  
                        clone =null;  
                  }else{  
                        //还原成默认状态  
                        $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");  
                  }  
            });  
      }  
});


/**
 * 格式化产品-如果是证大前前显示为空
 * @param value
 */
function formatterProduct(value, row, index) {
	if (isNotNull(value)) {
		if ("证大前前" == value) {
			return "";
		} else {
			return value;
		}
	} else {
		return "";
	}
}