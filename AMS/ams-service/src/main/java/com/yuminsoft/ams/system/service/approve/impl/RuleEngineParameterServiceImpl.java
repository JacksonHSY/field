package com.yuminsoft.ams.system.service.approve.impl;

import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.RuleEngineParameterMapper;
import com.yuminsoft.ams.system.domain.approve.RuleEngineParameter;
import com.yuminsoft.ams.system.service.approve.RuleEngineParameterService;
import com.yuminsoft.ams.system.vo.apply.RuleEngineParameterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 规则引擎返回值
 *
 * @Author dmz
 * Created by YM10106 on 2018/6/11.
 */

@Service
public class RuleEngineParameterServiceImpl implements RuleEngineParameterService {

    @Autowired
    private RuleEngineParameterMapper ruleEngineParameterMapper;

    /**
     * 根据借款编号查询出最后一次保存值
     *
     * @param loanNo
     * @return
     */
    @Override
    public RuleEngineParameterVO getLastSaveByLoanNo(String loanNo) {
        RuleEngineParameterVO ruleEngineParameterVO = null;
        RuleEngineParameter ruleEngineParameter = ruleEngineParameterMapper.findByLoanNo(loanNo);
        if (null != ruleEngineParameter) {
            ruleEngineParameterVO = new RuleEngineParameterVO();
            BeanUtils.copyProperties(ruleEngineParameter, ruleEngineParameterVO);
        }
        return ruleEngineParameterVO;
    }

    /**
     * 根据借款编号获取最新手动调用规则引擎值
     * @param loanNo
     * @param remark-标记是否已分派
     * @return
     */
    @Override
    public RuleEngineParameterVO findHandByLoanNo(String loanNo, boolean remark) {
        RuleEngineParameterVO ruleEngineParameterVO = null;
        if (remark == true) {// 判断当前状态是否是分派状态
            ruleEngineParameterVO = getLastSaveByLoanNo(loanNo);
            if(null != ruleEngineParameterVO) {// 如果最后一次是分派调用规则 则不显示
                if (ruleEngineParameterVO.getExecuteType().equals(EnumUtils.ExecuteTypeEnum.XSCS06.name())) {
                    return null;
                }
            } else {
                return null;
            }
        }
        RuleEngineParameter ruleEngineParameter = ruleEngineParameterMapper.findHandByLoanNo(loanNo);
        if (null != ruleEngineParameter) {
            ruleEngineParameterVO = new RuleEngineParameterVO();
            BeanUtils.copyProperties(ruleEngineParameter, ruleEngineParameterVO);
        }
        return ruleEngineParameterVO;
    }
}
