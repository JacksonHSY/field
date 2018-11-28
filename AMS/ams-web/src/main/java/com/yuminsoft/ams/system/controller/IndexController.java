package com.yuminsoft.ams.system.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.domain.system.SystemLog;
import com.yuminsoft.ams.system.service.pms.PmsMenusService;
import com.yuminsoft.ams.system.service.system.SystemLogService;
import com.yuminsoft.ams.system.service.system.TimeManageService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.Tree;

@Controller
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private SystemLogService systemLogService;
	@Autowired
	private PmsMenusService pmsMenusService;
	@Value("${sys.code}")
	public String sysCode;
	
	@Autowired
	private TimeManageService timeManageService;

	@RequestMapping("/")
	public String index(String name, Model model) {
		// 获取当前登录用户
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		// 获取登录用户工号
		String userCode = currentUser.getUsercode();
		model.addAttribute("userCode", userCode);
		// 写入redis
		// redisUtil.set("user", userCode, 60l);
		LOGGER.info("======当前登录用户：{},工号：{}=====", userCode, currentUser.getName());
		if(!timeManageService.hasPermission()) {
			model.addAttribute("perms", "none");
			return "default";
		}else {
			return "index";
		}
	}

	/**
	 * 返回左边菜单
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月14日
	 * @return
	 */
	@RequestMapping("/leftMenu")
	public String leftMenu() {
		return "common/leftMenu";
	}

	@RequestMapping("dataGrid")
	public String dataGrid() {
		return "datagrid";
	}

	/**
	 * ajax加载表格数据
	 * 
	 * @param page
	 * @param rows
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("loadDataGrid")
	@ResponseBody
	public String loadDataGrid(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows, HttpServletResponse response) throws IOException {
		List<SystemLog> list = systemLogService.findAll(page, rows);
		JSONObject json = new JSONObject();
		if (!CollectionUtils.isEmpty(list)) {
			json.put("total", ((Page<SystemLog>) list).getTotal());
			json.put("rows", list);
		}
		return json.toJSONString();
	}

	@RequestMapping("addSystemLog")
	@ResponseBody
	public String addSystemLog(String param) throws IOException {
		SystemLog systemLog = JSON.parseObject(param, SystemLog.class);
		int count = systemLogService.save(systemLog);
		if (count > 0) {
			Result.Type.SUCCESS.toString();
		}
		return Result.Type.FAILURE.toString();
	}

	@RequestMapping("updateSystemLog")
	@ResponseBody
	public String updateSystemLog(SystemLog systemLog) throws IOException {
		int count = systemLogService.save(systemLog);
		if (count > 0) {
			Result.Type.SUCCESS.toString();
		}
		return Result.Type.FAILURE.toString();
	}

	@RequestMapping("deleteSystemLog")
	@ResponseBody
	public String deleteSystemLog(List<Long> ids) throws IOException {
		int count = systemLogService.delete(ids.get(0));
		if (count > 0) {
			Result.Type.SUCCESS.toString();
		}
		return Result.Type.FAILURE.toString();
	}

	/**
	 * 加载菜单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findMenuTree")
	@ResponseBody
	public Tree findMenuTree(HttpServletRequest request) {
		// 获取当前登录用户
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		// 获取登录用户工号
		String userCode = currentUser.getUsercode();
		LOGGER.info("======加载用户菜单资源=====");
		// 返回动态菜单，替换静态菜单
		ReqEmployeeVO vo = new ReqEmployeeVO();
		// File file=new File("bms.properties");
		// 业务系统编码
		vo.setSysCode(sysCode);
		// 业务类别
		vo.setBizType("");
		// 员工工号
		vo.setUsercode(userCode);
		// 菜单树形结构生成
		Tree menus = pmsMenusService.findMenuTree(vo);
		return menus;
	}

	/**
	 * 健康检查请求
	 * 
	 * @author dmz
	 * @date 2017年7月17日
	 * @return
	 */
	@RequestMapping("/healthCheck")
	@ResponseBody
	public String Health() {
		return "AMS-WEB SUCCESS";
	}

	@RequestMapping("sessionExpired")
	@ResponseBody
	public Result<String> sessionExpired(){
		// 验证登录是否超时
		if(StringUtils.isEmpty(ShiroUtils.getAccount())){
			return new Result<String>(Result.Type.FAILURE, "登录超时");
		}

		return new Result<String>(Result.Type.SUCCESS, "未登录超时");
	}
}
