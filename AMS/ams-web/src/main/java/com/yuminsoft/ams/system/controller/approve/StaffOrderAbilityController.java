package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.StaffOrderAbilityVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工接单能力配置控制层
 * 
 * @author dmz
 * @date 2017年3月3日
 */
@Controller
@RequestMapping("/staffOrderAbility")
public class StaffOrderAbilityController extends BaseController{

	@Autowired
	private StaffAbilityService staffAbilityService;

	@Autowired
	private StaffOrderTaskService staffOrderTaskService;

	@Autowired
	private BmsBasiceInfoService bmsBasiceInfoService;

	@Autowired
	private PmsApiService pmsApiService;

	@RequestMapping("/firstOrderAbility")
	public String index(Model model) {
		model.addAttribute("MaxNormal", staffOrderTaskService.findMaxNormalQueue());// 从系统参数读取初终审人员的正常队列上限最大值
		model.addAttribute("MaxHang", staffOrderTaskService.findMaxHangQueue());// 从系统参数读取初终审人员的挂起队列上限最大值
		return "/approve/first/firstOrderAbility";
	}

	/**
	 * 接单能力配置查询
	 * @param type	org-机构, staff-用户
	 * @param code	机构ID或者用户编号
	 * @param model
	 * @author fushangjun
	 * @return
	 */
	@RequestMapping("/config")
	public String findStaffAbility(String type, String code, Model model) {
		List<ResOrganizationVO> areaList = pmsApiService.findAllOrgAreas();			// 地区查询
		areaList.remove(4); /**删掉中区**/
		List<ResBMSProductVO> productList = bmsBasiceInfoService.getProductList();	// 产品查询
		List<String> userCodes = Lists.newArrayList(code.split(","));		// 拆分员工工号

		// 查询员工接单能力，去除中区数据
		List<StaffOrderAbility> existsList = Lists.newArrayList();
		if(userCodes.size() == 1){
			List<StaffOrderAbility> list = Lists.newArrayList();
			existsList = staffAbilityService.getStaffOrderAbilityByUserCodes(userCodes);
			for(StaffOrderAbility ability:existsList){
				if("0107".equals(ability.getAreaCode())){
					list.add(ability);
				}
			}
			existsList.removeAll(list);
		}

		// 员工接单能力
		List<StaffOrderAbilityVo> abilityVoList = Lists.newArrayList();
		for (ResBMSProductVO productItem : productList) {
			for (EnumUtils.ApplyTypeEnum applyType : EnumUtils.ApplyTypeEnum.values()) {
				StaffOrderAbilityVo abilityVo = new StaffOrderAbilityVo();
				abilityVo.setProductCode(productItem.getCode());
				abilityVo.setProductName(productItem.getName());
				abilityVo.setType(applyType.getValue());
				for (ResOrganizationVO orgItem : areaList) {
						StaffOrderAbility ability = new StaffOrderAbility();
						ability.setType(applyType.getValue());
						ability.setProductCode(productItem.getCode());
						ability.setAreaCode(orgItem.getOrgCode());
						abilityVo.getStaffAbilityList().add(ability);
					}
				abilityVoList.add(abilityVo);
			}
		}

		model.addAttribute("type", type);
		model.addAttribute("areaListJson", JSONArray.toJSONString(areaList));
		model.addAttribute("productListJson", JSONArray.toJSONString(productList));
		model.addAttribute("usersJson", JSONArray.toJSONString(userCodes));
		model.addAttribute("abilityListJson", JSONArray.toJSONString(existsList));
		model.addAttribute("abilityVoListJson", JSONArray.toJSONString(abilityVoList,SerializerFeature.WriteMapNullValue));

		return "/approve/first/firstStaffAbility";
	}

	/**
	 * 保存机构上限配置 fusj
	 * 
	 * @return
	 */
	@RequestMapping("/saveStaffAbility")
	@ResponseBody
	public Result<String> saveStaffAbility(StaffOrderSet staffOrderSet, Long orgId) {
		LOGGER.info("机构上限配置  params:{} result:{}", JSON.toJSONString(staffOrderSet));
		Boolean bol = false;
		if (orgId != 0) {
			bol = staffAbilityService.saveStaffAbility(staffOrderSet, orgId);
		}
		Result<String> result = new Result<String>();
		result.addMessage((bol == true) ? "成功" : "失败");
		result.setType((bol == true) ? Type.SUCCESS : Type.FAILURE);
		return result;
	}

	/**
	 * 保存人员上限配置 fusj
	 * 
	 * @return
	 */
	@RequestMapping("/saveUserStaffAbility")
	@ResponseBody
	public Result<String> saveUserStaffAbility(StaffOrderSet staffOrderSet, String usercode) {
		LOGGER.info("人员上限配置  params:{} result:{}", JSON.toJSONString(staffOrderSet));
		Boolean bol = false;
		if (StringUtils.isNotEmpty(usercode)) {
			bol = staffAbilityService.saveUserStaffAbility(staffOrderSet, usercode);
		}
		Result<String> result = new Result<String>();
		result.addMessage((bol == true) ? "成功" : "失败");
		result.setType((bol == true) ? Type.SUCCESS : Type.FAILURE);
		return result;
	}

	/**
	 * 员工或者组织机构能力配置保存
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @return
	 */
	@RequestMapping("/saveOrUpdateStaffAbility")
	@ResponseBody
	public Result<String> saveStaffAbility(String staffAbility, String userCodes) {
		Result<String> result = new Result<String>(Type.SUCCESS);
		try {
			List<String> userCodeList = JSONArray.parseArray(userCodes, String.class);
			List<StaffOrderAbility> staffOrderAbilities = JSONArray.parseArray(staffAbility, StaffOrderAbility.class);
			staffAbilityService.saveOrUpdateStaffAbilityService(userCodeList, staffOrderAbilities);
		} catch (Exception e) {
			LOGGER.error("员工能力配置异常", e);
			result.addMessage("操作失败");
			result.setType(Type.FAILURE);
		}

		result.addMessage("操作成功");

		return result;
	}

	/**
	 * 根据员工编号获取能力
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @return
	 */
	@RequestMapping("/getStaffAbility/{staffCode}")
	@ResponseBody
	public List<StaffOrderAbility> getStaffAbility(@PathVariable String staffCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staffCode", staffCode);
		List<StaffOrderAbility> list = staffAbilityService.getStaffOrderAbility(map);
		return list;
	}

	/**
	 * 根据员工编号获取员队列配置上限
	 * 
	 * @author dmz
	 * @date 2017年4月10日
	 * @return
	 */
	@RequestMapping("/getStaffOrdreSetByStaffCode")
	@ResponseBody
	public Result<StaffOrderSet> getStaffOrdreSetByStaffCode(String staffCode, String taskDefId) {
		Result<StaffOrderSet> result = new Result<StaffOrderSet>(Type.FAILURE);
		StaffOrderSet sos = staffAbilityService.getStaffOrderSetByStaffCodeAndTaskDefId(staffCode, taskDefId);
		if (null != sos) {
			result.setType(Type.SUCCESS);
			result.setData(sos);
		}
		return result;
	}
	/**
	 * @author wangzhenxing
	 * 获取机构下有初审角色的员工
	 */
	@RequestMapping("/getCheckRolePerson")
	@ResponseBody
	public List<String> getCheckRolePerson(Long orgId){
		List<ResEmployeeVO> list = pmsApiService.findEmpsByOrgId(orgId);
		List<String> codes = Lists.newArrayList();
		for(ResEmployeeVO res: list){
			if(pmsApiService.hasRole(res.getUsercode(), RoleEnum.CHECK.getCode())){
				codes.add(res.getUsercode());

			}
		}
		return codes;
	}
}