package com.yuminsoft.ams.system.vo.quality;

import java.util.Date;

/**
 * 质检处理情况待处理_导出Vo
 * @author YM10105
 *
 */
public class QualityInspectionProcessToDoExportVo {
	/**
     * 分派日期
     */
    private Date assignDate;
	/**
     * 客户姓名
     */
    private String customerName;
    /**
     * 身份证号码
     */
    private String idNo;
	/**
	 * 客户类型（申请类型）
	 */
	private String applyType;
    /**
     * 进件营业部
     */
	private String owningBrance;
	/**
     * 申请来源
     */
    private String source;
    /**
     * 申请件编号
     */
	private String loanNo;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 贷款类型
	 */
	private String productName;
	/**
	 * 初审人员
	 */
	private String checkPersonName;
    /**
     * 终审人员
     */
    private String finalPersonName;
	/**
	 * 审批日期
	 */
	private Date approveDate;
	/**
	 * 审批结果
	 */
	private String rtfState;
	/**
	 * 拒绝原因
	 */
	private String levelName;
	/**
	 * 质检人员
	 */
	private String checkUser;
	/**
	 * 初审工号
	 */
	private String checkPerson;
	/**
	 * 终审工号
	 */
	private String finalPerson;

	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
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

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getOwningBrance() {
		return owningBrance;
	}

	public void setOwningBrance(String owningBrance) {
		this.owningBrance = owningBrance;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
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

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
}
