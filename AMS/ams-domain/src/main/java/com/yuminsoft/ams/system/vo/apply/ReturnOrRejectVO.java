package com.yuminsoft.ams.system.vo.apply;

import com.ymkj.ams.api.vo.request.audit.first.ReqQqReturnReasonVO;

import java.io.Serializable;
import java.util.List;

/**
 * 批量拒绝或者退回
 * 
 * @author dmz
 * @date 2017年4月16日
 */
public class ReturnOrRejectVO implements Serializable {
	private static final long serialVersionUID = 5257824307693729380L;
	// Fields
	private List<ReformVO> reformVOList;// 批量信息
	private String userCode;// 操作人
	private String firstReason;// 一级原因
	private String firstReasonText;// 一级原因text
	private String secondReason;// 二级原因
	private String secondReasonText;// 二级原因text
	private String remark;// 备注信息
	private List<ReqQqReturnReasonVO> returnReasons;//前前进件退回原因列表

	public ReturnOrRejectVO() {

	}

	public List<ReformVO> getReformVOList() {
		return reformVOList;
	}

	public void setReformVOList(List<ReformVO> reformVOList) {
		this.reformVOList = reformVOList;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getFirstReason() {
		return firstReason;
	}

	public void setFirstReason(String firstReason) {
		this.firstReason = firstReason;
	}

	public String getFirstReasonText() {
		return firstReasonText;
	}

	public void setFirstReasonText(String firstReasonText) {
		this.firstReasonText = firstReasonText;
	}

	public String getSecondReason() {
		return secondReason;
	}

	public void setSecondReason(String secondReason) {
		this.secondReason = secondReason;
	}

	public String getSecondReasonText() {
		return secondReasonText;
	}

	public void setSecondReasonText(String secondReasonText) {
		this.secondReasonText = secondReasonText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ReqQqReturnReasonVO> getReturnReasons() {
		return returnReasons;
	}

	public void setReturnReasons(List<ReqQqReturnReasonVO> returnReasons) {
		this.returnReasons = returnReasons;
	}
}
