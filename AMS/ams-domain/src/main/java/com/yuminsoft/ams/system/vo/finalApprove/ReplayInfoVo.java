package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 还款汇总信息对应类
 * @author Shipf
 */
public class ReplayInfoVo implements Serializable{

	
	private static final long serialVersionUID = 1L;
	/**
	 * 债权iD
	 */
	public String iD;
	/**
	 * 姓名
	 */
	public String name;
	/**
	 * 证件类型
	 */
	public String   idType;
	/**
	 * 身份证号
	 */
	public String idNum;
	/**
	 * 手机
	 */
	public String mphone;
	/**
	 * 逾期起始日
	 */
	public String overDueDate;
	/**
	 * 逾期总数
	 */
	public String overDueTerm;
	/**
	 * 逾期利息
	 */
	public BigDecimal overInterest;
	/**
	 * 逾期本金
	 */
	public BigDecimal overCorpus;
	/**
	 * 罚息起算日
	 */
	public String fineDate;
	/**
	 * 罚息天数
	 */
	public String fineDay;
	/**
	 *剩余本息和
	 */
	public BigDecimal remnant;
	/**
	 * 罚息金额
	 */
	public BigDecimal fine;
	/**
	 * 当期还款日
	 */
	public String currDate;
	/**
	 * 当前期数
	 */
	public String currTerm;
	/**
	 * 当期利息
	 */
	public BigDecimal currInterest;
	/**
	 * 当期本金
	 */
	public BigDecimal currCorpus;
	/**
	 * 挂账金额
	 */
	public BigDecimal accAmount;
	/**
	 * 应还总额（不含当期）
	 */
	public BigDecimal overdueAmount;
	/**
	 * 应还总额（包含当期）
	 */
	public BigDecimal currAmount;
	/**
	 * 一次性还款金额合计
	 */
	public BigDecimal oneTimeRepayment;
	/**
	 * 查询时间
	 */
	public String tradeDate;
	
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
