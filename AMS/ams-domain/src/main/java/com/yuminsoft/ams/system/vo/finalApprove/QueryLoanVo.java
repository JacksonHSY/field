package com.yuminsoft.ams.system.vo.finalApprove;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看借款对应类
 * @author Shipf
 */
public class QueryLoanVo implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 借款人
	 */
	public String loanName;
	/**
	 * 性别
	 */
	public String sex;
	
	/**
	 * 身份证号
	 */
	public String idNum;
	/**
	 * 借款类型
	 */
	public String loanType;
	/**
	 * 所属机构
	 */
	public String parentOrg;
	/**
	 * 借款方案
	 */
	public String loanplan;
	/**
	 * 申请金额
	 */
	public BigDecimal requestMoney;
	/**
	 * 申请期限
	 */
	public String requestTime;
	/**
	 * 申请日期
	 */
	public String requestDate;
	/**
	 * 审批金额
	 */
	public BigDecimal approvalMoney;
	/**
	 *借款期限
	 */
	public String loanTime;
	/**
	 * 可接受的最高月还款额
	 */
	public BigDecimal monthlyRepaymentLimit;
	/**
	 * 状态
	 */
	public String status;
	/**
	 * 月还款能力
	 */
	public BigDecimal monthlyRepaymentAbility;
	/**
	 * 用途
	 */
	public String use;
	/**
	 * 管理营业部
	 */
	public String managerBranch;
	/**
	 * 还款银行
	 */
	public String returnBank;
	/**
	 * 放款银行
	 */
	public String loanBank;
	/**
	 * 客户经理
	 */
	public String accountManager;
	/**
	 * 客服
	 */
	public String customService;
	/**
	 * 合同来源
	 */
	public String contractSource;
	/**
	 * 备注
	 */
	public String memo;
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
