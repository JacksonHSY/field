package com.yuminsoft.ams.system.controller.sysservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.service.IOrganizationExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.vo.apply.ReformVO;

import lombok.SneakyThrows;

/***
 * 平台系统接口
 *
 * @author dmz
 * @date 2017年3月7日
 */
@Controller
@RequestMapping("/pmsApi")
public class PMSApiController extends BaseController{

	@Autowired
	private PmsApiService pmsApiService;

	@Autowired
	private IOrganizationExecuter iOrganizationExecuter;

	@Autowired
	private StaffAbilityService staffAbilityService;

	@Autowired
	private ApplyHistoryService applyHistoryService;

	@Autowired
	private StaffOrderTaskService staffOrderTaskService;

	/**
	 * 返回审批组信息
	 *
	 * @return
	 * @author dmz
	 * @date 2017年3月7日
	 */
	@RequestMapping("/findOrgTreeByOrgCode")
	@ResponseBody
	public List<ResOrganizationTreeVO> findOrgTreeByOrgCodes() {
		List<ResOrganizationTreeVO> list = new ArrayList<ResOrganizationTreeVO>();
		try {
			list = pmsApiService.findOrgTreeByOrgCodes();
		} catch (Exception e) {
			LOGGER.error("返回审批组信息异常:", e);
		}
		return list;

	}

	/**
	 * 根据机构id查询员工
	 *
	 * @param orgId
	 * @return
	 * @author dmz
	 * @date 2017年3月7日
	 */
	@RequestMapping("/findByOrgIdAndRoleCode")
	@ResponseBody
	public List<ResEmployeeVO> findEmpsByOrgId(Long orgId, String roleCode,String userCode, String userName) {
		List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
		List<String> roles = new ArrayList<String>();
		roles.add(RoleEnum.CHECK.getCode());
		roles.add(RoleEnum.FINAL_CHECK.getCode());
		ReqParamVO request = new ReqParamVO();
		request.setLoginUser(ShiroUtils.getAccount());
		request.setOrgId(orgId);
		request.setStatus(AmsConstants.ZERO);
		request.setInActive(AmsConstants.T);
		request.setHasRole(true);
		request.setRoleCodes(roles);
		try {
			if(StringUtils.isEmpty(userCode) && StringUtils.isEmpty(userName)){
				list = pmsApiService.getEmpsByAccount(request);
			}else{
				//因为不输入userName 和userCode的时候查询的包含初审和终审，所以输入userName和userCode的情况下，也应该查询初审和终审才能保证查询一致
				list = pmsApiService.findEmpByCodeOrName(orgId, userCode, userName, "check,finalCheck");
			}
			if (!CollectionUtils.isEmpty(list)) {
				String taskDefId = "check".equals(roleCode) ? EnumUtils.FirstOrFinalEnum.FIRST.getValue() : EnumUtils.FirstOrFinalEnum.FINAL.getValue();
				for (ResEmployeeVO relVO : list) {

					StaffOrderSet staffOrderSet = staffAbilityService.getStaffOrderSetByStaffCodeAndTaskDefId(relVO.getUsercode(), taskDefId);// 队列上下限
					if (null != staffOrderSet) {
						relVO.setCreateTime(staffOrderSet.getLastModifiedDate());
						relVO.setCreator(pmsApiService.findEmpByUserCode(staffOrderSet.getLastModifiedBy()).getName());
					} else {
						relVO.setCreateTime(null);
						relVO.setCreator(null);
					}
					relVO.setUpdateTime(null);
					relVO.setUpdator(null);
					StaffOrderAbility staffOrderAbility = staffAbilityService.getStaffOrderAbilityByStaffCode(relVO.getUsercode());
					if (null != staffOrderAbility) {
						relVO.setUpdateTime(staffOrderAbility.getLastModifiedDate());
						relVO.setUpdator(pmsApiService.findEmpByUserCode(staffOrderAbility.getLastModifiedBy()).getName());
					}
					if(StringUtils.isNotEmpty(userCode) || StringUtils.isNotEmpty(userName)){
						List<String> roleList = Lists.newArrayList();
						//根据工号查询用户角色
						roleList = pmsApiService.findRoleCodeListByAccount(relVO.getUsercode());
						relVO.setRoleCodes(roleList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("根据机构id查询员工异常", e);
		}
		return list;
	}

	/**
	 * 根据小组编码查询下面的组员区分终审和初审(目前用于改派)
	 * @author dmz
	 * @date 2017年6月12日
	 * @param orgId				机构ID
	 * @param checkPersonCodes	需要过滤的工号
	 * @param flagFirstOrFinal  check初审, finalCheck终审
	 * @param reformVoStr   	选中的改派申请件
	 * @return
	 */
	@RequestMapping("/findEmpByOrgIdAndRole")
	@ResponseBody
	@SneakyThrows
	@Deprecated
	public List<ResEmployeeVO> findEmpByOrgIdAndRole(Long orgId, final String checkPersonCodes,String flagFirstOrFinal, String reformVoStr) {
		List<ResEmployeeVO> employeeList = new ArrayList<ResEmployeeVO>();
		final List<ReformVO> reformVoList = StringUtils.isNotEmpty(reformVoStr) ? JSONObject.parseArray(reformVoStr, ReformVO.class) : new ArrayList<ReformVO>();
		ReqParamVO request = new ReqParamVO();
		if (null != orgId) {
			request.setOrgId(orgId);
			request.setRoleCode(flagFirstOrFinal);
			request.setStatus(AmsConstants.ZERO);
			request.setInActive(AmsConstants.T);
			employeeList = pmsApiService.getEmpsByAccount(request);
		} else {
			// 没有机构ID，则查询登录人辖下所有初审员工
			ReqLevelVO reqLevelVO = new ReqLevelVO();
			reqLevelVO.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
			reqLevelVO.setRoleCode(RoleEnum.CHECK.getCode());
			List<ResEmpOrgVO> resEmpOrgVOList = pmsApiService.getLowerEmpByAccount(reqLevelVO);
			if (!CollectionUtils.isEmpty(resEmpOrgVOList)) {
				for (ResEmpOrgVO evo : resEmpOrgVOList) {
					ResEmployeeVO resEmployeeVO = new ResEmployeeVO();
					PropertyUtils.copyProperties(resEmployeeVO, evo);
					employeeList.add(resEmployeeVO);
				}
			}
		}

		// 过滤掉没有接单能力、离职、禁用的员工
		CollectionUtils.filter(employeeList, new Predicate() {
			@Override
			public boolean evaluate(Object o) {
				ResEmployeeVO employee = (ResEmployeeVO)o;
				boolean flag = true;

				// 判断员工状态是否为"启用"(0-启用，1-禁用)
				if(employee.getStatus() != null && employee.getStatus() != 0){
					return false;
				}

				for (ReformVO reformVo : reformVoList) {
					// 如果当前员工工号等于被改派的申请件处理人，则过滤掉不显示
					if(StringUtils.isNotEmpty(reformVo.getUserCode()) && reformVo.getUserCode().equals(employee.getUsercode())){
						flag = false;
						break;
					}

					// 如果当前员工没有接单能力的员工，则过滤掉不显示
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("staffCode", employee.getUsercode());
					params.put("areaCode", reformVo.getSpecialOrg());
					params.put("productCode", reformVo.getProductCd() + "-" + reformVo.getApplyType());
					if (!staffAbilityService.getStaffOrderAbilityByMap(params)) {
						flag = false;
						break;
					}
				}

				return flag;
			}
		});


		return employeeList;
	}

	/**
	 * 查询指定机构下指定角色的员工
	 * @param orgId		机构ID
	 * @param roleCodes	用户编码,例如：huwf,dengchao
	 * @return 指定机构下指定角色的员工
	 */
	@RequestMapping("findEmpByDeptAndRoleCodes")
	@ResponseBody
	public List<ResEmployeeVO> findEmpByDeptAndRoleCodes(String orgId, String roleCodes){
        List<ResEmployeeVO> list = Lists.newArrayList();
        if (orgId == null || StringUtils.isEmpty(roleCodes)) {
            return list;
        }

        List<String> roleCodeList = Lists.newArrayList(roleCodes.split(","));
        String[] org = orgId.substring(1).split(",");
        for (String id : org) {
            List<ResEmployeeVO> res = pmsApiService.findByDeptAndRole(Long.parseLong(id), roleCodeList);
            list.addAll(res);
        }
        return list;
	}

	/**
	 * 根据机构ID获取所有一级子机构列表
	 *
	 * @return
	 */
	@RequestMapping("/findOrgsById")
	@ResponseBody
	public List<ResOrganizationVO> findOrgsById(Long orgId) {
		ReqParamVO request = new ReqParamVO();
		request.setSysCode(sysCode);
		request.setOrgId(orgId);
		try {
			Response<List<ResOrganizationVO>> response = iOrganizationExecuter.findOrgsById(request);
			LOGGER.info("返回审批组信息  findOrgTreeByOrgCodes params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
			if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
				return response.getData();
			}
		} catch (Exception e) {
			LOGGER.error("返回审批组信息  findOrgTreeByOrgCodes异常",e);
		}
		return null;
	}

	/**
	 * 根据机构编码,名字查询指定机构id下的子机构
	 *
	 * @param orgId
	 * @param orgCode
	 * @param orgName
	 * @return
	 * @author YM10168
	 * @date 2017年3月30日 上午11:40:04
	 */
	@RequestMapping("/findOrgByCodeOrName")
	@ResponseBody
	public List<ResOrganizationVO> findOrgByCodeOrName(Long orgId, String orgCode, String orgName) {
		List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
		if (orgId != null) {
			try {
				list = pmsApiService.findOrgByCodeOrName(orgId, orgCode, orgName);
			} catch (Exception e) {
				LOGGER.error("根据机构id查询子机构异常", e);
			}
		} else {
			LOGGER.info("机构id不能为空");
		}
		return list;
	}

	/**
	 * 根据工号和名字查询指定机构ID下的人员
	 *
	 * @param orgId
	 * @param userCode
	 * @param userName
	 * @return
	 * @author YM10168
	 * @date 2017年3月30日 下午1:28:09
	 */
//	@RequestMapping("/findEmpByCodeOrName")
//	@ResponseBody
//	public List<ResEmployeeVO> findEmpByCodeOrName(Long orgId, String userCode, String userName, String orgTypeCode) {
//		List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
//		if (orgId != null) {
//			try {
//				list = pmsApiService.findEmpByCodeOrName(orgId, userCode, userName, orgTypeCode);
//				if (!CollectionUtils.isEmpty(list)) {
//					for (ResEmployeeVO relVO : list) {
//						List<String> roles = Lists.newArrayList();
//						//根据工号查询用户角色
//						roles = pmsApiService.findRoleCodeListByAccount(relVO.getUsercode());
//						relVO.setRoleCodes(roles);
//						String taskDefId = orgTypeCode.equals("check") ? EnumUtils.FirstOrFinalEnum.FIRST.getValue() : EnumUtils.FirstOrFinalEnum.FINAL.getValue();
//						StaffOrderSet staffOrderSet = staffAbilityService.getStaffOrderSetByStaffCodeAndTaskDefId(relVO.getUsercode(), taskDefId);// 队列上下限
//						if (null != staffOrderSet) {
//							relVO.setCreateTime(staffOrderSet.getLastModifiedDate());
//							relVO.setCreator(pmsApiService.findEmpByUserCode(staffOrderSet.getLastModifiedBy()).getName());
//						} else {
//							relVO.setCreateTime(null);
//							relVO.setCreator(null);
//						}
//						relVO.setUpdateTime(null);
//						relVO.setUpdator(null);
//						StaffOrderAbility staffOrderAbility = staffAbilityService.getStaffOrderAbilityByStaffCode(relVO.getUsercode());
//						if (null != staffOrderAbility) {
//							relVO.setUpdateTime(staffOrderAbility.getLastModifiedDate());
//							relVO.setUpdator(pmsApiService.findEmpByUserCode(staffOrderAbility.getLastModifiedBy()).getName());
//						}
//					}
//				}
//			} catch (Exception e) {
//				LOGGER.error("根据机构id查询员工异常" ,e);
//			}
//		} else {
//			LOGGER.info("机构id不能为空");
//		}
//		return list;
//	}

	/**
	 * 查询所有营业部
	 *
	 * @return
	 * @author dmz
	 * @date 2017年4月6日
	 */
	@RequestMapping("/findAllDepts")
	@ResponseBody
	public List<ResOrganizationVO> findAllDepts() {
		List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
		try {
			list = pmsApiService.findAllDepts();
		} catch (Exception e) {
			LOGGER.error("查询所有营业部异常", e);
		}
		return list;
	}

	/**
	 * 根据工号获取所在机构及其子机构的成员
	 *
	 * @return
	 * @author dmz
	 * @date 2017年4月6日
	 */
	@RequestMapping("/findEmpByUsercode")
	@ResponseBody
	public List<ResEmployeeVO> findEmpByUsercode() {
		List<ResEmployeeVO> list = null;
		try {
			list = pmsApiService.getUserInfoByLikeUserName(null,RoleEnum.CHECK.getCode() ,null);// 当前登录用户的下级初审组列表
		} catch (Exception e) {
			LOGGER.error("根据工号获取所在机构及其子机构的成员异常", e);
		}
		return list;
	}

	/***
	 * 根据当前用户获取大组
	 *
	 * @author dmz
	 * @date 2017年4月8日
	 * @return
	 */
	@RequestMapping("/findBigGroupByAccount")
	@ResponseBody
	public List<ResOrganizationVO> findBigGroupByAccount() {
		List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
		try {
			list = pmsApiService.getBigGroupByAccount();
		} catch (Exception e) {
			LOGGER.error("根据当前用户获取大组异常", e);
		}
		return list;
	}

	/**
	 * 根据登录用户和大组id获取小组
	 *
	 * @param orgId
	 * @return
	 * @author dmz
	 * @date 2017年4月8日
	 */
	@RequestMapping("/findTeamByAccountAndOrgId")
	@ResponseBody
	public List<ResOrganizationVO> findTeamByAccountAndOrgId(Long orgId) {
		List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
		if (null != orgId && orgId > 0) {
			try {
				list = pmsApiService.getSmallGroupByAccount(orgId);
			} catch (Exception e) {
				LOGGER.error("根据登录用户和大组id获取小组异常", e);
			}
		}
		return list;
	}

	/**
	 * 根据小组机构id查询下属有初审权限的人员
	 *
	 * @param orgId
	 * @return
	 * @author JiaCX
	 * @date 2017年6月15日 下午6:18:56
	 */
	@RequestMapping("/findEmpsByGroupusculeOrgId")
	@ResponseBody
	public List<ResEmployeeVO> findEmpsByGroupusculeOrgId(Long orgId) {
		List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
		if (null != orgId && orgId > 0){
			try {
				ReqParamVO reqParamVO = new ReqParamVO();
				reqParamVO.setLoginUser(ShiroUtils.getAccount());
				reqParamVO.setStatus(AmsConstants.ZERO);
				reqParamVO.setInActive(AmsConstants.T);
				reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
				reqParamVO.setOrgId(orgId);
				list = pmsApiService.getEmpsByAccount(reqParamVO);
			} catch (Exception e) {
				LOGGER.error("根据小组机构id查询下属有初审权限的人员异常", e);
			}
		}

		return list;
	}

	/**
	 * @Desc: 查询出初审组下的所有人员
	 * @Author: phb
	 * @Date: 2017/4/25 17:55
	 */
	@RequestMapping("/findCheckPerson")
	@ResponseBody
	public List<ResEmployeeVO> findCheckPerson() {
		List<ResEmployeeVO> list = new ArrayList<>();
		try {
			Long orgId = pmsApiService.findOrgIdByOrgTypeCode(OrganizationEnum.OrgCode.CHECK.getCode());
			if (orgId != null) {
				list = pmsApiService.findEmpsByOrgId(orgId);
			}
		} catch (Exception e) {
			LOGGER.error("查询出终审组下的所有人员异常", e);
		}
		return list;
	}

	/**
	 * @Desc: 查询出终审组下的所有人员
	 * @Author: phb
	 * @Date: 2017/4/25 17:55
	 */
	@RequestMapping("/findFinalPerson")
	@ResponseBody
	public List<ResEmployeeVO> findFinalPerson() {
		List<ResEmployeeVO> list = new ArrayList<>();
		try {
			Long orgId = pmsApiService.findOrgIdByOrgTypeCode(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
			if (orgId != null) {
				list = pmsApiService.findEmpsByOrgId(orgId);
			}
		} catch (Exception e) {
			LOGGER.error("查询出终审组下的所有人员异常", e);
		}
		return list;
	}

	/**
	 * @Desc: 查询出质检组下的所有人员
	 * @Author: phb
	 * @Date: 2017/4/25 17:55
	 */
	@RequestMapping("/findQualityPerson")
	@ResponseBody
	public List<ResEmployeeVO> findQualityPerson() {
		List<ResEmployeeVO> list = new ArrayList<>();
		try {
			Long orgId = pmsApiService.findOrgIdByOrgTypeCode(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode());
			if (orgId != null) {
				list = pmsApiService.findEmpsByOrgId(orgId);
			}
		} catch (Exception e) {
			LOGGER.error("查询出质检组下的所有人员异常", e);
		}
		return list;
	}

	/**
	 * 获取当前登录用户权限内的机构人员树
	 *
	 * @return
	 * @author JiaCX
	 * @date 2017年9月8日 上午10:09:24
	 */
	@RequestMapping("/userOrgTree")
	public @ResponseBody List<ResOrganizationTreeVO> getUserOrgTree(){
		ReqParamVO reqParamVO = new ReqParamVO();
		List<String> list = new ArrayList<String>();
		list.add(OrganizationEnum.OrgCode.CHECK.getCode());
		list.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCodes(list);
		reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
		reqParamVO.setStatus(AmsConstants.ZERO);
		reqParamVO.setInActive(AmsConstants.T);
	    List<ResOrganizationTreeVO> resList = pmsApiService.getOrgTreeAndEmployees(reqParamVO);
	    convert(resList);
	    return resList;
	}
	

	/**
	 * 获取当前登录用户权限内的机构人员树(不跟据数据权限)
	 * 
	 * @author Jia CX
	 * <p>2018年5月29日 下午4:16:55</p>
	 * 
	 * @return
	 */
	@RequestMapping("/userOrgTreeWithoutDataPermission")
	public @ResponseBody List<ResOrganizationTreeVO> userOrgTreeWithoutDataPermission(){
		ReqParamVO reqParamVO = new ReqParamVO();
		List<String> list = new ArrayList<String>();
		list.add(OrganizationEnum.OrgCode.CHECK.getCode());
		list.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCodes(list);
		reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
		reqParamVO.setStatus(AmsConstants.ZERO);
		reqParamVO.setInActive(AmsConstants.T);
	    List<ResOrganizationTreeVO> resList = pmsApiService.getOrgTreeAndEmployeesWithoutDataPermission(reqParamVO);
	    convert(resList);
	    return resList;
	}

	/**
	 * 去掉个员工id最后的随机数,这个随机数跟pms的排序有关,每次接口请求返回的可能相同，也可能不相同
	 * (因为ams是用这个id来确定唯一的，导致新增收回权限的可能id有时候能匹配到，有时候不能，结果就是某个员工明明被收回了权限，但是有时候可收回的员工列表还是会出来)
	 * 如"id": "dongmingzhi_8888889379_0",改为"id": "dongmingzhi_8888889379"
	 * 等pms这个接口改掉最后的随机数，这个方法可以去掉不用
	 *
	 * @author Jia CX
	 * <p>2018年2月2日 下午6:53:34</p>
	 *
	 * @param reslist
	 */
	private void convert(List<ResOrganizationTreeVO> reslist) {
		if(!CollectionUtils.isEmpty(reslist)) {
			for (ResOrganizationTreeVO resOrganizationTreeVO : reslist) {
				List<ResOrganizationTreeVO> list = resOrganizationTreeVO.getChildren();
				if(CollectionUtils.isEmpty(list)) {
					if(resOrganizationTreeVO.getId().contains("_")) {
						resOrganizationTreeVO.setId(resOrganizationTreeVO.getId().substring(0, resOrganizationTreeVO.getId().lastIndexOf("_")));
					}
				}else {
					convert(list);
				}
			}
		}
	}


	private List<String> getUserListOfReform (List<ReformVO> rvList) {
		List<String> userList = new ArrayList<String>();
		if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(rvList)){
			for (ReformVO reformVO : rvList) {
				userList.add(reformVO.getUserCode());
			}
		}
		return userList;
	}

	private List<String> getUserListOfEmp (List<ResEmployeeVO> rvList) {
		List<String> userList = new ArrayList<String>();
		if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(rvList)){
			for (ResEmployeeVO reformVO : rvList) {
				userList.add(reformVO.getUsercode());
			}
		}
		return userList;
	}

	/**
	 * 查询员工信息
	 * 根据当前用户、机构id、用户名(拼音查询)
	 * @param orgId			机构ID
	 * @param roleCode 		角色编码(check-初审, finalCheck-终审)
	 * @param fpStatus         分派状态(1-已分派;2-未分派)
	 * @param reformVoStr 	选中的改派申请件
	 * @author dmz
	 * @return
	 */
	@RequestMapping("/getUserInfoByLikeUserName")
	@ResponseBody
	@SneakyThrows
	public List<ResEmployeeVO> getUserInfoByLikeUserName(Long orgId, String roleCode, int fpStatus, String reformVoStr){
		List<ResEmployeeVO> employeeList = new ArrayList<ResEmployeeVO>();
	/*	if(null == orgId && pmsApiService.isManagerAbove(ShiroUtils.getAccount(),false)) {// 如果是经理和经理以上角色且没有选择组织机构需要获取所有初审
			employeeList = pmsApiService.findByRoleCode();
		} else {*/
		if (2== fpStatus && null == orgId) { // 未分派查询所有可分派初审员
			employeeList = pmsApiService.findByRoleCode();
		} else {
			employeeList = pmsApiService.getUserInfoByLikeUserName(orgId, roleCode, null);
		}
		//}
		// 过滤掉没有接单能力、离职、禁用的员工
		return filterEmployee(employeeList, reformVoStr, orgId);

//		//下面注释的是备用方法(使用区域和产品拼接起来字符串，判断是否包含)
//		final List<ReformVO> reformVoList = StringUtils.isNotEmpty(reformVoStr) ? JSONObject.parseArray(reformVoStr, ReformVO.class) : new ArrayList<ReformVO>();
//		
//		List<String> prolist = new ArrayList<String>();
//		List<String> reformUsers = new ArrayList<String>();
//		for (ReformVO rv : reformVoList) {
//			prolist.add(rv.getSpecialOrg() + rv.getProductCd() + "-" + rv.getApplyType());
//			reformUsers.add(rv.getUserCode());
//		}
//		
//		//查询所有员工接单能力
//		final List<StaffOrderAbility> soaList = staffAbilityService.getStaffOrderAbilityByUserCodes(getUserListOfEmp(employeeList));
//		Map<String, String> soamap = new HashMap<String, String>();
//		for (StaffOrderAbility s : soaList) {
//			if(s.getValue().intValue() != 0) {
//				if(soamap.containsKey(s.getStaffCode())) {
//					soamap.put(s.getStaffCode(), soamap.get(s.getStaffCode()) + "|" + s.getAreaCode() + s.getProductCode());
//				} else {
//					soamap.put(s.getStaffCode(), s.getAreaCode() + s.getProductCode());
//				}
//			}
//		}
//		
//		// 过滤掉没有接单能力的员工
//		List<ResEmployeeVO> returnList = new ArrayList<ResEmployeeVO>();
//		if(!CollectionUtils.isEmpty(employeeList)) {
//			for (ResEmployeeVO rov : employeeList) {
//				//如果在改派列表的员工就放弃
//				if(!Strings.isEqualsEvenOnce(rov.getUsercode(), reformUsers)) {
//					//如果该员工对所有选中改派的产品都有接单能力，就选择他
//					if(soamap.containsKey(rov.getUsercode()) && Strings.isConstainsEveyone(soamap.get(rov.getUsercode()), prolist)) {
//						returnList.add(rov);
//					}
//				}
//			}
//		}
//		return returnList;
	}

	/**
	 * 过滤掉没有接单能力的员工,且包括历史处理过的员工
	 * @param employeeList
	 * @param reformVoStr
	 * @author wulj
	 * @return
	 */
	private List<ResEmployeeVO> filterEmployee(List<ResEmployeeVO> employeeList,String reformVoStr,Long orgId){
		List<ResEmployeeVO> result = Lists.newArrayList();
		List<ReformVO> reformVoList =  new ArrayList<ReformVO>();
		if(StringUtils.isNotEmpty(reformVoStr)){
			reformVoList = JSONObject.parseArray(reformVoStr, ReformVO.class);
			// 筛初reformList工号
			final List<String> reformUserCode = (List<String>)CollectionUtils.collect(reformVoList, new Transformer() {
				@Override
				public Object transform(Object reformVo) {
					return ((ReformVO)reformVo).getUserCode();
				}
			});
			StringBuffer loanNoArray  = new StringBuffer();
			Map<String, StaffOrderAbility> productMap = Maps.newHashMap();
			for (ReformVO reformVO : reformVoList) {
				StaffOrderAbility staffOrderAbility = new StaffOrderAbility();
				staffOrderAbility.setAreaCode(reformVO.getSpecialOrg());
				staffOrderAbility.setProductCode(reformVO.getProductCd() + "-" + reformVO.getApplyType());
				staffOrderAbility.setComCreditRating("%" + reformVO.getComCreditRating() +"%");// 欺诈风险评估
				productMap.put(staffOrderAbility.getAreaCode() + "-" + staffOrderAbility.getProductCode(), staffOrderAbility);
				loanNoArray.append(reformVO.getLoanNo() +",");
			}

			List<StaffOrderAbility> stoList = new ArrayList<StaffOrderAbility>(productMap.values());
			for (ResEmployeeVO employeeVO : employeeList) {
				// 是否为当前处理人
				if(!CollectionUtils.isEmpty(reformUserCode) && reformUserCode.contains(employeeVO.getUsercode())){
					continue;
				}
				// 是否有接单能力
				List<StaffOrderAbility> staffOrderAbilities = staffAbilityService.getByMultiProduct(employeeVO.getUsercode(), stoList);
				if(CollectionUtils.isEmpty(staffOrderAbilities) || staffOrderAbilities.size() < stoList.size()){
					continue;
				}
				result.add(employeeVO);
			}
			// 查找历史接过单的人
			if (null == orgId) {// 判断改派是否有新生件
				Map<String, Object> mapApply = Maps.newHashMap();
				mapApply.put("loanNoArray", loanNoArray.substring(0, loanNoArray.length() - 1));
				mapApply.put("loanNoCount", reformVoList.size());
				List<ApplyHistory> applyHistoriesList = applyHistoryService.getHistoryApplyDealsPerson(mapApply);
				for (ApplyHistory applyHistory : applyHistoriesList) {
					Map<String, Object> staffotMap = Maps.newHashMap();
					staffotMap.put("taskDefId", EnumUtils.FirstOrFinalEnum.FIRST.getValue());// 初审标识
					staffotMap.put("status", EnumUtils.DisplayEnum.ENABLE.getValue());// 是否是启用并且有初审角色
					staffotMap.put("staffCode", applyHistory.getCheckPerson());// 初审员编号
					StaffOrderTask staffOrderTask = staffOrderTaskService.findOneService(staffotMap);
					if (null != staffOrderTask && !reformUserCode.contains(staffOrderTask.getStaffCode())) {
						ResEmployeeVO employeeVO = new ResEmployeeVO();
						employeeVO.setUsercode(staffOrderTask.getStaffCode());
						employeeVO.setName(staffOrderTask.getStaffName());
						result.add(employeeVO);
                        result.add(employeeVO);
					}
				}
                result = CollectionUtils.removeDuplicateByProperty(result, "usercode");// 去重复
			}
        }
		return result;
	}
}
