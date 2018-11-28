package com.yuminsoft.ams.system.service.bms.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.service.approve.integrate.DistrictExecuter;
import com.ymkj.ams.api.service.approve.integrate.EnumExecuter;
import com.ymkj.ams.api.service.approve.integrate.ReasonExecuter;
import com.ymkj.ams.api.service.approve.product.ProductExecuter;
import com.ymkj.ams.api.vo.request.master.*;
import com.ymkj.ams.api.vo.response.integratedsearch.ResSupplementalContacts;
import com.ymkj.ams.api.vo.response.master.*;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BmsBasiceInfoServiceImpl implements BmsBasiceInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmsBasiceInfoServiceImpl.class);

    @Autowired
    private DistrictExecuter districtExecuter;
    @Autowired
    private EnumExecuter enumExecuter;
    @Autowired
    private ProductExecuter productExecuter;
    @Autowired
    private ReasonExecuter reasonExecuter;
    @Autowired
    private ApplyInfoExecuter applyInfoExecuter;
    @Value("${sys.code}")
    private String sysCode;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询省市区树形结构
     *
     * @author dmz
     * @date 2017年3月1日
     */
    @Override
    public List<ResBMSBaseAreaTreeVO> listByTree() {
        List<ResBMSBaseAreaTreeVO> list = new ArrayList<ResBMSBaseAreaTreeVO>();
        ReqBMSBaseAreaVO reVo = new ReqBMSBaseAreaVO();
        reVo.setSysCode(sysCode);
        ResListVO<ResBMSBaseAreaTreeVO> resultVo = districtExecuter.getDistrict(reVo);
        LOGGER.debug("省市区查询地区  params:{} result:{}", JSON.toJSONString(reVo), JSON.toJSONString(resultVo));
        if (null != resultVo && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(resultVo.getRepCode())) {
            list = resultVo.getCollections();
        } else {
            throw new BusinessException("省市区查询异常");
        }
        return list;
    }

    /**
     * 获取产品列表
     *
     * @return
     * @author dmz
     * @date 2017年3月6日
     */
    @Override
    public List<ResBMSProductVO> getProductList() {
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        ReqBMSProductVO request = new ReqBMSProductVO(sysCode);
        ResListVO<ResBMSProductVO> response = productExecuter.getAll(request);
        LOGGER.debug("获取产品列表 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            list = response.getCollections();
        } else {
            throw new BusinessException("获取产品列表异常");
        }
        return list;
    }

    /**
     * 下拉类型枚举
     *
     * @param emnuType          枚举类型
     * @param app-是否前前进件1:是，0：否
     * @author dmz
     * @date 2017年3月6日
     */
    @Override
    public List<ResBMSEnumCodeVO> getListEnumCodeBy(String emnuType, String app) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        ReqBMSEnumCodeVO request = new ReqBMSEnumCodeVO();
        request.setCodeType(emnuType);
        request.setSysCode(sysCode);
        request.setApp(null == app ? "0": app);
        ResListVO<ResBMSEnumCodeVO> response = enumExecuter.getAll(request);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            list = response.getCollections();
        } else {
            LOGGER.error("枚举查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            throw new BusinessException("枚举类型获取异常");
        }
        return list;
    }

    /**
     * edit by zw at 2017-04-24 更换接口通过产品code，门店id，ip等获取审批期限
     *
     * @param code
     * @return
     * @author dmz
     * @date 2017年4月5日
     */
    @Override
    public List<ResBMSOrgLimitChannelVO> listProductLimitBy(String code, String contractBranchId, String ip) {
        List<ResBMSOrgLimitChannelVO> list = new ArrayList<ResBMSOrgLimitChannelVO>();
        ReqBMSOrgLimitChannelVO request = new ReqBMSOrgLimitChannelVO();
        request.setSysCode(sysCode);
        request.setProductCode(code);
        request.setIp(ip);
        request.setServiceCode(ShiroUtils.getAccount());
        request.setServiceName(ShiroUtils.getCurrentUser().getName());
        if (StringUtil.isNotEmpty(contractBranchId)) {
            request.setOrgId(Long.parseLong(contractBranchId));
        }
        ResListVO<ResBMSOrgLimitChannelVO> response = productExecuter.getTermList(request);
        LOGGER.debug("产品期限查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            list = response.getCollections();
        } else {
            throw new BusinessException("产品期限查询异常");
        }
        return list;
    }

    /**
     * 根据审核产品code获取资产配置信息
     *
     * @param code
     * @return list
     * @date 2017年4月19日
     */
    @Override
    public List<ResBMSEnumCodeVO> getAssetsByProdCode(String code) {
        List<ResBMSEnumCodeVO> enumCodeVOList = Lists.newArrayList();
        ReqBMSEnumCodeVO request = new ReqBMSEnumCodeVO();
        request.setSysCode(sysCode);
        request.setProductCode(code);
        Response<List<ResBMSEnumCodeVO>> response = productExecuter.getAssetModule(request);
        LOGGER.debug("资产配置信息查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (response != null && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getData())) {
                enumCodeVOList = (List<ResBMSEnumCodeVO>) response.getData();
            }
        } else {
            throw new BusinessException("资产配置信息查询异常");
        }
        return enumCodeVOList;
    }

    // end at 2017-04-19

    /**
     * add by zw at 2017-04-25 根据产品code查询到产品信息
     *
     * @param code
     * @return
     */
    @Override
    public Result<ResBMSProductVO> getBMSProductVOByCode(String code) {
        ReqBMSProductVO request = new ReqBMSProductVO();
        request.setCode(code);
        request.setSysCode(sysCode);
        Result<ResBMSProductVO> result = new Result<ResBMSProductVO>(Type.FAILURE);
        Response<ResBMSProductVO> response = productExecuter.getByCode(request);
        LOGGER.info("根据产品code查询产品信息 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            result.setData(response.getData());
            result.setType(Type.SUCCESS);
        }
        return result;
    }

    /**
     * 返回退回,拒绝,挂起原因
     *
     * @param req
     * @return
     * @author dmz
     * @date 2017年5月16日
     */
    @Override
    public List<ReqBMSTMReasonVO> getReasonList(ReqBMSTMReasonVO req) {
        List<ReqBMSTMReasonVO> result = new ArrayList<ReqBMSTMReasonVO>();
        req.setSysCode(sysCode);
        req.setReasonType("1");
        ResListVO<ReqBMSTMReasonVO> response = null;
        if ("1".equals(req.getType())) {
            response = reasonExecuter.getPrimaryReason(req);
        } else {
            response = reasonExecuter.getSecondaryReason(req);
        }
        LOGGER.info("返回退回,拒绝,挂起原因  params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
        if (response != null && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            if (response.getSize() > 0) {
                result = response.getCollections();
            }
        }
        return result;
    }

    /**
     * 返回产品期限
     *
     * @return
     * @author dmz
     * @date 2017年5月18日
     */
    @Override
    public List<ResBMSOrgLimitChannelVO> listOrgProductLimitByOrgProApp(ReqBMSOrgLimitChannelVO request) {
        List<ResBMSOrgLimitChannelVO> list = new ArrayList<ResBMSOrgLimitChannelVO>();
        request.setSysCode(sysCode);
        ResListVO<ResBMSOrgLimitChannelVO> response = productExecuter.getTermList(request);
        LOGGER.info("产品期限查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getCollections())) {
                list = response.getCollections();
            }
        } else {
            throw new BusinessException("产品期限查询异常");
        }
        return list;
    }

    /**
     * 返回产品或者期限对应的审批上下限
     *
     * @return
     * @author dmz
     * @date 2017年5月18日
     */
    @Override
    public ResBMSOrgLimitChannelVO findOrgLimitChannelLimitUnion(ReqBMSOrgLimitChannelVO request) {
        ResBMSOrgLimitChannelVO result = new ResBMSOrgLimitChannelVO();
        request.setSysCode(sysCode);
        Response<ResBMSOrgLimitChannelVO> response = productExecuter.getLimit(request);
        LOGGER.info("返回产品或者期限对应的审批上下限  params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (null != response.getData()) {
                result = response.getData();
            }
        } else {
            throw new BusinessException("产品或期限查询对应的审批上下限异常");
        }
        return result;
    }

    /**
     * 根据进件营业部和是否是优惠客户获取产品列表
     *
     * @param orgId-进件网点id
     * @param ifPreferentialUser-是否是优惠客户:Y是,N否
     * @return
     * @author zw
     * @date 2017年5月22日
     */
    @Override
    public List<ResBMSProductVO> getProductListByOrgId(String orgId, String ifPreferentialUser) {
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        ReqBMSProductVO request = new ReqBMSProductVO(sysCode);
        request.setOrgId(Long.parseLong(orgId));
        if (EnumUtils.YOrNEnum.Y.getValue().equals(ifPreferentialUser)) {
            request.setIsCanPreferential("0");
        } else if (EnumUtils.YOrNEnum.N.getValue().equals(ifPreferentialUser)) {
            request.setIsCanPreferential("1");
        }
        ResListVO<ResBMSProductVO> response = productExecuter.getByOrgId(request);
        LOGGER.debug("根据进件营业部ID获取产品列表 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getCollections())) {
            list = response.getCollections();
            for(ResBMSProductVO re :list) {
                if ("00025".equals(re.getCode())) {
                    list.remove(re);
                    break;
                }
            }

        } else {
            throw new BusinessException("根据进件营业部ID获取产品列表异常");
        }
        return list;
    }

    /**
     * 前前根据进件营业部和优惠客户和资产信息类型获取产品列表
     * @param orgId
     * @param ifPreferentialUser
     * @param assetType
     * @return
     */
    @Override
    public List<ResBMSProductVO> getProductListByOrgIdAndAssetType(Long orgId, String ifPreferentialUser, String assetType){
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        ReqQqProductVO reqQqProductVO = new ReqQqProductVO();
        reqQqProductVO.setOrgId(orgId);
        if (EnumUtils.YOrNEnum.Y.getValue().equals(ifPreferentialUser)) {
            reqQqProductVO.setIsCanPreferential("0");
        } else if (EnumUtils.YOrNEnum.N.getValue().equals(ifPreferentialUser)) {
            reqQqProductVO.setIsCanPreferential("1");
        }
        reqQqProductVO.setAssetsEnums(Arrays.asList(assetType.split(",")));
        ResListVO<ResBMSProductVO> response = productExecuter.getByOrgIdAndAssetEnum(reqQqProductVO);
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getCollections())) {
                list = response.getCollections();
            }
        } else {
            LOGGER.error("前前获取审批产品异常 params:{} result:{}",JSON.toJSONString(reqQqProductVO), JSON.toJSONString(response));
            throw new BusinessException("前前获取产品异常");
        }
        return list;
    }


    @Override
    public List<ResBMSProductVO> getAllProductList() {
        ReqBMSProductVO req = new ReqBMSProductVO();
        req.setSysCode(sysCode);
        ResListVO<ResBMSProductVO> response = productExecuter.getAll(req);
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return CollectionUtils.isEmpty(response.getCollections()) ? new ArrayList<ResBMSProductVO>() : response.getCollections();
        }
        return new ArrayList<ResBMSProductVO>();
    }

    @Override
    public List<ResBMSEnumCodeVO> findCodeByUnit(String code) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        ReqBMSEnumCodeVO request = new ReqBMSEnumCodeVO();
        request.setSysCode(sysCode);
        request.setCode(code);
        try {
            Response<List<ResBMSEnumCodeVO>> response = enumExecuter.getByWorkType(request);
            LOGGER.info("根据客户工作类型查询单位性质 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
                list = response.getData();
            }
        } catch (Exception e) {
            LOGGER.error("根据客户工作类型查询单位性质异常", e);
        }
        return list;
    }

    @Override
    public List<ResBMSEnumCodeVO> findCodeByProfession(String code, String parentCode) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        ReqBMSEnumCodeVO request = new ReqBMSEnumCodeVO();
        request.setSysCode(sysCode);
        request.setCode(code);
        request.setParentCode(parentCode);
        try {
            Response<List<ResBMSEnumCodeVO>> response = enumExecuter.getByCompanyType(request);
            LOGGER.info("根据客户工作类型和单位性质查询职业 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
            if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
                list = response.getData();
            }
        } catch (Exception e) {
            LOGGER.error("根据客户工作类型和单位性质查询职业异常", e);
        }
        return list;
    }

    /**
     * 校验审批商品审批期限审批金额是否有效
     *
     * @param loanNo
     * @return true被禁用;false没有被禁用
     */
    @Override
    public boolean checkApprovalProduct(String loanNo) {
        boolean action = false;
        Response<Boolean> response = applyInfoExecuter.approvalDisabled(loanNo);
        LOGGER.info("校验审批商品审批期限审批金额是否有效 params:[loanNo:{}] result:{}", loanNo, JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            action = response.getData();
        } else {
            LOGGER.error("校验审批商品审批期限审批金额是否有效异常");
            throw new BusinessException("判断产品是否有效异常");
        }
        return action;
    }

    /**
     * 获取签约补充联系人
     * @param loanNo
     * @return
     */
    public List<ResSupplementalContacts> getSupplementalContacts(String loanNo) {
        List<ResSupplementalContacts> list = null;
        Response<List<ResSupplementalContacts>> response =  applyInfoExecuter.getSupplementalContacts(loanNo);
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getData())) {
                list = response.getData();
            }
        } else {
            LOGGER.error("综合查询获取补充联系人信息异常 params:{} result:{}", loanNo , JSON.toJSONString(response));
            throw  new BusinessException("获取补充联系人异常");
        }
        return list;
    }
}
