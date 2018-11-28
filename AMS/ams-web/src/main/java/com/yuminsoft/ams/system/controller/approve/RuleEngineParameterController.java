package com.yuminsoft.ams.system.controller.approve;

import com.yuminsoft.ams.system.service.approve.RuleEngineParameterService;
import com.yuminsoft.ams.system.vo.apply.RuleEngineParameterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YM10106 on 2018/6/11.
 */
@Controller
@RequestMapping("/ruleEngineParameter")
public class RuleEngineParameterController {
    @Autowired
    private RuleEngineParameterService ruleEngineParameterService;

    /**
     * 根据借款编号查询出最后一次保存值
     *
     * @param loanNo
     * @return
     */
    @RequestMapping("/getRuleEngineParameterLast/{loanNo}")
    @ResponseBody
    public RuleEngineParameterVO getLastSaveByLoanNo(@PathVariable String loanNo) {
        RuleEngineParameterVO ruleEngineParameterVO = ruleEngineParameterService.getLastSaveByLoanNo(loanNo);
        if (null == ruleEngineParameterVO) {
            ruleEngineParameterVO = new RuleEngineParameterVO();
        }
        return ruleEngineParameterVO;
    }

    /**
     * 根据借款编号查询出最后一次手动调规则保存值
     * @param loanNo
     * @param remark-标记是否已分派
     * @return
     */
    @RequestMapping("/getRuleEngineParameterLastHand/{loanNo}/{remark}")
    @ResponseBody
    public RuleEngineParameterVO getLastHandRuleEnginParamByLoanNo(@PathVariable String loanNo,@PathVariable boolean remark) {
        RuleEngineParameterVO ruleEngineParameterVO = ruleEngineParameterService.findHandByLoanNo(loanNo,remark);
        if (null == ruleEngineParameterVO) {
            ruleEngineParameterVO = new RuleEngineParameterVO();
        }
        return ruleEngineParameterVO;
    }
}
