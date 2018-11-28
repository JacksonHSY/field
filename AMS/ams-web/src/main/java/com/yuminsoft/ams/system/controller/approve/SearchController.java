package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.integratedsearch.ReqIntegratedSearchVO;
import com.ymkj.ams.api.vo.request.integratedsearch.ReqQueryLoanLogVO;
import com.ymkj.ams.api.vo.response.integratedsearch.ResIntegratedSearchVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.service.approve.SearchCompService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ReturnAccountCardVO;
import com.yuminsoft.ams.system.vo.apply.ReturnRepaymentDetailVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 综合查询
 * @author fusj
 *
 */

@Controller
@RequestMapping("/search")
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private IntegrateSearchExecuter integrateSearchExecuter;
	
	@Autowired
	private SearchCompService searchCompServiceImpl;
	
	@Autowired
    private BmsLoanInfoService bmsLoanInfoServiceImpl;
	
	/** pic文件管理地址 */
	@Value("${ams.pic.image.url}")
	private String picImageUrl;

	/** pic信审查询 */
	@Value("${ams.pic.image.picApprovalQuery}")
	private String picApprovalQuery;
	
	/** pic信审环节 */
    @Value("${ams.pic.image.picApproval}")
    private String picApproval;

	@Value("${sys.code}")//系统编码
	private String sysCode;
	
	@Value("${sys.credit.zx}")
	private String sysCreditZX;
	
	
	@RequestMapping("/index")
	public @ResponseBody ResponsePage<ResIntegratedSearchVO> getPage(ReqIntegratedSearchVO vo , RequestPage requestPage, HttpServletRequest request, Boolean flag) {
		ResponsePage<ResIntegratedSearchVO> page = new ResponsePage<ResIntegratedSearchVO>();
		if(flag){
		    try{
		        page = searchCompServiceImpl.search(vo,requestPage,request);
            }catch(Exception e){
                if(e instanceof TimeoutException){
                    LOGGER.error("-------------综合查询超时-------------", e);
                    page.setTotal(-1L);//认为-1代表查询超时，前台会据此解析弹框超时
                }else {
                	LOGGER.error("-------------综合查询异常-------------", e);
                }
            }
		}
		return page;
	}
	
	
	/**
	 * 返回(综合查询)办理页面
	 * 
	 * @author Jia CX
	 * @date 2018年1月16日 下午4:30:58
	 * @notes
	 * 
	 * @param loanNo
	 * @param queryParams
	 * @param model
	 * @param session
	 * @param approveType	0:综合查询，1：初审改派，2：终审改派，3：初审已完成，4：终审已完成，5：内部匹配，6：复议已完成
	 * @return
	 */
	@RequestMapping("/handle/{loanNo}/{approveType}")
	public String handle(@PathVariable String loanNo, String queryParams, Model model, HttpSession session, @PathVariable String approveType) {
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("picImageUrl", picImageUrl);
		model.addAttribute("picApproval", picApprovalQuery);
		model.addAttribute("sysCode",sysCode);
		model.addAttribute("jobNumber",ShiroUtils.getCurrentUser().getUsercode());//工号
		model.addAttribute("operator",ShiroUtils.getCurrentUser().getName());//操作人姓名
		model.addAttribute("sysCreditZX", sysCreditZX);
		model.addAttribute("approveType", approveType);
		model.addAttribute("applyBasiceInfoJSON", JSONObject.toJSONString(applyBasiceInfo));
		return "/apply/integratedApplyInfo";
	}
	
	/**
	 * 查看
	 * 还款明细页面 
	 * 
	 * @author 
	 * @date 2017年3月2日 上午10:38:46
	 */
	@RequestMapping("/integratedQueryViewDetails/{loanNo}")
	public String integratedQueryViewDetails(@PathVariable String loanNo,Model model, HttpSession session) {
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
	    model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("loanNo", loanNo);
		model.addAttribute("loanVo", searchCompServiceImpl.findLoan(loanNo,applyBasiceInfo.getIdNo()));
		model.addAttribute("repaySumVo", searchCompServiceImpl.findRepaySum(loanNo));
		return "/apply/integratedQueryViewDetails";
	}

	/**
	 * 还款详细信息数据
	 * 
	 * @author 
	 * @date 
	 */
	@RequestMapping("/integratedQueryDetailsInfo")
	public @ResponseBody ResponsePage<ReturnRepaymentDetailVo> integratedQueryDetailsInfo(RequestPage requestPage,String loanNo) {
		
		List<ReturnRepaymentDetailVo> list =searchCompServiceImpl.findReturnRepaymentDetail(loanNo);
		ResponsePage<ReturnRepaymentDetailVo> page = new ResponsePage<ReturnRepaymentDetailVo>();
		if(list !=null){
			page.setRows(list);
			page.setTotal(list.size());
		}
		return page;

	}

	/**
	 * 加载查看详情-查看账单信息数据
	 * 
	 * @author 
	 * @date 
	 */
	@RequestMapping("/integratedQueryBillInfo")
	public @ResponseBody ResponsePage<ReturnAccountCardVO> integratedQueryBillInfo(RequestPage requestPage,String loanNo) {
		return searchCompServiceImpl.findReturnAccountCard(loanNo,requestPage);
	}

	
	
	
	/**
	 * 日志
	 * 
	 * @author 
	 * @date 2017年3月7日 下午1:43:53
	 */
	 @RequestMapping("/integratedQueryLog/{loanNo}")
	  public String insideMatchViewInfo(@PathVariable String loanNo,Model model,HttpServletRequest request) {
		 ReqQueryLoanLogVO requestVo = new ReqQueryLoanLogVO();
		 requestVo.setLoanNo(loanNo);
		 requestVo.setServiceCode(ShiroUtils.getCurrentUser().getUsercode());
		 requestVo.setServiceName(ShiroUtils.getCurrentUser().getName());
		 requestVo.setSysCode(sysCode);
		 requestVo.setIp(WebUtils.retrieveClientIp(request));
		 Response<List<ResBMSQueryLoanLogVO>> response=  integrateSearchExecuter.queryLoanLog(requestVo);
	    LOGGER.info("返回借款系统综合查询 params:{} result:{}", JSON.toJSONString(loanNo), JSON.toJSONString(response));
	    if (null != response && AmsCode.RESULT_SUCCESS.equals(response.getRepCode())) {
	      model.addAttribute("LoanLogList", response.getData());
	    }
	    return "/apply/integratedQueryLog";
	  }
	 
	 /**
	  * 查询复议日志
	  * 
	 * @author Jia CX
	 * <p>2018年6月20日 上午11:10:01</p>
	 * 
	 * @param loanNo
	 * @param model
	 * @return
	 */
	@RequestMapping("/reviewLog/{loanNo}")
	 public String reviewLog(@PathVariable String loanNo, Model model){
		 model.addAttribute("applyBasiceInfo", bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, false));
		 model.addAttribute("reviewLogList",searchCompServiceImpl.getReviewLog(loanNo));
		 return "/apply/reviewLog";
	 }
}
