$(function () {
    /**
     * on事件监听只准执行一次
     */
    if (typeof firstStaffAbility_mark == 'undefined') {
        firstStaffAbility_mark = true;

        /**
         *回填的时候选中下拉框
         */
        function reSelect(input, dl) {
            var value = $(input).data('val'); //单选情况下获取隐藏input框的值
            if (isNotNull(value)) {
                if ('空' == value.substring(value.length - 1, value.length)) {
                    value = value.substring(0, value.length - 1) + "0"
                }
                if (value.length >= 1) {
                    var str = value.substring(value.length - 1, value.length);
                    if ("0" == str) {
                        str = "0";
                        $(dl).find('dd').each(function () {
                            var ddValue = $(this).data('val');
                            if (value.substring(0, value.length - 1).indexOf(ddValue) != -1 || str == ddValue) { //如果有0，或者除此之外的字母，就选中
                                $(this).addClass('ddselect')
                            } else {
                                $(this).removeClass('ddselect')
                            }
                        })
                    } else {
                        $(dl).find('dd').each(function () {
                            var ddValue = $(this).data('val');
                            if (value.indexOf(ddValue) != -1) {
                                $(this).addClass('ddselect')
                            } else {
                                $(this).removeClass('ddselect')
                            }
                        })
                    }
                } else {
                    $(dl).find('dd').each(function () {
                        var ddValue = $(this).data('val');
                        if (value.indexOf(ddValue) != -1) {
                            $(this).addClass('ddselect')
                        } else {
                            $(this).removeClass('ddselect')
                        }
                    })

                }
            }
        }

        /**
         * 改变选择框的值
         */
        function change(div) {
            var dl = $(div).find('dl')
            var input = $(div).find('input')
            var json = getSelected(dl);                       //获取选中对象的值

            $(input).attr('value', json.text);
            $($(input).parent().next()).val(json.value);    // 为隐藏标签设置绑定到vm中
            $(input).data('val', json.value);                //做为悬浮展示的值
            var obj = $(div).prev().val();                  //修改的区域 如果是4位数，说明是修改区域，'-'代表产品，为空代表全部
            if(!isNotNull(json.value) && (obj.length!=4 && 'all'!=obj && obj.search('-') == -1)){                        //取消标红
                $(input).css('border-color','#ffb3b3');
                $(input).css('background-color','#fff3f3');
            }else{
                $(input).css('border-color','');
                $(input).css('background-color','');
            }
            if (4 == obj.length) {
                changeLevelByArea(json, obj)
            } else if ('all'==obj) {
                ChangeAllLevel(json);                       // 修改所有欺诈等级
            } else if (obj.search('-') != -1) {
                changeLevelByProduct(obj, json);             //根据产品修改欺诈等级
            }

        }

        /**
         * 获取选择的值
         * @param dl
         * @returns {{text: string, value: string}}
         */
        function getSelected(dl) {
            // input显示的文字
            var text = ''
            var value = '';
            // var length = 0;
            $(dl).find(" dd.ddselect").each(function () {
                value += $(this).data('val')+",";
                text += $(this).text()+",";
                // length += 1;
            })
            value = value.substring(0,value.length-1);
            text = text.substring(0,text.length-1);
            text = getValue(value);
            return {text: text, value: value}
        }


        $(document)
            .on('click', '.mymultiple input', function (event) {
                $('.mymultiple dl').removeClass('show');
                var dl = $(this).next();
                $(dl).addClass('show')
                reSelect($(this), dl)
                event.stopPropagation();
            })
            .on('click', '.mymultiple dl dd', function (event) {
                var values = $(this)
                var div = $(values).parent().parent();
                var value = $(values).text();
                if ($(values).hasClass("ddselect")) {
                    $(values).removeClass('ddselect')
                } else {
                    $(values).addClass('ddselect')
                }
                change(div);
                event.stopPropagation();
            })
            .on('click', function () {
                $('.mymultiple dl').removeClass('show')
            })
    }
})

/**
 * 批量修改所有欺诈等级
 * @param self
 * @constructor
 */
function ChangeAllLevel(self) {
    var value = self.value;
    var text = self.text;
    _.map(vm.abilityVoList, function (staffAbilityVo) {
        $.each(staffAbilityVo.staffAbilityList, function (index, element) {
            element.comCreditRating = value;
        });
        return staffAbilityVo;
    });
    // var str = transform(value);
    $('.level').attr('value', value); //隐藏域的值
    $('.text').attr('value', text);   //下拉框的值
    $('.text').data('val', value);    //下拉框的val属性，作为悬浮显示用
    $('.text').css('border-color','');
    $('.text').css('background-color','');
    $('.area').attr('value', text);   //区域的下拉值
    $('.area').data('val', value);    //区域悬浮值
    $('.product').attr('value', text);//产品下拉值
    $('.product').data('val', value); //产品悬浮值
    validates();
}

/**
 * 根据区域修改欺诈等级
 * @param self
 * @param orgCode
 */
function changeLevelByArea(self, orgCode) {
    var sty = "area-" + orgCode;
    _.map(vm.abilityVoList, function (staffAbilityVo) {
        $.each(staffAbilityVo.staffAbilityList, function (index, element) {
            if (element.areaCode == orgCode) {
                element.comCreditRating = self.value;
            }
        });
        return staffAbilityVo;
    });
    // var str = transform(self.value);
    $('.' + sty).attr('value', self.text);
    $('.' + sty).data('val', self.value);
    $('.' + sty).css('border-color','');
    $('.' + sty).css('background-color','');
    validates();
}

/**
 * 根据产品修改欺诈等级
 * @param obj
 * @param self
 */
function changeLevelByProduct(obj, self) {
    var product = getProduct(obj);
    _.map(product.value.staffAbilityList, function (staffAbility) {
        staffAbility.comCreditRating = self.value;
        return staffAbility;
    });
    vm.abilityVoList[product.index] = product.value;
    // var str = transform(self.value);
    $('.' + obj).attr('value', self.text);
    $('.' + obj).data('val', self.value);
    $('.' + obj).css('border-color','');
    $('.' + obj).css('background-color','');
    validates();
}

// 根据产品型号跟产品类型获取产品接单能力
function getProduct(obj) {
    var list = obj.split('-');
    var product = new Object();
    $.each(vm.abilityVoList, function (index, value) {
        if (list[1] == value.type && list[0] == value.productCode) {
            product.value = value;
            product.index = index;
        }
    });
    return product;
}

// 悬浮框值的修改
// function transform(str) {
//     var value = str;
//     if ('0' == str.substring(str.length - 1, str.length)) {
//         value = str.substring(0, str.length - 1) + "空";
//     }
//     return value;
// }

//将value的值改为ABCDE...的形式
function getValue(value) {
    var str = value.split(",");
    if(str.length>1){
        value=str[0]+"...";
    }
    return value;

}

/**
 * 欺诈等级字段必输项校验
 * @returns {number}
 */
function validates(){
    var list = document.getElementsByClassName('text');
    var flag = 0;
    $.each(list,function (index, element) {
       var value = $(element).val();
       if(!isNotNull(value)){
           $(element).css('border-color','#ffb3b3');
           $(element).css('background-color','#fff3f3');
           flag++;
       }
    })
    return flag;

}


