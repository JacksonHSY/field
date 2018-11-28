package com.yuminsoft.ams.system.job;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.quality.QualityFeedBackService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZJY on 2017/3/16.
 * 开启质检反馈工作流的定时任务
 */
@DisallowConcurrentExecution
public class QualityFeedbackJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTestJob.class);


    /**
     * 定时任务执行
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //手动注入QualityFeedBackService
        QualityFeedBackService qualityFeedBackService = (QualityFeedBackService)SpringBeanUtil.getBean(QualityFeedBackService.class);

        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
        //qualityFeedBackService.qualityFeedbackJobExecute();
    }
}
