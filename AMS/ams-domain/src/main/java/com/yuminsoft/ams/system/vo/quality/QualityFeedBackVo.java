package com.yuminsoft.ams.system.vo.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import java.util.Date;
import java.util.List;

/**
 * @Desc: 质检反馈Vo
 * @Author: phb
 * @Date: 2017/5/12 11:25
 */
public class QualityFeedBackVo extends AbstractEntity {

    private String type;

    private String opinion;

    private Long checkResId;

    private String nodeName;

    private String checkType;

    private String checkError;

    private String checkResult;

    private String checkView;

    private String errorCode;

    private String qualityLeader;

    private Long qualityCheckId;

    private Long feedbackCode;

    private String createdBy;

    private Long approveHistoryId;

    private Long status;

    private String loanNo;

    private Integer approvalStatus;

    private Date assignDate;

    private String checkStatus;

    private String checkUser;

    private Date endDate;

    private String redundant;

    private Date samplingDate;

    private String source;

    private String isClosed;

    private String isRegular;

    private String isChoiced;

    private String customerName;

    private String idNo;

    private String customerType;

    private String owningBranceId;

    private String owningBrance;

    private Date applyDate;

    private String productName ;

    private String rtfState;

    private String checkPerson;

    private String checkPersonName;

    private String finalPerson;

    private String finalPersonName;

    private Date approveDate;

    private String assignType;

    private Integer version;


    /**
     * 登录用户
     */
    private String loginUser;
    /**
     *质检结论流水号集合
     */
    private List<Long> checkResIds;
    /**
     * 时间参数
     */
    private String assignDateStart;
    private String assignDateEnd;
    private String endDateStart;
    private String endDateEnd;
    /**
     * 初审小组组长code
     */
    private String checkLeader;
    /**
     * 初审小组组长name
     */
    private String checkLeaderName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Long getCheckResId() {
        return checkResId;
    }

    public void setCheckResId(Long checkResId) {
        this.checkResId = checkResId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCheckError() {
        return checkError;
    }

    public void setCheckError(String checkError) {
        this.checkError = checkError;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckView() {
        return checkView;
    }

    public void setCheckView(String checkView) {
        this.checkView = checkView;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getQualityLeader() {
        return qualityLeader;
    }

    public void setQualityLeader(String qualityLeader) {
        this.qualityLeader = qualityLeader;
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

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getApproveHistoryId() {
        return approveHistoryId;
    }

    public void setApproveHistoryId(Long approveHistoryId) {
        this.approveHistoryId = approveHistoryId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRedundant() {
        return redundant;
    }

    public void setRedundant(String redundant) {
        this.redundant = redundant;
    }

    public Date getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(Date samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }

    public String getIsChoiced() {
        return isChoiced;
    }

    public void setIsChoiced(String isChoiced) {
        this.isChoiced = isChoiced;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getOwningBranceId() {
        return owningBranceId;
    }

    public void setOwningBranceId(String owningBranceId) {
        this.owningBranceId = owningBranceId;
    }

    public String getOwningBrance() {
        return owningBrance;
    }

    public void setOwningBrance(String owningBrance) {
        this.owningBrance = owningBrance;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRtfState() {
        return rtfState;
    }

    public void setRtfState(String rtfState) {
        this.rtfState = rtfState;
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

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public List<Long> getCheckResIds() {
        return checkResIds;
    }

    public void setCheckResIds(List<Long> checkResIds) {
        this.checkResIds = checkResIds;
    }

    public String getAssignDateStart() {
        return assignDateStart;
    }

    public void setAssignDateStart(String assignDateStart) {
        this.assignDateStart = assignDateStart;
    }

    public String getAssignDateEnd() {
        return assignDateEnd;
    }

    public void setAssignDateEnd(String assignDateEnd) {
        this.assignDateEnd = assignDateEnd;
    }

    public String getEndDateStart() {
        return endDateStart;
    }

    public void setEndDateStart(String endDateStart) {
        this.endDateStart = endDateStart;
    }

    public String getEndDateEnd() {
        return endDateEnd;
    }

    public void setEndDateEnd(String endDateEnd) {
        this.endDateEnd = endDateEnd;
    }

    public String getCheckLeader() {
        return checkLeader;
    }

    public void setCheckLeader(String checkLeader) {
        this.checkLeader = checkLeader;
    }

    public String getCheckLeaderName() {
        return checkLeaderName;
    }

    public void setCheckLeaderName(String checkLeaderName) {
        this.checkLeaderName = checkLeaderName;
    }
}
