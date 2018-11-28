package com.yuminsoft.ams.system.vo.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;
/**
 * @Desc: 质检结论信息表Vo
 * @Author: phb
 * @Date: 2017/5/11 14:19
 */
public class QualityCheckResVo extends AbstractEntity {

    private String checkResult;
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
     * 质检流水号
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
     * 质检状态(quality_complete:完成质检   quality_recheck:申请复核  quality_save:暂存  quality_recheck_wait:申请复核等待)
     */
    private String status;
    
    //checkInfo表的字段
    /**
     * 质检员
     */
    private String checkUser;
    /**
     * 初审工号
     */
    private String checkPerson;
    /**
     * 初审姓名
     */
    private String checkPersonName;
    /**
     * 终审工号
     */
    private String finalPerson;
    /**
     * 终审姓名
     */
    private String finalPersonName;
    
    /**
     * quality_firstApprove-初审质检   quality_finishtApprove-终审质检   quality_leaderApprove-领导质检  
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

    public String getApprovePerson() {
		return approvePerson;
	}

	public void setApprovePerson(String approvePerson) {
		this.approvePerson = approvePerson;
	}

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckPersonName() {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    public String getFinalPerson() {
        return finalPerson;
    }

    public void setFinalPerson(String finalPerson) {
        this.finalPerson = finalPerson;
    }

    public String getFinalPersonName() {
        return finalPersonName;
    }

    public void setFinalPersonName(String finalPersonName) {
        this.finalPersonName = finalPersonName;
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