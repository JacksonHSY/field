package com.yuminsoft.ams.system.vo.apply;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批历史Vo
 * @author unknow
 */
@ToString
@Data
public class ApproveHistoryVO implements Serializable {
    private static final long serialVersionUID = 7494727136540951123L;

    private Long id;// 主键id

    private String loanNo;//申请件编号

    private String name;//姓名

    private String idNo;//身份证号码

    private String proNum;//流程实例号

    private String proName;//流程节点名称

    private String rtfState;//审批状态

    private String rtfNodeState;// 流程节点状态

    private String checkNodeState;//复核状态(终审相关状态)

    private String refuseCode;//拒绝原因码(一级二级原因用-隔开兼容老系统)
    private String refuseCodeName;//拒绝原因码(一级二级原因用-隔开兼容老系统)

    private String checkPerson;//初审员
    private String checkPersonName;//初审员

    private String checkComplex;//初审复核人员
    private String checkComplexName;//初审复核人员

    private String finalPerson;//终审员
    private String finalPersonName;//终审员

    private String approvalPerson;//协审员
    private String approvalPersonName;//协审员

    private String finalRole;//终审权限

    private String patchBolt;//补件信息

    private String approvalLeader;      // 用户组长
    private String approvalLeaderName;      // 用户组长

    private String approvalDirector;    // 信审主管
    private String approvalDirectorName;    // 信审主管

    private String approvalManager;     // 信审经理
    private String approvalManagerName;     // 信审经理

    private String remark;//备注

    private String memo; //审批备注

    /**
     * 是否自动拒绝(Y-是,N-否)
     */
    private String autoRefuse;

    private Date createdDate;// 创建时间

    private Date lastModifiedDate;// 修改时间

    private String createdBy;// 创建人
    private String createdByName;// 创建人

    private String lastModifiedBy;// 修改人
    private String lastModifiedByName;// 修改人
}
