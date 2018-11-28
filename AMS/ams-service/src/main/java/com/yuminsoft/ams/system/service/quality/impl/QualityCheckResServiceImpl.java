package com.yuminsoft.ams.system.service.quality.impl;

import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.yuminsoft.ams.system.dao.quality.QualityCheckResMapper;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityCheckResService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QualityCheckResServiceImpl implements QualityCheckResService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityCheckResService.class);
	
	@Autowired
	private QualityCheckResMapper qualityCheckResMapper;

	@Autowired
	private PmsApiService pmsApiService;

	@Override
	public Response<List<QualityCheckResult>> findAll(Map<String, Object> map) {
		Response<List<QualityCheckResult>> dd = new Response<List<QualityCheckResult>>();
		dd.setData(qualityCheckResMapper.findAll(map));
		return dd ;
	}

	@Override
	public QualityCheckResult findOne(Map<String, Object> map) {
		return qualityCheckResMapper.findOne(map);
	}

	@Override
	public int save(QualityCheckResult qualityCheckRes) {
		return qualityCheckResMapper.save(qualityCheckRes);
	}

	@Override
	public int update(QualityCheckResult qualityCheckRes) {
		return qualityCheckResMapper.update(qualityCheckRes);
	}

	@Override
	public List<QualityCheckResult> getQualityCheckOpinion(long qualityCheckId) {
		return qualityCheckResMapper.getQualityCheckOpinion(qualityCheckId);
	}

	/**
	 * 根据用户Code，获取辖下申请复核等待的申请件
	 * @param userCode 工号
	 * @author wulinjie
	 * @return
	 */
	@Override
	public List<QualityCheckResult> getQualityReCheckWaitList(String userCode){
		// 获取辖下质检员
		List<String> lowerByAccount = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(userCode, OrganizationEnum.OrgCode.QUALITY_CHECK);
		if(!CollectionUtils.isEmpty(lowerByAccount)){
			// 获取辖下质检员的"申请复核等待"的申请件
			return qualityCheckResMapper.findQualityRecheckWait(lowerByAccount);
		}

		return null;
	}

}
