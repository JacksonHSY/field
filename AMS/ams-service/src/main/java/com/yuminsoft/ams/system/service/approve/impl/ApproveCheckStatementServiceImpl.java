package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSONObject;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApproveCheckStatementMapper;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;
import com.yuminsoft.ams.system.service.GenericCrudService;
import com.yuminsoft.ams.system.service.approve.ApproveCheckStatementService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 资料核对流水信息
 * @author wulj
 */
@Service
public class ApproveCheckStatementServiceImpl extends GenericCrudService<ApproveCheckStatement, Long> implements ApproveCheckStatementService {

    @Autowired
    private ApproveCheckStatementMapper approveCheckStatementMapper;

    @Autowired
    public ApproveCheckStatementServiceImpl(ApproveCheckStatementMapper mapper) {
        super(mapper);
    }


    @Override
    public void batchSaveOrUpdate(List<ApproveCheckStatement> approveCheckStatementList) {
        for (ApproveCheckStatement approveCheckStatement : approveCheckStatementList) {
            saveOrUpdate(approveCheckStatement);
        }
    }

    @Override
    @SneakyThrows
    public ApproveCheckStatement saveOrUpdate(ApproveCheckStatement approveCheckStatement) {
        LOGGER.info("保存资料核对流水, approveCheckStatement:{}", JSONObject.toJSONString(approveCheckStatement));
        if(approveCheckStatement.getId() == null){
            approveCheckStatement.setCreatedBy(ShiroUtils.getAccount());
            approveCheckStatement.setCreatedDate(new Date());
            approveCheckStatement.setLastModifiedBy(ShiroUtils.getAccount());
            approveCheckStatement.setLastModifiedDate(new Date());
            save(approveCheckStatement);
        }else{
            ApproveCheckStatement existsApproveCheckInfo = getById(approveCheckStatement.getId());
            PropertyUtils.copyProperties(existsApproveCheckInfo, approveCheckStatement);
            approveCheckStatement.setLastModifiedBy(ShiroUtils.getAccount());
            approveCheckStatement.setLastModifiedDate(new Date());
            update(approveCheckStatement);
        }

        return getById(approveCheckStatement.getId());
    }

    @Override
    public List<ApproveCheckStatement> getByLoanNo(String loanNo) {

        return approveCheckStatementMapper.findByLoanNo(loanNo);
    }

    @Override
    public List<ApproveCheckStatement> getByLoanNoAndType(String loanNo, EnumUtils.CheckStatementType type) {

        return approveCheckStatementMapper.findByLoanNoAndType(loanNo, type.name());
    }

    @Override
    public int removeByLoanNo(String loanNo) {
        return approveCheckStatementMapper.deleteByLoanNo(loanNo);
    }
}
