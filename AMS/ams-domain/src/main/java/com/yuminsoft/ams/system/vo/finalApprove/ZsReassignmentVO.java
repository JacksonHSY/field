package com.yuminsoft.ams.system.vo.finalApprove;


import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;

public class ZsReassignmentVO extends ResBMSReassignmentVo
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7193767281456158482L;
    /**
     * 初审最后一次审批金额
     */
    private String lastCsApprovalAmount;

    public String getLastCsApprovalAmount()
    {
        return lastCsApprovalAmount;
    }

    public void setLastCsApprovalAmount(String lastCsApprovalAmount)
    {
        this.lastCsApprovalAmount = lastCsApprovalAmount;
    }
    
    
}
