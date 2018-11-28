package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;

/**
 * Created by YM10106 on 2018/6/15.
 */
public interface ReconsiderHistoryMapper extends GenericCrudMapper<ReconsiderHistory, Long> {

    /**
     * F2 办理根据借款编号查询是否有F3办理
     *
     * @param loanNo
     * @return
     */
    ReconsiderHistory findReconsiderHistoryByLevelThreeHandle(String loanNo);

    /**
     * 根据级别查询派单上一次分派
     * @return
     */
    ReconsiderHistory findLastDispathByLevel(String reconsiderLeve);

    /**
     * 复议派单F3提交查找原F2
     *
     * @param loanNo
     * @return
     */
    ReconsiderHistory findReconsiderHistoryByLevelTwoHandle(String loanNo);

    /**
     * 根据借款编号查询最后一次办理节点用于pic操作
     * @param loanNo
     * @return
     */
    ReconsiderHistory findLastNodeHistoryByLoanNo(String loanNo);

}
