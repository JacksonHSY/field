package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSONObject;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.dao.approve.ApproveCheckInfoMapper;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.service.GenericCrudService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckInfoService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApproveCheckInfoServiceImpl extends GenericCrudService<ApproveCheckInfo, Long> implements ApproveCheckInfoService {

    @Autowired
    private ApproveCheckInfoMapper approveCheckInfoMapper;

    @Autowired
    public ApproveCheckInfoServiceImpl(ApproveCheckInfoMapper mapper) {
        super(mapper);
    }

    @Override
    @SneakyThrows
    public ApproveCheckInfo saveOrUpdate(ApproveCheckInfo approveCheckInfo) {
        LOGGER.info("保存资料核对薪资, approveCheckInfo:{}", JSONObject.toJSONString(approveCheckInfo));
        if(approveCheckInfo.getId() == null){
            approveCheckInfo.setCreatedBy(ShiroUtils.getAccount());
            approveCheckInfo.setCreatedDate(new Date());
            approveCheckInfo.setLastModifiedBy(ShiroUtils.getAccount());
            approveCheckInfo.setLastModifiedDate(new Date());
            save(approveCheckInfo);
        }else{
            ApproveCheckInfo existsApproveCheckInfo = getById(approveCheckInfo.getId());
            PropertyUtils.copyProperties(existsApproveCheckInfo, approveCheckInfo);
            approveCheckInfo.setLastModifiedBy(ShiroUtils.getAccount());
            approveCheckInfo.setLastModifiedDate(new Date());
            update(approveCheckInfo);
        }

        return getById(approveCheckInfo.getId());
    }



    @Override
    public ApproveCheckInfo getByLoanNo(String loanNo) {
        return approveCheckInfoMapper.findByLoanNo(loanNo);
    }
}
