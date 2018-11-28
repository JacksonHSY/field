package com.yuminsoft.ams.system.vo.apply;

import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@ToString
public class ApprovalSaveVO implements Serializable {

	private static final long serialVersionUID = 6393644661542584576L;

	private String loanNo;// 借款编号
	private String approvalPerson;// 审批员

	// 资料核对
	private ApproveCheckInfo approveCheckInfo;
	private List<ApproveCheckStatement> approveCheckStatementList;

	// 负债信息
	private BigDecimal creditTotalLimit;			// 信用卡总额度
	private BigDecimal creditUsedLimit;				// 信用卡已用额度
	private BigDecimal creditDebt;					// 信用卡负债
	private String[] debtType;						// 负债类型(TOPUPLOAN:追加贷款;FASTLOAN:速贷;CREDITLOAN:信用贷)
	private BigDecimal[] creditLoanLimit;			// 信用卡贷款额度
	private Integer[] creditLoanTerm;

	// 信用卡贷款期限
	private BigDecimal[] creditLoanDebt;			// 信用卡贷款负债
	private BigDecimal outDebtTotal;				// 外部负债总额
	private BigDecimal outCreditDebtTotal; 			// 外部信用负债总额
	private BigDecimal monthlyTotalLiabilities;		// 月总负债
	private BigInteger[] creditNo;					// 贷款对应的编号
	private String fastLoanSituation;				// 速度还款情况-正常:NORMAL;逾期:OVERDUE;结清:SETTLE
	private String ifBlackRoster; 				// 是否是灰名单Y是N否

	// 借款信息&& 审批信息
	private String approvalProductCd;				// 审批产品
	private String productCd;						// 申请产品
	private BigDecimal approvalCheckIncome;			// 核实收入
	private BigDecimal approvalApplyLimit;			// 申请金额
	private String approvalApplyTerm;				// 申请期限
	private BigDecimal approvalLimit;				// 审批额度
	private String approvalTerm;					// 审批期限
	private BigDecimal approvalMonthPay;			// 月还款额
	private BigDecimal approvalDebtTate;			// 内部负债率
	private BigDecimal approvalAllDebtRate;			// 总负债率
	private BigDecimal incomeCertificate;			// 收入证明金额
	private String creditRecord;					// 是否有信用记录
	private String approvalMemo;					// 备注
	private String assetType;// 前前审批保存勾选资产信息类型

	// 资产信息
	private String threeMonthsAddress;				// 网购达人贷 3个月内收货地址 00001 同地址 00002同司址 00003其他
	private String sixMonthsAddress;				// 网购达人贷 6个月内收货地址 00001 同地址 00002同司址 00003其他
	private String oneYearAddress;					// 网购达人贷 12个月内收货地址 00001 同地址 00002同司址 00003其他
	private String policyCheckIsVerify;				// 保单贷是否核实
	private String carCheckIsVerify;				// 车辆贷是否核实
	private String[] memo;							// 备注信息

	private Long version;							// 版本控制
	private String ip;								// UP
	private String markUpdateReportId;				// 标记是否修改央行报告绑定状态
}
