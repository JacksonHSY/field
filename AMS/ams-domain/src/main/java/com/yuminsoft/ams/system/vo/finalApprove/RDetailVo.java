package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;

/**
 * 还款计划对应的类
 * @author Shipf
 *
 */
public class RDetailVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 当前利息
	 */
	public String currentAccrual;
	/**
	 * 当前期数
	 */
	public Integer currentTerm;
	/**
	 * 一次性还款退费
	 */
	public String giveBackRate;
	/**
	 * 借款ID
	 */
	public String loanId;
	/**
	 * 本金余额
	 */
	public String principalBalance;
	/**
	 * 一次性还款金额
	 */
	public String repaymentAll;
	/**
	 * 还款日期
	 */
	public String returnDate;
	/**
	 * 剩余欠款,用于记录不足额部分
	 */
	public String deficit;
	/**
	 * 当前还款状态
	 */
	public String repaymentState;
	/**
	 * 结清日期
	 */
	public String factReturnDate;
	/**
	 * 罚息起算日期
	 */
	public String penaltyDate;
	/**
	 * 每期还款金额
	 */
	public String returneterm;
	/**
	 * 违约金
	 */
	public String penalty;
	/**
	 * 对应第三方的贴息或扣息 (积木盒子)
	 */
	public String accrualRevise;
	
	public String code;
	
	public String message;

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
