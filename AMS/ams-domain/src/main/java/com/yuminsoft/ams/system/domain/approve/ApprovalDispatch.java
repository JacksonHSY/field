package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 派单VO
 * @author wulj
 */
@Data
@ToString
public class ApprovalDispatch extends AbstractEntity {
    private static final long serialVersionUID = -3566016218850896562L;
    /**
     * 申请件编号
     */
    private String loanNo;
    /**
     * 终审级别
     */
    private String finalLevel;
    /**
     * 是否优先件
     */
    private String isPrior;
    /**
     * 是否高审件
     */
    private String isHighPass;
    /**
     * 是否协审件
     */
    private String isApproval;
    /**
     * 是否复议再申请件
     */
    private String isReconsider;
    /**
     * 分派状态 DONE-已分派, WAIT-待分派
     */
    private String status;

    /**
     * 原终审人员
     */
    @Deprecated
    private String finalPerson;
}
