package com.yuminsoft.ams.system.domain.system;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 支行信息
 * @author fuhongxing
 */
public class BankBranch extends AbstractEntity{

	/*public static final String TABLE_NAME = "AMS_BANK_BRANCH";*/
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3478777433156138523L;

	private String bankTypeId;//银行类型

    private String parentBankId;//所属银行编号

    private String bankName;//开户行

    private String areaCode;//地区编号

    private String cityName;//支行所在市/区

    private String provinceName;//支行所在省

    private String bankCode;//支行编码

    private String startDateTime;//开始时间

    private String endDateTime;//结束时间

    private String bankId;//银行支行编号

    private String bankType;//分支行类型

    /**
     * <p>银行类型</p>
     */
    public String getBankTypeId() {
        return bankTypeId;
    }

    /**
     * <p>银行类型</p>
     */
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    /**
     * <p>所属银行编号</p>
     */
    public String getParentBankId() {
        return parentBankId;
    }

    /**
     * <p>所属银行编号</p>
     */
    public void setParentBankId(String parentBankId) {
        this.parentBankId = parentBankId;
    }

    /**
     * <p>开户行</p>
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * <p>开户行</p>
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * <p>地区编号</p>
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * <p>地区编号</p>
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * <p>支行所在市/区</p>
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * <p>支行所在市/区</p>
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * <p>支行所在省</p>
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * <p>支行所在省</p>
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * <p>支行编码</p>
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * <p>支行编码</p>
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * <p>开始时间</p>
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * <p>开始时间</p>
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * <p>结束时间</p>
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * <p>结束时间</p>
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * <p>银行支行编号</p>
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * <p>银行支行编号</p>
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * <p>分支行类型</p>
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * <p>分支行类型</p>
     */
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
}