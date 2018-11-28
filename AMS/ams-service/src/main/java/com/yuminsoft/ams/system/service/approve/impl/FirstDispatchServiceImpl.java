package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSON;
import com.bstek.uflo.service.ProcessService;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.dispatch.FirstDispatchExecuter;
import com.ymkj.ams.api.vo.request.apply.PersonHistoryLoanVO;
import com.ymkj.ams.api.vo.request.apply.ReasonVO;
import com.ymkj.ams.api.vo.request.audit.ReqCsUpdVO;
import com.ymkj.ams.api.vo.request.audit.ReqZsUpdVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAudiUpdVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.dao.system.ApproveMessageMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.ApproveMessage;
import com.yuminsoft.ams.system.domain.uflo.Task;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.FirstDispatchService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.MailService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.mail.MailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author dongmz
 */
@Service
public class FirstDispatchServiceImpl extends BaseService implements FirstDispatchService {

    @Autowired
    private StaffOrderTaskService staffOrderTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApplyHistoryMapper applyHistorymapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private PmsApiService pmsApiServiceImpl;
    @Autowired
    private FirstDispatchExecuter firstDispatchExecuter;
    @Autowired
    private RuleEngineService ruleEngineService;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Autowired
    private ApplyHistoryMapper applyHistoryMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private PmsApiService pmsApiService;
    @Autowired
    private ApproveMessageMapper approveMessageMapper;
    @Resource(name = ProcessService.BEAN_ID)
    private ProcessService processService;
    @Resource(name = com.bstek.uflo.service.TaskService.BEAN_ID)
    private com.bstek.uflo.service.TaskService ufloTaskService;

    /**
     * 初审自动派单
     *
     * @param order
     * @author dmz
     */
    @Override
    public void automaticDispatch(ResBMSAutomaticDispatchAttrVO order) {
        LOGGER.info("开始处理单号:{} 申请人姓名:{}", order.getLoanNo(), order.getCustmerName());
        RuleEngineVO ruleEngineVO = executeRuleEngineHandle(order);
        if (null != ruleEngineVO) {
            if (Strings.isNotEmpty(ruleEngineVO.getComCreditRating())) {
                if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(order.getIfNewLoanNo())) {// 判断是否是优先件
                    if (null != order.getCheckPersonCode()) { // 判断是否有初审员
                        // 查询优先级原初审员是否可以接单
                        Map<String, Object> mapOld = new HashMap<String, Object>();
                        mapOld.put("staffCode", order.getCheckPersonCode());// 员工编号
                        mapOld.put("productCode", order.getInitProductCode() + "-" + order.getApplyType());
                        mapOld.put("comCreditRating", "%" + ruleEngineVO.getComCreditRating() + "%");// 欺诈风险评估
                        mapOld.put("areaCode", order.getAreaCode());
                        StaffOrderTask sotOld = staffOrderTaskService.getOriginalApprover(mapOld);
                        if (null != sotOld) {// 如果原来初审员可以接单直接分配
                            //if (executeRuleEngine(order)) {//调用规则引擎
                            // 修改当前初审员-优先-队列状态
                            staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.PRIORITY);
                            addApplyHistory(order, sotOld.getStaffCode(), null);
                            if (updateBorrowNumberState(order, sotOld.getStaffCode())) {
                                LOGGER.info("借款单号[{}]派给 [{}],信审初审派发优先件成功!", order.getLoanNo(), JSON.toJSONString(sotOld));
                            } else {
                                LOGGER.error("借款单号[{}],信审初审修改优先件状态失败!", order.getLoanNo());
                                throw new BusinessException("信审初审修改优先件状态失败!");
                            }
                            //}
                        } else {// 重新分配
                            priorityTaskNotCheckDispatch(order, ruleEngineVO.getComCreditRating());
                        }
                    } else { // 如果初审员为空
                        ApplyHistory applyHistory = applyHistorymapper.findLastOperationState(order.getLoanNo());
                        if (null != applyHistory) {
                            priorityTaskNotCheckDispatch(order, ruleEngineVO.getComCreditRating());// 重新分配
                        } else {
                            updateLoanIfNewLoanNoStatus(order);// 数据问题修复措施
                        }
                    }
                    // 正常件--派到正常队列
                } else if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(order.getIfNewLoanNo()) && Strings.isEmpty(order.getCheckPersonCode())) {
                    // 判断是否是复议再申请
                    if (EnumUtils.YOrNEnum.Y.getValue().equals(order.getIfReconsiderUser())) {
                        // 复议在申请
                        ApplyHistory applyHistory = applyHistoryMapper.findLastFirstApplyHistory(order.getReconsiderLoanNo());
                        if (null != applyHistory && !EnumUtils.YOrNEnum.Y.getValue().equals(applyHistory.getAutoRefuse())) {// 判断是复议在申请且是人为拒绝
                            Map<String, Object> mapAM = new HashMap<String, Object>();
                            mapAM.put("rtfState", EnumConstants.RtfState.CSFP.getValue());
                            mapAM.put("rtfNodeState", EnumConstants.RtfNodeState.CSFPSUBMIT.getValue());
                            mapAM.put("loanNo", order.getLoanNo());
                            ApproveMessage approveMessage = approveMessageMapper.findOne(mapAM);
                            if (null == approveMessage) { // 判断是否已经发过邮件
                                Map<String, Object> mapR = new HashMap<String, Object>();
                                mapR.put("staffCode", applyHistory.getCreatedBy());// 员工编号
                                mapR.put("productCode", order.getInitProductCode() + "-" + order.getApplyType());
                                mapR.put("comCreditRating", "%" + ruleEngineVO.getComCreditRating() + "%");// 欺诈风险评估
                                mapR.put("areaCode", order.getAreaCode());
                                StaffOrderTask staffOrderTask = staffOrderTaskService.getReconsiderActiviesTask(mapR);
                                if (null != staffOrderTask) {// 判断原初审人员是否可接单
                                    //if (executeRuleEngine(order)) {// 调用规则引擎
                                    LOGGER.info("借款编号[{}]复议再申请原初审可以接单分派给原初审[{}]", order.getLoanNo(), applyHistory.getCreatedBy());
                                    // 修改正常队列队列数
                                    staffOrderTaskService.updateStaffTaskNum(staffOrderTask.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.ACTIVITY);
                                    addApplyHistory(order, staffOrderTask.getStaffCode(), null);
                                    if (updateBorrowNumberState(order, staffOrderTask.getStaffCode())) {
                                        LOGGER.info("借款单号[{}]派给 [{}]信审初审派发复议正常件成功!", order.getLoanNo(), JSON.toJSONString(staffOrderTask));
                                    } else {
                                        LOGGER.error("借款单号[{}],信审初审修改复议正常件状态失败!", order.getLoanNo());
                                        throw new BusinessException("信审初审修改复议正常件状态失败!");
                                    }
                                    //}
                                } else {
                                    LOGGER.info("借款编号[{}]复议再申请原初审[{}]不可接单发生邮件", order.getLoanNo(), applyHistory.getCreatedBy());
                                    sendNotice(order, applyHistory.getCreatedBy());
                                }
                            }
                        } else {
                            LOGGER.info("借款编号[{}]复议再申请未找到上一笔借款[{}]审批意见,故走初审正常件派单", order.getLoanNo(), order.getReconsiderLoanNo());
                            currActivieTaskCheckDispatch(order, ruleEngineVO.getComCreditRating());
                        }
                    } else {
                        // 正常件自动派单
                        currActivieTaskCheckDispatch(order,  ruleEngineVO.getComCreditRating());
                    }
                }
            } else {
                LOGGER.error("执行规则引擎未返回综合信用评级借款单号[{}]", order.getLoanNo());
            }
        }
    }

    /***
     * 初审派单添加规则引擎 -目前初审派单只有拒绝和通过两个动作
     *
     * @author dmz
     * @date 2017年9月4日
     * @param order
     * @return
     */
    private RuleEngineVO executeRuleEngineHandle(ResBMSAutomaticDispatchAttrVO order) {
        RuleEngineVO ruleEngineVO = null;
        Result<RuleEngineVO> result = ruleEngineService.executeRuleEngine(order.getLoanNo(), EnumUtils.ExecuteTypeEnum.XSCS06);
        if (result.getSuccess()) {// 调用规则引擎成功
            if (EnumUtils.EngineType.PASS.getValue().equals(result.getFirstMessage())) {
                LOGGER.info("借款单号[{}],初审派单调用规则引擎返回通过!", order.getLoanNo());
                ruleEngineVO = result.getData();
            } else if (EnumUtils.EngineType.REJECT.getValue().equals(result.getFirstMessage())) {// 拒绝
                // 执行拒绝操作
                Result<ReasonVO> resultReason = bmsLoanInfoService.queryReason(result.getData().getRejectCode());
                if (resultReason.success()) {
                    ReasonVO reasonVO = resultReason.getData();
                    String rejectCode = "";
                    if (null != reasonVO.getFirstLevelReasonCode()) {
                        rejectCode = reasonVO.getFirstLevelReasonCode();
                    }
                    if (null != reasonVO.getTwoLevelReasonCode()) {
                        rejectCode = reasonVO.getFirstLevelReasonCode() + "-" + reasonVO.getTwoLevelReasonCode();
                    }
                    addApplyHistory(order, null, rejectCode);// 添加日志
                    ReqZsUpdVO reqZsUpdVO = new ReqZsUpdVO();
                    reqZsUpdVO.setLoanNo(order.getLoanNo());
                    reqZsUpdVO.setRtfStatus(EnumConstants.RtfState.CSFP.getValue());
                    reqZsUpdVO.setRtfNodeStatus(EnumConstants.RtfNodeState.CSFPREJECT.getValue());
                    reqZsUpdVO.setVersion(order.getVersion());
                    reqZsUpdVO.setOperatorIp(WebUtils.getLocalIpAddress());
                    reqZsUpdVO.setFirstLevelReasonCode(reasonVO.getFirstLevelReasonCode());
                    reqZsUpdVO.setFirstLevelReasons(reasonVO.getFirstLevelReason());
                    reqZsUpdVO.setTwoLevelReasonCode(reasonVO.getTwoLevelReasonCode());
                    reqZsUpdVO.setTwoLevelReasons(reasonVO.getTwoLevelReason());

                    // 判断是否是优先件
                    if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(order.getIfNewLoanNo()) && null != order.getCheckPersonCode()) {
                        Long taskId = taskMapper.findTaskIdByBusinessId(order.getLoanNo());
                        if (null != taskId && taskId.compareTo(new Long(0)) > 0) {
                            taskService.deny(taskId);
                        }
                    }
                    boolean ruleAction = bmsLoanInfoService.ruleEngineReject(reqZsUpdVO);
                    if (ruleAction) {// 执行拒绝成功
                        LOGGER.error("借款单号[{}],初审派单拒绝成功!", order.getLoanNo());
                    } else {
                        LOGGER.error("借款单号[{}],规则引擎拒绝调用失败!", order.getLoanNo());
                        throw new BusinessException("规则引擎拒绝调用失败!");
                    }
                    LOGGER.info("借款单号[{}],初审派单调用规则引擎返拒绝成功!", order.getLoanNo());
                } else {
                    LOGGER.error("借款单号[{}],规则引擎拒绝调用原因码失败!", order.getLoanNo());
                    throw new BusinessException("规则引擎拒绝调用原因码失败!");
                }
            } else {
                LOGGER.error("借款单号[{}],初审自动派单调用规则引擎异常 result:{}", order.getLoanNo(), JSON.toJSONString(result));
                throw new BusinessException("初审自动派单调用规则引擎异常 loanNo:" + order.getLoanNo());
            }
        }
        return ruleEngineVO;
    }

    /**
     * 保存流程日志
     *
     * @param order
     * @param staffCode
     * @param rejectCode-标记是分派日志还是规则引擎拒绝日志(null:分派,notNull:规则引擎拒绝)
     * @return
     * @author dmz
     * @date 2017年3月20日
     */
    private void addApplyHistory(ResBMSAutomaticDispatchAttrVO order, String staffCode, String rejectCode) {
        ApplyHistory apply = new ApplyHistory();
        apply.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
        apply.setCheckPerson(staffCode);
        apply.setIdNo(order.getCustmerIDNo());
        apply.setLoanNo(order.getLoanNo());
        apply.setName(order.getCustmerName());
        // Task task = taskMapper.getTaskByBusinessId(order.getLoanNo());
        apply.setProNum(order.getLoanNo());
        if (null == rejectCode) {// 派单通过日志
            apply.setProName("信审初审已分派");
            apply.setRtfNodeState(EnumConstants.RtfNodeState.XSCSASSIGN.getValue());
            apply.setRtfState(EnumConstants.RtfState.XSCS.getValue());
            apply.setRemark("初审自动分派");
        } else { // 规则引擎拒绝日志
            apply.setProName("规则引擎拒绝");
            apply.setRtfNodeState(EnumConstants.RtfNodeState.CSFPREJECT.getValue());
            apply.setRtfState(EnumConstants.RtfState.CSFP.getValue());
            apply.setRemark("规则引擎拒绝");
            apply.setRefuseCode(rejectCode);
            apply.setAutoRefuse(EnumUtils.YOrNEnum.Y.getValue());
        }
        int save = applyHistorymapper.save(apply);
        if (1 != save) {
            throw new BusinessException("添加审批流程日志失败!");
        }
    }

    /**
     * 修改借款单号状态
     *
     * @param order
     * @param staffCode
     * @return
     * @author dmz
     * @date 2017年3月20日
     */
    private boolean updateBorrowNumberState(ResBMSAutomaticDispatchAttrVO order, String staffCode) {
        boolean action = false;
        // 修改工作流
        if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(order.getIfNewLoanNo()) && null != order.getCheckPersonCode()) {
            Task task = taskMapper.findByBusinessId(order.getLoanNo()); // 查询工作流
            String taskStaffCode = order.getCheckPersonCode();
            if (!staffCode.equals(order.getCheckPersonCode())) {
                taskStaffCode = staffCode;
            }
            ufloTaskService.start(task.getId());
            processService.saveProcessVariable(task.getProcessInstanceId(), "firstApprove", taskStaffCode);
            ufloTaskService.complete(task.getId());
        } else {// 新生件开启新的工作流
            String task = taskService.startProcess(order.getLoanNo(), staffCode, staffCode);
            LOGGER.info("创建工作流 {}", task);
        }
        ReqCsUpdVO request = new ReqCsUpdVO();
        request.setSysCode(sysCode);
        request.setLoanNo(order.getLoanNo());
        request.setRtfNodeStatus(EnumConstants.RtfNodeState.XSCSASSIGN.getValue());
        request.setCheckNodeStatus(EnumConstants.ChcekNodeState.NOCHECK.getValue());
        ResEmployeeVO resEmployeeVO = pmsApiServiceImpl.findEmpByUserCode(staffCode);
        if (null != resEmployeeVO) {
            request.setRemark("分派至:" + resEmployeeVO.getName());
            request.setCsPersonName(resEmployeeVO.getName());
        }
        request.setOperatorCode("系统");
        request.setCsPersonCode(staffCode);
        request.setOperatorIP(WebUtils.getLocalIpAddress());
        request.setAccDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
        if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(order.getIfNewLoanNo())) {
            request.setAllotDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
        }
        request.setVersion(order.getVersion());
        ResBMSAudiUpdVo response = firstDispatchExecuter.dispatch(request);
        LOGGER.info("自动派单修改状态 updateBorrowNumberState params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            action = true;
        }
        return action;
    }

    /**
     * 优先件未找到原初审员重新分配
     *
     * @param order
     * @author dmz
     */
    private void priorityTaskNotCheckDispatch(ResBMSAutomaticDispatchAttrVO order,String comCreditRating) {
        // 当前优先队列数最小>接单能力
        LOGGER.info("借款单号[{}],信审初审优先件未找到原信审人员，重新查找其他信审初审人员!", order.getLoanNo());
        Map<String, Object> mapN = new HashMap<String, Object>();
        mapN.put("productCode", order.getInitProductCode() + "-" + order.getApplyType());
        mapN.put("comCreditRating","%" + comCreditRating + "%");// 欺诈风险评估
        mapN.put("areaCode", order.getAreaCode());
        StaffOrderTask sotN = staffOrderTaskService.findPriorityTaskOrderService(mapN);
        if (null != sotN) { //是否找到待分配人员
            //if (executeRuleEngine(order)) {//调用规则引擎
            // 修改当前初审员-优先-队列状态
            staffOrderTaskService.updateStaffTaskNum(sotN.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.PRIORITY);
            addApplyHistory(order, sotN.getStaffCode(), null);
            if (updateBorrowNumberState(order, sotN.getStaffCode())) {
                LOGGER.info("借款单号[{}]派给[{}],信审初审重新派发优先件成功", order.getLoanNo(), JSON.toJSONString(sotN));
            } else {
                LOGGER.info("借款单号[{}],信审初审修改重新派发优先件状态失败!", order.getLoanNo());
                throw new BusinessException("信审初审修改重新派发优先件状态失败!");
            }
            // }
        } else {
            LOGGER.info("信审初审优先件重新派发未找到合适初审员重新进入待分配池!");
        }
    }

    /**
     * 正常件派单
     *
     * @param order
     */
    private void currActivieTaskCheckDispatch(ResBMSAutomaticDispatchAttrVO order, String comCreditRating) {
        // 正常队列派单规则:接单能力>饱和度>当前正常队列数最小>等待时间长
        Map<String, Object> mapA = new HashMap<String, Object>();
        mapA.put("productCode", order.getInitProductCode() + "-" + order.getApplyType());
        mapA.put("comCreditRating", "%" + comCreditRating + "%");// 欺诈风险评估
        mapA.put("areaCode", order.getAreaCode());
        StaffOrderTask sotA = staffOrderTaskService.findActiviesTaskOrderService(mapA);
        if (null != sotA) {// 判断是否找到合适人
            //if (executeRuleEngine(order)) {//调用规则引擎
            // 修改正常队列队列数
            staffOrderTaskService.updateStaffTaskNum(sotA.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.ACTIVITY);
            addApplyHistory(order, sotA.getStaffCode(), null);
            if (updateBorrowNumberState(order, sotA.getStaffCode())) {
                LOGGER.info("借款单号[{}]派给 [{}]信审初审派发正常件成功!", order.getLoanNo(), JSON.toJSONString(sotA));
            } else {
                LOGGER.error("借款单号[{}],信审初审修改正常件状态失败!", order.getLoanNo());
                throw new BusinessException("信审初审修改正常件状态失败!");
            }
            // }
        } else {
            LOGGER.info("借款单号[{}],信审初审正常件未找到合适初审员重新进入待分配池!", order.getLoanNo());
        }
    }

    /**
     * 修复接口(借款数据问题)
     *
     * @param order
     * @author dmz
     */
    private void updateLoanIfNewLoanNoStatus(ResBMSAutomaticDispatchAttrVO order) {
        LOGGER.info("信审派单判断有数据问题的申请件  order:{}", JSON.toJSONString(order));
        PersonHistoryLoanVO requests = new PersonHistoryLoanVO();
        requests.setLoanNo(order.getLoanNo());
        requests.setSysCode(sysCode);
        Response<Object> response = firstDispatchExecuter.modifyToNewApplication(requests);
        LOGGER.info("维护借款数据修改队列标识 params:{} result:{}", JSON.toJSONString(requests), JSON.toJSONString(response));
        if (response == null || !response.isSuccess()) {
            throw new BusinessException("维护借款数据修改队列标识错误异常");
        }
    }

    /**
     * 复议件未找到原初审发送邮件
     *
     * @param order
     * @param staffCode
     * @author dmz
     */
    private void sendNotice(ResBMSAutomaticDispatchAttrVO order, String staffCode) {
        try {
            ResEmployeeVO resEmployeeVO = pmsApiService.findByAccount(staffCode);
            ///**申请件渠道标识 applyInput 普通进件 directApplyInput 直通车进件*/
            int flag = "directApplyInput".equals(order.getApplyInputFlag()) ? 1 : 0;// 0-非直通车;1-直通车
            MailVo maliVo = mailService.sendFirstDispatchFailedMail(flag, order.getCustmerName(), order.getCustmerIDNo(), order.getLoanNo(), resEmployeeVO.getName(), resEmployeeVO.getUsercode());
            ApproveMessage am = new ApproveMessage();
            am.setLoanNo(order.getLoanNo());
            am.setTo(StringUtils.join(maliVo.getToList(), ","));
            if (!CollectionUtils.isEmpty(maliVo.getCcList())) {
                am.setCc(StringUtils.join(maliVo.getCcList(), ","));
            }
            am.setFrom(maliVo.getFrom());
            am.setContent(maliVo.getContent());
            am.setRtfState(EnumConstants.RtfState.CSFP.getValue());
            am.setRtfNodeState(EnumConstants.RtfNodeState.CSFPSUBMIT.getValue());
            am.setSubject(maliVo.getSubject());
            approveMessageMapper.save(am);
        } catch (Exception e) {
            LOGGER.error("借款编号[{}] 发送邮件失败", order.getLoanNo(), e);
            throw new BusinessException("发送邮件失败");
        }
    }
}
