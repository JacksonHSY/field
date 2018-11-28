package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.vo.apply.RuleEngineParameterVO;

/**
 * Created by YM10106 on 2018/6/11.
 */
public interface RuleEngineParameterService {

    /**
     * 根据借款编号查询出最后一次保存值
     *
     * @param loanNo
     * @return
     */
    RuleEngineParameterVO getLastSaveByLoanNo(String loanNo);

    /**
     * 根据借款编号获取最新手动调用规则引擎值
     * @param loanNo
     * @param remark-标记是否已分派
     * @return
     */
    RuleEngineParameterVO findHandByLoanNo(String loanNo,boolean remark);
}
