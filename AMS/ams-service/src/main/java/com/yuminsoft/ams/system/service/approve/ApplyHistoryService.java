package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.request.audit.ReqCsRefusePlupdateStatusVO;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.vo.SortVo;
import com.yuminsoft.ams.system.vo.apply.ApproveHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.List;
import java.util.Map;

/**
 * 终审历史表数据维护
 * @author shipf
 * @date 2017年3月7日
 */
public interface ApplyHistoryService {

	/**
	 * 保存
	 *
	 * @param applyHistory
	 */
	int save(ApplyHistory applyHistory);

	/**
	 * 删除
	 *
	 * @param id
	 */
	int delete(Long id);

	/**
	 * 修改
	 *
	 * @param applyHistory
	 */
	int update(ApplyHistory applyHistory);

	/**
	 * 查询
	 *
	 * @param params
	 */
	List<ApplyHistory> findAll(Map<String, Object> params);

	/**
	 *
	 * @param params
	 * @return
	 */
	ApplyHistory findOne(Map<String, Object> params);

	/**
	 * 根据状态查询首次提交终审
	 * @param loanNo
	 * @author dmz
	 * @date 2017年3月21日
	 * @return
	 */
	List<ApplyHistory> findFirstSubmitToFinal(String loanNo);

	/**
	 * 已完成页面查询出最后一次信审操作
	 *
	 * @author dmz
	 * @date 2017年6月3日
	 * @param loanNo
	 * @return
	 */
	ApplyHistory getLastOperationState(String loanNo);

	/**
	 * 判断进件是否做过审核（质检用）
	 * @param map
	 * @return
	 */
	ApplyHistory getDoCheck(Map<String, Object> map);

	/**
	 * 根据借款编号查询出工作台是初审退回，终审退回，复核退回的最近审批历史记录
	 *
	 * @author zhouwen
	 * @date 2017年6月27日
	 * @param loanNo
	 * @return
	 */
	ApplyHistory getWorkbenchStateByLoanNo(String loanNo);

	/**
	 * 查询案件历史审批信息
	 * @author wangzx
	 * @date 2017年09月27日
	 */
	ResponsePage<ApproveHistoryVO> getApplyHistoryList(Map<String,Object> map);

	/**
	 * 获取最后一次审批历史信息(按照创建时间倒叙)
	 * @param loanNo	    借款编号
	 * @param rtfState      工作流
	 * @param rtfNodeState	工作流节点列表
	 * @param sortList 		排序列表
	 * @return 最后一次审批历史信息
	 */
	ApplyHistory getByLoanNoAndRtfStateInAndRtfNodeStateIn(String loanNo, List<String> rtfState, List<String> rtfNodeState, List<SortVo.Order> sortList);

	/**
	 * 获取最后一次审批历史信息(按照创建时间倒叙)
	 * @param loanNo			借款编号
	 * @param rtfState			工作流
	 * @param rtfNodeState		工作流节点列表
	 * @return 最后一次审批历史信息
	 */
	ApplyHistory getByLoanNoAndRtfStateAndRtfNodeState(String loanNo, String rtfState, String rtfNodeState, List<SortVo.Order> sortList);

	/**
	 * 获取审批历史信息(按照创建时间倒叙)
	 * @param loanNo		借款编号
	 * @param userCode		用户工号
	 * @param rtfState		工作流
	 * @param rtfNodeState	工作流节点
	 * @return
	 */
	List<ApplyHistory> getByLoanNoAndUserCodeAndRtfStateAndRtfNodeStateIn(String loanNo, String userCode, String rtfState, List<String> rtfNodeState);

	/**
	 * 批量改派审批日志写入
	 *
	 * @param reformVo
	 * @param sessionId
	 * @return
	 * @date 2017年05月04日
	 */
	 void saveReformApplyHistory(ReformVO reformVo, String sessionId);


	/**
	 * 批量退回或拒绝审批日志写入
	 *
	 * @param request
	 * @param sessionId
	 * @param type
	 * @return
	 * @date 2017年05月04日
	 */
	void saveReturnOrRejectApplyHistory(ReqCsRefusePlupdateStatusVO request, String sessionId, String type);

	/**
	 * 根据单号查询历史处理过的初审员(初审改派查询)
	 *
	 * @return
	 * @param-map:loanNoArray-借款编号逗号隔开;loanNoCount-借款编号个数
	 */
	List<ApplyHistory> getHistoryApplyDealsPerson(Map<String, Object> map);

	/**
	 * 复议查询出拒绝信息
	 * @param loanNo
	 * @return
	 */
	ApplyHistory getApplyHistoryRejectByLoanNo(String loanNo);
}
