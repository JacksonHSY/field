package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.DebtsInfoMapper;
import com.yuminsoft.ams.system.dao.approve.ApprovalHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.RuleEngineParameterMapper;
import com.yuminsoft.ams.system.domain.DebtsInfo;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;
import com.yuminsoft.ams.system.domain.approve.RuleEngineParameter;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ApprovalHistoryService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckInfoService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckStatementService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.*;
import com.yuminsoft.ams.system.vo.firstApprove.ApprovalHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Service
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalHistoryServiceImpl.class);

	@Autowired
	private ApprovalHistoryMapper approvalHistoryMapper;
	@Autowired
	private DebtsInfoMapper debtsInfoMapper;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;
	@Autowired
	private BmsBasiceInfoService bmsBasiceInfoService;
	@Autowired
	private PmsApiService pmsApiServiceImpl;
	@Autowired
	private ApproveCheckInfoService approveCheckInfoService;
	@Autowired
	private ApproveCheckStatementService approveCheckStatementService;
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;
	@Autowired
	private CreditzxService creditzxService;
	@Autowired
	private RuleEngineParameterMapper ruleEngineParameterMapper;
	/**
	 * 添加或修改审批意见(包括负债信息,资料核对信息)
	 * @author dmz
	 * @date 2017年3月31日
	 * @param saveVo
	 * @return
	 */
	@Override
	public void saveOrUpdateApprovalHistory(ApprovalSaveVO saveVo) {
		// 删除负债信息
		debtsInfoMapper.deleteDebtsInfoByLoanNo(saveVo.getLoanNo());

		// 删除审批意见(删除为完成的审批意见)
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", saveVo.getLoanNo());
		map.put("approvalPerson", saveVo.getApprovalPerson());

		// 若没有该用户对申请的最新审批意见则不删除
		Long approvalHistoryId = approvalHistoryMapper.findApprovalHistoryId(map);
		if (null != approvalHistoryId) {
			approvalHistoryMapper.delete(approvalHistoryId);
		}

		// 更新资料核对
		updateApproveCheckInfoAndStatement(saveVo);

		// 添加负债信息
		saveDebtsInfo(saveVo);

		// 更新审批意见历史
		ApprovalHistory approvalHistory = new ApprovalHistory();
		approvalHistory.setApprovalAllDebtRate(saveVo.getApprovalAllDebtRate());
		approvalHistory.setApprovalApplyLimit(saveVo.getApprovalApplyLimit());
		approvalHistory.setApprovalApplyTerm(saveVo.getApprovalApplyTerm());
		approvalHistory.setApprovalCheckIncome(saveVo.getApprovalCheckIncome());
		approvalHistory.setApprovalDebtTate(saveVo.getApprovalDebtTate());
		approvalHistory.setApprovalLimit(saveVo.getApprovalLimit());
		approvalHistory.setApprovalMemo(saveVo.getApprovalMemo());
		approvalHistory.setApprovalMonthPay(saveVo.getApprovalMonthPay());
		approvalHistory.setApprovalPerson(saveVo.getApprovalPerson());
		approvalHistory.setApprovalProductCd(saveVo.getApprovalProductCd());
		approvalHistory.setRtfState(EnumConstants.RtfState.XSCS.getValue());
		approvalHistory.setApprovalTerm(saveVo.getApprovalTerm());
		approvalHistory.setLoanNo(saveVo.getLoanNo());

		// 增加大组小组字段值
		Result<ResGroupVO> groupVOResult = new Result<ResGroupVO>(Result.Type.FAILURE);
		ReqParamVO reqParamVO = new ReqParamVO();
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCode(OrganizationEnum.OrgCode.CHECK.getCode());
		groupVOResult = pmsApiServiceImpl.getGroupInfoByAccount(reqParamVO);
		if (groupVOResult.getData() != null) {
			ResGroupVO groupVo = groupVOResult.getData();
			if (groupVo.getBigGroupId() != null) {
				approvalHistory.setLargeGroup(String.valueOf(groupVo.getBigGroupId()));// 保存大组ID
			}
			if (groupVo.getGroupId() != null) {
				approvalHistory.setCurrentGroup(String.valueOf(groupVo.getGroupId()));// 保存小组ID
			}
			if (groupVo.getRoleCodes() != null) {
				approvalHistory.setCurrentRole(groupVo.getRoleCodes());// 保存该用户的角色列表code
			}
		}

		approvalHistoryMapper.save(approvalHistory);

		bmsLoanInfoService.updateProductInfo(saveVo);// 修改产品信息
	}
	/**
	 * 前前添加或修改审批意见(包括负债信息,资料核对信息)
	 * @author dmz
	 * @date 2017年3月31日
	 * @param saveVo
	 *  @param saveAssetsVO 资产信息
	 * @return
	 */
	@Override
	public void saveOrUpdateMoneyApprovalHistory(ApprovalSaveVO saveVo,ReqAssetsInfoVO saveAssetsVO) {
		// 删除负债信息
		debtsInfoMapper.deleteDebtsInfoByLoanNo(saveVo.getLoanNo());

		// 删除审批意见(删除为完成的审批意见)
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", saveVo.getLoanNo());
		map.put("approvalPerson", saveVo.getApprovalPerson());

		// 若没有该用户对申请的最新审批意见则不删除
		Long approvalHistoryId = approvalHistoryMapper.findApprovalHistoryId(map);
		if (null != approvalHistoryId) {
			approvalHistoryMapper.delete(approvalHistoryId);
		}

		// 更新资料核对
		updateApproveCheckInfoAndStatement(saveVo);

		// 添加负债信息
		saveDebtsInfo(saveVo);

		// 更新审批意见历史
		ApprovalHistory approvalHistory = new ApprovalHistory();
		approvalHistory.setApprovalAllDebtRate(saveVo.getApprovalAllDebtRate());
		approvalHistory.setApprovalApplyLimit(saveVo.getApprovalApplyLimit());
		approvalHistory.setApprovalApplyTerm(saveVo.getApprovalApplyTerm());
		approvalHistory.setApprovalCheckIncome(saveVo.getApprovalCheckIncome());
		approvalHistory.setApprovalDebtTate(saveVo.getApprovalDebtTate());
		approvalHistory.setApprovalLimit(saveVo.getApprovalLimit());
		approvalHistory.setApprovalMemo(saveVo.getApprovalMemo());
		approvalHistory.setApprovalMonthPay(saveVo.getApprovalMonthPay());
		approvalHistory.setApprovalPerson(saveVo.getApprovalPerson());
		approvalHistory.setApprovalProductCd(saveVo.getApprovalProductCd());
		approvalHistory.setRtfState(EnumConstants.RtfState.XSCS.getValue());
		approvalHistory.setApprovalTerm(saveVo.getApprovalTerm());
		approvalHistory.setLoanNo(saveVo.getLoanNo());
		approvalHistory.setAssetType(saveVo.getAssetType());

		// 增加大组小组字段值
		Result<ResGroupVO> groupVOResult = new Result<ResGroupVO>(Result.Type.FAILURE);
		ReqParamVO reqParamVO = new ReqParamVO();
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCode(OrganizationEnum.OrgCode.CHECK.getCode());
		groupVOResult = pmsApiServiceImpl.getGroupInfoByAccount(reqParamVO);
		if (groupVOResult.getData() != null) {
			ResGroupVO groupVo = groupVOResult.getData();
			if (groupVo.getBigGroupId() != null) {
				approvalHistory.setLargeGroup(String.valueOf(groupVo.getBigGroupId()));// 保存大组ID
			}
			if (groupVo.getGroupId() != null) {
				approvalHistory.setCurrentGroup(String.valueOf(groupVo.getGroupId()));// 保存小组ID
			}
			if (groupVo.getRoleCodes() != null) {
				approvalHistory.setCurrentRole(groupVo.getRoleCodes());// 保存该用户的角色列表code
			}
		}

		approvalHistoryMapper.save(approvalHistory);

		bmsLoanInfoService.updateMoneyProductInfo(saveVo, saveAssetsVO);// 修改产品信息
	}

	/**
	 * 根据借款编号查询审批意见(包括负债信息,资料核对信息)
	 * @author dmz
	 * @date 2017年3月31日
	 * @param loanNo
	 * @return
	 */
	@Override
	public ApprovalOperationVO getApprovalHistoryByLoanNo(String loanNo) {
		ApprovalOperationVO approvalOperationVO = new ApprovalOperationVO();

		// 查询历史审批意见
		List<ApprovalHistory> approvalHistoryList = approvalHistoryMapper.findByLoanNo(loanNo);
		if(!CollectionUtils.isEmpty(approvalHistoryList)){
			List<ApprovalHistory> approvalHistoryVoList = Lists.newArrayList();
			for (ApprovalHistory approvalHistory : approvalHistoryList) {
				ApprovalHistoryVO approvalHistoryVO = new ApprovalHistoryVO();
				approvalHistoryVO = BeanUtil.copyProperties(approvalHistory, ApprovalHistoryVO.class);
				approvalHistoryVO.setLastModifiedDate(approvalHistory.getLastModifiedDate());
				approvalHistoryVO.setCreatedDate(approvalHistory.getCreatedDate());
				// 查询产品信息
				Result<ResBMSProductVO> resBMSProductVO = bmsBasiceInfoService.getBMSProductVOByCode(approvalHistory.getApprovalProductCd());
				if (resBMSProductVO.success()) {
					approvalHistoryVO.setApprovalProductName(resBMSProductVO.getData().getName());
				}
				// 查询审批人员姓名
				ResEmployeeVO resEmployeeVO = pmsApiServiceImpl.findEmpByUserCode(approvalHistory.getApprovalPerson());
				if (null != resEmployeeVO) {
					approvalHistoryVO.setApprovalPersonName(resEmployeeVO.getName());
				}
				approvalHistoryVoList.add(approvalHistoryVO);
			}

			approvalOperationVO.setApprovalHistoryList(approvalHistoryVoList);
		}

		// 查询资料核对-汇总信息
		ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(loanNo);
		if(approveCheckInfo == null){
			approveCheckInfo = new ApproveCheckInfo();
			approveCheckInfo.setLoanNo(loanNo);
			approveCheckInfo.setName("");
			approveCheckInfo.setOneMonthsCount(0);
			approveCheckInfo.setThreeMonthsCount(0);
			approveCheckInfo.setWeekendPay(null);
			approveCheckInfo.setCreditCheckException(null);
			approveCheckInfo.setCourtCheckException(null);
			approveCheckInfo.setInternalCheckException(null);
			approveCheckInfo.setMemo("");
			RuleEngineParameter ruleEngineParameter = ruleEngineParameterMapper.findByLoanNo(loanNo);
			if (null != ruleEngineParameter) {
				approveCheckInfo.setOneMonthsCount(ruleEngineParameter.getOneMonthsCount());
				approveCheckInfo.setThreeMonthsCount(ruleEngineParameter.getThreeMonthsCount());
			}
		}
		approvalOperationVO.setApproveCheckInfo(approveCheckInfo);

		// 查询资料核对-明细信息
		List<ApproveCheckStatement> approveCheckStatementList = approveCheckStatementService.getByLoanNo(loanNo);
		if(CollectionUtils.isEmpty(approveCheckStatementList)){
			approveCheckStatementList = Lists.newArrayList();

			ApproveCheckStatement personalCheckStatement = new ApproveCheckStatement();
			personalCheckStatement.setLoanNo(loanNo);
			personalCheckStatement.setType(EnumUtils.CheckStatementType.C.name());
			personalCheckStatement.setWater1(null);
			personalCheckStatement.setWater2(null);
			personalCheckStatement.setWater3(null);

			ApproveCheckStatement publicCheckStatement = new ApproveCheckStatement();
			publicCheckStatement.setLoanNo(loanNo);
			publicCheckStatement.setType(EnumUtils.CheckStatementType.B.name());
			publicCheckStatement.setWater1(null);
			publicCheckStatement.setWater2(null);
			publicCheckStatement.setWater3(null);

			approveCheckStatementList.add(personalCheckStatement);
			approveCheckStatementList.add(publicCheckStatement);
		}
		approvalOperationVO.setApproveCheckStatementList(approveCheckStatementList);

		// 查询负债信息
		List<DebtsInfo> debtInfoList = debtsInfoMapper.findByLoanNo(loanNo);
		if(!CollectionUtils.isEmpty(debtInfoList)){
			if (RegExpUtils.isNumeric(loanNo) && 14 != loanNo.length()) {
				approvalOperationVO.setDebtsInfoList(transformationOldDebVO(debtInfoList));	// 旧版借款单号
			} else {
				approvalOperationVO.setDebtsInfoList(debtInfoList);	// 新版借款单号
			}
		}

		return approvalOperationVO;
	}

	/**
	 * 老数据转换负责信息
	 *
	 * @author dmz
	 * @date 2017年7月14日
	 * @param listOld
	 * @return
	 */
	private List<DebtsInfo> transformationOldDebVO(List<DebtsInfo> listOld) {
		List<DebtsInfo> list = null;
		if (null != listOld && !listOld.isEmpty()) {
			list = new ArrayList<DebtsInfo>();
			DebtsInfo first = new DebtsInfo();
			for (DebtsInfo di : listOld) {
				if (null != di.getMonthlyTotalLiabilities() && di.getMonthlyTotalLiabilities().compareTo(new BigDecimal(0)) != -1) {
					BeanUtils.copyProperties(di, first);
				} else {
					DebtsInfo fnew = new DebtsInfo();
					BeanUtils.copyProperties(di, fnew);
					list.add(fnew);
				}
			}
			if (list.size() > 0) {
				BeanUtils.copyProperties(list.get(0), first);
				list.remove(0);
				list.add(0, first);
			} else {
				list.add(first);
			}
		}
		return list;
	}

	/**
	 * 根据借款编号和员工code查询未提交的审批意见
	 *
	 * @author dmz
	 * @date 2017年4月7日
	 * @param loanNo
	 * @return
	 */
	@Override
	public ApprovalHistory getApprovalHistoryByLoanNoAndStaffCode(String loanNo) {
		ApprovalHistory approvalHistory = approvalHistoryMapper.getApprovalHistoryNewByLoanNo(loanNo);
		if (null != approvalHistory) {
			if (null != approvalHistory.getRtfNodeState() || !ShiroUtils.getAccount().equals(approvalHistory.getApprovalPerson())) {
				approvalHistory = null;
			}
		}
		return approvalHistory;
	}

	/**
	 * 添加资料核对
	 * @author dmz
	 * @param saveVo
	*/
	@SneakyThrows
	private void updateApproveCheckInfoAndStatement(ApprovalSaveVO saveVo) {
		LOGGER.info("开始更新资料核对信息, loanNo:{}", saveVo.getLoanNo());
		// 更新资料核对-汇总信息
		ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(saveVo.getLoanNo());
		if(approveCheckInfo == null){
			approveCheckInfo = new ApproveCheckInfo();
		}
		approveCheckInfo.setLoanNo(saveVo.getLoanNo());
		approveCheckInfo.setOneMonthsCount(saveVo.getApproveCheckInfo().getOneMonthsCount());
		approveCheckInfo.setThreeMonthsCount(saveVo.getApproveCheckInfo().getThreeMonthsCount());
		approveCheckInfo.setWeekendPay(saveVo.getApproveCheckInfo().getWeekendPay());
		approveCheckInfo.setCreditCheckException(saveVo.getApproveCheckInfo().getCreditCheckException());
		approveCheckInfo.setCourtCheckException(saveVo.getApproveCheckInfo().getCourtCheckException());
		approveCheckInfo.setInternalCheckException(saveVo.getApproveCheckInfo().getInternalCheckException());
		approveCheckInfo.setMemo(saveVo.getApproveCheckInfo().getMemo());
		approveCheckInfo.setName(saveVo.getApproveCheckInfo().getName());
		approveCheckInfo = approveCheckInfoService.saveOrUpdate(approveCheckInfo);

		// 删除资料核对-流水信息
		approveCheckStatementService.removeByLoanNo(saveVo.getLoanNo());

		// 保存资料核对-流水信息
		if (!CollectionUtils.isEmpty(saveVo.getApproveCheckStatementList())) {
			for (ApproveCheckStatement approveCheckStatement : saveVo.getApproveCheckStatementList()) {
				// 如果没有填写任何流水金额，则不保存
				if(approveCheckStatement.getWater1() == null && approveCheckStatement.getWater2() == null && approveCheckStatement.getWater3() == null){
					continue;
				}
				approveCheckStatement.setInfoId(approveCheckInfo.getId());
				approveCheckStatementService.saveOrUpdate(approveCheckStatement);
			}
		}
	}


	/**
	 * 循环添加负债信息
	 *
	 * @author dmz
	 * @date 2017年4月3日
	 * @param saveVo
	 */
	private void saveDebtsInfo(ApprovalSaveVO saveVo) {
		DebtsInfo did = new DebtsInfo();
		did.setCreditDebt(saveVo.getCreditDebt());
		// did.setCreditLoanAlsoPay(saveVo.getCreditLoanAlsoPay()[0]);// 已还款期数
		did.setCreditLoanDebt(saveVo.getCreditLoanDebt()[0]);
		did.setCreditLoanLimit(saveVo.getCreditLoanLimit()[0]);
		did.setCreditLoanTerm(saveVo.getCreditLoanTerm()[0]);
		did.setCreditTotalLimit(saveVo.getCreditTotalLimit());
		did.setCreditUsedLimit(saveVo.getCreditUsedLimit());
		did.setDebtType(saveVo.getDebtType()[0]);
		did.setLoanNo(saveVo.getLoanNo());
		did.setMemo(saveVo.getMemo()[1]);
		did.setName(saveVo.getApprovalPerson());
		did.setOutDebtTotal(saveVo.getOutDebtTotal());
		did.setOutCreditDebtTotal(saveVo.getOutCreditDebtTotal());
		did.setMonthlyTotalLiabilities(saveVo.getMonthlyTotalLiabilities());
		did.setFastLoanSituation(saveVo.getFastLoanSituation());
		did.setIfBlackRoster(saveVo.getIfBlackRoster());
		if (null != saveVo.getCreditNo()) {
			did.setCreditNo(saveVo.getCreditNo()[0]);// 增加对应的贷款信息
		}
		if (1 != debtsInfoMapper.save(did)) {
			throw new BusinessException("添加负债信息");
		}
		for (int i = 1; i < saveVo.getCreditLoanDebt().length; i++) {
			if (null != saveVo.getCreditLoanDebt()[i] || null != saveVo.getCreditLoanLimit()[i] || null != saveVo.getCreditLoanTerm()[i]) {
				DebtsInfo di = new DebtsInfo();
				// di.setCreditLoanAlsoPay(saveVo.getCreditLoanAlsoPay()[i]);// 已还款期数
				di.setCreditLoanDebt(saveVo.getCreditLoanDebt()[i]);
				di.setCreditLoanLimit(saveVo.getCreditLoanLimit()[i]);
				di.setCreditLoanTerm(saveVo.getCreditLoanTerm()[i]);
				if (null != saveVo.getCreditNo() && saveVo.getCreditNo().length > i) {
					di.setCreditNo(saveVo.getCreditNo()[i]);
				}
				di.setDebtType(saveVo.getDebtType()[i]);
				di.setLoanNo(saveVo.getLoanNo());
				di.setName(saveVo.getApprovalPerson());
				if (1 != debtsInfoMapper.save(di)) {
					throw new BusinessException("添加负债信息");
				}
			}
		}
	}

	/**
	 * 根据借款编号和员工code查询终审当前审批意见
	 *
	 * @author Shipf
	 * @date
	 * @param loanNo
	 * @return
	 */
	@Override
	public Result<ApprovalOperationVO> getCurrentApprovalHistory(String loanNo) {
		ApprovalOperationVO approvalOperationVO = new ApprovalOperationVO();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanNo", loanNo);
		params.put("rtfNodeState", "XSZS-CURRENT");
		List<ApprovalHistory> approvalHistoryList = approvalHistoryMapper.findAll(params);
		if (!CollectionUtils.isEmpty(approvalHistoryList)) {
			approvalOperationVO.setApprovalHistoryList(approvalHistoryList);										// 审批历史
			approvalOperationVO.setDebtsInfoList(debtsInfoMapper.findByLoanNo(loanNo));								// 负债信息
			approvalOperationVO.setApproveCheckInfo(approveCheckInfoService.getByLoanNo(loanNo));					// 资料核对-汇总
			approvalOperationVO.setApproveCheckStatementList(approveCheckStatementService.getByLoanNo(loanNo));		// 资料核对-明细

			return new Result<ApprovalOperationVO>(Type.SUCCESS, null, approvalOperationVO);
		}

		return new Result<ApprovalOperationVO>(Type.FAILURE);
	}

	/**
	 * 初审办理修改审批意见表状态
	 *
	 * @author dmz
	 * @date 2017年4月17日
	 * @param rtfState
	 * @param rtfNodeState
	 * @param checkNodeState
	 */
	@Override
	public void updateFirstApprovalState(String loanNo, String rtfState, String rtfNodeState, String checkNodeState, String firstCheckOperation) {
		ApprovalHistory ah = null;
		// 如果是通过件必须有审批意见
		if (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(rtfNodeState)) {
			// 如果是通过不需要复核或者需要复核的时候 审批意见一定是没有设置状态的
			if (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(checkNodeState) || EnumConstants.ChcekNodeState.CHECK.getValue().equals(checkNodeState)) {
				ah = getApprovalHistoryByLoanNoAndStaffCode(loanNo);
				if (null != ah) {
					ah.setRtfNodeState(rtfNodeState);
					ah.setRtfState(rtfState);
					ah.setCheckNodeState(checkNodeState);
					ah.setLastModifiedBy(ShiroUtils.getAccount());
					ah.setLastModifiedDate(new Date());
					int update = approvalHistoryMapper.update(ah);
					if (1 != update) {
						throw new BusinessException("通过修改审批意见异常");
					}
				} else {
					throw new BusinessException("通过未找到审批意见异常");
				}
			} else {// 复核通过或者复核拒绝一定有状态而且checkNodeState==check
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("loanNo", loanNo);
				map.put("approvalPerson", firstCheckOperation);
				ah = approvalHistoryMapper.findApprovalHistoryByLoanNoAndCheck(map);
				if (null != ah) {
					ah.setCheckNodeState(checkNodeState);
					ah.setLastModifiedBy(ShiroUtils.getAccount());
					ah.setLastModifiedDate(new Date());
					int update = approvalHistoryMapper.update(ah);
					if (1 != update) {
						throw new BusinessException("复核确认通过修改审批意见异常");
					}
				} else {
					throw new BusinessException("复核确认通过未找到审批意见异常");
				}
			}
		} else {// 拒绝,退回,挂起没有审批意见修改
			if (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(checkNodeState) || EnumConstants.ChcekNodeState.CHECK.getValue().equals(checkNodeState)) {
				ah = getApprovalHistoryByLoanNoAndStaffCode(loanNo);
				if (null != ah) {
					ah.setRtfNodeState(rtfNodeState);
					ah.setRtfState(rtfState);
					ah.setCheckNodeState(checkNodeState);
					ah.setLastModifiedBy(ShiroUtils.getAccount());
					ah.setLastModifiedDate(new Date());
					int update = approvalHistoryMapper.update(ah);
					if (1 != update) {
						throw new BusinessException("拒绝或退回修改审批意见异常");
					}
				}
			} else {// 复核通过或者复核拒绝一定有状态而且checkNodeState
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("loanNo", loanNo);
				map.put("approvalPerson", firstCheckOperation);
				ah = approvalHistoryMapper.findApprovalHistoryByLoanNoAndCheck(map);
				if (null != ah) {
					ah.setCheckNodeState(checkNodeState);
					ah.setLastModifiedBy(ShiroUtils.getAccount());
					ah.setLastModifiedDate(new Date());
					int update = approvalHistoryMapper.update(ah);
					if (1 != update) {
						throw new BusinessException("复核确认拒绝或退回修改审批意见异常");
					}
				}
			}
		}
	}

	@Override
	public ApprovalHistory getLastApprovalHistoryByLoanNo(String loanNo) {
		return approvalHistoryMapper.findLastApprovalHistoryByLoanNo(loanNo);
	}

	/**
	 * 审批意见对外接口VO(规则引擎调用)
	 *
	 * @author dmz
	 * @date 2017年6月23日
	 * @param loanNo
	 * @param  assetTypeNow-页面时时传入勾选资产信息如果为空取数据库值
	 * @return
	 */
	@Override
	public ApprovalOpinionsVO getApprovalOpinions(String loanNo, String assetTypeNow) {
		ApprovalOpinionsVO approvalOpinionsVO = new ApprovalOpinionsVO();
		String assetType = null;// 定义选择的资产验证
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", loanNo);
		// 审批意见表
		ApprovalHistory approvalHistory = approvalHistoryMapper.getApprovalHistoryNewByLoanNo(loanNo);// 拿最新的审批意见
		if (null != approvalHistory) {
			if (null != approvalHistory.getApprovalCheckIncome()) {
				approvalOpinionsVO.setApprovalCheckIncome(approvalHistory.getApprovalCheckIncome().intValue());
			}
			if (null != approvalHistory.getApprovalLimit()) {
				approvalOpinionsVO.setApprovalLimit(approvalHistory.getApprovalLimit().intValue());
			}
			if (null != approvalHistory.getApprovalTerm() && !approvalHistory.getApprovalTerm().isEmpty()) {
				approvalOpinionsVO.setApprovalTerm(Integer.parseInt(approvalHistory.getApprovalTerm()));
			}
			if (null != approvalHistory.getApprovalProductCd() && !approvalHistory.getApprovalProductCd().isEmpty()) {
				approvalOpinionsVO.setApprovalProductCd(approvalHistory.getApprovalProductCd());
			}

			// 资料核对表
			ApproveCheckInfo approveCheckInfo = approveCheckInfoService.getByLoanNo(loanNo);
			List<ApproveCheckStatement> approveCheckStatementList = approveCheckStatementService.getByLoanNo(loanNo);
			int averageIncomeTotal = 0 ;
			if(!CollectionUtils.isEmpty(approveCheckStatementList)){
				for (ApproveCheckStatement approveCheckStatement : approveCheckStatementList) {
					BigDecimal water1 = approveCheckStatement.getWater1() == null ? BigDecimal.ZERO : approveCheckStatement.getWater1();
					BigDecimal water2 = approveCheckStatement.getWater2() == null ? BigDecimal.ZERO : approveCheckStatement.getWater2();
					BigDecimal water3 = approveCheckStatement.getWater3() == null ? BigDecimal.ZERO : approveCheckStatement.getWater3();

					int average = (water1.add(water2).add(water3)).divide(BigDecimal.valueOf(3),0, RoundingMode.HALF_EVEN).intValue();

					if(approveCheckStatement.getType().equals(EnumUtils.CheckStatementType.C.name())){	// 添加个人流水
						approvalOpinionsVO.getPersonalWater().add(water1.intValue());
						approvalOpinionsVO.getPersonalWater().add(water2.intValue());
						approvalOpinionsVO.getPersonalWater().add(water3.intValue());
						// 计算个人流水月均
						approvalOpinionsVO.getPersonMonthAverage().add(average);
					}else{	// 添加对公流水
						approvalOpinionsVO.getPublicWater().add(water1.intValue());
						approvalOpinionsVO.getPublicWater().add(water2.intValue());
						approvalOpinionsVO.getPublicWater().add(water3.intValue());
						// 计算个人流水月均
						approvalOpinionsVO.getPublicMonthAverage().add(average);
					}
					averageIncomeTotal += average;
				}
			}

			if(approveCheckInfo != null){
				approvalOpinionsVO.setOneMonthsCount(approveCheckInfo.getOneMonthsCount());			// 一个月查询次数
				approvalOpinionsVO.setThreeMonthsCount(approveCheckInfo.getThreeMonthsCount());		// 三个月查询次数
				approvalOpinionsVO.setMonthAverage(averageIncomeTotal);								// 月均流水合计
			}

			// 负债信息表
			List<DebtsInfo> debtsInfoList = debtsInfoMapper.findAll(map);
			if(!CollectionUtils.isEmpty(debtsInfoList)){
				if (null != debtsInfoList.get(0).getOutCreditDebtTotal()) {
					approvalOpinionsVO.setCreditLoanDebt(debtsInfoList.get(0).getOutCreditDebtTotal().intValue());
				}
				if (null != debtsInfoList.get(0).getOutDebtTotal()) {
					approvalOpinionsVO.setOutDebtTotal(debtsInfoList.get(0).getOutDebtTotal().intValue());
				}
			}
            assetType = approvalHistory.getAssetType();
		} else {
			RuleEngineParameter ruleEngineParameter = ruleEngineParameterMapper.findByLoanNo(loanNo);
			if (null != ruleEngineParameter) {
				approvalOpinionsVO.setOneMonthsCount(ruleEngineParameter.getOneMonthsCount());
				approvalOpinionsVO.setThreeMonthsCount(ruleEngineParameter.getThreeMonthsCount());
			}
		}

        //  判断资产信审验证
        assetType = Strings.isNotEmpty(assetTypeNow)  ? assetTypeNow : assetType;
        LOGGER.info("勾选资产信息验证：{}",assetType);
        if (Strings.isNotEmpty(assetType)) {
            List<String> assetTypeArray = Arrays.asList(assetType.split(","));
            approvalOpinionsVO.setEstateAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.ESTATE.getValue()) ? "Y" : "N");//	房产信息已验证(Y/N)
            approvalOpinionsVO.setCarAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.CAR.getValue()) ? "Y" : "N");// 车辆信息已验证(Y/N)
            approvalOpinionsVO.setPolicyAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.POLICY.getValue())  ? "Y" : "N");//保单信息已验证(Y/N)
            approvalOpinionsVO.setEducationAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.EDUCATION.getValue()) ? "Y" : "N");//学历信息已验证(Y/N)
            approvalOpinionsVO.setCardAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.CARDLOAN.getValue())  ? "Y" : "N");// 信用卡信息已验证(Y/N)
            approvalOpinionsVO.setMasterAuthenticated(assetTypeArray.contains(EnumConstants.qqProductModule.MASTERLOAN.getValue())  ? "Y" : "N");//淘宝账户信息已验证(Y/N)
        }

		// 数组类型判断是否有没值
		// 重写
		if (approvalOpinionsVO.getPersonalWater().size() == 0) {
			approvalOpinionsVO.getPersonalWater().add(-1);
		}
		if (approvalOpinionsVO.getPersonMonthAverage().size() == 0) {
			approvalOpinionsVO.getPersonMonthAverage().add(-1);
		}
		if (approvalOpinionsVO.getPublicWater().size() == 0) {
			approvalOpinionsVO.getPublicWater().add(-1);
		}
		if (approvalOpinionsVO.getPublicMonthAverage().size() == 0) {
			approvalOpinionsVO.getPublicMonthAverage().add(-1);
		}

		return approvalOpinionsVO;
	}

	/**
	 * 根据id修改总负债率和内部负债率(规则引擎)
	 *
	 * @author dmz
	 * @date 2017年7月6日
	 * @param map
	 * @return
	 */
	@Override
	public int updateResponsibleByLoanNo(Map<String, Object> map) {
		return approvalHistoryMapper.updateResponsibleByLoanNo(map);
	}

	/**
	 * 根据借款编号查询最后一次终审通过审批记录
	 *
	 * @param loanNo
	 * @return rtfNodeState
	 * @author lihm
	 */
	@Override
	public Response<FinalApprovalOpinionVO> getLastApprovalByLoanNo(String loanNo, String rtfNodeState) {
		Response<FinalApprovalOpinionVO> response = new Response<FinalApprovalOpinionVO>();
		ApprovalHistory approvalHistoryParam = new ApprovalHistory();
		approvalHistoryParam.setLoanNo(loanNo);
		approvalHistoryParam.setRtfNodeState(rtfNodeState);
		ApprovalHistory approvalHistory =  approvalHistoryMapper.findLastApprovalByLoanNo(approvalHistoryParam);
		LOGGER.info("最后审批意见查询: 借款编号:[{}],param：[{}], result:[{}]", loanNo, JSON.toJSONString(approvalHistoryParam), JSON.toJSONString(approvalHistory));
		DebtsInfo debtsInfo = debtsInfoMapper.findOutCreditDebtByLoanNo(loanNo);
		LOGGER.info("终审审批意见负债信息查询: 借款编号:[{}], result:{}", loanNo,  JSON.toJSONString(debtsInfo));
		if (null != approvalHistory && null != debtsInfo) {
			FinalApprovalOpinionVO fao = new FinalApprovalOpinionVO();
			BeanUtils.copyProperties(approvalHistory, fao);
			fao.setOutCreditDebtTotal(debtsInfo.getOutCreditDebtTotal());
			response.setData(fao);
		} else {
            response.setRepMsg("未找到对应的数据");
        }
		return response;
	}

	/**
	 * 根据loanNo获取审批日志
	 * @param loanNo
	 * @return
	 */
	@Override
	public ResponsePage<ApprovalOpinionHistoryVO> getHistoryListByLoanNo(String loanNo) {
		List<ApprovalHistory> list = approvalHistoryMapper.findByLoanNo(loanNo);
		ResponsePage<ApprovalOpinionHistoryVO> res = new ResponsePage<ApprovalOpinionHistoryVO>();
		List<ApprovalOpinionHistoryVO> listVO = Lists.newArrayList();
		try {
			listVO = getNameByCode(list);
		}catch (Exception e){
		LOGGER.error("审批日志查询code转name异常",e);
	}
		res.setRows(listVO);
		res.setTotal(listVO.size());
		return res;
	}

	/**
	 * 将对于的code转成对应的中文名
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalOpinionHistoryVO> getNameByCode(List<ApprovalHistory> list) throws Exception{
		List<ApprovalOpinionHistoryVO> listVO = Lists.newArrayList();
		if(!CollectionUtils.isEmpty(list)){
			ApprovalOpinionHistoryVO  vo = new ApprovalOpinionHistoryVO();
			for(ApprovalHistory approval:list) {
				BeanUtils.copyProperties(approval,vo);
				if(StringUtils.isNotEmpty(vo.getCreatedBy())){ //创建人姓名
					String ceatedByName = "";
					if(!"anonymous".equals(vo.getCreatedBy())){
						ceatedByName = qualityCheckInfoService.getNameByCode(vo.getCreatedBy());
					}else {
						ceatedByName = vo.getCreatedBy();
					}
					vo.setCreatedByName(ceatedByName);
				}
				if (StringUtils.isNotEmpty(vo.getApprovalPerson())) { //审批人员
					String approvalPersionName = qualityCheckInfoService.getNameByCode(vo.getApprovalPerson());
					vo.setApprovalPersonName(approvalPersionName);
				}
				if(StringUtils.isNotEmpty(vo.getApprovalProductCd())){ //审批产品
					Result<ResBMSProductVO> product = bmsBasiceInfoService.getBMSProductVOByCode(vo.getApprovalProductCd());
					if(null != product.getData() && product.success()){

						vo.setApprovalProductCdName(product.getData().getName());
					}
				}
				if(StringUtils.isNotEmpty(vo.getLargeGroup())){ //当前大组
					ResOrganizationVO res = pmsApiServiceImpl.findOrgById(Long.parseLong(vo.getLargeGroup()));
					if(null != res){
						vo.setLargeGroupName(res.getName());
					}

				}
				if(StringUtils.isNotEmpty(vo.getCurrentGroup())){ //当前小组
					ResOrganizationVO res = pmsApiServiceImpl.findOrgById(Long.parseLong(vo.getCurrentGroup()));
					if(null != res){
						vo.setCurrentGroupName(res.getName());
					}
				}
				listVO.add(vo);
			}
		}

		return listVO;
	}
}
