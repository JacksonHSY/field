package com.yuminsoft.ams.system.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;

/**
 * 同步员工Job
 * 
 * @author Jia CX
 * 
 */
@DisallowConcurrentExecution//禁止并发执行多个相同定义的JobDetail,
public class SynchronizeEmpJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizeEmpJob.class);


    /**
     * 定时任务执行
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	LOGGER.info("============同步员工job 开始==========");
    	StaffOrderTaskService staffOrderTaskService = (StaffOrderTaskService) SpringBeanUtil.getBean(StaffOrderTaskService.class);
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
		
		try {
			staffOrderTaskService.synchronizeEmpInfoNew();
		} catch (Exception e1) {
			LOGGER.error("同步员工时发生异常!!!", e1);
		}
		
		LOGGER.info("============同步员工job 结束==========");
    }
}
