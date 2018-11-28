package com.yuminsoft.ams.system.job;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.quality.QualitySetService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZJY on 2017/3/18.
 * 每天晚上定时抽单
 */
@DisallowConcurrentExecution
public class QualityOrderCheckJob implements Job{

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityOrderCheckJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //手动注入QualitySetSettingService
        QualitySetService QualitySetSettingService = (QualitySetService) SpringBeanUtil.getBean(QualitySetService.class);

        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
        QualitySetSettingService.systemSamplingRegular("");
        

    }
}
