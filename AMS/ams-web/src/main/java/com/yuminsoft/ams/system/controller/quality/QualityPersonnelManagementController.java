package com.yuminsoft.ams.system.controller.quality;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.service.quality.QualityPersonnelManagementService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityTaskInfoVo;

/**
 * 质检人员管理
 * @author sunlonggang
 */
@Controller
@RequestMapping("/qualityPersonnelManagement")
public class QualityPersonnelManagementController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityPersonnelManagementController.class);
	
	@Autowired
	private QualityPersonnelManagementService qualityPersonnelManagementService;
	/**
	 * 质检人员管理
	 * @return
	 */
	@RequestMapping("/qualityPersonnelManagement")
	public String qualityPersonnelManagement() {
		LOGGER.info("=======质检人员管理页面跳转=====");
		return "quality/qualityPersonnelManagement/qualityPersonnelManagement";
	}
	
	/**
	 * 质检人员树
	 * @return
	 */
	@RequestMapping("/getQualityPerson")
	@ResponseBody
	public String getQualityPerson() {
		//获取当前用户code,返回当前人员及辖下机构树
		String code = ShiroUtils.getCurrentUser().getUsercode();
		return qualityPersonnelManagementService.getCheckUser(code);
	}
	
	
	/**
	 * 分页显示日志
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<QualityTaskInfo> getPage(RequestPage requestPage,QualityTaskInfo qualityTaskInfo) {
		return qualityPersonnelManagementService.getPageList(requestPage,qualityTaskInfo);
	}
	
	/**
	 * 更新质检人员
	 * @param qualityTaskInfoVo
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Result<String> deleteBatch(QualityTaskInfoVo qualityTaskInfoVo) {
		Result<String> result = new Result<String>();

		if(StringUtils.isEmpty(qualityTaskInfoVo.getCheckedUsers())){
			return new Result<String>(Result.Type.FAILURE, "未选择质检人员");
		}

		// 质检人员列表
		List<ResOrganizationTreeVO> checkUsers = JSONObject.parseArray(qualityTaskInfoVo.getCheckedUsers(), ResOrganizationTreeVO.class);

		qualityPersonnelManagementService.updateQualityTask(qualityTaskInfoVo, checkUsers);

		return new Result<String>(Result.Type.SUCCESS);
	}
	
	/**
	 * 删除
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deleteBatch")
	@ResponseBody
	public Result<String> deleteBatch(HttpServletRequest request,String[] ids) {
		return qualityPersonnelManagementService.deleteBatch(ids);
	}
	//获取当前人员及下级，包括离职，禁用
		@RequestMapping("/getBranchPerson")
		@ResponseBody
		public List<ResEmployeeVO> getBranchPerson(HttpServletRequest request){
			String userCode = ShiroUtils.getCurrentUser().getUsercode();
			return qualityPersonnelManagementService.getBranchPerson(userCode);
			}
		
		
	//获取当前人员及下级，包括离职，禁用
	@RequestMapping("/getTwoCyclePerson")
	@ResponseBody
	public List<ResEmployeeVO> getTwoCyclePerson(HttpServletRequest request){
			String userCode = ShiroUtils.getCurrentUser().getUsercode();
			return qualityPersonnelManagementService.getTwoCyclePerson(userCode);
	}
		
		
}
