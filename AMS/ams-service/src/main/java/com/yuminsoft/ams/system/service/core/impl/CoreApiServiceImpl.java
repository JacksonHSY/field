package com.yuminsoft.ams.system.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ymkj.base.core.common.http.HttpResponse;
import com.ymkj.base.core.common.http.HttpUtil;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.core.CoreApiService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.core.ContractTrialVo;
import com.yuminsoft.ams.system.vo.core.ReturnLoanContractTrialVo;
import org.apache.commons.collections.Predicate;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * 核心系统接口
 * 
 * @author dmz
 * @date 2017年4月5日
 */
@Service
public class CoreApiServiceImpl extends BaseService implements CoreApiService {
	@Value("${ams.core.url}")
	private String coreApi;	// 核心系统地址

	@Override
	public List<RProductVo> getProductRateByCode(final String productCode) {
		List<RProductVo> data = Lists.newArrayList();
		try{
			List<BasicNameValuePair> params = Lists.newArrayList();
			params.add(new BasicNameValuePair("productCode", productCode));
			params.add(new BasicNameValuePair("fundsSources", "00001"));

			String result = Request.Post(coreApi + "core/productCore/queryProductTerm").bodyForm(params, Charset.forName("UTF-8")).execute().returnContent().asString();
			JSONObject response = JSONObject.parseObject(result);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("根据产品编号查询产品费率 Request:{}, Response:{}", JSONArray.toJSONString(params), response.toJSONString());
			}
			if(EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getString("code"))){
				data = JSONArray.parseArray(response.getJSONArray("productDetail").toJSONString(), RProductVo.class);
			}
		}catch (Exception e){
			LOGGER.error("查询产品费率异常", e);
		}

		return data;
	}

	@Override
	public RProductVo getProductByCodeAndTerm(final String productCode,final Long term) {
		List<RProductVo> productRateList = getProductRateByCode(productCode);
		RProductVo productRateVo = (RProductVo)CollectionUtils.find(productRateList, new Predicate() {
			@Override
			public boolean evaluate(Object o) {
				return ((RProductVo)o).getTerm().equals(term);
			}
		});

		return productRateVo;
	}

	@Override
	public String getLoanStatus(String idNum) {
		String topupLoanNo = "";
		HttpResponse httpResponse = HttpUtil.post(coreApi + "core/loanCore/loanStatus", "idnum=" + idNum, false);
		LOGGER.info("核心系统债权信息接口 params:{} result:{}", "idnum:" + idNum, JSON.toJSONString(httpResponse));
		if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
			String retList = httpResponse.getContent();
			JSONObject jsonObject = JSONArray.parseObject(retList);//
			if (jsonObject != null) {
				if (AmsCode.RESULT_SUCCESS.equals(jsonObject.getString("code"))) {
					String loan = jsonObject.getString("loan");
					JSONArray loanArray = JSONArray.parseArray(loan);
					for (int i = 0; i < loanArray.size(); i++) {
						JSONObject loanJson = loanArray.getJSONObject(i);
						if ("正常".equals(loanJson.get("loanState"))) {
							topupLoanNo = loanJson.getString("loanNo");
							break;
						}
					}
				}
			}
		}
		return topupLoanNo;
	}

	@Override
	public ContractTrialVo getLoanContractTrail(String loanType, BigDecimal money, Long time, String fundsSources, String isRatePreferLoan) {
		ContractTrialVo contractTrialVo = new ContractTrialVo();
		List<ReturnLoanContractTrialVo> returnDetailList =  Lists.newArrayList();
		try{
			List<BasicNameValuePair> params = Lists.newArrayList();
			params.add(new BasicNameValuePair("loanType", loanType));
			params.add(new BasicNameValuePair("money", money.toString()));
			params.add(new BasicNameValuePair("time", time.toString()));
			params.add(new BasicNameValuePair("fundsSources",fundsSources));
			params.add(new BasicNameValuePair("isRatePreferLoan", isRatePreferLoan));

			String result = Request.Post(coreApi + "core/loanCore/createLoanContractTrial").bodyForm(params, Charset.forName("UTF-8")).execute().returnContent().asString();
			JSONObject response = JSONObject.parseObject(result);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("查询借款合同试算 Request:{}, Response:{}", JSONArray.toJSONString(params), response.toJSONString());
			}
			if(EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getString("code"))){
				contractTrialVo = JSONObject.parseObject(response.getJSONObject("contract").toString(), ContractTrialVo.class);
				//returnDetailList = JSONArray.parseArray(response.getJSONObject("repaymentDetail").toString(), ReturnLoanContractTrialVo.class);
				//contractTrialVo.setReturnDetailList(returnDetailList);
			}else {
				throw new BusinessException("合同试算接口调用失败");
			}
		}catch (Exception e){
			LOGGER.error("查询借款合同试算接口异常", e);
		}

		return contractTrialVo;
	}

	@Override
	public BigDecimal getResidualPactMoney(String name, String idNum) {
		BigDecimal residualPactMoney = BigDecimal.ZERO;
		try{
			List<BasicNameValuePair> params = Lists.newArrayList();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("idNum", idNum));

			String result = Request.Post(coreApi + "core/loanCore/getResidualPactMoney").bodyForm(params, Charset.forName("UTF-8")).execute().returnContent().asString();
			JSONObject response = JSONObject.parseObject(result);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("获取剩余本金 Request:{}, Response:{}", JSONArray.toJSONString(params), response.toJSONString());
			}
			if(EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getString("code"))){
				residualPactMoney = response.getBigDecimal("residualPactMoney");
			}
		}catch (Exception e){
			LOGGER.error("获取剩余本金接口异常", e);
		}

		return residualPactMoney;
	}

	/**
	 * 判断结清再贷款最近一笔结清是否在三个月内包括三个月只精确到月
	 * @param idNo
	 * @return
	 */
	@Override
	public Boolean isReLoanAndThreeMonth(String idNo) {
		Boolean action = false;
		try {
			List<BasicNameValuePair> params = Lists.newArrayList();
			params.add(new BasicNameValuePair("idnum", idNo));
			String result = Request.Post(coreApi + "core/loanCore/findHistoryLoan").bodyForm(params, Charset.forName("UTF-8")).execute().returnContent().asString();
			if (null != result) {
				JSONObject jsonObject = JSON.parseObject(result);
				boolean isRight = jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("000000") && jsonObject.containsKey("infos") && Strings.isNotEmpty(jsonObject.get("infos").toString());
				if (isRight) {
					JSONObject infos = JSON.parseObject(jsonObject.get("infos").toString());
					if (infos.containsKey("settleDates") && Strings.isNotEmpty(infos.get("settleDates").toString())) {
						String settleDateStr = infos.get("settleDates").toString();
						JSONArray settleDateArray = JSONArray.parseArray(settleDateStr);
						String lastSettleDate = settleDateArray.get(settleDateArray.size()-1).toString();
						Date settleDate = DateUtils.stringToDate(lastSettleDate,DateUtils.DEFAULTDATEFORMAT);
						return DateUtils.monthsOfTwo(settleDate,null) <= 3;
					} else {
						throw new BusinessException("调用核心借口查询历史贷款记录异常没有结清日期 idNo:"+ idNo +" result:"+result);
					}
				} else {
					throw new BusinessException("调用核心借口查询历史贷款记录异常 idNo:"+ idNo +" result:"+result);
				}
			} else {
				throw new BusinessException("调用核心借口查询历史贷款记录返回值为空 idNo:{}",idNo);
			}
		}catch(BusinessException e) {
			LOGGER.error(e.getMessage());
			throw  new BusinessException("调用核心信息异常");
		}catch (Exception e){
			LOGGER.error("调用核心查询客户历史贷款记录 idNo:{}",idNo,e);
			throw  new BusinessException("调用核心信息异常");
		}
	}
}
