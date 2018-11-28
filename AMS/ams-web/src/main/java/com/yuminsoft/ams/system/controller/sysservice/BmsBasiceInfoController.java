package com.yuminsoft.ams.system.controller.sysservice;

import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.ReqAuditDifferencesVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.ams.api.vo.response.master.ResBMSBaseAreaTreeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSEnumCodeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.WebUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 借款系统基本信息查询接口
 *
 * @author dmz
 * @date 2017年3月1日
 */
@Controller
@RequestMapping("/bmsBasiceInfo")
public class BmsBasiceInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BmsBasiceInfoController.class);

    @Autowired
    private BmsBasiceInfoService bmsBasiceInfoService;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${sys.code}")
    private String sysCode;

    /**
     * 查询省市区树形结构
     *
     * @author dmz
     * @date 2017年3月1日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/getProvinceCorrespond")
    @ResponseBody
    public List<ResBMSBaseAreaTreeVO> getProvinceCorrespond() {
        List<ResBMSBaseAreaTreeVO> list = new ArrayList<ResBMSBaseAreaTreeVO>();
        if (redisUtil.exists("ams_area_tree")) {
            return (List<ResBMSBaseAreaTreeVO>) redisUtil.get("ams_area_tree");
        }
        try {
            list = bmsBasiceInfoService.listByTree();
            if (null != list && !list.isEmpty()) {
                redisUtil.set("ams_area_tree", list, 3600L);
            }
        } catch (Exception e) {
            LOGGER.info("省市区查询地区 异常:" + e);
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
    @SuppressWarnings("unchecked")
    @RequestMapping("getProductList")
    @ResponseBody
    public List<ResBMSProductVO> getProductList() {
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        if (redisUtil.exists("ams_product_list")) {
            return (List<ResBMSProductVO>) redisUtil.get("ams_product_list");
        }
        try {
            list = bmsBasiceInfoService.getProductList();
            if (null != list && !list.isEmpty()) {
                redisUtil.set("ams_product_list", list, 3600L);
            }
        } catch (Exception e) {
            LOGGER.info("获取产品列表异常:" + e);
        }
        return list;
    }

    /**
     * 获取产品列表根据进件营业部获取产品(时时查询)
     * @param owningBranchId-进件网点id
     *@param ifPreferentialUser-是否是优惠客户：Y是,N否
     *
     * @return
     * @author zw
     * @date 2017年05月24日
     */
    @RequestMapping("getProductListByOrgId/{owningBranchId}/{ifPreferentialUser}")
    @ResponseBody
    public List<ResBMSProductVO> getProductListByOrgId(@PathVariable String owningBranchId, @PathVariable String ifPreferentialUser) {
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        try {
            list = bmsBasiceInfoService.getProductListByOrgId(owningBranchId, ifPreferentialUser);
        } catch (Exception e) {
            LOGGER.info("根据进件营业部ID获取产品列表异常:" + e);
        }
        return list;
    }

    /**
     * 前前获取产品列表根据进件营业部获取产品(时时查询)
     * @param owningBranchId-进件网点id
     * @param ifPreferentialUser-是否是优惠客户：Y是,N否
     * @param assetTypeList-资产类型
     *
     * @return
     * @author zw
     * @date 2017年05月24日
     */
    @RequestMapping("getProductListByOrgIdAndAssetType")
    @ResponseBody
    public List<ResBMSProductVO> getMoneyProductListByOrgId(Long owningBranchId, String ifPreferentialUser, String assetTypeList) {
        List<ResBMSProductVO> list = new ArrayList<ResBMSProductVO>();
        try {
            list = bmsBasiceInfoService.getProductListByOrgIdAndAssetType(owningBranchId, ifPreferentialUser, assetTypeList);
        } catch (Exception e) {
            LOGGER.info("根据进件营业部ID获取产品列表异常:" + e);
        }
        return list;
    }


    /**
     * 下拉类型枚举
     *
     * @param emnuType
     * @param app-是否前前进件1:是，0：否
     * @author dmz
     * @date 2017年3月6日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("getEnumCode")
    @ResponseBody
    public List<ResBMSEnumCodeVO> listEnumCodeBy(String emnuType, String app) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        if (redisUtil.exists("ams_emnuType_" + emnuType +"_"+ app)) {// 前前枚举还是非前前枚举
            return (List<ResBMSEnumCodeVO>) redisUtil.get("ams_emnuType_" + emnuType +"_"+ app);
        }
        if (null != emnuType && !emnuType.isEmpty()) {
            try {
                list = bmsBasiceInfoService.getListEnumCodeBy(emnuType, app);
                ResBMSEnumCodeVO res = new ResBMSEnumCodeVO();
                res.setCode("");
                res.setNameCN("-请选择-");
                list.add(0, res);
                if (null != list && !list.isEmpty()) {
                    redisUtil.set("ams_emnuType_" + emnuType+"_"+ app, list, 3600L);
                }
            } catch (Exception e) {
                LOGGER.error("枚举查询  listEnumCodeBy 异常:" + e);
            }
        } else {
            LOGGER.info("枚举类型为空");
        }
        return list;
    }

    /**
     * edit by zw at 2017-04-24 更换接口通过产品code，门店id，ip等获取审批期限 TODO 后续删除 2017-05-18
     *
     * @param code
     * @return
     * @author dmz
     * @date 2017年4月5日
     */
    @RequestMapping("/listProductLimitBy")
    @ResponseBody
    public List<ResBMSOrgLimitChannelVO> listProductLimitBy(String code, String contractBranchId, HttpServletRequest request) {
        List<ResBMSOrgLimitChannelVO> list = new ArrayList<ResBMSOrgLimitChannelVO>();
        if (null != code && !code.isEmpty()) {
            try {
                list = bmsBasiceInfoService.listProductLimitBy(code, contractBranchId, WebUtils.retrieveClientIp(request));
            } catch (Exception e) {
                LOGGER.info("获取产品期限异常:{}", e);
            }
        }
        return list;
    }

    /**
     * 根据产品Id, 获取资产配置信息
     *
     * @author zhouwen
     */
    @RequestMapping("/getListProductAssetsInfoByCode/{code}")
    @ResponseBody
    public Result<ResBMSEnumCodeVO> getListProductAssetsInfoByCode(@PathVariable String code) {
        Result<ResBMSEnumCodeVO> result = new Result<ResBMSEnumCodeVO>(Type.FAILURE);
        try {
            List<ResBMSEnumCodeVO> list = bmsBasiceInfoService.getAssetsByProdCode(code);
            result.setDataList(list);
            result.setType(Type.SUCCESS);
        } catch (Exception e) {
            result.addMessage("系统异常");
            LOGGER.error("资产信息查询异常:", e);
        }

        return result;
    }

    /**
     * 获取拒绝或者退回原因列表
     *
     * @param req
     * @return
     * @author JiaCX
     * @date 2017年5月5日 上午10:17:58
     */
    @RequestMapping("/getReasonList")
    @ResponseBody
    public List<ReqBMSTMReasonVO> getReasonList(ReqBMSTMReasonVO req) {
        List<ReqBMSTMReasonVO> list = new ArrayList<ReqBMSTMReasonVO>();
        try {
            list = bmsBasiceInfoService.getReasonList(req);
        } catch (Exception e) {
            LOGGER.info("获取拒绝或者退回原因列表异常:", e);
        }
        return list;
    }

    /**
     * 批量获取拒绝或者退回原因列表
     *
     * @param arr
     * @return
     * @author JiaCX
     * @date 2017年8月3日 上午11:15:29
     */
    @RequestMapping("/getBatchReasonList")
    public @ResponseBody
    List<ReqBMSTMReasonVO> getBatchReasonList(@RequestBody List<ReqBMSTMReasonVO> arr) {
        List<ReqBMSTMReasonVO> returnlist = new ArrayList<ReqBMSTMReasonVO>();
        try {
            if (!CollectionUtils.isEmpty(arr)) {
                for (ReqBMSTMReasonVO req : arr) {
                    List<ReqBMSTMReasonVO> list = bmsBasiceInfoService.getReasonList(req);
                    returnlist.addAll(list);
                }
            }
        } catch (Exception e) {
            LOGGER.info("获取拒绝或者退回原因列表异常:", e);
        }
        return returnlist;
    }

    /**
     * 返回产品期限
     *
     * @param owningBranchId-进件网点id
     * @param productCode-产品code
     * @param applyLmt-审批金额
     * @param isCanPreferential-是否是优惠配置，0是 1否
     * @param req-request
     * @return
     * @author dmz
     * @date 2017年5月19日
     */
    @RequestMapping("/getProductLimitList")
    @ResponseBody
    public List<ResBMSOrgLimitChannelVO> getProductLimitList(Long owningBranchId, String productCode, BigDecimal applyLmt, String isCanPreferential, HttpServletRequest req) {
        List<ResBMSOrgLimitChannelVO> list = new ArrayList<ResBMSOrgLimitChannelVO>();
        ReqBMSOrgLimitChannelVO request = new ReqBMSOrgLimitChannelVO();
        request.setProductCode(productCode);
        request.setApplyLmt(applyLmt);
        request.setOrgId(owningBranchId);
        request.setServiceCode(ShiroUtils.getCurrentUser().getUsercode());
        request.setServiceName(ShiroUtils.getCurrentUser().getName());
        request.setIp(WebUtils.retrieveClientIp(req));
        request.setIsCanPreferential(isCanPreferential);
        try {
            list = bmsBasiceInfoService.listOrgProductLimitByOrgProApp(request);
        } catch (Exception e) {
            LOGGER.info("查询产品期限异常:", e);
        }
        return list;
    }

    /**
     * 返回产品或者期限对应的审批上下限
     *
     * @param owningBranchId-进件网点id
     * @param productCode-产品code
     * @param auditLimit-期限
     * @param isCanPreferential-是否是优惠配置，0是 1否
     * @param req-request
     * @return
     * @author dmz
     * @date 2017年5月19日
     */
    @RequestMapping("/getProductUpperLower")
    @ResponseBody
    public Result<ResBMSOrgLimitChannelVO> getProductUpperLower(Long owningBranchId, String productCode, Integer auditLimit, String isCanPreferential, HttpServletRequest req) {
        Result<ResBMSOrgLimitChannelVO> result = new Result<ResBMSOrgLimitChannelVO>(Type.FAILURE);
        ReqBMSOrgLimitChannelVO request = new ReqBMSOrgLimitChannelVO();
        request.setProductCode(productCode);
        request.setAuditLimit(auditLimit);
        request.setOrgId(owningBranchId);
        request.setServiceCode(ShiroUtils.getCurrentUser().getUsercode());
        request.setServiceName(ShiroUtils.getCurrentUser().getName());
        request.setIp(WebUtils.retrieveClientIp(req));
        request.setIsCanPreferential(isCanPreferential);
        try {
            result.setData(bmsBasiceInfoService.findOrgLimitChannelLimitUnion(request));
            result.setType(Type.SUCCESS);
        } catch (Exception e) {
            LOGGER.info("返回产品或者期限对应的审批上下限异常", e);
            result.addMessage("系统错误!");
        }
        return result;
    }

    /**
     * 查询出初审或终审工作台待办任务总数
     *
     * @param taskDefId
     * @return
     * @author dmz
     * @date 2017年5月23日
     */
    @RequestMapping("/getUserTaskNumber/{taskDefId}")
    @ResponseBody
    public Result<Integer> getUserTaskNumber(@PathVariable String taskDefId) {
        Result<Integer> result = new Result<Integer>(Type.FAILURE);
        try {
            if (EnumUtils.FirstOrFinalEnum.FIRST.getValue().equals(taskDefId)) {// 信审初审
                result = bmsLoanInfoService.getFirstTaskNumber();
            } else {// 信审终审
                result = bmsLoanInfoService.getFinalTaskNumber();
            }
        } catch (Exception e) {
            LOGGER.info("查询待办任务总数异常", e);
        }
        return result;
    }

    /**
     * 客户信息中根据客户工作类型查询单位性质
     *
     * @param code
     * @author zhouwen
     * @date 2017年8月28日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/findCodeByUnit")
    @ResponseBody
    public List<ResBMSEnumCodeVO> findCodeByUnit(String code) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        try {
            if (redisUtil.exists("ams_customerInfo_corpStructure_" + code)) {
                return (List<ResBMSEnumCodeVO>) redisUtil.get("ams_customerInfo_corpStructure_" + code);
            }
            if (Strings.isNotEmpty(code)) {
                list = bmsBasiceInfoService.findCodeByUnit(code);
                ResBMSEnumCodeVO res = new ResBMSEnumCodeVO();
                res.setCode("");
                res.setNameCN("-请选择-");
                list.add(0, res);
            }
            if (!CollectionUtils.isEmpty(list)) {
                redisUtil.set("ams_customerInfo_corpStructure_" + code, list, 3600L);
            }
        } catch (Exception e) {
            LOGGER.error("客户信息根据客户工作类型查询单位性质异常", e);
        }
        return list;
    }

    /**
     * 客户信息中根据客户工作类型查询单位性质
     *
     * @param parentCode
     * @param code
     * @author zhouwen
     * @date 2017年8月28日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/findCodeByProfession")
    @ResponseBody
    public List<ResBMSEnumCodeVO> findCodeByProfession(String code, String parentCode) {
        List<ResBMSEnumCodeVO> list = new ArrayList<ResBMSEnumCodeVO>();
        try {
            if (redisUtil.exists("ams_customerInfo_corpStructure_" + parentCode + "_" + code)) {
                return (List<ResBMSEnumCodeVO>) redisUtil.get("ams_customerInfo_corpStructure_" + parentCode + "_" + code);
            }
            if (Strings.isNotEmpty(code) && Strings.isNotEmpty(parentCode)) {
                list = bmsBasiceInfoService.findCodeByProfession(code, parentCode);
                ResBMSEnumCodeVO res = new ResBMSEnumCodeVO();
                res.setCode("");
                res.setNameCN("-请选择-");
                list.add(0, res);
            }
            if (null != list && !list.isEmpty()) {
                redisUtil.set("ams_customerInfo_corpStructure_" + parentCode + "_" + code, list, 3600L);
            }
        } catch (Exception e) {
            LOGGER.error("客户信息根据客户工作类型和单位性质查询职业异常", e);
        }
        return list;

    }

    /**
     * 初审挂起
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月9日
     */
    @RequestMapping("/deleteContactInfo")
    @ResponseBody
    @Deprecated
    public Result<String> firstHandleHang(String loanNo, String sequenceNum, String version) {
        Result<String> result = new Result<>(Type.FAILURE);
        try {
            String userCode = ShiroUtils.getAccount();
            LOGGER.info("客户信息删除联系人,借款编号[{}],联系人序列号[{}],操作人[{}]", loanNo, sequenceNum, userCode);
            ReqAuditDifferencesVO request = new ReqAuditDifferencesVO();
            request.setSequenceNum(sequenceNum);
            request.setLoanNo(loanNo);
            request.setVersion(version);
            result = bmsLoanInfoService.deleteContactInfo(request);
        } catch (Exception e) {
            LOGGER.error("删除联系人失败", e);
            result.setType(Type.FAILURE);
            result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);
        }
        return result;
    }

    /**
     * 获取所有拒绝或者退回原因的树形结构
     *
     * @param type
     * @param module
     * @return
     * @author JiaCX
     * @date 2017年9月7日 下午2:07:22
     */
    @RequestMapping("/getReasonTree/{module}/{type}")
    public @ResponseBody
    List<ReqBMSTMReasonVO> getReasonTree(@PathVariable String type, @PathVariable String module) {
        ReqBMSTMReasonVO req = new ReqBMSTMReasonVO();
        req.setOperationType(type);
        req.setOperationModule(module);
        req.setSysCode(sysCode);
        return bmsLoanInfoService.findReasonByOperType(req);
    }

    /**
     * 判断用户和身份证是否有改变
     *
     * @param loanNo-借款编号
     * @param taskDefId-初审或终审
     * @param session
     * @return
     */
    @RequestMapping("/judgeCustomerOrIDNOChange")
    @ResponseBody
    public Result<Boolean> judgeCustomerOrIDNOChange(String loanNo, String taskDefId, HttpSession session) {
        Result<Boolean> result = new Result<Boolean>(Type.SUCCESS);
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
        if (EnumUtils.FirstOrFinalEnum.FIRST.getValue().equals(taskDefId) && EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(applyBasiceInfo.getIfNewLoanNo())) {// 初审第一次
            result.setData(false);
        } else if (EnumUtils.FirstOrFinalEnum.FINAL.getValue().equals(taskDefId) && EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(applyBasiceInfo.getZsIfNewLoanNo().toString())) { // 终审第一次
            result.setData(false);
        } else {
            result = bmsLoanInfoService.judgeCustomerOrIDNOChange(applyBasiceInfo);
        }
        return result;
    }
}
