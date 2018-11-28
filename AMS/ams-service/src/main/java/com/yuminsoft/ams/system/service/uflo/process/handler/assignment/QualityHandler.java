package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.DecisionHandler;
import com.google.common.collect.Lists;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.service.pms.PmsApiService;

/**
 * 判断当前审批是否为质检主管审批
 * @author phb
 *
 */
@Component
public class QualityHandler implements DecisionHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityHandler.class);
//	@Resource(name=TaskService.BEAN_ID)
//	private  TaskService taskService;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private PmsApiService pmsApiService;
	@Override
	public String handle(Context context, ProcessInstance processInstance) {
		LOGGER.info("当前任务：" + processInstance.getCurrentTask());
		//确认还是争议
		String flowName = (String)context.getProcessService().getProcessVariable("flowName", processInstance);
		List<ResRoleVO> rolesByAccount = pmsApiService.findRolesByAccount(ShiroUtils.getAccount());
		//判断是否为质检员
		Boolean flag = false;
		for (ResRoleVO resRoleVO : rolesByAccount) {
			if(resRoleVO.getCode().equals(RoleEnum.QUALITY_CHECK.getCode())){//质检员审批的
				flag=true;
//				Long taskId = taskMapper.findTaskIdByBusinessId(processInstance.getBusinessId());
//				String currentAssignee = context.getTaskService().getTask(taskId).getAssignee();
				String currentAssignee = null;
				List<String> roles =  Lists.newArrayList();
				roles.add(RoleEnum.QUALITY_CHECK_DIRECTOR.getCode());
				//调用平台系统查询主管
				List<ResEmployeeVO> emplist = pmsApiService.findByOrgTypeCodeAndRole(roles, OrganizationEnum.OrgCode.QUALITY_CHECK.getCode(), 0);
		        if (!CollectionUtils.isEmpty(emplist)) {
		        	currentAssignee = emplist.get(0).getUsercode();//获得员工工号
		        }
				LOGGER.info("获取质检主管：{},返回质检主管核对审批节点", currentAssignee);
				Map<String, Object> map = new HashMap<String, Object>();
				//存放审批人
				List<String> list = new ArrayList<String>();
				list.add(currentAssignee);
				map.put("approveUserList", list);
				context.getProcessService().saveProcessVariables(processInstance.getId(), map);
				break;
			}
		}
		
		
		if(flag){
			//质检员审批
			return "to 质检主管核对";
		}else if(QualityEnum.FeedbackResult.CONFIRM.getCode().equals(flowName)){
			//质检主管审批
			return "to 质检经理定版";
		}else{
			return "to 信审组长二次反馈";
		}

	}
}
