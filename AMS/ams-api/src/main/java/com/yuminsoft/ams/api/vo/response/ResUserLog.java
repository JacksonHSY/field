package com.yuminsoft.ams.api.vo.response;

import com.yuminsoft.ams.api.vo.AbstractEntity;

/**
 * 用户操作日志记录(针对初审、终审、质检)
 * @author fuhongxing
 */
public class ResUserLog extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loanNo;//借款编号
	private String ip;	//ip地址
	private String link;//操作环节
	private String operation;//当前操作(针对具体的操作,例如：初审提交、初审拒绝)
	private String method;//调用方法
	private String args;//参数
	private String type;//类型(初审、终审、质检)
	private String remark;//备注
	
	
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public enum Type {
		初审, 终审, 质检
	}
}
