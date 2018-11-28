package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;

/**
 * 终审改派获取处理人接口入参对象
 * 
 * @author JiaCX 2017年6月10日 下午1:25:57
 *
 */
public class FinishReformHandlerParamIn implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 80047503896526146L;

    /** 审批金额数组 */
    private String[] approvalAmountArr;

    /** 处理人数组 */
    private String[] handlerArr;

    /** 借款编号数组 */
    private String[] loanNoArr;

    public String[] getApprovalAmountArr()
    {
        return approvalAmountArr;
    }

    public void setApprovalAmountArr(String[] approvalAmountArr)
    {
        this.approvalAmountArr = approvalAmountArr;
    }

    public String[] getHandlerArr()
    {
        return handlerArr;
    }

    public void setHandlerArr(String[] handlerArr)
    {
        this.handlerArr = handlerArr;
    }

    public String[] getLoanNoArr()
    {
        return loanNoArr;
    }

    public void setLoanNoArr(String[] loanNoArr)
    {
        this.loanNoArr = loanNoArr;
    }
    

}
