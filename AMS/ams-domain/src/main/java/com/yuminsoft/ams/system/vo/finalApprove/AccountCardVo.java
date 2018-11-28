package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 还款明细对应类
 * @author Shipf
 *
 */
public class AccountCardVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 交易日期
	 */
	public String tradeDate;
	/**
	 * 交易方式
	 */
	public String tradeType;
	/**
	 * 交易类型
	 */
	public String   tradeCode;
	/**
	 * 交易类型名称
	 */
	public String   tradeName;
	/**
	 * 期初余额
	 */
	public BigDecimal beginBalance;
	/**
	 * 收入
	 */
	public BigDecimal income;
	/**
	 * 支出
	 */
	public BigDecimal outlay;
	/**
	 * 期末余额
	 */
	public BigDecimal endBalance;
	/**
	 * 备注
	 */
	public String memo;
	/**
	 * 流水号
	 */
	public String tradeNo;
	/**
	 * 流水明细信息列表
	 */
	public  List<FlowDetialsVo> flowDetialsVOList;
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
