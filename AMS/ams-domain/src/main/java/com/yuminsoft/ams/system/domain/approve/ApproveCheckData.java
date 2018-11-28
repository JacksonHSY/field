package com.yuminsoft.ams.system.domain.approve;

import java.math.BigDecimal;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 资料核对表
 * @author fuhongxing
 */
public class ApproveCheckData extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public static final String TABLE_NAME = "AMS_APPROVE_CHECK_DATA";

    private String loanNo;//申请件编号

    private String name;//姓名

    private BigDecimal personalWater1;//个人流水1

    private BigDecimal personalWater2;//个人流水2

    private BigDecimal personalWater3;//个人流水3

    private BigDecimal personMonthAverage;//个人流水月均

    private BigDecimal personalWaterTotal;//个人流水合计

    private BigDecimal publicWater1;//对公流水1

    private BigDecimal publicWater2;//对公流水2

    private BigDecimal publicWater3;//对公流水3

    private BigDecimal publicMonthAverage;//对公流水月均

    private BigDecimal publicWaterTotal;//对公流水合计

    private BigDecimal waterIncomeTotal;//流水收入合计

    private Integer threeMonthsCount;//近三个月查询

    private Integer oneMonthsCount;//近一个月查询

    private String otherCheckMes;//其他核实信息
    
    private String creditCheckException; // 征信验证是否异常
    
    private String weekendPay; // 有无周末发薪

    private String memo;//备注


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
     * <p>个人流水1</p>
     */
    public BigDecimal getPersonalWater1() {
        return personalWater1;
    }

    /**
     * <p>个人流水1</p>
     */
    public void setPersonalWater1(BigDecimal personalWater1) {
        this.personalWater1 = personalWater1;
    }

    /**
     * <p>个人流水2</p>
     */
    public BigDecimal getPersonalWater2() {
        return personalWater2;
    }

    /**
     * <p>个人流水2</p>
     */
    public void setPersonalWater2(BigDecimal personalWater2) {
        this.personalWater2 = personalWater2;
    }

    /**
     * <p>个人流水3</p>
     */
    public BigDecimal getPersonalWater3() {
        return personalWater3;
    }

    /**
     * <p>个人流水3</p>
     */
    public void setPersonalWater3(BigDecimal personalWater3) {
        this.personalWater3 = personalWater3;
    }

    /**
     * <p>个人流水月均</p>
     */
    public BigDecimal getPersonMonthAverage() {
        return personMonthAverage;
    }

    /**
     * <p>个人流水月均</p>
     */
    public void setPersonMonthAverage(BigDecimal personMonthAverage) {
        this.personMonthAverage = personMonthAverage;
    }

    /**
     * <p>个人流水合计</p>
     */
    public BigDecimal getPersonalWaterTotal() {
        return personalWaterTotal;
    }

    /**
     * <p>个人流水合计</p>
     */
    public void setPersonalWaterTotal(BigDecimal personalWaterTotal) {
        this.personalWaterTotal = personalWaterTotal;
    }

    /**
     * <p>对公流水1</p>
     */
    public BigDecimal getPublicWater1() {
        return publicWater1;
    }

    /**
     * <p>对公流水1</p>
     */
    public void setPublicWater1(BigDecimal publicWater1) {
        this.publicWater1 = publicWater1;
    }

    /**
     * <p>对公流水2</p>
     */
    public BigDecimal getPublicWater2() {
        return publicWater2;
    }

    /**
     * <p>对公流水2</p>
     */
    public void setPublicWater2(BigDecimal publicWater2) {
        this.publicWater2 = publicWater2;
    }

    /**
     * <p>对公流水3</p>
     */
    public BigDecimal getPublicWater3() {
        return publicWater3;
    }

    /**
     * <p>对公流水3</p>
     */
    public void setPublicWater3(BigDecimal publicWater3) {
        this.publicWater3 = publicWater3;
    }

    /**
     * <p>对公流水月均</p>
     */
    public BigDecimal getPublicMonthAverage() {
        return publicMonthAverage;
    }

    /**
     * <p>对公流水月均</p>
     */
    public void setPublicMonthAverage(BigDecimal publicMonthAverage) {
        this.publicMonthAverage = publicMonthAverage;
    }

    /**
     * <p>对公流水合计</p>
     */
    public BigDecimal getPublicWaterTotal() {
        return publicWaterTotal;
    }

    /**
     * <p>对公流水合计</p>
     */
    public void setPublicWaterTotal(BigDecimal publicWaterTotal) {
        this.publicWaterTotal = publicWaterTotal;
    }

    /**
     * <p>流水收入合计</p>
     */
    public BigDecimal getWaterIncomeTotal() {
        return waterIncomeTotal;
    }

    /**
     * <p>流水收入合计</p>
     */
    public void setWaterIncomeTotal(BigDecimal waterIncomeTotal) {
        this.waterIncomeTotal = waterIncomeTotal;
    }

    /**
     * <p>近三个月查询</p>
     */
    public Integer getThreeMonthsCount() {
        return threeMonthsCount;
    }

    /**
     * <p>近三个月查询</p>
     */
    public void setThreeMonthsCount(Integer threeMonthsCount) {
        this.threeMonthsCount = threeMonthsCount;
    }

    /**
     * <p>近一个月查询</p>
     */
    public Integer getOneMonthsCount() {
        return oneMonthsCount;
    }

    /**
     * <p>近一个月查询</p>
     */
    public void setOneMonthsCount(Integer oneMonthsCount) {
        this.oneMonthsCount = oneMonthsCount;
    }

    /**
     * <p>其他核实信息</p>
     */
    public String getOtherCheckMes() {
        return otherCheckMes;
    }

    /**
     * <p>其他核实信息</p>
     */
    public void setOtherCheckMes(String otherCheckMes) {
        this.otherCheckMes = otherCheckMes;
    }

    /**
     * <p>征信验证是否异常</p>
     */
    public String getCreditCheckException() {
		return creditCheckException;
	}
    
    /**
     * <p>征信验证是否异常</p>
     */
	public void setCreditCheckException(String creditCheckException) {
		this.creditCheckException = creditCheckException;
	}

	
	/**
     * <p>有无周末发薪</p>
     */
	public String getWeekendPay() {
		return weekendPay;
	}

	/**
     * <p>有无周末发薪</p>
     */
	public void setWeekendPay(String weekendPay) {
		this.weekendPay = weekendPay;
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
   
}