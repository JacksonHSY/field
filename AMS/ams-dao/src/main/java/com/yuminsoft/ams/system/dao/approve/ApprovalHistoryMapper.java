package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApprovalHistoryMapper {

	int save(ApprovalHistory approvalHistory);

	int delete(Long id);

	int update(ApprovalHistory approvalHistory);

	ApprovalHistory findById(Long id);

	List<ApprovalHistory> findByLoanNo(String loanNo);

	ApprovalHistory findOne(Map<String, Object> map);

	List<ApprovalHistory> findAll(Map<String, Object> map);

	/**
	 * 根据借款编号查询最新审批意见(用于判断是否填写过审批意见)
	 * @author dmz
	 * @date 2017年4月7日
	 * @param loanNo
	 * @return
	 */
	ApprovalHistory getApprovalHistoryNewByLoanNo(String loanNo);

	/**
	 * 查询出当前需要复核确认的审批意见
	 * @author dmz
	 * @date 2017年4月17日
	 * @param map
	 * @return
	 */
	ApprovalHistory findApprovalHistoryByLoanNoAndCheck(Map<String, Object> map);

	/**
	 * add by zw at 2017-04-21 根据借款编号和员工编号查询出审批日志ID
	 * 
	 * @param map
	 * @return int
	 */
	Long findApprovalHistoryId(Map<String, Object> map);

	/**
	 * 查询质检的审核历史信息
	 */
	List<ApprovalHistory> findForQuality(Map<String, Object> map);

	/**
	 * 获取某申请件最后一次初审审批金额
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月2日 下午4:14:43
	 */
	String findLastFirstApprovalAmount(@Param("loanNo") String loanNo);

	/**
	 * 根据借款编号查询最近一次审批记录
	 * 
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月9日 下午2:15:36
	 */
	ApprovalHistory findLastApprovalHistoryByLoanNo(@Param("loanNo") String loanNo);

	/**
	 * 根据id修改总负债率和内部负债率(规则引擎)
	 * 
	 * @author dmz
	 * @date 2017年7月6日
	 * @param map
	 * @return
	 */
	int updateResponsibleByLoanNo(Map<String, Object> map);
	
	/**
	 * 查询最后一次审批通过的审批意见列表
	 * 
	 * @param map
	 * @return
	 * @author JiaCX
	 * @date 2017年7月18日 上午10:00:14
	 */
	List<ApprovalHistory> findLastPassedOperateList(Map<String, Object> map);

	/**
	 * 根据单号查询最后一个审批意见
	 * @param approvalHistory
	 * @return
	 * @author lihuimeng
	 */
	ApprovalHistory findLastApprovalByLoanNo(ApprovalHistory approvalHistory);
}