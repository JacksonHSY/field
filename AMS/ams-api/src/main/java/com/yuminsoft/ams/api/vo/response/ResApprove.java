package com.yuminsoft.ams.api.vo.response;

import java.math.BigDecimal;

import com.yuminsoft.ams.api.vo.AbstractEntity;

/**
 * 审批意见vo对象
 * 
 * @author fuhongxing
 */
public class ResApprove extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 606368600700043956L;

	private String loanNo;// 申请件编号

	private String rtfState;// 审批状态

	private String approvalPerson;// 审批人员

	private String approvalProductCd;// 申请产品

	private BigDecimal approvalCheckIncome;// 核实收入

	private BigDecimal approvalApplyLimit;// 申请金额

	private String approvalApplyTerm;// 申请期限

	private BigDecimal approvalLimit;// 审批额度

	private String approvalTerm;// 审批期限

	private BigDecimal approvalMonthPay;// 月还款额

	private BigDecimal approvalDebtTate;// 内部负债率

	private BigDecimal approvalAllDebtRate;// 总负债率

	private String approvalMemo;// 备注

	private Long nfcsId;// 对应的上海资信ID

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getRtfState() {
		return rtfState;
	}

	public void setRtfState(String rtfState) {
		this.rtfState = rtfState;
	}

	public String getApprovalPerson() {
		return approvalPerson;
	}

	public void setApprovalPerson(String approvalPerson) {
		this.approvalPerson = approvalPerson;
	}

	public String getApprovalProductCd() {
		return approvalProductCd;
	}

	public void setApprovalProductCd(String approvalProductCd) {
		this.approvalProductCd = approvalProductCd;
	}

	public BigDecimal getApprovalCheckIncome() {
		return approvalCheckIncome;
	}

	public void setApprovalCheckIncome(BigDecimal approvalCheckIncome) {
		this.approvalCheckIncome = approvalCheckIncome;
	}

	public BigDecimal getApprovalApplyLimit() {
		return approvalApplyLimit;
	}

	public void setApprovalApplyLimit(BigDecimal approvalApplyLimit) {
		this.approvalApplyLimit = approvalApplyLimit;
	}

	public String getApprovalApplyTerm() {
		return approvalApplyTerm;
	}

	public void setApprovalApplyTerm(String approvalApplyTerm) {
		this.approvalApplyTerm = approvalApplyTerm;
	}

	public BigDecimal getApprovalLimit() {
		return approvalLimit;
	}

	public void setApprovalLimit(BigDecimal approvalLimit) {
		this.approvalLimit = approvalLimit;
	}

	public String getApprovalTerm() {
		return approvalTerm;
	}

	public void setApprovalTerm(String approvalTerm) {
		this.approvalTerm = approvalTerm;
	}

	public BigDecimal getApprovalMonthPay() {
		return approvalMonthPay;
	}

	public void setApprovalMonthPay(BigDecimal approvalMonthPay) {
		this.approvalMonthPay = approvalMonthPay;
	}

	public BigDecimal getApprovalDebtTate() {
		return approvalDebtTate;
	}

	public void setApprovalDebtTate(BigDecimal approvalDebtTate) {
		this.approvalDebtTate = approvalDebtTate;
	}

	public BigDecimal getApprovalAllDebtRate() {
		return approvalAllDebtRate;
	}

	public void setApprovalAllDebtRate(BigDecimal approvalAllDebtRate) {
		this.approvalAllDebtRate = approvalAllDebtRate;
	}

	public String getApprovalMemo() {
		return approvalMemo;
	}

	public void setApprovalMemo(String approvalMemo) {
		this.approvalMemo = approvalMemo;
	}

	public Long getNfcsId() {
		return nfcsId;
	}

	public void setNfcsId(Long nfcsId) {
		this.nfcsId = nfcsId;
	}

}