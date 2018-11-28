package com.yuminsoft.ams.system.domain.system;

import com.yuminsoft.ams.system.domain.AbstractEntity;
/**
 * 银行管理
 * @author fuhongxing
 */
public class Banks extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572174838115711644L;
	
	
	private String code;//银行代码
	private String bankName; //银行名称
	private String bankType;//类型(0证大投资，1非证大投资)
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	
}
