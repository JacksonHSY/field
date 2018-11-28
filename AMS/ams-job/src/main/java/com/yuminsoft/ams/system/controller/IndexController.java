package com.yuminsoft.ams.system.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.system.ScheduleJobService;
import com.yuminsoft.ams.system.util.Result;
@RequestMapping("/job")
@Controller
public class IndexController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	//@Autowired
	//private JobTask jobTask;
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	@RequestMapping("/")
	@ResponseBody
	public String index(){
		LOGGER.info("=====ams job index=====");
		return "JOB SUCCESS";
	}
	
	/**
	 * 查询已添加到quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/getJobInfo")
	@ResponseBody
	public String getJobInfo() throws SchedulerException{
		return JSON.toJSONString(scheduleJobService.getJobInfo());
	}
	
	/**
	 * 查询运行中的quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/getRunningJobInfo")
	@ResponseBody
	public String getRunningJobInfo() throws SchedulerException{
		return JSON.toJSONString(scheduleJobService.getRunningJobInfo());
	}
	
	/**
	 * 暂停quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/stopJob/{id}")
	@ResponseBody
	public void stopJob(@PathVariable ("id") Long id, HttpServletRequest request) throws SchedulerException{
		ScheduleJob scheduleJob = scheduleJobService.findById(id);
		scheduleJobService.stopJob(scheduleJob);
	}
	
	/**
	 * 新增quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/addJob")
	@ResponseBody
	public Result<String> addJob(HttpServletRequest request) throws SchedulerException{
		LOGGER.info("=======新增定时任务信息=======");
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setLastModifiedBy(request.getParameter("usercode"));
		scheduleJob.setJobStatus(request.getParameter("jobStatus"));
		scheduleJob.setCronExpression(request.getParameter("cronExpression"));
		scheduleJob.setRemark(request.getParameter("remark"));
		scheduleJob.setJobDesc(request.getParameter("jobDesc"));
		scheduleJob.setJobName(request.getParameter("jobName"));
		scheduleJob.setJobGroup(request.getParameter("jobGroup"));
		return scheduleJobService.addScheduleJob(scheduleJob);
	}
	
	
	/**
	 * 删除quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/deleteJob/{id}")
	@ResponseBody
	public Result<String> deleteJob(@PathVariable ("id") Long id) throws SchedulerException{
		LOGGER.info("=======删除定时任务信息：{}=======", id);
		return scheduleJobService.delete(id);
	}
	
	/**
	 * 批量删除quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/deleteBatchJob")
	@ResponseBody
	public void deleteBatchJob(String ids) throws SchedulerException{
		LOGGER.info("=======批量删除定时任务信息：{}=======", ids);
//		scheduleJobService.deletes(JSON.parseArray(ids));
	}
	/**
	 * 更新quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping(value = "/updateJob/{id}", method = RequestMethod.POST )
	@ResponseBody
	public Result<String> updateJob(@PathVariable ("id") Long id,HttpServletRequest request){
		LOGGER.info("=======修改定时任务信息=======");
		//1、更新数据库配置信息
		ScheduleJob scheduleJob = scheduleJobService.findById(id);
		scheduleJob.setLastModifiedDate(new Date());
		scheduleJob.setLastModifiedBy(request.getParameter("usercode"));
		scheduleJob.setJobStatus(request.getParameter("jobStatus"));
		scheduleJob.setCronExpression(request.getParameter("cronExpression"));
		scheduleJob.setRemark(request.getParameter("remark"));
		scheduleJob.setJobDesc(request.getParameter("jobDesc"));
		scheduleJob.setJobName(request.getParameter("jobName"));
		scheduleJob.setJobGroup(request.getParameter("jobGroup"));
		LOGGER.info("定时任务修改后信息："+JSON.toJSONString(scheduleJob));
		Result<String> result = scheduleJobService.updateScheduleJob(scheduleJob);
		return result;
	}
	
	/**
	 * 执行quartz调度器的任务
	 * @throws SchedulerException
	 */
	@RequestMapping("/executeJob/{id}")
	@ResponseBody
	public void executeJob(@PathVariable ("id") Long id) throws SchedulerException{
		ScheduleJob scheduleJob = scheduleJobService.findById(id);
		scheduleJobService.executeJob(scheduleJob);
	}
}
