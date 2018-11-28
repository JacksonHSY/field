package com.yuminsoft.ams.system.service.engine;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;

public interface RuleEngineService {
	/**
	 * 调用规则引擎
	 *
	 * @param executeType-执行环节类型(枚举)
	 * @return-Result<AuditRulesVO> 1.result 中的RuleEngineVO为规则引擎返回值,如果有拒绝就包括拒绝原因码 2.result 中的massage 为规则引擎提示。默认第一个是规则引擎类型(EngineType 参考枚举值),第二个是规则引擎执行时间,其他为规则引擎提示值 3.目前除了系统初判需要返回规则引擎AuditRulesVO六个值以外其他都不用
	 * @author dmz
	 * @date 2017-06-30
	 */
	Result<RuleEngineVO> handleNodeRuleEngine(ReqInformationVO applyBasiceInfo, EnumUtils.ExecuteTypeEnum executeType, String ip);

	/**
	 * 执行规则引擎
	 *
	 * @author dmz
	 * @date 2017年9月4日
	 * @param loanNo-借款编号
	 * @param executeType-执行环节类型(枚举)
	 * @return Result<RuleEngineVO>1.result 中的RuleEngineVO为规则引擎返回值,如果有拒绝就包括拒绝原因码 2.result 中的massage 为规则引擎提示。默认第一个是规则引擎类型(EngineType 参考枚举值),第二个是规则引擎执行时间,其他为规则引擎提示值 3.目前除了系统初判需要返回规则引擎AuditRulesVO六个值以外其他都不用
	 */
	Result<RuleEngineVO> executeRuleEngine(String loanNo, EnumUtils.ExecuteTypeEnum executeType);


	/**
	 * 规则引擎修复数据
	 * @return
	 */
	void repairData(String loaNo)throws  Exception;

}
