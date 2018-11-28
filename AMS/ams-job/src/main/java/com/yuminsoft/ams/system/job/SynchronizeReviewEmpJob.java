/**
 * 
 */
package com.yuminsoft.ams.system.job;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.approve.ReconsiderSyncService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步拥有复议角色的员工
 * 
 * @author Jia CX
 * <p>2018年6月12日 下午2:15:23</p>
 * 
 */
@DisallowConcurrentExecution
public class SynchronizeReviewEmpJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeReviewEmpJob.class);
	 
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
    	LOGGER.info("============同步复议员工job 开始==========");
    	ReconsiderSyncService reconsiderSyncService = (ReconsiderSyncService) SpringBeanUtil.getBean(ReconsiderSyncService.class);
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
		
		try {
			reconsiderSyncService.synchronizeEmpInfo();
		} catch (Exception e1) {
			LOGGER.error("同步复议员工时发生异常!!!", e1);
		}
		
		LOGGER.info("============同步复议员工job 结束==========");
    }

}
