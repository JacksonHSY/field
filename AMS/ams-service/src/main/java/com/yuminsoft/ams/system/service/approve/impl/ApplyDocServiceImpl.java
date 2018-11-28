package com.yuminsoft.ams.system.service.approve.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.service.approve.maintain.ApplicationMaintainExecuter;
import com.ymkj.ams.api.service.approve.product.ProductExecuter;
import com.ymkj.ams.api.vo.request.apply.ApplyEntryVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.ReqApplicationPartUpVO;
import com.ymkj.ams.api.vo.request.audit.ReqApplicationPartVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSProductAuditLimitVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSProductVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSApplicationPartVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.master.ResBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductAuditLimitVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.ams.api.vo.response.master.ResBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.master.ResListVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.DebtsInfoMapper;
import com.yuminsoft.ams.system.domain.DebtsInfo;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.ApplyDocService;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.core.CoreApiService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ApplyDoc;
import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

/**
 * 申请件信息维护
 * @author JiaCX 2017年4月13日 下午5:09:49
 */
@Service
public class ApplyDocServiceImpl extends BaseService implements ApplyDocService {

    @Autowired
    private ProductExecuter productExecuter;
    @Autowired
    private ApplicationMaintainExecuter applicationMaintainExecuter;
    @Autowired
    private ApplyInfoExecuter applyInfoExecuter;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoServiceImpl;
    @Autowired
    private BmsBasiceInfoService bmsBasiceInfoService;
    @Autowired
    private ApplyHistoryService applyHistoryService;
    @Autowired
    private ApprovalHistoryService approvalHistoryService;
    @Autowired
    private DebtsInfoMapper debtsInfoMapper;
    @Autowired
    private CoreApiService coreApiService;

    /**
     * 申请件列表信息查询
     * 
     * @param requestPage
     * @param applyDoc
     * @param status 请求标识 1:通过件；2:拒绝件
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:07:58
     */
    @Override
    public PageResponse<ResBMSApplicationPartVO> getApplicationPartList(String sysCode, RequestPage requestPage, ApplyDoc applyDoc, String status) {
        ReqApplicationPartVO vo = new ReqApplicationPartVO();
        vo.setFlag(status);
        vo.setPageNum(requestPage.getPage());
        vo.setPageSize(requestPage.getRows());
        vo.setSysCode(sysCode);
        vo.setFieldSort(requestPage.getSort());
        vo.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        if(StringUtils.isNotEmpty(applyDoc.getCustomerName())){
            vo.setCustomerName(applyDoc.getCustomerName());
        }
        if(StringUtils.isNotEmpty(applyDoc.getCdNo())){
            vo.setCdNo(applyDoc.getCdNo());
        }
        if(StringUtils.isNotEmpty(applyDoc.getLoanNo())){
            vo.setLoanNo(applyDoc.getLoanNo());
        }
        
        Date startDate = new Date();
        Date endDate = new Date();
        if(StringUtils.isEmpty(applyDoc.getCustomerName()) && StringUtils.isEmpty(applyDoc.getCdNo()) && StringUtils.isEmpty(applyDoc.getLoanNo())) {
        	if(StringUtils.isEmpty(applyDoc.getStartDate()) && StringUtils.isEmpty(applyDoc.getEndDate())){
        		startDate = DateUtils.addDate(DateUtils.getStartTime(), -7);
        		endDate = DateUtils.getEndTime();
        	}else{
        		startDate = DateUtils.stringToDate(applyDoc.getStartDate() + " 00:00:00", DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
        		endDate = DateUtils.stringToDate(applyDoc.getEndDate() + " 23:59:59", DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
        	}
        	vo.setStartDate(startDate);
        	vo.setEndDate(endDate);
        } else {
        	if(StringUtils.isNotEmpty(applyDoc.getStartDate()) && StringUtils.isNotEmpty(applyDoc.getEndDate())) {
        		startDate = DateUtils.stringToDate(applyDoc.getStartDate() + " 00:00:00", DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
        		endDate = DateUtils.stringToDate(applyDoc.getEndDate() + " 23:59:59", DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
        		vo.setStartDate(startDate);
        		vo.setEndDate(endDate);
        	}
        }
        
        long a = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------申请件维护列表查询开始时间：" + a);
        PageResponse<ResBMSApplicationPartVO> response = applicationMaintainExecuter.getList(vo);
        long b = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------申请件维护列表查询结束时间：" + b);
        LOGGER.info("------------------------------------------------------申请件维护列表查询用时：" + (b - a) + "ms");
        LOGGER.info("申请件维护列表查询  params:{}  result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));
        return response;
    }

    /**
     * 通过件拒绝
     * 
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:16:53
     */
    @Override
    public Result<String> refusePassedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpServletRequest request) {
    	if(recordApplyHistory(applyDoc, 1) == 1) {//本地插入数据
    		Response<Object> retvo = updateApplicationInfo(sysCode, applyDoc, 1, request);// 借款系统数据更新
    		if(null != retvo && retvo.isSuccess()) {
    			return new Result<String>(Type.SUCCESS, "拒绝成功！");
    		}else {
    			LOGGER.info("借款编号:{} 借款系统数据更新 失败", applyDoc.getLoanNo());
    			throw new BusinessException("借款系统数据更新 失败");
    		}
        }
        return new Result<String>(Type.FAILURE, "拒绝失败！");
    }

    /**
     * 查询产品的审批期限
     * 
     * @param sysCode
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:28:11
     */
    @Deprecated
    @Override
    public List<ResBMSProductAuditLimitVO> getApprovalPeriod(String sysCode, ApplyDoc applyDoc) {
        /**
         * 借款系统 提供 审批期限 查询
         */
        if(StringUtils.isNotEmpty(applyDoc.getProductCode()))
        {
            ReqBMSProductAuditLimitVO param = new ReqBMSProductAuditLimitVO();
            param.setSysCode(sysCode);
            param.setProductCode(applyDoc.getProductCode());
        }
        return new ArrayList<ResBMSProductAuditLimitVO>();
    }
    
    /**
     * 根据网店id，产品code，审批金额获取可选的审批期限列表
     * @param sysCode
     * @param loanNo
     * @param accLmt
     * @param request
     * @return
     * @author JiaCX
     * @date 2017年5月19日 下午5:55:06
     */
    @Override
    public List<ResBMSOrgLimitChannelVO> getApprovalPeriodList(String sysCode, String loanNo, String accLmt, HttpServletRequest request){
    	ReqBMSOrgLimitChannelVO req = new ReqBMSOrgLimitChannelVO();
    	
    	ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
        if(null != applyBasicInfo.getZdqqApply() && 1 == applyBasicInfo.getZdqqApply()) {
        	ResApplicationInfoVO info = bmsLoanInfoServiceImpl.getMoneyLoanInfoDetail(loanNo,false,false);
        	req.setOrgId(info.getBaseInfo().getOwningBranchId());
        	req.setProductCode(info.getBaseInfo().getProductCd());
        }else {
        	ApplyEntryVO applyEntryVO = bmsLoanInfoServiceImpl.getBMSLoanInfoByLoanNoService(loanNo);
        	req.setOrgId(applyEntryVO.getApplyInfoVO().getOwningBranchId());
        	req.setProductCode(applyEntryVO.getApplyInfoVO().getProductCd());
        }
        
        req.setApplyLmt(new BigDecimal(accLmt));
        req.setServiceCode(ShiroUtils.getCurrentUser().getUsercode());
        req.setServiceName(ShiroUtils.getCurrentUser().getName());
        req.setIp(WebUtils.retrieveClientIp(request));
        req.setSysCode(sysCode);
        ResListVO<ResBMSOrgLimitChannelVO> response = productExecuter.getTermList(req);
        LOGGER.info("借款编号：{} 返回借款系统审批期限list查询   params:{} result:{}", loanNo, JSON.toJSONString(req), JSON.toJSONString(response));
        if(null != response && response.isSuccess()){
            return CollectionUtils.isEmpty(response.getCollections()) ? new ArrayList<ResBMSOrgLimitChannelVO>() : response.getCollections();
        } else {
        	throw new BusinessException("获取审批期限失败.");
        }
    }

    /**
     * 通过件修改
     * 校验审批额度是否大于申请额度，如不满足，提示“审批额度不可超过申请额度！”
     * 校验审批额度是否满足产品额度上下限，如不满足，提示“审批额度不可超过产品额度上下限！”
     * 校验内部负债率和总负债率是否满足条件，如不满足，提示“内部负债率超过上限！”或“总负债率超过上限！”，且保存不成功
     * @param sysCode
     * @param applyDoc
     * @param session
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:34:18
     */
    @Override
    public Result<String> editPassedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpSession session, HttpServletRequest request) {
        LOGGER.info("通过件修改, 借款编号:{}, applyDoc:{}", applyDoc.getLoanNo(), JSONObject.toJSONString(applyDoc));

        ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(session.getId(), applyDoc.getLoanNo(), true);

        Integer approvalTerm = Integer.parseInt(applyDoc.getAccTerm());             // 审批期限
        BigDecimal approvalLimit = new BigDecimal(applyDoc.getAccLmt());        // 审批额度
        BigDecimal applyLimit = new BigDecimal(applyBasicInfo.getApplyLmt());   // 申请额度
        BigDecimal productRate = BigDecimal.ZERO;                               // 产品费率
        String ifPreferentialUser = applyBasicInfo.getIfPreferentialUser();     // 是否优惠费率客户

        // 1：校验审批额度是否大于申请额度，如不满足，提示“审批额度不可超过申请额度！”
        if(approvalLimit.compareTo(applyLimit) > 0) {
            return new Result<String>(Type.FAILURE, "审批额度不可超过申请额度！");
        }

        // 2：校验审批额度是否满足产品额度上下限，如不满足，提示“审批额度不可超过产品额度上下限！”
        Result<ResBMSProductVO> response = bmsBasiceInfoService.getBMSProductVOByCode(applyDoc.getProductCode());
        if(!response.success()){
            return new Result<String>(Type.FAILURE, "借款系统未找到该产品！");
        }

        ResBMSProductVO productVo = response.getData();
        BigDecimal floorLimit = productVo.getFloorLimit();  // 最低额度
        BigDecimal upperLimit = productVo.getUpperLimit();  // 最高额度
        if(approvalLimit.compareTo(floorLimit) < 0 || approvalLimit.compareTo(upperLimit) > 0) {
            return new Result<String>(Type.FAILURE, "审批额度不可超过产品额度上下限！");
        }

        // 调用核心系统，获取产品所有的费率列表
        RProductVo productRateVo = coreApiService.getProductByCodeAndTerm(applyBasicInfo.getProductCd(), new Long(approvalTerm));
        productRate = productRateVo.getRate();
        if("Y".equals(ifPreferentialUser)){
            productRate = productRateVo.getPreferRate();
        }
        
        ReqBMSTmParameterVO reqDemoVO = new ReqBMSTmParameterVO();
        reqDemoVO.setCode(applyBasicInfo.getApplyType());
        reqDemoVO.setServiceCode(ShiroUtils.getAccount());
        reqDemoVO.setLoanNo(applyBasicInfo.getLoanNo());
        reqDemoVO.setSysCode(sysCode);
        ResListVO<ResBMSTmParameterVO> res = applyInfoExecuter.findTmParameterByCode(reqDemoVO);
        LOGGER.info("借款编号:{} 查询内部负债率和总负债率, param:{} result:{}", applyDoc.getLoanNo(), JSONObject.toJSONString(reqDemoVO), JSONObject.toJSONString(res));
        if (res.isSuccess() && CollectionUtils.isNotEmpty(res.getCollections())) {
        	ResBMSTmParameterVO resBMSTmParameterVO = res.getCollections().get(0);
			String internalTmParameterVO =resBMSTmParameterVO.getInternalDebtRadio();
			String allTmParameterVO = resBMSTmParameterVO.getTotalDebtRadio();
            BigDecimal _approvalDebtTate = new BigDecimal(internalTmParameterVO); // bms内部负债率
            BigDecimal _approvalGrossDebtRate = new BigDecimal(allTmParameterVO); // bms总负债率

            // 获取外部信用负债总额
            DebtsInfo debtsInfo = debtsInfoMapper.findOutCreditDebtByLoanNo(applyDoc.getLoanNo());
            if(null != debtsInfo){
                BigDecimal outCreditDebtTotal = debtsInfo.getOutCreditDebtTotal();    // 外部信用负债总额
                
                ApprovalHistory lastApprovalHistory = approvalHistoryService.getLastApprovalHistoryByLoanNo(applyDoc.getLoanNo());   // 获取最近一次审批意见
                BigDecimal approvalCheckIncome = lastApprovalHistory.getApprovalCheckIncome();  // 月核实收入
                BigDecimal approvalMonthPay = getApprovalMonthPay(approvalLimit, approvalTerm, productRate);    // 月还款额
                BigDecimal approvalDebtRate = getApprovalDebtRate(approvalMonthPay, approvalCheckIncome);       // 内部负债率
                BigDecimal approvalGrossDebtRate = getApprovalGrossDebtRate(approvalMonthPay, outCreditDebtTotal, approvalCheckIncome);   // 总负债率
                
                LOGGER.info("内部负债率上限:{}, 内部负债率:{}, 总负债率上限:{}, 总负债率:{}",_approvalDebtTate, approvalDebtRate,_approvalGrossDebtRate, approvalGrossDebtRate);
                
                // 3：校验内部负债率是否超过上限
                if(_approvalDebtTate.compareTo(approvalDebtRate) == -1) {
                    return new Result<String>(Type.FAILURE, "内部负债率超过上限！");
                }
                
                // 3：校验总负债率是否超过上限
                if(_approvalGrossDebtRate.compareTo(approvalGrossDebtRate) == -1) {
                    return new Result<String>(Type.FAILURE, "总负债率超过上限！");
                }
            }
        }

        //数据处理
        if(recordApplyHistory(applyDoc, 2) == 1) {//本地插入数据
    		Response<Object> retvo = updateApplicationInfo(sysCode, applyDoc, 2, request);// 借款系统数据更新
    		if(null != retvo && retvo.isSuccess()) {
    			return new Result<String>(Type.SUCCESS, "修改成功！");
    		}else {
    			LOGGER.info("借款编号:{} 借款系统数据更新 失败", applyDoc.getLoanNo());
    			throw new BusinessException("借款系统数据更新 失败");
    		}
        }
        return new Result<String>(Type.FAILURE, "修改失败！");
        
    }

    /**
     * 拒绝件修改
     * 
     * @param sysCode
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:40:28
     */
    @Override
    public Result<String> editRefusedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpServletRequest request) {
    	if(recordApplyHistory(applyDoc, 3) == 1) {//本地插入数据
    		Response<Object> retvo = updateApplicationInfo(sysCode, applyDoc, 3, request);// 借款系统数据更新
    		if(null != retvo && retvo.isSuccess()) {
    			return new Result<String>(Type.SUCCESS, "修改成功！");
    		}else {
    			LOGGER.info("借款编号:{} 借款系统数据更新 失败", applyDoc.getLoanNo());
    			throw new BusinessException("借款系统数据更新 失败");
    		}
        }
        return new Result<String>(Type.FAILURE, "修改失败！");
    }
    
    /**
     * 申请件维护，更新借款系统状态
     * 
     * @param applyDoc
     * @param type  1:通过件_拒绝;2:通过件_修改;3:拒绝件_修改
     * @param request
     * @return
     * @author JiaCX
     * @date 2017年6月7日 下午2:08:18
     */
    private Response<Object> updateApplicationInfo(String sysCode, ApplyDoc applyDoc,int type, HttpServletRequest request){
        ReqApplicationPartUpVO vo = new ReqApplicationPartUpVO();
        vo.setFlag(new Long((long)type));
        vo.setLoanNo(applyDoc.getLoanNo());
        if(2 == type){
            vo.setAccLmt(Long.valueOf(applyDoc.getAccLmt()));
            vo.setAccTerm(Long.valueOf(applyDoc.getAccTerm()));
        }else{
            vo.setFirstLevelReasons(applyDoc.getPrimaryReasonText() != null ? applyDoc.getPrimaryReasonText() : "");
            vo.setFirstLevelReasonsCode(applyDoc.getPrimaryReason() != null ? applyDoc.getPrimaryReason() : "");
            vo.setTwoLevelReasons(applyDoc.getSecodeReasonText() != null ? applyDoc.getSecodeReasonText() : "");
            vo.setTwoLevelReasonsCode(applyDoc.getSecodeReason() != null ? applyDoc.getSecodeReason() : "");
        }
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        vo.setVersion(Long.valueOf(applyDoc.getVersion()));
        vo.setRemark(applyDoc.getRemark());
        vo.setSysCode(sysCode);
        vo.setOperatorCode(currentUser.getUsercode());
        vo.setOperatorName(currentUser.getName());
        vo.setOperatorIP(WebUtils.retrieveClientIp(request));
        Response<Object> retvo = applicationMaintainExecuter.modifyApplication(vo);
        LOGGER.info("借款编号:{} 借款系统数据更新   params:{}  result:{}", applyDoc.getLoanNo(), JSON.toJSONString(vo), JSON.toJSONString(retvo));
        return retvo;
    }
    
    /**
     * 插入审批操作记录表
     * 
     * @param applyDoc
     * @param type  1:通过件_拒绝;2:通过件_修改;3:拒绝件_修改
     * @return
     * @author JiaCX
     * @date 2017年6月7日 上午11:05:13
     */
    private int recordApplyHistory(ApplyDoc applyDoc,int type){
        ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService("", applyDoc.getLoanNo(), true);
        ApplyHistory his = new ApplyHistory();
        his.setLoanNo(applyDoc.getLoanNo());
        his.setName(applyDoc.getCustomerName());
        his.setIdNo(applyBasiceInfo.getIdNo());
        his.setProName(AmsConstants.SQJWH);
        his.setProNum(applyDoc.getLoanNo());
        his.setCheckNodeState(applyBasiceInfo.getCheckNodeState());
        if(2 != type){
            String reasonCodeA = applyDoc.getPrimaryReason() != null ? applyDoc.getPrimaryReason() : "";
            String reasonCodeB = applyDoc.getSecodeReason() != null ? applyDoc.getSecodeReason() : "";
            his.setRefuseCode(StringUtils.isEmpty(reasonCodeB) ? reasonCodeA : reasonCodeA+"-"+reasonCodeB);
        }
        his.setCheckPerson(StringUtils.isEmpty(applyDoc.getCheckPersonCode()) ? null : applyDoc.getCheckPersonCode());
        his.setFinalPerson(StringUtils.isEmpty(applyDoc.getFinalPersonCode()) ? null : applyDoc.getFinalPersonCode());
        his.setApprovalPerson(StringUtils.isEmpty(applyDoc.getApprovalPersonCode()) ? null : applyDoc.getApprovalPersonCode());
        String remark = "";
        String rtfState = applyBasiceInfo.getRtfState();
        String rtfNodeState = applyBasiceInfo.getRtfNodeState();
        switch(type)
        {
            case 1:
            	rtfState = EnumConstants.RtfState.SQJXXWH.getValue();
            	rtfNodeState = EnumConstants.RtfNodeState.SQJWH_REJECT.getValue();
                remark = AmsConstants.SQJWH_PASSEDDOC_REFUSE;
                break;
            case 2:
                remark = AmsConstants.SQJWH_PASSEDDOC_EDIT;
                break;
            case 3:
            	rtfState = EnumConstants.RtfState.SQJXXWH.getValue();
            	rtfNodeState = EnumConstants.RtfNodeState.SQJWH_REJECT.getValue();
                remark = AmsConstants.SQJWH_REFUSEDDOC_EDIT;
                break;
        }
        his.setRemark(AmsConstants.SQJWH + "：" + remark + "。备注为：" + applyDoc.getRemark());
        his.setRtfState(rtfState);
        his.setRtfNodeState(rtfNodeState);
        return applyHistoryService.save(his);
    }

    /**
     * 获取月还款额
     * @param approvalLimit 审批额度
     * @param approvalTerm  审批期限
     * @param productRate   产品费率
     * @author wulj
     * @return 月还款额(2位小数,四舍五入)
     */
    private BigDecimal getApprovalMonthPay(BigDecimal approvalLimit, Integer approvalTerm, BigDecimal productRate){
        // 月还款额 = 审批额度 / 审批期限 + 审批额度 * 产品费率
        BigDecimal approvalMonthPrincipal = approvalLimit.divide(new BigDecimal(approvalTerm.longValue()),2, RoundingMode.HALF_EVEN);
        BigDecimal approvalMonthInterest = approvalLimit.multiply(productRate).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal approvalMonthPay = approvalMonthPrincipal.add(approvalMonthInterest);
        return approvalMonthPay;
    }

    /**
     * 获取内部负债率
     * 本次我司贷款的月还款额 / 月核实收入 * 100%
     * @param approvalMonthPay      月还款额
     * @param approvalCheckIncome   月核实收入
     * @author wulj
     * @return 内部负债率(2位小数,四舍五入)
     */
    private BigDecimal getApprovalDebtRate(BigDecimal approvalMonthPay, BigDecimal approvalCheckIncome){

        return approvalMonthPay.divide(approvalCheckIncome, 3, RoundingMode.HALF_EVEN);
    }

    /**
     * 获取总负债率
     * （本次我司贷款的月还款额 + 外部信用负债总额） / 月核实收入 * 100%
     * @param approvalMonthPay          月还款额
     * @param outCreditDebtTotal        外部信用负债总额
     * @param approvalCheckIncome       月核实收入
     * @return 总负债率(2位小数,四舍五入)
     */
    private BigDecimal getApprovalGrossDebtRate(BigDecimal approvalMonthPay, BigDecimal outCreditDebtTotal, BigDecimal approvalCheckIncome){

        return (approvalMonthPay.add(outCreditDebtTotal)).divide(approvalCheckIncome,3, RoundingMode.HALF_EVEN);
    }

    /**
     * 设置审批额度上下限
     * @param applyDoc
     */
    @Override
    public void setMaxAndMin(ApplyDoc applyDoc) {
        ReqInformationVO applyBasicInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(null, applyDoc.getLoanNo(), true);
        if(StringUtils.isNotEmpty(applyDoc.getProductCode())) {
            // 根据产品CODE，查询产品配置详细信息
            ReqBMSProductVO request = new ReqBMSProductVO();
            request.setSysCode(sysCode);
            request.setCode(applyDoc.getProductCode());
            request.setOrgId(Long.valueOf(applyBasicInfo.getOwningBranchId()));
            ResListVO<ResBMSProductVO> response = productExecuter.getAll(request);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("查出所有的借款产品 param:{} result：{}", JSONObject.toJSONString(request), JSONObject.toJSONString(response));
            }
            if(null != response && response.isSuccess()) {
                if(CollectionUtils.isNotEmpty(response.getCollections())) {
                    ResBMSProductVO productVo = response.getCollections().get(0);
                    BigDecimal maxLimit = productVo.getUpperLimit();
                    if(maxLimit.compareTo(new BigDecimal(applyBasicInfo.getApplyLmt())) > 0){ // 审批上限取"产品审批额度上限"或"申请额度"最小者
                        maxLimit = new BigDecimal(applyBasicInfo.getApplyLmt());
                    }
                    applyDoc.setProductName(productVo.getName());
                    applyDoc.setMaxLimit(maxLimit.toString());
                    applyDoc.setMinLimit(productVo.getFloorLimit().toString());
                    applyDoc.setOwningBranchId(applyBasicInfo.getOwningBranchId());
                    applyDoc.setApplyLmt(applyBasicInfo.getApplyLmt());
                }
            }
        }
    }

}
