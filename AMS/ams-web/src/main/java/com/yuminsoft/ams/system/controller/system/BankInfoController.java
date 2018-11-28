package com.yuminsoft.ams.system.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.domain.system.Banks;
import com.yuminsoft.ams.system.service.system.BankInfoService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 银行信息管理
 * @author zhouwq
 *
 */
@Controller
@RequestMapping("/bankInfo")
public class BankInfoController {
	
	@Autowired
	private BankInfoService bankInfoService;
	
	/**
	 * 银行信息管理页面
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "system/bankInfo/bankInfo";
	}

	/**
	 * 分页查询
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<Banks> getPage(RequestPage requestPage,Banks banks) {
		return bankInfoService.getPageList(requestPage,banks);
	}
	
	/**
	 * 查询ById
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(HttpServletRequest request,String id) {
		Banks banks = bankInfoService.findById(Long.parseLong(id));
		return JSONObject.toJSONString(banks);
	}
	
	/**
	 * 保存
	 * @param banks
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(Banks banks){
		bankInfoService.save(banks);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", "true");
		return JSONObject.toJSONString(map);
	}
	
	/**
	 * 修改
	 * @param banks
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(Banks banks){
		int updateRow = bankInfoService.update(banks);
		Map<String,Object> map = new HashMap<String,Object>();
		if(updateRow>0){
			map.put("status", "true");
		}
		return JSONObject.toJSONString(map);
	}
	
	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deletes")
	@ResponseBody
	public String delete(HttpServletRequest request,String[] ids) {
		int deletedId = bankInfoService.delete(ids);
		Map<String,Object> map = new HashMap<String,Object>();
		if(deletedId>0){
			map.put("status", "true");
			map.put("deletedId", deletedId);
		}
		return JSONObject.toJSONString(map);
	}
}
