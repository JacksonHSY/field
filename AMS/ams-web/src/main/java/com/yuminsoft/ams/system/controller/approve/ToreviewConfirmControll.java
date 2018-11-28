package com.yuminsoft.ams.system.controller.approve;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSCheckVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.approve.FirstHandleService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 复核确认
 * @author fusj
 */
@Controller
@RequestMapping("/toreviewConfirm")
public class ToreviewConfirmControll extends BaseController{

	@Autowired
	private FirstHandleService firstHandleServiceImpl ;
	
	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;
	
	/**
	 * 借款系统 复核确认
	 * @author 
	 * @date 
	 */
	@RequestMapping("/index")
	public @ResponseBody ResponsePage<ResBMSCheckVO> getPage(RequestPage requestPage) {
		return firstHandleServiceImpl.getReviewConfirmList(requestPage);
	}
	
	
	/**
	 * 办理页面
	 * 
	 * @author 
	 * @date
	 * @return
	 */
	@RequestMapping("/handle/{loanNo}/{version}/{checkPerson}")
	public String finishApproveReceive(@PathVariable String loanNo, @PathVariable int version, String queryParams, Model model,@PathVariable String checkPerson, HttpSession session) {
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("picImageUrl", picImageUrl);
		model.addAttribute("picApproval", picApproval);
		model.addAttribute("sysCode",sysCode);
		model.addAttribute("jobNumber",ShiroUtils.getCurrentUser().getUsercode());//工号
		model.addAttribute("operator",ShiroUtils.getCurrentUser().getName());//操作人姓名
		model.addAttribute("sysCreditZX", sysCreditZX);
		model.addAttribute("checkPerson", checkPerson);
		//增加反欺诈信息
		//Result<JSONObject> result = creditzxService.getSuanHuaAntifraud(loanNo);
		//model.addAttribute("creditInfo",result.getData());
		return "/apply/handleInfo";
	}
	
	/**
	 * 复核确认操作
	 * 
	 * @param passOrNot 0：退回；1：通过
	 * @param applyHistoryVo 
	 * @param request
	 * @return
	 * @author JiaCX
	 * @date 2017年5月9日 下午2:53:42
	 */
	@RequestMapping("/submit/{passOrNot}")
	@UserLogs(link = "复核确认", operation = "复核通过/退回")
	public @ResponseBody Result<String> toreviewConfirm(@PathVariable String passOrNot, ApplyHistoryVO applyHistoryVo, HttpServletRequest request) {
		Result<String> result = new Result<String>();
		try {
			result = firstHandleServiceImpl.firstApproveReview(applyHistoryVo, request, passOrNot);
		} catch (Exception e) {
			LOGGER.error("复核确认提交失败{}", applyHistoryVo.getLoanNo(), e);
			result.setType(Type.FAILURE);
			result.addMessage("提交失败!");
		}
		return result;
	}
}
