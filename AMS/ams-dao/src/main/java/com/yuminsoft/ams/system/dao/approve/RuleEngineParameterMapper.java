package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.RuleEngineParameter;

/**规则引擎返回参
 * @author wangzx
 * @date 2018/5/18 14:58:03
 */
public interface RuleEngineParameterMapper extends GenericCrudMapper<RuleEngineParameter, Long> {

    /**根据借款编号获取最新的规则引擎值**/
    RuleEngineParameter findByLoanNo(String loanNo);

    /**
     * 根据借款编号获取最新手动调用规则引擎值
     * @param loanNo
     * @return
     */
    RuleEngineParameter findHandByLoanNo(String loanNo);
}
