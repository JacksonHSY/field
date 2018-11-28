package com.yuminsoft.ams.api.vo.request;

/**
 * 定时任务信息
 * @author fuhongxing
 */
public class ReqJob extends Request {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6677626584160833576L;
	
	/** 任务名称 */
    private String jobName;
    
    /** 任务分组 */
    private String jobGroup;
    
    /** 任务状态 0禁用 1启用 2删除*/
    private String jobStatus;
    
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
    
}