package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工信息vo
 * 
 * @author JiaCX
 * 2017年4月10日 下午2:16:17
 *
 */
public class EmployeeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8322618111124408923L;

	private Long id;
	private String inActive;
	private String name;
//	private String password;
	private String usercode;
	private String acceptAuditTask;
	private String dempLevel;
	private String memo;
	private Date createTime;
	private Date updateTime;
	private String creator;
	private String updator;
	private Integer userType;
	private String employeeType;
	private String email;
	private Date leaveOfficeDate;
	private String mobile;
	private String isFirst;
	private Integer dataSource;
	private Integer status;
	private Long orgId;
	private String roleName;
	private String orgName;
	private String fullName;
	
	private String proxyUserCode;//代理组长code
	
	private String proxyUserName;//代理组长名称
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInActive() {
		return inActive;
	}
	public void setInActive(String inActive) {
		this.inActive = inActive;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getAcceptAuditTask() {
		return acceptAuditTask;
	}
	public void setAcceptAuditTask(String acceptAuditTask) {
		this.acceptAuditTask = acceptAuditTask;
	}
	public String getDempLevel() {
		return dempLevel;
	}
	public void setDempLevel(String dempLevel) {
		this.dempLevel = dempLevel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLeaveOfficeDate() {
		return leaveOfficeDate;
	}
	public void setLeaveOfficeDate(Date leaveOfficeDate) {
		this.leaveOfficeDate = leaveOfficeDate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}
	public Integer getDataSource() {
		return dataSource;
	}
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProxyUserCode() {
		return proxyUserCode;
	}
	public void setProxyUserCode(String proxyUserCode) {
		this.proxyUserCode = proxyUserCode;
	}
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	
	
}
