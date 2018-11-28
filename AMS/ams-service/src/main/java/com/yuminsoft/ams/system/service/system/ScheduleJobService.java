package com.yuminsoft.ams.system.service.system;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface ScheduleJobService {
	/**
	 * 添加定时任务
	 * 
	 * @param scheduleJob
	 * @return
	 */
	public Result<String> addScheduleJob(ScheduleJob scheduleJob);

	/**
	 * 修改定时任务
	 * 
	 * @param scheduleJob
	 * @return
	 */
	public Result<String> updateScheduleJob(ScheduleJob scheduleJob);

	/**
	 * 查询单个对象
	 * 
	 * @param map
	 * @return
	 */
	public ScheduleJob findById(Long id);

	/**
	 * 查询单个对象
	 * 
	 * @param map
	 * @return
	 */
	public ScheduleJob findOne(Map<String, Object> map);

	/**
	 * 查询所有定时任务信息
	 * 
	 * @param map
	 * @return
	 */
	public List<ScheduleJob> findAll(Map<String, Object> map);
	/**
	 * 根据查询条件查询所有定时任务信息
	 * 
	 * @param map
	 * @return
	 */
	public ResponsePage<ScheduleJob> findAll(int currentPage, int pageSize, Map<String, Object> map);
	/**
	 * 分页查询
	 * 
	 * @author luting
	 * @date 2017年3月16日 下午7:37:38
	 */
	public ResponsePage<ScheduleJob> getPageList(RequestPage requestPage);
	/**
	 * 删除
	 * 
	 * @author luting
	 * @date 2017年3月16日 上午10:36:21
	 */
	public Result<String> delete(Long id);
	/**
	 * 删除
	 * 
	 * @author luting
	 * @date 2017年3月16日 上午10:36:21
	 */
	public Result<String> deletes(String[] ids);
	/**
	 * 暂停定时任务
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void stopJob(ScheduleJob scheduleJob) throws SchedulerException;
	/**
	 * 查询已添加到quartz调度器的任务
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getJobInfo() throws SchedulerException;
	/**
	 * 查询运行中的quartz调度器的任务
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getRunningJobInfo() throws SchedulerException;
	/**
	 * 执行quartz调度器的任务
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void executeJob(ScheduleJob scheduleJob) throws SchedulerException;
}
