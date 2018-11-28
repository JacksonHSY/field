package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 还款详细对应类
 * @author Shipf
 *
 */
public class ReturnRepaymentDetailVo implements Serializable{
	public static final long serialVersionUID = 6092866769389401744L;
	/**
	 * 期数
	 */
	public Integer currentTerm;
	/**
	 * 应还款日
	 */
	public String returnDate;
	/**
	 * 实际还款日
	 */
	public String   factReturnDate;
	/**
	 * 还款金额
	 */
	public BigDecimal returneterm;
	/**
	 * 当期一次性还款金额
	 */
	public BigDecimal repaymentAll;
	/**
	 * 当期还款状态
	 */
	public String repaymentState;
	/**
	 * 当期剩余欠款
	 */
	public BigDecimal deficit;
	/**
	 * 还款方
	 */
	public String borrowerName ;
	
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
