package com.yuminsoft.ams.system.service.bds.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.bds.biz.api.service.IInternalMatchingExecuter;
import com.ymkj.bds.biz.api.vo.request.*;
import com.ymkj.bds.biz.api.vo.response.*;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.*;

/**
 * 行为库接口实现
 *
 * @author dmz
 */
@Service
public class BdsApiServiceImpl implements BdsApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BdsApiServiceImpl.class);

    @Autowired
    private IInternalMatchingExecuter internalMatchingExecuter;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoServiceImpl;
    @Autowired
    private CommonParamService commonParamService;
    @Value("${sys.code}")
    private String sysCode;

    /**
     * 反欺诈信息匹配 edit by zw at 2017-05-08 反欺诈信息匹配状态码匹配修改
     *
     * @param requestVO
     * @return
     */
    @Override
    public Result<MatchAntiFraudInfoResVO> matchAntiFraudInfo(MatchAntiFraudInfoReqVO requestVO) {
        Result<MatchAntiFraudInfoResVO> result = new Result<MatchAntiFraudInfoResVO>(Type.FAILURE);
        requestVO.setSysCode(sysCode);
        Response<List<MatchAntiFraudInfoResVO>> response = internalMatchingExecuter.matchAntiFraudInfo(requestVO);
        LOGGER.debug("反欺诈信息匹配 request:{} result:{}", JSON.toJSONString(requestVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            if (response.getData() == null) {// data为空时提示未查询到信息
                result.addMessage("未找到信息");
            } else {
                result.setDataList(response.getData());
            }
        } else {
            result.addMessage("查询异常");
        }
        return result;
    }

    /**
     * add by zw at 2017-05-03 内部匹配申请历史信息查询
     *
     * @param sessionId
     * @param loanNo
     * @return
     */
    @Override
    public ResponsePage<ApplicationHistoryResVO> matchApplicationHistory(String sessionId, String loanNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paramKey", "insideMatchBeginTime");//从配置参数中获取内匹配置申请历史起始时间
        SysParamDefine sysParamDefine = commonParamService.findOne(map);
        ResponsePage<ApplicationHistoryResVO> page = new ResponsePage<ApplicationHistoryResVO>();
        ReqInformationVO applyBasiceInfo = bmsLoanInfoServiceImpl.getBMSLoanBasiceInfoByLoanNoService(sessionId, loanNo, false);
        MatchApplicationHistoryReqVO request = new MatchApplicationHistoryReqVO();
        request.setUserCode(ShiroUtils.getAccount());
        request.setName(applyBasiceInfo.getName());
        request.setIdNo(applyBasiceInfo.getIdNo());
        request.setLoanNum(loanNo);
        request.setSysCode(sysCode);
        if (null != sysParamDefine) {
            request.setApplyDate(sysParamDefine.getParamValue());// 要查询历史借款的起始时间
        }
        Response<List<ApplicationHistoryResVO>> response = internalMatchingExecuter.matchApplicationHistory(request);
        LOGGER.debug("内部匹配申请历史信息查询信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            page.setRows(response.getData());
            page.setTotal(response.getData().size());
        } else {
            throw new BusinessException("内部匹配申请历史信息查询异常");
        }
        return page;
    }


    /**
     * add by zw at 2017-05-05 获取客户关键信息
     *
     * @param sessionId
     * @param loanNo
     * @return
     */
    @Override
    public Result<CustomerKeyInfoResVO> getCustomerKeyInformation(String sessionId, String loanNo) {
        Result<CustomerKeyInfoResVO> result = new Result<CustomerKeyInfoResVO>(Type.FAILURE);
        CustomerKeyInfoReqVO request = new CustomerKeyInfoReqVO();
        request.setLoanNum(loanNo);
        request.setUserCode(ShiroUtils.getAccount());
        request.setSysCode(sysCode);
        // 申请历史记录
        List<LoanInfoReqVO> loanInfoReqVOList = new ArrayList<LoanInfoReqVO>();
        ResponsePage<ApplicationHistoryResVO> page = matchApplicationHistory(sessionId, loanNo);
        if (page != null && page.getRows().size() > 0) {
            List<ApplicationHistoryResVO> list = page.getRows();
            for (ApplicationHistoryResVO avo : list) {
                LoanInfoReqVO loanInfoReqVO = new LoanInfoReqVO();
                loanInfoReqVO.setLoanNum(avo.getLoanNum());
                loanInfoReqVO.setApplyStatus(avo.getLoanStatus());
                loanInfoReqVO.setApplyDate(avo.getApplyDate());
                loanInfoReqVOList.add(loanInfoReqVO);
            }
        }
        request.setData(loanInfoReqVOList);
        Response<CustomerKeyInfoResVO> response = internalMatchingExecuter.getCustomerKeyInformation(request);
        LOGGER.debug("内部匹配获取客户关键信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.setData(response.getData());
        } else {
            result.addMessage("查询申请历史比对信息异常!");
        }
        return result;
    }

    /**
     * 内部匹配---电话号码匹配
     *
     * @param loanNo
     * @return
     */
    @Override
    public List<PhoneNumberResVO> matchByPhoneNumber(String loanNo) {
        LOGGER.info("内部匹配---电话号码匹配");
        PhoneNumberReqVO req = new PhoneNumberReqVO();
        req.setSysCode(sysCode);
        req.setLoanNum(loanNo);
        req.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        Response<List<PhoneNumberResVO>> response = internalMatchingExecuter.matchByPhoneNumber(req);
        if (null != response && response.isSuccess()) {
            LOGGER.info("===>号码匹配查询返回数据有[{}]条", response.getData().size());
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<PhoneNumberResVO>() : response.getData();
        } else {
            LOGGER.error("内部匹配---电话号码匹配 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
            throw new BusinessException("内部匹配电话号码匹配查询异常");
        }
    }

    /**
     * 内部匹配---查询申请信息
     *
     * @param loanNo
     * @return
     */
    @Override
    public ApplicationInfoResVO getApplicationInformation(String loanNo) {
        StopWatch stopWatch = new StopWatch();
        ApplicationInfoReqVO av = new ApplicationInfoReqVO();
        av.setSysCode(sysCode);
        av.setLoanNum(loanNo);
        stopWatch.start("内部匹配---查询申请信息");
        Response<ApplicationInfoResVO> response = internalMatchingExecuter.getApplicationInformation(av);
        stopWatch.stop();
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return null == response.getData() ? new ApplicationInfoResVO() : response.getData();
        } else {
            LOGGER.error("内部匹配---查询申请信息 params:{} result:{}", JSON.toJSONString(av), JSON.toJSONString(response));
           throw new BusinessException("内部匹配查询申请信息异常");
        }
    }

    /**
     * 内部匹配---姓名身份证信息匹配
     *
     * @param req
     * @return
     */
    @Override
    public List<NameAndIdCardResVO> matchByNameAndIdCard(NameAndIdCardReqVO req) {
        req.setSysCode(sysCode);
        req.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        Response<List<NameAndIdCardResVO>> response = internalMatchingExecuter.matchByNameAndIdCard(req);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<NameAndIdCardResVO>() : response.getData();
        } else {
            LOGGER.error("姓名身份证信息匹配查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
            throw new BusinessException("姓名身份证信息匹配查询异常");
        }
    }

    /**
     * 内部匹配---单位名称信息匹配
     *
     * @param req
     * @return
     */
    @Override
    public List<UnitNameResVO> matchByUnitName(UnitNameReqVO req) {
        req.setSysCode(sysCode);
        req.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        long starttime = System.currentTimeMillis();
        LOGGER.info("===>内部匹配单位名称信息匹配开始时间:{}", DateUtils.dateToString(new Date(), null));
        Response<List<UnitNameResVO>> response = internalMatchingExecuter.matchByUnitName(req);
        long endtime = System.currentTimeMillis();
        LOGGER.info("===>内部匹配单位名称信息匹配结束时间:{},匹配用时:{}", DateUtils.dateToString(new Date(), null), endtime - starttime);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<UnitNameResVO>() : response.getData();
        } else {
            LOGGER.error("单位名称信息匹配查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
          throw new BusinessException("内部匹配单位名称查询异常");
        }
    }

    /**
     * 内部匹配---地址信息匹配
     *
     * @param req
     * @return
     */
    @Override
    public List<AddressInfoResVO> matchByAddressInfo(AddressInfoReqVO req) {
        req.setSysCode(sysCode);
        req.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        Response<List<AddressInfoResVO>> response = internalMatchingExecuter.matchByAddressInfo(req);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<AddressInfoResVO>() : response.getData();
        } else {
            LOGGER.error("地址信息匹配查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
            throw new BusinessException("地址信息匹配查询异常");
        }
    }

    /**
     * 内部匹配---车牌号匹配
     *
     * @param req
     * @return
     */
    @Override
    public List<LicensePlateHistoryResVO> matchByLicensePlate(LicensePlateReqVO req) {
        req.setSysCode(sysCode);
        req.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        Response<List<LicensePlateHistoryResVO>> response = internalMatchingExecuter.matchByLicensePlate(req);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<LicensePlateHistoryResVO>() : response.getData();
        } else {
            LOGGER.error("车牌号匹配查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
            throw new BusinessException("车牌号匹配查询异常");
        }
    }

    /**
     * 联系人与申请人的关系
     *
     * @param request
     * @return
     */
   /* @Override
    public List<RelationResVO> matchRelationByLoanNoAndPhone(RelationReqVo request) {
        request.setSysCode(sysCode);
        request.setUserCode(ShiroUtils.getCurrentUser().getUsercode());
        Response<List<RelationResVO>> response = internalMatchingExecuter.matchRelationByLoanNoAndPhone(request);
        LOGGER.info("联系人与申请人关系 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getData()) ? new ArrayList<RelationResVO>() : response.getData();
        }
        return new ArrayList<RelationResVO>();
    }*/

    /**
     * 根据关键字匹配灰名单(速贷黑名单匹配)
     *
     * @param applyBasiceInfo
     * @param key-匹配关键字
     * @return
     */
    @Override
    public boolean fastLoanBlacklist(ReqInformationVO applyBasiceInfo, String key) {
        boolean action = false;
        IdentifyingAntiFraudReqVO identifyingAntiFraudReqVO = new IdentifyingAntiFraudReqVO();
        identifyingAntiFraudReqVO.setSysCode(sysCode);
        identifyingAntiFraudReqVO.setName(applyBasiceInfo.getName());
        identifyingAntiFraudReqVO.setIdNo(applyBasiceInfo.getIdNo());
        identifyingAntiFraudReqVO.setKey(key);
        Response<String> response = internalMatchingExecuter.identifyingAntiFraudKey(identifyingAntiFraudReqVO);
        if (null != response && response.isSuccess()) {
            action = "1".equals(response.getData()) ? true : false;
        } else {
            LOGGER.error("速贷黑名单匹配 params: {} result:{}", JSON.toJSONString(identifyingAntiFraudReqVO), JSON.toJSONString(response));
            throw new BusinessException("速贷黑名单匹配异常");
        }
        return action;
    }
}
