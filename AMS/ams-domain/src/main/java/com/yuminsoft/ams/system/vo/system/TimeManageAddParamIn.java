package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;

/**
 * 新增(修改、删除)时间限制记录入参
 * 
 * @author Jia CX
 * @date 2017年12月11日 上午11:43:07
 * @notes
 * 
 */
public class TimeManageAddParamIn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6128226229987169236L;

	/** 新增时此字段为空 */
	private Long id;
	
	/** 批量修改时，存放多个id*/
	private String ids;

	/** 角色*/
	private String role;
	
	/** 初审员code */
	private String[] userCodes;

	/** 日期类型 0工作日，1节假日，2单独日期 */
	private String timeType;

	/** 日期 */
	private String date;

	/** 开始工作时间 */
	private String startTime;

	/** 结束工作时间 */
	private String endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String[] getUserCodes() {
		return userCodes;
	}

	public void setUserCodes(String[] userCodes) {
		this.userCodes = userCodes;
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

}
