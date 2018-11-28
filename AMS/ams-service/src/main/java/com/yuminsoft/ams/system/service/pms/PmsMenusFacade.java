package com.yuminsoft.ams.system.service.pms;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.base.core.common.excption.CoreErrorCode;
import com.ymkj.base.core.web.facade.BaseFacade;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResResourceVO;
import com.yuminsoft.ams.system.exception.BusinessException;
/**
 * 平台操作
 * @author fuhongxing
 */
@Component
public class PmsMenusFacade extends BaseFacade{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsMenusFacade.class);
	
	@Autowired
	private IEmployeeExecuter iEmployeeExecuter;
	
	/**
	 * 1. 获取所有菜单集合
	 * 2. 接口调用
	 * 3. 响应结果处理
	 * @param reqEmployeeVO
	 * @return
	 */
	public List<ResResourceVO> findMenusByAccount(ReqEmployeeVO reqEmployeeVO) {
		// 请求参数构造
		Response<List<ResResourceVO>> response = iEmployeeExecuter.findMenusByAccount(reqEmployeeVO);
		
		// 响应结果处理, 如果失败 则抛出 处理失败异常
		if (response.isSuccess()) {
			return (List<ResResourceVO>) response.getData();
		} else {
			throw new BusinessException(CoreErrorCode.FACADE_RESPONSE_FAIL.toString(),this.getResMsg(response));
		}
	}
	
	/**
	 * 根据角色查询菜单资源
	 * 
	 * @author Jia CX
	 * <p>2018年10月8日 下午2:53:55</p>
	 * 
	 * @param reqEmployeeVO
	 * @return
	 */
	public List<ResResourceVO> findMenusByRoleCodes(ReqEmployeeVO reqEmployeeVO){
		// 请求参数构造
		LOGGER.info("根据角色查询菜单资源请求：" + JSON.toJSONString(reqEmployeeVO));
		Response<List<ResResourceVO>> response = iEmployeeExecuter.findResourceByRoles(reqEmployeeVO);
		LOGGER.info("根据角色查询菜单资源返回：" + JSON.toJSONString(response));
		
		// 响应结果处理, 如果失败 则抛出 处理失败异常
		if (response.isSuccess()) {
			return (List<ResResourceVO>) response.getData();
		} else {
			throw new BusinessException(CoreErrorCode.FACADE_RESPONSE_FAIL.toString(),this.getResMsg(response));
		}
	}
}