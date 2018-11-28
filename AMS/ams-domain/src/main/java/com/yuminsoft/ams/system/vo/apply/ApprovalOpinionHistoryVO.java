package com.yuminsoft.ams.system.vo.apply;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 审批意见表
 * 
 * @author wangzx
 */
@Data
@ToString
public class ApprovalOpinionHistoryVO extends AbstractEntity {
	private static final long serialVersionUID = 606368600700043956L;

	private String loanNo;// 申请件编号

	private String rtfState;// 审批状态

	private String rtfNodeState;// 流程节点状态

	private String checkNodeState;// 复核状态(终审相关状态)

	private String approvalPerson;// 审批人员
	private String approvalPersonName;// 审批人员

	private String approvalProductCd;// 申请产品
	private String approvalProductCdName;// 申请产品

	private BigDecimal approvalCheckIncome;// 核实收入

	private BigDecimal approvalApplyLimit;// 申请金额

	private String approvalApplyTerm;// 申请期限

	private BigDecimal approvalLimit;// 审批额度

	private String approvalTerm;// 审批期限

	private BigDecimal approvalMonthPay;// 月还款额

	private BigDecimal approvalDebtTate;// 内部负债率

	private BigDecimal approvalAllDebtRate;// 总负债率

	private String approvalMemo;// 备注

	private Long nfcsId;// NFCS_ID,对应的上海资信ID

	private String largeGroup; // 当前大组
	private String largeGroupName; // 当前大组

	private String currentGroup;// 当前小组
	private String currentGroupName;// 当前小组

	private String currentRole;// 当前用户角色
	private String currentRoleName;// 当前用户角色
	private String createdByName;

}