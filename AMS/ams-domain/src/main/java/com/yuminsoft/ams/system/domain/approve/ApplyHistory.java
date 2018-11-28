package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 申请审批操作历史表
 *
 * @author fuhongxing
 */
@Data
@ToString
public class ApplyHistory extends AbstractEntity {

    private static final long serialVersionUID = 7494727136540951123L;

    private String loanNo;//申请件编号

    private String name;//姓名

    private String nameBak;// 姓名明文

    private String idNo;//身份证号码

    private String idNoBak;// 身份证明文

    private String proNum;//流程实例号

    private String proName;//流程节点名称

    private String rtfState;//审批状态

    private String rtfNodeState;// 流程节点状态

    private String checkNodeState;//复核状态(终审相关状态)

    private String refuseCode;//拒绝原因码(一级二级原因用-隔开兼容老系统)

    private String checkPerson;//初审员

    private String checkComplex;//初审复核人员

    private String finalPerson;//终审员

    private String approvalPerson;//协审员

    private String finalRole;//终审权限

    private String patchBolt;//补件信息

    private String approvalLeader;      // 用户组长

    private String approvalDirector;    // 信审主管

    /**
     * 信审经理
     */
    private String approvalManager;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批备注
     */
    private String memo;

    /**
     * 是否自动拒绝(N-否,Y-是)
     */
    private String autoRefuse;


    public String getIdNoBak() {
        return this.getIdNo();
    }

    public String getNameBak() {
        return  this.name;
    }
}