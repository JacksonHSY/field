package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @author YM10106
 * @date 2018/6/15
 */
@Data
@ToString
public class ReconsiderHistory extends AbstractEntity {
    private static final long serialVersionUID = -8434264321337207306L;
    private String loanNo;//借款编号
    private String reconsiderNode;// 复议流程节点(分派、改派、F1~F4)
    private String reconsiderLevel;//复议层级(F1~F4)
    private String reconsiderState;// 复议状态(提交、退回、通过、拒绝、提交信审、提交复核)
    private String reconsiderNodeState;// 复议子状态(通过、拒绝)
    private String errorLevel;// 差错等级(无差错、一般差错、重大差错)
    private String errorPerson;// 差错人
    private String errorCode;// 差错代码
    private String reconsiderRejectCode; // 复议修改拒绝原因
    private String reconsiderOperator;// 复议操作人(F2指定F3或F4复议人)
    private String operator;// 操作人
    private String remark;// 备注
}
