package com.yuminsoft.ams.api.vo.request;

/**
 * 用户操作日志记录(针对初审、终审、质检)
 * @author fuhongxing
 */
public class ReqUserLog extends Request {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loanNo;//借款编号
	private String approveLink;//审批环节
	
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	
	public String getApproveLink() {
		return approveLink;
	}
	public void setApproveLink(String approveLink) {
		this.approveLink = approveLink;
	}
	
	public enum Type {
		初审, 终审, 质检, 复核
	}
}
