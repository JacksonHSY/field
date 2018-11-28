package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.*;
import com.ymkj.ams.api.vo.response.audit.ResBMSAuditVo;
import com.ymkj.ams.api.vo.response.audit.ResOffTheStocksAuditVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResDetailDifferenceVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationInfo;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.PhoneUtil;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.domain.system.UserLog.Type;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.*;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalOperationVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.finalApprove.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 终审相关操作控制器
 */
@Controller
@RequestMapping("/finishApprove")
public class FinishApproveController extends BaseController {

    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    
    @Autowired
    private FinalHandleService finalHandleService;
    
    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @Autowired
    private ApproveCheckInfoService approveCheckInfoService;
    
    @Autowired
    private StaffOrderTaskService staffOrderTaskService;
    
    @Autowired
    private MobileHistoryService mobileHistoryService;
    
    @Autowired
    private BdsApiService bdsApiService;
    
    @Autowired
    private CommonParamService commonParamService;
    
    @Autowired
    private RuleEngineService ruleEngineService;

    @Autowired
    private BmsLoanInfoService bmsLoanInfoServiceImpl;

    @Autowired
	private PmsApiService pmsApiService;

    @Autowired
    private BmsBasiceInfoService bmsBasiceInfoService;

    @Value("#{new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse(\"${master.loanA.date}\")}")
    private Date masterLoanADate;
    /**
     * 返回终审工作台页面
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishApprove")
    public String finishApprove() {
        return "/approve/finish/finishApprove";
    }

    /**
     * 获取终审工作台列表
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finalWorkbench")
    @ResponseBody
    public ResponsePage<ResBMSAuditVo> getFinalWorkbenchList(RequestPage requestPage, String taskType) {
        ResponsePage<ResBMSAuditVo> page = new ResponsePage<ResBMSAuditVo>();
        try {
            page = bmsLoanInfoService.getZsWorkbenchList(requestPage, taskType, EnumConstants.ReqFlag.ZS.getValue());
        } catch (Exception e) {
            LOGGER.error("终审工作台查询队列信息 异常:", e);
        }
        return page;
    }

    /**
     * 获取终审已完成任务列表
     *
     * @param requestPage
     * @return
     * @author JiaCX
     * @date 2017年4月25日 上午10:19:50
     */
    @RequestMapping("/getCompletedTask")
    @ResponseBody
    public ResponsePage<ResOffTheStocksAuditVO> getCompletedTask(RequestPage requestPage, FinalCompletedParamIn param) {
        ResponsePage<ResOffTheStocksAuditVO> page = new ResponsePage<ResOffTheStocksAuditVO>();
        try {
            page = finalHandleService.getCompletedTask(requestPage, param.getOffStartDate(), param.getOffEndDate(), request);
        } catch (Exception e) {
            LOGGER.error("获取终审已完成任务列表信息 异常:", e);
        }
        return page;
    }

    /**
     * 返回终审办理页面
     *
     * @return
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishApproveReceive/{loanNo}/{version}/{taskType}")
    @UserLogs(link = "信审终审", operation = "终审办理", type = UserLog.Type.终审)
    public String finishApproveReceive(@PathVariable String loanNo, @PathVariable int version, @PathVariable int taskType, Model model) {
        // TODO 校验version
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
        Result<RuleEngineVO> engineObj = ruleEngineService.handleNodeRuleEngine(applyBasiceInfo, EnumUtils.ExecuteTypeEnum.XSZS01, WebUtils.retrieveClientIp(request));
        if (null != engineObj.getData()) {
            applyBasiceInfo.setIsAntifraud(engineObj.getData().getIsAntifraud());//重新取最新规则引擎
            if (Strings.isNotEmpty(engineObj.getData().getComCreditRating())) {
                applyBasiceInfo.setComCreditRating(engineObj.getData().getComCreditRating());
            }
        }
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("taskType", taskType);
        model.addAttribute("sysCode", sysCode);
        model.addAttribute("picImageUrl", picImageUrl);
        model.addAttribute("picApproval", picApproval);
        model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
        model.addAttribute("sysCreditZX", sysCreditZX);
        model.addAttribute("limitFinishSubmitDisable", engineObj.getFirstMessage());// 标记是否规则引擎禁用提交
        model.addAttribute("ruleEngineResult", engineObj.getMessages());

        return "/approve/finish/finishApproveReceive";
    }

    /**
     * 返回终审电核汇总页面
     *
     * @param loanNo
     * @param model
     * @param session
     * @param type 请求此接口的类型1:终审办理电核，2：其他页面的电核;区别在于页面不展示号码查询那一列
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("/finishTelephoneSummary/{loanNo}/{type}")
    public String finishTelephoneSummary(@PathVariable String loanNo, Model model, HttpSession session, @PathVariable String type) {
    	LOGGER.info("终审/其他页面 电核汇总查询,单号:{}", loanNo);
        // 获取借款基本信息
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
 		// 获取第三方联系人信息
		List<MobileHistory> mobileHistory = mobileHistoryService.queryThridPartyByLoanId(loanNo);
		//设置第三方电话归属地
		for (MobileHistory mobile : mobileHistory) {
			if(StringUtils.isEmpty(mobile.getPhoneCity()) && StringUtils.isNotEmpty(mobile.getTelPhone())){
				mobile.setPhoneCity(PhoneUtil.getCity(mobile.getTelPhone()));
				mobile.setCarrier(PhoneUtil.getCarrier(mobile.getTelPhone()));
			}
		}
		model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("mobileHistoryInfoList", mobileHistory);
        if (null != applyBasiceInfo.getZdqqApply() && 1 == applyBasiceInfo.getZdqqApply()) { // 证大钱钱
            ResApplicationInfoVO applyInfo = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,true);
            // 根据机构id查询机构信息
            ResOrganizationInfo org = pmsApiService.findOrgInfoById(applyInfo.getBaseInfo().getEnterBranchId() == null ? applyInfo.getBaseInfo().getOwningBranchId() : applyInfo.getBaseInfo().getEnterBranchId());
            // 组装联系人信息列表(给电核汇总用)
            List<JSONArray> contactList = bmsLoanInfoServiceImpl.moneyCombineContactInfo(loanNo,applyBasiceInfo.getName(), org, 2);
            org.setProvince(PhoneUtil.handleSpecialPro(org.getProvince()));
            model.addAttribute("moneyApplyInfo", applyInfo);
            model.addAttribute("contactList", contactList);
            model.addAttribute("org", org);
        }else {
            // 查询借款信息只读信息
            ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
            // 根据机构id查询机构信息
            ResOrganizationInfo org = pmsApiService.findOrgInfoById(applyInfo.getApplyInfoVO().getEnterBranchId() == null ? applyInfo.getApplyInfoVO().getOwningBranchId() : applyInfo.getApplyInfoVO().getEnterBranchId());
            // 组装联系人信息列表(给电核汇总用)
            List<JSONArray> contactList = bmsLoanInfoServiceImpl.combineContactInfo(loanNo,applyBasiceInfo.getName(), org, 2);
            org.setProvince(PhoneUtil.handleSpecialPro(org.getProvince()));
            model.addAttribute("applyInfo", applyInfo);
            model.addAttribute("contactList", contactList);
            model.addAttribute("org", org);
        }



        //如果是质检操作
        if ("quality".equals(request.getParameter("businessType")) && null != session.getAttribute("approvalMap")) {
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("approvalMap");//获取session里面的审核信息
            String qualityPersonMapJson = JSONObject.toJSONString(map.get(loanNo));//获取本案件的审核信息
            model.addAttribute("qualityPersonMapJson", qualityPersonMapJson);
            model.addAttribute("businessType", "quality");
        }
        model.addAttribute("resType", type);

        return "/approve/finish/finishTelephoneSummary";
    }

    /**
     * 查询审批意见是否填写
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/findCurApprovalOpinion/{loanNo}")
    @ResponseBody
    public Result<ApprovalHistory> findCurApprovalOpinion(@PathVariable String loanNo) {
        Result<ApprovalHistory> result = new Result<ApprovalHistory>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
            // 校验是否填写了审批意见
            ApprovalHistory approvalHistory = finalHandleService.getCurrentApprovalOption(loanNo);
            if(approvalHistory == null){
                result.addMessage("请填写审批意见!");
                return result;
            }

            // 校验其他资料核对信息是否勾选了"征信平台是否已验证"字段
            ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(loanNo);
            if(approveCheckInfo == null || approveCheckInfo.getCreditCheckException() == null || approveCheckInfo.getCreditCheckException() == 1){
                result.addMessage("请勾选征信平台已验证!");
                return result;
            }
            // 校验产品期限额度是否可用
            try {
                if (bmsBasiceInfoService.checkApprovalProduct(loanNo)){
                    result.addMessage("请重新选择审批产品、审批额度、审批期限!");
                    return result;
                }
            } catch (BusinessException e) {
                result.addMessage(e.getMessage());
                return result;
            }

            StaffOrderTask staffOrderTask = finalHandleService.getStaffOrderTask(approvalHistory.getApprovalPerson(), null);
            if (null != staffOrderTask) {
                approvalHistory.setFinalAuditLevel(staffOrderTask.getFinalAuditLevel());// 放入终审审批级别
            }
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
            result.setData(approvalHistory);

        } catch (Exception e) {
            result.addMessage("系统繁忙，请稍后再试!");
            LOGGER.error("验证终审审批意见是否填写异常", e);
        }

        return result;
    }

    /**
     * 返回终审 审批意见页面（有操作按钮）
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishApprovalOpinion/{loanNo}")
    public String finishApprovalOpinion(@PathVariable String loanNo, Model model, HttpSession session) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
        ApprovalOperationVO approvalOperationVO = approvalHistoryService.getApprovalHistoryByLoanNo(loanNo);
        if (CollectionUtils.isNotEmpty(approvalOperationVO.getApprovalHistoryList())) {
            LOGGER.info("借款单号:{} 终审查询审批意见成功, 返回信息为:{}", loanNo, JSON.toJSONString(approvalOperationVO));
            if (approvalOperationVO.getApproveCheckInfo() != null) {
                ApproveCheckInfo approveCheckInfo = approvalOperationVO.getApproveCheckInfo();
                if (approveCheckInfo.getCourtCheckException() == null || approveCheckInfo.getCourtCheckException() == 0) {
                    model.addAttribute("courtCheck", "无异常");        // 法院网核查情况
                } else {
                    model.addAttribute("courtCheck", "异常");        // 法院网核查情况
                }
                if (approveCheckInfo.getInternalCheckException() == null || approveCheckInfo.getInternalCheckException() == 0) {
                    model.addAttribute("insideMatch", "无异常");        // 内部匹配情况
                } else {
                    model.addAttribute("insideMatch", "异常");        // 内部匹配情况
                }
            }

            model.addAttribute("approvalInfo", approvalOperationVO);    // 返回审批意见
            model.addAttribute("approvalInfoJson", JSONObject.toJSONString(approvalOperationVO, SerializerFeature.DisableCircularReferenceDetect));

            // 查询当前审批意见
            // Result<ApprovalOperationVO> currentApprovalHistory = approvalHistoryService.getCurrentApprovalHistory(loanNo);
            ApprovalHistory currentApprovalHistory = finalHandleService.getCurrentApprovalOption(loanNo);
            model.addAttribute("currentApprovalHistory", currentApprovalHistory);// 返回当前审批意见
        }


        model.addAttribute("applyBasiceInfo", applyBasicInfo);
        model.addAttribute("applyBasicInfoJson", JSONObject.toJSONString(applyBasicInfo, SerializerFeature.DisableCircularReferenceDetect));
        model.addAttribute("resEmployeeVO", ShiroUtils.getCurrentUser());

        Result<AuditRulesVO> auditRulesVO = bmsLoanInfoService.queryAuditRulesData(loanNo);// 系统初判查询
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

        // 判断央行报告是否有改变
        if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(applyBasicInfo.getZsIfNewLoanNo() + "")) { // 优先件才需要提示
            ApprovalHistory approvalHistory = finalHandleService.getCurrentApprovalOption(applyBasicInfo.getLoanNo());
            if (null == approvalHistory) {// 未填写审批意见表时需要提示
                String reportIdBackUp = bmsLoanInfoService.getLoanInfoHistoryColunm(applyBasicInfo.getLoanNo(), "2", "reportId");
                if (Strings.isEmpty(applyBasicInfo.getReportId()) && Strings.isNotEmpty(reportIdBackUp)) {// 当前没有央行报告id,原来有
                    model.addAttribute("markReportIdChange", "Y");
                } else if (Strings.isNotEmpty(applyBasicInfo.getReportId()) && !applyBasicInfo.getReportId().equals(reportIdBackUp)) {// 当前有央行报告id，匹配原来是否一致
                    model.addAttribute("markReportIdChange", "Y");
                }
            }
        }
        // 判断网购达人A贷首次提交初审时间如果大于等于2018-02-06 则不显示12个月外收货地址
        model.addAttribute("isMasterLoanADate",DateUtils.daysOfTwo(applyBasicInfo.getFirstSubmitAudit(),masterLoanADate) < 1);
        if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
            ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,true);
            model.addAttribute("applyInfo", resApplicationInfoVO);
            model.addAttribute("applyInfoJson", JSONObject.toJSONString(resApplicationInfoVO));
            return "/approve/zdmoney/finishMoneyApprovalOpinion";
        } else {
            // 查询借款信息只读信息
            ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
            model.addAttribute("applyInfo", applyInfo);
            model.addAttribute("applyInfoJson", JSONObject.toJSONString(applyInfo, SerializerFeature.DisableCircularReferenceDetect));
            return "/approve/finish/finishApprovalOpinion";
        }
    }

    /**
     * 返回终审审批意见页面（没有操作按钮）
     *
     * @param loanNo
     * @param model
     * @param session
     * @return
     * @author JiaCX
     * @date 2017年6月3日 上午9:49:27
     */
    @RequestMapping("/finishApprovalOpinionWithoutAction/{loanNo}")
    public String finishApprovalOpinionWithoutAction(@PathVariable String loanNo, Model model, HttpSession session) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        ApprovalOperationVO approvalOperationVO = approvalHistoryService.getApprovalHistoryByLoanNo(loanNo);

        LOGGER.info("借款单号:{} 终审查询审批意见成功,返回信息为:{}", loanNo, JSON.toJSONString(approvalOperationVO));
        ApproveCheckInfo approveCheckInfo = approvalOperationVO.getApproveCheckInfo();
        if (approveCheckInfo != null) {
            if (approveCheckInfo.getCourtCheckException() == null || approveCheckInfo.getCourtCheckException() == 0) {
                model.addAttribute("courtCheck", "无异常");        // 法院网核查情况
            } else {
                model.addAttribute("courtCheck", "异常");        // 法院网核查情况
            }

            if (approveCheckInfo.getInternalCheckException() == null || approveCheckInfo.getInternalCheckException() == 0) {
                model.addAttribute("insideMatch", "无异常");        // 内部匹配情况
            } else {
                model.addAttribute("insideMatch", "异常");        // 内部匹配情况
            }
        }
        // 返回审批意见
        model.addAttribute("approvalInfo", approvalOperationVO);
        model.addAttribute("approvalInfoJson", JSONObject.toJSONString(approvalOperationVO, SerializerFeature.DisableCircularReferenceDetect));



        // 规则引擎返回的值
        Result<AuditRulesVO> auditRulesVO = bmsLoanInfoService.queryAuditRulesData(loanNo);
        model.addAttribute("auditRulesVO", auditRulesVO.getData());


        model.addAttribute("applyBasiceInfo", applyBasicInfo);
        model.addAttribute("applyBasicInfoJson", JSONObject.toJSONString(applyBasicInfo, SerializerFeature.DisableCircularReferenceDetect));
        model.addAttribute("resEmployeeVO", ShiroUtils.getCurrentUser());

        if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
            // 查询借款信息
            ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,true,true);
            model.addAttribute("applyInfo", resApplicationInfoVO);
            model.addAttribute("applyInfoJson", JSONObject.toJSONString(resApplicationInfoVO, SerializerFeature.DisableCircularReferenceDetect));
            return "/approve/zdmoney/queryMoneyApprovalOpinion";
        }else {
            // 查询借款信息
            ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
            model.addAttribute("applyInfo", applyInfo);
            model.addAttribute("applyInfoJson", JSONObject.toJSONString(applyInfo, SerializerFeature.DisableCircularReferenceDetect));
            // 判断网购达人A贷首次提交初审时间如果大于等于2018-02-06 则不显示12个月外收货地址
            Date firstSubmitDate = applyBasicInfo.getFirstSubmitAudit();
            if (firstSubmitDate == null) {
                model.addAttribute("isMasterLoanADate", false);
            } else {
                model.addAttribute("isMasterLoanADate", DateUtils.daysOfTwo(firstSubmitDate, masterLoanADate) < 1);
            }
            return "/approve/finish/finishApprovalOpinionWithoutAction";
        }
    }

    /**
     * 保存审批意见
     *
     * @param loanNo
     * @return
     * @author Shipf
     * @date
     */
    @RequestMapping("/saveApprovalOpinion/{loanNo}")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "保存审批意见", type = Type.终审)
    public Result<String> saveApprovalOpinion(@PathVariable String loanNo, String form, HttpSession session) {
        LOGGER.info("借款单号：{} 终审保存审批意见,提交信息为: {}", loanNo, form);
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
        	ApprovalSaveVO approvalSaveVO = JSONObject.parseObject(form, ApprovalSaveVO.class);
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
            if (!applyBasiceInfo.getVersion().equals(approvalSaveVO.getVersion())) {
                result.addMessage("该笔借款有可能被修改,请重新办理!");
            } else {
                approvalSaveVO.setApprovalPerson(ShiroUtils.getCurrentUser().getUsercode());
                approvalSaveVO.setLoanNo(loanNo);
                approvalSaveVO.setApprovalApplyLimit(new BigDecimal(applyBasiceInfo.getApplyLmt()));
                approvalSaveVO.setApprovalApplyTerm(applyBasiceInfo.getApplyTerm());
                boolean flag = finalHandleService.saveOrUpdateApprovalHistory(approvalSaveVO, request);
                result.setType(flag ? com.yuminsoft.ams.system.util.Result.Type.SUCCESS : com.yuminsoft.ams.system.util.Result.Type.FAILURE);
                result.addMessage(flag ? "操作成功!" : "操作失败!");
            }
        } catch (Exception e) {
            LOGGER.error("借款单号：{} 终审保存审批意见失败:{}", loanNo, e);
            result.addMessage("操作失败");
        }
        return result;
    }

    /**
     * 返回 客户信息页面（不标红）
     *
     * @throws
     * @author Shipf
     * @date
     */
    @RequestMapping("/customerInfoWithoutRed/{loanNo}")
    public String customerInfoWithoutRed(@PathVariable String loanNo, Model model) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, false);
        if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
            ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoService.getMoneyLoanInfoDetail(loanNo, true,true);
            model.addAttribute("applyInfo",resApplicationInfoVO);
            return "/approve/zdmoney/moneyReadonlyCustomerInfo";
        }else {
            ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
            model.addAttribute("applyInfo", applyInfo);
            return "/approve/finish/customerInfoWithoutRed";
        }
    }

    /**
     * 返回客户信息页面（标红）
     *
     * @param loanNo
     * @param model
     * @return
     * @throws Exception
     * @author JiaCX
     * @date 2017年5月15日 上午11:32:28
     */
    @RequestMapping("/finishCustomerInfo/{loanNo}")
    public String finishCustomerInfo(@PathVariable String loanNo, Model model) throws Exception {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, false);
        if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
            ResDetailDifferenceVO resDetailDifferenceVO = bmsLoanInfoServiceImpl.getDetailDifference(loanNo,"2");// 前前标红信息查询
            model.addAttribute("oldApplyInfo",ClassCompareUtil.moneyEntitycomparison(resDetailDifferenceVO.getSubmitInfo(), resDetailDifferenceVO.getBackInfo()));
            model.addAttribute("applyInfo", resDetailDifferenceVO.getCurrentInfo());
            return "/approve/zdmoney/moneyReadonlyCustomerInfo";
        } else {
            AuditApplyEntryVO auditVO = bmsLoanInfoService.queryAuditDifferences(loanNo, "2");
            AuditEntryVO auditEntirtyVO = auditVO.getAuditApplyEntryVO();
            model.addAttribute("oldapplyInfo", ClassCompareUtil.entitycomparison(auditVO.getSubmitAuditApplyEntryVO(), auditVO.getBackAuditApplyEntryVO()));
            model.addAttribute("applyInfo", auditEntirtyVO);
            return "/approve/finish/finishCustomerInfo";
        }
    }

    /**
     * 返回外部征信页面
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishExternalCredit/{loanNo}")
    public String finishExternalCredit() {
        return "/approve/finish/finishExternalCredit";
    }

    /**
     * 返回内部匹配页面
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishInsideMatch/{loanNo}")
    public String finishInsideMatch(@PathVariable String loanNo, Model model, HttpSession session) {
        // 放入电话号码匹配信息
        // model.addAttribute("phoneNumberResVOList", bdsApiService.matchByPhoneNumber(loanNo));
        // 放入内匹返回的申请信息
        model.addAttribute("applicationInfo", bdsApiService.getApplicationInformation(loanNo));

        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("sysCode", sysCode);
        model.addAttribute("picImageUrl", picImageUrl);
        model.addAttribute("picApproval", picApproval);
        model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
        return "/approve/finish/finishInsideMatch";
    }

    /**
     * 获取当前登录人挂起队列状态
     *
     * @return
     * @author JiaCX
     * @date 2017年7月24日 上午11:10:41
     */
    @RequestMapping("/getHangQueenStatus")
    public @ResponseBody String getHangQueenStatus() {
        return staffOrderTaskService.checkInactiveTask();
    }

    /**
     * 挂起
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/suspend")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审挂起", type = Type.终审)
    public Result<String> suspend(ApplyHistoryVO applyHistoryVo) {
        LOGGER.info("借款单号:{} 信审终审挂起", applyHistoryVo.getLoanNo());
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
            applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
            applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSHANGUP.getValue());
            applyHistoryVo.setHandleCode(ShiroUtils.getCurrentUser().getUsercode());
            result = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL);
        } catch (Exception e) {
            LOGGER.error("借款单号:{}信审终审挂起失败", applyHistoryVo.getLoanNo(), e);
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            result.addMessage("挂起失败");
        }
        return result;
    }

    /**
     * 提交
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/sumbit")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审提交", type = Type.终审)
    public Result<String> sumbit(ApplyHistoryVO applyHistoryVo,HttpSession session) {
        LOGGER.info("借款单号:{} 信审终审提交", applyHistoryVo.getLoanNo());
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), applyHistoryVo.getLoanNo(), false);
            if (null == applyBasiceInfo.getZdqqApply() || 0 == applyBasiceInfo.getZdqqApply()) {// 非前前校验资产信息
                // 校验资产信息是否通过
                result = bmsLoanInfoService.validateApplyInfo(applyHistoryVo.getLoanNo());
            } else {
                result.setType(Result.Type.SUCCESS);
            }
            if (result.success()) {
                applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSPASS.getValue());
                applyHistoryVo.setHandleCode(ShiroUtils.getCurrentUser().getUsercode());
                result = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL);
            }
        } catch (Exception e) {
        	LOGGER.error("借款单号:{}信审终审提交失败", applyHistoryVo.getLoanNo(), e);
        	result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            result.addMessage("提交失败");
        }
        return result;
    }

    /**
     * 提交高审
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/sumbitHL")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审提交高审", type = Type.终审)
    public Result<String> sumbitHL(ApplyHistoryVO applyHistoryVo, HttpSession session) {
        LOGGER.info("借款单号:{} 信审终审提交高审", applyHistoryVo.getLoanNo());
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), applyHistoryVo.getLoanNo(), false);
            if (null == applyBasiceInfo.getZdqqApply() || 0 == applyBasiceInfo.getZdqqApply()) {// 非前前校验资产信息
                // 校验资产信息是否通过
                result = bmsLoanInfoService.validateApplyInfo(applyHistoryVo.getLoanNo());
            }else {
                result.setType(Result.Type.SUCCESS);
            }
            if (result.success()) {
                applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue());
                applyHistoryVo.setHandleCode(ShiroUtils.getCurrentUser().getUsercode());
                result = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL);
            }
        } catch (Exception e) {
            LOGGER.error("借款单号:{}信审终审提交高审失败", applyHistoryVo.getLoanNo(), e);
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            result.addMessage("提交高审失败");
        }
        return result;
    }

    /**
     * 退回
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/rollback")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审退回", type = Type.终审)
    public Result<String> rollback(String form) {
        LOGGER.info("借款单号:{} 信审终审退回", form);
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
        	ApplyHistoryVO applyHistoryVo = JSONObject.parseObject(form , ApplyHistoryVO.class);
        	ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, applyHistoryVo.getLoanNo(), true);
            applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
            if ("ZSRTNLR".equals(applyHistoryVo.getBackType())) {
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSRETURN.getValue());
            }else if ("ZSRTNQQ".equals(applyHistoryVo.getBackType())){
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue());
            } else {
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSRTNCS.getValue());
            }
            applyHistoryVo.setPatchBolt(applyHistoryVo.getBackType());
            applyHistoryVo.setHandleCode(ShiroUtils.getCurrentUser().getUsercode());
            result = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL);
        } catch (Exception e) {
            LOGGER.error("借款单号:{}信审终审退回失败", form, e);
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            result.addMessage("退回失败");
        }
        return result;
    }

    /**
     * 拒绝
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/deny")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审拒绝", type = Type.终审)
    public Result<String> deny(ApplyHistoryVO applyHistoryVo) {
        LOGGER.info("借款单号:{} 信审终审拒绝", applyHistoryVo.getLoanNo());
        Result<String> result = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        try {
            if (!Strings.isEmpty(applyHistoryVo.getConType()) && applyHistoryVo.getConType().contains("debtRatio_Y")) {
                result = finalHandleService.repaymentInsufficientJudge(request, applyHistoryVo.getLoanNo());// 若原因是偿还能力不足时进行负债率校验
            }
            if (result.getType().equals(com.yuminsoft.ams.system.util.Result.Type.FAILURE)) {// 没有审批意见则直接拒绝或者校验通过可以拒绝
                applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
                applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSREJECT.getValue());
                applyHistoryVo.setAutoRefuse(EnumUtils.YOrNEnum.N.getValue());
                applyHistoryVo.setHandleCode(ShiroUtils.getCurrentUser().getUsercode());
                result = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL);
            } else {
                result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            }
        } catch (Exception e) {
            LOGGER.error("借款单号:{}信审终审拒绝失败", applyHistoryVo.getLoanNo(), e);
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
            result.addMessage("拒绝失败");
        }
        return result;
    }

    /**
     * 返回终审改派页面
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishReform")
    public String finishReform() {
        return "/approve/finish/finishReform";
    }

    /**
     * 终审改派工作台
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishReformWorkbench")
    @ResponseBody
    public ResponsePage<ZsReassignmentVO> getFinishReformList(FinishReformListParamIn request, RequestPage requestPage, boolean isSearch) {
        ResponsePage<ZsReassignmentVO> page = new ResponsePage<ZsReassignmentVO>();
        try {
            page = finalHandleService.getFinishReformList(request, requestPage);
        } catch (Exception e) {
            LOGGER.error("终审改派工作台查询队列信息 异常:", e);
        }
        return page;
    }

    /**
     * 终审改派
     *
     * @author Shipf
     * @date
     */
    @RequestMapping("/finishReform/reassignment")
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "终审改派", type = Type.终审)
    public Result<String> finishReform(@RequestBody List<ReformVO> reformVOList) {
        Result<String> result = new Result<String>();
        int success = 0;
        for (ReformVO reformVO : reformVOList) {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, reformVO.getLoanNo(), true);
            if (!reformVO.getVersion().equals(String.valueOf(applyBasiceInfo.getVersion()))) {
                // 如果要处理的版本号和数据库最新版本号不一致，就返回不可改派
                result.addMessage("申请件：" + reformVO.getLoanNo() + "在处理中，不可改派。");
            } else {
                // 如果版本号一致，就可以做改派处理
                boolean flag = false;
                try {
                    flag = finalHandleService.finishReform(reformVO, request);
                } catch (Exception e) {
                    flag = false;
                }
                if (flag) {
                    success = success + 1;
                } else {
                    // 如果借款更新失败，不更新ams，并记录错误信息
                    result.addMessage("申请件：" + reformVO.getLoanNo() + "改派失败。");
                }
            }
        }

        if (success == 0) {
            // 全部都设置失败的时候才认为失败
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        } else if (success == reformVOList.size()) {
            // 全部成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
            result.addMessage("改派成功");
        } else {
            // 部分成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
        }

        return result;
    }

    /**
     * 终审改派导出excel
     *
     * @param param
     * @param res
     * @author JiaCX
     * @date 2017年4月18日 下午3:48:28
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(FinishReformListParamIn param, HttpServletResponse res) {
        try {
            finalHandleService.exportExcel(param, request, res);
        } catch (Exception e) {
            LOGGER.error("终审改派工作台导出队列信息 异常:", e);
        }
    }

    /**
     * 终审改派时根据选中的申请查找满足条件的处理人
     *
     * @param finishReformHandleInputVO
     * @return
     * @author JiaCX
     * @date 2017年4月19日 上午11:30:09
     */
    @RequestMapping("/approvePersonList")
    public @ResponseBody List<StaffOrderTaskVO> approvePersonList(@RequestBody FinishReformHandlerParamIn finishReformHandleInputVO) {
        return finalHandleService.approvePersonList(finishReformHandleInputVO);
    }

    /**
     * 根据借款编号获取借款信息最新的版本号
     *
     * @param loanNo
     * @param session
     * @return
     * @author JiaCX
     * @date 2017年7月5日 上午10:55:17
     */
    @RequestMapping("/getLastVersionOfApplication/{loanNo}")
    public @ResponseBody String getLastVersionOfApplication(@PathVariable String loanNo, HttpSession session) {
        String version = "0";
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
        if (null != applyBasiceInfo) {
            version = String.valueOf(applyBasiceInfo.getVersion());
        }
        return version;
    }

    /**
     * 终审批量退回
     *
     * @param appHisList
     * @return
     * @author JiaCX
     * @date 2017年7月13日 上午10:16:40
     */
    @RequestMapping("/batchReturn")
    @UserLogs(link = "信审终审", operation = "终审批量退回", type = Type.终审)
    public @ResponseBody
    Result<String> batchReturn(@RequestBody List<ApplyHistoryVO> appHisList) {
        Result<String> result = new Result<String>();
        int success = 0;
        for (ApplyHistoryVO applyHistoryVo : appHisList) {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, applyHistoryVo.getLoanNo(), true);
            if (applyHistoryVo.getVersion() != applyBasiceInfo.getVersion().intValue()) {
                // 如果要处理的版本号和数据库最新版本号不一致，就返回不可退回
                result.addMessage("申请件：" + applyHistoryVo.getLoanNo() + "在处理中，不可退回。");
            } else {
                try {
                    applyHistoryVo.setRtfState(EnumConstants.RtfState.XSZS.getValue());
                    if ("ZSRTNLR".equals(applyHistoryVo.getBackType())) {
                    	if(null != applyBasiceInfo.getZdqqApply() && applyBasiceInfo.getZdqqApply() == 1) {
                    		//如果是终审退回录入，而且是前前申请件，那么改为退回前前
                    		applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue());
                    	}else {
                    		applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSRETURN.getValue());
                    	}
                    } else {
                        applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSRTNCS.getValue());
                    }
                    applyHistoryVo.setPatchBolt(applyHistoryVo.getBackType());
                    
                    //这里要区分是已分派单子还是未分派单子，已分派单子要按照正常处理方式，未分派单子按照未分派方式处理
                    Result<String> resultTem = null;
                    if(StringUtils.isEmpty(applyHistoryVo.getHandleCode())) {
                    	resultTem = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_UNDISPATCH_BATCH);//未分派申请件批量操作
                    }else {
                    	resultTem = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_BATCH);//已分派申请件批量操作
                    }
                    
                    if (resultTem.success()) {
                        success = success + 1;
                    } else {
                        result.addMessage("申请件:" + applyHistoryVo.getLoanNo() + "信审终审退回失败。" + resultTem.getFirstMessage());
                    }
                } catch (Exception e) {
                    LOGGER.error("申请件:" + applyHistoryVo.getLoanNo() + "信审终审退回异常: ",e);
                    result.addMessage("申请件:" + applyHistoryVo.getLoanNo() + "信审终审退回失败。" );
                }
            }
        }

        if (success == 0) {
            // 全部都设置失败的时候才认为失败
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        } else if (success == appHisList.size()) {
            // 全部成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
            result.addMessage("退回成功");
        } else {
            // 部分成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
        }

        // 现在需求只要成功多少条，失败多少条，上面的取失败信息的暂时不用。如果后续需要显示每个的具体原因就直接去掉下面的代码
        List<String> mes = new ArrayList<String>();
        mes.add("退回成功" + success + "条，失败" + (appHisList.size() - success) + "条");
        result.setMessages(mes);

        return result;
    }

    /**
     * 终审批量拒绝
     *
     * @param appHisList
     * @return
     * @author JiaCX
     * @date 2017年7月13日 下午2:25:12
     */
    @RequestMapping("/batchRefuse")
    @UserLogs(link = "信审终审", operation = "终审批量拒绝", type = Type.终审)
    public @ResponseBody
    Result<String> batchRefuse(@RequestBody List<ApplyHistoryVO> appHisList) {
        Result<String> result = new Result<String>();
        int success = 0;
        for (ApplyHistoryVO applyHistoryVo : appHisList) {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, applyHistoryVo.getLoanNo(), true);
            if (applyHistoryVo.getVersion() != applyBasiceInfo.getVersion().intValue()) {
                // 如果要处理的版本号和数据库最新版本号不一致，就返回不可退回
                result.addMessage("申请件：" + applyHistoryVo.getLoanNo() + "在处理中，不可拒绝。");
            } else {
                try {
                    // Result<String> resultTem = new Result<String>(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
                    // if (!Strings.isEmpty(applyHistoryVo.getConType()) && applyHistoryVo.getConType().contains("debtRatio_Y")) {
                    // resultTem = finalHandleService.repaymentInsufficientJudge(request, applyHistoryVo.getLoanNo());// 若原因是偿还能力不足时进行负债率校验
                    // }
                    // if (resultTem.getType().equals(com.yuminsoft.ams.system.util.Result.Type.FAILURE)) {// 没有审批意见则直接拒绝或者校验通过可以拒绝
                    applyHistoryVo.setRtfState(EnumConstants.RtfState.ZSFP.getValue());
                    applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.ZSFPREJECT.getValue());
                    applyHistoryVo.setAutoRefuse(EnumUtils.YOrNEnum.N.getValue());

                    //这里要区分是已分派单子还是未分派单子，已分派单子要按照正常处理方式，未分派单子按照未分派方式处理
                    Result<String> resultTem = null;
                    if(StringUtils.isEmpty(applyHistoryVo.getHandleCode())) {
                    	resultTem = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_UNDISPATCH_BATCH);//未分派申请件批量操作
                    }else {
                    	resultTem = finalHandleService.updatePubFinalUflo(applyHistoryVo, request, AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_BATCH);//已分派申请件批量操作
                    }
                    
                    if (resultTem.success()) {
                        success = success + 1;
                    } else {
                        result.addMessage("申请件:" + applyHistoryVo.getLoanNo() + "信审终审拒绝失败。" + resultTem.getFirstMessage());
                    }
                    // } else {
                    // result.addMessage("申请件:" + applyHistoryVo.getLoanNo() + "信审终审拒绝失败。" + resultTem.getFirstMessage());
                    // }
                } catch (Exception e) {
                    LOGGER.error("申请件:" + applyHistoryVo.getLoanNo() + "信审终审拒绝失败。" + e.getMessage());
                    result.addMessage("申请件:" + applyHistoryVo.getLoanNo() + "信审终审拒绝失败。");
                }
            }
        }

        if (success == 0) {
            // 全部都设置失败的时候才认为失败
            result.setType(com.yuminsoft.ams.system.util.Result.Type.FAILURE);
        } else if (success == appHisList.size()) {
            // 全部成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
            result.addMessage("拒绝成功");
        } else {
            // 部分成功
            result.setType(com.yuminsoft.ams.system.util.Result.Type.SUCCESS);
        }

        // 现在需求只要成功多少条，失败多少条，上面的取失败信息的暂时不用。如果后续需要显示每个的具体原因就直接去掉下面的代码
        List<String> mes = new ArrayList<String>();
        mes.add("拒绝成功" + success + "条，失败" + (appHisList.size() - success) + "条");
        result.setMessages(mes);

        return result;
    }



    /**
     * 根据借款编号查询改派信息
     *
     * @param loanNo
     * @return
     * @author Jia CX
     * @date 2017年11月21日 下午3:23:55
     * @notes
     */
    @RequestMapping("/handle/reformInfo/{loanNo}")
    public @ResponseBody
    ZsReassignmentVO getReformInfo(@PathVariable String loanNo) {
        RequestPage requestPage = new RequestPage();
        requestPage.setOrder("desc");
        requestPage.setPage(1);
        requestPage.setRows(10);
        FinishReformListParamIn request = new FinishReformListParamIn();
        request.setLoanNo(loanNo);
        ResponsePage<ZsReassignmentVO> page = finalHandleService.getFinishReformList(request, requestPage);
        ZsReassignmentVO param = null;
        if (CollectionUtils.isNotEmpty(page.getRows())) {
            param = page.getRows().get(0);
        }
        return param;
    }

}