package com.yuminsoft.ams.system.controller.quality;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.vo.request.apply.*;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.service.quality.QualityCheckResService;
import com.yuminsoft.ams.system.service.quality.QualityControlDeskService;
import com.yuminsoft.ams.system.service.quality.QualityErrorCodeService;
import com.yuminsoft.ams.system.service.quality.QualityFeedBackService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ApprovalOperationVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 质检工作台
 * @author 
 */
@Controller
@RequestMapping("/qualityControlDesk")
public class QualityControlDeskController extends BaseController {
	@Autowired
	private QualityControlDeskService qualityControlDeskService;
	
	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;
	
	@Autowired
	private QualityCheckResService qualityCheckResService;
	
	@Autowired
	private CreditzxService creditzxService;
	
	@Autowired
	private ApplyInfoExecuter applyInfoExecuter;
	
	@Autowired
	private ApprovalHistoryService approvalHistoryService;

	@Autowired
	private QualityErrorCodeService errorCodeService;
	
	@Autowired
	private ApplyHistoryService applyHistoryService ;
	
	@Autowired
	private QualityFeedBackService qualityFeedBackService;
	@Value("#{new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse(\"${master.loanA.date}\")}")
	private Date masterLoanADate;
	/**
	 * 质检工作台
	 * @return
	 */
	@RequestMapping("/qualityControlDesk")
	public String qualityControlDesk(HttpServletRequest request, Model model) {
		// 查询当前登录人员最高角色code放在session中
		Map<String, String> infoMap = qualityControlDeskService.getQualityRolesInfo(ShiroUtils.getCurrentUser().getUsercode());
		
		request.getSession().setAttribute("roleCode",infoMap.get("roleCode"));
		model.addAttribute("qualityUser", ShiroUtils.getCurrentUser().getUsercode());

		return "quality/qualityControlDesk/qualityControlDesk";
	}
	
	/**
	 * 查询质检待处理列表
	 * @return
	 */
	@RequestMapping("/toDoPageList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getToDoPage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo) {
		return qualityControlDeskService.getPageList(requestPage, qualityControlDeskVo, QualityEnum.MenuFlag.质检待处理.getCode());
	}
	
	/**
	 * 查询质检已完成列表
	 * @return
	 */
	@RequestMapping("/donePageList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getDonePage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo) {
		return qualityControlDeskService.getPageList(requestPage, qualityControlDeskVo, QualityEnum.MenuFlag.质检已完成列表.getCode());
	}
	
	/**
	 * 返回质检结论
	 * @author lihuimeng
	 * @date 2017年6月8日 下午5:38:56
	 */
	@RequestMapping("/qualityOpinion")
	public String qualityOpinion(Model model,HttpServletRequest request) {
		String loanNo = request.getParameter("loanNo");	// 获取借款编号
		
		//获取申请件基本信息
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
		model.addAttribute("applyBasiceInfo",applyBasiceInfo);
		
		String flag = request.getParameter("flag");		// 获取只读标志
		String checkUser = request.getParameter("checkUser");	
		Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("approvalMap");//获取session里面的审核信息
		String approvalMap = JSON.toJSONString(map.get(loanNo));//获取本案件的审核信息
		List<QualityCheckResult> checkResList = qualityControlDeskService.getCheckResForShow(loanNo);//查询该案件的历史质检结论
		String jsonCheckResList = JSON.toJSONString(checkResList);

		// 如果没有任何质检意见，则带出初审领导初始值
		if(CollectionUtils.isEmpty(checkResList)){
			//获取审核时的直接上级领导
			String checkLeader = qualityControlDeskService.getApprovalLeader(loanNo, EnumConstants.RtfState.XSCS.getValue());
			model.addAttribute("checkLeader", checkLeader);
		}

		model.addAttribute("loanNo", loanNo);
		model.addAttribute("flag", flag);
		model.addAttribute("approvalMapJson", approvalMap);
		model.addAttribute("checkResListJson", jsonCheckResList);
        model.addAttribute("picImgUrl", picImageUrl + "/api/filedata");
        model.addAttribute("nodeKey", "qualityCheck");
        model.addAttribute("sysName", sysCode);
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());
        model.addAttribute("jobNumber", ShiroUtils.getAccount());
        model.addAttribute("checkUser", checkUser);
        
		LOGGER.info("=====质检意见表查看======");
		return "quality/qualityControlDesk/addQualityOpinion";
	}
	
	/**
	 * 完成质检
	 * @return 
	 */
	@RequestMapping("/completeQuality/{loanNo}/{flag}")
	@ResponseBody
	public Result<String> completeQuality(HttpServletRequest request, @PathVariable String loanNo,@PathVariable String flag, @RequestBody List<QualityCheckResult> resList) {
		String checkUser = request.getParameter("checkUser");
		return qualityControlDeskService.completeQuality(resList, loanNo,flag,checkUser);
	}
	
	/**
	 * 申请复核
	 * @author lihuimeng
	 * @date 2017年6月10日 下午2:55:02
	 */
	@RequestMapping("/reCheck/{loanNo}")
	@ResponseBody
	public Result<String> reCheck(HttpServletRequest request, @PathVariable String loanNo, @RequestBody List<QualityCheckResult> qualityCheckResultList){
		String checkUser = request.getParameter("checkUser");
		Result<String> result = qualityControlDeskService.reCheck(request, qualityCheckResultList, loanNo, checkUser);
		return result;
	}
	
	/**
	 * 暂存
	 * @author lihuimeng
	 * @date 2017年6月12日 下午2:54:07
	 */
	@RequestMapping("/pauseQuality/{loanNo}")
	@ResponseBody
	public Result<String> pauseQuality(HttpServletRequest request, @PathVariable String loanNo, @RequestBody List<QualityCheckResult> qualityCheckResultList) {
		String checkUser = request.getParameter("checkUser");
		return qualityControlDeskService.pauseQuality(qualityCheckResultList,loanNo, checkUser);
	}

	/**
	 * @author lihuimeng
	 * @date 2017年6月3日 下午2:09:34
	 * 返回质检处理页面
	 */
	@RequestMapping("/qualityReceive")
	public String qualityReceive(HttpServletRequest request, Model model, HttpSession session) {
		//跳转标识，判断是待处理列表请求过来，还是已完成列表请求过来
		String flag = request.getParameter("flag");
		String loanNo = request.getParameter("loanNo");
		String qualityCheckId = request.getParameter("qualityCheckId");
		String checkUser = request.getParameter("checkUser");
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		PersonHistoryLoanVO reqvo = new PersonHistoryLoanVO();
		reqvo.setLoanNo(loanNo);
		reqvo.setSysCode(sysCode);
		Response<ApplyEntryVO> applyEntryVO = applyInfoExecuter.getApplyDetailReplaceEnum(reqvo);
		LOGGER.info("查询借款基本信息 params:{} result:{}", JSON.toJSONString(reqvo), JSON.toJSONString(applyEntryVO));
		if (null != applyEntryVO && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(applyEntryVO.getRepCode())) {
			ApplyInfoVO applyInfoVO = applyEntryVO.getData().getApplyInfoVO();
			if (applyInfoVO != null) {
				applyBasiceInfo.setCreditApplication(applyInfoVO.getCreditApplication());
			}
		}
		// 查询当前审批意见
		Result<ApprovalOperationVO> currentApprovalHistory = approvalHistoryService.getCurrentApprovalHistory(loanNo);
		model.addAttribute("currentApprovalHistory", currentApprovalHistory.getData());// 返回当前审批意见
		//查询欺诈信息
		Result<JSONObject> result = creditzxService.getSuanHuaAntifraud(loanNo);
		
		model.addAttribute("flag", flag);
		model.addAttribute("loanNo", loanNo);
		model.addAttribute("qualityCheckId", qualityCheckId);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("checkUser", checkUser);
		//增加反欺诈信息
		model.addAttribute("creditInfo",result.getData());
		//添加系统信息
		model.addAttribute("picImageUrl", picImageUrl);
		model.addAttribute("picApproval", picApproval);
		model.addAttribute("sysCode", sysCode);
		model.addAttribute("jobNumber",ShiroUtils.getCurrentUser().getUsercode());//工号
		model.addAttribute("operator",ShiroUtils.getCurrentUser().getName());//操作人姓名
		model.addAttribute("loanNo",loanNo);
		//获取审核日志表最后一个初审审核信息
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loanNo", loanNo);
		map.put("rtfState", EnumConstants.RtfState.XSCS.getValue());
		ApplyHistory applyHistory = applyHistoryService.getDoCheck(map);
		
		model.addAttribute("applyHistory",applyHistory);
		// 获取初审、终审、领导别名
		Map<String, Object> approvalLoanNoMap = qualityControlDeskService.getApprovalName(loanNo, session.getAttribute("roleCode").toString());
		Map<String, Object> approvalMap = null;
		if(null != session.getAttribute("approvalMap")){
			approvalMap = (Map<String, Object>) session.getAttribute("approvalMap");
		}else{
			approvalMap = new HashMap<String, Object>();
		}
		approvalMap.put(loanNo, approvalLoanNoMap);
		session.setAttribute("approvalMap", approvalMap);

		return "quality/qualityControlDesk/qualityReceive";
	}

	/**
	 * 返回审核日志备注
	 * @author lihuimeng
	 * @date 2017年6月9日 下午5:33:06
	 */
	@RequestMapping("/logNotesInfo/{loanNo}")
	public String logNotesInfo(@PathVariable String loanNo, HttpServletRequest request, Model model) {
		List<ResBMSQueryLoanLogVO> result = new ArrayList<ResBMSQueryLoanLogVO>();
		Map<String, Object> map = new HashMap<String, Object>();
		map = (Map<String, Object>) request.getSession().getAttribute("approvalMap");//获取session里面的审核信息
		Map<String, Object> approvalMap = (Map<String, Object>) map.get(loanNo);//获取本案件的审核信息
		try {
			result = qualityControlDeskService.queryLoanLog(loanNo, WebUtils.retrieveClientIp(request), approvalMap);
		} catch (Exception e) {
			LOGGER.info("返回日志备注页面异常", e);
		}
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
        model.addAttribute("applyBasiceInfo",applyBasiceInfo);
		model.addAttribute("loanLogList", result);
		return "/approve/first/firstLogNotesInfo";
	}

	/**
	 * 质检意见查询
	 * @return
	 */
	@RequestMapping("/quryQualityOpion")
	public String quryQualityOpion() {
		LOGGER.info("=====质检意见查询====");
		return "quality/qualityControlDesk/quryQualityOpion";
	}
	
	/**
	 * 差错代码下拉框数据查询
	 */
	@RequestMapping("/quryErrorCode")
	@ResponseBody
	public List<QualityErrorCode> quryErrorCode() {
		return errorCodeService.findAllUsableErrorCodes();
	}
	
	/**
	 * 返回质检结论查询界面
	 * @author lihuimeng
	 * @date 2017年6月12日 下午3:46:46
	 */
	@RequestMapping("/queryQualityOpoin/{loanNo}")
	public String quryQualityOpion(HttpServletRequest request,@PathVariable String loanNo, Model model) {
		//获取申请件基本信息
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
        model.addAttribute("applyBasiceInfo",applyBasiceInfo);
        
		Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("approvalMap");//获取session里面的审核信息
		String approvalMap = JSON.toJSONString(map.get(loanNo));//获取本案件的审核信息
		List<QualityCheckResult> checkResList = qualityControlDeskService.getCheckResForShow(loanNo);//查询该案件的历史质检结论
		String jsonCheckResList = JSON.toJSONString(checkResList);
		model.addAttribute("loanNo", loanNo);
		model.addAttribute("approvalMapJson", approvalMap);
		model.addAttribute("checkResListJson", jsonCheckResList);
		return "quality/qualityControlDesk/queryQualityOpoin";
	}
	
	/**
	 * 质检意见列表
	 * @return
	 */
	@RequestMapping("/qualityOpoionPageList/{qualityCheckId}")
	@ResponseBody
	public ResponsePage<QualityCheckResult> qualityOpoionPageList(RequestPage requestPage, @PathVariable String qualityCheckId) {
		QualityCheckResult checkRes = new QualityCheckResult();
		checkRes.setQualityCheckId(Long.valueOf(qualityCheckId));
		return qualityControlDeskService.getOpoinPageList(requestPage, checkRes);
	}
	
	/**
	 * 返回质检意见结论
	 * 
	 */
	@RequestMapping("/getHisttoryOpinion/{qualityCheckId}")
	@ResponseBody
	public List<QualityCheckResult> getHisttoryOpinion(  @PathVariable long qualityCheckId) {
		return qualityCheckResService.getQualityCheckOpinion(qualityCheckId);
	}

	@RequestMapping("/getQualityCheckOpinion/{qualityCheckId}")
	public String getQualityCheckOpinion(RequestPage requestPage, @PathVariable long qualityCheckId, Model model) {
		model.addAttribute("qualityCheckId", qualityCheckId);
		return "quality/qualityControlDesk/QualityHistoryOpinion";
	}
	/**
	 * 质检查询导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportQueryList")
	@ResponseBody
	public void exportQueryList(HttpServletRequest request,HttpServletResponse response,QualityControlDeskVo qualityControlDeskVo) {
		LOGGER.info("=====质检综合查询信息导出======");
		qualityControlDeskService.exportQueryList(request,response,qualityControlDeskVo);
	}
	
	/**
	 * auther:lihm
	 * 导出列表
	 */
	@RequestMapping("/exportQualityList/{type}")
	@ResponseBody
	public Result<String> exportQualityList(HttpServletRequest request, HttpServletResponse response, QualityControlDeskVo controlDeskVo,@PathVariable String type){
		
		return qualityControlDeskService.exportQualityList(request, response, controlDeskVo,type);
	}
	
	
	/**
	 * 返回审批意见
	 * @author lihuimeng
	 * @date 2017年6月9日 下午3:12:33
	 */
	@RequestMapping("/qualityApprovalOpinion/{loanNo}")
	public String qualityApprovalOpinion(@PathVariable String loanNo, Model model, HttpSession session) {
		ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		ApprovalOperationVO approvalOperationVO = approvalHistoryService.getApprovalHistoryByLoanNo(loanNo);
		// 有审批记录
		if (CollectionUtils.isNotEmpty(approvalOperationVO.getApprovalHistoryList())) {
			model.addAttribute("courtCheck",  approvalOperationVO.getApproveCheckInfo().getCourtCheckException() == 0 ? "无异常" : "异常");		// 法院网核查情况
			model.addAttribute("insideMatch", approvalOperationVO.getApproveCheckInfo().getInternalCheckException() == 0 ? "无异常" : "异常");	// 内部匹配情况

            // 替换姓名为别称（质检专用）
            List<ApprovalHistory> approvalHistoryList = qualityControlDeskService.setApprovalPersonName(approvalOperationVO.getApprovalHistoryList(), loanNo, session);
			approvalOperationVO.setApprovalHistoryList(approvalHistoryList);

			// 查询当前审批意见
			Result<ApprovalOperationVO> currentApprovalHistory = approvalHistoryService.getCurrentApprovalHistory(loanNo);
			model.addAttribute("currentApprovalHistory", currentApprovalHistory.getData());// 返回当前审批意见

			model.addAttribute("approvalInfoJson", JSONObject.toJSONString(approvalOperationVO));// 返回审批意见
			model.addAttribute("approvalInfo", approvalOperationVO);// 返回审批意见
		}
        


		model.addAttribute("applyBasicInfoJson", JSONObject.toJSONString(applyBasicInfo));

		model.addAttribute("applyBasiceInfo", applyBasicInfo);
		model.addAttribute("resEmployeeVO", ShiroUtils.getCurrentUser());
		Result<AuditRulesVO> auditRulesVO = bmsLoanInfoServiceImpl.queryAuditRulesData(loanNo);
		model.addAttribute("auditRulesVO", auditRulesVO.getData());


		if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
			// 查询借款信息
			ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,true);
			model.addAttribute("applyInfo", resApplicationInfoVO);
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(resApplicationInfoVO, SerializerFeature.DisableCircularReferenceDetect));
			return "/approve/zdmoney/queryMoneyApprovalOpinion";
		}else {
			// 查询借款信息
			ApplyEntryVO applyInfo = bmsLoanInfoServiceImpl.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
			model.addAttribute("applyInfo", applyInfo);
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(applyInfo));
			// 判断网购达人A贷首次提交初审时间如果大于等于2018-02-06 则不显示12个月外收货地址
			model.addAttribute("isMasterLoanADate", DateUtils.daysOfTwo(applyBasicInfo.getFirstSubmitAudit(),masterLoanADate) < 1);

		}
		return "/approve/finish/finishApprovalOpinionWithoutAction";
	}
	
	/**
	 * 返回质检日志
	 * @author lihuimeng
	 * @date 2017年6月16日 上午11:28:26
	 */
	@RequestMapping("/qualityLog/{loanNo}")
	public String qualityLog(@PathVariable String loanNo, Model model) {
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
        model.addAttribute("applyBasiceInfo",applyBasiceInfo);
		model.addAttribute("loanNo", loanNo);
		return "/quality/qualityControlDesk/qualityReceive/qualityLogInfo";
	}
	
	/**
	 * 获取质检日志列表
	 * @author lihuimeng
	 * @date 2017年6月16日 上午11:42:23
	 */
	@RequestMapping("/queryQualityLog/{loanNo}")
	@ResponseBody
	public ResponsePage<QualityLogVo> queryQualityLog(RequestPage requestPage, @PathVariable String loanNo) {
		return qualityControlDeskService.queryQualityLog(requestPage,loanNo);
	}
	
	/**
	 * 获取信审所有领导 
	 * @author lihuimeng
	 * @date 2017年6月17日 下午3:46:28
	 */
	@RequestMapping("/getApproveLeader")
	@ResponseBody
	public List<ResEmployeeVO> getApproveLeader() {
		return qualityControlDeskService.getQualityLeader();
	}
	
    /**
     * 删除附件
     * @author lihuimeng
     * @date 2017年7月10日 下午5:47:51
     */
    @RequestMapping(value = "/deleteAttachmentById", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteAttachmentById(Long id, String jobNumber, String operator, String loanNo) {
        return qualityFeedBackService.deleteAttachmentById(id, jobNumber, operator, picImageUrl, loanNo);
    }
}

