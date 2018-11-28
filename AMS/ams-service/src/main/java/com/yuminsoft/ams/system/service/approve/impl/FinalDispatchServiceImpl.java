package com.yuminsoft.ams.system.service.approve.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.dispatch.FinalDispatchExecuter;
import com.ymkj.ams.api.vo.request.apply.ReasonVO;
import com.ymkj.ams.api.vo.request.audit.ReqZsUpdVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAudiUpdVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ApprovalDispatchMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderTaskMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.dao.system.ApproveMessageMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalDispatch;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.ApproveMessage;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.domain.uflo.Task;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.exception.FinalDispatchException;
import com.yuminsoft.ams.system.exception.RecipientNotFoundException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.FinalDispatchService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.service.system.MailService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.SortVo;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.mail.MailVo;

import lombok.SneakyThrows;

@Service
public class FinalDispatchServiceImpl extends BaseService implements FinalDispatchService {
    @Autowired
    private FinalDispatchExecuter finalDispatchExecuter;
    @Autowired
    private StaffOrderTaskService staffOrderTaskService;
    @Autowired
    private StaffOrderTaskMapper staffOrderTaskMapper;
    @Autowired
    private ApplyHistoryService applyHistoryService;
    @Autowired
    private ApplyHistoryMapper applyHistoryMapper;
    @Autowired
    private ApprovalDispatchMapper approvalDispatchMapper;
    @Autowired
    private ApproveMessageMapper approveMessageMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CommonParamService commonParamService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PmsApiService pmsApiService;
    @Autowired
    private RuleEngineService ruleEngineService;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Autowired
    private MailService mailService;

    /**
     * 终审自动派单
     *
     * @param order 申请件
     * @author wulj
     */
    @Override
    public void automaticDispatch(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始处理申请件[{}]，申请件详情:{} ", order.getLoanNo(), JSONObject.toJSONString(order));
        } else {
            LOGGER.info("开始处理申请件[{}]", order.getLoanNo());
        }

        ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());
        if("Y".equals(approvalDispatch.getIsReconsider())){
            // 复议再申请件派单
            processReconsiderOrder(order);
        }else{
            if ("Y".equals(approvalDispatch.getIsHighPass())) {
                if ("Y".equals(approvalDispatch.getIsPrior())) {
                    // 高审优先件
                    processPriorHighPassOrder(order);
                } else {
                    // 高审普通件
                    processNormalHighPassOrder(order);
                }
            } else if ("Y".equals(approvalDispatch.getIsApproval())) {
                if ("Y".equals(approvalDispatch.getIsPrior())) {
                    // 协审优先件
                    processPriorApprovalOrder(order);
                } else {
                    // 协审普通件
                    processNormalApprovalOrder(order);
                }
            } else {
                if ("Y".equals(approvalDispatch.getIsPrior())) {
                    // 终审优先件
                    processPriorFinalOrder(order);
                } else {
                    // 终审普通件
                    processNormalFinalOrder(order);
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("申请件[{}]处理结束", order.getLoanNo());
        }
    }

    /**
     * 复议再申请件
     * @param order
     */
    private void processReconsiderOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为复议再申请件", order.getLoanNo());

        Map<String, Object> params = Maps.newHashMap();
        params.put("loanNo", order.getLoanNo());
        params.put("rtfState",  EnumConstants.RtfState.ZSFP.getValue());
        params.put("rtfNodeState", EnumConstants.RtfNodeState.XSCSSUBMIT.getValue());
        ApproveMessage msg = approveMessageMapper.findOne(params);
        // 如果已经发送过邮件，则系统不做重复处理，由客户手动改派
        if(msg == null){
            // 获取拒绝操作人
            List<String> rtfStateList =  Lists.newArrayList();
            rtfStateList.add(EnumConstants.RtfState.XSZS.getValue());
            rtfStateList.add(EnumConstants.RtfState.ZSFP.getValue());

            List<String> rtfNodeStateList = Lists.newArrayList();
            rtfNodeStateList.add(EnumConstants.RtfNodeState.XSZSREJECT.getValue());
            rtfNodeStateList.add(EnumConstants.RtfNodeState.ZSFPREJECT.getValue());

            List<SortVo.Order> sortList = Lists.newArrayList(new SortVo.Order(SortVo.Direction.ASC,"created_date"));

            ApplyHistory applyHistory = applyHistoryService.getByLoanNoAndRtfStateInAndRtfNodeStateIn(order.getReconsiderLoanNo(), rtfStateList, rtfNodeStateList, sortList);
            String staffCode = applyHistory.getCreatedBy();

            params = Maps.newHashMap();
            params.put("staffCode", staffCode);   // 最近一次终审拒绝人员Code
            params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());// 终审级别
            params.put("ifAccept", "Y"); // 是否接单
            params.put("status", "0");   // 员工状态
            StaffOrderTask staffTask = staffOrderTaskMapper.findOne(params);

            if(null != staffTask){
                // 开始派单
                assignOrder(staffTask, order);
            } else {
                // 发送邮件通知
                ResEmployeeVO employee = pmsApiService.findEmpByUserCode(staffCode);
                sendEmail(order.getLoanNo(), applyHistory.getName(), applyHistory.getIdNo(), employee.getName(), staffCode);
                LOGGER.info("复议再申请件[{}]未找到终审员", order.getLoanNo());

            }
        }else{
            LOGGER.info("复议再申请件[{}]已发送过邮件，请手工改派", order.getLoanNo());
        }
    }

    /**
     * 发送邮件
     * @param loanNo
     * @param name
     * @param idNo
     * @param staffName
     * @param staffCode
     */
    private void sendEmail(String loanNo, String name, String idNo, String staffName, String staffCode){
        try{
            // 发送邮件通知相关负责人
            MailVo msgVo = mailService.sendFinalDispatchFailedMail(name, idNo, loanNo, staffName, staffCode);

            // 保存邮件发送历史
            ApproveMessage msg = new ApproveMessage();
            msg.setLoanNo(loanNo);
            msg.setSubject(msgVo.getSubject());
            msg.setFrom(msgVo.getFrom());
            msg.setTo(StringUtils.join(msgVo.getToList(), ","));
            if(!CollectionUtils.isEmpty(msgVo.getCcList())){
                msg.setCc(StringUtils.join(msgVo.getCcList(), ","));
            }
            msg.setContent(msgVo.getContent());
            msg.setRtfState(EnumConstants.RtfState.ZSFP.getValue());
            msg.setRtfNodeState(EnumConstants.RtfNodeState.XSCSSUBMIT.getValue());
            msg.setCreatedDate(new Date());
            msg.setLastModifiedDate(new Date());
            approveMessageMapper.save(msg);
        }catch (RecipientNotFoundException ex){
            LOGGER.error("终审复议再申请件邮件发送异常", ex);
        }
    }


    /**
     * 提交高审(优先件)
     *
     * @param order
     * @author wulinjie
     */
    private void processPriorHighPassOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为提交高审优先件", order.getLoanNo());
        String finalPersonCode = getFinalReturnPerson(order.getLoanNo(), 1);

        if (StringUtils.isEmpty(finalPersonCode)) {
            LOGGER.info("申请件[{}]找不到原终审员，分派给其他终审员", order.getLoanNo());
            dispatchToProperLevel(order);

        } else if (StringUtils.isNotEmpty(finalPersonCode) && !pmsApiService.hasRole(finalPersonCode, RoleEnum.FINAL_CHECK_L4.getCode())) {
            // 判断"退回操作人"是否为L4
            LOGGER.info("申请件[{}]退回操作人[{}]没有L4权限，不可接单", order.getLoanNo(), finalPersonCode);
            dispatchToProperLevel(order);

        } else {
            // 判断原终审员是否可以接单
            ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());
            Map<String, Object> params = Maps.newHashMap();                            // sql查询参数
            params.put("staffCode", finalPersonCode);                                // 最近一次终审人员Code
            params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());    // 终审级别
            params.put("ifAccept", "Y");                                            // 是否接单
            params.put("status", "0");                                                // 员工状态
            StaffOrderTask staff = staffOrderTaskMapper.findOne(params);
            if (staff != null && staff.getFinalAuditLevel().equalsIgnoreCase(approvalDispatch.getFinalLevel())) {
                assignOrder(staff, order);
            } else {
                LOGGER.info("申请件[{}]原终审员[{}]无法接单，分派给其他终审员", order.getLoanNo(), finalPersonCode);    // 如果原终审员可以接单(原终审员终审层级 = L4)，则分派给原终审员
                dispatchToProperLevel(order);
            }
        }
    }

    /**
     * 提交高审(普通件)
     *
     * @param order
     * @author wulinjie
     */
    private void processNormalHighPassOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为提交高审普通件", order.getLoanNo());
        dispatchToProperLevel(order);
    }

    /**
     * 优先件(即初审重提) and 提交协审
     *
     * @param order
     * @author wulinjie
     */
    private void processPriorApprovalOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为协审优先件", order.getLoanNo());
        String approvalPersonCode = getFinalReturnPerson(order.getLoanNo(), 2);    // 获取原协审人员

        if (StringUtils.isEmpty(approvalPersonCode)) {
            LOGGER.info("申请件[{}]找不到原协审员，分派给其他协审员", order.getLoanNo());
            dispatchToProperLevel(order);

        } else if (StringUtils.isNotEmpty(approvalPersonCode) && !pmsApiService.hasRole(approvalPersonCode, RoleEnum.FINAL_CHECK_L4.getCode())) {
            LOGGER.info("申请件[{}]退回操作人[]没有L4权限，不可接单", order.getLoanNo(), approvalPersonCode);    // 判断"退回操作人"是否为L4
            dispatchToProperLevel(order);

        } else {
            ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());
            Map<String, Object> params = Maps.newHashMap();// sql查询参数
            params.put("approvalPerson", approvalPersonCode);
            List<StaffOrderTask> staffList = staffOrderTaskMapper.findApprovalStaff(params);

            if (!CollectionUtils.isEmpty(staffList)) {    // 判断原来的协审员是否可以接单
                StaffOrderTask staff = staffList.get(0);
                assignOrder(staff, order);
            } else {
                LOGGER.info("原协审员[{}]无法接单，分派给其他协审员", approvalPersonCode);
                params.put("finalAuditLevel", approvalDispatch.getFinalLevel());                    // 终审层级 L4
                params.put("loanNo", order.getLoanNo());                                            // 历史终审员均不可以接单
                List<String> rtfNodeStatusList = new ArrayList<String>();
                rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue());        // 终审提交终审
                rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());        // 终审提交高审
                rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());    // 终审提交协审
                params.put("rtfNodeStatusList", rtfNodeStatusList);
                StaffOrderTask staff = staffOrderTaskMapper.findPriorityStaffForFinal(params);    // 优先队列
                if (staff != null) {
                    assignOrder(staff, order);    // 分派申请件
                } else {
                    LOGGER.info("申请件[{}]找不到可接单的协审员，进入等待队列", order.getLoanNo());
                }
            }
        }
    }

    /**
     * 优先件(初审重提) and 提交终审
     *
     * @param order
     * @author wulinjie
     */
    private void processPriorFinalOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为终审优先件", order.getLoanNo());

        List<String> finalRoleList = Lists.newArrayList();
        finalRoleList.add(RoleEnum.FINAL_CHECK_L1.getCode());
        finalRoleList.add(RoleEnum.FINAL_CHECK_L2.getCode());
        finalRoleList.add(RoleEnum.FINAL_CHECK_L3.getCode());
        finalRoleList.add(RoleEnum.FINAL_CHECK_L4.getCode());

        String finalPersonCode = getFinalReturnPerson(order.getLoanNo(), 1);

        if (StringUtils.isEmpty(finalPersonCode)) {
            LOGGER.info("申请件[{}]找不到原终审员，分派给其他终审员", order.getLoanNo());
            dispatchToProperLevel(order);

        } else if (StringUtils.isNotEmpty(finalPersonCode) && !pmsApiService.hasAnyRoles(finalPersonCode, finalRoleList)) {
            // 判断"最后操作批量退回的终审员"是否含"终审角色"，否则分派给其他终审员
            LOGGER.info("申请件[{}]退回操作人[{}]没有终审角色，不可接单", order.getLoanNo(), finalPersonCode);
            dispatchToProperLevel(order);

        } else {
            // 判断原终审员是否可以接单，否则分派给其他终审员
            Map<String, Object> params = Maps.newHashMap();                            // sql查询参数
            params.put("staffCode", finalPersonCode);                                // 最近一次终审人员Code
            params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());    // 终审级别
            params.put("ifAccept", "Y");                                            // 是否接单
            params.put("status", "0");                                                // 员工状态
            StaffOrderTask staff = staffOrderTaskMapper.findOne(params);
            if (staff != null) {
                assignOrder(staff, order);
            } else {
                LOGGER.info("申请件[{}]原终审员[{}]无法接单，分派给其他终审员", order.getLoanNo(), finalPersonCode);
                dispatchToProperLevel(order);
            }

        }
    }

    /**
     * 终审提交协审
     *
     * @param order
     * @author wulinjie
     */
    private void processNormalApprovalOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为协审普通件", order.getLoanNo());

        ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());

        Map<String, Object> params = Maps.newHashMap();        // sql查询参数
        params.put("loanNo", order.getLoanNo());
        params.put("finalAuditLevel", approvalDispatch.getFinalLevel());
        // 流程节点状态
        ArrayList<Object> rtfNodeStatusList = Lists.newArrayList();
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue());        // 终审提交终审
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());        // 终审提交高审
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());    // 终审提交协审
        params.put("rtfNodeStatusList", rtfNodeStatusList);
        StaffOrderTask staff = staffOrderTaskMapper.findActivityStaffForFinal(params);        // 正常队列

        if (staff != null) {
            // 分派申请件
            assignOrder(staff, order);

        } else {
            LOGGER.info("申请件[{}]找不到可接单的协审员，进入等待队列", order.getLoanNo());
        }

    }

    /**
     * 普通件(新生件 + 终审提交终审) and 提交终审
     *
     * @param order
     * @author wulinjie
     */
    private void processNormalFinalOrder(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("申请件[{}]为终审普通件", order.getLoanNo());
        dispatchToProperLevel(order);
    }

    /**
     * 分派申请件到对应层级
     *
     * @param order 申请件
     * @author wulinjie
     */
    private void dispatchToProperLevel(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());

        StaffOrderTask staff = null;    // 申请件所属终审员
        // 历史上操作过的流程节点状态
        ArrayList<Object> rtfNodeStatusList = Lists.newArrayList();
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSCSPASS.getValue());                // 初审通过
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.HIGHPASS.getValue());                // 初审提交高审
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue());        // 终审提交终审
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());        // 终审提交高审
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());    // 终审提交协审

        // 获取该申请件历史最高层级
        String highestLevel = applyHistoryMapper.findHighestFinalLevel(order.getLoanNo());
        if (StringUtils.isNotEmpty(highestLevel) && approvalDispatch.getFinalLevel().compareTo(highestLevel) < 0) {
            // 如果 "申请金额所属层级" < "历史最高层级" ，则"申请件所属层级"修改为"历史最高层级"
            approvalDispatch.setFinalLevel(highestLevel);
            saveOrUpdateApprovalDispatch(approvalDispatch);
        }

        // 查找该层级可接单终审员(终审提交终审/终审提交高审/终审提交协审的终审人员不可重复接单)
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("finalAuditLevel", approvalDispatch.getFinalLevel());
        params.put("loanNo", order.getLoanNo());
        params.put("rtfNodeStatusList", rtfNodeStatusList);

        if ("Y".equals(approvalDispatch.getIsPrior())) {
            staff = staffOrderTaskMapper.findPriorityStaffForFinal(params);    // 查找优先队列
        } else {
            staff = staffOrderTaskMapper.findActivityStaffForFinal(params);    // 查找正常队列
        }

        // 没有满足条件的终审员，查找更高级别的终审员
        if (staff == null) {
            LOGGER.info("申请件[{}]在{}层级没有找到满足条件的终审员，", order.getLoanNo(), approvalDispatch.getFinalLevel());

            if (!EnumUtils.FinalLevel.L4.name().equals(approvalDispatch.getFinalLevel())) {    // 如果当前级别是L4，则没必要向上查找
                LOGGER.info("开始查找更高级别的终审员");
                for (EnumUtils.FinalLevel level : EnumUtils.FinalLevel.values()) {    // 迭代所有终审层级，查找更高层级
                    if (approvalDispatch.getFinalLevel().compareTo(level.name()) >= 0) {
                        LOGGER.info("{}级别低，无法接单", level.name());
                        continue;
                    }

                    if ("Y".equals(approvalDispatch.getIsPrior())) { // 优先件
                        params.clear();
                        params.put("loanNo", order.getLoanNo());
                        params.put("finalAuditLevel", level.name());
                        params.put("rtfNodeStatusList", rtfNodeStatusList);
                        staff = staffOrderTaskMapper.findPriorityStaffForFinal(params);    // 查找优先队列

                    } else {
                        List<ApprovalDispatch> finalDispatchPool = approvalDispatchMapper.findWaitPoolWithoutApproval(level.name());    // 待分派池-未分派的"终审件"
                        if (!CollectionUtils.isEmpty(finalDispatchPool)) {    // 待分派池里有更高层级的申请单，不能分派给该层级，继续向上找
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("{}级别含有多个待分派的终审件，无法接单, 待分派池-终审件：{}", level.name(), JSONArray.toJSONString(finalDispatchPool));
                            } else {
                                LOGGER.info("{}级别含有多个待分派的终审件，无法接单", level.name());
                            }
                            continue;
                        }

                        // 如果向上找到了L4层级，并且待分派池里"协审件"提交人数量大于1个，则L4层级终审员均无法接单
                        List<String> approvalPersonList = approvalDispatchMapper.findWaitPoolFinalPersonList();
                        if (level.equals(EnumUtils.FinalLevel.L4) && !CollectionUtils.isEmpty(approvalPersonList)) {
                            if (approvalPersonList.size() > 1) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("{}级别含有多个待分派的协审件，无法接单，提交协审的终审员工号：{}", level.name(), JSONArray.toJSONString(approvalPersonList));
                                } else {
                                    LOGGER.info("{}级别含有多个待分派的协审件，无法接单", level.name());
                                }
                                continue;
                            } else {
                                params.clear();
                                params.put("staffCode", approvalPersonList.get(0));
                                params.put("loanNo", order.getLoanNo());
                                params.put("finalAuditLevel", level.name());
                                params.put("rtfNodeStatusList", rtfNodeStatusList);
                                staff = staffOrderTaskMapper.findActivityStaffForFinal(params);
                            }
                        } else {
                            params.clear();
                            params.put("loanNo", order.getLoanNo());
                            params.put("finalAuditLevel", level.name());
                            params.put("rtfNodeStatusList", rtfNodeStatusList);
                            staff = staffOrderTaskMapper.findActivityStaffForFinal(params);    // 查找正常队列
                        }
                    }

                    if (staff == null) {
                        LOGGER.info("{}层级队无法接单 ", level.name());    // 队列无法接单
                        continue;
                    }

                    LOGGER.info("在{}层级找到可接单的终审员，工号为:{}", staff.getFinalAuditLevel(), staff.getStaffCode());
                    break;
                }
            }

            // 所有终审层级均找不到可接单的终审员
            if (null == staff) {
                throw new FinalDispatchException("找不到可接单的终审员，进入等待队列");
            }
        }

        // 分派申请件
        assignOrder(staff, order);
    }

    /**
     * 初始化终审待分派池
     *
     * @param orders
     * @author wulinjie
     */
    @Override
    public void initOrderPool(List<ResBMSAutomaticDispatchAttrVO> orders) {
        LOGGER.debug("Start 初始化终审待分派池");

        for (ResBMSAutomaticDispatchAttrVO order : orders) {
            // 校验申请件是否有审批金额
            if (order.getAccLmt() == null) {
                LOGGER.warn("申请件[{}]审批金额为空，暂不处理", order.getLoanNo());
                continue;
            }

            // 根据审批金额，查找申请件对应的终审层级
            String finalLevel = getFinalLevel(new BigDecimal(order.getAccLmt()));
            if(StringUtils.isEmpty(finalLevel)){
                LOGGER.warn("申请件[{}]审批金额[{}]找不到对应的终审层级", order.getLoanNo(), order.getAccLmt());
                continue;
            }

            // 申请件当前流程节点状态
            String nodeStatus = order.getRtfNodeStatus();
            // 原终审员code
            String finalPersonCode = order.getFinalPersonCode();
            // 原协审员code
            String approvalPersonCode = order.getApppovalPersonCode();

            ApprovalDispatch approvalDispatch = new ApprovalDispatch();
            approvalDispatch.setLoanNo(order.getLoanNo());
            approvalDispatch.setFinalLevel(finalLevel);
            approvalDispatch.setIsPrior("N");
            approvalDispatch.setIsHighPass("N");
            approvalDispatch.setIsApproval("N");
            approvalDispatch.setIsReconsider("N");
            approvalDispatch.setStatus("WAIT");
            approvalDispatch.setFinalPerson(finalPersonCode);

            // 是否复议再申请件
            if(isReconsiderLoan(order)){
                approvalDispatch.setIsReconsider(AmsConstants.YES);
            }

            // 判断是否为"初审通过"，并且为"终审优先件"
            if (order.getzSIfNewLoanNo().equals(EnumConstants.ifNewLoanNo.NOLOANNO.getValue())) {
                approvalDispatch.setIsPrior("Y");
            }

            if(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue().equals(nodeStatus)){
                // 终审提交终审，视为"终审普通件"
                approvalDispatch.setIsPrior("N");

            } else if (EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(nodeStatus)) {
                // 初审提交高审，层级直接为L4
                approvalDispatch.setIsHighPass("Y");
                approvalDispatch.setFinalLevel(EnumUtils.FinalLevel.L4.name());

            } else if(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue().equals(nodeStatus)){
                // 终审提交高审，层级直接为L4，且视为普通件
                approvalDispatch.setIsHighPass("Y");
                approvalDispatch.setIsPrior("N");
                approvalDispatch.setFinalLevel(EnumUtils.FinalLevel.L4.name());

            }else if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(nodeStatus)) {
                // 终审提交协审（注意：协审只能提交一次），并且第一次提交协审都视为"终审普通件"
                approvalDispatch.setIsApproval("Y");
                approvalDispatch.setIsPrior("N");
                approvalDispatch.setFinalLevel(EnumUtils.FinalLevel.L4.name());

            } else if (StringUtils.isNotEmpty(approvalPersonCode)) {
                // 协审人员不为空，则为协审件，且层级为L4
                approvalDispatch.setIsApproval("Y");
                approvalDispatch.setFinalLevel(EnumUtils.FinalLevel.L4.name());

            }

            saveOrUpdateApprovalDispatch(approvalDispatch);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("申请件[{}]初始化完成, {}", order.getLoanNo(), JSONObject.toJSONString(approvalDispatch));
            }
        }

        // 删除待分派池里不存在的申请件
        List<String> loanNoList = (List<String>)CollectionUtils.collect(orders, new Transformer() {
            @Override
            public Object transform(Object input) {
                return ((ResBMSAutomaticDispatchAttrVO)input).getLoanNo();
            }
        });

        approvalDispatchMapper.deleteByLoanNoNotIn(loanNoList);

        LOGGER.debug("End 初始化终审待分派池");
    }

    /**
     * 销毁终审待分派池
     */
    @Override
    public void destroyOrderPool() {
        LOGGER.debug("销毁终审待分派池");
        approvalDispatchMapper.deleteByStatus("DONE");
    }

    /**
     * 根据申请金额，查找对应的终审层级(L1,L2,L3,L4)
     * @param accLimit 申请金额
     * @return 终审层级
     * @author wulinjie
     */
    private String getFinalLevel(BigDecimal accLimit) {
        SysParamDefine properLevel = null;
        String paramType = "FinalAuditLevel";
        List<SysParamDefine> finalLevelList = null;

        // 读取终审层级
        Object value = redisUtil.get(EnumUtils.CacheType.SYSTEM, paramType);
        if (value != null) {
            finalLevelList = (List<SysParamDefine>) value;
        } else {
            finalLevelList = commonParamService.findByParamType(paramType);
            redisUtil.set(EnumUtils.CacheType.SYSTEM, paramType, finalLevelList);
        }

        // 终审层级不存在
        if (CollectionUtils.isEmpty(finalLevelList)) {
            throw new RuntimeException("系统参数审批未找到，请检查系统参数-配置金额");
        }

        for (SysParamDefine sysParamDefine : finalLevelList) {
            String paramValue = sysParamDefine.getParamValue();
            String[] auditLimits = StringUtils.split(paramValue, "-");
            BigDecimal minAuditLimit = new BigDecimal(auditLimits[0]);
            BigDecimal maxAuditLimit = new BigDecimal(auditLimits[1]);
            // 如果审批金额上限小于审批金额下限，则互换审批金额
            if (maxAuditLimit.compareTo(minAuditLimit) == -1) {
                BigDecimal tempAuditLimit = minAuditLimit;
                maxAuditLimit = minAuditLimit;
                minAuditLimit = tempAuditLimit;
            }
            // 判断审批金额所属终审层级
            if (accLimit.compareTo(minAuditLimit) > 0 && accLimit.compareTo(maxAuditLimit) <= 0) { // 左开右闭
                LOGGER.debug("审批金额 [ " + accLimit + " ] 所属层级{}", sysParamDefine.getParamKey());
                properLevel = sysParamDefine;
                break;
            }
        }

        if(properLevel != null){
            return properLevel.getParamKey();
        }

        return "";
    }

    /**
     * 调用规则引擎（目前终审派单只有拒绝和通过两个动作）
     *
     * @param order 申请件信息
     * @return 调用结果(通过 or 拒绝)
     * @author wulj
     */
    private boolean executeRuleEngine(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        boolean action = false;

        Result<RuleEngineVO> result = ruleEngineService.executeRuleEngine(order.getLoanNo(), EnumUtils.ExecuteTypeEnum.XSZS06);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("终审派单调用规则引擎 response:{}", JSONObject.toJSONString(result));
        }

        if (result.getSuccess()) {
            if (EnumUtils.EngineType.PASS.getValue().equals(result.getFirstMessage())) {
                LOGGER.debug("终审派单调用规则引擎返回通过");
                action = true;
            } else if (EnumUtils.EngineType.REJECT.getValue().equals(result.getFirstMessage())) {// 拒绝
                // 执行拒绝操作
                action = false;
                Result<ReasonVO> resultReason = bmsLoanInfoService.queryReason(result.getData().getRejectCode());
                if (!resultReason.success()) {
                    throw new FinalDispatchException("规则引擎拒绝调用原因码失败!");
                }

                ReasonVO reasonVO = resultReason.getData();
                String rejectCode = "";
                if (StringUtils.isNotEmpty(reasonVO.getFirstLevelReasonCode())) {
                    rejectCode = reasonVO.getFirstLevelReasonCode();
                }
                if (StringUtils.isNotEmpty(reasonVO.getTwoLevelReasonCode())) {
                    rejectCode = reasonVO.getFirstLevelReasonCode() + "-" + reasonVO.getTwoLevelReasonCode();
                }
                
                // 添加日志
                addApplyHistory(order, null, rejectCode);
                
                //修改工作流为拒绝
                Long taskId = taskMapper.findTaskIdByBusinessId(order.getLoanNo());
                if (null != taskId && taskId.compareTo(new Long(0)) > 0) {
                	taskService.deny(taskId);
                }
                
                ReqZsUpdVO reqZsUpdVO = new ReqZsUpdVO();
                reqZsUpdVO.setLoanNo(order.getLoanNo());
                reqZsUpdVO.setRtfStatus(EnumConstants.RtfState.ZSFP.getValue());
                reqZsUpdVO.setRtfNodeStatus(EnumConstants.RtfNodeState.ZSFPREJECT.getValue());
                reqZsUpdVO.setVersion(order.getVersion());
                reqZsUpdVO.setOperatorIp(WebUtils.getLocalIpAddress());
                reqZsUpdVO.setFirstLevelReasonCode(reasonVO.getFirstLevelReasonCode());
                reqZsUpdVO.setFirstLevelReasons(reasonVO.getFirstLevelReason());
                reqZsUpdVO.setTwoLevelReasonCode(reasonVO.getTwoLevelReasonCode());
                reqZsUpdVO.setTwoLevelReasons(reasonVO.getTwoLevelReason());
                boolean ruleAction = bmsLoanInfoService.ruleEngineReject(reqZsUpdVO);
                if (ruleAction) {
                	LOGGER.info("终审派单调用规则引擎返回拒绝，继而调用api规则引擎拒绝接口成功!");
                } else {
                    throw new BusinessException("规则引擎拒绝调用失败!");
                }

            } else {
                throw new FinalDispatchException("调用规则引擎异常");
            }
        }

        return action;
    }

    /**
     * 申请件更新分派人
     *
     * @param staff 员工队列
     * @param order 申请件
     * @author wulinjie
     */
    private void assignOrder(StaffOrderTask staff, ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException {
        LOGGER.info("开始更新申请件: {}, 终审员: {}", order.getLoanNo(), staff.getStaffCode());

        ApprovalDispatch approvalDispatch = approvalDispatchMapper.findByLoanNo(order.getLoanNo());

        // 调用规则引擎
        boolean action = executeRuleEngine(order);
        if(!action){
        	LOGGER.info("终审申请件[{}]分派失败，失败原因：{}", order.getLoanNo(), "调用规则引擎不通过");
        	//由于事务配置是所有异常都回滚(相见applicationContent.xml中)，故这个异常会导致executeRuleEngine中的事务回滚，所以不抛出这个异常，改用日志输出
            //throw new FinalDispatchException("调用规则引擎不通过");
        } else {
        	// 校验工作流节点是否正确
            Task task = taskMapper.findByBusinessId(order.getLoanNo());
            if (null == task) {
                throw new FinalDispatchException("此单没有查到对应任务号,返回待分配池,等待下次分配");
            }

            // 校验是否已被改派
            String assignee = task.getAssignee();
            if (StringUtils.isNotEmpty(assignee) && !"finalAutoDispatch".equals(assignee)) {
                throw new FinalDispatchException("此单已被改派, ulfo_task.id:" + task.getId() + ", assignee:" + assignee);
            }

            // 1.更新终审员队列
            if ("Y".equals(approvalDispatch.getIsPrior())) {
                // 更新优先队列
                staffOrderTaskService.updateStaffTaskNum(staff.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.PRIORITY);
            } else {
                // 更新普通队列
                staffOrderTaskService.updateStaffTaskNum(staff.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.ACTIVITY);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("更新终审员队列信息, staffCodeTask:{}", JSONObject.toJSONString(staff));
            } else {
                LOGGER.info("更新终审员队列信息, staffCode:{}", staff.getStaffCode());
            }

            // 2.记录日志
            ResEmployeeVO emp = pmsApiService.findEmpByUserCode(staff.getStaffCode());
            addApplyHistory(order, emp.getUsercode(), null);

            // 3.调用工作流进行派单
            String result = taskService.changeTask(task.getId(), staff.getStaffCode());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("更新工作流, result:{}", result);
            } else {
                LOGGER.info("更新工作流");
            }

            // 4更新待分配池中当前层级的申请件数量
            approvalDispatch.setStatus("DONE");    // 已分派
            approvalDispatchMapper.update(approvalDispatch);
            
            //5.更新借款系统状态
            //最后调用api派单更新接口，如果调用失败，就抛出异常，回滚本地事务
            ReqZsUpdVO request = new ReqZsUpdVO();

            String approvalPersonCode = order.getApppovalPersonCode();    // 协审员code
            String nodeStatus = order.getRtfNodeStatus();
            
            request.setLoanNo(order.getLoanNo());    // 申请件编号
            if ("Y".equals(approvalDispatch.getIsPrior())) {
                request.setZsIfNewLoanNo(EnumConstants.ifNewLoanNo.NOLOANNO.getValue());// 新生件标识-优先件
            } else {
                request.setZsIfNewLoanNo(EnumConstants.ifNewLoanNo.NEWLOANNO.getValue());// 新生件标识-普通件
            }

            request.setRtfNodeStatus(EnumConstants.RtfNodeState.XSZSASSIGN.getValue());

            if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(nodeStatus) || StringUtils.isNotEmpty(approvalPersonCode)) {
                request.setApppovalPersonCode(staff.getStaffCode());    // 如果"终审提交协审"，或者"协审退回后，初审再次提交协审"
            } else {
                request.setZsPersonCode(staff.getStaffCode());            // 更新终审员
            }

            request.setAllotDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
            request.setOperatorCode("系统");
            request.setVersion(order.getVersion());
            request.setSysCode(sysCode);
            request.setOperatorIp(WebUtils.getLocalIpAddress());
            request.setRemark("分派至：" + emp.getName());
            
            ResBMSAudiUpdVo response = finalDispatchExecuter.dispatch(request);
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("调用借款系统更新申请件状态，request:{}, response:{}", JSONObject.toJSONString(request), JSONObject.toJSONString(response));
            } else {
            	LOGGER.info("调用借款系统更新申请件[{}]状态", order.getLoanNo());
            }
            
            if (null == response || !response.isSuccess()) {
            	throw new BusinessException("借款系统申请件更新失败");
            }

            LOGGER.info("申请件[{}]分派成功", order.getLoanNo());
        }
        
    }

    /**
     * 维护历史记录
     *
     * @param order      申请件信息
     * @param staffCode  员工工号
     * @param rejectCode 规则引擎拒绝原因码
     */
    private void addApplyHistory(ResBMSAutomaticDispatchAttrVO order, String staffCode, String rejectCode) {
        ApplyHistory applyHistory = new ApplyHistory();
        //更新协审
        if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(order.getRtfNodeStatus()) || StringUtils.isNotEmpty(order.getApppovalPersonCode())) {
            applyHistory.setApprovalPerson(staffCode);
        } else {//更新终审
            applyHistory.setFinalPerson(staffCode);
        }
        applyHistory.setLoanNo(order.getLoanNo());
        applyHistory.setName(order.getCustmerName());
        applyHistory.setIdNo(order.getCustmerIDNo());
        applyHistory.setProNum(order.getLoanNo());

        if (StringUtils.isNotEmpty(rejectCode)) {
            applyHistory.setProName("规则引擎拒绝");
            applyHistory.setRtfNodeState(EnumConstants.RtfNodeState.ZSFPREJECT.getValue());
            applyHistory.setRtfState(EnumConstants.RtfState.ZSFP.getValue());
            applyHistory.setRemark("规则引擎拒绝");
            applyHistory.setRefuseCode(rejectCode);
            applyHistory.setAutoRefuse(EnumUtils.YOrNEnum.Y.getValue());
        } else {
            applyHistory.setProName("信审终审已分派");
            applyHistory.setRtfNodeState(EnumConstants.RtfNodeState.XSZSASSIGN.getValue());
            applyHistory.setRtfState(EnumConstants.RtfState.XSZS.getValue());
            applyHistory.setRemark("终审自动分派至" + staffCode);

        }
        applyHistory.setCreatedDate(new Date());
        applyHistory.setLastModifiedDate(new Date());

        applyHistoryService.save(applyHistory);

    }

    /**
     * 保存或更新终审待分派池信息
     *
     * @param approvalDispatch
     * @return
     * @author wulj
     */
    @SneakyThrows
    private ApprovalDispatch saveOrUpdateApprovalDispatch(ApprovalDispatch approvalDispatch) {
        ApprovalDispatch existsApprovalDispatch = approvalDispatchMapper.findByLoanNo(approvalDispatch.getLoanNo());
        if (existsApprovalDispatch == null) {
            existsApprovalDispatch = new ApprovalDispatch();
            BeanUtil.copyPropertiesIgnoreNull(existsApprovalDispatch, approvalDispatch);
            existsApprovalDispatch.setCreatedDate(new Date());
            existsApprovalDispatch.setLastModifiedDate(new Date());
            approvalDispatchMapper.insert(existsApprovalDispatch);
        } else {
            BeanUtil.copyPropertiesIgnoreNull(existsApprovalDispatch, approvalDispatch);
            existsApprovalDispatch.setLastModifiedDate(new Date());
            approvalDispatchMapper.update(existsApprovalDispatch);
        }

        return existsApprovalDispatch;
    }

    /**
     * 获取原终/协审的人员工号
     *
     * @param loanNo 借款编号
     * @param flag   标志位（1-终审,2-协审）
     * @return 原终/协审的人员工号
     * @author wulj
     */
    private String getFinalReturnPerson(String loanNo, int flag) {
        // 获取该申请件最近操作"终审退回"的人员工号
        List<SortVo.Order> sortList = Lists.newArrayList(new SortVo.Order(SortVo.Direction.DESC,"created_date"));

        List<String> rtfStateList = Lists.newArrayList();
        rtfStateList.add(EnumConstants.RtfState.XSZS.getValue());

        List<String> rtfNodeStatusList = Lists.newArrayList();
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSRTNCS.getValue());
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSRETURN.getValue());
        rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue());
        ApplyHistory lastApplyHistory = applyHistoryService.getByLoanNoAndRtfStateInAndRtfNodeStateIn(loanNo, rtfStateList, rtfNodeStatusList, sortList);

        String finalPersonCode;
        if (flag == 1) {    // 终审人员
            finalPersonCode = lastApplyHistory.getFinalPerson();
        } else {                // 协审人员
            finalPersonCode = lastApplyHistory.getApprovalPerson();
        }

        // 如果原终审员(or 协审员)不存在，则取终审退回操作人
        if (StringUtils.isEmpty(finalPersonCode)) {
            LOGGER.info("申请件[{}]原{}审人员为null", loanNo, flag == 1 ? "终" : "协");
            finalPersonCode = lastApplyHistory.getCreatedBy();
        }

        if (StringUtils.isNotEmpty(finalPersonCode)) {
            // 判断该工号是否操作过"终审提交终审"、"终审提交高审"、"终审提交协审"
            rtfNodeStatusList = Lists.newArrayList();
            rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSCSPASS.getValue());                // 初审通过
            rtfNodeStatusList.add(EnumConstants.RtfNodeState.HIGHPASS.getValue());                // 初审提交高审
            rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue());        // 终审提交终审
            rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());        // 终审提交高审
            rtfNodeStatusList.add(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());    // 终审提交协审
            List<ApplyHistory> applyHistoryList = applyHistoryService.getByLoanNoAndUserCodeAndRtfStateAndRtfNodeStateIn(loanNo, finalPersonCode, EnumConstants.RtfState.XSZS.getValue(), rtfNodeStatusList);
            if (!CollectionUtils.isEmpty(applyHistoryList)) {
                LOGGER.info("申请件[{}]退回人员工号[{}]为历史提交操作人", loanNo, finalPersonCode);
                finalPersonCode = null;
            }
        }

        return finalPersonCode;
    }

    /**
     * 是否符合终审复议再申请件
     * @param order 订单
     * @return true-是,false-否
     */
    private boolean isReconsiderLoan(ResBMSAutomaticDispatchAttrVO order){
        String ifReconsiderUser = order.getIfReconsiderUser();

        if(StringUtils.isEmpty(ifReconsiderUser) || AmsConstants.NO.equals(ifReconsiderUser)){
            return false;
        }

        // 判断是否首次进入终审派单环节
        if(!isNewLoan(order)){
            LOGGER.info("申请件[{}]不是首次进入终审派单环节，不符合复议再申请规则", order.getLoanNo());
            return false;
        }

        // reconsiderLoanNo字段不能为空
        if(StringUtils.isEmpty(order.getReconsiderLoanNo())){
            LOGGER.info("申请件[{}]上一笔拒绝的借款编号(reconsiderLoanNo字段)不存在，不符合复议再申请规则", order.getLoanNo());
            return false;
        }

        // 查找该客户最近一次终审环节被拒绝的审批历史
        List<String> rtfStateList =  Lists.newArrayList();
        rtfStateList.add(EnumConstants.RtfState.XSZS.getValue());
        rtfStateList.add(EnumConstants.RtfState.ZSFP.getValue());

        List<String> rtfNodeStateList = Lists.newArrayList();
        rtfNodeStateList.add(EnumConstants.RtfNodeState.XSZSREJECT.getValue());
        rtfNodeStateList.add(EnumConstants.RtfNodeState.ZSFPREJECT.getValue());

        List<SortVo.Order> sortList = Lists.newArrayList(new SortVo.Order(SortVo.Direction.ASC,"created_date"));
        ApplyHistory applyHistory = applyHistoryService.getByLoanNoAndRtfStateInAndRtfNodeStateIn(order.getReconsiderLoanNo(), rtfStateList, rtfNodeStateList, sortList);
        if(applyHistory == null){
            LOGGER.info("申请件[{}]终审办理拒绝记录不存在，不符合复议再申请规则", order.getLoanNo());
            return false;
        }

        // 是否属于系统自动拒绝
        if(AmsConstants.YES.equals(applyHistory.getAutoRefuse())){
            LOGGER.info("申请件[{}]属于系统自动拒绝，不符合复议再申请规则", order.getLoanNo());
            return false;
        }

        // 员工在平台系统不存在
        String staffCode = applyHistory.getCreatedBy();
        if(StringUtils.isEmpty(staffCode)){
            LOGGER.info("申请件[{}]最近操作拒绝人工号不存在，不符合复议再申请规则", order.getLoanNo());
            return false;
        }

        ResEmployeeVO employee = pmsApiService.findEmpByUserCode(staffCode);
        if( employee == null ){
            LOGGER.info("申请件[{}]最近操作拒绝的员工[{}]不存在，不符合复议再申请规则", order.getLoanNo(), staffCode);
            return false;
        }

        return true;
    }

    /**
     * 是否新生件(首次进件)
     * @param order 订单
     * @return true-是,false-否
     */
    private boolean isNewLoan(ResBMSAutomaticDispatchAttrVO order){
        if(order.getzSIfNewLoanNo().equals(EnumConstants.ifNewLoanNo.NEWLOANNO.getValue())){
            return true;
        }

        return false;
    }
}