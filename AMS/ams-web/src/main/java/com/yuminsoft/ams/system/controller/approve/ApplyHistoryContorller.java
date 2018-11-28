package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Maps;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.ApprovalOpinionHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApproveHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 审核历史
 * 
 * @author dmz
 * @date 2017年6月3日
 */
@Controller
@RequestMapping("/applyHistory")
public class ApplyHistoryContorller {
	@Autowired
	private ApplyHistoryService applyHistoryService;

	@Autowired
	private ApprovalHistoryService approvalHistoryService;

	/**
	 * 已完成页面查询出最后一次信审操作
	 * 
	 * @author dmz
	 * @date 2017年6月3日
	 * @return
	 */
	@RequestMapping("/getLastOpertionState/{loanNo}")
	@ResponseBody
	public Result<ApplyHistory> getLastOpertionState(@PathVariable String loanNo) {
		Result<ApplyHistory> result = new Result<ApplyHistory>(Type.FAILURE);
		ApplyHistory apply = applyHistoryService.getLastOperationState(loanNo);
		if (null != apply) {
			result.setType(Type.SUCCESS);
			result.setData(apply);
		}
		return result;
	}
	/**
	 * 已完成页面查询出最后一次信审操作
	 * 
	 * @author dmz
	 * @date 2017年6月3日
	 * @return
	 */
	@RequestMapping("/getWorkbenchStateByLoanNo/{loanNo}")
	@ResponseBody
	public Result<ApplyHistory> getWorkbenchStateByLoanNo(@PathVariable String loanNo) {
		Result<ApplyHistory> result = new Result<ApplyHistory>(Type.FAILURE);
		ApplyHistory apply = applyHistoryService.getWorkbenchStateByLoanNo(loanNo);
		if (null != apply) {
			result.setType(Type.SUCCESS);
			result.setData(apply);
		}
		return result;
	}


	/**
	 * 查询页面
	 * @author wangzx
	 * @date 2017年09月27日
	 */
	@RequestMapping("/index")
	public String applyHistroy() {
		return "/system/applyHistory/applyHistory";
	}

	/**
	 * 查询操作日志列表
	 * @author wangzx
	 * @date 2017年09月27日
	 */
	@RequestMapping("/getApplyHistoryList")
	@ResponseBody
	public ResponsePage<ApproveHistoryVO> getApplyHistoryList(String loanNo) {
		if(StringUtils.isNotEmpty(loanNo)) {
			Map<String,Object> map  = Maps.newHashMap();
			map.put("loanNo",loanNo);
			return applyHistoryService.getApplyHistoryList(map);
		} else {
			return null;
		}

	}

	/**
	 * 查询审批日志列表
	 * @author wangzx
	 * @date 2017年12月5日
	 */
	@RequestMapping("/getApproveHistoryList")
	@ResponseBody
	public ResponsePage<ApprovalOpinionHistoryVO> getApproveHistoryList(String loanNo) {
		if(StringUtils.isNotEmpty(loanNo)) {
			return approvalHistoryService.getHistoryListByLoanNo(loanNo);
		} else {
			return null;
		}

	}

	/**
	 * 复议查询出拒绝信息
	 * @param loanNo
	 * @return
	 */
	@RequestMapping("/getApplyHistoryRejectByLoanNo/{loanNo}")
	@ResponseBody
	public Result<ApplyHistory> getApplyHistoryRejectByLoanNo(@PathVariable String loanNo) {
		Result<ApplyHistory> result = new Result<ApplyHistory>(Type.FAILURE);
		ApplyHistory applyHistory = applyHistoryService.getApplyHistoryRejectByLoanNo(loanNo);
		if (null != applyHistory){
			result.setData(applyHistory);
			result.setType(Type.SUCCESS);
		}
		return result;
	}

}
