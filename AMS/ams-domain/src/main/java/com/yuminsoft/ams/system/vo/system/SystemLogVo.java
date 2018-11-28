package com.yuminsoft.ams.system.vo.system;

import java.util.Date;

import com.yuminsoft.ams.system.domain.AbstractEntity;

public class SystemLogVo extends AbstractEntity {
	private static final long serialVersionUID = -2372618986604255480L;
	// Fields
	private String operation;// 操作(例如操作方法、job类型)
	private String requestContent;// 请求传递数据
	private String responseContent; // 响应内容
	private Date requestDate;// 请求系统开始时间
	private Date responseDate;// 响应返回数据时间
	private String ip;// ip地址
	private String remark; // 备注
	// Constructors

	public SystemLogVo() {

	}

	// Property accessors
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
