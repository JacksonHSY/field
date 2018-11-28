package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.StringUtil;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.*;
import com.ymkj.ams.api.vo.request.audit.ReqBMSAdultOffTheStocksVo;
import com.ymkj.ams.api.vo.request.audit.ReqBMSReassignmentVo;
import com.ymkj.ams.api.vo.request.audit.first.ReqApplicationInfoVO;
import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAuditVo;
import com.ymkj.ams.api.vo.response.audit.ResOffTheStocksAuditVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResDetailDifferenceVO;
import com.ymkj.ams.api.vo.response.master.ResBMSEnumCodeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.bds.biz.api.vo.response.ApplicationInfoResVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationInfo;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.PhoneUtil;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.approve.FirstHandleService;
import com.yuminsoft.ams.system.service.approve.MobileHistoryService;
import com.yuminsoft.ams.system.service.approve.SearchCompService;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.core.CoreApiService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.ApprovalOperationVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.LoanCoreVo;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.firstApprove.MobileHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/***
 * 初审
 * 
 * @author dongmingzhi
 * @param
 * @date 2016年12月22日
 */
@Controller
@RequestMapping("/firstApprove")
public class FirstApproveController extends BaseController{

	@Autowired
	private MobileHistoryService mobileHistoryService;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;
	@Autowired
	private CreditzxService creditzxService;
	@Autowired
	private ApprovalHistoryService approvalHistoryService;
	@Autowired
	private FirstHandleService firstHandleServiceImpl;
	@Autowired
	private CommonParamService commonParamService;
	@Autowired
	private RuleEngineService ruleEngineService;
	@Autowired
	private CoreApiService coreApiService;
	@Autowired
	private SearchCompService searchCompService;
	@Autowired
	private PmsApiService pmsApiService;
	@Autowired
	private BdsApiService bdsApiService;
	@Autowired
	private BmsBasiceInfoService bmsBasiceInfoService;
	@Value("#{new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse(\"${master.loanA.date}\")}")
	private Date masterLoanADate;

	/***
	 * 返回初审工作台页面
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月14日
	 * @return
	 */
	@RequestMapping("/firstApprove")
	public String firstApprove(Model model) {
		model.addAttribute("staffCode", ShiroUtils.getAccount());
		return "/approve/first/firstApprove";
	}

	/**
	 * 返回初审待办任务队列数
	 *
	 * @author dmz
	 * @date 2017年3月21日
	 * @param requestPage
	 * @param taskType-1:优先队列信息;2:正常队列信息;3:挂起队列数
	 * @return
	 */
	@RequestMapping("/firstWorkbench")
	@ResponseBody
	public ResponsePage<ResBMSAuditVo> getFirstWorkbenchList(RequestPage requestPage, String taskType) {
		ResponsePage<ResBMSAuditVo> page = new ResponsePage<ResBMSAuditVo>();
		try {
			page = bmsLoanInfoServiceImpl.getWorkbenchList(requestPage, taskType, EnumConstants.ReqFlag.CS.getValue());
		} catch (Exception e) {
			LOGGER.error("初审工作台查询队列信息 异常!!", e);
		}
		return page;
	}

	/**
	 * 初审已完成队列信息查询
	 * 
	 * @author dmz
	 * @date 2017年4月13日
	 * @return
	 */
	@RequestMapping("/firstFinishWorkbench")
	@ResponseBody
	public ResponsePage<ResOffTheStocksAuditVO> adultOffTheStocks(HttpServletRequest req, RequestPage requestPage, String startDate, String endDate) {
		ResponsePage<ResOffTheStocksAuditVO> page = new ResponsePage<ResOffTheStocksAuditVO>();
		ReqBMSAdultOffTheStocksVo request = new ReqBMSAdultOffTheStocksVo();
		request.setOffStartDate(StringUtils.isEmpty(startDate) ? DateUtils.dateToString(DateUtils.getStartTime(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS) : startDate + " 00:00:00");
		request.setOffEndDate(StringUtils.isEmpty(endDate) ? DateUtils.dateToString(DateUtils.getEndTime(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS) : endDate + " 23:59:59");
		request.setPageNum(requestPage.getPage());
		request.setPageSize(requestPage.getRows());
		request.setOperatorIP(WebUtils.retrieveClientIp(req));
		request.setReqFlag(EnumConstants.ReqFlag.CS.getValue());
		try {
			page = bmsLoanInfoServiceImpl.adultOffTheStocks(request,requestPage);
		} catch (Exception e) {
			LOGGER.error("初审已完成队列信息查询异常", e);
		}
		return page;
	}

	/**
	 * 返回初审办理页面
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月16日
	 * @return
	 */
	@RequestMapping("/firstApproveReceive/{loanNo}/{version}")
	@UserLogs(link = "信审初审", operation = "初审办理", type = UserLog.Type.初审)
	public String firstApproveReceive(@PathVariable String loanNo, @PathVariable Long version, Model model, HttpServletRequest request) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
		// 判断版本号是否相同如果不同有可能借款被改派
		if (!version.equals(applyBasiceInfo.getVersion())) {
			model.addAttribute("updateVersion", true);
		}else {
			Result<RuleEngineVO> engineObj = ruleEngineService.handleNodeRuleEngine(applyBasiceInfo, EnumUtils.ExecuteTypeEnum.XSCS01, WebUtils.retrieveClientIp(request));
			if (null != engineObj.getData()) {
				applyBasiceInfo.setIsAntifraud(engineObj.getData().getIsAntifraud());//是否欺诈可疑重新取最新规则引擎返回值
				if (Strings.isNotEmpty(engineObj.getData().getComCreditRating())) {
					applyBasiceInfo.setComCreditRating(engineObj.getData().getComCreditRating());
				}
			}
			model.addAttribute("applyBasiceInfo", applyBasiceInfo);
			model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
			model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
			model.addAttribute("sysCode", sysCode);// 系统代码
			model.addAttribute("picImageUrl", picImageUrl);
			model.addAttribute("picApproval", picApproval);
			model.addAttribute("sysCreditZX", sysCreditZX);// 征信系统
			model.addAttribute("limitFirstSubmitDisable", engineObj.getFirstMessage());// 获取规则引擎返回类型
			model.addAttribute("ruleEngineResult", engineObj.getMessages());// 获取规则引擎返回提示信息(注意:第一个是规则引擎类型,第二个是执行时间,后面是是提示信息)
		}
		return "/approve/first/firstApproveReceive";
	}

	/**
	 * 返回审批意见页面
	 * 
	 * @author dongmingzhi
	 * @date 2017年1月4日
	 * @return
	 */
	@RequestMapping("/approvalOpinion/{loanNo}")
	public String approvalOpinion(@PathVariable String loanNo, Model model, HttpSession session) {
		ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		ApprovalOperationVO approvalOperationVO = approvalHistoryService.getApprovalHistoryByLoanNo(loanNo);
		// 判断是否需要解读央行报告和征信报告
		if (StringUtils.isNotEmpty(applyBasicInfo.getReportId()) && EnumUtils.YOrNEnum.Y.getValue().equals(applyBasicInfo.getIfReportId())) {
			Result<JSONObject> reportInfo = new Result<JSONObject>();					// 央行报告
			Result<JSONObject> loanLimitInfo = new Result<JSONObject>();					// 自动填充
			// 调用央行征信
			reportInfo = creditzxService.getCreditReportInfoService(applyBasicInfo);		// 信用卡报告
			loanLimitInfo = creditzxService.getLoanInfoService(applyBasicInfo);			// 贷款记录

			model.addAttribute("reportInfo", reportInfo.getData());			// 央行报告
			model.addAttribute("loanLimitInfo", loanLimitInfo.getData());	// 信用贷款、房贷、车贷、其他

			model.addAttribute("reportInfoJson", JSONObject.toJSONString(reportInfo.getData()));			// 央行报告
			model.addAttribute("loanLimitInfoJson", JSONObject.toJSONString(loanLimitInfo.getData()));	// 信用贷款、房贷、车贷、其他
		}

		model.addAttribute("markReportIdChange", applyBasicInfo.getIfReportId()); // 标记央行报告是否修改
		if (CollectionUtils.isEmpty(approvalOperationVO.getApprovalHistoryList())) {
			// 标记央行报告是否修改(初审第一次审批，央行报告默认未发生变化)
			LOGGER.info("判断申请件{}，首次审批央行报告未发生变化", loanNo);
			model.addAttribute("markReportIdChange", "N");

			if(EnumUtils.ApplyTypeEnum.TOPUP.getValue().equals(applyBasicInfo.getApplyType())){
				String topUpLoan=coreApiService.getLoanStatus(applyBasicInfo.getIdNo());
				LoanCoreVo loanCoreVo =searchCompService.findLoan(topUpLoan, applyBasicInfo.getIdNo());
				model.addAttribute("topupLoan", loanCoreVo);
				model.addAttribute("topupLoanJson", JSONObject.toJSONString(loanCoreVo));
			}
		}

		model.addAttribute("approvalInfo", approvalOperationVO);	 // 返回审批意见
		model.addAttribute("approvalInfoJson", JSONObject.toJSONString(approvalOperationVO, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));	// 返回审批意见JSON
		model.addAttribute("ifCreditRecord", applyBasicInfo.getIfCreditRecord() == null ? 0 : applyBasicInfo.getIfCreditRecord());// 是否有信用记录
		model.addAttribute("applyBasiceInfo", applyBasicInfo);
		model.addAttribute("applyBasicInfoJson", JSONObject.toJSONString(applyBasicInfo, SerializerFeature.DisableCircularReferenceDetect));
		model.addAttribute("resEmployeeVO", ShiroUtils.getCurrentUser());


		Result<AuditRulesVO> auditRulesVO = bmsLoanInfoServiceImpl.queryAuditRulesData(loanNo);// 系统初判查询
		model.addAttribute("auditRulesVO", auditRulesVO.getData());

		// 判断是否调用规则引擎
		Map<String, Object> mapSys = new HashMap<String, Object>();
		mapSys.put("paramKey", "systemRuleEngine");// 从配置参数中获取内匹配置申请历史起始时间
		SysParamDefine sysParamDefine = commonParamService.findOne(mapSys);
		if (null != sysParamDefine && "true".equals(sysParamDefine.getParamValue())) {
			model.addAttribute("markIsExecuteEngine", "true");
		} else {
			model.addAttribute("markIsExecuteEngine", "false");
		}
		model.addAttribute("loanNo", loanNo);
		// 判断网购达人A贷首次提交初审时间如果大于等于2018-02-06 则不显示12个月外收货地址
		model.addAttribute("isMasterLoanADate",DateUtils.daysOfTwo(applyBasicInfo.getFirstSubmitAudit(),masterLoanADate) < 1);
		if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
			ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,false);
			model.addAttribute("applyInfo", resApplicationInfoVO);
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(resApplicationInfoVO));
			model.addAttribute("nowYear",Calendar.getInstance().get(Calendar.YEAR));
			return "/approve/zdmoney/moneyApprovalOpinion";
		} else {
			// 查询借款系统产品列表
			List<ResBMSProductVO> productList = bmsBasiceInfoService.getProductListByOrgId(applyBasicInfo.getOwningBranchId(), applyBasicInfo.getIfPreferentialUser());
			model.addAttribute("productListJson", JSONArray.toJSONString(productList));
			ApplyEntryVO applyInfo = bmsLoanInfoServiceImpl.getBMSLoanInfoByLoanNoService(loanNo);
			model.addAttribute("applyInfo", applyInfo);		// TODO 后续合并applyInfoOnlyRead
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(applyInfo));

			ApplyEntryVO applyInfoOnlyRead = bmsLoanInfoServiceImpl.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
			if(applyInfoOnlyRead != null){
				model.addAttribute("applyInfoOnlyRead", applyInfoOnlyRead);
				model.addAttribute("applyInfoOnlyReadJson", JSONObject.toJSONString(applyInfoOnlyRead));
			}
			return "/approve/first/firstApprovalOpinion";
		}
	}

	/**
	 * 修改审批意见
	 * 
	 * @author dmz
	 * @date 2017年3月31日
	 * @param loanNo
	 * @return
	 */
	@RequestMapping("/updateApprovalOpinion")
	@ResponseBody
	public Result<String> updateApprovalOpinion(String loanNo, String form, HttpServletRequest request) {
		LOGGER.info("更新审批意见,loanNo:{}, form:{}", loanNo, form);
		Result<String> result = new Result<String>(Type.FAILURE);
		try {
			ApprovalSaveVO approvalSaveVO = JSONObject.parseObject(form, ApprovalSaveVO.class);
			ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, false);
			approvalSaveVO.setApprovalPerson(ShiroUtils.getCurrentUser().getUsercode());
			approvalSaveVO.setLoanNo(loanNo);
			approvalSaveVO.setApprovalApplyLimit(new BigDecimal(applyBasicInfo.getApplyLmt()));
			approvalSaveVO.setApprovalApplyTerm(applyBasicInfo.getApplyTerm());
			approvalSaveVO.setProductCd(applyBasicInfo.getProductCd());
			approvalSaveVO.setIp(WebUtils.retrieveClientIp(request));
			approvalSaveVO.setMarkUpdateReportId(applyBasicInfo.getIfReportId());

			approvalHistoryService.saveOrUpdateApprovalHistory(approvalSaveVO);

			result.setType(Type.SUCCESS);
			result.addMessage("操作成功!");

			// 重新保存借款基本信息
			bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);

		} catch (Exception e) {
			LOGGER.error("修改审批意见失败:", e);
			result.addMessage("操作失败");
		}

		return result;
	}

	/**
	 * 前前修改审批意见
	 *
	 * @author dmz
	 * @date 2017年3月31日
	 * @param loanNo
	 * @return
	 */
	@RequestMapping("/updateMoneyApprovalOpinion")
	@ResponseBody
	public Result<String> updateMoneyApprovalOpinion(String loanNo, String form, HttpServletRequest request) {
		LOGGER.info("更新审批意见,loanNo:{}, form:{}", loanNo, form);
		Result<String> result = new Result<String>(Type.FAILURE);
		try {
			JSONObject jsonObject = JSON.parseObject(form);
			ApprovalSaveVO approvalSaveVO = JSONObject.parseObject(jsonObject.getString("approveInfo"), ApprovalSaveVO.class);// 获取审批信息
			ReqAssetsInfoVO saveAssetsVO = JSONObject.parseObject(jsonObject.getString("assetsInfo"),ReqAssetsInfoVO.class);
			ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, false);
			approvalSaveVO.setApprovalPerson(ShiroUtils.getCurrentUser().getUsercode());
			approvalSaveVO.setLoanNo(loanNo);
			approvalSaveVO.setApprovalApplyLimit(new BigDecimal(applyBasicInfo.getApplyLmt()));
			approvalSaveVO.setApprovalApplyTerm(applyBasicInfo.getApplyTerm());
			approvalSaveVO.setProductCd(applyBasicInfo.getProductCd());
			approvalSaveVO.setIp(WebUtils.retrieveClientIp(request));
			approvalSaveVO.setMarkUpdateReportId(applyBasicInfo.getIfReportId());

			approvalHistoryService.saveOrUpdateMoneyApprovalHistory(approvalSaveVO,saveAssetsVO);

			result.setType(Type.SUCCESS);
			result.addMessage("操作成功!");
			// 重新保存借款基本信息
			bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
		} catch (Exception e) {
			LOGGER.error("修改审批意见失败:", e);
			result.addMessage("操作失败");
		}

		return result;
	}

	/**
	 * 返回客户信息页面
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月22日
	 * @return
	 */
	@RequestMapping("/customerInfo/{loanNo}")
	public String customerInfo(@PathVariable String loanNo, Model model, HttpSession session){
		ReqInformationVO bmsBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		if (null != bmsBasicInfo.getZdqqApply() && 1 == bmsBasicInfo.getZdqqApply()){// 前前进件
			ResDetailDifferenceVO resDetailDifferenceVO = bmsLoanInfoServiceImpl.getDetailDifference(loanNo,"1");// 前前标红信息查询
			ResApplicationInfoVO currentInfoVO = resDetailDifferenceVO.getCurrentInfo();
			if (!currentInfoVO.getBaseInfo().getVersion().equals(bmsBasicInfo.getVersion())) {
				model.addAttribute("updateVersion", true);
			}
			// 对比当前客户信息跟上一次修改哪些字段做了修改
			JSONObject compareResult = ClassCompareUtil.moneyEntitycomparison(resDetailDifferenceVO.getSubmitInfo(), resDetailDifferenceVO.getBackInfo());
			model.addAttribute("oldApplyInfo", compareResult);
			model.addAttribute("applyInfo", currentInfoVO);
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(currentInfoVO));
			model.addAttribute("loanNo", loanNo);
			boolean flag = "directApplyInput".equals(currentInfoVO.getBaseInfo().getApplyInputFlag()) && ("RELOAN".equals(currentInfoVO.getBaseInfo().getApplyType()) || "TOPUP".equals(currentInfoVO.getBaseInfo().getApplyType()));
			model.addAttribute("isDirectApp", flag);
			return "approve/zdmoney/moneyCustomerInfo";
		} else {// 正常录单进件
			AuditApplyEntryVO auditVO = bmsLoanInfoServiceImpl.queryAuditDifferences(loanNo, "1");// 标红信息查询
			AuditEntryVO auditEntryVO = auditVO.getAuditApplyEntryVO();// 当前版本信息
			if (!auditEntryVO.getVersion().equals(bmsBasicInfo.getVersion())) {
				model.addAttribute("updateVersion", true);
			}
			// 获取产品相关的资产信息
			String productCode = auditEntryVO.getApplyInfoVO().getProductCd();
			List<ResBMSEnumCodeVO> assetsList = bmsBasiceInfoService.getAssetsByProdCode(productCode);
			if(!CollectionUtils.isEmpty(assetsList)){
				model.addAttribute("assetsListJson", JSONArray.toJSONString(assetsList));
			}

			// 对比当前客户信息跟上一次修改哪些字段做了修改
			JSONObject compareResult = ClassCompareUtil.entitycomparison(auditVO.getSubmitAuditApplyEntryVO(), auditVO.getBackAuditApplyEntryVO());
			model.addAttribute("oldapplyInfo", compareResult);
			model.addAttribute("applyInfo", auditEntryVO);
			model.addAttribute("applyInfoJson", JSONObject.toJSONString(auditEntryVO));

			model.addAttribute("loanNo", loanNo);
			// 标记是否是直通车进件 且申请件类型是TOPUP 或 RELOAN 申请件
			boolean flag = "directApplyInput".equals(auditEntryVO.getApplyInfoVO().getApplyInputFlag()) && ("RELOAN".equals(auditEntryVO.getApplyInfoVO().getApplyType()) || "TOPUP".equals(auditEntryVO.getApplyInfoVO().getApplyType()));

			// 标记是否是直通车进件 且申请类型是TOPUP 或 RELOAN 申请且RELOAN 的上一笔结清时间小于或等于三个月内精确到月
			// 如果是取消资产信息必填项校验
			boolean flagValidate = flag;
			if (flagValidate && "RELOAN".equals(auditEntryVO.getApplyInfoVO().getApplyType())){
				flagValidate = coreApiService.isReLoanAndThreeMonth(bmsBasicInfo.getIdNo());
			}
			model.addAttribute("isDirectApp", flag);
			model.addAttribute("flagValidate",flagValidate);
			return "approve/first/firstCustomerInfo";
		}
	}

	/**
	 * 初审修改客户信息
	 * 
	 * @author dmz
	 * @date 2017年3月23日
	 * @return
	 */
	@RequestMapping("/updateCustomerInfo/{loanNo}")
	@ResponseBody
	public Result<String> updateCustomerInfo(@PathVariable String loanNo, @RequestBody AuditAmendEntryVO auditAmendEntryVO, HttpSession session) {
		Result<String> result = new Result<String>();
		try {
			auditAmendEntryVO.setLoanNo(loanNo);
			result = bmsLoanInfoServiceImpl.updateLoanInfo(auditAmendEntryVO);
			// 重新保存借款基本信息
			if (result.success()) {
				bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, result.success());
			}
		} catch (Exception e) {
			LOGGER.error("修改客户信息异常", e);
			result.setType(Type.FAILURE);
			result.addMessage("系统忙请稍后");
		}
		return result;
	}

	/**
	 * 前前修改客户信息
	 *
	 * @author dmz
	 * @date 2017年3月23日
	 * @return
	 */
	@RequestMapping("/updateMoneyCustomerInfo/{loanNo}")
	@ResponseBody
	public Result<String> updateMoneyCustomerInfo(@PathVariable String loanNo, @RequestBody ReqApplicationInfoVO reqApplicationInfoVO, HttpSession session) {
		Result<String> result = new Result<String>();
		try {
			result = bmsLoanInfoServiceImpl.updateMoneyLoanInfo(reqApplicationInfoVO);
			// 重新保存借款基本信息
			if (result.success()) {
				bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, result.success());
			}
		} catch (Exception e) {
			LOGGER.error("修改客户信息异常", e);
			result.setType(Type.FAILURE);
			result.addMessage("系统忙请稍后");
		}
		return result;
	}


	/**
	 * 返回内部匹配页面
	 *
	 * @author dongmingzhi
	 * @date 2017年1月4日
	 * @return
	 */
	@RequestMapping("/insideMatch/{loanNo}")
	public String insideMatch(@PathVariable String loanNo, Model model, HttpSession session) {
		LOGGER.info("返回内部匹配页面,单号：{}", loanNo);
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("jobNumber", ShiroUtils.getAccount());// 工号
		model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
		model.addAttribute("sysCode", sysCode);// 系统代码
		model.addAttribute("picImageUrl", picImageUrl);
		model.addAttribute("picApproval", picApproval);
		ApplicationInfoResVO resvo = bdsApiService.getApplicationInformation(loanNo);
		model.addAttribute("applicationInfo", resvo);
		return "/approve/first/firstInsideMatch";
	}

	/**
	 * 返回电核汇总页面
	 * @author dongmingzhi
	 * @param loanNo
	 * @param model
	 * @param session
	 * @date 2017年1月4日
	 * @return
	 * @ps 20171122代码优化改造
	 */
	@RequestMapping("/telephoneSummary/{loanNo}")
	public String telephoneSummary(@PathVariable String loanNo, Model model, HttpSession session) {
		LOGGER.info("初审电核汇总查询,单号:{}", loanNo);
		// 获取借款基本信息
		ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		// 获取第三方联系人信息
		List<MobileHistory> mobileHistory = mobileHistoryService.queryThridPartyByLoanId(loanNo);
		//设置第三方电话归属地
		for (MobileHistory mobile : mobileHistory) {
			if(StringUtils.isNotEmpty(mobile.getTelPhone())){
				mobile.setPhoneCity(PhoneUtil.getCity(mobile.getTelPhone()));
				mobile.setCarrier(mobile.getTelPhone());
			}
		}
		model.addAttribute("applyBasiceInfo", applyBasicInfo);											// 借款基本信息
		model.addAttribute("mobileHistoryInfoList", mobileHistory);										// 获取第三方联系人信息
		if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) { // 证大钱钱
			ResApplicationInfoVO applyInfo = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,true);
			// 调用API获取联系人信息
			ResOrganizationInfo org = pmsApiService.findOrgInfoById(applyInfo.getBaseInfo().getEnterBranchId() == null ? applyInfo.getBaseInfo().getOwningBranchId() : applyInfo.getBaseInfo().getEnterBranchId());
			org.setProvince(PhoneUtil.handleSpecialPro(org.getProvince()));

			List<JSONArray> contactList = bmsLoanInfoServiceImpl.moneyCombineContactInfo(loanNo,applyBasicInfo.getName(),org,1);

			model.addAttribute("moneyApplyInfo", applyInfo);														// 借款信息只读信息
			model.addAttribute("homePhone", applyInfo.getApplicantInfo().getPersonalInfo().getHomePhone1());	//添加宅电
			model.addAttribute("org", org);
			model.addAttribute("contactList", contactList);
		} else {
			// 查询借款信息只读信息
			ApplyEntryVO applyInfo = bmsLoanInfoServiceImpl.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
			// 调用API获取联系人信息
			ResOrganizationInfo org = pmsApiService.findOrgInfoById(applyInfo.getApplyInfoVO().getEnterBranchId() == null ? applyInfo.getApplyInfoVO().getOwningBranchId() : applyInfo.getApplyInfoVO().getEnterBranchId());
			org.setProvince(PhoneUtil.handleSpecialPro(org.getProvince()));
			List<JSONArray> contactList = bmsLoanInfoServiceImpl.combineContactInfo(loanNo,applyBasicInfo.getName(),org,1);
			model.addAttribute("applyInfo", applyInfo);														// 借款信息只读信息
			model.addAttribute("homePhone", applyInfo.getBasicInfoVO().getPersonInfoVO().getHomePhone1());	//添加宅电
			model.addAttribute("org", org);
			model.addAttribute("contactList", contactList);											// 获取联系人信息
		}
		return "/approve/first/firstTelephoneSummary";
	}

	/**
	 * 加载电核记录数据
	 * 
	 * @author luting
	 * @date 2017年2月23日 上午10:30:41
	 */
	@RequestMapping("/telephoneHistory")
	@ResponseBody
	public ResponsePage<MobileHistoryVO> getTelephoneHistory(RequestPage requestPage, String loanNo) {
		/** 借款账号为空不查询电核记录 */
		if (StringUtil.isEmpty(loanNo)) {
			return null;
		}
		/** 查询电核记录 */
		ResponsePage<MobileHistoryVO> mobileHistoryLst = mobileHistoryService.queryMobileHisByLoanId(requestPage, loanNo, ShiroUtils.getAccount());
		return mobileHistoryLst;
	}

	/**
	 * 返回日志备注页面
	 * 
	 * @author dongmingzhi
	 * @date 2017年1月4日
	 * @return
	 */
	@RequestMapping("/logNotesInfo/{loanNo}")
	public String logNotesInfo(@PathVariable String loanNo, HttpServletRequest request, Model model) {
		List<ResBMSQueryLoanLogVO> result = new ArrayList<ResBMSQueryLoanLogVO>();
		try {
			result = bmsLoanInfoServiceImpl.queryLoanLog(loanNo, WebUtils.retrieveClientIp(request));
			ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, false);
			model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		} catch (Exception e) {
			LOGGER.info("返回日志备注页面异常", e);
		}


		model.addAttribute("loanLogList", result);
		return "/approve/first/firstLogNotesInfo";
	}

	/***
	 * 返回外部征信页面
	 * @date 2016年12月21日
	 * @author dongmingzhi
	 * @return
	 */
	@RequestMapping("/externalCredit/{loanNo}")
	public String externalCredit(@PathVariable String loanNo, HttpSession session, Model model) {
		ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		List<MobileOnline> mobileOnlineList = firstHandleServiceImpl.getMobileOnlineByLoanNo(loanNo, session);
		model.addAttribute("mobileOnlineList", JSONArray.toJSONString(mobileOnlineList, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
		model.addAttribute("applyBasiceInfo", applyBasicInfo);

		return "/approve/first/firstExternalCredit";
	}

	/**
	 * 返回初审改派页面
	 * @author luting
	 * @date 2017年1月18日 下午3:07:31
	 */
	@RequestMapping("/firstReform")
	public String firstReform() {
		return "/approve/first/firstReform";
	}

	/**
	 * add by zw at 2017-04-24 返回初审初审已完成查看页面
	 * 
	 * @author zw
	 * @date 2017年04月24日
	 * @return
	 */
	@RequestMapping("/firstApproveLook/{loanNo}")
	public String firstApproveLook(@PathVariable String loanNo, Model model, HttpSession session) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
		model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
		model.addAttribute("sysCode", sysCode);// 系统代码
		model.addAttribute("picImageUrl", picImageUrl);
		model.addAttribute("picApproval", picApproval);
		model.addAttribute("sysCreditZX", sysCreditZX);// 政审系统
		return "/approve/first/firstApproveLook";
	}

	/**
	 * add by zw at 2017-05-04 初审改派导出excel
	 * 
	 * @param queryParams
	 * @param req
	 * @param res
	 * @date 2017年05月04日 下午3:48:28
	 */
	@RequestMapping("/exportExcel/{queryParams}")
	public void exportExcel(@PathVariable String queryParams, HttpServletRequest req, HttpServletResponse res) {
		try {
			ReqBMSReassignmentVo request = JSON.parseObject(queryParams, ReqBMSReassignmentVo.class);
			firstHandleServiceImpl.exportExcel(request, req, res);
		} catch (Exception e) {
			LOGGER.error("初审改派工作台导出队列信息 异常:", e);
		}
	}

	/**
	 * 返回央行报告页面
	 * 
	 * @author zhouwen
	 * @date 2017年7月11日
	 * @param loanNo
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/centralBankCredit/{loanNo}")
	public String centralBankCredit(@PathVariable String loanNo, Model model, HttpSession session) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
		model.addAttribute("auditEndTime", DateUtils.dateToString(applyBasiceInfo.getAuditEndTime(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
		model.addAttribute("sysCode", sysCode);// 系统代码
		model.addAttribute("sysCreditZX", sysCreditZX);// 征信系统
		return "/approve/first/centralBankCredit";
	}
}
