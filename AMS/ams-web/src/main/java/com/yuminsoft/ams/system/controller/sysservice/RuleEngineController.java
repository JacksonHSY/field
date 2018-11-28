package com.yuminsoft.ams.system.controller.sysservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ApprovalOpinionsVO;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 规则引擎
 *
 * @author dmz
 * @date 2017年7月3日
 */
@Controller
@RequestMapping("/ruleEngine")
public class RuleEngineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineController.class);
    @Autowired
    private RuleEngineService ruleEngineService;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 调用规则引擎
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年7月3日
     */
    @RequestMapping("/executeRuleEngine/{loanNo}/{executeType}")
    @ResponseBody
    public Result<RuleEngineVO> executeRuleEngine(@PathVariable String loanNo, @PathVariable EnumUtils.ExecuteTypeEnum executeType, ApprovalOpinionsVO approvalOpinionsVO, HttpServletRequest request) {
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, false);
        Result<RuleEngineVO> result = new Result<RuleEngineVO>(Type.FAILURE);
        // 初审系统初判-- 需要时时传惨(审核产品|月均流水合计|外部负债总额)
        // 终审系统初判--  需要时时传惨(审核产品)
        if (EnumUtils.ExecuteTypeEnum.XSCS03.equals(executeType) || EnumUtils.ExecuteTypeEnum.XSZS03.equals(executeType)) {
            ReqAssetsInfoVO saveAssetsVO = JSONObject.parseObject(approvalOpinionsVO.getAssetsInfo(), ReqAssetsInfoVO.class);
            approvalOpinionsVO.setAssetsInfo(null);
            approvalOpinionsVO.setSaveAssetsVO(saveAssetsVO);
            LOGGER.info("规则引擎调用取页面参数 params:{}", JSON.toJSONString(approvalOpinionsVO));
            // key = 借款编号 + -ruleEngine- + executeType
            redisUtil.set(loanNo + "-ruleEngine-" + executeType.name(), approvalOpinionsVO, 1800L);
        }
        try {
            result = ruleEngineService.handleNodeRuleEngine(applyBasiceInfo, executeType, WebUtils.retrieveClientIp(request));
        } catch (Exception e) {
            result.addMessage("调用规则引擎失败");
            LOGGER.info("调用规则引擎失败", e);
        }
        return result;
    }
}
