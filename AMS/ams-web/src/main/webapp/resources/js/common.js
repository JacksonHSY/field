function post(api, params, dataType, callback, error, async) {
	var timestamp = Date.parse(new Date());
	api = api + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		dataType : dataType,
		method : 'post',
		async: async || false,
		data : params,
		success : callback,
		error : error || function(data) {
			$.info("警告", '系统繁忙，请稍后再试！', "warning");
		}
	});
}

function load(api) {
	var timestamp = Date.parse(new Date());
	api = api + "?timestamp=" + timestamp;
	$.ajax({
		url : api,
		success : function(result) {
			if (result.indexOf('登录页面') >= 0) {
				window.location.href = '/';
			} else {
				$('.main').html(result);
			}
		}
	});
}

/** 格式当前时间 带时分秒 yyyy-MM-dd HH:mm:ss */
function getLocalTime(milliseconds) {
	var datetime = new Date();
	datetime.setTime(milliseconds);
	var year = datetime.getFullYear();
	// 月份重0开始，所以要加1，当小于10月时，为了显示2位的月份，所以补0
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
	var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
	var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
	var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
	return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}

/** 时间戳=>本地时间 */
function getLocalTimeDate(milliseconds) {
	var datetime = new Date();
	datetime.setTime(milliseconds);
	var year = datetime.getFullYear();
	// 月份重0开始，所以要加1，当小于10月时，为了显示2位的月份，所以补0
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
	var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
	var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
	var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
	return year + "-" + month + "-" + date;
}

/**
 * 判断value是否不为空
 * @param value
 * @returns {boolean}
 */
function isNotNull(value) {

	return !isEmpty(value);
}

/**
 * 判断value是否为空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {

	return /(null|undefined|^(?![\s\S]))/.test(value);
}

/**
 * 判断给定的数据是否是空的
 * 字符串	-- 不是null，不是undefined，而且length不为0
 * 数组		-- 不是null，不是undefined，而且length不为0
 * 数字		-- 不是null，不是undefined
 * 
 * @param obj
 * 
 * @returns boolean true表明是空的
 */
function isEmptyObj(obj){
	if(obj != null && obj != undefined){
		if ((typeof str == 'string') && str.constructor == String) {// 如果是字符串
			return !(obj.length > 0);
		} else if ((typeof obj == 'object') && obj.constructor == Array) {// 如果是数组
			return !(obj.length > 0);
		} else if ((typeof obj == 'number') && obj.constructor == Number) {// 如果是数值
			return false;
		}
	}else{
		return true;
	}
}

/**
 * 规则引擎提示js
 * 
 * @author dmz
 * @date 2017年7月31日
 */
function ruleEngineHintOver() {
	document.getElementById("ruleEngineHint_div").style.display = "block";
}
function ruleEngineHintOut() {
	document.getElementById("ruleEngineHint_div").style.display = "none";
}
/**
 * 控制规则引擎是否显示
 * 
 * @author dmz
 * @date 2017年8月4日
 * @param length
 */
function ruleEngineHintShow(length) {
	if (isNotNull(length) && parseInt(length) > 0) {
		$("#ruleEngineHint_parent_div").bind("mouseover",ruleEngineHintOver);
	} else {
		$("#ruleEngineHint_parent_div").unbind("mouseover");
	}
}

/**
 * 格式化所有numFormat样式的数字，添加千位分隔符，保留0位小数
 */
function formatThousandSymbol() {
	$('.numFormat').each(function(index, element) {
		if ($(element).hasClass('textbox-f') && !$(element).hasClass('easyui-numberbox')) { // easyui的text-box
			var $numberText = $(element).next('span').find('.textbox-text');
			$numberText.number(true, 0);

		} else if ($(element).hasClass('textbox-f') && $(element).hasClass('numberbox-f')) { // easyui的number-box
			var $numberText = $(element).next('span').find('.textbox-text');
			$numberText.number(true, 0);

		} else {
            // 普通的文本
			var innerText = $(element).text();
			var innerTextArray = /\d+/.exec(innerText);
			if(innerTextArray && innerTextArray.length > 0){
				var innerNumber = innerTextArray[0];		// 提取数字部分内容
				var innerNumberIdx = innerTextArray.index;	// 提取数字部分下标
				// 拼接数字部分和其他部分
				if(innerNumber.length == innerText.length){
					$(element).text($.number(innerNumber));
				}else{
					$(element).text(innerText.substring(0, innerNumberIdx) + $.number(innerNumber) + innerText.substring(innerNumberIdx + innerNumber.length));
				}
			}
		}
	});
}

$(function() {
	setTimeout(function() {
		formatThousandSymbol();
	}, 1000);
});

/**
 * TODO 临时解决借新还旧客户标识
 * @param cardId
 * @returns {boolean}
 */
var oldCardIdMap = null;
function getOldCardIdExists(cardId) {
	var  action = false;
	if (isNotNull(cardId)) {
		if (!isNotNull(oldCardIdMap)) {
            $.ajax({
                url: ctx.rootPath()+"/resources/js/oldCardIdJson.json",//json文件位置
                type: "POST",
                dataType: "json", //返回数据格式为json
                async:false,
                success: function(data) {//请求成功完成后要执行的方法
                    oldCardIdMap = isNotNull(data) ? data : null;
                }
            })
		}
		if (isNotNull(oldCardIdMap)) {
			action = isNotNull(oldCardIdMap[cardId]);
		}
	}
	return action;
}