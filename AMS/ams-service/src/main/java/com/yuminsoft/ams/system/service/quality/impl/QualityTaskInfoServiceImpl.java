package com.yuminsoft.ams.system.service.quality.impl;

import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.dao.quality.QualityTaskInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityTaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质检信息
 *
 * @author YM10105
 */
@Service
public class QualityTaskInfoServiceImpl implements QualityTaskInfoService {
	
	@Autowired
	private QualityTaskInfoMapper taskInfoMapper;
	
	@Autowired
	private IEmployeeExecuter employeeExecuter;
	
	@Autowired
	private PmsApiService pmsApiService;

	@Override
	public List<QualityTaskInfo> findForAssign(Map<String, Object> map) {
		return taskInfoMapper.findForAssign(map);
	}

	@Override
	public List<QualityTaskInfo> findQualityUser(String sysCode, String userCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> codeList = new ArrayList<String>();
		codeList.add(userCode);
		List<String> lowerByAccount = pmsApiService.findLowerByAccountAndOrgTypeCode(ShiroUtils.getAccount(), OrganizationEnum.OrgCode.QUALITY_CHECK);
		if(null != lowerByAccount){
			codeList.addAll(lowerByAccount);
		}
		map.put("codeList",codeList);

		return taskInfoMapper.findForManual(map);
	}

	@Override
	public QualityTaskInfo findOne(Map<String, Object> map) {
		return taskInfoMapper.findOne(map);
	}

	@Override
	public List<QualityTaskInfo> findForManual(Map<String, Object> map) {
		return taskInfoMapper.findForManual(map);
	}
}
