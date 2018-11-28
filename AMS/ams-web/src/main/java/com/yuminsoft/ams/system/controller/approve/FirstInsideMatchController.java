package com.yuminsoft.ams.system.controller.approve;

import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.bds.biz.api.vo.response.ApplicationHistoryResVO;
import com.ymkj.bds.biz.api.vo.response.CustomerKeyInfoResVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pic.PicService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 内部匹配
 * 
 * @author luting
 * @date 2017年3月1日 下午5:33:09
 */
@Controller
@RequestMapping("/firstInsideMatch")
public class FirstInsideMatchController extends BaseController {
	@Autowired
	private BdsApiService bdsApiService;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;
	@Autowired
	private PicService picService;


	/**
	 * 查询对比案件附件集合
	 * @author lihuimeng
	 * @param loanNo1
	 * @param loanNo2
	 * @return
	 */
	@RequestMapping("/getAttachmentVoList/{loanNo1}/{loanNo2}")
	@ResponseBody
	public Result<String> getAttachmentVoList(@PathVariable String loanNo1, @PathVariable String loanNo2){
		LOGGER.info("内匹信息比对附件查询，操作人：[{}], 单号:[{}, {}] ==============》",ShiroUtils.getAccount(), loanNo1, loanNo2);
		return picService.getCompareAttachment(loanNo1, loanNo2);
	}

	/**
	 * 历史匹配
	 * 
	 * @author dmz
	 * @date 2017年7月5日
	 * @return
	 */
	@RequestMapping("/firstContrastHistory/{loanNo}")
	public String firstContrastHistory(@PathVariable String loanNo, Model model, HttpSession session) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		model.addAttribute("applyBasiceInfo",applyBasiceInfo);
		return "/approve/first/firstContrast";
	}

	/**
	 * 加载查看详情-查看还款详细信息数据
	 * 
	 * @author luting
	 * @date 2017年3月2日 上午11:41:52
	 */
/*	@RequestMapping("/firstInsideMatchDetailsInfo")
	@ResponseBody
	public ResponsePage<Map<String, Object>> firstInsideMatchDetailsInfo() {
		ResponsePage<Map<String, Object>> page = new ResponsePage<Map<String, Object>>();
		page.setTotal(1);
		return page;

	}*/

	/**
	 * 加载查看详情-查看账单信息数据
	 * 
	 * @author luting
	 * @date 2017年2月23日 上午10:30:41
	 */
	@RequestMapping("/firstTelephoneSummaryBillInfo")
	@ResponseBody
	public String firstTelephoneSummaryBillInfo() {
		return "firstTelephoneSummaryBillInfo";
	}

	/**
	 * 查看日志详情列表
	 * 
	 * @author fusj
	 * @date 2017年3月2日
	 */
	@RequestMapping("/insideMatchViewInfo")
	public String insideMatchViewInfo() {
		LOGGER.info("日志详情列表");
		return "/approve/first/firstInsideMatchLog";
	}

	/**
	 * add by zw at 2017-05-03 内部匹配申请历史信息查询
	 * 
	 * @date 2017年05月03日
	 * @return
	 */
	@RequestMapping("/matchApplicationHistory/{loanNo}")
	@ResponseBody
	public ResponsePage<ApplicationHistoryResVO> matchApplicationHistory(@PathVariable String loanNo, HttpSession session) {
		ResponsePage<ApplicationHistoryResVO> page = new ResponsePage<ApplicationHistoryResVO>();
		try {
			page = bdsApiService.matchApplicationHistory(session.getId(), loanNo);
		} catch (Exception e) {
			LOGGER.error("内部匹配申请历史信息查询异常", e);
		}
		return page;
	}

	// end at 2017-05-03
	/**
	 * add by zw at 2017-05-05 内部匹配 获取客户关键信息
	 * 
	 * @date 2017年05月03日
	 * @return
	 */
	@RequestMapping("/getCustomerKeyInformation/{loanNo}")
	@ResponseBody
	public Result<CustomerKeyInfoResVO> getCustomerKeyInformation(@PathVariable String loanNo, HttpSession session) {
		Result<CustomerKeyInfoResVO> result = new Result<CustomerKeyInfoResVO>(Result.Type.FAILURE);
		try {
			result = bdsApiService.getCustomerKeyInformation(session.getId(), loanNo);
		} catch (Exception e) {
			LOGGER.error("内部匹配 获取客户关键信息", e);
		}
		return result;
	}
	// end at 2017-05-03
}
