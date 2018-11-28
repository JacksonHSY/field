package com.yuminsoft.ams.system.service.bds;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.bds.biz.api.vo.request.*;
import com.ymkj.bds.biz.api.vo.response.*;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.List;

public interface BdsApiService {

    /**
     * 反欺诈信息匹配
     *
     * @param matchAntiFraudInfoReqVO
     * @return
     */
    Result<MatchAntiFraudInfoResVO> matchAntiFraudInfo(MatchAntiFraudInfoReqVO matchAntiFraudInfoReqVO);

    /**
     * add by zw at 2017-05-03 内部匹配申请历史信息查询
     *
     * @param sessionId
     * @param loanNo
     * @return
     */
    ResponsePage<ApplicationHistoryResVO> matchApplicationHistory(String sessionId, String loanNo);

    /**
     * add by zw at 2017-05-05 内部匹配 获取客户关键信息
     *
     * @param sessionId
     * @param loanNo
     * @return
     */
    Result<CustomerKeyInfoResVO> getCustomerKeyInformation(String sessionId, String loanNo);

    /**
     * 内部匹配---电话号码匹配
     *
     * @param loanNo
     * @return
     */
    List<PhoneNumberResVO> matchByPhoneNumber(String loanNo);

    /**
     * 内部匹配---查询申请信息
     *
     * @param loanNo
     * @return
     */
    ApplicationInfoResVO getApplicationInformation(String loanNo);

    /**
     * 内部匹配---姓名身份证信息匹配
     *
     * @param req
     * @return
     */
    List<NameAndIdCardResVO> matchByNameAndIdCard(NameAndIdCardReqVO req);

    /**
     * 内部匹配---单位名称信息匹配
     *
     * @param req
     * @return
     */
    List<UnitNameResVO> matchByUnitName(UnitNameReqVO req);

    /**
     * 内部匹配---地址信息匹配
     *
     * @param req
     * @return
     */
    List<AddressInfoResVO> matchByAddressInfo(AddressInfoReqVO req);

    /**
     * 内部匹配---车牌号匹配
     *
     * @param req
     * @return
     */
    List<LicensePlateHistoryResVO> matchByLicensePlate(LicensePlateReqVO req);

 /*   *//**
     * 联系人与申请人的关系
     *
     * @param request
     * @return
     *//*
    List<RelationResVO> matchRelationByLoanNoAndPhone(RelationReqVo request);*/

    /**
     * 根据关键字匹配灰名单(速贷黑名单匹配)
     *
     * @param applyBasiceInfo
     * @param key-匹配关键字
     * @return
     */
    boolean fastLoanBlacklist(ReqInformationVO applyBasiceInfo, String key);
}
