package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;

public interface ApproveCheckInfoMapper extends GenericCrudMapper<ApproveCheckInfo, Long> {

    /**
     * 根据申请件编号，查询资料核对信息
     * @author wulj
     * @param loanNo 申请件编号
     * @return 资料核对信息
     */
    public ApproveCheckInfo findByLoanNo(String loanNo);

}
