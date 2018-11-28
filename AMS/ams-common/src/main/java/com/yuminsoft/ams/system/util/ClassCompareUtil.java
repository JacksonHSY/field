package com.yuminsoft.ams.system.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.vo.request.apply.AuditEntryVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassCompareUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassCompareUtil.class);

    /**
     * 标红生成对应的标记
     *
     * @param source
     * @param target
     * @return
     * @date 2017年5月13日
     * @author dmz
     */
    public static JSONObject entitycomparison(AuditEntryVO source, AuditEntryVO target) {
        JSONObject jsonObj = new JSONObject();
        if (null != source && null != target) {// 对比对象不为空判断
            // 申请信息
            jsonObj.put("applyInfoVO", classOfSrc(source.getApplyInfoVO(), target.getApplyInfoVO()));

            // Start基本信息
            JSONObject basiceInfo = new JSONObject();
            /** 个人信息 **/
            basiceInfo.put("personInfoVO", classOfSrc(source.getBasicInfoVO().getPersonInfoVO(), target.getBasicInfoVO().getPersonInfoVO()));
            /** 工作信息 **/
            basiceInfo.put("workInfoVO", classOfSrc(source.getBasicInfoVO().getWorkInfoVO(), target.getBasicInfoVO().getWorkInfoVO()));
            /** 私营业主信息 **/
            basiceInfo.put("privateOwnerInfoVO", classOfSrc(source.getBasicInfoVO().getPrivateOwnerInfoVO(), target.getBasicInfoVO().getPrivateOwnerInfoVO()));
            jsonObj.put("basicInfoVO", basiceInfo);
            // End基本信息
            // Start 资产信息
            JSONObject assetsInfo = new JSONObject();
            /** 房产信息 **/
            assetsInfo.put("estateInfoVO", classOfSrc(source.getAssetsInfoVO().getEstateInfoVO(), target.getAssetsInfoVO().getEstateInfoVO()));
            /** 车辆信息 **/
            assetsInfo.put("carInfoVO", classOfSrc(source.getAssetsInfoVO().getCarInfoVO(), target.getAssetsInfoVO().getCarInfoVO()));
            /** 保单信息 **/
            assetsInfo.put("policyInfoVO", classOfSrc(source.getAssetsInfoVO().getPolicyInfoVO(), target.getAssetsInfoVO().getPolicyInfoVO()));
            /** 公积金信息 **/
            assetsInfo.put("providentInfoVO", classOfSrc(source.getAssetsInfoVO().getProvidentInfoVO(), target.getAssetsInfoVO().getProvidentInfoVO()));
            /** 卡友贷信息 **/
            assetsInfo.put("cardLoanInfoVO", classOfSrc(source.getAssetsInfoVO().getCardLoanInfoVO(), target.getAssetsInfoVO().getCardLoanInfoVO()));
            /** 随薪贷信息 **/
            assetsInfo.put("salaryLoanInfoVO", classOfSrc(source.getAssetsInfoVO().getSalaryLoanInfoVO(), target.getAssetsInfoVO().getSalaryLoanInfoVO()));
            /** 网购达人贷信息 **/
            assetsInfo.put("masterLoanInfoVO", classOfSrc(source.getAssetsInfoVO().getMasterLoanInfoVO(), target.getAssetsInfoVO().getMasterLoanInfoVO()));
            /** 淘宝商户贷信息 **/
            assetsInfo.put("merchantLoanInfoVO", classOfSrc(source.getAssetsInfoVO().getMerchantLoanInfoVO(), target.getAssetsInfoVO().getMerchantLoanInfoVO()));
            jsonObj.put("assetsInfoVO", assetsInfo);
            // End 资产信息
            // 联系人List
            JSONArray jsonArray = new JSONArray();
            if (!CollectionUtils.isEmpty(source.getContactInfoVOList())) {
                for (int i = 0; i < source.getContactInfoVOList().size() ; i++) {
                    if (i < target.getContactInfoVOList().size()) {
                        jsonArray.add(i, classOfSrc(source.getContactInfoVOList().get(i), target.getContactInfoVOList().get(i)));
                    } else {
                        jsonArray.add(i, classOfSrc(source.getContactInfoVOList().get(i), null));
                    }
                }
                jsonObj.put("contactInfoVOList", jsonArray);
            }
            LOGGER.info(JSON.toJSONString(jsonObj));
        } else {
            LOGGER.info("借款接口返回数据为空(标红) source:{}---target:{}", JSON.toJSONString(source), JSON.toJSONString(target));
        }

        return jsonObj;
    }

    /**
     * 前前标红生成对应的标记
     *
     * @param source
     * @param target
     * @return
     * @author dmz
     * @date 2017年5月13日
     */
    public static JSONObject moneyEntitycomparison(ResApplicationInfoVO source, ResApplicationInfoVO target) {
        JSONObject jsonObj = new JSONObject();
        if (null != source && null != target) {// 对比对象不为空判断
            // 申请信息
            jsonObj.put("baseInfo", classOfSrc(source.getBaseInfo(), target.getBaseInfo()));

            // Start基本信息
            JSONObject basiceInfo = new JSONObject();
            /** 个人信息 **/
            basiceInfo.put("personalInfo", classOfSrc(source.getApplicantInfo().getPersonalInfo(), target.getApplicantInfo().getPersonalInfo()));
            /** 工作信息 **/
            basiceInfo.put("workInfo", classOfSrc(source.getApplicantInfo().getWorkInfo(), target.getApplicantInfo().getWorkInfo()));
            /** 私营业主信息 **/
            basiceInfo.put("privateOwnerInfo", classOfSrc(source.getApplicantInfo().getPrivateOwnerInfo(), target.getApplicantInfo().getPrivateOwnerInfo()));
            jsonObj.put("applicantInfo", basiceInfo);
            // End基本信息
            // Start 资产信息
            JSONObject assetsInfo = new JSONObject();
            /** 保单信息List **/
            JSONArray policyInfoList = new JSONArray();
            if (!CollectionUtils.isEmpty(source.getAssetsInfo().getPolicyInfo())) {
                for (int i = 0; i < source.getAssetsInfo().getPolicyInfo().size(); i++) {
                    if (i < target.getAssetsInfo().getPolicyInfo().size()) {
                        policyInfoList.add(i, classOfSrc(source.getAssetsInfo().getPolicyInfo().get(i), target.getAssetsInfo().getPolicyInfo().get(i)));
                    } else {
                        policyInfoList.add(i, classOfSrc(source.getAssetsInfo().getPolicyInfo().get(i), null));
                    }
                }
                assetsInfo.put("policyInfoList", policyInfoList);
            }
            /** 车辆信息 **/
            assetsInfo.put("carInfo", classOfSrc(source.getAssetsInfo().getCarInfo(), target.getAssetsInfo().getCarInfo()));
            /** 公积金信息 **/
            assetsInfo.put("fundInfo", classOfSrc(source.getAssetsInfo().getFundInfo(), target.getAssetsInfo().getFundInfo()));
            /** 社保信息 **/
            assetsInfo.put("socialInsuranceInfo", classOfSrc(source.getAssetsInfo().getSocialInsuranceInfo(), target.getAssetsInfo().getSocialInsuranceInfo()));
            /** 卡友贷信息 **/
            assetsInfo.put("cardLoanInfo", classOfSrc(source.getAssetsInfo().getCardLoanInfo(), target.getAssetsInfo().getCardLoanInfo()));
            /** 房产信息 **/
            assetsInfo.put("estateInfo", classOfSrc(source.getAssetsInfo().getEstateInfo(), target.getAssetsInfo().getEstateInfo()));
            /** 淘宝账户信息 **/
            assetsInfo.put("masterLoanInfo", classOfSrc(source.getAssetsInfo().getMasterLoanInfo(), target.getAssetsInfo().getMasterLoanInfo()));
            /***学历信息 **/
            assetsInfo.put("educationInfo", classOfSrc(source.getAssetsInfo().getEducationInfo(), target.getAssetsInfo().getEducationInfo()));
            jsonObj.put("assetsInfo", assetsInfo);
            // End 资产信息
            // 联系人List
            JSONArray jsonArray = new JSONArray();
            if (!CollectionUtils.isEmpty(source.getContactInfo())) {
                for (int i = 0; i < source.getContactInfo().size(); i++) {
                    if (i < target.getContactInfo().size()) {
                        jsonArray.add(i, classOfSrc(source.getContactInfo().get(i), target.getContactInfo().get(i)));
                    } else {
                        jsonArray.add(i, classOfSrc(source.getContactInfo().get(i), null));
                    }
                }
                jsonObj.put("contactInfoList", jsonArray);
            }
            LOGGER.info(JSON.toJSONString(jsonObj));
        } else {
            LOGGER.info("借款接口返回数据为空(标红) source:{}---target:{}", JSON.toJSONString(source), JSON.toJSONString(target));
        }

        return jsonObj;
    }

    /**
     * 源目标为非MAP类型时
     *
     * @param source
     * @param target
     * @return
     */
    public static JSONObject classOfSrc(Object source, Object target) {
        JSONObject jsonObj = new JSONObject();
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey).toString();
            String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey).toString();
            if ((srcValue == null && tarValue != null) || (srcValue != null && !srcValue.equals(tarValue))) {
                jsonObj.put(nameKey, "markRed");
            } else {
                jsonObj.put(nameKey, "noMarkRed");
            }
        }

        return jsonObj;
    }

    /**
     * 根据字段名称取值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        try {
            Class beanClass = obj.getClass();
            Method[] ms = beanClass.getMethods();
            for (int i = 0; i < ms.length; i++) {
                // 非get方法不取
                if (!ms[i].getName().startsWith("get")) {
                    continue;
                }
                Object objValue = null;
                try {
                    objValue = ms[i].invoke(obj, new Object[]{});
                } catch (Exception e) {
                    LOGGER.info("反射取值出错：" + e.toString());
                    continue;
                }
                if (objValue == null) {
                    continue;
                }
                if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase()) || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                    return objValue;
                } else if ("SID".equals(fieldName.toUpperCase()) && ("ID".equals(ms[i].getName().toUpperCase()) || "ID".equals(ms[i].getName().substring(3).toUpperCase()))) {
                    return objValue;
                }
            }
        } catch (Exception e) {
            // logger.info("取方法出错！" + e.toString());
        }

        return null;
    }

}
