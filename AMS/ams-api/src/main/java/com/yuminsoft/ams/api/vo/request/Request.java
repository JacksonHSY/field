package com.yuminsoft.ams.api.vo.request;

import java.io.Serializable;

public abstract class Request implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sysCode; //系统编号
	
	private String serialNo;//请求流水号
	
	private String createdBy;//创建人
	
	private String ip;	//ip地址
	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
}
