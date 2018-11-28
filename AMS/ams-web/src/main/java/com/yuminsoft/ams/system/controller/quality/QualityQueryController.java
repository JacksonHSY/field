package com.yuminsoft.ams.system.controller.quality;

import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.quality.QualityControlDeskService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 质检查询
 * @author sunlonggang
 */
@Controller
@RequestMapping("/qualityQuery")
public class QualityQueryController extends BaseController{
	
	@Autowired
	private QualityControlDeskService qualityControlDeskService;
	
	/**
	 * 质检查询
	 * @return
	 */
	@RequestMapping("/qualityQuery")
	public String qualityQuery(HttpServletRequest request) {
		//查询当前登录人员最高角色code放在session中
		Map<String, String> infoMap = qualityControlDeskService.getQualityRolesInfo(ShiroUtils.getCurrentUser().getUsercode());
		request.getSession().setAttribute("roleCode",infoMap.get("roleCode"));
		return "quality/qualityQuery/qualityQuery";
	}
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getDonePage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo) {
		return qualityControlDeskService.getPageList(requestPage,qualityControlDeskVo,QualityEnum.MenuFlag.质检综合查询.getCode());
	}
}
