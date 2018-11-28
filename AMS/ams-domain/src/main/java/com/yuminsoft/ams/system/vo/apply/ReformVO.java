package com.yuminsoft.ams.system.vo.apply;

import java.io.Serializable;

/**
 * 初、终审改派VO
 * @author wulj
 */
public class ReformVO implements Serializable {
    private static final long serialVersionUID = 6402436518996447002L;
    private String loanNo;// 借款编号
    private String ifNewLoanNo;// 是否是新生件
    private String zSIfNewLoanNo;// 是否是新生件
    private String rtfNodeState;// 流程节点状态
    private String version;// 版本号
    private String userCode;// 被改派用户
    private String targetUserCode;// 改派目标用户
    private String targetuserName;// 改派目标用户名
    private String personId;// 客户ID
    private String specialOrg;// 区域code
    private String applyType;// 申请件类型
    private String productCd;// 产品code
    private String remark;// 备注
    private String approvalPersonCode;
    private String finalPersonCode;
    //private String  fraudLevel;// 欺诈风险评估
	private String comCreditRating; // 综合信用评级
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getIfNewLoanNo() {
		return ifNewLoanNo;
	}
	public void setIfNewLoanNo(String ifNewLoanNo) {
		this.ifNewLoanNo = ifNewLoanNo;
	}
	public String getzSIfNewLoanNo() {
		return zSIfNewLoanNo;
	}
	public void setzSIfNewLoanNo(String zSIfNewLoanNo) {
		this.zSIfNewLoanNo = zSIfNewLoanNo;
	}
	public String getRtfNodeState() {
		return rtfNodeState;
	}
	public void setRtfNodeState(String rtfNodeState) {
		this.rtfNodeState = rtfNodeState;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getTargetUserCode() {
		return targetUserCode;
	}
	public void setTargetUserCode(String targetUserCode) {
		this.targetUserCode = targetUserCode;
	}
	public String getTargetuserName() {
		return targetuserName;
	}
	public void setTargetuserName(String targetuserName) {
		this.targetuserName = targetuserName;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getSpecialOrg() {
		return specialOrg;
	}
	public void setSpecialOrg(String specialOrg) {
		this.specialOrg = specialOrg;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getProductCd() {
		return productCd;
	}
	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getApprovalPersonCode() {
		return approvalPersonCode;
	}
	public void setApprovalPersonCode(String approvalPersonCode) {
		this.approvalPersonCode = approvalPersonCode;
	}
	public String getFinalPersonCode() {
		return finalPersonCode;
	}
	public void setFinalPersonCode(String finalPersonCode) {
		this.finalPersonCode = finalPersonCode;
	}
	public String getComCreditRating() {
		return comCreditRating;
	}
	public void setComCreditRating(String comCreditRating) {
		this.comCreditRating = comCreditRating;
	}
}
