package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;

/**
 * 内部匹配--号码匹配返回vo
 * 
 * @author JiaCX 2017年7月17日 下午3:08:01
 *
 */
public class PhoneNumMatchResVO implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // 匹配号码
    String phoneNumber;

    // 字段名称
    String fieldName;

    // 对应信息
    String correspondingInfo;

    // 来源
    String source;

    // 借款编号
    String loanNum;

    // 被匹配的对应字段
    String matchFieldName;

    // 被匹配的对应信息
    String matchCorrespondingInfo;

    // 被匹配的客户姓名
    String matchName;

    // 被匹配的申请时间
    String applyDate;

    // 申请产品
    String productName;

    // 借款状态
    String loanStatus;

    // 业务环节
    String businessLink;

    // 审批结论
    String approvalConclusion;

    // 进件营业部
    String owningBranch;

    // 客户经理
    String branchManager;

    // 被匹配对应信息是否标红 1-是 0-否
    String matchCorrespondingInfoSetRed;

    // 借款状态是否标红 1-是 0-否
    String loanStatusSetRed;

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getLoanNum()
    {
        return loanNum;
    }

    public void setLoanNum(String loanNum)
    {
        this.loanNum = loanNum;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getMatchFieldName()
    {
        return matchFieldName;
    }

    public void setMatchFieldName(String matchFieldName)
    {
        this.matchFieldName = matchFieldName;
    }

    public String getCorrespondingInfo()
    {
        return correspondingInfo;
    }

    public void setCorrespondingInfo(String correspondingInfo)
    {
        this.correspondingInfo = correspondingInfo;
    }

    public String getMatchCorrespondingInfo()
    {
        return matchCorrespondingInfo;
    }

    public void setMatchCorrespondingInfo(String matchCorrespondingInfo)
    {
        this.matchCorrespondingInfo = matchCorrespondingInfo;
    }

    public String getMatchName()
    {
        return matchName;
    }

    public void setMatchName(String matchName)
    {
        this.matchName = matchName;
    }

    public String getApplyDate()
    {
        return applyDate;
    }

    public void setApplyDate(String applyDate)
    {
        this.applyDate = applyDate;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getLoanStatus()
    {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus)
    {
        this.loanStatus = loanStatus;
    }

    public String getBusinessLink()
    {
        return businessLink;
    }

    public void setBusinessLink(String businessLink)
    {
        this.businessLink = businessLink;
    }

    public String getApprovalConclusion()
    {
        return approvalConclusion;
    }

    public void setApprovalConclusion(String approvalConclusion)
    {
        this.approvalConclusion = approvalConclusion;
    }

    public String getOwningBranch()
    {
        return owningBranch;
    }

    public void setOwningBranch(String owningBranch)
    {
        this.owningBranch = owningBranch;
    }

    public String getBranchManager()
    {
        return branchManager;
    }

    public void setBranchManager(String branchManager)
    {
        this.branchManager = branchManager;
    }

    public String getMatchCorrespondingInfoSetRed()
    {
        return matchCorrespondingInfoSetRed;
    }

    public void setMatchCorrespondingInfoSetRed(String matchCorrespondingInfoSetRed)
    {
        this.matchCorrespondingInfoSetRed = matchCorrespondingInfoSetRed;
    }

    public String getLoanStatusSetRed()
    {
        return loanStatusSetRed;
    }

    public void setLoanStatusSetRed(String loanStatusSetRed)
    {
        this.loanStatusSetRed = loanStatusSetRed;
    }

}
