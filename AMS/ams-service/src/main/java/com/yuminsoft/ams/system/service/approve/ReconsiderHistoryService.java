package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;

/**
 * Created by YM10106 on 2018/6/19.
 */
public interface ReconsiderHistoryService {

    /**
     * F2 办理根据借款编号查询是否有F3办理
     *
     * @param loanNo
     * @return
     */
    ReconsiderHistory getReconsiderHistoryByLevelThreeHandle(String loanNo);

    /**
     * 根据借款编号查询最后一次办理节点用于pic操作
     * @param loanNo
     * @return
     */
    ReconsiderHistory getLastNodeHistoryByLoanNo(String loanNo);
}
