//@ sourceURL=suanHuaExternalCredit.js
//定义过滤器
/**
 * 字符转时间
 * @param value-被转参数
 * @param format-时间格式类型(默认:yyyy-MM-dd)
 * @param valueFormat-值的默认格式(默认:YYYYMMDD)
 */
avalon.filters.strToDate = function(value, format,valueFormat){
    if(isNotNull(value)){
        format = format || 'YYYY-MM-DD';
        valueFormat = valueFormat || 'YYYYMMDD';
        return  moment(value,valueFormat).format(format);
    }
};
var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        loanNo: '',
		suanHuanCreditObj:{},// 算话征信报告
		suanHuanQueryTimes:{},// 算话查询次数
        suanHuanObjMessage: '',// 算话查询提示信息
        suanHuanTimesMessage:'', // 算话查询次数信息提示
        indicator:{"cash":"信用贷款","circle":" 循环租赁业务","lease":" 融资租赁业务","estate":"房产抵押贷款","car":"车辆抵押贷款","other":"其他业务"},//信用提示和授信及负债概要(未结清)
        specTrade:{"extends":"展期","angents":"担保人代还","leases":"以资抵债","over30":"逾期1-29天","over60":"逾期30-59","over90":"逾期60-89","overL":"长期拖欠(90天以上)","illegal":"法律诉讼(已判决生效)","cheat":"欺诈","advance":"提前还款","other":"其他"},//异常交易信息
        getSexType:function(key){// 获取性别
        	return getSexType(key);
		},
		getCardIdType:function (key) {// 获取证件类型
			return getCardIdType(key);
        },
		getMarriageType:function(key) {// 婚姻类型
			return getMarriageType(key);
		},
		getEducationType: function (key) {// 学历类型
			return getEducationType(key);
		},
		getLiveType:function (key) {// 居住类型
			return getLiveType(key);
		},
		getCompactType:function (key){// 合同状态类型
			return getCompactType(key);
		},
		getGuaranteeType:function (key){//担保方式
			return getGuaranteeType(key);
		},
		getRepayFreqType:function (key) {// 还款频率类型
			return getRepayFreqType(key);
        },
		getGuaranteeStatusType:function (key) {// 担保状态
			return getGuaranteeStatusType(key);
		},
		getQueryReasonType:function(key) {// 查询原因类型
			return getQueryReasonType(key);
		},
        getBusinessType:function (key) { //获取信贷业务类型
            return getBusinessType(key);
        },
        isEmpty: function (object) {
            return _.isEmpty(object);
        }

    };

    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);

    avalon.scan(document.body);
}

avalon.ready(function () {
    getSuanhuaCredit(vm.loanNo);			 // 算话征信
});

/**
 * 算话征信
 * @param loanNo
 */
function getSuanhuaCredit(loanNo) {
	post(ctx.rootPath()+"/creditzx/getSuanHuaCredit/" + loanNo, null,"json", function (result){
		if(result.success) {
            if (isNotNull(result.data) && isNotNull(result.data.data) ) {//// 算话信息查询
                if ((result.data.data.Credit && !isEmptyObj(result.data.data.Credit.guaranteeInfos) && !isEmptyObj(result.data.data.Credit.creditInfos)) && (result.data.data.Person || result.data.data.SpecTrade)) {
                    vm.suanHuanCreditObj = result.data.data;
                } else {
                    vm.suanHuanObjMessage = "查无结果";
                }
            } else {
                vm.suanHuanObjMessage = "查无结果";
            }
            if (isNotNull(result.data) && isNotNull(result.data.queryRecordData)) { // 算话查询次数
                vm.suanHuanQueryTimes = result.data.queryRecordData;
            } else {
                vm.suanHuanTimesMessage = "查无结果";
            }
		} else {
			vm.suanHuanTimesMessage =result.firstMessage;
			vm.suanHuanObjMessage = result.firstMessage;
		}
	});
}

/**
 * 获取性别类型
 * @param value
 */
function getSexType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
			case '0':
				action = "未知性别";
				break;
			case '1':
				action = "男性";
				break;
			case '2':
				action ="女性";
				break;
			case '9':
				action ='未说明性别';
				break;
			default:
		}
	}
	return action;
}

/**
 * 获取证件类型
 * @param key
 */
function getCardIdType(key){
	var action = "";
	if (isNotNull(key)){
		switch (key+'') {
			case '0':
				action='身份证';
				break;
			case '1':
				action ='户口簿';
				break;
            case '2':
                action ='护照';
                break;
            case '3':
                action ='军官证';
                break;
            case '4':
                action ='士兵证';
                break;
            case '5':
                action ='港澳居民往来内地通行证';
                break;
			case '6':
				action ='台湾同胞往来内地通讯证';
				break;
            case '7':
                action ='临时身份证';
                break;
            case '8':
                action ='外国人局留证';
                break;
            case '9':
                action ='警官证';
                break;
            case 'A':
                action ='香港身份证';
                break;
            case 'B':
                action ='澳门身份证';
                break;
            case 'X':
                action ='其他证件';
                break;
			default:
		}
	}
	return action;
}
/**
 * 获取婚姻状态
 * @param key
 */
function getMarriageType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
            case '10':
                action ='未婚';
                break;
            case '20':
                action ='已婚';
                break;
            case '30':
                action ='丧偶';
                break;
            case '40':
                action ='离婚';
                break;
            case '90':
                action ='未说明婚姻状况';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取学历类型
 * @param key
 */
function getEducationType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+''){
            case '10':
                action ='研究生及以上';
                break;
            case '20':
                action ='大学本科';
                break;
            case '30':
                action ='大学专科或专科学校';
                break;
            case '40':
                action ='中等专业学校或中等技术学院';
                break;
            case '50':
                action ='技术学校';
                break;
            case '60':
                action ='高中';
                break;
            case '70':
                action ='初中';
                break;
            case '80':
                action ='小学';
                break;
            case '90':
                action ='文盲或半文盲';
                break;
            case '99':
                action ='未知';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取居住状态
 * @param key
 */
function getLiveType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
            case '1':
                action ='自置(自建,自购)无贷款或贷款已还清';
                break;
            case '2':
                action ='按揭';
                break;
            case '3':
                action ='亲属楼宇';
                break;
            case '4':
                action ='集体宿舍';
                break;
            case '5':
                action ='租房';
                break;
            case '6':
                action ='共有住宅';
                break;
            case '7':
                action ='其他';
                break;
            case '9':
                action ='未知';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取
 * @param key
 */
function getCompactType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
            case '0':
                action ='正常未结清';
                break;
            case '1':
                action ='代偿未结清';
                break;
            case '2':
                action ='正常结清';
                break;
            case '3':
                action ='代偿结清';
                break;
            case '4':
                action ='呆账';
                break;
            case '5':
                action ='核销';
                break;
            case '6':
                action ='以资抵债';
                break;
            case '7':
                action ='提前还款未结清';
                break;
            case '8':
                action ='提前还款结清';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取担保方式
 * @param key
 */
function getGuaranteeType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
            case '1':
                action ='质押(含保证金)';
                break;
            case '2':
                action ='抵押';
                break;
            case '3':
                action ='自然人保证';
                break;
            case '4':
                action ='信用/免担保';
                break;
            case '5':
                action ='组合(含自然人保证)';
                break;
            case '6':
                action ='组合(不含自然人保证)';
                break;
            case '7':
                action ='农户联保';
                break;
            case '9':
                action ='其他';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取还款频率
 * @param key
 */
function getRepayFreqType(key) {
	var action="";
	if (isNotNull(key)) {
		switch (key+'') {
            case '1':
                action ='日';
                break;
            case '2':
                action ='周';
                break;
            case '3':
                action ='月';
                break;
            case '4':
                action ='季';
                break;
            case '5':
                action ='半年';
                break;
            case '6':
                action ='年';
                break;
			case '7':
				action ='一次性';
				break;
            case '8':
                action ='不定期';
                break;
            case '99':
                action ='其他';
                break;
			default:
		}
	}
	return action;
}

/**
 * 担保状态
 * @param key
 */
function getGuaranteeStatusType(key) {
	var action = "";
	if (isNotNull(key)){
		switch (key+'') {
            case '1':
                action ='担保中';
                break;
            case '2':
                action ='担保解除';
                break;
			default:
		}
	}
	return action;
}

/**
 * 获取查询原因
 * @param key
 * @returns {string}
 */
function  getQueryReasonType(key) {
	var action = "";
	if (isNotNull(key)) {
		switch (key+'') {
			case "1":
				action ="借贷审查";
				break;
            case "2":
                action ="贷后管理";
                break;
            case "3":
                action ="其他授信查询";
                break;
            case "4":
                action ="授信后管理";
                break;
            case "5":
                action ="个人查询";
                break;
            case "6":
                action ="异议查询";
                break;
			default:
		}
	}
	return action;
}

/**
 * 业务类型
 * @param key
 */
function  getBusinessType(key) {
    var action = "";
    if (isNotNull(key)) {
        switch (key+'') {
            case "1":
                action ="信用贷款-现金贷(大于30天)";
                break;
            case "2":
                action ="循环贷款业务";
                break;
            case "3":
                action ="融资租赁业务";
                break;
            case "5":
                action ="房产抵押贷款";
                break;
            case "6":
                action ="车辆抵押贷款";
                break;
            case "7":
                action ="信用贷款-短期现金贷(<=30天)";
                break;
            case "8":
                action ="信用贷款-消费分期贷(有业务场景)";
                break;
            case "z":
                action ="其他业务";
                break;
            default:
        }
    }
    return action;
}