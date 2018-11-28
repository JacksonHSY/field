package com.yuminsoft.ams.system.domain.uflo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wulinjie on 2017/6/9.
 */
@Data
@ToString
public class Task implements Serializable {

    private static final long serialVersionUID = 606368600700043956L;

    private Long id;
    private String description;
    private String nodeName;
    private Integer processId;
    private String assignee;
    private String businessId;
    private Integer countersignCount;
    private Date createDate;
    private String dateUnit;
    private Date dueActionDate;
    private Date dueDate;
    private String opinion;
    private String owner;
    private String prevState;
    private String prevTask;
    private Long processInstanceId;
    private Integer progress;
    private Integer rootProcessInstanceId;
    private String state;
    private String subject;
    private String taskName;
    private String type;
    private String url;
}
