/**
 * 
 */
package com.yuminsoft.ams.system.vo.system;

import java.io.Serializable;

/**
 * 剩余可用时间对象
 * 
 * @author Jia CX
 * <p>2018年9月28日 下午4:48:09</p>
 * 
 */
public class TimeManageRemainVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1521206567851889760L;
	
	/** 剩余在线时间 */
	private int remainLoginTime;
	
	/** 当前时间用户以什么角色登陆的系统 */
	private String currentLoginRole;
	
	/** 当前时间用户登陆的角色的剩余时间 */
	private int remainRoleTime;
	
	/** 是否需要刷新页面0,不需要；1,需要 */
	private int needRefresh;

	public TimeManageRemainVO(String currentLoginRole, int remainRoleTime, int needRefresh) {
		super();
		this.currentLoginRole = currentLoginRole;
		this.remainRoleTime = remainRoleTime;
		this.needRefresh = needRefresh;
	}

	public TimeManageRemainVO(int remainLoginTime, String currentLoginRole, int remainRoleTime, int needRefresh) {
		super();
		this.remainLoginTime = remainLoginTime;
		this.currentLoginRole = currentLoginRole;
		this.remainRoleTime = remainRoleTime;
		this.needRefresh = needRefresh;
	}

	public int getNeedRefresh() {
		return needRefresh;
	}

	public void setNeedRefresh(int needRefresh) {
		this.needRefresh = needRefresh;
	}

	public int getRemainLoginTime() {
		return remainLoginTime;
	}

	public void setRemainLoginTime(int remainLoginTime) {
		this.remainLoginTime = remainLoginTime;
	}

	public String getCurrentLoginRole() {
		return currentLoginRole;
	}

	public void setCurrentLoginRole(String currentLoginRole) {
		this.currentLoginRole = currentLoginRole;
	}

	public int getRemainRoleTime() {
		return remainRoleTime;
	}

	public void setRemainRoleTime(int remainRoleTime) {
		this.remainRoleTime = remainRoleTime;
	}
	
	

}
