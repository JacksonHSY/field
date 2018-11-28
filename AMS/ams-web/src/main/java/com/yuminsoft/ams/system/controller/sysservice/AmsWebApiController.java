package com.yuminsoft.ams.system.controller.sysservice;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.ams.api.vo.request.apply.ApplyEntryVO;
import com.ymkj.ams.api.vo.request.apply.AuditRulesVO;
import com.ymkj.ams.api.vo.request.apply.ContactInfoVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.integratedsearch.ResSupplementalContacts;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.base.core.web.result.JsonResult;
import com.ymkj.bds.biz.api.vo.response.ApplicationInfoResVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.approve.FirstHandleService;
import com.yuminsoft.ams.system.service.approve.MobileHistoryService;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.service.websocket.MessageServer;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.vo.apply.ApprovalOperationVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalOpinionsVO;
import com.yuminsoft.ams.system.vo.apply.FinalApprovalOpinionVO;
import com.yuminsoft.ams.system.vo.pluginVo.RestResponse;
import com.yuminsoft.ams.system.vo.webapi.TelephoneCheckParamIn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dmz
 * @date 2017年6月1日
 */
@Controller
@RequestMapping("/amsWebApi")
public class AmsWebApiController extends BaseController {
    @Autowired
    private MobileHistoryService mobileHistoryService;
    @Autowired
    private FirstHandleService firstHandleServiceImpl;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Autowired
    private ApprovalHistoryService approvalHistoryService;
    @Autowired
    private StaffAbilityService staffAbilityService;
    @Autowired
    private BdsApiService bdsApiService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("#{new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse(\"${master.loanA.date}\")}")
    private Date masterLoanADate;

    @Autowired
    private MessageServer messageServer;

    @Autowired
    private RuleEngineService ruleEngineService;
    @Autowired
    private IntegrateSearchExecuter integrateSearchExecuter;

    @Autowired
    private SysParamDefineMapper sysParamDefineMapper;

    @Autowired
    private BmsBasiceInfoService bmsBasiceInfoService;

    /**
     * 返回客户信息
     *
     * @param loanNo
     * @param model
     * @return
     * @author dmz
     * @date 2017年6月1日
     */
    @RequestMapping("/customerInfo/{loanNo}")
    public String getCustomerInfo(@PathVariable String loanNo, Model model) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, false);
        model.addAttribute("showResSupplementalContacts", true);
        List<ResSupplementalContacts> supplementalContactsList = bmsBasiceInfoService.getSupplementalContacts(loanNo);// 获取签约补充联系人
        model.addAttribute("supplementalContactList", supplementalContactsList);
        if (null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {// 前前进件
            ResApplicationInfoVO resApplicationInfoVO = bmsLoanInfoService.getMoneyLoanInfoDetail(loanNo,true,true);
            model.addAttribute("applyInfo", resApplicationInfoVO);
            return "/approve/zdmoney/moneyReadonlyCustomerInfo";
        }else {
            ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
            model.addAttribute("applyInfo", applyInfo);
            model.addAttribute("applyBasicInfo", applyBasicInfo);
            return "/approve/finish/customerInfoWithoutRed";
        }
    }

    /**
     * 返回内匹配
     *
     * @param loanNo
     * @param model
     * @param session
     * @return
     * @author dmz
     * @date 2017年6月2日
     */
    @RequestMapping("/insideMatch/{loanNo}")
    public String getInsideMatch(@PathVariable String loanNo, Model model, HttpSession session) {
        ApplicationInfoResVO resvo = bdsApiService.getApplicationInformation(loanNo);
        model.addAttribute("applicationInfo", resvo);// 放入内匹返回的申请信息
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("sysCode", sysCode);
        model.addAttribute("picImageUrl", picImageUrl);
        model.addAttribute("picApproval", picApproval);
        model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
        return "/approve/finish/finishInsideMatch";
    }

    /***
     * 电话汇总
     *
     * @author dmz
     * @date 2017年6月2日
     * @param loanNo
     * @param model
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/telephoneSummary/{loanNo}")
    public String getTelephoneSummary(@PathVariable String loanNo, Model model, HttpSession session, HttpServletRequest request) {
        // 获取借款基本信息
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        // 查询借款信息只读信息
        ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);
        model.addAttribute("applyInfo", applyInfo);
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        List<MobileHistory> thirdTelList = mobileHistoryService.queryThridPartyByLoanId(loanNo);
        model.addAttribute("thirdTelList", thirdTelList);

        // 联系人加上本人
        ContactInfoVO contactInfoVO = new ContactInfoVO();
        contactInfoVO.setLoanNo(loanNo);
        contactInfoVO.setSequenceNum(0);
        contactInfoVO.setIfKnowLoan("Y");
        contactInfoVO.setContactName(applyBasiceInfo.getName());
        contactInfoVO.setContactRelation("本人");
        if (null != applyInfo.getBasicInfoVO() && null != applyInfo.getBasicInfoVO().getPersonInfoVO()) {
            contactInfoVO.setContactCellPhone(applyInfo.getBasicInfoVO().getPersonInfoVO().getCellphone());
            contactInfoVO.setContactCellPhone_1(applyInfo.getBasicInfoVO().getPersonInfoVO().getCellphoneSec());
            model.addAttribute("homePhone", applyInfo.getBasicInfoVO().getPersonInfoVO().getHomePhone1());//添加宅电
        }
        if (null != applyInfo.getBasicInfoVO() && null != applyInfo.getBasicInfoVO().getWorkInfoVO()) {
            contactInfoVO.setContactCorpPhone(applyInfo.getBasicInfoVO().getWorkInfoVO().getCorpPhone());
            contactInfoVO.setContactCorpPhone_1(applyInfo.getBasicInfoVO().getWorkInfoVO().getCorpPhoneSec());
        }
        List<ContactInfoVO> phoneContactInfoVO = new ArrayList<ContactInfoVO>();
        // 获取联系人信息
        List<ContactInfoVO> vo = applyInfo.getContactInfoVOList();
        phoneContactInfoVO.add(contactInfoVO);
        if (CollectionUtils.isNotEmpty(vo)) {
            for (ContactInfoVO c : vo) {
                phoneContactInfoVO.add(c);
            }
        }
        model.addAttribute("phoneContactInfoVO", phoneContactInfoVO);
        return "/approve/finish/finishTelephoneSummary";
    }

    /**
     * 审批日志
     *
     * @param loanNo
     * @param model
     * @param request
     * @return
     * @author dmz
     * @date 2017年6月2日
     */
    @RequestMapping("/approveLog/{loanNo}")
    public String getApproveLog(@PathVariable String loanNo, Model model, HttpServletRequest request) {
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, false);
        List<ResBMSQueryLoanLogVO> result = new ArrayList<ResBMSQueryLoanLogVO>();
        try {
            result = bmsLoanInfoService.queryLoanLog(loanNo, WebUtils.retrieveClientIp(request));
        } catch (Exception e) {
            LOGGER.info("返回日志备注页面异常", e);
        }
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("loanLogList", result);
        return "/approve/first/firstLogNotesInfo";
    }

    /**
     * 外部征信
     *
     * @param loanNo
     * @param session
     * @param model
     * @return
     * @author dmz
     * @date 2017年6月2日
     */
    @RequestMapping("/externalCredit/{loanNo}")
    public String getExternalCredit(@PathVariable String loanNo, HttpSession session, Model model) {
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        List<MobileOnline> mobileOnlineList = firstHandleServiceImpl.getMobileOnlineByLoanNo(loanNo, session);

        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("mobileOnlineList", JSONArray.toJSONString(mobileOnlineList, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));

        return "/approve/first/firstExternalCredit";
    }

    /**
     * 审批意见
     *
     * @param loanNo
     * @param model
     * @param session
     * @return
     * @author dmz
     * @date 2017年6月2日
     */
    @RequestMapping("/approvalOpinion/{loanNo}")
    public String getApprovalOpinion(@PathVariable String loanNo, Model model, HttpSession session) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        ApprovalOperationVO approvalOperationVO = approvalHistoryService.getApprovalHistoryByLoanNo(loanNo);
        ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoOnlyReadByLoanNoService(loanNo);    // 查询借款信息
        Result<AuditRulesVO> auditRulesVO = bmsLoanInfoService.queryAuditRulesData(loanNo);

        if (CollectionUtils.isNotEmpty(approvalOperationVO.getApprovalHistoryList())) {
            if (approvalOperationVO.getApproveCheckInfo() != null) {
                model.addAttribute("courtCheck", approvalOperationVO.getApproveCheckInfo().getCourtCheckException() == 0 ? "无异常" : "异常");        // 法院网核查情况
                model.addAttribute("insideMatch", approvalOperationVO.getApproveCheckInfo().getInternalCheckException() == 0 ? "无异常" : "异常");    // 内部匹配情况
            }

            model.addAttribute("approvalInfo", approvalOperationVO);    // 返回审批意见
        }

        model.addAttribute("approvalInfoJson", JSONObject.toJSONString(approvalOperationVO, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect));    // 返回审批意见JSON
        model.addAttribute("applyInfo", applyInfo);
        model.addAttribute("applyInfoJson", JSONObject.toJSONString(applyInfo, SerializerFeature.DisableCircularReferenceDetect));
        model.addAttribute("applyBasiceInfo", applyBasicInfo);
        model.addAttribute("applyBasicInfoJson", JSONObject.toJSONString(applyBasicInfo, SerializerFeature.DisableCircularReferenceDetect));
        model.addAttribute("resEmployeeVO", ShiroUtils.getCurrentUser());
        model.addAttribute("auditRulesVO", auditRulesVO.getData());
        model.addAttribute("loanNo", loanNo);
        // 判断网购达人A贷首次提交初审时间如果大于等于2018-02-06 则不显示12个月外收货地址
        model.addAttribute("isMasterLoanADate", DateUtils.daysOfTwo(applyBasicInfo.getFirstSubmitAudit(), masterLoanADate) < 1);
        return "/approve/finish/finishApprovalOpinionWithoutAction";
    }

    /**
     * 审批意见对外接口VO(规则引擎调用)
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月23日
     */
    @RequestMapping("/getApprovalOpinions/{loanNo}/{sysCode}/{timestamp}")
    @ResponseBody
    public Response<ApprovalOpinionsVO> getApprovalOpinions(@PathVariable String loanNo, @PathVariable String sysCode, @PathVariable Long timestamp, HttpSession session) {
        LOGGER.info("审批意见对外接口VO(规则引擎调用) loanNo:{} sysCode:{} timestamp:{} 当前时间戳:{}", loanNo, sysCode, timestamp, System.currentTimeMillis());
        LOGGER.info("系统时间差:{}S", (System.currentTimeMillis() - timestamp) / 1000 + "");
        Response<ApprovalOpinionsVO> response = new Response<ApprovalOpinionsVO>();
        ApprovalOpinionsVO approvalOpinionsVo = new ApprovalOpinionsVO();
        try {
            if (StringUtils.isNotEmpty(loanNo) && StringUtils.isNotEmpty(sysCode) && null != timestamp) {
                String key = loanNo + "-ruleEngine-";
                // 初审系统初判-- 需要时时传惨(审核产品|月均流水合计|外部负债总额)
                if (redisUtil.exists(key + EnumUtils.ExecuteTypeEnum.XSCS03.name())) {
                    ApprovalOpinionsVO aoVoRedis = (ApprovalOpinionsVO) redisUtil.get(key + EnumUtils.ExecuteTypeEnum.XSCS03.name());
                    LOGGER.info("初审系统初判规则引擎时时取值 Object:{}", JSON.toJSONString(aoVoRedis));
                    String selectAssetsType =  Strings.isNotEmpty(aoVoRedis.getSelectAssetsType()) ? aoVoRedis.getSelectAssetsType(): "&";
                    approvalOpinionsVo= approvalHistoryService.getApprovalOpinions(loanNo,selectAssetsType);
                    approvalOpinionsVo.setApprovalProductCd(aoVoRedis.getApprovalProductCd());        // 审批产品
                    approvalOpinionsVo.setMonthAverage(aoVoRedis.getMonthAverage());                // 月平均
                    approvalOpinionsVo.setOutDebtTotal(aoVoRedis.getOutDebtTotal());                // 外部负责总额
                    approvalOpinionsVo.setSaveAssetsVO(aoVoRedis.getSaveAssetsVO());               // 资产信息
                    redisUtil.remove(key + EnumUtils.ExecuteTypeEnum.XSCS03.name());
                } else {
                    approvalOpinionsVo =approvalHistoryService.getApprovalOpinions(loanNo,null);// 审批勾选审批资产信息取数据库
                }

                if (redisUtil.exists(key + EnumUtils.ExecuteTypeEnum.XSZS03.name())) {
                    ApprovalOpinionsVO approvalOpinionsVO = (ApprovalOpinionsVO) redisUtil.get(key + EnumUtils.ExecuteTypeEnum.XSZS03.name());
                    LOGGER.info("终审系统初判规则引擎时时取值 Object:{}", JSON.toJSONString(approvalOpinionsVO));
                    approvalOpinionsVo.setApprovalProductCd(approvalOpinionsVO.getApprovalProductCd());        // 审批产品
                    redisUtil.remove(key + EnumUtils.ExecuteTypeEnum.XSZS03.name());
                }

                response.setData(approvalOpinionsVo);
                response.setRepCode(EnumUtils.ResponseCodeEnum.SUCCESS.getValue());
            } else {
                response.setRepCode(EnumUtils.ResponseCodeEnum.PARAMERROR.getValue());
                response.setRepMsg("参数错误!");
            }

            LOGGER.info("给规则引擎返回值 response:{}", JSON.toJSONString(approvalOpinionsVo));

        } catch (Exception e) {
            response.setRepCode(EnumUtils.ResponseCodeEnum.EXCEPTION.getValue());
            response.setRepMsg("系统忙请稍后");
            LOGGER.error("审批意见对外接口VO(规则引擎调用)异常", e);
        }

        return response;
    }

    @RequestMapping("syncStaffOrderSetWithTask")
    @ResponseBody
    public Result<String> syncStaffOrderSetWithTask() {
        LOGGER.info("同步员工接单正常队列上限，挂起队列上限，初终审标识");
        int count = staffAbilityService.syncStaffOrderSetAndTask();
        LOGGER.info("成功同步数据量:{}", count);

        return new Result<String>(Result.Type.SUCCESS, "成功同步数据量" + count);
    }

    /**
     * 查询任务数
     *
     * @return
     * @author dmz
     */
    @RequestMapping("/getListStaffOrderTask")
    public String getListStaffOrderTask(Model model) {
        List<StaffOrderTask> list = new ArrayList<StaffOrderTask>();
        list = staffAbilityService.getListStaffOrderTask(null);
        model.addAttribute("staffOrderTaskList", list);
        return "/system/staffOrderTask";
    }

    /**
     * 根据单号查询最后终审通过的审批意见对外接口
     *
     * @param loanNo
     * @param sysCode
     * @return
     * @author lihm
     */
    @RequestMapping(value = "/getApprovalOpinions/getLastApprovalHistory", method = RequestMethod.POST)
    @ResponseBody
    @UserLogs(link = "信审终审", operation = "查询最后一条审批意见对外接口", type = UserLog.Type.终审)
    public String getLastApprovalHistoryByLoanNo(String sysCode, String loanNo, String userCode, Long timestamp, String rtfNodeState) {
        LOGGER.info("根据借款编号查询最后一条审批意见对外接口：当前时间戳：[{}],调用人：[{}], 借款编号:[{}] ,sysCode:[{}]", timestamp, userCode, loanNo, sysCode);
        Response<FinalApprovalOpinionVO> response = new Response<FinalApprovalOpinionVO>();
        try {
            if (null != timestamp && StringUtils.isNotEmpty(loanNo) && StringUtils.isNotEmpty(sysCode) && StringUtils.isNotEmpty(userCode)) {
                response = approvalHistoryService.getLastApprovalByLoanNo(loanNo, rtfNodeState);
            } else {
                response.setRepCode(EnumUtils.ResponseCodeEnum.PARAMERROR.getValue());
                response.setRepMsg("参数错误!");
            }
        } catch (Exception e) {
            response.setRepCode(EnumUtils.ResponseCodeEnum.EXCEPTION.getValue());
            response.setRepMsg("系统忙请稍后");
            LOGGER.error("查询最后审批意见对外接口异常:loanNo：[{}]，exception:", loanNo, e);
        }
        LOGGER.info("根据借款编号查询最后一条审批意见对外接口返回值 result:{}", JSON.toJSONString(response));
        return JSON.toJSONString(response);
    }

    /**
     * 根据借款编号分页查询电核记录
     *
     * @param param
     * @return
     * @author Jia CX
     * <p>2018年3月27日 下午4:16:30</p>
     */
    @RequestMapping(value = "/getTelephoneCheckList", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse<List<MobileHistory>> getTelephoneCheckList(@RequestBody TelephoneCheckParamIn param) {
        try {
            return mobileHistoryService.getTelephoneCheckList(param);
        } catch (Exception e) {
            LOGGER.info("webapi--根据借款编号分页查询电核记录,发生异常", e);
            return new RestResponse<List<MobileHistory>>(EnumUtils.ResponseCodeEnum.EXCEPTION.getValue(), "系统忙请稍后");
        }
    }

    /**
     * 更新站内消息数量
     *
     * @param account
     * @return
     * @author Jia CX
     * <p>2018年6月22日 下午3:38:43</p>
     */
    @RequestMapping(value = "/message/sendCount/{account}", method = RequestMethod.POST)
    public @ResponseBody
    JsonResult<Boolean> sendCount(@PathVariable String account) {
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        messageServer.sendUnreadCountToEmployees(accounts);
        return new JsonResult<Boolean>();
    }


    @RequestMapping("/getApprovalOpinions/updateData")
    @ResponseBody
    public Result<String> updateData() {
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        List<SysParamDefine> sysParamDefineList = sysParamDefineMapper.findByParamType("ruleEngineRepairData");
        if (!org.springframework.util.CollectionUtils.isEmpty(sysParamDefineList)) {
            SysParamDefine sysParamDefine1 = sysParamDefineList.get(0);
            String[] params = sysParamDefine1.getParamValue().split(",");
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(20);
            executor.setMaxPoolSize(Integer.parseInt(params[0]));
            executor.setKeepAliveSeconds(100);
            executor.setQueueCapacity(20);
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.initialize();
            Date beginDate = DateUtils.stringToDate(params[2], DateUtils.DEFAULT_DATE_FORMAT);
            Date maxDate = DateUtils.stringToDate(params[3], DateUtils.DEFAULT_DATE_FORMAT);
            boolean mark = true;
            while (mark) {
                Date endDate = DateUtils.addDate(beginDate, Integer.parseInt(params[1]));
                if (endDate.getTime() >= maxDate.getTime()) {
                    endDate = maxDate;
                    mark = false;
                }
                RepairDataThread repairDataThread = new RepairDataThread();
                repairDataThread.setIntegrateSearchExecuter(integrateSearchExecuter);
                repairDataThread.setRuleEngineService(ruleEngineService);
                repairDataThread.setBeginDate(DateUtils.dateToString(beginDate, DateUtils.DEFAULT_DATE_FORMAT));
                repairDataThread.setEndDate(DateUtils.dateToString(endDate, DateUtils.DEFAULT_DATE_FORMAT));
                executor.execute(repairDataThread);
                beginDate = DateUtils.addDate(endDate, 1);
            }
        }
        return result;
    }
}