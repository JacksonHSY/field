package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.reconsider.ReconsiderExecuter;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsiderDispatchUpdate;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderDispatch;
import com.ymkj.base.core.biz.api.message.Response;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ReconsiderHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ReconsiderStaffMapper;
import com.yuminsoft.ams.system.dao.system.ApproveMessageMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.domain.system.ApproveMessage;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ReconsiderDispatchService;
import com.yuminsoft.ams.system.service.system.MailService;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.mail.MailVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复议自动派单
 * Created by YM10106 on 2018/6/20.
 */
@Service
public class ReconsiderDispatchServiceImpl implements ReconsiderDispatchService {
    private static Logger LOGGER = LoggerFactory.getLogger(ReconsiderDispatchServiceImpl.class);
    @Autowired
    private ReconsiderExecuter reconsiderExecuter;
    @Autowired
    private ReconsiderStaffMapper reconsiderStaffMapper;
    @Autowired
    private ReconsiderHistoryMapper reconsiderHistoryMapper;
    @Autowired
    private ApproveMessageMapper approveMessageMapper;
    @Autowired
    private MailService mailService;

    // 记录3个等级上一次分派指定的人key 层级value 员工号
    private static Map<String, String> saveDispatchUser = new HashMap<String, String>();

    /**
     * 复议派单查询
     *
     * @return
     */
    @Override
    public List<ResReconsiderDispatch> getReconsiderDispatchList() {
        List<ResReconsiderDispatch> list = new ArrayList<ResReconsiderDispatch>();
        Response<List<ResReconsiderDispatch>> response = reconsiderExecuter.getDispatchList();
        if (null != response && response.isSuccess()) {
            if (null != response.getData() && response.getData().size() > 0) {
                list = response.getData();
            }
        } else {
            LOGGER.error("复议判断查询异常 result:{}", JSON.toJSONString(response));
        }
        return list;
    }


    /**
     * 自动分派
     *
     * @param order
     */
    @Override
    public void automaticDispatch(ResReconsiderDispatch order) {
        String ruleLevel = Strings.isNotEmpty(order.getXsReviewPersonLevel()) ? order.getXsReviewPersonLevel() : "F1";
        LOGGER.info("复议派单借款编号[{}] 所属层级[{}] 状态[{}]", order.getLoanNo(), ruleLevel, order.getXsStatus());
        // 判断是否是退回件
        if ("2".equals(order.getXsStatus())) {
            findOldStaff(order.getXsReviewPersonCode(), order, ruleLevel);
        } else if ("F3".equals(ruleLevel) && "1".equals(order.getXsStatus())) {
            ReconsiderHistory reconsiderHistory = reconsiderHistoryMapper.findReconsiderHistoryByLevelTwoHandle(order.getLoanNo());
            findOldStaff(reconsiderHistory.getOperator(), order, "F2");
        } else {
            // F1 提交
            if ("F1".equals(ruleLevel) && "1".equals(order.getXsStatus())) {
                ruleLevel = "F2";
            }
            ReconsiderStaff reconsiderStaffNew = getReConsiderStaff(ruleLevel);
            if (null != reconsiderStaffNew) {
                // 添加复议日志 更新复议信息
                updateReconsid(reconsiderStaffNew, order, ruleLevel);
            } else {
                sendEmail(order, ruleLevel);
            }
        }

        // 记录复议派单日志
        // 修改借款
    }

    /**
     * 找原复议人员
     *
     * @param oldStaffCode
     * @param order
     * @param ruleLevel
     */
    private void findOldStaff(String oldStaffCode, ResReconsiderDispatch order, String ruleLevel) {
        // 判断原初审是否可接单
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("staffCode", oldStaffCode);
        map.put("status", 0);
        map.put("ifAccept", EnumUtils.YOrNEnum.Y.getValue());
        ReconsiderStaff reconsiderStaffOld = reconsiderStaffMapper.findOne(map);
        if (null != reconsiderStaffOld && reconsiderStaffOld.getRuleLevel().indexOf(ruleLevel) >= 0) {
            LOGGER.info("原复议人员[{}]可接单 当前层级[{}]", oldStaffCode, reconsiderStaffOld.getRuleLevel());
            // 添加复议日志 更新复议信息
            updateReconsid(reconsiderStaffOld, order, ruleLevel);
        } else {
            LOGGER.info("原复议人员[{}]不可接单重新派单", oldStaffCode);
            ReconsiderStaff reconsiderStaffNew = getReConsiderStaff(ruleLevel);
            if (null != reconsiderStaffNew) {
                // 添加复议日志 更新复议信息
                updateReconsid(reconsiderStaffNew, order, ruleLevel);
            } else {
                sendEmail(order, ruleLevel);
            }
        }
    }

    /**
     * 添加复议日志
     *
     * @param reconsiderStaff
     * @param loanNo
     * @param ruleLevel
     */
    private void saveReconsiderHistory(ReconsiderStaff reconsiderStaff, String loanNo, String ruleLevel) {
        ReconsiderHistory reconsiderHistory = new ReconsiderHistory();
        reconsiderHistory.setLoanNo(loanNo);
        reconsiderHistory.setOperator("系统");
        reconsiderHistory.setReconsiderLevel(ruleLevel);
        reconsiderHistory.setRemark("复议分派至:" + reconsiderStaff.getStaffName());
        reconsiderHistory.setReconsiderNode(EnumConstants.ReconsiderLink.FP.getCode());
        reconsiderHistory.setReconsiderOperator(reconsiderStaff.getStaffCode());
        reconsiderHistoryMapper.insert(reconsiderHistory);
    }

    /**
     * 根据级别查找合适的复议人员(根据工号一次分派)
     *
     * @param ruleLeve
     * @return
     */
    private ReconsiderStaff getReConsiderStaff(String ruleLeve) {
        // 获取上一个分派人员
        String staffCode = "";
        if (saveDispatchUser.containsKey(ruleLeve)) {
            staffCode = saveDispatchUser.get(ruleLeve);
        } else {
            ReconsiderHistory reconsiderHistory = reconsiderHistoryMapper.findLastDispathByLevel(ruleLeve);
            if (null != reconsiderHistory) {
                staffCode = reconsiderHistory.getReconsiderOperator();
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("level", ruleLeve);
        map.put("userCode", staffCode);
        return reconsiderStaffMapper.findDispatchUser(map);
    }

    /**
     * 更新复议信息
     *
     * @param reconsiderStaff
     * @param order
     * @param ruleLevel
     */
    private void updateReconsid(ReconsiderStaff reconsiderStaff, ResReconsiderDispatch order, String ruleLevel) {
        ReqReconsiderDispatchUpdate req = new ReqReconsiderDispatchUpdate();
        req.setLoanNo(order.getLoanNo());
        req.setVersion(order.getVersion());
        req.setXsReviewPersonCode(reconsiderStaff.getStaffCode());
        req.setXsReviewPersonLevel(ruleLevel);
        req.setXsReviewPersonName(reconsiderStaff.getStaffName());
        req.setRemark("复议分派至:" + reconsiderStaff.getStaffName());
        Response<Boolean> response = reconsiderExecuter.dispatch(req);
        saveReconsiderHistory(reconsiderStaff, order.getLoanNo(), ruleLevel);// 添加日志
        LOGGER.info("复议分派修改状态 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && response.getData()) {
            saveDispatchUser.put(ruleLevel, reconsiderStaff.getStaffCode());// 更新保存上一次分派人
        } else {
            throw new BusinessException("复议分派修改状态异常");
        }
    }

    /**
     * 复议发送邮件
     *
     * @param order
     * @param ruleLevel
     */
    private void sendEmail(ResReconsiderDispatch order, String ruleLevel) {
        Map<String, Object> mapAM = new HashMap<String, Object>();
        mapAM.put("rtfState", "XSFY");
        mapAM.put("rtfNodeState", "FYFP-SUBMIT");
        mapAM.put("loanNo", order.getLoanNo());
        ApproveMessage approveMessage = approveMessageMapper.findOne(mapAM);
        if (null == approveMessage) {
            // 首次未找到合适复议人员发送邮件
            LOGGER.info("首次未找到合适复议人员发送邮件[{}]", order.getLoanNo());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ruleLevel", EnumConstants.ReconsiderLink.F4.getCode());
            List<ReconsiderStaff> reconsiderStaffsList = reconsiderStaffMapper.findReconsiderStaffByRuleLevel(map);
            if (CollectionUtils.isEmpty(reconsiderStaffsList)) {
                LOGGER.error("复议派单找不到邮件接收人");
            } else {
                try {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("name", order.getName());
                    params.put("loanNo", order.getLoanNo());
                    params.put("idNo", order.getIdNo());
                    String[] recipients = new String[reconsiderStaffsList.size()];
                    for (int i = 0; i < reconsiderStaffsList.size(); i++) {
                        recipients[i] = reconsiderStaffsList.get(i).getStaffEmail();
                    }
                    MailVo maliVo = mailService.sendMail("复议申请件分派", recipients, null, "reconsiderDispatchFailed.ftl", params);
                    ApproveMessage am = new ApproveMessage();
                    am.setLoanNo(order.getLoanNo());
                    am.setTo(StringUtils.join(maliVo.getToList(), ","));
                    if (!com.yuminsoft.ams.system.util.CollectionUtils.isEmpty(maliVo.getCcList())) {
                        am.setCc(StringUtils.join(maliVo.getCcList(), ","));
                    }
                    am.setFrom(maliVo.getFrom());
                    am.setContent(maliVo.getContent());
                    am.setRtfState("XSFY");
                    am.setRtfNodeState("FYFP-SUBMIT");
                    am.setSubject(maliVo.getSubject());
                    approveMessageMapper.save(am);
                } catch (Exception e) {
                    LOGGER.error("借款编号[{}] 发送邮件失败", order.getLoanNo(), e);
                    throw new BusinessException("发送邮件失败");
                }
            }
        }
    }
}
