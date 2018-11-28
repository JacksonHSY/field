package com.yuminsoft.ams.api.vo.request;

public class ReqMobileHistory extends Request {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String loanNo;//借款编号

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	
	
	
}
