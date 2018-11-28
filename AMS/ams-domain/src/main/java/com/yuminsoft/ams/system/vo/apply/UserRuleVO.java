package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户被收回权限列表对象
 * 
 * @author JiaCX 2017年9月4日 下午5:29:01
 *
 */
public class UserRuleVO implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1812238444648555587L;

    /** 用户被收回权限主表id */
    private Long id;

    /** 用户被收回权限子表id */
    private Long subId;
    
    /** 被收回权限的用户id*/
    private String userId;

    /** 被收回权限的用户code */
    private String userCode;

    /** 被收回权限的用户name */
    private String userName;

    /** 被收回权限的用户所在组名称 */
    private String groupName;

    /** 被收回的权限类型XSCS-PASS：通过；XSCS-REJECT：拒绝；XSCS-RETURN：退回 */
    private String ruleType;

    /** 被收回的拒绝或者退回原因的数量 */
    private String ruleNum;

    /** 被收回的拒绝或者退回原因的id，按英文逗号分隔 */
    private String reasonIds;

    /** 更新人 */
    private String updatePerson;

    /** 更新时间 */
    private Date updateTime;

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the subId
     */
    public Long getSubId()
    {
        return subId;
    }

    /**
     * @param subId the subId to set
     */
    public void setSubId(Long subId)
    {
        this.subId = subId;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return the userCode
     */
    public String getUserCode()
    {
        return userCode;
    }

    /**
     * @param userCode the userCode to set
     */
    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return the groupName
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    /**
     * @return the ruleType
     */
    public String getRuleType()
    {
        return ruleType;
    }

    /**
     * @param ruleType the ruleType to set
     */
    public void setRuleType(String ruleType)
    {
        this.ruleType = ruleType;
    }

    /**
     * @return the ruleNum
     */
    public String getRuleNum()
    {
        return ruleNum;
    }

    /**
     * @param ruleNum the ruleNum to set
     */
    public void setRuleNum(String ruleNum)
    {
        this.ruleNum = ruleNum;
    }

    /**
     * @return the reasonIds
     */
    public String getReasonIds()
    {
        return reasonIds;
    }

    /**
     * @param reasonIds the reasonIds to set
     */
    public void setReasonIds(String reasonIds)
    {
        this.reasonIds = reasonIds;
    }

    /**
     * @return the updatePerson
     */
    public String getUpdatePerson()
    {
        return updatePerson;
    }

    /**
     * @param updatePerson the updatePerson to set
     */
    public void setUpdatePerson(String updatePerson)
    {
        this.updatePerson = updatePerson;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

}
