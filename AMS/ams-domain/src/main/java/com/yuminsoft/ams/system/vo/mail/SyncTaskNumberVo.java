package com.yuminsoft.ams.system.vo.mail;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SyncTaskNumberVo implements Serializable {
    private static final long serialVersionUID = 8225662502097227682L;
    private String staffCode;
    private String name;
    private String taskDef;
    private Integer activiyNum;
    private Integer activiyCorrNum;
    private Boolean activiyFlag;
    private Integer priorityNum;
    private Integer priorityCorrNum;
    private Boolean priorityFlag;
    private Integer inactivyNum;
    private Integer inactivyCorrNum;
    private Boolean inactiviyFlag;
}
