package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;

import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

/**
 * 时间管理列表入参
 * 
 * @author Jia CX
 * @date 2017年12月11日 上午11:15:01
 * @notes
 * 
 */
public class TimeManageListParamIn extends RequestPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -785112541913864612L;

	/** 要查询的角色*/
	private String roleCode;
	
	/** 查询类型 0工作日，1节假日 */
	private String timeType;

	/** 开始日期 */
	private String startDate;

	/** 截止日期 */
	private String endDate;
	
	/** 1:其他角色节点，2：初审员节点状态，3：初审员树中的某个机构，4：某个初审员 */
	private int searchType;
	
	/** 搜索工号*/
	private String userCode;
	
	/** 搜索用户名*/
	private String userName;
	
	/** 机构id*/
	private Long orgId;
	
	/** 如果查询类型是4（某个初审员），此字段需有值*/
	private String checkUserName;
	
	/** 如果查询类型是4（某个初审员），此字段需有值*/
	private String checkUserCode;
	
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
