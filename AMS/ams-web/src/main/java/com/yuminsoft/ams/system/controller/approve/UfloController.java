package com.yuminsoft.ams.system.controller.approve;

import com.yuminsoft.ams.system.service.uflo.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
/**
 * 工作流操作
 * @author fuhongxing
 */
@Controller
public class UfloController {
	
	@Autowired
	private TaskService taskService;
	
	/**
	 * 开启一个审批流程
	 * @return
	 */
	@RequestMapping("/startProcess")
	@ResponseBody
	public String startProcess(String loanNo, String currentUser, String approvalPerson) {
		// todo 注意 promoter 是流程发起人，应该是员工工号
		return taskService.startProcess(loanNo, currentUser, approvalPerson);
	}
	
	/**
	 * 审批回退
	 * @return
	 */
	@RequestMapping("/rollback")
	@ResponseBody
	public String rollback(Long taskId) {
		return taskService.rollback(taskId);
	}
	
	/**
	 * 审批任务挂起
	 * @return
	 */
	@RequestMapping("/suspend")
	@ResponseBody
	public String suspend(Long taskId) {
		return taskService.suspend(taskId);
	}
	
	/**
	 * 让处于挂起状态的任务恢复正常
	 * @return
	 */
	@RequestMapping("/resume")
	@ResponseBody
	public String resume(Long taskId) {
		return taskService.resume(taskId);
	}
	
	/**
	 * 审批拒绝
	 * @return
	 */
	@RequestMapping("/deny")
	@ResponseBody
	public String deny(Long taskId) {
		return taskService.deny(taskId);
	}
	
	/**
	 * 审批改派
	 * @return
	 */
	@RequestMapping("/changeTask")
	@ResponseBody
	public String changeTask(Long taskId, String approvalPerson) {
		return taskService.changeTask(taskId, approvalPerson);
	}
	
	/**
	 * 审批提交
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public String sumbit(Long taskId, BigDecimal applyLimit) {
		return taskService.submit(taskId, applyLimit);
	}
}
