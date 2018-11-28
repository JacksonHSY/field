package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;

/**
 * 终审工作台已完成列表请求参数
 * 
 * @author JiaCX
 * 2017年4月25日 上午10:48:26
 *
 */
public class FinalCompletedParamIn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1188768725087100648L;
	
	/**起始时间*/
	private String offStartDate;
	
	/**结束时间*/
	private String offEndDate;

	public String getOffStartDate() {
		return offStartDate;
	}

	public void setOffStartDate(String offStartDate) {
		this.offStartDate = offStartDate;
	}

	public String getOffEndDate() {
		return offEndDate;
	}

	public void setOffEndDate(String offEndDate) {
		this.offEndDate = offEndDate;
	}
	
	

}
