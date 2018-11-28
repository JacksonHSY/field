avalon.config({debug:false});
var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        loanNo: '',
        education: '',
        customerName: '',
        customerIdCard: '',
        mobileOnlineList: [],
        creditData:{},
        getArrayByType: getArrayByType,
        getContactList: getContactList,
        checkSpouse: checkSpouse
    };

    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);

    avalon.scan(document.body);
}

avalon.ready(function () {
    queryCreditById(vm.loanNo);				 // 上海资信
    // 查询外部征信提示
    post(ctx.rootPath() + "/ruleEngineParameter/getRuleEngineParameterLast/" + vm.loanNo, null, "json", function(data) {
        if (isNotNull(data) && isNotNull(data.externalCreditHints)) {
            var message = "";
            var messageList = data.externalCreditHints.split("$");
            if (messageList.length > 0) {
                for (var mes in messageList) {
                    message +=messageList[mes] +"<br>";
                }
            } else  {
                message = data.externalCreditHints;
            }
            $.messager.alert('外部征信提示', message , 'warning');
        }
    });
});

function queryCreditById(loanNo) {
    post(ctx.rootPath()+"/creditzx/queryCreditById/"+loanNo, null,null, function (result) {
        if (result.success) {
            vm.creditData = result.data;
        }
    });
}

/**
 * 判断是否有配偶信息
 * @param instance
 */
function checkSpouse(instance){
    var name = instance['个人身份信息']['配偶姓名'];
    var telNum = instance['个人身份信息']['配偶联系电话'];
    var birthDay = instance['个人身份信息']['配偶出生日期'];
    var gender = instance['个人身份信息']['配偶性别'];
    var idNum = instance['个人身份信息']['配偶证件号码'];
    var idType = instance['个人身份信息']['配偶证件类型'];
    var company = instance['个人身份信息']['配偶工作单位'];

    if(isNotNull(name) || isNotNull(telNum) ||isNotNull(birthDay) ||isNotNull(gender) ||isNotNull(idNum) ||isNotNull(idType) ||isNotNull(company)){
        return true;
    }

    return false;
}

/**
 * 获取联系人信息
 * @param instance
 */
function getContactList(instance){
    var firstContactList = getArrayByType(instance['第一联系人信息']);
    _.map(firstContactList, function(item){
        item['type'] = '第一联系人';
        return item;
    });

    var secondContactList = getArrayByType(instance['第二联系人信息']);
    _.map(secondContactList, function(item){
        item['type'] = '第二联系人';
        return item;
    });

    return _.union(firstContactList, secondContactList);
}

/**
 * 获取上海资信报告中的数组
 * @param instance
 * @returns {*}
 */
function getArrayByType(instance) {
    if(instance instanceof Array){

        return instance;
    }else{
        // 不是数组，则构建为数组
        var arr = [];
        if(isNotNull(instance)){
            arr.push(instance);
        }

        return arr;
    }
}