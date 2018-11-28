package com.yuminsoft.ams.system.service.approve.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.ams.api.service.approve.reconsider.ReconsiderExecuter;
import com.ymkj.ams.api.vo.request.integratedsearch.ReqIntegratedSearchVO;
import com.ymkj.ams.api.vo.response.integratedsearch.ResIntegratedSearchVO;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderLog;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.base.core.common.http.HttpResponse;
import com.ymkj.base.core.common.http.HttpUtil;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.dao.approve.MobileHistoryMapper;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.SearchCompService;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.LoanCoreVo;
import com.yuminsoft.ams.system.vo.apply.RProductVo;
import com.yuminsoft.ams.system.vo.apply.RepaySumVo;
import com.yuminsoft.ams.system.vo.apply.ReturnAccountCardVO;
import com.yuminsoft.ams.system.vo.apply.ReturnRepaymentDetailVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 综合查询
 * @author fusj
 */
@Service
public class SearchCompServiceImpl extends BaseService implements SearchCompService {
    @Autowired
    private IntegrateSearchExecuter integrateSearchExecuter;
    @Autowired
    private MobileHistoryMapper mobileHistoryMapper;
    @Value("${sys.code}") // 系统编码
    private String sysCode;
    @Value("${ams.core.url}")
    private String coreApi;
    
    @Autowired
    private ReconsiderExecuter reconsiderExecuter;

    /**
     * 核心借款
     *
     * @param loanNo
     * @param idNum
     * @return
     */
    @Override
    public LoanCoreVo findLoan(String loanNo, String idNum) {
        // 获取当前登录用户
        Map<String, String> paramsMap = new HashMap<String, String>();
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        paramsMap.put("userCode", currentUser.getUsercode());
        paramsMap.put("loanNo", loanNo);
        paramsMap.put("idNum", idNum);
        String jsonStr = buildParams(paramsMap, "UTF-8");
        String url = coreApi + "core/loanCore/queryLoan";
        HttpResponse httpResponse = HttpUtil.post(url, jsonStr, false);
        if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
            LOGGER.info("核心系统借款接口 params:{} result:{}", JSON.toJSONString(paramsMap), JSON.toJSONString(httpResponse));
            String retList = httpResponse.getContent();
            JSONObject jsonObject = JSONArray.parseObject(retList);// userStr是json字符串
            LoanCoreVo queryLoanVO = new LoanCoreVo();
            if (jsonObject != null) {

                if (jsonObject.containsKey("loan")) {
                    JSONObject result = jsonObject.getJSONObject("loan");
                    queryLoanVO = JSONObject.toJavaObject(result, LoanCoreVo.class);
                    LOGGER.debug("params:{}", JSON.toJSONString(queryLoanVO));
                }
                return queryLoanVO;
            }
        }
        return null;
    }

    /**
     * 核心还款汇总接口
     *
     * @param loanNo
     * @param
     * @return
     */
    @Override
    public RepaySumVo findRepaySum(String loanNo) {
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("userCode", currentUser.getUsercode());
        paramsMap.put("loanNo", loanNo);
        String jsonStr = buildParams(paramsMap, "UTF-8");
        String url = coreApi + "core/repaymentDetialCore/queryRepaymentSummary";
        HttpResponse httpResponse = HttpUtil.post(url, jsonStr, false);
        LOGGER.info("核心系统还款汇总接口 params:{} result:{}", JSON.toJSONString(paramsMap), JSON.toJSONString(httpResponse));
        if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
            String retList = httpResponse.getContent();
            JSONObject jsonObject = JSONArray.parseObject(retList);// userStr是json字符串
            if (jsonObject != null) {
                if (jsonObject.containsKey("repayInfo")) {
                    JSONObject result = jsonObject.getJSONObject("repayInfo");
                    return JSONObject.toJavaObject(result, RepaySumVo.class);
                }
            }
        }
        return null;
    }

    /**
     * 核心还款详情接口
     *
     * @param loanNo
     * @param
     * @return
     */
    @Override
    public List<ReturnRepaymentDetailVo> findReturnRepaymentDetail(String loanNo) {
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("userCode", currentUser.getUsercode());
        paramsMap.put("loanNo", loanNo);
        String jsonStr = buildParams(paramsMap, "UTF-8");
        String url = coreApi + "core/repaymentDetialCore/queryRepaymentDetailAll";
        HttpResponse httpResponse = HttpUtil.post(url, jsonStr, false);
        LOGGER.info("核心系统还款详情接口 params:{} result:{}", JSON.toJSONString(paramsMap), JSON.toJSONString(httpResponse));
        if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
            String retList = httpResponse.getContent();
            JSONObject jsonObject = JSONArray.parseObject(retList);// userStr是json字符串
            if (jsonObject != null) {
                if (jsonObject.containsKey("repaymentDetail")) {
                    JSONArray result = jsonObject.getJSONArray("repaymentDetail");
                    LOGGER.info("借款单号:{} 调用核心还款详情接口成功,返回数据有:{} 条", loanNo, JSONArray.parseArray(result.toJSONString(), ReturnRepaymentDetailVo.class).size());
                    return JSONArray.parseArray(result.toJSONString(), ReturnRepaymentDetailVo.class);
                }
            }
        }
        return null;
    }

    /**
     * 核心帐卡信息接口
     *
     * @param loanNo
     * @param
     * @return
     */
    @Override
    public ResponsePage<ReturnAccountCardVO> findReturnAccountCard(String loanNo, RequestPage requestPage) {
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("userCode", currentUser.getUsercode());
        paramsMap.put("loanNo", loanNo);
        paramsMap.put("max", "9999");
        paramsMap.put("offset", "0");
        String jsonStr = buildParams(paramsMap, "UTF-8");
        String url = coreApi + "core/repaymentDetialCore/queryflow";
        HttpResponse httpResponse = HttpUtil.post(url, jsonStr, false);
        ResponsePage<ReturnAccountCardVO> page = new ResponsePage<ReturnAccountCardVO>();
        LOGGER.info("核心系统帐卡信息接口 params:{} result:{}", JSON.toJSONString(paramsMap), JSON.toJSONString(httpResponse));
        if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
            String retStr = httpResponse.getContent();
            JSONObject jsonObject = JSONArray.parseObject(retStr);// userStr是json字符串
            if (jsonObject != null) {
                if (jsonObject.containsKey("accountCardVOs")) {
                    JSONArray result = jsonObject.getJSONArray("accountCardVOs");
                    List<ReturnAccountCardVO> retList = new ArrayList<ReturnAccountCardVO>();
                    retList = JSONArray.parseArray(result.toJSONString(), ReturnAccountCardVO.class);
                    page.setRows(retList);
                }
                if (jsonObject.containsKey("total")) {
                    Long total = jsonObject.getLong("total");
                    page.setTotal(total);
                    LOGGER.info("借款单号:{} 调用核心帐卡信息接口成功,返回数据有:{} 条", loanNo, total);
                }
            }
        }
        return page;
    }

    /**
     * 产品费率
     * @param productCode
     * @return
     */
    @Override
    public RProductVo monthPay(String productCode) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("fundsSources", productCode);
        String jsonStr = buildParams(paramsMap, "UTF-8");
        HttpResponse httpResponse = HttpUtil.post(coreApi + "core/productCore/queryProductTerm", jsonStr, false);
        LOGGER.debug("核心系统还款汇总接口 params:{} result:{}", JSON.toJSONString(paramsMap), JSON.toJSONString(httpResponse));
        if (null != httpResponse && httpResponse.getCode() == AmsCode.CORN_RESULT_SUCCESS) {
            String retList = httpResponse.getContent();
            JSONObject jsonObject = JSONArray.parseObject(retList);//
            if (jsonObject != null) {
                if (jsonObject.containsKey("productDetail")) {
                    JSONObject result = jsonObject.getJSONObject("productDetail");
                    return JSONObject.toJavaObject(result, RProductVo.class);
                }
            }
        }
        return null;
    }


    /**
     * 构建请求参数
     *
     * @param param
     * @param charset
     * @return
     */
    public static String buildParams(Map<String, String> param, String charset) {
        if (param != null && !param.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                try {
                    buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset)).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return buffer.deleteCharAt(buffer.length() - 1).toString();
        } else {
            return null;
        }
    }

    /**
     * @param vo
     * @param requestPage
     * @param request
     * @return
     */
    @Override
    public ResponsePage<ResIntegratedSearchVO> search(ReqIntegratedSearchVO vo, RequestPage requestPage, HttpServletRequest request) {
        ResponsePage<ResIntegratedSearchVO> page = new ResponsePage<ResIntegratedSearchVO>();
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        vo.setServiceCode(currentUser.getUsercode());
        vo.setServiceName(currentUser.getName());
        vo.setSysCode(sysCode);
        vo.setIp(WebUtils.retrieveClientIp(request));
        vo.setPageNum(requestPage.getPage());
        vo.setPageSize(requestPage.getRows());
        vo.setSysCode(sysCode);
        vo.setFieldSort(requestPage.getSort());
        vo.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        
        if (StringUtils.isNotEmpty(vo.getTel())) {
            List<String> loanNos = mobileHistoryMapper.findLoanNosByThirdTelNum(vo.getTel());
            if (CollectionUtils.isNotEmpty(loanNos)) {
                vo.setTeLoanNo(loanNos.toArray(new String[loanNos.size()]));
            }
        }
        
        long a = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------综合查询bms查询开始时间：" + a);
        PageResponse<ResIntegratedSearchVO> response = integrateSearchExecuter.search(vo);
        long b = System.currentTimeMillis();
        LOGGER.info("------------------------------------------------------综合查询bms查询结束时间：" + b);
        LOGGER.info("------------------------------------------------------综合查询bms查询用时：" + (b - a) + "ms");
        LOGGER.info("返回借款系统综合查询 params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));
        if (null != response && AmsCode.RESULT_SUCCESS.equals(response.getRepCode())) {
            List<ResIntegratedSearchVO> list = response.getRecords();
            if (CollectionUtils.isNotEmpty(list)) {
                // TODO 临时解决借新还旧客户标识 故取消身份隐藏
                /*for (ResIntegratedSearchVO voList : list) {
                    String idNo = voList.getIdNo();
                    if (idNo != null && idNo.length() > 14) {
                        voList.setIdNo("*" + idNo.substring(14));
                    }
                }*/
                page.setRows(list);
                page.setTotal(response.getTotalCount());
            }
        }
        return page;
    }

	/**
	 * 查询复议日志
	 * 
	 * @author Jia CX
	 * <p>2018年6月20日 上午11:10:12</p>
	 * 
	 * @param loanNo
	 * @return
	 */
	public List<ResReconsiderLog> getReviewLog(String loanNo) {
		Response<List<ResReconsiderLog>> res = reconsiderExecuter.getReviewLog(loanNo);
		if(res.isSuccess()) {
			return res.getData();
		}
		return new ArrayList<ResReconsiderLog>();
	}

}