package com.yuminsoft.ams.system.controller.approve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuminsoft.ams.system.controller.BaseController;

@Controller
@RequestMapping("/applayManagement")
public class ApplyManagementController extends BaseController {

	/**
	 * 返回客服管理页面
	 * 
	 * @author dongmingzhi
	 * @date 2017年1月5日
	 * @return
	 */
	@RequestMapping("/customerService")
	public String customerService() {
		return "/apply/customerService";
	}

	/**
	 * 返回综合查询页面
	 * 
	 * @author dongmingzhi
	 * @date 2017年1月6日
	 * @return
	 */
	@RequestMapping("integratedQuery")
	public String integratedQuery() {
		return "/apply/integratedQuery";
	}

	/**
	 * 返回任务数查询页面
	 * 
	 * @author dongmingzhi
	 * @date 2017年1月6日
	 * @return
	 */
	@RequestMapping("/taskCountQuery")
	public String taskCountQuery() {
		return "/apply/taskCountQuery";
	}

	/**
	 * 返回申请件信息维护
	 * 
	 * @author luting
	 * @date 2017年1月6日 上午10:03:19
	 */
	@RequestMapping("/applyInfoManage")
	public String applyInfoManage() {
		return "/apply/applyInfoManage";
	}

	/**
	 * 返回初审/终审已完成件统计 页面
	 * 
	 * @author luting
	 * @date 2017年1月6日 上午10:03:19
	 */
	@RequestMapping("/auditStatistics")
	public String auditStatistics() {
		return "/apply/auditStatistics";
	}

	/**
	 * 返回规则配置页面
	 * 
	 * @author luting
	 * @date 2017年3月6日 下午2:49:04
	 */
	@RequestMapping("/ruleConfiguration")
	public String ruleConfiguration() {
		return "/apply/ruleConfiguration";
	}

	/**
	 * 返回复核确认页面
	 * 
	 * @author luting
	 * @date 2017年3月9日 上午10:40:33
	 */
	@RequestMapping("/reviewConfirmation")
	public String reviewConfirmation() {
		return "/apply/reviewConfirmation";
	}
}
