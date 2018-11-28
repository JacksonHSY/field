/**
 * 信审常用校验规则库
 * @author wulj
 * @date 2017/11/17
 * @link {https://ourcodeworld.com/articles/read/37/how-to-create-your-own-javascript-library}
 */
;(function (define) {
    define(['jquery', 'underscore'], function ($) {
        return (function () {
            // 默认验证规则
            var defaultOptions = {
                mobile: {   // 手机号
                    validator: function (value, param) {

                        return new RegExp(regexp.mobile).test(value);
                    },
                   // message: "只允许录入11位数字，且不能录入11个相同数字！"
                    message: "电话号码不合法，请重新输入"
                },
                telNum: { // 座机号
                    validator: function (value, param) {

                        return new RegExp(regexp.tel).test(value);
                    },
                    message: '电话号码不合法，请重新输入'
                },
                numberMultiple: {   // 给定值的倍数
                    validator: function (value, param) {
                        if (value.indexOf(",") >= 0) {
                            value = removeComma(value);//取消字符串中出现的所有逗号
                        }
                        if (isNotNull(value)) {
                            return (parseInt(value) % parseInt(param) == 0);
                        }
                    },
                    message: "请输入{0}的倍数"
                },
                pnum: { // 正整数
                    validator: function (value, param) {
                        return /^[0-9]*[1-9][0-9]*$/.test(value);
                    },
                    message: "请输入正整数"
                },
                pznum: {    // 非0开头正整数
                    validator: function (value, param) {
                        return /^[1-9]*[1-9][0-9]*$/.test(value);
                    },
                    message: "请输入非0开头的正整数"
                },
                num: {  // 正实数，包含小数
                    validator: function (value, param) {
                        return /^\d+(\.\d+)?$/.test(value);
                    },
                    message: "请输入正整数或者小数"
                },
                maxLength: {
                    validator: function (value, param) {
                        var limitLength = param[0];
                        if(value && value.length > limitLength){
                            return false;
                        }

                        return true;
                    },
                    message: "不可以超过{0}个字符"
                },
                nonnegativePnum: {  //非负整数
                    validator: function (value, param) {
                        value = removeComma(value);//取消字符串中出现的所有逗号
                        if (isEmpty(param[0])) {
                            $.fn.validatebox.defaults.rules.nonnegativePnum.message = "只允许录入大于等于0的整数";
                        } else {
                            $.fn.validatebox.defaults.rules.nonnegativePnum.message = "{0}只允许填写大于等于0的整数!";
                        }

                        return /^\d+$/.test(value);
                    },
                    message: "{0}只允许填写大于等于0的整数!"
                },
                compareNum: {
                    validator: function (value, param) {
                        var val = $(param[0]).numberbox("getValue");
                        if (isNotNull(val) && parseInt(val) <= parseInt(value)) {
                            return true;
                        } else {
                            return false;
                        }
                    },
                    message: "人行近3个月查询不能小于人行近1个月查询"
                },
                weChat: {    // 微信号不能输入汉字和空格
                    validator: function (value, param) {
                        return (/^[^\u4e00-\u9fa5]{0,}$/.test(value) && !/\s/.test(value));// 过滤空格;
                    },
                    message: "请输入正确的微信号"
                },
                fiterBlank:{// 过滤空格
                    validator: function (value, param) {
                        var flag = /\s/.test(value);// 过滤空格
                        return !flag;
                    },
                    message: "不能输入空格"
                },
                filterSpecial: {    // 过滤特殊字符
                    validator: function (value, param) {
                        var flag = /\s/.test(value);// 过滤空格
                        // 过滤特殊字符串
                        var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】’‘《》；：”“'。，、？]");
                        var specialFlag = pattern.test(value);
                        return !flag && !specialFlag;
                    },
                    message: "非法字符，请重新输入"
                },
                chinessAndLetter: { // 只能输入字母和中文
                    validator: function (value, param) {
                        var flag = /\s/.test(value);// 过滤空格
                        var pattern = /^[\u4e00-\u9fa5a-zA-Z]+$/ // 中文或英文
                        var specialFlag = false;
                        var s = value.split('');
                        for (var i = 0; i < s.length; i++) {
                            if (pattern.test(s[i])) {
                                specialFlag = true;
                            } else {
                                specialFlag = false;
                                break;
                            }
                        }
                        return !flag && specialFlag;
                    },
                    message: "只能输入中文或者字母，请重新输入"
                },
                // 必须有汉字
                chiness:{
                    validator: function (value, param) {
                        var pattern = /^[^\u4e00-\u9fa5]+$/ // 中文
                        return  !pattern.test(value);
                    },
                    message: "必须包括汉字"
                },
                chinessOrNumber:{//只能输入汉字和数字不能有字母
                    validator: function (value, param) {
                            var pattern = /^[^\u4e00-\u9fa50-9]+$/ // 中文和数字
                            var letter = /^[A-Za-z]+$/;// 字母
                            var letterFlag = false;
                            var  s = value.split("");
                            for (var i = 0; i < s.length; i++) {
                                if (letter.test(s[i])) {
                                    letterFlag = false;
                                    break;
                                } else {
                                    letterFlag = true;
                                }
                            }
                            return !pattern.test(value) && letterFlag;
                    },
                    message: "只能输入中文和数字"
                },
                chinessLetterNumber: {  // 只能输入汉字、数字和字母的校验
                    validator: function (value, param) {
                        var flag = /\s/.test(value);// 过滤空格
                        var pattern = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/ // 中文或英文
                        var specialFlag = false;
                        var s = value.split('');
                        for (var i = 0; i < s.length; i++) {
                            if (pattern.test(s[i])) {
                                specialFlag = true;
                            } else {
                                specialFlag = false;
                                break;
                            }
                        }
                        return !flag && specialFlag;
                    },
                    message: "只能输入中文、数字或者字母或，请重新输入"
                },
                peopleName: {
                    validator: function (value, param) {
                        var flag = /\s/.test(value);// 过滤空格
                        var pattern = /^[\u4e00-\u9fa5a|.]+$/ // 中文或英文
                        var specialFlag = false;
                        var s = value.split('');
                        for (var i = 0; i < s.length; i++) {
                            if (pattern.test(s[i])) {
                                specialFlag = true;
                            } else {
                                specialFlag = false;
                                break;
                            }
                        }
                        return !flag && specialFlag;
                    },
                    message: "请输入正确的姓名"
                },
                /**
                 * 车牌验证
                 * 只允许输入汉字+大写字母+数字
                 * 第一位必须是汉字，且只能有一个汉字
                 * 长度不少于7位，不多于8位
                 * 不可输入英文字母I和O
                 */
                carPlateNum: {
                    validator: function (value, param) {

                        return new RegExp(regexp.plantNumber).test(value);
                    },
                    message: "输入的车牌号不合法，请重新输入"
                },
                compareMaxMin: {
                    validator: function (value, param) {
                        if (value.indexOf(",") >= 0) {
                            value = removeComma(value);//取消字符串中出现的所有逗号
                        }
                        if (parseFloat(param[0]) > parseFloat(param[1])) {
                            $.fn.validatebox.defaults.rules.compareMaxMin.message = '审批额度低于当前选择期限的最小值，请修改审批额度或修改审批期限';
                            return false;
                        } else {
                            if (parseFloat(value) >= parseFloat(param[0]) && parseFloat(value) <= parseFloat(param[1])) {
                                return true;
                            } else {
                                $.fn.validatebox.defaults.rules.compareMaxMin.message = '取值范围必须是大于等于{0},小于等于{1}';
                                return false;
                            }
                        }
                    },
                    message: ""
                },
                range: {   // 在指定取值范围内的正整数
                    validator: function (value, param) {
                        value = removeComma(value);// 去掉千位分隔符
                        if (parseFloat(value) >= parseFloat(param[0]) && parseFloat(value) <= parseFloat(param[1])) {
                            return /^\d+$/.test(value);
                        } else {
                            if (param.length > 3) {// 提示语特殊化
                                param[1] = param[3];
                            }
                            return false;
                        }
                    },
                    message: "{2}只允许填写{0}到{1}之间的整数！"
                },
                same: {
                    validator: function (value, param) {
                        var _value = $(param[0]).val();
                        if(value && _value){
                            return !(value == _value)
                        }

                        return true;
                    },
                    message: "{1}和{2}相同"
                },
                IDCard: {    // 身份证
                    validator: function (value, param) {
                        // 是否为空
                        if (value === '') {
                            $.fn.validatebox.defaults.rules.IDCard.message = '身份证号不能为空,请输入身份证号';
                            return false;
                        }
                        // 校验长度，类型
                        if (isCardNo(value) === false) {
                            $.fn.validatebox.defaults.rules.IDCard.message = '身份证号码不正确,请重新输入';
                            return false;
                        }
                        // 检查省份
                        if (checkProvince(value) === false) {
                            $.fn.validatebox.defaults.rules.IDCard.message = '身份证号码不正确,请重新输入';
                            return false;
                        }
                        // 校验生日
                        if (checkBirthday(value) === false) {
                            $.fn.validatebox.defaults.rules.IDCard.message = "请输入正确的年月日";
                            return false;
                        }
                        // 检验位的检测
                        //if (checkParity(value) === false) { $.fn.validatebox.defaults.rules.IDCard.message = '您的身份证校验位不正确,请重新输入'; return false; }
                        return true;
                    },
                    message: "请输入正确的身份证号码"
                },
                compareToday: {
                    validator: function (value) {
                        if (value) {
                            value = value + " 00:00:01";
                            return (new Date(value) <= new Date())
                        }
                    },
                    message: "时间不能大于今天"
                },
                compareTimeLimit: { // 时间期限的判断
                    validator: function (value, param) {
                        var date = new Date(value);
                        var sourceDate = new Date(param);
                        return dateCompare(sourceDate, date);// dateCompare
                    },
                    message: "时间不能小于{0}"
                },
                compareTimeLimitWithParam: {    // 时间期限的判断（带参数）
                    validator: function (value, param) {
                        //将年月日日期改为YYYY-MM-DD
                        var paramDate = param[0].replace(/年/g, '-');
                        if (paramDate.indexOf('日') >= 0) {
                            alert(paramDate.indexOf('日'));
                            paramDate = paramDate.replace(/月/g, '-').replace(/日/g, '');
                        } else {
                            paramDate = paramDate.replace(/月/g, '');
                        }
                        var date = new Date(value);
                        var sourceDate = new Date(paramDate);
                        return dateCompare(sourceDate, date);// dateCompare
                    },
                    message: "{1}早于{0}，请重新选择！"
                },
                compareTimeUpLimit: { // 时间期限的判断
                    validator: function (value) {
                        return (new Date(value).getFullYear() < new Date().getFullYear());// dateCompare
                    },
                    message: "时间不能大于当前年份"
                },
                compareDate: {  // 比较日期选择器
                    validator: function (value, param) {
                        return dateCompare($(param[0]).datebox("getValue"), value);
                    },
                    message: "结束日期不能小于开始日期"
                },
                compareOffset: {
                    validator: function (value, param) {
                        var compareValue = $(param[1]).next("span.combo").find("input:hidden").val();
                        if (isNotNull(value) && isNotNull(compareValue)) {
                            var a = moment(value);
                            var b = moment(compareValue);
                            if ("start" == param[0]) {
                                if (a.add(param[2], "days").diff(b) >= 0) {
                                    return true;
                                } else {
                                    $.fn.validatebox.defaults.rules.compareOffset.message = "查询时间段最长为30天";
                                    return false;
                                }
                                return (a.add(param[2], "days").diff(b) >= 0);
                            } else {
                                if ((a.diff(b.add(param[2], "days")) <= 0)) {
                                    return true;
                                } else {
                                    $.fn.validatebox.defaults.rules.compareOffset.message = "结束时间不能大于开始时间30天";
                                    return false;
                                }
                                return (a.diff(b.add(param[2], "days")) <= 0);
                            }
                        } else {
                            return true;
                        }
                    },
                    //message : "结束时间不能大于开始时间{2}"
                },
                compareTime: {  // 比较时间选择器（时分秒）
                    validator: function (value, param) {
                        return timeCompare($(param[0]).timespinner("getValue"), value);
                    },
                    message: "结束时间不能小于或等于开始时间"
                },
                compareDateTime: {  // 比较时间选择器（年月日时分秒）
                    validator: function (value, param) {
                        return dateTimeCompare($(param[0]).timespinner("getValue"), value);
                    },
                    message: "结束时间不能小于或等于开始时间"
                },
                unnormal: { // 验证是否包含空格和非法字符
                    validator: function (value) {
                        return /^[a-zA-Z0-9]/i.test(value);

                    },
                    message: '输入值不能为空和包含其他非法字符'
                },
                date: { // 验证日期
                    validator: function (value) {
                        // 格式yyyy-MM-dd或yyyy-M-d
                        return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
                    },
                    message: '清输入合法的日期格式'
                },
                bothNotEmpty: { // 开始时间和结束时间，只要有一个不是空，另一个也必须不为空
                    validator: function (value, param) {
                        var value1 = $(param[0]).datebox("getValue");
                        if (value != null && value != '') {
                            return value1 != null && value1 != '';
                        }
                        if (value1 != null && value1 != '') {
                            return value != null && value != '';
                        }

                    },
                    message: "开始时间和结束时间都需要选择"
                },
                bothEmpty: {    // 时间比较开始时间可以为空
                    validator: function (value, param) {
                        var value1 = $(param[0]).datebox("getValue");
                        if (isNotNull(value1)) {
                            var data1 = new Date(value1);
                            var data = new Date(value);
                            return data1 <= data;
                        } else {
                            return true;
                        }
                    },
                    message: "结束时间不能小于开始时间"
                },
                notAllEnglish: {    // 不可以是全英文
                    validator: function (value, param) {
                        var reg = /^[a-zA-Z]+$/;
                        return !reg.test(value);
                    },
                    message: "不可以是全英文"
                },
                notAllNumber: { // 不可以是全数字
                    validator: function (value, param) {
                        var reg = /^[0-9]+$/;
                        return !reg.test(value);
                    },
                    message: "不可以是全数字"
                },
                samePhone: {
                    validator: function (value, param) {
                        var index = 0;
                        var flag = true;
                        $("input.cellPhoneCheck").each(function (ind, val) {
                            var otherPhone = $(val).val();
                            if (isNotNull(otherPhone) && value == otherPhone) {
                                index++;
                                if (index == 2) {// 包括自己这个号码超过两个时表示有重复号码
                                    flag = false;
                                    return false;
                                }
                            }
                        });
                        return flag;
                    },
                    message: "手机号码重复"
                },
                checkSex: { // 根据传入的身份证号码判断性别是否相同,倒数第二位，奇数为男性，偶数为女性
                    validator: function (value, param) {
                        var mateID = value;
                        var applicantID = param[0];
                        var mateSex = mateID.substr(mateID.length - 2, 1) % 2;
                        var applicantSex = applicantID.substr(applicantID.length - 2, 1) % 2;
                        if (mateSex == applicantSex) {
                            return false;
                        } else {
                            return true;
                        }
                    },
                    message: "配偶性别不能与申请人本人相同，请修改"
                },
                checkTel: {// 电核汇总第三方电话添加判断当前列表是否已有该第三方电话号码

                    validator: function (value, param) {
                        var name = $(param[0]).combobox('getText');
                        var flag = true;
                        $(param[1]).each(function (ind, val) {
                            if (name == $(val).children("td").eq(1).text() && value == $(val).children("td").eq(3).children("input").val()) {
                                // alert(name + "/" + telPhone + "/" + $(val).children("td").eq(1).text() + $(val).children("td").eq(3).children("input").val());
                                flag = false;
                                return false;
                            }

                        });
                        return flag;
                    },
                    message: "电话号码重复！"
                },
                comboboxUnSelectWithParam: {
                    validator: function (value, param) {
                        if(isNotNull(value)){
                            return value != "-请选择-";
                        }

                        return true;
                    },
                    message: "请选择{0}"
                },
                decimalsVerified: { // 保留两位小数数验证（带参数）
                    validator: function (value, param) {
                        if (parseFloat(value) >= parseFloat(param[0]) && parseFloat(value) <= parseFloat(param[1])) {
                            return /^[0-9]+(.[0-9]{0,2})?$/.test(value);
                        } else {
                            return false;
                        }
                    },
                    message: '{2}只允许填写大于等于{0}小于等于{1}的数字，最多支持2位小数'
                },
                decimalsVerifiedOne: { // 保留一位小数数验证（带参数）
                    validator: function (value, param) {
                        if (parseFloat(value) >= parseFloat(param[0]) && parseFloat(value) <= parseFloat(param[1])) {
                            return /^[0-9]+(.[0-9]{0,1})?$/.test(value);
                        } else {
                            return false;
                        }
                    },
                    message: '{2}只允许填写{0}到{1}的数字'
                },
                compareNowDate: { // 时间不能晚于当前日期
                    validator: function (value, param) {
                        if (value) {
                            value = value + " 00:00:01";
                            return (new Date(value) <= new Date())
                        }
                    },
                    message: "{0}不可晚于当前日期，请重新选择！"
                },
                postcode: { // 中国邮编
                    validator: function (value, param) {

                        return new RegExp(regexp.zipcode).test(value);
                    },
                    message: "只允许输入6位数字!"
                },
                QQ: { // QQ验证
                    validator: function (value, param) {

                        return new RegExp(regexp.qq).test(value);
                    },
                    message: 'QQ号只能输入5位或以上数字'
                },
                email: {
                    validator: function (value, param) {

                        return new RegExp(regexp.email).test(value);
                    },
                    message: '电子邮箱不合法，请重新输入！'
                },
                notAllNumberAndLength: {    // 不可以是全数字,且不能超过指定字符(带参数)
                    validator: function (value, param) {
                        if (value.length > parseFloat(param[0])) {
                            return false;
                        }
                        var reg = /^[0-9]+$/;
                        return !reg.test(value);
                    },
                    message: "{1}不能全部都是数字，且不能超过{0}个字符"
                },
                //客户姓名验证（带参数）
                peopleNameWithParam: {
                    validator: function (value, param) {
                        var isAllpoint = true;
                        var flag = /\s/.test(value);// 过滤空格
                        var pattern = /^[\u4e00-\u9fa5a|·]+$/ // 中文或英文
                        var specialFlag = false;
                        var s = value.split('');
                        for (var i = 0; i < s.length; i++) {
                            if ("·" != s[i]) {
                                isAllpoint = false;
                            }

                            if (pattern.test(s[i])) {
                                specialFlag = true;
                            } else {
                                specialFlag = false;
                                break;
                            }
                        }
                        return !flag && specialFlag && !isAllpoint;
                    },
                    message: "{0}只可输入·和汉字，且不能只输入·！"
                },
                // 相同手机号验证（包括手机姓名）当前联系人专用
                samePhoneWithName: {
                    validator: function (value, param) {

                        var flag = true;
                        var type = param[0];
                        //获取当前联系人的序号（即：第几联系人）
                        var contactsIndex = $(this).parents("form").find("div").eq(0).find("span").text();
                        if ('firstPhone' == type) {
                            $(".firstPhone").each(function (ind, val) {
                                var otherPhone = $(val).val();
                                if (contactsIndex != $(val).parents("form").find("div").eq(0).find("span").text()) {
                                    if (isNotNull(otherPhone) && otherPhone == value) {
                                        var otherName = $(val).parents("form").find("input[name='contactName']").val();
                                        $.fn.validatebox.defaults.rules.samePhoneWithName.message = "手机号与" + otherName + "常用手机一致，不可重复录入！";
                                        flag = false;
                                    }
                                }
                            });
                            $(".secondPhone").each(function (ind, val) {
                                var otherPhone = $(val).val();
                                if (isNotNull(otherPhone) && otherPhone == value) {
                                    var otherName = $(val).parents("form").find("input[name='contactName']").val();
                                    $.fn.validatebox.defaults.rules.samePhoneWithName.message = "手机号与" + otherName + "备用手机一致，不可重复录入！";
                                    flag = false;
                                }
                            });
                        }

                        if ('secondPhone' == type) {
                            $(".secondPhone").each(function (ind, val) {
                                var otherPhone = $(val).val();
                                if (contactsIndex != $(val).parents("form").find("div").eq(0).find("span").text()) {
                                    if (isNotNull(otherPhone) && otherPhone == value) {
                                        var otherName = $(val).parents("form").find("input[name='contactName']").val();
                                        $.fn.validatebox.defaults.rules.samePhoneWithName.message = "手机号与" + otherName + "备用手机一致，不可重复录入！";
                                        flag = false;
                                    }
                                }
                            });
                            $(".firstPhone").each(function (ind, val) {
                                var otherPhone = $(val).val();
                                if (isNotNull(otherPhone) && otherPhone == value) {
                                    var otherName = $(val).parents("form").find("input[name='contactName']").val();
                                    $.fn.validatebox.defaults.rules.samePhoneWithName.message = "手机号与" + otherName + "常用手机一致，不可重复录入！";
                                    flag = false;
                                }
                            });
                        }

                        return flag;
                    },
                    message: "手机号码重复!"
                },
                // 验证联系人姓名不能相同
                contactName: {
                    validator:function(value) {
                        // 先判断是否和申请人姓名相同
                        if ($("td.customerBaseInfo_name").html() == value) {
                            $.fn.validatebox.defaults.rules.contactName.message ="联系人姓名不能是申请人";
                            return false;
                        }
                        // 判断入联系人之间的姓名是否相同
                        var flag = true;
                        var form = $(this).parents("form");
                        $("#first_contactInfoVOList_div").find(".contactForm").each(function(ind,obj){
                            if (!$(form).is( $(obj))) {
                                if ($(obj).find("input[name='contactName']").val() == value){
                                    flag = false;
                                    $.fn.validatebox.defaults.rules.contactName.message ="联系人姓名相同";
                                    return false;
                                }
                            }
                        });
                        return flag;
                    },
                    message:"联系人姓名重复!"
                },
                // 配偶信息身份证
                consortIDCard: {
                    validator: function (value, param) {
                        // 是否为18位，是否是'X'结尾
                        if (/^\d{17}(\d|X)$/.test(value) == false) {
                            $.fn.validatebox.defaults.rules.consortIDCard.message = "配偶身份证身份证必须为18位，只允许输入数字或大写字母X，且大写字母X只能在最后1位！！";
                            return false;
                        }

                        // 检查省份
                        if (checkProvince(value) === false) {
                            $.fn.validatebox.defaults.rules.consortIDCard.message = "配偶身份证号码校验不通过，非有效身份证号码！";
                            return false;
                        }
                        // 校验生日
                        if (checkBirthday(value) === false) {
                            $.fn.validatebox.defaults.rules.consortIDCard.message = "配偶身份证号码校验不通过，非有效身份证号码！";
                            return false;
                        }

                        return /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/.test(value);
                    },
                    message: "配偶身份证号码校验不通过，非有效身份证号码！"
                },
                //根据传入的身份证号码判断性别是否相同,倒数第二位，奇数为男性，偶数为女性（联系人信息专用）
                checkSexForContacts: {
                    validator: function (value, param) {
                        var mateID = value;
                        var applicantID = param[0];
                        var mateSex = mateID.substring(applicantID.length - 2, applicantID.length - 1) % 2;
                        var applicantSex = applicantID.substring(applicantID.length - 2, applicantID.length - 1) % 2;
                        if (mateSex == applicantSex) {
                            return false;
                        } else {
                            return true;
                        }
                    },
                    message: "客户配偶非外籍人士，配偶性别不能与客户相同！"
                },
                //验证客户配偶信息长度
                consortInfo: {
                    validator: function (value, param) {
                        if (isEmpty(value)) {
                            $.fn.validatebox.defaults.rules.consortIDCard.message = "请输入身份信息！";
                            return false;
                        }

                        if (value.length >= parseFloat(param[0]) && value.length <= parseFloat(param[1])) {
                            return true;
                        } else {
                            $.fn.validatebox.defaults.rules.consortIDCard.message = "客户配偶身份信息录入过长！";
                            return false;
                        }
                    },
                    message: "客户配偶身份信息录入过长！"
                },
                //必须有汉字，不能有空格
                chineseAndNoSpace: {
                    validator: function (value, param) {
                        if (/\s+/.test(value)) {
                            return false;
                        }
                        return /[\u4e00-\u9fa5]/.test(value);
                    },
                    message: "{0}必须包含汉字，且不能有空格!"

                },
                //保险年限
                insurancePeriod: {
                    validator: function (value, param) {
                        value = removeComma(value);// 去掉千位分隔符
                        if (/^[1-9][0-9]{0,7}$/.test(value)) {
                            return true;
                        } else if("终身" == value) {
                            return true;
                    } else {
                            return false;
                        }
                    },
                    message: "{2}只允许填写{0}到{1}之间的整数或终身！"
                },
                notAllNum:{
                    validator: function (value, param) {
                        return !/^\d+$/.test(value);
                    },
                    message: "不能全为数字！"

                },
                //相同地址验证
                duplicateAddress: {
                    validator: function (value, param) {
                       var address1 = $(param[0]).val();
                       var address2 = $(param[1]).val();
                       var canBeSame =$(param[2]).val();

                       if(canBeSame == 1){  // 两个地址不能相同
                           return (address1 != address2);
                       }

                       return true;
                    },
                    message: "{3}是否同{4}选择为否，两个地址不能相同，请重新填写！"
                },// 公积金 和社保账号验证
                accountAndPwd:{
                    validator:function(value, param) {
                        return /^[^\u4e00-\u9fa5\s]{1,50}$/.test(value)
                    },
                    message:"{0}不合法,不能包括汉字和空格且1到50位!"
                },// 学信网账号密码
                userAndPwd:{
                    validator:function(value, param) {
                        return /^[^\u4e00-\u9fa5\s]{1,30}$/.test(value)
                    },
                    message:"{0}不合法,不能包括汉字和空格且1到30位!"
                },
                /*学历信息-认证书编号字段校验规则：
                1.输入长度[10,29]
                2.倒数9位必须是数字，且前四位数字范围[1980,当前年份]
                3.9位数字前必须是汉字，汉字长度[1,20]*/
                certificateNumber:{
                    validator:function(value,param) {
                        var flag = false;
                        if (isNotNull(value)) {
                            if (value.length >= 10 && value.length <=29) {
                                var prefix = value.substring(0,value.length -9);// 前缀
                                if (!/^[\u4e00-\u9fa5]+$/.test(prefix)) {
                                    $.fn.validatebox.defaults.rules.certificateNumber.message = "认证书编号倒数9位数字前必须是汉字！";
                                }  else {
                                    var surfix = value.substring(prefix.length);// 后缀
                                    if (!/[0-9]{9}/.test(surfix)) {
                                        $.fn.validatebox.defaults.rules.certificateNumber.message = "认证书编号倒数9位必须是数字！";
                                    } else {
                                        var year =parseInt(surfix.substr(0,4));
                                        if (year <1980 || year > parseInt(param[0])) {
                                            $.fn.validatebox.defaults.rules.certificateNumber.message = "认证书编号倒数9位必须是数字，且前四位数字范围[1980,"+param[0]+"]！";
                                        } else {
                                            flag = true;
                                        }
                                    }
                                }
                            } else {
                                $.fn.validatebox.defaults.rules.certificateNumber.message = "认证书编号必须是10-29位！";
                            }
                        }
                        return flag;
                    },
                    message:"认证书编号不合法",

                }
            };

            // 常用的正则表达式
            var regexp = {
                decmal: "^([+-]?)\\d*\\.\\d+$", // 浮点数
                decmal1: "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$", // 正浮点数
                decmal2: "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$", // 负浮点数
                decmal3: "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$", // 浮点数
                decmal4: "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$",// 非负浮点数（正浮点数 + 0）
                decmal5: "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$",    // 非正浮点数（负浮点数 + 0）
                intege: "^-?[1-9]\\d*$",// 整数
                intege1: "^[1-9]\\d*$",  // 正整数
                intege2: "^-[1-9]\\d*$", // 负整数
                num: "^([+-]?)\\d*\\.?\\d+$",   // 数字
                num1: "^[1-9]\\d*|0$",  // 正数（正整数 + 0）
                num2: "^-[1-9]\\d*|0$", // 负数（负整数 + 0）
                ascii: "^[\\x00-\\xFF]+$",  // 仅ACSII字符
                chinese: "^[\\u4e00-\\u9fa5]+$",// 仅中文
                color: "^[a-fA-F0-9]{6}$",  // 颜色
                date: "^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$",// 日期
                email: "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$",// 邮件
                idcard: "^[1-9]([0-9]{14}|[0-9]{17})$",// 身份证
                ip4: "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$", // ip地址
                letter: "^[A-Za-z]+$", // 字母
                letter_l: "^[a-z]+$",// 小写字母
                letter_u: "^[A-Z]+$",// 大写字母
                mobile: "^0?(1)(?!\\1{10}$)[0-9]{10}$", // 手机
                notempty: "^\\S+$",// 非空
                password: "^.*[A-Za-z0-9\\w_-]+.*$",// 密码
                fullNumber: "^[0-9]+$",// 数字
                picture: "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$",// 图片
                qq: "^[1-9][0-9]{4,}$",  // QQ号码
                rar: "(.*)\\.(rar|zip|7zip|tgz)$",// 压缩文件
                tel: "^(0)(?!\\1{2,3}-)\\d{2,3}-([1-9])(?!\\2{6,7}$|\\2{6,7}-)\\d{6,7}(-\\d{1,6})?$",// 电话号码的函数(包括验证国内区号,国际区号,分机号)
                url: "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$", // url
                username: "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+$", // 户名
                deptname: "^[A-Za-z0-9_()（）\\-\\u4e00-\\u9fa5]+$", // 单位名
                zipcode: "^\\d{6}$",// 邮编
                realname: "^[A-Za-z\\u4e00-\\u9fa5]+$",
                plantNumber: "^[\u4e00-\u9fa5]{1}[A-H|J-N|P-Z]{1}[A-H|J-N|P-Z|0-9]{5,6}$"
            };

            // 扩展easyui-validatebox验证规则
            $.extend($.fn.validatebox.defaults.rules, defaultOptions);

            var jValidate = {
                addRule: addRule
            };

            /**
             * 删除value中的逗号
             * @param value
             * @returns {void|string|*|XML}
             */
            function removeComma(value){
                return value.replace(/,/g, "");
            }

            // 添加新的校验规则
            function addRule(ruleName, callback, message) {
                defaultOptions[ruleName] = {
                    validator: callback,
                    message: message
                };

                $.extend($.fn.validatebox.defaults.rules, defaultOptions);

                return jValidate;
            }

            // 初始化待验证的页面元素
            /*
            $.fn.jValidate = function(ruleName, params) {
                // todo 1-获取元素和元素label
                var ele = this;
                var label = '婚姻状况';
                //var id = ele.attr("id");


                // todo 2-初始化验证规则
                addRule(ruleName + '_' + label,  getRuleFunction() , label + "区间必须在{0}到{1}之间");


                // todo 3-把验证规则添加到元素
                ele.validatebox({
                    validType: ["comboboxUnSelect_" + label + "[" ]
                });

                return this;
            };
            */

            return jValidate;
        })();
    });
}(typeof define === 'function' && define.amd ? define : function (deps, factory) {
    if (typeof module !== 'undefined' && module.exports) { //Node
        module.exports = factory(require('jquery'), require('underscore'));
    } else {
        window['jValidate'] = factory(window['jQuery'], window['underscore']);
    }
}));