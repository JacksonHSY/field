package com.yuminsoft.ams.system.vo.apply;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务数vo
 *
 * @author fusj
 */
@Data
@ToString
public class TaskNumber implements Serializable {

    private static final long serialVersionUID = -5774937154808042398L;
    /**
     * 审核节点
     */
    private String taskDefId;

    /**
     * 用户编码
     */
    private String staffCode;

    /**
     * 用户名称
     */
    private String staffName;

    /**
     * 大组
     */
    private String parentOrgName;

    /**
     * 小组
     */
    private String orgName;

    /**
     * 正常队列
     */
    private int currActivieTaskNum;

    /**
     * 优先队列
     */
    private int currPriorityNum;

    /**
     * 挂起队列
     */
    private int currInactiveTaskNum;

    /**
     * 是否接单
     */
    private String ifAccept;

    /**
     * 大组code
     */
    private String parentOrgCode;

    /**
     * 小组code
     */
    private String orgCode;
}
