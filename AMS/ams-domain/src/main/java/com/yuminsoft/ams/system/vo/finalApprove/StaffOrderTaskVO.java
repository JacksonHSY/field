package com.yuminsoft.ams.system.vo.finalApprove;

import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;

/**
 * 员工接单能力VO
 * 
 * @author JiaCX
 * 2017年6月27日 下午2:30:41
 *
 */
public class StaffOrderTaskVO extends StaffOrderTask
{

    /**
     * 
     */
    private static final long serialVersionUID = 471219700119102865L;
    
    /**员工名字*/
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
