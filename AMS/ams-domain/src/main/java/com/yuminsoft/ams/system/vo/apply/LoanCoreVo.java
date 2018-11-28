package com.yuminsoft.ams.system.vo.apply;

import java.math.BigDecimal;

public class LoanCoreVo  {
	

	private String userCode;
	private String LoanNo;
	
	//	借款人	borrower	String
	private String borrower;
	
	//	性别	sex	String
	private String sex;
	//	身份证号	idnum	String
	private String idnum;
	//	借款类型	loanType	String
	private String loanType;
	//	所属机构(渠道)	organizationName	String
	private String organizationName;
	//	借款方案(渠道)	planName	String
	private String planName;
	//	申请金额	requestMoney	BigDecimal
	private String requestMoney;
	//	申请期限	requestTime	Integer
	private String requestTime;
	//	申请日期	requestDate	Date
	private String requestDate;
	//	审批金额	money	BigDecimal
	private BigDecimal money;
	//	借款期限	time	Integer
	private Integer time;
	//	可接受的最高月还款额	maxMonthlyPayment	BigDecimal
	private BigDecimal maxMonthlyPayment;
	//	状态	loanFlowState	String
	private String loanFlowState;
	//	月还款能力	restoreEM	BigDecimal
	private BigDecimal restoreEM;
	//	用途	purpose	String
	private String purpose;
	//	管理营业部	salesDepartment	String
	private String salesDepartment;
	//	还款银行	giveBackBank	String
	private String giveBackBank;
	//	放款银行	grantBank	String
	private String grantBank;
	//	客户经理	salesman	String
	private String salesman;
	//	客服	crm	String
	private String crm;
	//	合同来源	fundsSources	String
	private String fundsSources;
	//	备注	remark	String
	private String remark;
	//	剩余未结清期数	residualTime	Integer
	private Integer residualTime;
	//	放款金额	grantMoney	BigDecimal
	private BigDecimal grantMoney;
	//	结清类型 	settlementType 	String
	private String settlementType;
	//	借款状态	loanState 	String
	private String loanState;
	//月还款额
    private BigDecimal returneterm;
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getRequestMoney() {
		return requestMoney;
	}
	public void setRequestMoney(String requestMoney) {
		this.requestMoney = requestMoney;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public BigDecimal getMaxMonthlyPayment() {
		return maxMonthlyPayment;
	}
	public void setMaxMonthlyPayment(BigDecimal maxMonthlyPayment) {
		this.maxMonthlyPayment = maxMonthlyPayment;
	}
	public String getLoanFlowState() {
		return loanFlowState;
	}
	public void setLoanFlowState(String loanFlowState) {
		this.loanFlowState = loanFlowState;
	}
	public BigDecimal getRestoreEM() {
		return restoreEM;
	}
	public void setRestoreEM(BigDecimal restoreEM) {
		this.restoreEM = restoreEM;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSalesDepartment() {
		return salesDepartment;
	}
	public void setSalesDepartment(String salesDepartment) {
		this.salesDepartment = salesDepartment;
	}
	public String getGiveBackBank() {
		return giveBackBank;
	}
	public void setGiveBackBank(String giveBackBank) {
		this.giveBackBank = giveBackBank;
	}
	public String getGrantBank() {
		return grantBank;
	}
	public void setGrantBank(String grantBank) {
		this.grantBank = grantBank;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getCrm() {
		return crm;
	}
	public void setCrm(String crm) {
		this.crm = crm;
	}
	public String getFundsSources() {
		return fundsSources;
	}
	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getResidualTime() {
		return residualTime;
	}
	public void setResidualTime(Integer residualTime) {
		this.residualTime = residualTime;
	}
	public BigDecimal getGrantMoney() {
		return grantMoney;
	}
	public void setGrantMoney(BigDecimal grantMoney) {
		this.grantMoney = grantMoney;
	}
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getLoanState() {
		return loanState;
	}
	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getLoanNo() {
		return LoanNo;
	}
	public void setLoanNo(String loanNo) {
		LoanNo = loanNo;
	}
	public BigDecimal getReturneterm() {
		return returneterm;
	}
	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}
	
}
