/**
 * Copyright 2016-2026 the original author or authors.
 */
package com.yuminsoft.ams.system.vo.apply;

import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 返回终审最后一次审批意见信息(报表用)
 * 注意:继承ApprovalHistory是不确定之前用到哪些参数
 * @author xiaodong-java
 * @date 2017/12/12.
 */
@Data
public class FinalApprovalOpinionVO extends ApprovalHistory {
    private BigDecimal outCreditDebtTotal;// 外部信用负债总额
}
