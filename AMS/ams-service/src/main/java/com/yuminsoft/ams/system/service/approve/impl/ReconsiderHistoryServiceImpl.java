package com.yuminsoft.ams.system.service.approve.impl;

import com.yuminsoft.ams.system.dao.approve.ReconsiderHistoryMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;
import com.yuminsoft.ams.system.service.approve.ReconsiderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by YM10106 on 2018/6/19.
 */
@Service
public class ReconsiderHistoryServiceImpl implements ReconsiderHistoryService {
    @Autowired
    private ReconsiderHistoryMapper reconsiderHistoryMapper;

    /**
     * F2 办理根据借款编号查询是否有F3办理
     *
     * @param loanNo
     * @return
     */
    @Override
    public ReconsiderHistory getReconsiderHistoryByLevelThreeHandle(String loanNo) {
        return reconsiderHistoryMapper.findReconsiderHistoryByLevelThreeHandle(loanNo);
    }

    /**
     * 根据借款编号查询最后一次办理节点用于pic操作
     * @param loanNo
     * @return
     */
    @Override
    public ReconsiderHistory getLastNodeHistoryByLoanNo(String loanNo){
        return  reconsiderHistoryMapper.findLastNodeHistoryByLoanNo(loanNo);
    }


}
