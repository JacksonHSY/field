package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;

/**
 * 返回终审办理页面入参
 * 
 * @author JiaCX
 * 2017年6月12日 下午3:22:49
 *
 */
public class FinishManageParamIn implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -1138177269299342329L;
    
    private String loanNo;
    
    private int version;
    
    private int taskType;
    
    private String checkPerson;

    public String getLoanNo()
    {
        return loanNo;
    }

    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public int getTaskType()
    {
        return taskType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public String getCheckPerson()
    {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson)
    {
        this.checkPerson = checkPerson;
    }
    
    

}
