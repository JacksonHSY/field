package com.yuminsoft.ams.system.job;

import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderDispatch;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.approve.ReconsiderDispatchService;
import com.yuminsoft.ams.system.util.SpringBeanUtil;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 复议自动派单
 *
 * @author dongmingzhi
 */
@DisallowConcurrentExecution
public class ReconsiderDispatchOrderJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReconsiderDispatchOrderJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("复议自动派单开始, startDate:{}", DateTime.now());
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        ReconsiderDispatchService reconsiderDispatchService = SpringBeanUtil.getBean(ReconsiderDispatchService.class);
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());
        List<ResReconsiderDispatch> list = reconsiderDispatchService.getReconsiderDispatchList();
        LOGGER.info("复议待分派申请件数量:{}", list.size());
        for (ResReconsiderDispatch order : list) {
            try {
                reconsiderDispatchService.automaticDispatch(order);
            } catch (Exception e) {
                LOGGER.error("借款编号[{}],复议自动分配失败:{}", order.getLoanNo(), e);
            }
        }
        LOGGER.info("复议自动派单结束, endDate:{}", DateTime.now());
    }

}
