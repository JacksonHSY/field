package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 资料核对流水表
 * @author wulj
 */
@Data
@ToString
public class ApproveCheckStatement extends AbstractEntity {
    private static final long serialVersionUID = -3964000578914058816L;
    private Long infoId;                // ams_approve_check_info主键
    private String loanNo;              // 申请件编号
    private String type;                // 对公or个人(B-对公,C-个人)
    private BigDecimal water1;          // 流水1
    private BigDecimal water2;          // 流水2
    private BigDecimal water3;          // 流水3
}
