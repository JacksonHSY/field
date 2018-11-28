package com.yuminsoft.ams.system.service.approve.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.request.ReqMessageVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpRoleOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.StaffOrderSetMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderTaskMapper;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.websocket.MessageServer;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.finalApprove.StaffOrderTaskVO;

/**
 * @author dmz
 * @date 2017年2月17日
 */
@Service
public class StaffOrderTaskServiceImpl extends BaseService implements StaffOrderTaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffOrderTaskServiceImpl.class);

	@Autowired
	private StaffOrderTaskMapper staffOrderTaskMapper;
	
	@Autowired
	private StaffOrderSetMapper staffOrderSetMapper;
	
	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;

	@Autowired
	private MessageServer messageServer;

	@Autowired
	private PmsApiService pmsApiService;

	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;

	/**
	 * 查询所有对象
	 * 
	 * @author dmz
	 * @date 2017年5月17日
	 * @param map
	 * @return
	 */
	@Override
	public List<StaffOrderTask> findAllService(Map<String, Object> map) {
		return staffOrderTaskMapper.findAll(map);
	}

	/**
	 * 更新
	 * 
	 * @param staffOrderTask
	 * @return
	 */
	@Override
	public boolean update(StaffOrderTask staffOrderTask) {
		boolean action = false;
		int update = staffOrderTaskMapper.update(staffOrderTask);
		if (1 == update) {
			action = true;
		} else {
			LOGGER.info("修改员工队列信息失败 count=:{}", update);
		}
		return action;
	}

	/**
	 * 添加员工接单任务表
	 * 
	 * @author dmz
	 * @date 2017年5月17日
	 * @param staffOrderTask
	 * @return
	 */
	@Override
	public boolean save(StaffOrderTask staffOrderTask) {
		boolean action = false;
		int count = staffOrderTaskMapper.insert(staffOrderTask);
		if (1 == count) {
			action = true;
		} else {
			LOGGER.info("添加员工接单任务失败  count=:{}", count);
		}
		return action;
	}

	@Override
	public StaffOrderTask findOneService(Map<String, Object> map) {
		return staffOrderTaskMapper.findOne(map);
	}

	@Override
	public Result<String> delete(Long id) {
		Result<String> result = new Result<String>(Result.Type.SUCCESS);
		int count;
		try {
			count = staffOrderTaskMapper.deleteById(id);
			if (count > 1) {
				return result;
			} else {
				result.addMessage("删除员工接单能力失败！");
				result.setType(Result.Type.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.error("删除接单能力异常！", e);
			result.addMessage("删除接单能力异常！");
			result.setType(Result.Type.FAILURE);
		}
		return result;
	}

    /**
     * 修改初审员队列数
     * 注意：只有正常队列数减少才会修改等待时间
     * @param staffCode 员工编号
     * @param rtfNode   审批环节(初审or终审)
     * @param calType	计算类型(加1 or 减1)
     * @param staffTaskType 队列类型
     */
	@Override
	public synchronized void updateStaffTaskNum(String staffCode, EnumUtils.FirstOrFinalEnum rtfNode, EnumUtils.CalType calType, EnumUtils.StaffTaskType staffTaskType) {
		LOGGER.info("更新员工队列数， staffCode:{}, rtfNode:{}, calType:{}, staffTaskType:{}", staffCode, rtfNode.getValue(), calType.name(), staffTaskType.getCode());

		Map<String, Object> params = Maps.newHashMap();
		params.put("staffCode", staffCode);
		params.put("taskDefId", rtfNode.getValue());
		List<StaffOrderTask> staffOrderTaskList = staffOrderTaskMapper.findAll(params);

		if(staffOrderTaskList.size() > 1){
			throw new BusinessException("员工：" + staffCode + ", 环节：" + rtfNode.getValue() +"，队列数多于一条");
		}

		StaffOrderTask staffOrderTask = staffOrderTaskList.get(0);
		LOGGER.info("员工{}当前队列详情{}", staffCode, JSONObject.toJSONString(staffOrderTask));

		if(calType.equals(EnumUtils.CalType.ADD)){
			if(staffTaskType.equals( EnumUtils.StaffTaskType.ACTIVITY)){
				staffOrderTask.setWaitTime(new Date());
				staffOrderTask.setCurrActivieTaskNum(staffOrderTask.getCurrActivieTaskNum() + 1);
			}else if(staffTaskType.equals(EnumUtils.StaffTaskType.INACTIVITY)){
				staffOrderTask.setCurrInactiveTaskNum(staffOrderTask.getCurrInactiveTaskNum() + 1);
			}else if(staffTaskType.equals(EnumUtils.StaffTaskType.PRIORITY)){
				staffOrderTask.setCurrPriorityNum(staffOrderTask.getCurrPriorityNum() + 1);
			}

		} else if(calType.equals(EnumUtils.CalType.MINUS)){
			if(staffTaskType.equals( EnumUtils.StaffTaskType.ACTIVITY)){
				staffOrderTask.setWaitTime(new Date());
				staffOrderTask.setCurrActivieTaskNum(staffOrderTask.getCurrActivieTaskNum() - 1);
			}else if(staffTaskType.equals(EnumUtils.StaffTaskType.INACTIVITY)){
				staffOrderTask.setCurrInactiveTaskNum(staffOrderTask.getCurrInactiveTaskNum() - 1);
			}else if(staffTaskType.equals(EnumUtils.StaffTaskType.PRIORITY)){
				staffOrderTask.setCurrPriorityNum(staffOrderTask.getCurrPriorityNum() - 1);
			}
		}

		if(staffOrderTask.getCurrActivieTaskNum() < 0 || staffOrderTask.getCurrInactiveTaskNum() < 0 || staffOrderTask.getCurrPriorityNum() < 0){
			LOGGER.error("员工：" + staffCode + ", 环节：" + rtfNode.getValue() +"，队列类型:"+ staffTaskType.getValue() + "，操作类型：" + calType.name() +"，队列数更新后小于0");
			throw new BusinessException("申请件修改失败");
		}

		staffOrderTask.setLastModifiedDate(new Date());

		int updateCount = staffOrderTaskMapper.updateTaskNum(staffOrderTask);
		if (1 != updateCount) {
			LOGGER.error("修改队列数错误 修改条数:{}",updateCount);
			throw new BusinessException("申请件修改失败");
		}
	}

	/**
	 * 查询原初审是否有能力接单(用于优先级分派给原初审)
	 * @param map
	 * @return
	 */
	@Override
	public StaffOrderTask getOriginalApprover(Map<String, Object> map) {
		return staffOrderTaskMapper.findOriginalApprover(map);
	}

	/**
	 * 查询初审待派单员工信息(优先队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月21日
	 * @param map
	 * @return
	 */
	@Override
	public StaffOrderTask findPriorityTaskOrderService(Map<String, Object> map) {
		return staffOrderTaskMapper.findPriorityTaskOrder(map);
	}

	/**
	 *  初审复议派单查询原初审是否可以接单且未超过正常队列上线
	 * @param map
	 * @return
	 */
	@Override
	public StaffOrderTask getReconsiderActiviesTask(Map<String, Object> map) {
		return staffOrderTaskMapper.findReconsiderActiviesTask(map);
	}

	/**
	 * 查询初审待派单员工信息(正常队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月20日
	 * @param map
	 * @return
	 */
	@Override
	public StaffOrderTask findActiviesTaskOrderService(Map<String, Object> map) {
		return staffOrderTaskMapper.findActiviesTaskOrder(map);
	}

	/**
	 * 判断初审挂起队列是否达到上限
	 * 
	 * @author dmz
	 * @date 2017年5月18日
	 * @param staffCode
	 * @return
	 */
	@Override
	public Result<String> findAurrInactiveTaskNumService(String staffCode) {
		Result<String> reuslt = new Result<String>(Type.FAILURE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staffCode", staffCode);
		StaffOrderTask sot = staffOrderTaskMapper.findAurrInactiveTaskNum(map);
		if (null != sot) {
			if (EnumUtils.DisplayEnum.DISABLE.getValue().equals(sot.getStatus())) {
				reuslt.addMessage("您的用户状态已被禁用,挂起失败！");
			} else {
				reuslt.setType(Type.SUCCESS);
			}
		} else {
			reuslt.addMessage("您的挂起队列已达上限,挂起失败！");
		}
		return reuslt;
	}

	/**
	 * 设置员工是否接单(Y/N)
	 * 
	 * @author dmz
	 * @date 2017年3月7日
	 * @param ifAccept-标记Y或者N枚举
	 * @param taskDefId-标记初审终审枚举
	 * @param staffCode-账户编号
	 * @return
	 */
	@Override
	public Result<String> isAcceptService(String ifAccept, String taskDefId, String staffCode) {
		Result<String> result = new Result<String>(Type.FAILURE);
		if (EnumUtils.YOrNEnum.Y.getValue().equals(ifAccept) || EnumUtils.YOrNEnum.N.getValue().equals(ifAccept)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("staffCode", staffCode);
			map.put("taskDefId", taskDefId);
			StaffOrderTask obj = staffOrderTaskMapper.findOne(map);
			if (null != obj) {
				if (!ifAccept.equals(obj.getIfAccept())) {
					StaffOrderTask newObj = new StaffOrderTask();
					newObj.setIfAccept(ifAccept);
					String message = ShiroUtils.getCurrentUser().getName()+"设置关闭接单成功";
					if (ifAccept.equals(EnumUtils.YOrNEnum.Y.getValue())) {
						newObj.setWaitTime(new Date());
						message = ShiroUtils.getCurrentUser().getName()+"设置开启接单成功";
					}
					newObj.setId(obj.getId());
					newObj.setVersion(obj.getVersion());
					if (1 == staffOrderTaskMapper.update(newObj)) {
						result.addMessage("操作成功");
						result.setType(Type.SUCCESS);
						// 发送站内消息
						ReqMessageVO vo = new ReqMessageVO();
						vo.setSysCode("ams");
						vo.setTitle("开启关闭接单设置");
						vo.setContent(message);
						/** 查找上级 */
						ReqLevelVO reqVo = new ReqLevelVO();
						reqVo.setSysCode(sysCode);
						reqVo.setLoginUser(ShiroUtils.getAccount());
						reqVo.setInActive(AmsConstants.T);
						reqVo.setStatus(AmsConstants.ZERO);
						reqVo.setCascade(true);
						// 判断是初审员还是终审员找上级
						if (EnumUtils.FirstOrFinalEnum.FIRST.getValue().equals(taskDefId)) {
							// 初审
							reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
						}else if(EnumUtils.FirstOrFinalEnum.SALE.getValue().equals(taskDefId)) {
							reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
						} else {
							// 终审
							reqVo.setLevelType(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
						}
						ResEmployeeVO emp = pmsApiService.getLeaderByCode(reqVo);
						if (emp != null) {
							vo.setReceiver(emp.getUsercode());// 发送人
							// 发送站内消息
							boolean flag = messageServer.sendMessages(vo);
							LOGGER.info("发送站内消息成功！！！！");
							if (flag) {
								// 发送消息数量
								Map<String, String> param = new HashMap<String, String>();
								param.put("account", ShiroUtils.getAccount());
								String sendMessage = HttpUtils.doPost(pmsUrl + "api/message/sendCount", param);
								LOGGER.info("通知{}未读站内消息数量，返回结果:{}", ShiroUtils.getAccount(), sendMessage);
								//messageServer.sendUnreadCountToEmployees(null);
							}
						}
					} else {
						result.addMessage("操作失败");
					}
				} else {
					result.addMessage("参数错误,操作失败");
				}
			} else {
				result.addMessage("未配置接单能力或接单上限");
			}
		} else {
			result.addMessage("参数错误,操作失败");
		}
		return result;
	}

	/*
	@Override
	@Deprecated
	public void synchronizeEmpInfo(ResEmployeeVO vo) {
		StaffOrderTask orderTask = null;
		StaffOrderTask staffOrderTask = null;
		List<String> roleList = null;
		String level = "";// 审批级别
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询是否已经存在该终审员信息
		map.put("staffCode", vo.getUsercode());
		map.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());
		staffOrderTask = staffOrderTaskMapper.findOne(map);
		level = "";
		// 判断当前是否存在,如果存在则更新
		if (staffOrderTask == null) {
			LOGGER.info("当前终审员信息信审系统不存在，新增加入员工接单能力表.{}", vo.getUsercode());
			orderTask = new StaffOrderTask();
			orderTask.setCreatedBy(vo.getName());
			orderTask.setLastModifiedBy(vo.getUsercode());
			orderTask.setOrgCode(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
			orderTask.setStaffCode(vo.getUsercode());// 员工编号
			orderTask.setTaskDefId(EnumUtils.FirstOrFinalEnum.FINAL.getValue());// 终审
			orderTask.setIfAccept("N");// 是否接单Y/N
			orderTask.setCurrActivieTaskNum(0);
			orderTask.setCurrPriorityNum(0);
			orderTask.setCurrInactiveTaskNum(0);
			orderTask.setVersion(0);
			orderTask.setWaitTime(new Date());
			// 判断员工是否正常
			orderTask.setStatus("t".equals(vo.getInActive()) ? "0" : "1");
			// 获取角色信息
			roleList = vo.getRoleCodes();
			if (!CollectionUtils.isEmpty(roleList)) {
				if (roleList.contains(AmsConstants.L1)) {
					level = AmsConstants.L1;
				} else if (roleList.contains(AmsConstants.L2)) {
					level = AmsConstants.L2;
				} else if (roleList.contains(AmsConstants.L3)) {
					level = AmsConstants.L3;
				} else if (roleList.contains(AmsConstants.L4)) {
					level = AmsConstants.L4;
				}
				orderTask.setFinalAuditLevel(level);
			}
			// 具有审批级别终审员才保存
			if (StringUtils.isNotEmpty(orderTask.getFinalAuditLevel())) {
				// 添加
				staffOrderTaskMapper.insert(orderTask);
			} else {
				LOGGER.warn("当前终审员未分配L1-L4的审批角色，不进行保存操作.{}", vo.getUsercode());
			}

		} else {
			boolean flag = false;
			// 判断是否在职
			String status = "0".equals(staffOrderTask.getStatus()) ? "t" : "f";
			if (!status.equals(vo.getInActive())) {
				LOGGER.info("===========终审员已离职，更新状态===========");
				staffOrderTask.setStatus("t".equals(vo.getInActive()) ? "0" : "1");
				flag = true;
			}
			roleList = vo.getRoleCodes();
			if (!CollectionUtils.isEmpty(roleList)) {
				if (roleList.contains(AmsConstants.L1)) {
					level = AmsConstants.L1;
				} else if (roleList.contains(AmsConstants.L2)) {
					level = AmsConstants.L2;
				} else if (roleList.contains(AmsConstants.L3)) {
					level = AmsConstants.L3;
				} else if (roleList.contains(AmsConstants.L4)) {
					level = AmsConstants.L4;
				}
			}
			// 判断审批级别是否发生变化
			if (StringUtils.isNotEmpty(level) && !level.equals(staffOrderTask.getFinalAuditLevel())) {
				staffOrderTask.setFinalAuditLevel(level);
				flag = true;
			}
			if (flag) {
				LOGGER.info("终审员信息发生变化，执行更新操作.{}", vo.getUsercode());
				staffOrderTask.setLastModifiedBy(vo.getUsercode());
				staffOrderTask.setLastModifiedDate(new Date());
				staffOrderTaskMapper.update(staffOrderTask);
			}
		}

	}


	@Override
	@Deprecated
	public void synchronizeEmpInfo(List<String> list, String type) {
		LOGGER.info("============当前本地存在，但是根据终审机构编码查询接口未获取到的终审员信息更新处理==============");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskDefId", type);
		map.put("userList", list);
		List<StaffOrderTask> userList = staffOrderTaskMapper.findByStaffCode(map);
		for (StaffOrderTask staffOrderTask : userList) {
			try {
				staffOrderTask.setStatus("1");
				staffOrderTaskMapper.update(staffOrderTask);
			} catch (Exception e) {
				LOGGER.error("设置终审员为禁用状态异常", e);
			}
		}

	}
	*/

	@Override
	public String checkInactiveTask() {
	    //1--查询系统设定的挂起队列数信息
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("staffCode", ShiroUtils.getCurrentUser().getUsercode());
        //map.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());
        StaffOrderSet sos = staffOrderSetMapper.findOne(map);
		
		if(null == sos){
		    return "0";
		}else{
		    //2--查询借款实时的队列数信息
		    int actual = 0;//实际挂起队列数
		    List<TaskNumber> list = new ArrayList<TaskNumber>();
		    TaskNumber task = new TaskNumber();
		    task.setTaskDefId(EnumUtils.FirstOrFinalEnum.FINAL.getValue());
		    task.setStaffCode(ShiroUtils.getCurrentUser().getUsercode());
		    list.add(task);
		    bmsLoanInfoServiceImpl.getQueenNum(list);
	        actual = list.get(0).getCurrInactiveTaskNum();
		    
		    return actual - sos.getHangQueueMax() >= 0 ? "1" : "2";
		}
	}

	@Override
	public List<StaffOrderTaskVO> getFinalAcceptOrder() {
		ReqParamVO req = new ReqParamVO();
		req.setSysCode(EnumConstants.AMS_SYSTEM_CODE);
        req.setUsercode(ShiroUtils.getCurrentUser().getUsercode());
        req.setRoleCode(RoleEnum.FINAL_CHECK.getCode());
        req.setStatus(0);
        req.setLoginUser(ShiroUtils.getCurrentUser().getUsercode());
		List<ResEmployeeVO> finalGroup = pmsApiService.getEmpsByAccount(req);// 当前登录用户的数据权限内终审员列表

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskDefId", "applyinfo-finalaudit");
		map.put("status", "0");
		List<StaffOrderTask> staffList = staffOrderTaskMapper.findAll(map);

		List<StaffOrderTaskVO> returnList = new ArrayList<StaffOrderTaskVO>();
		if(CollectionUtils.isNotEmpty(finalGroup) && CollectionUtils.isNotEmpty(staffList)) {
			for (StaffOrderTask staff : staffList) {
				for (ResEmployeeVO emp : finalGroup) {
					if (emp.getUsercode().equals(staff.getStaffCode())) {
						StaffOrderTaskVO sot = BeanUtil.copyProperties(staff, StaffOrderTaskVO.class);
						sot.setName(emp.getName());
						returnList.add(sot);
					}
				}
			}
			return returnList;
		}else {
			return null;
		}
	}

	@Override
	public String findMaxNormalQueue() {
		String maxNormalQueue = sysParamDefineMapper.findMaxNormalQueue();
		if (maxNormalQueue == null) {
			LOGGER.info("============未配置初终审人员的正常队列上限最大值============");
		}
		return maxNormalQueue;
	}

	@Override
	public String findMaxHangQueue() {
		String maxHangQueue = sysParamDefineMapper.findMaxHangQueue();
		if (maxHangQueue == null) {
			LOGGER.info("============未配置初终审人员的挂起队列上限最大值============");
		}
		return maxHangQueue;
	}

	@Override
	public void synchronizeEmpInfoNew() {
		List<StaffOrderTask> needInsert = new ArrayList<StaffOrderTask>();//需要插入的员工列表
		List<StaffOrderTask> needUpdate = new ArrayList<StaffOrderTask>();//需要更新的员工列表
		List<StaffOrderTask> needDelete = new ArrayList<StaffOrderTask>();//需要删除的员工列表
		
		//查询平台所有员工信息
		List<ResEmpRoleOrgVO> emps = pmsApiService.getAllEmps();
		if(null == emps) {
			throw new BusinessException("同步员工时，平台系统发生错误，没有返回一个初审员或者终审员，不继续往下执行。");
		}
		
		//查询ams员工接单任务表
		List<StaffOrderTask> staffOrderTaskList = staffOrderTaskMapper.findAll(new HashMap<String, Object>());
		
		/*
		 * (工号+初审/终审)确定唯一
		 * 如果平台存在，ams存在，如果机构信息，终审级别，员工状态，任何一个发生变化，则更新
		 * 如果平台存在，ams不存在，直接插入
		 * 如果平台不存在，ams存在，直接删除
		 */
		Map<String, Object> amsSot = getamsSotUsers(staffOrderTaskList);
		Map<String, Object> pmsEmp = new HashMap<String, Object>();
		for (ResEmpRoleOrgVO emp : emps) {
			List<String> roles = emp.getRoleCodes();
			String empKey = "";
			
			//如果有初审角色
			if(roles.contains(RoleEnum.CHECK.getCode())) {
				empKey = emp.getUsercode()+ RoleEnum.CHECK.getCode();
				if(amsSot.containsKey(empKey)) {
					//如果平台存在,ams存在
					StaffOrderTask sot = (StaffOrderTask) amsSot.get(empKey);
					if(isNeedUpdate(emp, sot)) {
						//如果大组id，大组名称，小组id，小组名称，员工状态，员工名称，任何一个发生变化，更新
						getUpdateStaff(emp, sot);
						needUpdate.add(sot);
					}
				} else {
					//如果平台存在,ams不存在
					StaffOrderTask sot = new StaffOrderTask();
					sot.setTaskDefId(EnumUtils.FirstOrFinalEnum.FIRST.getValue());//初审
					getInsertStaff(emp, sot);
					needInsert.add(sot);
				}
				pmsEmp.put(empKey, emp);
			}
			
			//如果有终审角色
			if(roles.contains(RoleEnum.FINAL_CHECK.getCode())) {
				empKey = emp.getUsercode()+ RoleEnum.FINAL_CHECK.getCode();
				String level = getFinalLevel(roles);
				if(amsSot.containsKey(empKey)) {
					//如果平台存在,ams存在
					StaffOrderTask sot = (StaffOrderTask) amsSot.get(empKey);
					//flag来判断终审级别是否发生变化
					boolean flag = isDiff(sot.getFinalAuditLevel(), level);
					if(isNeedUpdate(emp, sot) || flag) {
						//如果大组id，大组名称，小组id，小组名称，员工状态，员工名称，终审级别，任何一个发生变化，更新
						getUpdateStaff(emp, sot);
						sot.setFinalAuditLevel(level);
						needUpdate.add(sot);
					}
				} else {
					//如果平台存在,ams不存在
					if(StringUtils.isNotEmpty(level)) {
						//没有终审级别的不插入
						StaffOrderTask sot = new StaffOrderTask();
						sot.setTaskDefId(EnumUtils.FirstOrFinalEnum.FINAL.getValue());// 终审
						getInsertStaff(emp, sot);
						sot.setFinalAuditLevel(level);
						needInsert.add(sot);
					}
				}
				pmsEmp.put(empKey, emp);
			}
		}
		
		//3 如果本地存在,平台不存在
		for (StaffOrderTask sot : staffOrderTaskList) {
			String sotKey = sot.getStaffCode()+(sot.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue()) ? RoleEnum.CHECK.getCode() : RoleEnum.FINAL_CHECK.getCode());
			if(!pmsEmp.containsKey(sotKey) && "0".equals(sot.getStatus())) {
				needDelete.add(sot);
			}
		}
		
		LOGGER.info("同步员工信息，有【" + needInsert.size() + "】条记录需要插入");
		if(CollectionUtils.isNotEmpty(needInsert)) {
			staffOrderTaskMapper.batchInsert(needInsert);
		}
		
		LOGGER.info("同步员工信息，有【" + needUpdate.size() + "】条记录需要更新");
		if(CollectionUtils.isNotEmpty(needUpdate)) {
			staffOrderTaskMapper.batchUpdate(needUpdate);
		}
		
		LOGGER.info("同步员工信息，有【" + needDelete.size() + "】条记录需要删除(禁用)");
		//实际上不删除，只是把员工状态改为不正常，不可用（1）
		if(CollectionUtils.isNotEmpty(needDelete)) {
			//staffOrderTaskMapper.batchDelete(needDelete);
			staffOrderTaskMapper.batchForbidden(needDelete);
		}
	}
	
	private boolean isNeedUpdate(ResEmpRoleOrgVO emp, StaffOrderTask sot) {
		return isDiff(sot.getOrgCode(), emp.getSmallGroupId()) 
				|| isDiff(sot.getParentOrgCode(), emp.getBigGroupId())
				|| isDiff(sot.getOrgName(), emp.getSmallGroupName())
				|| isDiff(sot.getParentOrgName(), emp.getBigGroupName())
				|| isDiff(sot.getStatus(), emp.getStatus())
				|| isDiff(sot.getStaffName(), emp.getName());
	}
	
	/**
	 * 比较传入的两个参数是否不相等
	 * 
	 * @author Jia CX
	 * @date 2018年1月3日 上午9:20:46
	 * @notes
	 * 
	 * @param sot String类型
	 * @param emp 可能是Long,Integer,String
	 * @return true 表明不相等
	 */
	private static <T> boolean  isDiff(String sot, T emp){
		if(null == sot && null != emp) {
			return true;
		} else if(null != sot && null == emp) {
			return true;
		} else if(null != sot && null != emp) {
			if(emp instanceof Long || emp instanceof Integer) {
				return !sot.equals(emp.toString());
			} else if(emp instanceof String) {
				return !sot.equals(emp);
			}
		}
		return false;
	}
	
	private void getInsertStaff(ResEmpRoleOrgVO emp, StaffOrderTask sot) {
		sot.setStaffCode(emp.getUsercode());// 员工编号
		sot.setStaffName(emp.getName());
		sot.setOrgCode(null == emp.getSmallGroupId() ? null : emp.getSmallGroupId().toString());
		sot.setOrgName(emp.getSmallGroupName());
		sot.setParentOrgCode(null == emp.getBigGroupId() ? null : emp.getBigGroupId().toString());
		sot.setParentOrgName(emp.getBigGroupName());
		sot.setIfAccept("N");// 是否接单Y/N,默认接单
		sot.setCurrActivieTaskNum(0);
		sot.setCurrPriorityNum(0);
		sot.setCurrInactiveTaskNum(0);
		sot.setVersion(0);
		sot.setWaitTime(new Date());
		sot.setStatus(null == emp.getStatus() ? null : emp.getStatus().toString());
	}
	
	private void getUpdateStaff(ResEmpRoleOrgVO emp, StaffOrderTask sot) {
		sot.setLastModifiedBy("anonymous");
		sot.setLastModifiedDate(new Date());
		sot.setStaffName(emp.getName());
		sot.setOrgCode(null == emp.getSmallGroupId() ? null : emp.getSmallGroupId().toString());
		sot.setOrgName(emp.getSmallGroupName());
		sot.setParentOrgCode(null == emp.getBigGroupId() ? null : emp.getBigGroupId().toString());
		sot.setParentOrgName(emp.getBigGroupName());
		sot.setStatus(null == emp.getStatus() ? null : emp.getStatus().toString());
	}
	
	/**
	 * 获取终审级别
	 * 
	 * @author Jia CX
	 * @date 2017年12月28日 上午9:18:48
	 * @notes
	 * 
	 * @param roles
	 * @return
	 */
	private static String getFinalLevel(List<String> roles) {
		List<String> list = new ArrayList<String>();
		for (String str : roles) {
			if(Strings.isEqualsEvenOnce(str, RoleEnum.FINAL_CHECK_L1.getCode(),RoleEnum.FINAL_CHECK_L2.getCode(),RoleEnum.FINAL_CHECK_L3.getCode(),RoleEnum.FINAL_CHECK_L4.getCode())) {
				list.add(str);
			}
		}
		if(CollectionUtils.isNotEmpty(list)) {
			CollectionUtils.sortSimpleName(list);
			return list.get(list.size()-1);
		}
		return null;
	}

	/**
	 * 返回ams员工接单能力表中员工+角色列表
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 上午11:19:10
	 * @notes
	 * 
	 * @param staffOrderTaskList
	 * @return
	 */
	private Map<String, Object> getamsSotUsers(List<StaffOrderTask> staffOrderTaskList) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(staffOrderTaskList)) {
			for (StaffOrderTask sot : staffOrderTaskList) {
				map.put(sot.getStaffCode()+ (sot.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue()) ? RoleEnum.CHECK.getCode() : RoleEnum.FINAL_CHECK.getCode()), sot);
			}
		}
		return map;
	}
}
