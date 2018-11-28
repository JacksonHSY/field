package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSApplicationPartVO;
import com.ymkj.ams.api.vo.response.master.ResBMSOrgLimitChannelVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.service.approve.ApplyDocService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ApplyDoc;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 申请件信息维护
 * @author fusj
 */
@Controller
@RequestMapping("/applyDoc")
public class ApplyDocController extends BaseController {
	
	@Autowired
	private ApplyDocService applyDocServiceImpl;

	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;

	@Autowired
	private CommonParamService commonParamService;
	
	/**
	 * 分页查询申请件列表
	 * @param requestPage
	 * @param applyDoc
	 * @param statu	1:通过件，2：拒绝件
	 * @return
	 * @author JiaCX
	 * @date 2017年4月13日 下午5:13:06
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ResponsePage<ResBMSApplicationPartVO> getPage(RequestPage requestPage, ApplyDoc applyDoc, String statu) {
		ResponsePage<ResBMSApplicationPartVO> page = new ResponsePage<ResBMSApplicationPartVO>();
		PageResponse<ResBMSApplicationPartVO> response = applyDocServiceImpl.getApplicationPartList(sysCode,requestPage,applyDoc,statu);
		if (null != response && response.isSuccess()) {
			page.setRows(response.getRecords());
			page.setTotal(response.getTotalCount());
		}
		return page;
	}
	
	/**
	 * 返回-->通过件拒绝页面
	 * @param model
	 * @param rtfStatus
	 * @return
	 */
	@RequestMapping("/applyInfoDeny/{rtfStatus}")
	public String refuseApplyDoc(Model model,@PathVariable String rtfStatus) {
	    model.addAttribute("rtfStatus",rtfStatus);
		return "/apply/applyInfoDeny";
	} 
	
	/**
	 * 通过件拒绝
	 * @param applyDoc
	 * @param request
	 * @return
	 * @author JiaCX
	 * @date 2017年4月13日 上午11:04:26
	 */
	@RequestMapping("/applyInfoDenySubmit")
	public @ResponseBody Result<String> applyInfoDenySubmit(ApplyDoc applyDoc,HttpServletRequest request) {
		return applyDocServiceImpl.refusePassedApplicationPart(sysCode, applyDoc,request);
	} 
	
	/**
	 * 返回-->通过件修改页面
	 */
	@RequestMapping("/applyInfoUpdate")
	public String applyInfoUpdate(ApplyDoc applyDoc, Model model, HttpSession session) {
		// 获取申请件
		ReqInformationVO bmsBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), applyDoc.getLoanNo(), false);
		applyDocServiceImpl.setMaxAndMin(applyDoc);

		// 获取签约合同金额的上限
		BigDecimal contractAmtLimit = BigDecimal.valueOf(99999999);
		Map<String, Object> params = Maps.newHashMap();
		params.put("paramKey", "contractAmtLimit");
		params.put("paramType", "SYSTEM");
		SysParamDefine sysParamDefine = commonParamService.findOne(params);
		if(sysParamDefine != null){
			contractAmtLimit = new BigDecimal(sysParamDefine.getParamValue());
		}

		model.addAttribute("applyDocJson", JSONObject.toJSONString(applyDoc));
		model.addAttribute("basicInfoJson", JSONObject.toJSONString(bmsBasicInfo));
		model.addAttribute("contractAmtLimit", contractAmtLimit);

		return "/apply/applyInfoUpdate";
	}
	
	/**
	 * 根据审批额度获取该产品的审批期限列表
	 * @param loanNo
	 * @param accLmt
	 * @param request
	 * @return
	 * @author JiaCX
	 * @date 2017年5月19日 下午6:21:38
	 */
	@RequestMapping("/getApprovalPeriodList/{loanNo}/{accLmt}")
	@ResponseBody
	public List<ResBMSOrgLimitChannelVO> getApprovalPeriodList(@PathVariable String loanNo, @PathVariable String accLmt, HttpServletRequest request){
		return applyDocServiceImpl.getApprovalPeriodList(sysCode, loanNo, accLmt, request);
	}
	
	/**
	 * 通过件修改
	 */
	@RequestMapping("/applyInfoUpdateSubmit")
	@ResponseBody
	public Result<String> applyInfoUpdateSubmit(ApplyDoc applyDoc,HttpSession session,HttpServletRequest request) {

		return applyDocServiceImpl.editPassedApplicationPart(sysCode, applyDoc, session,request);
	}
	
	/**
	 * 返回-->拒绝件修改页面件
	 */
	@RequestMapping("/applyInfoDenyReasonUpdate/{rtfStatus}")
	public String applyInfoDenyReasonUpdate(ApplyDoc applyDoc,Model model,@PathVariable String rtfStatus) {
		model.addAttribute("applyDoc",applyDoc);
		model.addAttribute("rtfStatus",rtfStatus);
		return "/apply/applyInfoDenyUpdate";
	}
	
	/**
	 * 拒绝件修改
	 * 
	 * @param applyDoc
	 * @return
	 * @author JiaCX
	 * @date 2017年4月13日 下午4:26:27
	 */
	@RequestMapping("/applyInfoDenyReasonUpdateSubmit")
	@ResponseBody
	public Result<String> applyInfoDenyReasonUpdateSubmit(ApplyDoc applyDoc,HttpSession session,HttpServletRequest request) {
		return applyDocServiceImpl.editRefusedApplicationPart(sysCode, applyDoc, request);
	}
	
}
