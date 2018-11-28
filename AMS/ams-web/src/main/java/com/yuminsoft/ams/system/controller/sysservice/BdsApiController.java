package com.yuminsoft.ams.system.controller.sysservice;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.bds.biz.api.vo.request.MatchAntiFraudInfoReqVO;
import com.ymkj.bds.biz.api.vo.response.MatchAntiFraudInfoResVO;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 行为库接口控制
 * 
 * @author dmz
 * @date 2017年4月13日
 */
@Controller
@RequestMapping("/bdsApi")
public class BdsApiController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BdsApiController.class);
	@Autowired
	private BdsApiService bdsApiService;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	/**
	 * 反欺诈信息匹配
	 * 
	 * @author dmz
	 * @date 2017年4月13日
	 * @return
	 */
	@RequestMapping("/matchAntiFraudInfo/{loanNo}")
	@ResponseBody
	public Result<MatchAntiFraudInfoResVO> matchAntiFraudInfo(@PathVariable String loanNo) {
		Result<MatchAntiFraudInfoResVO> result = new Result<MatchAntiFraudInfoResVO>(Type.FAILURE);
		MatchAntiFraudInfoReqVO request = new MatchAntiFraudInfoReqVO();
		request.setLoanNum(loanNo);
		try {
			result = bdsApiService.matchAntiFraudInfo(request);
		} catch (Exception e) {
			result.addMessage("系统异常");
			LOGGER.info("反欺诈信息匹配 异常:{}", e);
		}
		return result;
	}

	/**
	 * 匹配速贷灰名单
	 * 
	 * @author dmz
	 * @date 2017年9月5日
	 * @param loanNo
	 * @param session
	 * @return
	 */
	@RequestMapping("/fastLoanBlacklist/{loanNo}")
	@ResponseBody
	public Result<Boolean> fastLoanBlacklist(@PathVariable String loanNo, HttpSession session) {
		Result<Boolean> result = new Result<Boolean>(Type.FAILURE);
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		result.setData(bdsApiService.fastLoanBlacklist(applyBasiceInfo, "速贷"));
		result.setType(Type.SUCCESS);
		return result;
	}

}
