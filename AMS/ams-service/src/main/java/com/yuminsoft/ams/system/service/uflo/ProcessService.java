package com.yuminsoft.ams.system.service.uflo;

import java.util.Map;

import com.bstek.uflo.query.ProcessInstanceQuery;
import com.bstek.uflo.query.ProcessQuery;
import com.bstek.uflo.query.ProcessVariableQuery;
import com.bstek.uflo.service.StartProcessInfo;
/**
 * 流程服务接口
 * @author fuhongxing
 *
 */
public interface ProcessService {

	/**
	 * 根据流程名称开启流程
	 * @param proType
	 * @param variables
	 */
	public void startProcessByName(String proType,StartProcessInfo startProcessInfo);
	
	
	/**
	  * 向指定流程实例ID对应的流程实例中添加流程变量
	  * @param processInstanceId 流程实例ID
	  * @param key 流程变量的key
	  * @param value 对应的流程变量的值
	  */
	 void saveProcessVariable(long processInstanceId,String key,Object value);
	 
	 /**
	  * 向指定流程实例ID对应的流程实例中批量添加流程变量
	  * @param processInstanceId 流程实例ID
	  * @param variables 要添加的流程变量的Map
	  */
	 void saveProcessVariables(long processInstanceId,Map<String,Object> variables);
	  
	 /**
	  * @return 返回创建成功的流程实例查询对象
	  */
	 ProcessInstanceQuery createProcessInstanceQuery();
	 /**
	  * @return 返回创建成功的流程变量查询对象
	  */
	 ProcessVariableQuery createProcessVariableQuery();
	  
	 /**
	  * @return 创建创建成功的流程模版查询对象
	  */
	 ProcessQuery createProcessQuery();

}
