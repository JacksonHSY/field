package com.yuminsoft.ams.system.vo.quality;

import com.yuminsoft.ams.system.vo.AbstractEntityVo;

/**
 * 质检日志
 * @author lihm
 * @data 2017年6月21日下午4:03:48
 */
public class QualityLogVo extends AbstractEntityVo{
	
	private static final long serialVersionUID = 1L;
	/**
     * 借款编号
     */
    private String loanNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 环节
     */
    private String link;
    /**
     * 环节名称
     */
    private String linkText;
    /**
     * 当前操作
     */
    private String operation;
    /**
     * 当前操作名称
     */
    private String operationText;
    
    /**
     * 操作人员名称
     */
    private String creatorName;
    
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getLinkText() {
		return linkText;
	}
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	public String getOperationText() {
		return operationText;
	}
	public void setOperationText(String operationText) {
		this.operationText = operationText;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
    
}
