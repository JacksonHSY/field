package com.yuminsoft.ams.system.domain;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 负债信息表
 * @author fuhongxing
 */
public class DebtsInfo extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3177788019263356569L;

//	public static final String TABLE_NAME = "AMS_DEBTS_INFO";

    private String loanNo;//申请件编号
    
    private String debtType;//负债类型(TOPUPLOAN:追加贷款;FASTLOAN:速贷;CREDITLOAN:信用贷)
    
    private String name;//姓名

    private BigDecimal creditTotalLimit;//信用卡总额度

    private BigDecimal creditUsedLimit;//信用卡已用额度

    private BigDecimal creditDebt;//信用卡负债

    private BigDecimal creditLoanLimit;//信用卡贷款额度

    private Integer creditLoanTerm;//信用卡贷款期限

    private BigDecimal creditLoanDebt;//信用卡贷款负债

    private Integer creditLoanAlsoPay;//信用卡贷款已还期数

    private BigDecimal outDebtTotal;//外部负债总额
    
    private BigDecimal outCreditDebtTotal;// 外部信用负债总额
    
    private BigDecimal monthlyTotalLiabilities;//月总负债

    private String memo;//备注
    private BigInteger creditNo;//对应征信贷款编号
    private String fastLoanSituation;// 速度还款情况-正常:NORMAL;逾期:OVERDUE;结清:SETTLE
    private String ifBlackRoster; // 是否是灰名单Y是N否


    public BigDecimal getMonthlyTotalLiabilities()
    {
        return monthlyTotalLiabilities;
    }

    public void setMonthlyTotalLiabilities(BigDecimal monthlyTotalLiabilities)
    {
        this.monthlyTotalLiabilities = monthlyTotalLiabilities;
    }

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
     * <p>负债类型</p>
     */
    public String getDebtType() {
		return debtType;
	}
    /**
     * <p>负债类型</p>
     */
	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}

	/**
     * <p>姓名</p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>姓名</p>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>信用卡总额度</p>
     */
    public BigDecimal getCreditTotalLimit() {
        return creditTotalLimit;
    }

    /**
     * <p>信用卡总额度</p>
     */
    public void setCreditTotalLimit(BigDecimal creditTotalLimit) {
        this.creditTotalLimit = creditTotalLimit;
    }

    /**
     * <p>信用卡已用额度</p>
     */
    public BigDecimal getCreditUsedLimit() {
        return creditUsedLimit;
    }

    /**
     * <p>信用卡已用额度</p>
     */
    public void setCreditUsedLimit(BigDecimal creditUsedLimit) {
        this.creditUsedLimit = creditUsedLimit;
    }

    /**
     * <p>信用卡负债</p>
     */
    public BigDecimal getCreditDebt() {
        return creditDebt;
    }

    /**
     * <p>信用卡负债</p>
     */
    public void setCreditDebt(BigDecimal creditDebt) {
        this.creditDebt = creditDebt;
    }

    /**
     * <p>信用卡贷款额度</p>
     */
    public BigDecimal getCreditLoanLimit() {
        return creditLoanLimit;
    }

    /**
     * <p>信用卡贷款额度</p>
     */
    public void setCreditLoanLimit(BigDecimal creditLoanLimit) {
        this.creditLoanLimit = creditLoanLimit;
    }

    /**
     * <p>信用卡贷款期限</p>
     */
    public Integer getCreditLoanTerm() {
        return creditLoanTerm;
    }

    /**
     * <p>信用卡贷款期限</p>
     */
    public void setCreditLoanTerm(Integer creditLoanTerm) {
        this.creditLoanTerm = creditLoanTerm;
    }

    /**
     * <p>信用卡贷款负债</p>
     */
    public BigDecimal getCreditLoanDebt() {
        return creditLoanDebt;
    }

    /**
     * <p>信用卡贷款负债</p>
     */
    public void setCreditLoanDebt(BigDecimal creditLoanDebt) {
        this.creditLoanDebt = creditLoanDebt;
    }

    /**
     * <p>信用卡贷款已还期数</p>
     */
    public Integer getCreditLoanAlsoPay() {
        return creditLoanAlsoPay;
    }

    /**
     * <p>信用卡贷款已还期数</p>
     */
    public void setCreditLoanAlsoPay(Integer creditLoanAlsoPay) {
        this.creditLoanAlsoPay = creditLoanAlsoPay;
    }

    /**
     * <p>外部负债总额</p>
     */
    public BigDecimal getOutDebtTotal() {
        return outDebtTotal;
    }

    /**
     * <p>外部负债总额</p>
     */
    public void setOutDebtTotal(BigDecimal outDebtTotal) {
        this.outDebtTotal = outDebtTotal;
    }

    /**
     * <p>外部信用负债总额</p>
     */
    public BigDecimal getOutCreditDebtTotal() {
		return outCreditDebtTotal;
	}

    /**
     * <p>外部信用负债总额</p>
     */
	public void setOutCreditDebtTotal(BigDecimal outCreditDebtTotal) {
		this.outCreditDebtTotal = outCreditDebtTotal;
	}

	/**
     * <p>备注</p>
     */
    public String getMemo() {
        return memo;
    }

    /**
     * <p>备注</p>
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    /**
     * <p>对应征信贷款编号</p>
     */
	public BigInteger getCreditNo() {
		return creditNo;
	}
	/**
     * <p>对应征信贷款编号</p>
     */
	public void setCreditNo(BigInteger creditNo) {
		this.creditNo = creditNo;
	}

	/**
     * <p>速度还款情况</p>
     */
	public String getFastLoanSituation() {
		return fastLoanSituation;
	}

	/**
     * <p>速度还款情况</p>
     */
	public void setFastLoanSituation(String fastLoanSituation) {
		this.fastLoanSituation = fastLoanSituation;
	}

    public String getIfBlackRoster() {
        return ifBlackRoster;
    }

    public void setIfBlackRoster(String ifBlackRoster) {
        this.ifBlackRoster = ifBlackRoster;
    }
}