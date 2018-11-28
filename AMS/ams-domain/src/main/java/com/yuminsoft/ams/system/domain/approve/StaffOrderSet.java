package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 员工接单设置表
 * @author fuhongxing
 */
public class StaffOrderSet extends AbstractEntity {
    //public static final String TABLE_NAME = "AMS_STAFF_ORDER_SET";

    /**
	 * 
	 */
	private static final long serialVersionUID = -880077619507693099L;

	private String staffCode;//员工工号

    private Integer normalQueueMax;//正常队列上限

    private Integer hangQueueMax;//挂起队列上限
    
    private String taskDefId;// 任务节点id初审(apply-check);终审(applyinfo-finalaudit)

    /**
     * <p>员工工号</p>
     * <p>员工工号</p>
     */
    public String getStaffCode() {
        return staffCode;
    }

    /**
     * <p>员工工号</p>
     * <p>员工工号</p>
     */
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    /**
     * <p>正常队列上限</p>
     * <p>正常队列上限</p>
     */
    public Integer getNormalQueueMax() {
        return normalQueueMax;
    }

    /**
     * <p>正常队列上限</p>
     * <p>正常队列上限</p>
     */
    public void setNormalQueueMax(Integer normalQueueMax) {
        this.normalQueueMax = normalQueueMax;
    }

    /**
     * <p>挂起队列上限</p>
     * <p>挂起队列上限</p>
     */
    public Integer getHangQueueMax() {
        return hangQueueMax;
    }

    /**
     * <p>挂起队列上限</p>
     * <p>挂起队列上限</p>
     */
    public void setHangQueueMax(Integer hangQueueMax) {
        this.hangQueueMax = hangQueueMax;
    }
    /**
     * <p>任务节点id初审</p>
     * <p>任务节点id初审</p>
     */
	public String getTaskDefId() {
		return taskDefId;
	}
	/**
     * <p>任务节点id初审</p>
     * <p>任务节点id初审</p>
     */
	public void setTaskDefId(String taskDefId) {
		this.taskDefId = taskDefId;
	}

}