package com.yuminsoft.ams.system.vo.apply;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class RProductVo implements Serializable {
	private static final long serialVersionUID = -56511726795476231L;

	private String productName;			// 产品名字
	private BigDecimal rate;			// 综合费率
	private BigDecimal accrualem;		// 补偿利率
	private Long term;					// 借款期数
	private BigDecimal preferRate;		// 优惠客户综合费率
	private BigDecimal preferAccrualem;	// 优惠客户补偿利率
}
