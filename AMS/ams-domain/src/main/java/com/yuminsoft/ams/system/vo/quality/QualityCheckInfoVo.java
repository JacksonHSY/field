package com.yuminsoft.ams.system.vo.quality;


import com.yuminsoft.ams.system.domain.AbstractEntity;

import java.util.Date;

/**
 * 质检申请件信息表Vo
 * @author fuhongxing
 */
public class QualityCheckInfoVo extends AbstractEntity {

	/**
     * 借款编号
     */
	private String loanNo;
	/**
     * 抽检时间，抽取申请件的时间
     */
    private Date samplingDate;
    /**
     * 分派时间，把抽取的申请件分派给质检员的时间
     */
    private Date assignDate;
    /**
     * 质检状态
     */
    private String checkStatus;
    /**
     * 完成时间
     */
    private Date endDate;
    /**
     * 质检员
     */
    private String checkUser;
    /**
     * 申请件状态 0：通过件，1拒绝件
     */
    private Integer approvalStatus;
    /**
     * 来源
     */
    private String source;
    /**
     * 是否冗余
     */
    private String redundant;
    /**
     * 是否关闭 Y-是，N-否
     */
    private String isClosed;
    /**
     * 是否已抽，Y-已抽，N-未抽
     */
    private String isChoiced;
    /**
     * 是否常规，Y-是，N-否
     */
    private String isRegular;
	/**
     * 客户姓名
     */
    private String customerName;
    /**
     * 身份证号码
     */
    private String idNo;
	/**
	 * 客户类型
	 */
	private String customerType;
    /**
     * 进件营业部ID
     */
	private String owningBranceId;
	/**
     * 进件营业部
     */
	private String owningBrance;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 贷款类型
	 */
	private String productName;
	/**
	 * 审批结果
	 */
	private String rtfState;
	/**
	 * 初审工号
	 */
    private String checkPerson;
    /**
     * 初审人员
     */
    private String checkPersonName;
    /**
     * 终审工号
     */
    private String finalPerson;
    /**
     * 终审人员
     */
    private String finalPersonName;
    /**
     * 审批日期
     */
    private Date approveDate;
    /**
     * 派单类型
     */
    private String assignType;
    /**
     * 申请复核对象
     */
    private String recheckPerson;

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Date getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}

	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRedundant() {
		return redundant;
	}

	public void setRedundant(String redundant) {
		this.redundant = redundant;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public String getIsChoiced() {
		return isChoiced;
	}

	public void setIsChoiced(String isChoiced) {
		this.isChoiced = isChoiced;
	}

	public String getIsRegular() {
		return isRegular;
	}

	public void setIsRegular(String isRegular) {
		this.isRegular = isRegular;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getOwningBranceId() {
		return owningBranceId;
	}

	public void setOwningBranceId(String owningBranceId) {
		this.owningBranceId = owningBranceId;
	}

	public String getOwningBrance() {
		return owningBrance;
	}

	public void setOwningBrance(String owningBrance) {
		this.owningBrance = owningBrance;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRtfState() {
		return rtfState;
	}

	public void setRtfState(String rtfState) {
		this.rtfState = rtfState;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public String getCheckPersonName() {
		return checkPersonName;
	}

	public void setCheckPersonName(String checkPersonName) {
		this.checkPersonName = checkPersonName;
	}

	public String getFinalPerson() {
		return finalPerson;
	}

	public void setFinalPerson(String finalPerson) {
		this.finalPerson = finalPerson;
	}

	public String getFinalPersonName() {
		return finalPersonName;
	}

	public void setFinalPersonName(String finalPersonName) {
		this.finalPersonName = finalPersonName;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getRecheckPerson() {
		return recheckPerson;
	}

	public void setRecheckPerson(String recheckPerson) {
		this.recheckPerson = recheckPerson;
	}
}