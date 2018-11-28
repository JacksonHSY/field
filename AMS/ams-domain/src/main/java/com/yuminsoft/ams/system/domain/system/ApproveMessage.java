package com.yuminsoft.ams.system.domain.system;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 系统邮件消息记录
 *
 * @author dmz
 */
@Data
@ToString
public class ApproveMessage extends AbstractEntity {
    private static final long serialVersionUID = 496445057439598051L;
    private String loanNo;        // 借款编号
    private String subject;       // 邮件主题
    private String to;            // 收件人
    private String cc;            // 抄送人
    private String from;          // 发送人
    private String content;       // 内容
    private String rtfState;      // 审批状态
    private String rtfNodeState;  // 流程节点名称
}
