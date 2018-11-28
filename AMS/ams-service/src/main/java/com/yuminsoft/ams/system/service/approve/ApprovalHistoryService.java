package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.*;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.Map;

public interface ApprovalHistoryService {

	/**
	 * 添加或修改审批意见(包括负债信息,资料核对信息)
	 * 
	 * @author dmz
	 * @date 2017年3月31日
	 * @param approvalSaveVO
	 * @return
	 */
	void saveOrUpdateApprovalHistory(ApprovalSaveVO approvalSaveVO);

	/**
	 * 前前添加或修改审批意见(包括负债信息,资料核对信息)
	 *
	 * @author dmz
	 * @date 2017年3月31日
	 * @param approvalSaveVO
	 * @param saveAssetsVO 资产信息
	 * @return
	 */
	void saveOrUpdateMoneyApprovalHistory(ApprovalSaveVO approvalSaveVO,ReqAssetsInfoVO saveAssetsVO);

	/**
	 * 根据接口编号查询审批意见(包括负债信息,资料核对信息)
	 * 
	 * @author dmz
	 * @date 2017年3月31日
	 * @param loanNo
	 * @return
	 */
	ApprovalOperationVO getApprovalHistoryByLoanNo(String loanNo);

	/**
	 * 根据借款编号和员工code查询未提交的审批意见
	 * 
	 * @author dmz
	 * @date 2017年4月7日
	 * @param loanNo
	 * @return
	 */
	ApprovalHistory getApprovalHistoryByLoanNoAndStaffCode(String loanNo);

	/**
	 * 根据借款编号和员工code查询终审当前审批意见
	 * 
	 * @author Shipf
	 * @date
	 * @param loanNo
	 * @return
	 */
	Result<ApprovalOperationVO> getCurrentApprovalHistory(String loanNo);

	/**
	 * 初审办理修改审批意见表状态
	 * 
	 * @author dmz
	 * @date 2017年4月17日
	 * @param rtfState
	 * @param rtfNodeState
	 * @param checkNodeState
	 */
	void updateFirstApprovalState(String loanNo, String rtfState, String rtfNodeState, String checkNodeState, String firstCheckOperation);

	/**
	 * 根据借款编号查询最近一次审批记录
	 * 
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月9日 下午1:55:42
	 */
	ApprovalHistory getLastApprovalHistoryByLoanNo(String loanNo);

	/**
	 * 审批意见对外接口VO(规则引擎调用)
	 * 
	 * @author dmz
	 * @date 2017年6月23日
	 * @param loanNo
	 * @param  assetTypeNow-页面时时传入勾选资产信息如果为空取数据库值
	 * @return
	 */
	ApprovalOpinionsVO getApprovalOpinions(String loanNo, String assetTypeNow);
	
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
	 * 根据借款编号查询最后一次终审通过审批记录
	 *
	 * @param loanNo
	 * @param rtfNodeState
	 * @return
	 * @author lihm
	 */
	Response<FinalApprovalOpinionVO> getLastApprovalByLoanNo(String loanNo, String rtfNodeState);

	/**
	 * 根据借款编号获取审批日志
	 * @param loanNo
	 * @return
	 */
	ResponsePage<ApprovalOpinionHistoryVO> getHistoryListByLoanNo (String loanNo);
}
