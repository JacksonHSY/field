package com.yuminsoft.ams.system.vo.core;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class ContractTrialVo implements Serializable{
    private static final long serialVersionUID = -6721663578676776426L;
    private BigDecimal pactMoney;               // 合同金额
    private Long time;                          // 期数
    private Long promiseReturnDate;             // 约定还款日
    private Date startRDate;                    // 开始还款日期
    private Date endRDate;                      // 最后还款日期
    private BigDecimal referRate;               // 咨询费
    private BigDecimal evalRate;                // 评估费
    private BigDecimal manageRate;              // 管理费
    private BigDecimal risk;                    // 风险金
    private BigDecimal manageRateForPartyC;     // 丙方管理费
    private BigDecimal rateSum;                 // 收费合计
    private BigDecimal rateEM;                  // 贷款月利率
    private BigDecimal rateey;                  // 年化利率

    // 还款期数明细
    private List<ReturnLoanContractTrialVo>  returnDetailList = Lists.newArrayList();
}
