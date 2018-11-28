package com.yuminsoft.ams.system.service.aop;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.domain.system.SystemLog;
import com.yuminsoft.ams.system.service.system.SystemLogService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.WebUtils;

/**
 * 系统日志切面
 * @author fuhongxing
 */
@Component
@Aspect
public class SysLogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);
	@Autowired
	private SystemLogService systemLogService;
	@Autowired
	private RedisUtil redisUtil;
	private long startTimeMillis = 0; // 开始时间
	private long endTimeMillis = 0; // 结束时间
	/**
	 * 
	 * @description 切点
	 * @author fuhongxing
	 */
	@Pointcut("execution(* com.yuminsoft.ams.system.controller..*.*(..))")
	public void sysLog() {
		
	}
	
	/**
	 * 
	 * @Description: 方法调用前触发  记录开始时间 
	 * @param joinPoint
	 */
	@Before("sysLog()")
	public void doBeforeSysLog(JoinPoint joinPoint) {
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
	}

	/**
	 * 
	 * @Description: 方法调用后触发  记录结束时间
	 * @param joinPoint
	 */
	@After("sysLog()")
	public void doAfterSysLog(JoinPoint joinPoint) {
		endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
	}
	
	/**
	 * 
	 * @description 用户操作日志通知，环绕方法前后记录
	 * @param jp 连接点
	 * @author fuhongxing
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("sysLog()")
	public Object syslogOperate(ProceedingJoinPoint jp) throws Throwable {
		Object result = null;
		try {
			//获取方法返回值
			result = jp.proceed();
		} catch (Exception e) {
			LOGGER.error("aop系统日志记录获取方法返回值异常{}", e.getMessage());
			throw e;
//			throw new BusinessException(AmsConstants.DEFAULT_ERROR_MESSAGE);
		} finally {
			try {
				//获取方法参数
				JSONObject json = getArgs(jp);
				ServletRequestAttributes request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
				//获取当前用户信息
				ResEmployeeVO userInfo = null;
				//获取存放redis中的菜单信息
				Map<String, String> map = null;
				if("ams-job".equals(request.getRequest().getContextPath())){
					return result;
				}
				try {
					userInfo = ShiroUtils.getCurrentUser();
					if(redisUtil.exists("menu-" + userInfo.getUsercode())){
						map = (Map<String, String>) redisUtil.get("menu-" + userInfo.getUsercode());
					}
				} catch (Exception e) {
					LOGGER.info("AOP系统日志记录获取当前用户信息异常,方法名称:{}  异常信息:{} ", (jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName()),e.getMessage());
				}
				if (null == userInfo) {
//					userInfo = new ResEmployeeVO();
//					userInfo.setUsercode("anonymous");
//					userInfo.setName("anonymous");
					return result;
				}
				//获取存放redis中的菜单信息
				
				//只记录在平台系统配置的资源对应的请求日志
				if(!CollectionUtils.isEmpty(map) && map.containsKey(request.getRequest().getRequestURI())){
					LOGGER.debug("==========AOP系统操作日志记录===========");
					SystemLog sysLog = new SystemLog(map.get(request.getRequest().getRequestURI()), new Date(startTimeMillis), WebUtils.retrieveClientIp(request.getRequest()));
					sysLog.setCreatedBy(userInfo.getName());
					sysLog.setLastModifiedBy(userInfo.getUsercode());
					sysLog.setRemark(request.getRequest().getRequestURI());
					if(!json.isEmpty()){
						sysLog.setRequestContent(json.toJSONString());
					}
					//响应时间
					sysLog.setResponseDate(new Date(endTimeMillis));
					//根据请求url判断当前操作,读取拼接菜单信息
					sysLog.setResponseContent(JSON.toJSONString(result));
					LOGGER.debug("参数：{}",json.toJSONString());
					systemLogService.save(sysLog);
				}
				
			} finally {
			}

		}
		return result;
	}
	
	/**
	 * 
	 * @description 获取参数
	 * @param jp 连接点
	 * @return 参数列表
	 * @author fuhongxing
	 */
	public JSONObject getArgs(JoinPoint jp) {
		Object[] argValues = jp.getArgs();
		//获取当前执行的方法
		MethodSignature signature = (MethodSignature) jp.getSignature();
		//获取方法参数名称
		String[] argNames = signature.getParameterNames();
		JSONObject json = new JSONObject();
		for (int i = 0; i < argValues.length; i++) {
			if (null == argValues[i] || argValues[i] instanceof Model || argValues[i] instanceof ModelMap || argValues[i] instanceof ServletRequest || argValues[i] instanceof ServletResponse || argValues[i] instanceof MultipartFile || argValues[i] instanceof HttpSession) {
				continue;
			}
			//记录方法参数
			json.put(argNames[i], argValues[i]);
		}
		return json;

	}
}
