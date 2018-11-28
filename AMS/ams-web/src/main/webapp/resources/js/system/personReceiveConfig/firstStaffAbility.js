//@ sourceURL = firstStaffAbility.js
$(function($) {
    setTimeout(function () {
        avalon.each(vm.abilityList, function(index, element){
            // 手动修改vm对象的value
            if(isNotNull(element.value)){
                var abilityElement = document.getElementById(element.productCode + '_' + element.areaCode);
                var avalonField = abilityElement._ms_duplex_;    // 获取avalon双向绑定对象
                avalonField.setValue(element.value);         // 修改vm对象的value
            }
            //手动修改vm的欺诈评分
            if(isNotNull(element.comCreditRating)){ //接单能力，反欺诈都有值的情况
                // element.comCreditRating= element.comCreditRating.replace(/\,/g,'');
                var abilityElement = document.getElementById(element.productCode + '_' + element.areaCode + "-level");
                var avalonField = abilityElement._ms_duplex_;    // 获取avalon双向绑定对象
                    avalonField.setValue(element.comCreditRating); // 设置绑定对象的值

                var text = document.getElementById(element.productCode + '_' + element.areaCode + "-text");
                   var value = getValue(element.comCreditRating);
                    $(text).data('val', element.comCreditRating);
                    $(text).attr('value', value);
            }
        });
        if(JSON.stringify(vm.abilityList)=='[]'){ //接单能力从未配置的情况
            var list = document.getElementsByClassName('level');
            $.each(list,function (index) {
                var avalonField = list[index]._ms_duplex_;
                avalonField.setValue(null);
            })
        }

    }, 500);
    // 添加表单验证规则
    $('#abilityConfigForm').validate({
        errorPlacement: function (error, element) {
            error.insertBefore(element);
        }
    });

});

var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        type: '',
        userList: [],
        areaList: [],
        productList: [],
        abilityList: {},
        abilityVoList: {},
        changeAll: changeAll,
        changeByArea: changeByArea,
        changeByProduct: changeByProduct
        // changeAllD:changeAllD
    };

    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

/**
 * 全局修改修改下拉列表
 * @param event
 */
function changeAll(event) {
    var self = event.target;
        _.map(vm.abilityVoList, function(staffAbilityVo){
            $.each(staffAbilityVo.staffAbilityList, function(index, element){
                element.value = $(self).val();
            });
            return staffAbilityVo;
        });
        $('select.ability').val($(self).val());
    $('#abilityConfigForm').valid()
    validates();
}

/**
 * 根据地区修改接单能力
 * @param areaItem
 * @param event
 */
function changeByArea(areaItem, event){
    var self = event.target;
        _.map(vm.abilityVoList, function(staffAbilityVo){
            $.each(staffAbilityVo.staffAbilityList, function(index, element){
                if(element.areaCode == areaItem.orgCode){
                    element.value = $(self).val();
                }
            });

            return staffAbilityVo;
        });
    $('#abilityConfigForm').valid()
    validates();
}

/**
 * 根据产品和申请类型修改接单能力
 * @param product
 * @param event
 */
function changeByProduct(product, event){
    var self = event.target;
        _.map(product.staffAbilityList, function (staffAbility) {
            staffAbility.value = $(self).val();
            return staffAbility;
        });
    $('#abilityConfigForm').valid()
    validates();
}

