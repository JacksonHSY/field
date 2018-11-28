package com.yuminsoft.ams.system.controller.approve;

import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 审核审批公共
 * 
 * @author dmz
 * @date 2017年7月5日
 */
@Controller
@RequestMapping("/applyApprove")
public class ApplyApproveController extends BaseController{

	/**
	 * 影响资料对比
	 * @param firstLoanNo
	 * @param secondLoanNo
	 * @param model
	 * @return
	 */
	@RequestMapping("/imageMatchIframe/{firstLoanNo}/{secondLoanNo}")
	public String imageMatchIframe(@PathVariable String firstLoanNo, @PathVariable String secondLoanNo, Model model) {
		StringBuffer src = new StringBuffer();
		src.append(picImageUrl).append("/api/filedata/compareTo?nodeKey=").append(picApproval).append("&sysName=")
				.append(sysCode).append("&appNo=").append(firstLoanNo).append("&appNo2=").append(secondLoanNo)
				.append("&title=本次申请&title2=历史申请&operator=").append(ShiroUtils.getCurrentUser().getName())
				.append("&jobNumber=").append(ShiroUtils.getCurrentUser().getUsercode());

		model.addAttribute("iframeSrc", src.toString());

		return "/approve/openIframeWindow";
	}
}
