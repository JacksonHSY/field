package com.yuminsoft.ams.system.service.quality.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.OrganizationEnum.OrgCode;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.service.IOrganizationExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.QualityCheckInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualitySetInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualityTaskInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityCheckResService;
import com.yuminsoft.ams.system.service.quality.QualityPersonnelManagementService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityTaskInfoVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QualityPersonnelManagementServiceImpl implements QualityPersonnelManagementService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityPersonnelManagementServiceImpl.class);
	
	@Autowired
	private QualityTaskInfoMapper qualityTaskInfoMapper;

	@Autowired
	private IEmployeeExecuter iEmployeeExecuter;

	@Autowired
	private IOrganizationExecuter iOrganizationExecuter;

	@Autowired
	private QualitySetInfoMapper qualitySetInfoMapper;

	@Autowired
	private QualityCheckInfoMapper qualityCheckInfoMapper;

	@Autowired
	private QualityCheckResService qualityCheckResService;

	@Autowired
	private PmsApiService pmsApiService;

	@Value("${sys.code}")
	public String sysCode;

	@Override
	public List<QualityTaskInfo> findAll(Map<String, Object> map) {
		return qualityTaskInfoMapper.findAll(map);
	}

	@Override
	public QualityTaskInfo findOne(Map<String, Object> map) {
		return qualityTaskInfoMapper.findOne(map);
	}

	@Override
	public Result<String> save(QualityTaskInfo qualityTaskInfo) {
		Result<String> result = new Result<String>(Result.Type.SUCCESS);
		try {
			qualityTaskInfoMapper.save(qualityTaskInfo);
			result.addMessage("添加质检员成功");
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			result.addMessage("添加质检员失败");
			LOGGER.error("添加质检员失败...", e);
		}
		return result;
	}

	@Override
	public int delete(Long id) {
		return qualityTaskInfoMapper.delete(id);
	}

	@Override
	public ResponsePage<QualityTaskInfo> getPageList(RequestPage requestPage,QualityTaskInfo qualityTaskInfo) {
		ResponsePage<QualityTaskInfo> rp = new ResponsePage<QualityTaskInfo>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		Map<String, Object> paraMap = new HashMap<String,Object>();
		//获取当前人员及辖下人员code
		String code = ShiroUtils.getCurrentUser().getUsercode();
		ReqParamVO vo = new ReqParamVO();
		vo.setSysCode(sysCode);
		vo.setOrgId(pmsApiService.findOrgIdByOrgTypeCode(OrgCode.QUALITY_CHECK.getCode()));
		vo.setLoginUser(code);
        List<ResEmployeeVO> resVO = pmsApiService.getEmpsByAccount(vo);
		List<String> codeList = new ArrayList<String>(); 
		codeList.add(0,code);//把自己添加到查询人员列表里
		if(!CollectionUtils.isEmpty(resVO)){
			for(ResEmployeeVO resEmpOrgVO: resVO){
				codeList.add(resEmpOrgVO.getUsercode());
			}
		}
		paraMap.put("ifAccept", AmsConstants.TWO);
		paraMap.put("codeList", codeList);
		paraMap.put("checkUser", qualityTaskInfo.getCheckUser());
		paraMap.put("checkUserName", qualityTaskInfo.getCheckUserName());
		List<QualityTaskInfo> list = qualityTaskInfoMapper.findAll(paraMap);
		rp.setRows(list);
		rp.setTotal(((Page<QualityTaskInfo>) list).getTotal());
		return rp;
	}

	@Override
	public Result<String> deleteBatch(String[] ids) {
		Result<String> result = new Result<String>(Result.Type.SUCCESS);
		int delCount = 0;
		try {
			delCount =  qualityTaskInfoMapper.deleteBatch(ids);
			result.setData(String.valueOf(delCount));
			LOGGER.info("批量删除质检员成功!!本次删除条数：{}", delCount);
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			result.addMessage("批量删除质检员失败");
			LOGGER.error("批量删除质检员失败...", e);
		}
		return result;
	}

	@Override
	@SneakyThrows
	public void updateQualityTask(QualityTaskInfoVo qualityTaskInfoVo, List<ResOrganizationTreeVO> checkUsers) {
		if(!CollectionUtils.isEmpty(checkUsers)){
			for(ResOrganizationTreeVO checkUser :checkUsers){
				if(checkUser.getAttributes() == null){
					continue;
				}

				Map<String, String> attributes = (Map<String, String>)checkUser.getAttributes();
				String userCode = attributes.get("usercode");

				ResEmployeeVO employee = pmsApiService.findEmpByUserCode(userCode);

				qualityTaskInfoVo.setCheckUser(employee.getUsercode());  // 质检员工号
				qualityTaskInfoVo.setCheckUserName(employee.getName());	 // 质检员姓名

				//如果当前人员已存在，则删除
				Map<String, Object> queryParams = new HashMap<String,Object>();
				queryParams.put("checkUserName", employee.getName());	// 员工姓名
				queryParams.put("checkUser", employee.getUsercode());	// 工号
				queryParams.put("ifAccept", QualityEnum.IfAccept.DELETE.getCode()); // 是否已删除
				QualityTaskInfo taskInfo = findOne(queryParams);
				if(taskInfo != null){
					delete(taskInfo.getId());
				}

				QualityTaskInfo newTaskInfo = new QualityTaskInfo();
				newTaskInfo.setCheckUserName(employee.getName());
				newTaskInfo.setCheckUser(employee.getUsercode());
				newTaskInfo.setIfAccept(qualityTaskInfoVo.getIfAccept());
				newTaskInfo.setIfApplyCheck(qualityTaskInfoVo.getIfApplyCheck());
				newTaskInfo.setIfReback(qualityTaskInfoVo.getIfReback());
				newTaskInfo.setMaxTaskNum(qualityTaskInfoVo.getMaxTaskNum());
				save(newTaskInfo);

				// 开启质检复核接单，更新辖下质检员"申请复核等待"的申请件
				List<QualityCheckResult> qualityReCheckWaitList = qualityCheckResService.getQualityReCheckWaitList(employee.getUsercode()); // 获取申请件
				if(!CollectionUtils.isEmpty(qualityReCheckWaitList)){
					for (QualityCheckResult item : qualityReCheckWaitList) {
						item.setRecheckPerson(employee.getUsercode());
						item.setStatus(QualityEnum.QualityStatus.QUALITY_RECHECK.getCode());
						item.setLastModifiedBy(employee.getUsercode());
						item.setLastModifiedDate(new Date());
						qualityCheckResService.update(item);
					}
				}

			}
		}
	}

	/**获取当前人员及其辖下树
	 * @author wangzx
	 * @version 2017年6月6日
	 * @param userCode
	 * @return
	 */
	@Override
	public String getCheckUser(String userCode) {
		//得到当前人员辖下所有人员
		ReqParamVO vo = new ReqParamVO();
		vo.setSysCode(sysCode);
		vo.setOrgId(pmsApiService.findOrgIdByOrgTypeCode(OrgCode.QUALITY_CHECK.getCode()));
		vo.setLoginUser(userCode);
		vo.setInActive(AmsConstants.T); //在职
		vo.setStatus(AmsConstants.ZERO);
		vo.setAllPerms(true);
		List<ResEmployeeVO> resVO = pmsApiService.getEmpsByAccount(vo);
		List<String> codeList = new ArrayList<String>();
		codeList.add(0,userCode);//把自己添加到查询人员列表里
		if(!CollectionUtils.isEmpty(resVO)){
			for(ResEmployeeVO resEmpOrgVO: resVO){
				codeList.add(resEmpOrgVO.getUsercode());
			}
		}

		//调用平台接口，返回当前人员及辖下人员机构树
		vo.setUsercodes(codeList);
		List<String> list = new ArrayList<String>(Arrays.asList(OrgCode.QUALITY_CHECK.getCode()));
		vo.setOrgTypeCodes(list);
		List<ResOrganizationTreeVO> res = pmsApiService.getOrgTreeAndEmployees(vo);
		Gson gson = new Gson();
		return gson.toJson(res);
	}
	/**获取本人及其辖下人员
	 * @author wangzx
	 * @version 2017年6月16日
	 * @param code
	 * @return
	 */
	@Override
	public List<ResEmployeeVO> getBranchPerson(String usercode) {
		ReqParamVO vo = new ReqParamVO();
		vo.setLoginUser(usercode);
		vo.setSysCode(sysCode);
		vo.setOrgTypeCode(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode());
		Response<List<ResEmployeeVO>> resVO = iEmployeeExecuter.findLower(vo);
		List<ResEmployeeVO> list = resVO.getData();
		return list;
	}

	/**
	 * 获取两个周期内当前人员及辖下的人员，包括离职
	 * @author wangzx
	 * @version 2017年6月21日
	 * @param code
	 * @return
	 */
	@Override
	public List<ResEmployeeVO> getTwoCyclePerson(String code) {
		//获取当前辖下人员
		List<String> codeList = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(ShiroUtils.getAccount(), OrgCode.QUALITY_CHECK);
		//获取当前周期跟前一周期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isRegular", AmsConstants.ZERO);
		map.put("date", sdf.format(DateUtils.addDate(new Date(), 0)));
		String startDate =null;
		String endDate = null;
		QualitySetInfo samplingInfo = qualitySetInfoMapper.findByDate(map);
		QualitySetInfo info = null;
		List<ResEmployeeVO> voList = new ArrayList<ResEmployeeVO>();
		//判断当前周期是否为空
		if(samplingInfo!=null){
			startDate = samplingInfo.getStartDate();
			endDate = samplingInfo.getEndDate();
			//根据当前周期获取上一周期的结束日期
			if(StringUtils.isNotEmpty(samplingInfo.getStartDate())){
					try {
						map.put("date", sdf.format(DateUtils.addDate(sdf.parse(samplingInfo.getStartDate()),-1)));
						info = qualitySetInfoMapper.getLast(map);
					} catch (ParseException e) {
						LOGGER.error("质检手工派单页面获取质检人员失败"+e);
					}
					
				
			}
			if(info!=null){
				startDate = info.getStartDate();
			}
			//根据周期查找周期内分派过单子的质检人员
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("codeList", codeList);
			//根据code获取人员列表
			List<String> list = qualityCheckInfoMapper.getUserCode(map);
			//获取员工信息
			if(list.size()>0){
				for(String str:list){
					voList.add(pmsApiService.findByAccount(str));
				}
			}
		}
		
		return voList;
	}
	
}
