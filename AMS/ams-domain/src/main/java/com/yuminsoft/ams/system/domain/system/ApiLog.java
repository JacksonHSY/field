package com.yuminsoft.ams.system.domain.system;

import java.util.Date;

import com.yuminsoft.ams.system.domain.AbstractEntity;
/**
 * 外围系统交互日志
 * @author fuhongxing
 */
public class ApiLog  extends AbstractEntity{
	
	private static final long serialVersionUID = 496445057439598051L;
	
	private String platName ;		      //平台名称
	private String platCode ;			  //平台编码
	private String serialNo;              //请求流水号
	private String interfaceName;         //接口名称
	private Date   requestTime;           //请求时间
	private String requestMessage;        //请求报文
	private Date   responseTime;          //响应时间
	private String responseMessage;       //响应报文
	private String dealFlag;              //处理标识
	private String exception;             //异常信息
	
	public ApiLog() {
		super();
	}
	public ApiLog(String interfaceName, Date requestTime, String requestMessage, String dealFlag) {
		super();
		this.interfaceName = interfaceName;
		this.requestTime = requestTime;
		this.requestMessage = requestMessage;
		this.dealFlag = dealFlag;
	}
	
	public String getPlatName() {
		return platName;
	}
	public void setPlatName(String platName) {
		this.platName = platName;
	}
	public String getPlatCode() {
		return platCode;
	}
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	
	
	
	
	
}
