package com.yuminsoft.ams.system.service.bms;


import com.ymkj.ams.api.vo.request.master.ReqBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.ams.api.vo.response.integratedsearch.ResSupplementalContacts;
import com.ymkj.ams.api.vo.response.master.ResBMSBaseAreaTreeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSEnumCodeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.yuminsoft.ams.system.util.Result;

import java.util.List;

public interface BmsBasiceInfoService {

    /***
     * 查询省市区树形结构
     *
     * @author dmz
     * @date 2017年3月21日
     * @return
     */
    List<ResBMSBaseAreaTreeVO> listByTree();

    /**
     * 获取产品列表
     *
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    List<ResBMSProductVO> getProductList();

    /**
     * 下拉类型枚举
     *
     * @param emnuType
     * @param app-是否前前进件1:是，0：否
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    List<ResBMSEnumCodeVO> getListEnumCodeBy(String emnuType, String app);

    /**
     * edit by zw at 2017-04-24 更换接口通过产品code，门店id，ip等获取审批期限 根据产品code获取期限
     *
     * @param code
     * @return
     * @author dmz
     * @date 2017年4月5日
     */
    List<ResBMSOrgLimitChannelVO> listProductLimitBy(String code, String contractBranchId, String ip);

    /**
     * 根据申请产品code获取资产配置信息
     *
     * @param code
     * @return list
     */
    List<ResBMSEnumCodeVO> getAssetsByProdCode(String code);

    /**
     * add by zw at 2017-04-25 根据产品code查询到产品信息
     *
     * @param code
     * @return ResBMSProductVO
     */
    Result<ResBMSProductVO> getBMSProductVOByCode(String code);

    /**
     * 返回退回,拒绝,挂起原因
     *
     * @param req
     * @return
     * @author dmz
     * @date 2017年5月16日
     */
    List<ReqBMSTMReasonVO> getReasonList(ReqBMSTMReasonVO req);

    /**
     * 返回产品期限
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年5月18日
     */
    List<ResBMSOrgLimitChannelVO> listOrgProductLimitByOrgProApp(ReqBMSOrgLimitChannelVO request);

    /**
     * 返回产品或者期限对应的审批上下限
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年5月18日
     */
    ResBMSOrgLimitChannelVO findOrgLimitChannelLimitUnion(ReqBMSOrgLimitChannelVO request);

    /**
     * 根据进件营业部和是否是优惠客户获取产品列表
     *
     * @param orgId-进件网点id
     * @param ifPreferentialUser-是否是优惠客户:Y是,N否
     * @return
     * @author zw
     * @date 2017年5月22日
     */
    List<ResBMSProductVO> getProductListByOrgId(String orgId, String ifPreferentialUser);

    /**
     * 前前根据进件营业部和优惠客户和资产信息类型获取产品列表
     * @param orgId
     * @param ifPreferentialUser
     * @param assetType
     * @return
     */
    List<ResBMSProductVO> getProductListByOrgIdAndAssetType(Long orgId, String ifPreferentialUser, String assetType);

    /**
     * 获取所有产品列表
     *
     * @return
     * @author JiaCX
     * @date 2017年6月8日 上午9:53:05
     */
    List<ResBMSProductVO> getAllProductList();

    /**
     * 客户信息中根据客户工作类型查询单位性质
     *
     * @param code
     * @author zhouwen
     * @date 2017年8月28日
     */
    List<ResBMSEnumCodeVO> findCodeByUnit(String code);

    /**
     * 客户信息中根据客户工作类型code和单位性质code查询职业
     *
     * @param code
     * @param parentCode
     * @return
     * @author zhouwen
     * @date 2017年8月28日
     */
    List<ResBMSEnumCodeVO> findCodeByProfession(String code, String parentCode);

    /**
     * 校验审批商品审批期限审批金额是否有效
     *
     * @param loanNo
     * @return true被禁用;false没有被禁用
     */
    boolean checkApprovalProduct(String loanNo);

	/**
	 * 获取签约补充联系人
	 * @param loanNo
	 * @return
	 */
	List<ResSupplementalContacts> getSupplementalContacts(String loanNo);
}
