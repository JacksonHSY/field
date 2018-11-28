package com.yuminsoft.ams.system.service.core;

import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.core.ContractTrialVo;

import java.math.BigDecimal;
import java.util.List;

public interface CoreApiService {

	/**
	 * 根据产品code获取产品费率
	 * 
	 * @author dmz
	 * @date 2017年4月5日
	 * @param productCode 产品CODE
	 * @return
	 */
	List<RProductVo> getProductRateByCode(final String productCode);

	/**
	 * 根据产品code和期限，获取产品费率
	 * @param productCode
	 * @param term
	 * @return
	 */
	RProductVo getProductByCodeAndTerm(final String productCode,final Long term);

	/**
	 * 根据身份证号码查询客户债权状态
	 * @author zhouwen
	 * @date 2017年8月1日
	 * @param idCard
	 * @return
	 */
	String getLoanStatus(String idCard);

	/**
	 * 借款合同试算接口
	 * @param loanType			借款款类型(产品编号)
	 * @param money				借款金额
	 * @param time				借款期数
	 * @param fundsSources		合同来源（渠道）
	 * @param isRatePreferLoan	费率优惠客户
	 * @author wulj
	 * @return
	 */
	ContractTrialVo getLoanContractTrail(String loanType, BigDecimal money, Long time, String fundsSources, String isRatePreferLoan);

	/**
	 * 获取剩余本金接口
	 * @param name
	 * @param idNum
	 * @return
	 */
	BigDecimal getResidualPactMoney(String name, String idNum);

	/**
	 * 判断结清再贷款最近一笔结清是否在三个月内包括三个月只精确到月
	 * @param idNo
	 * @return
	 */
	Boolean isReLoanAndThreeMonth(String idNo);

}
