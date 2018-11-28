package com.yuminsoft.ams.system.domain.approve;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户收回权限详细
 * 
 * @author JiaCX 2017年9月5日 上午10:04:14
 *
 */
public class UserRuleSub implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -7486885548591765161L;

    private Long id;

    /** 用户收回权限表的id */
    private Long userRuleId;

    /** 被收回的权限类型XSCS-PASS：通过；XSCS-REJECT：拒绝；XSCS-RETURN：退回 */
    private String ruleType;
    
    /** 被收回的拒绝或者退回原因的数量*/
    private String reasonNum;

    /** 被收回的拒绝或者退回原因的id，按英文逗号分隔 */
    private String reasonId;

    /** 被收回的拒绝或者退回原因的code，按英文逗号分隔 */
    private String reasonCode;

    /** 创建时间 */
    private Date createTime;

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
     * @return the userRuleId
     */
    public Long getUserRuleId()
    {
        return userRuleId;
    }

    /**
     * @param userRuleId the userRuleId to set
     */
    public void setUserRuleId(Long userRuleId)
    {
        this.userRuleId = userRuleId;
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
     * @return the reasonNum
     */
    public String getReasonNum()
    {
        return reasonNum;
    }

    /**
     * @param reasonNum the reasonNum to set
     */
    public void setReasonNum(String reasonNum)
    {
        this.reasonNum = reasonNum;
    }

    /**
     * @return the reasonId
     */
    public String getReasonId()
    {
        return reasonId;
    }

    /**
     * @param reasonId the reasonId to set
     */
    public void setReasonId(String reasonId)
    {
        this.reasonId = reasonId;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode()
    {
        return reasonCode;
    }

    /**
     * @param reasonCode the reasonCode to set
     */
    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
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
