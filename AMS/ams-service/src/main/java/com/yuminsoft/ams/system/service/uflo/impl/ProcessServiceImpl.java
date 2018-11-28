package com.yuminsoft.ams.system.service.uflo.impl;

import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 流程服务接口
 * @author fuhongxing
 *
 */
@Service					
public class ProcessServiceImpl implements com.yuminsoft.ams.system.service.uflo.ProcessService{
	
	@Resource(name=ProcessService.BEAN_ID)
	private ProcessService processService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessServiceImpl.class);
	
	 /**
	  * 根据流程模版的名称，根据该名称流程模版最新版本开启一个流程实例
	  * @param proname 流程模版名称
	  * @param startProcessInfo 开启流程实例时所需要的各种信息的包装对象
	  * @return 返回开启成功的流程实例对象
	  */
	 @Override
	public void startProcessByName(String proname,StartProcessInfo startProcessInfo){
		LOGGER.info("=====开始流程======");
		processService.startProcessByName(proname, startProcessInfo);
    }
	
	@Override
	public void saveProcessVariable(long processInstanceId, String key, Object value) {
		processService.saveProcessVariable( processInstanceId,  key,  value);
		
	}

	@Override
	public void saveProcessVariables(long processInstanceId, Map<String, Object> variables) {
		
		processService.saveProcessVariables(processInstanceId, variables);
	}

	@Override
	public ProcessInstanceQuery createProcessInstanceQuery() {
		
		return processService.createProcessInstanceQuery();
	}

	@Override
	public ProcessVariableQuery createProcessVariableQuery() {
		
		return processService.createProcessVariableQuery();
	}

	@Override
	public ProcessQuery createProcessQuery() {
		
		return processService.createProcessQuery();
	}
	
}
