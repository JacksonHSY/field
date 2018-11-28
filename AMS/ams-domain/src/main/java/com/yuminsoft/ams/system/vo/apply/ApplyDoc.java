package com.yuminsoft.ams.system.vo.apply;

public class ApplyDoc
{
    /**
     * fusj 申请件
     */
    private String loanNo;// 借款编号

    private String owningBanchName;// 营业部名称

    private String applyDate;// 申请时间

    private String customerName;// 客户姓名

    private String cdNo;// 身份证ID

    private String productName;// 产品名称

    private String productCode;// 产品编码

    private String accLmt;// 审批额度

    private String accTerm;// 审批期限

    private String checkPersonName;// 初审员

    private String finalPersonName;// 终审员

    private String approvalPersonName; // 协审人员

    private String primaryReason;// 一级原因Code

    private String primaryReasonText;// 一级原因内容

    private String secodeReason;// 二级原因Code

    private String secodeReasonText;// 二级原因内容

    private String accDate;// 最终终审审批时间

    private String rejectPersonName;// 拒绝人姓名

    private int version;// 版本号

    private String remark;// 备注

    private String firstReasonDendy;// 原一级拒绝原因Code

    /** 审批金额的上限 */
    private String maxLimit;

    /** 审批金额的下限 */
    private String minLimit;

    /** 进件门店id */
    private String owningBranchId;

    /** 申请金额 */
    private String applyLmt;

    private String startDate;// 开始时间

    private String endDate;// 结束时间

    private String checkPersonCode;// 初审员Code

    private String finalPersonCode;// 终审员Code

    private String approvalPersonCode; // 协审人员Code

    public String getApprovalPersonName()
    {
        return approvalPersonName;
    }

    public void setApprovalPersonName(String approvalPersonName)
    {
        this.approvalPersonName = approvalPersonName;
    }

    public String getCheckPersonCode()
    {
        return checkPersonCode;
    }

    public void setCheckPersonCode(String checkPersonCode)
    {
        this.checkPersonCode = checkPersonCode;
    }

    public String getFinalPersonCode()
    {
        return finalPersonCode;
    }

    public void setFinalPersonCode(String finalPersonCode)
    {
        this.finalPersonCode = finalPersonCode;
    }

    public String getApprovalPersonCode()
    {
        return approvalPersonCode;
    }

    public void setApprovalPersonCode(String approvalPersonCode)
    {
        this.approvalPersonCode = approvalPersonCode;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getApplyLmt()
    {
        return applyLmt;
    }

    public void setApplyLmt(String applyLmt)
    {
        this.applyLmt = applyLmt;
    }

    public String getOwningBranchId()
    {
        return owningBranchId;
    }

    public void setOwningBranchId(String owningBranchId)
    {
        this.owningBranchId = owningBranchId;
    }

    public String getMaxLimit()
    {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit)
    {
        this.maxLimit = maxLimit;
    }

    public String getMinLimit()
    {
        return minLimit;
    }

    public void setMinLimit(String minLimit)
    {
        this.minLimit = minLimit;
    }

    public String getPrimaryReasonText()
    {
        return primaryReasonText;
    }

    public void setPrimaryReasonText(String primaryReasonText)
    {
        this.primaryReasonText = primaryReasonText;
    }

    public String getSecodeReasonText()
    {
        return secodeReasonText;
    }

    public void setSecodeReasonText(String secodeReasonText)
    {
        this.secodeReasonText = secodeReasonText;
    }

    public String getFirstReasonDendy()
    {
        return firstReasonDendy;
    }

    public void setFirstReasonDendy(String firstReasonDendy)
    {
        this.firstReasonDendy = firstReasonDendy;
    }

    public String getApplyType()
    {
        return applyType;
    }

    public void setApplyType(String applyType)
    {
        this.applyType = applyType;
    }

    private String applyType;

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getLoanNo()
    {
        return loanNo;
    }

    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
    }

    public String getOwningBanchName()
    {
        return owningBanchName;
    }

    public void setOwningBanchName(String owningBanchName)
    {
        this.owningBanchName = owningBanchName;
    }

    public String getApplyDate()
    {
        return applyDate;
    }

    public void setApplyDate(String applyDate)
    {
        this.applyDate = applyDate;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCdNo()
    {
        return cdNo;
    }

    public void setCdNo(String cdNo)
    {
        this.cdNo = cdNo;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getAccLmt()
    {
        return accLmt;
    }

    public void setAccLmt(String accLmt)
    {
        this.accLmt = accLmt;
    }

    public String getCheckPersonName()
    {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName)
    {
        this.checkPersonName = checkPersonName;
    }

    public String getFinalPersonName()
    {
        return finalPersonName;
    }

    public void setFinalPersonName(String finalPersonName)
    {
        this.finalPersonName = finalPersonName;
    }

    public String getAccTerm()
    {
        return accTerm;
    }

    public void setAccTerm(String accTerm)
    {
        this.accTerm = accTerm;
    }

    public String getAccDate()
    {
        return accDate;
    }

    public void setAccDate(String accDate)
    {
        this.accDate = accDate;
    }

    public String getPrimaryReason()
    {
        return primaryReason;
    }

    public void setPrimaryReason(String primaryReason)
    {
        this.primaryReason = primaryReason;
    }

    public String getSecodeReason()
    {
        return secodeReason;
    }

    public void setSecodeReason(String secodeReason)
    {
        this.secodeReason = secodeReason;
    }

    public String getRejectPersonName()
    {
        return rejectPersonName;
    }

    public void setRejectPersonName(String rejectPersonName)
    {
        this.rejectPersonName = rejectPersonName;
    }

}
