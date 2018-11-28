package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymkj.ams.api.service.approve.recheck.RecheckExecuter;
import com.ymkj.ams.api.service.approve.task.TaskNumberExecuter;
import com.ymkj.ams.api.vo.request.recheck.RecheckQueenNumberVo;
import com.ymkj.ams.api.vo.request.recheck.RecheckQueenParamIn;
import com.ymkj.ams.api.vo.request.task.PersonCodeAndRoleVo;
import com.ymkj.ams.api.vo.request.task.ReqBMSTaskNumberVo;
import com.ymkj.ams.api.vo.response.recheck.RecheckQueenParamOut;
import com.ymkj.ams.api.vo.response.task.ResBMSTaskNumberVo;
import com.ymkj.ams.api.vo.response.task.TaskNumberQueVo;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqMessageVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.ymkj.sso.client.ShiroUser;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.StaffOrderTaskMapper;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.approve.TaskNumberService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.MailService;
import com.yuminsoft.ams.system.service.websocket.MessageServer;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.mail.SyncTaskNumberVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author jiacx
 */
@Service
public class TaskNumberServiceImpl extends BaseService implements TaskNumberService{

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskNumberServiceImpl.class);

	@Autowired
	private StaffOrderTaskMapper staffOrderTaskMapper;

	@Autowired
	private PmsApiService pmsApiService;
	
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	@Autowired
    private MessageServer messageServer;

	@Autowired
	private MailService mailService;

	@Value("${ams.pms.url}")
	private String pmsUrl;
	
	@Autowired
	private RecheckExecuter recheckExecuter;
	
	@Autowired
	private TaskNumberExecuter taskNumberExecuter;

	/**
	 *  人员接单配置
	 *  状态修改
	 *  
	 */
	@Override
	public Result<String> updateTaskNumber(String ifAccept, String taskDefId, String staffCode) {
	    Result<String> result = new Result<String>(Type.FAILURE);
	    
	    //查找被修改人的接单能力
	    Map<String, Object> map = new HashMap<String, Object>();
        map.put("staffCode", staffCode);
        map.put("taskDefId", taskDefId.equals(RoleEnum.CHECK.getCode()) ? EnumUtils.FirstOrFinalEnum.FIRST.getValue() : EnumUtils.FirstOrFinalEnum.FINAL.getValue());
        StaffOrderTask sot = staffOrderTaskMapper.findOne(map);
        if(null == sot){
            LOGGER.info("员工【" + staffCode + "】未配置接单能力,设置是否接单失败");
        }else{
            sot.setIfAccept(ifAccept);
            sot.setWaitTime(new Date());
            sot.setLastModifiedBy(ShiroUtils.getCurrentUser().getUsercode());
            sot.setLastModifiedDate(new Date());
            if(1 == staffOrderTaskMapper.update(sot)){
                result.setType(Type.SUCCESS);
                
                //修改成功，发送站内信给被修改人
                sendInnerMsg(ifAccept, staffCode);
            }
        }
        
		return result;
	}

    /**
     * 发送站内消息
     * 
     * @param ifAccept
     * @param staffCode
     * @author JiaCX
     * @date 2017年10月12日 上午11:21:09
     */
    private void sendInnerMsg(String ifAccept, String staffCode){
        ReqMessageVO vo = new ReqMessageVO();
        vo.setSysCode("ams");
        vo.setTitle("开启关闭接单设置");
        String op = ifAccept.equals(EnumUtils.YOrNEnum.Y.getValue()) ? "开启" : "关闭";
        vo.setContent(ShiroUtils.getCurrentUser().getName() + op + "了您的接单");
        vo.setReceiver(staffCode);
        if(messageServer.sendMessages(vo)){
            //发送消息数量
            Map<String, String> param = new HashMap<String, String>();
            param.put("account", staffCode);
            String sendMessage = HttpUtils.doPost(pmsUrl + "api/message/sendCount", param);
            LOGGER.info("通知{}未读站内消息数量，返回结果:{}", staffCode, sendMessage);
        }
    }

	public String findBycodes(String userCode, String approveType) {
		LOGGER.info("返回 params:{} ", JSON.toJSONString(userCode));
		StaffOrderTask staffOrderTask  = staffOrderTaskMapper.findByCodes(userCode,approveType);
		if(staffOrderTask != null){
			return staffOrderTask.getIfAccept();
		}
		return null;
		
	}

	public static String buildParams(Map<String, Object> param, String charset) {
	    if (param != null && !param.isEmpty()) {
	        StringBuffer buffer = new StringBuffer();
	        for (Map.Entry<String, Object> entry : param.entrySet()) {
	            try {
	                buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(ObjectUtils.toString(entry.getValue()), charset)).append("&");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	        }
	        return buffer.deleteCharAt(buffer.length() - 1).toString();
	    } else {
	        return null;
	    }
	}

	/**
	 * 查询所有人员的接单能力
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年4月17日 上午11:06:55
	 */
	public List<StaffOrderTask> findAll(Map<String,Object> map) {
		return staffOrderTaskMapper.findAll(map);
	}

	/**
	 * 按条件查出人员接单能力列表
	 * 
	 * @param requestPage
	 * @param firstGroup
	 * @param finalGroup
	 * @param taskNumber
	 * @return
	 * @author JiaCX
	 * @date 2017年5月11日 上午10:24:52
	 */
	@Override
	public ResponsePage<TaskNumber> getOrderTaskList(RequestPage requestPage, List<ResEmpOrgVO> firstGroup,	List<ResEmpOrgVO> finalGroup, TaskNumber taskNumber) {
		List<TaskNumber> reslist = new ArrayList<TaskNumber>();
		Page<StaffOrderTask> taskList = new Page<StaffOrderTask>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("staffCode", taskNumber.getStaffCode());
		map.put("staffName", taskNumber.getStaffName());
		//处理排序参数
		if (StringUtils.isNotEmpty(requestPage.getSort())){
			StringBuilder sb = new StringBuilder("");
			String[] sort = requestPage.getSort().split(",");
			String[] order = requestPage.getOrder().split(",");
			for (int i = 0; i < sort.length; i++) {
				String s = "";
				if("taskDefId".equals(sort[i])) {
					s = "task_def_id";
				}else if("parentOrgName".equals(sort[i])) {
					s = "CONVERT(parent_org_name USING gbk)";
				}else if("orgName".equals(sort[i])) {
					s = "CONVERT(org_name USING gbk)";
				}
				sb.append(s + " " + order[i] + ",");
			}
			map.put("sort",sb.toString().substring(0, sb.toString().length()-1));
		}
		
		map.put("firstUserList", new ArrayList<String>());
		map.put("finalUserList", new ArrayList<String>());
		if(StringUtils.isNotEmpty(taskNumber.getTaskDefId()) && RoleEnum.CHECK.getCode().equals(taskNumber.getTaskDefId())){
		    //如果要查初审
			map.put("firstUserList", getUserList(firstGroup));
		}else if(StringUtils.isNotEmpty(taskNumber.getTaskDefId()) && RoleEnum.FINAL_CHECK.getCode().equals(taskNumber.getTaskDefId())){
		    //如果要查终审
			map.put("finalUserList", getUserList(finalGroup));
		}else{
		    ShiroUser re = ShiroUtils.getShiroUser();
		    List<ResRoleVO> roles = re.getRoles();
		    if(isManagerAboveButManager() || (hasFirstOrFinalApproval(roles, 1) && hasFirstOrFinalApproval(roles, 2))){
		        //如果是初审经理或者终审经理以上级别，或者既有初审组长，主管，经理角色，也有终审组长，主管，经理角色,查看所有的
                map.put("firstUserList", getUserList(firstGroup));
                map.put("finalUserList", getUserList(finalGroup));
		    }else{
		        if(hasFirstOrFinalApproval(roles,1)){
		            map.put("firstUserList", getUserList(firstGroup));
		        }else if(hasFirstOrFinalApproval(roles,2)){
		            map.put("finalUserList", getUserList(finalGroup));
		        }
		    }
		}
		
		ResponsePage<TaskNumber> page = new ResponsePage<TaskNumber>();
		
		if(CollectionUtils.isNotEmpty((List<?>) map.get("firstUserList")) || CollectionUtils.isNotEmpty((List<?>) map.get("finalUserList"))){
		    PageHelper.startPage(requestPage.getPage(),requestPage.getRows());
            taskList = staffOrderTaskMapper.findByStaffCodeListAndApprovalType(map);
			if(CollectionUtils.isNotEmpty(taskList.getResult())){
			    List<StaffOrderTask> list = taskList.getResult();
				for (StaffOrderTask task : list) {
					reslist.add(BeanUtil.copyProperties(task, TaskNumber.class));
				}
			}
			
			bmsLoanInfoService.getQueenNum(reslist);
			
			page.setRows(reslist);
			page.setTotal(taskList.getTotal());
		}
		return page;
	}

	/**
	 * 是否是终审经理或者初审经理以上级别（不包含初审经理或者终审经理）
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年6月23日 下午3:40:32
	 */
	public boolean isManagerAboveButManager() {
	    ReqParamVO reqParamVO = new ReqParamVO();
	    reqParamVO.setSysCode(sysCode);
	    reqParamVO.setLoginUser(ShiroUtils.getCurrentUser().getUsercode());
	    reqParamVO.setNotManager(true);
        return pmsApiService.isManagerAbove(reqParamVO);
    }

    /**
	 * 是否有终审或者初审中的经理或主管或者组长的角色
	 * 
	 * @param roles
	 * @param i 1:初审，2：终审
	 * @return
	 * @author JiaCX
	 * @date 2017年6月23日 下午3:03:18
	 */
	private boolean hasFirstOrFinalApproval(List<ResRoleVO> roles, int i) {
	    if(1 == i){
	        if(CollectionUtils.isNotEmpty(roles)){
	            for(ResRoleVO resRoleVO: roles)
	            {
	                if(Strings.isEqualsEvenOnce(resRoleVO.getCode(), RoleEnum.CHECK_GROUP_LEADER.getCode(),RoleEnum.CHECK_DIRECTOR.getCode(),RoleEnum.CHECK_MANAGER.getCode())){
	                    return true;
	                }
	            }
	        }
	    }else if(2 == i){
	        if(CollectionUtils.isNotEmpty(roles)){
	            for(ResRoleVO resRoleVO: roles)
	            {
	                if(Strings.isEqualsEvenOnce(resRoleVO.getCode(), RoleEnum.FINAL_CHECK_GROUP_LEADER.getCode(),RoleEnum.FINAL_CHECK_DIRECTOR.getCode(),RoleEnum.FINAL_CHECK_MANAGER.getCode())){
	                    return true;
	                }
	            }
	        }
	    }
	    
        return false;
    }

	private List<String> getUserList (List<ResEmpOrgVO> empList) {
		List<String> userList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(empList)){
			for (ResEmpOrgVO resEmpOrgVO : empList) {
				userList.add(resEmpOrgVO.getUsercode());
			}
		}
		return userList;
	}

	@Override
    public String getRoleTypeOfCurrentUser() {
StringBuilder res = new StringBuilder("");
		
		if (pmsApiService.hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.CHECK_GROUP_LEADER.getCode()})) {
			LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.CHECK_GROUP_LEADER.getCode());
			res.append(",checkGroupLeader");// 如果数据权限内有初审组长角色
		}
	    if(pmsApiService.hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.CHECK.getCode()})) {
	    	LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.CHECK.getCode());
	    	res.append(",check");//如果数据权限内有初审角色
	    }
	    if(pmsApiService.hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.FINAL_CHECK.getCode()})) {
	    	LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.FINAL_CHECK.getCode());
	    	res.append(",finalCheck");//如果数据权限内有终审角色
	    }
	    
	    return res.toString();
    }

	@Override
	public void syncTaskNumber() {
		// 查询所有task表员工
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", AmsConstants.ZERO);
		List<StaffOrderTask> staffOrderTasks = staffOrderTaskMapper.findAll(params);

		List<SyncTaskNumberVo> syncTaskNumberVos = new ArrayList<SyncTaskNumberVo>();

		// 调用BMS接口，查询当前队列数
		for (StaffOrderTask staffOrderTask : staffOrderTasks) {
			TaskNumberQueVo taskNumberQueVo = bmsLoanInfoService.getTaskNumberByStaffCodeAndTaskDef(staffOrderTask.getStaffCode(), staffOrderTask.getTaskDefId());

			SyncTaskNumberVo item = new SyncTaskNumberVo();
			item.setStaffCode(staffOrderTask.getStaffCode());
			ResEmployeeVO employeeVO = pmsApiService.findEmpByUserCode(item.getStaffCode());
			item.setName(employeeVO.getName());
			if(EnumUtils.FirstOrFinalEnum.FIRST.getValue().equals(staffOrderTask.getTaskDefId())){
				item.setTaskDef("初审");
			}else{
				item.setTaskDef("终审");
			}
			item.setActiviyNum(staffOrderTask.getCurrActivieTaskNum());
			item.setActiviyCorrNum(taskNumberQueVo.getNormalQueue());
			item.setActiviyFlag(false);
			item.setInactivyNum(staffOrderTask.getCurrInactiveTaskNum());
			item.setInactivyCorrNum(taskNumberQueVo.getHangQueue());
			item.setInactiviyFlag(false);
			item.setPriorityNum(staffOrderTask.getCurrPriorityNum());
			item.setPriorityCorrNum(taskNumberQueVo.getPriorityQueue());
			item.setPriorityFlag(false);

			// 如果正常队列数不相等，则更新正常队列数
			if(taskNumberQueVo.getNormalQueue() != null && !staffOrderTask.getCurrActivieTaskNum().equals(taskNumberQueVo.getNormalQueue())){
				item.setActiviyNum(staffOrderTask.getCurrActivieTaskNum());
				item.setActiviyCorrNum(taskNumberQueVo.getNormalQueue());
				item.setActiviyFlag(true);

				staffOrderTask.setCurrActivieTaskNum(taskNumberQueVo.getNormalQueue());
				staffOrderTask.setLastModifiedDate(new Date());
				staffOrderTask.setLastModifiedBy("anonymous");
			}

			// 如果挂起队列数不相等，则更新挂起队列数
			if(taskNumberQueVo.getHangQueue() != null && !staffOrderTask.getCurrInactiveTaskNum().equals(taskNumberQueVo.getHangQueue())){
				item.setInactivyNum(staffOrderTask.getCurrInactiveTaskNum());
				item.setInactivyCorrNum(taskNumberQueVo.getHangQueue());
				item.setInactiviyFlag(true);

				staffOrderTask.setCurrInactiveTaskNum(taskNumberQueVo.getHangQueue());
				staffOrderTask.setLastModifiedDate(new Date());
				staffOrderTask.setLastModifiedBy("anonymous");
			}

			// 如果优先队列数不相等，则更新优先队列数
			if(taskNumberQueVo.getPriorityQueue() != null && !staffOrderTask.getCurrPriorityNum().equals(taskNumberQueVo.getPriorityQueue())){
				item.setPriorityNum(staffOrderTask.getCurrPriorityNum());
				item.setPriorityCorrNum(taskNumberQueVo.getPriorityQueue());
				item.setPriorityFlag(true);

				staffOrderTask.setCurrPriorityNum(taskNumberQueVo.getPriorityQueue());
				staffOrderTask.setLastModifiedDate(new Date());
				staffOrderTask.setLastModifiedBy("anonymous");
			}

			if(item.getInactiviyFlag() || item.getActiviyFlag() || item.getPriorityFlag()){
				// 更新员工队列数
				staffOrderTaskMapper.update(staffOrderTask);
				syncTaskNumberVos.add(item);
			}
		}

		// 发送员工队列数同步提醒邮件
		if(!CollectionUtils.isEmpty(syncTaskNumberVos)){
			mailService.sendSyncTaskNumMail(syncTaskNumberVos);
		}
	}

	@Override
	public ResponsePage<TaskNumber> getTaskList(RequestPage requestPage, TaskNumber tasknum) {
		/*
		 * 1.从pms获取当前登录人数据权限内，初审人员，终审人员，初审组长列表
		 */
		PageResponse<ResEmpOrgVO> res = pmsApiService.getEmployeesAndGroupLeaders(requestPage, tasknum);
		if(res.getTotalCount() == 0) {
			return new ResponsePage<TaskNumber>();
		}
		
		/*
		 *2.从api查找初审，终审，初审组长各自对应的任务数
		 */
		List<RecheckQueenNumberVo> leaders = new ArrayList<RecheckQueenNumberVo>();
		List<PersonCodeAndRoleVo> users = new ArrayList<PersonCodeAndRoleVo>();
		for (ResEmpOrgVO vo : res.getRecords()) {
			if(RoleEnum.CHECK_GROUP_LEADER.getCode().equals(vo.getRoleCode())) {
				RecheckQueenNumberVo leader = new RecheckQueenNumberVo();
				leader.setUserCode(vo.getUsercode());
				leaders.add(leader);
			} else {
				PersonCodeAndRoleVo user = new PersonCodeAndRoleVo();
				user.setPersonCode(vo.getUsercode());
				user.setPersonRole(RoleEnum.CHECK.getCode().equals(vo.getRoleCode()) ? "1" : "2");
				users.add(user);
			}
		}
		
		//查询组长复核队列数
		Response<RecheckQueenParamOut> leaderRes = null;
		if(CollectionUtils.isNotEmpty(leaders)) {
			RecheckQueenParamIn param = new RecheckQueenParamIn();
			param.setList(leaders);
			leaderRes = recheckExecuter.getRecheckQueen(param);
		}
		
		//查询审核人员队列数
		ResBMSTaskNumberVo userRes = null;
		if(CollectionUtils.isNotEmpty(users)) {
			ReqBMSTaskNumberVo param = new ReqBMSTaskNumberVo();
			param.setPersonCoAndRos(users);
			userRes = taskNumberExecuter.getQueueAmount(param);
		}
		
		//查询ams中task表所有员工信息
		List<StaffOrderTask> all = staffOrderTaskMapper.findAll();
		
		/*
		 * 3.把队列数和员工是否接单组装返回页面
		 */
		List<TaskNumber> returnList = new ArrayList<TaskNumber>();
		for (ResEmpOrgVO vo : res.getRecords()) {
			TaskNumber t = new TaskNumber();
			t.setStaffCode(vo.getUsercode());
			t.setStaffName(vo.getName());
			t.setOrgName(vo.getOrgName());
			t.setParentOrgName(vo.getOrgPname());
			
			//如果是初审组长(只用放需要复核确认的队列数到正常队列字段)
			if(RoleEnum.CHECK_GROUP_LEADER.getCode().equals(vo.getRoleCode())) {
				t.setTaskDefId(RoleEnum.CHECK_GROUP_LEADER.getCode());
				if(CollectionUtils.isNotEmpty(leaderRes.getData().getList())) {
					for (RecheckQueenNumberVo r : leaderRes.getData().getList()) {
						if(r.getUserCode().equals(vo.getUsercode())) {
							t.setCurrActivieTaskNum(r.getAmount());
							break;
						}
					}
				}
			} else {
				if(CollectionUtils.isNotEmpty(userRes.getTaskNumQues())) {
					
					//组装队列数
					for (TaskNumberQueVo q : userRes.getTaskNumQues()) {
						if(q.getPersonRole().equals("1") && vo.getRoleCode().equals(RoleEnum.CHECK.getCode()) && q.getPersonCode().equals(vo.getUsercode())) {
							//如果是初审员
							t.setTaskDefId(RoleEnum.CHECK.getCode());
							t.setCurrActivieTaskNum(q.getNormalQueue());
							t.setCurrPriorityNum(q.getPriorityQueue());
							t.setCurrInactiveTaskNum(q.getHangQueue());
							break;
						} else if(q.getPersonRole().equals("2") && vo.getRoleCode().equals(RoleEnum.FINAL_CHECK.getCode()) && q.getPersonCode().equals(vo.getUsercode())) {
							//如果是终审员
							t.setTaskDefId(RoleEnum.FINAL_CHECK.getCode());
							t.setCurrActivieTaskNum(q.getNormalQueue());
							t.setCurrPriorityNum(q.getPriorityQueue());
							t.setCurrInactiveTaskNum(q.getHangQueue());
							break;
						}
					}
					
					//组装是否接单
					for (StaffOrderTask so : all) {
						if((so.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FIRST.getValue()) && vo.getRoleCode().equals(RoleEnum.CHECK.getCode()) && so.getStaffCode().equals(vo.getUsercode()))
								|| (so.getTaskDefId().equals(EnumUtils.FirstOrFinalEnum.FINAL.getValue()) && vo.getRoleCode().equals(RoleEnum.FINAL_CHECK.getCode()) && so.getStaffCode().equals(vo.getUsercode()))) {
							t.setIfAccept(so.getIfAccept());
							break;
						}
					}
				}
			}
			returnList.add(t);
		}
		
		ResponsePage<TaskNumber> page = new ResponsePage<TaskNumber>();
		page.setRows(returnList);
		page.setTotal(res.getTotalCount());
		return page;
	}
}
