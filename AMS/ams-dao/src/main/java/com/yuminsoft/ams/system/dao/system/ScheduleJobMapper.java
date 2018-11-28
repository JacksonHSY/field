package com.yuminsoft.ams.system.dao.system;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.system.ScheduleJob;

/**
 * 定时任务dao
 * @author fuhongxing
 */
public interface ScheduleJobMapper {

	public int save(ScheduleJob scheduleJob);

	public int delete(Long id);

	public int deletes(String[] ids);

	public int update(ScheduleJob scheduleJob);

	public ScheduleJob findById(Long id);

	public ScheduleJob findOne(Map<String, Object> map);

	public List<ScheduleJob> findAll(Map<String, Object> map);

}