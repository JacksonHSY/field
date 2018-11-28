package com.yuminsoft.ams.system.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.system.ScheduleJobService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;

/**
 * 动态定时任务案例
 * @author fuhongxing
 */
public class ScheduleTestJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTestJob.class);
	/**
	 * 定时任务执行
	 */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//job中不能通过spring注入,需手动获取bean对象
    	ScheduleJobService scheduleJobService =(ScheduleJobService) SpringBeanUtil.getBean(ScheduleJobService.class);
    	
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
    }
    
    
}