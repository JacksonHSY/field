package com.yuminsoft.ams.system.vo.mail;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件VO
 * @author wulj
 */
@Data
@ToString
public class MailVo implements Serializable {
    private static final long serialVersionUID = 3492742657988883171L;
    /**
     * 发送人
     */
    private String from;
    /**
     * 收件人
     */
    private List<String> toList;
    /**
     * 抄送人
     */
    private List<String> ccList;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件主题
     */
    private String subject;
}
