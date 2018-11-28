package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApproveCheckStatementMapper extends GenericCrudMapper<ApproveCheckStatement, Long>{

    /**
     * 查询资料核对流水信息
     * @param loanNo
     * @return
     */
    List<ApproveCheckStatement> findByLoanNo(String loanNo);

    /**
     * 查询资料核对流水信息
     * @param loanNo
     * @param type
     * @return
     */
    List<ApproveCheckStatement> findByLoanNoAndType(@Param("loanNo") String loanNo,@Param("type") String type);

    /**
     * 根据借款编号，删除流水信息
     * @param loanNo 借款编号
     * @return
     */
    int deleteByLoanNo(String loanNo);

}
