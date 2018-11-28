package com.yuminsoft.ams.system.vo.firstApprove;

/**
 * create by zw at 2017-05-05 批量改派导出实体类
 * 
 * @author YM10173
 *
 */
public class FirstResBMSReassignmentExportVo {
	private String ifPri;// 是否加急
	private String loanNo;// 借款单号
	private String xsSubDate;// 提交信审时间
	private String customerName;// 申请人姓名
	private String customerIDNO;// 身份证号码
	private String productName;// 借款产品
	private String owningBranchName;// 营业部名称
	private String owningBranchAttr;// 营业部属性
	private String checkPersonCode;// 处理人编码
	private String cSProxyGroupName;// 初审人员所在组名称
	private String nowQueue;

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getXsSubDate() {
		return xsSubDate;
	}

	public void setXsSubDate(String xsSubDate) {
		this.xsSubDate = xsSubDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerIDNO() {
		return customerIDNO;
	}

	public void setCustomerIDNO(String customerIDNO) {
		this.customerIDNO = customerIDNO;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOwningBranchName() {
		return owningBranchName;
	}

	public void setOwningBranchName(String owningBranchName) {
		this.owningBranchName = owningBranchName;
	}

	public String getOwningBranchAttr() {
		return owningBranchAttr;
	}

	public void setOwningBranchAttr(String owningBranchAttr) {
		this.owningBranchAttr = owningBranchAttr;
	}

	public String getIfPri() {
		return ifPri;
	}

	public void setIfPri(String ifPri) {
		this.ifPri = ifPri;
	}

	public String getCheckPersonCode() {
		return checkPersonCode;
	}

	public void setCheckPersonCode(String checkPersonCode) {
		this.checkPersonCode = checkPersonCode;
	}

	public String getCSProxyGroupName() {
		return cSProxyGroupName;
	}

	public void setCSProxyGroupName(String cSProxyGroupName) {
		this.cSProxyGroupName = cSProxyGroupName;
	}

	public String getNowQueue() {
		return nowQueue;
	}

	public void setNowQueue(String nowQueue) {
		this.nowQueue = nowQueue;
	}

}
