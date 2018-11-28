/**
 * 
 */
package com.yuminsoft.ams.system.vo.webapi;

import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

/**
 * 电核记录查询入参
 * 
 * @author Jia CX
 * <p>2018年3月27日 下午4:11:18</p>
 * 
 */
public class TelephoneCheckParamIn extends RequestPage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -382129757378405200L;
	
	private String loanNo;

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	
	

}
