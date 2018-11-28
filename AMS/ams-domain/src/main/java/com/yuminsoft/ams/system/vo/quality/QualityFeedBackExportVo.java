package com.yuminsoft.ams.system.vo.quality;

/**
 * @Desc: 质检反馈导出VO
 * @Author: phb
 * @Date: 2017/5/6 14:52
 */
public class QualityFeedBackExportVo {

    /**
     * 申请件编号
     */
    private String loanNo;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 身份证号码
     */
    private String idNo;
    /**
     * 初审人员
     */
    private String checkPersonName;
    /**
     * 终审人员
     */
    private String finalPersonName;
    /**
     * 初审小组组长
     */
    private String checkGroupLeader;
    /**
     * 差错代码
     */
    private String errorCode;
    /**
     * 质检意见
     */
    private String checkView;

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

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getCheckPersonName() {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    public String getFinalPersonName() {
        return finalPersonName;
    }

    public void setFinalPersonName(String finalPersonName) {
        this.finalPersonName = finalPersonName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCheckGroupLeader() {
        return checkGroupLeader;
    }

    public void setCheckGroupLeader(String checkGroupLeader) {
        this.checkGroupLeader = checkGroupLeader;
    }

    public String getCheckView() {
        return checkView;
    }

    public void setCheckView(String checkView) {
        this.checkView = checkView;
    }
}
