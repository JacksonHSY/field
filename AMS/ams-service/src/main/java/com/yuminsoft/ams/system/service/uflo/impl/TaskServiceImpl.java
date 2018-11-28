package com.yuminsoft.ams.system.service.uflo.impl;

import com.alibaba.fastjson.JSONObject;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.service.TaskService;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.approve.AgenLeaderMapper;
import com.yuminsoft.ams.system.domain.approve.AgenLeader;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程节点服务接口实现类
 * 
 * @author fuhongxing
 *
 */
@Service
public class TaskServiceImpl implements com.yuminsoft.ams.system.service.uflo.TaskService {

	@Resource(name = TaskService.BEAN_ID)
	private TaskService taskService;
	@Resource(name = ProcessService.BEAN_ID)
	private ProcessService processService;
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
	@Autowired
	private AgenLeaderMapper agenLeaderMapper;
	@Autowired
	private PmsApiService pmsApiService;
	@Value("${sys.code}")
	private String sysCode;

	/**
	 * 查询待办
	 */
	@Override
	public List<Task> findNotDoTasks(Map<String, Object> variables) {
		LOGGER.info("======查询待办任务=======");
		String loginUser = (String) variables.get("loginUser");
		// 查询创建的任务
		TaskQuery query = taskService.createTaskQuery();
		query.addAssignee(loginUser);
		List<Task> tasks = query.list();
		return tasks;
	}

	/**
	 * 提交任务 并指定下一节点
	 *
	 */
	@Override
	public void completeTask(long taskId, String path) {
		// 获得任务bean
		taskService.start(taskId);
		taskService.complete(taskId, path); // 完成任务
	}

	/**
	 * 提交任务
	 */
	@Override
	public void completeTask(long taskId) {
		taskService.start(taskId);
		taskService.complete(taskId);
	}

	/**
	 * 根据ID查找任务
	 */
	@Override
	public Task findTaskById(long taskId) {
		Task task = taskService.getTask(taskId);
		return task;
	}

	/**
	 * 开启一个审批流程
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 * @param loanNo	借款编号
	 * @param promoter 	借款Id
	 * @param approvalPerson 审批人编号
	 * @return
	 */
	@Override
	public String startProcess(String loanNo, String promoter, String approvalPerson) {
		LOGGER.info("======开启审批流程======");
		// 1、开启流程
		StartProcessInfo info = new StartProcessInfo(promoter);// 设置流程开启人(申请人)
		info.setBusinessId(loanNo);// 对应loanNo
		// 2、设置初审员
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("firstApprove", approvalPerson);
		info.setVariables(variables);
		// 启动流程实例
		Map<String, Object> map = new HashMap<String, Object>();
		processService.startProcessByName("approve", info);
		map.put("status", "审批流程开启");

		return JSONObject.toJSONString(map);
	}

	@Override
	public String rollback(Long taskId) {
		LOGGER.info("======审批回退操作{}======", taskId);
		// 回退上一节点
		// taskService.withdraw(taskId);
		// 回退到指定流程节点
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.rollback(taskId, "初审");
		map.put("status", "审批回退");
		return JSONObject.toJSONString(map);
	}

	@Override
	public String suspend(Long taskId) {
		LOGGER.info("======审批挂起操作{}======", taskId);
		// 挂起
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.suspend(taskId);
		map.put("status", "审批挂起");
		return JSONObject.toJSONString(map);
	}

	@Override
	public String resume(Long taskId) {
		LOGGER.info("======审批挂起恢复正常操作{}======", taskId);
		// 将挂起任务恢复正常
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.resume(taskId);
		map.put("status", "挂起恢复");
		return JSONObject.toJSONString(map);
	}

	@Override
	public String deny(Long taskId) {
		LOGGER.info("======审批拒绝操作{}======", taskId);
		// 审批拒绝
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.forward(taskId, "结束");
		map.put("status", "审批拒绝");
		return JSONObject.toJSONString(map);
	}

	@Override
	public String changeTask(Long taskId, String approvalPerson) {
		// 改派
		// Created, Ready, Reserved, InProgress, Completed, Suspended, Canceled, Forwarded, Rollback, Withdraw;
		LOGGER.info("======审批改派操作{}======", taskId);
		LOGGER.info("======审批改派至{}======", approvalPerson);
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.changeTaskAssignee(taskId, approvalPerson);
		map.put("status", "审批改派");
		return JSONObject.toJSONString(map);
	}

	/**
	 * 终审提交工作流转
	 * 
	 * @author Shipf
	 * @param taskId
	 * @param applyLimit
	 * @return
	 */
	@Override
	public String submit(Long taskId, BigDecimal applyLimit) {

		LOGGER.info("======审批任务提交操作{}======", taskId);
		Task task = taskService.getTask(taskId);
		Map<String, Object> variables = new HashMap<String, Object>();
		// 判断当前审批节点
		if ("初审".equals(task.getNodeName())) {
			LOGGER.info("=====终审动态分配规则调用=======");
			variables.put("finalEmpId", "finalAutoDispatch");
		} else if ("终审".equals(task.getNodeName())) {
			LOGGER.info("======终审动态分配规则调用=====");
			// 审批金额(是否进行协审时使用)
			variables.put("money", applyLimit);
			// 写入终审员
			variables.put("finalEmpId", "finalAutoDispatch");
		} else if ("协审".equals(task.getNodeName())) {
			LOGGER.info("======协审动态分配规则调用=====");
			// 写入协审员
			variables.put("finalEmpId", ShiroUtils.getCurrentUser().getUsercode());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.start(taskId);
		taskService.complete(taskId, variables);
		map.put("status", "审批提交");
		return JSONObject.toJSONString(map);
	}

	/**
	 * 初审根据路径提交下一个节点并且指的操作人
	 * 
	 * @author dmz
	 * @date 2017年3月22日
	 * @param taskId
	 * @param path
	 * @return
	 */
	@Override
	public String submitPath(Long taskId, String path) {
		Task task = taskService.getTask(taskId);
		Map<String, Object> variables = new HashMap<String, Object>();
		if ("初审".equals(task.getNodeName())) {
			if ("to终审".equals(path)) {// 终审待分配
				variables.put("finalEmpId", "finalAutoDispatch");
			} else if ("to初审复核".equals(path)) {// 初审组长
				getNextHandler(task, variables);
			}
		} else if ("复核确认".equals(task.getNodeName())) {
			if ("to终审".equals(path)) {// 终审待分配
				variables.put("finalEmpId", "finalAutoDispatch");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		taskService.start(taskId);
		taskService.complete(taskId, path, variables);
		map.put("status", "审批提交");
		return JSONObject.toJSONString(map);
	}

	/**
	 * 查询复核确认人
	 * 
	 * @author dmz
	 * @date 2017年4月10日
	 * @param task
	 * @param variables
	 */
	private void getNextHandler(Task task, Map<String, Object> variables) {
		List<AgenLeader> agList = agenLeaderMapper.findByUserCode(ShiroUtils.getAccount());
		// 判断代理组长
		if (!CollectionUtils.isEmpty(agList)) {
			variables.put("checkApprove", agList.get(0).getUserCode());
		} else {
			ReqLevelVO reqVo = new ReqLevelVO();
			reqVo.setLoginUser(ShiroUtils.getAccount());
			reqVo.setInActive(AmsConstants.T);
			reqVo.setStatus(AmsConstants.ZERO);
			reqVo.setRoleCode(RoleEnum.CHECK_GROUP_LEADER.getCode());
			reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
			ResEmployeeVO resEmployeeVO = pmsApiService.getLeaderByCode(reqVo);
			if (null != resEmployeeVO) {
				variables.put("checkApprove", resEmployeeVO.getUsercode());
			} else {
				throw new BusinessException("复核确认查找上级领导失败");
			}
		}
	}
}
