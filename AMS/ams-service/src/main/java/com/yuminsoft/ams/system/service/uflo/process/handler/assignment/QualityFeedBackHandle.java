package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * 工作流分配质检人员
 * @author penghb
 */
@Component
public class QualityFeedBackHandle implements AssignmentHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityFeedBackHandle.class);
	@Override
	public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance,Context context) {
		String empId = (String)context.getProcessService().getProcessVariable("handler", processInstance);
		LOGGER.info("=====质检动态分配审批员handler-code：{}=====", empId);
		//动态分配审批员
		List<String> list=new ArrayList<String>();
		list.add(empId);
		return list;
	}
}
