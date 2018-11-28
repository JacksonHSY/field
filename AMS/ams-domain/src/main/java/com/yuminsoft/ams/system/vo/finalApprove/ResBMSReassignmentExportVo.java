package com.yuminsoft.ams.system.vo.finalApprove;

public class ResBMSReassignmentExportVo {

    private String ifPri;//是否加急(作为案件标识使用)
	private String loanNo;//借款单号
	private String xsSubDate;//提交时间
	private String customerName;//申请人姓名
	private String customerIDNO;//身份证号码
	private String productName;//借款产品
	private String owningBranchName;//营业部名称
	private String owningBranchAttr;//营业部属性
	private String handleName;//处理人编码
	//private String proxyGroupName;//处理人所在小组名称
	private String loanNoTopClass;//申请件层级
	private String checkPersonCode;//初审人员code
	private String cSProxyGroupName;//初审人员所在组名称
	private String lastCsApprovalAmount;//提交额度
	private String zSIfNewLoanNo;//所在队列
    public String getIfPri()
    {
        return ifPri;
    }
    public void setIfPri(String ifPri)
    {
        this.ifPri = ifPri;
    }
    public String getLoanNo()
    {
        return loanNo;
    }
    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
    }
    public String getXsSubDate()
    {
        return xsSubDate;
    }
    public void setXsSubDate(String xsSubDate)
    {
        this.xsSubDate = xsSubDate;
    }
    public String getCustomerName()
    {
        return customerName;
    }
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    public String getCustomerIDNO()
    {
        return customerIDNO;
    }
    public void setCustomerIDNO(String customerIDNO)
    {
        this.customerIDNO = customerIDNO;
    }
    public String getProductName()
    {
        return productName;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    public String getOwningBranchName()
    {
        return owningBranchName;
    }
    public void setOwningBranchName(String owningBranchName)
    {
        this.owningBranchName = owningBranchName;
    }
    public String getOwningBranchAttr()
    {
        return owningBranchAttr;
    }
    public void setOwningBranchAttr(String owningBranchAttr)
    {
        this.owningBranchAttr = owningBranchAttr;
    }
    public String getHandleName()
    {
        return handleName;
    }
    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }
    public String getzSIfNewLoanNo()
    {
        return zSIfNewLoanNo;
    }
    public void setzSIfNewLoanNo(String zSIfNewLoanNo)
    {
        this.zSIfNewLoanNo = zSIfNewLoanNo;
    }
    public String getLoanNoTopClass()
    {
        return loanNoTopClass;
    }
    public void setLoanNoTopClass(String loanNoTopClass)
    {
        this.loanNoTopClass = loanNoTopClass;
    }
    public String getCheckPersonCode()
    {
        return checkPersonCode;
    }
    public void setCheckPersonCode(String checkPersonCode)
    {
        this.checkPersonCode = checkPersonCode;
    }
    public String getCSProxyGroupName()
    {
        return cSProxyGroupName;
    }
    public void setCSProxyGroupName(String cSProxyGroupName)
    {
        this.cSProxyGroupName = cSProxyGroupName;
    }
    public String getLastCsApprovalAmount()
    {
        return lastCsApprovalAmount;
    }
    public void setLastCsApprovalAmount(String lastCsApprovalAmount)
    {
        this.lastCsApprovalAmount = lastCsApprovalAmount;
    }
    public String getZSIfNewLoanNo()
    {
        return zSIfNewLoanNo;
    }
    public void setZSIfNewLoanNo(String zSIfNewLoanNo)
    {
        this.zSIfNewLoanNo = zSIfNewLoanNo;
    }
	
}
