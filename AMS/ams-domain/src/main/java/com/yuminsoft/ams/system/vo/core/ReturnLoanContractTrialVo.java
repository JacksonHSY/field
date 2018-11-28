package com.yuminsoft.ams.system.vo.core;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class ReturnLoanContractTrialVo implements Serializable {
    private static final long serialVersionUID = -4504041710785035066L;
    private Long currentTerm;               // 当前期数
    private BigDecimal currentAccrual;      // 当期利息
    private BigDecimal giveBackRate;        // 一次性还款退费
    private BigDecimal principalBalance;    // 本金余额
    private BigDecimal repaymentAll;        // 一次性还款金额
    private Date returnDate;                // 还款日期
    private Date penaltyDate;               // 罚息起算日期
    private BigDecimal returneterm;         // 每期还款金额
    private BigDecimal penalty;             // 违约金
}
