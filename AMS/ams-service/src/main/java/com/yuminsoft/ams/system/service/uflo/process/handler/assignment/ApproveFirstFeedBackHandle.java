package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;

//工作流分配质检人员
@Component
public class ApproveFirstFeedBackHandle implements AssignmentHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApproveFirstFeedBackHandle.class);
	@Override
	public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance,Context context) {

//		String empId = (String)context.getProcessService().getProcessVariable("handler", processInstance);
//		String checkUser = (String)context.getProcessService().getProcessVariable("checkUser", processInstance);
		//审批人
		List<String> approveList = null;
		if(context.getProcessService().getProcessVariable("approveUserList", processInstance)!=null){
			approveList = (List<String>)context.getProcessService().getProcessVariable("approveUserList", processInstance);
		}
		LOGGER.info("=====质检动态分配审批员handler-code：{}=====", JSON.toJSONString(approveList));
		//动态分配审批员
//		List<String> list=new ArrayList<String>();
//		list.add(empId);
//		if(checkUser != null ){//信审组长一次反馈则需要同时分配给质检员和质检主管
//			LOGGER.info("=====质检动态分配审批员checkUser-code：{}=====", checkUser);
//			list.add(checkUser);
//		}
//		LOGGER.info("=====质检动态分配审批员code：{}=====", empId);
//		list.add(empId);
		return approveList;
	}
}
