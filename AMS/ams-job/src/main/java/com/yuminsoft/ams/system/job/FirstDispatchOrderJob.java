package com.yuminsoft.ams.system.job;

import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.service.approve.FirstDispatchService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
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
 * 初审自动派单
 * @author dongmingzhi
 */
@DisallowConcurrentExecution
public class FirstDispatchOrderJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstDispatchOrderJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("初审自动派单开始, startDate:{}", DateTime.now());
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		BmsLoanInfoService bmsLoanInfoService = SpringBeanUtil.getBean(BmsLoanInfoService.class);
		FirstDispatchService autoDispatchService = SpringBeanUtil.getBean(FirstDispatchService.class);
		LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());

		// 获取所有初审节点所有借款单信息:优先件+加急>优先件>正常件+加急件>正常件(同级别按初次提交初审时间优先)
		List<ResBMSAutomaticDispatchAttrVO> firstDispatchOrders = bmsLoanInfoService.getFirstDispatchOrders();
		LOGGER.info("初审待分派申请件数量:{}", firstDispatchOrders.size());
		for (ResBMSAutomaticDispatchAttrVO order : firstDispatchOrders) {
			try {
				autoDispatchService.automaticDispatch(order);
			} catch (Exception e) {
				LOGGER.error("借款编号[{}],信审初审自动分配失败:{}", order.getLoanNo(), e);
			}
		}
		LOGGER.info("初审自动派单结束, endDate:{}", DateTime.now());
	}

}
