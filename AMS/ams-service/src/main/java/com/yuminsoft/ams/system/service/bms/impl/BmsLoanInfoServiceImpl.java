package com.yuminsoft.ams.system.service.bms.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.apply.IApplyEnterExecuter;
import com.ymkj.ams.api.service.approve.audit.ContactInfoExecuter;
import com.ymkj.ams.api.service.approve.audit.FinalApproveExecuter;
import com.ymkj.ams.api.service.approve.audit.FirstApproveExecuter;
import com.ymkj.ams.api.service.approve.dispatch.FinalDispatchExecuter;
import com.ymkj.ams.api.service.approve.dispatch.FirstDispatchExecuter;
import com.ymkj.ams.api.service.approve.integrate.ApplicationInfoExecuter;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.ams.api.service.approve.integrate.ReasonExecuter;
import com.ymkj.ams.api.service.approve.ruleengine.RuleEngineExecuter;
import com.ymkj.ams.api.service.approve.task.TaskNumberExecuter;
import com.ymkj.ams.api.vo.request.apply.*;
import com.ymkj.ams.api.vo.request.audit.*;
import com.ymkj.ams.api.vo.request.audit.first.ReqApplicationInfoVO;
import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.ymkj.ams.api.vo.request.integrate.application.ReqDetailDifferenceVO;
import com.ymkj.ams.api.vo.request.integratedsearch.ReqQueryLoanLogVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSAppPersonInfoVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.ams.api.vo.request.task.PersonCodeAndRoleVo;
import com.ymkj.ams.api.vo.request.task.ReqBMSTaskNumberVo;
import com.ymkj.ams.api.vo.response.audit.*;
import com.ymkj.ams.api.vo.response.integrate.application.*;
import com.ymkj.ams.api.vo.response.master.ResBMSAppPersonInfoVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.ams.api.vo.response.master.ResListVO;
import com.ymkj.ams.api.vo.response.task.ResBMSTaskNumberVo;
import com.ymkj.ams.api.vo.response.task.TaskNumberQueVo;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationInfo;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.EnumUtils.ResponseCodeEnum;
import com.yuminsoft.ams.system.common.PhoneUtil;
import com.yuminsoft.ams.system.dao.approve.AgenLeaderMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.domain.approve.AgenLeader;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.domain.uflo.Task;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.ContactVO;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.firstApprove.CustomerContactInfoVO;
import com.yuminsoft.ams.system.vo.firstApprove.FirstTelephoneSummaryRelationInfoVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 借款信息接口实现
 *
 * @author dmz
 * @date 2017年3月15日
 */
@Service
public class BmsLoanInfoServiceImpl extends BaseService implements BmsLoanInfoService {

    @Autowired
    private FirstApproveExecuter firstApproveExecuter;
    @Autowired
    private ApplyInfoExecuter applyInfoExecuter;
    @Autowired
    private IApplyEnterExecuter applyEnterExecuter;
    @Autowired
    private ContactInfoExecuter contactInfoExecuter;
    @Autowired
    private FirstDispatchExecuter firstDispatchExecuter;
    @Autowired
    private FinalDispatchExecuter finalDispatchExecuter;
    @Autowired
    private RuleEngineExecuter ruleEngineExecuter;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ApplyHistoryService applyHistoryService;
    @Autowired
    private ReasonExecuter reasonExecuter;
    @Autowired
    private IntegrateSearchExecuter integrateSearchExecuter;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private PmsApiService pmsApiService;
    @Autowired
    private AgenLeaderMapper agenLeaderMapper;
    @Autowired
    private TaskNumberExecuter taskNumberExecuter;
    @Autowired
    private FinalApproveExecuter finalApproveExecuter;
    @Autowired
    private ApplicationInfoExecuter applicationInfoExecuter;

    /**
     * 修改借款信息
     *
     * @param auditAmendEntryVO
     * @return
     * @author dmz
     * @date 2017年3月23日
     */
    @Override
    public Result<String> updateLoanInfo(AuditAmendEntryVO auditAmendEntryVO) {
        Result<String> result = new Result<String>(Type.FAILURE);
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        auditAmendEntryVO.setSysCode(sysCode);
        auditAmendEntryVO.setModifier(currentUser.getName());
        auditAmendEntryVO.setModifierId(currentUser.getId());
        //和录单一至，对月均字段四舍五入后入库
        if (null != auditAmendEntryVO.getAssetsInfoVO().getCardLoanInfoVO().getPayMonthAmt()) {
            auditAmendEntryVO.getAssetsInfoVO().getCardLoanInfoVO().setPayMonthAmt(
                    auditAmendEntryVO.getAssetsInfoVO().getCardLoanInfoVO().getPayMonthAmt().setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        if (null != auditAmendEntryVO.getAssetsInfoVO().getMerchantLoanInfoVO().getPayMonthAmt()) {
            auditAmendEntryVO.getAssetsInfoVO().getMerchantLoanInfoVO().setPayMonthAmt(
                    auditAmendEntryVO.getAssetsInfoVO().getMerchantLoanInfoVO().getPayMonthAmt().setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        Response<ReqApplyEntryVO> response = firstApproveExecuter.modifyCustomerInfo(auditAmendEntryVO);
        LOGGER.info("修改借款信息 params:{} result:{}", JSON.toJSONString(auditAmendEntryVO), JSON.toJSONString(response));
        if (null != response) {
            if (EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
                result.setType(Type.SUCCESS);
                result.addMessage("修改成功");
            } else if (EnumUtils.ResponseCodeEnum.PARAMMISSING.getValue().equals(response.getRepCode())) {
                result.setType(Type.VERSIONERR);
            } else if (EnumUtils.ResponseCodeEnum.CHECK.getValue().equals(response.getRepCode())) {
                result.addMessage(response.getRepMsg());
            } else {
                LOGGER.error("客户信息修改失败 params:{} result:{}", JSON.toJSONString(auditAmendEntryVO), JSON.toJSONString(response));
                result.addMessage("信息修改失败");
            }
        } else {
            result.addMessage("修改失败");
        }
        return result;
    }

    /**
     * 前前客户信息修改
     *
     * @param reqApplicationInfoVO
     * @return
     */
    @Override
    public Result<String> updateMoneyLoanInfo(ReqApplicationInfoVO reqApplicationInfoVO) {
        Result<String> result = new Result<String>(Type.FAILURE);
        reqApplicationInfoVO.getBaseInfo().setOperatorName(ShiroUtils.getCurrentUser().getName());
        reqApplicationInfoVO.getBaseInfo().setOperatorId(ShiroUtils.getCurrentUser().getId());
        Response<Boolean> response = firstApproveExecuter.modifyQqCustomerInfo(reqApplicationInfoVO);
        if (null != response) {
            if (response.isSuccess() && response.getData()) {
                result.setType(Type.SUCCESS);
                result.addMessage("修改成功");
            } else if (EnumUtils.ResponseCodeEnum.PARAMMISSING.getValue().equals(response.getRepCode())) {
                result.setType(Type.VERSIONERR);
            } else if (EnumUtils.ResponseCodeEnum.CHECK.getValue().equals(response.getRepCode()) || ResponseCodeEnum.CHECKPARAM.getValue().equals(response.getRepCode())) {
                result.addMessage(response.getRepMsg());
            } else {
                LOGGER.error("前前客户信息修改失败 params:{} result:{}", JSON.toJSONString(reqApplicationInfoVO), JSON.toJSONString(response));
                result.addMessage("客户信息修改失败");
            }
        } else {
            LOGGER.error("前前客户信息修改异常 params:{} result:{}", JSON.toJSONString(reqApplicationInfoVO), JSON.toJSONString(response));
            throw new BusinessException("前前客户信息修改异常");
        }
        return result;
    }


    /**
     * 修改初审借款单状态
     *
     * @param applyVo  审批历史
     * @param loanInfo 申请件基本信息
     * @return
     * @author dmz
     * @date 2017年3月17日
     */
    @Override
    public Result<String> updateFirstLoanNoStateService(ApplyHistoryVO applyVo, ReqInformationVO loanInfo) {
        StopWatch stopWatch = new StopWatch();
        Result<String> result = new Result<String>(Type.FAILURE);
        ReqCsUpdVO request = new ReqCsUpdVO();
        request.setSysCode(sysCode);
        request.setLoanNo(applyVo.getLoanNo());
        request.setIdNo(loanInfo.getIdNo()); // 身份证
        request.setName(loanInfo.getName()); // 姓名
        request.setCellphone(loanInfo.getCellPhone()); // 手机号
        request.setCellPhoneSec(loanInfo.getCellPhone_sec()); // 备用手机号
        request.setCsPersonCode(applyVo.getCheckPerson()); // 初审员
        request.setCheckNodeStatus(applyVo.getCheckNodeState()); // 初审复核确认(终审相关状态)
        request.setOperatorCode(request.getCsPersonCode()); // 初审人员code
        if (!EnumConstants.RtfNodeState.XSCSZDQQRETURN.getValue().equals(applyVo.getRtfNodeState())) {
            request.setFirstLevelReasonCode(applyVo.getFirstReason());
            request.setFirstLevelReasons(applyVo.getFirstReasonText());
            request.setTwoLevelReasonCode(applyVo.getSecondReason());
            request.setTwoLevelReasons(applyVo.getSecondReasonText());
        } else {
            request.setReturnReasons(applyVo.getReturnReasons());// 前前进件退回原因
        }
        if (EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(applyVo.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECKPASS.getValue().equals(applyVo.getCheckNodeState())) {
            request.setOperatorCode(applyVo.getCheckComplex());// 取复核人员
            if (EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(applyVo.getCheckNodeState())) {// 如退回修改成优先件
                request.setIfNewLoanNo(EnumConstants.ifNewLoanNo.NOLOANNO.getValue());
            }
        }
        request.setVersion(applyVo.getVersion());
        request.setOperatorIP(applyVo.getIp());
        request.setComplexPersonCode(applyVo.getCheckComplex());
        request.setRemark(applyVo.getRemark());
        // 判断是否是首次提交终审--复核确认-不通过ifNewLoan要修改所有无法通过ifNewLoan判断
        if ((EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(applyVo.getRtfNodeState()) || EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(applyVo.getRtfNodeState())) && (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(applyVo.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECKPASS.getValue().equals(applyVo.getCheckNodeState()))) {
            stopWatch.start("查询首次提交耗时");
            List<ApplyHistory> list = applyHistoryService.findFirstSubmitToFinal(applyVo.getLoanNo());
            stopWatch.stop();
            if (null == list || list.size() < 1) {
                request.setFirstSubZsDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
            }
        }
        request.setAccDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
        // add by zw at 2017-05-06 需要符合确认时传复核人的code
        if (EnumConstants.ChcekNodeState.CHECK.getValue().equals(applyVo.getCheckNodeState())) {
            List<AgenLeader> agList = agenLeaderMapper.findByUserCode(ShiroUtils.getAccount());
            // 判断代理组长
            if (!CollectionUtils.isEmpty(agList)) {
                request.setComplexPersonCode(agList.get(0).getProxyUser());// 获取代理组长
            } else {
                ReqLevelVO reqVo = new ReqLevelVO();
                reqVo.setSysCode(sysCode);
                reqVo.setLoginUser(ShiroUtils.getAccount());
                reqVo.setInActive(AmsConstants.T);
                reqVo.setStatus(AmsConstants.ZERO);
                reqVo.setRoleCode(RoleEnum.CHECK_GROUP_LEADER.getCode());
                reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
                ResEmployeeVO resEmployeeVO = pmsApiService.getLeaderByCode(reqVo);
                if (null == resEmployeeVO) {
                    throw new BusinessException("获取复核确认组长时发生异常！");
                }
                request.setComplexPersonCode(resEmployeeVO.getUsercode());
            }
        }
        Response<ResBMSAudiUpdVo> response = null;// 原始接口拆分成、拒绝、退回、挂起、提交四个接口
        String rtfNodeState = applyVo.getRtfNodeState();
        stopWatch.start("修改借款单状态");
        if (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(rtfNodeState)) {// 拒绝操作
            response = firstApproveExecuter.reject(request);
            LOGGER.info("修改借款单状态为拒绝   params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        } else if (EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(rtfNodeState)) {// 退回操作
            response = firstApproveExecuter.back(request);
            LOGGER.info("修改借款单状态为退回  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        } else if (EnumConstants.RtfNodeState.XSCSZDQQRETURN.getValue().equals(rtfNodeState)){
            response = firstApproveExecuter.back(request);
            LOGGER.info("修改借款单状态为前前退回  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        } else if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(rtfNodeState)) {// 挂起操作
            response = firstApproveExecuter.hangUp(request);
            LOGGER.info("修改借款单状态为挂起   params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        } else if (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(rtfNodeState) || EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(rtfNodeState)) {// 提交操作（包括提交至高审）
            if (EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(rtfNodeState)) {// 高审判断
                request.setFlag(2L);
            } else {// 终审
                request.setFlag(1L);
            }
            response = firstApproveExecuter.submit(request);
            LOGGER.info("修改借款单状态为提交   params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        }
        stopWatch.stop();
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            result.setType(Type.SUCCESS);
            result.addMessage("操作成功!");
        } else if (null != response && ResponseCodeEnum.PARAMMISSING.getValue().equals(response.getRepCode())) {
            result.setType(Type.VERSIONERR);
            result.addMessage("您当前办理的借款单有可能已经被改派!");
        } else {
            result.addMessage("操作失败");
        }
        LOGGER.info(stopWatch.prettyPrint());
        return result;
    }

    /**
     * 根据借款编号查询借款基本信息
     *
     * @param sessionId-回话id
     * @param loanNo-借款编号
     * @param flag-标记是刷新redis(true:直接从借款拿;false:从redis拿)
     * @return
     * @author dmz
     * @date 2017年3月18日
     */
    @Override
    public ReqInformationVO getBMSLoanBasiceInfoByLoanNoService(String sessionId, String loanNo, boolean flag) {
        ReqInformationVO applyBasicInfo = new ReqInformationVO();
        String loanBasicInfoRedisId = sessionId + "-" + loanNo;
        if (redisUtil.exists(loanBasicInfoRedisId) && !flag) {
            applyBasicInfo = (ReqInformationVO) redisUtil.get(loanBasicInfoRedisId);
        } else {
            Response<ReqInformationVO> response = applyInfoExecuter.getBaseInfo(loanNo);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询借款基本信息 params:{} result:{}", loanNo, JSON.toJSONString(response));
            }
            if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
                applyBasicInfo = response.getData();
            } else {
                LOGGER.error("查询借款基本信息异常 result:{}", JSON.toJSONString(response));
                throw new BusinessException("查询借款基本信息失败");
            }
            redisUtil.set(loanBasicInfoRedisId, applyBasicInfo, 3600L);
        }

        return applyBasicInfo;
    }

    /**
     * 根据借款编号查询借款信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月15日
     */
    @Override
    public ApplyEntryVO getBMSLoanInfoByLoanNoService(String loanNo) {
        ApplyEntryVO applyVo = null;
        PersonHistoryLoanVO request = new PersonHistoryLoanVO();
        request.setLoanNo(loanNo);
        request.setSysCode(sysCode);
        Response<ApplyEntryVO> response = applyInfoExecuter.getApplyDetail(request);
        if (null != response && response.isSuccess()) {
            applyVo = response.getData();
        } else {
            LOGGER.error("查询借款信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("查询借款信息异常");
        }
        return applyVo;
    }

    /**
     * 根据借款编号查询借款信息(客户信息标红)
     *
     * @param loanNo
     * @param type   1:初审，2：终审
     * @return
     * @author dmz
     * @date 2017年5月12日
     */
    @Override
    public AuditApplyEntryVO queryAuditDifferences(String loanNo, String type) {
        AuditApplyEntryVO auditVo = null;
        ReqAuditDifferencesVO request = new ReqAuditDifferencesVO();
        request.setLoanNo(loanNo);
        request.setSysCode(sysCode);
        request.setVersion(type);
        Response<AuditApplyEntryVO> response = applyInfoExecuter.getApplyDetailDifference(request);
        if (null != response && response.isSuccess()) {
            auditVo = response.getData();
        } else {
            LOGGER.error("借款单号[{}]查询借款信息(标红) params:{} result:{}", loanNo, JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("借款信息查询(标红)异常");
        }
        return auditVo;
    }

    /**
     * 根据借款编号查询借款信息(前前客户信息标红)
     *
     * @param loanNo
     * @param type-1:初审，2：终审
     * @return
     */
    @Override
    public ResDetailDifferenceVO getDetailDifference(String loanNo, String type) {
        ResDetailDifferenceVO resDetailDifferenceVO = null;
        ReqDetailDifferenceVO request = new ReqDetailDifferenceVO();
        request.setLoanNo(loanNo);
        request.setType(type);
        Response<ResDetailDifferenceVO> response = applicationInfoExecuter.getDetailDifference(request);
        if (null != response && response.isSuccess()) {
            resDetailDifferenceVO = response.getData();
        } else {
            LOGGER.error("借款单号[{}]查询前前借款信息(标红) params:{} result:{}", loanNo, JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("系统忙,请稍后!");
        }
        return resDetailDifferenceVO;
    }

    /**
     * 添加联系人
     * <p>
     * zjy 2017年3月17日
     */
    @Override
    public Result<String> insertContactInfoService(FirstTelephoneSummaryRelationInfoVo infoVo, String loanNo, String version) {
        Result<String> result = new Result<>(Type.FAILURE);
        // 判断新增的联系人 与申请人关系是否是配偶
        ApplyEntryVO applyInfo = getBMSLoanInfoByLoanNoService(loanNo);
        if ("00013".equals(infoVo.getRelationship()) && !"00002".equals(applyInfo.getBasicInfoVO().getPersonInfoVO().getMaritalStatus())) {
            result.addMessage("添加失败，该申请人婚姻状态未婚!");
            return result;
        }
        ReqContactInfoVO request = new ReqContactInfoVO();
        request.setSysCode(sysCode);
        request.setVersion(version);
        request.setIp(WebUtils.getIp());// 在controller设置进去
        request.setServiceCode(ShiroUtils.getCurrentUser().getUsercode()); // 工号
        request.setServiceName(ShiroUtils.getCurrentUser().getName()); // 用户名
        request.setContactName(infoVo.getName());
        request.setContactRelation(infoVo.getRelationship());
        request.setIfKnowLoan(infoVo.getIsKnow());
        request.setContactEmpName(infoVo.getCompanyName());
        request.setContactCellPhone(infoVo.getMobileNumber());
        request.setContactCorpPost(infoVo.getJob());
        request.setContactCorpPhone(infoVo.getTelephoneNumber());
        request.setLoanNo(loanNo);
        Response<Object> response = contactInfoExecuter.addContactInfo(request);
        LOGGER.info("添加联系人 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.addMessage("添加联系人成功!");
        } else {
            result.addMessage("添加联系人失败!");
        }
        return result;
    }

    /**
     * 第三方电话增加
     */
    @Override
    public Result<String> insertContactInfoService(ReqContactInfoVO infoVo) {
        Result<String> result = new Result<>(Type.FAILURE);
        infoVo.setSysCode(sysCode);
        infoVo.setServiceCode(ShiroUtils.getCurrentUser().getUsercode()); // 工号
        infoVo.setServiceName(ShiroUtils.getCurrentUser().getName()); // 用户名
        Response<Object> response = contactInfoExecuter.addContactInfo(infoVo);
        LOGGER.info("添加联系人 params:{} result:{}", JSON.toJSONString(infoVo), JSON.toJSONString(response));
        if (null != response) {
            if (ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
                result.setType(Type.SUCCESS);
                result.addMessage("添加联系人成功!");
            } else if (ResponseCodeEnum.PARAMMISSING.getValue().equals(response.getRepCode())) {
                result.setType(Type.VERSIONERR);
            }
        } else {
            result.addMessage("添加联系人失败!");
        }
        return result;
    }

    /**
     * 分页查询初审工作台队列信息
     *
     * @param requestPage
     * @param taskType-队列类型-1:优先队列信息;2:正常队列信息;3:挂起队列数
     * @param reqFlag-区分初审
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    @Override
    public ResponsePage<ResBMSAuditVo> getWorkbenchList(RequestPage requestPage, String taskType, String reqFlag) {
        ResponsePage<ResBMSAuditVo> page = new ResponsePage<ResBMSAuditVo>();
        ReqAuditVo request = new ReqAuditVo();
        request.setFlag(reqFlag);
        request.setServiceCode(ShiroUtils.getAccount());// 获取登录用户工号
        request.setCaseType(taskType);
        request.setSysCode(sysCode);
        request.setPageNum(requestPage.getPage());
        request.setPageSize(requestPage.getRows());
        request.setFieldSort(requestPage.getSort());
        request.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        PageResponse<ResBMSAuditVo> response = firstApproveExecuter.getApproveList(request);
        LOGGER.info("初审工作台查询队列信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            page.setRows(response.getRecords());
            page.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("初审工作台队列查询异常");
        }
        return page;
    }

    /**
     * 分页查询终审工作台队列信息
     *
     * @param requestPage
     * @param taskType
     * @param reqFlag
     * @return
     * @author JiaCX
     * @date 2017年6月14日 上午11:51:02
     */
    @Override
    public ResponsePage<ResBMSAuditVo> getZsWorkbenchList(RequestPage requestPage, String taskType, String reqFlag) {
        ResponsePage<ResBMSAuditVo> page = new ResponsePage<ResBMSAuditVo>();
        ReqAuditVo request = new ReqAuditVo();
        request.setFlag(reqFlag);
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        // 获取登录用户工号
        request.setServiceCode(currentUser.getUsercode());
        request.setCaseType(taskType);
        request.setSysCode(sysCode);
        request.setPageNum(requestPage.getPage());
        request.setPageSize(requestPage.getRows());
        changeSort(requestPage);
        request.setFieldSort(requestPage.getSort());
        request.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        PageResponse<ResBMSAuditVo> response = finalApproveExecuter.getApproveList(request);
        LOGGER.debug("终审工作台查询队列信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            LOGGER.info("终审工作台查询队列信息 params:{} 返回数据有:{}条", JSON.toJSONString(request), response.getTotalCount());
            page.setRows(response.getRecords());
            page.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("终审工作台队列查询异常");
        }
        return page;
    }

    /**
     * 分页查询初审已完成队列信息
     *
     * @return request
     * @author dmz
     * @date 2017年4月13日
     */
    @Override
    public ResponsePage<ResOffTheStocksAuditVO> adultOffTheStocks(ReqBMSAdultOffTheStocksVo request, RequestPage requestPage) {
        ResponsePage<ResOffTheStocksAuditVO> page = new ResponsePage<ResOffTheStocksAuditVO>();
        request.setSysCode(sysCode);
        request.setAuditPersonCode(ShiroUtils.getAccount());
        request.setOperatorCode(ShiroUtils.getAccount());
        request.setFieldSort(requestPage.getSort());
        request.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        PageResponse<ResOffTheStocksAuditVO> response = firstApproveExecuter.getApprovedList(request);
        LOGGER.debug("初审工作台已完成查询队列信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            page.setRows(response.getRecords());
            page.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("初审工作台已完成队列查询异常");
        }
        return page;
    }

    /**
     * 分页查询终审工已完成队列信息
     *
     * @param requestPage
     * @param offStartDate
     * @param offEndDate
     * @param request
     * @return
     * @author JiaCX
     * @date 2017年6月14日 上午11:44:20
     */
    @Override
    public ResponsePage<ResOffTheStocksAuditVO> getCompletedTask(RequestPage requestPage, String offStartDate, String offEndDate, HttpServletRequest request) {
        ResponsePage<ResOffTheStocksAuditVO> returnpage = new ResponsePage<ResOffTheStocksAuditVO>();
        ReqBMSAdultOffTheStocksVo reqVo = new ReqBMSAdultOffTheStocksVo();
        reqVo.setReqFlag(EnumConstants.ReqFlag.ZS.getValue());
        reqVo.setSysCode(sysCode);
        reqVo.setOffStartDate(StringUtils.isEmpty(offStartDate) ? DateUtils.dateToString(DateUtils.getStartTime(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS) : offStartDate + " 00:00:00");
        reqVo.setOffEndDate(StringUtils.isEmpty(offEndDate) ? DateUtils.dateToString(DateUtils.getEndTime(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS) : offEndDate + " 23:59:59");
        reqVo.setAuditPersonCode(ShiroUtils.getCurrentUser().getUsercode());
        reqVo.setOperatorCode(ShiroUtils.getCurrentUser().getUsercode());
        reqVo.setOperatorIP(WebUtils.retrieveClientIp(request));
        reqVo.setPageNum(requestPage.getPage());
        reqVo.setPageSize(requestPage.getRows());
        reqVo.setFieldSort(requestPage.getSort());
        reqVo.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        long a = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------终审工作台查询已完成队列bms查询开始时间：" + a);
        PageResponse<ResOffTheStocksAuditVO> response = finalApproveExecuter.getApprovedList(reqVo);
        long b = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------终审工作台查询已完成队列bms查询结束时间：" + b);
        LOGGER.info("------------------------------------------------------终审工作台查询已完成队列bms查询用时：" + (b - a) + "ms");
        LOGGER.info("终审工作台查询已完成队列信息 params:{} result:{}", JSON.toJSONString(reqVo), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            LOGGER.info("终审工作台查询已完成队列信息 params:{} 返回数据有:{}条", JSON.toJSONString(reqVo), response.getTotalCount());
            returnpage.setRows(response.getRecords());
            returnpage.setTotal(response.getTotalCount());
        } else {
            throw new BusinessException("终审工作台已完成队列查询异常");
        }
        return returnpage;
    }

    /**
     * 修改或删除联系人联系方式 type 1 删除 0 修改
     */
    @Override
    public Result<String> updateRelationContactInfo(ReqContactInfoVO vo) {
        Result<String> result = new Result<>(Type.FAILURE);
        vo.setSysCode(sysCode);
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        // 获取登录用户工号
        vo.setServiceCode(currentUser.getUsercode());
        vo.setServiceName(currentUser.getName());
        Response<Object> response = contactInfoExecuter.modifyContactInfo(vo);
        LOGGER.info("修改或删除联系人联系方式  params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));
        if (response != null && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.addMessage("联系方式修改成功");
        } else if (response != null && response.getRepCode().equals(EnumUtils.ResponseCodeEnum.VERSIONERR.getValue())) {
            result.setType(Type.VERSIONERR);
        } else {
            result.addMessage("修改失败！");
        }
        return result;
    }

    /**
     * 修改联系人信息
     */
    @Override
    public Result<String> updateContactInfoService(ReqContactInfoVO[] vo) {
        Result<String> result = new Result<>(Type.FAILURE);
        ApplyEntryVO applyInfo = getBMSLoanInfoByLoanNoService(vo[0].getLoanNo());
        int allRight = 1;
        // 先判断是否已婚
        for (ReqContactInfoVO aVos : vo) {
            if ("00013".equals(aVos.getContactRelation()) && !"00002".equals(applyInfo.getBasicInfoVO().getPersonInfoVO().getMaritalStatus())) {
                result.addMessage("保存失败，该申请人婚姻状态未婚!");
                return result;
            }
        }
        for (ReqContactInfoVO aVo : vo) {
            aVo.setSysCode(sysCode);
            aVo.setIp(WebUtils.getIp());// 在controller设置进去
            aVo.setServiceCode(ShiroUtils.getCurrentUser().getUsercode()); // 工号
            aVo.setServiceName(ShiroUtils.getCurrentUser().getName()); // 用户名
            // 判断是否已婚
            PersonHistoryLoanVO applyEntryVORequest = new PersonHistoryLoanVO();
            applyEntryVORequest.setLoanNo(aVo.getLoanNo());
            applyEntryVORequest.setSysCode(sysCode);
            Response<Object> response = contactInfoExecuter.modifyContactInfo(aVo);
            LOGGER.info("修改联系人信息 params:{} result:{}", JSON.toJSONString(aVo), JSON.toJSONString(response));
            if (null == response || !response.isSuccess()) {
                allRight = 0;
            }
        }
        if (allRight == 1) {
            result.setType(Type.SUCCESS);
            result.addMessage("修改联系人成功");
        } else {
            result.addMessage("修改联系人失败");
        }
        return result;
    }

    /**
     * 电核汇总中修改用户基本信息
     *
     * @param vo
     * @return
     * @author dmz
     * @date 2017年6月16日
     */
    @Override
    public Result<String> updatePersonalInformation(ReqPersonalInformation vo) {
        Result<String> result = new Result<>(Type.FAILURE);
        vo.setSysCode(sysCode);
        vo.setServiceCode(ShiroUtils.getCurrentUser().getUsercode()); // 工号
        vo.setServiceName(ShiroUtils.getCurrentUser().getName()); // 用户名
        Response<String> response = firstApproveExecuter.modifyCustomerInfoInPhoneCheck(vo);
        LOGGER.info("修改基本信息 params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));
        if (response != null && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.addMessage("修改成功");
        } else if (response != null && EnumUtils.ResponseCodeEnum.PARAMMISSING.getValue().equals(response.getRepCode())) {
            result.setType(Type.VERSIONERR);
            result.addMessage("您当前办理的借款单有可能已经被改派,请关闭当前页面!");
        } else {
            result.addMessage("修改失败");
        }
        return result;
    }

    /**
     * 审批修改产品相关信息
     *
     * @param approvalSaveVO
     * @author dmz
     * @date 2017年4月6日
     */
    @Override
    public void updateProductInfo(ApprovalSaveVO approvalSaveVO) {
        ReqcsBMProductUpdVo req = new ReqcsBMProductUpdVo();// 修改借款产品接口拆分
        req.setSysCode(sysCode);
        req.setLoanNo(approvalSaveVO.getLoanNo());
        req.setIfCreditRecode(approvalSaveVO.getCreditRecord());
        // 收入证明金额可能为空
        if (null != approvalSaveVO.getIncomeCertificate() && approvalSaveVO.getIncomeCertificate().compareTo(new BigDecimal("0")) != -1) {
            req.setAmoutInconme(approvalSaveVO.getIncomeCertificate().doubleValue());
        }
        req.setProductCd(approvalSaveVO.getApprovalProductCd());
        req.setAccLmt(approvalSaveVO.getApprovalLimit().doubleValue());
        req.setAccTerm(Integer.parseInt(approvalSaveVO.getApprovalTerm()));
        req.setAccDate(new Date());
        req.setVersion(approvalSaveVO.getVersion());
        req.setOperatorCode(approvalSaveVO.getApprovalPerson());
        req.setOperatorIP(approvalSaveVO.getIp());
        req.setOnlineAWithin3MonthsAddress(approvalSaveVO.getThreeMonthsAddress());
        req.setOnlineAWithin6MonthsAddress(approvalSaveVO.getSixMonthsAddress());
        req.setOnlineAWithin12MonthsAddress(approvalSaveVO.getOneYearAddress());
        req.setPolicyCheckIsVerify(approvalSaveVO.getPolicyCheckIsVerify());
        req.setCarCheckIsVerify(approvalSaveVO.getCarCheckIsVerify());
        Response<Object> response = firstApproveExecuter.modifyApproveInfo(req);
        LOGGER.info("审批修改产品相关信息  params:{} result:{}", JSON.toJSONString(req), JSON.toJSON(response));
        if (null == response || !response.isSuccess()) {
            throw new BusinessException("审批修改产品相关信息异常");
        }
        // 修改央行报告是否改变状态(用于是否重新解读)
        if (null != approvalSaveVO.getMarkUpdateReportId() && EnumUtils.YOrNEnum.Y.getValue().equals(approvalSaveVO.getMarkUpdateReportId())) {
            updateReportIdByLoanNo(approvalSaveVO.getLoanNo());
        }
    }

    /**
     * 前前审批修改产品相关信息
     *
     * @param approvalSaveVO
     * @author dmz
     * @date 2017年4月6日
     */
    @Override
    public void updateMoneyProductInfo(ApprovalSaveVO approvalSaveVO,ReqAssetsInfoVO saveAssetsVO) {
        ReqQqCsApproveInfoVO req = new ReqQqCsApproveInfoVO();// 修改借款产品接口拆分
        req.setLoanNo(approvalSaveVO.getLoanNo());
        req.setIfCreditRecode(approvalSaveVO.getCreditRecord());
        // 收入证明金额可能为空
        if (null != approvalSaveVO.getIncomeCertificate() && approvalSaveVO.getIncomeCertificate().compareTo(new BigDecimal("0")) != -1) {
            req.setAmoutInconme(approvalSaveVO.getIncomeCertificate().doubleValue());
        }
        req.setProductCd(approvalSaveVO.getApprovalProductCd());
        req.setAccLmt(approvalSaveVO.getApprovalLimit().doubleValue());
        req.setAccTerm(Integer.parseInt(approvalSaveVO.getApprovalTerm()));
        req.setAccDate(new Date());
        req.setVersion(approvalSaveVO.getVersion());
        req.setOperatorCode(approvalSaveVO.getApprovalPerson());
        req.setOperatorName(ShiroUtils.getCurrentUser().getName());
        req.setOperatorId(ShiroUtils.getCurrentUser().getId());
        req.setOperatorIP(approvalSaveVO.getIp());
        req.setAssets(saveAssetsVO);
        Response<Boolean> response = firstApproveExecuter.modifyQqApproveInfo(req);
        LOGGER.info("审批修改产品相关信息  params:{} result:{}", JSON.toJSONString(req), JSON.toJSON(response));
        if (null == response || !response.isSuccess()) {
            String info = null != response ? response.getRepMsg() : "系统忙请稍后";
            throw new BusinessException(info);
        }
        // 修改央行报告是否改变状态(用于是否重新解读)
        if (null != approvalSaveVO.getMarkUpdateReportId() && EnumUtils.YOrNEnum.Y.getValue().equals(approvalSaveVO.getMarkUpdateReportId())) {
            updateReportIdByLoanNo(approvalSaveVO.getLoanNo());
        }
    }

    /**
     * 改派查询接口
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月6日
     */
    @Override
    public ResponsePage<ResBMSReassignmentVo> getLoanNoListPage(ReqBMSReassignmentVo request, RequestPage requestPage) {
        StopWatch stopWatch = new StopWatch();
        ResponsePage<ResBMSReassignmentVo> page = new ResponsePage<ResBMSReassignmentVo>();
        request.setSysCode(sysCode);
        request.setLoginPersonCode(ShiroUtils.getAccount());
        changeSort(requestPage);
        request.setFieldSort(requestPage.getSort());
        request.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        request.setFuncCode(ShiroUtils.getShiroUser().getFuncCode());
        String startDate = request.getXsStartDate();
        String endDate = request.getXsEndDate();
        if (StringUtils.isNotEmpty(startDate)) {
            startDate = startDate + " 00:00:00";
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        request.setXsStartDate(startDate);
        request.setXsEndDate(endDate);
        String caseType = request.getCaseType();
        request.setCaseType(null);
        if (StringUtils.isNotEmpty(caseType)) {
            List<String> type = Arrays.asList(caseType.split(","));
            request.setCaseIdentifyList(type);
        }
        stopWatch.start("改派查询接口耗时");
        PageResponse<ResBMSReassignmentVo> pageList = firstDispatchExecuter.getReassignmentList(request);
        stopWatch.stop();
        if (null != pageList && pageList.isSuccess()) {
            page.setRows(pageList.getRecords());
            page.setTotal(pageList.getTotalCount());
        } else {
            LOGGER.error("改派查询接口 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(pageList));
            throw new BusinessException("改派查询异常");
        }
        LOGGER.info(stopWatch.prettyPrint());
        return page;
    }

    /**
     * 批量改派
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月8日
     */
    @Override
    public Result<ResReassignmentUpdVO> updateReform(ReqBMSReassignmentBatchVo request) {
        Response<List<ResReassignmentUpdVO>> response = new Response<List<ResReassignmentUpdVO>>();
        Result<ResReassignmentUpdVO> result = new Result<ResReassignmentUpdVO>(Type.FAILURE);
        request.setSysCode(sysCode);
        request.setOperatorCode(ShiroUtils.getAccount());
        request.setOperatorName(ShiroUtils.getCurrentUser().getName());
        response = firstDispatchExecuter.batchReassignment(request);
        LOGGER.info("信审初审改派 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            result.setType(Type.SUCCESS);
            result.setDataList(response.getData());
            result.addMessage("批量改派成功");
        } else {
            result.addMessage("批量改派失败");
        }
        return result;
    }

    /**
     * edit by zw at 2017-05-06 批量退回或拒绝
     *
     * @return
     * @author dmz
     * @date 2017年4月16日
     */
    @Override
    public Result<String> updateReturnOrRejectService(ReqCsRefusePlupdateStatusVO request, String type) {
        // 添加初审改派拒绝日志
        applyHistoryService.saveReturnOrRejectApplyHistory(request, "FirstReform_" + type, type);// 2017-05-04批量退回或拒绝审批日志写入
        Result<String> result = new Result<String>(Type.FAILURE);
        request.setSysCode(sysCode);
        request.setOperatorCode(ShiroUtils.getAccount());
        request.setOperatorName(ShiroUtils.getCurrentUser().getName());
        Response<List<ResReassignmentUpdVO>> response = null;
        if (EnumConstants.RtfNodeState.CSFPREJECT.getValue().equals(type)) {
            LOGGER.info("执行批量拒绝");
            // 先执行工作流
            ReqCsRefuseUpdStatusVO rcrusVo = request.getList().get(0);
            Task task = taskMapper.findByBusinessId(rcrusVo.getLoanNo());
            if (task != null) {
                taskService.deny(task.getId());
            }
            response = firstDispatchExecuter.batchReject(request);
        } else if (EnumConstants.RtfNodeState.CSFPCANCEL.getValue().equals(type)) {
            LOGGER.info("执行批量退回");
            if (CollectionUtils.isNotEmpty(request.getReturnReasons())) {
                request.setFirstLevelReasonCode(null);
                request.setFirstLevelReasons(null);
                request.setTwoLevelReasonCode(null);
                request.setTwoLevelReasons(null);
            }
            response = firstDispatchExecuter.batchBack(request);
        }
        LOGGER.info("批量退回或拒绝  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && null != response.getData() && null != response.getData().get(0) && response.getData().get(0).isIfSuccessful()) {
            LOGGER.info("批量退回或拒绝成功");
            result.setType(Type.SUCCESS);
        } else {
            LOGGER.info("批量退回或拒绝失败,借款号{}", request.getList().get(0).getLoanNo());
            throw new BusinessException("批量退回或拒绝失败");
        }
        return result;
    }

    /**
     * 借款日志查询
     *
     * @param ip
     * @return
     * @author dmz
     * @date 2017年4月14日
     */
    @Override
    public List<ResBMSQueryLoanLogVO> queryLoanLog(String loanNo, String ip) {
        List<ResBMSQueryLoanLogVO> result = new ArrayList<ResBMSQueryLoanLogVO>();
        ReqQueryLoanLogVO request = new ReqQueryLoanLogVO();
        request.setIp(ip);
        request.setLoanNo(loanNo);
        request.setServiceCode(ShiroUtils.getAccount());
        request.setServiceName(ShiroUtils.getCurrentUser().getName());
        request.setSysCode(sysCode);
        Response<List<ResBMSQueryLoanLogVO>> response = integrateSearchExecuter.queryLoanLog(request);
        LOGGER.info("借款日志查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result = response.getData();
        } else {
            throw new BusinessException("借款日志查询异常");
        }
        return result;
    }

    /**
     * add by zw at 2017-04-24 根据借款编号查询只读的借款信息
     *
     * @param loanNo
     * @return
     * @author zw
     */
    @Override
    public ApplyEntryVO getBMSLoanInfoOnlyReadByLoanNoService(String loanNo) {
        ApplyEntryVO applyVo = new ApplyEntryVO();
        PersonHistoryLoanVO request = new PersonHistoryLoanVO();
        request.setLoanNo(loanNo);
        request.setSysCode(sysCode);
        Response<ApplyEntryVO> response = applyInfoExecuter.getApplyDetailReplaceEnum(request);
        LOGGER.info("借款单号：{} 查询只读的借款信息 params:{} result:{}", loanNo, JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            applyVo = response.getData();
        } else {
            throw new BusinessException("查询只读借款信息异常");
        }
        return applyVo;
    }
    // end at 2017-04-24


    /**
     * 根据借款编号获取前前只读信息
     *
     * @param loanNo
     * @param needReplaceCustomerEnum-客户信息是否转换枚举
     * @param needReplaceAssetsEnum-资产信息是否转换枚举
     * @return
     */
    @Override
    public ResApplicationInfoVO getMoneyLoanInfoDetail(String loanNo,boolean needReplaceCustomerEnum, boolean needReplaceAssetsEnum) {
        ResApplicationInfoVO result = null;
        com.ymkj.ams.api.vo.request.integrate.application.ReqApplicationInfoVO request = new com.ymkj.ams.api.vo.request.integrate.application.ReqApplicationInfoVO();
        request.setLoanNo(loanNo);
        request.setNeedReplaceCustomerEnum(needReplaceCustomerEnum);
        request.setNeedReplaceAssetsEnum(needReplaceAssetsEnum);
        Response<ResApplicationInfoVO> response = applicationInfoExecuter.getDetail(request);
        if (null != response && response.isSuccess() && null != response.getData()) {
            result = response.getData();
        } else {
            LOGGER.error("根据借款编号获取前前只读信息异常 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("系统忙,请稍后!");
        }
        return result;
    }

    /**
     * 算话反欺诈评分上传数据查询
     */
    @Override
    public ResBMSAduitPersonVo getBMSLoanInfoByloanNo(String loanNo) {
        ReqBMSAduitPersonVo request = new ReqBMSAduitPersonVo();
        request.setSysCode(sysCode);
        request.setLoanNo(loanNo);
        // 查询算话反欺诈评分需要使用的借款信息
        Response<ResBMSAduitPersonVo> response = applyInfoExecuter.getApplyPersonInfo(request);
        ResBMSAduitPersonVo vo = null;
        if (null != response && response.isSuccess()) {
            vo = response.getData();
        } else {
            throw new BusinessException("算话反欺诈评分上传数据查询异常");
        }
        return vo;
    }

    /**
     * 查询信审初审待办任务数
     *
     * @return
     * @author dmz
     * @date 2017年5月23日
     */
    @Override
    public Result<Integer> getFirstTaskNumber() {
        Result<Integer> result = new Result<Integer>(Type.FAILURE);
        ReqCsUpdVO request = new ReqCsUpdVO();
        request.setSysCode(sysCode);
        request.setCsPersonCode(ShiroUtils.getAccount());
        Response<Integer> response = taskNumberExecuter.getBacklogAmountOfFirst(request);
        LOGGER.info("查询初审待办任务数  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setData(response.getData());
            result.setType(Type.SUCCESS);
        } else {
            throw new BusinessException("查询初审代办任务数异常");
        }
        return result;
    }

    /**
     * 查询信审终审待办任务数
     *
     * @return
     * @author dmz
     * @date 2017年5月23日
     */
    @Override
    public Result<Integer> getFinalTaskNumber() {
        Result<Integer> result = new Result<Integer>(Type.FAILURE);
        ReqZsUpdVO request = new ReqZsUpdVO();
        request.setSysCode(sysCode);
        request.setZsPersonCode(ShiroUtils.getAccount());
        Response<Integer> response = taskNumberExecuter.getBacklogAmountOfFinal(request);
        LOGGER.info("查询终审待办任务数  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setData(response.getData());
            result.setType(Type.SUCCESS);
        } else {
            throw new BusinessException("查询终审代办任务数异常");
        }
        return result;
    }

    @Override
    public PageResponse<ResBMSReassignmentVo> getFinishReformList(ReqBMSReassignmentVo request, RequestPage requestPage) {
        request.setFlag(EnumConstants.ReqFlag.ZS.getValue());
        request.setSysCode(sysCode);
        request.setPageNum(requestPage.getPage());
        request.setPageSize(requestPage.getRows());
        changeSort(requestPage);
        request.setFieldSort(requestPage.getSort());
        request.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        request.setLoginPersonCode(ShiroUtils.getCurrentUser().getUsercode());
        long a = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------终审改派bms查询开始时间：" + a);
        PageResponse<ResBMSReassignmentVo> response = finalDispatchExecuter.getReassignmentList(request);
        long b = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------终审改派bms查询结束时间：" + b);
        LOGGER.info("------------------------------------------------------终审改派bms查询用时：" + (b - a) + "ms");
        LOGGER.info("终审改派工作台查询队列信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        return response;
    }

    @Override
    public Result<String> updateCustomerContactInfo(CustomerContactInfoVO customerContactInfoVO) {
        Result<String> result = new Result<String>(Result.Type.FAILURE);
        ReqApplyPhoneVO request = new ReqApplyPhoneVO();
        request.setSysCode(sysCode);
        request.setLoanNo(customerContactInfoVO.getLoanNo());
        request.setCellphone(customerContactInfoVO.getCellphone());
        request.setCellphoneSec(customerContactInfoVO.getCellphoneSec());
        request.setCorpPhone(customerContactInfoVO.getCorpPhone());
        request.setCorpPhoneSec(customerContactInfoVO.getCorpPhoneSec());
        Response<Object> response = contactInfoExecuter.modifyContactWay(request);
        LOGGER.info("更新本人的联系方式  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            if (response.isSuccess()) {
                result.setType(Result.Type.SUCCESS);
                result.addMessage("修改成功");
            } else {
                result.addMessage("更新失败");
            }
        } else {
            result.addMessage("更新失败");
        }
        //
        return result;

    }

    /**
     * 根据借款编号校验客户信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月23日
     */
    @Override
    public Result<String> validateApplyInfo(String loanNo) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("验证客户信息必填项");
        Result<String> result = new Result<String>(Type.FAILURE);
        AuditAmendEntryVO request = new AuditAmendEntryVO();
        request.setSysCode(sysCode);
        request.setLoanNo(loanNo);
        Response<Object> response = applyInfoExecuter.validateApplyInfo(request);
        LOGGER.info("验证客户信息必填项 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response) {
            if (response.isSuccess()) {
                result.setType(Type.SUCCESS);
            } else {
                result.addMessage(response.getRepMsg());
            }
        }
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
        return result;
    }
    /**
     * 根据借款编号校验前前客户信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月23日
     */
    @Override
    public Result<String> validateMoneyApplyInfo(String loanNo) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("验证前前客户信息必填项");
        Result<String> result = new Result<String>(Type.FAILURE);
        Response<Boolean> response = applicationInfoExecuter.validate(loanNo);
        LOGGER.info("验证前前客户信息必填项 params:{} result:{}", loanNo , JSON.toJSONString(response));
        if (null != response) {
            if (response.isSuccess() && response.getData()) {
                result.setType(Type.SUCCESS);
            } else {
                result.addMessage(response.getRepMsg());
            }
        }
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
        return result;
    }

    /**
     * 根据原因码获取原因(规则引擎)
     *
     * @param reasonCode
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    @Override
    public Result<ReasonVO> queryReason(String reasonCode) {
        Result<ReasonVO> result = new Result<ReasonVO>(Type.FAILURE);
        ReasonVO reasonVO = new ReasonVO();
        reasonVO.setSysCode(sysCode);
        reasonVO.setQueryReasonCode(reasonCode);
        Response<ReasonVO> response = reasonExecuter.getReason(reasonVO);
        LOGGER.info("根据原因码获取原因(规则引擎) params:{} result:{}", JSON.toJSONString(reasonVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.setData(response.getData());
        } else {
            throw new BusinessException("根据原因码获取原因(规则引擎)异常");
        }
        return result;
    }

    /**
     * 插入规则引擎返回值6个(规则引擎)
     *
     * @param ruleEngineVO
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    @Override
    public Result<String> auditUpdateRulesData(RuleEngineVO ruleEngineVO) {
        Result<String> result = new Result<String>(Type.FAILURE);
        AuditRulesVO auditRulesVO = new AuditRulesVO();
        BeanUtils.copyProperties(ruleEngineVO, auditRulesVO);
        if (Strings.isNotEmpty(ruleEngineVO.getIfCreditRecord())) {
            auditRulesVO.setIfCreditRecord("Y".equals(ruleEngineVO.getIfCreditRecord()) ? "1" : "0");
        }
        auditRulesVO.setSysCode(sysCode);
        Response<Object> response = ruleEngineExecuter.modifyRuleEngineData(auditRulesVO);
        LOGGER.info("插入规则引擎返回值(规则引擎) params:{} result:{}", JSON.toJSONString(auditRulesVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
        } else {
            throw new BusinessException("规则引擎返回值保存异常");
        }
        return result;
    }

    /**
     * 根据借款编号查询最新规则引擎返回的值(规则引擎)
     *
     * @param loanNo
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    @Override
    public Result<AuditRulesVO> queryAuditRulesData(String loanNo) {
        Result<AuditRulesVO> result = new Result<AuditRulesVO>(Type.FAILURE);
        AuditRulesVO auditRulesVO = new AuditRulesVO();
        auditRulesVO.setSysCode(sysCode);
        auditRulesVO.setLoanNo(loanNo);
        Response<AuditRulesVO> response = ruleEngineExecuter.getRuleEngineData(auditRulesVO);
        LOGGER.info("根据借款编号查询最新规则引擎返回的值(规则引擎) params:{} result:{}", JSON.toJSONString(auditRulesVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.setData(response.getData());
        } else {
            throw new BusinessException("借款编号查询最新规则引擎返回的值(规则引擎)异常");
        }
        return result;
    }

    /**
     * 根据借款单号修改该借款央行报告是否重绑
     *
     * @param loanNo
     * @author zhouwen
     * @date 2017年7月15日
     */
    @Override
    public void updateReportIdByLoanNo(String loanNo) {
        ReqBMSAppPersonInfoVO request = new ReqBMSAppPersonInfoVO();
        request.setSysCode(sysCode);
        request.setLoanNo(loanNo);
        Response<ResBMSAppPersonInfoVO> response = firstApproveExecuter.updateStatusOfCentralBankId(request);
        LOGGER.info("根据借款编号更新央行报告ID是否重新绑定值为N params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null == response || !response.isSuccess()) {
            throw new BusinessException("根据借款编号更新央行报告ID是否重新绑定值为N异常");
        }
    }

    /**
     * 排序字段转换
     *
     * @param requestPage
     * @return
     */
    public RequestPage changeSort(RequestPage requestPage) {
        if ("loanNoTopClass".equals(requestPage.getSort())) {// 申请件层级
            requestPage.setSort("accLmt");
        } else if ("owningBranchName".equals(requestPage.getSort())) {
            requestPage.setSort("owningBrance");
        }
        return requestPage;
    }

    /**
     * 查询在网时长和实名认证绑定的ID
     *
     * @param loanNo
     * @return
     * @author wulj
     */
    @Override
    public List<MobileOnline> getCreditReportInfo(String loanNo) {
        List<MobileOnline> mobileOnlineList = Lists.newArrayList();

        ReqCsUpdVO reqVo = new ReqCsUpdVO();
        reqVo.setLoanNo(loanNo);
        reqVo.setSysCode(sysCode);
        Response<Object> response = firstApproveExecuter.getHzReportInfo(reqVo);
        LOGGER.info("根据借款编号，查询在网时长及实名认证 params:{}, result:{}", JSON.toJSONString(reqVo), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            Map<String, String> data = (Map<String, String>) response.getData();

            JSONObject mobileOnlineID = JSONObject.parseObject(data.get("mobileOnlineID"));
            JSONObject idCardCheckID = JSONObject.parseObject(data.get("idCardCheckID"));

            if (null != mobileOnlineID) {
                // 获取手机在网时长ID
                Iterator<String> iterator1 = mobileOnlineID.keySet().iterator();
                while (iterator1.hasNext()) {
                    MobileOnline mobileOnline = new MobileOnline();

                    String mobile = (String) iterator1.next();
                    Long mobileCreditId = mobileOnlineID.getLong(mobile);

                    mobileOnline.setLoanNo(loanNo);
                    mobileOnline.setMobile(mobile);
                    mobileOnline.setMobileOnlineId(mobileCreditId);

                    if (null != idCardCheckID) {
                        Long idCardCreditId = idCardCheckID.getLong(mobile);
                        mobileOnline.setRealCertiId(idCardCreditId); // 获取实名认证
                        idCardCheckID.remove(mobile); // 删除实名认证中重复的手机号
                    }

                    mobileOnlineList.add(mobileOnline);
                }
            }

            if (null != idCardCheckID) {
                // 获取手机实名认证ID
                Iterator<String> iterator2 = idCardCheckID.keySet().iterator();
                while (iterator2.hasNext()) {
                    MobileOnline mobileOnline = new MobileOnline();

                    String mobile = (String) iterator2.next();
                    Long idCardCreditId = idCardCheckID.getLong(mobile);

                    mobileOnline.setLoanNo(loanNo);
                    mobileOnline.setMobile(mobile);
                    mobileOnline.setRealCertiId(idCardCreditId);

                    mobileOnlineList.add(mobileOnline);
                }
            }
        }

        return mobileOnlineList;
    }

    /**
     * 更新借款"入网时长"和"实名认证"信息
     *
     * @param loanNo
     * @param name
     * @param idNo
     * @param longOnlineIdJson
     * @param realNameAuthIdJson
     * @return
     */
    @Override
    public Result<String> syncHZReportID(String loanNo, String name, String idNo, String longOnlineIdJson, String realNameAuthIdJson) {
        Result<String> result = new Result<String>(Result.Type.FAILURE);

        ReqCsUpdVO request = new ReqCsUpdVO();
        request.setSysCode(sysCode);
        request.setLoanNo(loanNo);
        request.setName(name);
        request.setIdNo(idNo);
        request.setLongOnlineId(longOnlineIdJson);
        request.setRealNameAuthId(realNameAuthIdJson);

        Response<Object> response = firstApproveExecuter.modifyHzReportInfo(request);
        LOGGER.info("更新借款入网时长和实名认证 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            if (response.isSuccess()) {
                result.setType(Result.Type.SUCCESS);
                result.addMessage("修改成功");
            } else {
                result.addMessage("更新失败");
            }
        } else {
            result.addMessage("更新失败");
        }

        return result;
    }

    /**
     * 规则引擎执行拒绝(初审、终审派单)
     *
     * @param reqZsUpdVO
     * @return
     * @author dmz
     * @date 2017年9月4日
     */
    @Override
    public boolean ruleEngineReject(ReqZsUpdVO reqZsUpdVO) {
        boolean action = false;
        reqZsUpdVO.setSysCode(sysCode);
        reqZsUpdVO.setOperatorCode("系统");
        Response<Object> response = ruleEngineExecuter.ruleEngineReject(reqZsUpdVO);
        LOGGER.info("规则引擎执行拒绝(初审、终审派单) params:{} result:{}", JSON.toJSONString(reqZsUpdVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            action = true;
        } else {
            throw new BusinessException("规则引擎执行拒绝异常");
        }
        return action;
    }

    @Override
    public List<ResBMSAutomaticDispatchAttrVO> getFirstDispatchOrders() {
        ReqAutomaticDispatchVO request = new ReqAutomaticDispatchVO();
        request.setSysCode(sysCode);
        ResBMSAutomaticDispatchVO response = firstDispatchExecuter.getDispatchList(request);
        LOGGER.debug("初审待分派申请件查询 request:{} response:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode()) && response.getData() != null) {
            return response.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<ResBMSAutomaticDispatchAttrVO> getFinalDispatchOrders() {
        ReqAutomaticDispatchVO request = new ReqAutomaticDispatchVO();
        request.setSysCode(sysCode);
        ResBMSAutomaticDispatchVO response = finalDispatchExecuter.getDispatchList(request);
        LOGGER.info("终审待分派申请件查询 request:{} response:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return response.getData();
        }

        return Lists.newArrayList();
    }

    @Override
    public Result<String> deleteContactInfo(ReqAuditDifferencesVO request) {
        Result<String> result = new Result<>(Type.FAILURE);
        request.setSysCode(sysCode);
        Response<Long> response = applyEnterExecuter.deleteApplyContactInfo(request);
        LOGGER.info("删除联系人  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (response != null && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.addMessage("删除联系人成功！");
        } else {
            result.addMessage("删除联系人失败！");
        }
        return result;
    }

    @Override
    public List<ReqBMSTMReasonVO> findReasonByOperType(ReqBMSTMReasonVO req) {
        List<ReqBMSTMReasonVO> returnList = new ArrayList<ReqBMSTMReasonVO>();
        String reasonInfoRedisId = "REASON" + "-" + req.getOperationType() + "-" + req.getOperationModule();
        if (redisUtil.exists(reasonInfoRedisId)) {
            returnList = JSONObject.parseArray(redisUtil.get(reasonInfoRedisId).toString(), ReqBMSTMReasonVO.class);
        } else {
            ResListVO<ReqBMSTMReasonVO> res = reasonExecuter.getReasonByModuleAndType(req);
            LOGGER.info("所有拒绝或者退回原因查询 request:{} response:{}", JSON.toJSONString(req), JSON.toJSONString(res));
            if (res.getSize() > 0) {
                List<ReqBMSTMReasonVO> temlist = new ArrayList<ReqBMSTMReasonVO>();
                temlist = res.getCollections();
                ReqBMSTMReasonVO vo = new ReqBMSTMReasonVO();
                vo.setId(0L);
                vo.setText("reject".equals(req.getOperationType()) ? "所有拒绝原因" : "所有退回原因");
                vo.setCode("reject".equals(req.getOperationType()) ? "RJ-ALL" : "RT-ALL");
                vo.setChildren(convertReasonList(temlist));
                returnList.add(vo);
                redisUtil.set(reasonInfoRedisId, JSONObject.toJSONString(returnList), 600L);
            }
        }
        return returnList;
    }

    /**
     * 把原因的list重新排序，没有二级原因的一级原因排到前面
     *
     * @param temlist
     * @return
     * @author JiaCX
     * @date 2017年9月21日 上午9:53:06
     */
    private List<ReqBMSTMReasonVO> convertReasonList(List<ReqBMSTMReasonVO> temlist) {
        List<ReqBMSTMReasonVO> returnList = new ArrayList<ReqBMSTMReasonVO>();
        List<ReqBMSTMReasonVO> hasNotReasonB = new ArrayList<ReqBMSTMReasonVO>();
        List<ReqBMSTMReasonVO> hasReasonB = new ArrayList<ReqBMSTMReasonVO>();
        if (CollectionUtils.isNotEmpty(temlist)) {
            for (ReqBMSTMReasonVO vo : temlist) {
                if (CollectionUtils.isEmpty(vo.getChildren())) {
                    hasNotReasonB.add(vo);
                } else {
                    hasReasonB.add(vo);
                }
            }
        }
        returnList.addAll(hasNotReasonB);
        returnList.addAll(hasReasonB);
        return returnList;
    }

    /**
     * 判断用户和身份证是否有改变
     *
     * @param applyBasiceInfo
     * @return
     * @author dmz
     */
    @Override
    public Result<Boolean> judgeCustomerOrIDNOChange(ReqInformationVO applyBasiceInfo) {
        Result<Boolean> result = new Result<Boolean>(Type.FAILURE);
        ReqAuditVo request = new ReqAuditVo();
        request.setLoanNo(applyBasiceInfo.getLoanNo());
        request.setIdNo(applyBasiceInfo.getIdNo());
        request.setName(applyBasiceInfo.getName());
        request.setSysCode(sysCode);
        Response<Object> response = applyInfoExecuter.ifIdCardChanged(request);
        LOGGER.info("判断用户姓名或省份证是否有改变 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (null != response.getData() && "false".equals(response.getData().toString())) {
                result.setData(true);
            }
            result.setType(Type.SUCCESS);
        }
        return result;
    }

    @Override
    public void getQueenNum(List<TaskNumber> taskNumbers) {
        StopWatch stw = new StopWatch();
        stw.start("查询借款系统队列数");

        ReqBMSTaskNumberVo requset = new ReqBMSTaskNumberVo();
        List<PersonCodeAndRoleVo> personCodeAndRoleList = new ArrayList<PersonCodeAndRoleVo>();
        if (CollectionUtils.isNotEmpty(taskNumbers)) {
            for (TaskNumber taskNumber : taskNumbers) {
                PersonCodeAndRoleVo personCodeAndRoleVo = new PersonCodeAndRoleVo();
                personCodeAndRoleVo.setPersonCode(taskNumber.getStaffCode());
                if (taskNumber.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue())) {
                    personCodeAndRoleVo.setPersonRole(EnumUtils.approvalType.FIRST.getValue());
                } else {
                    personCodeAndRoleVo.setPersonRole(EnumUtils.approvalType.FINAL.getValue());
                }
                personCodeAndRoleList.add(personCodeAndRoleVo);
            }
            requset.setPersonCoAndRos(personCodeAndRoleList);
            requset.setSysCode(sysCode);
            ResBMSTaskNumberVo response = taskNumberExecuter.getQueueAmount(requset);
            LOGGER.info("借款队列数查询 params:{} result:{}", JSON.toJSONString(requset), JSON.toJSONString(response));
            if (response.isSuccess() && CollectionUtils.isNotEmpty(response.getTaskNumQues())) {
                List<TaskNumberQueVo> taskNumQues = response.getTaskNumQues();
                for (TaskNumberQueVo taskNumberVo : taskNumQues) {
                    for (TaskNumber taskNumber : taskNumbers) {
                        String role = taskNumber.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue()) ? EnumUtils.approvalType.FIRST.getValue() : EnumUtils.approvalType.FINAL.getValue();
                        if (taskNumberVo.getPersonCode().equals(taskNumber.getStaffCode()) && taskNumberVo.getPersonRole().equals(role)) {
                            taskNumber.setCurrPriorityNum(taskNumberVo.getPriorityQueue());
                            taskNumber.setCurrActivieTaskNum(taskNumberVo.getNormalQueue());
                            taskNumber.setCurrInactiveTaskNum(taskNumberVo.getHangQueue());
                        }
                    }
                }
            }
        }

        stw.stop();

        LOGGER.info(stw.prettyPrint());
    }

    /**
     * 查询队列数
     *
     * @param params 员工列表
     * @return
     */
    @Override
    public List<TaskNumberQueVo> getTaskNumber(List<PersonCodeAndRoleVo> params) {
        List<TaskNumberQueVo> taskNumberQueVoList = Lists.newArrayList();

        ReqBMSTaskNumberVo request = new ReqBMSTaskNumberVo();
        request.setPersonCoAndRos(params);
        request.setSysCode(sysCode);
        ResBMSTaskNumberVo response = taskNumberExecuter.getQueueAmount(request);
        LOGGER.info("借款队列数查询 method:{}, params:{}, result:{}", "getTaskNumber", JSON.toJSONString(request), JSON.toJSONString(response));
        if (response.isSuccess() && CollectionUtils.isNotEmpty(response.getTaskNumQues())) {

            taskNumberQueVoList = response.getTaskNumQues();
        }

        return taskNumberQueVoList;
    }

    /**
     * 查询队列数
     *
     * @param staffCode 工号
     * @param taskDef   {@link EnumUtils.FirstOrFinalEnum}
     * @return
     */
    @Override
    public TaskNumberQueVo getTaskNumberByStaffCodeAndTaskDef(String staffCode, String taskDef) {
        ReqBMSTaskNumberVo request = new ReqBMSTaskNumberVo();

        PersonCodeAndRoleVo personCodeAndRoleVo = new PersonCodeAndRoleVo();
        personCodeAndRoleVo.setPersonCode(staffCode);
        if (taskDef.equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue())) {
            personCodeAndRoleVo.setPersonRole(EnumUtils.approvalType.FIRST.getValue());
        } else {
            personCodeAndRoleVo.setPersonRole(EnumUtils.approvalType.FINAL.getValue());
        }

        request.setPersonCoAndRos(Lists.newArrayList(personCodeAndRoleVo));
        request.setSysCode(sysCode);
        ResBMSTaskNumberVo response = taskNumberExecuter.getQueueAmount(request);
        LOGGER.info("借款队列数查询 method:{}, params:{}, result:{}", "getTaskNumberByStaffCodeAndTaskDef", JSON.toJSONString(request), JSON.toJSONString(response));
        if (response.isSuccess() && CollectionUtils.isNotEmpty(response.getTaskNumQues())) {

            return response.getTaskNumQues().get(0);
        }

        return null;
    }

    /**
     * 获取借款信息备份版本
     *
     * @param loanNo-借款编号
     * @param flag-1:初审   2:终审
     * @author dmz
     */
    @Override
    public String getLoanInfoBackup(String loanNo, String flag) {
        String action = null;
        ReqAuditVo reqAuditVo = new ReqAuditVo();
        reqAuditVo.setFlag(flag);
        reqAuditVo.setLoanNo(loanNo);
        reqAuditVo.setSysCode(sysCode);
        Response<String> response = applyInfoExecuter.getLastedSnapshoot(reqAuditVo);
        LOGGER.info("获取借款信息备份版本 params:{} result:{}", JSON.toJSONString(reqAuditVo), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (Strings.isNotEmpty(response.getData())) {
                action = response.getData();
            }
        }
        return action;
    }

    /**
     * 根据字段名称获取对应的历史信息(目前只获取央行报告id)
     *
     * @param key-字段名称
     * @return
     */
    @Override
    public String getLoanInfoHistoryColunm(String loanNo, String flag, String key) {
        String action = null;
        String loanInfo = getLoanInfoBackup(loanNo, flag);
        if (null != loanInfo) {
            JSONObject jsonObject = JSON.parseObject(loanInfo);
            if (jsonObject.containsKey("basicInfoVO")) {
                JSONObject basicInfoVO = jsonObject.getJSONObject("basicInfoVO");
                if (basicInfoVO.containsKey("personInfoVO")) {
                    JSONObject personInfoVO = basicInfoVO.getJSONObject("personInfoVO");
                    if (personInfoVO.containsKey(key)) {
                        action = personInfoVO.getString(key);
                    }
                }
            }
        }
        return action;
    }

    @Override
    public List<JSONArray> combineContactInfo(String loanNo,String name, ResOrganizationInfo org, int type) {
        List<JSONArray> list = new ArrayList<JSONArray>();
        ApplyEntryVO oriInfo = getBMSLoanInfoByLoanNoService(loanNo);//未转为汉字枚举的原始数据

        //本人信息组装
        ContactInfoVO personalContact = newContactInfoVO(loanNo,name, oriInfo);
        convertContactInfo(org, list, personalContact, type);

        //联系人信息组装
        List<ContactInfoVO> contactInfoVOList = oriInfo.getContactInfoVOList();
        if (CollectionUtils.isNotEmpty(contactInfoVOList)) {
            for (ContactInfoVO contactInfoVO : contactInfoVOList) {
                convertContactInfo(org, list, contactInfoVO, type);
            }
        }

        return list;
    }

    /**
     * 钱钱组装联系人信息列表(给电核汇总用)
     *
     * @param loanNo-借款编号
     * @param name-申请人姓名
     * @param org
     * @param type      1:初审，2：终审
     * @return
     * @author Jia CX
     * @date 2017年11月30日 下午4:23:37
     * @notes
     */
    @Override
    public List<JSONArray> moneyCombineContactInfo(String loanNo,String name, ResOrganizationInfo org, int type) {
        List<JSONArray> list = new ArrayList<JSONArray>();
        ResApplicationInfoVO applyInfo = getMoneyLoanInfoDetail(loanNo,false,false);//未转为汉字枚举的原始数据
        //本人信息组装
        ContactInfoVO personalContact = moneyNewContactInfoVO(loanNo,name, applyInfo);
        convertContactInfo(org, list, personalContact, type);
        //联系人信息组装
        List<ResContactInfoVO> contactInfoVOList = applyInfo.getContactInfo();
        if (CollectionUtils.isNotEmpty(contactInfoVOList)) {
            for (ResContactInfoVO resContactInfoVO : contactInfoVOList) {
                ContactInfoVO contactInfoVO = new ContactInfoVO();
                BeanUtils.copyProperties(resContactInfoVO,contactInfoVO);
                moneyConvertContactInfo(org, list, contactInfoVO, type);
            }
        }
        return list;
    }

    private ContactInfoVO newContactInfoVO(String loanNo, String name, ApplyEntryVO oriInfo) {
        PersonInfoVO personInfo = oriInfo.getBasicInfoVO().getPersonInfoVO();
        WorkInfoVO workInfo = oriInfo.getBasicInfoVO().getWorkInfoVO();
        ContactInfoVO personalContact = new ContactInfoVO();
        personalContact.setLoanNo(loanNo);
        personalContact.setSequenceNum(0);
        personalContact.setIfKnowLoan("Y");
        personalContact.setContactName(name);
        personalContact.setContactRelation("00000");
        personalContact.setPhoneCity(personInfo.getPhoneCity());                //本人号码归属地json
        personalContact.setCarrier(personInfo.getCarrier());                    //本人号码运营商json
        personalContact.setContactCellPhone(personInfo.getCellphone());            //常用手机(手机1)
        personalContact.setContactCellPhone_1(personInfo.getCellphoneSec());    //备用手机(手机2)
        personalContact.setContactCorpPhone(workInfo.getCorpPhone());            //单位电话1(单电1)
        personalContact.setContactCorpPhone_1(workInfo.getCorpPhoneSec());        //单位电话2(单电2)
        personalContact.setHomePhone(personInfo.getHomePhone1());                //宅电
        return personalContact;
    }

    private ContactInfoVO moneyNewContactInfoVO(String loanNo, String name, ResApplicationInfoVO oriInfo) {
        ResPersonalInfoVO personInfo = oriInfo.getApplicantInfo().getPersonalInfo();
        ResWorkInfoVO workInfo = oriInfo.getApplicantInfo().getWorkInfo();
        ContactInfoVO personalContact = new ContactInfoVO();
        personalContact.setLoanNo(loanNo);
        personalContact.setSequenceNum(0);
        personalContact.setIfKnowLoan("Y");
        personalContact.setContactName(name);
        personalContact.setContactRelation("00000");
        personalContact.setPhoneCity(personInfo.getPhoneCity());                //本人号码归属地json
        personalContact.setCarrier(personInfo.getCarrier());                    //本人号码运营商json
        personalContact.setContactCellPhone(personInfo.getCellphone());            //常用手机(手机1)
        personalContact.setContactCellPhone_1(personInfo.getCellphoneSec());    //备用手机(手机2)
        personalContact.setContactCorpPhone(workInfo.getCorpPhone());            //单位电话1(单电1)
        personalContact.setContactCorpPhone_1(workInfo.getCorpPhoneSec());        //单位电话2(单电2)
        personalContact.setHomePhone(personInfo.getHomePhone1());                //宅电
        return personalContact;
    }

    /**
     * 组装联系人信息
     *
     * @param org
     * @param list
     * @param contact
     * @param type
     * @author Jia CX
     * @date 2017年12月1日 上午10:15:30
     * @notes
     */
    private void convertContactInfo(ResOrganizationInfo org, List<JSONArray> list, ContactInfoVO contact, int type) {
        /*
         * 终审字段中json是这种格式
		 * 
		 * {"18617713220":"联通","0123-1234567-112":"","021-75478888":"","13162508558":"联通","0123-1234567-123":""}
		 * {"18617713220":"河北省沧州市","0123-1234567-112":"","021-75478888":"上海市","13162508558":"上海市","0123-1234567-123":""}
		 */
        if (1 == type) {
            // 如果是初审，直接查询
            JSONArray arrays = PhoneUtil.setPhoneCity(contact, org.getProvince(), org.getCity(), true);
            list.add(arrays);
        } else {
            // 如果是终审，库里存了就转换展示，没存就直接查询
            JSONObject phoneCityJsonStr = JSONObject.parseObject(contact.getPhoneCity());//号码归属地json
            JSONObject phoneCarrierJsonStr = JSONObject.parseObject(contact.getCarrier());//号码运营商json
            if (null != phoneCityJsonStr) {
                Set<String> keys = phoneCityJsonStr.keySet();
                if (CollectionUtils.isNotEmpty(keys)) {
                    JSONArray array = JSONArray.parseArray("[null,null,null,null,null]");
                    for (String str : keys) {
                        ContactVO contactVO = BeanUtil.copyProperties(contact, ContactVO.class);
                        contactVO.setPhone(str);
                        contactVO.setCarrier(phoneCarrierJsonStr.getString(str));
                        contactVO.setPhoneCity(phoneCityJsonStr.getString(str));
                        //营业部城市与电话归属地对比
                        String pc = Strings.contactStr(PhoneUtil.handleSpecialPro(org.getProvince()), org.getCity());
                        if (Strings.isAllNotEmpty(phoneCityJsonStr.getString(str), pc) && phoneCityJsonStr.getString(str).contains(pc)) {
                            contactVO.setMatchBrach(true);
                        }
                        //判断该电话是本人的哪一个电话
                        if (str.equals(contact.getContactCellPhone())) {// 如果是常用手机(手机1)
                            contactVO.setRemark(AmsCode.PHONEENUM.常用手机.name());
                            array.set(0, contactVO);
                        } else if (str.equals(contact.getContactCellPhone_1())) {// 如果是备用手机(手机2)
                            contactVO.setRemark(AmsCode.PHONEENUM.备用手机.name());
                            array.set(1, contactVO);
                        } else if (str.equals(contact.getHomePhone())) {// 如果是宅电
                            contactVO.setRemark(AmsCode.PHONEENUM.宅电.name());
                            array.set(2, contactVO);
                        } else if (str.equals(contact.getContactCorpPhone())) {// 如果是单位电话1(单电1)
                            contactVO.setRemark(AmsCode.PHONEENUM.单位电话1.name());
                            array.set(3, contactVO);
                        } else if (str.equals(contact.getContactCorpPhone_1())) {// 如果是单位电话2(单电2)
                            contactVO.setRemark(AmsCode.PHONEENUM.单位电话2.name());
                            array.set(4, contactVO);
                        }
                    }
                    JSONArray arr = new JSONArray();
                    for (Object object : array) {
                        if (null != object) {
                            arr.add(object);
                        }
                    }
                    list.add(arr);
                }
            } else {
                JSONArray arrays = PhoneUtil.setPhoneCity(contact, org.getProvince(), org.getCity(), true);
                list.add(arrays);
            }
        }
    }

    /**
     * 组装联系人信息
     *
     * @param org
     * @param list
     * @param contact
     * @param type
     * @author Jia CX
     * @date 2017年12月1日 上午10:15:30
     * @notes
     */
    private void moneyConvertContactInfo(ResOrganizationInfo org, List<JSONArray> list, ContactInfoVO contact, int type) {
        /*
         * 终审字段中json是这种格式
		 *
		 * {"18617713220":"联通","0123-1234567-112":"","021-75478888":"","13162508558":"联通","0123-1234567-123":""}
		 * {"18617713220":"河北省沧州市","0123-1234567-112":"","021-75478888":"上海市","13162508558":"上海市","0123-1234567-123":""}
		 */
        if (1 == type) {
            // 如果是初审，直接查询
            JSONArray arrays = PhoneUtil.setPhoneCity(contact, org.getProvince(), org.getCity(), true);
            list.add(arrays);
        } else {
            // 如果是终审，库里存了就转换展示，没存就直接查询
            JSONObject phoneCityJsonStr = JSONObject.parseObject(contact.getPhoneCity());//号码归属地json
            JSONObject phoneCarrierJsonStr = JSONObject.parseObject(contact.getCarrier());//号码运营商json
            if (null != phoneCityJsonStr) {
                Set<String> keys = phoneCityJsonStr.keySet();
                if (CollectionUtils.isNotEmpty(keys)) {
                    JSONArray array = JSONArray.parseArray("[null,null,null,null,null]");
                    for (String str : keys) {
                        ContactVO contactVO = BeanUtil.copyProperties(contact, ContactVO.class);
                        contactVO.setPhone(str);
                        contactVO.setCarrier(phoneCarrierJsonStr.getString(str));
                        contactVO.setPhoneCity(phoneCityJsonStr.getString(str));
                        //营业部城市与电话归属地对比
                        String pc = Strings.contactStr(PhoneUtil.handleSpecialPro(org.getProvince()), org.getCity());
                        if (Strings.isAllNotEmpty(phoneCityJsonStr.getString(str), pc) && phoneCityJsonStr.getString(str).contains(pc)) {
                            contactVO.setMatchBrach(true);
                        }
                        //判断该电话是本人的哪一个电话
                        if (str.equals(contact.getContactCellPhone())) {// 如果是常用手机(手机1)
                            contactVO.setRemark(AmsCode.PHONEENUM.常用手机.name());
                            array.set(0, contactVO);
                        } else if (str.equals(contact.getContactCellPhone_1())) {// 如果是备用手机(手机2)
                            contactVO.setRemark(AmsCode.PHONEENUM.备用手机.name());
                            array.set(1, contactVO);
                        } else if (str.equals(contact.getHomePhone())) {// 如果是宅电
                            contactVO.setRemark(AmsCode.PHONEENUM.宅电.name());
                            array.set(2, contactVO);
                        } else if (str.equals(contact.getContactCorpPhone())) {// 如果是单位电话1(单电1)
                            contactVO.setRemark(AmsCode.PHONEENUM.单位电话1.name());
                            array.set(3, contactVO);
                        } else if (str.equals(contact.getContactCorpPhone_1())) {// 如果是单位电话2(单电2)
                            contactVO.setRemark(AmsCode.PHONEENUM.单位电话2.name());
                            array.set(4, contactVO);
                        }
                    }
                    JSONArray arr = new JSONArray();
                    for (Object object : array) {
                        if (null != object) {
                            arr.add(object);
                        }
                    }
                    list.add(arr);
                }
            } else {
                JSONArray arrays = PhoneUtil.setPhoneCity(contact, org.getProvince(), org.getCity(), true);
                list.add(arrays);
            }
        }
    }

    @Override
    public List<ContactInfoVO> queryContactInfoByLoanNo(String loanNo, String phone) {
        //封装接口请求参数
        ReqContactInfoVO request = new ReqContactInfoVO();
        request.setSysCode(sysCode);
        request.setLoanNo(loanNo);
        request.setContactCorpPhone(phone);
        request.setIp(WebUtils.getIp());
        request.setServiceCode(ShiroUtils.getCurrentUser().getUsercode()); // 工号
        request.setServiceName(ShiroUtils.getCurrentUser().getName()); // 用户名
        // 查询联系人信息
        Response<List<ContactInfoVO>> response = contactInfoExecuter.getContactInfoByLoanNo(request);
        LOGGER.info("查询联系人信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            LOGGER.info("=========根据借款编号和电话号码查询本次录入的联系人信息成功:{}=======", loanNo);
            return response.getData();
        }

        return null;
    }
}
