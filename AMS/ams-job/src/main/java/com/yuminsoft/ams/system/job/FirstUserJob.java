package com.yuminsoft.ams.system.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.util.SpringBeanUtil;

/***
 * 同步用户job
 * 
 * @author dmz
 * @date 2017年5月17日
 */
@DisallowConcurrentExecution
public class FirstUserJob implements Job {
	private static final Logger LOGGER = LoggerFactory.getLogger(FirstUserJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// job中不能通过spring注入,需手动获取bean对象
		FirstSynchronizationUser firstUser = (FirstSynchronizationUser) SpringBeanUtil.getBean(FirstSynchronizationUser.class);
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		firstUser.initSynchronizationUser();
		LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
	}
}
