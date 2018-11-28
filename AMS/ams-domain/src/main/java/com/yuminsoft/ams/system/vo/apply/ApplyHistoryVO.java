package com.yuminsoft.ams.system.vo.apply;

import com.ymkj.ams.api.vo.request.audit.first.ReqQqReturnReasonVO;

import java.io.Serializable;
import java.util.List;

/**
 * 用于保存初审终审人员操作记录
 * 
 * @author dmz
 * @date 2017年3月16日
 */
public class ApplyHistoryVO implements Serializable {
	private static final long serialVersionUID = 8333503030372923946L;
	private String loanNo;// 申请件编号
	private String name;// 姓名
	private String idNo;// 身份证号码
	private String proNum;// 流程实例号
	private String proName;// 流程节点名称
	private String rtfState;// 审批状态
	private String rtfNodeState;// 流程节点状态
	private String checkNodeState;// 初审复核确认(终审相关状态)
	private String firstReason;// 一级原因
	private String firstReasonText;// 一级原因text
	private String secondReason;// 二级原因
	private String secondReasonText;// 二级原因text
	private String checkPerson;// 初审员
	private String checkComplex;// 初审复核人员
	private String finalPerson;// 终审员
	private String approvalPerson;// 协审员
	private String finalRole;// 终审权限
	private String patchBolt;// 补件信息
	private String remark;// 备注
	private int version;// 版本控制
	private String ip = "127.0.0.1";// 客户端ip
	private String remarkBlack;// 标记是否记录黑灰名单
	private String applyType;// 申请类型(新申请:NEW;追加贷款:TOPUP; 结清再贷:RELOAN)
	private String approveProduct;// 申请产品code
	private String approvalLeader;  // 用户组长
	private String approvalDirector; // 信审主管
	private String approvalManager;   // 信审经理

	private List<ReqQqReturnReasonVO> returnReasons;//前前进件退回原因列表
	
	/*以下字段是为了终审操作的时候使用的一些特殊参数*/
	/**
	 * 1：优先件，2正常件，3挂起件
	 */
	private String taskType;

	/**
     *退回类型 （退回门店或者退回初审）
     */
    private String backType;

    /**
     * 是否是偿债能力不足标识（debtRatio_Y）
     */
    private String conType;

	/**
	 * 是否自动拒绝(N-否,Y-是)
	 */
	private String autoRefuse;
	
	/**
	 * 处理人code
	 */
	private String handleCode;

	public String getHandleCode() {
		return handleCode;
	}

	public void setHandleCode(String handleCode) {
		this.handleCode = handleCode;
	}

	public ApplyHistoryVO() {

	}

	public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public String getBackType()
    {
        return backType;
    }

    public void setBackType(String backType)
    {
        this.backType = backType;
    }

    public String getConType()
    {
        return conType;
    }

    public void setConType(String conType)
    {
        this.conType = conType;
    }

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getProNum() {
		return proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getRtfState() {
		return rtfState;
	}

	public void setRtfState(String rtfState) {
		this.rtfState = rtfState;
	}

	public String getRtfNodeState() {
		return rtfNodeState;
	}

	public void setRtfNodeState(String rtfNodeState) {
		this.rtfNodeState = rtfNodeState;
	}

	public String getCheckNodeState() {
		return checkNodeState;
	}

	public void setCheckNodeState(String checkNodeState) {
		this.checkNodeState = checkNodeState;
	}

	public String getFirstReason() {
		return firstReason;
	}

	public void setFirstReason(String firstReason) {
		this.firstReason = firstReason;
	}

	public String getFirstReasonText() {
		return firstReasonText;
	}

	public void setFirstReasonText(String firstReasonText) {
		this.firstReasonText = firstReasonText;
	}

	public String getSecondReason() {
		return secondReason;
	}

	public void setSecondReason(String secondReason) {
		this.secondReason = secondReason;
	}

	public String getSecondReasonText() {
		return secondReasonText;
	}

	public void setSecondReasonText(String secondReasonText) {
		this.secondReasonText = secondReasonText;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public String getCheckComplex() {
		return checkComplex;
	}

	public void setCheckComplex(String checkComplex) {
		this.checkComplex = checkComplex;
	}

	public String getFinalPerson() {
		return finalPerson;
	}

	public void setFinalPerson(String finalPerson) {
		this.finalPerson = finalPerson;
	}

	public String getApprovalPerson() {
		return approvalPerson;
	}

	public void setApprovalPerson(String approvalPerson) {
		this.approvalPerson = approvalPerson;
	}

	public String getFinalRole() {
		return finalRole;
	}

	public void setFinalRole(String finalRole) {
		this.finalRole = finalRole;
	}

	public String getPatchBolt() {
		return patchBolt;
	}

	public void setPatchBolt(String patchBolt) {
		this.patchBolt = patchBolt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemarkBlack() {
		return remarkBlack;
	}

	public void setRemarkBlack(String remarkBlack) {
		this.remarkBlack = remarkBlack;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getApproveProduct() {
		return approveProduct;
	}

	public void setApproveProduct(String approveProduct) {
		this.approveProduct = approveProduct;
	}

	public String getApprovalLeader() {
		return approvalLeader;
	}

	public void setApprovalLeader(String approvalLeader) {
		this.approvalLeader = approvalLeader;
	}

	public String getApprovalDirector() {
		return approvalDirector;
	}

	public void setApprovalDirector(String approvalDirector) {
		this.approvalDirector = approvalDirector;
	}

	public String getApprovalManager() {
		return approvalManager;
	}

	public void setApprovalManager(String approvalManager) {
		this.approvalManager = approvalManager;
	}

	public String getAutoRefuse() {
		return autoRefuse;
	}

	public void setAutoRefuse(String autoRefuse) {
		this.autoRefuse = autoRefuse;
	}

	public List<ReqQqReturnReasonVO> getReturnReasons() {
		return returnReasons;
	}

	public void setReturnReasons(List<ReqQqReturnReasonVO> returnReasons) {
		this.returnReasons = returnReasons;
	}
}
