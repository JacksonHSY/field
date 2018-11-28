package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.response.audit.ResOffTheStocksAuditVO;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.finalApprove.FinishReformHandlerParamIn;
import com.yuminsoft.ams.system.vo.finalApprove.FinishReformListParamIn;
import com.yuminsoft.ams.system.vo.finalApprove.StaffOrderTaskVO;
import com.yuminsoft.ams.system.vo.finalApprove.ZsReassignmentVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 终审相关操作Service
 * 
 * @author Shipf
 * @date 
 * @param 
 */
public interface FinalHandleService {
	
	/**
	 * 终审操作公共方法
	 * 
	 * @param applyVo
	 * @param request
	 * @param operateType 1:正常操作，2：批量退回或者批量拒绝
	 * @return
	 * @author JiaCX
	 * @date 2017年7月11日 下午1:36:43
	 */
	Result<String> updatePubFinalUflo(ApplyHistoryVO applyVo, HttpServletRequest request, String operateType);
	
	/**
	 * 添加或修改审批意见(包括负债信息,资料核对信息)
	 * 
	 * @param approvalSaveVO
	 * @return
	 */
	boolean saveOrUpdateApprovalHistory(ApprovalSaveVO approvalSaveVO, HttpServletRequest request);

	/**
	 * 终审改派时根据选中的申请查找满足条件的处理人
	 * 
	 * @param finishReformHandleInputVO
	 * @return
	 * @author JiaCX
	 * @date 2017年4月19日 上午11:34:47
	 */
	public List<StaffOrderTaskVO> approvePersonList(FinishReformHandlerParamIn finishReformHandleInputVO);

	/**
	 * 终审改派工作台
	 * 
	 * @param request
	 * @param requestPage
	 * @return
	 * @author JiaCX
	 * @date 2017年4月20日 下午3:01:55
	 */
	public ResponsePage<ZsReassignmentVO> getFinishReformList(FinishReformListParamIn request,
			RequestPage requestPage);

	/**
	 * 终审改派借款接口更新
	 * @param reformVOList
	 * @param request
	 * @return
	 * @author JiaCX
	 * @date 2017年4月20日 下午3:07:48
	 */
	public boolean finishReform(ReformVO reformVO, HttpServletRequest request);

	/**
	 * 终审改派导出excel
	 * 
	 * @param queryParams
	 * @param req
	 * @param res
	 * @author JiaCX
	 * @throws Exception 
	 * @date 2017年4月20日 下午3:14:39
	 */
	public void exportExcel(FinishReformListParamIn request, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 终审已完成任务列表
	 * 
	 * @param requestPage
	 * @return
	 * @author JiaCX
	 * @param request 
	 * @param offEndDate 
	 * @param offStartDate 
	 * @date 2017年4月25日 上午10:20:39
	 */
	public ResponsePage<ResOffTheStocksAuditVO> getCompletedTask(RequestPage requestPage, String offStartDate, String offEndDate, HttpServletRequest request);

	/**
	 * 偿还能力不足时进行负债率校验
	 * 
	 * @param request
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月14日 下午2:29:37
	 */
	Result<String> repaymentInsufficientJudge(HttpServletRequest request, String loanNo);

    /**
     * 根据产品id，校验loanNo的申请件对应的资产信息是佛填写完整
     * 
     * @param result
     * @param loanNo
     * @param productCd
     * @return
     * @author JiaCX
     * @date 2017年6月1日 下午1:43:27
     */
    public Result<String> checkAssetsInfo(Result<String> result, String loanNo, String productCd);

    /**
     * 获取当前审批意见
     * 
     * @param loanNo
     * @return
     * @author JiaCX
     * @date 2017年6月5日 上午11:12:19
     */
    public ApprovalHistory getCurrentApprovalOption(String loanNo);

    /**
     * 获取某个员工的接单能力
     * 
     * @param approvalPerson
     * @param status 员工状态（0-正常；1-非正常）
     * @return
     * @author JiaCX
     * @date 2017年6月5日 上午11:50:08
     */
    public StaffOrderTask getStaffOrderTask(String approvalPerson, String status);

}
