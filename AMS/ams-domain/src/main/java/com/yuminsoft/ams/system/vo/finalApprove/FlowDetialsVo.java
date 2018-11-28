package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 还款明细中对应的流水明细信息列表
 * @author Shipf
 *
 */
public class FlowDetialsVo implements Serializable{

	public static final long serialVersionUID = 1L;
	/**
	 * 明细项目
	 */
	public String acctTitle;
	/**
	 * 明细项目名称
	 */
	public String acctTitleName;
	/**
	 * 期数
	 */
	public String time;
	/**
	 * 明细金额
	 */
	public BigDecimal tradeAmount;

}
