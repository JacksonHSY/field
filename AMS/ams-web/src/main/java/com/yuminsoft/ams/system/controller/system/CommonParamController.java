package com.yuminsoft.ams.system.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通用参数管理
 * 
 * @author zhouwq
 *
 */
@Controller
@RequestMapping("/commonParam")
public class CommonParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonParamController.class);

	@Autowired
	private CommonParamService commonParamService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "system/commonParam/commonParam";
	}

	/**
	 * 分页查询
	 * 
	 * @param requestPage
	 * @param sysParamDefine
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<SysParamDefine> getPage(RequestPage requestPage, SysParamDefine sysParamDefine) {
		return commonParamService.getPageList(requestPage, sysParamDefine);
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		SysParamDefine sysParamDefine = commonParamService.findById(id);
		return JSONObject.toJSONString(sysParamDefine);
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(HttpServletRequest request, SysParamDefine sysParamDefine) {
		return commonParamService.update(sysParamDefine);
	}

	/**
	 * @Desc: 根据参数类型查询
	 * @Author: phb
	 * @Date: 2017/5/13 17:43
	 */
	@RequestMapping("/findByParamType")
	@ResponseBody
	public List<SysParamDefine> findByParamType(SysParamDefine sysParamDefine) {
		return commonParamService.findByParamType(sysParamDefine);
	}

	/**
	 * 新增系统参数
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public String add(HttpServletRequest request, SysParamDefine sysParamDefine) {
		return commonParamService.save(sysParamDefine);
	}

	/**
	 * 删除系统参数
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody List<String> list) {
		String[] ids = list.toArray(new String[list.size()]);
		return commonParamService.deletes(ids);
	}

	@RequestMapping(value="cleanRedis", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> cleanCache(){
		LOGGER.info("开始清除redis缓存");

		redisUtil.removeAll();	// 清空redis

		return new Result<String>(Result.Type.SUCCESS, "操作成功");
	}
}
