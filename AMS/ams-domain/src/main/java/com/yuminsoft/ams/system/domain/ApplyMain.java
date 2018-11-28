package com.yuminsoft.ams.system.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 申请主表
 * @author fuhongxing
 */
public class ApplyMain extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public static final String TABLE_NAME = "AMS_APPLY_MAIN";

    private String loanNo;//申请件编号

    private String initProductCd;//原产品

    private String productCd;//申请产品

    private BigDecimal applyLmt;//申请金额,申请额度

    private String applyTerm;//申请期限

    private BigDecimal applyRate;//费率

    private String creditApplication;//贷款用途

    private BigDecimal sugLmt;//系统建议额度

    private BigDecimal accLmt;//审批额度,核准额度

    private Integer accTerm;//审批期限

    private Date accDate;//审批日期

    private String priority;//客户等级

    private String clientType;//客户类型

    private String rtfState;//审批状态

    private Integer pointResult;//评分值

    private String proNum;//流程实例号

    private String proName;//流程节点名称

    private String owningBranch;//门店

    private String owningBranchAttribute;//门店属性

    private Integer repayDate;//还款日

    private String status;//申请件状态

    private String branchManager;//客户经理

    private String director;//业务主任

    private String groupForDirector;//业务组

    private String contractBranch;//签约营业部

    private String loanBranch;//放款营业部

    private String manageBranch;//管理营业部

    private Date manageUpdateDate;//管理营业部变更时间

    private String appOrgName;//机构名称

    private BigDecimal ensureAmtAmount;//保证金

    private String appLoanPlan;//借款计划

    private Date signDate;//签约时间

    private String contractSource;//合同来源

    private String contractNum;//合同编号

    private BigDecimal contractLmt;//合同金额

    private Integer contractTrem;//合同期限

    private String applyBankName;//所属银行

    private String applyBankBranch;//开户行

    private String applyBankCardNo;//银行卡号

    private String bankPhone;//银行预留手机号

    private String thirdId;//第三方Id

    private String refuseCode;//拒绝原因码

    private String ifOldOrNewLogo;//是否新老客户标识

    private String ifPatchBolt;//是否补件

    private String ifPri;//是否加急

    private Integer ifUrgent;//加急等级

    private String ifRefuse;//是否拒绝

    private String ifLoanAgain;//是否结清再贷

    private String ifSuspectCheat;//是否疑似欺诈

    private String ifEnd;//是否处理完成件

    private String remark;//备注

    private String loanId;//债权ID

    private String checkPerson;//初审员

    private Date checkAllotDate;//初审分配时间

    private String finalPerson;//终审员

    private String finalRole;//终审权限

    private Date finalAllotDate;//终审分配时间

    private String approvalPerson;//协审员

    private String specialOrg;//机构

    private String specialPlan;//方案

    private BigDecimal amoutIncome;//收入证明金额

    private String ifCreditRecord;//有无信用记录

    private BigDecimal sysCheckLmt;//系统建议核实收入

    private BigDecimal sysAccLmt;//系统建议审批金额

    private Integer sysAccTrem;//系统建议审批期限
    
    private Date loanDate;//放款日期

    private String isrollback;//是否刚回退到录入修改

    private String applyType;//申请类型

    private String applyInputFlag;//申请录入标识

    private String ifGrey;//是否为灰名单

    private String lockTarget;//锁定标的

    private String appInputFlag;//APP进件标识

    private Date appApplyDate;//APP进件申请时间

    private String regularState;//规则状态

    private Date pushDate;//数信推送时间

    private String reasonShuxin;//数信拒绝原因



    /**
     * <p>申请件编号</p>
     * <p>申请件编号</p>
     */
    public String getLoanNo() {
        return loanNo;
    }

    /**
     * <p>申请件编号</p>
     * <p>申请件编号</p>
     */
    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    /**
     * <p>原产品</p>
     */
    public String getInitProductCd() {
        return initProductCd;
    }

    /**
     * <p>原产品</p>
     */
    public void setInitProductCd(String initProductCd) {
        this.initProductCd = initProductCd;
    }

    /**
     * <p>申请产品</p>
     */
    public String getProductCd() {
        return productCd;
    }

    /**
     * <p>申请产品</p>
     */
    public void setProductCd(String productCd) {
        this.productCd = productCd;
    }

    /**
     * <p>申请金额</p>
     * <p>申请额度</p>
     */
    public BigDecimal getApplyLmt() {
        return applyLmt;
    }

    /**
     * <p>申请金额</p>
     * <p>申请额度</p>
     */
    public void setApplyLmt(BigDecimal applyLmt) {
        this.applyLmt = applyLmt;
    }

    /**
     * <p>申请期限</p>
     */
    public String getApplyTerm() {
        return applyTerm;
    }

    /**
     * <p>申请期限</p>
     */
    public void setApplyTerm(String applyTerm) {
        this.applyTerm = applyTerm;
    }

    /**
     * <p>费率</p>
     */
    public BigDecimal getApplyRate() {
        return applyRate;
    }

    /**
     * <p>费率</p>
     */
    public void setApplyRate(BigDecimal applyRate) {
        this.applyRate = applyRate;
    }

    /**
     * <p>贷款用途</p>
     */
    public String getCreditApplication() {
        return creditApplication;
    }

    /**
     * <p>贷款用途</p>
     */
    public void setCreditApplication(String creditApplication) {
        this.creditApplication = creditApplication;
    }

    /**
     * <p>系统建议额度</p>
     */
    public BigDecimal getSugLmt() {
        return sugLmt;
    }

    /**
     * <p>系统建议额度</p>
     */
    public void setSugLmt(BigDecimal sugLmt) {
        this.sugLmt = sugLmt;
    }

    /**
     * <p>审批额度</p>
     * <p>核准额度</p>
     */
    public BigDecimal getAccLmt() {
        return accLmt;
    }

    /**
     * <p>审批额度</p>
     * <p>核准额度</p>
     */
    public void setAccLmt(BigDecimal accLmt) {
        this.accLmt = accLmt;
    }

    /**
     * <p>审批期限</p>
     */
    public Integer getAccTerm() {
        return accTerm;
    }

    /**
     * <p>审批期限</p>
     */
    public void setAccTerm(Integer accTerm) {
        this.accTerm = accTerm;
    }

    /**
     * <p>审批日期</p>
     */
    public Date getAccDate() {
        return accDate;
    }

    /**
     * <p>审批日期</p>
     */
    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    /**
     * <p>客户等级</p>
     */
    public String getPriority() {
        return priority;
    }

    /**
     * <p>客户等级</p>
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * <p>客户类型</p>
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * <p>客户类型</p>
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * <p>审批状态</p>
     */
    public String getRtfState() {
        return rtfState;
    }

    /**
     * <p>审批状态</p>
     */
    public void setRtfState(String rtfState) {
        this.rtfState = rtfState;
    }

    /**
     * <p>评分值</p>
     */
    public Integer getPointResult() {
        return pointResult;
    }

    /**
     * <p>评分值</p>
     */
    public void setPointResult(Integer pointResult) {
        this.pointResult = pointResult;
    }

    /**
     * <p>流程实例号</p>
     * <p>流程实例号</p>
     */
    public String getProNum() {
        return proNum;
    }

    /**
     * <p>流程实例号</p>
     * <p>流程实例号</p>
     */
    public void setProNum(String proNum) {
        this.proNum = proNum;
    }

    /**
     * <p>流程节点名称</p>
     */
    public String getProName() {
        return proName;
    }

    /**
     * <p>流程节点名称</p>
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     * <p>门店</p>
     */
    public String getOwningBranch() {
        return owningBranch;
    }

    /**
     * <p>门店</p>
     */
    public void setOwningBranch(String owningBranch) {
        this.owningBranch = owningBranch;
    }

    /**
     * <p>门店属性</p>
     */
    public String getOwningBranchAttribute() {
        return owningBranchAttribute;
    }

    /**
     * <p>门店属性</p>
     */
    public void setOwningBranchAttribute(String owningBranchAttribute) {
        this.owningBranchAttribute = owningBranchAttribute;
    }

    /**
     * <p>还款日</p>
     */
    public Integer getRepayDate() {
        return repayDate;
    }

    /**
     * <p>还款日</p>
     */
    public void setRepayDate(Integer repayDate) {
        this.repayDate = repayDate;
    }

    /**
     * <p>申请件状态</p>
     */
    public String getStatus() {
        return status;
    }

    /**
     * <p>申请件状态</p>
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * <p>客户经理</p>
     */
    public String getBranchManager() {
        return branchManager;
    }

    /**
     * <p>客户经理</p>
     */
    public void setBranchManager(String branchManager) {
        this.branchManager = branchManager;
    }

    /**
     * <p>业务主任</p>
     */
    public String getDirector() {
        return director;
    }

    /**
     * <p>业务主任</p>
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * <p>业务组</p>
     */
    public String getGroupForDirector() {
        return groupForDirector;
    }

    /**
     * <p>业务组</p>
     */
    public void setGroupForDirector(String groupForDirector) {
        this.groupForDirector = groupForDirector;
    }

    /**
     * <p>签约营业部</p>
     */
    public String getContractBranch() {
        return contractBranch;
    }

    /**
     * <p>签约营业部</p>
     */
    public void setContractBranch(String contractBranch) {
        this.contractBranch = contractBranch;
    }

    /**
     * <p>放款营业部</p>
     */
    public String getLoanBranch() {
        return loanBranch;
    }

    /**
     * <p>放款营业部</p>
     */
    public void setLoanBranch(String loanBranch) {
        this.loanBranch = loanBranch;
    }

    /**
     * <p>管理营业部</p>
     */
    public String getManageBranch() {
        return manageBranch;
    }

    /**
     * <p>管理营业部</p>
     */
    public void setManageBranch(String manageBranch) {
        this.manageBranch = manageBranch;
    }

    /**
     * <p>管理营业部变更时间</p>
     */
    public Date getManageUpdateDate() {
        return manageUpdateDate;
    }

    /**
     * <p>管理营业部变更时间</p>
     */
    public void setManageUpdateDate(Date manageUpdateDate) {
        this.manageUpdateDate = manageUpdateDate;
    }

    /**
     * <p>机构名称</p>
     */
    public String getAppOrgName() {
        return appOrgName;
    }

    /**
     * <p>机构名称</p>
     */
    public void setAppOrgName(String appOrgName) {
        this.appOrgName = appOrgName;
    }

    /**
     * <p>保证金</p>
     */
    public BigDecimal getEnsureAmtAmount() {
        return ensureAmtAmount;
    }

    /**
     * <p>保证金</p>
     */
    public void setEnsureAmtAmount(BigDecimal ensureAmtAmount) {
        this.ensureAmtAmount = ensureAmtAmount;
    }

    /**
     * <p>借款计划</p>
     */
    public String getAppLoanPlan() {
        return appLoanPlan;
    }

    /**
     * <p>借款计划</p>
     */
    public void setAppLoanPlan(String appLoanPlan) {
        this.appLoanPlan = appLoanPlan;
    }

    /**
     * <p>签约时间</p>
     */
    public Date getSignDate() {
        return signDate;
    }

    /**
     * <p>签约时间</p>
     */
    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    /**
     * <p>合同来源</p>
     */
    public String getContractSource() {
        return contractSource;
    }

    /**
     * <p>合同来源</p>
     */
    public void setContractSource(String contractSource) {
        this.contractSource = contractSource;
    }

    /**
     * <p>合同编号</p>
     */
    public String getContractNum() {
        return contractNum;
    }

    /**
     * <p>合同编号</p>
     */
    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    /**
     * <p>合同金额</p>
     */
    public BigDecimal getContractLmt() {
        return contractLmt;
    }

    /**
     * <p>合同金额</p>
     */
    public void setContractLmt(BigDecimal contractLmt) {
        this.contractLmt = contractLmt;
    }

    /**
     * <p>合同期限</p>
     */
    public Integer getContractTrem() {
        return contractTrem;
    }

    /**
     * <p>合同期限</p>
     */
    public void setContractTrem(Integer contractTrem) {
        this.contractTrem = contractTrem;
    }

    /**
     * <p>所属银行</p>
     */
    public String getApplyBankName() {
        return applyBankName;
    }

    /**
     * <p>所属银行</p>
     */
    public void setApplyBankName(String applyBankName) {
        this.applyBankName = applyBankName;
    }

    /**
     * <p>开户行</p>
     */
    public String getApplyBankBranch() {
        return applyBankBranch;
    }

    /**
     * <p>开户行</p>
     */
    public void setApplyBankBranch(String applyBankBranch) {
        this.applyBankBranch = applyBankBranch;
    }

    /**
     * <p>银行卡号</p>
     */
    public String getApplyBankCardNo() {
        return applyBankCardNo;
    }

    /**
     * <p>银行卡号</p>
     */
    public void setApplyBankCardNo(String applyBankCardNo) {
        this.applyBankCardNo = applyBankCardNo;
    }

    /**
     * <p>银行预留手机号</p>
     */
    public String getBankPhone() {
        return bankPhone;
    }

    /**
     * <p>银行预留手机号</p>
     */
    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    /**
     * <p>第三方Id</p>
     */
    public String getThirdId() {
        return thirdId;
    }

    /**
     * <p>第三方Id</p>
     */
    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    /**
     * <p>拒绝原因码</p>
     */
    public String getRefuseCode() {
        return refuseCode;
    }

    /**
     * <p>拒绝原因码</p>
     */
    public void setRefuseCode(String refuseCode) {
        this.refuseCode = refuseCode;
    }

    /**
     * <p>是否新老客户标识</p>
     */
    public String getIfOldOrNewLogo() {
        return ifOldOrNewLogo;
    }

    /**
     * <p>是否新老客户标识</p>
     */
    public void setIfOldOrNewLogo(String ifOldOrNewLogo) {
        this.ifOldOrNewLogo = ifOldOrNewLogo;
    }

    /**
     * <p>是否补件</p>
     */
    public String getIfPatchBolt() {
        return ifPatchBolt;
    }

    /**
     * <p>是否补件</p>
     */
    public void setIfPatchBolt(String ifPatchBolt) {
        this.ifPatchBolt = ifPatchBolt;
    }

    /**
     * <p>是否加急</p>
     */
    public String getIfPri() {
        return ifPri;
    }

    /**
     * <p>是否加急</p>
     */
    public void setIfPri(String ifPri) {
        this.ifPri = ifPri;
    }

    /**
     * <p>加急等级</p>
     */
    public Integer getIfUrgent() {
        return ifUrgent;
    }

    /**
     * <p>加急等级</p>
     */
    public void setIfUrgent(Integer ifUrgent) {
        this.ifUrgent = ifUrgent;
    }

    /**
     * <p>是否拒绝</p>
     * <p>///@com.allinfinance.aps.param.def.enums.Indicator</p>
     */
    public String getIfRefuse() {
        return ifRefuse;
    }

    /**
     * <p>是否拒绝</p>
     * <p>///@com.allinfinance.aps.param.def.enums.Indicator</p>
     */
    public void setIfRefuse(String ifRefuse) {
        this.ifRefuse = ifRefuse;
    }

    /**
     * <p>是否结清再贷</p>
     */
    public String getIfLoanAgain() {
        return ifLoanAgain;
    }

    /**
     * <p>是否结清再贷</p>
     */
    public void setIfLoanAgain(String ifLoanAgain) {
        this.ifLoanAgain = ifLoanAgain;
    }

    /**
     * <p>是否疑似欺诈</p>
     */
    public String getIfSuspectCheat() {
        return ifSuspectCheat;
    }

    /**
     * <p>是否疑似欺诈</p>
     */
    public void setIfSuspectCheat(String ifSuspectCheat) {
        this.ifSuspectCheat = ifSuspectCheat;
    }

    /**
     * <p>是否处理完成件</p>
     */
    public String getIfEnd() {
        return ifEnd;
    }

    /**
     * <p>是否处理完成件</p>
     */
    public void setIfEnd(String ifEnd) {
        this.ifEnd = ifEnd;
    }

    /**
     * <p>备注</p>
     * <p>备注</p>
     */
    public String getRemark() {
        return remark;
    }

    /**
     * <p>备注</p>
     * <p>备注</p>
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * <p>债权ID</p>
     */
    public String getLoanId() {
        return loanId;
    }

    /**
     * <p>债权ID</p>
     */
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    /**
     * <p>初审员</p>
     */
    public String getCheckPerson() {
        return checkPerson;
    }

    /**
     * <p>初审员</p>
     */
    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    /**
     * <p>初审分配时间</p>
     */
    public Date getCheckAllotDate() {
        return checkAllotDate;
    }

    /**
     * <p>初审分配时间</p>
     */
    public void setCheckAllotDate(Date checkAllotDate) {
        this.checkAllotDate = checkAllotDate;
    }

    /**
     * <p>终审员</p>
     */
    public String getFinalPerson() {
        return finalPerson;
    }

    /**
     * <p>终审员</p>
     */
    public void setFinalPerson(String finalPerson) {
        this.finalPerson = finalPerson;
    }

    /**
     * <p>终审权限</p>
     */
    public String getFinalRole() {
        return finalRole;
    }

    /**
     * <p>终审权限</p>
     */
    public void setFinalRole(String finalRole) {
        this.finalRole = finalRole;
    }

    /**
     * <p>终审分配时间</p>
     */
    public Date getFinalAllotDate() {
        return finalAllotDate;
    }

    /**
     * <p>终审分配时间</p>
     */
    public void setFinalAllotDate(Date finalAllotDate) {
        this.finalAllotDate = finalAllotDate;
    }

    /**
     * <p>协审员</p>
     */
    public String getApprovalPerson() {
        return approvalPerson;
    }

    /**
     * <p>协审员</p>
     */
    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    }

    /**
     * <p>机构</p>
     */
    public String getSpecialOrg() {
        return specialOrg;
    }

    /**
     * <p>机构</p>
     */
    public void setSpecialOrg(String specialOrg) {
        this.specialOrg = specialOrg;
    }

    /**
     * <p>方案</p>
     */
    public String getSpecialPlan() {
        return specialPlan;
    }

    /**
     * <p>方案</p>
     */
    public void setSpecialPlan(String specialPlan) {
        this.specialPlan = specialPlan;
    }

    /**
     * <p>收入证明金额</p>
     */
    public BigDecimal getAmoutIncome() {
        return amoutIncome;
    }

    /**
     * <p>收入证明金额</p>
     */
    public void setAmoutIncome(BigDecimal amoutIncome) {
        this.amoutIncome = amoutIncome;
    }

    /**
     * <p>有无信用记录</p>
     */
    public String getIfCreditRecord() {
        return ifCreditRecord;
    }

    /**
     * <p>有无信用记录</p>
     */
    public void setIfCreditRecord(String ifCreditRecord) {
        this.ifCreditRecord = ifCreditRecord;
    }

    /**
     * <p>系统建议核实收入</p>
     */
    public BigDecimal getSysCheckLmt() {
        return sysCheckLmt;
    }

    /**
     * <p>系统建议核实收入</p>
     */
    public void setSysCheckLmt(BigDecimal sysCheckLmt) {
        this.sysCheckLmt = sysCheckLmt;
    }

    /**
     * <p>系统建议审批金额</p>
     */
    public BigDecimal getSysAccLmt() {
        return sysAccLmt;
    }

    /**
     * <p>系统建议审批金额</p>
     */
    public void setSysAccLmt(BigDecimal sysAccLmt) {
        this.sysAccLmt = sysAccLmt;
    }

    /**
     * <p>系统建议审批期限</p>
     */
    public Integer getSysAccTrem() {
        return sysAccTrem;
    }

    /**
     * <p>系统建议审批期限</p>
     */
    public void setSysAccTrem(Integer sysAccTrem) {
        this.sysAccTrem = sysAccTrem;
    }

    /**
     * <p>放款日期</p>
     */
    public Date getLoanDate() {
        return loanDate;
    }

    /**
     * <p>放款日期</p>
     */
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * <p>是否刚回退到录入修改</p>
     */
    public String getIsrollback() {
        return isrollback;
    }

    /**
     * <p>是否刚回退到录入修改</p>
     */
    public void setIsrollback(String isrollback) {
        this.isrollback = isrollback;
    }

    /**
     * <p>申请类型</p>
     * <p>申请类型</p>
     */
    public String getApplyType() {
        return applyType;
    }

    /**
     * <p>申请类型</p>
     * <p>申请类型</p>
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * <p>申请录入标识</p>
     */
    public String getApplyInputFlag() {
        return applyInputFlag;
    }

    /**
     * <p>申请录入标识</p>
     */
    public void setApplyInputFlag(String applyInputFlag) {
        this.applyInputFlag = applyInputFlag;
    }

    /**
     * <p>是否为灰名单</p>
     */
    public String getIfGrey() {
        return ifGrey;
    }

    /**
     * <p>是否为灰名单</p>
     */
    public void setIfGrey(String ifGrey) {
        this.ifGrey = ifGrey;
    }

    /**
     * <p>锁定标的</p>
     */
    public String getLockTarget() {
        return lockTarget;
    }

    /**
     * <p>锁定标的</p>
     */
    public void setLockTarget(String lockTarget) {
        this.lockTarget = lockTarget;
    }

    /**
     * <p>APP进件标识</p>
     * <p>app_input</p>
     */
    public String getAppInputFlag() {
        return appInputFlag;
    }

    /**
     * <p>APP进件标识</p>
     * <p>app_input</p>
     */
    public void setAppInputFlag(String appInputFlag) {
        this.appInputFlag = appInputFlag;
    }

    /**
     * <p>APP进件申请时间</p>
     */
    public Date getAppApplyDate() {
        return appApplyDate;
    }

    /**
     * <p>APP进件申请时间</p>
     */
    public void setAppApplyDate(Date appApplyDate) {
        this.appApplyDate = appApplyDate;
    }

    /**
     * <p>规则状态</p>
     */
    public String getRegularState() {
        return regularState;
    }

    /**
     * <p>规则状态</p>
     */
    public void setRegularState(String regularState) {
        this.regularState = regularState;
    }

    /**
     * <p>数信推送时间</p>
     */
    public Date getPushDate() {
        return pushDate;
    }

    /**
     * <p>数信推送时间</p>
     */
    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    /**
     * <p>数信拒绝原因</p>
     */
    public String getReasonShuxin() {
        return reasonShuxin;
    }

    /**
     * <p>数信拒绝原因</p>
     */
    public void setReasonShuxin(String reasonShuxin) {
        this.reasonShuxin = reasonShuxin;
    }

}