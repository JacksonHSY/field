package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;

/**
 * 用户收回权限详细入参
 * 
 * @author JiaCX
 * 2017年9月6日 上午8:59:53
 *
 */
public class UserRuleSubParamIn implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1116525115447047586L;

    /** 被收回的权限类型XSCS-PASS：通过；XSCS-REJECT：拒绝；XSCS-RETURN：退回 */
    private String ruleType;

    /** 被收回的拒绝或者退回原因的id，按英文逗号分隔 */
    private String reasonIds;

    /** 被收回的拒绝或者退回原因的code，按英文逗号分隔 */
    private String reasonCodes;

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
     * @return the reasonCodes
     */
    public String getReasonCodes()
    {
        return reasonCodes;
    }

    /**
     * @param reasonCodes the reasonCodes to set
     */
    public void setReasonCodes(String reasonCodes)
    {
        this.reasonCodes = reasonCodes;
    }

}
