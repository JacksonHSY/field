package com.yuminsoft.ams.system.domain;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆信息表
 * @author fuhongxing
 */
public class CarInfo extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9094464994667658833L;

//	public static final String TABLE_NAME = "AMS_CAR_INFO";

    private String loanNo;//申请件编号

    private String carTypr;//车辆类型

    private String carLoan;//是否有车贷

    private BigDecimal monthPaymentAmt;//月供
    
    private Date carBuyDate;//车辆购买时间

    private BigDecimal nakedCarPrice;//裸车价/万元

    private BigDecimal carBuyPrice;//购买价/万元

    private Date transferDate;//过户时间

    private Integer carLoanTerm;//贷款剩余期数

    private String operationStatus;//营运状况

    private String carSeat;//车辆座数

    private String localPlate;//本地车牌

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
     * <p>车辆类型</p>
     */
    public String getCarTypr() {
        return carTypr;
    }

    /**
     * <p>车辆类型</p>
     */
    public void setCarTypr(String carTypr) {
        this.carTypr = carTypr;
    }

    /**
     * <p>是否有车贷</p>
     */
    public String getCarLoan() {
        return carLoan;
    }

    /**
     * <p>是否有车贷</p>
     */
    public void setCarLoan(String carLoan) {
        this.carLoan = carLoan;
    }

    /**
     * <p>月供</p>
     */
    public BigDecimal getMonthPaymentAmt() {
        return monthPaymentAmt;
    }

    /**
     * <p>月供</p>
     */
    public void setMonthPaymentAmt(BigDecimal monthPaymentAmt) {
        this.monthPaymentAmt = monthPaymentAmt;
    }

    /**
     * <p>购买时间</p>
     * <p>车辆购买时间</p>
     */
    public Date getCarBuyDate() {
        return carBuyDate;
    }

    /**
     * <p>购买时间</p>
     * <p>车辆购买时间</p>
     */
    public void setCarBuyDate(Date carBuyDate) {
        this.carBuyDate = carBuyDate;
    }

    /**
     * <p>裸车价/万元</p>
     */
    public BigDecimal getNakedCarPrice() {
        return nakedCarPrice;
    }

    /**
     * <p>裸车价/万元</p>
     */
    public void setNakedCarPrice(BigDecimal nakedCarPrice) {
        this.nakedCarPrice = nakedCarPrice;
    }

    /**
     * <p>购买价/万元</p>
     */
    public BigDecimal getCarBuyPrice() {
        return carBuyPrice;
    }

    /**
     * <p>购买价/万元</p>
     */
    public void setCarBuyPrice(BigDecimal carBuyPrice) {
        this.carBuyPrice = carBuyPrice;
    }

    /**
     * <p>过户时间</p>
     */
    public Date getTransferDate() {
        return transferDate;
    }

    /**
     * <p>过户时间</p>
     */
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * <p>贷款剩余期数</p>
     */
    public Integer getCarLoanTerm() {
        return carLoanTerm;
    }

    /**
     * <p>贷款剩余期数</p>
     */
    public void setCarLoanTerm(Integer carLoanTerm) {
        this.carLoanTerm = carLoanTerm;
    }

    /**
     * <p>营运状况</p>
     */
    public String getOperationStatus() {
        return operationStatus;
    }

    /**
     * <p>营运状况</p>
     */
    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
     * <p>车辆座数</p>
     */
    public String getCarSeat() {
        return carSeat;
    }

    /**
     * <p>车辆座数</p>
     */
    public void setCarSeat(String carSeat) {
        this.carSeat = carSeat;
    }

    /**
     * <p>本地车牌</p>
     */
    public String getLocalPlate() {
        return localPlate;
    }

    /**
     * <p>本地车牌</p>
     */
    public void setLocalPlate(String localPlate) {
        this.localPlate = localPlate;
    }
   
}