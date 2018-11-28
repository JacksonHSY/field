package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;
import java.util.List;

/**
 * 用户收回权限入参
 * 
 * @author JiaCX 2017年9月5日 上午9:45:12
 *
 */
public class UserRuleParamIn implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -8374930657716239557L;

    /** 收回权限主表id */
    private Long id;
    
    /** 用户id*/
    private String userId;

    /** 用户code */
    private String userCode;

    /** 用户被收回权限的详细信息 */
    private List<UserRuleSubParamIn> subList;

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
     * @return the subList
     */
    public List<UserRuleSubParamIn> getSubList()
    {
        return subList;
    }

    /**
     * @param subList the subList to set
     */
    public void setSubList(List<UserRuleSubParamIn> subList)
    {
        this.subList = subList;
    }

}
