package com.yuminsoft.ams.system.domain.approve;

import java.util.Date;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 员工接单任务表
 * 
 * @author dmz
 * @modified wulj
 * @date 2017年2月15日
 */
@Data
@ToString
public class StaffOrderTask extends AbstractEntity {
	private static final long serialVersionUID = -4479890070895479028L;
	// Fields
	private String staffCode;// 员工编号
	private String taskDefId;// 任务节点id初审(apply-check);终审(applyinfo-finalaudit)
	private String parentOrgCode;// 父级机构代码
	private String orgCode;// 机构编码
	private String orgPath;// 机构路径
	private Integer currActivieTaskNum;// 正常队列数
	private Integer currPriorityNum;// 优先队列数
	private Integer currInactiveTaskNum;// 挂起队列数
	private String ifAccept;// 是否接单Y/N
	private Date waitTime;// 等待时间
	private Integer version;// 版本控制
	private String finalAuditLevel;// 终审审批级别
	private String status;// 员工状态(0-正常;1-非正常)
	private String orgName;//小组名称
	private String parentOrgName;//大组名称
	private String staffName;//员工名称
	
}
