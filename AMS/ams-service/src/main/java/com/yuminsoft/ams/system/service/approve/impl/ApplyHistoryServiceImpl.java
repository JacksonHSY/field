package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.ReasonVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.ReqCsRefusePlupdateStatusVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.SortVo;
import com.yuminsoft.ams.system.vo.apply.ApproveHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批历史Service
 *
 * @author shipf
 * @date 2017年3月7日
 */
@Service
public class ApplyHistoryServiceImpl implements ApplyHistoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyHistoryServiceImpl.class);
    @Autowired
    private ApplyHistoryMapper applyHistoryMapper;

    @Autowired
    private QualityCheckInfoService qualityCheckInfoService;

    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;

    @Override
    public int save(ApplyHistory applyHistory) {
        return applyHistoryMapper.save(applyHistory);
    }

    @Override
    public int delete(Long id) {
        return applyHistoryMapper.delete(id);
    }

    @Override
    public int update(ApplyHistory applyHistory) {
        return applyHistoryMapper.update(applyHistory);
    }

    @Override
    public List<ApplyHistory> findAll(Map<String, Object> map) {
        return applyHistoryMapper.findAll(map);
    }

    @Override
    public ApplyHistory findOne(Map<String, Object> params) {
        return applyHistoryMapper.findOne(params);
    }

    /**
     * 根据状态查询首次提交终审
     *
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    @Override
    public List<ApplyHistory> findFirstSubmitToFinal(String loanNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanNo", loanNo);
        map.put("rtfState", EnumConstants.RtfState.XSCS.getValue());
        map.put("rtfNodeState", EnumConstants.RtfNodeState.XSCSPASS.getValue());
        map.put("noCheck", EnumConstants.ChcekNodeState.NOCHECK.getValue());
        map.put("checkPass", EnumConstants.ChcekNodeState.CHECKPASS.getValue());
        return applyHistoryMapper.findFirstSubmitToFinal(map);
    }

    /**
     * 已完成页面查询出最后一次信审操作
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月3日
     */
    @Override
    public ApplyHistory getLastOperationState(String loanNo) {
        ApplyHistory apply = applyHistoryMapper.findLastOperationState(loanNo);
        return apply;
    }

    @Override
    public ApplyHistory getDoCheck(Map<String, Object> map) {
        return applyHistoryMapper.findDoCheck(map);
    }

    @Override
    public ApplyHistory getWorkbenchStateByLoanNo(String loanNo) {
        return applyHistoryMapper.getWorkbenchStateByLoanNo(loanNo);
    }

    @Override
    public ApplyHistory getByLoanNoAndRtfStateInAndRtfNodeStateIn(String loanNo, List<String> rtfState, List<String> rtfNodeState, List<SortVo.Order> sortList) {
        // 默认排序规则
        if (CollectionUtils.isEmpty(sortList)) {
            sortList = Lists.newArrayList();
            sortList.add(new SortVo.Order(SortVo.Direction.DESC, "id"));
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("loanNo", loanNo);
        params.put("rtfState", rtfState);
        params.put("rtfNodeState", rtfNodeState);
        params.put("sortList", sortList);
        return applyHistoryMapper.findByLoanNoAndRtfStateInAndRtfNodeStateIn(params);
    }

    @Override
    public ApplyHistory getByLoanNoAndRtfStateAndRtfNodeState(String loanNo, String rtfState, String rtfNodeState, List<SortVo.Order> sortList) {
        return getByLoanNoAndRtfStateInAndRtfNodeStateIn(loanNo, Lists.newArrayList(rtfState), Lists.newArrayList(rtfNodeState), sortList);
    }

    @Override
    public List<ApplyHistory> getByLoanNoAndUserCodeAndRtfStateAndRtfNodeStateIn(String loanNo, String userCode, String rtfState, List<String> rtfNodeState) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("loanNo", loanNo);
        params.put("userCode", userCode);
        params.put("rtfState", rtfState);
        params.put("rtfNodeState", rtfNodeState);
        return applyHistoryMapper.findByLoanNoAndUserCodeAndRtfStateAndRtfNodeStateIn(params);
    }

    @Override
    public ResponsePage<ApproveHistoryVO> getApplyHistoryList(Map<String, Object> map) {
        List<ApplyHistory> applyList = applyHistoryMapper.findHistory(map);
        ArrayList<ApproveHistoryVO> list = Lists.newArrayList();
        for (ApplyHistory apply : applyList) {
            ApproveHistoryVO vo = new ApproveHistoryVO();
            BeanUtils.copyProperties(apply, vo);
            list.add(vo);
        }
        List<ApproveHistoryVO> listVO = getInceptionName(list);
        ResponsePage<ApproveHistoryVO> res = new ResponsePage<ApproveHistoryVO>();
        res.setRows(listVO);
        res.setTotal(listVO.size());
        return res;
    }

    /**
     * 将初审操作列表中的code转换成对应中文
     *
     * @param list
     * @return
     */
    private List<ApproveHistoryVO> getInceptionName(List<ApproveHistoryVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (ApproveHistoryVO vo : list) {
                if (StringUtils.isNotEmpty(vo.getCheckPerson())) { // 初审姓名
                    String checkPersonName = qualityCheckInfoService.getNameByCode(vo.getCheckPerson());
                    vo.setCheckPersonName(checkPersonName);
                }
                if (StringUtils.isNotEmpty(vo.getCheckComplex())) { // 初审复核人员姓名
                    String checkComplexName = qualityCheckInfoService.getNameByCode(vo.getCheckComplex());
                    vo.setCheckComplexName(checkComplexName);
                }
                if (StringUtils.isNotEmpty(vo.getFinalPerson())) { // 终审人员姓名
                    String finalPersonName = qualityCheckInfoService.getNameByCode(vo.getFinalPerson());
                    vo.setFinalPersonName(finalPersonName);
                }
                if (StringUtils.isNotEmpty(vo.getApprovalPerson())) { //协审员姓名
                    String approvalPersonName = qualityCheckInfoService.getNameByCode(vo.getApprovalPerson());
                    vo.setApprovalPersonName(approvalPersonName);
                }
                if (StringUtils.isNotEmpty(vo.getApprovalLeader())) { //用户组长姓名
                    String approvalLeaderName = qualityCheckInfoService.getNameByCode(vo.getApprovalLeader());
                    vo.setApprovalLeaderName(approvalLeaderName);
                }
                if (StringUtils.isNotEmpty(vo.getApprovalDirector())) { //信审主管姓名
                    String approvalDirectorName = qualityCheckInfoService.getNameByCode(vo.getApprovalDirector());
                    vo.setApprovalDirectorName(approvalDirectorName);
                }
                if (StringUtils.isNotEmpty(vo.getApprovalManager())) { //信审主管姓名
                    String approvalManagerName = qualityCheckInfoService.getNameByCode(vo.getApprovalManager());
                    vo.setApprovalManagerName(approvalManagerName);
                }

                if (StringUtils.isNotEmpty(vo.getCreatedBy())) { //创建人姓名
                    String ceatedByName = "";
                    if (!"anonymous".equals(vo.getCreatedBy())) {
                        ceatedByName = qualityCheckInfoService.getNameByCode(vo.getCreatedBy());
                    } else {
                        ceatedByName = vo.getCreatedBy();
                    }
                    vo.setCreatedByName(ceatedByName);
                }
                if (StringUtils.isNotEmpty(vo.getLastModifiedBy())) { //修改人姓名
                    String lastModifiedByName = "";
                    if (!"anonymous".equals(vo.getCreatedBy())) {
                        lastModifiedByName = qualityCheckInfoService.getNameByCode(vo.getLastModifiedBy());
                    } else {
                        lastModifiedByName = vo.getCreatedBy();
                    }
                    vo.setLastModifiedByName(lastModifiedByName);
                }
                if (StringUtils.isNotEmpty(vo.getRefuseCode())) { // 拒绝原因
                    String[] codes = vo.getRefuseCode().split("-");
                    String refuseCode = codes[0];
                    if (codes.length == 2) {
                        refuseCode = codes[1];
                    }
                    Result<ReasonVO> result = bmsLoanInfoService.queryReason(refuseCode);
                    if (result != null && result.success()) {
                        StringBuffer refuseCodeName = new StringBuffer();
                        refuseCodeName.append(result.getData().getFirstLevelReason()).append("-")
                                .append(result.getData().getTwoLevelReason());
                        vo.setRefuseCodeName(refuseCodeName.toString());
                    }
                }
            }
        }
        return list;
    }

    /**
     * add by zw at 2017-05-04批量改派审批日志写入
     *
     * @param reformVo
     * @param sessionId
     * @return
     * @date 2017年05月04日
     */
    @Override
    public void saveReformApplyHistory(ReformVO reformVo, String sessionId) {
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(sessionId, reformVo.getLoanNo(), true);
        ApplyHistory apply = new ApplyHistory();
        apply.setLoanNo(reformVo.getLoanNo());
        apply.setName(applyBasiceInfo.getName());
        apply.setIdNo(applyBasiceInfo.getIdNo());
        apply.setRtfState(EnumConstants.RtfState.XSCS.getValue());
        apply.setProNum(applyBasiceInfo.getLoanNo());// 流程实例号
        apply.setRtfNodeState(EnumConstants.RtfNodeState.XSCSASSIGN.getValue());
        apply.setProName("信审初审批量改派");
        apply.setCheckPerson(reformVo.getTargetUserCode());
        if (null == reformVo.getUserCode()) {
            apply.setRemark(ShiroUtils.getAccount() + "通过信审批量改派给" + reformVo.getTargetUserCode());
        } else {
            apply.setRemark(ShiroUtils.getAccount() + "信审批量改派由" + reformVo.getUserCode() + "改派给" + reformVo.getTargetUserCode());
        }
        try {
            applyHistoryMapper.save(apply);
        } catch (Exception e) {
            LOGGER.info("初审批量改派保存审批日志异常:", e);
        }
    }

    /**
     * 批量退回或拒绝审批日志写入
     *
     * @param request
     * @param sessionId
     * @return
     * @date 2017年05月04日
     */
    @Override
    public void saveReturnOrRejectApplyHistory(ReqCsRefusePlupdateStatusVO request, String sessionId, String type) {
        String loanNo = request.getList().get(0).getLoanNo();
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(sessionId, loanNo, true);
        ApplyHistory apply = new ApplyHistory();
        apply.setLoanNo(loanNo);
        apply.setName(applyBasiceInfo.getName());
        apply.setIdNo(applyBasiceInfo.getIdNo());
        // TODO 批量拒绝退还 分两种情况
        apply.setRtfState(EnumConstants.RtfState.CSFP.getValue());
        apply.setProNum(applyBasiceInfo.getLoanNo());// 流程实例号
        apply.setRtfNodeState(type);
        if (type.equals(EnumConstants.RtfNodeState.CSFPREJECT.getValue())) {
            apply.setProName("信审初审批量拒绝");
            apply.setAutoRefuse(EnumUtils.YOrNEnum.N.getValue());
        } else if (type.equals(EnumConstants.RtfNodeState.CSFPCANCEL.getValue())) {
            if (null != applyBasiceInfo.getZdqqApply() && 1==applyBasiceInfo.getZdqqApply()) {// 前前退回
                apply.setProName("信审初审批量退回前前");
                apply.setRtfNodeState(EnumConstants.RtfNodeState.CSFPZDQQRETURN.getValue());
            } else {
                apply.setProName("信审初审批量退回");
            }
        }
        apply.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
        if (null != request.getFirstLevelReasonCode()) {
            apply.setRefuseCode(request.getFirstLevelReasonCode());
        }
        if (null != request.getTwoLevelReasonCode()) {
            apply.setRefuseCode(apply.getRefuseCode() + "-" + request.getTwoLevelReasonCode());
        }
        apply.setRemark(request.getRemark());
        try {
            applyHistoryMapper.save(apply);
            LOGGER.info("初审批量改派退回、拒绝保存审批日志成功{}:", apply.getLoanNo());
        } catch (Exception e) {
            LOGGER.info("初审批量改派退回、拒绝保存审批日志异常:", e);
        }
    }

    /**
     * 根据单号查询历史处理过的初审员(初审改派查询)
     *
     * @return
     * @param-map:loanNoArray-借款编号逗号隔开;loanNoCount-借款编号个数
     */
    @Override
    public List<ApplyHistory> getHistoryApplyDealsPerson(Map<String, Object> map) {
        return applyHistoryMapper.findHistoryApplyDealsPerson(map);
    }

    /**
     * 复议查询出拒绝信息
     * @param loanNo
     * @return
     */
    @Override
    public ApplyHistory getApplyHistoryRejectByLoanNo(String loanNo) {
        return applyHistoryMapper.findApplyHistoryRejectByLoanNo(loanNo);
    }
}
