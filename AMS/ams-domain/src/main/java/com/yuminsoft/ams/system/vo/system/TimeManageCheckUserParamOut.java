/**
 * 
 */
package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;

/**
 * 新增初审员时间限制弹框初始化时，返回的初审员信息列表
 * \
 * @author Jia CX
 * <p>2018年5月15日 上午10:01:33</p>
 * 
 */
public class TimeManageCheckUserParamOut implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4311547285827698825L;
	
	private String userCode;
	
	private String userName;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
