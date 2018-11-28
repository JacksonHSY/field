package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;

import java.util.List;

public interface ApproveCheckStatementService {

    /**
     * 批量更新流水信息
     * @author wulj
     * @param approveCheckStatementList
     */
    void batchSaveOrUpdate(List<ApproveCheckStatement> approveCheckStatementList);

    /**
     * 保存或更新
     * @param approveCheckStatement
     * @return
     */
    ApproveCheckStatement saveOrUpdate(ApproveCheckStatement approveCheckStatement);

    /**
     * 查询资料核对流水信息
     * @param loanNo 借款编号
     * @return
     */
    List<ApproveCheckStatement> getByLoanNo(String loanNo);

    /**
     * 查询资料核对流水信息
     * @param loanNo    借款编号
     * @param type      个人 or 对公
     * @return
     */
    List<ApproveCheckStatement> getByLoanNoAndType(String loanNo, EnumUtils.CheckStatementType type);

    /**
     * 根据借款编号，删除流水信息
     * @author wulj
     * @param loanNo
     */
    int removeByLoanNo(String loanNo);
}
