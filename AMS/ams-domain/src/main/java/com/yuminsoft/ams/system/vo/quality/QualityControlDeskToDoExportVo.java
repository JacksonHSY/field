package com.yuminsoft.ams.system.vo.quality;

import java.util.Date;

/**
 * 质检工作台待处理_导出Vo
 * @author YM10105
 *
 */
public class QualityControlDeskToDoExportVo {
	
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
     * 进件营业部
     */
	private String org;
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
	private Date appApplyDate;
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
     * 质检人员
     */
    private String checkUser;

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

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
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

	public Date getAppApplyDate() {
		return appApplyDate;
	}

	public void setAppApplyDate(Date appApplyDate) {
		this.appApplyDate = appApplyDate;
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
}
