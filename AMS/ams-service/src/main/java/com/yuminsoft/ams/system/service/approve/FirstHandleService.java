package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.ReqBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSCheckVO;
import com.ymkj.bds.biz.api.vo.response.PhoneNumberResVO;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface FirstHandleService {

	/**
	 * 信审初审办理
	 * @author dmz
	 * @date 2017年3月17日
	 * @param applyVo
	 * @return
	 */
	public Result<String> updateFirstHandLoanNoService(ApplyHistoryVO applyVo, ReqInformationVO loanInfo);

	/**
	 * 初审改派批量导出方法
	 * @param request
	 * @param req
	 * @param res
	 * @author zhouwen
	 * @throws Exception
	 */
	public void exportExcel(ReqBMSReassignmentVo request, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 增加电话号码内匹配方法
	 * @param loanNo
	 * @author zhouw
	 * @return
	 */
	public List<PhoneNumberResVO> matchByPhoneNumber(String loanNo);

	/**
	 * 初审复核确认
	 * @param applyVo
	 * @param request
	 * @param passOrNot
	 *            0:退回；1：通过
	 * @return
	 * @author JiaCX
	 * @date 2017年5月9日 下午2:51:03
	 */
	public Result<String> firstApproveReview(ApplyHistoryVO applyVo, HttpServletRequest request, String passOrNot);

	/**
	 * 偿还能力不足时进行负债率校验
	 * 
	 * @param applyBasiceInfo
	 * @return
	 */
	public Result<String> repaymentInsufficientJudge(ReqInformationVO applyBasiceInfo,String rtfNodeState);

	/**
	 * 根据借款编号，查询申请件对应的入网时长和实名认证
	 * @param loanNo
	 * @param session
	 * @author wulj
	 * @return
	 */
	public List<MobileOnline> getMobileOnlineByLoanNo(String loanNo, HttpSession session);

    /**
     * 复核确认列表
     * 
     * @param requestPage
     * @return
     * @author JiaCX
     * @date 2017年10月9日 下午3:27:51
     */
    public ResponsePage<ResBMSCheckVO> getReviewConfirmList(RequestPage requestPage);

}
