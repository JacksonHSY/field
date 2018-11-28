package com.yuminsoft.ams.system.service.approve.impl;

import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.dao.approve.MobileOnlineMapper;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.MobileOnlineService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzRequest;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzResponse;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.IdCardCheckInfoVo;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.MobileOnlineInfoVo;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MobileOnlineServiceImpl implements MobileOnlineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileOnlineServiceImpl.class);

    @Autowired
    private CreditzxService creditzxService;

    @Autowired
    private MobileOnlineMapper mobileOnlineMapper;

    /**
     * 查询入网时长和实名认证
     * @param loanNo
     * @param name
     * @param idNo
     * @param mobile
     * @return
     */
    @Override
    public MobileOnline getByLoanNoAndNameAndIdNoAndMobile(String loanNo, String name, String idNo, String mobile) {
        return mobileOnlineMapper.findByLoanNoAndNameAndIdNoAndMobile(loanNo, name, idNo, mobile);
    }

    /**
     * 创建入网时长和实名认证查询记录
     * @param mobileOnline
     * @return
     */
    @Override
    @SneakyThrows
    public MobileOnline createMobileOnline(MobileOnline mobileOnline) {
        MobileOnline existsMobileOnline = getByLoanNoAndNameAndIdNoAndMobile(mobileOnline.getLoanNo(), mobileOnline.getName(), mobileOnline.getIdNo(), mobileOnline.getMobile());
        if (existsMobileOnline == null) {
            existsMobileOnline = new MobileOnline();
            PropertyUtils.copyProperties(existsMobileOnline, mobileOnline);
            existsMobileOnline.setSearchTimes(0); // 默认征信系统调用查询次数=0
            existsMobileOnline = saveOrUpdate(existsMobileOnline);
        }

        // 调用超过5次，不再调用
        if(existsMobileOnline.getSearchTimes() < 5){
            HzRequest hzRequest = new HzRequest();
            hzRequest.setAppNo(existsMobileOnline.getLoanNo());
            hzRequest.setName(existsMobileOnline.getName());
            hzRequest.setIdCard(existsMobileOnline.getIdNo());
            hzRequest.setMobile(existsMobileOnline.getMobile());
            hzRequest.setIsCheck(true);
            hzRequest.setCreatorId(ShiroUtils.getAccount());
            hzRequest.setTimestamp(System.currentTimeMillis());
            // 更新入网时长
            if(mobileOnline.getMobileOnlineId() == null){
                HzResponse<MobileOnlineInfoVo> hzMobileResponse = creditzxService.getMobileOnlineInfoService(hzRequest);   // 查询入网时长
                if(hzMobileResponse.success()){
                    existsMobileOnline.setMobileOnlineId(hzMobileResponse.getData().getId());
                    existsMobileOnline.setMobileOnline(hzMobileResponse.getData().getTimes());
                }else{
                    existsMobileOnline.setMobileOnline(hzMobileResponse.getMessages());
                }
            }
            // 更新实名认证
            if(mobileOnline.getRealCertiId() == null){
                HzResponse<IdCardCheckInfoVo> hzIdCardResponse = creditzxService.getIdCardCheckInfo(hzRequest);            // 查询实名认证
                if(hzIdCardResponse.success()){
                    existsMobileOnline.setRealCertiId(hzIdCardResponse.getData().getId());
                    existsMobileOnline.setRealCerti(hzIdCardResponse.getData().getMsg());
                }else{
                    existsMobileOnline.setRealCerti(hzIdCardResponse.getMessages());
                }
            }
            // 更新征信系统调用查询次数
            existsMobileOnline.setSearchTimes(existsMobileOnline.getSearchTimes() + 1);
            saveOrUpdate(existsMobileOnline);
        }

        return existsMobileOnline;
    }

    /**
     * 更新或保存入网时长
     * @param mobileOnline
     * @return
     */
    @Override
    @SneakyThrows
    public MobileOnline saveOrUpdate(MobileOnline mobileOnline){
        LOGGER.info("保存或更新入网时长和实名认证, mobileOnline:{}", mobileOnline.toString());
        if(StringUtils.isEmpty(mobileOnline.getLoanNo())){
            throw new BusinessException("借款编号不能为空");
        }

        if (mobileOnline.getId() == null) {
            mobileOnline.setCreatedBy(ShiroUtils.getAccount());
            mobileOnline.setCreatedDate(new Date());
            mobileOnline.setLastModifiedBy(ShiroUtils.getAccount());
            mobileOnline.setLastModifiedDate(new Date());
            mobileOnlineMapper.insert(mobileOnline);

        }else{
            MobileOnline existsMobileOnline = getByLoanNoAndNameAndIdNoAndMobile(mobileOnline.getLoanNo(), mobileOnline.getName(), mobileOnline.getIdNo(), mobileOnline.getMobile());
            PropertyUtils.copyProperties(existsMobileOnline, mobileOnline);
            existsMobileOnline.setLastModifiedBy(ShiroUtils.getAccount());
            existsMobileOnline.setLastModifiedDate(new Date());
            mobileOnlineMapper.update(mobileOnline);

        }

        return mobileOnline;
    }
}
