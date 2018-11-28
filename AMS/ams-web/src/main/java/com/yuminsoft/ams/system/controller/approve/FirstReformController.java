package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.audit.*;
import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.audit.ResReassignmentUpdVO;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.domain.system.UserLog.Type;
import com.yuminsoft.ams.system.service.approve.FirstReformService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.apply.ReformVO;
import com.yuminsoft.ams.system.vo.apply.ReturnOrRejectVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/firstReform")
public class FirstReformController extends BaseController{
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;
	@Autowired
	private FirstReformService firstReformService;
	@Autowired
	private PmsApiService pmsApiService;
	@Autowired
	private StaffAbilityService staffAbilityService;

	/**
	 * 初审改派查询
	 *
	 * @author dmz
	 * @date 2017年4月7日
	 * @param request
	 * @param requestPage
	 * @return
	 */
	@RequestMapping("/loanNoListPage")
	@ResponseBody
	public ResponsePage<ResBMSReassignmentVo> getLoanNoListPage(ReqBMSReassignmentVo request, RequestPage requestPage) {
		ResponsePage<ResBMSReassignmentVo> page = new ResponsePage<ResBMSReassignmentVo>();
		try {
			if (Strings.isNotEmpty(request.getFpStatus())) {
				request.setPageNum(requestPage.getPage());
				request.setPageSize(requestPage.getRows());
				page = bmsLoanInfoService.getLoanNoListPage(request,requestPage);
				// TODO 临时解决借新还旧客户标识 故取消身份隐藏
				/*if (null != page && null != page.getRows()) {// 身份证号码隐藏
					for (ResBMSReassignmentVo resBMSReassignmentVo : page.getRows()) {
						resBMSReassignmentVo.setCustomerIDNO(Strings.hideIdCard(resBMSReassignmentVo.getCustomerIDNO()));
					}
				}*/
			}
		} catch (Exception e) {
			LOGGER.info("初审改派查询异常:", e);
		}
		return page;
	}

	/**
	 * 根据用户查询所在的大组小组(用于批量改派根据用户判断是否可编辑大组小组)
	 *
	 * @author dmz
	 * @date 2017年4月12日
	 * @return
	 */
	@RequestMapping("/findGroupInfoByAccount")
	@ResponseBody
	public Result<ResGroupVO> findGroupInfoByAccount() {
		Result<ResGroupVO> result = new Result<ResGroupVO>(Result.Type.FAILURE);
		try {
			ReqParamVO reqParamVO = new ReqParamVO();
			reqParamVO.setLoginUser(ShiroUtils.getAccount());
			reqParamVO.setOrgTypeCode(OrganizationEnum.OrgCode.CHECK.getCode());
			result = pmsApiService.findGroupInfoByAccount(reqParamVO);
		} catch (Exception e) {
			LOGGER.info("根据用户查询所在的大组小组(用于批量改派根据用户判断是否可编辑大组小组) 异常:{}", e);
		}
		return result;
	}

	/***
	 * 根据当前用户获取大组(没有接单能力用户的大组不显示)
	 * @param taskDef 初终审标识 初审(apply-check) 终审(applyinfo-finalaudit)
	 * @param reformVoStr
	 * @author wulj
	 * @return
	 */
	@RequestMapping("/findBigGroupByAccountAndAbility")
	@ResponseBody
	public Result<ResOrganizationVO> findBigGroupByAccountAndAbility(String taskDef, String reformVoStr) {
		long a = System.currentTimeMillis();
		LOGGER.info("根据当前用户获取大组,  初终审标识:{}, 改派申请件:{}", taskDef, reformVoStr);
		Result<ResOrganizationVO> result = new Result<ResOrganizationVO>();
		try {
			List<ReformVO> reformVOList = JSONArray.parseArray(reformVoStr, ReformVO.class);
			List<ResOrganizationVO> orgList = firstReformService.getBigGroupByAccountAndAbility(taskDef, reformVOList);
			result.setType(Result.Type.SUCCESS);
			result.setDataList(orgList);
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			LOGGER.error("根据当前用户获取大组异常", e);
		}

		LOGGER.info("findBigGroupByAccountAndAbility========"+(System.currentTimeMillis()-a));
		return result;
	}

	/***
	 * 据登录用户和大组ID获取小组
	 * @param orgId 		大组ID
	 * @param taskDef 		初终审标识 初审(apply-check) 终审(applyinfo-finalaudit)
	 * @param reformVoStr
	 * @author wulj
	 * @return
	 */
	@RequestMapping("/findTeamByAccountAndOrgIdAndAbility")
	@ResponseBody
	public Result<ResOrganizationVO> findTeamByAccountAndOrgIdAndAbility(Long orgId, String taskDef, String reformVoStr) {
		long a = System.currentTimeMillis();
		LOGGER.info("据登录用户和大组ID获取小组, 大组ID:{}, 初终审标识:{}, 改派申请件:{}", orgId, taskDef, reformVoStr);
		Result<ResOrganizationVO> result = new Result<ResOrganizationVO>();
		try {
			List<ReformVO> reformVOList = JSONArray.parseArray(reformVoStr, ReformVO.class);
			List<ResOrganizationVO> orgList = firstReformService.getTeamByAccountAndOrgIdAndAbility(orgId, taskDef, reformVOList);
			result.setType(Result.Type.SUCCESS);
			result.setDataList(orgList);
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			LOGGER.error("根据当前用户和大组ID获取小组异常", e);
		}
		LOGGER.info("findTeamByAccountAndOrgIdAndAbility========"+(System.currentTimeMillis()-a));
		return result;
	}

	/**
	 * 批量改派
	 *
	 * @author dmz
	 * @date 2017年4月8日
	 * @param reformVOList
	 * @param req
	 * @return
	 */
	@RequestMapping("/updateReform")
	@ResponseBody
	@UserLogs(link = "信审初审", operation = "信审批量改派", type = UserLog.Type.初审)
	public Result<ResReassignmentUpdVO> updateReform(@RequestBody List<ReformVO> reformVOList, HttpServletRequest req) {
		Result<ResReassignmentUpdVO> result = new Result<ResReassignmentUpdVO>(Result.Type.FAILURE);
		ReqBMSReassignmentBatchVo request = new ReqBMSReassignmentBatchVo();
		request.setOperatorIP(WebUtils.retrieveClientIp(req));
		int updateSuccess = 0;
		for (ReformVO reformVo : reformVOList) {
			List<ReqBMSLoansChackVO> listChackVO = new ArrayList<ReqBMSLoansChackVO>();
			ReqBMSLoansChackVO reqBMSLoansChackVO = new ReqBMSLoansChackVO();
			reqBMSLoansChackVO.setLoanNo(reformVo.getLoanNo());
			reqBMSLoansChackVO.setOldAuditPersonCode(reformVo.getUserCode());
			reqBMSLoansChackVO.setOldAuditPersonName(reformVo.getUserCode());
			reqBMSLoansChackVO.setVersion(Long.parseLong(reformVo.getVersion()));
			listChackVO.add(reqBMSLoansChackVO);
			request.setAuditPersonCode(reformVo.getTargetUserCode());
			request.setList(listChackVO);
			request.setAuditPersonName(reformVo.getTargetuserName());
			try {// 初审批量改派接口更新
				Result<ResReassignmentUpdVO> update = firstReformService.updateFirstReformService(request, reformVo);
				if (update.success()) {
						updateSuccess++;
				}
			} catch (Exception e) {
				LOGGER.info("初审批量改派异常", e);
			}
		}
		if (updateSuccess > 0) {
			result.setType(Result.Type.SUCCESS);
		}
		result.addMessage("改派成功 " + updateSuccess + " 条,失败 " + (reformVOList.size() - updateSuccess) + " 条");
		return result;
	}

	/**
	 * 初审改派拒绝
	 * 
	 * @author dmz
	 * @date 2017年4月8日
	 * @return
	 */
	@RequestMapping("/updateReformReject")
	@ResponseBody
	@UserLogs(link = "信审初审", operation = "初审改派拒绝", type = Type.初审)
	public Result<String> updateReformReject(@RequestBody ReturnOrRejectVO returnOrRejectVO, HttpServletRequest req) {
		Result<String> result = new Result<String>(Result.Type.FAILURE);
		int updateSuccess = 0;
		for (ReformVO rf : returnOrRejectVO.getReformVOList()) {
			try {
				ReqCsRefusePlupdateStatusVO request = new ReqCsRefusePlupdateStatusVO();// 初审改派拒绝接口拆分

				ReqCsRefuseUpdStatusVO rrsv = new ReqCsRefuseUpdStatusVO();
				rrsv.setLoanNo(rf.getLoanNo());
				rrsv.setVersion(Long.parseLong(rf.getVersion()));

				List<ReqCsRefuseUpdStatusVO> list = new ArrayList<ReqCsRefuseUpdStatusVO>();
				list.add(rrsv);

				request.setOperatorIP(WebUtils.retrieveClientIp(req));
				request.setList(list);
				request.setFirstLevelReasonCode(returnOrRejectVO.getFirstReason());
				request.setFirstLevelReasons(returnOrRejectVO.getFirstReasonText());
				request.setTwoLevelReasonCode(returnOrRejectVO.getSecondReason());
				request.setTwoLevelReasons(returnOrRejectVO.getSecondReasonText());
				request.setRemark(returnOrRejectVO.getRemark());
				result = bmsLoanInfoService.updateReturnOrRejectService(request, EnumConstants.RtfNodeState.CSFPREJECT.getValue());
				if (result.success()) {
					updateSuccess++;
				}
			} catch (Exception e) {
				LOGGER.info("初审批量改派拒绝异常:", e);
			}
		}
		if (updateSuccess > 0) {
			result.setType(Result.Type.SUCCESS);
		}
		result.addMessage("批量拒绝成功 " + updateSuccess + " 条,失败 " + ( returnOrRejectVO.getReformVOList().size() - updateSuccess) + " 条");
		return result;
	}

	/**
	 * 初审改派退回
	 * 
	 * @author dmz
	 * @date 2017年4月8日
	 * @return
	 */
	@RequestMapping("/updateReformReturn")
	@ResponseBody
	@UserLogs(link = "信审初审", operation = "初审改派退回", type = Type.初审)
	public Result<String> updateReturn(String form, HttpServletRequest req) {
		Result<String> result = new Result<String>(Result.Type.FAILURE);
		int updateSuccess = 0;
		ReturnOrRejectVO returnOrRejectVO = JSONObject.parseObject(form,ReturnOrRejectVO.class);
		for (ReformVO rf : returnOrRejectVO.getReformVOList()) {
			try {
				ReqCsRefusePlupdateStatusVO request = new ReqCsRefusePlupdateStatusVO();// 初审初审改派拒绝接口拆分
				ReqCsRefuseUpdStatusVO rrsv = new ReqCsRefuseUpdStatusVO();
				rrsv.setLoanNo(rf.getLoanNo());
				rrsv.setVersion(Long.parseLong(rf.getVersion()));

				List<ReqCsRefuseUpdStatusVO> list = new ArrayList<ReqCsRefuseUpdStatusVO>();
				list.add(rrsv);

				request.setOperatorIP(WebUtils.retrieveClientIp(req));
				request.setList(list);
				request.setFirstLevelReasonCode(returnOrRejectVO.getFirstReason());
				request.setFirstLevelReasons(returnOrRejectVO.getFirstReasonText());
				request.setTwoLevelReasonCode(returnOrRejectVO.getSecondReason());
				request.setTwoLevelReasons(returnOrRejectVO.getSecondReasonText());
				request.setRemark(returnOrRejectVO.getRemark());
				request.setReturnReasons(returnOrRejectVO.getReturnReasons());
				result = bmsLoanInfoService.updateReturnOrRejectService(request, EnumConstants.RtfNodeState.CSFPCANCEL.getValue());
				if (result.success()) {
					updateSuccess++;
				}
			} catch (Exception e) {
				LOGGER.info("初审批量改派退回异常:", e);
			}
		}
		if (updateSuccess > 0) {
			result.setType(Result.Type.SUCCESS);
		}
		result.addMessage("批量退回成功 " + updateSuccess + " 条,失败 " + ( returnOrRejectVO.getReformVOList().size() - updateSuccess) + " 条");
		return result;
	}

	/**
	 * 根据改派申请单查询判断改派目标用户是否有接单能力
	 * @param reformVOStr
	 * @param targetUserName
	 * @date 2017年04月28日
	 * @return
	 */
	@RequestMapping("/checkOrderAbility")
	@ResponseBody
	@Deprecated
	public Result<String> checkOrderAbility(String reformVOStr, String targetUserName) {
		LOGGER.info("批量改派判断用户是否有接单能力, reformVOList:{}, targetUesrName:{}", reformVOStr, targetUserName);
		Result<String> result = new Result<String>();
		try {
			List<ReformVO> reformVOList = JSONArray.parseArray(reformVOStr, ReformVO.class);
			for (ReformVO reformVO : reformVOList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("areaCode", reformVO.getSpecialOrg());
				map.put("staffCode", reformVO.getTargetUserCode());
				map.put("comCreditRating","%" + reformVO.getComCreditRating() + "%");// 欺诈风险评估
				map.put("productCode", reformVO.getProductCd() + "-" + reformVO.getApplyType());
				boolean flag = staffAbilityService.getStaffOrderAbilityByMap(map);
				if (!flag) {// 若改派目标用户对批量单子中任何一单没有接单能力就返回
					result.setType(Result.Type.FAILURE);
					result.addMessage(targetUserName  + "对" + reformVO.getLoanNo() + "没有接单能力！");
					break;
				}else{
					result.setType(Result.Type.SUCCESS);
				}
			}
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			result.addMessage("系统异常");
			LOGGER.error("批量改派判断用户是否有接单能力异常", e);
		}

		return result;
	}


	/**
	 * 根据借款编号查询改派信息
	 *
	 * @author Jia CX
	 * @date 2017年11月21日 下午3:23:55
	 * @notes
	 *
	 * @param loanNo
	 * @return
	 */
	@RequestMapping("/handle/reformInfo/{loanNo}")
	public @ResponseBody ResBMSReassignmentVo getReformInfo(@PathVariable String loanNo) {
		RequestPage requestPage = new RequestPage();
		requestPage.setOrder("desc");
		requestPage.setPage(1);
		requestPage.setRows(10);

		ReqBMSReassignmentVo request = new ReqBMSReassignmentVo();
		request.setPageNum(requestPage.getPage());
		request.setPageSize(requestPage.getRows());
		request.setLoanNo(loanNo);
		ReqInformationVO baseInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(null, loanNo, true);
		//1已分派,2为分派
		boolean ifStatus = EnumConstants.RtfNodeState.XSCSHANGUP.getValue().equals(baseInfo.getRtfNodeState()) ||  EnumConstants.RtfNodeState.XSCSASSIGN.getValue().equals(baseInfo.getRtfNodeState());
		request.setFpStatus(ifStatus ? "1" : "2");
		ResponsePage<ResBMSReassignmentVo> page = bmsLoanInfoService.getLoanNoListPage(request,requestPage);
		ResBMSReassignmentVo param = null;
		if(CollectionUtils.isNotEmpty(page.getRows())) {
			param = page.getRows().get(0);
		}
		return param;
	}

}
