package com.yuminsoft.ams.system.domain.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核系统使用时间设置表
 * 
 * @author Jia CX
 * @date 2017年12月11日 下午2:58:49
 * @notes
 * 
 */
public class TimeManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7020374760474341139L;

	private Long id;

	/** 角色code */
	private String roleCode;
	
	/** 初审员code*/
	private String userCode;

	/** 日期类型 0工作日，1节假日，2单独日期 */
	private String timeType;

	/** 日期 */
	private String date;

	/** 开始工作时间 */
	private String startTime;

	/** 结束工作时间 */
	private String endTime;

	/** 创建人 */
	private String creator;
	
	/** 创建人姓名 */
	private String creatorName;

	/** 创建时间 */
	private Date createTime;

	/** 更新人 */
	private String modifier;
	
	/** 更新人姓名 */
	private String modifierName;

	/** 更新时间 */
	private Date modifyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

}
