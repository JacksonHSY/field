package com.yuminsoft.ams.system.vo.apply;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 还款详细vo
 * @author fusj
 *
 */
public class ReturnRepaymentDetailVo {
	
	//	当前期数	currentTerm	Long
	private Long currentTerm;
	//	应还款日	returnDate	Date
	private Date returnDate;
	//	实际还款日	factReturnDate	Date
	private Date factReturnDate;
	//	还款金额	returneterm	BigDecimal
	private BigDecimal returneterm;
	//	当期一次性还款金额	repaymentAll	BigDecimal
	private BigDecimal repaymentAll;
	//	当期还款状态	repaymentState	String
	private String repaymentState;
	//	当期剩余欠款	deficit	BigDecimal
	private BigDecimal deficit;
	//	还款方	borrowerName	String
	private String borrowerName;
	
	public List<ReturnRepaymentDetailVo> getReturnRepaymentList() {
		return returnRepaymentList;
	}
	public void setReturnRepaymentList(List<ReturnRepaymentDetailVo> returnRepaymentList) {
		this.returnRepaymentList = returnRepaymentList;
	}
	private List<ReturnRepaymentDetailVo> returnRepaymentList;
	
	public Long getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public Date getFactReturnDate() {
		return factReturnDate;
	}
	public void setFactReturnDate(Date factReturnDate) {
		this.factReturnDate = factReturnDate;
	}
	public BigDecimal getReturneterm() {
		return returneterm;
	}
	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}
	public BigDecimal getRepaymentAll() {
		return repaymentAll;
	}
	public void setRepaymentAll(BigDecimal repaymentAll) {
		this.repaymentAll = repaymentAll;
	}
	public String getRepaymentState() {
		return repaymentState;
	}
	public void setRepaymentState(String repaymentState) {
		this.repaymentState = repaymentState;
	}
	public BigDecimal getDeficit() {
		return deficit;
	}
	public void setDeficit(BigDecimal deficit) {
		this.deficit = deficit;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	

}
