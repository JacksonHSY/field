package com.yuminsoft.ams.system.service.uflo;

import com.bstek.uflo.model.task.Task;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 流程节点服务接口
 * 
 * @author fuhongxing
 *
 */
public interface TaskService {
	/**
	 * 查询待办
	 * 
	 * @param variables
	 * @throws ServletException
	 * @throws IOException
	 */
	public List<Task> findNotDoTasks(Map<String, Object> variables);

	/**
	 * 提交任务
	 * 
	 * @param taskId
	 * @throws ServletException
	 * @throws IOException
	 */
	public void completeTask(long taskId);

	/**
	 * 提交任务 并指定下节点
	 * 
	 * @param taskId
	 * @param path
	 * @throws ServletException
	 * @throws IOException
	 */
	public void completeTask(long taskId, String path);

	/**
	 * 根据task得到TaskId
	 * @param taskId
	 * @throws ServletException
	 * @throws IOException
	 */
	public Task findTaskById(long taskId);

	/**
	 * 开启一个审批流程
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 * @param loanNo-借款编号
	 * @param applyId-借款Id
	 * @param approvalPerson-审批人编号
	 * @return
	 */
	public String startProcess(String loanNo, String applyId, String approvalPerson);

	/**
	 * 审批回退
	 * 
	 * @return
	 */
	public String rollback(Long taskId);

	/**
	 * 审批任务挂起
	 * 
	 * @return
	 */
	public String suspend(Long taskId);

	/**
	 * 让处于挂起状态的任务恢复正常
	 * 
	 * @return
	 */
	public String resume(Long taskId);

	/**
	 * 审批拒绝
	 * 
	 * @return
	 */
	public String deny(Long taskId);

	/**
	 * 审批改派
	 * 
	 * @return
	 */
	public String changeTask(Long taskId, String approvalPerson);

	/**
	 * 审批提交
	 * 
	 * @return
	 */
	public String submit(Long taskId, BigDecimal applyLimit);

	/**
	 * 初审根据路径提交下一个节点并且指的操作人
	 * 
	 * @author dmz
	 * @date 2017年3月22日
	 * @param taskId
	 * @param path
	 * @return
	 */
	 String submitPath(Long taskId, String path);
}
