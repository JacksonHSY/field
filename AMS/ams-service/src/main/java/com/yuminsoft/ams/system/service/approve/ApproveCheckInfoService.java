package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;

/**
 * 资料核对信息表
 */
public interface ApproveCheckInfoService {

    /**
     * 保存或更新资料核对信息
     * @param approveCheckInfo
     * @author wulj
     * @return
     */
    public ApproveCheckInfo saveOrUpdate(ApproveCheckInfo approveCheckInfo);

    /**
     * 根据申请件编号，查询资料核对信息
     * @author wulj
     * @param loanNo 申请件编号
     * @return 资料核对信息
     */
    public ApproveCheckInfo getByLoanNo(String loanNo);

}
