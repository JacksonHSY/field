package com.yuminsoft.ams.system.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.quality.QualityDispatchService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;

/**
 * 系统派单定时任务
 * @author lihm
 *
 *@data 2017年5月4日上午11:35:45
 */
@DisallowConcurrentExecution
public class QualityDispatchJob implements Job{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTestJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		//手动注入QualityDispatchService
		QualityDispatchService dispatchService = (QualityDispatchService)SpringBeanUtil.getBean(QualityDispatchService.class);
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
		dispatchService.dispatch();
	}
	
}
