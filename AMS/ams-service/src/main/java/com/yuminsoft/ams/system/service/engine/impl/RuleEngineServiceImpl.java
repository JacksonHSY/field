package com.yuminsoft.ams.system.service.engine.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.audit.FinalApproveExecuter;
import com.ymkj.ams.api.service.approve.audit.FirstApproveExecuter;
import com.ymkj.ams.api.vo.request.apply.ReasonVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.ReqCsUpdVO;
import com.ymkj.ams.api.vo.request.audit.ReqZsUpdVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAudiUpdVo;
import com.ymkj.rule.biz.api.message.MapResponse;
import com.ymkj.rule.biz.api.message.Response;
import com.ymkj.rule.biz.api.message.RuleEngineRequest;
import com.ymkj.rule.biz.api.service.IRuleEngineExecuter;
import com.ymkj.rule.biz.api.vo.CreditCheckExecVo;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.RuleEngineParameterMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.approve.RuleEngineParameter;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckInfoService;
import com.yuminsoft.ams.system.service.approve.FinalHandleService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 规则引擎接口
 *
 * @author dmz
 * @date 2017年6月30日
 */
@Service
public class RuleEngineServiceImpl implements RuleEngineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineService.class);
    @Autowired
    private IRuleEngineExecuter ruleEngineExecuter;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Value("${sys.code}")
    private String sysCode;
    @Autowired
    private FirstApproveExecuter firstApproveExecuter;
    @Autowired
    private FinalApproveExecuter finalApproveExecuter;
    @Autowired
    private ApplyHistoryService applyValidateExecuter;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ApprovalHistoryService approvalHistoryService;
    @Autowired
    private FinalHandleService finalHandleService;
    @Autowired
    private CommonParamService commonParamService;
    @Autowired
    private RuleEngineParameterMapper ruleEngineParameterMapper;
    @Autowired
    private ApproveCheckInfoService approveCheckInfoService;

    /**
     * 调用规则引擎
     *
     * @param executeType-执行环节类型(枚举)
     * @return-Result<RuleEngineVO> 1.result 中的RuleEngineVO为规则引擎返回的六个值,如果有拒绝就包括拒绝原因码 2.result 中的massage 为规则引擎提示。默认第一个是规则引擎类型(EngineType 参考枚举值),第二个是规则引擎执行时间,其他为规则引擎提示值 3.目前除了系统初判需要返回规则引擎AuditRulesVO六个值以外其他都不用
     * @author dmz
     * @date 2017-06-30
     */
    @Override
    public Result<RuleEngineVO> handleNodeRuleEngine(ReqInformationVO applyBasiceInfo, EnumUtils.ExecuteTypeEnum executeType, String ip) {
        Result<RuleEngineVO> result = new Result<RuleEngineVO>(Type.FAILURE);
        result = executeRuleEngine(applyBasiceInfo.getLoanNo(), executeType);
        if (result.getSuccess()) {// 调用规则引擎成功
            if (EnumUtils.EngineType.REJECT.getValue().equals(result.getFirstMessage())) {// 拒绝
                ruleEngineRejectLoanNo(result.getData().getRejectCode(), applyBasiceInfo, ip);
            }
        }
        return result;
    }

    /**
     * 执行规则引擎
     *
     * @param loanNo-借款编号
     * @param executeType-执行环节类型(枚举)
     * @return Result<RuleEngineVO>1.result 中的RuleEngineVO为规则引擎返回的六个值,如果有拒绝就包括拒绝原因码 2.result 中的massage 为规则引擎提示。默认第一个是规则引擎类型(EngineType 参考枚举值),第二个是规则引擎执行时间,其他为规则引擎提示值 3.目前除了系统初判需要返回规则引擎AuditRulesVO六个值以外其他都不用
     * @author dmz
     * @date 2017年9月4日
     */
    @Override
    public Result<RuleEngineVO> executeRuleEngine(String loanNo, EnumUtils.ExecuteTypeEnum executeType) {
        StopWatch stopWatch = new StopWatch();
        Result<RuleEngineVO> result = new Result<RuleEngineVO>(Type.FAILURE);
        // 读取规则引擎开关配置
        stopWatch.start("读取规则引擎开关配置");
        Map<String, Object> mapSys = new HashMap<String, Object>();
        mapSys.put("paramKey", "systemRuleEngine");
        SysParamDefine sysParamDefine = commonParamService.findOne(mapSys);
        stopWatch.stop();
        if (null != sysParamDefine && "true".equals(sysParamDefine.getParamValue())) {
            RuleEngineRequest<CreditCheckExecVo> request = new RuleEngineRequest<CreditCheckExecVo>();
            request.setSysCode(sysCode);
            request.setBizType("creditCheck");
            CreditCheckExecVo vo = new CreditCheckExecVo();
            vo.setLoanNo(loanNo);
            vo.setExecuteType(executeType.name());
            vo.setRtfNodeState(executeType.getValue());
            request.setData(vo);
            stopWatch.start("调用规则引擎");
            Response response = ruleEngineExecuter.executeRuleEngine(request);
            stopWatch.stop();
            String executeTime = DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM);
            LOGGER.info("调用规则引擎 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            if (response != null && "000000".equals(response.getRepCode())) {
                MapResponse map = (MapResponse) response;
                result = handleResult(map.getMap(), loanNo, executeType);
            } else {
                throw new BusinessException("执行规则引擎异常");
            }
            result.addMessage(1, executeTime);// 保存执行时间
        } else {
            result.addMessage(EnumUtils.EngineType.PASS.getValue());
            result.addMessage(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM));// 保存执行时间
            result.setType(Type.SUCCESS);
            result.setData(new RuleEngineVO());
            LOGGER.info("未开启调用规则引擎");
        }
        LOGGER.info(stopWatch.prettyPrint());
        return result;
    }

    /**
     * 处理规则引擎返回结果
     *
     * @param mapResult
     * @param loanNo
     * @param executeType
     * @return
     * @author dmz
     */
    private Result<RuleEngineVO> handleResult(Map<String, Object> mapResult, String loanNo, EnumUtils.ExecuteTypeEnum executeType) {
        Result<RuleEngineVO> result = new Result<RuleEngineVO>(Type.FAILURE);
        RuleEngineVO ruleEngineVO = new RuleEngineVO();
        // 解析规则引擎返回值
        RuleEngineParameter ruleEngineParameter = getAuditRulesVO(mapResult);
        ruleEngineParameter.setLoanNo(loanNo);
        ruleEngineParameter.setExecuteType(executeType.name());
        String action = ruleEngineParameter.getEngineType();
        // 修改审批意见表
        if (null != ruleEngineParameter.getTotalDebtRatio() || null != ruleEngineParameter.getInternalDebtRatio()) {
            ApprovalHistory ah = null;
            if ("XSCS".equals(executeType.getValue())) {// 判断初审,并且排除定初审时任务
                ah = approvalHistoryService.getApprovalHistoryByLoanNoAndStaffCode(loanNo);
            } else if ("XSZS".equals(executeType.getValue())) {// 判断终审,并且排除定终审时任务
                ah = finalHandleService.getCurrentApprovalOption(loanNo);
            }
            if (null != ah) {// 执行修改审批意见
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", ah.getId());
                map.put("approvalAllDebtRate", ruleEngineParameter.getTotalDebtRatio());
                map.put("approvalDebtTate", ruleEngineParameter.getInternalDebtRatio());
                approvalHistoryService.updateResponsibleByLoanNo(map);
            }
        }
        //  更新资料核对表
        ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(loanNo);
        if (null != approveCheckInfo) {
            // 判断近一个月近三个月有信用记录是否有变更
            boolean updateMark = !approveCheckInfo.getOneMonthsCount().equals(ruleEngineParameter.getOneMonthsCount()) || !approveCheckInfo.getThreeMonthsCount().equals(ruleEngineParameter.getThreeMonthsCount());
            if (updateMark) {
                approveCheckInfo.setOneMonthsCount(ruleEngineParameter.getOneMonthsCount());
                approveCheckInfo.setThreeMonthsCount(ruleEngineParameter.getThreeMonthsCount());
                approveCheckInfoService.saveOrUpdate(approveCheckInfo);
            }
        }

        // 保存规则引擎返回值
        ruleEngineParameterMapper.insert(ruleEngineParameter);// 保存规则引擎值
        // 修改借款信息
        BeanUtils.copyProperties(ruleEngineParameter, ruleEngineVO);
        Result<String> resultUpdate = bmsLoanInfoService.auditUpdateRulesData(ruleEngineVO);// 将规则引擎返回的值保持到借款
        if (!resultUpdate.success()) {
            throw new BusinessException("保存规则引擎放回值异常");
        }
        // 判断规则引擎类型
        if (EnumUtils.EngineType.PASS.getKey().equals(action)) { // 通过
            result.addMessage(EnumUtils.EngineType.PASS.getValue());
        } else if (EnumUtils.EngineType.REJECT.getKey().equals(action)) { // 系统直接拒绝借款
            ruleEngineVO.setRejectCode(ruleEngineParameter.getRejectCode());
            result.addMessage(EnumUtils.EngineType.REJECT.getValue());
        } else if (EnumUtils.EngineType.LIMIT.getKey().equals(action)) { // 限制提交或者保存
            result.addMessage(EnumUtils.EngineType.LIMIT.getValue());
        } else if (EnumUtils.EngineType.FLAG.getKey().equals(action)) {// 标识--目前审核系统没用用到主要用在跑批调用
            result.addMessage(EnumUtils.EngineType.FLAG.getValue());
        } else if (EnumUtils.EngineType.HINT.getKey().equals(action)) { // 返回提示信息
            result.addMessage(EnumUtils.EngineType.HINT.getValue());
        } else if (EnumUtils.EngineType.CALLBACK.getKey().equals(action)) {// 第三方调用--目前没有用到后续需要调用征信系统接口
            result.addMessage(EnumUtils.EngineType.CALLBACK.getValue());
        }
        result.setType(Type.SUCCESS);// 设置调用成功
        iterationByKeyToString("hint", mapResult, 1, result);// 设置返回提示
        result.setData(ruleEngineVO);
        return result;
    }

    /**
     * 规则引擎拒绝操作
     *
     * @param rejectCode
     * @param applyBasiceInfo
     * @param ip·
     * @author dmz
     * @date 2017年7月4日
     */
    private void ruleEngineRejectLoanNo(String rejectCode, ReqInformationVO applyBasiceInfo, String ip) {
        LOGGER.info("规则引擎拒绝操作==============>>");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("根据原因码获取原因");
        Result<ReasonVO> resultReason = bmsLoanInfoService.queryReason(rejectCode);
        stopWatch.stop();
        if (resultReason.success()) {
            stopWatch.start("保存审核历史日志");
            ReasonVO reasonVO = resultReason.getData();
            // 记录日志
            ApplyHistory applyHistory = new ApplyHistory();
            applyHistory.setIdNo(applyBasiceInfo.getIdNo());// 身份证号
            applyHistory.setLoanNo(applyBasiceInfo.getLoanNo()); // 编号
            applyHistory.setName(applyBasiceInfo.getName());// 申请人
            applyHistory.setProName("规则引擎拒绝");
            applyHistory.setProNum(applyBasiceInfo.getLoanNo());
            applyHistory.setAutoRefuse(EnumUtils.YOrNEnum.Y.getValue());
            if (null != reasonVO.getFirstLevelReasonCode()) {
                applyHistory.setRefuseCode(reasonVO.getFirstLevelReasonCode());
            }
            if (null != reasonVO.getTwoLevelReasonCode()) {
                applyHistory.setRefuseCode(applyHistory.getRefuseCode() + "-" + reasonVO.getTwoLevelReasonCode());
            }
            applyHistory.setRtfState(applyBasiceInfo.getRtfState());
            if (EnumConstants.RtfState.XSCS.getValue().equals(applyHistory.getApprovalLeader())) {
                applyHistory.setRtfNodeState(EnumConstants.RtfNodeState.XSCSREJECT.getValue());
                applyHistory.setCheckNodeState(EnumConstants.ChcekNodeState.NOCHECK.getValue());
                applyHistory.setCheckPerson(ShiroUtils.getAccount());// 初审员
            } else {
                applyHistory.setRtfNodeState(EnumConstants.RtfNodeState.XSZSREJECT.getValue());
                applyHistory.setFinalPerson(ShiroUtils.getAccount());// 终审员
            }
            applyValidateExecuter.save(applyHistory);
            stopWatch.stop();
            // 调用借款拒绝
            ReqCsUpdVO request = new ReqCsUpdVO();
            request.setLoanNo(applyBasiceInfo.getLoanNo());
            request.setCheckNodeStatus(EnumConstants.ChcekNodeState.NOCHECK.getValue());
            request.setCsPersonCode(ShiroUtils.getAccount());
            request.setFirstLevelReasonCode(reasonVO.getFirstLevelReasonCode());
            request.setTwoLevelReasonCode(reasonVO.getTwoLevelReasonCode());
            request.setVersion(applyBasiceInfo.getVersion().intValue());
            request.setOperatorCode("规则引擎拒绝");
            request.setOperatorIP(ip);
            request.setSysCode(sysCode);
            request.setRemark("规则引擎拒绝");

            stopWatch.start("修改工作流");
            // 修改工作流
            Long taskId = taskMapper.findTaskIdByBusinessId(applyBasiceInfo.getLoanNo());
            if (taskId > 0) {// 派单拒绝有可能没有id
                taskService.deny(taskId);
            }
            stopWatch.stop();

            com.ymkj.base.core.biz.api.message.Response<ResBMSAudiUpdVo> response = null;
            if (EnumConstants.RtfState.XSCS.getValue().equals(applyBasiceInfo.getRtfState())) {// 初审拒绝
                stopWatch.start("规则引擎初审拒绝调用借款");
                response = firstApproveExecuter.reject(request);
                LOGGER.info("规则引擎初审拒绝调用借款 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
                stopWatch.stop();
            } else {// 终审拒绝
                stopWatch.start("规则引擎终审拒绝调用借款");
                ReqZsUpdVO reqZsUpdVo = new ReqZsUpdVO();
                BeanUtils.copyProperties(request, reqZsUpdVo);
                response = finalApproveExecuter.reject(reqZsUpdVo);// 拒绝
                LOGGER.info("规则引擎终审拒绝调用借款 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(reqZsUpdVo));
                stopWatch.stop();
            }
            if (null == response || !response.isSuccess()) {
                throw new BusinessException("规则引擎拒绝调用失败!");
            }
        } else {
            throw new BusinessException("规则引擎拒绝调用原因码失败!");
        }
        LOGGER.info(stopWatch.prettyPrint());
    }

    /**
     * 解析规则引擎返回值
     *
     * @param mapObj
     * @return
     * @author dmz
     * @date 2017年8月28日
     */
    private RuleEngineParameter getAuditRulesVO(Map<String, Object> mapObj) {
        RuleEngineParameter ruleEngineParameter = new RuleEngineParameter();
        // 定义参数名称和参数类型和RuleEngineParameter(list 默认分割符用"$") 对应的属性名称-(后面添加返回参数只需要修改vo和返回参数)
        Map<String, String[]> paramsMap = Maps.newHashMap();
        paramsMap.put("action", new String[]{"string", "engineType"});// 规则引擎返回类型--不保存到bms
        paramsMap.put("actionCode", new String[]{"string", "rejectCode"});// 规则引擎返会拒绝原因吗--不保存到bms
        paramsMap.put("hint", new String[]{"list", "engineHints"});// 规则引擎返回提示信息--不保存到bms

        paramsMap.put("anticipationIncome", new String[]{"int", "adviceVerifyIncome"});// 建议核实收入--默认-1
        paramsMap.put("suggestQuota", new String[]{"int", "adviceAuditLines"});//// 建议审批金额--默认-1
        paramsMap.put("innerDebtRatio", new String[]{"double", "internalDebtRatio"});//// 内部负债率--默认-1
        paramsMap.put("totalDebtRatio", new String[]{"double", "totalDebtRatio"});// 总负债率--默认-1
        paramsMap.put("executedCCRules", new String[]{"list", "ccRuleSet"});// 经过的CC规则集名称--默认-1

        paramsMap.put("scoreCardZDSCA", new String[]{"int", "scorecardScore"}); // 系统评分卡2.1--默认-1-----ZDSCA
        paramsMap.put("scoreCardZDSCANC", new String[]{"int", "scorecardScoreOnePointOne"}); //// 系统评分卡1.1--默认-1---ZDSCANC
        paramsMap.put("scoreCardZDSCAPromptLevel", new String[]{"string", "scorecardCreditLevel"});// ZDSC提示级别--默认空---ZDSCA风险等级
        paramsMap.put("scoreCardZDSCAGrade", new String[]{"string", "scorecardRanking"});// 评分卡2.1评分等级--默认空---ZDSCA评级
        paramsMap.put("customerRiskGrade", new String[]{"string", "riskRating"}); // 风险评级-默认空
        paramsMap.put("scoreCardZDSCACCL", new String[]{"string", "comCreditRating"});// ZDSC综合信用评级-默认空----ZDSCA综合信用评级
        paramsMap.put("ifCreditRecord", new String[]{"string", "ifCreditRecord"}); //有信用记录 Y:有;N:无默认空
        paramsMap.put("isAntifraud", new String[]{"string", "isAntifraud"});//欺诈可疑-Y/N--一定会有值
        paramsMap.put("handsAmount",new String[]{"double","handsAmount"});// 到手金额--默认值-1
        paramsMap.put("estimatedCost",new String[]{"double","estimatedCost"});//预估评级费--默认值-1


        paramsMap.put("oneMonthsCount", new String[]{"int", "oneMonthsCount"});//近1个月查询次数默认-1-不保存到bms
        paramsMap.put("threeMonthsCount", new String[]{"int", "threeMonthsCount"});//近3个月查询次数默认-1-不保存到bms
        paramsMap.put("creditReportHints", new String[]{"list", "creditReportHints"});//央行提示信息为空需要保存-不保存到bms
        paramsMap.put("externalCreditHints", new String[]{"list", "externalCreditHints"});//外部征信提示信息为空需要保存-不保存到bms

        // 循环key获取规则返回值
        Map<String, Object> valueMap = Maps.newHashMap();
        for (String key : paramsMap.keySet()) {
            String type = paramsMap.get(key)[0];
            String propertiesName = paramsMap.get(key)[1];
            if ("list".equals(type)) {
                String executedCCRules = iterationByKeyToString(key, mapObj, 1);
                if (Strings.isNotEmpty(executedCCRules)) {
                    valueMap.put(propertiesName, executedCCRules);
                }
            } else {
                if (mapObj.containsKey(key)) {
                    // 注意 mapObj json 中 -1 解析出来其实是-1.0
                    Object objValue = mapObj.get(key);
                    Boolean ifNotNull = null != objValue && Strings.isNotEmpty(objValue.toString()) && !"-1.0".equals(objValue.toString()) && !"-1".equals(objValue.toString());
                    if (ifNotNull) {
                        valueMap.put(propertiesName, objValue);
                    }
                }
            }
        }

        //将map转换成对象
        try {
            org.apache.commons.beanutils.BeanUtils.populate(ruleEngineParameter, valueMap);
        } catch (Exception e) {
            LOGGER.error("规则引擎返回值转换对象错误 params:{}", JSON.toJSONString(valueMap), e);
            throw new BusinessException("执行规则引擎失败");
        }
        return ruleEngineParameter;
    }

    /**
     * 迭代返回规则引擎数组类型
     *
     * @param key-map的key
     * @param map-集合
     * @param index-数字下标从1开始
     * @return
     * @date 2017年7月03日
     */
    private String iterationByKeyToString(String key, Map<String, Object> map, int index) {
        String action = null;
        if (index <= 100 && map.containsKey(key + "[" + index + "]")) {
            String str = map.get(key + "[" + index + "]").toString();
            if (Strings.isNotEmpty(str)) {
                StringBuffer sb = new StringBuffer(str);
                String nextStr = iterationByKeyToString(key, map, ++index);
                if (null != nextStr) {
                    sb.append("$" + nextStr);
                }
                action = sb.toString();
            }
        }
        return action;
    }

    /**
     * 迭代返回规则引擎数组类型提示信息
     *
     * @param key-map的key
     * @param map-集合
     * @param index-数字下标从1开始
     * @return
     * @date 2017年7月03日
     */
    private void iterationByKeyToString(String key, Map<String, Object> map, int index, Result<RuleEngineVO> result) {
        String action = null;
        if (index <= 100) {
            String str = map.get(key + "[" + index + "]").toString();
            if (Strings.isNotEmpty(str)) {
                result.addMessage(str);
                iterationByKeyToString(key, map, ++index, result);
            }
        }
    }


    /**
     * 规则引擎修复数据
     *
     * @return
     */
    @Override
    public void repairData(String loanNo) throws Exception {
        RuleEngineRequest<CreditCheckExecVo> request = new RuleEngineRequest<CreditCheckExecVo>();
        request.setSysCode(sysCode);
        request.setBizType("creditCheck");
        CreditCheckExecVo vo = new CreditCheckExecVo();
        vo.setLoanNo(loanNo);
        vo.setExecuteType(EnumUtils.ExecuteTypeEnum.XSZS02.name());
        vo.setRtfNodeState(EnumUtils.ExecuteTypeEnum.XSZS02.getValue());
        request.setData(vo);
        Response response = ruleEngineExecuter.executeRuleEngine(request);
        String executeTime = DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM);
        if (response != null && "000000".equals(response.getRepCode())) {
            MapResponse map = (MapResponse) response;
            // 解析规则引擎返回值
            RuleEngineParameter ruleEngineParameter = getAuditRulesVO(map.getMap());
            ruleEngineParameter.setLoanNo(loanNo);
            ruleEngineParameter.setExecuteType(EnumUtils.ExecuteTypeEnum.XSZS02.name());
            int insertCount = ruleEngineParameterMapper.insert(ruleEngineParameter);
            if (1 != insertCount) {
                LOGGER.error("插入规则引擎返回值异常 insertCount:{}", insertCount);
            } else {
                // 修改借款信息
                RuleEngineVO ruleEngineVO = new RuleEngineVO();
                ruleEngineVO.setLoanNo(ruleEngineParameter.getLoanNo());
                // 只关心五个字段
                ruleEngineVO.setScorecardScore(ruleEngineParameter.getScorecardScore());
                ruleEngineVO.setScorecardScoreOnePointOne(ruleEngineParameter.getScorecardScoreOnePointOne());
                ruleEngineVO.setScorecardRanking(ruleEngineParameter.getScorecardRanking());
                ruleEngineVO.setScorecardCreditLevel(ruleEngineParameter.getScorecardCreditLevel());
                ruleEngineVO.setComCreditRating(ruleEngineParameter.getComCreditRating());
                Result<String> resultUpdate = bmsLoanInfoService.auditUpdateRulesData(ruleEngineVO);// 将规则引擎返回的值保持到借款
                if (null == resultUpdate || !resultUpdate.success()) {
                    throw new BusinessException("保存规则引擎放回值异常");
                }
            }
        } else {
            LOGGER.error("调用规则引擎异常 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("执行规则引擎异常");
        }
    }
}
