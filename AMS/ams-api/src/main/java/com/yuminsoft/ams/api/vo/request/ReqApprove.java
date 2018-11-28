package com.yuminsoft.ams.api.vo.request;

/**
 * 审批意见vo对象
 * 
 * @author fuhongxing
 */
public class ReqApprove extends Request {
	/**
	 * 
	 */
	private static final long serialVersionUID = 606368600700043956L;

	private String loanNo;// 借款编号

	private String rtfState;// 审批状态

	private String approvalPerson;// 审批人员

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

}