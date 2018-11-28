package com.yuminsoft.ams.system.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.yuminsoft.ams.system.dao.system.ScheduleJobMapper;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
/**
 * 定时任务公共操作
 * @author fuhongxing
 */
@Component
public class JobTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobTask.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired 
	private ScheduleJobMapper scheduleJobMapper;
	
	/**
	 * 动态生成定时任务
	 * @throws SchedulerException
	 * @throws ClassNotFoundException 
	 */
	@PostConstruct
	public void initSchedulerJob() throws SchedulerException, ClassNotFoundException{
		LOGGER.info("==========初始化定时任务信息=========");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		//获取所有启用状态的定时任务信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobStatus", "0");
		List<ScheduleJob> jobList = scheduleJobMapper.findAll(map);
		for (ScheduleJob job : jobList) {
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
			//获取trigger，即在spring配置文件中定义的 bean 例如：id="trigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			//如果不存在，创建一个
			if (null == trigger) {
				LOGGER.info("生成定时任务：{},任务名称:{}", job.getJobDesc(), job.getJobName());
				Class clz = Class.forName(job.getRemark());
				JobDetail jobDetail = JobBuilder.newJob(clz).withIdentity(job.getJobName(), job.getJobGroup()).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);
				//表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				//按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				LOGGER.info("更新定时任务：{},任务名称:{}", job.getJobDesc(), job.getJobName());
				// Trigger已存在，更新相应的定时设置
				//表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				//按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				//按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				
//				JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
//				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//				jobDetail.getJobDataMap().put("scheduleJob", job);
				
			}
		}
	}
	
	
}

