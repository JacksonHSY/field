package com.yuminsoft.ams.system.dao.finalaudit;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.finalaudit.FinalTaskInfo;

public interface FinalAuditDealParamMapper {

	int save(FinalTaskInfo finalTaskInfo);

	int delete(Long id);

	int update(FinalTaskInfo finalTaskInfo);
	
	int updateTaskNum(FinalTaskInfo finalTaskInfo);

	FinalTaskInfo findById(Long id);

	FinalTaskInfo findOne(Map<String, Object> map);

	List<FinalTaskInfo> findAll(Map<String, Object> map);

}

