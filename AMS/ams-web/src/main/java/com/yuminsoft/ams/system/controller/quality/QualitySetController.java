package com.yuminsoft.ams.system.controller.quality;

import com.google.gson.Gson;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum.OrgCode;
import com.ymkj.pms.biz.api.service.IOrganizationExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;
import com.yuminsoft.ams.system.service.quality.QualitySetService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 质检抽检率设置
 * @author sunlonggang
 */
@Controller
@RequestMapping("/QualitySet")
public class QualitySetController {

	@Autowired
	private QualitySetService QualitySetService;
	@Autowired
	private IOrganizationExecuter organizationExecuter;
	@Value("${sys.code}")
	private String sysCode;
	
	/**
	 * 质检抽检率设置
	 * @return
	 */
	@RequestMapping("/qualitySet")
	public String samplingRateSetting(Model model) {
		List<QualitySetInfo> twoSet = QualitySetService.getTwoSet(); //获取抽检率
		if(!CollectionUtils.isEmpty(twoSet)){ //只有本次周期
			model.addAttribute("now",twoSet.get(0));
			if(twoSet.size()>AmsConstants.ONE){
				model.addAttribute("next", twoSet.get(1));
			}
		}
		return "quality/qualitySet/qualitySet";
	}
	
	/**
	 * 质检抽检率保存
	 * @return
	 */
	@RequestMapping("/samplingRateSave")
	@ResponseBody
	public Result<String> QualitySet(QualitySetInfo samplingInfo) {
		return QualitySetService.samplingRateSave(samplingInfo);
	}
	
	/**
	 * 营业部树图
	 */
	@RequestMapping("/findAllDepts")
	@ResponseBody
	public  String findAllDepts(){
		ReqParamVO ReqParamVO  = new  ReqParamVO();
		List<String> org = new ArrayList<String>();
		org.add("funcDepartment");
		org.add("group");
		ReqParamVO.setSysCode("ams");
		ReqParamVO.setOrgTypes(org);
		Response<ResOrganizationTreeVO> list = new Response<ResOrganizationTreeVO>();
			 list = organizationExecuter.findAllOrgTreeByOrgTypes(ReqParamVO);
			 Gson gson = new Gson();
			return "["+gson.toJson(list.getData())+"]";
	}

	//判断当前人员是否是组长及以上人员
	/**
	 * @author wangzx
	 * @version 2017年6月13日
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/ifLeader")
	@ResponseBody
	public Result<String> ifLeader(HttpServletRequest request,Model model){
		//获取当前登录人员code
		String code = ShiroUtils.getAccount();
		ReqParamVO vo = new ReqParamVO();
		vo.setSysCode(sysCode);
		vo.setOrgTypeCode(OrgCode.QUALITY_CHECK.getCode());
		vo.setLoginUser(code);
		Result<String> result = new Result<String>();
		result.setType(Result.Type.SUCCESS);
		try {
			/**调用平台系统判断当前人员是否是质检组下领导人员**/
			Response<ResGroupVO> revo = organizationExecuter.findGroupInfoByAccount(vo);
			ResGroupVO group = revo.getData();
			//isAuth为true说明该人是组长及以上角色
			result.addMessage(group.isAuth()+"");
			List<QualitySetInfo> list = QualitySetService.getTwoSet();
			if(list.size()==0 || list.size()==1){
				result.addMessage(String.valueOf(AmsConstants.ZERO));
			}else{
				QualitySetInfo  info = list.get(1);
				if(StringUtils.isEmpty(info.getEndDate())){
					result.addMessage(String.valueOf(AmsConstants.ZERO));
				}else{
					result.addMessage(String.valueOf(AmsConstants.ONE));
				}
				
			}
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
		}
		return result;
	}
}
