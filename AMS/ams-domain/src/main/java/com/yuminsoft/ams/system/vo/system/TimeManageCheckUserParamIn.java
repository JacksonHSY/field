/**
 * 
 */
package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;

/**
 * 新增初审员时间限制弹框初始化时，获取返回的初审员信息列表所需要的入参
 * 
 * @author Jia CX
 * <p>2018年5月15日 上午10:08:40</p>
 * 
 */
public class TimeManageCheckUserParamIn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4367199090325601051L;
	
	/** 1:其他角色节点，2：初审员节点状态，3：初审员树中的某个机构，4：某个初审员 */
	private int searchType;
	
	/** 机构id*/
	private Long orgId;
	
	/** 如果查询类型是4（某个初审员），此字段需有值*/
	private String checkUserName;
	
	/** 如果查询类型是4（某个初审员），此字段需有值*/
	private String checkUserCode;

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}

	public String getCheckUserCode() {
		return checkUserCode;
	}

	public void setCheckUserCode(String checkUserCode) {
		this.checkUserCode = checkUserCode;
	}
	
}
