package com.yuminsoft.ams.system.job;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.approve.TaskNumberService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每天凌晨2点同步员工队列数
 * @author wulj
 */
public class SyncTaskNumJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTaskNumJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("同步员工队列数开始 startDate:{}", DateTime.now());
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        TaskNumberService taskNumberService = SpringBeanUtil.getBean(TaskNumberService.class);
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());

        taskNumberService.syncTaskNumber();

        LOGGER.info("同步员工队列数结束 endDate:{}", DateTime.now());
    }

}
