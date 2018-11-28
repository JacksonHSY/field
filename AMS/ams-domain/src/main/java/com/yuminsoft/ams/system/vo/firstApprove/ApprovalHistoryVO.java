package com.yuminsoft.ams.system.vo.firstApprove;

import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;

/**
 * add by zw at2017-05-10 审批意见表VO
 * 
 * @author YM10173
 *
 */
public class ApprovalHistoryVO extends ApprovalHistory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 606368600700043956L;
	// add by zw at 2017-05-03
	private String approvalProductName;// 增加申请产品名称
	private String approvalPersonName;// 审批员对应的中文名字
	// end at 2017-05-03

	public String getApprovalProductName() {
		return approvalProductName;
	}

	public void setApprovalProductName(String approvalProductName) {
		this.approvalProductName = approvalProductName;
	}

	public String getApprovalPersonName() {
		return approvalPersonName;
	}

	public void setApprovalPersonName(String approvalPersonName) {
		this.approvalPersonName = approvalPersonName;
	}

}
