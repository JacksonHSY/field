/**
 * @title 身份证号合法性验证
 * @author fuhongxing
 * @description 根据<<中华人民共和国国家标准 GB11643-1999>>中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
 *              排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
 *              地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
 *              出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
 *              顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
 *              校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
 * 
 * 出生日期计算方法。
 * 15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;2000年后出生的肯定都是18位的了没有这个烦恼
 * 正则表达式: 15位校验规则 6位地址编码+6位出生日期+3位顺序号 18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
 * 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
 */

// 禁用空格
$(function() {
	// 输入身份证时禁用空格
	$("#idcard").keydown(
			function() {
				var event = event || window.event || arguments.callee.caller.arguments[0];
				if (event.keyCode == 32) {
					event.returnValue = false;
				}
			});
})

var vcity = {
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

/**
 * 身份证验证
 * @returns {Boolean}
 */
function validateIdCard() {

	var card = document.getElementById('idcard').value;
	// 是否为空
	if (card === '') {
		showAlert('身份证号不能为空,请输入身份证号');
		// document.getElementById('idcard').focus;
		return false;
	}
	// 校验长度，类型
	if (isCardNo(card) === false) {
		showAlert('身份证号码不正确,请重新输入');
		return false;
	}
	// 检查省份
	if (checkProvince(card) === false) {
		showAlert('身份证号码不正确,请重新输入');
		return false;
	}
	// 校验生日
	if (checkBirthday(card) === false) {
		return false;
	}
	// 检验位的检测
	/*
	 * if(checkParity(card) === false) { showAlert('您的身份证校验位不正确,请重新输入'); return
	 * false; }
	 */
	return true;
};

// 检查号码是否符合规范，包括长度，类型
isCardNo = function(card) {
	// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
	var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
	if (reg.test(card) === false) {
		return false;
	}

	return true;
};

// 取身份证前两位,校验省份
checkProvince = function(card) {
	var province = card.substr(0, 2);
	if (vcity[province] == undefined) {
		return false;
	}
	return true;
};

// 检查生日是否正确
checkBirthday = function(card) {
	var len = card.length;
	// 身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
	if (len == '15') {
		var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
		var arr_data = card.match(re_fifteen);
		var year = arr_data[2];
		var month = arr_data[3];
		var day = arr_data[4];
		var birthday = new Date('19' + year + '/' + month + '/' + day);
		return verifyBirthday('19' + year, month, day, birthday);
	}
	// 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
	if (len == '18') {
		var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
		var arr_data = card.match(re_eighteen);// 使用正则表达式模式对字符串执行查找，并将包含查找的结果作为数组返回;匹配则返回结果数组，不匹配返回null
		var year = arr_data[2];
		var month = arr_data[3];
		var day = arr_data[4];
		var birthday = new Date(year + "/" + month + "/" + day);
		if (month > 12) {
			//showAlert("身份证号码不正确,请重新输入");
			return false;
		}
		if (day > 31) {
			//showAlert("身份证号码不正确,请重新输入");
			return false;
		}
		return verifyBirthday(year, month, day, birthday);
	}
	return false;
};

// 校验日期
verifyBirthday = function(year, month, day, birthday) {
	var now = new Date();
	var now_year = now.getFullYear();
	// 年月日是否合理
	if (birthday.getFullYear() == year && (birthday.getMonth() + 1) == month
			&& birthday.getDate() == day) {
		// 判断年份的范围（18岁到200岁之间)
		var time = now_year - year;
		if (time >= 0 && time < 18) {
			//showAlert("暂不对未成年人士开放理财功能");
			return false;
		}
		if (time >= 18 && time <= 200) {
			return true;
		} else {
			//showAlert('身份证号码不正确,请重新输入');
		}
		return false;
	}
	return false;
};

// 校验位的检测
/*
 * checkParity = function(card){ //15位转18位 card = changeFivteenToEighteen(card);
 * var len = card.length; if(len == '18') { var arrInt = new Array(7, 9, 10, 5,
 * 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); var arrCh = new Array('1', '0', 'X',
 * '9', '8', '7', '6', '5', '4', '3', '2'); var cardTemp = 0, i, valnum; for(i =
 * 0; i < 17; i ++) { cardTemp += card.substr(i, 1) * arrInt[i]; } valnum =
 * arrCh[cardTemp % 11]; if (valnum == card.substr(17, 1)) { return true; }
 * return false; } return false; };
 * 
 * //15位转18位身份证号 changeFivteenToEighteen = function(card){ if(card.length ==
 * '15') { var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
 * 4, 2); var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4',
 * '3', '2'); var cardTemp = 0, i; card = card.substr(0, 6) + '19' +
 * card.substr(6, card.length - 6); for(i = 0; i < 17; i ++) { cardTemp +=
 * card.substr(i, 1) * arrInt[i]; } card += arrCh[cardTemp % 11]; return card; }
 * return card; };
 */

function showSex(val) {
	var birthdayValue;
	var sex = "";
	var sr = "";
	// 15位身份证号码
	if (15 == val.length) { 
		birthdayValue = val.charAt(6) + val.charAt(7);
		if (parseInt(birthdayValue) < 10) {
			birthdayValue = '20' + birthdayValue;
		} else {
			birthdayValue = '19' + birthdayValue;
		}
		birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11);
		if (parseInt(val.charAt(14) / 2) * 2 != val.charAt(14)){
			sex = '男';
		}else{
			sex = '女';
		}
		sr = birthdayValue;
	}
	// 18位身份证号码
	if (18 == val.length) { 
		birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11) + '-' + val.charAt(12) + val.charAt(13);
		if (parseInt(val.charAt(16) / 2) * 2 != val.charAt(16)){
			sex = '男';
		}else{
			sex = '女';
		}
		sr = birthdayValue;
	}

	return sex;
}