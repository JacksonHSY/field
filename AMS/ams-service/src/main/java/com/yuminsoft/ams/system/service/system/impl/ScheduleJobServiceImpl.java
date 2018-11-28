package com.yuminsoft.ams.system.service.system.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.dao.system.ScheduleJobMapper;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.system.ScheduleJobService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;

/***
 * 定时任务
 * 
 * @author fuhongxing
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);
	@Autowired
	private ScheduleJobMapper scheduleJobMapper;
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Override
	public Result<String> addScheduleJob(ScheduleJob scheduleJob) {
		Result<String> result = new Result<String>(Type.FAILURE);
		try {
			int deletedId = scheduleJobMapper.save(scheduleJob);
			if (deletedId > 0) {
				result.setType(Type.SUCCESS);
				result.addMessage("新增定时任务成功！");
			}
			updateJob(scheduleJob);
		}  catch (SchedulerException | ClassNotFoundException e) {
			result.setType(Type.FAILURE);
			result.addMessage("更新定时任务调度器异常！");
			LOGGER.error("更新quartz调度器的任务异常!!!", e);
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			result.addMessage("新增定时任务异常！");
			LOGGER.error("新增定时任务异常!!!", e);
		}
		
		
		return result;
	}

	@Override
	public Result<String> updateScheduleJob(ScheduleJob scheduleJob) {
		Result<String> result = new Result<String>(Type.FAILURE);
		try {
			int updateRow = scheduleJobMapper.update(scheduleJob);
			if (updateRow > 0) {
				result.setType(Type.SUCCESS);
				result.addMessage("修改成功！");
				if("1".equals(scheduleJob.getJobStatus())){
					stopJob(scheduleJob);
				}else{
					updateJob(scheduleJob);
				}
			}else{
				result.setType(Type.FAILURE);
				result.addMessage("修改失败！");
			}
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			result.addMessage("更新定时任务异常！");
			LOGGER.error("更新定时任务异常!!!", e);
			throw new BusinessException("更新定时任务异常!!!");
		}
		return result;

	}

	@Override
	public ScheduleJob findOne(Map<String, Object> map) {
		return scheduleJobMapper.findOne(map);
	}

	@Override
	public List<ScheduleJob> findAll(Map<String, Object> map) {
		return scheduleJobMapper.findAll(map);
	}

	@Override
	public ScheduleJob findById(Long id) {
		return scheduleJobMapper.findById(id);
	}

	/**
	 * 分页查询
	 */
	@Override
	public ResponsePage<ScheduleJob> getPageList(RequestPage requestPage) {
		ResponsePage<ScheduleJob> rp = new ResponsePage<ScheduleJob>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<ScheduleJob> list = scheduleJobMapper.findAll(null);
		rp.setRows(list);
		rp.setTotal(((Page<ScheduleJob>) list).getTotal());
		return rp;
	}
	
	
	@Override
	public Result<String> delete(Long id) {
		Result<String> result = new Result<String>(Type.SUCCESS);
		int count = 0;
		try {
			ScheduleJob job =scheduleJobMapper.findById(id);
			if(job!=null){
				//设置为删除状态
				job.setJobStatus("2");
				job.setLastModifiedDate(new Date());
				count = scheduleJobMapper.update(job);
			}
			if (count > 0) {
				result.setType(Type.SUCCESS);
				result.addMessage("删除成功！");
			}else{
				result.setType(Type.FAILURE);
				result.addMessage("删除失败！");
			}
			//调用quartz进行删除
			deleteJob(job);
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			result.addMessage("删除定时任务异常！");
			LOGGER.error("删除定时任务异常!!!", e);
			throw new BusinessException("删除定时任务异常!!!");
		}
		
		return result;
	}
	/**
	 * 删除
	 */
	@Override
	public Result<String> deletes(String[] ids) {
		Result<String> result = new Result<String>(Type.SUCCESS);
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			try {
				ScheduleJob job =scheduleJobMapper.findById(Long.valueOf(ids[i]));
				if(job!=null){
					//设置为删除状态
					job.setJobStatus("2");
					scheduleJobMapper.update(job);
					count++;
				}
			} catch (NumberFormatException e) {
				result.setType(Type.FAILURE);
				result.addMessage("删除定时任务异常！");
				LOGGER.error("删除定时任务异常!!!", e);
			}
		}
		if (count > 0) {
			result.setType(Type.SUCCESS);
			result.addMessage(count + "条删除成功！");
		}else{
			result.setType(Type.FAILURE);
		}
		return result;
	}
	
	/**
	 * 查询已添加到quartz调度器的任务
	 * @throws SchedulerException
	 */
	@Override
	public List<ScheduleJob> getJobInfo() throws SchedulerException{
		LOGGER.info("=========查询查询已添加到quartz的定时任务=========");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		for (JobKey jobKey : jobKeys) {
		    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
		    ScheduleJob job = null;
		    for (Trigger trigger : triggers) {
		        job = new ScheduleJob();
		        job.setJobName(jobKey.getName());
		        job.setJobGroup(jobKey.getGroup());
		        job.setJobDesc("触发器:" + trigger.getKey());
		        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
		        job.setJobStatus(triggerState.name());
		        if (trigger instanceof CronTrigger) {
		            CronTrigger cronTrigger = (CronTrigger) trigger;
		            String cronExpression = cronTrigger.getCronExpression();
		            job.setCronExpression(cronExpression);
		        }
		        jobList.add(job);
		    }
		}
		return jobList;
	}
	/**
	 * 查询运行中的quartz调度器的任务
	 * @throws SchedulerException
	 */
	@Override
	public List<ScheduleJob> getRunningJobInfo() throws SchedulerException{
		LOGGER.info("=========查询运行中的定时任务=========");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
		ScheduleJob job = null;
		for (JobExecutionContext executingJob : executingJobs) {
		    job = new ScheduleJob();
		    JobDetail jobDetail = executingJob.getJobDetail();
		    JobKey jobKey = jobDetail.getKey();
		    Trigger trigger = executingJob.getTrigger();
		    job.setJobName(jobKey.getName());
		    job.setJobGroup(jobKey.getGroup());
		    job.setJobDesc("触发器:" + trigger.getKey());
		    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
		    job.setJobStatus(triggerState.name());
		    if (trigger instanceof CronTrigger) {
		        CronTrigger cronTrigger = (CronTrigger) trigger;
		        String cronExpression = cronTrigger.getCronExpression();
		        job.setCronExpression(cronExpression);
		    }
		    jobList.add(job);
		}
		return jobList;
	}
	/**
	 * 暂停quartz调度器的任务
	 * @throws SchedulerException
	 */
	@Override
	public void stopJob(ScheduleJob scheduleJob) throws SchedulerException{
		LOGGER.info("暂停任务：{}",scheduleJob.getJobDesc());
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseJob(jobKey);
	}
	
	/**
	 * 删除quartz调度器的任务
	 * @throws SchedulerException
	 */
	public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException{
		LOGGER.info("删除定时任务：{}",scheduleJob.getJobDesc());
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseJob(jobKey);
	}
	/**
	 * 更新quartz调度器的任务
	 * @throws SchedulerException
	 * @throws ClassNotFoundException 
	 */
	public void updateJob(ScheduleJob job) throws SchedulerException, ClassNotFoundException{
		LOGGER.info("更新定时任务：{}",job.getJobDesc());
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
//		//获取trigger，即在spring配置文件中定义的 bean 例如：id="myTrigger"
//		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//		//表达式调度构建器
//		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
//		//按新的cronExpression表达式重新构建trigger
//		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
//		//按新的trigger重新设置job执行
//		scheduler.rescheduleJob(triggerKey, trigger);
		
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		//获取trigger，即在spring配置文件中定义的 bean 例如：id="trigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		//如果不存在，创建一个
		if (null == trigger) {
			LOGGER.info("生成新的定时任务：{},任务名称:{}", job.getJobDesc(), job.getJobName());
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
			
//			JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
//			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//			jobDetail.getJobDataMap().put("scheduleJob", job);
		}
		
		LOGGER.info("更新quartz调度器的任务成功!!!{}", job.getJobDesc());
	}
	
	/**
	 * 执行quartz调度器的任务
	 * @throws SchedulerException
	 */
	@Override
	public void executeJob(ScheduleJob scheduleJob) throws SchedulerException{
		LOGGER.info("执行定时任务：{}",scheduleJob.getJobDesc());
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.triggerJob(jobKey);
	}

	@Override
	public ResponsePage<ScheduleJob> findAll(int currentPage, int pageSize, Map<String, Object> map) {
		PageHelper.startPage(currentPage, pageSize);
		ResponsePage<ScheduleJob> pageList = new ResponsePage<ScheduleJob>();
		List<ScheduleJob> list = scheduleJobMapper.findAll(map);
		pageList.setRows(list);
		pageList.setTotal(((Page<ScheduleJob>) list).getTotal());
		return pageList;
	}

	
}
