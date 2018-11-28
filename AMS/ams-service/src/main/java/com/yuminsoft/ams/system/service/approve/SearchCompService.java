package com.yuminsoft.ams.system.service.approve;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ymkj.ams.api.vo.request.integratedsearch.ReqIntegratedSearchVO;
import com.ymkj.ams.api.vo.response.integratedsearch.ResIntegratedSearchVO;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderLog;
import com.yuminsoft.ams.system.vo.apply.LoanCoreVo;
import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.apply.RepaySumVo;
import com.yuminsoft.ams.system.vo.apply.ReturnAccountCardVO;
import com.yuminsoft.ams.system.vo.apply.ReturnRepaymentDetailVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 
 * @author fusj
 *综合查询
 */
public interface SearchCompService {
	
	
	public RProductVo monthPay(String productCode);

	/**
     * 核心帐卡信息接口
     * @param loanNo
     * @param 
     * @return
     */
    ResponsePage<ReturnAccountCardVO> findReturnAccountCard(String loanNo, RequestPage requestPage);

    /**
     * 核心还款详情接口
     * @param loanNo
     * @param 
     * @return
     */
    List<ReturnRepaymentDetailVo> findReturnRepaymentDetail(String loanNo);

    /**
     * 核心还款汇总接口
     * 
     * @param loanNo
     * @param 
     * @return
     */
    RepaySumVo findRepaySum(String loanNo);

    /**
     * 核心借款
     * 
     * @param loanNo
     * @param idNum
     * @return
     */
    LoanCoreVo findLoan(String loanNo, String idNum);

    /**
     * 综合查询
     * 
     * @param vo
     * @param requestPage
     * @return
     * @author JiaCX
     * @date 2017年7月7日 下午3:47:57
     */
    ResponsePage<ResIntegratedSearchVO> search(ReqIntegratedSearchVO vo, RequestPage requestPage, HttpServletRequest request);

	/**
	 * 查询复议日志
	 * 
	 * @author Jia CX
	 * <p>2018年6月20日 上午11:14:20</p>
	 * 
	 * @param loanNo
	 * @return
	 */
	List<ResReconsiderLog> getReviewLog(String loanNo);
	

}
