/**
 * 核心 API基本信息查询库
 * @author wulj
 * @date 2017/12/16
 */
;(function (define) {
    define(['jquery', 'underscore'], function ($, _) {
        return (function () {

            /**
             * 获取产品综合费率
             * @param productCode 产品编号
             * @param productTerm 产品期限
             */
            function getProductRateByCode(productCode, productTerm){
                var productRate = {};
                post(ctx.rootPath() + '/coreApi/getProductRate',{productCode: productCode}, 'json', function (result) {
                    if(result.type == 'SUCCESS' &&  result.dataList.length > 0){
                        productRate = _.find(result.dataList, function(item){
                            return item.term == productTerm;
                        });
                    }
                });

                return productRate;
            }

            /**
             * 借款合同试算
             * @param loanType
             * @param money
             * @param time
             * @param fundsSources
             * @param isRatePreferLoan
             * @returns {{}}
             */
            function getLoanContractTrail(loanType, money, time, fundsSources, isRatePreferLoan){
                var loanContractTrail = {};
                post(ctx.rootPath() + '/coreApi/getLoanContractTrail',{
                    loanType: loanType,
                    money: money,
                    time: time,
                    fundsSources: fundsSources,
                    isRatePreferLoan: isRatePreferLoan
                }, 'json', function (result) {
                    if(result.type == 'SUCCESS'){
                        loanContractTrail = result.data;
                    }else{
                        $.info("提示", result.firstMessage, "warning");
                    }
                });

                return loanContractTrail;
            }

            /**
             * 获取剩余本金
             * @param name 姓名
             * @param idNum 身份证号码
             * @returns {number}
             */
            function getResidualPactMoney(name, idNum){
                var residualPactMoney = 0;
                post(ctx.rootPath() + '/coreApi/getResidualPactMoney',{name: name, idNum: idNum}, 'json', function (result) {
                    if(result.type == 'SUCCESS'){
                        residualPactMoney = result.data;
                    }else{
                        $.info("提示", result.firstMessage, "warning");
                    }
                });

                return residualPactMoney;
            }

           return {
               getProductRateByCode: getProductRateByCode,  // 查询产品费率接口
               getLoanContractTrail: getLoanContractTrail,  // 借款合同试算接口
               getResidualPactMoney: getResidualPactMoney   // 获取剩余本金接口
           };
        })();
    });
}(typeof define === 'function' && define.amd ? define : function (deps, factory) {
    if (typeof module !== 'undefined' && module.exports) { //Node
        module.exports = factory(require('jquery'), require('underscore'));
    } else {
        window['coreApi'] = factory(jQuery, _);
    }
}));