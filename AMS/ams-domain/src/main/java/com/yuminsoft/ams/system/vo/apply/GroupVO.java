package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;
import java.util.List;

/**
 * 小組信息vo
 * 
 * @author JiaCX
 * 2017年4月10日 下午2:21:23
 *
 */
public class GroupVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8039195425351218438L;
	
	private Long groupId;
	
	private String groupName;
	
	private String groupUsercode;
	
	private String groupLeader;
	
	private List<EmployeeVO> employees;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupUsercode() {
		return groupUsercode;
	}
	public void setGroupUsercode(String groupUsercode) {
		this.groupUsercode = groupUsercode;
	}
	public String getGroupLeader() {
		return groupLeader;
	}
	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}
	public List<EmployeeVO> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeVO> employees) {
		this.employees = employees;
	}
	 

}
