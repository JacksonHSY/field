package com.yuminsoft.ams.system.domain;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 人行征信信息表
 * @author fuhongxing
 */
public class Credit extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


//	public static final String TABLE_NAME = "AMS_CREDIT";

    private String loanNo;//申请件编号

    private String creditPhone;//征信手机号

    private String creditCorpPhone;//征信单位电话

    private String creditHomePhone;//征信住宅电话

    private Integer houseLoanNum;//住房贷款笔数

    private Integer ortherLoanNum;//其他贷款笔数

    private Date loanFirstMonth;//首笔贷款发放月份

    private Integer creditAccounts;//贷记卡账户数

    private Date creditFirstMonth;//首张贷记卡发放月份

    private Integer semiCreditAccounts;//准贷记卡账户数

    private Date semiCreditFirstMonth;//首张准贷记卡发卡月份

    private String selfStateBooklist;//本人声明书目

    private Integer dissentLableNum;//异议标注数目

    private Integer loanOverCount;//笔数

    private Integer loanOverMonth;//月份数

    private BigDecimal loanOverLtotal;//单月最高逾期总额

    private Integer loanOverLmonths;//最长逾期月数

    private Integer creditOverAccounts;//逾期账户数

    private Integer creditOverMonths;//逾期月份数

    private BigDecimal creditOverLtotal;//单月最高逾期总额

    private Integer creditOverLmonth;//最长逾期月数

    private Integer loaningCorpOrg;//贷款法人机构数

    private Integer loaningOrgNums;//贷款机构数

    private Integer loaningNum;//笔数

    private BigDecimal loaningContractTotal;//合同总额

    private BigDecimal loaningRemainSum;//余额

    private BigDecimal loaningMeanPay;//最近6个月平均应还款

    private Integer existCorpOrg;//发卡法人机构数

    private Integer existOrgNum;//发卡机构数

    private Integer existAccount;//账户数

    private BigDecimal existCreditTotal;//授信总额

    private BigDecimal existMaxAmount;//单家银行最高授信额

    private BigDecimal existUsedAmount;//已用额度

    private BigDecimal existMeanAmount;//最近6个月平均使用额度

    private Integer ensureCount;//担保笔数

    private BigDecimal ensureAmount;//担保金额

    private BigDecimal ensureRemainder;//担保本金余额

    private String isneedwritecredittips;//是否需要填写信息

    private String isneedwriteloanover;//是否需要填写信息

    private String isneedwritecreditover;//是否需要填写信息

    private String isneedwriteloanoverinfo;//是否需要填写信息

    private String isneedwriteensuremes;//是否需要填写信息

    private String isneedwriteexistcredit;//是否需要填写信息

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
     * <p>征信手机号</p>
     */
    public String getCreditPhone() {
        return creditPhone;
    }

    /**
     * <p>征信手机号</p>
     */
    public void setCreditPhone(String creditPhone) {
        this.creditPhone = creditPhone;
    }

    /**
     * <p>征信单位电话</p>
     */
    public String getCreditCorpPhone() {
        return creditCorpPhone;
    }

    /**
     * <p>征信单位电话</p>
     */
    public void setCreditCorpPhone(String creditCorpPhone) {
        this.creditCorpPhone = creditCorpPhone;
    }

    /**
     * <p>征信住宅电话</p>
     */
    public String getCreditHomePhone() {
        return creditHomePhone;
    }

    /**
     * <p>征信住宅电话</p>
     */
    public void setCreditHomePhone(String creditHomePhone) {
        this.creditHomePhone = creditHomePhone;
    }

    /**
     * <p>住房贷款笔数</p>
     */
    public Integer getHouseLoanNum() {
        return houseLoanNum;
    }

    /**
     * <p>住房贷款笔数</p>
     */
    public void setHouseLoanNum(Integer houseLoanNum) {
        this.houseLoanNum = houseLoanNum;
    }

    /**
     * <p>其他贷款笔数</p>
     */
    public Integer getOrtherLoanNum() {
        return ortherLoanNum;
    }

    /**
     * <p>其他贷款笔数</p>
     */
    public void setOrtherLoanNum(Integer ortherLoanNum) {
        this.ortherLoanNum = ortherLoanNum;
    }

    /**
     * <p>首笔贷款发放月份</p>
     */
    public Date getLoanFirstMonth() {
        return loanFirstMonth;
    }

    /**
     * <p>首笔贷款发放月份</p>
     */
    public void setLoanFirstMonth(Date loanFirstMonth) {
        this.loanFirstMonth = loanFirstMonth;
    }

    /**
     * <p>贷记卡账户数</p>
     */
    public Integer getCreditAccounts() {
        return creditAccounts;
    }

    /**
     * <p>贷记卡账户数</p>
     */
    public void setCreditAccounts(Integer creditAccounts) {
        this.creditAccounts = creditAccounts;
    }

    /**
     * <p>首张贷记卡发放月份</p>
     */
    public Date getCreditFirstMonth() {
        return creditFirstMonth;
    }

    /**
     * <p>首张贷记卡发放月份</p>
     */
    public void setCreditFirstMonth(Date creditFirstMonth) {
        this.creditFirstMonth = creditFirstMonth;
    }

    /**
     * <p>准贷记卡账户数</p>
     */
    public Integer getSemiCreditAccounts() {
        return semiCreditAccounts;
    }

    /**
     * <p>准贷记卡账户数</p>
     */
    public void setSemiCreditAccounts(Integer semiCreditAccounts) {
        this.semiCreditAccounts = semiCreditAccounts;
    }

    /**
     * <p>首张准贷记卡发卡月份</p>
     */
    public Date getSemiCreditFirstMonth() {
        return semiCreditFirstMonth;
    }

    /**
     * <p>首张准贷记卡发卡月份</p>
     */
    public void setSemiCreditFirstMonth(Date semiCreditFirstMonth) {
        this.semiCreditFirstMonth = semiCreditFirstMonth;
    }

    /**
     * <p>本人声明书目</p>
     */
    public String getSelfStateBooklist() {
        return selfStateBooklist;
    }

    /**
     * <p>本人声明书目</p>
     */
    public void setSelfStateBooklist(String selfStateBooklist) {
        this.selfStateBooklist = selfStateBooklist;
    }

    /**
     * <p>异议标注数目</p>
     */
    public Integer getDissentLableNum() {
        return dissentLableNum;
    }

    /**
     * <p>异议标注数目</p>
     */
    public void setDissentLableNum(Integer dissentLableNum) {
        this.dissentLableNum = dissentLableNum;
    }

    /**
     * <p>笔数</p>
     */
    public Integer getLoanOverCount() {
        return loanOverCount;
    }

    /**
     * <p>笔数</p>
     */
    public void setLoanOverCount(Integer loanOverCount) {
        this.loanOverCount = loanOverCount;
    }

    /**
     * <p>月份数</p>
     */
    public Integer getLoanOverMonth() {
        return loanOverMonth;
    }

    /**
     * <p>月份数</p>
     */
    public void setLoanOverMonth(Integer loanOverMonth) {
        this.loanOverMonth = loanOverMonth;
    }

    /**
     * <p>单月最高逾期总额</p>
     */
    public BigDecimal getLoanOverLtotal() {
        return loanOverLtotal;
    }

    /**
     * <p>单月最高逾期总额</p>
     */
    public void setLoanOverLtotal(BigDecimal loanOverLtotal) {
        this.loanOverLtotal = loanOverLtotal;
    }

    /**
     * <p>最长逾期月数</p>
     */
    public Integer getLoanOverLmonths() {
        return loanOverLmonths;
    }

    /**
     * <p>最长逾期月数</p>
     */
    public void setLoanOverLmonths(Integer loanOverLmonths) {
        this.loanOverLmonths = loanOverLmonths;
    }

    /**
     * <p>账户数</p>
     */
    public Integer getCreditOverAccounts() {
        return creditOverAccounts;
    }

    /**
     * <p>账户数</p>
     */
    public void setCreditOverAccounts(Integer creditOverAccounts) {
        this.creditOverAccounts = creditOverAccounts;
    }

    /**
     * <p>月份数</p>
     */
    public Integer getCreditOverMonths() {
        return creditOverMonths;
    }

    /**
     * <p>月份数</p>
     */
    public void setCreditOverMonths(Integer creditOverMonths) {
        this.creditOverMonths = creditOverMonths;
    }

    /**
     * <p>单月最高逾期总额</p>
     */
    public BigDecimal getCreditOverLtotal() {
        return creditOverLtotal;
    }

    /**
     * <p>单月最高逾期总额</p>
     */
    public void setCreditOverLtotal(BigDecimal creditOverLtotal) {
        this.creditOverLtotal = creditOverLtotal;
    }

    /**
     * <p>最长逾期月数</p>
     */
    public Integer getCreditOverLmonth() {
        return creditOverLmonth;
    }

    /**
     * <p>最长逾期月数</p>
     */
    public void setCreditOverLmonth(Integer creditOverLmonth) {
        this.creditOverLmonth = creditOverLmonth;
    }

    /**
     * <p>贷款法人机构数</p>
     */
    public Integer getLoaningCorpOrg() {
        return loaningCorpOrg;
    }

    /**
     * <p>贷款法人机构数</p>
     */
    public void setLoaningCorpOrg(Integer loaningCorpOrg) {
        this.loaningCorpOrg = loaningCorpOrg;
    }

    /**
     * <p>贷款机构数</p>
     */
    public Integer getLoaningOrgNums() {
        return loaningOrgNums;
    }

    /**
     * <p>贷款机构数</p>
     */
    public void setLoaningOrgNums(Integer loaningOrgNums) {
        this.loaningOrgNums = loaningOrgNums;
    }

    /**
     * <p>笔数</p>
     */
    public Integer getLoaningNum() {
        return loaningNum;
    }

    /**
     * <p>笔数</p>
     */
    public void setLoaningNum(Integer loaningNum) {
        this.loaningNum = loaningNum;
    }

    /**
     * <p>合同总额</p>
     */
    public BigDecimal getLoaningContractTotal() {
        return loaningContractTotal;
    }

    /**
     * <p>合同总额</p>
     */
    public void setLoaningContractTotal(BigDecimal loaningContractTotal) {
        this.loaningContractTotal = loaningContractTotal;
    }

    /**
     * <p>余额</p>
     */
    public BigDecimal getLoaningRemainSum() {
        return loaningRemainSum;
    }

    /**
     * <p>余额</p>
     */
    public void setLoaningRemainSum(BigDecimal loaningRemainSum) {
        this.loaningRemainSum = loaningRemainSum;
    }

    /**
     * <p>最近6个月平均应还款</p>
     */
    public BigDecimal getLoaningMeanPay() {
        return loaningMeanPay;
    }

    /**
     * <p>最近6个月平均应还款</p>
     */
    public void setLoaningMeanPay(BigDecimal loaningMeanPay) {
        this.loaningMeanPay = loaningMeanPay;
    }

    /**
     * <p>发卡法人机构数</p>
     */
    public Integer getExistCorpOrg() {
        return existCorpOrg;
    }

    /**
     * <p>发卡法人机构数</p>
     */
    public void setExistCorpOrg(Integer existCorpOrg) {
        this.existCorpOrg = existCorpOrg;
    }

    /**
     * <p>发卡机构数</p>
     */
    public Integer getExistOrgNum() {
        return existOrgNum;
    }

    /**
     * <p>发卡机构数</p>
     */
    public void setExistOrgNum(Integer existOrgNum) {
        this.existOrgNum = existOrgNum;
    }

    /**
     * <p>账户数</p>
     */
    public Integer getExistAccount() {
        return existAccount;
    }

    /**
     * <p>账户数</p>
     */
    public void setExistAccount(Integer existAccount) {
        this.existAccount = existAccount;
    }

    /**
     * <p>授信总额</p>
     */
    public BigDecimal getExistCreditTotal() {
        return existCreditTotal;
    }

    /**
     * <p>授信总额</p>
     */
    public void setExistCreditTotal(BigDecimal existCreditTotal) {
        this.existCreditTotal = existCreditTotal;
    }

    /**
     * <p>单家行最高授信额</p>
     */
    public BigDecimal getExistMaxAmount() {
        return existMaxAmount;
    }

    /**
     * <p>单家行最高授信额</p>
     */
    public void setExistMaxAmount(BigDecimal existMaxAmount) {
        this.existMaxAmount = existMaxAmount;
    }

    /**
     * <p>已用额度</p>
     */
    public BigDecimal getExistUsedAmount() {
        return existUsedAmount;
    }

    /**
     * <p>已用额度</p>
     */
    public void setExistUsedAmount(BigDecimal existUsedAmount) {
        this.existUsedAmount = existUsedAmount;
    }

    /**
     * <p>最近6个月平均使用额度</p>
     */
    public BigDecimal getExistMeanAmount() {
        return existMeanAmount;
    }

    /**
     * <p>最近6个月平均使用额度</p>
     */
    public void setExistMeanAmount(BigDecimal existMeanAmount) {
        this.existMeanAmount = existMeanAmount;
    }

    /**
     * <p>担保笔数</p>
     */
    public Integer getEnsureCount() {
        return ensureCount;
    }

    /**
     * <p>担保笔数</p>
     */
    public void setEnsureCount(Integer ensureCount) {
        this.ensureCount = ensureCount;
    }

    /**
     * <p>担保金额</p>
     */
    public BigDecimal getEnsureAmount() {
        return ensureAmount;
    }

    /**
     * <p>担保金额</p>
     */
    public void setEnsureAmount(BigDecimal ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    /**
     * <p>担保本金余额</p>
     */
    public BigDecimal getEnsureRemainder() {
        return ensureRemainder;
    }

    /**
     * <p>担保本金余额</p>
     */
    public void setEnsureRemainder(BigDecimal ensureRemainder) {
        this.ensureRemainder = ensureRemainder;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwritecredittips() {
        return isneedwritecredittips;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwritecredittips(String isneedwritecredittips) {
        this.isneedwritecredittips = isneedwritecredittips;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwriteloanover() {
        return isneedwriteloanover;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwriteloanover(String isneedwriteloanover) {
        this.isneedwriteloanover = isneedwriteloanover;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwritecreditover() {
        return isneedwritecreditover;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwritecreditover(String isneedwritecreditover) {
        this.isneedwritecreditover = isneedwritecreditover;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwriteloanoverinfo() {
        return isneedwriteloanoverinfo;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwriteloanoverinfo(String isneedwriteloanoverinfo) {
        this.isneedwriteloanoverinfo = isneedwriteloanoverinfo;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwriteensuremes() {
        return isneedwriteensuremes;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwriteensuremes(String isneedwriteensuremes) {
        this.isneedwriteensuremes = isneedwriteensuremes;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public String getIsneedwriteexistcredit() {
        return isneedwriteexistcredit;
    }

    /**
     * <p>是否需要填写信息</p>
     */
    public void setIsneedwriteexistcredit(String isneedwriteexistcredit) {
        this.isneedwriteexistcredit = isneedwriteexistcredit;
    }

}