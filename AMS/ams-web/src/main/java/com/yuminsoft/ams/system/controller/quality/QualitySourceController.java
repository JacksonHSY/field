package com.yuminsoft.ams.system.controller.quality;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;
import com.yuminsoft.ams.system.service.quality.QualitySourceService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 质检申请来源
 * @author YM10174
 */
@Controller
@RequestMapping("/qualitySource")
public class QualitySourceController {
	@Autowired
	private QualitySourceService qualitySourceService;
	
	@RequestMapping("/index")
	public String index() {
		return "quality/qualitySource/qualitySource";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<QualitySourceInfo> getPage(RequestPage requestPage,QualitySourceInfo qualitySourceInfo) {
		return qualitySourceService.getPageList(requestPage,qualitySourceInfo);
	}
	
	/**
	 * 申请来源保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(HttpServletRequest request, QualitySourceInfo qualitySourceInfo) {
		return qualitySourceService.save(qualitySourceInfo);
	}
	
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(HttpServletRequest request, String id) {
		QualitySourceInfo qualitySourceInfo = qualitySourceService.findById(Long.parseLong(id));
		return JSONObject.toJSONString(qualitySourceInfo);
	}
	@RequestMapping("/update/{oldSource}")
	@ResponseBody
	public String update(HttpServletRequest request, QualitySourceInfo qualitySourceInfo,@PathVariable String oldSource) {
		return qualitySourceService.update(qualitySourceInfo,oldSource);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, String[] ids) {
		return qualitySourceService.delete(ids);
	}
	//获得所有质检来源
	@RequestMapping("/getAllSource")
	@ResponseBody
	public List<QualitySourceInfo> getAllSource(){
		return qualitySourceService.getAllSource();
	} 
	
}
