package com.yuminsoft.ams.system.job;

import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;
import com.yuminsoft.ams.system.domain.system.ScheduleJob;
import com.yuminsoft.ams.system.exception.FinalDispatchException;
import com.yuminsoft.ams.system.service.approve.FinalDispatchService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.CollectionUtils;
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
 * 终审自动派单Job
 *
 * @param
 * @author shipf
 * @date
 */
@DisallowConcurrentExecution
public class FinalDispatchOrderJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalDispatchOrderJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("终审自动派单开始, startDate:{}", DateTime.now());
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        FinalDispatchService finalDispatchService = SpringBeanUtil.getBean(FinalDispatchService.class);
        BmsLoanInfoService bmsLoanInfoService = SpringBeanUtil.getBean(BmsLoanInfoService.class);
        LOGGER.info("{},任务名称 = [{}]", scheduleJob.getJobDesc(), scheduleJob.getJobName());

        // 1.通过调用借款系统接口，取到终审环节待分配单子(单子按照加急优先件 > 优先件 > 加急件 > 普通件(内部按照门店首次提交时间T1 > 初审首次提交终审时间T2)排序)
        List<ResBMSAutomaticDispatchAttrVO> orderList = bmsLoanInfoService.getFinalDispatchOrders();
        LOGGER.info("终审待分派申请件数量:{}", orderList.size());

        if (!CollectionUtils.isEmpty(orderList)) {
            // 2.初始化终审待分派池
            finalDispatchService.initOrderPool(orderList);

            //3.开始派单
            for (ResBMSAutomaticDispatchAttrVO order : orderList) {
                try {
                    if (order.getAccLmt() == null) {
                        throw new FinalDispatchException("审批金额为null");
                    }
                    finalDispatchService.automaticDispatch(order);
                } catch (FinalDispatchException ex) {
                    LOGGER.error("终审申请件[{}]分派失败，失败原因：{}", order.getLoanNo(), ex.getMessage());
                }
            }

            // 4.销毁终审待分派池
            finalDispatchService.destroyOrderPool();
        }

        LOGGER.info("终审自动派单结束, endDate:{}", DateTime.now());
    }

}
