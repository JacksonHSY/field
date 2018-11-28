package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.reconsider.ReconsiderExecuter;
import com.ymkj.ams.api.vo.request.reconsider.*;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApprove;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApproved;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderReassignment;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ReconsiderHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ReconsiderStaffMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ReconsiderationService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.ReconsiderationVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReconsiderationServiceImpl implements ReconsiderationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReconsiderationServiceImpl.class);
    @Autowired
    private ReconsiderExecuter reconsiderExecuter;
    @Value("${sys.code}") // 系统编码
    private String sysCode;
    @Autowired
    private ReconsiderHistoryMapper reconsiderHistoryMapper;
    @Autowired
    private ReconsiderStaffMapper reconsiderStaffMapper;

    /**
     * 查询复议待办任务
     *
     * @param req
     * @return
     */
    @Override
    public ResponsePage<ResReconsiderApprove> getReconsiderUnfinished(ReqReconsiderApprove req) {
        ResponsePage<ResReconsiderApprove> page = new ResponsePage<ResReconsiderApprove>();
        String staffCode = ShiroUtils.getAccount();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("staffCode", staffCode);
        map.put("status", EnumUtils.DisplayEnum.ENABLE.getValue());
        ReconsiderStaff reconsiderStaff = reconsiderStaffMapper.findOne(map);
        if (null != reconsiderStaff) {
            req.setSysCode(sysCode);
            req.setXsReviewPersonCode(staffCode);
            req.setXsReviewPersonLevel(reconsiderStaff.getRuleLevel());
            PageResponse<ResReconsiderApprove> response = reconsiderExecuter.getApproveList(req);
            LOGGER.info("查询复议待办任务 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
            if (null != response && response.isSuccess()) {
                page.setRows(response.getRecords());
                page.setTotal(response.getTotalCount());
            } else {
                throw new BusinessException("查询复议待办任务异常");
            }
        }
        return page;
    }

    /**
     * 查询复议已完成任务
     *
     * @return
     */
    @Override
    public ResponsePage<ResReconsiderApproved> getReconsiderFinished(ReqReconsiderApproved req) {
        ResponsePage<ResReconsiderApproved> page = new ResponsePage<ResReconsiderApproved>();
        req.setSysCode(sysCode);
        req.setXsReviewPersonCode(ShiroUtils.getAccount());
        PageResponse<ResReconsiderApproved> response = reconsiderExecuter.getApprovedList(req);
        LOGGER.info("查询复议已完成任务 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            page.setRows(response.getRecords());
            page.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("查询复议已完成任务异常");
        }
        return page;
    }

    /**
     * 复议办理
     *
     * @param reconsiderationVO
     * @return
     */
    @Override
    public Result<String> reconsiderHandle(ReconsiderationVO reconsiderationVO) {
        Result<String> result = new Result<>(Result.Type.FAILURE);
        // 添加复议日志
        addReconsiderHistory(reconsiderationVO);
        ReqReconsiderManage req = new ReqReconsiderManage();
        req.setLoanNo(reconsiderationVO.getLoanNo());
        req.setReviewRemark(reconsiderationVO.getRemark());
        req.setLink(Enum.valueOf(EnumConstants.ReconsiderLink.class, reconsiderationVO.getReconsiderNode()));
        req.setReviewOperation(Enum.valueOf(EnumUtils.ReconsiderState.class, reconsiderationVO.getReconsiderState()).getValue());
        if (Strings.isNotEmpty(reconsiderationVO.getReconsiderNodeState())) {
            req.setReviewOperationSub("PASS".equals(reconsiderationVO.getReconsiderNodeState()) ? "1" : "2");
        }
        req.setErrorType(reconsiderationVO.getErrorLevel());
        req.setRejectFirstReasonCode(reconsiderationVO.getReconsiderRejectCodeOne());
        req.setRejectFirstReasonName(reconsiderationVO.getFirstReasonText());
        req.setRejectSecondReasonCode(reconsiderationVO.getReconsiderRejectCodeTwo());
        req.setRejectSecondReasonName(reconsiderationVO.getSecondReasonText());
        req.setXsReviewPersonCode(reconsiderationVO.getReconsiderOperator());
        req.setOperationUserLevel(reconsiderationVO.getReconsiderNode());
        req.setXsReviewPersonName(reconsiderationVO.getReconsiderOperatorName());
        req.setOperationUserCode(ShiroUtils.getAccount());
        req.setOperationUserName(ShiroUtils.getCurrentUser().getName());
        req.setVersion(reconsiderationVO.getVersion());
        Response<Boolean> response = reconsiderExecuter.reconsiderManage(req);
        LOGGER.info("复议{}办理 params:{} result:{}", reconsiderationVO.getReconsiderNode(), JSON.toJSONString(reconsiderationVO), JSON.toJSONString(response));
        if (null != response && response.getData()) {
            result.setType(Result.Type.SUCCESS);
            result.addMessage("操作成功");
        } else {
            throw new BusinessException("复议办理执行修改信息失败");
        }
        return result;
    }

    /**
     * 添加复议办理操作日志
     *
     * @param reconsiderationVO
     */
    private void addReconsiderHistory(ReconsiderationVO reconsiderationVO) {
        ReconsiderHistory reconsiderHistory = new ReconsiderHistory();
        BeanUtils.copyProperties(reconsiderationVO, reconsiderHistory);
        reconsiderHistory.setOperator(ShiroUtils.getAccount());
        reconsiderHistory.setReconsiderRejectCode(reconsiderationVO.getReconsiderRejectCodeOne());
        if (Strings.isNotEmpty(reconsiderationVO.getReconsiderRejectCodeTwo())) {
            reconsiderHistory.setReconsiderRejectCode(reconsiderHistory.getReconsiderRejectCode() + "-" + reconsiderationVO.getReconsiderRejectCodeTwo());
        }
        reconsiderHistory.setReconsiderLevel(reconsiderationVO.getReconsiderNode());
        reconsiderHistoryMapper.insert(reconsiderHistory);
    }

    /**
     * 复议改派查询
     *
     * @param req
     * @return
     */
    @Override
    public ResponsePage<ResReconsiderReassignment> getReconsiderReformList(ReqReconsderReassignment req) {
        ResponsePage<ResReconsiderReassignment> page = new ResponsePage<ResReconsiderReassignment>();
        PageResponse<ResReconsiderReassignment> response = reconsiderExecuter.getReconsiderReassignmentList(req);
        LOGGER.info("复议改派查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            page.setRows(response.getRecords());
            page.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("复议改派查询异常");
        }
        return page;
    }

    /**
     * 复议改派
     *
     * @param obj
     */
    @Override
    public void getReconsiderReformSubmit(ResReconsiderReassignment obj) {
        ReqReconsiderReassignment req = new ReqReconsiderReassignment();
        req.setLoanNo(obj.getLoanNo());
        req.setXsReviewPersonCode(obj.getXsReviewPersonCode());
        req.setXsReviewPersonName(obj.getHandler());
        String ruleLevel = "F1";
        if (Strings.isNotEmpty(obj.getReviewStatus())) {
            // 判断当前状态            //待办  退回
            if (EnumConstants.ReconsiderOperation.O0.getCode().equals(obj.getXsStatus()) || EnumConstants.ReconsiderOperation.O2.getCode().equals(obj.getXsStatus())) {
                ruleLevel = obj.getReviewStatus();
                // 提交 (F1提交F3提交都找F2)
            } else if (EnumConstants.ReconsiderOperation.O1.getCode().equals(obj.getXsStatus())) {
                ruleLevel = EnumConstants.ReconsiderLink.F2.getCode();
            }
        }
        req.setXsReviewPersonLevel(ruleLevel);
        req.setOperationUserCode(ShiroUtils.getAccount());
        req.setOperationUserName(ShiroUtils.getCurrentUser().getName());
        req.setVersion(obj.getVersion());
        req.setRemark("复议改派至:" + obj.getHandler());
        addReconsiderReformHistory(obj, ruleLevel);
        Response<Boolean> respons = reconsiderExecuter.reassignment(req);
        LOGGER.info("复议改派 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(respons));
        boolean isSuccess = null != respons && respons.isSuccess() && respons.getData();
        if (!isSuccess) {
            throw new BusinessException("复议改派失败");
        }
    }

    /**
     * 添加改派日志
     *
     * @param obj
     * @param ruleLevel
     */
    private void addReconsiderReformHistory(ResReconsiderReassignment obj, String ruleLevel) {
        ReconsiderHistory reconsiderHistory = new ReconsiderHistory();
        reconsiderHistory.setLoanNo(obj.getLoanNo());
        reconsiderHistory.setOperator("系统");
        reconsiderHistory.setReconsiderLevel(ruleLevel);
        reconsiderHistory.setRemark("复议改派至:" + obj.getHandler());
        reconsiderHistory.setReconsiderNode(EnumConstants.ReconsiderLink.GP.getCode());
        reconsiderHistory.setReconsiderOperator(obj.getHandlerCode());
        reconsiderHistoryMapper.insert(reconsiderHistory);
    }
}
