package com.yuminsoft.ams.system.domain.finalaudit;

import java.math.BigDecimal;
import java.sql.Date;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 终审分单信息表
 * @author shipf
 */
public class FinalTaskInfo extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	private String appNo;//申请件编号
	
	private String groupId;//申请件所属机构

	private String rtfState;//申请件状态

	private Integer ifUrgent;//优先级
	
	private String ifPri;//是否加急
	
	private BigDecimal accLmt;//审批金额
	
	private String finalPerson;//终审人员
	
	private String finalRole;//终审角色
	
	private String approvalPerson;//协审人员
	
	private Date checkDate;//门店首次提交时间
	
	private Date finalDate;//初审首次提交终审时间

    /**
     * <p>申请件编号</p>
     */
    public String getAppNo() {
        return appNo;
    }

    /**
     * <p>申请件编号</p>
     */
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    
    /**
     * <p>申请件所属机构</p>
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * <p>申请件所属机构</p>
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * <p>申请件状态</p>
     */
    public String getRtfState() {
        return rtfState;
    }

    /**
     * <p>申请件状态</p>
     */
    public void setRtfState(String rtfState) {
        this.rtfState = rtfState;
    }
    
    /**
     * <p>优先级</p>
     */
    public Integer getIfUrgent() {
        return ifUrgent;
    }

    /**
     * <p>优先级</p>
     */
    public void setIfUrgent(Integer ifUrgent) {
        this.ifUrgent = ifUrgent;
    }
    
    /**
     * <p>是否加急</p>
     */
    public String getIfPri() {
        return ifPri;
    }

    /**
     * <p>是否加急</p>
     */
    public void setIfPri(String ifPri) {
        this.ifPri = ifPri;
    }
    
    /**
     * <p>审批金额</p>
     */
    public BigDecimal getAccLmt() {
        return accLmt;
    }

    /**
     * <p>审批金额</p>
     */
    public void setAccLmt(BigDecimal accLmt) {
        this.accLmt = accLmt;
    }
    
    /**
     * <p>终审人员</p>
     */
    public String getFinalPerson() {
        return finalPerson;
    }

    /**
     * <p>终审人员</p>
     */
    public void setFinalPerson(String finalPerson) {
        this.finalPerson = finalPerson;
    }   
    
    /**
     * <p>终审角色</p>
     */
    public String getFinalRole() {
        return finalRole;
    }

    /**
     * <p>终审角色</p>
     */
    public void setFinalRole(String finalRole) {
        this.finalRole = finalRole;
    }   
    
    /**
     * <p>协审人员</p>
     */
    public String getApprovalPerson() {
        return approvalPerson;
    }

    /**
     * <p>协审人员</p>
     */
    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    } 
    
    /**
     * <p>门店首次提交时间</p>
     */
    public Date getCheckDate() {
        return checkDate;
    }

    /**
     * <p>门店首次提交时间</p>
     */
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    } 
    
    /**
     * <p>初审首次提交终审时间</p>
     */
    public Date getFinalDate() {
        return finalDate;
    }

    /**
     * <p>初审首次提交终审时间</p>
     */
    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    } 
    
    
}