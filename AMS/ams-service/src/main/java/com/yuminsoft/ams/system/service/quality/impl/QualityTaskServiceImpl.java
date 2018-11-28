package com.yuminsoft.ams.system.service.quality.impl;

import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.service.quality.QualityTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 质检信息
 *
 * @author YM10105
 */
@Service
public class QualityTaskServiceImpl implements QualityTaskService {

	@Autowired
   private TaskMapper taskMapper;

	/**
	 * @Desc: 根据businessId查询任务ID
	 * @Author: phb
	 * @Date: 2017/5/10 19:29
	 */
	@Override
	public Long getTaskIdByBusinessId(String businessId) {
		return taskMapper.findTaskIdByBusinessId(businessId);
	}
}
