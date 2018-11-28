package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.bms.biz.api.vo.request.job.ReqZhongAnHistoryVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckInfoService;
import com.yuminsoft.ams.system.service.approve.FirstHandleService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.bms.ZhongAnCheatService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 初审办理控制层
 *
 * @author dmz
 * @date 2017年3月9日
 */
@Controller
@RequestMapping("/firstHandle")
public class FirstHandleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstHandleController.class);
    @Autowired
    private BmsLoanInfoService bmsLoanInfoServiceImpl;
    @Autowired
    private FirstHandleService firstHandleService;
    @Autowired
    private ApprovalHistoryService approvalHistoryService;
    @Autowired
    private StaffOrderTaskService staffOrderTaskService;
    @Autowired
    private ApproveCheckInfoService approveCheckInfoService;
    @Autowired
    private BmsBasiceInfoService bmsBasiceInfoService;
    @Autowired
    private ZhongAnCheatService zhongAnCheatService;

    /**
     * 验证
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年4月7日
     */
    @RequestMapping("/approvalHistory/{loanNo}")
    @ResponseBody
    public Result<ApprovalHistory> getApprovalHistory(@PathVariable String loanNo) {
        Result<ApprovalHistory> result = new Result<ApprovalHistory>(Type.FAILURE);

        // 校验是否填写了审批意见
        ApprovalHistory approvalHistory = approvalHistoryService.getApprovalHistoryByLoanNoAndStaffCode(loanNo);
        if (approvalHistory == null) {
            result.addMessage("请填写审批意见!");
            return result;
        }

        // 校验其他资料核对信息是否勾选了"征信平台是否已验证"字段
        ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(loanNo);
        if (approveCheckInfo == null || approveCheckInfo.getCreditCheckException() == null || approveCheckInfo.getCreditCheckException() == 1) {
            result.addMessage("请勾选征信平台已验证!");
            return result;
        }

        // 校验产品期限额度是否可用
        try {
            /*if (bmsBasiceInfoService.checkApprovalProduct(loanNo)){
				result.addMessage("请重新选择审批产品、审批额度、审批期限!");
				return result;
			}*/
        } catch (BusinessException e) {
            result.addMessage(e.getMessage());
            return result;
        }

        result.setType(Type.SUCCESS);
        result.setData(approvalHistory);

        return result;
    }

    /**
     * 初审办理提交
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月9日
     */
    @RequestMapping("/submit")
    @ResponseBody
    @UserLogs(link = "信审初审", operation = "初审通过", type = UserLog.Type.初审)
    public Result<String> submit(String loanNo, Long version, ApplyHistoryVO applyHistoryVo, HttpServletRequest request) {
        LOGGER.info("信审初审办理提交，单号:{}", loanNo);
        Result<String> result = new Result<String>(Type.FAILURE);
        try {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
            if (version.equals(applyBasiceInfo.getVersion())) {
                // 校验资产信息是否通过
                Long startDate = System.currentTimeMillis();
                if (null != applyBasiceInfo.getZdqqApply() && 1 == applyBasiceInfo.getZdqqApply()) {// 前前进件
                    result = bmsLoanInfoServiceImpl.validateMoneyApplyInfo(loanNo);
                } else {
                    result = bmsLoanInfoServiceImpl.validateApplyInfo(loanNo);
                }
                if (result.success()) {
                    // 调用众安发欺诈
                    ReqZhongAnHistoryVO reqZhongAnHistoryVO = new ReqZhongAnHistoryVO();
                    reqZhongAnHistoryVO.setLoanNo(applyBasiceInfo.getLoanNo());
                    reqZhongAnHistoryVO.setCellphone(applyBasiceInfo.getCellPhone());
                    reqZhongAnHistoryVO.setIdNo(applyBasiceInfo.getIdNo());
                    reqZhongAnHistoryVO.setName(applyBasiceInfo.getName());
                    reqZhongAnHistoryVO.setCellPhoneSec(applyBasiceInfo.getCorpPhoneSec());
                    zhongAnCheatService.zhongAnHistory(reqZhongAnHistoryVO);

                    applyHistoryVo.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
                    applyHistoryVo.setCheckPerson(ShiroUtils.getCurrentUser().getUsercode());
                    applyHistoryVo.setIdNo(applyBasiceInfo.getIdNo());
                    applyHistoryVo.setIp(WebUtils.retrieveClientIp(request));
                    applyHistoryVo.setLoanNo(loanNo);
                    applyHistoryVo.setName(applyBasiceInfo.getName());
                    applyHistoryVo.setRtfState(EnumConstants.RtfState.XSCS.getValue());
                    applyHistoryVo.setVersion(applyBasiceInfo.getVersion().intValue());
                    applyHistoryVo.setApplyType(applyBasiceInfo.getApplyType());
                    applyHistoryVo.setApproveProduct(applyBasiceInfo.getProductCd());
                    result = firstHandleService.updateFirstHandLoanNoService(applyHistoryVo, applyBasiceInfo);
                }
            } else {
                result.addMessage("您当前办理的借款单有可能已经被改派,请重新办理!");
                result.setType(Type.VERSIONERR);
            }

        } catch (BusinessException e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审提交失败,借款单号:{}", loanNo, e);
            result.addMessage(e.getMessage());
        } catch (Exception e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审提交失败,借款单号:{}", loanNo, e);
            result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);

        }

        return result;
    }

    /**
     * 初审回退
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月9日
     */
    @RequestMapping("/rollback/{loanNo}/{version}")
    @ResponseBody
    @UserLogs(link = "信审初审", operation = "初审回退", type = UserLog.Type.初审)
    public Result<String> rollback(@PathVariable String loanNo, @PathVariable Long version, String form, HttpServletRequest request) {
        Result<String> result = new Result<String>(Type.FAILURE);
        try {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
            if (version.equals(applyBasiceInfo.getVersion())) {
                ApplyHistoryVO applyHistoryVo = JSONObject.parseObject(form, ApplyHistoryVO.class);
                applyHistoryVo.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
                applyHistoryVo.setCheckPerson(ShiroUtils.getCurrentUser().getUsercode());
                applyHistoryVo.setIdNo(applyBasiceInfo.getIdNo());
                applyHistoryVo.setIp(WebUtils.retrieveClientIp(request));
                applyHistoryVo.setLoanNo(loanNo);
                applyHistoryVo.setName(applyBasiceInfo.getName());
                if (null != applyBasiceInfo.getZdqqApply() && 1 == applyBasiceInfo.getZdqqApply()) { // 退回前前
                    applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSCSZDQQRETURN.getValue());
                } else {
                    applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSCSRETURN.getValue());
                }
                applyHistoryVo.setRtfState(EnumConstants.RtfState.XSCS.getValue());
                applyHistoryVo.setVersion(applyBasiceInfo.getVersion().intValue());
                applyHistoryVo.setApplyType(applyBasiceInfo.getApplyType());
                applyHistoryVo.setApproveProduct(applyBasiceInfo.getProductCd());
                result = firstHandleService.updateFirstHandLoanNoService(applyHistoryVo, applyBasiceInfo);
            } else {
                result.addMessage("您当前办理的借款单有可能已经被改派,请重新办理!");
                result.setType(Type.VERSIONERR);
            }
        } catch (BusinessException e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审退回失败,借款单号:{}", loanNo, e);
            result.addMessage(e.getMessage());
        } catch (Exception e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审退回失败,借款单号:{}", loanNo, e);
            result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);
        }
        return result;
    }

    /**
     * 初审拒绝
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月9日
     */
    @RequestMapping("/refuse/{loanNo}/{version}/{conditionType}")
    @ResponseBody
    @UserLogs(link = "信审初审", operation = "初审拒绝", type = UserLog.Type.初审)
    public Result<String> refuse(@PathVariable String loanNo, @PathVariable Long version, ApplyHistoryVO applyHistoryVo, @PathVariable String conditionType, HttpServletRequest request) {
        Result<String> result = new Result<String>(Type.FAILURE);
        try {
            ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
            if (version.equals(applyBasiceInfo.getVersion())) {
                if (!Strings.isEmpty(conditionType) && conditionType.contains("debtRatio_Y")) {
                    result = firstHandleService.repaymentInsufficientJudge(applyBasiceInfo, EnumConstants.RtfNodeState.XSCSREJECT.getValue());// 若原因是偿还能力不足时进行负债率校验
                }
                if (result.getType().equals(Type.FAILURE)) {// 没有审批意见则直接拒绝或者校验通过可以拒绝
                    applyHistoryVo.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
                    applyHistoryVo.setCheckPerson(ShiroUtils.getCurrentUser().getUsercode());
                    applyHistoryVo.setIdNo(applyBasiceInfo.getIdNo());
                    applyHistoryVo.setIp(WebUtils.retrieveClientIp(request));
                    applyHistoryVo.setLoanNo(loanNo);
                    applyHistoryVo.setName(applyBasiceInfo.getName());
                    applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSCSREJECT.getValue());
                    applyHistoryVo.setRtfState(EnumConstants.RtfState.XSCS.getValue());
                    applyHistoryVo.setVersion(applyBasiceInfo.getVersion().intValue());
                    applyHistoryVo.setApplyType(applyBasiceInfo.getApplyType());
                    applyHistoryVo.setApproveProduct(applyBasiceInfo.getProductCd());
                    applyHistoryVo.setAutoRefuse(EnumUtils.YOrNEnum.N.getValue());
                    result = firstHandleService.updateFirstHandLoanNoService(applyHistoryVo, applyBasiceInfo);
                } else {
                    result.setType(Type.FAILURE);
                }
            } else {
                result.addMessage("您当前办理的借款单有可能已经被改派,请重新办理!");
                result.setType(Type.VERSIONERR);
            }
        } catch (BusinessException e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审拒绝失败,借款单号:{}", loanNo, e);
            result.addMessage(e.getMessage());
        } catch (Exception e) {
            result.setType(Type.FAILURE);
            LOGGER.error("信审初审拒绝失败,借款单号:{}", loanNo, e);
            result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);
        }
        return result;
    }

    /**
     * 初审挂起
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月9日
     */
    @RequestMapping("/hang/{loanNo}/{version}")
    @ResponseBody
    @UserLogs(link = "信审初审", operation = "初审挂起")
    public Result<String> hangup(@PathVariable String loanNo, @PathVariable Long version, ApplyHistoryVO applyHistoryVo, HttpServletRequest request) {
        Result<String> result = staffOrderTaskService.findAurrInactiveTaskNumService(ShiroUtils.getCurrentUser().getUsercode());
        if (result.success()) {
            try {
                ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true);
                if (version.equals(applyBasiceInfo.getVersion())) {
                    applyHistoryVo.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
                    applyHistoryVo.setCheckPerson(ShiroUtils.getCurrentUser().getUsercode());
                    applyHistoryVo.setIdNo(applyBasiceInfo.getIdNo());
                    applyHistoryVo.setIp(WebUtils.retrieveClientIp(request));
                    applyHistoryVo.setLoanNo(loanNo);
                    applyHistoryVo.setName(applyBasiceInfo.getName());
                    applyHistoryVo.setRtfNodeState(EnumConstants.RtfNodeState.XSCSHANGUP.getValue());
                    applyHistoryVo.setRtfState(EnumConstants.RtfState.XSCS.getValue());
                    applyHistoryVo.setVersion(applyBasiceInfo.getVersion().intValue());
                    applyHistoryVo.setApplyType(applyBasiceInfo.getApplyType());
                    applyHistoryVo.setApproveProduct(applyBasiceInfo.getProductCd());
                    result = firstHandleService.updateFirstHandLoanNoService(applyHistoryVo, applyBasiceInfo);
                } else {
                    result.addMessage("您当前办理的借款单有可能已经被改派,请重新办理!");
                    result.setType(Type.VERSIONERR);
                }
            } catch (BusinessException e) {
                LOGGER.error("信审初审挂起失败,借款单号:{}", loanNo, e);
                result.setType(Type.FAILURE);
                result.addMessage(e.getMessage());
            } catch (Exception e) {
                LOGGER.error("信审初审挂起失败,借款单号:{}", loanNo, e);
                result.setType(Type.FAILURE);
                result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);
            }
        }
        return result;
    }
}
