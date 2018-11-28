package com.yuminsoft.ams.system.service.approve.impl;

import com.bstek.uflo.service.ProcessService;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.audit.ReqBMSReassignmentBatchVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.audit.ResReassignmentUpdVO;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import com.yuminsoft.ams.system.domain.uflo.Task;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.approve.FirstReformService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初审改派service
 * @author dmz
 */
@Service
public class FirstReformServiceImpl implements FirstReformService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstReformServiceImpl.class);
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;
	@Autowired
	private StaffOrderTaskService staffOrderTaskService;
	@Autowired
	private StaffAbilityService staffAbilityService;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;
	@Autowired
	private ApplyHistoryMapper applyHistoryMapper;
	@Autowired
	private ApplyHistoryService applyHistoryService;
	@Autowired
	private PmsApiService pmsApiService;
	@Value("${sys.code}")
	private String sysCode;
	@Resource(name = ProcessService.BEAN_ID)
	private ProcessService processService;
	@Resource(name = com.bstek.uflo.service.TaskService.BEAN_ID)
	private com.bstek.uflo.service.TaskService ufloTaskService;
	/**
	 * 初审改派
	 * @param request
	 * @param reformVO
	 * @return
	 */
	@Override
	public Result<ResReassignmentUpdVO> updateFirstReformService(ReqBMSReassignmentBatchVo request, ReformVO reformVO) {
		applyHistoryService.saveReformApplyHistory(reformVO, "FirstReform");// 批量改派审批日志写入
		String userCode = reformVO.getUserCode();// 员工编号
		String targetUserCode = reformVO.getTargetUserCode();// 改派目标用户code
		String updateType = "default";// 标记工作流修改类型
		// 判断申请件属于正常件还是优先件
		EnumUtils.StaffTaskType staffTaskType = EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(reformVO.getIfNewLoanNo()) ? EnumUtils.StaffTaskType.ACTIVITY : EnumUtils.StaffTaskType.PRIORITY;
		// 获取被改派人初审或直通车标识
		if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(reformVO.getRtfNodeState())) {// 挂起队列
			LOGGER.info("改派借款单号[{}],原挂起队列", reformVO.getLoanNo());
			// 修改被改派人队列数
			staffOrderTaskService.updateStaffTaskNum(userCode, EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.INACTIVITY);
			// 修改改派人队列数
			staffOrderTaskService.updateStaffTaskNum(targetUserCode, EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, staffTaskType);

			updateType = EnumConstants.RtfNodeState.XSCSHANGUP.getValue();
		} else if (EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(reformVO.getRtfNodeState())) {// 正常队列或优先队列
			LOGGER.info("改派借款单号[{}],原已分配,是否是优先:{}", reformVO.getLoanNo(), reformVO.getIfNewLoanNo());
			// 修改被改派人队列数
			staffOrderTaskService.updateStaffTaskNum(userCode, EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.MINUS, staffTaskType);
			// 修改改派人队列数
			staffOrderTaskService.updateStaffTaskNum(targetUserCode, EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, staffTaskType);

			updateType = EnumConstants.RtfNodeState.XSCSASSIGN.getValue();
		} else {// 如果在未分配时，需要修改流程节点
			LOGGER.info("改派借款单号[{}],原未分配", reformVO.getLoanNo());
			// 修改改派人队列数
			staffOrderTaskService.updateStaffTaskNum(targetUserCode, EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, staffTaskType);
		}
		reformUpdateWorkflow(updateType, reformVO); // 修改工作流
		Result<ResReassignmentUpdVO> result = bmsLoanInfoService.updateReform(request);
		if (null != result  && null != result.getDataList() && null!= result.getDataList().get(0) && result.getDataList().get(0).isIfSuccessful()) {
			LOGGER.info("改派成功,由 {} 改派给 {} 的借款号 {} 成功!", reformVO.getUserCode() == null ? "待分派池" : reformVO.getUserCode(), reformVO.getTargetUserCode(), reformVO.getLoanNo());
		} else {
			LOGGER.info("改派失败,由 {} 改派给 {} 的借款号 {} 失败!", reformVO.getUserCode(), reformVO.getTargetUserCode(), reformVO.getLoanNo());
			throw new BusinessException("改派失败");
		}
		return result;
	}

	/**
	 * 初审改派时修改工作流
	 *
	 * @author dmz
	 * @date 2017年6月12日
	 * @param updateType
	 * @param reformVO
	 */
	private void reformUpdateWorkflow(String updateType, ReformVO reformVO) {
		if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(updateType)) {// 原来在挂起队列的改派工作流
			// 修改工作流
			LOGGER.info("原来在挂起队列的改派工作流");
			Task task = taskMapper.findByBusinessId(reformVO.getLoanNo());
			taskService.changeTask(task.getId(), reformVO.getTargetUserCode());
		} else if (EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(updateType)) {// 原来在已分配的情况下修改工作流
			// 修改工作流
			LOGGER.info("原来在已分配的情况下修改工作流");
			Task task = taskMapper.findByBusinessId(reformVO.getLoanNo());
			taskService.changeTask(task.getId(), reformVO.getTargetUserCode());
		} else {// 如果在未分配时，需要修改流程节点
			Task task = taskMapper.findByBusinessId(reformVO.getLoanNo()); //查询工作流
			if (null == task) {
				LOGGER.info("未分派无工作流情况下修改工作流");
				String taskJSON = taskService.startProcess(reformVO.getLoanNo(), reformVO.getTargetUserCode(), reformVO.getTargetUserCode());
				LOGGER.info("创建工作流 {}", taskJSON);
			} else {
				LOGGER.info("未分派有工作流情况下修改工作流");
				ufloTaskService.start(task.getId());
				processService.saveProcessVariable(task.getProcessInstanceId(), "firstApprove", reformVO.getTargetUserCode());
				ufloTaskService.complete(task.getId());
			}
		}
	}

	/**
	 * 如果有退回件标识，则对获取到的结果进行筛选
	 * 
	 * @author zhouwen
	 * @date 2017年7月6日
	 * @param resBMSReassignmentVoList
	 * @return
	 */
	@Override
	public List<ResBMSReassignmentVo> getReturnLoanNoList(List<ResBMSReassignmentVo> resBMSReassignmentVoList) {
		List<ResBMSReassignmentVo> list = new ArrayList<ResBMSReassignmentVo>();
		for (ResBMSReassignmentVo resBMSReassignmentVo : resBMSReassignmentVoList) {
			resBMSReassignmentVo.setCustomerIDNO(Strings.hideIdCard(resBMSReassignmentVo.getCustomerIDNO()));
			ApplyHistory apply = applyHistoryService.getWorkbenchStateByLoanNo(resBMSReassignmentVo.getLoanNo());
			if (apply != null) {
				String rtfState = apply.getRtfState();
				String rtfNodeState = apply.getRtfNodeState();
				String checkNodeState = apply.getCheckNodeState();
				if ((EnumConstants.RtfState.XSZS.getValue().equals(rtfState) && EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(rtfNodeState)) || (checkNodeState != null && EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(checkNodeState))) {
					list.add(resBMSReassignmentVo);
				}
			}
		}
		return list;
	}

	/**
	 * 获取大组信息，没有接单能力用户的大组不显示
	 * @param taskDef      初终审标识 初审(apply-check) 终审(applyinfo-finalaudit)
	 * @param reformVOList
	 * @author wulj
	 * @return
	 */
	@Override
	public List<ResOrganizationVO> getBigGroupByAccountAndAbility(String taskDef, List<ReformVO> reformVOList) {
		// 查询出数据权限下所有大组
		List<ResOrganizationVO> orgList = pmsApiService.getBigGroupByAccount();
		return filterEmptyOrganization(orgList, taskDef, reformVOList);
	}

	/**
	 * 据登录用户和大组id获取小组
	 * @param orgId 大组ID
	 * @param taskDef
	 * @param reformVOList
	 * @author wulj
	 * @return
	 */
	@Override
	public List<ResOrganizationVO> getTeamByAccountAndOrgIdAndAbility(Long orgId, String taskDef, List<ReformVO> reformVOList) {
		List<ResOrganizationVO> teamList = null;
		teamList = pmsApiService.getSmallGroupByAccount(orgId);
		return filterEmptyOrganization(teamList, taskDef, reformVOList);
	}

	/**
	 * 过滤掉没有员工的机构列表
	 * @param orgList 机构列表
	 * @param taskDef 初终审标识(初审(apply-check) 终审(applyinfo-finalaudit))
	 * @param reformVOList 申请件改派列表
	 * @return 过滤后的机构列表
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private List<ResOrganizationVO> filterEmptyOrganization1(List<ResOrganizationVO> orgList, String taskDef,final List<ReformVO> reformVOList){
		//  判断角色
		final RoleEnum userRole = EnumUtils.FirstOrFinalEnum.FINAL.getValue().equals(taskDef) ? RoleEnum.FINAL_CHECK : RoleEnum.CHECK;
		// 查询每个大组下是否有接单能力的人员
		if(!CollectionUtils.isEmpty(orgList)) {
			CollectionUtils.filter(orgList, new Predicate() {
				@Override
				public boolean evaluate(Object o) {
					ResOrganizationVO orgItem = (ResOrganizationVO)o;
					ReqParamVO request = new ReqParamVO();
					request.setOrgId(orgItem.getId());
					request.setRoleCode(userRole.getCode());
					request.setStatus(AmsConstants.ZERO);
					request.setInActive(AmsConstants.T);
					request.setLoginUser(ShiroUtils.getAccount());
					List<ResEmployeeVO> employeeVoList = pmsApiService.getEmpsByAccount(request);
					if (CollectionUtils.isEmpty(employeeVoList)) {	// 如果该组下没有任何员工，则过滤掉
						return false;
					}
					CollectionUtils.filter(employeeVoList, new Predicate() {
						@Override
						public boolean evaluate(Object o) {
							ResEmployeeVO employee = (ResEmployeeVO)o;
							// 判断员工状态是否为"启用"(0-启用，1-禁用)
							boolean flag = employee.getStatus() == 0;
							if(flag){
								for (ReformVO reformVO : reformVOList) {
									// 如果当前员工工号等于被改派的申请件处理人，则过滤掉不显示
									if(StringUtils.isNotEmpty(reformVO.getUserCode()) && reformVO.getUserCode().equals(employee.getUsercode())){
										flag = false;
									}
									// 如果当前员工没有接单能力的员工，则过滤掉不显示
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("staffCode", employee.getUsercode());
									params.put("areaCode", reformVO.getSpecialOrg());
									params.put("productCode", reformVO.getProductCd() + "-" + reformVO.getApplyType());
									if (!staffAbilityService.getStaffOrderAbilityByMap(params)) {
										flag = false;
										break;
									}
								}
							}

							return flag;
						}
					});
					if(CollectionUtils.isEmpty(employeeVoList)){
						return false;
					}
					return true;
				}
			});
		}
		return orgList;
	}
	
	@Autowired
	private PmsApiService pmsApiServiceImpl;

	/**
	 * 改派过滤大组或小组
	 * @param orgList-数据权限下的所有大组或小组
	 * @param taskDef-初审角色
	 * @param reformVOList-被改派借款信息
	 * @return
	 */
	private List<ResOrganizationVO> filterEmptyOrganization(List<ResOrganizationVO> orgList, String taskDef,final List<ReformVO> reformVOList){
		/*
		 * 1---查询当前登录用户下级所有初审组员列表
		 * 2---禁用状态的员工不要，当前员工工号等于被改派的申请件处理人不要，当前员工没有接单能力不要
		 * 3---把过滤后的初审员匹配大组或者小组，没有员工的组去掉
		 */
		//  判断角色
		final RoleEnum userRole = EnumUtils.FirstOrFinalEnum.FINAL.getValue().equals(taskDef) ? RoleEnum.FINAL_CHECK : RoleEnum.CHECK;
		
		//查询当前登录用户下级所有初审组员列表（数据权限查询的已经是启用和在职的）
		ReqParamVO req = new ReqParamVO();
		req.setLoginUser(ShiroUtils.getAccount());
		req.setRoleCode(userRole.getCode());
		List<ResEmpOrgVO> firstGroup = pmsApiServiceImpl.findEmpOrgByAccount(req);
		

		List<StaffOrderAbility> soaList = staffAbilityService.getStaffOrderAbilityByUserCodes(getUserListOfEmp(firstGroup));
		//去掉当前员工工号等于被改派的申请件处理人的员工和没有接单能力的员工
		List<ResEmpOrgVO> firstGroupTem = new ArrayList<ResEmpOrgVO>();
		if(!CollectionUtils.isEmpty(firstGroup)) {
			List<String> reformUsers = getUserListOfReform(reformVOList);
			for (ResEmpOrgVO rov : firstGroup) {
				if(!Strings.isEqualsEvenOnce(rov.getUsercode(), reformUsers)) {
					OK: for (ReformVO rv : reformVOList) {
						String userCode = rov.getUsercode();
						String specialOrg = rv.getSpecialOrg();
						String productCode = rv.getProductCd() + "-" + rv.getApplyType();
						if(!CollectionUtils.isEmpty(soaList)) {
							for (StaffOrderAbility soa : soaList) {
								if(soa.getValue().intValue() !=0 && soa.getStaffCode().equals(userCode) && soa.getAreaCode().equals(specialOrg) && soa.getProductCode().equals(productCode)) {
									firstGroupTem.add(rov); 
									break OK;
								}
							}
						}
					}
				}
			}
		}
		//匹配大组小组
		List<ResOrganizationVO> returnList = new ArrayList<ResOrganizationVO>();
		for (ResOrganizationVO org : orgList) {
			OK: for (ResEmpOrgVO emp : firstGroupTem) {
				if (org.getId().longValue() == emp.getOrgPid().longValue() || org.getId().longValue() == emp.getOrgId().longValue()) {
					returnList.add(org);
					break OK;
				}
			}
		}
		
		return returnList;
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
	
	private List<String> getUserListOfEmp (List<ResEmpOrgVO> empList) {
		List<String> userList = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(empList)){
			for (ResEmpOrgVO resEmpOrgVO : empList) {
				userList.add(resEmpOrgVO.getUsercode());
			}
		}
		return userList;
	}
}
