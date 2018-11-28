package com.yuminsoft.ams.system.controller.quality;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualityControlDeskService;
import com.yuminsoft.ams.system.service.quality.QualityInspectionProcessService;
import com.yuminsoft.ams.system.service.quality.QualityLogService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;

/**
 * @Desc: 质检处理情况
 * @Author: phb
 * @Date: 2017/4/26 9:12
 */
@Controller
@RequestMapping("/qualityInspectionProcess")
public class QualityInspectionProcessController extends BaseController {
	 private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionProcessController.class);
	@Autowired
	private QualityInspectionProcessService qualityInspectionProcessService;
	@Autowired
	private QualityControlDeskService qualityControlDeskService;
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;
	/**
	 * 质检处理情况
	 * @return
	 */
	@RequestMapping("/qualityInspectionProcess")   
	public String qualityInspectionProcess(HttpServletRequest request, Model model) {
		//查询当前登录人员最高角色code放在session中
		Map<String, String> infoMap = qualityControlDeskService.getQualityRolesInfo(ShiroUtils.getCurrentUser().getUsercode());
		request.getSession().setAttribute("roleCode",infoMap.get("roleCode"));
		model.addAttribute("qualityUser", ShiroUtils.getCurrentUser().getUsercode());
		return "quality/qualityInspectionProcess/qualityInspectionProcess";
	}
	
	/**
	 * 待处理列表
	 * @return
	 */
	@RequestMapping("/toDoPageList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getToDoPage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo) {
		return qualityInspectionProcessService.getPageTodoList(requestPage,qualityControlDeskVo);
	}
	
	/**
	 * 已完成列表
	 * @return
	 */
	@RequestMapping("/donePageList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getDonePage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo) {
		try {
			return qualityInspectionProcessService.getPageDoneList(requestPage,qualityControlDeskVo);
		} catch (BusinessException e) {
			return null;
		}
	}
	
	/**
	 * 待处理列表导出
	 * @return
	 */
	@RequestMapping("/exportToDoExcel")
	@ResponseBody
	public void exportToDoExcel(String queryParams, HttpServletRequest req, HttpServletResponse res) {
		QualityControlDeskVo qualityControlDeskVo = JSON.parseObject(queryParams, QualityControlDeskVo.class);
		qualityInspectionProcessService.exportToDoList(qualityControlDeskVo,req,res);
	}
	/**
	 * 已完成列表导出
	 * @return
	 */
	@RequestMapping("/exportDoneExcel")
	@ResponseBody
	public void exportDoneExcel(String queryParams,HttpServletRequest req, HttpServletResponse res) {
		try {
			QualityControlDeskVo qualityControlDeskVo = JSON.parseObject(queryParams, QualityControlDeskVo.class);
			qualityInspectionProcessService.exportDoneExcel(qualityControlDeskVo, req,res);
			
		} catch (BusinessException e) {
			LOGGER.info("质检待处理已完成页面无数据导出"+e);
		}
	}
	
	/**
	 * 修改信审人员(只修改本地质检抽查时保留的质检数据中审批员)
	 * @return
	 */
	@RequestMapping("/modifyApprovePerson")
	@ResponseBody
	public Result<String> modifyApprovePerson(QualityCheckInfo qualityCheckInfo) {
		//质检修改信审审批员(修改不需要调用借款系统接口，只修改本地抽检时保留的数据)
		return qualityInspectionProcessService.modifyApprovePerson(qualityCheckInfo);
	}

	/**
	 * @Desc: 查询符合条件的申请件自动发起反馈
	 * @Author: phb
	 * @Date: 2017/5/8 16:59
	 */
	@RequestMapping(value = "qualityFeedbackJobExecute",method = RequestMethod.POST)
	@ResponseBody
	public Result<String> qualityFeedbackJobExecute(){
		return qualityInspectionProcessService.qualityFeedbackJobExecute();
	}
	
	/**
	 * 已完成批量关闭
	 * @author lihuimeng
	 * @date 2017年6月23日 上午11:18:43
	 */
	@RequestMapping("/close")
	@ResponseBody
	public Result<String> close(HttpServletRequest request, @RequestBody String[] ids) {
		return qualityCheckInfoService.closes(ids);
	}
}
