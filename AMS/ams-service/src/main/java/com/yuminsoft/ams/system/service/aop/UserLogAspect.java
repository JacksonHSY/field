package com.yuminsoft.ams.system.service.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.system.UserLogService;
import com.yuminsoft.ams.system.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 用户日志切面
 * @author fuhongxing
 */
@Component
@Aspect
public class UserLogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserLogAspect.class);
	@Autowired
	private UserLogService userLogService;
	
	/**
	 * 
	 * @description 切点
	 * @author fuhongxing
	 */
	@Pointcut("@annotation(com.yuminsoft.ams.system.annotation.UserLogs)")
	public void userLogs() {

	}

	/**
	 * 
	 * @description 用户操作日志通知，围绕方法会记录结果
	 * @param jp 连接点
	 * @author fuhongxing
	 * @throws Throwable
	 */
	@Around("userLogs()")
	public Object userlogOperate(ProceedingJoinPoint jp) throws Throwable {
		Object result = null;
		try {
			result = jp.proceed();
		} catch (Exception e) {
			LOGGER.error("aop用户操作日志记录获取方法返回值异常", e);
			throw e;
		} finally {
			try {
				//获取注解参数
				Map<String, String> map = getRemark(jp);
				//获取方法参数
				JSONObject json = getArgs(jp);
				String loanNo = getLoanNo(json);
				//请求方法
//				String signature = getSignature(jp);
				ServletRequestAttributes request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
				if (!CollectionUtils.isEmpty(map)) {
					LOGGER.debug("==========AOP用户操作日志记录===========");
					//获取当前用户信息
					ResEmployeeVO userInfo = null;
					try {
						userInfo = ShiroUtils.getCurrentUser();
					} catch (Exception e) {
						LOGGER.info("获取当前用户异常！！！", e);
					}
					if (null == userInfo) {
						userInfo = new ResEmployeeVO();
						userInfo.setUsercode("anonymous");
						userInfo.setName("anonymous");
					}
					
					UserLog userLog = new UserLog(loanNo, WebUtils.retrieveClientIp(request.getRequest()), map.get("link"), map.get("operation"), json.toJSONString(), request.getRequest().getRequestURI(), map.get("type"), map.get("remark"));
					userLog.setCreatedBy(userInfo.getName());
					userLog.setLastModifiedBy(userInfo.getUsercode());
					LOGGER.debug("参数：{}",json.toJSONString());
					userLogService.save(userLog);
				}
			} finally {
			}

		}
		return result;
	}
	/**
	 * 从json中获取loanNo
	 * 
	 * @param json
	 * @return
	 * @author JiaCX
	 * @date 2017年9月27日 下午2:38:45
	 */
	private String getLoanNo(JSONObject json){
	    if(StringUtils.isNotEmpty(json.getString("loanNo"))){
	        return json.getString("loanNo");
	    }else{
	        Set<Entry<String, Object>> entrySet = json.entrySet();
	        for (Entry<String, Object> s : entrySet) {
	            try{
	                JSONObject obj = JSON.parseObject(JSON.toJSONString(s.getValue()));
	                return getLoanNo(obj);
                }catch(Exception e){
                    return null;
                }
	        }
	    }
        return null;
    }

    /**
	 * 输出异常信息到文件中
	 * @param exception
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static String getUncaughtException(Exception exception) throws FileNotFoundException {
//		StringWriter stringWriter = new StringWriter();
//		String errorStack = Throwables.getStackTraceAsString(exception);
		String errorMsg = exception instanceof BusinessException ? exception.getMessage() : AmsConstants.DEFAULT_ERROR_MESSAGE;        
//      String errorStack = Throwables.getStackTraceAsString(exception);
		PrintWriter printWriter = new PrintWriter(errorMsg);
		exception.printStackTrace(printWriter);
		printWriter.close();
		return errorMsg;
	}

	/**
	 * 
	 * 获取注解参数内容
	 * @param jp 连接点
	 * @return 备注
	 * @author fuhongxing
	 */
	public Map<String, String> getRemark(JoinPoint jp) {
		UserLogs userLog = getAnnotation(jp);
		if (userLog != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("link", userLog.link());
			map.put("operation", userLog.operation());
			map.put("remark", userLog.remark());
			map.put("type", userLog.type().toString());
			return map;
		}
		return null;
	}
	
	/**
	 * 注解参数获取
	 * @param jp
	 * @return
	 */
	private UserLogs getAnnotation(JoinPoint jp) {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		UserLogs userLog = method.getAnnotation(UserLogs.class);
		return userLog;
	}
	
	/*private String getSignature(JoinPoint jp) {
		return jp.getSignature().toLongString();
	}*/

	/**
	 * 
	 * @description 获取参数
	 * @param jp 连接点
	 * @return 参数列表
	 * @author fuhongxing
	 */
	public JSONObject getArgs(JoinPoint jp) {
		Object[] argValues = jp.getArgs();
//		LOGGER.info("获取请求参数："+JSON.toJSONString(argValues));
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
