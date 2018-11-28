package com.yuminsoft.ams.system.vo.apply;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author zhouwen
 */
@Data
@ToString
public class ReconsiderationVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String loanNo;// 借款编号
    private String reconsiderNode;// 复议节点(分派FP,改派GP,F1,F2,F3,F4)
    private String reconsiderState;// 复议状态(提交、退回、通过、拒绝、提交信审、提交复核)
    private String reconsiderNodeState;// 复议子状态(通过、拒绝)
    private String errorLevel;// 差错等级(无差错、一般差错、重大差错)
    private String errorPerson;// 差错人
    private String errorCode;// 差错代码
    private String reconsiderRejectCodeOne; // 复议修改拒绝原因一级
    private String firstReasonText;// 复议修改拒绝原因名称
    private String reconsiderRejectCodeTwo;// 复议修改拒绝原因二级
    private String secondReasonText;// 复议修改拒绝原因名称
    private String reconsiderOperator;// 复议操作人(F2指定F3或F4复议人)
    private String reconsiderOperatorName;//// 复议操作人姓名(F2指定F3或F4复议人)
    private Integer version;// 版本号控制
    private String remark; //备注
}
