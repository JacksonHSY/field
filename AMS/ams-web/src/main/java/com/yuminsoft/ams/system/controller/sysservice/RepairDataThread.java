package com.yuminsoft.ams.system.controller.sysservice;

import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.base.core.biz.api.message.Response;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.engine.RuleEngineService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by YM10106 on 2018/7/19.
 */
@Component
public class RepairDataThread implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Getter
    @Setter
    private IntegrateSearchExecuter integrateSearchExecuter;

    @Getter
    @Setter
    private RuleEngineService ruleEngineService;

    @Getter
    @Setter
    private String beginDate;
    @Getter
    @Setter
    private String endDate;
    private int countNumber = 0;

    @Override
    public void run() {
        Response<List<String>> response = integrateSearchExecuter.getScoreCardLoanNo(beginDate, endDate);
        LOGGER.info("数据维护总数----{}",response.getData().size());
        if (null != response && response.isSuccess() && null != response.getData()) {
            for (String loanNo : response.getData()) {
                try {
                    ruleEngineService.repairData(loanNo);
                    countNumber=countNumber+1;
                } catch (BusinessException be) {
                    LOGGER.error("维护数据手动异常:{} loanNO:{}", be.getMessage(),loanNo);
                } catch (Exception e) {
                    LOGGER.error("维护数据异常:", e);
                }
            }
            LOGGER.info("开始时间:[{}] 结束时间:[{}] 维护成功:[{}]  失败:[{}]",beginDate,endDate,countNumber,(response.getData().size()-countNumber));
        } else {
            LOGGER.info("异常查询 开始时间:[{}] 结束时间:[{}] 返回信息:{}", JSON.toJSONString(response));
        }
    }
}
