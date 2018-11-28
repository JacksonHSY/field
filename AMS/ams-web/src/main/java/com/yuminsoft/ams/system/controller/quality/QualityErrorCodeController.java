package com.yuminsoft.ams.system.controller.quality;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.service.quality.QualityErrorCodeService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 质检差错代码
 * @author YM10174
 */
@Controller
@RequestMapping("/qualityErrorCode")
public class QualityErrorCodeController {
	@Autowired
	private QualityErrorCodeService qualityErrorCodeService;
	
	@RequestMapping("/index")
	public String index() {
		return "quality/QualityErrorCode/qualityErrorCode";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<QualityErrorCode> getPage(RequestPage requestPage,QualityErrorCode qualityErrorCodeInfo) {
		return qualityErrorCodeService.getPageList(requestPage,qualityErrorCodeInfo);
	}
	
	/**
	 * 差错代码保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(HttpServletRequest request, QualityErrorCode qualityErrorCodeInfo) {
		return qualityErrorCodeService.save(qualityErrorCodeInfo);
	}
	
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(HttpServletRequest request, String id) {
		QualityErrorCode qualityErrorCodeInfo = qualityErrorCodeService.findById(Long.parseLong(id));
		return JSONObject.toJSONString(qualityErrorCodeInfo);
	}
	@RequestMapping("/update/{oldCode}")
	@ResponseBody
	public String update(HttpServletRequest request, QualityErrorCode qualityErrorCodeInfo,@PathVariable String oldCode) {
		return qualityErrorCodeService.update(qualityErrorCodeInfo,oldCode);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, String[] ids) {
		return qualityErrorCodeService.delete(ids);
	}
}
