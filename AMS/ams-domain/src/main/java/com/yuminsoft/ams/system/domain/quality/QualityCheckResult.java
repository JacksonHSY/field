package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 质检结论信息表
 *
 * @author fuhongxing
 */
public class QualityCheckResult extends AbstractEntity {

    private static final long serialVersionUID = 5287270081674228797L;
    /**
     * 无差错-->E_000000-->D
     * 预警-->E_000001-->E
     * 建议-->E_000002-->C
     * 一般差错-->E_000003-->B
     * 重大差错-->E_000004-->A
     */
    private String checkResult;
    /**
     * 质检结论 0初审  1终审
     */
    private String checkError;
    /**
     * 差错代码
     */
    private String errorCode;
    /**
     * 质检意见
     */
    private String checkView;
    /**
     * 组长/主管
     */
    private String approvalLeader;
    /**
     * 质检流水号，关联质检信息表id
     */
    private Long qualityCheckId;
    /**
     * 流程表示 0：未进入反馈流程 1：进入流程 2：流程结束
     */
    private Long feedbackCode;
    /**
     *审核人员
     */
    private String approvePerson;
    /**
     * 质检状态(quality_complete:完成质检   quality_recheck:申请复核   quality_save:暂存  quality_recheck_wait:申请复核等待)
     */
    private String status;
    
    /**
     * 质检意见类型(checkPart:初审质检意见, finalPart:终审质检意见, checkLeaderPart:初审组长质检意见)
     */
    private String checkPart;
    
    /**
     * 申请复核对象
     */
    private String recheckPerson;

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckError() {
        return checkError;
    }

    public void setCheckError(String checkError) {
        this.checkError = checkError;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCheckView() {
        return checkView;
    }

    public void setCheckView(String checkView) {
        this.checkView = checkView;
    }

    public String getApprovalLeader() {
		return approvalLeader;
	}

	public void setApprovalLeader(String approvalLeader) {
		this.approvalLeader = approvalLeader;
	}

	public Long getQualityCheckId() {
        return qualityCheckId;
    }

    public void setQualityCheckId(Long qualityCheckId) {
        this.qualityCheckId = qualityCheckId;
    }

    public Long getFeedbackCode() {
        return feedbackCode;
    }

    public void setFeedbackCode(Long feedbackCode) {
        this.feedbackCode = feedbackCode;
    }
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovePerson() {
		return approvePerson;
	}

	public void setApprovePerson(String approvePerson) {
		this.approvePerson = approvePerson;
	}

	public String getCheckPart() {
		return checkPart;
	}

	public void setCheckPart(String checkPart) {
		this.checkPart = checkPart;
	}

	public String getRecheckPerson() {
		return recheckPerson;
	}

	public void setRecheckPerson(String recheckPerson) {
		this.recheckPerson = recheckPerson;
	}
}