package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;
/**
 * 工作流协审审批员分配
 * @author Shipf
 *
 */
@Component
public class ApprovalProcess implements AssignmentHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalProcess.class);
	@Override
	public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance,Context context) {
		//获取终审员id
		String empId = (String)context.getProcessService().getProcessVariable("finalEmpId", processInstance);
		LOGGER.info("=====终审动态分配协审审批员id{}=====", empId);
		
		//动态分配审批员
		List<String> list=new ArrayList<String>();
		list.add(empId);
		
		return list;
	}
}
