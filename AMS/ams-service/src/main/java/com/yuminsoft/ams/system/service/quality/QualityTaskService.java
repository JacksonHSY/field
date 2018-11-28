package com.yuminsoft.ams.system.service.quality;

/**
 * @Desc: 任务查询接口
 * @Author: phb
 * @Date: 2017/5/10 19:18
 */
public interface QualityTaskService {
	/**
	 * @Desc: 根据businessId查询任务ID
	 * @Author: phb
	 * @Date: 2017/5/10 19:21
	 */
	Long getTaskIdByBusinessId(String businessId);
}
