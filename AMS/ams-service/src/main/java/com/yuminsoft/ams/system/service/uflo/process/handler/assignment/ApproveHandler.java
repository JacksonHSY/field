package com.yuminsoft.ams.system.service.uflo.process.handler.assignment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.DecisionHandler;
import com.bstek.uflo.service.TaskService;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
/**
 * 判断审批金额是否超过当前级别审批范围
 * @author Shipf
 *
 */
@Component
public class ApproveHandler implements DecisionHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(ApproveHandler.class);
	@Resource(name=TaskService.BEAN_ID)
	private  TaskService taskService;
	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;
	
	@Override
	public String handle(Context context, ProcessInstance processInstance) {
		LOGGER.info("当前任务：" + processInstance.getCurrentTask());
		//获取终审审批金额
		BigDecimal money = (BigDecimal) (context.getProcessService().getProcessVariable("money", processInstance));
		//获取终审员id
		String empId = (String)context.getProcessService().getProcessVariable("finalEmpId", processInstance);
		LOGGER.info("=====终审动态分配审批员id:{},审批金额{}=====", empId, money);
		
		//获取字典中的协审额度阀值---额度为8万
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paramType", "ApprovalLimit");
		SysParamDefine spd = sysParamDefineMapper.findOne(map);
		String approvalLimit = "";
		if(null != spd){
			approvalLimit = spd.getParamValue();
			LOGGER.info(" --------------- 协审额度阀值[" + approvalLimit+"]");
		}else{
			LOGGER.info(" --------------- 协审额度阀值查询异常");
		}
		
		if(money.compareTo(new BigDecimal(approvalLimit)) <= 0){
			return "to 结束";
		}
		return "to 协审";
	}
	
}
