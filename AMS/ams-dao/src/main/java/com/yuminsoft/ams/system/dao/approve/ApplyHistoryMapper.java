package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 审批意见Mapper
 *
 * @author wulj
 */
public interface ApplyHistoryMapper {

    int delete(Long id);

    int save(ApplyHistory applyHistory);

    int update(ApplyHistory record);

    ApplyHistory findById(Long id);

    ApplyHistory findOne(Map<String, Object> map);

    List<ApplyHistory> findAll(Map<String, Object> map);

    /**
     * 获取申请件历史最高终审层级
     *
     * @param loanNo 申请件编号
     * @return
     * @author wulinjie
     */
    String findHighestFinalLevel(String loanNo);

    /**
     * 根据状态查询首次提交终审
     *
     * @param map
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    List<ApplyHistory> findFirstSubmitToFinal(Map<String, Object> map);

    /**
     * 已完成页面查询出最后一次信审操作
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月3日
     */
    ApplyHistory findLastOperationState(String loanNo);

    /**
     * 判断进件是否做过审核（质检用）
     */
    ApplyHistory findDoCheck(Map<String, Object> map);

    /**
     * 根据借款编号查询初审最近一次提交终审、高审、退回门店的操作
     *
     * @param loanNo
     * @return
     * @author zhouwen
     * @date 2017年6月27日
     */
    ApplyHistory findLastByLoanNo(String loanNo);

    /**
     * 查询最近一次审批历史(按照创建时间倒叙)
     *
     * @param params 参数
     * @return 最近一次审批历史信息
     * @author wulj
     */
    ApplyHistory findByLoanNoAndRtfStateInAndRtfNodeStateIn(Map<String, Object> params);

    /**
     * 查询审批历史(按照创建时间倒叙)
     *
     * @param params 参数
     * @return 审批历史信息
     * @author wulj
     */
    List<ApplyHistory> findByLoanNoAndUserCodeAndRtfStateAndRtfNodeStateIn(Map<String, Object> params);

    /**
     * 根据借款编号查询出工作台是初审退回，终审退回，复核退回的最近审批历史记录
     *
     * @param loanNo
     * @return
     * @author zhouwen
     * @date 2017年6月27日
     */
    ApplyHistory getWorkbenchStateByLoanNo(String loanNo);

    /**
     * 查询历史上曾经对这些单子审批通过的终审员列表
     *
     * @param loanNoArr
     * @return
     * @author JiaCX
     * @date 2017年6月27日 下午5:33:58
     */
    List<String> findApprovePassedPersonByLoanNos(String[] loanNoArr);

    /**
     * 历史上对这些单子审批过的终审员所在层级，取其中最大值
     *
     * @param loanNoArr
     * @return
     * @author JiaCX
     * @date 2017年6月27日 下午6:17:14
     */
    String findMaxLevelOfApprovedPersonByLoanNos(String[] loanNoArr);

    /**
     * @author wangzx
     * date 2017年10月18日
     */
    List<ApplyHistory> findHistory(Map<String, Object> map);

    /**
     * 根据借款编号查询最后一次初审审批历史(用于复议再申请)
     *
     * @param loanNo
     * @return
     */
    ApplyHistory findLastFirstApplyHistory(@Param("loanNo") String loanNo);

    /**
     * 根据单号查询历史处理过的初审员(初审改派查询)
     *
     * @return
     * @param-map:loanNoArray-借款编号逗号隔开;loanNoCount-借款编号个数
     */
    List<ApplyHistory> findHistoryApplyDealsPerson(Map<String, Object> map);

    /**
     * 复议查询出拒绝信息
     */
    ApplyHistory findApplyHistoryRejectByLoanNo(String loanNo);
}