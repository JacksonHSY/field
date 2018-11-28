/**
 * 
 */
package com.yuminsoft.ams.system.vo.pluginVo;

import java.io.Serializable;

/**
 * http调用接口返回对象(简)
 * 
 * @author Jia CX
 * <p>2018年3月27日 下午5:46:42</p>
 * 
 */
public class RestResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5229518423836747435L;
	
	public static final String SUCCESS_RESPONSE_CODE = "000000";
	
	// 响应码
	private String resCode;
	
	// 响应消息
	private String resMsg;
	
	// 返回数据
	private T data;
	
	// 总数(如果返回的不是列表，就不用这个字段)
	private long total;

	public RestResponse() {
		this.resCode = SUCCESS_RESPONSE_CODE;
	}

	public RestResponse(T data) {
		super();
		this.resCode = SUCCESS_RESPONSE_CODE;
		this.data = data;
	}
	
	public RestResponse(T data, long total) {
		super();
		this.resCode = SUCCESS_RESPONSE_CODE;
		this.data = data;
		this.total = total;
	}

	public RestResponse(String resCode, String resMsg) {
		super();
		this.resCode = resCode;
		this.resMsg = resMsg;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	
	
}
