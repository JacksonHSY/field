package com.yuminsoft.ams.system.controller.approve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.finalApprove.StaffOrderTaskVO;

/**
 * 员工任务控制层
 * 
 * @author dmz
 * @date 2017年3月7日
 */
@Controller
@RequestMapping("/staffOrderTask")
public class StaffOrderTaskController {

	@Autowired
	private StaffOrderTaskService staffOrderTaskService;
	
	/**
	 * 返回初审接单信息(用于显示界面信息)
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 * @return
	 */
	@RequestMapping("/getIsAcceptOrder")
	@ResponseBody
	public Result<String> getIsAcceptOrder() {
		Result<String> result= new Result<String>(Type.FAILURE);
		Map<String,Object> map = new HashMap<String,Object>();
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		map.put("staffCode", currentUser.getUsercode());
		map.put("taskDefId",  EnumUtils.FirstOrFinalEnum.FIRST.getValue());
		StaffOrderTask sot = staffOrderTaskService.findOneService(map);
		if (null != sot) {
			result.setType(Type.SUCCESS);
			result.setData(sot.getIfAccept());
		}
		return result;
	}

	/**
	 * 设置初审员工是否接单(Y/N)
	 * 
	 * @author dmz
	 * @date 2017年3月7日
	 * @param ifAccept
	 * @return
	 */
	@RequestMapping("/isAcceptOrder")
	@ResponseBody
	@UserLogs(link = "是否接单", operation = "初审是否接单", type = com.yuminsoft.ams.system.domain.system.UserLog.Type.初审)
	public Result<String> isAcceptOrder(String ifAccept, String staffCode) {
		// 如果员工编号为空就设置当前用户
		if (null == staffCode || staffCode.isEmpty()) {
			ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
			staffCode = currentUser.getUsercode();
		}
		return staffOrderTaskService.isAcceptService(ifAccept, EnumUtils.FirstOrFinalEnum.FIRST.getValue(), staffCode);
	}
	
	/**
	 * 返回终审接单信息(用于显示界面信息)
	 * 
	 * @author Shipf
	 * @date
	 * @return
	 */
	@RequestMapping("/getFinalIsAcceptOrder")
	@ResponseBody
	public Result<String> getFinalIsAcceptOrder() {
		Result<String> result= new Result<String>(Type.FAILURE);
		Map<String,Object> map = new HashMap<String,Object>();
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		map.put("staffCode", currentUser.getUsercode());
		map.put("taskDefId",  EnumUtils.FirstOrFinalEnum.FINAL.getValue());
		StaffOrderTask sot = staffOrderTaskService.findOneService(map);
		if (null != sot) {
			result.setType(Type.SUCCESS);
			result.setData(sot.getIfAccept());
		}
		return result;
	}

	/**
	 * 设置终审员工是否接单(Y/N)
	 * 
	 * @author Shipf
	 * @date
	 * @param ifAccept
	 * @return
	 */
	@RequestMapping("/isFinalAcceptOrder")
	@ResponseBody
	@UserLogs(link = "是否接单", operation = "终审是否接单", type = com.yuminsoft.ams.system.domain.system.UserLog.Type.终审)
	public Result<String> isFinalAcceptOrder(String ifAccept, String staffCode) {
		// 如果员工编号为空就设置当前用户
		if (null == staffCode || staffCode.isEmpty()) {
			ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
			staffCode = currentUser.getUsercode();
		}

		return staffOrderTaskService.isAcceptService(ifAccept, EnumUtils.FirstOrFinalEnum.FINAL.getValue(), staffCode);
	}
	
	/**
	 * 终审同步员工
	 * 
	 * @author Shipf
	 * @date
	 * @param
	 * @return
	 */
	@RequestMapping("/synchronizeEmp")
	@ResponseBody
	public Result<String> synchronizeEmp() {
		Result<String> result= new Result<String>(Type.SUCCESS);
		// 同步员工
		staffOrderTaskService.synchronizeEmpInfoNew();
		return result;
	}
	
	/**
	 * 终审改派时查询时，获取终审人员列表
	 * 
	 * @author Shipf
	 * @date
	 * @param ifAccept
	 * @return
	 */
	@RequestMapping("/getFinalAcceptOrder")
	@ResponseBody
	public List<StaffOrderTaskVO> getFinalAcceptOrder() {
	    return staffOrderTaskService.getFinalAcceptOrder();
	}
	
}
