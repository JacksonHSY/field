/**
 * BMS API基本信息查询库
 * @author wulj
 * @date 2017/12/14
 */
;(function (define) {
    define(['jquery', 'underscore'], function ($, _) {
        return (function () {

            /**
             * 获取产品审批进度上下限
             * @param productCode       产品编号
             * @param owningBranchId    进件营业部
             * @param auditLimit        审批期限
             * @param isCanPreferential 是否是优惠配置，0是 1否
             */
           function getProductUpperLower(productCode, owningBranchId, auditLimit,isCanPreferential) {
               var productLimit;
               $.ajax({
                    type: 'POST',
                    url: ctx.rootPath() + '/bmsBasiceInfo/getProductUpperLower',
                    dataType: 'json',
                    async: false,
                    data: {
                        productCode: productCode,
                        owningBranchId: owningBranchId,
                        auditLimit: auditLimit,
                        isCanPreferential:isCanPreferential
                    },
                    success:function(result){
                        if(result.type=='SUCCESS'){
                            productLimit = result.data;
                        }else{
                            $.info("提示", data.firstMessage, "error");
                        }
                    }
                });

               return productLimit;
           };


           return {
               getProductUpperLower: getProductUpperLower
           };
        })();
    });
}(typeof define === 'function' && define.amd ? define : function (deps, factory) {
    if (typeof module !== 'undefined' && module.exports) { //Node
        module.exports = factory(require('jquery'), require('underscore'));
    } else {
        window['bmsBasicApi'] = factory(jQuery, _);
    }
}));