package com.yuminsoft.ams.system.controller.system;

import com.yuminsoft.ams.system.domain.system.SystemLog;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.service.system.SystemLogService;
import com.yuminsoft.ams.system.service.system.UserLogService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/log")
public class LogController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);
	@Autowired
	private SystemLogService systemLogService;
	@Autowired
	private UserLogService userLogService;
	
	@RequestMapping("/testDemo")
	public String test() {
		return "demo";
	}

	@RequestMapping("/testDemo2")
	public String tests() {
		return "demo2";
	}

	/**
	 * 用户操作日志管理页面
	 * 
	 * @author luting
	 * @date 2017年3月16日 下午3:45:54
	 */
	@RequestMapping("/userLog")
	public String userLog() {
		return "/system/log/userLog";
	}

	/**
	 * 分页查询用户操作日志
	 */
	@RequestMapping("/userLogPageList")
	@ResponseBody
	public ResponsePage<UserLog> getUserLogPage(RequestPage requestPage, String loanNo, String userCode, String link) {
		Map<String, Object> map = new HashMap<String, Object>();

		if(StringUtils.isNotEmpty(userCode)){
			map.put("lastModifiedBy", userCode);
		}

		if(StringUtils.isNotEmpty(loanNo)){
			map.put("loanNo", loanNo);
		}

		if(StringUtils.isNotEmpty(link)){
			map.put("link", link);
		}

		return userLogService.getAll(requestPage.getPage(), requestPage.getRows(), map);
	}
	
	/**
	 * 分页查询系统日志
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月12日
	 * @return
	 */
	@RequestMapping("/systemLogPageList")
	@ResponseBody
	public ResponsePage<SystemLog> systemLogPage(RequestPage requestPage) {
		return systemLogService.getPageList(requestPage);
	}
	/**
	 * 系统日志查看
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "/system/log/systemLog";
	}
}
