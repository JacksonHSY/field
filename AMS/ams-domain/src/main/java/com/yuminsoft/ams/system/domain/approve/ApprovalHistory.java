package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import java.math.BigDecimal;

/**
 * 审批意见表
 * 
 * @author fuhongxing
 */
public class ApprovalHistory extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 606368600700043956L;

	// public static final String TABLE_NAME = "AMS_APPROVAL_HISTORY";//表名

	private String loanNo;// 申请件编号

	private String rtfState;// 审批状态

	private String rtfNodeState;// 流程节点状态

	private String checkNodeState;// 复核状态(终审相关状态)

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

	private Long nfcsId;// NFCS_ID,对应的上海资信ID

	private String finalAuditLevel;// 终审审批级别

	private String largeGroup; // 当前大组
	
	private String currentGroup;// 当前小组
	
	private String currentRole;// 当前用户角色

	private String assetType;// 审批勾选资产信息

	public String getFinalAuditLevel() {
		return finalAuditLevel;
	}

	public void setFinalAuditLevel(String finalAuditLevel) {
		this.finalAuditLevel = finalAuditLevel;
	}

	/**
	 * 申请件编号
	 */
	public String getLoanNo() {
		return loanNo;
	}

	/**
	 * 申请件编号
	 */
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	/**
	 * 审批状态
	 */
	public String getRtfState() {
		return rtfState;
	}

	/**
	 * 审批状态
	 */
	public void setRtfState(String rtfState) {
		this.rtfState = rtfState;
	}

	/**
	 * 流程节点状态
	 */
	public String getRtfNodeState() {
		return rtfNodeState;
	}

	/**
	 * 流程节点状态
	 */
	public void setRtfNodeState(String rtfNodeState) {
		this.rtfNodeState = rtfNodeState;
	}

	/**
	 * 复核状态(终审相关状态)
	 */
	public String getCheckNodeState() {
		return checkNodeState;
	}

	/**
	 * 复核状态(终审相关状态)
	 */
	public void setCheckNodeState(String checkNodeState) {
		this.checkNodeState = checkNodeState;
	}

	/**
	 * 审批人员
	 */
	public String getApprovalPerson() {
		return approvalPerson;
	}

	/**
	 * <p>
	 * 审批人员
	 * </p>
	 */
	public void setApprovalPerson(String approvalPerson) {
		this.approvalPerson = approvalPerson;
	}

	/**
	 * <p>
	 * 申请产品
	 * </p>
	 */
	public String getApprovalProductCd() {
		return approvalProductCd;
	}

	/**
	 * <p>
	 * 申请产品
	 * </p>
	 */
	public void setApprovalProductCd(String approvalProductCd) {
		this.approvalProductCd = approvalProductCd;
	}

	/**
	 * <p>
	 * 核实收入
	 * </p>
	 */
	public BigDecimal getApprovalCheckIncome() {
		return approvalCheckIncome;
	}

	/**
	 * <p>
	 * 核实收入
	 * </p>
	 */
	public void setApprovalCheckIncome(BigDecimal approvalCheckIncome) {
		this.approvalCheckIncome = approvalCheckIncome;
	}

	/**
	 * <p>
	 * 申请金额
	 * </p>
	 */
	public BigDecimal getApprovalApplyLimit() {
		return approvalApplyLimit;
	}

	/**
	 * <p>
	 * 申请金额
	 * </p>
	 */
	public void setApprovalApplyLimit(BigDecimal approvalApplyLimit) {
		this.approvalApplyLimit = approvalApplyLimit;
	}

	/**
	 * <p>
	 * 申请期限
	 * </p>
	 */
	public String getApprovalApplyTerm() {
		return approvalApplyTerm;
	}

	/**
	 * <p>
	 * 申请期限
	 * </p>
	 */
	public void setApprovalApplyTerm(String approvalApplyTerm) {
		this.approvalApplyTerm = approvalApplyTerm;
	}

	/**
	 * <p>
	 * 审批额度
	 * </p>
	 */
	public BigDecimal getApprovalLimit() {
		return approvalLimit;
	}

	/**
	 * <p>
	 * 审批额度
	 * </p>
	 */
	public void setApprovalLimit(BigDecimal approvalLimit) {
		this.approvalLimit = approvalLimit;
	}

	/**
	 * <p>
	 * 审批期限
	 * </p>
	 */
	public String getApprovalTerm() {
		return approvalTerm;
	}

	/**
	 * <p>
	 * 审批期限
	 * </p>
	 */
	public void setApprovalTerm(String approvalTerm) {
		this.approvalTerm = approvalTerm;
	}

	/**
	 * <p>
	 * 月还款额
	 * </p>
	 */
	public BigDecimal getApprovalMonthPay() {
		return approvalMonthPay;
	}

	/**
	 * <p>
	 * 月还款额
	 * </p>
	 */
	public void setApprovalMonthPay(BigDecimal approvalMonthPay) {
		this.approvalMonthPay = approvalMonthPay;
	}

	/**
	 * <p>
	 * 内部负债率
	 * </p>
	 */
	public BigDecimal getApprovalDebtTate() {
		return approvalDebtTate;
	}

	/**
	 * <p>
	 * 内部负债率
	 * </p>
	 */
	public void setApprovalDebtTate(BigDecimal approvalDebtTate) {
		this.approvalDebtTate = approvalDebtTate;
	}

	/**
	 * <p>
	 * 总负债率
	 * </p>
	 */
	public BigDecimal getApprovalAllDebtRate() {
		return approvalAllDebtRate;
	}

	/**
	 * <p>
	 * 总负债率
	 * </p>
	 */
	public void setApprovalAllDebtRate(BigDecimal approvalAllDebtRate) {
		this.approvalAllDebtRate = approvalAllDebtRate;
	}

	/**
	 * <p>
	 * 备注
	 * </p>
	 */
	public String getApprovalMemo() {
		return approvalMemo;
	}

	/**
	 * <p>
	 * 备注
	 * </p>
	 */
	public void setApprovalMemo(String approvalMemo) {
		this.approvalMemo = approvalMemo;
	}

	/**
	 * <p>
	 * NFCS_ID
	 * </p>
	 * <p>
	 * 对应的上海资信ID
	 * </p>
	 */
	public Long getNfcsId() {
		return nfcsId;
	}

	/**
	 * <p>
	 * NFCS_ID
	 * </p>
	 * <p>
	 * 对应的上海资信ID
	 * </p>
	 */
	public void setNfcsId(Long nfcsId) {
		this.nfcsId = nfcsId;
	}

	public String getLargeGroup() {
		return largeGroup;
	}

	public void setLargeGroup(String largeGroup) {
		this.largeGroup = largeGroup;
	}

	public String getCurrentGroup() {
		return currentGroup;
	}

	public void setCurrentGroup(String currentGroup) {
		this.currentGroup = currentGroup;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
}