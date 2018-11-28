package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 被检人员信息表
 * @author fuhongxing
 */
public class QualityRegularInfo extends AbstractEntity {

    
	private static final long serialVersionUID = -4324298892523367924L;
	/**
     * 被检人员
     */
	private String checkedUser;
	/**
     * 是否常规
     */
    private String ifRegular;
    /**
     * 被检人员姓名
     */
    private String checkedUserName;
    
    private String users;
    
	public String getCheckedUser() {
		return checkedUser;
	}
	public void setCheckedUser(String checkedUser) {
		this.checkedUser = checkedUser;
	}
	public String getIfRegular() {
		return ifRegular;
	}
	public void setIfRegular(String ifRegular) {
		this.ifRegular = ifRegular;
	}
	public String getCheckedUserName() {
		return checkedUserName;
	}
	public void setCheckedUserName(String checkedUserName) {
		this.checkedUserName = checkedUserName;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
   

}