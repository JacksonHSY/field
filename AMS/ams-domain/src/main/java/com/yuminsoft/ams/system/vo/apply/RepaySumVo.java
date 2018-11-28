package com.yuminsoft.ams.system.vo.apply;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 核心 还款汇总vo
 * @author fusj
 *
 */
public class RepaySumVo {

	//	债权唯一码	loanNo	String
	private String loanNo;
	//	姓名	name	String
	private String name;
	//	证件类型	idType	String
	private String idType;
	//	身份证号	idNum	String
	private String idNum;
	//	手机	mphone	String
	private String mphone;
	//	逾期起始日	overDueDate	Date
	private Date overDueDate;
	//	逾期总数	overDueTerm	int
	private int overDueTerm;
	//	逾期利息	overInterest	BigDecimal
	private BigDecimal overInterest;
	//	逾期本金	overCorpus	BigDecimal
	private BigDecimal overCorpus;
	//	罚息起算日	fineDate	Date
	private Date fineDate;
	//	罚息天数	fineDay	int
	private int fineDay;
	//	剩余本息和	remnant	BigDecimal
	private BigDecimal remnant;
	//	罚息金额	fine	BigDecimal
	private BigDecimal fine;
	//	当期还款日	currDate	Date
	private Date currDate;
	//	当前期数	currTerm	Long
	private Long currTerm;
	//	当期利息	currInterest	BigDecimal
	private BigDecimal currInterest;
	//	当期本金	currCorpus	BigDecimal
	private BigDecimal currCorpus;
	//	挂账金额	accAmount	BigDecimal
	private BigDecimal accAmount;
	//	应还总额（不含当期）	overdueAmount	BigDecimal
	private BigDecimal overdueAmount;
	//	应还总额（包含当期）	currAmount	BigDecimal
	private BigDecimal currAmount;
	//	一次性还款金额合计	oneTimeRepayment	BigDecimal
	private BigDecimal oneTimeRepayment;
	//	查询时间	tradeDate	Date
	private Date tradeDate;
	
	/** 剩余本金 */
	private BigDecimal residualPactMoney;
	
	public BigDecimal getResidualPactMoney() {
		return residualPactMoney;
	}
	public void setResidualPactMoney(BigDecimal residualPactMoney) {
		this.residualPactMoney = residualPactMoney;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public Date getOverDueDate() {
		return overDueDate;
	}
	public void setOverDueDate(Date overDueDate) {
		this.overDueDate = overDueDate;
	}
	public int getOverDueTerm() {
		return overDueTerm;
	}
	public void setOverDueTerm(int overDueTerm) {
		this.overDueTerm = overDueTerm;
	}
	public BigDecimal getOverInterest() {
		return overInterest;
	}
	public void setOverInterest(BigDecimal overInterest) {
		this.overInterest = overInterest;
	}
	public BigDecimal getOverCorpus() {
		return overCorpus;
	}
	public void setOverCorpus(BigDecimal overCorpus) {
		this.overCorpus = overCorpus;
	}
	public Date getFineDate() {
		return fineDate;
	}
	public void setFineDate(Date fineDate) {
		this.fineDate = fineDate;
	}
	public int getFineDay() {
		return fineDay;
	}
	public void setFineDay(int fineDay) {
		this.fineDay = fineDay;
	}
	public BigDecimal getRemnant() {
		return remnant;
	}
	public void setRemnant(BigDecimal remnant) {
		this.remnant = remnant;
	}
	public BigDecimal getFine() {
		return fine;
	}
	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}
	public Date getCurrDate() {
		return currDate;
	}
	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}
	public Long getCurrTerm() {
		return currTerm;
	}
	public void setCurrTerm(Long currTerm) {
		this.currTerm = currTerm;
	}
	public BigDecimal getCurrInterest() {
		return currInterest;
	}
	public void setCurrInterest(BigDecimal currInterest) {
		this.currInterest = currInterest;
	}
	public BigDecimal getCurrCorpus() {
		return currCorpus;
	}
	public void setCurrCorpus(BigDecimal currCorpus) {
		this.currCorpus = currCorpus;
	}
	public BigDecimal getAccAmount() {
		return accAmount;
	}
	public void setAccAmount(BigDecimal accAmount) {
		this.accAmount = accAmount;
	}
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	public BigDecimal getCurrAmount() {
		return currAmount;
	}
	public void setCurrAmount(BigDecimal currAmount) {
		this.currAmount = currAmount;
	}
	public BigDecimal getOneTimeRepayment() {
		return oneTimeRepayment;
	}
	public void setOneTimeRepayment(BigDecimal oneTimeRepayment) {
		this.oneTimeRepayment = oneTimeRepayment;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
}
