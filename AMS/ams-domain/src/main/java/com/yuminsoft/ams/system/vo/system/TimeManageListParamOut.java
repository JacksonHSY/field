package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间管理列表出参
 * 
 * @author Jia CX
 * @date 2017年12月11日 上午11:16:10
 * @notes
 * 
 */
public class TimeManageListParamOut implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3068441377054806845L;

	private Long id;

	/** 日期类型 0工作日，1节假日，2单独日期 */
	private String timeType;

	/** 日期 */
	private String date;

	/** 开始工作时间 */
	private String startTime;

	/** 结束工作时间 */
	private String endTime;

	/** 角色code */
	private String roleCode;

	/** 角色名称 */
	private String roleName;
	
	/** 初审员code*/
	private String userCode;
	
	/** 初审员name*/
	private String userName;

	/** 更新人 */
	private String modifier;

	/** 更新时间 */
	private Date modifyTime;
	
	/** 更新人姓名 */
	private String modifierName;

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
