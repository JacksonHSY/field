package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;
import com.bstek.uflo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 * 工作流初审员分配业务(复核确认)
 * @author dmz
 * @date 2017年4月18日
 */
@Component
public class FirstCheckApprove implements AssignmentHandler{
private static final Logger LOGGER = LoggerFactory.getLogger(FirstCheckApprove.class);
	
	@Resource(name=TaskService.BEAN_ID)
	private  TaskService taskService;
	
	@Override
	public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance,Context context) {
		//获取初审员id
		String empId = (String)context.getProcessService().getProcessVariable("checkApprove", processInstance);
		LOGGER.info("=====复核确认动态分配审批员id{}=====", empId);
		//动态分配审批员
		List<String> list=new ArrayList<String>();
		list.add(empId);
		return list;
	}
}
