package com.yuminsoft.ams.system.controller.sysservice;

import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.core.CoreApiService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.core.ContractTrialVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/coreApi")
public class CoreApiController extends BaseController {

	@Autowired
	private CoreApiService coreApiService;

	/**
	 * 查询产品费率
	 * @param productCode 产品编码
	 * @return
	 */
	@RequestMapping("/getProductRate")
	@ResponseBody
	public Result<RProductVo> getProductRate(String productCode) {
		Result<RProductVo> result = new Result<RProductVo>(Type.SUCCESS);

		if (StringUtils.isEmpty(productCode)) {
			return new Result<RProductVo>(Type.FAILURE, "缺少产品编码");
		}

		try {
			List<RProductVo> productTermList = coreApiService.getProductRateByCode(productCode);
			result.setDataList(productTermList);

		} catch (Exception e) {
			LOGGER.error("获取产品费率错误", e);
		}

		return result;
	}

	/**
	 * 借款合同试算
	 * @param loanType
	 * @param money
	 * @param time
	 * @param fundsSources
	 * @param isRatePreferLoan
	 * @author wulj
	 * @return
	 */
	@RequestMapping("/getLoanContractTrail")
	@ResponseBody
	public Result<ContractTrialVo> getLoanContractTrail(String loanType, BigDecimal money, Long time,String fundsSources, String isRatePreferLoan){
		Result<ContractTrialVo> result = new Result<ContractTrialVo>(Type.SUCCESS);
		if(StringUtils.isEmpty(loanType) || money == null || time == null){
			return new Result<ContractTrialVo>(Type.FAILURE, "借款合同试算缺少必要的查询参数(产品、金额、期限)");
		}

		fundsSources = StringUtils.defaultIfEmpty(fundsSources, AmsConstants.CHANNEL_ZENDAI);
		isRatePreferLoan = StringUtils.defaultIfEmpty(isRatePreferLoan, AmsConstants.NO);

		// 查询借款合同试算金额
		ContractTrialVo contractTrialVo = coreApiService.getLoanContractTrail(loanType, money, time, fundsSources, isRatePreferLoan);
		result.setData(contractTrialVo);

		return result;
	}

	/**
	 * 获取剩余本金接口
	 * @param name
	 * @param idNum
	 * @auhor wulj
	 * @return
	 */
	@RequestMapping("getResidualPactMoney")
	@ResponseBody
	public Result<BigDecimal> getResidualPactMoney(String name, String idNum){
		Result<BigDecimal> result = new Result<BigDecimal>(Type.SUCCESS);
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(idNum)){
			return new Result<BigDecimal>(Type.FAILURE, "获取剩余本金接口缺少必要的查询参数(姓名、身份证)");
		}

		BigDecimal residualPactMoney = coreApiService.getResidualPactMoney(name, idNum);	// 获取剩余本金
		result.setData(residualPactMoney);

		return result;
	}
}
