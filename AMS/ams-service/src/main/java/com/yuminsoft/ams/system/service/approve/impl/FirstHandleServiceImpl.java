package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.audit.ContactInfoExecuter;
import com.ymkj.ams.api.service.approve.dispatch.FirstDispatchExecuter;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.service.approve.recheck.RecheckExecuter;
import com.ymkj.ams.api.vo.request.apply.ReqContactInfoVO;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.ReqBMSReassignmentVo;
import com.ymkj.ams.api.vo.request.audit.ReqChcekVO;
import com.ymkj.ams.api.vo.request.audit.ReqCsUpdVO;
import com.ymkj.ams.api.vo.request.audit.first.ReqQqReturnReasonVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAudiUpdVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSCheckVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.master.ResBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.master.ResListVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.bds.biz.api.service.IInternalMatchingExecuter;
import com.ymkj.bds.biz.api.vo.request.PhoneNumberReqVO;
import com.ymkj.bds.biz.api.vo.response.PhoneNumberResVO;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ApprovalHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ProcessrulesCfgMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.excel.ExcelUtil;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.*;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzRequest;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzResponse;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.IdCardCheckInfoVo;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.MobileOnlineInfoVo;
import com.yuminsoft.ams.system.vo.firstApprove.FirstResBMSReassignmentExportVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service
public class FirstHandleServiceImpl implements FirstHandleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstHandleServiceImpl.class);

	@Autowired
	private ApplyHistoryMapper applyHistoryMapper;

	@Autowired
	private TaskService taskServiceImpl;

	@Autowired
	private TaskMapper taskMapper;

	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	@Autowired
	private StaffOrderTaskService staffOrderTaskService;

	@Autowired
	private MobileOnlineService mobileOnlineService;

	@Autowired
	private ProcessrulesCfgMapper processrulesCfgMapper;

	@Autowired
	private ApprovalHistoryService approvalHistoryService;

	@Autowired
	private IInternalMatchingExecuter iInternalMatchingExecuter;

	@Autowired
	private FirstDispatchExecuter firstDispatchExecuter;

	@Autowired
	ApplyInfoExecuter applyInfoExecuter;

	@Autowired
	private PmsApiService pmsApiService;

	@Autowired
	private ApplyHistoryService applyHistoryService;

	@Autowired
	private ApprovalHistoryMapper approvalHistoryMapper;

	@Autowired
	private CreditzxService creditzxService;
	@Autowired
	private RecheckExecuter recheckExecuter;

	@Value("${sys.code}")
	private String sysCode;

	@Autowired
	private ContactInfoExecuter contactInfoExecuter;

	/**
	 * 信审初审办理
	 * 
	 * @author dmz
	 * @date 2017年3月17日
	 * @param applyVo
	 * @return
	 */
	@Override
	public Result<String> updateFirstHandLoanNoService(ApplyHistoryVO applyVo, ReqInformationVO loanInfo) {
		StopWatch stopWatch = new StopWatch();
		Result<String> result = new Result<String>(Type.FAILURE);
		stopWatch.start("查询单个StaffOrderTask对象");
		Map<String, Object> params = new HashMap<String, Object>();// 查询当前操作人员
		// 判断是初审员还是复核确认人员
		params.put("staffCode", EnumConstants.ChcekNodeState.CHECKPASS.getValue().equals(loanInfo.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(loanInfo.getCheckNodeState()) ? applyVo.getCheckComplex() : applyVo.getCheckPerson());
		params.put("status", EnumUtils.DisplayEnum.ENABLE.getValue());// 状态正常
		params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FIRST.getValue());// 初审标识
		StaffOrderTask staffOrderTask = staffOrderTaskService.findOneService(params);
		stopWatch.stop();
		if (null != staffOrderTask) {
			stopWatch.start("保存审核日志");
			// 判断复核节点状态
			if (null == applyVo.getCheckNodeState() || EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(applyVo.getCheckNodeState())) {
				applyVo.setCheckNodeState(getCheckNodeValue(staffOrderTask, applyVo));
			}
			// 添加记录
			ApplyHistory apply = new ApplyHistory();
			BeanUtils.copyProperties(applyVo, apply);
			if (null != applyVo.getFirstReason()) {
				apply.setRefuseCode(applyVo.getFirstReason());
			}
			// edit by zw at 2017-05-04 二级原因拼接
			if (null != applyVo.getSecondReason()) {
				apply.setRefuseCode(apply.getRefuseCode() + "-" + applyVo.getSecondReason());
			}
			// end at 2017-05-04
			apply.setProName(getFirstNodeName(apply.getRtfNodeState(), apply.getCheckNodeState()));
			apply.setProNum(applyVo.getLoanNo());
			// 初审办理获取领导 编号
			if (!EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(apply.getRtfNodeState()) && (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(apply.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECK.getValue().equals(apply.getCheckNodeState()))) {// 保存领导
				apply.setApprovalLeader(pmsApiService.findLeaderByUserCodeAndRole(apply.getCheckPerson(), RoleEnum.CHECK_GROUP_LEADER, OrganizationEnum.OrgCode.CHECK)); // 设置信审组长
				apply.setApprovalDirector(pmsApiService.findLeaderByUserCodeAndRole(apply.getCheckPerson(), RoleEnum.CHECK_DIRECTOR, OrganizationEnum.OrgCode.CHECK)); // 设置信审主管
				apply.setApprovalManager(pmsApiService.findLeaderByUserCodeAndRole(apply.getCheckPerson(), RoleEnum.CHECK_MANAGER, OrganizationEnum.OrgCode.CHECK)); // 设置信审经理
			}
			int save = applyHistoryMapper.save(apply);
			stopWatch.stop();
			if (1 == save) {
				stopWatch.start("修改队列数");
				updateTaskNumber(staffOrderTask, loanInfo, applyVo);// 修改队列数和审批意见状态
				stopWatch.stop();

				stopWatch.start("修改工作流");
				updateFirstWorkflow(applyVo, loanInfo.getApprovalPersonCode());// 修改工作流
				stopWatch.stop();

				// 调用借款系统修改状态
				stopWatch.start("修改审批意见状态");
				result = bmsLoanInfoService.updateFirstLoanNoStateService(applyVo, loanInfo);
				stopWatch.stop();
				if (!result.success()) {
					LOGGER.error(result.getFirstMessage());
					throw new BusinessException(result.getFirstMessage());
				}

				// 调用借款系统，更新联系人电话运行商和归属地信息
				ReqContactInfoVO contactInfoVO = new ReqContactInfoVO();
				contactInfoVO.setLoanNo(applyVo.getLoanNo());
				Response<Object> resp = contactInfoExecuter.modifyOperatorsAndLocation(contactInfoVO);
				if(!resp.isSuccess()) {
					throw new BusinessException("更新联系人电话运行商和归属地信息出错!");
				}

			} else {
				LOGGER.error("添加审核记录出错:{}", save);
				result.addMessage("操作失败");
				throw new BusinessException("添加审核记录出错!");
			}
		} else {
			result.addMessage("你没有权限操作,操作失败");
		}
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 修改流程节点
	 * 
	 * @author dmz
	 * @date 2017年3月17日
	 * @param applyVo
	 * @return
	 */
	private void updateFirstWorkflow(ApplyHistoryVO applyVo, String approvalPersonCode) {
		Long taskId = taskMapper.findTaskIdByBusinessId(applyVo.getLoanNo());
		// 初审任务挂起
		if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
			return;// taskServiceImpl.suspend(taskId);
		}
		// 初审任务办理
		// 判断不需要复核确认或者复核确认通过
		if (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(applyVo.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECKPASS.getValue().equals(applyVo.getCheckNodeState())) {
			if (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(applyVo.getRtfNodeState()) || EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(applyVo.getRtfNodeState())) {// 初审通过到终审
				if (null != approvalPersonCode && !approvalPersonCode.isEmpty()) {
					taskServiceImpl.submitPath(taskId, AmsConstants.TO_APPROVAL);
				} else {
					taskServiceImpl.submitPath(taskId, AmsConstants.TO_FINAL);
				}
				// 初审退回门店 或 前期
			} else if (EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(applyVo.getRtfNodeState()) ||EnumConstants.RtfNodeState.XSCSZDQQRETURN.getValue().equals(applyVo.getRtfNodeState())) {
				taskServiceImpl.submitPath(taskId, AmsConstants.TO_START);
			} else if (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(applyVo.getRtfNodeState())) {// 拒绝
				taskServiceImpl.deny(taskId);
			}
		} else if (EnumConstants.ChcekNodeState.CHECK.getValue().equals(applyVo.getCheckNodeState())) {// 需要复核确认
			taskServiceImpl.submitPath(taskId, AmsConstants.TO_FIRST_CHECK);
		} else if (EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(applyVo.getCheckNodeState())) {// 复核确认不通过
			// 复核确认不通过-所有的单子会回到初审分派
			taskServiceImpl.submitPath(taskId, AmsConstants.TO_START);
		}
	}

	/**
	 * 判断是否需要复核确认
	 * 
	 * @author dmz
	 * @date 2017年3月22日
	 * @param sot
	 * @param applyVo
	 * @return
	 */
	private String getCheckNodeValue(StaffOrderTask sot, ApplyHistoryVO applyVo) {
		String action = EnumConstants.ChcekNodeState.NOCHECK.getValue();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", sot.getStaffCode());
		if (EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(applyVo.getRtfNodeState())) {// 判断"初审提交高审"是否走复核确认
			map.put("ruleType", EnumConstants.RtfNodeState.XSCSPASS.getValue());
		} else {
			map.put("ruleType", applyVo.getRtfNodeState());
		}
		boolean  flag = false;
		if (!CollectionUtils.isEmpty(applyVo.getReturnReasons())) { // 前前进件退回原因是多个
			for (ReqQqReturnReasonVO re : applyVo.getReturnReasons()) {
				map.put("reasonCodeA", re.getPrimaryReasonCode());
				map.put("reasonCodeB", re.getSecondReasonCode());
				map.put("ruleType", EnumConstants.RtfNodeState.XSCSRETURN.getValue());
				flag = processrulesCfgMapper.findByApproveResult(map);
				if (flag) {
					break;
				}
			}
		} else {
			if (!EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(applyVo.getRtfNodeState()) && !EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(applyVo.getRtfNodeState())) {
				map.put("reasonCodeA", applyVo.getFirstReason());
				map.put("reasonCodeB", applyVo.getSecondReason());
			}
			flag = processrulesCfgMapper.findByApproveResult(map);
		}

		if (flag) {
			action = EnumConstants.ChcekNodeState.CHECK.getValue();
		}
		return action;
	}

	/**
	 * 修改员工队列数
	 * 
	 * @author dmz
	 * @date 2017年3月22日
	 * @param sot
	 * @param applyVo
	 */
	private void updateTaskNumber(StaffOrderTask sot, ReqInformationVO loanInfo, ApplyHistoryVO applyVo) {
		if (EnumConstants.ChcekNodeState.NOCHECK.getValue().equals(applyVo.getCheckNodeState()) || EnumConstants.ChcekNodeState.CHECK.getValue().equals(applyVo.getCheckNodeState())) {
			// 挂起件办理--挂起队列减一
			if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(loanInfo.getRtfNodeState())) {
                staffOrderTaskService.updateStaffTaskNum(sot.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.INACTIVITY);
			} else {
				// 新生件办理--办理或者挂起
				if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(loanInfo.getIfNewLoanNo())) {
					if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
                        staffOrderTaskService.updateStaffTaskNum(sot.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.INACTIVITY);
					}
                    staffOrderTaskService.updateStaffTaskNum(sot.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.ACTIVITY);
					// 优先件办理--办理或者挂起
				} else if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(loanInfo.getIfNewLoanNo())) {
					if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
                        staffOrderTaskService.updateStaffTaskNum(sot.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.INACTIVITY);
					}
                    staffOrderTaskService.updateStaffTaskNum(sot.getStaffCode(), EnumUtils.FirstOrFinalEnum.FIRST, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.PRIORITY);
				}
			}
			// 修改审批意见
			if (!EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
				approvalHistoryService.updateFirstApprovalState(applyVo.getLoanNo(), applyVo.getRtfState(), applyVo.getRtfNodeState(), applyVo.getCheckNodeState(), applyVo.getCheckPerson());
			} else {
				LOGGER.info("挂起不需要修改审批意见");
			}
			LOGGER.info("修改队列数成功");
		} else {
			LOGGER.info("不需要修改队列数");
		}
	}

	/**
	 * 返回初审节点名称
	 * 
	 * @author dmz
	 * @date 2017年4月19日
	 * @param rtfNodeState
	 * @param checkNodeState
	 * @return
	 */
	private String getFirstNodeName(String rtfNodeState, String checkNodeState) {
		StringBuffer action = new StringBuffer("信审初审");
		if (EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(rtfNodeState)) {// 分配
			action.append("已分派");
		} else if (EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(rtfNodeState)) {// 挂起
			action.append("挂起");
		} else if (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(rtfNodeState)) {// 通过
			action.append("通过");
		} else if (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(rtfNodeState)) {// 拒绝
			action.append("拒绝");
		} else if (EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(rtfNodeState)) {// 退回
			action.append("退回");
		} else if (EnumConstants.RtfNodeState.HIGHPASS.getValue().equals(rtfNodeState)) {// 提交高审
			action.append("提交高审");
		} else if (EnumConstants.RtfNodeState.XSCSZDQQRETURN.getValue().equals(rtfNodeState)) {
			action.append("退回前前");
		}
		// 复核确认节点
		if (EnumConstants.ChcekNodeState.CHECK.getValue().equals(checkNodeState)) {//
			action.append("-复核确认");
		} else if (EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(checkNodeState)) {// 复核确认拒绝
			action.append("-复核确认拒绝");
		} else if (EnumConstants.ChcekNodeState.CHECKPASS.getValue().equals(checkNodeState)) {// 复核确认通过
			action.append("-复核确认通过");
		}
		return action.toString();
	}

	/**
	 * add by zw at 2017-05-04 初审改派导出excel
	 * 
	 * @param request
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@Override
	public void exportExcel(ReqBMSReassignmentVo request, HttpServletRequest req, HttpServletResponse res) throws Exception {
		request.setPageNum(1);
		request.setPageSize(100000);
		request.setSysCode(sysCode);
		request.setLoginPersonCode(ShiroUtils.getAccount());
		request.setFuncCode(ShiroUtils.getShiroUser().getFuncCode());
		String startDate = request.getXsStartDate();
		String endDate = request.getXsEndDate();
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			startDate = null;
			endDate = null;
		} else {
			startDate = startDate + " 00:00:00";
			endDate = endDate + " 23:59:59";
		}
		request.setXsStartDate(startDate);
		request.setXsEndDate(endDate);
		String caseType = request.getCaseType();
		request.setCaseType(null);
		if (StringUtils.isNotEmpty(caseType)) {
			List<String> type =Arrays.asList(caseType.split(","));
			request.setCaseIdentifyList(type);
		}
		PageResponse<ResBMSReassignmentVo> pageList = firstDispatchExecuter.getReassignmentList(request);
		LOGGER.debug("改派查询接口 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(pageList));
		if (null != pageList && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(pageList.getRepCode())) {
			List<ResBMSReassignmentVo> list = pageList.getRecords();
			String fileName = "初审改派工作台导出数据.xlsx";
			String[] headers = { "案件标识", "借款编号", "提交时间", "申请人姓名", "身份证号码", "借款产品", "营业部", "营业部属性", "处理人", "管理组", "所在队列" };
			List<FirstResBMSReassignmentExportVo> evolist = new ArrayList<FirstResBMSReassignmentExportVo>();
			for (ResBMSReassignmentVo r : list) {
				FirstResBMSReassignmentExportVo exportvo = BeanUtil.copyProperties(r, FirstResBMSReassignmentExportVo.class);
				exportvo.setNowQueue(getNowQueue(r));
				exportvo.setIfPri(getIfPri(r));
				exportvo.setXsSubDate(Strings.truncate(r.getXsSubDate(), 10, ""));
				exportvo.setCheckPersonCode(r.getCheckPerson());
				if (Strings.isEmpty(exportvo.getNowQueue())) {
					exportvo.setCheckPersonCode("");
					exportvo.setCSProxyGroupName("");
				}
				evolist.add(exportvo);
			}
			ExcelUtil.exportExcel(fileName, headers, evolist, req, res, DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
		}
	}

	/**
	 * 获取申请单当前队列
	 * 
	 * @param r
	 * @return
	 */
	private String getNowQueue(ResBMSReassignmentVo r) {
		String queue = "";
		// 信审初审
		if (StringUtil.isNotEmpty(r.getRtfStatus()) && EnumConstants.RtfState.XSCS.getValue().equals(r.getRtfStatus())) {
			if (StringUtil.isNotEmpty(r.getRtfNodeStatus()) && EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(r.getRtfNodeStatus())) {
				queue = "挂起队列";
			} else if (StringUtil.isNotEmpty(r.getIfNewLoanNo())) {
				if ("0".equals(r.getIfNewLoanNo())) {
					if (EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(r.getRtfNodeStatus())) {
						queue = "优先队列";
					}
				} else {
					queue = "正常队列";
				}
			}
		} else if (StringUtil.isNotEmpty(r.getRtfStatus()) && EnumConstants.RtfState.XSZS.getValue().equals(r.getRtfStatus())) {
			if (StringUtil.isNotEmpty(r.getRtfNodeStatus()) && EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(r.getRtfNodeStatus())) {
				queue = "挂起队列";
			} else if (StringUtil.isNotEmpty(r.getIfNewLoanNo())) {
				if ("0".equals(r.getIfNewLoanNo())) {
					if (EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(r.getRtfNodeStatus())) {
						queue = "优先队列";
					}
				} else {
					queue = "正常队列";
				}
			}
		}
		return queue;
	}

	private String getIfPri(ResBMSReassignmentVo r) {
		StringBuffer ifPri = new StringBuffer();
		if ("1".equals(r.getIfPri())) {
				ifPri.append("加急件");
		}
		if ("1".equals(r.getAppInputFlag())) {
			if ("".equals(ifPri.toString())) {
				ifPri.append("APP进件");
			} else {
				ifPri.append("|APP进件");
			}
		}
		if ("1".equals(r.getIfSuspectCheat())) {
			if ("".equals(ifPri.toString())) {
				ifPri.append("触发欺诈规则");
			} else {
				ifPri.append("|触发欺诈规则");
			}
		}
		if ("0".equals(r.getIfNewLoanNo())) {
			ApplyHistory apply = applyHistoryService.getWorkbenchStateByLoanNo(r.getLoanNo());
			if (apply != null) {
				String rtfState = apply.getRtfState();
				String rtfNodeState = apply.getRtfNodeState();
				String checkNodeState = apply.getCheckNodeState();
				if ((EnumConstants.RtfState.XSZS.getValue().equals(rtfState) && EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(rtfNodeState)) || (checkNodeState != null && EnumConstants.ChcekNodeState.CHECKNOPASS.getValue().equals(checkNodeState))) {
					if ("".equals(ifPri.toString())) {
						ifPri.append("退回件");
					} else {
						ifPri.append("|退回件");
					}
				}
			}
		}
		if (EnumUtils.YOrNEnum.Y.getValue().equals(r.getIfPreferentialUser())) {
			if (ifPri.length()>0) {
				ifPri.append("|费率优惠客户");
			} else {
				ifPri.append("费率优惠客户");
			}
		}
		if(EnumUtils.YOrNEnum.Y.getValue().equals(r.getSimplifiedDataUser())) {
			if (ifPri.length() > 0) {
				ifPri.append("|简化资料客户");
			}else {
				ifPri.append("简化资料客户");
			}
		}
		if(EnumUtils.YOrNEnum.Y.getValue().equals(r.getIfReconsiderUser())) {
			if (ifPri.length() > 0) {
				ifPri.append("|复议再申请客户");
			}else {
				ifPri.append("复议再申请客户");
			}
		}
		if (null != r.getZdqqApply() && 1 == r.getZdqqApply()) {
			if (ifPri.length() > 0) {
				ifPri.append("|证大前前");
			} else {
				ifPri.append("证大前前");
			}
		}
		return ifPri.toString();
	}

	/**
	 * add by zw at 2017-05-06 增加电话号码内匹配方法
	 * 
	 * @param loanNo
	 * @return
	 */
	@Override
	public List<PhoneNumberResVO> matchByPhoneNumber(String loanNo) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start(" 增加电话号码内匹配方法");
		PhoneNumberReqVO request = new PhoneNumberReqVO();
		List<PhoneNumberResVO> result = new ArrayList<PhoneNumberResVO>();
		request.setSysCode(sysCode);
		request.setLoanNum(loanNo);
		request.setUserCode(ShiroUtils.getAccount());
		Response<List<PhoneNumberResVO>> response = iInternalMatchingExecuter.matchByPhoneNumber(request);
		stopWatch.stop();
		LOGGER.debug("电话号码匹配查询 params:{}", JSON.toJSONString(request));
		if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
			result = response.getData();
		}
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 初审复核确认
	 * 
	 * @param applyVo
	 * @param request
	 * @param passOrNot
	 *            0:退回；1：通过
	 * @return
	 * @author JiaCX
	 * @date 2017年5月9日 下午2:51:03
	 */
	@Override
	public Result<String> firstApproveReview(ApplyHistoryVO applyVo, HttpServletRequest request, String passOrNot) {
	    ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), applyVo.getLoanNo(), true);
		Result<String> result = new Result<String>();
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		if ("1".equals(passOrNot)) {
			applyVo.setCheckNodeState(EnumConstants.ChcekNodeState.CHECKPASS.getValue());// 复核节点状态(信审初审复核同意)
		} else if ("0".equals(passOrNot)) {
			applyVo.setCheckNodeState(EnumConstants.ChcekNodeState.CHECKNOPASS.getValue());// 复核节点状态(信审初审复核退回)
		}
		applyVo.setCheckComplex(currentUser.getUsercode());// 复核人
		applyVo.setName(applyBasiceInfo.getName());// 借款人
		applyVo.setIdNo(applyBasiceInfo.getIdNo());// 身份证
		applyVo.setRtfState(EnumConstants.RtfState.XSCS.getValue());
		applyVo.setRtfNodeState(applyBasiceInfo.getRtfNodeState());// 流程节点状态
		applyVo.setIp(WebUtils.retrieveClientIp(request));
		applyVo.setVersion(applyBasiceInfo.getVersion().intValue());
		// 添加审批操作历史记录
		ApplyHistory apply = BeanUtil.copyProperties(applyVo, ApplyHistory.class);
		/*if (null != applyVo.getFirstReason()) {
			apply.setRefuseCode(applyVo.getFirstReason());
		}
		if (null != applyVo.getSecondReason()) {
			apply.setRefuseCode(apply.getRefuseCode() + "-" + applyVo.getSecondReason());
		}*/
		apply.setProName(getFirstNodeName(apply.getRtfNodeState(), apply.getCheckNodeState()));
		apply.setProNum(applyVo.getLoanNo());
		int save = applyHistoryMapper.save(apply);
		if (1 == save) {
			// 修改工作流
			updateFirstWorkflow(applyVo, applyBasiceInfo.getApprovalPersonCode());
						
			// 调用借款系统修改状态
			ReqCsUpdVO req = new ReqCsUpdVO();
			req.setLoanNo(applyVo.getLoanNo());
			req.setVersion(applyBasiceInfo.getVersion().intValue());
			req.setOperatorCode(currentUser.getUsercode());
			req.setOperatorIP(WebUtils.retrieveClientIp(request));
			req.setSysCode(sysCode);
			req.setCsPersonCode(applyVo.getCheckPerson());
			req.setComplexPersonCode(currentUser.getUsercode());
			req.setIdNo(applyBasiceInfo.getIdNo());
			req.setName(applyBasiceInfo.getName());
			req.setCellphone(applyBasiceInfo.getCellPhone());
			req.setCellPhoneSec(applyBasiceInfo.getCellPhone_sec());

			if ("1".equals(passOrNot)) {
				req.setCheckNodeStatus(EnumConstants.ChcekNodeState.CHECKPASS.getValue());
			} else if ("0".equals(passOrNot)) {
				req.setCheckNodeStatus(EnumConstants.ChcekNodeState.CHECKNOPASS.getValue());
				req.setRemark(applyVo.getRemark());
			}

			Response<ResBMSAudiUpdVo> response = recheckExecuter.recheck(req);
			LOGGER.info("复核确认调用借款系统 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(response));
			if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
				result.setType(Type.SUCCESS);
				result.addMessage("操作成功");
			} else {
				LOGGER.info("复核确认操作调用借款系统失败:{}", applyVo.getLoanNo());
				throw new BusinessException("复核确认操作时，调用借款系统失败");
			}
		} else {
			LOGGER.info("复核确认添加审核记录出错:{}", applyVo.getLoanNo());
			throw new BusinessException("添加审核记录出错!");
		}
		return result;
	}

	/**
	 * 偿还能力不足时进行负债率校验
	 * 
	 * @param applyBasiceInfo
	 * @return
	 */
	@Override
	public Result<String> repaymentInsufficientJudge(ReqInformationVO applyBasiceInfo, String rtfNodeState) {
		Result<String> result = new Result<String>(Type.FAILURE);
		ApprovalHistory ah = null;
		if (rtfNodeState != null && EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(rtfNodeState)) {// 若申请件状态已经是XSCSREJECT代表是复议修改拒绝原因
			ah = approvalHistoryService.getApprovalHistoryByLoanNoAndStaffCode(applyBasiceInfo.getLoanNo());
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loanNo", applyBasiceInfo.getLoanNo());
			map.put("rtfNodeState", EnumConstants.RtfNodeState.XSCSREJECT.getValue());
			ah = approvalHistoryMapper.findOne(map);// 查询初审拒绝时有无审批记录
		}
		if (null != ah) {
			BigDecimal approvalDebtTate = ah.getApprovalDebtTate();// 内部负债率
			BigDecimal approvalAllDebtRate = ah.getApprovalAllDebtRate();// 总负债率
			ReqBMSTmParameterVO request = new ReqBMSTmParameterVO();
			request.setCode(applyBasiceInfo.getApplyType());
			request.setServiceCode(ShiroUtils.getAccount());
			request.setLoanNo(applyBasiceInfo.getLoanNo());
			request.setSysCode(sysCode);
			ResListVO<ResBMSTmParameterVO> response = applyInfoExecuter.findTmParameterByCode(request);
			LOGGER.info("偿还能力不足时进行负债率校验接口调用 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
			if (EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode()) && null != response.getCollections() && response.getCollections().size() >= 1) {
				ResBMSTmParameterVO resBMSTmParameterVO = response.getCollections().get(0);
				String internalTmParameterVO = resBMSTmParameterVO.getInternalDebtRadio();
				String allTmParameterVO = resBMSTmParameterVO.getTotalDebtRadio();
				BigDecimal bmsApprovalDebtTate = new BigDecimal(internalTmParameterVO);// bms内部负债率
				BigDecimal bmsApprovalAllDebtRate = new BigDecimal(allTmParameterVO);// bms总负债率
				int approvalDebtTateDeviation = bmsApprovalDebtTate.compareTo(approvalDebtTate);
				int approvalAllDebtRateDeviation = bmsApprovalAllDebtRate.compareTo(approvalAllDebtRate);
				if (approvalDebtTateDeviation == -1 || approvalAllDebtRateDeviation == -1) {// 内部负债率和总负债率都超过上限
					BigDecimal approvalLimit = ah.getApprovalLimit();// 审批额度
					String approvalTerm = ah.getApprovalTerm();// 审批期限
					if (approvalLimit.compareTo(new BigDecimal("10000")) == 0 && "12".equals(approvalTerm)) {// 超过限制,且审批额度为1万且审批期限为12期,拒绝操作
						result.setType(Type.FAILURE);
					}
					if (approvalLimit.compareTo(new BigDecimal("10000")) != 0 || !"12".equals(approvalTerm)) {// 额度不是1万，或者审批期限不是12期提示“填写的审批额度及审批期限不符合偿债能力不足拒绝的要求，请修改！”
						result.setType(Type.SUCCESS);
						result.addMessage("填写的审批额度及审批期限不符合偿债能力不足拒绝的要求，请修改！");
					}
				} else {// 内部负债率和总负债率未超过限制
					result.setType(Type.SUCCESS);
					result.addMessage("客户负债率在允许范围内，不可以偿债能力不足拒绝！");
				}
			} else {
				LOGGER.error("偿还能力不足时进行负债率校验接口调用出错:{}", JSON.toJSONString(response));
				throw new BusinessException("偿还能力不足时进行负债率校验接口调用出错!");
			}
		}
		return result;
	}

	/**
	 * 根据借款编号，查询申请件对应的入网时长和实名认证
	 * @param loanNo
	 * @param session
	 * @author wulj
	 * @return
	 */
	@Override
	public List<MobileOnline> getMobileOnlineByLoanNo(String loanNo, HttpSession session){
		ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);	// 查询申请件基本信息

		// 调用借款系统接口，查询该申请件对应的手机号"实名认证"和"在网时长"信息
		List<MobileOnline> mobileOnlineList = bmsLoanInfoService.getCreditReportInfo(loanNo);
		List<MobileOnline> filterList = new ArrayList<MobileOnline>();

		// 如果所有手机号都没有查询到实名认证和入网时长，则返回空数组
		if(CollectionUtils.isEmpty(mobileOnlineList)){
			return filterList;
		}

		List<String> customerMobiles = Lists.newArrayList();
		customerMobiles.add(applyBasicInfo.getCellPhone());
		customerMobiles.add(applyBasicInfo.getCellPhone_sec());
		// 过滤掉数组的null和空字符串
		customerMobiles.removeAll(Collections.singleton(null));
		customerMobiles.removeAll(Collections.singleton(""));

		for (String customerMobile : customerMobiles) {
			MobileOnline mobileOnline = new MobileOnline();

			mobileOnline.setLoanNo(applyBasicInfo.getLoanNo());
			mobileOnline.setIdNo(applyBasicInfo.getIdNo());
			mobileOnline.setName(applyBasicInfo.getName());
			mobileOnline.setMobile(customerMobile);

			Iterator<MobileOnline> iterator = mobileOnlineList.iterator();
			while (iterator.hasNext()) {
				MobileOnline item = iterator.next();
				if(!item.getMobile().equals(customerMobile)){
					continue;
				}

				// 如果借款系统返回的手机号，既不是客户常用手机号，也不是客户备用手机号，则过滤掉不在页面显示
				if(!item.getMobile().equals(applyBasicInfo.getCellPhone()) && !item.getMobile().equals(applyBasicInfo.getCellPhone_sec())){
					LOGGER.info("借款返回手机号{}不是客户常用手机号(或备用手机号)，不显示", mobileOnline.getMobile());
					iterator.remove();
					continue;
				}

				HzRequest hzRequest = new HzRequest();
				hzRequest.setId(mobileOnline.getId());
				hzRequest.setAppNo(applyBasicInfo.getLoanNo());
				hzRequest.setName(applyBasicInfo.getName());
				hzRequest.setIdCard(applyBasicInfo.getIdNo());
				hzRequest.setMobile(mobileOnline.getMobile());
				hzRequest.setIsCheck(false);
				hzRequest.setCreatorId(ShiroUtils.getAccount());
				hzRequest.setTimestamp(System.currentTimeMillis());

				// 通过征信系统，查询手机在网时长
				if(null !=  item.getMobileOnlineId()){
					hzRequest.setId(item.getMobileOnlineId());
					HzResponse<MobileOnlineInfoVo> mobileResult = creditzxService.getMobileOnlineInfoService(hzRequest);
					if(mobileResult.success() && StringUtils.isNotEmpty(mobileResult.getData().getTimes())){
						mobileOnline.setMobileOnline(mobileResult.getData().getTimes());
					}
				}

				// 通过征信系统，查询手机实名认证
				if(null !=  item.getRealCertiId()){
					hzRequest.setTimestamp(System.currentTimeMillis());
					hzRequest.setId(item.getRealCertiId());
					HzResponse<IdCardCheckInfoVo> idCardResult = creditzxService.getIdCardCheckInfo(hzRequest);
					if(idCardResult.success()){
						mobileOnline.setRealCerti(idCardResult.getData().getMsg());
						if (StringUtils.isEmpty(mobileOnline.getRealCerti())) {
							// 如果实名认证msg为空，则根据succeed字段判断是否认证一致
							if ("T".equals(idCardResult.getData().getSucceed())) {
								mobileOnline.setRealCerti("认证结果一致");
							}else{
								mobileOnline.setRealCerti("认证结果不一致");
							}
						}
					}
				}
			}

			filterList.add(mobileOnline);
		}

		return filterList;
	}

	/**
	 * 将"实名认证"和"在网时长"查询结果通过接口更新回借款系统
	 * @param loanNo
	 * @param name
	 * @param idNo
	 * @param mobileOnlineList
	 */
	private void updateBmsMobileOnline(String loanNo, String name, String idNo, List<MobileOnline> mobileOnlineList) {
		JSONObject longOnlineId = new JSONObject();
		JSONObject realNameAuthId = new JSONObject();
		for (MobileOnline mobileOnline : mobileOnlineList) {
			if (mobileOnline.getMobileOnlineId() != null) {
				longOnlineId.put(mobileOnline.getMobile(), mobileOnline.getMobileOnlineId());
			}
			if (mobileOnline.getRealCertiId() != null) {
				realNameAuthId.put(mobileOnline.getMobile(), mobileOnline.getRealCertiId());
			}
		}

		if (!longOnlineId.isEmpty() || !realNameAuthId.isEmpty()) {
			bmsLoanInfoService.syncHZReportID(loanNo, name, idNo, longOnlineId.isEmpty() ? null : longOnlineId.toJSONString(), realNameAuthId.isEmpty() ? null : realNameAuthId.toJSONString());
		}
	}

    @Override
    public ResponsePage<ResBMSCheckVO> getReviewConfirmList(RequestPage requestPage){
        // 获取当前登录用户
        ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
        ResponsePage<ResBMSCheckVO> page = new ResponsePage<ResBMSCheckVO>();
        ReqChcekVO vo = new ReqChcekVO();
        vo.setSysCode(sysCode);
        vo.setLoginUserCode(currentUser.getUsercode());
        vo.setPageNum(requestPage.getPage());
        vo.setPageSize(requestPage.getRows());
        vo.setFieldSort(requestPage.getSort());
        vo.setRulesSort("desc".equals(requestPage.getOrder()) ? 0 : 1);
        PageResponse<ResBMSCheckVO>  resp =   recheckExecuter.getRecheckList(vo);
        LOGGER.info("返回借款系统复核确认 page params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(resp));
        if (null != resp && AmsCode.RESULT_SUCCESS.equals(resp.getRepCode())) {
            page.setRows(CollectionUtils.isEmpty(resp.getRecords()) ? new ArrayList<ResBMSCheckVO>() : resp.getRecords());
            page.setTotal(resp.getTotalCount());
        }
        return page;
    }
}
