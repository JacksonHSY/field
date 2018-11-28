package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.audit.FinalApproveExecuter;
import com.ymkj.ams.api.service.approve.dispatch.FinalDispatchExecuter;
import com.ymkj.ams.api.service.approve.integrate.ApplyInfoExecuter;
import com.ymkj.ams.api.service.approve.product.ProductExecuter;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.*;
import com.ymkj.ams.api.vo.request.master.ReqBMSEnumCodeVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSPlReassignMentUpdVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.audit.ResOffTheStocksAuditVO;
import com.ymkj.ams.api.vo.response.audit.last.ResBMSFinalAduitUpdVO;
import com.ymkj.ams.api.vo.response.master.ResBMSEnumCodeVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductVO;
import com.ymkj.ams.api.vo.response.master.ResBMSTmParameterVO;
import com.ymkj.ams.api.vo.response.master.ResListVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.ApprovalHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderTaskMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.domain.uflo.Task;
import com.yuminsoft.ams.system.excel.ExcelUtil;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.FinalHandleService;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.bms.BmsBasiceInfoService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.service.uflo.TaskService;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.finalApprove.*;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 终审相关操作ServiceImpl
 * 
 * @author Shipf
 * @date
 * @param
 */
@Service
public class FinalHandleServiceImpl implements FinalHandleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FinalHandleServiceImpl.class);

	@Autowired
	private ApplyHistoryMapper applyHistoryMapper;

	@Autowired
	private StaffOrderTaskService staffOrderTaskService;

	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FinalApproveExecuter finalApproveExecuter;

	@Autowired
	private ApprovalHistoryMapper approvalHistoryMapper;

	@Autowired
	private StaffOrderTaskMapper staffOrderTaskMapper;

	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	@Autowired
	private TaskMapper taskMapper;

	@Autowired
	private PmsApiService pmsApiServiceImpl;

	@Autowired
	private BmsBasiceInfoService bmsBasiceInfoService;

	@Autowired
	private CommonParamService commonParamService;

	@Autowired
	private FinalDispatchExecuter finalDispatchExecuter;

	@Autowired
	private ApplyInfoExecuter applyInfoExecuter;
	@Autowired
	private ProductExecuter productExecuter;

	@Value("${sys.code}")
	private String sysCode;

	@Override
	public Result<String> updatePubFinalUflo(ApplyHistoryVO applyVo, HttpServletRequest req, String operateType) {
		StopWatch stopWatch = new StopWatch();
		String loanNo = applyVo.getLoanNo();

		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(req.getSession().getId(), loanNo, true);
		if (applyVo.getVersion() != applyBasiceInfo.getVersion().intValue()) {
			LOGGER.info("当前借款[" + loanNo + "]有可能被修改,请重新办理!");
			return new Result<String>(Type.FAILURE,"该笔借款有可能被修改,请重新办理!");
		}

		// 1--组装审核人员操作记录对象VO
		stopWatch.start("组装审核人员操作记录对象VO");
		applyVo.setLoanNo(loanNo);
		applyVo.setName(applyBasiceInfo.getName());
		applyVo.setIdNo(applyBasiceInfo.getIdNo());
		applyVo.setIp(WebUtils.retrieveClientIp(req));
		applyVo.setVersion(applyBasiceInfo.getVersion().intValue());
		if (AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL.equals(operateType)) {
			// 如果是已分派申请件正常操作要记录,已分派申请件批量操作和未分派申请批量操作不记录，因为操作人不是终审员本人或者没有终审员/协审员
			if (StringUtils.isNotEmpty(applyBasiceInfo.getApprovalPersonCode())) {
				applyVo.setApprovalPerson(applyBasiceInfo.getApprovalPersonCode());
			} else {
				applyVo.setFinalPerson(ShiroUtils.getCurrentUser().getUsercode());
				//满足报表系统要求，只记录终审人员层级（协审不记录);改派，派单，申请件维护，批量退回和拒绝等不是这个单子的终审人的审核操作，也不记录
				StaffOrderTask task = getStaffOrderTask(ShiroUtils.getCurrentUser().getUsercode(), null);
				applyVo.setFinalRole(null == task ? null : task.getFinalAuditLevel());
			}
			
			// 如果是已分派申请件操要记录这三个，未分派申请件的不记录
			applyVo.setApprovalLeader(pmsApiServiceImpl.findLeaderByUserCodeAndRole(ShiroUtils.getAccount(), RoleEnum.FINAL_CHECK_GROUP_LEADER, OrganizationEnum.OrgCode.FINAL_CHECK));
			applyVo.setApprovalDirector(pmsApiServiceImpl.findLeaderByUserCodeAndRole(ShiroUtils.getAccount(), RoleEnum.FINAL_CHECK_DIRECTOR, OrganizationEnum.OrgCode.FINAL_CHECK));
			applyVo.setApprovalManager(pmsApiServiceImpl.findLeaderByUserCodeAndRole(ShiroUtils.getAccount(), RoleEnum.FINAL_CHECK_MANAGER, OrganizationEnum.OrgCode.FINAL_CHECK));
		}
		applyVo.setProName(getFinalNodeName(applyVo.getRtfNodeState()));
		applyVo.setProNum(loanNo);
		stopWatch.stop();

		// 2--转换为审核人员操作记录对象实体
		ApplyHistory apply = BeanUtil.copyProperties(applyVo, ApplyHistory.class);
		String str = "";
		if (StringUtil.isNotEmpty(applyVo.getFirstReason())) {
			str = applyVo.getFirstReason();
			if (StringUtil.isNotEmpty(applyVo.getSecondReason())) {
				str = str + "-" + applyVo.getSecondReason();
			}
		}
		apply.setRefuseCode(str);

		// 3--进行更新数据库操作
		stopWatch.start("更新数据库操作");
		if (AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL.equals(operateType) || AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_BATCH.equals(operateType)) {
			// 如果是已分派申请件操作
			String finalPersonCode = "";
			StaffOrderTask sotOld = null;
			if(AmsConstants.FINAL_APPROVE_TYPE_DISPATCHED_NORMAL.equals(operateType)) {
				finalPersonCode = ShiroUtils.getCurrentUser().getUsercode();
				sotOld = getStaffOrderTask(finalPersonCode, EnumUtils.DisplayEnum.ENABLE.getValue());
			}else {
				finalPersonCode = applyVo.getHandleCode();
				sotOld = getStaffOrderTask(finalPersonCode, null);//已分派单子的批量拒绝或者批量退回时，不要求分派给的终审员状态正常
			}
			if (null == sotOld) {
				LOGGER.info("当前操作员{}队列状态异常!", ShiroUtils.getCurrentUser().getUsercode());
				return new Result<String>(Type.FAILURE,"当前操作员状态异常!");
			}
			operateNormal(applyVo, apply, req, sotOld);

		} else {
			// 如果是未分派申请件操作
			operateBatch(applyVo, apply, req);

		}
		stopWatch.stop();

		LOGGER.info(stopWatch.prettyPrint());
		
		return new Result<String>(Type.SUCCESS);
	}

	/**
	 * 批量退回或者批量拒绝的更新数据库操作
	 * 
	 * @param applyVo
	 * @param apply
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年7月11日 下午2:02:37
	 */
	private void operateBatch(ApplyHistoryVO applyVo, ApplyHistory apply, HttpServletRequest req) {
		// 添加审核操作记录信息
		if (1 != applyHistoryMapper.save(apply)) {
			throw new BusinessException("------------------- 申请件：" + applyVo.getLoanNo() + "添加审核操作记录出错!");
		}
		
		// 修改工作流节点流转
		if (!updateWorkflow(applyVo, null)) {
		    throw new BusinessException("------------------- 申请件：" + applyVo.getLoanNo() + "终审办理修改流程节点失败!");
		}

		// 终审办理更新借款系统状态
		if (!updateZsLoanNoState(applyVo, req)) {
			throw new BusinessException("------------------- 申请件：" + applyVo.getLoanNo() + "终审办理修改状态失败!");
		}
	}

	/**
	 * 正常终审操作
	 * 
	 * @param applyVo	审批历史VO
	 * @param apply		审批历史
	 * @param req		http请求
	 * @param staffOrderTask 员工接单队列信息
	 * @return
	 * @author JiaCX
	 * @date 2017年7月11日 下午1:56:56
	 */
	private void operateNormal(ApplyHistoryVO applyVo, ApplyHistory apply, HttpServletRequest req, StaffOrderTask staffOrderTask) {
		BigDecimal accLmt = new BigDecimal(0);

		//如果是提交操作  a--如果终审员权限内最大审批金额大于等于当前审批金额，判断是否走协审 
		//				b--否则，终审回到终审
		if (EnumConstants.RtfNodeState.XSZSPASS.getValue().equals(applyVo.getRtfNodeState())) {
			accLmt = getCurrentApprovalOption(applyVo.getLoanNo()).getApprovalLimit();
			LOGGER.info("审批人{}对申请件{}的当前审批金额为{}", ShiroUtils.getCurrentUser().getUsercode(), applyVo.getLoanNo(), accLmt);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("paramKey", staffOrderTask.getFinalAuditLevel());
			params.put("paramType", "FinalAuditLevel");
			SysParamDefine sysParam = sysParamDefineMapper.findOne(params);
			
			BigDecimal upLimit = new BigDecimal(sysParam.getParamValue().substring(sysParam.getParamValue().indexOf("-") + 1));
			if (upLimit.compareTo(accLmt) >= 0) {
				// 查询协审额度
				params.clear();
				params.put("paramType", "ApprovalLimit");
				String assistLimit = sysParamDefineMapper.findOne(params).getParamValue();

				LOGGER.info("协审审批额度为:{}", assistLimit);

				// 如果没有走过协审，而且审批金额大于协审金额,那么就走协审
				if (!ifAssisted(applyVo.getLoanNo()) && accLmt.compareTo(new BigDecimal(assistLimit)) > 0) {
					apply.setRtfNodeState(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());
					applyVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());
				}
			} else {
				// 当前终审员可审批额度低于审批金额，终审回到终审
				apply.setRtfNodeState(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());
				applyVo.setRtfNodeState(EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue());
			}
		}
		updateFinalOperate(applyVo, apply, req, staffOrderTask, accLmt);
	}

	/**
	 * 判断是否走过协审
	 *
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月14日 下午2:19:11
	 */
	private boolean ifAssisted(String loanNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", loanNo);
		map.put("rtfNodeState", EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue());
		List<ApplyHistory> applyHistory = applyHistoryMapper.findAll(map);
		return CollectionUtils.isEmpty(applyHistory) ? false : true;
	}

	/**
	 * 获取最后一次审批意见
	 *
	 * @param loanNo
	 * @param rtfNodeState 某个节点的（可为空）
	 * @param approvalPerson 某个审核人的（可为空）
	 * @author JiaCX
	 * @return
	 */
	private ApprovalHistory getLastApproveOpinion(String loanNo, String rtfNodeState, String approvalPerson) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanNo", loanNo);

		if (StringUtil.isNotEmpty(rtfNodeState)) {
			params.put("rtfNodeState", rtfNodeState);
		}

		if (StringUtil.isNotEmpty(approvalPerson)) {
			params.put("approvalPerson", approvalPerson);
		}

		return approvalHistoryMapper.findOne(params);
	}

	/**
	 * 终审操作更新数据库
	 *
	 * @param applyVo 	审批信息
	 * @param apply 	审批历史信息
	 * @param req 		申请件节点状态
	 * @param req 		httpServletRequest
	 * @param sotOld 	员工队列信息
	 * @param accLmt 	审批金额
	 * @return
	 * @author JiaCX
	 * @date 2017年6月14日 下午4:27:02
	 */
	private void updateFinalOperate(ApplyHistoryVO applyVo, ApplyHistory apply, HttpServletRequest req, StaffOrderTask sotOld, BigDecimal accLmt) {
		// 添加审核操作记录信息
		if (1 != applyHistoryMapper.save(apply)) {
			throw new BusinessException("申请件：" + applyVo.getLoanNo() + "添加审核操作记录出错!");
		}

		// 修改队列数
		String taskType = applyVo.getTaskType();
		if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {	// 如果是挂起队列
			// 更新挂起队列 +1
			staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.INACTIVITY);
			if ("2".equals(taskType) ) {
				// 正常队列-1
				staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.ACTIVITY);
			} else if ("1".equals(taskType) && sotOld.getCurrPriorityNum() > 0) {
				// 优先队列-1
				staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.PRIORITY);
			}
		} else {
			if ("3".equals(taskType) && sotOld.getCurrInactiveTaskNum() > 0) {
				// 挂起队列-1
				staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.INACTIVITY);
			} else if ("2".equals(taskType) && sotOld.getCurrActivieTaskNum() > 0) {
				// 正常队列-1
				staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.ACTIVITY);
			} else if ("1".equals(taskType) && sotOld.getCurrPriorityNum() > 0) {
				// 优先队列-1
				staffOrderTaskService.updateStaffTaskNum(sotOld.getStaffCode(), EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.PRIORITY);
			}
		}

		// 终审更新当前审批意见
		if (!updateApprovalHistory(applyVo)) {
			throw new BusinessException("申请件：" + applyVo.getLoanNo() + "更新当前审批意见异常!");
		}
		
		// 修改工作流节点流转
		if (!updateWorkflow(applyVo, accLmt)) {
		    throw new BusinessException("申请件：" + applyVo.getLoanNo() + "终审办理修改流程节点失败!");
		}
		
		// 终审办理更新借款系统状态
		if (!updateZsLoanNoState(applyVo, req)) {
			throw new BusinessException("申请件：" + applyVo.getLoanNo() + "终审办理修改状态失败!");
		}
		
	}

	/**
	 * 修改流程节点
	 * 
	 */
	private boolean updateWorkflow(ApplyHistoryVO applyVo, BigDecimal accLmt) {
		boolean action = true;
		try {
			String loanNo = applyVo.getLoanNo();
			if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
				// 终审挂起，不操作工作流，只改新借款系统状态
				// taskService.suspend(getTaskId(loanNo))
			} else if (EnumConstants.RtfNodeState.XSZSPASS.getValue().equals(applyVo.getRtfNodeState())) {
				// 终审提交
			    taskService.submit(getTaskId(loanNo), accLmt);
			} else if (EnumConstants.RtfNodeState.XSZSRETURN.getValue().equals(applyVo.getRtfNodeState())) {
				// 退回门店
				taskService.submitPath(getTaskId(loanNo), AmsConstants.TO_FIRST_START);
			} else if (EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue().equals(applyVo.getRtfNodeState())) {
				// 退回前前
				taskService.submitPath(getTaskId(loanNo), AmsConstants.TO_FIRST_START);
			} else if (EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(applyVo.getRtfNodeState())) {
				// 退回初审
				taskService.submitPath(getTaskId(loanNo), AmsConstants.TO_FIRST_START);
			} else if (EnumConstants.RtfNodeState.XSZSREJECT.getValue().equals(applyVo.getRtfNodeState())) {
				// 拒绝
			    taskService.deny(getTaskId(loanNo));
			} else if (EnumConstants.RtfNodeState.XSZSASSIGN.getValue().equals(applyVo.getRtfNodeState())) {
				// 改派
			    taskService.changeTask(getTaskId(loanNo), applyVo.getFinalPerson());
			} else if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(applyVo.getRtfNodeState())) {
				// 提交协审
			    taskService.submit(getTaskId(loanNo), accLmt);
			} else if (EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue().equals(applyVo.getRtfNodeState())) {
				// 提交高审
			    taskService.changeTask(getTaskId(loanNo), AmsConstants.FINAL_VIRTUAL_PERSON);
			} else if (EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue().equals(applyVo.getRtfNodeState())) {
				// 终审回到终审
			    taskService.changeTask(getTaskId(loanNo), AmsConstants.FINAL_VIRTUAL_PERSON);
			} else if (EnumConstants.RtfNodeState.ZSFPREJECT.getValue().equals(applyVo.getRtfNodeState())) {
				// 终审分派(改派)拒绝
				taskService.deny(getTaskId(loanNo));
			}
		} catch (Exception e) {
			LOGGER.error("终审修改工作流流程节点状态发生异常:{}", applyVo.getLoanNo(), e);
			action = false;
		}

		return action;
	}
	
	/**
	 * 根据借款编号，查询对应的任务号
	 * 
	 * @param loanNo
	 *            借款编号
	 * @return
	 */
	private Long getTaskId(String loanNo) {
		Task task = taskMapper.findByBusinessId(loanNo);
		if (null == task) {
			LOGGER.info("终审未查到对应任务号，借款单号:{}", loanNo);
			throw new BusinessException("终审未查到对应任务号!");
		}
		return task.getId();
	}

	/**
	 * 终审更新当前审批意见
	 */
	private boolean updateApprovalHistory(ApplyHistoryVO applyVo) {
		boolean flag = true;
		// 更新当前审批意见
		if (!EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(applyVo.getRtfNodeState())) {
			ApprovalHistory ah = getCurrentApprovalOption(applyVo.getLoanNo(), applyVo.getHandleCode());
			if (null != ah) {
				ah.setRtfNodeState(applyVo.getRtfNodeState());
				ah.setLastModifiedBy(ShiroUtils.getCurrentUser().getUsercode());
				ah.setLastModifiedDate(new Date());
				int update = approvalHistoryMapper.update(ah);
				if (1 != update) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 终审办理更新借款系统状态
	 * 
	 */
	private boolean updateZsLoanNoState(ApplyHistoryVO applyVo, HttpServletRequest req) {
		ReqZsUpdVO request = new ReqZsUpdVO();
		request.setLoanNo(applyVo.getLoanNo());
		// 终审优先件标识维护
		if (EnumConstants.RtfNodeState.XSZSRETURN.getValue().equals(applyVo.getRtfNodeState()) 
				|| EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue().equals(applyVo.getRtfNodeState())
				|| EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(applyVo.getRtfNodeState())) {
			request.setZsIfNewLoanNo(EnumConstants.ifNewLoanNo.NOLOANNO.getValue());
			request.setIfNewLoanNo(EnumConstants.ifNewLoanNo.NOLOANNO.getValue());
		}
		request.setRtfStatus(applyVo.getRtfState());
		request.setRtfNodeStatus(applyVo.getRtfNodeState());
		request.setZsPersonCode(applyVo.getFinalPerson());
		request.setApppovalPersonCode(applyVo.getApprovalPerson());
		request.setFirstLevelReasonCode(applyVo.getFirstReason());
		request.setTwoLevelReasonCode(applyVo.getSecondReason());
		request.setFirstLevelReasons(applyVo.getFirstReasonText());
		request.setTwoLevelReasons(applyVo.getSecondReasonText());
		request.setRemark(applyVo.getRemark());
		request.setOperatorCode(ShiroUtils.getCurrentUser().getUsercode());
		request.setAllotDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
		request.setVersion(applyVo.getVersion());
		request.setSysCode(sysCode);
		request.setReturnReasons(applyVo.getReturnReasons());
		request.setOperatorIp(WebUtils.retrieveClientIp(req));

		// 根据更新的节点状态，调用不同的接口
		return updateFinalApprovalStatus(request);
	}

	/**
	 * 根据节点状态不同，调用借款系统不同的接口
	 * 
	 * @param request
	 * @return
	 * @author JiaCX
	 * @date 2017年5月11日 下午2:40:30
	 */
	private boolean updateFinalApprovalStatus(ReqZsUpdVO request) {
		ResBMSFinalAduitUpdVO response = new ResBMSFinalAduitUpdVO();
		if (Strings.isEqualsEvenOnce(request.getRtfNodeStatus(), EnumConstants.RtfNodeState.XSZSHANGUP.getValue())) {
			response = finalApproveExecuter.hungUp(request);// 挂起
		} else if (Strings.isEqualsEvenOnce(request.getRtfNodeStatus(), EnumConstants.RtfNodeState.XSZSPASS.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue())) {
			response = finalApproveExecuter.submit(request);// 提交或者提交高审
		} else if (Strings.isEqualsEvenOnce(request.getRtfNodeStatus(), EnumConstants.RtfNodeState.XSZSRETURN.getValue(), EnumConstants.RtfNodeState.XSZSRTNCS.getValue(), EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue())) {
			if (EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue().equals(request.getRtfNodeStatus())){// 前前去掉原因用前前批量退回原因
				request.setFirstLevelReasonCode(null);
				request.setFirstLevelReasons(null);
				request.setTwoLevelReasonCode(null);
				request.setTwoLevelReasons(null);
			}
			response = finalApproveExecuter.back(request);// 退回门店或者退回初审或者退回前前
		} else if (Strings.isEqualsEvenOnce(request.getRtfNodeStatus(), EnumConstants.RtfNodeState.XSZSREJECT.getValue(),EnumConstants.RtfNodeState.ZSFPREJECT.getValue())) {
			response = finalApproveExecuter.reject(request);// 拒绝
		}
		LOGGER.info("终审更新借款系统状态request:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
		if (null != response && response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 保存审批意见
	 * 
	 * @author Shipf
	 * @date
	 * @param saveVo
	 * @return
	 */
	@Override
	public boolean saveOrUpdateApprovalHistory(ApprovalSaveVO saveVo, HttpServletRequest request) {
	    LOGGER.info("借款编号：{} 终审保存审批意见：{}", saveVo.getLoanNo(), JSONObject.toJSONString(saveVo));
		StopWatch stopWatch = new StopWatch();
		boolean flag = false;
		ApprovalHistory ah = new ApprovalHistory();
		ah = BeanUtil.copyProperties(saveVo, ApprovalHistory.class);
		ah.setRtfState(EnumConstants.RtfState.XSZS.getValue());
		// 设置状态，标记当前审批意见
		ah.setRtfNodeState(AmsConstants.FINAL_CURRENT_OPINION_STATE);

		stopWatch.start("根据用户查询所在大组小组");
		// 增加大组小组字段值（终审现在就没有大组小组，就不保存了）
//		Result<ResGroupVO> groupVOResult = new Result<ResGroupVO>(Result.Type.FAILURE);
//		ReqParamVO reqParamVO = new ReqParamVO();
//		reqParamVO.setLoginUser(ShiroUtils.getAccount());
//		reqParamVO.setOrgTypeCode(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
//		groupVOResult = pmsApiServiceImpl.getGroupInfoByAccount(reqParamVO);
//		if (null != groupVOResult && null != groupVOResult.getData()) {
//			ResGroupVO groupVo = groupVOResult.getData();
//			if (groupVo.getBigGroupId() != null) {
//				ah.setLargeGroup(String.valueOf(groupVo.getBigGroupId()));// 保存大组ID
//			}
//			if (groupVo.getGroupId() != null) {
//				ah.setCurrentGroup(String.valueOf(groupVo.getGroupId()));// 保存小组ID
//			}
//			if (groupVo.getRoleCodes() != null) {
//				ah.setCurrentRole(groupVo.getRoleCodes());// 保存该用户的角色列表code
//			}
//		}
		ah.setCurrentRole(StringUtils.join(ShiroUtils.getCurrentUser().getRoleCodes(),","));
		stopWatch.stop();

		// 查询当前审批意见
		stopWatch.start("查询当前审批意见");
		ApprovalHistory currApproval = getCurrentApprovalOption(saveVo.getLoanNo());
		stopWatch.stop();

		stopWatch.start("更新审批意见");
		if (null != currApproval) {
			ah.setId(currApproval.getId());
			if (1 == approvalHistoryMapper.update(ah)) {
				flag = true;
			}
		} else {
			if (1 == approvalHistoryMapper.save(ah)) {
				flag = true;
			}
		}
		stopWatch.stop();

		stopWatch.start("修改产品信息");
		// 修改产品信息
		if (flag) {
			updateProductInfo(saveVo, request);
		}
		stopWatch.stop();
		LOGGER.info(stopWatch.prettyPrint());
		return flag;
	}

	/**
	 * 调用借款系统更新产品信息
	 * 
	 * @param approvalSaveVO
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年6月12日 下午4:50:38
	 */
	public void updateProductInfo(ApprovalSaveVO approvalSaveVO, HttpServletRequest req) {
		ReqBMProductUpdVo request = new ReqBMProductUpdVo();
		request.setLoanNo(approvalSaveVO.getLoanNo());
		request.setSysCheckLmt(approvalSaveVO.getApprovalCheckIncome().toString());
		request.setOperatorCode(approvalSaveVO.getApprovalPerson());
		request.setOperatorIP(WebUtils.retrieveClientIp(req));
		request.setProductCd(approvalSaveVO.getApprovalProductCd());
		request.setReqFlag(EnumConstants.ReqFlag.ZS.getValue());
		request.setSysCode(sysCode);
		request.setVersion(Integer.parseInt(approvalSaveVO.getVersion().toString()));
		request.setAccLmt(approvalSaveVO.getApprovalLimit().toString());
		request.setProductCd(approvalSaveVO.getApprovalProductCd());
		request.setAccDate(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
		request.setAccTerm(approvalSaveVO.getApprovalTerm());
		// 收入证明金额可能为空
		if (null != approvalSaveVO.getIncomeCertificate() && approvalSaveVO.getIncomeCertificate().compareTo(new BigDecimal("0")) == 1) {
			request.setAmoutIncome(approvalSaveVO.getIncomeCertificate().toString());
		}
		Response<Object> response = finalApproveExecuter.modifyApproveInfo(request);
		LOGGER.info("借款单号：{} 终审保存审批意见时,修改产品相关信息  params:{} result:{}", approvalSaveVO.getLoanNo(), JSON.toJSONString(request), JSON.toJSON(response));
		if (null == response || !response.isSuccess()) {
			throw new BusinessException("更新产品信息失败");
		}
	}

	/**
	 * 返回终审节点名称
	 * 
	 * @date
	 * @param rtfNodeState
	 * @return
	 */
	private String getFinalNodeName(String rtfNodeState) {
		StringBuffer action = new StringBuffer("信审终审");
		if (EnumConstants.RtfNodeState.XSZSASSIGN.getValue().equals(rtfNodeState)) {// 分配
			action.append("已分派");
		} else if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(rtfNodeState)) {// 挂起
			action.append("挂起");
		} else if (EnumConstants.RtfNodeState.XSZSPASS.getValue().equals(rtfNodeState)) {// 通过
			action.append("通过");
		} else if (EnumConstants.RtfNodeState.XSZSREJECT.getValue().equals(rtfNodeState)) {// 拒绝
			action.append("拒绝");
		} else if (EnumConstants.RtfNodeState.XSZSRETURN.getValue().equals(rtfNodeState)) {// 退回录入
			action.append("退回录入");
		} else if (EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(rtfNodeState)) {// 退回初审
			action.append("退回初审");
		} else if (EnumConstants.RtfNodeState.XSZSPASS.getValue().equals(rtfNodeState)) {// 通过
			action.append("通过");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue().equals(rtfNodeState)) {// 提交高审
			action.append("提交高审");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue().equals(rtfNodeState)) {// 回到终审
			action.append("回到终审");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(rtfNodeState)) {// 提交协审
			action.append("提交协审");
		} else if (EnumConstants.RtfNodeState.ZSFPREJECT.getValue().equals(rtfNodeState)) {//分派拒绝
			action.append("分派拒绝");
		} else if (EnumConstants.RtfNodeState.XSZSZDQQRETURN.getValue().equals(rtfNodeState)) {// 终审退回前前
			action.append("退回前前");
		}

		return action.toString();
	}

	@Override
	public List<StaffOrderTaskVO> approvePersonList(FinishReformHandlerParamIn finishReformHandleInputVO) {
		long a = System.currentTimeMillis();
		LOGGER.info("------------------------------------------------------终审改派获取处理人开始时间：" + a);
		/*
		 * 1、排除掉历史上对该笔借款申请操作过【提交】、【提交高审】的终审员； 
		 * 2、≥当前借款的申请件层级；
		 * 3、≥历史上操作过【提交】、【提交高审】、【退回】的终审员所在层级；
		 * 4、如果当前借款申请已分派，改派时，还需要满足大于等于当前处理人所在层级
		 */

		String[] handlerArr = finishReformHandleInputVO.getHandlerArr();// 这个列表是要排除在外的
		String[] approvalAmountArr = finishReformHandleInputVO.getApprovalAmountArr();
		String[] loanNoArr = finishReformHandleInputVO.getLoanNoArr();
		String maxLevel = "L1";// 设置最高申请件层级

		// 查询历史上曾经对这些单子审批通过的终审员列表(这个列表是要排除在外的)
		List<String> approvePassedPersonList = applyHistoryMapper.findApprovePassedPersonByLoanNos(loanNoArr);
		LOGGER.info("1---历史上曾经对这些单子审批通过的终审员有:" + approvePassedPersonList.toString());

		// 当前借款的申请件层级，取其中最大值
		List<SysParamDefine> finalLevelList = commonParamService.findByParamType("FinalAuditLevel");
		maxLevel = convertAppLevel(finalLevelList, getBiggest(approvalAmountArr));
		LOGGER.info("2---当前借款的申请件层级中最大值是:" + maxLevel);

		// 历史上对这些单子审批过的终审员所在层级，取其中最大值
		String level = applyHistoryMapper.findMaxLevelOfApprovedPersonByLoanNos(loanNoArr);
		if (StringUtils.isNotEmpty(level) && level.compareTo(maxLevel) > 0) {
			maxLevel = level;
		}
		LOGGER.info("3---历史上对这些单子审批（提交，提交高审，退回）过的终审员中层级最大值是:" + maxLevel);

		// 如果有已分派的单子（就是当前处理人不为空），再加一个条件---大于等于当前处理人级别
		if (null != handlerArr && handlerArr.length > 0) {
			String handlerLevel = staffOrderTaskMapper.findMaxLevelByStaffCodes(handlerArr);
			LOGGER.info("4---有已分派的单子，这些当前处理人中层级最大值是:" + handlerLevel);
			if (StringUtils.isNotEmpty(handlerLevel) && handlerLevel.compareTo(maxLevel) > 0) {
				maxLevel = handlerLevel;
			}
		}
		LOGGER.info("最终选定的终审员层级是:" + maxLevel);

		List<StaffOrderTask> sotList = staffOrderTaskMapper.findStaffCodesByLevel(maxLevel);

		Map<String, String> map = new HashMap<String, String>();
		for (String handler : handlerArr) {
			map.put(handler, handler);
		}
		for (String handler : approvePassedPersonList) {
			map.put(handler, handler);
		}

		ReqParamVO req = new ReqParamVO();
		req.setSysCode(EnumConstants.AMS_SYSTEM_CODE);
        req.setUsercode(ShiroUtils.getCurrentUser().getUsercode());
        req.setRoleCode(RoleEnum.FINAL_CHECK.getCode());
        req.setStatus(0);
        req.setLoginUser(ShiroUtils.getCurrentUser().getUsercode());
		List<ResEmployeeVO> finalGroup = pmsApiServiceImpl.getEmpsByAccount(req);// 当前登录用户的数据权限内终审员列表

		List<StaffOrderTaskVO> returnList = new ArrayList<StaffOrderTaskVO>();
		
		if (CollectionUtils.isNotEmpty(finalGroup)) {
		    for (ResEmployeeVO emp : finalGroup) {
        		for (StaffOrderTask staff : sotList) {
        			if (!map.containsKey(staff.getStaffCode()) && emp.getUsercode().equals(staff.getStaffCode())) {
						StaffOrderTaskVO sot = new StaffOrderTaskVO();
						sot = BeanUtil.copyProperties(staff, StaffOrderTaskVO.class);
						sot.setName(emp.getName());
						returnList.add(sot);
					}
				}
			}
		}
		long b = System.currentTimeMillis();
		LOGGER.info("------------------------------------------------------终审改派获取处理人结束时间：" + b);
		LOGGER.info("------------------------------------------------------终审改派获取处理人用时：" + (b - a) + "ms");
		return returnList;
	}
	
	private String getBiggest(String[] arr) {
		if (null != arr && arr.length > 0) {
			BigDecimal max = new BigDecimal(arr[0]);
			for (String str : arr) {
				if (new BigDecimal(str).compareTo(max) > 0) {
					max = new BigDecimal(str);
				}
			}
			return max.toString();
		}
		return null;
	}

	@Override
	public ResponsePage<ZsReassignmentVO> getFinishReformList(FinishReformListParamIn req, RequestPage requestPage) {
		ReqBMSReassignmentVo request = BeanUtil.copyProperties(req, ReqBMSReassignmentVo.class);
		ResponsePage<ZsReassignmentVO> page = new ResponsePage<ZsReassignmentVO>();
		List<SysParamDefine> finalLevelList = commonParamService.findByParamType("FinalAuditLevel");
		if (StringUtils.isNotEmpty(req.getLoanNoTopClass())) {
			String[] levels = req.getLoanNoTopClass().split(",");
			String[] accLmt = new String[levels.length];
			for (int i = 0; i < levels.length; i++) {
				OK: for (SysParamDefine param : finalLevelList) {
					if (levels[i].equals(param.getParamKey())) {
						accLmt[i] = param.getParamValue();
						break OK;
					}
				}
			}
			request.setAccLmt(accLmt);
		}

		String startDate = request.getXsStartDate();
		String endDate = request.getXsEndDate();
		if (!StringUtils.isEmpty(startDate)) {
			startDate = startDate + " 00:00:00";
		}
		if (!StringUtils.isEmpty(endDate)) {
			endDate = endDate + " 23:59:59";
		}
		request.setXsStartDate(startDate);
		request.setXsEndDate(endDate);
		String caseType = request.getCaseType();
		if (StringUtils.isNotEmpty(caseType)) {
			List<String> type = Arrays.asList(caseType.split(","));
			request.setCaseIdentifyList(type);
			request.setCaseType(null);
		}
		PageResponse<ResBMSReassignmentVo> response = bmsLoanInfoService.getFinishReformList(request, requestPage);
		if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
		    LOGGER.info("终审改派工作台查询队列信息 params:{} 返回数据有:{}条", JSON.toJSONString(request), response.getTotalCount());
			List<ResBMSReassignmentVo> returnlist = response.getRecords();
			if(CollectionUtils.isEmpty(returnlist)) {
				return page;
			}
			List<ZsReassignmentVO> listvo = new ArrayList<ZsReassignmentVO>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rtfState", EnumConstants.RtfState.XSCS.getValue());
			map.put("rtfNodeStateArray", new String[] { EnumConstants.RtfNodeState.XSCSPASS.getValue(), EnumConstants.RtfNodeState.HIGHPASS.getValue() });
			map.put("loanNos", getReformLoanNos(returnlist));
			long a = System.currentTimeMillis();
			LOGGER.info("------------------------------------------------------终审改派ams查询开始时间：" + a);
			List<ApprovalHistory> hisList = approvalHistoryMapper.findLastPassedOperateList(map);
			long b = System.currentTimeMillis();
			LOGGER.info("------------------------------------------------------终审改派ams查询结束时间：" + b);
			LOGGER.info("------------------------------------------------------终审改派ams查询用时：" + (b - a) + "ms");
			for (ResBMSReassignmentVo obj : returnlist) {
				ZsReassignmentVO vo = new ZsReassignmentVO();
				vo = BeanUtil.copyProperties(obj, ZsReassignmentVO.class);
				for (ApprovalHistory approvalHistory : hisList) {
					if (obj.getLoanNo().equals(approvalHistory.getLoanNo())) {
						vo.setLastCsApprovalAmount(approvalHistory.getApprovalLimit().toString());
					}
				}
				vo.setLoanNoTopClass(convertAppLevel(finalLevelList, vo.getAccLmt()));
				// TODO 临时解决借新还旧客户标识 故取消身份隐藏
				/*vo.setCustomerIDNO("*" + obj.getCustomerIDNO().substring(14));*/
				listvo.add(vo);
			}
			page.setRows(listvo);
			page.setTotal(response.getTotalCount());
		}
		return page;
	}

	private List<String> getReformLoanNos(List<ResBMSReassignmentVo> list) {
		List<String> returnlist = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (ResBMSReassignmentVo vo : list) {
				returnlist.add(vo.getLoanNo());
			}
		}
		return returnlist;
	}

	private List<String> getCompletedLoanNos(List<ResOffTheStocksAuditVO> list) {
		List<String> returnlist = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (ResOffTheStocksAuditVO vo : list) {
				returnlist.add(vo.getLoanNo());
			}
		}
		return returnlist;
	}

	/**
	 * 根据金额判断属于哪个申请件层级
	 * 
	 * @param finalLevelList
	 * @param accLmt
	 * @return
	 * @author JiaCX
	 * @date 2017年6月26日 下午7:35:43
	 */
	private String convertAppLevel(List<SysParamDefine> finalLevelList, String accLmt) {
		// 判断审批金额所属终审层级
		SysParamDefine properLevel = null;
		BigDecimal accLimit = new BigDecimal(accLmt);
		for (SysParamDefine sysParamDefine : finalLevelList) {
			String paramValue = sysParamDefine.getParamValue();
			String[] auditLimits = StringUtils.split(paramValue, "-");
			BigDecimal minAuditLimit = new BigDecimal(auditLimits[0]);
			BigDecimal maxAuditLimit = new BigDecimal(auditLimits[1]);
			if (accLimit.compareTo(minAuditLimit) > 0 && accLimit.compareTo(maxAuditLimit) <= 0) { // 左开右闭
				LOGGER.info("审批金额 [ " + accLimit + " ] 所属层级{}", sysParamDefine.getParamKey());
				properLevel = sysParamDefine;
				break;
			}
		}
		return properLevel == null ? "" : properLevel.getParamKey();
	}

	@Override
	public boolean finishReform(ReformVO reformVO, HttpServletRequest request) throws BusinessException {
		// 先做ams数据更新
		finishReformUpdateQueen(reformVO);

		// 如果ams更新成功再调用接口更新数据
		ReqBMSReassignmentUpdVo reqBMSReassignmentUpdVo = new ReqBMSReassignmentUpdVo();
		List<ReqBMSLoansAndVersionsVO> list = new ArrayList<ReqBMSLoansAndVersionsVO>();

		ReqBMSLoansAndVersionsVO reqBMSLoansAndVersionsVO = new ReqBMSLoansAndVersionsVO();
		reqBMSLoansAndVersionsVO.setLoanNo(reformVO.getLoanNo());
		reqBMSLoansAndVersionsVO.setVersion(reformVO.getVersion());
		reqBMSLoansAndVersionsVO.setRtfNodeStatus(EnumConstants.RtfNodeState.XSZSASSIGN.getValue());
		reqBMSLoansAndVersionsVO.setOldAuditPersonCode(reformVO.getUserCode());
		list.add(reqBMSLoansAndVersionsVO);

		reqBMSReassignmentUpdVo.setAuditPersonCode(reformVO.getTargetUserCode());
		reqBMSReassignmentUpdVo.setReqFlag(EnumConstants.ReqFlag.ZS.getValue());
		reqBMSReassignmentUpdVo.setSysCode(sysCode);
		reqBMSReassignmentUpdVo.setList(list);
		reqBMSReassignmentUpdVo.setOperatorCode(ShiroUtils.getCurrentUser().getUsercode());
		reqBMSReassignmentUpdVo.setOperatorIP(WebUtils.retrieveClientIp(request));

		Response<ResBMSPlReassignMentUpdVo> response = finalDispatchExecuter.batchReassignment(reqBMSReassignmentUpdVo);
		LOGGER.info("终审改派更新 params:{} result:{}", JSON.toJSONString(reqBMSReassignmentUpdVo), JSON.toJSONString(response));
		if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData().getResList()) && response.getData().getResList().get(0).getStatus()) {
			return true;
		} else {
			// 抛出异常，把上一步的ams数据更新回滚
			throw new BusinessException("借款更新改派失败.");
		}
	}

	/**
	 * 终审改派更新队列数
	 * 
	 * @param reformVO
	 * @return
	 * @author JiaCX
	 * @date 2017年7月3日 下午5:15:14
	 */
	private void finishReformUpdateQueen(ReformVO reformVO) {
	    LOGGER.info("借款编号:{}，信审终审改派，参数为：{}", reformVO.getLoanNo(), JSONObject.toJSONString(reformVO));
        if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(reformVO.getRtfNodeState())) {
            // 如果是终审挂起队列，当前申请件一定是有处理人的，修改被改派人队列数
            staffOrderTaskService.updateStaffTaskNum(reformVO.getUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.INACTIVITY);

            // 修改改派人队列数
            if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(reformVO.getzSIfNewLoanNo())) {
                // 正常队列
                staffOrderTaskService.updateStaffTaskNum(reformVO.getTargetUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.ACTIVITY);
            } else {
                // 优先队列
                staffOrderTaskService.updateStaffTaskNum(reformVO.getTargetUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.PRIORITY);
            }
        } else {
            // 正常队列 ||优先队列
            // 修改被改派人队列数
            if (StringUtil.isNotEmpty(reformVO.getUserCode())) {
                // 如果处理人是空的，说明是未分派的，不修改队列数；只有处理人不为空才更改队列数
                if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(reformVO.getzSIfNewLoanNo())) {
                    // 正常队列
                    staffOrderTaskService.updateStaffTaskNum(reformVO.getUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.ACTIVITY);
                } else if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(reformVO.getzSIfNewLoanNo())) {
                    // 优先队列
                    staffOrderTaskService.updateStaffTaskNum(reformVO.getUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.MINUS, EnumUtils.StaffTaskType.PRIORITY);
                }
            }

            // 修改改派人队列数
            if (EnumConstants.ifNewLoanNo.NEWLOANNO.getValue().equals(reformVO.getzSIfNewLoanNo())) {
                // 正常队列
                staffOrderTaskService.updateStaffTaskNum(reformVO.getTargetUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.ACTIVITY);
            } else if (EnumConstants.ifNewLoanNo.NOLOANNO.getValue().equals(reformVO.getzSIfNewLoanNo())) {
                // 优先队列
                staffOrderTaskService.updateStaffTaskNum(reformVO.getTargetUserCode(),  EnumUtils.FirstOrFinalEnum.FINAL, EnumUtils.CalType.ADD, EnumUtils.StaffTaskType.PRIORITY);
            }
        }

        // 修改工作流
        Task task = taskMapper.findByBusinessId(reformVO.getLoanNo());
        if (task != null && task.getId() != 0) {
            taskService.changeTask(task.getId(), reformVO.getTargetUserCode());
        }

        saveApplyHistory(reformVO, null);
	}

	/**
	 * 终审批量改派添加审批历史记录
	 * 
	 * @param reformVo
	 * @param sessionId
	 * @author JiaCX
	 * @date 2017年5月10日 下午5:34:45
	 */
	public void saveApplyHistory(ReformVO reformVo, String sessionId) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(sessionId, reformVo.getLoanNo(), true);
		ApplyHistory apply = new ApplyHistory();
		apply.setLoanNo(reformVo.getLoanNo());
		apply.setName(applyBasiceInfo.getName());
		apply.setIdNo(applyBasiceInfo.getIdNo());
		apply.setRtfState(EnumConstants.RtfState.XSZS.getValue());
		apply.setProNum(applyBasiceInfo.getLoanNo());// 流程实例号
		apply.setRtfNodeState(EnumConstants.RtfNodeState.XSZSASSIGN.getValue());
		if (StringUtils.isNotEmpty(reformVo.getApprovalPersonCode()) || reformVo.getRtfNodeState().equals(EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue())) {
			// 如果协审人员不为空，那么就给协审人员赋值
			apply.setApprovalPerson(reformVo.getTargetUserCode());
		} else {
			apply.setFinalPerson(reformVo.getTargetUserCode());
		}
		apply.setProName(getFirstNodeName(reformVo.getRtfNodeState()));
		if (null == reformVo.getUserCode()) {
			apply.setRemark(ShiroUtils.getAccount() + "终审改派给" + reformVo.getTargetUserCode());
		} else {
			apply.setRemark(ShiroUtils.getAccount() + "终审改派由" + reformVo.getUserCode() + "改派给" + reformVo.getTargetUserCode());
		}
		applyHistoryMapper.save(apply);
	}

	private String getFirstNodeName(String rtfNodeState) {
		StringBuffer action = new StringBuffer("信审终审");
		if (EnumConstants.RtfNodeState.XSZSASSIGN.getValue().equals(rtfNodeState)) {// 分配
			action.append("已分派");
		} else if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(rtfNodeState)) {// 挂起
			action.append("挂起");
		} else if (EnumConstants.RtfNodeState.XSZSPASS.getValue().equals(rtfNodeState)) {// 通过
			action.append("通过");
		} else if (EnumConstants.RtfNodeState.XSZSREJECT.getValue().equals(rtfNodeState)) {// 拒绝
			action.append("拒绝");
		} else if (EnumConstants.RtfNodeState.XSZSRETURN.getValue().equals(rtfNodeState)) {// 退回
			action.append("退回录入");
		} else if (EnumConstants.RtfNodeState.XSZSRTNCS.getValue().equals(rtfNodeState)) {// 退回
			action.append("退回初审");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue().equals(rtfNodeState)) {// 提交高审
			action.append("提交高审");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue().equals(rtfNodeState)) {
			action.append("回到终审");
		} else if (EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue().equals(rtfNodeState)) {
			action.append("提交协审");
		} else if (EnumConstants.RtfNodeState.XSCSSUBMIT.getValue().equals(rtfNodeState)) {
			action.append("未分派");
		}
		return action.toString();
	}

	@Override
	public void exportExcel(FinishReformListParamIn request, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ReqBMSReassignmentVo rbr = BeanUtil.copyProperties(request, ReqBMSReassignmentVo.class);
		List<SysParamDefine> finalLevelList = commonParamService.findByParamType("FinalAuditLevel");
		if (StringUtils.isNotEmpty(request.getLoanNoTopClass())) {
			String[] levels = request.getLoanNoTopClass().split(",");
			String[] accLmt = new String[levels.length];
			for (int i = 0; i < levels.length; i++) {
				OK: for (SysParamDefine param : finalLevelList) {
					if (levels[i].equals(param.getParamKey())) {
						accLmt[i] = param.getParamValue();
						break OK;
					}
				}
			}
			rbr.setAccLmt(accLmt);
		}

		String startDate = request.getXsStartDate();
		String endDate = request.getXsEndDate();
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			startDate = null;
			endDate = null;
		} else {
			startDate = startDate + " 00:00:00";
			endDate = endDate + " 23:59:59";
		}
		rbr.setXsStartDate(startDate);
		rbr.setXsEndDate(endDate);
		String caseType = request.getCaseType();
		if (StringUtils.isNotEmpty(caseType)) {
			List<String> type =Arrays.asList(caseType.split(","));
			rbr.setCaseIdentifyList(type);
			rbr.setCaseType(null);
		}
		RequestPage requestPage = new RequestPage();
		requestPage.setPage(1);
		requestPage.setRows(100000);
		PageResponse<ResBMSReassignmentVo> response = bmsLoanInfoService.getFinishReformList(rbr, requestPage);
		if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
			List<ResBMSReassignmentVo> list = response.getRecords();
			String fileName = AmsConstants.FINAL_REFORM_EXPORT_FILENAME;
			String[] headers = AmsConstants.FINAL_REFORM_EXPORT_COLUMN;
			List<ResBMSReassignmentExportVo> evolist = new ArrayList<ResBMSReassignmentExportVo>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rtfState", EnumConstants.RtfState.XSCS.getValue());
			map.put("rtfNodeStateArray", new String[] { EnumConstants.RtfNodeState.XSCSPASS.getValue(), EnumConstants.RtfNodeState.HIGHPASS.getValue() });
			map.put("loanNos", getReformLoanNos(list));
			long m = System.currentTimeMillis();
			LOGGER.info("------------------------------------------------------终审改派导出ams查询开始时间：" + m);
			List<ApprovalHistory> hisList = approvalHistoryMapper.findLastPassedOperateList(map);
			long n = System.currentTimeMillis();
			LOGGER.info("------------------------------------------------------终审改派导出ams查询结束时间：" + n);
			LOGGER.info("------------------------------------------------------终审改派导出ams查询用时：" + (n - m) + "ms");
			for (ResBMSReassignmentVo r : list) {
				ZsReassignmentVO vo = new ZsReassignmentVO();
				vo = BeanUtil.copyProperties(r, ZsReassignmentVO.class);
				for (ApprovalHistory approvalHistory : hisList) {
					if (r.getLoanNo().equals(approvalHistory.getLoanNo())) {
						vo.setLastCsApprovalAmount(approvalHistory.getApprovalLimit().toString());
					}
				}
				vo.setLoanNoTopClass(convertAppLevel(finalLevelList, vo.getAccLmt()));
				vo.setXsSubDate(DateUtils.dateToString(DateUtils.stringToDate(vo.getXsSubDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), DateUtils.FORMAT_DATE_YYYY_MM_DD));
				StringBuffer ifPriSb = new StringBuffer();
				if ("1".equals(vo.getIfPri())) {
					ifPriSb.append("加急件");
				}
				if ("1".equals(vo.getAppInputFlag())) {
					if (ifPriSb.length()>0) {
						ifPriSb.append("|APP进件");
					} else {
						ifPriSb.append("APP进件");
					}
				}
				if ("1".equals(vo.getIfSuspectCheat())) {
					if (ifPriSb.length()>0) {
						ifPriSb.append("|触发欺诈规则");
					} else {
						ifPriSb.append("触发欺诈规则");
					}
				}
				if (EnumUtils.YOrNEnum.Y.getValue().equals(vo.getIfPreferentialUser())) {
					if (ifPriSb.length() > 0) {
						ifPriSb.append("|费率优惠客户");
					}else {
						ifPriSb.append("费率优惠客户");
					}
				}
				if(EnumUtils.YOrNEnum.Y.getValue().equals(vo.getSimplifiedDataUser())) {
					if (ifPriSb.length() > 0) {
						ifPriSb.append("|简化资料客户");
					}else {
						ifPriSb.append("简化资料客户");
					}
				}
				if(EnumUtils.YOrNEnum.Y.getValue().equals(vo.getIfReconsiderUser())) {
					if (ifPriSb.length() > 0) {
						ifPriSb.append("|复议再申请客户");
					}else {
						ifPriSb.append("复议再申请客户");
					}
				}
				if (null != r.getZdqqApply() && 1 == r.getZdqqApply()) {
					if (ifPriSb.length() > 0) {
						ifPriSb.append("|证大前前");
					} else {
						ifPriSb.append("证大前前");
					}
				}
				vo.setIfPri(ifPriSb.toString());
				if (EnumConstants.RtfNodeState.XSZSHANGUP.getValue().equals(vo.getRtfNodeStatus())) {// 如果是信审终审挂起
					vo.setZsIfNewLoanNo("2");
				}
				if (StringUtils.isEmpty(vo.getHandleCode())) {
					vo.setZsIfNewLoanNo("");
				} else {
					if ("1".equals(vo.getZsIfNewLoanNo())) {
						vo.setZsIfNewLoanNo("正常队列");
					} else if ("0".equals(vo.getZsIfNewLoanNo())) {
						vo.setZsIfNewLoanNo("优先队列");
					} else if ("2".equals(vo.getZsIfNewLoanNo())) {
						vo.setZsIfNewLoanNo("挂起队列");
					}
				}
				ResBMSReassignmentExportVo exportvo = BeanUtil.copyProperties(vo, ResBMSReassignmentExportVo.class);
				evolist.add(exportvo);
			}
			ExcelUtil.exportExcel(fileName, headers, evolist, req, res, DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
		}
	}

	@Override
	public ResponsePage<ResOffTheStocksAuditVO> getCompletedTask(RequestPage requestPage, String offStartDate, String offEndDate, HttpServletRequest request) {
		ResponsePage<ResOffTheStocksAuditVO> returnpage = new ResponsePage<ResOffTheStocksAuditVO>();
		returnpage = bmsLoanInfoService.getCompletedTask(requestPage, offStartDate, offEndDate, request);
		if (returnpage.getTotal() > 0) {
			/* 当前终审员的终审操作是通过时，展示当前信审员的审批产品和审批额度 */
			List<ResOffTheStocksAuditVO> list = returnpage.getRows();
			if (CollectionUtils.isNotEmpty(list)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("approvalPerson", ShiroUtils.getCurrentUser().getUsercode());
				map.put("rtfState", EnumConstants.RtfState.XSZS.getValue());
				map.put("rtfNodeStateArray", new String[] { EnumConstants.RtfNodeState.XSZSPASS.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue() });
				map.put("loanNos", getCompletedLoanNos(list));
				long a = System.currentTimeMillis();
				LOGGER.info("------------------------------------------------------终审工作台查询已完成队列ams查询开始时间：" + a);
				List<ApprovalHistory> hisList = approvalHistoryMapper.findLastPassedOperateList(map);
				long b = System.currentTimeMillis();
				LOGGER.info("------------------------------------------------------终审工作台查询已完成队列ams查询结束时间：" + b);
				LOGGER.info("------------------------------------------------------终审工作台查询已完成队列ams查询用时：" + (b - a) + "ms");
				List<ResBMSProductVO> proList = bmsBasiceInfoService.getAllProductList();
				for (ResOffTheStocksAuditVO res : list) {
					if (Strings.isEqualsEvenOnce(res.getHistorNodeStatus(), EnumConstants.RtfNodeState.XSZSPASS.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITHIGH.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITBACK.getValue(), EnumConstants.RtfNodeState.XSZSSUBMITAPPROVAL.getValue())) {
						for (ApprovalHistory his : hisList) {
							if (res.getLoanNo().equals(his.getLoanNo())) {
								for (ResBMSProductVO p : proList) {
									if (his.getApprovalProductCd().equals(p.getCode())) {
										res.setProductName(p.getName());
									}
								}
								res.setAccLmt(his.getApprovalLimit().toString());
							}
						}
					}
				}
			}
		}
		return returnpage;
	}

	/**
	 * 偿还能力不足时进行负债率校验
	 * 
	 * @param loanNo
	 * @return
	 */
	@Override
	public Result<String> repaymentInsufficientJudge(HttpServletRequest req, String loanNo) {
		Result<String> result = new Result<String>(Type.FAILURE);
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(req.getSession().getId(), loanNo, true);
		ApprovalHistory ah = null;
		if (applyBasiceInfo.getRtfNodeState()!=null&&EnumConstants.RtfNodeState.XSZSREJECT.getValue().equals(applyBasiceInfo.getRtfNodeState())) {
			ah = getLastApproveOpinion(loanNo, EnumConstants.RtfNodeState.XSZSREJECT.getValue(), null);
		} else {
			ah = getLastApproveOpinion(loanNo, AmsConstants.FINAL_CURRENT_OPINION_STATE, null);
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
					if (approvalLimit.compareTo(new BigDecimal("10000.00")) == 0 && "12".equals(approvalTerm)) {// 超过限制,且审批额度为1万且审批期限为12期,拒绝操作
						result.setType(Type.FAILURE);
					}
					if (approvalLimit.compareTo(new BigDecimal("10000.00")) != 0 || !"12".equals(approvalTerm)) {// 额度不是1万，或者审批期限不是12期提示“填写的审批额度及审批期限不符合偿债能力不足拒绝的要求，请修改！”
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

	@Override
	public Result<String> checkAssetsInfo(Result<String> result, String loanNo, String productCd) {
		ReqBMSEnumCodeVO reqBMSEnumCodeVO = new ReqBMSEnumCodeVO();
		reqBMSEnumCodeVO.setProductCode("00001");
		reqBMSEnumCodeVO.setSysCode(sysCode);
		Response<List<ResBMSEnumCodeVO>> res = productExecuter.getAssetModule(reqBMSEnumCodeVO);
		if (null != res && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(res.getRepCode())) {
			// List<ResBMSEnumCodeVO> list = res.getData();
		}
		return null;
	}

	@Override
	public ApprovalHistory getCurrentApprovalOption(String loanNo) {
		return getLastApproveOpinion(loanNo, AmsConstants.FINAL_CURRENT_OPINION_STATE, ShiroUtils.getCurrentUser().getUsercode());
	}

	private ApprovalHistory getCurrentApprovalOption(String loanNo, String aprovelPerson) {
		return getLastApproveOpinion(loanNo, AmsConstants.FINAL_CURRENT_OPINION_STATE, aprovelPerson);
	}

	/**
	 * 查询员工队列信息
	 * @param staffCode 员工工号
	 * @param status 员工状态（0-正常；1-非正常）
	 * @return
	 */
	@Override
	public StaffOrderTask getStaffOrderTask(String staffCode, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("staffCode", staffCode);
		params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FINAL.getValue());
		if(StringUtils.isNotEmpty(status)){
		    params.put("status", status);
		}

		return staffOrderTaskService.findOneService(params);
	}

}