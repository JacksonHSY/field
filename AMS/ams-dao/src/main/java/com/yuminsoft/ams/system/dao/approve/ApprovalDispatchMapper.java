package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.ApprovalDispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 终审派单待分派池表
 * @author wulj
 */
public interface ApprovalDispatchMapper extends GenericCrudMapper<ApprovalDispatch, Long> {

    /**
     * 查找待分派申请件
     * @param params
     * @return
     */
    ApprovalDispatch findOne(Map<String, Object> params);

    /**
     * 查找待分派申请件
     * @param loanNo
     * @return
     */
    ApprovalDispatch findByLoanNo(String loanNo);

    /**
     * 删除待分派池表
     * @param loanNoList
     * @return
     */
    int deleteByLoanNoNotIn(@Param("loanNoList") List<String> loanNoList);

    /**
     * 删除待分派池表
     * @return
     */
    int deleteAll();

    /**
     * 删除待分派池表
     * @param status
     * @return
     */
    int deleteByStatus(String status);

    /**
     * 查找待分派池(不含协审件)
     * @param finalLevel 终审层级
     * @author wulj
     * @return
     */
    List<ApprovalDispatch> findWaitPoolWithoutApproval(String finalLevel);

    /**
     * 查找待分派池里，协审件的提交人工号
     * @author wulj
     * @return
     */
    List<String> findWaitPoolFinalPersonList();
}
