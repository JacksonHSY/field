package com.yuminsoft.ams.system.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.system.ScheduleJobService;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

@Controller
@RequestMapping("/scheduleJob")
public class ScheduleJobController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobController.class);
	@Autowired
	private ScheduleJobService scheduleJobService;
	@Value("${ams.job.url}")
	private String jobUrl;
	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "system/scheduleJob/scheduleJob";
	}

	/**
	 * 分页查询
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<ScheduleJob> getPage(RequestPage requestPage,ScheduleJob scheduleJob) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(scheduleJob.getJobName())){
			map.put("jobName", scheduleJob.getJobName());
		}
		return scheduleJobService.findAll(requestPage.getPage(), requestPage.getRows(), map);
	}

	/**
	 * 保存
	 * 
	 * @author luting
	 * @date 2017年3月16日 下午1:38:36
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Result<String> save(HttpServletRequest request, ScheduleJob scheduleJob) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("jobStatus", scheduleJob.getJobStatus());
		params.put("cronExpression", scheduleJob.getCronExpression());
		params.put("jobDesc", scheduleJob.getJobDesc());
		params.put("remark", scheduleJob.getRemark());
		params.put("jobName", scheduleJob.getJobName());
		params.put("jobGroup", scheduleJob.getJobGroup());
		params.put("usercode", ShiroUtils.getCurrentUser().getUsercode());
//		String url = "http://127.0.0.1:8081/job/addJob/"+scheduleJob.getId();
		String result = HttpUtils.doPost(jobUrl+"addJob/", params);
		Result<String> rst = JSON.parseObject(result, Result.class);
		return rst;
	}

	/**
	 * 删除
	 * @author fuhongxing
	 * @date 2017年3月16日 上午10:34:37
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	@ResponseBody
	public Result<String> delete(HttpServletRequest request, String[] ids) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercode", ShiroUtils.getCurrentUser().getUsercode());
		Result<String> deleteResult = null;
		int success = 0;
		int failure = 0;
		for (int i = 0; i < ids.length; i++) {
			String result = HttpUtils.doPost(jobUrl+"deleteJob/"+ids[i], params);
			deleteResult = JSON.parseObject(result, Result.class);
			if(deleteResult.getSuccess()){
				success++;
			}else{
				failure++;
			}
		}
		if(failure>0){
			deleteResult.addMessage("成功删除"+success+"条，删除失败"+failure+"条");
		}else{
			deleteResult.addMessage("成功删除"+success+"条");
		}
		
		return scheduleJobService.deletes(ids);
	}

	/**
	 * 根据id查找
	 * 
	 * @author fuhongxing
	 * @date 2017年3月16日 上午11:21:52
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(HttpServletRequest request, Long id) {
		ScheduleJob scheduleJob = scheduleJobService.findById(id);
		return JSONObject.toJSONString(scheduleJob);
	}

	/**
	 * 修改
	 * 
	 * @author fuhongxing
	 * @date 2017年3月16日 上午11:43:58
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/update")
	@ResponseBody
	public Result<String> update(HttpServletRequest request, ScheduleJob scheduleJob) {
//		Result<String> result = scheduleJobService.updateScheduleJob(scheduleJob);
		//调用job接口更新job
		LOGGER.info("=============更新定时任务============");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", scheduleJob.getId()+"");
		params.put("jobStatus", scheduleJob.getJobStatus());
		params.put("cronExpression", scheduleJob.getCronExpression());
		params.put("jobDesc", scheduleJob.getJobDesc());
		params.put("remark", scheduleJob.getRemark());
		params.put("jobName", scheduleJob.getJobName());
		params.put("jobGroup", scheduleJob.getJobGroup());
		params.put("usercode", ShiroUtils.getCurrentUser().getUsercode());
//		String url = "http://127.0.0.1:8081/job/updateJob/"+scheduleJob.getId();
		String result = HttpUtils.doPost(jobUrl+"updateJob/"+scheduleJob.getId(), params);
		Result<String> updateResult = JSON.parseObject(result, Result.class);
		return updateResult;
	}
}
